package slave.classify;


import java.util.Enumeration;
import java.util.LinkedList;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import global.pluginStructure.*;
import global.pluginStructure.types.LocationType;
import global.Fields;
import global.XMLPluginParser;
import global.Filter;
import global.Utils;

import net.sourceforge.jpcap.net.IPPacket;
import net.sourceforge.jpcap.net.Packet;
import slave.PacketBuffer;
import slave.Slave;

/**
 * Ein Classifier liest Pakete aus einem PacketBuffer und klassifiziert sie nach Protokollen (SIP, RTP, ...).
 * Anschließend überprüft er, ob die in dem Plugin definierten Nachrichten (analyze-Teil) auf das aktuelle
 * Paket zutreffen und versendet ggf. diese Nachrichten über die DataConnection.
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 * 
 * @version	02.06.2004
 */
public class Classifier extends Thread {
	
	private static Fields fields = new Fields();
	
	// hieraus werden die Pakete gelesen
	private PacketBuffer buffer;

	// hierüber wird an den Server versendet
    private slave.connection.DataConnection connection;

    // über dieses Objekt werden die Plugins eingelesen
	private XMLPluginParser plugins;
    private LinkedList protocols;

	// sobald diese Variable durch die Methode end() auf true gesetzt wird, wird das Klassifizieren beendet.
	private boolean end = false;

    // der vom Server übertragene Filter
    private Filter filter;

    // in diesem Objekt werden die erkannten Streams verwaltet
    private StreamManager streamManager;
    
    // das Geraet, von dem gelesen wird
    private NetworkInterface networkInterface;
    
	// Gibt an, ob dieser Slave auch Multicast-Nachrichten akzeptieren und an den Master weiterleiten soll
	// HINWEIS: es darf nur 1 Multicast-Slave geben!
    private boolean multicast;
    
    // Gibt an, ab welchem Bit im Datenteil des Paketes die Methode checkCondition das Paket untersuchen.
    // Durch die Moeglichkeit, "multipleObjects" angeben zu koennen, wird die checkCondition-Methode fuer eine
    // Condition mehrmals aufgerufen, allerdings soll sie dann an verschiedenen Stellen des Datenteils pruefen.
    private int locationOffset = 0;

	private Slave slave;


	public Classifier(PacketBuffer buffer, slave.connection.DataConnection connection, Filter filter, String device, boolean multicast, Slave slave){
		this.filter = filter;
        this.buffer = buffer;
        this.connection = connection;
        this.multicast = multicast;
        this.slave = slave;
        this.plugins = new XMLPluginParser();
        this.streamManager = new StreamManager(this.connection, this.slave);
        //this.streamManager.start();
        
		try {
			this.networkInterface = NetworkInterface.getByName(device);
		} catch (SocketException e) {
			System.out.println("fatal error in client.classify.Classifier.setDevice: SocketException while getting IP addresses from given device.");
			// TODO und jetzt?			
		}
	}
	
		
	/** wird automatisch ausgeführt, wenn der Thread gestartet wird
	 * 
	 */
	public void run(){
        // die Plugins aus dem XMLProtocolParser holen
        this.protocols = this.plugins.getPlugins();

        // hierdrin wird der Name des Protokolls gespeichert, wenn eine Klassifizierung
        // erfolgreich war.
        Plugin plugin = null;		

		// Endlosschleife
		while(!this.end){
			
			//* DEBUG */ System.out.println(this.buffer.getElements() + " Pakete im Puffer.");
			
			// Paket aus Puffer lesen
			Packet packet = this.buffer.read();
			
			//* DEBUG */ System.out.println("Classifier: Paket aus Puffer gelesen.");			
			
			// wir behandeln hier nur IP-Pakete. Der Rest ist uninteressant.
			if (packet instanceof IPPacket) {
				//* DEBUG */ System.out.println("Classifier: Paket ist IP-Paket");				
				//* DEBUG */ System.out.println("Classifier: EthernetHeader-Length: " + ((EthernetPacket) packet).getEthernetHeaderLength());
				//* DEBUG */ System.out.println("Classifier: EthernetData-Length: " + ((EthernetPacket) packet).getEthernetData().length);            
            	//* DEBUG */ System.out.println("Classifier: IP header length " + ((IPPacket) packet).getIPHeaderLength());
				//* DEBUG */ System.out.println("Classifier: IP data length "+ ((IPPacket) packet).getIPData().length);
				//* DEBUG */ System.out.println("Classifier: IP destination address "+ ((IPPacket) packet).getDestinationAddress());
				//* DEBUG */ System.out.println("Classifier: IP source address "+ ((IPPacket) packet).getSourceAddress());				
            
	            // Paket klassifizieren
	            plugin = this.classify(packet);
	            
	            /* DEBUG */ if (plugin != null) {
	            	//* DEBUG */ System.out.println("Paket klassifiziert. Protokoll ist: " + plugin.getName());
	            	//* DEBUG */ System.out.println("IP Header Length: " + ((IPPacket) packet).getHeaderLength() );
					//* DEBUG */ System.out.println("Length: " + ((IPPacket) packet).getLength() );
					//* DEBUG */ System.out.println("next header: " + ((IPPacket) packet).getProtocol() );
	            }
	            
				// von Vitamin verursachte Datenpakete muessen herausgefiltert werden.		
	            // es existiert standardmaessig zwei Plugins "vitaminS" und "vitaminD", die
	            // das Signalisierungs- sowie das Datenprotokoll von Vitamin beschreiben.
	            // Pakete, die diesen Protokollen zugehoerig klassifiziert wurden, werden nicht weiter behandelt.
	            if (plugin != null) {
		            if (!plugin.getName().equals("vitaminS") && !plugin.getName().equals("vitaminD")) {	            				
			            // Paket ist jetzt klassifiziert --> jetzt nach Protokollen filtern
			            if (this.filter.isProtocolFiltered(plugin.getName())) {
			                // das Paket wird weiterhin behandelt
			                // Analyse durchführen und ggf. Messages an den Server versenden (über this.connection)
							//* DEBUG */ System.out.println("Paket wird in den Analyseteil gegeben.");
			                this.analyze(packet, plugin);
							//* DEBUG */ System.out.println("Analyse abgeschlossen.");
			            }           
					}
	            }
	            else {
	            	//* DEBUG */ System.out.println("Paket konnte nicht klassifiziert werden und wird verworfen.");
	            }
			}
		}
	}


    /** ordnet ein IP-Paket einem Protokoll zu. Liefert das Protokoll zurück,
     * dem das Paket zugeordnet werden konnte.
     *
     * @param packet das Paket, welches untersucht werden soll
     * @return das Protokoll, dem das Paket zugeordnet wurde
     */
    private Plugin classify(Packet packet) {
        Plugin plugin = null;

        // über die Protokolle iterieren und prüfen
        Condition [] conditions;
        for (int i = 0; i < this.protocols.size(); i++) {
        	        	
           // es wird nur auf dieses Protokoll geprueft, wenn es auch im Filter steht
           if (this.filter.isProtocolFiltered( ((Plugin) protocols.get(i)).getName() )) {
				//* DEBUG */ System.out.println("Classifier.classify: pruefe Protokoll " + ((Plugin) this.protocols.get(i)).getName());
				
				// pruefen, ob diese Bedingungen zutreffen
				if (this.checkConditionGroup( ((Plugin) this.protocols.get(i)).getClassify().getConditionGroup(), packet, 0)) {		// 0 ist der offset
	               plugin = (Plugin) this.protocols.get(i);
	
	               // Schleife abbrechen; d.h. es werden keine weiteren Protokolle mehr geprüft
	               break;
	           }
           }
       }	   

       return plugin;
    }
    
    
    private boolean checkConditionGroup(ConditionGroup conditionGroup, Packet packet, int offset) {
		boolean isTrue = false;
		
		// wenn keine Bedingungen in der conditionGroup liegen, sind diese nicht
		// vorhandenen Bedingungen natuerlich erfuellt.
		if (conditionGroup == null)
			return true;			
		
		// immer wenn eine Bedingung zutrifft, wird dieser Counter um 1
		// hochgezählt. Ist er nachher gleich der Anzahl der Felder plus der
		// Anzahl der Choices, dann ist das Paket als Paket dieses
		// Protokolls klasifiziert
		int fieldOkCounter = 0;
		
		// conditions pruefen, wenn es welche gibt
		if (conditionGroup.getConditionCount() > 0) {
			// die Klassifizierungsfelder aus dem Protokoll auslesen
			Condition [] conditions = conditionGroup.getCondition();
		
			for (int j = 0; j < conditions.length; j++) {
				// Bedingung dieses Feldes überprüfen
				if (this.checkCondition(conditions[j], packet, offset))
					fieldOkCounter++;
				else
					 // wenn ein Feld falsch ist, muss der Rest nich mehr geprueft werden
					 j = conditions.length;
			}
		}
	
		// choices pruefen, wenn es welche gibt
		if (conditionGroup.getChoiceCount() > 0) {		
			// TODO "Choice" kann z.Zt. nur auf oberster Ebene benutzt werden
			
			// auch die choices muessen noch geprueft werden
			// in jedem Choice-Element muss mindestens eine Bedingung zutreffen         
			Choice [] choices;
				   
			// die Choices aus dem Protokoll auslesen
			choices = conditionGroup.getChoice();
				   		
			// ueber alle Choices iterieren und pruefen
			for (int j = 0; j < choices.length; j++) {
				 Condition [] choiceConditions = choices[j].getConditionGroup().getCondition();
				   		
				 int choiceCounter = 0;
				   		
				 // pruefen, ob mindestens eine Bedingung (field) zutrifft
				 for (int k = 0; k < choiceConditions.length; k++) {
					 if (this.checkCondition(choiceConditions[k], packet, offset))
						 choiceCounter++;
				 }
				   		
				 if (choiceCounter > 0)
					 fieldOkCounter++;
			}
		}
			   
		//* DEBUG */ System.out.println("erfuellte Felder: " + fieldOkCounter);
	
		// wenn jetzt der fieldOkCounter gleich der Anzahl der Bedingungen ist,
		// ist das Paket als zu diesem Protokoll zugehörig klassifiziert
		if (fieldOkCounter == (conditionGroup.getConditionCount() + conditionGroup.getChoiceCount()))
			isTrue = true;
			
		return isTrue;
    }


    /** prüft die Bedingung eines Feldes im Klassifizierungsvorgang.
     * Liefert true wenn die Bedingung zutrifft und false wenn sie nicht
     * zutrifft.
     * Diese Methode wird rekursiv geschachtelt, falls das Feld Bedingung
     * für weitere Felder ist (also ein Condition-Element) enthält.
     *
     * @param field
     * @param packet
     * @return
     */
    private boolean checkCondition(Condition condition, Packet packet, int offset) {
		boolean isOk = false;
		
		// binaere Angabe
		if (condition.getBinaryPointerWithValue() != null) {
			BinaryPointerWithValue pointer = condition.getBinaryPointerWithValue();
			
			//* DEBUG */ System.out.println("Name der Condition: " + pointer.getName());
	
	        // Bedingung prüfen
	
	        // "location" im field prüfen und entsprechendes Teil des Paketes in byte-Array ablegen
	        LocationType location = pointer.getLocation();
	        byte [] array = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, location.getType());			
	        
	        // Wenn das array die Laenge 0 hat, gibt es diesen Teil des Paketes nicht (z.B. TCP bei einem UDPPacket).
			// Auch kann nach dem IPv6-Header verlangt worden sein, obwohl es ein IPv4-Paket ist und andersrum.
			// Ebenso kann es passieren, dass die Daten abgefragt wurden und das Transportprotokoll ist nicht TCP
			// oder UDP (mehr unterstuetzt die Software z.Zt. nicht).
	        // Das Feld ist dann logischerweise nicht erfuellt.
	        //
	        // Die zweite Bedingung schliesst aus, das leere Felder untersucht werden. Dies trat beim Testen bei einigen
	        // TCP-Paketen auf, die keine Daten enthielten.
	        if (!(array == null)) {
				// den offset abschneiden
				//* DEBUG */ System.out.println("Das Array hat die Laenge " + array.length + ", offset ist " + offset);
				array = Utils.getBitIndicatedPartOfByteArray(array, offset, array.length * 8 - offset);
				//* DEBUG */ System.out.println("Das Array hat die Laenge " + array.length);
	        	
				if (array.length > 0)
				{
		        // Angabe ueber Namen oder Start/Length ?	        	        
		        int start;
		        int length;
		        if (pointer.hasStart() && pointer.hasLength()) {
					//* DEBUG */ System.out.println("Angabe ueber start und length.");
		        	start = pointer.getStart();
		        	length = pointer.getLength();
		        }
		        else 
		        {
		        	// Angabe ueber Namen
					//* DEBUG */ System.out.println("Angabe ueber Namen: " + pointer.getName());
					//* DEBUG */ System.out.println("pointer.getLocation().getType(): " + pointer.getLocation().getType() + ",LocationType.IP_TYPE " + LocationType.IP_TYPE + ",LocationType.IPV6_TYPE " + LocationType.IPV6_TYPE);
					
					// "next header" kann wg. Erweiterungsheadern an verschiedenen Stellen liegen. Daher Sonderbehandlung
					if ((pointer.getLocation().getType() == LocationType.IP_TYPE || pointer.getLocation().getType() == LocationType.IPV6_TYPE) &&  ((IPPacket) packet).getVersion() == 6 && pointer.getName().equals(("next header"))) {
						//* DEBUG */ System.out.println("Classifier: Sonderbehandlung fuer name=\"next header\"");						
						if (Integer.parseInt(condition.getBinaryPointerWithValue().getValue()) == ((IPPacket) packet).getNextHeader())						
							return true;
						else
							return false;
					}
					
			        // wenn die Location-Angabe "ip" ist, muss erstmal die Version herausgefunden werden
			        if ((pointer.getLocation().getType() == LocationType.IP_TYPE || pointer.getLocation().getType() == LocationType.IPV6_TYPE) &&  ((IPPacket) packet).getVersion() == 6) {
						//* DEBUG */ System.out.println("name = " + pointer.getName() + ", IP version = " + ((IPPacket) packet).getVersion());
						start = fields.getStartFromName(pointer.getName(), LocationType.IPV6);
						length = fields.getLengthFromName(pointer.getName(), LocationType.IPV6);        		
			        }
			        else if ((pointer.getLocation().getType() == LocationType.IP_TYPE || pointer.getLocation().getType() == LocationType.IPV4_TYPE) &&  ((IPPacket) packet).getVersion() == 4) {
						//* DEBUG */ System.out.println("IPv4");						
						start = fields.getStartFromName(pointer.getName(), LocationType.IPV4);
						length = fields.getLengthFromName(pointer.getName(), LocationType.IPV4);
			        }
			        else {
						//* DEBUG */ System.out.println("nicht im IP Header sondern woanders.");
						start = fields.getStartFromName(pointer.getName(), pointer.getLocation());
						length = fields.getLengthFromName(pointer.getName(), pointer.getLocation());
			        }
		        }
		        
		        //* DEBUG */ System.out.println("start = " + start + ", length = " + length + ", array.length = " + array.length + ", location = " + field.getLocation().toString());
	
				// wenn start == -1 ist, war der Name unbekannt
				if (start != -1) {
			        // wichtigen Teil aus byte-Array extrahieren (von "start" angefangen die nächsten "length" Bytes)
			        //* DEBUG */ System.out.println("Classifier.checkField: array hat die Laenge: " + array.length);
			       	//* DEBUG */ System.out.println("Classifier.checkField: vor dem Extrahieren des wichtigen Teils. start = " + start + ", length = " + length);
			       	array = Utils.getBitIndicatedPartOfByteArray(array, start, length);
						
					//* DEBUG */ System.out.println("Ergebnis getBitIndicatedPartOfByteArray: " + Utils.byteToInt(array));
					
			        
			        // prüfen, ob der entsprechende Wert ("value") drinsteht und ggf. isOk auf true setzen
			        isOk = PluginStructureUtils.checkBinaryField(array, 0, array.length, condition.getBinaryPointerWithValue().getValue(), 0); // der offset wurde bereits vorher abgeschnitten
				    
				    		
			        // ggf. weitere Felder im Conditionselement überprüfen
			        // (wird nur geprüft, wenn die Bedingung im oberen Feld erfüllt ist)
			        if (condition.getConditionFor() != null && isOk) {
			            // wenn diese Stelle erreicht wird, ist isOk = true, da die obere
			            // Bedingung erfüllt ist.
			            // isOk bleibt aber nur true, wenn auch die in dem Conditions-Objekt
			            // liegenden Bedingungen erfüllt sind
				
			            // die in der Condition enthaltenen Felder extrahieren
			            Condition [] conditions = condition.getConditionFor().getConditionGroup().getCondition();
				
			            // hierdrin wird das Ergebnis der Prüfung der Bedingungen in den gerade
			            // extrahierten Feldern gespeichert
			            boolean [] results = new boolean  [conditions.length];
			
			            // über alle Felder iterieren und durch rekursiven Aufruf der checkField-Methode
			            // die Bedingungen prüfen
			            for (int i = 0; i < conditions.length; i++) {
			                results [i] = checkCondition(conditions[i], packet, offset);
			            }
			
			            // wenn eine der Bedingungen nicht zutrifft, muss isOk = false sein
			            for (int i = 0; i < conditions.length; i++) {
			                isOk = isOk && results[i];
			            }
			    	}
				}
			}
		}
		}
		// textueller Pointer
		else if (condition.getTextualPointerWithValue() != null) {
			TextualPointerWithValue texPointer = condition.getTextualPointerWithValue();
			
			//* DEBUG */ System.out.println("Name der Condition: " + texPointer.getName());
				
			byte [] garray = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, texPointer.getLocation().getType());						
			if (!(texPointer.getRow() == null) && !(texPointer.getWord() == null)) {				
				// "start" und "length" existieren --> einfach diese Stelle pruefen
						
				isOk = PluginStructureUtils.checkTextualField(garray, texPointer.getRow(), texPointer.getWord(), texPointer.getValue(), texPointer.getAdditionalSeperator(), offset);
			}
		}
		else {
			System.out.println("error in client.classify.PluginStructureUtils.checkIdentifier: identifier-object does not include binaryPointerWithValue or textualPointerWithValue.");
		}
        
		//* DEBUG */ System.out.println("Classifier.checkCondition: checkField ergab: " + isOk);
        return isOk;
    }


    /** analysiert ein Paket hinsichtlich der im Protokoll enthaltenen Information über
     * Datennachrichten und veranlasst die Versendung von entsprechenden Nachrichten an
     * den Server.
     *
     * @param packet
     * @param protocol
     */
    private void analyze(Packet packet, Plugin plugin) {
    	// ich gehe davon aus, dass ich nur IP-Pakete mitlese -> in anderen Netzen
    	// wuerde hier ein Fehler auftreten
    	IPPacket ipPacket = (IPPacket) packet;
    	
        // für jeden Nachrichtentyp prüfen, ob für ihn etwas angegeben ist
        Analyze analyze = plugin.getAnalyze();
        
        
		// es folgt die Auswertung des multipleObjects-Elements
        
        // gibt an, ob eine For- oder eine While-Schleife zur Iteration ueber die Objekte benutzt werden soll
        // 1 = For-Schleife, bei der obL fix ist
        // 2 = While (ObL < OvL) { ObL += OvL }
        // 3 = While (NH != NHCondition) { next NH auslesen }
        // 4 = For-Schleife, bei der obL variabel ist (d.h. immer wieder neu ausgelesen wird
        int loopType = 0;
        
        int obL = 0;
        int ovL = 0;
        int noO = 0;
        int nh = 0;
        int nhConditionValue = 0;
        String nhConditionType = "";
        
		int headerLength = 0;
        
        // testen, ob in dem Plugin "multipleObjects" definiert ist (d.h. ob ein Paket dieses
        // Protokolls mehrere Objekte enthalten kann)
        if (plugin.getAnalyze().getMultipleObjects() != null) {
        	// multiple objects
        	
        	// headerLength auslesen
        	if (plugin.getAnalyze().getMultipleObjects().hasHeaderLength())
        		headerLength = plugin.getAnalyze().getMultipleObjects().getHeaderLength();
        		
        	// lengthType auslesen (kann entw. "fixed" oder "variable" sein)
        	String lengthType = plugin.getAnalyze().getMultipleObjects().getLengthType();
        	
        	// jetzt die angegebenen zwei Elemente auslesen (OvL, ObL, NH, NoO)
        	ObjectLength objectLength = plugin.getAnalyze().getMultipleObjects().getObjectLength();
        	OverallLength overallLength = plugin.getAnalyze().getMultipleObjects().getOverallLength();
        	NextHeader nextHeader = plugin.getAnalyze().getMultipleObjects().getNextHeader();
        	NumberOfObjects numberOfObjects = plugin.getAnalyze().getMultipleObjects().getNumberOfObjects();
        	
        	// pruefen, ob zwei der vier im Plugin angegeben wurden und es auch die richtigen zwei sind
        	boolean multipleObjectDefinitionIsValid = this.isMultipleObjectDefinitionValid(objectLength, overallLength, nextHeader, numberOfObjects, lengthType);
        	        		 
        	if (! multipleObjectDefinitionIsValid) {
        		// irgendwas ist in dem Plugin falsch --> Programm kann nich fortfahren mit der Analyse --> Abbruch
        		// des Analysedurchlaufs mit diesem Plugin
        		return;
        	}
        	else {
        		// multiple objects + Definition im Plugin ist gueltig
    
        		// herausfinden, welche der vier angegeben die gluecklichen zwei sind
        		if (objectLength != null) {
	        		if (overallLength != null) {
	        			// ObL - OvL
	        			
						// ovL setzen
						// givenValue oder Pointer?
						if (overallLength.getGivenValue() != null) {
							ovL = overallLength.getGivenValue().getValue();
						}
						else {
							// obL als Pointer angegeben	        					
							// entsprechenden Wert aus Paket extrahieren     					
							ovL = Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(overallLength.getReference().getBinaryPointer(), packet));
						}
	        			
	        			if (lengthType.equals("fixed")) {
	        				loopType = 1;
	        				
	        				// obL setzen
	        				// givenValue oder Pointer?
							if (objectLength.getGivenValue() != null) {
								obL = objectLength.getGivenValue().getValue();
							}
							else {
								// obL als Pointer angegeben	        					
								// entsprechenden Wert aus Paket extrahieren     					
								obL = Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(objectLength.getReference().getBinaryPointer(), packet));
							}
								
							// noO berechnen
							noO = ovL / obL;	        				
	        			}
	        			else {
	        				loopType = 2; // While (obL < OvL)
	        				
	        				// obL ist hier variabel, d.h. in den Objekten gegeben --> wird in der Schleife jeweils ausgelesen.
	        				// (ovL wurde bereits oben ausgelesen)
	        			}
	        		}
	        		else if (numberOfObjects != null) {
						// ObL - NoO
						
						// noO auslesen (ist in beiden Faellen fix
						// givenValue oder Pointer?
						if (numberOfObjects.getGivenValue() != null) {
							noO = numberOfObjects.getGivenValue().getValue();
						}
						else {
							// noO als Pointer angegeben	        					
							// entsprechenden Wert aus Paket extrahieren     					
							noO = Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(numberOfObjects.getReference().getBinaryPointer(), packet));
						}						
						
						if (lengthType.equals("fixed")) {
							loopType = 1;
							
							// obL setzen
							// givenValue oder Pointer?
							if (objectLength.getGivenValue() != null) {
								obL = objectLength.getGivenValue().getValue();
							}
							else {
								// obL als Pointer angegeben	        					
								// entsprechenden Wert aus Paket extrahieren     					
								obL = Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(objectLength.getReference().getBinaryPointer(), packet));
							}
							
							// (noO wurde bereits oben gesetzt)
						}
						else {
							loopType = 4; // for mit variablem ObL
						}
						
	        		}
	        		else {
						// ObL - NH
						loopType = 3; // bei "fixed" und "variable"
						
						// NH-Kram
						nhConditionValue = nextHeader.getConditionValue();
						nhConditionType = nextHeader.getConditionType();
						
						if (lengthType.equals("fixed")) {							
							// obL auslesen
							// givenValue oder Pointer?
							if (objectLength.getGivenValue() != null) {
								obL = objectLength.getGivenValue().getValue();
							}
							else {
								// obL als Pointer angegeben	        					
								// entsprechenden Wert aus Paket extrahieren     					
								obL = Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(objectLength.getReference().getBinaryPointer(), packet));
							}
						}
						else {
							// nh-Stuff wurde bereits oben ausgelesen. obL wird dynamisch (in der Scgleife) ausgelesen.
							// --> hier gibt's nix zu tun.
						}
	        		}	        		
        		}
        		else {
					// OvL - NoO (nur bei lengthType="fixed")
					if (lengthType.equals("fixed")) {
						loopType = 1; // for
						
						// NoO auslesen
						// givenValue oder Pointer?
						if (numberOfObjects.getGivenValue() != null) {
							noO = numberOfObjects.getGivenValue().getValue();
						}
						else {
							// noO als Pointer angegeben	        					
							// entsprechenden Wert aus Paket extrahieren     					
							noO = Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(numberOfObjects.getReference().getBinaryPointer(), packet));
						}										
						
						// OvL auslesen
						// givenValue oder Pointer?
						if (overallLength.getGivenValue() != null) {
							ovL = overallLength.getGivenValue().getValue();
						}
						else {
							// obL als Pointer angegeben	        					
							// entsprechenden Wert aus Paket extrahieren     					
							ovL = Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(overallLength.getReference().getBinaryPointer(), packet));
						}						
						
						// ObL berechnen
						obL = ovL / noO;
					}
					else {
						System.out.println("error in Classifier.analyze: multipleObjects.lengthType is variable and OvL and NoO are given. That's not possible.");
					}
        		}        		
        	}
        }
        else {
        	// only one object
        	loopType = 1; // Schleifentyp 1 (for)
			noO = 1; // Schleifenvariablen auf Durchlauf 1 setzen (das ist dann ja wie nur ein Objekt)
        }
        
        // Hier kommt jetzt die Schleife, die ueber die in diesem Paket enthaltennen Objekte iteriert.
        // Bei nur einem Objekt erfolgt nur ein Durchlauf.
        // Der Typ der Schleife sowie die Parameter wurden hier oben drueber ermittelt.

        int pos = headerLength; // das erste Objekt beginnt hinter dem Header // TODO gehoert da noch ne +1 hin?

        switch (loopType) {
        	case 1: // for (obL fix)
        	case 4: // for (obL variabel)
        		for (int i = 0; i < noO; i++) {
        			
        			// Objekt untersuchen.
        			this.analyzeObject(analyze, packet, plugin, pos);
        			
        			// pos weiterseitzen
        			if (loopType == 1) { // fix
        				pos += obL;
        			}
        			else { // variabel
        				// objectLength in Abhaengigkeit von pos aus dem Paket auslesen
        				// pos zeigt auf den Anfang des Objektes. Also muss ab pos + objectLength ausgelesen werden.
        				
        				// Der Pointer wird veraendert, indem start um pos weitergesetzt wird. Dann wird ausglesen.
        				// Der Wert, der ausgelesen wird, ist die Laenge dieses Objektes. Das naechste Objekt beginnt
        				// also bei (pos + (ausgelesenerWert * multiplier)).
        				BinaryPointer newPointer = analyze.getMultipleObjects().getObjectLength().getReference().getBinaryPointer();
        				newPointer.setStart(newPointer.getStart() + pos);
        				pos += Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(newPointer, packet)) * analyze.getMultipleObjects().getObjectLength().getMultiplier();
        			}
        		}
        		break;
        	
        	case 2:
        		// While (pos < OvL) pos += obL
        		while (pos < ovL) {
        			
					this.analyzeObject(analyze, packet, plugin, pos);
        			        			
					// objectLength in Abhaengigkeit von pos aus dem Paket auslesen (obL ist bei diesem Schleifentyp immer variabel)
					// pos zeigt auf den Anfang des Objektes. Also muss ab pos + objectLength ausgelesen werden.
        				
					// Der Pointer wird veraendert, indem start um pos weitergesetzt wird. Dann wird ausglesen.
					// Der Wert, der ausgelesen wird, ist die Laenge dieses Objektes. Das naechste Objekt beginnt
					// also bei (pos + (ausgelesenerWert * multiplier)).
					BinaryPointer newPointer = analyze.getMultipleObjects().getObjectLength().getReference().getBinaryPointer();
					newPointer.setStart(newPointer.getStart() + pos);
					pos += Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(newPointer, packet)) * analyze.getMultipleObjects().getObjectLength().getMultiplier();
        		}
        		break;
        	
        	case 3:
        		// While mit next header
        		do {
					this.analyzeObject(analyze, packet, plugin, pos);
					
					// nh auslesen (dazu Pointer so modifizieren, dass er hinter dem Header beginnt)
					BinaryPointer newPointer = analyze.getMultipleObjects().getNextHeader().getReference().getBinaryPointer();
					newPointer.setStart(newPointer.getStart() + pos);
					nh = Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(newPointer, packet));
					
					// pos weitersetzen (um ObL)
					// dabei Unterscheidung, ob fixed oder variable ObL
					if (analyze.getMultipleObjects().getLengthType().equals("fixed")) {
						// fixed
						pos += obL;
					}
					else {
						// variable
						BinaryPointer absoluteNewPointer = analyze.getMultipleObjects().getObjectLength().getReference().getBinaryPointer();
						newPointer.setStart(absoluteNewPointer.getStart() + pos);
						pos += Utils.byteToInt(PluginStructureUtils.getByteArrayFromBinaryPointer(absoluteNewPointer, packet)) * analyze.getMultipleObjects().getObjectLength().getMultiplier();
					}
        			
        		} while ( (nhConditionType.equals("break") && nh !=  nhConditionValue) || (nhConditionType.equals("continue") && nh == nhConditionValue) );
        		break;
        		
        	default:
        		System.out.println("warning in slave.classify.Classifier: undefined looptype: " + loopType);        		
        		break;
        }
    }
    
    
    private void analyzeObject(Analyze analyze, Packet packet, Plugin plugin, int offset) {        
        
        // Unterscheidung nach den verschiedenen Nachrichtentypen

        if (analyze.getMessageCount() != 0) {
        	// es existieren Message-Objekte --> verarbeiten
        	Message [] messages = analyze.getMessage();
        	
        	// über alle enthaltenen Nachrichtenfelder iterieren
        	for (int i = 0; i < messages.length; i++) {
        		// die Pruefung der Bedingung und evtl. das Versenden der Nachricht
        		// an den Master geschieht der Uebersichtlichkeit halber in einer eigenen Methode 
        		this.checkMessage(packet, plugin.getName(), messages[i], offset);
           	}
        }

        else if (analyze.getConnectionCount() != 0) {
        	// es existieren Connection-Objekte --> verarbeiten
        	Connection [] connections = analyze.getConnection();
        	
        	// über alle enthaltenen Connection-Felder iterieren
        	for (int i = 0; i < connections.length; i++) {
        		this.checkConnection(packet, plugin.getName(), connections[i], offset);
        	}
        }
		
		else if (analyze.getConnectionEndCount() != 0) {
			// es existieren ConnectionEnd-Objekte --> verarbeiten
			ConnectionEnd [] connectionEnds = analyze.getConnectionEnd();
			
			// über alle enthaltenen ConnectionEnds iterieren
			for (int i = 0; i < connectionEnds.length; i++) {
				this.checkConnectionEnd(packet, plugin.getName(), connectionEnds[i], offset);
			}
		}
		
		else if (analyze.getNameCount() != 0) {
			// es existieren Name-Objekte --> verarbeiten
			Name [] names = analyze.getName();
			
			// über alle enthaltenen Name-Objekte iterieren
			for (int i = 0; i < names.length; i++) {
				this.checkName(packet, plugin.getName(), names[i], offset);
			}
		}
		
		else if (analyze.getStreamCount() != 0) {
			// es existieren Stream-Objekte --> verarbeiten
			global.pluginStructure.Stream [] streams = analyze.getStream();

			// über alle Streams iterieren
			for (int i = 0; i < streams.length; i++) {
				this.checkStream(packet, plugin.getName(), streams[i]);
			}			
		}        
    }
    
    
    private void checkMessage(Packet packet, String pluginName, Message message, int offset) {
		// Messages werden nur versendet, wenn die angegebene Zieladresse gleich
		// der Adresse dieses Rechners ist. Dadurch wird verhindert, dass der Server ueber eine
		// Nachricht merhmals benachrichtigt wird (von jedem Rechner auf dem Weg).
		//
		// ein Device kann mehrere IP-Adressen haben. Deswegen werden hier alle geprueft, die diesem Device zugehoerig gefunden wurden						
		if (this.ipAddressIsAssociatedToLocalNetworkDevice(PluginStructureUtils.getDestinationAddressFromSourceAndDestination(message.getSourceAndDestination(), packet, offset))) {					        		
			//* DEBUG */ System.out.println("Message " + i + ": Zieladresse ist gleich localhost oder multicast.");
			// wenn die Bedingungen zutreffen, dann Nachricht senden                                        	                    
			if (this.checkConditionGroup(message.getConditionsGroup().getConditions().getConditionGroup(), packet, offset)) {
				// Quelle und Ziel aus der Nachricht extrahieren
	             
				InetAddress source = PluginStructureUtils.getSourceAddressFromSourceAndDestination(message.getSourceAndDestination(), packet, offset);	                    
				//* DEBUG / System.out.println("Classifier: Source extrahiert: " + source);
				InetAddress destination = PluginStructureUtils.getDestinationAddressFromSourceAndDestination(message.getSourceAndDestination(), packet, offset);
				//* DEBUG / System.out.println("Classifier: Destination extrahiert: " + destination);
	                                        
				// die eigentliche Nachricht aus dem Paket extrahieren
				String text = PluginStructureUtils.getStringFromTextGroup(message.getTextGroup(), packet, offset);
				//* DEBUG / System.out.println("Classifier: Text extrahiert: " + text);
	                    
				this.connection.sendMessageMessage( packet.getTimeval().getSeconds() * 1000 + packet.getTimeval().getMicroSeconds() , pluginName, source, destination, text);
				/* DEBUG */ System.out.println("Classifier: MessageMessage sent. Text: " + text);
			}
		}
    }
    
	private void checkConnection(Packet packet, String pluginName, Connection connection, int offset) {
		// Bedingungen für die Nachricht prüfen und ggf. Nachricht senden
		if (this.checkConditionGroup(connection.getConditionsGroup().getConditions().getConditionGroup(), packet, offset)) {
			// source und dest aus ConnectionTypeChoice extrahieren
			InetAddress source = PluginStructureUtils.getSourceAddressFromSourceAndDestination(connection.getSourceAndDestination(), packet, offset);        			
			InetAddress destination = PluginStructureUtils.getDestinationAddressFromSourceAndDestination(connection.getSourceAndDestination(), packet, offset);                    
			this.connection.sendConnectionMessage(packet.getTimeval().getSeconds() * 1000 + packet.getTimeval().getMicroSeconds(), pluginName, source, destination);
			/* DEBUG */ System.out.println("Classifier: ConnectionMessage sent.");
		}
	}
		
	private void checkConnectionEnd(Packet packet, String pluginName, ConnectionEnd connectionEnd, int offset) {
		// Bedingungen prüfen und Nachricht ggf. versenden
		if (this.checkConditionGroup(connectionEnd.getConditionsGroup().getConditions().getConditionGroup(), packet, offset)){
			// source und dest aus ConnectionEndTypeChoice extrahieren
			InetAddress source = PluginStructureUtils.getSourceAddressFromSourceAndDestination(connectionEnd.getSourceAndDestination(), packet, offset);
			InetAddress destination = PluginStructureUtils.getDestinationAddressFromSourceAndDestination(connectionEnd.getSourceAndDestination(), packet, offset);
			this.connection.sendConnectionEndMessage(packet.getTimeval().getSeconds() * 1000 + packet.getTimeval().getMicroSeconds(), pluginName, source, destination);
			/* DEBUG */ System.out.println("Classifier: ConnectionEndMessage sent.");
		}
	}
		
	private void checkName(Packet packet, String pluginName, Name name, int offset) {
		// Bedingungen prüfen und ggf. Nachricht versenden
		if (this.checkConditionGroup(name.getConditionsGroup().getConditions().getConditionGroup(), packet, offset)) {
			// Namen und Computer-IP aus Message extrahieren
			String nameA = PluginStructureUtils.getStringFromTextGroup(name.getTextGroup(), packet, offset);
			InetAddress computer = PluginStructureUtils.getInetAddressFromComputer(name.getComputer(), packet, offset);
			this.connection.sendNameMessage(packet.getTimeval().getSeconds() * 1000 + packet.getTimeval().getMicroSeconds(), pluginName, computer, nameA);
			/* DEBUG */ System.out.println("Classifier: NameMessage sent.");
		}
	}
	
	private void checkStream(Packet packet, String pluginName, global.pluginStructure.Stream stream) {
		// die Verwaltung der Streams übernimmt eine Instanz der Klasse StreamManager
		// hier wird dem Manager nur das Paket mit den zugehörigen Komparatoren
		// übergeben. Der Manager regelt alles weitere.
		this.streamManager.addPacket(packet, pluginName, stream.getComparators());
		//* DEBUG */ System.out.println("Classifier: Streammanager benachrichtigt.");
		
		// --------------------------------------------------------------------------------------------------------------------------------------------------------
		// Streams und MultipleObjects lassen sich in der momentanen Version nicht kombinieren. D.h. auch wenn
		// MultipleObjects angegeben ist, werden diese Angaben in bezug auf Streams ignoriert!
		// --------------------------------------------------------------------------------------------------------------------------------------------------------
	}
    

	/** testet, ob zwei dieser Parameter nicht null sind und ob die Kombination
	 * dieser beiden der Definition entspricht
	 * @param objectLength
	 * @param overallLength
	 * @param nextHeader
	 * @param numberOfObjects
	 * @return
	 */
	private boolean isMultipleObjectDefinitionValid(ObjectLength objectLength, OverallLength overallLength, NextHeader nextHeader, NumberOfObjects numberOfObjects, String lengthType) {
		
		if (lengthType.equals("fixed")) {
			// fixed
			// (die Kombinationen ObL-OvL, ObL-NoO, ObL-NH, OvL-NoO sind moeglich)
		
			if (objectLength == null) {
				// wenn ObL nicht angegeben ist, ist nur noch OvL-NoO moeglich
				if (overallLength != null && numberOfObjects != null)
					return true;
				else
					return false;
			}
			else {
				// pruefen, ob entweder Ovl, NoO oder NH angegeben ist
				if (overallLength != null || numberOfObjects != null || nextHeader != null)
					return true;
				else
					return false;		
			}
		}
		else {
			// variable
			// (die Kombinationen ObL-OvL, ObL-NoO, ObL-NH sind moeglich)
			
			// wenn ObL nicht gegeben ist, ist sowieso alles vorbei
			if (objectLength == null)
				return false;
			else {
				// pruefen, ob entweder Ovl, NoO oder NH angegeben ist
				if (overallLength != null || numberOfObjects != null || nextHeader != null)
					return true;
				else
					return false;		
			}
		}
	}


	/**
	 * @param address
	 * @return
	 */
	private boolean ipAddressIsAssociatedToLocalNetworkDevice(InetAddress address) {
		boolean isAssociated = false;

		// alle IP-Adressen holen, die dem Geraet, von dem gelesen wird, zugeordnet sind
		Enumeration ipAddresses = this.networkInterface.getInetAddresses();
		
		//* DEBUG */ System.out.print("Adresse " + address.getHostAddress() + " ");		
		
		while(ipAddresses.hasMoreElements()) {
			if (((InetAddress) ipAddresses.nextElement()).equals(address)) {
				isAssociated = true;
				//* DEBUG */ System.out.println("gehoert zu diesem Interface.");				
			}
		}
		
		// Es koennte auch noch eine Multicast-Adresse sein. Dieser werden nur akzeptiert, wenn dieser Knoten explizit als Multicast-Knoten definiert ist
		// (per Parameter).
		if (this.multicast && address.isMulticastAddress()) {
			//* DEBUG */ System.out.println("M U L T I C A S T");			
			isAssociated = true;			
		}
			

		return isAssociated;
	}


	/** prüft eine Menge von Identifier-Objekten, zusammengefasst in einem Identifiers-Objekt.
	 * Treffen alle Bedingungen zu, liefert die Methode true zurück. Es müssen ALLE Bedingungen
	 * zutreffen, um true zu erhalten.
	 * Diese Methode wird benutzt, um im Analyseteil zu prüfen, ob Nachrichten, die im Plugin definiert sind,
	 * an den Server versendet werden sollen.
	 * @param identifiers
	 * @return true, wenn alle Bedingungen zutreffen; sonst false
	 */
	/*private boolean checkIdentifiers(IPPacket packet, Identifiers identifiers) {
		// trifft eine Bedingung nicht zu, so wird diese Variable auf false gesetzt 
		boolean everythingApplies = true;
		
		// Bedingungen in binärer Angabe und in textueller Angabe werden
		// getrennt voneinander behandelt, da es unterschiedlicher Zugriffe
		// in das Paket bedarf
		Identifier [] identifierField = identifiers.getIdentifier();		
		
		// ueber alle Identifier iterieren. Jeder wird ueberprueft. Ist er nicht erfuellt, liefert die
		// Pruefmethode false zurueck, wodurch everythingApplies unweigerlich false wird und bleibt.
		for (int i = 0; i < identifierField.length; i++) {
			everythingApplies = PluginStructureUtils.checkIdentifier(identifierField[i], packet);
		}		

		return everythingApplies;
	}*/


	/** hierüber lässt sich das Klassifizieren beenden. Es werden keine weiteren Pakete mehr aus dem Puffer
	 * gelesen.
	 *
	 *
	 */
	public void end(){
		this.end = true;
	}


	/**
	 * @param filter
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
}

package slave.classify;


import global.Fields;
import global.Utils;
import global.pluginStructure.Comparator;
import global.pluginStructure.Comparators;
import global.pluginStructure.types.LocationType;
import global.pluginStructure.types.ValueStreamType;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import net.sourceforge.jpcap.net.EthernetPacket;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.IPPacket;
import net.sourceforge.jpcap.net.TCPPacket;
import net.sourceforge.jpcap.net.UDPPacket;


/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class Stream {
	
	private static Fields fields = new Fields();

    // gibt an, was das letzte Paket zu diesem Stream angekommen ist
    private Date lastPacketArrived;

    // die Komparatoren fuer diesen Stream
    private Comparators comparators;

    // das erste Paket dieses Streams. Dient als Vergleichsbasis fuer neue Pakete
    private Packet packet;

    // gibt an, wieviele Pakete fuer diesen Stream bereits erkannt wurden (ohne das erste)
    private int numberOfPackets;

    // was ist das hier eigentlich fuer ein Stream?
    private String protocol;
    private int port;


    public Stream (Packet packet, String protocol, int port, Comparators comparators) {
        //this.comparators = comparators; // TODO auch hier geschieht wohl ein Call-by-Value
        StringWriter stringWriter = new StringWriter();        
        try {
			comparators.marshal(stringWriter);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
        StringReader stringReader = new StringReader(stringWriter.toString());
        try {
			this.comparators = (Comparators) Comparators.unmarshal(stringReader);
		} catch (MarshalException e1) {
			e1.printStackTrace();
		} catch (ValidationException e1) {
			e1.printStackTrace();
		}
        
        this.protocol = protocol;
        this.port = port;
        //this.packet = packet; // hier darf kein Call-by-Reference passieren!
		byte [] data = new byte [((EthernetPacket) packet).getEthernetData().length + ((EthernetPacket) packet).getEthernetHeader().length];
		System.arraycopy(((EthernetPacket) packet).getEthernetHeader(), 0, data, 0, ((EthernetPacket) packet).getEthernetHeader().length);
		System.arraycopy(((EthernetPacket) packet).getEthernetData(), 0, data, ((EthernetPacket) packet).getEthernetHeader().length, ((EthernetPacket) packet).getEthernetData().length);
		
        if (packet instanceof UDPPacket) {
        	this.packet = new UDPPacket(14, data ); // 14 als Ethernetheaderlength und dann das ganze Paket als Byte-Array
        }
        else if (packet instanceof TCPPacket) {
        	this.packet = new TCPPacket (14, data );
        }
        else {
        	System.out.println("NICHTS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");        	
        }
        
        /*System.out.print("neues Paket:");
        byte [] h = this.packet.getData();
        for (int i = 0; i < h.length; i++) {
        	System.out.print(h[i] + " ");        	
        }
        System.out.println();
                                
		System.out.print("altes Paket:");
		byte [] g = packet.getData();
		for (int i = 0; i < g.length; i++) {
			System.out.print(g[i] + " ");        	
		}
		System.out.println();*/        
        
        this.lastPacketArrived = new Date();
        this.numberOfPackets = 0;
		
		/* DEBUG */ System.out.println("");
        /* DEBUG */ System.out.println("new Stream detected.");
        
        Comparator [] debug = this.comparators.getComparator();
        for (int i = 0; i < debug.length; i++) {
        	byte [] array =  PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) this.packet, debug[i].getBinaryPointerStream().getLocation().getType());
            array = Utils.getBitIndicatedPartOfByteArray(array, debug[i].getBinaryPointerStream().getStart(), debug[i].getBinaryPointerStream().getLength());
            //* DEBUG */ System.out.println("Comparator " + i + " hat Wert " + Utils.byteToInt(array));
            
			byte [] array1 =  PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, debug[i].getBinaryPointerStream().getLocation().getType());
			array1 = Utils.getBitIndicatedPartOfByteArray(array1, debug[i].getBinaryPointerStream().getStart(), debug[i].getBinaryPointerStream().getLength());
			//* DEBUG */ System.out.println("Comparator " + i + " hat Wert " + Utils.byteToInt(array1) + " (Urpsrungspaket).");			
        }
		/* DEBUG */ System.out.println("");
    }


    /** hierueber signalisiert der StreamManager dem Stream, dass ein neues ihm
     * zugehoeriges Paket eingetroffen ist, der Stream also noch lebt
     *
     */
    public void newPacket() {
        // durch Neuanlegen des Objektes wird es auf die aktuelle Zeit gesetzt
        this.lastPacketArrived = new Date();
        this.numberOfPackets++;
		//* DEBUG */ System.out.println("Stream wurde ueber neues Paket infomiert.");
    }


    /** vergleicht das als Parameter uebergegene Paket mit den Komparatoren und
     * findet so heraus, ob es zu diesem Stream gehoert.
     *
     * @param packet das zu vergleichende Paket
     * @return true, wenn das Paket zu diesem Stream gehoert; sonst false
     */
    public boolean compare(Packet packet, String protocol) {
        // gibt an, ob das Paket zu diesem Strom gehoert. Ist eine Bedingung nicht erfuellt
        boolean isSame = true;

        // wenn das Protokoll nicht stimmt, ist sowieso schon alles vorbei
        if (protocol.equals(this.protocol)) {        
	        // ueber alle Komparatoren iterieren
			//* DEBUG */ System.out.println("Stream.compare: pruefe " + this.comparators.getComparatorCount() + " Komparatoren.");			
	        for (int i = 0; i < this.comparators.getComparatorCount(); i++) {
	            Comparator comparator = this.comparators.getComparator(i);	            				            
	            // Unterscheidung ob textuelle oder binaere Angabe
	            if (comparator.getBinaryPointerStream() != null) {	            	
					// der interessante Teil des uebergebenen Paketes
					byte [] lookHere = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, comparator.getBinaryPointerStream().getLocation().getType());
					
					//* DEBUG */ System.out.println("lookHere hat die Laenge: " + lookHere.length);
				
					// der interessante Teil im Referenzpaket des Streams
					byte [] valueInReferencePacket = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) this.packet, comparator.getBinaryPointerStream().getLocation().getType());	           					
					
					int start;
					int length;

	            	if (comparator.getBinaryPointerStream().hasStart() && comparator.getBinaryPointerStream().hasLength()) {
						 start = comparator.getBinaryPointerStream().getStart();
						 length = comparator.getBinaryPointerStream().getLength();
					}
					else {
						// Angabe ist ueber Namen erfolgt
						
						// "next header" kann wg. Erweiterungsheadern an verschiedenen Stellen liegen. Daher Sonderbehandlung
						if ((comparator.getBinaryPointerStream().getLocation().getType() == LocationType.IP_TYPE || comparator.getBinaryPointerStream().getLocation().getType() == LocationType.IPV6_TYPE) &&  ((IPPacket) packet).getVersion() == 6 && comparator.getBinaryPointerStream().getName().equals("next header")) {
							//* DEBUG */ System.out.println("Stream: Sonderbehandlung fuer name=\"next header\"");				
							start = ((IPPacket) packet).getNextHeaderPos();
							length = fields.getLengthFromName(comparator.getBinaryPointerStream().getName(), LocationType.IPV6);
						}
						// wenn die Location-Angabe "ip" ist, muss erstmal die Version herausgefunden werden
						else if ((comparator.getBinaryPointerStream().getLocation().getType() == LocationType.IP_TYPE || comparator.getBinaryPointerStream().getLocation().getType() == LocationType.IPV6_TYPE) &&  ((IPPacket) packet).getVersion() == 6) {
							start = fields.getStartFromName(comparator.getBinaryPointerStream().getName(), LocationType.IPV6);
							length = fields.getLengthFromName(comparator.getBinaryPointerStream().getName(), LocationType.IPV6);        		
						}
						else if ((comparator.getBinaryPointerStream().getLocation().getType() == LocationType.IP_TYPE || comparator.getBinaryPointerStream().getLocation().getType() == LocationType.IPV4_TYPE) &&  ((IPPacket) packet).getVersion() == 4) {
							start = fields.getStartFromName(comparator.getBinaryPointerStream().getName(), LocationType.IPV4);
							length = fields.getLengthFromName(comparator.getBinaryPointerStream().getName(), LocationType.IPV4);
						}
						else {
							start = fields.getStartFromName(comparator.getBinaryPointerStream().getName(), comparator.getBinaryPointerStream().getLocation());
							length = fields.getLengthFromName(comparator.getBinaryPointerStream().getName(), comparator.getBinaryPointerStream().getLocation());
						}  	            		
					}
					
					// Wert aus dem Referenzpaket holen
					byte [] value = global.Utils.getBitIndicatedPartOfByteArray(valueInReferencePacket, start, length);
					
					int type = comparator.getBinaryPointerStream().getValue().getType();
					
					//* DEBUG */ System.out.println("start = " + start);
					//* DEBUG */ System.out.println("length = " + length);
					
					//------------------- DEBUG -----------------------
					//byte [] help = Utils.getBitIndicatedPartOfByteArray(lookHere, start, length);
					//System.out.print("new packet: ");					
					//for (int t = 0; t < help.length; t++) {
					//	System.out.print(help[t]);
					//}
					//System.out.println();
					// ------------------- DEBUG -----------------------
					
					// ------------------- DEBUG -----------------------
					//System.out.print("referencePacket: ");					
					//for (int t = 0; t < value.length; t++) {
					//	System.out.print(value[t]);
					//}
					//System.out.println();
					// ------------------- DEBUG -----------------------

					switch (type) {
						case ValueStreamType.SAME_TYPE:
							//* DEBUG */ System.out.println("Stream.compare: SAME ");
							//* DEBUG */ System.out.println("Referenzpaket enthaelt folgenden Wert: "+ String.valueOf(Utils.byteToInt(value)) + "; als int: " + Utils.byteToInt(value));						
							//* DEBUG */ System.out.println("neues Paket enthaelt folgenden Wert: " + Utils.byteToInt(Utils.getBitIndicatedPartOfByteArray(lookHere, start, length)));
							// hier muss verglichen werden, ob an der angegebenen Stelle derselbe Wert steht wie im Referenzpaket des Streams
							isSame = PluginStructureUtils.checkBinaryField(Utils.getBitIndicatedPartOfByteArray(lookHere, start, length), 0, Utils.getBitIndicatedPartOfByteArray(lookHere, start, length).length, String.valueOf(Utils.byteToInt(value)), 0);
							//* DEBUG */ System.out.println("Stream.compare: this comparator was " + isSame);
							break;
	            			
							case ValueStreamType.INCREASE_TYPE:
								//* DEBUG */ System.out.println("Stream.compare: INCREASE ");								
								// hier muss verglichen werden, ob an der angegebenen Stelle der Wert um eins erhoeht steht wie im letzten erkannten Paket													
						
								// wert des Referenzpaketes plus anzahl bereits empfangener Pakete
								int lastValue = Utils.byteToInt(value) + this.numberOfPackets;
								
								//* DEBUG*/ System.out.println("numberOfPackets = " + numberOfPackets);								
							
								//* DEBUG */ System.out.println("Referenzpaket enthaelt folgenden Wert (-1): "+ String.valueOf(lastValue + 1));
								//* DEBUG */ System.out.println("neues Paket enthaelt folgenden Wert: " + Utils.byteToInt(Utils.getBitIndicatedPartOfByteArray(lookHere, start, length)));
							
								// dieser Wert um eins erhoeht muss im zu pruefenden Paket drinstehen
								isSame = PluginStructureUtils.checkBinaryField(Utils.getBitIndicatedPartOfByteArray(lookHere, start, length), 0, Utils.getBitIndicatedPartOfByteArray(lookHere, start, length).length, String.valueOf(lastValue + 1), 0);
								//* DEBUG */ System.out.println("Stream.compare: this comparator was " + isSame);														
								break;
							
							case ValueStreamType.DECREASE_TYPE:
								//* DEBUG */ System.out.println("Stream.compare: DECREASE ");								
								// hier muss verglichen werden, ob an der angegebenen Stelle der Wert um eins erniedrigt steht wie im letzten erkannten Paket
								
								// wert des Referenzpaketes plus anzahl bereits empfangener Pakete
								int lastOne = Utils.byteToInt(value) - this.numberOfPackets;
								
								//* DEBUG */ System.out.println("erwartet wird folgender Wert: "+ String.valueOf(lastOne - 1));								
							
								// dieser Wert um eins erhoeht muss im zu pruefenden Paket drinstehen
								isSame = PluginStructureUtils.checkBinaryField(lookHere, start, length, String.valueOf(lastOne - 1), 0);
								//* DEBUG */ System.out.println("Stream.compare: this comparator was " + isSame);
								break;

							default:
								// Beten wir alle, dass dieser Fall nie eintrifft.......
								System.out.println("error in client.classify.Stream:");
					}
	            }
	            
	            else if (comparator.getTextualPointerStream() != null) {	        		        				
					// der interessante Teil des uebergebenen Paketes
					byte [] lookHere = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, comparator.getTextualPointerStream().getLocation().getType());
				
					// der interessante Teil im Referenzpaket des Streams
					byte [] valueInReferencePacket = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) this.packet, comparator.getTextualPointerStream().getLocation().getType());
				
					// den Vergleichswert aus dem Referenzpaket holen
					int value = Integer.parseInt(PluginStructureUtils.getStringFromTextualField(valueInReferencePacket, comparator.getTextualPointerStream().getRow(), comparator.getTextualPointerStream().getWord(), comparator.getTextualPointerStream().getAdditionalSeperator()));				
	            
					if (!comparator.getTextualPointerStream().getRow().equals(null) && !comparator.getTextualPointerStream().getWord().equals(null)) {
						int type = comparator.getTextualPointerStream().getValue().getType();

						switch (type) {
							case ValueStreamType.SAME_TYPE:
								// hier muss verglichen werden, ob an der angegebenen Stelle derselbe Wert steht wie im Referenzpaket des Streams
								isSame = PluginStructureUtils.checkTextualField(lookHere, comparator.getTextualPointerStream().getRow(), comparator.getTextualPointerStream().getWord(), String.valueOf(value), comparator.getTextualPointerStream().getAdditionalSeperator(), 0);
								break;
							
							case ValueStreamType.INCREASE_TYPE:
								// hier muss verglichen werden, ob an der angegebenen Stelle der Wert um eins erhoeht steht wie im letzten erkannten Paket
							
								// wert des Referenzpaketes plus anzahl bereits empfangener Pakete
								int lastValue = value + this.numberOfPackets;
						
								// dieser Wert um eins erhoeht muss im zu pruefenden Paket drinstehen
								isSame = PluginStructureUtils.checkTextualField(lookHere, comparator.getTextualPointerStream().getRow(), comparator.getTextualPointerStream().getWord(), String.valueOf(lastValue + 1), comparator.getTextualPointerStream().getAdditionalSeperator(), 0);
								break;
												
							case ValueStreamType.DECREASE_TYPE:
								// hier muss verglichen werden, ob an der angegebenen Stelle der Wert um eins erniedrigt steht wie im letzten erkannten Paket
								
								// wert des Referenzpaketes plus anzahl bereits empfangener Pakete
								int lastOne = value - this.numberOfPackets;
							
								// dieser Wert um eins erhoeht muss im zu pruefenden Paket drinstehen
								isSame = PluginStructureUtils.checkTextualField(lookHere, comparator.getTextualPointerStream().getRow(), comparator.getTextualPointerStream().getWord(), String.valueOf(lastOne -1), comparator.getTextualPointerStream().getAdditionalSeperator(), 0);
								break;
						}
					}
					else {
						// in textuellen Protokollen koennen keine namensbasierten Angaben gemacht werden, da
						// namensbasierte Angaben nur bei IP, TCP und UDP (also den Grundprotokollen) moeglich sind.
					}
				}
				    	            	
	        }	            	            		        	    
        }
        
        //* DEBUG */ System.out.println("Stream.compare ergab: " + isSame);
        return isSame;
    }


    /** gibt zurueck, wie lange schon kein Paket mehr fuer diesen Stream angekommen ist
     *
     */
    public long getInactiveTime() {
        // Hinweis: die Zeiten liegen in Millisekunden vor
        
        long lastOne = this.lastPacketArrived.getTime();
        Date nowDate = new Date();
        long now = nowDate.getTime();
        
        //System.out.println("inactiveTime: " + (now - lastOne) + ", lastOne: " + lastOne);
        
        return now - lastOne;
    }
    
    
    public String getProtocol () {
    	return this.protocol;
    }
    
    
	public int getPort () {
		return this.port;
	}
    
    
    public InetAddress getSourceAddress () {
    	InetAddress address = null;
    	
    	try {
			address = InetAddress.getByName(((IPPacket) this.packet).getSourceAddress());
		} catch (UnknownHostException e) {
			System.out.println("fatal error in client.classify.Stream: an IP extracted from a captured packet could not be resolved (unknown host exception). That can't be.");
			e.printStackTrace();
		} 
    	
    	return address;
    }
    
    
    public InetAddress getDestinationAddress () {
		InetAddress address = null;
    	
		try {
			address = InetAddress.getByName(((IPPacket) this.packet).getDestinationAddress());
		} catch (UnknownHostException e) {
			System.out.println("fatal error in client.classify.Stream: an IP extracted from a captured packet could not be resolved (unknown host exception). That can't be.");
			e.printStackTrace();
		} 
    	
		return address;
    }

}

package slave.classify;

import global.pluginStructure.types.BinaryPointerTypeTypeType;
import global.pluginStructure.types.LocationType;
import global.pluginStructure.BinaryPointer;
import global.pluginStructure.BinaryPointerWithValue;
import global.pluginStructure.Computer;
import global.pluginStructure.SourceAndDestination;
import global.pluginStructure.TextGroup;
import global.pluginStructure.TextualPointer;
import global.pluginStructure.TextualPointerWithValue;
import global.Fields;
import global.Utils;
import net.sourceforge.jpcap.net.IPPacket;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.UDPPacket;
import net.sourceforge.jpcap.net.TCPPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;


/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class PluginStructureUtils {
	
	// hierdrin liegen die Zuordnungen der Namen der Felder zu den Positionen
	// (wenn z.B. nur "source port" und "udp" angegeben wird, kann man hierdrueber
	// nachgucken, wo genau das ist)
	private static Fields fields = new Fields();


    public static byte [] getLocationByteArrayFromLocationType(IPPacket packet, int type) {
        byte [] newArray = null;

        switch (type) {
            case LocationType.UDP_TYPE:
            	if (packet instanceof UDPPacket)
                	newArray = ((UDPPacket) packet).getUDPHeader();
                break;

            case LocationType.TCP_TYPE:
				if (packet instanceof TCPPacket)
                	newArray = ((TCPPacket) packet).getTCPHeader();                
                break;

            case LocationType.SCTP_TYPE:
                // TODO wird spaeter mal unterstuetzt
                break;

            case LocationType.TRANSPORT_TYPE:
                // hier soll allgemein im Transport-Header geguckt werden. Da dieser
			    // keine einheitliche Laenge hat, werden einfach die gesamten Daten des
				// IP-Pakets (also Transport-Header und weitere Daten) untersucht.
                newArray = packet.getData();
                break;

            case LocationType.IPV4_TYPE:
				// wenn es kein IPv4-Paket ist, gibt's auch keinen IPv4-Header
				if (packet.getVersion() == 4)
					newArray = packet.getIPHeader();
            	break;
            	
            case LocationType.IPV6_TYPE:
				// wenn es kein IPv6-Paket ist, gibt's auch keinen IPv6-Header
				if (packet.getVersion() == 6)
					newArray = packet.getIPHeader();
				break;
				
            case LocationType.IP_TYPE:
            	// hier wird allgemein nach dem IP-Header gefragt. Also wird einfach einer geliefert.
            	// Hier ist natuerlich nicht festgelegt, ob es IPv4 oder IPv6 ist.
                newArray = packet.getIPHeader();
                break;

            case LocationType.DATA_TYPE:
                // die Daten liegen je nach Transportprotokoll an anderer Stelle
                if (packet instanceof UDPPacket) {
                	//* DEBUG */ System.out.println("Paket ist UDP-Paket.");
					newArray = ((UDPPacket) packet).getUDPData();
                }                    
                else if (packet instanceof TCPPacket) {
					//* DEBUG */ System.out.println("Paket ist TCP-Paket.");
					//* DEBUG */ System.out.println("TCP payload length: " + ((TCPPacket) packet).getPayloadDataLength());
					//* DEBUG */ System.out.println("TCP header length: " + ((TCPPacket) packet).getTCPHeaderLength());
					newArray = ((TCPPacket) packet).getTCPData();
                }                    
                else
                	//* DEBUG */ System.out.println("Paket ist weder UDP-Paket noch TCP-Paket.");
                	
                	// wenn es weder TCP noch UDP ist, werden die Daten wohl direkt hinter dem IP-Header liegen
                	newArray = packet.getIPData();
                	                	
                /*else if (packet instanceof ICMPPacket) {
                	newArray = ((ICMPPacket) packet).getICMPData();
                }*/
                // TODO weitere Protokolle (z.B. SCTP)

                break;

            default:
                System.out.println("error in client.classify.Classifier.checkIdentifiers: given LocationType is not valid.");
                break;
        }
        return newArray;
    }


    /** prueft, ob im Byte-Feld array begonnen an der Position start in den n�chsten length
	 * Feldern der Wert value drinsteht.
	 *
	 * @param array
	 * @param start
	 * @param length
	 * @param value
	 * @return true, wenn der Wert value drinsteht; false wenn nicht
	 */
	public static boolean checkBinaryField(byte [] array, int start, int length, String value, int offset) {
		
		// array wird so beschnitten, dass es erst bei offset anfaengt    	
		array = Utils.getBitIndicatedPartOfByteArray(array, offset, array.length * 8 - offset);	// TODO +1 ?
		
		boolean isOk = false;

        // Sonderbehandlung, wenn der String "EVEN" oder "ODD" ist
        if (value.equals("EVEN")) {
            // den angegebenen Wert aus dem array herausholen und in int umwandeln
            //byte [] toCompare = Utils.getBitIndicatedPartOfByteArray(array, start, length);
            int toCompareAsInt = Utils.byteToInt(array);

            // gucken, ob dieser Wert gerade ist
            if (toCompareAsInt % 2 == 0)
                isOk = true;
            else
                isOk = false;
        }
        else if (value.equals("ODD")) {
            // den angegebenen Wer aus dem array herausholen und in int umwandeln
            //byte [] toCompare = Utils.getBitIndicatedPartOfByteArray(array, start, length);
            int toCompareAsInt = Utils.byteToInt(array);

            // gucken, ob dieser Wert ungerade ist
            if (toCompareAsInt % 2 != 0)
                isOk = true;
            else
                isOk = false;
        }
        else {
            // Normalfall: der String ist eine Integerzahl. jetzt muss nur geprueft werden
            // ob diese Zahl im angegebenen Bereich steht

            // erstmal muss der value auch in ein Byte-Array umgewandelt werden
            byte [] valueAsByteArray = Utils.intToByte(Integer.parseInt(value), length);
            
            //* DEBUG */ System.out.println("erwarteter Wert = " + value + ", Wert im Paket = " + global.Utils.byteToInt(global.Utils.getBitIndicatedPartOfByteArray(array, start, length)));            

            // und jetzt vergleichen
            int counter = 0;
            for (int i = 0; i < length; i++) {
                if (array [start + i] == valueAsByteArray[i])
                    counter++;
            }
            if (counter == length)
                isOk = true;
            else
                isOk = false;
        }

        return isOk;
	}



    public static boolean checkTextualField(byte [] array, String row, String word, String value, String additionalSeperator, int offset) {
    	
		// array wird so beschnitten, dass es erst bei offset anfaengt    	
    	array = Utils.getBitIndicatedPartOfByteArray(array, offset, array.length * 8 - offset);	// TODO +1 ?
    	
		// pruefen, ob im Array array, welches einen textuellen Teil eines Protokolls enthaelt
		// in der angegebenen Zeile an der angegegeben Stelle gleich value ist		
		String extractedValue = getStringFromTextualField(array, row, word, additionalSeperator);
        	
        // Wort im Paket und Wort in der Bedingung vergleichen
        if (value.equals(extractedValue))
        	return true;                        	    
        return false;
    }

    
    
    public static String getStringFromTextualField(byte [] array, String row, String word, String additionalSeperator) {
		String text = Utils.byteToString(array);
		StringReader stringReader = new StringReader(text);
		BufferedReader bufferedReader = new BufferedReader(stringReader);
        		
		// die Zeilenangabe kann numerisch oder ueber das erste Wort der Zeile erfolgen --> unterscheiden		
		String line = "";
		try {
			int rowNumber = Integer.parseInt(row);
			
			// die richtige Zeile auslesen			
			for (int i = 0; i < rowNumber; i++) {
				try {
					line = bufferedReader.readLine();
					//* DEBUG */ System.out.println("Zeile " + i +": " + line);
				} catch (IOException e) {
					System.out.println("error in client.classify.PluginStructureUtils.checkTextualField: could not reach line \"row\" because the String is not that long.");
					e.printStackTrace();
				}
			}
		}
		catch (NumberFormatException e) {
			// ist keine Zahl --> Angabe ueber ersters Wort der Zeile
			
			// die richtige Zeile auslesen			
			while (!line.startsWith(row)) {
				try {
					line = bufferedReader.readLine();
					//* DEBUG */ System.out.println("Zeile: " + line);
				} catch (IOException e1) {
					System.out.println("error in client.classify.PluginStructureUtils.checkTextualField: could not reach line \"row\" because the String is not that long.");
					e1.printStackTrace();
				}
			}
		}
		
		//* DEBUG */ System.out.println("die ganze Zeile: " + line);

		// auch das Wort kann als Nummer oder als String ("LAST") angebeben sein
		String extractedValue = "";
		
		if (additionalSeperator == null)
			additionalSeperator = "";
			
		StringTokenizer lineTokenizer = new StringTokenizer(line, additionalSeperator + " ");
		//* DEBUG */ System.out.println("PluginStructureUtils.getStringFromTextualField: lineTokenizer.countTokens() = " + lineTokenizer.countTokens());
		boolean isGivenAsWordNumber = true;
		int wordNumber = 0;
		try {
			wordNumber = Integer.parseInt(word);			
		}
		catch (NumberFormatException e) {
			isGivenAsWordNumber = false;
		}
		
		//* DEBUG */ System.out.println("wordNumber = " + wordNumber + ", isGivenAsWordNumber = " + isGivenAsWordNumber);

		if (isGivenAsWordNumber) {
			// das Wort kann nur extrahiert werden, wenn die Zeile mindestens so viele Woerter hat
			if (wordNumber <= lineTokenizer.countTokens() - 1) {
				//* DEBUG */ System.out.println("extrahiere Wort.");				
				// aus dieser Zeile das richtige Wort extrahieren
				for (int i = 0; i < wordNumber; i++)					
					extractedValue = lineTokenizer.nextToken();
			}
		}
		else {	
			// Wort ist als String angegeben ("LAST") --> letztes Wort extrahieren
			for (int i = 0; i <lineTokenizer.countTokens(); i++) {
				extractedValue = lineTokenizer.nextToken();
			}
		}

		return extractedValue;
    }


	public static InetAddress getSourceAddressFromSourceAndDestination (SourceAndDestination sad, Packet packet, int offset) {
		InetAddress address = null;
	
		if (sad.getSource().getBinaryPointer() != null) {
			// binaeres Protokoll
			try {
				byte [] feld = PluginStructureUtils.getByteArrayFromBinaryPointer (sad.getSource().getBinaryPointer(), packet);
				address = InetAddress.getByAddress(Utils.getBitIndicatedPartOfByteArray(feld, offset, feld.length * 8 - offset));	// TODO +1 ?
			} catch (UnknownHostException e) {
				// Dieser Fehler kann nicht auftreten
				e.printStackTrace();
			}
		}
		else if (sad.getSource().getTextualPointer() != null) {
			// textuelles Protokoll
			try {
		    	address = InetAddress.getByName(PluginStructureUtils.getStringFromTextualPointer (sad.getSource().getTextualPointer(), packet));
		    	
				// TODO hier muss der offset noch eingebunden werden. Aber wie wird der bei TextualPointern interpretiert?
		    	
			} catch (UnknownHostException e) {
				// Dieser Fehler kann nicht auftreten
				e.printStackTrace();
			}						
		}
		else {
			System.out.println("error in client.classify.PluginStructureUtils.getSourceAddressFromSourceAndDestination: source-object does not include binaryPointer oder textualPointer.");
		}
		
		return address;
	}
	
	
	public static InetAddress getDestinationAddressFromSourceAndDestination (SourceAndDestination sad, Packet packet, int offset) {
		InetAddress address = null;
	
		if (sad.getDestination().getBinaryPointer() != null) {
			// binaeres Protokoll
			
			/* DEBUG */
			//byte [] olala = PluginStructureUtils.getByteArrayFromBinaryPointer (sad.getDestination().getBinaryPointer(), packet);
			//System.out.println("length of byte field: " + olala.length);
			/* DEBUG */
			
			try {
				byte [] feld = PluginStructureUtils.getByteArrayFromBinaryPointer (sad.getDestination().getBinaryPointer(), packet);
				address = InetAddress.getByAddress(Utils.getBitIndicatedPartOfByteArray(feld, offset, feld.length * 8 - offset)); // TODO +1 ?
			} catch (UnknownHostException e) {
				// Dieser Fehler kann nicht auftreten
				e.printStackTrace();
			}
		}
		else if (sad.getDestination().getTextualPointer() != null) {
			// textuelles Protokoll
			try {
				String string = PluginStructureUtils.getStringFromTextualPointer (sad.getDestination().getTextualPointer(), packet);
				address = InetAddress.getByName(string);
				
				// TODO hier muss der offset noch eingebunden werden. Aber wie wird der bei TextualPointern interpretiert?
				
			} catch (UnknownHostException e) {
				// Dieser Fehler kann nicht auftreten
				e.printStackTrace();
			}						
		}
		else {
			System.out.println("error in client.classify.PluginStructureUtils.getDestinationAddressFromSourceAndDestination: destination-object does not include binaryPointer oder textualPointer.");
		}
		
		return address;
	}
	
	
	public static String getStringFromTextGroup(TextGroup textGroup, Packet packet, int offset) {
		String text = "";
		
		if (textGroup.getGivenText() != null) {
			// die Textangabe ist fest im Plugin codiert (offset interessiert hier nicht)
			text = textGroup.getGivenText().getText();
		}
		else if (textGroup.getReference() != null) {
			// die Textangabe ist eine Referenz in das Paket						
			if (textGroup.getReference().getBinaryPointer() != null) {
				// binaer
				
				// es kann angegeben werden, ob die Daten, auf die der Zeiger zeigt, als String oder Integer interpretiert werden sollen
				if (textGroup.getReference().getBinaryPointer().getLocation().getType() == BinaryPointerTypeTypeType.STRING_TYPE) {
					byte feld [] = PluginStructureUtils.getByteArrayFromBinaryPointer (textGroup.getReference().getBinaryPointer(), packet);
					text = Utils.byteToString(Utils.getBitIndicatedPartOfByteArray(feld, offset, feld.length * 8 - offset));
				}
				else {
					byte feld [] = PluginStructureUtils.getByteArrayFromBinaryPointer (textGroup.getReference().getBinaryPointer(), packet);
					text = "" + Utils.byteToInt(Utils.getBitIndicatedPartOfByteArray(feld, offset, feld.length * 8 - offset));
				}
			}
			else if (textGroup.getReference().getTextualPointer() != null){			
				// textuell
				text = PluginStructureUtils.getStringFromTextualPointer (textGroup.getReference().getTextualPointer(), packet);	
				
				// TODO hier muss der offset noch beruecksichtigt werden. Aber wie wird er bei TextualPointern interpretiert?
						
			}
			else {
				System.out.println("error in client.classify.PluginStructureUtils.getStringFromTextGroup: reference for text does not include binaryPointer or textualPointer.");
			}
		}
		
		return text;
	}
	
	
	public static InetAddress getInetAddressFromComputer(Computer computer, Packet packet, int offset) {
		InetAddress address =  null;
		
		if (computer.getBinaryPointer() != null ) {
			// binaer
			try {
				byte [] feld = PluginStructureUtils.getByteArrayFromBinaryPointer(computer.getBinaryPointer(), packet);	// TODO +1 ?
				address = InetAddress.getByAddress(Utils.getBitIndicatedPartOfByteArray(feld, offset, feld.length * 8 - offset));
			} catch (UnknownHostException e) {
				// kann eigentlich nicht auftreten
				e.printStackTrace();
			}
		}
		else if (computer.getTextualPointer() != null){			
			// textuell
			try {
				address = InetAddress.getByName(PluginStructureUtils.getStringFromTextualPointer(computer.getTextualPointer(), packet));
				
				// TODO hier muss noch der offset eingebunden werden. Aber wie wird der bei TextualPointern interpretiert?
				
			} catch (UnknownHostException e) {
				// kann eigentlich nicht auftreten
				e.printStackTrace();
			}
		}
		else {
			System.out.println("error in client.classify.PluginStructureUtils.getInetAddressFromComputer: computer-object does not include binaryPointer oder textualPointer.");			
		}
		
		return address;
	}
	
	
	
	// ------------ Methoden zum Extrahieren der Byte-Felder, die durch Pointer angegeben werden,  aus einem Paket
	
	public static byte [] getByteArrayFromBinaryPointer (BinaryPointer bpe, Packet packet) {
		byte [] array = null;
		
		byte [] locationArray = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, bpe.getLocation().getType());
		//* DEBUG */ System.out.println("locationArray.length: " + locationArray.length);
		
		if (bpe.hasLength() && bpe.hasStart()) {
			array = Utils.getBitIndicatedPartOfByteArray(locationArray, bpe.getStart(), bpe.getLength());
		}
		else {
			// Angabe ueber den Namen des Feldes
			int start;
			int length;
			
			// "next header" kann wg. Erweiterungsheadern an verschiedenen Stellen liegen. Daher Sonderbehandlung
			if ( (bpe.getLocation().getType() == LocationType.IP_TYPE || bpe.getLocation().getType() == LocationType.IPV6_TYPE) &&  ((IPPacket) packet).getVersion() == 6 && bpe.getName().equals("next header")) {
				//* DEBUG */ System.out.println("PluginStructureUtils: Sonderbehandlung fuer name=\"next header\"");
				start = ((IPPacket) packet).getNextHeaderPos();
				length = fields.getLengthFromName(bpe.getName(), LocationType.IPV6);
			}
			// wenn die Location-Angabe "ip" ist, muss erstmal die Version herausgefunden werden
			else if ( (bpe.getLocation().getType() == LocationType.IP_TYPE || bpe.getLocation().getType() == LocationType.IPV6_TYPE) &&  ((IPPacket) packet).getVersion() == 6) {
				//* DEBUG */ System.out.println("name = " + bpe.getName() + ", IP version = " + ((IPPacket) packet).getVersion());
				//* DEBUG */ System.out.println("IPv6");
				start = fields.getStartFromName(bpe.getName(), LocationType.IPV6);
				length = fields.getLengthFromName(bpe.getName(), LocationType.IPV6);        		
			}
			else if ( (bpe.getLocation().getType() == LocationType.IP_TYPE || bpe.getLocation().getType() == LocationType.IPV4_TYPE) &&  ((IPPacket) packet).getVersion() == 4) {
				//* DEBUG */ System.out.println("IPv4");
				start = fields.getStartFromName(bpe.getName(), LocationType.IPV4);
				length = fields.getLengthFromName(bpe.getName(), LocationType.IPV4);
			}
			else {
				/* DEBUG */ System.out.println("IP version undefined.");
				start = fields.getStartFromName(bpe.getName(), bpe.getLocation());
				length = fields.getLengthFromName(bpe.getName(), bpe.getLocation());
			}
			
			//* DEBUG */ System.out.println("start = " + start + ", length =  " + length + ", locationArray.length = " + locationArray.length);
						
 			array = Utils.getBitIndicatedPartOfByteArray(locationArray, start, length);
		}
				
		return array;
	}
	
	
	public static byte [] getByteArrayFromBinaryPointerWithValue (BinaryPointerWithValue bpvwe, Packet packet) {
		byte [] array = null;
		
		byte [] locationArray = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, bpvwe.getLocation().getType());
		
		if (bpvwe.hasLength() && bpvwe.hasStart()) {
			array = Utils.getBitIndicatedPartOfByteArray(locationArray, bpvwe.getStart(), bpvwe.getLength());
		}
		else {
			// Angabe ueber den Namen des Feldes
			int start;
			int length;
			
			// "next header" kann wg. Erweiterungsheadern an verschiedenen Stellen liegen. Daher Sonderbehandlung
			if ((bpvwe.getLocation().getType() == LocationType.IP_TYPE || bpvwe.getLocation().getType() == LocationType.IPV6_TYPE) &&  ((IPPacket) packet).getVersion() == 6 && bpvwe.getName().equals("next header")) {
				//* DEBUG */ System.out.println("PluginStructureUtils: Sonderbehandlung fuer name=\"next header\"");				
				start = ((IPPacket) packet).getNextHeaderPos();
				length = fields.getLengthFromName(bpvwe.getName(), LocationType.IPV6);
			}				
			// wenn die Location-Angabe "ip" ist, muss erstmal die Version herausgefunden werden
			else if ((bpvwe.getLocation().getType() == LocationType.IP_TYPE || bpvwe.getLocation().getType() == LocationType.IPV6_TYPE) &&  ((IPPacket) packet).getVersion() == 6) {
				//* DEBUG */ System.out.println("name = " + bpvwe.getName() + ", IP version = " + ((IPPacket) packet).getVersion());
				start = fields.getStartFromName(bpvwe.getName(), LocationType.IPV6);
				length = fields.getLengthFromName(bpvwe.getName(), LocationType.IPV6);        		
			}
			else if ((bpvwe.getLocation().getType() == LocationType.IP_TYPE || bpvwe.getLocation().getType() == LocationType.IPV4_TYPE) &&  ((IPPacket) packet).getVersion() == 4) {
				start = fields.getStartFromName(bpvwe.getName(), LocationType.IPV4);
				length = fields.getLengthFromName(bpvwe.getName(), LocationType.IPV4);
			}
			else {			
				start = fields.getStartFromName(bpvwe.getName(), bpvwe.getLocation());
				length = fields.getLengthFromName(bpvwe.getName(), bpvwe.getLocation());
			}
			
			array = Utils.getBitIndicatedPartOfByteArray(locationArray, start, length);
		}
		
		return array;
	}
	
	
	public static String getStringFromTextualPointer (TextualPointer tpe, Packet packet) {
		String text = "";
		
		byte [] locationArray = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, tpe.getLocation().getType());
		
		if (!(tpe.getRow() == null) && !(tpe.getWord() == null)) {
			//* DEBUG */ System.out.println("row = " + tpe.getRow());
			//* DEBUG */ System.out.println("word = " + tpe.getWord());
						
			text = PluginStructureUtils.getStringFromTextualField(locationArray, tpe.getRow(), tpe.getWord(), tpe.getAdditionalSeperator());
		}
		else {
			// in textuellen Protokollen koennen keine namensbasierten Angaben gemacht werden, da
			// namensbasierte Angaben nur bei IP, TCP und UDP (also den Grundprotokollen) moeglich sind.
		}
		
		return text;
	}
	
	
	public static String getStringFromTextualPointerWithValue (TextualPointerWithValue tpvwe, Packet packet) {
		String text = "";
		
		byte [] locationArray = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, tpvwe.getLocation().getType());
		
		if (!(tpvwe.getRow() == null) && !(tpvwe.getWord() == null)) {			
			text = PluginStructureUtils.getStringFromTextualField(locationArray, tpvwe.getRow(), tpvwe.getWord(), tpvwe.getAdditionalSeperator());
		}
		else {
			// in textuellen Protokollen koennen keine namensbasierten Angaben gemacht werden, da
			// namensbasierte Angaben nur bei IP, TCP und UDP (also den Grundprotokollen) moeglich sind. 
		}
		
		return text;
	}


	/**
	 * @param array
	 * @param identifier
	 * @return
	 */
	/*public static boolean checkIdentifier(Identifier identifier, Packet packet) {
		boolean isOk = true;        

		if (identifier.getBinaryPointerWithValue() != null) {						
			byte [] array = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, identifier.getBinaryPointerWithValue().getLocation().getType());			
			if (identifier.getBinaryPointerWithValue().hasStart() && identifier.getBinaryPointerWithValue().hasLength()) {
				// "start" und "length" existieren --> einfach diese Stelle pruefen				
				isOk = checkBinaryField(array, identifier.getBinaryPointerWithValue().getStart(), identifier.getBinaryPointerWithValue().getLength(), identifier.getBinaryPointerWithValue().getValue());
			}
			else {
				// der Zugriff muss ueber den Namen erfolgen
				int start;
				int length;
				
				// wenn die Location-Angabe "ip" ist, muss erstmal die Version herausgefunden werden
				if (identifier.getBinaryPointerWithValue().getLocation().getType() == LocationType.IP_TYPE &&  ((IPPacket) packet).getVersion() == 6) {
					start = fields.getStartFromName(identifier.getBinaryPointerWithValue().getName(), LocationType.IPV6);
					length = fields.getLengthFromName(identifier.getBinaryPointerWithValue().getName(), LocationType.IPV6);        		
				}
				else if (identifier.getBinaryPointerWithValue().getLocation().getType() == LocationType.IP_TYPE &&  ((IPPacket) packet).getVersion() == 4) {
					start = fields.getStartFromName(identifier.getBinaryPointerWithValue().getName(), LocationType.IPV4);
					length = fields.getLengthFromName(identifier.getBinaryPointerWithValue().getName(), LocationType.IPV4);
				}
				else {							
					start = fields.getStartFromName(identifier.getBinaryPointerWithValue().getName(), identifier.getBinaryPointerWithValue().getLocation());
					length = fields.getLengthFromName(identifier.getBinaryPointerWithValue().getName(), identifier.getBinaryPointerWithValue().getLocation());
				}
				
				isOk = checkBinaryField(array, start, length, identifier.getBinaryPointerWithValue().getValue());
			}
		}
		else if (identifier.getTextualPointerWithValue() != null) {			
			byte [] array = PluginStructureUtils.getLocationByteArrayFromLocationType((IPPacket) packet, identifier.getTextualPointerWithValue().getLocation().getType());						
			if (!(identifier.getTextualPointerWithValue().getRow() == null) && !(identifier.getTextualPointerWithValue().getWord() == null)) {				
				// "start" und "length" existieren --> einfach diese Stelle pruefen
						
				isOk = checkTextualField(array, identifier.getTextualPointerWithValue().getRow(), identifier.getTextualPointerWithValue().getWord(), identifier.getTextualPointerWithValue().getValue(), identifier.getTextualPointerWithValue().getAdditionalSeperator());
			}
			else {
				// in textuellen Protokollen koennen keine namensbasierten Angaben gemacht werden, da
				// namensbasierte Angaben nur bei IP, TCP und UDP (also den Grundprotokollen) moeglich sind.
			}			
		}
		else {
			System.out.println("error in client.classify.PluginStructureUtils.checkIdentifier: identifier-object does not include binaryPointerWithValue oder textualPointerWithValue.");
		}

		return isOk;
	}*/
	
}

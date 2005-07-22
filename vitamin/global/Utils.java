package global;

import java.util.StringTokenizer;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Diese Klasse stellt nicht klassenspezifische Methoden zur Verfuegung, die an mehreren
 * Orten benutzt werden (z.B Cast-Methoden).
 * 
 * Die Klasse entstammt dem Projekt CARL (gforge.stud.inf.fh-brs.de) und wurde nicht
 * von mir alleine geschrieben!
 * Die Methoden zur Datentypumwandlung sind von mir, die IP-Adressen-Validierungsmethoden
 * entstammen der Feder von Moritz Vieth
 *
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 *
 */
public class Utils {

//-------------------------- Methoden zum Umwandeln von Datentypen ----------------------

    /** wandelt Bitstroeme, wie sie in den Argumenten der Kommandos vorkommen in
     * Strings um. Wird z.B. bei LOGIN verwendet, um den Usernamen als String zu
     * erhalten.
     *
     * @param arg
     * @return
     */
    public static String byteToString(byte[] arg) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < arg.length; i++)
            result.append(String.valueOf((char) arg[i]));

        return result.toString().trim();
    }
    
    
    /** schreibt einen String in ein Byte-Array. Die Länge des Arrays
     * richtet sich hierbei nach der Länge des Strings.
     * 
     * @param arg
     * @return
     */
    public static byte[] StringToByteArray(String arg){
    	byte [] array = new byte [arg.length()];    	
    	for (int i = 0; i < array.length; i++){
    		array [i] = (byte) arg.charAt(i);
    	}    	
    	return array;
    }
    

	public static byte[] longToByteArray(long value, int size) {
		byte[] array = new byte[size];
		for (int i = 0; i < size; i++)
			array[i] = (byte) ((value >> (((size - 1) * 8) - (8 * i))) & 255);

		return array;
	}
	
	
	public static long byteArrayToLong(byte [] array) {
		long value = 0;
		
		for (int i = 0; i < array.length; i++) {
			value = value | (((long) array[i]) & 0xFF) << (((array.length - 1) * 8) - (8 * i));
		}
		
		return value;
	}


    /**
     * Wandelt ein Byte-Array in ein int um.
     * @param array das umzuwandelnde Byte-Array.
     * @return int-Wert des Byte-Arrays.
     */
    public static int byteToInt(byte[] array) {
        int a = 0;
        for (int i = 0; i < array.length; i++)
            a = a | (((int) array[i]) & 0xFF) << (((array.length - 1) * 8) - (8 * i));

        return a;
    }


    public static byte [] getPartOfByteArray (byte [] array, int start, int length) {
        byte [] newArray = new byte [length];
        for (int i = 0; i < newArray.length; i++)
            newArray [i] = array [start + i];
        return newArray;
    }



    /** gibt einen Teil eines Byte-Feld zurueck. Der gewuenschte Teil kann bei jedem Bit beginnen,
     * also nicht nur an den Anfaengen der jeweiligen Bytes.
     * HINWEIS: da die Methode ein Byte-Feld zurueckliefert, werden logischerweise x*8 Bits zurueckgeliefert.
     * Ist lengthInBits kein Vielfaches von 8, so wird das erste Byte nach vorne mit Nullen aufgefuellt.
     * Bsp: aus 1101 1100 0000  wurde folgendes Byte-Feld: | 0000 1101 | 1100 0000 |  
     * 
     * @param array
     * @param startBit das Bit, an dem das Feld beginnen soll
     * @param lengthInBits die Laenge des Feldes in Bit
     * @return
     */
     public static byte [] getBitIndicatedPartOfByteArray (byte [] array, int startBit, int lengthInBits) {
       	int startByte = startBit / 8; // / schneidet Nachkommastellen einfach ab. perfekt hierfuer.
		//* DEBUG */ System.out.println("array.length: " + array.length);
		
		//* DEBUG */ System.out.println("startByte: " + startByte);
		//* DEBUG */ System.out.println("startBit: " + startBit + ", startBit % 8: " + startBit % 8);

    	// herausfinden, wieviele Ausgabebytes ich brauche
        int lengthInBytes = lengthInBits / 8;
        // wenn das Startbit nicht am Anfang eines Bytes liegt, brauch ich ein Byte mehr
        // dies gilt allerdings nur, wenn ich fuer das erste Ausagebyte zwei Quellbytes brauche.
        // Dies ist nicht der Fall, wenn die LengthInBit nicht durch 8 teilbar ist, das erste
        // Byte also mit 0..0 aufgfuellt werden muss, und ich dadurch fuer das erste Byte weniger
        // als 7 - startBit Bits benoetige.
        //if (startBit % 8 != 0 && (startBit + lengthInBits % 8) < 8)
        //    lengthInBytes++;
    	// wenn lengthInBits nicht genau durch 8 teilbar ist, muss noch ein Byte hinzugefuegt werden, da / immer abrundet
        if (lengthInBits % 8 != 0)
        	lengthInBytes ++;

        //* DEBUG */ System.out.println("benoetigte Bytes: " + lengthInBytes);

		byte [] newArray = new byte [lengthInBytes];

		if ((startBit % 8) + lengthInBits < 8) {
			// trivialer Fall:
            // alle zu extrahierenden Bits liegen im selben Byte
			//System.out.println("Alles im selben Byte.");

			int startBitInTheByte = startBit % 8; // Stelle in dem Bit, ab der gestartet wird
			int endBitInTheByte = startBitInTheByte + lengthInBits - 1;
			//System.out.println("startBitInTheByte: " + startBitInTheByte);
			//System.out.println("endBitInTheByte: " + endBitInTheByte);
			newArray [0] = (byte) (getPartOfByte (array [startByte], startBitInTheByte, endBitInTheByte)); // (8 - (endBitInTheByte - startBitInTheByte) - 2));
			//System.out.println("Wert in Quellfeld: " + Utils.byteToInt(Utils.getPartOfByteArray(array, 0, 1)));
			//System.out.println("Wert vor Shiften: " + Utils.byteToInt(newArray));
			//System.out.println("Shiften um: " + (8 - lengthInBits - startBitInTheByte));
			newArray[0] = (byte) (newArray[0]  >>> (8 - lengthInBits - startBitInTheByte));
			
			// jetzt noch die aufgefuellten 1 entfernen (durch &)
			int negierer = 0;
			for (int i = 0; i < lengthInBits - startBitInTheByte; i++)
				negierer += Math.pow(2, i);				
			newArray[0] = (byte) (newArray[0] & negierer);
			
			//System.out.println("Wert nach Shiften: " + Utils.byteToInt(newArray));
		}
    	else {
            // Verteilung ueber mehrere Bytes im Feld (startBit + lengthInBits > 8)
            //System.out.println("Verteilt auf verschiedene Bytes.");

            // das erste Ausgabebyte wird evtl. nicht komplett mit Bits aus dem Quellfeld
            // aufgefuellt. Dies ist der Fall, wenn die Laenge nicht durch 8 teilbar ist.
            // Das erste Byte muss dann nach vorne mit 0...0 aufgefuellt werden.

            int lengthOfFirstByte = lengthInBits % 8;
            int startBitOfTheFollowing = (startBit + lengthOfFirstByte) % 8;
            int startByteOfTheFollowing = startByte + 1;
            int start = 0;
            
            //* DEBUG */ System.out.println("lengthOfFirstByte: " + lengthOfFirstByte + ", startBitOfTheFollowing: " + startBitOfTheFollowing);

            // die Sonderbehandlung fuer das erste Byte gibt's aber nur, wenn die
            // LengthInBits nicht genau durch 8 teilbar ist.
            if (lengthOfFirstByte != 0) {
                //* DEBUG */ System.out.println("Das erste Byte bekommt eine Sonderbehandlung.");
                start = 1;;

                //System.out.println("startBitOfTheFollowing: " + startBitOfTheFollowing);

                // es kann sein, dass das erste Ausgabebyte sich nur aus dem ersten
                // Quell-Byte zusammensetzt.
                if (startBit%8 + lengthOfFirstByte < 8) {
                    //System.out.println("Erstes Byte wird aus nur einem Byte gespeist.");
                    int startBitInTheByte = startBit % 8; // Stelle in dem Byte, ab der gestartet wird
                    int endBitInTheByte = startBitInTheByte + lengthOfFirstByte - 1;

                    //System.out.println("startBitInTheByte = " + startBitInTheByte);
                    //System.out.println("endBitInTheByte = " + endBitInTheByte);

                    newArray [0] = (byte) (getPartOfByte (array [startByte], startBitInTheByte, endBitInTheByte) >> (8 - lengthOfFirstByte - startBitInTheByte));

                    // das zweite Zielbyte wird jetzt noch aus dem ersten Quellbyte gespeist
                    startByteOfTheFollowing--;

                }
                else {
                    // hole die Bits von startBit bis Ende des Bytes aus dem 1. Byte
                    byte firstPart =  getPartOfByte(array[startByte], startBit, 7);

                    // hole von 0 an soviele Bits aus dem 2. Byte, dass es insgesamt lengthOfFirstByte Bits sind
                    byte secondPart = getPartOfByte(array[startByte+ 1], 0, (startBit + lengthOfFirstByte) % 8);

                    newArray [0] = (byte)  ((firstPart << (startBit)) + (secondPart >> (7 - startBit + 1)));
                }

                //System.out.println("array[0] = " + byteToInt(new byte [] {newArray[0]}));
            }

            //System.out.println("startByteOfTheFollowing: " + startByteOfTheFollowing);

            // Diese Schleife iteriert ueber alle folgenden Ausgabebytes und fuellt diese nach und nach auf.
            for (int i = start; i < lengthInBytes; i++) {
                // newArray [i] auffuellen. Dazu benoetige ich 2 Quellbytes.

                // hole die Bits von startBit bis Ende des Bytes aus dem i. Byte
                byte firstPart =  getPartOfByte(array[startByteOfTheFollowing + i - 1], startBitOfTheFollowing, 7);

                //* DEBUG */ System.out.println("startBitOfTheFollowing: " + startBitOfTheFollowing);
                
				byte secondPart = 0;
                // wenn startBitOfTheFollowing 0 ist, brauch ich keinen zweiten Teil
                if (startBitOfTheFollowing != 0)
                	// hole die Bits von 0 bis startBit - 1 aus dem (i+1). Byte                
                	secondPart = getPartOfByte(array[startByteOfTheFollowing + i], 0, startBitOfTheFollowing - 1);

                //* DEBUG */ System.out.println("firstPart: " + firstPart);
                //* DEBUG */ System.out.println("secondPart: " + secondPart);

                //System.out.println(((firstPart << (startBitOfTheFollowing)) + (secondPart >> (7 - startBitOfTheFollowing + 1))));
                newArray [i] = (byte)  ((firstPart << (startBitOfTheFollowing)) + (secondPart >> (7 - startBitOfTheFollowing + 1)));

                //System.out.println("array[" + i +  "] = " + byteToInt(new byte [] {newArray[i]}));
            }
        }

        return newArray;
    }


    private static byte getPartOfByte (byte byteToWorkOn, int startBit, int endBit) {
    	byte value = 0;

		// Hier wird jetzt eine Maske erstellt, mit der nur die gewuenschten Bits aus dem Byte extrahiert werden.
		// Dazu wird ein byte aufgebaut, welches von startBit bis endBit 1 ist und sonst 0. Durch eine &-Verknuepfung
		// bleiben in dem array[x] dann nur die gewollten Bits stehen.
		int mask = 0;
		for (int i = startBit; i <= endBit; i++) {
			mask += Math.pow(2, 8 - i - 1);
            //System.out.println(8 - i - 1);
		}

		//System.out.println("mask: " + mask);

		// Hier wird die angesprochene &-Verknuepfung durchgefuehrt.
		value = ((byte) (byteToWorkOn & mask));

    	return value;
    }


	/**
	 * @param array1
	 * @param array2
	 */
	public static byte [] joinByteArrays(byte[] array1, byte[] array2) {
		byte [] newArray = new byte [array1.length + array2.length];
		
		for (int i = 0; i < array1.length; i++) {
			newArray [i] = array1[i];
		}
		
		for (int i = array1.length; i < array1.length + array2.length; i++) {
			newArray[i] = array2[i - array1.length];
		}
		
		return newArray;		
	}



    /** Wandelt einen int-Wert in ein Byte-Array der angegebenen Laenge um.
     * HINWEIS: achtet nicht darauf, ob der int-Wert zu gross ist! Schneidet ggf.
     * die vorderen Bytes ab.
     *
     * @param value der umzuwandelnde Wert
     * @param size die Groesse des Arrays, das zurueckgeliefert wird
     * @return ein Byte-Array der Groesse size, das, verteilt auf die einzelnen Felder, den Wert value enthaelt
     */
    public static byte[] intToByte(int value, int size) {
        byte[] array = new byte[size];
        for (int i = 0; i < size; i++)
            array[i] = (byte) ((value >> (((size - 1) * 8) - (8 * i))) & 255);

        return array;
    }


//------------------------- Methoden zur Pruefung von IP-Adressen -----------------------

    public static boolean isValidAddress(String ip) {
        return (isValidIPv4Address(ip) || isValidIPv6Address(ip));
    }


    public static boolean isValidIPv4Address(String ip) {
        StringTokenizer st = new StringTokenizer(ip, ".");
        if (st.countTokens() != 4) return false;

        int value;

        while (st.hasMoreTokens()) {
            try {
                value = Integer.parseInt(st.nextToken());
            } catch (NumberFormatException e) {
                return false;
            }
            if (value < 0 || value > 255) return false;
        }

        return true;
    }


    public static boolean isValidIPv6Address(String ip) {
        StringTokenizer st = new StringTokenizer(ip, ":");
        if (st.countTokens() >= 8) return false;
        String token;
        char[] validChars = {};
        char c;
        while (st.hasMoreTokens()) {
            try {
                token = st.nextToken();
	            //TODO ueberpruefen, ob Token zu lang (>2) (Mo)
	            
                for (int i = 0; i < st.nextToken().length(); i++) {
                    c = token.charAt(i);
                    if (c != '0' && c != '1' && c != '2' && c != '3'
                            && c != '4' && c != '5' && c != '6' && c != '7'
                            && c != '8' && c != '9' && c != '0' && c != 'A'
                            && c != 'B' && c != 'C' && c != 'D' && c != 'E'
                            && c != 'F' && c != 'a' && c != 'b' && c != 'c'
                            && c != 'd' && c != 'e' && c != 'f' )
                        return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
    
    
    
    /** baut aus einer IPv4-Adresse, dargestellt als byte-Feld, eine IPv4-mapped-IPv6-Adresse.
     * 
     * @param ipv4
     * @return
     */
    public static byte [] ipv4ToIpv6ByteArray (byte [] ipv4) {
		byte [] temp = new byte [16];
			
		// die ersten 10 Bytes mit führenden Nullen auffüllen
		for (int i = 0; i < 10; i++){
			temp[i] = 0;				
		}
		
		// dann zwei Byte Einsen
		temp [10] = (byte) 255;
		temp [11] = (byte) 255;
			
		// und in die letzten 32 Bit die IPv4-Adresse schreiben
		temp[12] = ipv4[0];
		temp[13] = ipv4[1];
		temp[14] = ipv4[2];
		temp[15] = ipv4[3];
		
		return temp;
    }


    public static InetAddress stringToInetAddress(String ipAsString) {
        // TODO diese Methode ist eigentlich voelliger Quatsch, oder?

        InetAddress address = null;
        byte [] byteAddress;

        if (isValidIPv4Address(ipAsString)) {
            // IPv4-Adress-String in InetAddress umwandeln
            byteAddress = new byte [4];

            for (int i = 0; i < byteAddress.length; i++) {
                // Substring bis zum nächsten Punkte extrahieren
                int punktIndex = ipAsString.indexOf('.');
                String value = ipAsString.substring(0, punktIndex - 1);

                // der String wird verkürzt (der vordere, gerade extrahierte Wert wird abgeschnitten)
                ipAsString = ipAsString.substring(punktIndex);

                byteAddress [i] = Byte.parseByte(value);
            }
        }
        else {
            // IPv6-Adresse
            byteAddress = new byte [16];

            for (int i = 0; i < byteAddress.length; i++) {
                // Substring bis zum nächsten Doppelpunkt extrahieren
                int doppelPunktIndex = ipAsString.indexOf(':');
                String value = ipAsString.substring(0, doppelPunktIndex -1);

                // der String wird verkürzt (der vordere, gerade extrahierte Wert wird abgeschnitten)
                ipAsString = ipAsString.substring(doppelPunktIndex);

                byteAddress [i] = Byte.parseByte(value, 16);
            }
        }

        try {
            address = InetAddress.getByAddress(byteAddress);
        } catch (UnknownHostException e) {
            System.out.println("error in global.Utils: UnknownHostException in method stringToInetAddress(String ipAsString).");
        }

        return address;
    }
    
    
	/** liefert zu einer IP und einer Subnetzmaske die IP des Subnetzes
	 * @param ip
	 * @param subnetMask subnetMask given as integer (prefix syntax)
	 * @return
	 */
	public static String getNetIP(String ip, int subnetMask) {
		//* DEBUG */ System.out.println("IP: " + ip);
		//* DEBUG */ System.out.println("Subnetmask: " + subnetMask);
		
		String subnetIP = "";
	
		// IP in Byte-Array umwandeln
		byte [] addressAsByte = ipAsStringToByteArray(ip);		
		///* DEBUG */ System.out.println("Laenge des IP-Feldes: " + addressAsByte.length);
		
		/*System.out.print("IP als Byte Array: ");		
		for (int i = 0; i < addressAsByte.length; i++) {
			System.out.print(addressAsByte[i]);
			System.out.print(" ");		
		}
		System.out.println("");*/

		// Subnetzmaske in Byte-Array umwandeln
		/*byte [] maskAsByte = ipAsStringToByteArray(subnetMask);*/
		
		/*System.out.print("Mask als Byte Array: ");
		for (int i = 0; i < maskAsByte.length; i++) {
			System.out.print(maskAsByte[i]);
			System.out.print(" ");		
		}
		System.out.println("");*/	
		
		// ich hab jetzt die IP als Byte-Array und die Subnetzmaske als int
		byte [] subnetIPAsByte = new byte [addressAsByte.length];
		
		// die ersten SUBNETMASK / 8 Bytes koennen aus der IP uebernommen werden
		int fullBytes = subnetMask / 8;
		for (int i = 0; i < fullBytes; i++) {
			subnetIPAsByte[i] = addressAsByte[i]; 
		}
		
		// das (SUBNETMASK / 8) + 1 'te Byte muss nur teilweise uebernommen werden
		int numberOfBits = subnetMask % 8; // soviele Bits dieses Bytes (von links an) muessen uebernommen werden
				
		// aus diesem Wert bau ich mir jetzt ein byte, dass ich mit dem Wert in der IP ver-&-en kann
		byte theByte = 0;
		for (int i = 0; i < numberOfBits; i++) {
			theByte += (int) Math.pow(2, 7 - i);
		}		
		subnetIPAsByte[fullBytes] = (byte) (addressAsByte[fullBytes] & theByte);
		
		// alle Bytes danach werden 0
		for (int i = fullBytes + 1; i < subnetIPAsByte.length; i++) {
			subnetIPAsByte[i] = 0;
		}
		
		
		// Jetzt hab ich Subnetzmaske und IP als Byte-Array vorliegen.
		// Jetzt wird wiederum ein neues Byte-Array angelegt, in das die Werte der IP uebernommen werden, falls
		// an derselben Stelle in der Subnetzmaske 1 steht. Sonst kommt da 0 rein.
		/*byte [] subnetIPAsByte = new byte [addressAsByte.length];
	
		for (int i = 0; i < subnetIPAsByte.length; i++) {
			if (maskAsByte [i] == 1)
				subnetIPAsByte [i] = addressAsByte [i];
			else
				subnetIPAsByte [i] = 0;
		}
		
		/*System.out.print("NetIP als Byte Array: ");
		for (int i = 0; i < subnetIPAsByte.length; i++) {
			System.out.print(subnetIPAsByte[i]);
			System.out.print(" ");		
		}
		System.out.println("");*/

		// und daraus mach ich jetzt wieder einen String
		if (subnetIPAsByte.length == 4) {
			// IPv4		
			for (int i = 0; i < subnetIPAsByte.length; i++) {
				// jedes Byte in einen String umwandeln und "." dazwischen
				subnetIP += Utils.byteToInt(new byte [] {subnetIPAsByte[i]});
				if (i < subnetIPAsByte.length - 1)
					subnetIP += ".";
			}			
		}
		else {
			// IPv6
			for (int i = 0; i < subnetIPAsByte.length; i += 2) {
				// dieses Byte und das folgende zu einem Hex-String zusammenbauen
				
				// dafuer erst mal 
				int value = Utils.byteToInt(new byte [] {subnetIPAsByte[i], subnetIPAsByte[i+1]});				
				subnetIP += Integer.toString(value, 16);
				
				if (i < subnetIPAsByte.length - 2)
					subnetIP += ":";
			}
		}
		
		//* DEBUG */ System.out.println("DEBUG in getNetIp: will return |" + subnetIP + "|");
		
		return subnetIP;
	}
	
	
		/** 
		 *  
		 * @param ip
		 * @return
		 */
	private static byte [] ipAsStringToByteArray (String ip) {
		int length;
		StringTokenizer ipTokenizer;
			
		// IPv4 oder IPv6?
		if (ip.indexOf(".") != -1) {
			// IPv4
			length = 4;
			ipTokenizer = new StringTokenizer(ip, ".");
		}
		else {
			length = 16;
			ipTokenizer = new StringTokenizer(ip, ":");
		}
				
		byte [] array = new byte [length];
			
		for (int i = 0; i < array.length; i++) {
			//System.out.println(i);
			if (length == 4) {
				array [i] = (byte) Integer.parseInt(ipTokenizer.nextToken(), 10); // Dezimal
			}
			else {
				// bei IPv6 muss ein Token in zwei Bytes
				int value =  Integer.parseInt(ipTokenizer.nextToken(), 16); // Hex
				byte [] valueArray = Utils.intToByte(value, 2); // kann ja nicht groesser als 2 Bytes werden
				array[i] = valueArray[0];
				array[i +1] = valueArray[1];
						
				i++;
			}
		}
			
		return array;
				
		// es folgt voelliger Schwachsinn:
			/*byte [] maskAsByte = new byte [length * 8]; // kann ja 32 oder 128 Bit sein (IPv4 bzw. IPv6)		
		
			// der String wird in ein int-Array zerlegt. Jedes Feld des Array erhaelt eine Zahl der Subnetzmaske (z.B: "255.255.255.0" wird zu new Byte [] {255, 255, 255, 0})
			int [] maskAsInt = new int [length];
		
			StringTokenizer maskTokenizer;
			if (maskAsByte.length == 32)
				// IPv4
				maskTokenizer = new StringTokenizer (ip, "."); // gibt dann die einzelnen Teile des Subnetzes zurueck
			else
				// IPv6
				maskTokenizer = new StringTokenizer (ip, ":"); // gibt dann die einzelnen Teile des Subnetzes zurueck
		
			for (int i = 0; i < maskAsInt.length; i++) {
				try {
					maskAsInt [i] = Integer.parseInt((String) maskTokenizer.nextElement());				
				}
				catch (NumberFormatException e) {
					// ist keine Dezimalangabe --> IPv6-Adresse --> Hex
					maskAsInt [i] = Integer.parseInt((String) maskTokenizer.nextElement(), 16);
				}
				///* DEBUG  System.out.println("maskAsInt[" + i + "]: " + maskAsInt[i]);
		
				byte[] oneNumber = new byte[8];
				for (int z = 7; z >= 0; z--) {
					oneNumber[7 - z] = (byte) (maskAsInt[i] / (int) Math.pow(2, z));
					maskAsInt[i] = (byte) (maskAsInt[i] % (int) Math.pow(2, z)); 
				}
							
				//byte [] oneNumber = global.Utils.intToByte(maskAsInt[i], 8);
				System.arraycopy(oneNumber, 0, maskAsByte, i * 8, oneNumber.length);
			}
	
			return maskAsByte;*/	
		}
		
		
	public static boolean isSubnetFromSubnet(String subnetIP, int subnetMask, String subnetInSubnetIP, int subnetInSubnetMask) {
		boolean is = false;
		
		// wenn subnetIP = "0.0.0.0" ist, was das Internet symbolisiert, ist das subnetInSubnetIP natuerlich ein Subnetz davon
		if (subnetIP.equals("0.0.0.0")) {
			return true;
		}
		
		// wenn die subnetInSubnetMaske nicht groesser als die subnetMask ist, ist sowieso alles vorbei
		if (!(subnetMask > subnetInSubnetMask)) {		
			// pruefen, ob die ersten subnetMask Bits der beiden IPs gleich sind
			
			// dafuer muessen so viele Bytes gleich sein
			int counter;			
			
			StringTokenizer subnetIpTokenizer;
			StringTokenizer subnetInSubnetIpTokenizer;
			
			// IPv4 oder IPv6?			
			if (subnetIP.indexOf(".") != -1) {
				// IPv4
				counter = subnetMask / 8;
				if (subnetMask % 8 != 0)
					counter++;
								
				subnetIpTokenizer = new StringTokenizer(subnetIP, ".");
				subnetInSubnetIpTokenizer = new StringTokenizer(subnetInSubnetIP, ".");
				
				// die ersten subnetMask / 8 Bytes koennen einfach komplett (d.h. das ganze Byte) verglichen werden
				int fullBytes = subnetMask / 8;
				for (int i = 0; i < fullBytes; i++) {
					if (subnetIpTokenizer.nextToken().equals(subnetInSubnetIpTokenizer.nextToken()))
						counter --; 
				}

				// hier kann der Counter == subnetMask sein, naemlich dann, wenn die subnetMaks genau / 8 teilbar ist und alles gleich war
				// wenn subnetMask / 8 nicht genau teilbar ist, muss noch ein Teil des folgenden Bytes geprueft werden
		
				if (subnetMask % 8 != 0) {				
					// das (subnetMask / 8) + 1 'te Byte muss nur teilweise geprueft werden
					int numberOfBits = subnetMask % 8; // soviele Bits dieses Bytes (von links an) muessen geprueft werden
			
					// aus diesem Wert bau ich mir jetzt ein byte, dass ich mit dem Wert in der IP ver-&-en kann
					byte theByte = 0;
					for (int i = 0; i < numberOfBits; i++) {
						theByte += (int) Math.pow(2, 7 - i);
					}		
					
					if ( (Integer.parseInt(subnetIpTokenizer.nextToken()) & theByte) == (Integer.parseInt(subnetInSubnetIpTokenizer.nextToken()) & theByte) )					
						counter --;
				}
				
			}
			else {
				// IPv6				
				counter = subnetMask / 16;
				if (subnetMask % 16 != 0)
					counter++;
				
				subnetIpTokenizer = new StringTokenizer(subnetIP, ":");
				subnetInSubnetIpTokenizer = new StringTokenizer(subnetInSubnetIP, ":");
				
				// hier entspricht ein Token zwei Bytes
				
				// die ersten subnetMask / 16 Bytes koennen einfach komplett (d.h. das ganze Byte) verglichen werden
				int fullDoubleBytes = subnetMask / 16;
				for (int i = 0; i < fullDoubleBytes; i++) {
					if (subnetIpTokenizer.nextToken().equals(subnetInSubnetIpTokenizer.nextToken()))
						counter --; 
				}
				
				// hier kann der Counter == subnetMask sein, naemlich dann, wenn die subnetMaks genau / 8 teilbar ist und alles gleich war
				// wenn subnetMask / 8 nicht genau teilbar ist, muss noch ein Teil des folgenden Bytes geprueft werden
				if (subnetMask % 16 != 0) {				
					// das (subnetMask / 8) + 1 'te Byte muss nur teilweise geprueft werden
					int numberOfBits = subnetMask % 16; // soviele Bits dieses Bytes (von links an) muessen geprueft werden
			
					// aus diesem Wert bau ich mir jetzt ein byte, dass ich mit dem Wert in der IP ver-&-en kann
					byte theByte = 0;
					for (int i = 0; i < numberOfBits; i++) {
						theByte += (int) Math.pow(2, 15 - i);
					}		
					
					if ( (Integer.parseInt(subnetIpTokenizer.nextToken()) & theByte) == (Integer.parseInt(subnetInSubnetIpTokenizer.nextToken()) & theByte) )					
						counter --;
					}
			}
			
			// wenn der Counter komplett heruntergezaehlt wurde, ist alles gleich
			if (counter == 0)
				is = true;			
		}
		
		return is;
				
		//* DEBUG */ System.out.println("DEBUG in isSubnetFromSubnet: subnetIP is " + subnetIP);
		//* DEBUG */ System.out.println("DEBUG in isSubnetFromSubnet: subnetInSubnetIP is " + subnetInSubnetIP);
		/*
		// zaehlen, wieviele Elemente in der subnetIP hinten 0 sind; diese abschneiden
		int counter = 0; // hierdrin wird gespeichert, wie viele Elemente abgeschnitten wurden		
		while (subnetIP.endsWith("0")) {
			int lastSeperator = subnetIP.lastIndexOf(seperator);
			subnetIP = subnetIP.substring(0, lastSeperator);
			counter++;
		}
		
		//* DEBUG / System.out.println("DEBUG in isSubnetFromSubnet: counter = " + counter);
		//* DEBUG / System.out.println("DEBUG in isSubnetFromSubnet: short subnetIP is " + subnetIP);
		
		// in der subnetInSubnetIP genausoviele Elemente hinten abschneiden
		for (int i = 0; i < counter; i++) {
			int lastSeperator = subnetInSubnetIP.lastIndexOf(seperator);
			subnetInSubnetIP = subnetInSubnetIP.substring(0, lastSeperator);
		}
		
		//* DEBUG / System.out.println("DEBUG in isSubnetFromSubnet: short subnetInSubnetIP is " + subnetInSubnetIP);
		
		// pruefen, ob die beiden kastrierten IPs gleich sind
		if (subnetIP.equals(subnetInSubnetIP))
			return true;				
		return false;*/
	}


	public static boolean thisStringArraysAreEqual(String [] one, String [] two) {
		if (one.length != two.length)
			return false;
		else {
			for (int i = 0; i < one.length; i++)
				if (!one[i].equals(two[i]))
					return false;
		}
		
		return true;
	}


	public boolean isNotIn(int x, int [] array) {
		for (int i = 0; i < array.length; i++)
			if (array[i] == x)
				return true;
		return false;
	}


}
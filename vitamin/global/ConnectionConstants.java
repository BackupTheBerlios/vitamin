package global;

/* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class ConnectionConstants {

    // die Ports fuer die Signalisierung
	public static final int clientPort = 2598;
	public static final int serverPort = 2599;	
	
	// der Port, auf dem Server nach Datennachrichten lauscht
	public static final int tcpPort = 2597;

	// die Signalisierungsnachrichtentypen
	public static final String [] signalMessages = {"HELLO", "BYE", "", "START", "STOP"};
	
	// die Nachrichtentypen
	public static final int HELLO = 1;
	public static final int BYE = 2;
	public static final int SETFILTER = 3;
	public static final int START = 4;
	public static final int STOP = 5;
	
	public static final int MESSAGE = 1;
	public static final int CONNECTION = 2;
	public static final int CONNECTION_END = 3;
	public static final int STREAM = 4;
	public static final int STREAM_END = 5;
	public static final int NAME = 6;


	// die Laenge des Nachrichtenheaders (in Byte)
	public static final int TYPE = 1;	
	public static final int HEADERLENGTH = TYPE;
	
	
	// die Laengen der Felder im Datenpaket
	public static final int PROTOCOL = 20;
	public static final int SOURCE = 16;
	public static final int DESTINATION = 16;
	public static final int TIMESTAMP = 8;
	public static final int COMPUTER = SOURCE;
	public static final int TEXT = 20;
	public static final int PORT = TEXT; 
	
	public static final int DATAMESSAGELENGTH = TYPE + SOURCE + DESTINATION + TIMESTAMP + PROTOCOL + TEXT;
}

package slave;

import java.net.InetAddress;
import java.net.UnknownHostException;

import slave.capture.NetworkListener;
import slave.classify.Classifier;
import slave.connection.SignalingConnection;
import slave.connection.DataConnection;
import slave.connection.MessageListenerInterface;
import global.messages.SignalMessage;
import global.Filter;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 05-05-2004
 * 
 * zentrale Klasse des Slaves / Kollektors.
 *
 */
public class Slave implements MessageListenerInterface {
	
	private NetworkListener networkListener;	// hierüber werden Pakete mitgelesen
	private PacketBuffer buffer;	// hier schreibt der NetworkListener die mitgelesenen Pakete rein
	private Classifier classifier;	// ...und der Classifier liest sie wieder raus und klassifiziert sie anhand der Plugins
	private SignalingConnection sConnection;
	private DataConnection dConnection;

	// Ports und IP können bereits im NetworkListener ausgefiltert werden
    // nach Protokollen kann erst nach der Klassifizierung im Classifier gefiltert werden
    private Filter filter;

    // Name des Servers; muss als Parameter übergeben werden
    private String server;
    
    // die MAC-Adresse des Netzwerkinterfaces in textueller Repraesentation
    private String myMAC;
    
    // Devicename
    private String device;
    
    // Gibt an, ob dieser Slave auch Multicast-Nachrichten akzeptieren und an den Master weiterleiten soll
    // HINWEIS: es darf nur 1 Multicast-Slave geben!
    private boolean multicast = false;



	/**
	 * 
	 * @param server die IP-Adresse des Masters, bei dem sich der Slave anmelden soll
	 * @param device das Interface, an das der Slave gebunden wird (z.B. eth0)
	 * @param myMAC die MAC-Adresse des Interfaces, an dass der Slave gebunden wird.
	 * @param localIP gibt an, an welche lokale IP die TCP-Verbindung zum Master gebunden werden soll.
	 */public Slave(String server, String device, /*String myMAC,*/ String localIP, String optional){
        this.server = server;
        this.device = device;
        //this.myMAC = myMAC.toLowerCase();
        //* DEBUG */ System.out.println("myMAC: " + this.myMAC);
        
        if (optional.equalsIgnoreCase("multicast")) {
        	this.multicast = true;
      		System.out.println("This slave is a multicast slave.");
        }

		// Filter erst mal auf nichts setzen. Wird später noch gesetzt
		this.filter = null;
		
		// PacketBuffer anlegen
		this.buffer = new PacketBuffer();

		// ( NetworkListener anlegen (wird jetzt erst beim Starten der Visualisierungsphase angelegt und gestartet) )

        // Connections anlegen (und starten)
        this.sConnection = new SignalingConnection(this, server, localIP);
        this.dConnection = new DataConnection(this, server, localIP);
        this.sConnection.start();

        // Classifier anlegen
		this.classifier = new Classifier(this.buffer, this.dConnection, this.filter, device, this.multicast, this);
        this.classifier.start();
        
        // HELLO an Server senden
        SignalMessage signalMessage = null;
		try {
			signalMessage = new SignalMessage(InetAddress.getLocalHost(), "HELLO", null);
		} catch (UnknownHostException e1) {
			// es gibt keinen localhost? Unsinn!
			e1.printStackTrace();
		}
        this.sConnection.sendSignalMessage(signalMessage);
        
        // shutDownHook anlegen. Dies bewirkt, dass beim Beenden des Programms durch CTRL-C erst dieser
        // Thread gestartet wird. In unserem Fall wird ein BYE an den Master gesendet.
        ExitThread exitThread = new ExitThread(this, this.sConnection);
        Runtime.getRuntime().addShutdownHook(exitThread);
	}


    /** wird aus der Connection aufgerufen, wenn ein Paket
     * angekommen ist.
     *
     * @param message
     */
    public void messageArrived(SignalMessage message){
        
        if (message.getSignal().equals("BYE")) {
			System.out.println("received BYE signal.");
            
            // slave beenden
            this.exitClient();
            
            // quit program
            System.exit(0);
        }
        else if (message.getSignal().equals("START")) {
            // Filter setzen
            this.filter = message.getFilter();
            //this.networkListener.setFilter(this.filter); (wird dem NetworkListener jetzt direkt beim Start uebergeben)
            this.classifier.setFilter(this.filter);

            // mit dem Mitlesen beginnen
            this.startCollecting();
        }
        else if (message.getSignal().equals("STOP")) {    	
			System.out.println("received STOP signal.");
            
            // Mitlesen beenden
            this.endCollecting();
        }
		else if (message.getSignal().equals("HELLO")) {
			//System.out.println("HELLO Signal empfangen.");
			// bewirkt das eigentlich irgendwas?
		}
        else {
			System.out.println("slave.Slave.messageArrived: received signal message with unknown type field.");			
    	}
    }

	
	
	private void startCollecting(){
		// Sammeln im NetworkListener starten (wird jetzt im NetworkListener selber gemacht)
		/*try {
			//this.networkListener.startCollecting();			
		} catch (InvalidFilterException e) {
			// BYE an Server senden, da Client nichts mitlesen kann
			e.printStackTrace();
		} catch (CaptureDeviceOpenException e) {
			// BYE an Server senden, da Client nichts mitlesen kann
			e.printStackTrace();
		} catch (CapturePacketException e) {
			// BYE an Server senden, da Client nichts mitlesen kann
			e.printStackTrace();
		}*/
		
		// NetworkListener anlegen
		try {
			this.networkListener = new NetworkListener(this.device, this.buffer, this.filter, "");
		} catch (Exception e) {
			System.out.println("error in slave.Slave.Slave: could not create NetworkListener. " + e.getMessage() + ". Can't capture packets. Program will exit.");
			e.printStackTrace();
			System.exit(1);
		}	
		
		// NetworkListener-Thread starten, damit dieser mit dem Mitlesen beginnt
		this.networkListener.start();
	}


	private void endCollecting(){
		// natuerlich nur, wenn wir gerade mitlesen (sonst macht's ja keinen Sinn)
		if (this.networkListener != null && this.networkListener.isAlive()){
		
			// Sammeln im NetworkListener beenden
			this.networkListener.stopCollecting();
			
			// NetworkListener-Thread beenden
			try {
				this.networkListener.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/* DEBUG */ System.out.println("NetworkListener beendet.");
		}
	}


    protected void exitClient() {
        // Mitlesen beenden
        this.endCollecting();

        // Classifier beenden
        this.classifier.end();

        // Verbindung zum Server beenden
        // (die DataConnection ist kein Thread und muss daher nicht explizit beendet werden)
        this.sConnection.end();
    }
    
    
    public void printDebug(String message) {
    	System.out.println(message);
    }
    
    
    // implementieren, dass Strg-C abgefangen und die Software vernuenftig beendet wird
    // done --> have a look at the constructor (search "shutDownHook")


	public static void main(String [] args){
		if (args.length == 3){
			new Slave(args[0], args[1], args[2], /*args[3],*/ "no multicast");
		}
		else if (args.length == 4){
			new Slave(args[0], args[1], args[2], args[3]/*, args[4]*/);
		}
		else {
            System.out.println("usage: java slave.Slave <master> <device> <localIP> [multicast]");
            System.exit(1);
        }
        
	}

}

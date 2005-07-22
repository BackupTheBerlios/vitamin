package slave.classify;

import slave.Slave;
import slave.connection.DataConnection;
import global.pluginStructure.Comparators;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import net.sourceforge.jpcap.net.IPPacket;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.TCPPacket;
import net.sourceforge.jpcap.net.UDPPacket;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 *
 * verwaltet die erkannten Streams. Enthält Methoden zur Prüfung, ob ein Paket zu einem bereits
 * erkannten Stream gehört. Außerdem erkennt diese Klasse, wann ein Stream vorbei ist (nämlich
 * genau dann, wenn in einem festgelegten Zeitintervall keine neuen Pakete mehr ankommen) und
 * informiert den Server darüber.
 */
public class StreamManager extends TimerTask {

    // so lange schläft der Thread, bevor er wieder prüft,
    // ob wieder Streams zu Ende sind (in ms)
    private final static int STREAM_CHECK_INTERVAL = 2000;

    // gibt an, nach welcher Zeit ein Stream für tot erklärt wird
    private static final int MAX_STREAM_TIME = 1000;


	// die Verbindung zum Server
	private DataConnection connection;

    // Datenstruktur zum Speichern der aktuellen Datenströme
    private LinkedList streams;
    
    private Slave slave;
    
    private Timer timer;


	// ------------------ Konstruktor ----------------------

	public StreamManager (DataConnection connection, Slave slave) {
		this.slave = slave;
		this.connection = connection;
        this.streams = new LinkedList();
        
        this.timer = new Timer();
        
        timer.scheduleAtFixedRate(this, 0, STREAM_CHECK_INTERVAL);
	}


    /** wird automatisch aufgerufen, wenn der Thread gestartet wird.
     *
     */
    public void run () {                        
            //* DEBUG*/ this.slave.printDebug("StreamManager aufgewacht. Pruefe " + this.streams.size() + " Streams.");
            
            // über alle gespeicherten Streams iterieren und prüfen, ob sie noch aktiv sind
            for (int i = 0; i < this.streams.size(); i++) {
                if (((Stream) this.streams.get(i)).getInactiveTime() > MAX_STREAM_TIME) {
                    // ist der Stream nicht mehr aktiv, wird er gelöscht
                    // und der Server ueber das Ende des Streams benachrichtigt
                    Stream stream = (Stream)this.streams.get(i);
                    this.connection.sendStreamEndMessage(0, stream.getProtocol(), stream.getSourceAddress(), stream.getDestinationAddress(), stream.getPort());
					/* DEBUG */ this.slave.printDebug("StreamManager: StreamEndMessage versendet.");
					///* DEBUG */ this.slave.printDebug("port " + stream.getPort());
                    this.streams.remove(i);
                }
            }
            
			//* DEBUG*/ this.slave.printDebug("StreamManager eingeschlafen.");
    }


	// ------------------ Methoden -------------------------
	
	public void addPacket (Packet packet, String protocol, Comparators comparators){
        boolean streamFound = false;

        // über alle Streams iterieren und prüfen, ob dieses Paket dazugehört
		//* DEBUG */ System.out.println("pruefe " + this.streams.size() + " streams.");
        for (int i = 0; i < this.streams.size(); i++) {
			//* DEBUG */ System.out.println("pruefe Stream " + i + ".");			
            if(((Stream) this.streams.get(i)).compare(packet, protocol)) {
                // wenn es dazu gehört, Stream über neues Paket informieren
                /* DEBUG */ System.out.println("Paket gehoert zu bekanntem Stream " + i + ".");
                ((Stream) this.streams.get(i)).newPacket();
                streamFound = true;
            }
			/* DEBUG */ System.out.println("");
        }

        // wenn das Paket keinem Stream zugeordnet werden konnte,
        // wird ein neuer Stream mit den Komparatoren dieses Paketes angelegt
        // und der Server wird über den neuen Stream informiert
        if (!streamFound && this.streams.size() < 10) { // TODO die Groessenbegrenzung muss wieder raus
        	//* DEBUG */ System.out.println("neuer Stream wird erstellt.");
        	int port = 0;
        	if (packet instanceof UDPPacket) {
        		port = ((UDPPacket) packet).getDestinationPort();
        	}
        	else if (packet instanceof TCPPacket) {
				port = ((TCPPacket) packet).getDestinationPort();
        	}
            try {				
				this.connection.sendStreamMessage(packet.getTimeval().getSeconds() * 1000 + packet.getTimeval().getMicroSeconds(), protocol, InetAddress.getByName(((IPPacket) packet).getSourceAddress()), InetAddress.getByName(((IPPacket) packet).getDestinationAddress()), port);
				/* DEBUG */ System.out.println("StreamManager: StreamMessage versendet.");
				this.streams.add(new Stream( packet, protocol,port, comparators));				
			} catch (UnknownHostException e) {
				// Wenn es die IPs nicht gibt, ist ein Fehler aufgetreten, der eigentlich gar nicht auftreten kann.
				// Denn die IPs, die im Paket drinstehen, muss es ja zwangslauefig geben.
				// Der Stream wird dann auch nicht angelegt.
				System.out.println("fatal error in client.classify.StreamManager: an IP extracted from a captured packet could not be resolved (unknown host exception). That can't be.");				
				e.printStackTrace();
			}
        }
    }


    public void end() {
        this.timer.cancel();
    }
	
}

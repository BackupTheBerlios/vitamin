package master.connection;

import global.messages.ConnectionEndMessage;
import global.messages.ConnectionMessage;
import global.messages.MessageMessage;
import global.messages.NameMessage;
import global.messages.StreamEndMessage;
import global.messages.StreamMessage;

import global.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import master.Master;


/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 28.05.2004
 *
 * Wartet auf Datennachrichten von den Kollektoren, empfaengt diese und leitet sie an das Master-Objekt weiter
 */
public class DataReader extends Thread {
	
	// hierueber wird kommuniziert
	private DatagramSocket socket;

	private Master parent;

	// wenn diese Variable durch die Methode end() auf true gesetzt wird,
	// hoert der Thread mit dem Lesen auf und schliesst den Socket.
	private boolean end = false;
	
	
	public DataReader (Master server, InetAddress serverIP) {
		this.parent = server;
		
		// Socket anlegen und an Port binden
		try {
			/* DEBUG */ System.out.println("Binding socket to port" + global.ConnectionConstants.serverPort + "...");
			this.socket = new DatagramSocket(global.ConnectionConstants.serverPort, serverIP);
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("error in server.connection.DataReader: could not open socket. program will exit.");
			System.exit(1);
		}
		//* DEBUG */ System.out.println("server.Connection started.");
		
		System.out.println("Socket bound to following IP-address: " + this.socket.getLocalAddress().getHostAddress());
	}
	
	
	public void run () {
		// Endlosschleife
		while(!end){
			// Nachricht empfangen
			DatagramPacket packet = this.receive(global.ConnectionConstants.DATAMESSAGELENGTH);
			byte [] message = packet.getData(); 
			int type = message[0];
			
			String protocol = null;
			InetAddress source = null;
			InetAddress destination = null;
			long timestamp = 0;
			String text = null;
			int port = 0;
			
			// 	Unterscheidung nach Typ der Nachricht
			switch (type) {
				// hier muessen die Datennachrichten im Prinzip nur angenommen und an den Server
				// weitergeleitet werden. Dazu bedarf es einer geeigneten Datenstruktur. Diese
				// liegt im Package "global.messages".

				case (byte) global.ConnectionConstants.MESSAGE:
					// message
					protocol = Utils.byteToString(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP, global.ConnectionConstants.PROTOCOL));
					try {
						source = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE,global. ConnectionConstants.SOURCE));
					} catch (UnknownHostException e) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");						
						//e.printStackTrace();
					}									
					try {
						destination = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE, global.ConnectionConstants.DESTINATION));
					} catch (UnknownHostException e1) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e1.printStackTrace();
					}
					timestamp = Utils.byteArrayToLong(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION, global.ConnectionConstants.TIMESTAMP));
					text = Utils.byteToString(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP + global.ConnectionConstants.PROTOCOL, global.ConnectionConstants.TEXT));
					MessageMessage mm = new MessageMessage(packet.getAddress(), timestamp, protocol, source, destination, text);
					this.parent.dataMessageArrived(mm);
					break;

				case (byte) global.ConnectionConstants.CONNECTION:
					// connection established
					protocol = Utils.byteToString(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP, global.ConnectionConstants.PROTOCOL));
					try {
						source = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE, global.ConnectionConstants.SOURCE));
					} catch (UnknownHostException e2) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e2.printStackTrace();
					}					
					try {
						destination = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE, global.ConnectionConstants.DESTINATION));
					} catch (UnknownHostException e3) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e3.printStackTrace();
					}					
					timestamp = Utils.byteArrayToLong(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION, global.ConnectionConstants.TIMESTAMP));
					ConnectionMessage cm = new ConnectionMessage(packet.getAddress(), timestamp, protocol, source, destination);
					this.parent.dataMessageArrived(cm);
					break;

				case (byte) global.ConnectionConstants.CONNECTION_END:
					// connection end
					protocol = Utils.byteToString(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP, global.ConnectionConstants.PROTOCOL));
					try {
						source = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE, global.ConnectionConstants.SOURCE));
					} catch (UnknownHostException e4) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e4.printStackTrace();
					}					
					try {
						destination = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE, global.ConnectionConstants.DESTINATION));
					} catch (UnknownHostException e5) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e5.printStackTrace();
					}					
					timestamp = Utils.byteArrayToLong(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION, global.ConnectionConstants.TIMESTAMP));
					ConnectionEndMessage cem = new ConnectionEndMessage(packet.getAddress(), timestamp, protocol, source, destination);
					this.parent.dataMessageArrived(cem);
					break;

				case (byte) global.ConnectionConstants.STREAM:
					// stream
					protocol = Utils.byteToString(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP, global.ConnectionConstants.PROTOCOL));
					//protocol = String.valueOf(Utils.byteToInt(Utils.getPartOfByteArray(message, ConnectionConstants.TYPE + ConnectionConstants.SOURCE + ConnectionConstants.DESTINATION, ConnectionConstants.PROTOCOL)));
					try {
						source = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE, global.ConnectionConstants.SOURCE));
					} catch (UnknownHostException e6) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e6.printStackTrace();
					}
					try {
						destination = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE, global.ConnectionConstants.DESTINATION));
					} catch (UnknownHostException e7) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e7.printStackTrace();
					}
					timestamp = Utils.byteArrayToLong(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION, global.ConnectionConstants.TIMESTAMP));
					port = Utils.byteToInt(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP + global.ConnectionConstants.PROTOCOL, global.ConnectionConstants.PORT));
					StreamMessage sm = new StreamMessage(packet.getAddress(), timestamp, protocol, source, destination, port);
					this.parent.dataMessageArrived(sm);
					break;

				case (byte) global.ConnectionConstants.STREAM_END:
					// stream end
					protocol = Utils.byteToString(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP, global.ConnectionConstants.PROTOCOL));
					try {
						source = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE, global.ConnectionConstants.SOURCE));
					} catch (UnknownHostException e8) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e8.printStackTrace();
					}
					try {
						destination = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE, global.ConnectionConstants.DESTINATION));
					} catch (UnknownHostException e9) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e9.printStackTrace();
					}
					timestamp = Utils.byteArrayToLong(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION, global.ConnectionConstants.TIMESTAMP));
					port = Utils.byteToInt(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP + global.ConnectionConstants.PROTOCOL, global.ConnectionConstants.PORT));
					//* DEBUG */ System.out.println("port: " + port);
					StreamEndMessage sem = new StreamEndMessage(packet.getAddress(), timestamp, protocol, source, destination, port);
					this.parent.dataMessageArrived(sem);
					break;

				case (byte) global.ConnectionConstants.NAME:
					// name
					protocol = Utils.byteToString(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP, global.ConnectionConstants.PROTOCOL));
					InetAddress computer = null;;
					try {
						computer = InetAddress.getByAddress(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE, global.ConnectionConstants.SOURCE));
					} catch (UnknownHostException e10) {
						System.out.println("error in server.connection.DataReader: received IP address is unknown.");
						//e10.printStackTrace();
					};
					timestamp = Utils.byteArrayToLong(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION, global.ConnectionConstants.TIMESTAMP));
					text = Utils.byteToString(Utils.getPartOfByteArray(message, global.ConnectionConstants.TYPE + global.ConnectionConstants.SOURCE + global.ConnectionConstants.DESTINATION + global.ConnectionConstants.TIMESTAMP + global.ConnectionConstants.PROTOCOL, global.ConnectionConstants.TEXT));
					NameMessage nm = new NameMessage(packet.getAddress(), timestamp, computer, protocol, text);
					this.parent.dataMessageArrived(nm);
					break;
								
				// wenn dieser Fall eintrifft, ist es ein Fehler
				default:
					System.out.println("bad behaviour in server.connection.DataReader: messages does not begin with valid type field (received type: " + type + ").");
					break;
			}
		}
		
		// Diese Stelle wird nur erreicht, wenn die end-Variable durch die end-Methode auf true gesetzt wurde.
		// Der Socket wird nun geschlossen. Anschliessend ist der Thread zu Ende.
		this.socket.close();
	}
	
	
	public void end () {
		this.end = true;
	}
	
	
	// --------------------- private Hilfsmethoden --------------------------
	
	
	/** empfaengt ein Paket der angegebenen Laenge
	 * 
	 */
	private DatagramPacket receive(int length) {
		byte [] array = new byte [length];
		DatagramPacket packet = new DatagramPacket (array, array.length);
		try {
			this.socket.receive(packet);
		} catch (IOException e) {
			System.out.println("error in server.connection.Connection: could not receive IP-address-field.");
		}
		return packet;
	}

}

package slave.connection;

//import global.*;
import global.Filter;
import global.messages.SignalMessage;
import global.Utils;
import global.ConnectionConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import slave.Slave;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 28.05.2004
 *
 */
public class SignalingConnection extends Thread {

	private Socket socket;
	private InetAddress server;
	
	private InputStream iStream;
	private OutputStream oStream;

	private Slave parent;
	
	private boolean end = false;


	public SignalingConnection(Slave client, String server, String localIP) {
		this.parent = client;
		
		InetAddress localIPAddress = null;
		
		try {
			this.server = InetAddress.getByName(server);
			localIPAddress = InetAddress.getByName(localIP);
		} catch (UnknownHostException e) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: given master address is unknown.");
			System.out.println("CAN'T CONNECT TO MASTER. PROGRAM WILL EXIT!");
			System.exit(1);			
			// e.printStackTrace();
		}	
		
		System.out.println("Try to connect to " + this.server.getHostAddress() + "...");
		
		try {
			this.socket = new Socket(server, ConnectionConstants.tcpPort, localIPAddress, 3333); // remote IP, remote port, local IP, local port
		} catch (UnknownHostException e1) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: UnknownHostException while binding socket.");
			System.out.println("CAN'T CONNECT TO MASTER. PROGRAM WILL EXIT!");
			System.exit(1);
			//e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: IOException while binding socket.");
			System.out.println("CAN'T CONNECT TO MASTER. PROGRAM WILL EXIT!");
			System.exit(1);
			//e1.printStackTrace();
		}
		
		try {
			this.iStream = this.socket.getInputStream();
		} catch (IOException e2) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: IOException while getting InputStream.");
			System.out.println("CAN'T COMMUNICATE WITH MASTER. PROGRAM WILL EXIT!");
			System.exit(1);
			// e2.printStackTrace();
		}
		
		try {
			this.oStream = this.socket.getOutputStream();
		} catch (IOException e3) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: IOException while getting OutputStream.");
			System.out.println("CAN'T COMMUNICATE WITH MASTER. PROGRAM WILL EXIT!");
			System.exit(1);
			// e3.printStackTrace();
		}
		
		System.out.println("Connection to master established.");
	}
	
	
	public void run () {
		// Endlosschleife
		while(!end){
			//* DEBUG */ System.out.println("will type empfangen.");
			
			// Typ der Nachricht empfangen
			int type = global.Utils.byteToInt(this.read(ConnectionConstants.TYPE)); 
			
			switch (type) {
				// bei allen Nachrichten außer START hängen keine Daten dran
				// --> Nachricht ist schon komplett empfangen
				case ConnectionConstants.HELLO:
				case ConnectionConstants.BYE:
				case ConnectionConstants.STOP:               
					//* DEBUG */ System.out.println("Hab was empfangen.");					
				
					 // an den Client wird ein entsprechendes Message-Objekt weitergeleitet
					 SignalMessage message = new SignalMessage(this.server, ConnectionConstants.signalMessages[type-1], null);
					 this.parent.messageArrived(message);
					 break;
	
				// START
				case ConnectionConstants.START:
					/* DEBUG */ System.out.println("got START signal with following filter:");            	
		            
					Filter filter = new Filter();
	
					int count;
	
					// source ports empfangen
					count = Utils.byteToInt(this.read(2)); // Length-field empfangen
					/* DEBUG */ System.out.println("#source ports: " + count);
					for(int i = 0; i < count; i++) {
						int number = Utils.byteToInt(this.read(2)); // einen Port empfangen
						filter.addPort(number, "source");
						/* DEBUG */ System.out.println("source port: " + number);
					}
	
					// destination ports empfangen
					count = Utils.byteToInt(this.read(2)); // Length-field empfangen
					/* DEBUG */ System.out.println("#destination ports: " + count);
					for(int i = 0; i < count; i++) {
						int number = Utils.byteToInt(this.read(2)); // einen Port empfangen
						filter.addPort(number, "destination");
						/* DEBUG */ System.out.println("destination port: " + number);
					}
	
					// source IPs empfangen
					count = Utils.byteToInt(this.read(2)); // Length-field empfangen
					/* DEBUG */ System.out.println("#source IPs: " + count);
					for(int i = 0; i < count; i++) {
						InetAddress ip = null;
						try {
							ip = InetAddress.getByAddress(this.read(16));
							filter.addIP(ip, "source");
							/* DEBUG */ System.out.println("source IP: " + ip.getHostAddress());
						} catch (UnknownHostException e) {
							System.out.println("error in client.connection.Connection: received IP is an unknown host. IP will not be added to the filter.");
						}
					}
	
					// destination IPs empfangen
					count = Utils.byteToInt(this.read(2)); // Length-field empfangen
					/* DEBUG */ System.out.println("#destination IPs: " + count);
					for(int i = 0; i < count; i++) {
						InetAddress ip = null;
						try {
							ip = InetAddress.getByAddress(this.read(16));
							filter.addIP(ip, "destination");
							/* DEBUG */ System.out.println("destination IP: " + ip.getHostAddress());
						} catch (UnknownHostException e) {
							System.out.println("error in client.connection.Connection: received IP is an unknown host. IP will not be added to the filter.");
						}
					}
	
					// protocols empfangen
					count = Utils.byteToInt(this.read(2)); // Length-field empfangen
					/* DEBUG */ System.out.println("#protocols: " + count);
					for (int i = 0; i < count; i++) {
						// Laenge des Protokolls empfangen
						int length = Utils.byteToInt(this.read(1));
	
						// Name des Protokolls empfangen
						String protocol = Utils.byteToString(this.read(length));
						/* DEBUG */ System.out.println("protocol: " + protocol);
						filter.addProtocol(protocol, null);
					}
	
					// im Client signalisieren, dass eine Message angekommen ist
					SignalMessage sm = new SignalMessage(this.server, "START", filter);
					this.parent.messageArrived(sm);
	
					break;
		
				// wenn dieser Fall eintrifft, ist es ein Fehler
				default:
					System.out.println("bad behaviour in client.connection.Connection: messages does not begin with valid type field (received type: " + type + ")");
					break;				
			}			
		}
		
		/* DEBUG */ System.out.println("Hoppala, da hat mich ja jemand beendet!");		
		
		// Diese Stelle wird nur erreicht, wenn die end-Variable durch die end-Methode auf true gesetzt wurde.
		// Der Socket wird nun geschlossen. Anschließend ist der Thread zu Ende.
		try {
			this.iStream.close();
			this.oStream.close();
			this.socket.close();
		} catch (IOException e) {
			System.out.println("error in client.comm.SignalingConnection: could not close Connection.");			
			// e.printStackTrace();
		}
	}
	
	
	/** Methode zum Senden von Signalisierungsnachrichten an den Server
	 *
	 */
	public void sendSignalMessage (SignalMessage message){
		// hier kommt die Nachricht rein;
		// Signalisierungsnachrichten bestehen immer nur aus Header
		// (abgesehen von START, aber die wird vom Client nicht verschickt)
		byte [] messageAsByteArray = new byte [ConnectionConstants.HEADERLENGTH];

		// Integertyp der Nachricht herausfinden
		byte type = 0;
		for (int i = 0; i < ConnectionConstants.signalMessages.length; i++) {
			if (ConnectionConstants.signalMessages[i].equals(message.getSignal())){
				type = (byte) (i + 1);
				//* DEBUG */ System.out.println("Typ der Signalnachricht ist: " + ConnectionConstants.signalMessages[i] + " (in Zahlen: " + type + ").");
			}				
		}

		// und als Typ steht der gerade herausgefundene Typ der Nachricht drin
		messageAsByteArray[0] = type;

		// jetzt die Nachricht versenden
		this.write(messageAsByteArray);				                        
	}

		
	/** wird von aussen aufgerufen, wenn der Thread beendet werden soll
	 * 
	 *
	 */
	public void end () {
		this.end = true;	
	}
	

	// ---------------------------- private Hilfsmethoden ---------------------------------
	
	
	private byte [] read(int length) {
		byte [] array = new byte [length];
		
		try {
			this.iStream.read(array, 0, array.length);
		} catch (IOException e) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: IOException while reading from InputStream.");
			//e.printStackTrace();
		}
		
		return array;
	}
	
	
	private void write (byte [] toSend) {
		try {
			this.oStream.write(toSend);
			//* DEBUG */ System.out.println("habe gerade ein lustiges Paket versendet. Das Paket hatte die Laenge " + toSend.length);
		} catch (IOException e) {
			System.out.println("error in client.connection.SignalingConnection: could not write to OutputStream");			
			// e.printStackTrace();
		}
	}

}

package master.connection;

import global.ConnectionConstants;
import global.Filter;
import global.Utils;
import global.messages.SignalMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import master.Master;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 28.05.2004
 *
 * Hierueber wird die Signalisierung mit den Kollektoren abgewickelt. Fuer jeden Kollektor existiert eine Instanz dieser
 * Klasse. Diese Instanzen werden im Server-Objekt verwaltet.
 */
public class SignalingConnection extends Thread {
	
	private Master parent;
	
	private Socket socket;
	private InetAddress collector;
	
	private InputStream iStream;
	private OutputStream oStream;
	
	//	wenn diese Variable durch die Methode end() auf true gesetzt wird,
	// hoert der Thread mit dem Lesen auf und schliesst den Socket.
	private boolean end = false;
	
	
	public SignalingConnection (Master server, Socket socket) {
		this.parent = server;
		
		this.socket = socket;
		this.collector = socket.getInetAddress();
		
		try {
			this.iStream = this.socket.getInputStream();
		} catch (IOException e2) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: IOException while getting InputStream.");
			// e2.printStackTrace();
		}
	
		try {
			this.oStream = this.socket.getOutputStream();
		} catch (IOException e3) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: IOException while getting OutputStream.");
			// e3.printStackTrace();
		}
	}
	
	
	public void run () {
		// Endlosschleife
		while(!end){
			int type = global.Utils.byteToInt(this.read(global.ConnectionConstants.TYPE));
			
			String protocol;
			InetAddress source;
			InetAddress destination;
			String text;

			// abhaengig vom Typ den Rest der Nachricht empfangen und weiterleiten
			switch(type){
				// bei allen Signalisierungsnachrichten, die beim Server eintreffen
				// (nur HELLO und BYE) haengen keine Daten dran
				// --> Nachricht ist schon komplett empfangen
				case ConnectionConstants.BYE:
					end = true;
				case ConnectionConstants.HELLO:				
					// an den Server wird ein Objekt der Klasse SignalMessage weitergeleitet
					SignalMessage message = new SignalMessage(this.collector, global.ConnectionConstants.signalMessages[type-1], null);
					this.parent.signalMessageArrived(message);
					break;
					
				// wenn dieser Fall eintrifft, ist es ein Fehler
				default:
					System.out.println("bad behaviour in server.connection.SignalingConnection: messages does not begin with valid type field (received type: " + type + ").");
					break;
			}
	
		}	
	}
	
	
	/** Methoden zum Senden von Signalisierungsnachrichten an den Server
	 *
	 */
	public void sendSignalMessage (String name, InetAddress client, Filter filter) {
		// hier kommt die Nachricht rein;
		// Signalisierungsnachrichten bestehen immer nur aus Header
		// (abgesehen von setFilter, aber die wird vom Client nicht verschickt)
		byte [] message = new byte [global.ConnectionConstants.HEADERLENGTH];

		// Integertyp der Nachricht herausfinden
		byte type = 0;
		for (int i = 0; i < global.ConnectionConstants.signalMessages.length; i++) {
			if (global.ConnectionConstants.signalMessages[i].equals(name))
				type = (byte) (i + 1);
		}

		// und als Typ steht der gerade herausgefundene Typ der Nachricht drin
		message[0] = type;

		byte [] sourcePorts;
		byte [] destinationPorts;
		byte [] sourceIps;
		byte [] destinationIps;
		byte [] protocols;

		// wenn es eine Start-Nachricht ist, muss noch der Filter mit rein
		if (name.equals("START")) {
			// sourcePort-Feld fuellen
			LinkedList sourcePortsList = filter.getSourcePorts();
			sourcePorts = new byte [2 + 2 * sourcePortsList.size()]; // 2 fuer die Anzahl der Ports und dann je 2 pro Port
			//* DEBUG */ System.out.println("sourcePorts-Laenge: " + sourcePorts.length);
 
			// erstmal das #sourcePorts-Feld fuellen
			System.arraycopy(Utils.intToByte(sourcePortsList.size(), 2), 0, sourcePorts, 0, 2);

			for (int i = 0; i < sourcePortsList.size(); i++) {
				byte [] port = Utils.intToByte(((Integer) sourcePortsList.get(i)).intValue(), 2);
				System.arraycopy(port, 0, sourcePorts, 2 + i*2, port.length);
			}

			// destinationPort-Feld fuellen
			LinkedList destinationPortsList = filter.getDestinationPorts();
			destinationPorts = new byte [2 + 2 * destinationPortsList.size()];
			//* DEBUG */ System.out.println("destinationPorts-Laenge: " + destinationPorts.length);

			// erstmal das #destinationPorts-Feld fuellen
			System.arraycopy(Utils.intToByte(destinationPortsList.size(), 2), 0, destinationPorts, 0, 2);

			for (int i = 0; i < destinationPortsList.size(); i++) {
				byte [] port = Utils.intToByte(((Integer) destinationPortsList.get(i)).intValue(), 2);
				System.arraycopy(port, 0, destinationPorts, 2 + i * 2, port.length);
			}

			// source-IP-Feld fuellen
			LinkedList sourceIpsList = filter.getSourceIps();
			sourceIps = new byte [2 + 16 * sourceIpsList.size()];
			//* DEBUG */ System.out.println("sourceIPs-Laenge: " + sourceIps.length);

			// erstmal das #sourceIP-Feld fuellen
			System.arraycopy(Utils.intToByte(sourceIpsList.size(), 2), 0, sourceIps, 0, 2);

			for (int i = 0; i < sourceIpsList.size(); i++) {
				byte [] ip = ((InetAddress) sourceIpsList.get(i)).getAddress();

				if (ip.length == 4) {
					ip = Utils.ipv4ToIpv6ByteArray(ip);								
				}
					
				System.arraycopy(ip, 0, sourceIps, 2 + i * 16, ip.length);
			}

			// destination-IP-Feld fuellen
			LinkedList destinationIpsList = filter.getDestinationIps();
			destinationIps = new byte [2 + 16 * destinationIpsList.size()];
			//* DEBUG */ System.out.println("destinationIPs-Laenge: " + destinationIps.length);

			// erstmal das #destinationIP-Feld fuellen
			System.arraycopy(Utils.intToByte(destinationIpsList.size(), 2), 0, destinationIps, 0, 2);

			for (int i = 0; i < destinationIpsList.size(); i++) {
				byte [] ip = ((InetAddress) destinationIpsList.get(i)).getAddress();

				if (ip.length == 4)
					ip = Utils.ipv4ToIpv6ByteArray(ip);

				System.arraycopy(ip, 0, destinationIps, 2 + i * 16, ip.length);
			}

			// Protokoll-Feld fuellen
			LinkedList protocolsList = filter.getProtocols();

			// Laenge des Byte-Feldes herausfinden
			int length = 0;
			for (int i = 0; i < protocolsList.size(); i++) {
				length += ((String) protocolsList.get(i)).length();
			}
			protocols = new byte [2 + length + protocolsList.size()]; // 2 fuer die Anzahl der Protokolle, length ist die Gesamtlaenge aller Protokolle, und nochmal size fuer die length protocol-Felder
			//* DEBUG */ System.out.println("protocols-Laenge: " + protocols.length);

			// erstmal das #protocols-Feld fuellen
			System.arraycopy(Utils.intToByte(protocolsList.size(), 2), 0, protocols, 0, 2);

			// und jetzt alle Protokolle in das Feld hereinschreiben
			int position = 2; // bei 2 fangen die eigentlichen Protokolllaenge + Protokoll-Kombis an; davor steht die # aller Protokolle
			for (int i = 0; i < protocolsList.size(); i++) {
				byte [] protocol = Utils.StringToByteArray((String) protocolsList.get(i));

				// protocol-length-Feld fuellen
				protocols[position++] = (byte) ((String) protocolsList.get(i)).length();

				// eigentliches Protokoll fuellen
				System.arraycopy(protocol, 0, protocols, position, protocol.length);
				position += protocol.length;
			}

			// aus allen Feldern ein grosses Feld (die Nachricht) zusammenbauen
			// type + source ports + destination ports + source IPs + destination IPs + protocols
			message = new byte [message.length + sourcePorts.length + destinationPorts.length + sourceIps.length + destinationIps.length + protocols.length];
			message [0] = type;
			position = 1;
			System.arraycopy(sourcePorts, 0, message, position, sourcePorts.length);
			position += sourcePorts.length;
			System.arraycopy(destinationPorts, 0, message, position, destinationPorts.length);
			position += destinationPorts.length;
			System.arraycopy(sourceIps, 0, message, position, sourceIps.length);
			position += sourceIps.length;
			System.arraycopy(destinationIps, 0, message, position, destinationIps.length);
			position += destinationIps.length;
			System.arraycopy(protocols, 0, message, position, protocols.length);
			position += protocols.length;

			//message = nachricht;
			//* DEBUG */ System.out.println("Die START-Nachricht inkl. Filter hat eine Laenge von " + message.length);
		}

		//* DEBUG */ System.out.println("signal message sent: " + type);

		// jetzt die Nachricht versenden
		this.write(message);
	}
	
	
	public void end () {
		this.end = true;
	}
	
	
	public InetAddress getCollector () {
		return this.collector;
	}
	
	
	// ---------------------------- private Hilfsmethoden ---------------------------------
	
	
	private byte [] read(int length) {
		byte [] array = new byte [length];
		
		try {
			this.iStream.read(array, 0, array.length);
		} catch (IOException e) {
			System.out.println("error in server.connection.SignalingConnection.SignalingConnection: IOException while reading from InputStream.");
			//e.printStackTrace();
		}
		
		return array;
	}
	
	
	private void write (byte [] toSend) {
		try {
			this.oStream.write(toSend);
			//* DEBUG */ System.out.println("habe gerade ein lustiges Paket versendet. Das Paket hatte die Laenge " + toSend.length);
		} catch (IOException e) {
			System.out.println("error in server.connection.SignalingConnection: could not write to OutputStream");			
			// e.printStackTrace();
		}
	}

}

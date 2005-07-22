package slave.connection;

import global.messages.ConnectionEndMessage;
import global.messages.ConnectionMessage;
import global.messages.Message;
import global.messages.MessageMessage;
import global.messages.NameMessage;
import global.messages.StreamEndMessage;
import global.messages.StreamMessage;

import global.*;
import global.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import slave.Slave;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 28.5.2004
 *
 * Die DataConnection im Client ist kein Thread, da sie nur sendet und niemals empfaengt.
 */
public class DataConnection {
	
	//	hierüber wird kommuniziert
	private DatagramSocket socket;
	 
	private Slave parent;
	
	private InetAddress server;	
	
	
	public DataConnection (Slave client, String server, String localIP) {
		this.parent = client;
		
		InetAddress localIPAddress = null;
		
		try {
			this.server = InetAddress.getByName(server);
			localIPAddress = InetAddress.getByName(localIP);
		} catch (UnknownHostException e) {
			System.out.println("error in client.connection.SignalingConnection.SignalingConnection: given server address is unknown.");			
			// e.printStackTrace();
		}
		
		// Socket anlegen und an uebergebenen Port binden
		try {
			this.socket = new DatagramSocket(ConnectionConstants.tcpPort, localIPAddress); // port, inetAddress
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("error in client.connection.Connection: could not open socket. program will exit.");
			System.exit(1);
		}
	}


	public void sendDataMessage(Message message) {
		if (message instanceof MessageMessage) {
			MessageMessage mm = (MessageMessage) message;
			this.sendMessageMessage(mm.getTimeStamp(), mm.getProtocol(), mm.getSource(), mm.getDestination(), mm.getMessage());
		}
		else if(message instanceof ConnectionMessage) {
			ConnectionMessage cm = (ConnectionMessage) message;
			this.sendConnectionMessage(cm.getTimeStamp(), cm.getProtocol(), cm.getSource(), cm.getDestination());
		}
		else if(message instanceof ConnectionEndMessage) {
			ConnectionEndMessage cm = (ConnectionEndMessage) message;
			this.sendConnectionEndMessage(cm.getTimeStamp(), cm.getProtocol(), cm.getSource(), cm.getDestination());
		}
		else if(message instanceof StreamMessage) {
			StreamMessage cm = (StreamMessage) message;
			this.sendStreamMessage(cm.getTimeStamp(), cm.getProtocol(), cm.getSource(), cm.getDestination(), cm.getPort());
		}
		else if(message instanceof StreamEndMessage) {
			StreamEndMessage cm = (StreamEndMessage) message;
			this.sendStreamEndMessage(cm.getTimeStamp(), cm.getProtocol(), cm.getSource(), cm.getDestination(), cm.getPort());
		}
		else if(message instanceof NameMessage) {
			NameMessage cm = (NameMessage) message;
			this.sendNameMessage(cm.getTimeStamp(), cm.getProtocol(), cm.getComputer(), cm.getName());
		}
		else {
			System.out.println("error in client.connection.Connection.sendDataMessage: invalid parameter.");
		}

	}


	public void sendMessageMessage(long timestamp, String protocol, InetAddress source, InetAddress destination, String text) {		
		// InetAdressen in byte-Arrays umwandeln				
		byte [] sourceAsByteArray = this.checkInetAddressAndReturnByteArray(source);
		byte [] destinationAsByteArray = this.checkInetAddressAndReturnByteArray(destination);
		
		// Puffer mit der richitgen Größe anlegen
		byte [] buf = new byte [ConnectionConstants.DATAMESSAGELENGTH];

		// Typ setzen
		buf[0] = (byte) ConnectionConstants.MESSAGE;		
		
		// Quelle setzen
		int destPos = ConnectionConstants.TYPE;
		System.arraycopy(sourceAsByteArray, 0, buf, destPos, ConnectionConstants.DESTINATION);
			
		// Ziel setzen
		destPos += ConnectionConstants.SOURCE;
		System.arraycopy(destinationAsByteArray, 0, buf, destPos, ConnectionConstants.SOURCE);
		
		// new in 2/2005: set timestamp
		destPos += ConnectionConstants.DESTINATION;
		byte [] timestampAsByteArray = Utils.longToByteArray(timestamp, ConnectionConstants.TIMESTAMP);
		System.arraycopy(timestampAsByteArray, 0, buf, destPos, ConnectionConstants.TIMESTAMP);
		
		// Protocol setzen
		destPos += ConnectionConstants.TIMESTAMP;
		protocol = this.toSpecificLength(protocol, 20);
		byte [] protocolAsByteArray = Utils.StringToByteArray(protocol);
		System.arraycopy(protocolAsByteArray, 0, buf, destPos, protocolAsByteArray.length);
				
		// Text setzen
		destPos += ConnectionConstants.PROTOCOL; 
		text = this.toSpecificLength(text, 20);
		byte [] textAsByteArray = Utils.StringToByteArray(text);
		System.arraycopy(textAsByteArray, 0, buf, destPos, textAsByteArray.length);
		
		// Nachricht versenden
		this.send(buf);
	}


	public void sendStreamMessage(long timestamp, String protocol, InetAddress source, InetAddress destination, int port) {
		byte [] buf = this.createStreamMessage(ConnectionConstants.STREAM, timestamp, protocol, source, destination, port);
		this.send(buf);
	}


	public void sendStreamEndMessage(long timestamp, String protocol, InetAddress source, InetAddress destination, int port) {
		byte [] buf = this.createStreamMessage(ConnectionConstants.STREAM_END, timestamp, protocol, source, destination, port);
		this.send(buf);		
	}


	public void sendConnectionMessage(long timestamp, String protocol, InetAddress source, InetAddress destination) {
		byte [] buf = this.createConnectionMessage(ConnectionConstants.CONNECTION, timestamp, protocol, source, destination);
		this.send(buf);
	}


	public void sendConnectionEndMessage(long timestamp, String protocol, InetAddress source, InetAddress destination) {
		byte [] buf = this.createConnectionMessage(ConnectionConstants.CONNECTION_END, timestamp, protocol, source, destination);
		this.send(buf);
	}


	public void sendNameMessage(long timestamp, String protocol, InetAddress computer, String text) {
		// InetAdressen in byte-Arrays umwandeln				
		byte [] computerAsByteArray = this.checkInetAddressAndReturnByteArray(computer);
		
		// Puffer mit der richitgen Größe anlegen
		byte [] buf = new byte [ConnectionConstants.DATAMESSAGELENGTH];
 				 						  
		// Typ setzen
		buf[0] = (byte) ConnectionConstants.NAME;		
		
		// Computer setzen
		int destPos = ConnectionConstants.TYPE;
		System.arraycopy(computerAsByteArray, 0, buf, destPos, ConnectionConstants.COMPUTER);

		// new in 2/2005: set timestamp
		destPos = ConnectionConstants.TYPE + ConnectionConstants.SOURCE + ConnectionConstants.DESTINATION;
		byte [] timestampAsByteArray = Utils.longToByteArray(timestamp, ConnectionConstants.TIMESTAMP);
		System.arraycopy(timestampAsByteArray, 0, buf, destPos, ConnectionConstants.TIMESTAMP);
		
		// Protocol setzen
		protocol = this.toSpecificLength(protocol, 20);		
		byte [] protocolAsByteArray = Utils.StringToByteArray(protocol);
		System.arraycopy(protocolAsByteArray, 0, buf, ConnectionConstants.TYPE + ConnectionConstants.SOURCE + ConnectionConstants.DESTINATION + ConnectionConstants.TIMESTAMP, protocolAsByteArray.length);
		
		// Text setzen
		text = this.toSpecificLength(text, 20);
		byte [] textAsByteArray = Utils.StringToByteArray(text);
		destPos = ConnectionConstants.TYPE + ConnectionConstants.COMPUTER;
		System.arraycopy(textAsByteArray, 0, buf, destPos, textAsByteArray.length);

		
		// Nachricht versenden
		this.send(buf);		
	}
		
	
	// ----------------- Hilfsmethoden --------------------
	
	private String toSpecificLength (String string, int length) {
		if (string.length() >= length) {
			string = string.substring(0, length - 1);
		}
		else {
			for (int i = string.length(); i < length; i++) {
				string = string + " ";
			}
		}		
		return string;
	}
	
	
	private byte [] checkInetAddressAndReturnByteArray(InetAddress address) {
		byte [] array = address.getAddress();
		
		// falls dies IPv4-Adressen sind, müssen die Arrays vergrößert und vorne mit 0....0 aufgefüllt werden
		// Java speichert in MSB first, genauso wie wir es brauchen.
		if (array.length == 4){
			array = Utils.ipv4ToIpv6ByteArray(array);
		}
		
		return array;
	}
	
	
	private void send (byte [] buf) {
		// DatagramPacket erzeugen
		DatagramPacket packetToSend = new DatagramPacket(buf, buf.length, this.server, ConnectionConstants.serverPort);
	   
		 // jetzt die Nachricht versenden
		try {
			this.socket.send(packetToSend);
			//* DEBUG */ System.out.println(" DataPacket per UDP versendet.");			
		} catch (IOException e) {
			System.out.println("Error in client.connection.Connection: could not send DataPacket to server.");
		}
	}
	
	
	/** diese Methode wird aus den Methoden zum Versenden einer streamConnection- sowie zum
	 * Versenden einer streamConnectionEnd-Nachricht aufgerufen. Diese Nachrichten sind bis auf den Typ
	 * identisch. Daher brauch es diese Methode nicht 2mal geben.
	 * 
	 * @param type
	 * @param protocol
	 * @param source
	 * @param destination
	 * @return
	 */
	private byte [] createConnectionMessage(int type, long timestamp, String protocol, InetAddress source, InetAddress destination) {
		//		InetAdressen in byte-Arrays umwandeln				
		byte [] sourceAsByteArray = this.checkInetAddressAndReturnByteArray(source);
		byte [] destinationAsByteArray = this.checkInetAddressAndReturnByteArray(destination);
		
		// Puffer mit der richitgen Größe anlegen
		byte [] buf = new byte [ConnectionConstants.DATAMESSAGELENGTH];

		// Typ setzen
		buf[0] = (byte) type;
		
		// Quelle setzen
		int destPos = ConnectionConstants.TYPE;
		System.arraycopy(destinationAsByteArray, 0, buf, destPos, ConnectionConstants.DESTINATION);
			
		// Ziel setzen
		destPos += ConnectionConstants.SOURCE;
		System.arraycopy(sourceAsByteArray, 0, buf, destPos, ConnectionConstants.SOURCE);
		
		// new in 2/2005: set timestamp
		destPos += ConnectionConstants.DESTINATION;
		byte [] timestampAsByteArray = Utils.longToByteArray(timestamp, ConnectionConstants.TIMESTAMP);
		System.arraycopy(timestampAsByteArray, 0, buf, destPos, ConnectionConstants.TIMESTAMP);
				
		//Protocol setzen
		destPos += ConnectionConstants.TIMESTAMP;
		protocol = this.toSpecificLength(protocol, 20);		
		byte [] protocolAsByteArray = Utils.StringToByteArray(protocol);
		System.arraycopy(protocolAsByteArray, 0, buf, destPos, protocolAsByteArray.length);
		
				
		return buf;
	}


	/** diese Methode wird aus den Methoden zum Versenden einer streamConnection- sowie zum
	 * Versenden einer streamConnectionEnd-Nachricht aufgerufen. Diese Nachrichten sind bis auf den Typ
	 * identisch. Daher brauch es diese Methode nicht 2mal geben.
	 * 
	 * @param type
	 * @param protocol
	 * @param source
	 * @param destination
	 * @return
	 */
	private byte [] createStreamMessage(int type, long timestamp, String protocol, InetAddress source, InetAddress destination, int port) {
		// InetAdressen in byte-Arrays umwandeln				
		byte [] sourceAsByteArray = this.checkInetAddressAndReturnByteArray(source);
		byte [] destinationAsByteArray = this.checkInetAddressAndReturnByteArray(destination);
		
		// Puffer mit der richitgen Größe anlegen
		byte [] buf = new byte [ConnectionConstants.DATAMESSAGELENGTH];

		// Typ setzen
		buf[0] = (byte) type;
		
		// Quelle setzen
		int destPos = ConnectionConstants.TYPE;
		System.arraycopy(destinationAsByteArray, 0, buf, destPos, ConnectionConstants.DESTINATION);
			
		// Ziel setzen
		destPos += ConnectionConstants.SOURCE;
		System.arraycopy(sourceAsByteArray, 0, buf, destPos, ConnectionConstants.SOURCE);
		
		// new in 2/2005: set timestamp
		destPos += ConnectionConstants.DESTINATION;
		byte [] timestampAsByteArray = Utils.longToByteArray(timestamp, ConnectionConstants.TIMESTAMP);
		System.arraycopy(timestampAsByteArray, 0, buf, destPos, ConnectionConstants.TIMESTAMP);
		
		// Protocol setzen
		destPos += ConnectionConstants.TIMESTAMP;
		protocol = this.toSpecificLength(protocol, 20);		
		byte [] protocolAsByteArray = Utils.StringToByteArray(protocol);
		System.arraycopy(protocolAsByteArray, 0, buf, destPos, protocolAsByteArray.length);
		
		// Port setzen
		destPos += ConnectionConstants.PROTOCOL;
		System.arraycopy(Utils.intToByte(port, 20), 0, buf, destPos, ConnectionConstants.PORT);
				
		return buf;
	}

}

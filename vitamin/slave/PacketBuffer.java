package slave;

import java.util.LinkedList;

import net.sourceforge.jpcap.net.Packet;


/** FIFO-Puffer, in dem die mitgelesenen Pakete zwischengespeichert werden.
 * Der Puffer ist threadsicher. 
 * 
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 08.04.2004
 *
 */
public class PacketBuffer {
	
	private LinkedList buffer;	// der Puffer
	private int elements;			// gibt an, wieviele Elemente sich gerade in dem Puffer befinden
	
	
	public PacketBuffer(){
		this.buffer = new LinkedList();
		this.elements = 0;
	}
	
	
	/** Schreibt ein Paket an das Ende des Puffers.
	 * Durch "synchronized" wird sichergestellt, dass neben dieser Methode nicht auch
	 * noch die Methode read läuft.
	 * 
	 * @param packet das Paket, das in den Puffer geschrieben werden soll
	 */
	public synchronized void write(Packet packet){		
		// Paket hinten an die LinkedList hängen
		this.buffer.addLast(packet);
		
		// Element-Counter um eins hochzählen
		this.elements++;
		
		/*IPPacket test = new IPPacket(6, new byte [] {47, 11, 11, 11, 47, 11, 11, 11, 47, 11, 11, 11, 47, 11, 11, 11, 47, 11, 11, 11, 47, 11, 11, 11, 47, 11, 11, 11});
		System.out.println("IP-Paket angelegt.");
		byte [] header = test.getIPHeader();
		System.out.println("header length: " + header.length);*/
		
		//System.out.println("Paket in Puffer geschrieben.");
		
		//EthernetPacket ePacket =(EthernetPacket) packet;
		//System.out.println("nach EthernetPacket gecastet.");
		
		//UDPPacket uPacket = (UDPPacket) packet;
		//System.out.println("nach UDPPacket gecastet.");
		
		//IPPacket ipPacket = (IPPacket) packet;
		//System.out.println("gecastet.");
		//System.out.println("IPHeaderLength: " + ((IPPacket) packet).getIpHeaderLength());
		//System.out.println("IPDataLength: " + ((IPPacket) packet).getIPData().length);
		
		// andere Threads aufwecken, falls einer lesen wollte und sich schlafen gelegt hat
		this.notifyAll();
	}
	
	
	/** Gibt das erste Paket zurück. Wenn kein Paket da ist, wird so lange gewartet,
	 * bis wieder eins da ist.
	 * Durch "synchronized" wird sichergestellt, dass neben dieser Methode nicht auch
	 * noch die Methode write läuft.
	 * 
	 * @return das erste Paket
	 */
	public synchronized Packet read(){
		// solange warten, bis mindestens ein Element da ist 
		while(this.elements == 0){
			try {
				this.wait();
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}

		// erstes Paket lesen
		Packet packet = (Packet) this.buffer.removeFirst();
		
		// Element-Counter eins heruntersetzen
		this.elements--;
		
		// Paket zurückgeben
		return packet;
	}
	
	
	public synchronized int getElements() {
		return this.elements;
	}
	
}

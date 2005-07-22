package slave.capture;

import slave.*;
import net.sourceforge.jpcap.capture.CaptureDeviceOpenException;
import net.sourceforge.jpcap.capture.CapturePacketException;
import net.sourceforge.jpcap.capture.InvalidFilterException;
import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.capture.PacketListener;
//import net.sourceforge.jpcap.net.EthernetPacket;
import net.sourceforge.jpcap.net.IPPacket;
import net.sourceforge.jpcap.net.Packet;
import global.Filter;

/*
 * Ein NetworkListener liest mittels der JpCap-Bibliothekt dauerhaft Pakete mit, die über ein
 * bestimmtes Netzwerkinterface gehen und schreibt diese in einen PacketBuffer, aus dem
 * sie zur weiteren Verarbeitung wieder ausgelesen werden können.
 * 
 * Es wird - unabhängig vom setzbaren Filter - nur der eingeheden Datenverkehr mitgelesen.
 * Dadurch wird verhindert, dass der Server ueber jedes Paket zweimal informiert wird (vom
 * Sender und vom Empfaenger). 
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 *
 * @version	02.06.2004
 */
public class NetworkListener extends Thread implements PacketListener {
	
	// ---------------------------------- Konstanten ------------------------
	
	private static final int INFINITE = -1;
	private static final int PACKET_COUNT = INFINITE; 
	
	//	----------------------------- Klassenvariablen ------------------------
	
	private PacketCapture m_pcap;	// die CaptureEngine
	private String m_device;				// der Name des Devices von dem gelesen wird
	
	private PacketBuffer buffer;			// der Puffer, in den die mitgelesenen Pakete geschrieben werden. Der Classifier liest sie dann wieder von dort
	
	private String filter;						// Filter in BPF; z.Zt werden alle Pakete gecaptured
	private Filter filterObject;
	
	private String myMAC;					// MAC-Adresse des Kollektors. Wird verwendet, um nur den eingehenden Datenverkehr weiterzuleiten
	
	private boolean in;	// gibt an, ob ein- oder ausgehender Datenverkehr mitgelesen wird (true = eingehend, false = ausgehend)

	
	//	---------------------------------- Konstruktoren ------------------------
	
	public NetworkListener(String device, PacketBuffer buffer, Filter filter, String myMAC) throws Exception {
		
		// Klassenvariablen setzen
		this.buffer = buffer;
		this.filterObject = filter;		
		this.filter = "";
		/*if (inOut.equals("in"))
			this.in = true;
		else
			this.in = false;*/
		
		this.m_device = device;
		
		this.myMAC = myMAC;

		// Instanz der Capture Engine holen
		m_pcap = new PacketCapture();
    
		// Device finden (nimmt eins, das funktioniert)
		//m_device = m_pcap.findDevice();
		System.out.println("using device: " + m_device);
	}
	
	
	//	----------------------------- Methoden ---------------------------

	public void run () {
		try {
			this.startCollecting();
		} catch (InvalidFilterException e) {
			e.printStackTrace();
		} catch (CaptureDeviceOpenException e) {
			e.printStackTrace();
		} catch (CapturePacketException e) {
			e.printStackTrace();
		}
	}
	
	
	private void startCollecting() throws InvalidFilterException, CaptureDeviceOpenException, CapturePacketException {
		// Device öffnen (hierzu werden root-Rechte benötigt)
		int snaplen = 65535;
		m_pcap.open(m_device, snaplen, true, INFINITE);

		// Hier wird der Filter gesetzt
		/* DEBUG */ System.out.println("Filter: " + this.filter);
		m_pcap.setFilter(this.filter, true);

		// Listener registrieren (in diesem Fall diese Klasse selber)
		m_pcap.addPacketListener(this);

		// Pakete capturen; da PACKET_COUNT = -1 ist, werden unendlich viele
		// Pakete gecpatured
		m_pcap.capture(PACKET_COUNT);
	}
	
	
	public void stopCollecting(){	
		m_pcap.endCapture();
		
		// PackageListener entfernen..,
		m_pcap.removePacketListener(this);
					
		m_pcap.close();
	}
		
	
	/** wird aufgerufen, sobald JPcap ein Paket mitgelesen hat
	 * 
	 */
	public void packetArrived(Packet packet) {
		//* DEBUG */ System.out.println("packet arrived.");		
		// es wird nur eingehender Datenverkehr betrachtet. Der Rest wird einfach erst gar nicht gepuffert
		//* DEBUG */ System.out.println ("destinationHwAddress: " + ((EthernetPacket) packet).getDestinationHwAddress());
		//if (this.in) {
		
		//EthernetPacket test = (EthernetPacket) packet;
		
		//* DEBUG */ System.out.println("Packet -> EthernetPacket umgewandelt.");
		//* DEBUG */ System.out.println("test.getDestinationHwAddress(): " + test.getDestinationHwAddress());		
		
		// ist das nicht totaler Unsinn?
		//if ( ((EthernetPacket) packet).getDestinationHwAddress().equals(this.myMAC) || ((EthernetPacket) packet).getSourceHwAddress().equals(this.myMAC)) {
				// Paket in Puffer schreiben
				//* DEBUG */ System.out.println("myMAC ist Quelle oder Ziel.");
				if (packet instanceof IPPacket) {
					this.buffer.write(packet);
					//* DEBUG */ System.out.println("Paket in Puffer geschrieben.");					
				}
				//else
					//* DEBUG */ System.out.println("no IP-Packet.");
				//* DEBUG */ System.out.println("packet written to buffer.");
		//	}
		//}
		//else
			//* DEBUG */ System.out.println("myMAC ist weder Start-noch Ziel-MAC.");
	}
	
	
	public void setFilter(Filter filter){
		this.filterObject = filter;
		this.filter = filter.getPortsAndIPsAsBPF();
	}
	
}

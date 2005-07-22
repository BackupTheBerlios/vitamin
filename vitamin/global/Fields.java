package global;

import global.pluginStructure.types.LocationType;

import java.util.Hashtable;


/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 *
 */
public class Fields {

	private Hashtable ipv4Fields = new Hashtable();
	private Hashtable ipv6Fields = new Hashtable();
	private Hashtable tcpFields = new Hashtable();
	private Hashtable udpFields = new Hashtable();
	
	public Fields () {
		// alle Angaben in Bit
		
		this.udpFields = new Hashtable();
		udpFields.put("destination port", new Position(16, 16));
		udpFields.put("source port", new Position(0, 16));
		udpFields.put("length", new Position(32, 16));
		udpFields.put("checksum", new Position(48, 16));
		
		this.tcpFields = new Hashtable();
		tcpFields.put("source port", new Position(0, 16));
		tcpFields.put("destination port", new Position(16, 16));
		tcpFields.put("sequence number", new Position(32, 32));		
		tcpFields.put("acknowledgement number", new Position(64, 32));		
		tcpFields.put("window", new Position(112, 16));
		tcpFields.put("checksum", new Position(128, 16));		
		tcpFields.put("urgent pointer", new Position(144, 16));	
		
		this.ipv4Fields = new Hashtable();
		ipv4Fields.put("source address", new Position(96, 32));
		ipv4Fields.put("destination address", new Position(128, 32));
		ipv4Fields.put("type of service", new Position(8, 8));
		ipv4Fields.put("time to live", new Position(64, 8));
		ipv4Fields.put("internet header length", new Position(4, 4));
		ipv4Fields.put("version", new Position(0, 4));
		ipv4Fields.put("protocol", new Position(72, 8));
		
		this.ipv6Fields = new Hashtable();
		ipv6Fields.put("version", new Position(0, 4));
		ipv6Fields.put("traffic class", new Position(4, 8));
		ipv6Fields.put("flow label", new Position(12, 20));
		ipv6Fields.put("payload length", new Position(32, 16));
		ipv6Fields.put("next header", new Position(48, 8));
		ipv6Fields.put("hop limit", new Position(56, 8));
		ipv6Fields.put("source address", new Position(64, 16 * 8));
		ipv6Fields.put("destination address", new Position(192, 16 * 8));				
	}


	/** Liefert die Startposition zum Namen eines Feldes in einem Bereich eines Paketes.
	 * Liefert -1, wenn der Name unbekannt ist. die Position wird in Bit (nicht in Byte) angegeben.
	 * 
	 * @param name
	 * @param location
	 * @return
	 */
	public int getStartFromName(String name, LocationType location) {
		// damit die Angabe in den Plugins bzgl. Gross- und Kleinschreibung egal ist
		name = name.toLowerCase();
		
		int start = -1;
		switch (location.getType()) {
			case LocationType.IPV4_TYPE:
				start = ((Position) this.ipv4Fields.get(name)).getStart();
				break;

			case LocationType.IPV6_TYPE:
				start = ((Position) this.ipv6Fields.get(name)).getStart();
				break;

			case LocationType.UDP_TYPE:
				start = ((Position) this.udpFields.get(name)).getStart();
				break;

			case LocationType.TCP_TYPE:
				start = ((Position) this.tcpFields.get(name)).getStart();
				break;
				
			default:
				System.out.println("error in global.Fields.getStartFromName: given location is unknown.");
				break;
		}
		return start;
	}
	
	
	public int getLengthFromName(String name, LocationType location) {
		int length = 0;
		switch (location.getType()) {
			case LocationType.IPV4_TYPE:
				length = ((Position) this.ipv4Fields.get(name)).getLength();
				break;

			case LocationType.IPV6_TYPE:
				length = ((Position) this.ipv6Fields.get(name)).getLength();
				break;

			case LocationType.UDP_TYPE:
				length = ((Position) this.udpFields.get(name)).getLength();
				break;

			case LocationType.TCP_TYPE:
				length = ((Position) this.tcpFields.get(name)).getLength();
				break;

			default:
				System.out.println("error in global.Fields.getLengthFromName: given location is unknown.");
				break;
		}
		return length;
	}
		
}

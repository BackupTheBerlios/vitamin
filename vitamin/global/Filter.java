package global;

import java.net.InetAddress;
import java.util.LinkedList;
import java.awt.*;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 14-05-2004
 *
 * Diese Klasse repräsentiert einen Filter. Hier wird definiert, welche Pakete betrachtet werden
 * und welche nicht. Gefiltert werden kann nach den folgenden Kriterien:
 * - Ports
 * - IP
 * - Protokolle
 * Bei Ports und IPs kann nach Quelle und Ziel unterschieden werden. 
 * Nur die Elemente, die in einer Liste drinstehen, werden zum Server weitergeleitet.
 * Ist eine List leer, so werden alle Elemente dieses Typs weitergeleitet (es wird also z.B.
 * nicht auf den Port geachtet).
 * 
 */
public class Filter {
	
	// hier werden die Dinge, die dargestellt werden sollen, drin gespeichert.
	private LinkedList sourcePorts;
	private LinkedList destinationPorts;
	private LinkedList protocols;
	private LinkedList sourceIps;
	private LinkedList destinationIps;

    // hierdrin werden die Farben gespeichert
    private LinkedList protocolColors;

    /* Ports und IPs werden keine Farben zugeordnet.
    Grund hierfuer ist folgendes:
    - IPs: die IPs sind ja die UML-Objekte bzw. Knoten im Netzwerktopologiediagramm.
      Eine farbliche Darstellung waere hier Unsinn, da es ja auf die Kommunikation
      zwischen den Knoten ankommt.
    */


	public Filter() {
		this.sourcePorts = new LinkedList();
		this.destinationPorts = new LinkedList();
		this.protocols = new LinkedList();
		this.sourceIps = new LinkedList();
		this.destinationIps = new LinkedList();

        this.protocolColors = new LinkedList();
	}
	
	
	// -------------- Methoden zum Hinzufügen und Entfernen von Filteroptionen ----------------------
	
	public void addPort(int portNumber, String sourceDest) {
		if (sourceDest.equals("source"))
			this.sourcePorts.add(new Integer(portNumber));
		else
			this.destinationPorts.add(new Integer(portNumber));
	}
	
	
	public void removePort (int portNumber, String sourceDest) {
		if (sourceDest.equals("source"))
			this.sourcePorts.remove(new Integer(portNumber));
		else
			this.destinationPorts.remove(new Integer(portNumber));
	}


	/** fuegt dem Filter ein Protokoll hinzu. Dem Protokoll kann eine spezielle
     * Darstellungsfarbe zugeordnet werden. Wird als Farbe "null" uebergeben,
     * wird die Standardfarbe schwarz zur Darstellung verwendet.
     *
      * @param protocol
     * @param color
     */
    public void addProtocol (String protocol, Color color) {
		this.protocols.add(protocol);

        if (color == null) {
            this.protocolColors.add(Color.BLACK);
        }
        else {
            this.protocolColors.add(color);
        }
	}


	public void removeProtocol (String protocol) {
		int index = this.protocols.indexOf(protocol);
		this.protocols.remove(index);
		this.protocolColors.remove(index); // die Farbe zu dem Protokoll muss auch weg
	}


	public void addIP (InetAddress ip, String sourceDest) {
		if (sourceDest.equals("source"))
			this.sourceIps.add(ip);
		else
			this.destinationIps.add(ip);
	}


	public void removeIP (InetAddress ip, String sourceDest) {
		if (sourceDest.equals("source"))
			this.sourceIps.remove(ip);
		else
			this.destinationIps.remove(ip);
	}


	//	-------------- Methoden zum Prüfen ----------------------

	public boolean isPortFiltered (int port, String sourceDest) {
		if (sourceDest.equals("source")) {
			if (this.sourcePorts.contains(new Integer(port)))
				return true;
		}
		else {
			if (this.destinationPorts.contains(new Integer(port)))
				return true;
		}

		return false;
	}


	public boolean isProtocolFiltered (String protocol) {
		if (this.protocols.contains(protocol))
			return true;

		return false;
	}


	public boolean isIPFiltered (InetAddress ip, String sourceDest) {
		 if (sourceDest.equals("source")) {
			if (this.sourceIps.contains(ip))
				return true;
		 }
		 else {
			if (this.destinationIps.contains(ip))
				return true;
		 }

		 return false;
	}


	/** liefert den Filter als String in BPF (BSD Packet Filter) zurück.
	 * Es werden nur die Ports und IPs berücksichtigt, da nur die
	 * grundlegenden Protokolle per BPF berücksichtigt werden können.
	 * Das Filtern nach Protokollen kann nicht direkt beim Mitlesen der
	 * Pakete sondern erst nach der Klassifizierung erfolgen.
	 *
	 * Die Syntax des Filters ist wie folgt:
	 * (src host (ip1 or ip2 or ...... or ipn)) and (dst host (ip1 or ip2 or ...... or ipn)) and (src port (p1 or p2 or .... or pn)) and (dst port (p1 or p2 or ... or pn))
	 *
	 */
	public String getPortsAndIPsAsBPF () {
		String filter ="";
		
		// der Filter besteht aus vier Elementen: source-IPs, destination IPs, source ports und destination ports
		// diese Teile werden jedoch nur angehängt, wenn auch Regeln dafür im Filter existieren
		if (this.sourceIps.size() > 0) {
			filter = filter + "(src host (";		
			for (int i = 0; i < this.sourceIps.size(); i++) {
				filter = filter + ((InetAddress) this.sourceIps.get(i)).getHostAddress() + " ";
				if (i < this.sourceIps.size() - 1)
					filter = filter + "or ";				
			}			
			filter = filter + "))";
		}
		
		if (!filter.equals("") && this.destinationIps.size() > 0)
			filter = filter + "and ";
		
		// destination hosts
		if (this.destinationIps.size() > 0) {
			filter = filter + "(dst host (";		
			for (int i = 0; i < this.destinationIps.size(); i++) {
				filter = filter + ((InetAddress) this.destinationIps.get(i)).getHostAddress() + " ";
				if (i < this.destinationIps.size() - 1)
					filter = filter + "or ";				
			}			
			filter = filter + "))";
		}
		
		if (!filter.equals("") && this.sourcePorts.size() > 0)
			filter = filter + "and ";

		// source ports
		if (this.sourcePorts.size() > 0) {
			filter = filter + "(src port (";		
			for (int i = 0; i < this.sourcePorts.size(); i++) {
				filter = filter + ((Integer) this.sourcePorts.get(i)).intValue() + " ";
				if (i < this.sourcePorts.size() - 1)
					filter = filter + "or ";				
			}			
			filter = filter + "))";
		}

		if (!filter.equals("") && this.destinationPorts.size() > 0)
			filter = filter + "and ";

		// destination ports
		if (this.destinationPorts.size() > 0) {
			filter = filter + "(dst port (";		
			for (int i = 0; i < this.destinationPorts.size(); i++) {
				filter = filter + ((Integer) this.destinationPorts.get(i)).intValue() + " ";
				if (i < this.destinationPorts.size() - 1)
					filter = filter + "or ";				
			}			
			filter = filter + "))";
		}
		
		return filter;
	}


    public LinkedList getSourcePorts() {
        return sourcePorts;
    }


    public LinkedList getDestinationPorts() {
        return destinationPorts;
    }


    public LinkedList getProtocols() {
        return protocols;
    }


    public LinkedList getProtocolColors () {
        return protocolColors;
    }


    public Color [] getProtocolColorsAsArray () {
        Color [] colors = new Color [protocolColors.size()];

        for (int i = 0; i < protocolColors.size(); i++) {
            colors [i] = (Color) protocolColors.get(i);
        }

        return colors;
    }


    public LinkedList getSourceIps() {
        return sourceIps;
    }


    public LinkedList getDestinationIps() {
        return destinationIps;
    }


	/** resets the filter. all filter options will be deleted
	 * 
	 */
	public void reset() {
		// clean all LinkedLists
		this.sourcePorts.clear();
		this.destinationPorts.clear();
		this.protocols.clear();
		this.sourceIps.clear();
		this.destinationIps.clear();
		this.protocolColors.clear();		
	}

}

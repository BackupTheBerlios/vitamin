package master.diagram;

import java.awt.*;

import javax.swing.JPanel;

import master.Master;
import master.diagram.objects.Node;
import master.diagram.objects.Subnet;


/*
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 * 
 * @version 08.06.2004
 */
public class NetworkDiagram extends Diagram {
															
	private JPanel contentPane; // die ContentPane
	private Dimension contentPaneDimension;

	// das Ausgangssubnetz. Dieses Subnetz hat seinen Mittelpunkt in der Mitte des Diagramms.
	// als Knoten dieses Subnetzes koennen die weiteren Subnetze liegen
	private Subnet subnet;
	
	// die festgelegte Diagrammgroesse
	private static final int DIAGRAMSIZE = 1000;															
	
	
	public NetworkDiagram(Master master, String title, String [] protocols, Color [] protocolColors){
		super(master, title, protocols, protocolColors);
		
		Font myFont = new Font("SansSerif", Font.PLAIN, 10);
		
		this.contentPane = new JPanel(new BorderLayout());
		this.contentPaneDimension = new Dimension(DIAGRAMSIZE, DIAGRAMSIZE);
		this.contentPane.setPreferredSize(this.contentPaneDimension);
		this.contentPane.setFont(myFont);
		
		// Content Pane setzen und alles fertig machen
		this.setContentPane(contentPane);
		this.pack();
	}

	
	
	public void paintDiagram(Graphics graphics){				
		// Den Zeichenbefehl an das oberste Subnetz weiterleiten.
		// Dieses zeichnet sich dann und ruft die paint-Methode in allen
		// in ihm enthaltenen Subnetzen auf.
		
		// wenn es das Paint-Objekt noch nicht gibt, dann wurde noch kein Knoten hinzugefuegt
		// und es gibt auch noch nichts zu zeichnen
		if (this.subnet != null) {
			this.subnet.paint(this.getContentPane());
		}		
	}



    public void addNode(long time, String ip, int subnetMask, String name, String subnetIp) {
		//* DEBUG */ System.out.println("Knoten hinzufuegen beginnen: " + ip);
		
		String netIP = global.Utils.getNetIP(ip, subnetMask);
		
		// pruefen, ob es der erste Knoten in diesem Diagramm ist (in diesem Fall muss erst mal das Subnetz angelegt werden)
		if (this.subnet == null) {			
			Node node = new Node(ip, subnetMask, name, DIAGRAMSIZE / 2, DIAGRAMSIZE / 2);			
			this.subnet = new Subnet(this, DIAGRAMSIZE, node, netIP, subnetMask);
			//* DEBUG */ System.out.println("erster Knoten im Diagramm.");
		}
		else {
			// pruefen, ob der Knoten in das bestehende Subnetz passt (Net-IPs vergleichen)
			if (netIP.equals(this.subnet.getNetIp()) || global.Utils.isSubnetFromSubnet(this.subnet.getNetIp(), this.subnet.getSubnetMask(), netIP, subnetMask)) {				
				// passt rein -> Knoten an Subnetz weiterleiten
				//* DEBUG */ System.out.println("uebergebe Knoten an oberstes Subnetz.");				
				this.subnet.addNode(ip, subnetMask, name, subnetIp);
			}
			else {
				// TODO Der Knoten passt nicht in das bestehende Subnetz.
				// Es muss also ein weiteres Subnetz angelegt werden.
				// Dann existieren auf oberster Ebene aber zwei Subnetze.
				// Dies ist verboten. Also wird ein weiteres Subnetz angelegt,
				// dass den beiden Subnetzen uebergeordnet wird. Dieses neue
				// Subnetz wird dann this.subnet
				//* DEBUG */ System.out.println("Knoten passt nicht in oberstes Subnetz --> TODO!");
			}
		}
		
		//* DEBUG */ System.out.println("Knoten hinzufuegen beendet.");
		//* DEBUG */ System.out.println();
    }


	public void removeNode(String ip) {
		// an das Subnetz (falls es existiert) weiterleiten
		if (this.subnet != null) {
			this.subnet.removeNode(ip);
		}
    }
    
    
    public void addSubnet (long time, String netIP, int subnetMask, String gateway) {
		//* DEBUG */ System.out.println("Subnet hinzufuegen beginnen, IP = " + netIP);
		if (this.subnet == null) {
			// es gibt in diesem Diagramm noch kein Subnetz -> erstes anlegen
			//* DEBUG */ System.out.println("first subnet");
			this.subnet = new Subnet(this, DIAGRAMSIZE, null, netIP, subnetMask);
		}
		else
		{
			// es gibt schon ein Subnetz -> dahin uebergeben
			//* DEBUG */ System.out.println("give into existing top subnet");
			this.subnet.addSubnet(netIP, subnetMask, gateway);
		}
		// TODO der Fall, dass das neue Subnetz nicht ins bestehende passt, ist noch nicht implementiert
		//* DEBUG */ System.out.println("Subnet hinzufuegen beendet.");
		//* DEBUG */ System.out.println();
    }


    public void addSignal(long time, String protocol, String sourceIP, String destinationIP, String text) {
        // wird im NetworkTopologyDiagram nicht dargestellt
    }


    public void addConnection(long time, String protocol, String sourceIP, String destinationIP) {
		//* DEBUG */ System.out.println("addConnection beginnen.");
		
		// an das Subnetz (falls es existiert) weiterleiten
        if (this.subnet != null) {
        	//* DEBUG */ System.out.println("Verbindung von " + sourceIP + " nach " + destinationIP + " wird an oberstes Subnetz gegeben.");
        	this.subnet.addConnection(protocol, sourceIP, destinationIP);
        }
        
		//* DEBUG */ System.out.println("addConnection beendet.");
		//* DEBUG */ System.out.println();
    }


    public void removeConnection(String protocol, String sourceIP, String destinationIP) {
		// an das Subnetz (falls es existiert) weiterleiten
		if (this.subnet != null) {
			this.subnet.removeConnection(protocol, sourceIP, destinationIP);
		}
    }


    public void addStream(long time, String protocol, String sourceIP, String destinationIP) {
		// an das Subnetz (falls es existiert) weiterleiten
		if (this.subnet != null) {
			this.subnet.addStream(protocol, sourceIP, destinationIP);
		}
    }


    public void removeStream(String protocol, String sourceIP, String destinationIP) {
		// an das Subnetz (falls es existiert) weiterleiten
		if (this.subnet != null) {
			this.subnet.removeStream(protocol, sourceIP, destinationIP);
		}
    }


    public void addName(long time, String protocol, String computer, String name) {
		// an das Subnetz (falls es existiert) weiterleiten
		if (this.subnet != null) {
			this.subnet.addName(protocol, computer, name);
		}
    }


    public void removeName(String protocol, String computer, String name) {
		// an das Subnetz (falls es existiert) weiterleiten
		if (this.subnet != null) {
			this.subnet.removeName(protocol, computer, name);
		}
    }

}

package master.diagram.objects;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;
import java.awt.*;

import master.diagram.NetworkDiagram;

/**
 * Der Algorithmus zur Darstellung der Informationen in diesem Diagramm ist in
 * meiner Bachelorarbeit ausfuehrlich erlautert.
 *
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class Subnet {

    // Referenz auf das Wurzelobjekt des Diagramms
    private NetworkDiagram top;
    
    private int n;      // die Gesamtanzahl der Knoten und Subnetze in diesem Subnetz (abzgl. dem Mittelpunkt)
    private int alpha;  // der Winkel zwischen den einzelnen Subnetzen
    private int r;      // der Radius des Kreises, auf dem die Subnetze bzw. die Knoten liegen
    private int size;   // die Groesse dieses Subnetzes
    private int sns;	// der Durchmesser eines untergeordneten Subnetzes.


    private String ip;
    private int subnetMask;

    private Node m;     // der Knoten im Mittelpunkt des Subnetzes (dieser Knoten tauch nicht in subnets auf)

    // Hierdrin liegen alle Knoten und Subnetze, die in diesem Subnetz liegen.
    // Die Laenge der Liste muss immer gleich n sein.
    // Ein Objekt in dieser Liste kann entwerde "Node" oder "Subnet" sein.
    private LinkedList nodesAndSubnets;

    private Hashtable connections;  // hierdrin liegen alle Verbindungen zwischen den Knoten in diesem Subnetz
    
    private static Random random = new Random(); // hierueber werden die Subnetze zufaellig etwas gedreht. Das soll verhindern, dass bsp. bei zwei und vier Knoten nicht ein Knoten genau auf der Verbindungslinie zum hierueber liegenden Subnetz liegt.
    private int verschiebung; 


    public Subnet (NetworkDiagram top, int size, Node m, String ip, int subnetMask) {
        // gegebene Werte (Anzahl der Subnetze, Gesamtgroesse des Diagramms) setzen
        this.top = top;
        this.n = 0;
        this.size = size;       
        this.m = m; // m kann auch null sein; dann wird der erste Knoten, der kommt, in die Mitte gesetzt
        
        this.verschiebung = random.nextInt(45);

        this.ip = ip;
        this.subnetMask = subnetMask;

		this.nodesAndSubnets = new LinkedList();

        this.connections = new Hashtable();
        
		// fuer den Mittelpunkt wird auch schon mal eine LinkedList angelegt, in der ausgehende
		// Connections abgelegt werden koennen
		if (this.m != null)
			this.connections.put(this.m.getIP(), new LinkedList());

        //this.initCoords(this.size, this.m.getX(), this.m.getY());
    }
    

    protected void initCoords (int size, int mX, int mY) {
    	this.size = size;
    	
    	// wenn m noch nicht gesetzt ist, kann ich auch nichts berechnen
    	if (m != null) {    	
	    	this.m.setX(mX);
	    	this.m.setY(mY);
	    	    	
	        // n == 1 ist ein Sonderfall, da hier die Berechnung Mist ergibt (alpha ist dann 360°,
	        // alpha/2 = 180° und damit ist der sin = 0, was für die meisten Groessen 0 als Ergebnis gibt.
	        //if (this.n == 1) {
	        // 	this.alpha = 180°;
	        //}        
	        // Sonderfall beachten, wenn es noch keine Subnetze gibt
	        if (this.n > 0) {
	        	        
		        if (this.n > 1)
		        	this.alpha = 360 / this.n;
		        else
		        	this.alpha  = 180;
			        	        
		        // r berechnen
		        this.r = (int) (this.size / (2 + 2 * Math.sin(Math.toRadians(this.alpha / 2))));
		        
		        // sns berechnen
		        this.sns = (int) (2 * this.r * Math.sin(Math.toRadians(this.alpha / 2)));	        
		        
		        // sns berechnen
		        //this.sns = (int) (this.size / ( 1 + Math.sin(Math.toRadians(this.alpha / 2)) ) );
		
		        // r berechnen
		        //this.r = (this.size - this.sns) / 2;
	        }
	
	        // jetzt wird ueber alle Nodes und Subnets iteriert
	        // die neue Position des Objektes wird berechnet.
	        // Ist es ein Knoten, wird die Position einfach gesetzt.
	        // Ist es ein Subnetz, wird in diesem Subnetz, die Methode initCoords
	        // mit der neuen Groesse und dem neuen Mittelpunkt aufgerufen.
			
			// die Position des ersten Subnetzes in Diagrammkoordinaten
			int x0 = this.m.getX();
			int y0 = this.m.getY() - this.r;
		            
			// Translation in Mittelpunktskoordinaten
			x0 = x0 - this.m.x;
			y0 = y0 - this.m.y;
	                
	        for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
				// die neuen Koordinaten fuer dieses Objekt berechnen
				
				// den Winkel fuer dieses Subnetz berechnen
				// (das erste Subnetz steht im Winkel 0, die nachfolgenden immer i * alpha weiter
				int winkel = i * this.alpha + this.verschiebung;
		            
				// Wir rotieren jetzt den Mittelpunkt des ersten Subnetzes um den Winkel.
				// Die Formel dafuer rotiert um den Ursprung. Wir moechten aber um den Mittelpunkt rotieren.
				// Wir muessen also erst den Ursprung zum Mittelpunkt translaieren, dann rotieren und dann zurueck-
				// translaieren.
		
				// die Rotation um den Winkel "winkel"
				int xi = (int) (x0 * Math.cos(Math.toRadians(winkel)) - y0 * Math.sin(Math.toRadians(winkel)));
				int yi = (int) (x0 * Math.sin(Math.toRadians(winkel)) + y0 * Math.cos(Math.toRadians(winkel)));
		            
				// die Translation zurueck in die Ursprungskoordinaten
				xi = this.m.getX() + xi;
				yi = this.m.getY() + yi;
		                        
		                        
				// die neuen Koordinaten setzen        	        	        	        
	        	if (this.nodesAndSubnets.get(i) instanceof Subnet) {
					// wenn dies ein Subnetz ist, wird die initCoords-Methode aufgerufen
					( (Subnet) this.nodesAndSubnets.get(i)).initCoords(this.sns, xi, yi);
	        	}        		
	        	else {
					// bei einem Knoten werden einfach die neuen Koordinaten gesetzt
					( (Node) this.nodesAndSubnets.get(i)).setX(xi);
					( (Node) this.nodesAndSubnets.get(i)).setY(yi);
					//* DEBUG */ System.out.println("Knoten " + ( (Node) this.nodesAndSubnets.get(i)).getIP() + " bekommt Koordinaten (" + xi + ", " + yi + ").");				
	        	}
	        }
	        
	        //* DEBUG */ System.out.println("alpha = " + this.alpha + ", r = " + this.r + ", sns = " + this.sns + ", size = " + this.size + ", n = " + this.n + ", M = (" + this.m.getX() + ", " + this.m.getY() + ").");
    	}
    }


    public void addNode (String ip, int subnetMask, String name, String subnetIp) {
		//* DEBUG */ System.out.println("Knoten hinzufuegen im Subnetz " + this.ip + " beginnen.");
		//* DEBUG */ System.out.println("Knoten: " + ip + ", subnetMask: " + subnetMask);
        
        String netIP = global.Utils.getNetIP(ip, subnetMask);
        Node node = new Node(ip, subnetMask, name);
        
        //* DEBUG */ System.out.println("netIP = " + netIP + ", subnetIP = " + subnetIp);

        // erstmal pruefen, ob das Subnetz stimmt, oder ob wir noch tiefer muessen
        if (this.ip.equals(subnetIp)) {
            // Subnetz stimmt. Der Knoten muss hier rein
                        
            // pruefen, ob der Knoten bereits existiert. Wenn ja, iser ja schon da und die Aktion kann uebergangen werden            
            boolean isAlreadyHere = false;
            if (this.m != null)
            if (this.m.getIP().equals(ip)) {
				//* DEBUG */ System.out.println("Den Knoten gibt's schon. Ist m.");
            	isAlreadyHere = true;
            }
            else {
	            for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
	            	if (this.nodesAndSubnets.get(i) instanceof Node) {
	            		if ( ((Node) this.nodesAndSubnets.get(i)).getIP().equals(ip) ) {
	            			isAlreadyHere = true;
							//* DEBUG */ System.out.println("Den Knoten gibt's schon.");            			
	            		}
	            	}
	            }
            }
	
            if (!isAlreadyHere) {
            	//* DEBUG */ System.out.println("Den Knoten gibt's nocht nicht.");
				// pruefen, ob es der erste Knoten ist (das kann passieren, wenn das Subnetz nicht durch einen Knoten,
				// sondern von ausserhlab durch ein addSubnet angelegt wurde
				if (this.m == null) {
					this.m = node;
					this.m.setX(this.size / 2);
					this.m.setY(this.size / 2);
					
            		//* DEBUG */ System.out.println("Der Knoten wird m.");            		
					this.connections.put(this.m.getIP(), new LinkedList());
				
					this.initCoords(this.size, this.m.getX(), this.m.getY());
				}            
				else {
					
					//* DEBUG */ System.out.println("Den Knoten ganz normal dem Subnetz hinzufuegen.");
	            	
	                // Knoten ablegen
	                this.nodesAndSubnets.add(node);
	
	                // fuer den neuen Knoten wird auch schon mal eine LinkedList angelegt, in der ausgehende
				    // Connections abgelegt werden koennen
				    this.connections.put(node.getIP(), new LinkedList());
	
	                // n wird erhoeht (da neuer Knoten) --> 
	                // die Positionen der einzelnen Subnetze muessen neu berechnet werden.
	                // Ausserdem muss sns neu berechnet werden.
	                // dazu wird initCoords ausgefuehrt. Als Parameter werden die bestehenden uebergeben, da sich Groesse und Mittelpunkt durch einen neuen Knoten nicht aendern
	                this.n++;
	                this.initCoords(this.size, this.m.getX(), this.m.getY());
	            }
            }
            
        }
        else {
            // das Subnetz ist falsch. Es muss also ein tieferliegendes Subnetz gemeint sein --> richtiges finden!
            boolean subnetFound = false;
            
            for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
            	if (this.nodesAndSubnets.get(i) instanceof Subnet) {
            		//  Der Knoten muss an alle Subnetze weitergeleitet
            		//  werden, da das Netz nicht hierarchisch aufgebaut
            		//  sein muss.
            		
					//if (global.Utils.isSubnetFromSubnet(((Subnet) this.nodesAndSubnets.get(i)).getNetIp(), ((Subnet) this.nodesAndSubnets.get(i)).getSubnetMask(), netIP, subnetMask)) {
						subnetFound = true;
						// richtiges Subnetz gefunden. Knoten dorthin weiterleiten
						((Subnet) this.nodesAndSubnets.get(i)).addNode(ip, subnetMask, name, subnetIp);
						//* DEBUG */ System.out.println("Knoten wird an untergeordnetes Subnetz weitergeleitet.");
					//}
						
            	}
            }
            
            /*if (!subnetFound) {
            	//  ---------------------------------------------------------------------------------
            	//   Dieser Fall darf bei nichthierarchischen Netzen nicht
            	//   auftreten, da nicht eindeutig erkennbar ist, wo das
            	//   Subnetz zu diesem Knoten angelegt werden sollte.
				//  ---------------------------------------------------------------------------------
            	
                // das Subnetz existiert noch nicht --> neues Subnetz anlegen
                Subnet subnet = new Subnet(this.top, this.sns, node, netIP, subnetMask);
                
				// TODO ------------------------------------------------------------------------------------
				// TODO Was ist mit dem hier drunter? Kann das noch auftreten?
				 // TODO ------------------------------------------------------------------------------------

				// Es kann sein, dass der Knoten bereits als Knoten (und eben nicht als Subnetz) vorliegt.
				// In diesem Fall wird der bestehende Knoten durch ein neues Subnetz mit dem Knoten als
				// Mittelpunkt ersetzt.
				boolean replaced = false;
				for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
					if (this.nodesAndSubnets.get(i) instanceof Node) {
						if ( ((Node) this.nodesAndSubnets.get(i)).getIP().equals(ip) ) {
							// beim Ersetzen muss auch in this.connections die Referenz geaendert werden
							LinkedList list = (LinkedList) this.connections.get(ip);
							this.connections.remove(ip);
							this.connections.put(global.Utils.getNetIP(ip, subnetMask), list);
							
							subnet = new Subnet (this.top, this.sns, (Node) this.nodesAndSubnets.get(i), netIP, subnetMask);
							
							this.nodesAndSubnets.remove(i);
							this.nodesAndSubnets.add(subnet);
							
							replaced = true;
						}
					}				
				}
                
                // Subnetz diesem Subnetz hinzufuegen
				if (!replaced) {
	                this.nodesAndSubnets.add(subnet);
	                
					// n wird erhoeht (da neues Subnetz) und initcoords neu ausgefuehrt.
					// (dadurch werden die Positionen in diesem Subnetz und allen Untersubnetzen neu berechnet).
					this.n++;
									
				}
				
				this.initCoords(this.size, this.m.getX(), this.m.getY());
            }*/
        }
        
		//* DEBUG */ System.out.println("Knoten hinzufuegen im Subnetz beendet.");
    }


    public void removeNode (String ip){
        // TODO pruefen, ob der Knoten in dieser Ebene liegt. Wenn ja, loeschen. Wenn nein, ans richtige Subnet delegieren
        // WICHTIG: wird ein Knoten oder Subnetz geloescht, so muss auch connections entsprechend angepasst werden
    }
    

    public void addSubnet (String netIP, int subnetMask, String gateway) {
    	//* DEBUG */ System.out.println("addSubnet called in subnet " + this.ip + ", gateway = " + gateway);

		/* Wird ein neues Subnetz angelegt, schmeisst Vitamin das Netz oben in die Hierarchie
           rein. Das oberste Subnetz prueft, ob das Gateway in ihm drinliegt. Wenn ja, dann wird
		   dieser Knoten zu einem Subnetz "ausgebaut", welches dann das neue Subnetz ist.
	       Wenn nicht, gibt er das "add" an alle seine Kinder weiter, die das Verfahren rekursiv
		   fortsetzen. */
		   
		// pruefen, ob der Knoten "gateway" in diesem Subnetz liegt
		boolean gatewayAsNode = false;
		for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
			// wenn es ein Knoten ist (in der Liste liegen auch die Subnetze)
			if (this.nodesAndSubnets.get(i) instanceof Node) {		
				// wenn der Knoten das angegebene Gateway ist
				//* DEBUG */ System.out.println("IP des zu vergleichenden Nodes: " + ((Node) this.nodesAndSubnets.get(i)).getIP() );
				if (  ((Node) this.nodesAndSubnets.get(i)).getIP().equals(gateway) ) {
					// dann erweitere den Knoten zu einem neuen Subnetz (halt DEM neuen)
					//* DEBUG */ System.out.println("Node found. Extending Node to subnet.");
					 
					// beim Ersetzen muss auch in this.connections die Referenz geaendert werden
					String ip = ((Node) this.nodesAndSubnets.get(i)).getIP();
					LinkedList list = (LinkedList) this.connections.get(ip);
					this.connections.remove(ip);
					this.connections.put(global.Utils.getNetIP(ip, subnetMask), list);
					
					Node m = (Node) this.nodesAndSubnets.get(i); // TODO muss ich das hier komplett umkopieren?
					Subnet subnet = new Subnet (this.top, this.sns, m , netIP, subnetMask);
								
					this.nodesAndSubnets.remove(i);
					this.nodesAndSubnets.add(subnet);
								
					gatewayAsNode = true;
				}
			}				
		}
			
		if (!gatewayAsNode) {
			// Gateway nicht gefunden. Befehl an alle Subnetze dieses Subnetzes weiterleiten
			//* DEBUG */ System.out.println("calling addSubnet in all underlying networks");
			for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
				if (this.nodesAndSubnets.get(i) instanceof Subnet) {
					((Subnet) this.nodesAndSubnets.get(i)).addSubnet(netIP, subnetMask, gateway);
				}
			}
		}
		
    	// --------------- alte Methode fuer altes Dateiformat ---------------------
    	
		/*// pruefen, ob dieser Befehl hier richtig ist oder an ein Subnetz dieses Subnetzes
		// uebergeben werden muss
		//String newSubnetIP = global.Utils.getNetIP(netIP, subnetMask); // brauch ich das?
		// wenn es genau dieses Subnetz hier ist, ises ja schon da
		
		if (!(this.ip.equals(netIP) && subnetMask == this.subnetMask)) {
			// dieses Subnetz ises schonmal nicht
					
			boolean isDeeperSubnet = false;
			// dazu ueber alle Knoten und Subnetze in diesem Subnetz iterieren und pruefen
			for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
				if (this.nodesAndSubnets.get(i) instanceof Subnet) {
					// ist netIP ein Subnetz von this.nodesAndSubnets.get(i) ?
					if (global.Utils.isSubnetFromSubnet(((Subnet) this.nodesAndSubnets.get(i)).getNetIp(), ((Subnet) this.nodesAndSubnets.get(i)).getSubnetMask(), netIP, subnetMask)) {
						// Befehl an dieses Subnetz weitergeben, damit das neue Subnetz an der richtigen Stelle angelegt wird			
						((Subnet) this.nodesAndSubnets.get(i)).addSubnet(netIP, subnetMask);
						isDeeperSubnet = true;
					}
				}
			}
	
			if (!isDeeperSubnet) {    	
		    	// pruefen, ob es einen Knoten gibt, der in diesem Subnetz liegt.
		    	// Dann Knoten in Subnetz umwandeln
				boolean replaced = false;
				for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
					if (this.nodesAndSubnets.get(i) instanceof Node) {
						//* DEBUG / System.out.println("IP-Adresse des Knotens: " + ((Node) this.nodesAndSubnets.get(i)).getIP());
						//* DEBUG / System.out.println("NetIP aus IP des Knotens und Maske dieses Subnetzes " + global.Utils.getNetIP(((Node) this.nodesAndSubnets.get(i)).getIP(), subnetMask));
						//* DEBUG / System.out.println("netIP des neuen Subnets: " + netIP);
						//* DEBUG / System.out.println("Maske des neuen Subnets: " + subnetMask);
						if ( global.Utils.getNetIP(((Node) this.nodesAndSubnets.get(i)).getIP(), subnetMask).equals(netIP) ) {						
							//* DEBUG / System.out.println("Knoten " + ((Node) this.nodesAndSubnets.get(i)).getIP() + " wird durch Subnetz ersetzt.");
							// beim Ersetzen muss auch in this.connections die Referenz geaendert werden
							String ip = ((Node) this.nodesAndSubnets.get(i)).getIP();
							LinkedList list = (LinkedList) this.connections.get(ip);
							this.connections.remove(ip);
							this.connections.put(global.Utils.getNetIP(ip, subnetMask), list);
								
							Subnet subnet = new Subnet (this.top, this.sns, (Node) this.nodesAndSubnets.get(i), netIP, subnetMask);
									
							this.nodesAndSubnets.remove(i);
							this.nodesAndSubnets.add(subnet);
								
							replaced = true;
						}
					}				
				}
		    	
		    	// pruefen, ob es dieses Subnetz schon gibt
				//* DEBUG / System.out.println("neue netIP: " + global.Utils.getNetIP(netIP, subnetMask));
				//* DEBUG / System.out.println("this.nodesAndSubnets.size(): " + this.nodesAndSubnets.size());
				boolean alreadyExists= false;
				for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
					if (this.nodesAndSubnets.get(i) instanceof Subnet) {
						//* DEBUG / System.out.println("bestehende netIP: " + ((Subnet) this.nodesAndSubnets.get(i)).getNetIp());
						if (((Subnet) this.nodesAndSubnets.get(i)).getNetIp().equals(global.Utils.getNetIP(netIP, subnetMask))) {
							//* DEBUG / System.out.println("Lege kein neues Subnetz an, da es bereits existiert.");
							alreadyExists = true;					
						}
					}
				}
		    	
				if (!alreadyExists) {
					this.nodesAndSubnets.add(new Subnet(this.top, this.sns, null, netIP, subnetMask));
				                
					// n wird erhoeht (da neues Subnetz) und initcoords neu ausgefuehrt.
					// (dadurch werden die Positionen in diesem Subnetz und allen Untersubnetzen neu berechnet).
					this.n++;
					
					//* DEBUG / System.out.println("neues Subnetz mit netIP " + netIP + " und Subnetmask " + subnetMask + " angelegt.");
				}
			}
		}*/
    }


    public void addSignal (String protocol, String sourceIP, String destinationIP, String text){
        // Signale werden hier nicht dargestellt....
    }


    // removeSignal makes no sense


    public void addConnection (String protocol, String sourceIP, String destinationIP){
        //* DEBUG */ System.out.println("Verbindung hinzufuegen beginnen in subnet " + this.ip);
        
        // pruefen, ob die beiden Knoten in diesem Subnetz oder in einem tieferen liegen
        boolean foundSource = false;
        boolean foundDestination = false;
        
       	Node source = null;
        Node destination = null;
        String ip = ""; // wird gespeichert, da bei Subnetzen in this.connections nicht die Node-IP, sondern die IP des Subnetzes verwendet wird
        
        // erstmal pruefen, ob es der Mittelpunkt ist
        if (this.m.getIP().equals(sourceIP)) {
			//* DEBUG */ System.out.println("Source ist der Mittelpunkt von Subnetz " + this.ip + ".");
        	source = this.m;
        	ip = this.m.getIP();
        	foundSource = true;
        }
        else if (this.m.getIP().equals(destinationIP)) {
			//* DEBUG */ System.out.println("Destination ist der Mittelpunkt von Subnetz " + this.ip + ".");
        	destination = this.m;
        	foundDestination = true;
        }
        
        // und dann die restlichen Knoten und Subnetzmittelpunkte in diesem Subnetz pruefen        
        for (int i = 0; i < this.nodesAndSubnets.size(); i++) {
        	if (this.nodesAndSubnets.get(i) instanceof Node) {
        		// Element i ist ein Knoten        		 
        		if ( ((Node) this.nodesAndSubnets.get(i)).getIP().equals(sourceIP) ) {
        			// Source-Knoten gefunden
					//* DEBUG */ System.out.println("Source in Subnetz " + this.ip + " gefunden.");					
					source = (Node) this.nodesAndSubnets.get(i);
					ip = source.getIP();
					foundSource = true; 					
        		}
        		else if ( ((Node) this.nodesAndSubnets.get(i)).getIP().equals(destinationIP) ) {
					// Destination-Knoten gefunden
					//* DEBUG */ System.out.println("Destination in Subnetz " + this.ip + " gefunden.");
					destination = (Node) this.nodesAndSubnets.get(i);
					foundDestination = true;	
				}
        	}
        	else {
        		// Element i ist ein Subnetz
        		if ( ((Subnet) this.nodesAndSubnets.get(i)).getM().getIP().equals(sourceIP) ) {
					// Source-Knoten gefunden
					//* DEBUG */ System.out.println("Source in Subnetz " + ((Subnet) this.nodesAndSubnets.get(i)).getNetIp() + " gefunden.");
					source = ((Subnet) this.nodesAndSubnets.get(i)).getM();
					ip = ((Subnet) this.nodesAndSubnets.get(i)).getNetIp();
					foundSource = true;
        		}
        		else if ( ((Subnet) this.nodesAndSubnets.get(i)).getM().getIP().equals(destinationIP) ) {
					// Destination-Knoten gefunden
					//* DEBUG */ System.out.println("Destination in Subnetz " + ((Subnet) this.nodesAndSubnets.get(i)).getNetIp() + " gefunden.");
					destination = ((Subnet) this.nodesAndSubnets.get(i)).getM();
					foundDestination = true;
				}        		 
        	}
        }
        
        if (foundSource && foundDestination) {
        	//* DEBUG */ System.out.println("start and destination found in this subnet.");
			// Start- und Zielknoten in diesem Subnetz gefunden --> Verbindung hinzufuegen
			Line line = new Line("Linie", (DiagramObject) source, (DiagramObject) destination);
			((LinkedList) this.connections.get(sourceIP)).add(line);
			
			/*if (!this.connections.containsKey(destinationIP))
				this.connections.put(destinationIP, new LinkedList()); // key, Object
			Line line2 = new Line("Linie", line.getEndObject(), line.getStartObject());
			((LinkedList) this.connections.get(destinationIP)).add(line2); // hier muss die andersume Linie rein*/
        }
        else {
            // TODO addConnection-Befehl muss in tieferes Subnetz weitergeleitet werden
            // wie finde ich das richtige Subnetz heraus? Ich habe ja keine Subnetmask zu Source und Destination
            // --> Uebergangsloesung: connection wird einfach an alle Subnetze weitergeleitet
            for (int i = 0; i< this.nodesAndSubnets.size(); i++) {
            	if (this.nodesAndSubnets.get(i) instanceof Subnet) {
            		((Subnet) this.nodesAndSubnets.get(i)).addConnection(protocol, sourceIP, destinationIP);
            	}
            }
        }
        
		//* DEBUG */ System.out.println("Verbindung hinzufuegen beendet.");		
    }


    public void removeConnection (String protocol, String sourceIP, String destinationIP){
        // TODO
    }


    public void addStream (String protocol, String sourceIP, String destinationIP) {
		//* DEBUG */ System.out.println("addStream in Subnetz " + this.getNetIp() + " beginnen (src " + sourceIP +", dst " + destinationIP + ").");
		
		// Farbe zu Protokoll herausfinden
		Color color = this.top.getColor(protocol);
		//* DEBUG */ System.out.println("Farbe zu Protokoll ist " + color.toString());

		// Liste der vom Startknoten ausgehenden Linien holen...
		LinkedList list = (LinkedList) this.connections.get(sourceIP);

		// Linie suchen
		boolean found = false;
		boolean mehrAls0Verbindungen = true;
		if (list != null) {
			//* DEBUG */ System.out.println("Verbindungen zu " + sourceIP + " gefunden: " + list.size());
			if (list.size() == 0)
				mehrAls0Verbindungen = false;
							
			for (int i = 0; i < list.size(); i++) {
				//* DEBUG */ System.out.println("name: " + ((Node) ((Line) list.get(i)).getEndObject()).getIP());
				if ( ((Node) ((Line) list.get(i)).getEndObject()).getIP().equals(destinationIP) ) {
					//* DEBUG */ System.out.println("Setze Farbe in Linie.");
					
					// Linie gefunden --> neue Farbe setzen
					((Line) list.get(i)).setColor(color);
					found = true;
				}
			}
		}
		if (list == null || !mehrAls0Verbindungen) {
			// zu sourceIP gab's keine Verbindungen. vielleicht zu DestinationIP
			list = (LinkedList) this.connections.get(destinationIP);
			
			if (list != null) {
				//* DEBUG */ System.out.println("Verbindungen zu " + destinationIP + " gefunden: " + list.size());			
				for (int i = 0; i < list.size(); i++) {
					//* DEBUG */ System.out.println("name: " + ((Node) ((Line) list.get(i)).getEndObject()).getIP());
					if ( ((Node) ((Line) list.get(i)).getEndObject()).getIP().equals(sourceIP) ) {
						//* DEBUG */ System.out.println("Setze Farbe in Linie.");
					
						// Linie gefunden --> neue Farbe setzen
						((Line) list.get(i)).setColor(color);
						found = true;
					}
				}
			}			
		}
		
		// falls Linie nicht gefunden werden kann, an Subnetze weitergeben
		if (!found) {
			for (int i = 0; i <this.nodesAndSubnets.size(); i++) {
				if (this.nodesAndSubnets.get(i) instanceof Subnet) {
					//* DEBUG */ System.out.println("gebe addStream an Subnetz " + ((Subnet) this.nodesAndSubnets.get(i)).getNetIp() + " weiter");
					((Subnet) this.nodesAndSubnets.get(i)).addStream(protocol, sourceIP, destinationIP);
				}
			}
		}
		
		
		//* DEBUG */ System.out.println("addStream in Subnetz " + this.getNetIp() + " beenden.");
		//* DEBUG */ System.out.println("");		
    }
    


    public void removeStream (String protocol, String sourceIP, String destinationIP) {
		// Liste der vom Startknoten ausgehenden Linien holen...
		LinkedList list = (LinkedList) this.connections.get(sourceIP);

		// Linie suchen
		boolean found = false;
		boolean mehrAls0Verbindungen = true;
		if (list != null) {
			//* DEBUG */ System.out.println("Verbindungen zu " + sourceIP + " gefunden: " + list.size());
			if (list.size() == 0)
				mehrAls0Verbindungen = false;
							
			for (int i = 0; i < list.size(); i++) {
				//* DEBUG */ System.out.println("name: " + ((Node) ((Line) list.get(i)).getEndObject()).getIP());
				if ( ((Node) ((Line) list.get(i)).getEndObject()).getIP().equals(destinationIP) ) {
					//* DEBUG */ System.out.println("Setze Farbe in Linie.");
					
					// Linie gefunden --> neue Farbe setzen
					((Line) list.get(i)).setColor(Color.BLACK);
					found = true;
				}
			}
		}
		if (list == null || !mehrAls0Verbindungen) {
			// zu sourceIP gab's keine Verbindungen. vielleicht zu DestinationIP
			list = (LinkedList) this.connections.get(destinationIP);
			
			if (list != null) {
				//* DEBUG */ System.out.println("Verbindungen zu " + destinationIP + " gefunden: " + list.size());			
				for (int i = 0; i < list.size(); i++) {
					//* DEBUG */ System.out.println("name: " + ((Node) ((Line) list.get(i)).getEndObject()).getIP());
					if ( ((Node) ((Line) list.get(i)).getEndObject()).getIP().equals(sourceIP) ) {
						//* DEBUG */ System.out.println("Setze Farbe in Linie.");
					
						// Linie gefunden --> neue Farbe setzen
						((Line) list.get(i)).setColor(Color.BLACK);
						found = true;
					}
				}
			}			
		}
		
		// falls Linie nicht gefunden werden kann, an Subnetze weitergeben
		if (!found) {
			for (int i = 0; i <this.nodesAndSubnets.size(); i++) {
				if (this.nodesAndSubnets.get(i) instanceof Subnet) {
					//* DEBUG */ System.out.println("gebe addStream an Subnetz " + ((Subnet) this.nodesAndSubnets.get(i)).getNetIp() + " weiter");
					((Subnet) this.nodesAndSubnets.get(i)).removeStream(protocol, sourceIP, destinationIP);
				}
			}
		}
    }


    public void addName (String protocol, String computer, String name) {
        // TODO
    }


    public void removeName (String protocol, String computer, String name) {
        // TODO
    }
    
    
    //----------------------------------------------------------------------------------------------------------------
    
    
    public void paint(Container contentPane) {
		// Mittelpunkt zeichnen
		this.m.draw((Graphics2D) contentPane.getGraphics());
		
		// als Debug-Ausgabe wird der Kreis auch mal eingezeichnet
		//* DEBUG */ ((Graphics2D) contentPane.getGraphics()).drawOval(this.sns/2, this.sns/2, 2 * this.r, 2 * this.r);  // upper left corner
		
		// sonstige Knoten und Subnetze zeichnen ...
		Node node = null;
		for (int i = 0; i< this.nodesAndSubnets.size(); i++) {
			if (this.nodesAndSubnets.get(i) instanceof Node) {
				// Knoten zeichnen
				node = (Node) this.nodesAndSubnets.get(i);
				node.draw((Graphics2D) contentPane.getGraphics());
			}
			else {
				// Subnet zeichnen
				( (Subnet) this.nodesAndSubnets.get(i) ).paint(contentPane);
			}
		}
		
		// ... und dann die Verbindungslinien (in diesem Subnetz. Die tieferlegenden Subnetze kuemmern sich selber darum).
		Enumeration connections = this.connections.elements();		
		LinkedList list = null;
		while(connections.hasMoreElements()){
			list = (LinkedList)  connections.nextElement();			
			// hier muss jetzt noch über die gesamte Liste iteriert werden und alle enthalteten Linien gezeichnet werden 
			for(int i = 0; i < list.size(); i++){
				((Line) list.get(i)).draw((Graphics2D) contentPane.getGraphics());
			}
		}
    }
    
    
    public String getNetIp () {
    	return this.ip;
    }
    
    
    public int getSubnetMask () {
    	return this.subnetMask;
    }
    
    
    public Node getM() {
    	return this.m;
    }

}


package master.diagram.objects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;


/*
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 * 
 * @version	08.04.2004
 */
public class Node extends DiagramObject {
	
	// IP-Adresse des Knotens
	// (wird zusaetzlich zum Namen dargestellt)
	private String ip;
	
	private int subnetMask;
	
	/* Der Einfachheit halber wird erstmal keine Box, sondern nur ein Punkt verwendet werden, um die
	 * Node darzustellen. Dadurch entfällt die komplizierte Bestimmung der Anfangs- und Endkoordinaten
	 * der Linien. Diese können z.Zt. einfach an dem Punkt beginnen und enden.
	 */
	private Point2D box; // zur graphischen Darstellung des Knotens
	
	
	public Node(String ip, int subnetMask, String name){
		super(name);
		this.ip = ip;
		this.subnetMask = subnetMask;
		
		this.x = 50;
		this.y = 50;		
	}
	
	
	public Node (String ip, int subnetMask, String name, int x, int y) {
		this(ip, subnetMask, name);
		this.x = x;
		this.y = y;		
	}
	

	
	public void draw(Graphics2D graphics){
		// Punkt zeichnen
		graphics.fillRect(this.x -3, this.y - 3, 6, 6);
		
		// Name und IP neben den Punkt zeichnen
		graphics.drawString(this.name, this.x - 30, this.y - 10);
		//if (this.ip.equals("0.0.0.0"))
		//	graphics.drawString("Internet", this.x - 30, this.y - 10);			
		//else
		//	graphics.drawString(this.ip, this.x - 30, this.y - 10);
	}
	
	
	public String getIP(){
		return this.ip;
	}
	
	
	public int getMask(){
		return this.subnetMask;
	}

}

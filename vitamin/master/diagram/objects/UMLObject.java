package master.diagram.objects;


import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.awt.Color;

import master.diagram.Parameter;
import master.diagram.SequenceDiagram;


/*
 * Created on 26.02.2004
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 */
public class UMLObject extends DiagramObject {
		
	// ---------------- Klassenvariablen ------------------	
	
	private SequenceDiagram diagram; // Referenz zum Diagramm, in dem dieses Objekt liegt
	
	private Rectangle2D box; // die Box um den Namen
	private Line2D lifeline; // die Lebenslinie
	
	private int height = 50;	// Standardhoehe
	private int width = 90;	// Standardbreite
	
	private LinkedList arrowList; // hier werden die Nummern aller Pfeile gespeichert 
	
	// dieser Name wird in der Anzeige dargestellt
	private String nameToDraw;
	
	
	// ---------------- Konstruktoren ------------------		
	
	public UMLObject(SequenceDiagram diagram, String name){
		this(diagram, name, 50, 50, name);
	}
	
	
	public UMLObject(SequenceDiagram diagram, String name, int x, int y, String nameToDraw){
		super(name);
		this.diagram = diagram;
		this.nameToDraw = nameToDraw;
		this.x = x;
		this.y = y;
		this.arrowList = new LinkedList();
		this.box = new Rectangle2D.Float(x, y, this.width, this.height);
		this.lifeline = new Line2D.Float(x + this.width/2, 0, this.x + this.width/2, this.diagram.getLifelineLength());
	}


	// ---------------- Methoden ------------------	

	public void draw(Graphics2D graphics){
		
		graphics.setColor(Color.WHITE);
		graphics.fill(this.box);
		graphics.setColor(Color.BLACK);
		graphics.draw(this.box);		
		if (this.name.equals("0.0.0.0"))
			graphics.drawString("Internet", this.x + 5, this.y + 20);			
		else {
			if (this.nameToDraw.length() > 17) {
				graphics.drawString(this.nameToDraw.substring(0, 17), this.x + 5, this.y + 20);
				graphics.drawString(this.nameToDraw.substring(17, this.nameToDraw.length()), this.x + 5, this.y + 30);
			}
			else
				graphics.drawString(this.nameToDraw, this.x + 5, this.y + 20);
			
			if (this.name.length() > 17) {
				graphics.drawString(this.name.substring(0, 17), this.x + 5, this.y + 30);
				graphics.drawString(this.name.substring(17, this.name.length()), this.x + 5, this.y + 40);
			}
			else
				graphics.drawString(this.name, this.x + 5, this.y + 30);
		}			
		
	}
	
	public void drawLifeLines(Graphics2D graphics) {
		this.lifeline.setLine(this.lifeline.getX1(), this.lifeline.getY1(), this.lifeline.getX2(), this.diagram.getLifelineLength());		
		graphics.draw(this.lifeline);		
	}
	
	
	public void addArrow(int number){
		this.arrowList.add(new Integer(number));
		
		// die Lebenslinie verlaengern
		this.diagram.increaseLifelineLength(Parameter.arrowDistance);				
	}
	
	
	public int getWidth(){
		return this.width;
	}
	
	
	public int getHeight(){
		return this.height;
	}
	
}

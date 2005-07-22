package master.diagram.objects;

import java.awt.Graphics2D;

/*
 * Created on 26.02.2004
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 */
public abstract class DiagramObject {
	
	// linke obere Ecke
	protected int x;
	protected int y;
	
	// jedes Objekt hat einen Namen
	protected String name;
	
	
	public DiagramObject(){
		this.name = "";
		this.x = 100;
		this.y = 100;
	}
	
	
	public DiagramObject(String name){
		this.name = name;
	}

	
	public abstract void draw(Graphics2D graphics);
	
	
	public String getName(){
		return this.name;
	}
	
	
	public int getX(){
		return this.x;
	}
	
	
	public void setX(int x){
		this.x = x;
	}
	
	
	public int getY(){
		return this.y;
	}
	
	
	public void setY(int y){
		this.y = y;
	}
	
}

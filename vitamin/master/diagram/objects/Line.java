package master.diagram.objects;

import java.awt.Color;
import java.awt.Graphics2D;


/*
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 * 
 * @version	08.04.2004
 */
public class Line extends DiagramObject {
	
	// die zwei Endpunkte der Linien
	// Linien koennen grundsaetzlich an allen DiagramObjects andocken
	private DiagramObject startObject;
	private DiagramObject endObject;
	
	// eine Linie muss auch einen Fluss anzeigen können (also z.B. hier fließen gerade RTP-Daten)
	// Solch ein Fluß wird am besten durch verschiedene Farben dargestellt.
	private Color color;
	
	
	public Line(String name, DiagramObject start, DiagramObject end){		
		super(name);
		
		this.startObject = start;
		this.endObject = end;	
		
		this.color = Color.BLACK;	
	}
	
	
	public void draw(Graphics2D graphics){
		// jetzige Farbe speichern
        Color remember = graphics.getColor();

        // Farbe dieser Linie setzen
        graphics.setColor(this.color);

        // Linie zeichnen
		graphics.drawLine(this.startObject.getX(), this.startObject.getY(), this.endObject.getX(), this.endObject.getY());

        // vorherige Farbe wieder setzen
        graphics.setColor(remember);
	}
	
	
	public void setColor(Color color){
		this.color = color;
	}


    public DiagramObject getStartObject (){
        return this.startObject;
    }


    public DiagramObject getEndObject (){
        return this.endObject;
    }

}

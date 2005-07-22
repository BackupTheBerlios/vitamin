package master.diagram.objects;


import java.awt.*;

import master.diagram.Parameter;
import master.diagram.SequenceDiagram;



/*
 * Created on 04.03.2004
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 */
public class UMLArrow {

	// ---------------- Klassenvariablen ------------------	
	
	// der Pfeil geht von hier...
	private DiagramObject startObject;
	
	// ...bis hier (hier ist also die Spitze).
	private DiagramObject endObject;
	
	// die Nachricht
	private String message;

    // und die Farbe des Pfeiles (wird im Diagramm abhaengig vom Protokoll entschieden
    private Color color;

	// jeder Pfeil bekommt eine Nummer... (wird benutzt, um die y-Koordinate zu berechnen; wird ersetzt durch Zeitwert)
	private int number;
	
	// die Zeit der Nachricht (in Mikrosekunden seti 1.1.1970)
	private long messageTime;
	
	// Groesse der Pfeilspitze in Pixeln
	private int arrowHeadSize = 10;
		
	
	// Diese Variable gibt es nicht mehr, da dies Fehler verursachen wuerde bei mehreren SequenzDiagrammen gleichzeitig.
	// Jedes Sequenzdiagramm hat jetzt einen eigenen arrowCounter. Ein UMLArrow-Objekt fragt diesen bei dem Diagramm ab.
	// private static int arrowCounter = 0;
	
	// ab welchem Y fangen die Werte an?
	private static int startY = 30;
	
	// Referenz auf das Diagramm, in dem dieser Pfeil vorkommt
	private SequenceDiagram diagram;
	
	
	// ---------------- Konstruktoren ------------------	
	
	
	public UMLArrow(SequenceDiagram diagram, long timestamp, Color color, String message, DiagramObject startObject, DiagramObject endObject){
		this.color = color;
        this.message = message;
		this.startObject = startObject;
		this.endObject = endObject;
		this.number = diagram.newArrow();
		this.diagram = diagram;
		this.messageTime = timestamp;
		
		// Jeder Pfeil bekommt sowohl eine Nummer als auch eine Zeit. Dadurch ist es moeglich, waehrend der Visualisierung
		// zwischen den zwei Modi (mit Zeitstempeln und ohne) einfach zu wechseln.
	}


	// ------------------- Methoden ---------------------
	
	public void draw(Graphics2D graphics){
		int startX = this.startObject.getX() + ((UMLObject) this.startObject).getWidth()/2;
		int endX = this.endObject.getX() + ((UMLObject) this.endObject).getWidth()/2;
		int y = 0;
		
		// der Wert fuer y berechnet sich in Abaengigkeit vom Modus des Diagramms (mit Timestamps oder ohne)
		if (this.diagram.diagramIsUsingTimestamps()) {
			// mit Timestamps; die Timestamps bestimmen die Reihenfolge der Pfeile sowie die Abstaende			
			
			// Differenz von Zeit und startZeit bilden
			long diff = this.messageTime - this.diagram.getStartTime();
			
			// TODO Genauigkeit (accuracy) einbauen (sec, millisec, microsec)
			// dafuer wird die Differenz gerundet
			int accuracy = this.diagram.getAccuracy();
			
			// 1 Sekunde = scaleFactor --> 1 Mikrosekunde = scaleFactor / 1000 --> diff = scaleFactor / 1000 * diff
			y = (int) (this.diagram.getScaleFactor() / 1000 * diff); 
		}
		else {
			// ohne Timestamps; fester Abstand zwischen den Pfeilen
			y = startY + this.number * Parameter.arrowDistance;	
		}
		
		// momentane Farbe speichern
        Color remember = graphics.getColor();

        // Farbe des Pfeiles setzen
        graphics.setColor(this.color);

        // Linie zeichnen
		graphics.drawLine(startX, y, endX, y);
		
		// Pfeilspitze zeichnen
		int	[] triangleCoordsX = new int [3];
		int	[] triangleCoordsY = new int [3];
		
		triangleCoordsX[0] = endX;
		
		triangleCoordsY[0] = y;
		triangleCoordsY[1] = y - this.arrowHeadSize/2;
		triangleCoordsY[2] = y + this.arrowHeadSize/2;
		
		if(this.endObject.getX() > this.startObject.getX()){
			triangleCoordsX[1] = endX - this.arrowHeadSize;
			triangleCoordsX[2] = endX - this.arrowHeadSize;		
		}
		else{
			triangleCoordsX[1] = endX + this.arrowHeadSize;
			triangleCoordsX[2] = endX + this.arrowHeadSize;
		}
				
		graphics.fillPolygon(triangleCoordsX, triangleCoordsY, 3);
		
		// Message zeichnen
		if(this.endObject.getX() > this.startObject.getX())
			graphics.drawString(this.message, startX + 15, y - 3);
		else
			graphics.drawString(this.message, endX + 15, y - 3);


        // graphics wieder auf alte Farbe setzen
        graphics.setColor(remember);
	}
	
	
	public DiagramObject getStartObject(){
		return this.startObject;
	}
	
	
	public DiagramObject getEndObject(){
		return this.endObject;
	}
	
	
	public int getNumber(){
		return this.number;
	}
	
}

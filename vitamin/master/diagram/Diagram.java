package master.diagram;


import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import master.Master;


/**
 * Created on 26.02.2004
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 */
public abstract class Diagram extends JFrame implements WindowListener {

	// ---------------- Klassenvariablen ------------------	
	
	private Master master; // Referenz zum Master-Objekt, der dieses Diagramm erzeugt hat und mit Informationen versorgt
	
	private Timer timer;			// über diesen Timer refresht sich die Anzeige
	private TimerTask timerTask;	// gehört quasi zum Timer dazu

    private String [] protocols;    // die interessanten Protokolle
    private Color [] protocolColors;// in welchen Farben sollen die Protokolle dargestellt werden?
    
    private boolean end = false;


	// ---------------- Konstruktor ------------------
	
	public Diagram(Master master, String title, String [] protocols, Color [] protocolColors){
		super(title);

		this.master = master;

        this.protocols = protocols;
        this.protocolColors = protocolColors;

		// einige Einstellungen für das JFrame...
		this.setPreferredSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
		
		// Als WindowListener wird das Fenster selbst registriert.
		// Der WindowListener ist noetig, um darauf zu reagieren, wenn das Fenster geschlossen wird.
		this.addWindowListener(this); 
		
		this.setVisible(true);

		// der Timer sorgt dafür, dass das Diagramm in regelmäßigen
		// Zeitabständen seine Anzeige refresht.
		this.timer = new Timer();
		if(timerTask != null)
			timerTask.cancel();
		else {
			timerTask = new TimerTask() {
				public void run(){
					// Wenn die end-Variable gesetzt ist, wird der TimerTask gecancelt, d.h. nie wieder ausgeführt
					if (end) {
						this.cancel();
					}
					repaint();
				}
			};
		}		
		timer.scheduleAtFixedRate(timerTask, 0, Parameter.refreshRate);
		//try {
		//	this.finalize();
		//} catch (Throwable e) {
		//	System.out.println("error int server.diagram.Diagram: could not finalize diagram.");
		//}

 	}
	
	
	// ---------------- Methoden ------------------		
	
	public void end () {
		this.end = true;
        this.setVisible(false);
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		//super.paintComponents(g);
		paintDiagram(g);
	}
	
	
	public abstract void paintDiagram(Graphics g);


    public abstract void addNode (long time, String ip, int subnetMask, String name, String subnetIP);
    
    public abstract void addSubnet (long time, String netIP, int subnetMask, String gateway);

    public abstract void removeNode (String ip);

    public abstract void addSignal (long time, String protocol, String sourceIP, String destinationIP, String text);

    // removeSignal makes no sense

    public abstract void addConnection (long time, String protocol, String sourceIP, String destinationIP);

    public abstract void removeConnection (String protocol, String sourceIP, String destinationIP);

    public abstract void addStream (long time, String protocol, String sourceIP, String destinationIP);

    public abstract void removeStream (String protocol, String sourceIP, String destinationIP);

    public abstract void addName (long time, String protocol, String computer, String name);

    public abstract void removeName (String protocol, String computer, String name);


	// ---------------- methods of interface WindowListener ------------------
	
	public void windowActivated(WindowEvent e) {
		// do nothing (has to be implemented because this class implements WindowListener)
	}
	
	
	public void windowClosed(WindowEvent e) {
		// do nothing (has to be implemented because this class implements WindowListener)
	}
	
	
	public void windowClosing(WindowEvent e) {
		// dieses Fenster wird geschlossen. Visualisierungsphase beenden
		// (Schlessen des Fensters ist aequivalent zu Druecken de STOP-Buttons im Hauptfenster)
		
		// im Master wird die entsprechende Methode aufgerufen (dieselbe, wie wenn man STOP drueckt)
		this.master.endVisualizing();
	}
	
	
	public void windowDeactivated(WindowEvent e) {
		// do nothing (has to be implemented because this class implements WindowListener)
	}


	public void windowDeiconified(WindowEvent e) {
		// do nothing (has to be implemented because this class implements WindowListener)
	}
	
	
	public void windowIconified(WindowEvent e) {
		// do nothing (has to be implemented because this class implements WindowListener)
	}
	
	
	public void windowOpened(WindowEvent e) {
		// do nothing (has to be implemented because this class implements WindowListener)
	}


    // ---------------- Hilfsmethoden ------------------

    public Color getColor(String protocol) {
        Color color = Color.BLACK;
        for (int i = 0; i < this.protocols.length; i++){
            if (this.protocols[i].equals(protocol))
                color = this.protocolColors[i];
        }

        return color;
    }

}

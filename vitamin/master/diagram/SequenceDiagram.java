package master.diagram;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import master.Master;
import master.diagram.objects.UMLArrow;
import master.diagram.objects.UMLObject;
import master.diagram.objects.DiagramObject;


/*
 * Created on 06.04.2004
 * 
 * @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class SequenceDiagram extends Diagram implements ActionListener {

	private LinkedList arrowList;	// hier werden alle Pfeile drin gespeichert
	private Hashtable objectList;	// hier werden alle UMLObjekte drin gespeichert; Key ist der Name des Objektes

    private JScrollPane contentScrollPane; 
    private JPanel contentPane; // die ContentPane
    
    private JPanel headPane; // hierdrin liegen die buttonPane und die objectPane
    
    private JPanel objectPane; // oberes Teilfenster; hier werden die Objekte dargestellt
    
    
    private JPanel buttonPane; // liegt ganz oben; hier ist der Reset-Button drin, ausserdem alle Einstellungsmoeglichkeiten
    
    private JButton resetButton; // setzt das Diagramm zurueck, d.h. die Pfeilliste wird geloescht
    
	private JCheckBox useTimeStampsCheckbox; // hierueber kann man den Modus wahlen (Visualisierung ueber Zeitstempel oder fester Abstand)
    
    private JLabel scaleTextLeft; // hier steht "scale: 1 sec = " drin. dahinter kommt das JTextField...    
    private JTextField scaleFactor; // ... in dieses traegt man den Skalierungsfaktor ein ...
	private JLabel scaleTextRight; // ... und dahinter steht dann nocht "pixel".
	
	private JLabel accuracyLabel; // hier steht drin: "accuracy"
	private JComboBox accuracyList; // hier kann man waehlen zwischen Sekunden, Millisekunden, Mikrosekunden
		
    
    private JScrollPane arrowScrollPane;
    private JPanel arrowPane; // unteres Teilfenster; hier werden die Pfeile und Lebenslinien dargestellt
    
    private Dimension arrowPaneDimension;
    private Dimension contentPaneDimension;
    private Dimension objectPaneDimension;
    private Dimension headPaneDimension;
    private Dimension buttonPaneDimension;

    private int nextObjectX = 20;	// X-Koordinate des jeweils naechsten Objektes (das 1. Objekt steht also bei 20)
    
    private int scaleFactorValue = 50; // gibt an, wieviele Pixel eine Sekunde darstellen; variabel (durch den User veraenderbar)
    
    private static final int SECONDS = 1;
    private static final int MILLISECONDS = 2;
    private static final int MICROSECONDS = 3;
    private int accuracy = SECONDS;
    
	// Ab dieser Zeit wird gerade visualisiert. startTime ist also im Diagramm am oberen Ende der Lebenslinien.
	// Beim Druecken des Reset-Buttons wird startTime auf die aktuelle Zeit gesetzt (-> TESTEN Abweichung Java-Zeitnahme <-> C-Zeitnahme in JPcap!)
    private long startTime;
    
    // Hierueber wird angegeben, ob das Diagramm die Timestamps zur Visualisierung verwendet oder nicht.
    // Verwendet es sie nicht, werden die Nachrichten in der Reihenfolge wie sie ankommen mit einem festen Abstand dargestellt
    // Wird die Zeit verwendet, so bestimmt diese die Reihenfolge der Nachrichten. Auch die Abstaende zwischen den Nachrichten
    // werden aus den Zeiten errechnet. Der User kann dann die Skalierung veraendern (d.h. wieviel Platz im Diagramm eine Sekunde einnimmt).
    private boolean useTimestamps = false;
    
    // die Laenge der Lebenslinien der Objekte variiert mit der Anzahl der Pfeile
    private int lifelineLength = 50;
    
    // Zaehler, wie viele Pfeile momentan dargestellt werden. Wird benoetigt, um neuen Pfeilen ihre Nummer zuweisen zu koennen
    // (Pfeile haben eine Nummer, damit bei Darstellung ohne Zeitstempel die Reihenfolge stimmt).
    private int arrowCounter = 0;


	public SequenceDiagram(Master master, String title, String [] protocols, Color [] protocolColors){
		super(master, title, protocols, protocolColors);
		
		this.arrowList = new LinkedList();
	    this.objectList = new Hashtable();
	    
	    // startTime auf aktuelle Zeit setzen (in Mikrosekunden seit 1.1.1970)
	    this.startTime = new GregorianCalendar().getTimeInMillis() * 1000;
	    
		// Standardfont erzeugen
		Font myFont = new Font("SansSerif", Font.PLAIN, 10);
		
		// Content Pane erzeugen und in JScrollPane kapseln
		// gleichzeitig BorderLayout setzen. In den Norden kommen die Objekte,
		// in die Mitte die Lebenslinien und Pfeile
		this.contentPane = new JPanel(new BorderLayout());
		this.contentPaneDimension = new Dimension(550, 550);
		this.contentPane.setPreferredSize(this.contentPaneDimension);		
		this.contentScrollPane = new JScrollPane(this.contentPane);


		// oberes Teilfenster erzeugen und zu Content Pane hinzufügen
		
		//dazu erst mal headPane erzeugen und FlowLayout setzen
		FlowLayout headLayout = new FlowLayout();
		headLayout.setAlignment(FlowLayout.LEFT);
		headLayout.setHgap(0);
		headLayout.setVgap(0);		
		this.headPane = new JPanel(headLayout);
		this.headPaneDimension = new Dimension(600, 91);
		this.headPane.setPreferredSize(this.headPaneDimension);
		this.headPane.setMaximumSize(this.headPaneDimension);
		
		// objectPane erzeugen
		this.objectPane = new JPanel();
		this.objectPaneDimension = new Dimension(599, 56);
		this.objectPane.setPreferredSize(this.objectPaneDimension);		
		this.objectPane.setFont(myFont);		
		
		// Buttonpanel mit Reset-Button und Einstellungen erzeugen
		FlowLayout buttonLayout = new FlowLayout();
		buttonLayout.setAlignment(FlowLayout.LEFT);
		this.buttonPane = new JPanel(buttonLayout);
		this.buttonPaneDimension = new Dimension(599, 35);
		this.buttonPane.setPreferredSize(this.buttonPaneDimension);		
		this.resetButton = new JButton("reset");
		this.resetButton.setMnemonic('r');	// d.h., dass durch Druecken von Alt-r dieser Knopf gedrueckt wird 
		this.resetButton.setToolTipText("press to delete all displayed messages and start again from top");
		this.resetButton.addActionListener(this);
		
		this.useTimeStampsCheckbox = new JCheckBox("use timestamps");
		this.useTimeStampsCheckbox.addActionListener(this);
		
		this.scaleTextLeft = new JLabel("scale: 1 sec = ");
		this.scaleTextLeft.setPreferredSize(new Dimension(100, 20));
		this.scaleTextLeft.setHorizontalAlignment(SwingConstants.RIGHT);
		this.scaleTextRight = new JLabel("pixel");
		this.scaleFactor = new JTextField("50");
		this.scaleFactor.setPreferredSize(new Dimension(40, 20));
		this.scaleFactor.addActionListener(this);
		
		this.accuracyLabel = new JLabel("accuracy");
		this.accuracyLabel.setPreferredSize(new Dimension(100, 20));
		this.accuracyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.accuracyList = new JComboBox(new String [] {"seconds", "milliseconds", "microseconds"});
		this.accuracyList.addActionListener(this);
		
		this.buttonPane.add(this.resetButton);
		this.buttonPane.add(this.useTimeStampsCheckbox);
		this.buttonPane.add(this.scaleTextLeft);
		this.buttonPane.add(this.scaleFactor);
		this.buttonPane.add(this.scaleTextRight);
		this.buttonPane.add(this.accuracyLabel);
		this.buttonPane.add(this.accuracyList);
		
		// die Skalierungseinstellung und die accuracy-Einstellung sind standardmaessig deaktiviert
		// die Aktivierung erfolgt durch Anklicken der CheckBox (use timestamps)
		this.scaleFactor.setEnabled(false);
		this.accuracyList.setEnabled(false);
		
		// buttonPane und objectPane der headPane hinzufuegen
		this.headPane.add(this.buttonPane);
		this.headPane.add(this.objectPane);
		
		// headPane der contentPane hinzufuegen
		this.contentPane.add(this.headPane, BorderLayout.NORTH);

		
		// unteres Teilfenster erzeugen, in JScrollPane kapseln und zu Content Pane hinzufügen
		this.arrowPane = new JPanel();
		this.arrowPaneDimension = new Dimension(600, 400);
		this.arrowPane.setPreferredSize(this.arrowPaneDimension);
		this.arrowPane.setFont(myFont);
		this.arrowScrollPane = new JScrollPane(this.arrowPane);
		this.contentPane.add(this.arrowScrollPane, BorderLayout.CENTER);
		
		// der ContentPane eine Größe geben
		//this.contentPane.setSize(400, 400);
		
		// Content Pane setzen und alles fertig machen
		this.setContentPane(contentScrollPane);
		//this.setContentPane(this.contentPane);
		this.pack();
	}
	
	
	
	public void paintDiagram(Graphics graphics){
		//super.paintComponents(graphics);
				
		// UML-Objekte zeichnen
		Enumeration objects = this.objectList.elements();
		UMLObject object = null;
		while(objects.hasMoreElements()){
			object = (UMLObject) objects.nextElement();
			
			// eigentliches Obekt zeichnen (in oberes JPanel)
			object.draw((Graphics2D) this.objectPane.getGraphics());			
			
			// Lebenslinie zeichnen (in unteres Panel)
			object.drawLifeLines((Graphics2D) this.arrowPane.getGraphics());
		}
		
		// UMLArrows zeichnen
		for(int i = 0; i < this.arrowList.size(); i++){
			((UMLArrow) this.arrowList.get(i)).draw((Graphics2D) this.arrowPane.getGraphics());
		}
	
	}


    public void addNode(long time, String ip, int subnetMask, String name, String subnetIp) {
        if (!this.objectList.containsKey(ip)) {
            UMLObject object = new UMLObject(this, ip, this.nextObjectX, 10, name);

            this.nextObjectX = this.nextObjectX + Parameter.objectDistance;

            this.objectList.put(object.getName(), object);

            this.objectPaneDimension.setSize(this.objectPaneDimension.getWidth() + Parameter.objectDistance, this.objectPaneDimension.getHeight());
			this.buttonPaneDimension.setSize(this.buttonPaneDimension.getWidth() + Parameter.objectDistance, this.buttonPaneDimension.getHeight());
			this.headPaneDimension.setSize(this.headPaneDimension.getWidth() + Parameter.objectDistance, this.headPaneDimension.getHeight());
            this.contentPaneDimension.setSize(this.contentPaneDimension.getWidth() + Parameter.objectDistance, this.contentPaneDimension.getHeight());
            this.objectPane.setPreferredSize(this.objectPaneDimension);
			this.buttonPane.setPreferredSize(this.buttonPaneDimension);
			this.headPane.setPreferredSize(this.headPaneDimension);
            this.contentPane.setPreferredSize(this.contentPaneDimension);
        }
    }
    
    
    public void addSubnet (long time, String netIP, int subnetMask, String gateway) {
    	// interessiert in diesem Diagrammtyp nicht
    }


    public void removeNode(String ip) {
        this.objectList.remove(ip);
    }


    public void addSignal(long time, String protocol, String sourceIP, String destinationIP, String text) {
		//* DEBUG */ System.out.println("addSignal in SequenceDiagram started.");
		//* DEBUG */ System.out.println("sourceIP: " + sourceIP + ", destinationIP: " + destinationIP);
        // zu dem Protokoll die richtige Farbe herausfinden
        Color color = this.getColor(protocol);

        // wenn es Quelle oder Ziel nicht gibt, wird der Pfeil auch nicht eingezeichnet
        if (this.objectList.containsKey(sourceIP) && this.objectList.containsKey(destinationIP)) {
			//* DEBUG */ System.out.println("source IP and destination IP are known in sequence diagram.");
	        UMLArrow arrow = new UMLArrow(this, time, color, text, (DiagramObject) this.objectList.get(sourceIP), (DiagramObject) this.objectList.get(destinationIP));
	
	        this.arrowList.add(arrow);
			((UMLObject) arrow.getStartObject()).addArrow(arrow.getNumber());
	
			this.arrowPaneDimension.setSize(this.arrowPaneDimension.getWidth(), this.arrowPaneDimension.getHeight() + Parameter.arrowDistance);
			this.contentPaneDimension.setSize(this.contentPaneDimension.getWidth(), this.contentPaneDimension.getHeight() + Parameter.arrowDistance);
			this.arrowPane.setPreferredSize(this.arrowPaneDimension);
			this.contentPane.setPreferredSize(this.contentPaneDimension);
        }
        else {
        	// wenn es Quelle oder Ziel nicht gibt, kann man das Signal immer noch vom oder zum Internet einzeichnen (falls es das gibt)
        	if (this.objectList.containsKey("0.0.0.0")) {
				//* DEBUG */ System.out.println("sequence diagram contains \"Internet\"");				
        		if (this.objectList.containsKey(sourceIP)) {
        			// von Source ins Internet
					//* DEBUG */ System.out.println("sequence diagram does not contain destination IP. Will add arrow to the internet.");					
					
					UMLArrow arrow = new UMLArrow(this, time, color, text, (DiagramObject) this.objectList.get(sourceIP), (DiagramObject) this.objectList.get("0.0.0.0"));
	
					this.arrowList.add(arrow);
					((UMLObject) arrow.getStartObject()).addArrow(arrow.getNumber());
					
					this.arrowPaneDimension.setSize(this.arrowPaneDimension.getWidth(), this.arrowPaneDimension.getHeight() + Parameter.arrowDistance);
					this.contentPaneDimension.setSize(this.contentPaneDimension.getWidth(), this.contentPaneDimension.getHeight() + Parameter.arrowDistance);
					this.arrowPane.setPreferredSize(this.arrowPaneDimension);
					this.contentPane.setPreferredSize(this.contentPaneDimension);
        		}
        		else if (this.objectList.containsKey(destinationIP)) {
        			// vom Internet zur Destination
        			
					//* DEBUG */ System.out.println("sequence diagram does not contain source IP. Will add arrow from the internet to destination IP.");
					UMLArrow arrow = new UMLArrow(this, time, color, text, (DiagramObject) this.objectList.get("0.0.0.0"), (DiagramObject) this.objectList.get(destinationIP));
	
					this.arrowList.add(arrow);
					((UMLObject) arrow.getStartObject()).addArrow(arrow.getNumber());
					
					this.arrowPaneDimension.setSize(this.arrowPaneDimension.getWidth(), this.arrowPaneDimension.getHeight() + Parameter.arrowDistance);
					this.contentPaneDimension.setSize(this.contentPaneDimension.getWidth(), this.contentPaneDimension.getHeight() + Parameter.arrowDistance);
					this.arrowPane.setPreferredSize(this.arrowPaneDimension);
					this.contentPane.setPreferredSize(this.contentPaneDimension);
        		}
				else {
					/* DEBUG */ System.out.println("sequence diagram doesn't contain source IP, destination IP oder Internet.");        		
				}
        	}
        	
        }
    }


    public void addConnection(long time, String protocol, String sourceIP, String destinationIP) {
        // TODO werden Connections in einem Sequenzdiagramm dargestellt?
    }


    public void removeConnection(String protocol, String sourceIP, String destinationIP) {
        // TODO werden Connections in einem Sequenzdiagramm dargestellt?
    }


    public void addStream(long time, String protocol, String sourceIP, String destinationIP) {
        // hat auf ein Sequenzdiagramm keine Auswirkung
    }


    public void removeStream(String protocol, String sourceIP, String destinationIP) {
        // hat auf ein Sequenzdiagramm keine Auswirkung
    }


    public void addName(long time, String protocol, String computer, String name) {
        // TODO
    }


    public void removeName(String protocol, String computer, String name) {
        // TODO
    }



	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(this.resetButton)){
			System.out.println("reset button pressed.");			
			
			// Arrowlist loeschen
			this.arrowList.clear();
			
			// startTime auf aktuelle Zeit setzen
			this.startTime = new GregorianCalendar().getTimeInMillis() * 1000;	
			
			// die Laenge der Lebenslinien in den Objekten zuruecksetzen
			this.lifelineLength = 50;
			
			// den ArrowCounter zuruecksetzen, damit die Pfeile wieder ab oben gezeichnet werden
			this.arrowCounter = 0;
			
			// paint-Methode aufrufen, damit das Diagramm neugezeichnet wird
		}
		else if (event.getSource().equals(this.useTimeStampsCheckbox)) {		
			// Den Modus umstellen...
			this.useTimestamps = !this.useTimestamps;
				
			//... und die Einstellungsmoeglichkeiten umstellen
			this.accuracyList.setEnabled(this.useTimestamps);
			this.scaleFactor.setEnabled(this.useTimestamps);
		}
		else if (event.getSource().equals(this.scaleFactor)) {
			// scaleFaktor anpassen
			this.scaleFactorValue = new Integer(this.scaleFactor.getText()).intValue();
			
			//System.out.println("scale factor set to " + this.scaleFactor.getText());
		}
		else if (event.getSource().equals(this.accuracyList)) {
			// Genauigkeit anpassen
			if ( ((String) this.accuracyList.getSelectedItem()).equals("seconds") ) {
				this.accuracy = SECONDS;			
			}
			else if ( ((String) this.accuracyList.getSelectedItem()).equals("milliseconds") ) {
				this.accuracy = MILLISECONDS;			
			}
			else if ( ((String) this.accuracyList.getSelectedItem()).equals("microseconds") ) {
				this.accuracy = MICROSECONDS;			
			} 
		}
	}
	
	
	
	// die folgenden Methoden werden von den UMLArrow-Objekten genutzt, um die aktuellen Parameter abzufragen
	
	public int getScaleFactor() {
		return this.scaleFactorValue;
	}
	
	
	public long getStartTime() {
		return this.startTime;
	}
	
	
	public int getAccuracy() {
		return this.accuracy;
	}
	
	
	public int getLifelineLength() {
		return this.lifelineLength;
	}
	
	
	public void increaseLifelineLength(int add) {
		this.lifelineLength += add;
	}
	
	
	/** Wird aus einem UMLArrow-Objekt aufgerufen, wenn dieses angelegt wird. Der Counter wird hochgezaehlt
	 * und die Nummer des neuen Pfeiles zurueckgegeben.
	 * 
	 */
	public int newArrow() {
		return ++this.arrowCounter;
	}
	
	
	public boolean diagramIsUsingTimestamps() {
		return this.useTimestamps;
	}

}

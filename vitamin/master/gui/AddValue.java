package master.gui;

import javax.swing.*;
import java.awt.*;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class AddValue extends JFrame {

    // die GUI, die dieses Fenster erzeugt hat
    private GUI gui;

    // Objekte
    protected JButton okButton;
    protected JButton cancelButton;
    protected JLabel name;
    protected JTextField inputField;
    protected ButtonGroup radioButtons;
    protected JRadioButton incomingButton;
    protected JRadioButton outgoingButton;


    public AddValue (GUI gui, String name) {
        super(name);

        this.gui = gui;

        // Objekte erzeugen
        this.okButton = new JButton("ok");
        this.cancelButton = new JButton("cancel");
        this.name = new JLabel (name);
        this.inputField = new JTextField(10);
        this.incomingButton = new JRadioButton("destination");
        this.outgoingButton = new JRadioButton("source");
        this.radioButtons = new ButtonGroup();
        this.radioButtons.add(this.incomingButton);
        this.radioButtons.add(this.outgoingButton);

        // Label auf Fensterbreite setzen, damit es in eigener Zeile bleibt
        this.name.setSize(40, 15);

        // Layout erzeugen (einfaches FlowLayout)
        this.getContentPane().setLayout(new FlowLayout());

        // Groesse des Fensters setzen
        this.setSize(200, 120);

        // Objekte dem Fenster hinzufuegen
        this.getContentPane().add(this.name);
        this.getContentPane().add(this.inputField);
        this.getContentPane().add(this.incomingButton);
		this.getContentPane().add(this.outgoingButton);
        this.getContentPane().add(this.okButton);
        this.getContentPane().add(this.cancelButton);

        // den beiden Buttons als ActionListener die GUI zuweisen
        this.okButton.addActionListener(this.gui);
        this.cancelButton.addActionListener(this.gui);
    }
    
    
    public String getInput () {
    	return this.inputField.getText();
    }
    
    
    /** liefert den Status der Radio-Button zurück. Liefert "outgoing", wenn der outgoing-Button
     * gewählt ist und "incoming", wenn der incoming-Button gewählt ist.
     * 
     * @return
     */
    public String getInOrOut() {
    	if (this.incomingButton.isSelected())
    		return "destination";
    	return "source";
    }

}

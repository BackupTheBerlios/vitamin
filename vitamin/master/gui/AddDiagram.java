package master.gui;

import javax.swing.*;

import java.awt.*;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class AddDiagram extends JFrame {

    // die GUI, die dieses Fenster erzeugt hat
    private GUI gui;

    // die Liste der Protokolle
    private String [] diagramsData;

    // Objekte
    protected JButton okButton;
    protected JButton cancelButton;
    protected JComboBox diagramsList;
    protected JTextField nameField;


    public AddDiagram (GUI gui, String [] diagrams){
        super("add a diagram");

        this.gui = gui;
        this.diagramsData = diagrams;

        // Objekte erzeugen
        this.okButton = new JButton("ok");
        this.cancelButton = new JButton("cancel");
        this.diagramsList = new JComboBox(this.diagramsData);
        this.diagramsList.setPreferredSize(new Dimension(200, 25));
        this.nameField = new JTextField("name of the diagram");
        nameField.setPreferredSize(new Dimension(200, 25));

        // Layout erzeugen (einfaches FlowLayout)
        this.getContentPane().setLayout(new FlowLayout());

        // Groesse des Fensters setzen
        this.setSize(220, 125);

        // Objekte dem Fenster hinzufuegen
        this.getContentPane().add(this.diagramsList);
        this.getContentPane().add(this.nameField);
        this.getContentPane().add(this.okButton);
        this.getContentPane().add(this.cancelButton);

        // den beiden Buttons als ActionListener die GUI zuweisen
        this.okButton.addActionListener(this.gui);
        this.cancelButton.addActionListener(this.gui);
    }
    
    
    public String getName () {
    	return this.nameField.getText();
    }
    
    
    public String getDiagram () {
    	return (String) this.diagramsList.getSelectedItem();
    }

}

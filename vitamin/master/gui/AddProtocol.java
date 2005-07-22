package master.gui;

import javax.swing.*;
import java.awt.*;

/** kleines Fenster zur Auswahl eines Protokolls. Wird gleich
 * zu Beginn in der Klasse GUI instanziiert, ist allerdings nur sichtbar,
 * wenn im Bereich "Protocols" auf den "+"-Button gedrueckt wird.
 *
 *
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 15-05-2004
 */
public class AddProtocol extends JFrame {

    // die GUI, die dieses Fenster erzeugt hat
    private GUI gui;

    // die Liste der Protokolle
    private String [] protocolsData;

    // Objekte
    protected JButton okButton;
    protected JButton cancelButton;
    protected JList protocolsList;
    protected JScrollPane protocolsScrollList;

    protected JColorChooser colorChooser;


    public AddProtocol (GUI gui, String [] protocols){
        super("add a protocol");

        this.gui = gui;
        this.protocolsData = protocols;

        // Objekte erzeugen
        this.okButton = new JButton("ok");
        this.cancelButton = new JButton("cancel");
        this.protocolsList = new JList(protocolsData);
        this.protocolsList.setPreferredSize(new Dimension(420, 300));
        this.protocolsScrollList = new JScrollPane(this.protocolsList);
        this.protocolsScrollList.setPreferredSize(new Dimension(430, 100));

        this.colorChooser = new JColorChooser();
        this.colorChooser.setColor(Color.BLACK);

        // Layout erzeugen (einfaches FlowLayout)
        this.getContentPane().setLayout(new FlowLayout());

        // Groesse des Fensters setzen
        this.setSize(450, 515);

        // Objekte dem Fenster hinzufuegen
        this.getContentPane().add(this.protocolsScrollList);
        this.getContentPane().add(this.colorChooser);
        this.getContentPane().add(this.okButton);
        this.getContentPane().add(this.cancelButton);

        // den beiden Buttons als ActionListener die GUI zuweisen
        this.okButton.addActionListener(this.gui);
        this.cancelButton.addActionListener(this.gui);
    }


    public Color getColor() {
        Color color = colorChooser.getColor();
        this.colorChooser.setColor(Color.BLACK);

        return color;
    }

}

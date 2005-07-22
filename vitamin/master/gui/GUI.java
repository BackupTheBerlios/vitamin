package master.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Hashtable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.File;

import javax.swing.*;

import master.Master;
import global.Filter;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 18-03-2005
 *
 * GUI fuer den Master. Hier kann der Filter eingestellt werden. Ausserdem stehen hier Buttons zum
 * Versenden von Start-, Stop- und Bye-Nachrichten zur Verfuegung.
 */
public class GUI extends JFrame implements ActionListener {

    // ---------------------- Swing objects to build the GUI ------------------------------
    
    // I defined a width and a height of a field. All grid fields in the gridbag layout
    // have a size which is a multiple of this sizes. Therefore only the constants
    // are used to set the sizes.
    private final int GRIDWIDTH = 200;
    private final int GRIDHEIGHT = 80;

    // the background color of all title bars
    private Color highlightColor = new Color(255, 102, 51);

    // die kleinen Fenster zum Auswaehlen eines Ports, Diagramms, usw.
    private AddProtocol addProtocolFrame;
    private AddDiagram addDiagramFrame;
    private AddValue addPortFrame;
    private AddValue addIpFrame;

	// die Buttons
	private JButton startButton;
	private JButton stopButton;
	private JButton exitButton;
	private JButton infoButton;

	// Liste, in der alle Rechner sowie die verbundenen Kollektoren angezeigt werden
	private JScrollPane listScrollPane;
	private JList collectorList;
	private DefaultListModel listModel;

	// Label, die ganz oben die Ueberschriften enthalten
	private JLabel filterLabel;
	private JLabel collectorsLabel;

	// Label fuer die Filtereinstellungen. Jedes Label ueber eine Einstellungsmoeglichkeit
	private JLabel portsLabel;
	private JLabel ipsLabel;
	private JLabel protocolsLabel;

	// Elemente fuer die Filtereinstellungen: Ports
	private JList portList;
	private DefaultListModel portListModel;
	private JScrollPane portListScrollPane;
	private JButton addPortButton;
	private JButton removePortButton;

	// Elemente fuer die Filtereinstellungen: IPs
	private JList ipList;
	private DefaultListModel ipListModel;
	private JScrollPane ipListScrollPane;
	private JButton addIpButton;
	private JButton removeIpButton;

	// Elemente fuer die Filtereinstellungen: Protocols
	private JList protocolList;
	private DefaultListModel protocolListModel;
	private JScrollPane protocolListScrollPane;
	private JButton addProtocolButton;
	private JButton removeProtocolButton;

	// die Labels, die im GridBagLayout liegen
	private JPanel buttonPanel;
	private JPanel protocolsPanel;
	private JPanel ipsPanel;
	private JPanel portsPanel;
	private JPanel filterPanel;
	private JPanel collectorLabelPanel;
	private JPanel collectorsPanel;

    // Komponenten des DiagramPanels
    private JPanel diagramPanel;
    private JLabel diagramLabel;
    private JList diagramList;
    private DefaultListModel diagramListModel;
    private JScrollPane diagramScrollList;
    private JButton setToDiagramButton;
    private JButton addDiagramButton;
    private JButton removeDiagramButton;

    // Komponenten des LogoPanels
    private JPanel logoPanel;

    
    // ------------------------------- class variables ---------------------------------
    
    // list of protocols supported by Vitamin (given through the plugins)
    // will be set in the constructor and is not changed in this class
    private String [] protocolTypes;

    // list of diagram types supported by Vitamin
	// will be set in the constructor and is not changed in this class
    private String [] diagramTypes;

    // the Master object which created this GUI object (parent)
    private Master master;

    // list of filters; for each diagram created in the GUI a new filter object will be added to this list
    private LinkedList filters;

    // When choosing a protocol to visualise the user can also choose a color.
    // All objects, lines, ... in the diagrams belonging to a protocol will be painted in this color.
    // The colors will be held in this hashtable.
    private Hashtable protocolColors;



	//--------------------------- constructor --------------------------

	/** creates a new Vitamin GUI
	 * 
	 * @param master	a reference to the Master object which created this GUI (parent)
	 * @param protocolTypes	a list of all protocols supported by this version of Vitamin
	 * @param diagramTypes	a list of all diagrams supported by this version of Vitamin
	 */
	public GUI(Master master, String [] protocolTypes, String [] diagramTypes){
		super("Vitamin - by Fraunhofer FOKUS.SatCom");

        // setting class variables
        this.master = master;
        this.protocolTypes = protocolTypes;
        this.diagramTypes = diagramTypes;
        this.filters = new LinkedList();
        this.protocolColors = new Hashtable();

		// set size of JFrame and set JFrame not resizable
		this.setSize(new Dimension(5 * GRIDWIDTH + 40, 8 * GRIDHEIGHT + 40));
		this.setResizable(false);

        // There are a few little windows which will be shown when creating a new diagram or filter rule.
        // These windows are created here once. When clicking the appropriate button the JFrames are
        // only set visible = true or false.
        this.addProtocolFrame = new AddProtocol(this, this.protocolTypes);
        this.addDiagramFrame = new AddDiagram (this, this.diagramTypes);
        this.addPortFrame = new AddValue (this, "add a port");
        this.addIpFrame = new AddValue (this, "add an IP");

		// create GridBagLayout for the main GUI window (this JFrame)
		GridBagLayout layout = new GridBagLayout();

		// create GridBagConstraints for all JPanels lying in this GridBagLayout
		GridBagConstraints buttons = new GridBagConstraints();
		buttons.gridx = 0;
		buttons.gridy = 8;
		buttons.gridheight = 1;
		buttons.gridwidth = 5;

		GridBagConstraints collectors = new GridBagConstraints();
		collectors.gridx = 4;
		collectors.gridy = 1;
		collectors.gridheight = 7;
		collectors.gridwidth = 1;

		GridBagConstraints collectorsLabel = new GridBagConstraints();
		collectorsLabel.gridx = 4;
		collectorsLabel.gridy = 0;
		collectorsLabel.gridheight = 1;
		collectorsLabel.gridwidth = 1;

		GridBagConstraints ports = new GridBagConstraints();
		ports.gridx = 2;
		ports.gridy = 1;
		ports.gridheight = 2;
		ports.gridwidth = 2;

		GridBagConstraints ips = new GridBagConstraints();
		ips.gridx = 2;
		ips.gridy = 3;
		ips.gridheight = 2;
		ips.gridwidth = 2;

		GridBagConstraints protocols = new GridBagConstraints();
		protocols.gridx = 2;
		protocols.gridy = 5;
		protocols.gridheight = 2;
		protocols.gridwidth = 2;

		GridBagConstraints filter = new GridBagConstraints();
		filter.gridx = 0;
		filter.gridy = 0;
		filter.gridwidth = 4;
		filter.gridheight = 1;

        GridBagConstraints colors = new GridBagConstraints();
        colors.gridx = 0;
        colors.gridy = 5;
        colors.gridwidth = 2;
        colors.gridheight = 2;

        GridBagConstraints diagrams = new GridBagConstraints();
        diagrams.gridx = 0;
        diagrams.gridy = 1;
        diagrams.gridwidth = 2;
        diagrams.gridheight = 4;


		// Komponenten erzeugen und ActionListener registrieren
		this.initObjects();

		// erzeugen der Elemente in den Filtereinstellungen
		this.initFilterPanelObjects();


		// ein paar Dimensionen
		Dimension fuenfMalEins = new Dimension(5 * GRIDWIDTH, GRIDHEIGHT);
		Dimension vierMalEins = new Dimension(4 * GRIDWIDTH, GRIDHEIGHT);
		Dimension zweiMalEins = new Dimension(2 * GRIDWIDTH, GRIDHEIGHT);
		Dimension einsMalNeun = new Dimension(GRIDWIDTH, 6 * GRIDHEIGHT);
		Dimension rechtsOben = new Dimension(GRIDWIDTH, GRIDHEIGHT);


		// jetzt die Panels erzeugen und bestuecken
		
		// ButtonPanel
		
		// Whitespace; wird benutzt als Abstand zwischen Buttons
		JLabel whitespace = new JLabel();
		whitespace.setSize(30, 5);
		whitespace.setPreferredSize(new Dimension(30, 5));
		
		this.buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(fuenfMalEins);
		buttonPanel.setLayout(new FlowLayout());
		
		buttonPanel.add(this.startButton);
		buttonPanel.add(this.stopButton);
		buttonPanel.add(this.exitButton);
		buttonPanel.add(whitespace);
		buttonPanel.add(this.infoButton);


		// Layouts fuer die Filterpanels anlegen und Filterpanels bestuecken
        this.initFilterPanels();

        this.initDiagramPanel();

        this.initLogoPanel();

		this.filterPanel = new JPanel();
		filterPanel.setPreferredSize(vierMalEins);
		filterPanel.setLayout(new FlowLayout());
		filterPanel.add(this.filterLabel);

		this.collectorLabelPanel = new JPanel();
		collectorLabelPanel.setPreferredSize(rechtsOben);
		collectorLabelPanel.setLayout(new FlowLayout());
		collectorLabelPanel.add(this.collectorsLabel);

		this.collectorsPanel = new JPanel();
		collectorsPanel.setPreferredSize(einsMalNeun);
		collectorsPanel.setLayout(new FlowLayout());
		collectorsPanel.add(this.listScrollPane);


		// add all JPanels to GridBagLayout
		layout.setConstraints(filterPanel, filter);
		this.getContentPane().add(filterPanel); // filter panel in the middle from top to bottom

		layout.setConstraints(collectorLabelPanel, collectorsLabel);
		this.getContentPane().add(collectorLabelPanel); // panel with list of connected slaves at the right from top to bottom

		layout.setConstraints(portsPanel, ports);
		this.getContentPane().add(portsPanel); // part of filter panel

		layout.setConstraints(ipsPanel, ips);
		this.getContentPane().add(ipsPanel); // part of filter panel

		layout.setConstraints(protocolsPanel, protocols);
		this.getContentPane().add(protocolsPanel); // part of filter panel

		layout.setConstraints(buttonPanel, buttons);
		this.getContentPane().add(buttonPanel); // this panel is under all panels at the bottom of the JFrame from left to right and contains the main buttons

		layout.setConstraints(collectorsPanel, collectors);
		this.getContentPane().add(collectorsPanel); // panel with list of connected slaves at the right from top to bottom

        layout.setConstraints(diagramPanel, diagrams);
        this.getContentPane().add(diagramPanel); // panel with objects to create and delete diagrams at the upper left of the GUI

        layout.setConstraints(logoPanel, colors);
        this.getContentPane().add(logoPanel); // panel showing the Vitamin logo at the lower left of the GUI

		// set Layout to contentPane
		this.getContentPane().setLayout(layout);

		// set DefaultCloseOperation and make JFrame visible
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	
	
	// ------------------------------------------------- init methods -----------.-----------------------------------
	// ----------- the following methods create and arrange the Swing objects ----------------

    
	/** Creates the main objects of the GUI.
	 * 
	 */
    private void initObjects(){
    	// create the buttons at the bottom of the GUI
        this.startButton = new JButton("start");
		this.startButton.addActionListener(this);
		this.stopButton = new JButton("stop");
		this.stopButton.addActionListener(this);
		this.exitButton = new JButton("exit");
		this.exitButton.addActionListener(this);
		this.infoButton = new JButton("info");
		this.infoButton.addActionListener(this);


		this.listModel = new DefaultListModel();
		this.collectorList = new JList(this.listModel);
		Dimension collectorListDimension = new Dimension(GRIDWIDTH - 10, 6 * GRIDHEIGHT - 10);

		this.listScrollPane = new JScrollPane(this.collectorList);
		this.listScrollPane.setPreferredSize(collectorListDimension);

		Dimension labelDimension = new Dimension(2 * GRIDWIDTH - 20, 20 - 6);
		this.filterLabel = new JLabel("filter-settings");
		this.filterLabel.setPreferredSize(labelDimension);
        this.filterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.collectorsLabel = new JLabel("collectors");
		this.portsLabel = new JLabel("ports");
		this.portsLabel.setPreferredSize(labelDimension);
		this.portsLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.ipsLabel = new JLabel("IPs");
		this.ipsLabel.setPreferredSize(labelDimension);
		this.ipsLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.protocolsLabel = new JLabel("protocols");
		this.protocolsLabel.setPreferredSize(labelDimension);
		this.protocolsLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.diagramLabel = new JLabel("diagrams");
		this.diagramLabel.setPreferredSize(labelDimension);
		this.diagramLabel.setHorizontalAlignment(SwingConstants.LEFT);
    }


	/** Creates all objects on the filterPanel.
	 * The filterPanel is in the middle of the GUI from top to bottom.
	 * It contains all objects which the user can use to specify the filter
	 * (lists, buttons, ... for ports, IPs and protocols)
	 * 
	 * This method does only create the objects. They are arranged in
	 * method initFilterPanel().
	 */
    private void initFilterPanelObjects(){
        Dimension filterListDimension = new Dimension(2 * GRIDWIDTH - 80, 2 * GRIDHEIGHT - 30);

		this.portListModel = new DefaultListModel();
	    this.portList = new JList(this.portListModel);
	    this.portListScrollPane = new JScrollPane(this.portList);
	    this.portListScrollPane.setPreferredSize(filterListDimension);
	    this.addPortButton = new JButton("+");
        this.addPortButton.addActionListener(this);
	    this.removePortButton = new JButton("-");
        this.removePortButton.addActionListener(this);

	    this.ipListModel = new DefaultListModel();
	    this.ipList = new JList(this.ipListModel);
		this.ipListScrollPane = new JScrollPane(this.ipList);
		this.ipListScrollPane.setPreferredSize(filterListDimension);
		this.addIpButton = new JButton("+");
        this.addIpButton.addActionListener(this);
		this.removeIpButton = new JButton("-");
        this.removeIpButton.addActionListener(this);

		this.protocolListModel = new DefaultListModel();
		this.protocolList = new JList(this.protocolListModel);
		this.protocolListScrollPane = new JScrollPane(this.protocolList);
		this.protocolListScrollPane.setPreferredSize(filterListDimension);
		this.addProtocolButton = new JButton("+");
        this.addProtocolButton.addActionListener(this);
		this.removeProtocolButton = new JButton("-");
        this.removeProtocolButton.addActionListener(this);

        this.diagramListModel = new DefaultListModel();
        this.diagramList = new JList(this.diagramListModel);
		this.diagramList.setPreferredSize(new Dimension (180, 2 * GRIDHEIGHT - 30 - 40)); // TODO
        this.diagramScrollList = new JScrollPane(this.diagramList);
        this.setToDiagramButton = new JButton("set to diagram");
        this.setToDiagramButton.addActionListener(this);
        this.addDiagramButton = new JButton("+");
        this.addDiagramButton.addActionListener(this);
        this.removeDiagramButton = new JButton("-");
        this.removeDiagramButton.addActionListener(this);
    }

    
	/** Arranges all objects on the filterPanel.
	 * The filterPanel is in the middle of the GUI from top to bottom.
	 * It contains all objects which the user can use to specify the filter
	 * (lists, buttons, ... for ports, IPs and protocols)
	 * 
	 * This method only arranges the objects. The objectes are created in
	 * method initFilterPanelObjects() (because this method was already so long)
	 */
    private void initFilterPanels(){
        Dimension zweiMalZwei = new Dimension(2 * GRIDWIDTH, 2 * GRIDHEIGHT);
        Dimension filterPanelLabel = new Dimension(2 * GRIDWIDTH - 6, 19);
		Dimension filterPanelButtons = new Dimension((2 * GRIDWIDTH) / 5, 2 * GRIDHEIGHT - 30 - 0);
		Dimension filterPanelList = new Dimension(((2 * GRIDWIDTH) / 5) * 4, 2 * GRIDHEIGHT - 30 - 0);

        // die Filtereinstellungen brauchen jeweils nochmal ein eigenes
		// GridBagLayout, damit die Elemente (Liste, Label und die zwei Buttons)
		// vernuenftig angeordnet werden koennen.
		GridBagLayout protocolsLayout = new GridBagLayout();
		GridBagLayout ipsLayout = new GridBagLayout();
		GridBagLayout portsLayout = new GridBagLayout();

		// GridBagConstraints fuer die Liste in einer Filtereinstellungsgruppe
		GridBagConstraints filterList = new GridBagConstraints();
		filterList.gridx = 0;
		filterList.gridy = 1;
		filterList.gridheight = 3;
		filterList.gridwidth = 4;

		// GridBagConstraints fuer das Titel-Label in einer Filtereinstellungsgruppe
		GridBagConstraints filterLabel = new GridBagConstraints();
		filterLabel.gridx = 0;
		filterLabel.gridy = 0;
		filterLabel.gridheight = 1;
		filterLabel.gridwidth = 5;
		filterLabel.insets = new Insets(2,2,2,2);

		// GridBagConstraints fuer die Add-/Remove-Buttons in einer Filtereinstellungsgruppe
		GridBagConstraints filterButtons = new GridBagConstraints();
		filterButtons.gridx = 4;
		filterButtons.gridy = 1;
		filterButtons.gridheight = 3;
		filterButtons.gridwidth = 1;

        this.protocolsPanel = new JPanel();
		protocolsPanel.setPreferredSize(zweiMalZwei);

		JPanel protocolsLabel = new JPanel();
		JPanel protocolsList = new JPanel();
		JPanel protocolsButtons = new JPanel();

		protocolsLabel.setBackground(highlightColor);

		protocolsLabel.setPreferredSize(filterPanelLabel);
		protocolsList.setPreferredSize(filterPanelList);
		protocolsButtons.setPreferredSize(filterPanelButtons);

		protocolsLabel.setLayout(new FlowLayout());
		protocolsList.setLayout(new FlowLayout());
		protocolsButtons.setLayout(new FlowLayout());

		protocolsLabel.add(this.protocolsLabel);
		protocolsList.add(this.protocolListScrollPane);
		protocolsButtons.add(this.addProtocolButton);
		protocolsButtons.add(this.removeProtocolButton);

		protocolsLayout.setConstraints(protocolsLabel, filterLabel);
		protocolsPanel.add(protocolsLabel);
		protocolsLayout.setConstraints(protocolsList, filterList);
		protocolsPanel.add(protocolsList);
		protocolsLayout.setConstraints(protocolsButtons, filterButtons);
		protocolsPanel.add(protocolsButtons);

		protocolsPanel.setLayout(protocolsLayout);


		this.ipsPanel = new JPanel();
		ipsPanel.setPreferredSize(zweiMalZwei);

		JPanel ipsLabel = new JPanel();
		JPanel ipsButtons = new JPanel();
		JPanel ipsList = new JPanel();

		ipsLabel.setBackground(highlightColor);

		ipsLabel.setPreferredSize(filterPanelLabel);
		ipsButtons.setPreferredSize(filterPanelButtons);
		ipsList.setPreferredSize(filterPanelList);

		ipsLabel.setLayout(new FlowLayout());
		ipsList.setLayout(new FlowLayout());
		ipsButtons.setLayout(new FlowLayout());

		ipsLabel.add(this.ipsLabel);
		ipsList.add(this.ipListScrollPane);
		ipsButtons.add(this.addIpButton);
		ipsButtons.add(this.removeIpButton);

		ipsLayout.setConstraints(ipsLabel, filterLabel);
		ipsPanel.add(ipsLabel);
		ipsLayout.setConstraints(ipsButtons, filterButtons);
		ipsPanel.add(ipsButtons);
		ipsLayout.setConstraints(ipsList, filterList);
		ipsPanel.add(ipsList);

		ipsPanel.setLayout(ipsLayout);


		this.portsPanel = new JPanel();
		portsPanel.setPreferredSize(zweiMalZwei);

		JPanel portsLabel = new JPanel();
		JPanel portsList = new JPanel();
		JPanel portsButtons = new JPanel();

		portsLabel.setBackground(highlightColor);

		portsLabel.setPreferredSize(filterPanelLabel);
		portsList.setPreferredSize(filterPanelList);
		portsButtons.setPreferredSize(filterPanelButtons);

		portsLabel.setLayout(new FlowLayout());
		portsList.setLayout(new FlowLayout());
		portsButtons.setLayout(new FlowLayout());

		portsLabel.add(this.portsLabel);
		portsList.add(this.portListScrollPane);
		portsButtons.add(this.addPortButton);
		portsButtons.add(this.removePortButton);

		portsLayout.setConstraints(portsLabel, filterLabel);
		portsPanel.add(portsLabel);
		portsLayout.setConstraints(portsButtons, filterButtons);
		portsPanel.add(portsButtons);
		portsLayout.setConstraints(portsList, filterList);
		portsPanel.add(portsList);

		portsPanel.setLayout(portsLayout);
    }


    /** Creates all objects on the diagramPanel.
     * The diagramPanel is in the upper left of the GUI above the logoPanel.
     * It contains all objects which the user can use to create and delete diagrams (list, buttons, ...)
     */
    private void initDiagramPanel(){
		Dimension zweiMalFuenf = new Dimension (2 * GRIDWIDTH, 4 * GRIDHEIGHT);

		this.diagramPanel = new JPanel();
		this.diagramPanel.setPreferredSize(zweiMalFuenf);

        Dimension filterPanelLabel = new Dimension(2 * GRIDWIDTH - 6, 19);
		Dimension filterPanelSetButton = new Dimension(2 * GRIDWIDTH - 6, 30);
		Dimension filterPanelButtons = new Dimension((2 * GRIDWIDTH) / 5, 130);
		Dimension filterPanelList = new Dimension(((2 * GRIDWIDTH) / 5) * 4, 130);

        // das GridBagLayout fuer das DiagramPanel
        GridBagLayout diagramLayout = new GridBagLayout();

        GridBagConstraints diagramList = new GridBagConstraints();
        diagramList.gridx = 0;
        diagramList.gridy = 1;
        diagramList.gridwidth = 4;
        diagramList.gridheight = 3;

        GridBagConstraints diagramButtons = new GridBagConstraints();
        diagramButtons.gridx = 4;
        diagramButtons.gridy = 1;
        diagramButtons.gridwidth = 1;
        diagramButtons.gridheight = 3;

        GridBagConstraints diagramLabel = new GridBagConstraints();
        diagramLabel.gridx = 0;
        diagramLabel.gridy = 0;
        diagramLabel.gridwidth = 5;
        diagramLabel.gridheight = 1;

        GridBagConstraints diagramSetToDiagramButton = new GridBagConstraints();
        diagramSetToDiagramButton.gridx = 0;
        diagramSetToDiagramButton.gridy = 4;
        diagramSetToDiagramButton.gridwidth = 5;
        diagramSetToDiagramButton.gridheight = 1;


        // jetzt die Panels fuer die einzelnen Bereiche erzeugen
        JPanel diagramLabelPanel = new JPanel();
        JPanel diagramListPanel = new JPanel();
        JPanel diagramButtonsPanel = new JPanel();
        JPanel diagramSetButtonPanel = new JPanel();

		diagramLabelPanel.setBackground(highlightColor);

        diagramLabelPanel.setPreferredSize(filterPanelLabel);
        diagramListPanel.setPreferredSize(filterPanelList);
        diagramButtonsPanel.setPreferredSize(filterPanelButtons);
        diagramSetButtonPanel.setPreferredSize(filterPanelSetButton);

        diagramLabelPanel.setLayout(new FlowLayout());
        diagramListPanel.setLayout(new FlowLayout());
        diagramButtonsPanel.setLayout(new FlowLayout());
        diagramSetButtonPanel.setLayout(new FlowLayout());

        diagramListPanel.add(this.diagramScrollList);
        diagramLabelPanel.add(this.diagramLabel);
        diagramButtonsPanel.add(this.addDiagramButton);
        diagramButtonsPanel.add(this.removeDiagramButton);
        diagramSetButtonPanel.add(this.setToDiagramButton);

        diagramLayout.setConstraints(diagramLabelPanel, diagramLabel);
        diagramPanel.add(diagramLabelPanel);
        diagramLayout.setConstraints(diagramListPanel, diagramList);
        diagramPanel.add(diagramListPanel);
        diagramLayout.setConstraints(diagramButtonsPanel, diagramButtons);
        diagramPanel.add(diagramButtonsPanel);
        diagramLayout.setConstraints(diagramSetButtonPanel, diagramSetToDiagramButton);
        diagramPanel.add(diagramSetButtonPanel);

        // und in den Panels das Layout setzen
        this.diagramPanel.setLayout(diagramLayout);
    }


    /** Creates all objects on the logoPanel.
     * The logoPanel is in the lower left of the GUI under the diagramPanel.
     * It only shows the Vitamin logo. This space can be used in the future for
     * additional settings or whatever.
     */
    private void initLogoPanel() {
		Dimension zweiMalZwei = new Dimension(2 * GRIDWIDTH, 2 * GRIDHEIGHT);

		this.logoPanel = new JPanel();
		this.logoPanel.setPreferredSize(zweiMalZwei);

        this.logoPanel.setLayout(new FlowLayout());

        // load Vitamin logo and add it to the logoPanel
        File file = new File("images/vitamin_logo.png");
        ImageIcon icon = null;
        if (file != null) {
             icon = new ImageIcon(file.getAbsolutePath());
        } else {
            System.err.println("Cannot find Vitamin logo.");
        }

        JLabel logo = new JLabel(icon);
        logo.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        this.logoPanel.add(logo);
    }
    
    
    // ----------------------- public methods called from the Master ---------------------------------------


	/** Is called from Master object to display the connected slaves
	 * in the list in this GUI.
	 *
	 * @param list	list of all connected slaves
	 */
	public void setListItems(String [] list){
		for (int i = 0; i < list.length; i++)
			this.listModel.addElement(list[i]);
	}


    public void addIPToFilterSettings(String ip) {
        this.ipListModel.addElement(ip + " (source)");
        this.ipListModel.addElement(ip + " (destination)");
    }


    public void addProtocolToFilterSettings(String protocol) {
        this.protocolListModel.addElement(protocol);
    }


    public void addPortToFilterSettings(int port) {
        this.portListModel.addElement(port + " (source)");
        this.portListModel.addElement(port + " (destination)");
    }


    public void addSourcePortToFilterSettings(int port) {
        this.portListModel.addElement(port + " (source)");
    }


    public void addDestinationPortToFilterSettings(int port) {
        this.portListModel.addElement(port + " (destination)");
    }


	/** markiert einen Rechner (Kollektor) in der GUI als verbunden mit dem Server
	 *
	 * @param name
	 */
	public void setCollectorConnected(String name){
		int position;

		if (!this.listModel.contains(name)) {
			// Element gibt's noch nicht --> neu anlegen
			this.listModel.addElement(name);
		}

		position = this.listModel.indexOf(name);
		this.listModel.set(position, name + " *");
	}


	/** markiert einen Rechner, der in der GUI vorher als "connected" markiert war, als nicht verbunden
	 *
	 * @param name
	 */
	public void setCollectorNotConnected(String name){
		int position;
		try {
			position = this.listModel.indexOf(name + " *");
		} catch (ArrayIndexOutOfBoundsException e){
			// Element gibt's nicht --> nichts tun; Methode beenden
			return;
		}

		this.listModel.set(position, name);
	}	
    
    
    /** is called from Master object to get the filters specified in the GUI
     * 
     * @return an array of the filters for all diagrams
     */
    public Filter [] getFilters() {
        Filter [] array = new Filter [this.filters.size()];

        for (int i = 0; i < this.filters.size(); i++)
            array [i] = (Filter) this.filters.get(i);

        return array;
    }


	/** liefert die Namen der zur Darstellung konfigurierten Diagramme zurück.
	 *
	 * @return
	 */
	public String [] getDiagramNames() {
		String [] diagrams = new String [this.diagramListModel.size()];

		for (int i = 0; i < this.diagramListModel.size(); i++) {
			diagrams[i] = (String) this.diagramListModel.get(i);

			// in dem String steht nocht der Diagrammtyp in Klammern drin. Kuett weg!
			int klammerAufPos = diagrams[i].indexOf('(');
			diagrams[i] = diagrams[i].substring(0, klammerAufPos);
		}

		return diagrams;
	}


	public String [] getDiagramTypes() {
		String [] diagrams = new String [this.diagramListModel.size()];

		for (int i = 0; i < this.diagramListModel.size(); i++) {
			diagrams[i] = (String) this.diagramListModel.get(i);

			// jetzt nur den Teil zwischen den Klammern extrahieren
			int klammerAufPos = diagrams[i].indexOf('(');
			int klammerZuPos = diagrams[i].indexOf(')');
			diagrams[i] = diagrams[i].substring(klammerAufPos + 1, klammerZuPos);
		}

		return diagrams;
	}
	


	// ---------------------- ActionListener.actionPerformed() method -------------------------


	/** is called when an object in the GUi which registered the ActionListener causes an event 
	 *
	 */
	public void actionPerformed(ActionEvent event){
		if(event.getSource().equals(this.startButton)){
			// "Start"-Button wurde gedrueckt
			// im Server entsprechende Methode aufrufen. Diese erledigt dann alles weitere
			this.master.startVisualizing();
		}
		else if(event.getSource().equals(this.stopButton)){
			// Stop-Button wurde gedrueckt
			// im Server entsprechende Methode aufrufen. Diese erledigt dann alles weitere
			this.master.endVisualizing();
		}
		else if(event.getSource().equals(this.exitButton)){
			// "Exit"-Button wurde gedrueckt
			// im Server entsprechende Methode aufrufen. Diese erledigt dann alles weitere
			this.master.end();
		}
		else if(event.getSource().equals(this.infoButton)){
			// "info" button pressed --> show info window
			InfoWindow infoWindow= new InfoWindow();
			infoWindow.setVisible(true);
		}
		else if (event.getSource().equals(this.setToDiagramButton)) {
			//* DEBUG */ System.out.println("setToDiagramButton wurde gedrueckt");
				// erst mal gucken, ob ueberhaupt ein Diagramm ausgewaehlt ist
			if (this.diagramList.getSelectedIndices().length == 0)
			{
				// TODO Fenster mit Deppmeldung anzeigen
				/* DEBUG */ System.out.println("no diagrams selected.");
			}
			else {
				// richtigen Filter heraussuchen
				Filter filter = (Filter) this.filters.get(this.diagramList.getSelectedIndex());

				// die Ports hinzufügen
				String port;
				for (int i = 0; i < this.portListModel.getSize(); i++) {
					port = (String) this.portListModel.get(i);

					// hinten im String steht "(incoming)" oder "(outgoing)".
					// Dieser Teilstring entscheidet über die Zugehörigkeit im Filter.
					int klammerAuf = port.indexOf('(');
					int klammerZu = port.indexOf(')');

					filter.addPort(Integer.valueOf(port.substring(0, klammerAuf -1)).intValue(), port.substring(klammerAuf + 1, klammerZu));
					//* DEBUG */ System.out.println("Port zum Diagrammfilter hinzugefuegt: " + Integer.valueOf(port.substring(0, klammerAuf - 1)).intValue() + ", " + port.substring(klammerAuf + 1, klammerZu));
				}

				// die IPs hinzufügen
				InetAddress ip = null;;
				for (int i = 0; i < this.ipListModel.getSize(); i++) {
					String ipAsString = (String) this.ipListModel.get(i);

					int klammerAuf = ipAsString.indexOf('(');
					int klammerZu = ipAsString.indexOf(')');

					try {
						ip = InetAddress.getByName(ipAsString.substring(0, klammerAuf - 1));
						filter.addIP(ip, ipAsString.substring(klammerAuf + 1, klammerZu));
						//* DEBUG */ System.out.println("IP zum Diagrammfilter hinzugefuegt: " + ip.getHostAddress());
					} catch (UnknownHostException e) {
						System.out.println("error in server.gui.GUI.actionPerformed: IP in filter settings is unknown in the network (UnknownHostException): " + ipAsString.substring(0, klammerAuf - 1));
					}
				}

				// die Protokolle hinzufuegen
				for (int i = 0; i < this.protocolListModel.getSize(); i++) {
					String protocol = (String) this.protocolListModel.get(i);

					// TODO die Farben sind in dem Feld protocolColors gespeichert
					Color color = (Color) protocolColors.get(protocol);

					filter.addProtocol(protocol, color);
					//* DEBUG */ System.out.println("Protokoll zum Diagrammfilter hinzugefuegt: " + (String) this.protocolListModel.get(i) + ", Farbe: " + color.toString());
				}
			}
		}

		// ----------- die "+"-Buttons ----------------

		else if (event.getSource().equals(this.addProtocolButton)){
			// der "+"-Button im Protocols-Bereich wurde gedrueckt
			// --> kleines Auswahlfenster sichtbar machen
			this.addProtocolFrame.setVisible(true);
		}
		else if (event.getSource().equals(this.addIpButton)){
			// der "+"-Button im IPs-Bereich wurde gedrueckt
			// --> kleines Auswahlfenster sichtbar machen
			this.addIpFrame.setVisible(true);
		}
		else if (event.getSource().equals(this.addPortButton)){
			// der "+"-Button im Ports-Bereich wurde gedrueckt
			// --> kleines Auswahlfenster sichtbar machen
			this.addPortFrame.setVisible(true);
		}
		else if (event.getSource().equals(this.addDiagramButton)){
			// der "+"-Button im Diagram-Bereich wurde gedrueckt
			// --> kleines Auswahlfenster sichtbar machen
			this.addDiagramFrame.setVisible(true);
		}

		// ----------- die "-"-Buttons ----------------

		else if (event.getSource().equals(this.removeProtocolButton)){
			// der "-"-Button im Protocols-Bereich wurde gedrueckt
			int [] selection = this.protocolList.getSelectedIndices();
			this.protocolListModel.removeRange(selection[0], selection[selection.length - 1]);
		}
		else if (event.getSource().equals(this.removeIpButton)){
			// der "-"-Button im IPs-Bereich wurde gedrueckt
			int [] selection = this.ipList.getSelectedIndices();
			this.ipListModel.removeRange(selection[0], selection[selection.length - 1]);
		}
		else if (event.getSource().equals(this.removePortButton)){
			// der "-"-Button im Ports-Bereich wurde gedrueckt
			int [] selection = this.portList.getSelectedIndices();
			this.portListModel.removeRange(selection[0], selection[selection.length - 1]);
		}
		else if (event.getSource().equals(this.removeDiagramButton)){
			// der "-"-Button im Diagram-Bereich wurde gedrueckt
			int [] selection = this.diagramList.getSelectedIndices();
			for (int i = 0; i < selection.length; i++){
				this.diagramListModel.remove(selection[i]);

				// die Filter der geloeschten Diagramme muessen auch geloescht werden
				this.filters.remove(selection[i]); // TODO ist diese Angabe nicht quatsch?
			}
		}

		// ----------- Buttons im AddProtocol-Fenster ----------------

		else if (event.getSource().equals(this.addProtocolFrame.okButton)){
			// ausgewaehltes Element der Liste in der Haupt-GUI hinzufuegen
			Object [] selectedProtocols = this.addProtocolFrame.protocolsList.getSelectedValues();
			for (int i = 0; i < selectedProtocols.length; i++) {
				// Protokoll der Protokollliste in der Haupt-GUI hinzufuegen
				this.protocolListModel.addElement(selectedProtocols [i]);

				// die diesem Protokoll zugewiesene Farbe speichern
				protocolColors.put((String) selectedProtocols[i], addProtocolFrame.getColor());

				// pruefen, ob das Protokollplugin Angaben ueber Ports
				// enthaelt. Diese werden ausgelesen und dem Filter hinzugefuegt
				this.master.addPortsFromPlugin((String) selectedProtocols [i]);
			}

			// zum Schluss wird das Fenster wieder unsichtbar gemacht
			this.addProtocolFrame.setVisible(false);
		}
		else if (event.getSource().equals(this.addProtocolFrame.cancelButton)){
			// bricht der User die Aktion durch ein Cancel ab, wird das Fenster einfach
			// wieder unsichtbar gemacht
			this.addProtocolFrame.setVisible(false);
		}

		// ----------- Buttons im AddPort-Fenster ----------------

		else if (event.getSource().equals(this.addPortFrame.okButton)){
			// gewählten Port der Liste hinzufügen. Der Port wird als String gespeichert,
			// da er auch "EVEN" oder "ODD" enthalten kann
			String chosenPort = this.addPortFrame.getInput();
			String inOut = this.addPortFrame.getInOrOut();
			this.portListModel.addElement(chosenPort + " (" + inOut + ")");

			// zum Schluss wird das Fenster wieder unsichtbar gemacht
			this.addPortFrame.setVisible(false);
		}
		else if (event.getSource().equals(this.addPortFrame.cancelButton)){
			// bricht der User die Aktion durch ein Cancel ab, wird das Fenster einfach
			// wieder unsichtbar gemacht
			this.addPortFrame.setVisible(false);
		}

		// ----------- Buttons im AddDiagram-Fenster ----------------

		else if (event.getSource().equals(this.addDiagramFrame.okButton)){
			// gewaehltes Diagramm der Liste hinzufuegen
			String name = this.addDiagramFrame.getName();
			String type = this.addDiagramFrame.getDiagram();

			this.diagramListModel.addElement(name + " (" + type + ")");

			// Filter für dieses Diagramm anlegen
			// der Filter wird hinten angefuegt, genau wie das Diagramm in der
			// Diagrammliste. So bleibt die Zuordnung möglich
			this.filters.add(new Filter());

			// zum Schluss wird das Fenster wieder unsichtbar gemacht
			this.addDiagramFrame.setVisible(false);
		}
		else if (event.getSource().equals(this.addDiagramFrame.cancelButton)){
			// bricht der User die Aktion durch ein Cancel ab, wird das Fenster einfach
			// wieder unsichtbar gemacht
			this.addDiagramFrame.setVisible(false);
		}

		// ----------- Buttons im AddIP-Fenster ----------------

		else if (event.getSource().equals(this.addIpFrame.okButton)){
			// eingegebene IP der Liste in der Haupt-GUI hinzufuegen
			String ip = this.addIpFrame.getInput();
			String inOut = this.addPortFrame.getInOrOut();
			this.ipListModel.addElement(ip + " (" + inOut + ")");

			// zum Schluss wird das Fenster wieder unsichtbar gemacht
			this.addIpFrame.setVisible(false);
		}
		else if (event.getSource().equals(this.addIpFrame.cancelButton)){
			// bricht der User die Aktion durch ein Cancel ab, wird das Fenster einfach
			// wieder unsichtbar gemacht
			this.addIpFrame.setVisible(false);
		}
	}

}

package master;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.awt.*;

import master.connection.SignalingConnection;
import master.connection.DataReader;
import master.connection.MessageListenerInterface;
import master.diagram.Diagram;
import master.diagram.NetworkDiagram;
import master.diagram.Parameter;
import master.diagram.SequenceDiagram;
import master.gui.GUI;
import master.topology.TopologyFileReader;
import master.topology.structure.Topology;
import master.topology.structure.Subnet;
import master.topology.structure.Node;
import master.topology.structure.types.SubnetTypeType;
import global.XMLPluginParser;
import global.Filter;
import global.pluginStructure.Plugin;
import global.pluginStructure.Condition;
import global.messages.*;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 05-05-2004
 *
 * zentrale Klasse des Servers. Enthält die Ablaufsteuerung etc.
 */
public class Master implements MessageListenerInterface {
	
	private TopologyFileReader topologyFileReader;
	private Topology topology;		// die Netzwerktopologie
	
	// um besser mit der Topologie arbeiten zu koennen (z.B. in den Streams) wird eine Hashtable
	// aufgebaut, in der zu jedem Knoten alle Verbindungen liegen
	// jedes Element der Hashtable ist eine LinkedList, die aus Strings (die Zielknoten) besteht. Key der Hashtable ist jeweils die IP eines Knotens (als String)
	protected Hashtable connections;
	
	
	private GUI gui;
	
	private LinkedList sConnections;
	private DataReader dataReader;
	
	private Diagram [] diagrams;

    // bietet Zugriff auf die Protokolle
    private XMLPluginParser plugins;

    // hierdrin werden die verbundenen Kollektoren als InetAddress gespeichert
    private LinkedList collectors;

    // der aus allen Diagrammen zusammengesetzte Filter
    private Filter completeFilter;

    // die Filter für die einzelnen Diagramme
    // (der Filter am Index i gehört zum Diagram an Stelle i im diagrams-Feld)
    private Filter [] filters;
    
    private StreamManager streamManager;
    
    // die IP-Adresse, auf der der Server auf Verbindungen von den Clients wartet.
    private InetAddress serverIP;
    
    // gibt die IP-Version an, die der Master gerade verwendet
    private int ipVersion = 4;


	public Master(String serverIP, String topologyFilePath){
        try {
			this.serverIP = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e1) {
			System.out.println("error in server.Server: given IP address is unknown.");
			System.exit(1);
		}
        
        this.connections = new Hashtable();
        
        this.collectors = new LinkedList();
        this.completeFilter = new Filter();
        
        this.sConnections = new LinkedList();

		this.topologyFileReader = new TopologyFileReader();
		
		// Netzwerktopologie einlesen
		try {
			this.topology = this.topologyFileReader.readFile(topologyFilePath);
		} catch (FileNotFoundException e) {
			System.out.println("error in server.Server: the given topology file name could not be found. Program will exit.");
            System.exit(1);
		} catch (IOException e) {
			System.out.println("error in server.Server: the given topology file name could not be handled. Program will exit.");
            System.exit(1);
		}				
		
		// setzen, welche IP-Version hier gerade verwendet wird
		// HINWEIS: die IP-Versionen des visualisierten Verkehrs und dem Datenverkehr, der durch Vitamin entsteht, koennen
		// durchaus verschieden sein! Die ipVersion-Variable legt fest, welche vitaminD-Nachrichten der Master verwirft.  
		if (this.topology.getSubnet(0).getNode(0).getIp().contains(":")) {
			this.ipVersion = 6;
		}
		
		// Protokollnamen auslesen
        this.plugins = new XMLPluginParser();
        String [] protocolNames = this.plugins.getPluginNames();

        // TODO Diagrammtypen irgendwoherholen (Verzeichnis auslesen?)
        String [] diagramTypes = new String [] {"sequence diagram", "networktopology diagram"};

		// DataReader anlegen und starten
		this.dataReader = new DataReader(this, this.serverIP);
		this.dataReader.start();
		
		this.streamManager = new StreamManager(this.topology, this);

        // GUI anzeigen
        this.gui = new GUI(this, protocolNames, diagramTypes);

        // die IPs aus der Topologiedatei werden automatisch in die IP-Liste
        // in der GUI geschrieben, da sie ja mitgelesen werden sollen
        // --> eigentlich nervig, dass die immer da stehen --> erstmal weg
        /*Subnet [] subnets = this.topology.getSubnet();
        for (int i = 0; i < subnets.length; i++) {
        	Node [] nodes = subnets[i].getNode();
            for (int j = 0; j < nodes.length; j++)
                this.gui.addIPToFilterSettings(nodes[j].getIp());
        }*/

        // auf Kollektoren warten, die sich verbinden wollen
        this.listen();
	}
	
	
	private void listen () {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
				
		try {
			serverSocket = new ServerSocket (global.ConnectionConstants.tcpPort, 50, this.serverIP);
		} catch (IOException e) {
			System.out.println("error in server.Server.listen: could not bind ServerSocket. Collectors can't connect. Program will exit.");
			// e.printStackTrace();
			System.exit(1);			
		}
		
		while (true) {
			// accept a Request from a collector
			try {
				clientSocket = serverSocket.accept();
			
				// diese Verbindung wird jetzt in einen eigenen Thread gepackt und da weiter bearbeitet
				SignalingConnection sConnection = new SignalingConnection(this, clientSocket);
				sConnection.start();
				
				// eine Referenz auf diesen Thread wird in einer LinkedList gespeichert, die die Verbidungen zu allen Kollektoren enthaelt
				this.sConnections.add(sConnection);				
			} catch (IOException e1) {
				System.out.println("error in server.Server.listen: error while accepting client request.");				
				// e1.printStackTrace();
			}
		}
	}



    /** wird aus der GUI aufgerufen, wenn die Visualisierung starten soll
	 * 
	 *
	 */
	public void startVisualizing(){
        // Filter auslesen
        this.filters = this.gui.getFilters();
        
        // completeFilter reseten (es koennten ja Einstellungen veraendert worden sein
        this.completeFilter.reset();

        // aus den diagrammspezifischen Filtern einen kompletten Filter bauen
        // dazu ueber alle Diagrammfilter iterieren und den jeweiligen Inhalt dem
        // completeFilter hinzufuegen
        for (int i = 0; i < this.filters.length; i++) {
            LinkedList list = null;

            list = filters[i].getSourcePorts();
            for  (int j = 0; j < list.size(); j++)
                this.completeFilter.addPort(((Integer)list.get(j)).intValue(), "source");

            list = filters[i].getDestinationPorts();
            for  (int j = 0; j < list.size(); j++)
                this.completeFilter.addPort(((Integer)list.get(j)).intValue(), "destination");

            list = filters[i].getSourceIps();
            for  (int j = 0; j < list.size(); j++)
                this.completeFilter.addIP(((InetAddress)list.get(j)), "source");

            list = filters[i].getDestinationIps();
            for  (int j = 0; j < list.size(); j++)
                this.completeFilter.addIP(((InetAddress)list.get(j)), "destination");

            list = filters[i].getProtocols();
            LinkedList colors = filters[i].getProtocolColors();
            for  (int j = 0; j < list.size(); j++)
                this.completeFilter.addProtocol((String)list.get(j), (Color) colors.get(i));
        }



		String [] diagramNames = this.gui.getDiagramNames();
		String [] diagramTypes = this.gui.getDiagramTypes();
		
		this.diagrams = new Diagram [diagramNames.length];

		LinkedList protocolList = this.completeFilter.getProtocols();
        String [] protocols = new String[protocolList.size()];
        for (int i = 0; i < protocolList.size(); i++) {
            protocols [i] = (String) protocolList.get(i);
        }

        for (int i = 0; i < diagramNames.length; i++) {
			if (diagramTypes [i].equals("sequence diagram")) {
				this.diagrams[i] = new SequenceDiagram(this, diagramNames[i], protocols, filters[i].getProtocolColorsAsArray());
            }
			else if (diagramTypes [i].equals("networktopology diagram")) {
				this.diagrams[i] = new NetworkDiagram(this, diagramNames[i], protocols, filters[i].getProtocolColorsAsArray());
            }
            else
                System.out.println("unknown diagram type");
		}

        // Netzwerktopologie in die Diagramme geben

        // dazu erst mal alle Subnetze auslesen
        Subnet [] subnets = topology.getSubnet();
        
        // Dieser String gibt nie netIP des Top-Elementes Internet an
        String internet = "0.0.0.0";

        // ueber alle Subnetze iterieren und die enthaltenen Knoten behandeln
        int mask;
        for (int i = 0; i < subnets.length; i++) {        	
        	Node [] nodes = subnets[i].getNode();
        	// Switch [] switches = subnets[i].get_switch(); Switches gibt's nicht mehr
        	//* DEBUG */ System.out.println("Subnetz " + i + " hat " + nodes.length + " Knoten.");        	
        	
        	// ueber alle Diagramme iterieren, um jedem Diagramm die Topologie "einspeisen" zu koennen 
        	for (int j = 0; j < this.diagrams.length; j++) {
                //System.out.println("j " + j + ", i " + i);
                //System.out.println("anzahl subnetze: " + subnets.length);
                //System.out.println("IP " + subnets[i].getIp());
                //System.out.println("nodes " + subnets[i].getNodeCount());
                //System.out.println("IP erster Knoten: " + subnets[i].getNode()[0].getIp());
        		if (subnets[i].getGateway().equals("internet")) {
					// das oberste Subnetz. Hier wird erst mal nur der Knoten Internet ins Diagramm gegeben.
                	
					// einen Knoten hinzufuegen, der dass Internet symbolisiert
					// Unterscheidung IPv4 und IPv6
					if (subnets[i].getIp().contains(":")) {
						// IPv6
						internet = "0:0:0:0:0:0:0:0";
					}
					
					this.diagrams[j].addNode(Parameter.TIME_UNDEFINED, internet, 0, "Top", internet);
					
					if (!this.connections.containsKey(internet))
						this.connections.put(internet, new LinkedList()); // key, value
        		}        			        			
        		
        		// in einem Ethernet-Subnetz gibt es eine beliebiege Menge Nodes.
        		// Jeder Knoten ist mit dem fuer das Subnetz definierten Gateway verbunden.
                    
				mask = subnets[i].getMask();

        		// hierdrin wird die Nummer des Routers gespeichert
        		//int router = -1;
        		String gateway = subnets[i].getGateway(); // gateway ersetzt quasi router
        		    
        		// Subnetz hinzufuegen
        		// Das Subnetz wird aufgrund des Gateways an die richtige Stelle im Diagramm positioniert.
        		// Das Gateway MUSS vorher bereits im Diagramm enthalten sein (es sei denn: gateway="internet")
				if (!subnets[i].getGateway().equals("internet")) {
        			this.diagrams[j].addSubnet(Parameter.TIME_UNDEFINED, subnets[i].getIp(), subnets[i].getMask(), subnets[i].getGateway());
        			
					// Knoten allen Diagrammen und der Verbindungs-Hashtable hinzufuegen
					for (int k = 0; k < nodes.length; k++) {
						this.diagrams[j].addNode(Parameter.TIME_UNDEFINED, nodes[k].getIp(), mask, nodes[k].getName(), subnets[i].getIp());
						
						if (!this.connections.containsKey(nodes[k].getIp()))
							this.connections.put(nodes[k].getIp(), new LinkedList()); // key, value

						//if (nodes[k].getType() != null)
						//	router = k;
					}
				}
				
				if (subnets[i].getGateway().equals("internet")) {	
					// Die Knoten aus dem Internet-Subnetz muessen mit einer anderen Maske in das Diagramm gegeben werden, damit
					// sie auch im Internet angelegt werden.
					
					// Knoten allen Diagrammen und der Verbindungs-Hashtable hinzufuegen
					for (int k = 0; k < nodes.length; k++) {
						this.diagrams[j].addNode(Parameter.TIME_UNDEFINED, nodes[k].getIp(), 0, nodes[k].getName(), internet);
						
						if (!this.connections.containsKey(nodes[k].getIp()))
							this.connections.put(nodes[k].getIp(), new LinkedList()); // key, value

							//if (nodes[k].getType() != null)
							//	router = k;
						}
				}

                // Vom Gateway gehen Verbindungen zu allen Nodes in diesem Subnetz
                for (int k = 0; k < nodes.length; k++) {
                    if (gateway.equals("internet")) {
						// Sonderbehandlung bei gateway="internet"
						this.diagrams[j].addConnection(Parameter.TIME_UNDEFINED, "", internet, nodes[k].getIp());
						((LinkedList) this.connections.get(internet)).add(nodes[k].getIp());
						((LinkedList) this.connections.get(nodes[k].getIp())).add(internet);	
                    }
                    else {
                    	//* DEBUG */ System.out.println("IP: " + nodes[k].getIp() + ", subnet: " + subnets[i].getIp());
						this.diagrams[j].addConnection(Parameter.TIME_UNDEFINED, "", gateway, nodes[k].getIp());
                    	((LinkedList) this.connections.get(gateway)).add(nodes[k].getIp());
	 					((LinkedList) this.connections.get(nodes[k].getIp())).add(gateway);
                    }
                }
                
                /*else if (subnets[i].getType().getType() == SubnetTypeType.INTERNET_TYPE) {
                    // ein Internet enthält mehrere Router (mindestens zwei), die irgendwie
                    // alle miteinander verbunden sind (vollvermascht). Ein Internet
                    // enthält NUR Router.
                    
                    // einen Knoten hinzufuegen, der dass Internet symbolisiert
                    this.diagrams[j].addNode(Parameter.TIME_UNDEFINED, "0.0.0.0", 1, "Top");
                    
					// und auch in die Hashtable
					if (!this.connections.containsKey("0.0.0.0"))
						this.connections.put("0.0.0.0", new LinkedList()); // key, value						
					
					
                    // die Router in dem Internet dem Diagramm hinzufuegen
                    for (int k = 0; k < nodes.length; k++) {
                        this.diagrams[j].addNode(Parameter.TIME_UNDEFINED, nodes[k].getIp(), nodes[k].getMask(), nodes[k].getName());
                        
						// und auch in die Hashtable
						if (!this.connections.containsKey(nodes[k].getIp()))
							this.connections.put(nodes[k].getIp(), new LinkedList()); // key, value
                    }

                    // Verbindungen vom Internet zu allen Routern hinzufuegen
					for (int k = 0; k < nodes.length; k++) {
						this.diagrams[j].addConnection(Parameter.TIME_UNDEFINED, "", "0.0.0.0", nodes[k].getIp());
						
						// und auch in die Hashtable
						((LinkedList) this.connections.get("0.0.0.0")).add(nodes[k].getIp());
						((LinkedList) this.connections.get(nodes[k].getIp())).add("0.0.0.0");
					}
                    
                    // vollvermaschen. Dazu wird ueber alle Knoten iteriert.
                    // Bei jeder Iteration werden Verbindungen zu allen nachfolgenden
                    // Knoten erzeugt.
                    /*for (int k = 0; k < nodes.length; k++) {
                        for (int l = k + 1; l < nodes.length; l++) {
                            this.diagrams[j].addConnection("", nodes[l].getIp(), nodes[k].getIp());
                        }
                    }/

                }*/
        	}
        }
        

		// "START" an die Kollektoren senden
        for (int i = 0; i < this.sConnections.size(); i++) {
        	((SignalingConnection) this.sConnections.get(i)).sendSignalMessage("START", (InetAddress) this.collectors.get(i), this.completeFilter);
        }        
	}
	
	
	/** wird aus der GUI aufgerufen, wenn die Visualisierung beendet werden soll
	 * 
	 *
	 */
	public void endVisualizing(){
		// nur abbrechen, wenn es auch etwas abzubrechen gibt (also wenn Diagramme angezeigt werden)
		if (this.diagrams != null) {
			System.out.println("Sending STOP to all slaves.");		
			
			// "STOP" an die Kollektoren senden
	        this.sendToAll("STOP");
	        
			// Diagramme beenden
			for (int i = 0; i < this.diagrams.length; i++) {
				this.diagrams[i].end();
			}
		}
		else {
			/* DEBUG */ System.out.println("STOP button pressed while showing no diagrams. Will do nothing...");			
		}
	}


    /** wird in der GUI ein Protokoll hinzugefuegt, dann muessen im Filter
     * die entsprechenden Ports eingestellt werden. Diese Aufgabe uebernimmt
     * diese Methode.
     * Sie ueberprueft das Plugin auf fields, die als Namen "source port" oder
     * "destination port" haben und fuegt ggf. den entsprechenden Wert dem Filter
     * hinzu.
     *
     * @param protocol
     */
    public void addPortsFromPlugin(String protocol) {
        Plugin plugin = this.plugins.getPlugin(protocol);

        this.addPortsFromFieldsToGUI(plugin.getClassify().getConditionGroup().getCondition());

        global.pluginStructure.Choice [] choices = plugin.getClassify().getConditionGroup().getChoice();
        for (int i = 0; i < choices.length; i++) {
            this.addPortsFromFieldsToGUI(choices[i].getConditionGroup().getCondition());            
        }
    }


    private void addPortsFromFieldsToGUI (Condition [] fields) {
        // Ports werden immer binaer angegeben, da TCP und UDP halt nun mal binaer sind
                
        for (int i = 0; i < fields.length; i++) {
        	if (fields[i].getBinaryPointerWithValue() != null) {
	        	///* DEBUG */ System.out.println("Name des Feldes: " + fields[i].getName());
	        	
	            if (fields[i].getBinaryPointerWithValue().getValue().equals("EVEN") || fields[i].getBinaryPointerWithValue().getValue().equals("ODD")) {
	                // TODO was ist, wenn in dem field EVEN oder ODD drinsteht?
	            }
	            else if (fields[i].getBinaryPointerWithValue().getName().equals("source port") ) {
	                ///* DEBUG */ System.out.println("source port found.");
	                ///* DEBUG */ System.out.println("port " + fields[i].getValue() + ".");
	                this.gui.addSourcePortToFilterSettings(Integer.parseInt(fields[i].getBinaryPointerWithValue().getValue()));
	            }
	            else if (fields[i].getBinaryPointerWithValue().getName().equals("destination port")) {
	                ///* DEBUG */ System.out.println("destination port found.");
	                ///* DEBUG */ System.out.println("port " + fields[i].getValue() + ".");
	                this.gui.addDestinationPortToFilterSettings(Integer.parseInt(fields[i].getBinaryPointerWithValue().getValue()));
	            }
        	}
        }
    }


	/** wird aus der GUI aufgerufen, wenn das Programm beendet werden soll
	 * 
	 *
	 */
	public void end(){
		// "BYE" an die Kollektoren senden
        this.sendToAll("BYE");

		// Connection beenden
		for (int i = 0; i < this.sConnections.size(); i++) {		
			((SignalingConnection) this.sConnections.get(i)).end();
		}

		// Diagramme beenden, wenn welche geoffnet sind
		if (this.diagrams != null) {
			for (int i = 0; i < this.diagrams.length; i++) {
				this.diagrams[i].end();
			}
		}

		// TODO GUI beenden (muss ich das?)
		
		// Programm beenden
		System.exit(0);
	}
	
	
	private void sendToAll(String message) {
		for (int i = 0; i < this.sConnections.size(); i++) {		
			((SignalingConnection) this.sConnections.get(i)).sendSignalMessage(message, (InetAddress) this.collectors.get(i), null);
		}
	}


    public void dataMessageArrived(Message message) {
    	//* DEBUG */ System.out.println("Hoppala, eine DataMessage is angekommen. Alaaf!.");
        if (message instanceof MessageMessage) {
            // über alle Diagramme iterieren und entsprechende Methode aufrufen
			/* DEBUG */ System.out.println("MessageMessage from " + ((MessageMessage) message).getSource().getHostAddress() + " to "+ ((MessageMessage) message).getDestination().getHostAddress() + ".");
			if ((this.ipVersion == 6 && ((MessageMessage) message).getSource().getHostAddress().contains(":")) || ( (this.ipVersion == 4 && ((MessageMessage) message).getSource().getHostAddress().contains(".") ) ) )
			{
	            for (int i = 0; i < this.diagrams.length; i++) {
	                MessageMessage mm = (MessageMessage) message;
	                this.diagrams[i].addSignal(mm.getTimeStamp(), mm.getProtocol(), mm.getSource().getHostAddress(), mm.getDestination().getHostAddress(), mm.getMessage());
	            }
			}
        }
        else if (message instanceof ConnectionMessage) {
            // über alle Diagramme iterieren und entsprechende Methode aufrufen
			/* DEBUG */ System.out.println("ConnectionMessage.");
			if ((this.ipVersion == 6 && ((ConnectionMessage) message).getSource().getHostAddress().contains(":")) || ( (this.ipVersion == 4 && ((ConnectionMessage) message).getSource().getHostAddress().contains(".") ) ) )
			{
	            for (int i = 0; i < this.diagrams.length; i++) {
	                ConnectionMessage mm = (ConnectionMessage) message;
	                this.diagrams[i].addConnection(mm.getTimeStamp(), mm.getProtocol(), mm.getSource().getHostAddress(), mm.getDestination().getHostAddress());
	            }
			}
        }
        else if (message instanceof ConnectionEndMessage) {
            // über alle Diagramme iterieren und entsprechende Methode aufrufen
			/* DEBUG */ System.out.println("ConnectionEndMessage.");
			if ((this.ipVersion == 6 && ((ConnectionEndMessage) message).getSource().getHostAddress().contains(":")) || ( (this.ipVersion == 4 && ((ConnectionEndMessage) message).getSource().getHostAddress().contains(".") ) ) )
			{
	            for (int i = 0; i < this.diagrams.length; i++) {
	                ConnectionEndMessage mm = (ConnectionEndMessage) message;
	                this.diagrams[i].removeConnection(mm.getProtocol(), mm.getSource().getHostAddress(), mm.getDestination().getHostAddress());
	            }
			}
        }
        else if (message instanceof StreamMessage) {
            // über alle Diagramme iterieren und entsprechende Methode aufrufen
			/* DEBUG */ System.out.println("StreamMessage from " + message.getSender().getHostAddress() + ".");
			if ((this.ipVersion == 6 && ((StreamMessage) message).getSource().getHostAddress().contains(":")) || ( (this.ipVersion == 4 && ((StreamMessage) message).getSource().getHostAddress().contains(".") ) ) )
			{
            	this.streamManager.newStreamMessage((StreamMessage) message);
			}
            /*for (int i = 0; i < this.diagrams.length; i++) {
                StreamMessage mm = (StreamMessage) message;
                this.diagrams[i].addStream(mm.getProtocol(), mm.getSource().getHostAddress(), mm.getDestination().getHostAddress());
            }*/
        }
        else if (message instanceof StreamEndMessage) {
            // über alle Diagramme iterieren und entsprechende Methode aufrufen
			/* DEBUG */ System.out.println("StreamEndMessage from " + message.getSender().getHostAddress() + ".");
			if ((this.ipVersion == 6 && ((StreamEndMessage) message).getSource().getHostAddress().contains(":")) || ( (this.ipVersion == 4 && ((StreamEndMessage) message).getSource().getHostAddress().contains(".") ) ) )
			{
				this.streamManager.newStreamEndMessage((StreamEndMessage) message);
        	} 
            /*for (int i = 0; i < this.diagrams.length; i++) {
                StreamEndMessage mm = (StreamEndMessage) message;
                this.diagrams[i].removeStream(mm.getProtocol(), mm.getSource().getHostAddress(), mm.getDestination().getHostAddress());
            }*/
        }
        else if (message instanceof NameMessage) {
            // über alle Diagramme iterieren und entsprechende Methode aufrufen
			/* DEBUG */ System.out.println("NameMessage.");
			if ((this.ipVersion == 6 && ((NameMessage) message).getComputer().getHostAddress().contains(":")) || ( (this.ipVersion == 4 && ((NameMessage) message).getComputer().getHostAddress().contains(".") ) ) )
			{
	            for (int i = 0; i < this.diagrams.length; i++) {
	                NameMessage mm = (NameMessage) message;
	                this.diagrams[i].addName(mm.getTimeStamp(), mm.getProtocol(), mm.getComputer().getHostAddress(), mm.getName());
	            }
			}
        }
    }


    public void signalMessageArrived(SignalMessage message) {
		//* DEBUG */ System.out.println("SignalMessage angekommen. Herzlich willkommen! Typ: " + message.getSignal());
        String signal = message.getSignal();

        if (signal.equals("BYE")){
			/* DEBUG */ System.out.println("BYE from " + message.getSender().getHostAddress());
        	
        	// es ist eine BYE-Nachricht --> Client aus Collectors-Liste entfernen und GUI aktualisieren
        	this.collectors.remove(message.getSender());
        	this.gui.setCollectorNotConnected(message.getSender().getHostAddress());
        	
        	// zugehoerige signaling connection aus Liste entfernen
			for (int i = 0; i < this.sConnections.size(); i++) {
				if (((SignalingConnection) this.sConnections.get(i)).getCollector().equals(message.getSender())) {
					this.sConnections.remove(i);
					break;
				}
			}
        }
		else if (signal.equals("HELLO")) {
			/* DEBUG */ System.out.println("HELLO from " + message.getSender().getHostAddress());
			
        	// es ist eine HELLO-Nachricht --> Client in Liste aufnehmen GUI aktualisieren
        	this.collectors.add(message.getSender());
        	this.gui.setCollectorConnected(message.getSender().getHostAddress());

            // zur Bestätigung HELLO zurücksenden
			for (int i = 0; i < this.sConnections.size(); i++) {
				if (((SignalingConnection) this.sConnections.get(i)).getCollector().equals(message.getSender()))
					((SignalingConnection) this.sConnections.get(i)).sendSignalMessage("HELLO", message.getSender(), null);
			}
		}
    }
    
    protected Diagram[] getDiagrams() {
    	return this.diagrams;
    }



	public static void main(String [] args){
        if (args.length != 2){
            System.out.println("usage: java master.Master <IP to bind to> <topology-file>");
            System.exit(1);
        }

		new Master(args[0], args[1]);
	}

}

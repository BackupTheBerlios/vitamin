package master;

import java.net.InetAddress;
import java.util.LinkedList;

import master.diagram.Parameter;
import master.topology.structure.Node;
import master.topology.structure.Subnet;
import master.topology.structure.Topology;
import master.topology.structure.types.SubnetTypeType;

import global.messages.StreamEndMessage;
import global.messages.StreamMessage;


/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 *
 */
public class Stream {
	
	/* die ID, die denn Stream eindeutig kennzeichnet
	 diese IDs muss von den Kollektoren generiert werden.
	 Allerdings muessen alle Kollektoren dieselbe ID generieren
	 Die ID muss also aus dem Paket generiert werden.
	 Eine ID besteht aus:
	  - Protokoll
	  - Quell-IP
	  - Ziel-IP
	  - Ziel-Port	
	*/
	private int port;
	private InetAddress source;
	private InetAddress destination;
	private String protocol;
	
	private Master master;
	
	
	// Die einzelnen Stationen des Streams muessen in einer Datenstruktur abgelegt werden.
	// Diese Struktur kann keine Liste sein, da es zu Luecken kommen kann.
	// Also wird eine Liste von Teilstreams angelegt, die zusammengefuegt werden, wenn die Luecke geschlossen wird.
	// Die Teilstreams werden als LinkedList dargestellt. Die Liste streamParts enthaelt also eine variierende Anzahl von LinkedList.
	// Ist der Stream vollstaendig erkannt, so enthaelt streamParts nur noch eine LinkedList, die den ganzen Stream enthaelt.
	private LinkedList streamParts;
	
	
	private Topology topology;


	public Stream(StreamMessage streamMessage, Topology topology, Master master) {
		this.master = master;
		
		// die ID setzen
		this.port = streamMessage.getPort();
		this.source = streamMessage.getSource();
		this.destination = streamMessage.getDestination();
		this.protocol = streamMessage.getProtocol();
		
		this.topology = topology;
		
		this.streamParts = new LinkedList();
			
		boolean untergekommen = false;
		
		// pruefen, ob ich von der Quelle direkt zum Ziel komme (dann kann ich den Stream direkt einzeichnen)
		if (this.isReachableDirectly(this.source.getHostAddress(), this.destination.getHostAddress())) {
			//* DEBUG */ System.out.println("Stream: direkter Weg von Start zu Ziel.");			
			this.addNewStreamPart(source.getHostAddress(), destination.getHostAddress());
			untergekommen = true;
		}
		
		
		// pruefen, ob ich von der Quelle zum Sender oder vom Sender zum Ziel komme (oder beides)
		//* DEBUG */ System.out.println("streamMessage.getSender: " + streamMessage.getSender().getHostAddress());
		//* DEBUG */ System.out.println("source: " + this.source.getHostAddress());
		//* DEBUG */ System.out.println("destination: " + this.destination.getHostAddress());			
			
			
		if (!this.source.getHostAddress().equals(streamMessage.getSender().getHostAddress()) && this.isReachableDirectly(this.source.getHostAddress(), streamMessage.getSender().getHostAddress())) {			 
			// direkte Verbindung Quelle - Sender
			//* DEBUG */ System.out.println("Stream: direkter Weg von Start zu Sender.");
			this.addNewStreamPart(source.getHostAddress(), streamMessage.getSender().getHostAddress());
			this.addNewStreamPart(this.destination.getHostAddress(), this.destination.getHostAddress());
			untergekommen = true;
		}
		if (!this.destination.getHostAddress().equals(streamMessage.getSender().getHostAddress()) && this.isReachableDirectly(this.destination.getHostAddress(), streamMessage.getSender().getHostAddress())) {
			// direkte Verbindung Sender - Ziel
			//* DEBUG */ System.out.println("Stream: direkter Weg von Sender zu Ziel.");
			this.addNewStreamPart(streamMessage.getSender().getHostAddress(), destination.getHostAddress());
			this.addNewStreamPart(this.source.getHostAddress(), this.source.getHostAddress());
			untergekommen = true;
		}

		if (!untergekommen) {
			this.addNewStreamPart(this.source.getHostAddress(), this.source.getHostAddress());
			this.addNewStreamPart(this.destination.getHostAddress(), this.destination.getHostAddress());
		}

		
		// pruefen, ob sich Teile zusammenlegen lassen
		this.joinStreamParts();				
		
		// pruefen, ob der Stream schon komplett ist (dann einzeichnen)
		if (this.isStreamComplete()) {
			//* DEBUG */ System.out.println("StreamManager: Stream sofort komplett erkannt. Wird jetzt eingezeichnet.");			
			// einzeichnen
			this.giveIntoDiagrams();
		}
		
		
		/* DEBUG */ // alle Teilstuecke ausgeben
		//this.printStreamParts();
	}
	
	
	/** hierueber wird der Stream benachrichtigt, wenn ein diesem Stream zugehoeriges Paket angekommen ist.
	 * Es wird die Adresse des Kollektors uebergeben, der den Stream erkannt hat. Der Stream muss diesen
	 * Knoten dann in den Stream-Verlauf einbauen.
	 * 
	 * @param node
	 */
	public void addNode (InetAddress node) {
		//* DEBUG */ System.out.println("Stream wurde ueber neuen Knoten benachrichtigt: " + node.getHostAddress());
		// pruefen, ob ich von einem Ende eines Teilstuecks zum Sender oder vom Sender zum Anfang eines Teilstuecks komme
		//* DEBUG */ System.out.println("iteriere ueber " + this.streamParts.size() + " Stream-Parts.");		
		boolean untergekommen = false;
		for (int i = 0; i < this.streamParts.size(); i++) {
			// pruefen, ob ich vom Ende dieses StreamParts zum Sender komme			
			if ( this.isReachableDirectly(  ((String) ((LinkedList) this.streamParts.get(i)).getLast()), node.getHostAddress()  ) ) {
				//* DEBUG */ System.out.println("direkter Weg vom Ende eines Teilstuecks zum Sender.");
				
				// Sender hinten an diesen StreamPart dranhaengen
				((LinkedList) this.streamParts.get(i)).addLast(node.getHostAddress());
				untergekommen = true;
				
				/* DEBUG */ // alle Teilstuecke ausgeben
				//this.printStreamParts();
			}
			
			// pruefen, ob ich vom Sender zum Anfang dieses StreamParts komme
			else if ( this.isReachableDirectly( node.getHostAddress(), ((String) ((LinkedList) this.streamParts.get(i)).getFirst()) ) ) {
				//* DEBUG */ System.out.println("direkter Weg vom Anfang eines Teilstuecks zum Sender.");

				// Sender vorne an den StreamPart dranhaengen
				((LinkedList) this.streamParts.get(i)).addFirst(node.getHostAddress());
				untergekommen = true;
				
				/* DEBUG */ // alle Teilstuecke ausgeben
				//this.printStreamParts(); 
			}
		}
		
		// wenn der Knoten an kein Teilstueck dranpasst, wird ein neues Teilstueck nur fuer ihn angelegt
		if (!untergekommen) {
			//* DEBUG */ System.out.println("lege neues Teilstueck mit Knoten " + node.getHostAddress() + " an.");
			this.addNewStreamPart(node.getHostAddress(), node.getHostAddress());
		} 
		
		// pruefen, ob sich Teile zusammenlegen lassen
		this.joinStreamParts();		
				
		// pruefen, ob der Stream schon komplett ist (dann einzeichnen)
		if (this.isStreamComplete()) {
			//* DEBUG */ System.out.println("StreamManager: Stream komplett erkannt. Wird jetzt eingezeichnet.");
			// einzeichnen
			this.giveIntoDiagrams();
		}
		
		/* DEBUG */ // alle Teilstuecke ausgeben
		//this.printStreamParts(); 
	}
	
	
	public boolean belongsToThisStream (StreamMessage streamMessage) {
		boolean belongsToThisStream = true;
		
		// ist das Ziel gleich?
		if (!streamMessage.getDestination().equals(this.destination))
			belongsToThisStream = false;
			
		// ist die Quelle gleich?
		if (!streamMessage.getSource().equals(this.source))
			belongsToThisStream = false;
				
		// ist der Port gleich?
		if (!(streamMessage.getPort() == this.port))
			belongsToThisStream = false;
		
		// ist das Protokoll gleich?	
		if (!streamMessage.getProtocol().equals(this.protocol))
			belongsToThisStream = false;
		
		return belongsToThisStream;
	}
	
	
	public boolean belongsToThisStream (StreamEndMessage streamMessage) {
		boolean belongsToThisStream = true;
		
		// ist das Ziel gleich?
		if (!streamMessage.getDestination().equals(this.destination))
			belongsToThisStream = false;
			
		// ist die Quelle gleich?
		if (!streamMessage.getSource().equals(this.source))
			belongsToThisStream = false;
				
		// ist der Port gleich?
		if (!(streamMessage.getPort() == this.port))
			belongsToThisStream = false;
		
		// ist das Protokoll gleich?	
		if (!streamMessage.getProtocol().equals(this.protocol))
			belongsToThisStream = false;
		
		return belongsToThisStream;
	}
	
	
	private void printStreamParts() {
		System.out.println("Die aktuellen StreamParts:");		
		for (int i = 0; i < this.streamParts.size(); i++) {
			LinkedList list = (LinkedList) this.streamParts.get(i);
			
			for (int j = 0; j < list.size(); j++)
				System.out.print((String) list.get(j) + " ");
				
			System.out.println();
		}
		System.out.println();
	}
	
	
	private void giveIntoDiagrams() {
		// ueber das verbliebene eine Teilstueck (welches den gesamten Stream darstellt) iterieren und jede direkte Verbindung in die Diagramme geben
		LinkedList stream = (LinkedList) this.streamParts.getFirst();
		for (int p = 0; p < stream.size() - 1; p++) {						
			// pruefen, ob es im Diagramm eine direkte Verbindung ist (ueber die Hashtable im Master
			if ( ((LinkedList) (this.master.connections.get(stream.get(p)))).contains(stream.get(p + 1)) ) {
				// ueber alle Diagramme iterieren und das Stueck von p bis p+1 uebergeben
				for (int i = 0; i < this.master.getDiagrams().length; i++) 									
					this.master.getDiagrams()[i].addStream(Parameter.TIME_UNDEFINED, this.protocol, (String) stream.get(p), (String) stream.get(p + 1));	// TODO which timestamp?
				//* DEBUG */ System.out.println("gebe das Stueck von " + stream.get(p) + " bis " + stream.get(p + 1) + " in die Diagramme.");
			}
			else {
				// keine direkte Verbindung im Diagramm --> im Subnetz den Zwischenknoten finde
				//* DEBUG */ System.out.println("keine direkte Verbindung im Diagramm.");				
				
				Subnet[] subnets = this.findNode((String) stream.get(p));
				
				for (int s = 0; s < subnets.length; s ++) {
					Node[] nodes = subnets[s].getNode();
					
					// p + 1 finden
					for (int n = 0; n < nodes.length; n++) {
						if (nodes[n].getIp().equals((String) stream.get(p + 1)) ) {
							 // p + 1 gefunden --> richtiges Subnetz --> p und Verbindungsknoten finden
							 
							 // p finden
							 int pInSubnet = 0;
							for (int np = 0; np < nodes.length; np++) {
								if (nodes[np].getIp().equals((String) stream.get(p)) ) {
									// p gefunden
									pInSubnet = np;
								}								
							}
							
							// der Verbindungsknoten ist der erste Knoten in der Liste
							
							// das stimmt allerdings nicht, wenn der Type des Subnetzes INTERNET ist, dann ist der Mittelpunkt 0.0.0.0
							if (subnets[s].getType().equals(SubnetTypeType.INTERNET)) {
								// --> Verbindung p - 0.0.0.0 hinzufuegen
								for (int i = 0; i < this.master.getDiagrams().length; i++)
									this.master.getDiagrams()[i].addStream(Parameter.TIME_UNDEFINED, this.protocol, (String) stream.get(p), "0.0.0.0");	// TODO which timestamp?									
									//* DEBUG */ System.out.println("gebe das Stueck von " + stream.get(p) + " bis 0.0.0.0 in die Diagramme.");
							
									// --> Verbindung nodes[0] - (p + 1) hinzufuegen
									for (int i = 0; i < this.master.getDiagrams().length; i++)
										this.master.getDiagrams()[i].addStream(Parameter.TIME_UNDEFINED, this.protocol, "0.0.0.0", (String) stream.get(p + 1));	// TODO which timestamp?
									//* DEBUG */ System.out.println("gebe das Stueck von 0.0.0.0 bis " + stream.get(p + 1) + " in die Diagramme.");
							}
							// ETHERNET
							else {								
								// --> Verbindung p - nodes[0] hinzufuegen
								for (int i = 0; i < this.master.getDiagrams().length; i++)
									this.master.getDiagrams()[i].addStream(Parameter.TIME_UNDEFINED, this.protocol, (String) stream.get(p), nodes[0].getIp());	// TODO which timestamp?
								//* DEBUG */ System.out.println("gebe das Stueck von " + stream.get(p) + " bis " + nodes[0].getIp() + " in die Diagramme.");
								
								// --> Verbindung nodes[0] - (p + 1) hinzufuegen
								for (int i = 0; i < this.master.getDiagrams().length; i++)
									this.master.getDiagrams()[i].addStream(Parameter.TIME_UNDEFINED, this.protocol, nodes[0].getIp(), (String) stream.get(p + 1));	// TODO which timestamp?
								//* DEBUG */ System.out.println("gebe das Stueck von " + nodes[0].getIp() + " bis " + stream.get(p + 1) + " in die Diagramme.");							
							}
						}
					}
				}
			}
			
		}
	}
	
	
	/** hierueber wird dem Stream mitgeteilt, dass er vorbei ist. Der Stream loescht sich dann aus den Diagrammen
	 * 
	 *
	 */
	public void die () {
		this.printStreamParts();
		
		// ueber das verbliebene eine Teilstueck (welches den gesamten Stream darstellt) iterieren und jede direkte Verbindung in den Diagrammen loeschen
		LinkedList stream = (LinkedList) this.streamParts.getFirst();
		for (int p = 0; p < stream.size() - 1; p++) {						
			// pruefen, ob es im Diagramm eine direkte Verbindung ist (ueber die Hashtable im Master
			if ( ((LinkedList) (this.master.connections.get(stream.get(p)))).contains(stream.get(p + 1)) ) {
				// ueber alle Diagramme iterieren und das Stueck von p bis p+1 uebergeben
				for (int i = 0; i < this.master.getDiagrams().length; i++) 									
					this.master.getDiagrams()[i].removeStream(this.protocol, (String) stream.get(p), (String) stream.get(p + 1));
				/* DEBUG */ System.out.println("gebe das Stueck von " + stream.get(p) + " bis " + stream.get(p + 1) + " in die Diagramme.");
			}
			else {
				// keine direkte Verbindung im Diagramm --> im Subnetz den Zwischenknoten finde
				/* DEBUG */ System.out.println("keine direkte Verbindung im Diagramm.");				
				
				Subnet[] subnets = this.findNode((String) stream.get(p));
				
				for (int s = 0; s < subnets.length; s ++) {
					Node[] nodes = subnets[s].getNode();
					
					// p + 1 finden
					for (int n = 0; n < nodes.length; n++) {
						if (nodes[n].getIp().equals((String) stream.get(p + 1)) ) {
							// p + 1 gefunden --> richtiges Subnetz --> p und Verbindungsknoten finden
							 
							// p finden
							int pInSubnet = 0;
							for (int np = 0; np < nodes.length; np++) {
								if (nodes[np].getIp().equals((String) stream.get(p)) ) {
									// p gefunden
									pInSubnet = np;
								}								
							}
							
							// der Verbindungsknoten ist der erste Knoten in der Liste
							
							// das stimmt allerdings nicht, wenn der Type des Subnetzes INTERNET ist, dann ist der Mittelpunkt 0.0.0.0
							if (subnets[s].getType().equals(SubnetTypeType.INTERNET)) {
								// --> Verbindung p - 0.0.0.0 hinzufuegen
								for (int i = 0; i < this.master.getDiagrams().length; i++)
									this.master.getDiagrams()[i].removeStream(this.protocol, (String) stream.get(p), "0.0.0.0");									
									/* DEBUG */ System.out.println("gebe das Stueck von " + stream.get(p) + " bis 0.0.0.0 in die Diagramme.");
							
									// --> Verbindung nodes[0] - (p + 1) hinzufuegen
									for (int i = 0; i < this.master.getDiagrams().length; i++)
										this.master.getDiagrams()[i].removeStream(this.protocol, "0.0.0.0", (String) stream.get(p + 1));
									/* DEBUG */ System.out.println("gebe das Stueck von 0.0.0.0 bis " + stream.get(p + 1) + " in die Diagramme.");
							}
							// ETHERNET
							else {								
								// --> Verbindung p - nodes[0] hinzufuegen
								for (int i = 0; i < this.master.getDiagrams().length; i++)
									this.master.getDiagrams()[i].removeStream(this.protocol, (String) stream.get(p), nodes[0].getIp());
								/* DEBUG */ System.out.println("gebe das Stueck von " + stream.get(p) + " bis " + nodes[0].getIp() + " in die Diagramme.");
								
								// --> Verbindung nodes[0] - (p + 1) hinzufuegen
								for (int i = 0; i < this.master.getDiagrams().length; i++)
									this.master.getDiagrams()[i].removeStream(this.protocol, nodes[0].getIp(), (String) stream.get(p + 1));
								/* DEBUG */ System.out.println("gebe das Stueck von " + nodes[0].getIp() + " bis " + stream.get(p + 1) + " in die Diagramme.");							
							}
						}
					}
				}
			}
		}
	}
	
	
	// --------------------- Methoden zum Verwalten des Streamverlaufs ----------------------
	
	private void addNewStreamPart (String one, String two) {
		LinkedList streamPart = new LinkedList();			
		streamPart.add(one);
		if (!one.equals(two))
			streamPart.add(two);
			
		this.streamParts.add(streamPart);
	}
	
	
	/** guckt sich die verschiedenen StreamParts an und legt
	 * ggf. Parts zusammen, wenn sie zusammenpassen
	 * 
	 *
	 */
	private void joinStreamParts() {	
		//* DEBUG */ System.out.println("begin join");		
		
		// ueber alle StreamParts iterieren und bei jedem pruefen, ob er sich mit den nachfolgenden zusammenlegen laesst
		for (int i = 0; i < this.streamParts.size(); i++) {
			LinkedList list = (LinkedList) this.streamParts.get(i);
			for (int j = i + 1; j < this.streamParts.size(); j++) {
				// pruefen, ob das Ende des einen Teils der Anfang des anderen Teils ist
				if (list.getLast().equals( ((LinkedList) this.streamParts.get(j)).getFirst() )) {
					//* DEBUG */ System.out.println("last of " + i + " is first of " + j);
					
					// ist es --> StreamParts zusammenlegen
					LinkedList toJoin = (LinkedList) this.streamParts.get(i);
					
					// toJoin an list anhaengen (ausser erstes Element)
					for (int k = 1; k < toJoin.size(); k++) {
						list.add(toJoin.get(k));
					}
					
					// toJoin aus streamParts loeschen
					this.streamParts.remove(j);
					//* DEBUG */ System.out.println("remove streamPart " + j);
				}
				
				// pruefen, ob der Anfang des einen Teils das Ende des anderen Teils ist
				else if (list.getFirst().equals( ((LinkedList) this.streamParts.get(j)).getLast() )) {
					//* DEBUG */ System.out.println("first of " + i + " is last of " + j);
					
					// ist es --> StreamParts zusammenlegen					
					LinkedList joinTo = (LinkedList) this.streamParts.get(j);
					
					// toJoin an list anhaengen (ausser erstes Element)
					for (int k = 1; k < list.size(); k++) {
						joinTo.add(list.get(k));
					}
					
					// toJoin aus streamParts loeschen
					this.streamParts.remove(i);
					//* DEBUG */ System.out.println("remove streamPart " + i);
				}
				
				// pruefen, ob das Ende des einen das Ende des anderen ist
				else if (list.getLast().equals( ((LinkedList) this.streamParts.get(j)).getLast() )){
					//* DEBUG */ System.out.println("last of " + i + " is last of " + j);
					
					// i umdrehen und an j hinten anhaengen; i loeschen
					for (int g = ((LinkedList) this.streamParts.get(i)).size() - 2; g >= 0; g --)
						((LinkedList) this.streamParts.get(j)).addLast( ((LinkedList) this.streamParts.get(i)).get(g) );
						
					// i loeschen
					this.streamParts.remove(i); 
				}
				
				// pruefen, ob der Anfang des einen der Anfang des anderen ist (kann das passieren?)
				else if (list.getFirst().equals( ((LinkedList) this.streamParts.get(j)).getFirst() )){
					//* DEBUG */ System.out.println("first of " + i + " is first of " + j);
					
					// i umdrehen und an j vorne anhaengen; i loeschen
					for (int g = ((LinkedList) this.streamParts.get(i)).size() - 2; g >= 0; g --)
						((LinkedList) this.streamParts.get(j)).addFirst( ((LinkedList) this.streamParts.get(i)).get(g) );
						
					// i loeschen
					this.streamParts.remove(i);
				}
			}
		}
		//* DEBUG */ System.out.println("end join");	
	}
	
	
	private void stepIt() {
		// Implementierung des Algorithmus zum Erkennen von Luecken

		for (int f = 0; f < this.streamParts.size(); f++) {
			// hat ein Ende des Teilstuecks neben Verbindungen, die bereits im Datenstrom integriert sind, nur noch eine weitere Verbindung,
			// dann gehen wir mal da lang
			
			// vorne
			String first = (String) ((LinkedList) this.streamParts.get(f)).getFirst();				
						
			// gucken, ob die Bedingung erfuellt ist, dass nur eine freie Verbindung existiert
			String step = this.getOnlyFreeConnection(first);
					
			if (step != null) {
				// es existiert nur eine freie Verbindung
						
				// pruefen, ob von step aus ein Ende eines Teilstuecks erreichbar ist
				for (int i = 0; i< this.streamParts.size(); i++) {
					// beide Seiten dieses Teilstuecks testen
					
					// vorne
					if (this.isReachableDirectly(step, ((String) ((LinkedList) this.streamParts.get(i)).getFirst()) )) {
						// step vorne an f dran
						((LinkedList) this.streamParts.get(f)).addFirst(step);
						
						// i in umgekehrter Reihenfolge vorne an f dran
						for (int g = ((LinkedList) this.streamParts.get(i)).size() - 1; g >= 0; g --)
							((LinkedList) this.streamParts.get(f)).addFirst( ((LinkedList) this.streamParts.get(i)).get(g) );
						
						// i loeschen
						this.streamParts.remove(i); 
					}
					
					// hinten					
					else if (this.isReachableDirectly(step, ((String) ((LinkedList) this.streamParts.get(i)).getLast()) )) {
						// f an i dranhaengen mit step dazwischen
						
						((LinkedList) this.streamParts.get(f)).add(step);
						
						for (int g = 0; g < ((LinkedList) this.streamParts.get(f)).size(); g++)
							((LinkedList) this.streamParts.get(i)).add( ((LinkedList) this.streamParts.get(f)).get(g) );
							
						// f loeschen
						this.streamParts.remove(f);
					}
				}
		
			}
						
				
			// hinten
			String last = (String) ((LinkedList) this.streamParts.get(f)).getLast();
			
			// gucken, ob die Bedingung erfuellt ist, dass nur eine freie Verbindung existiert
			step = this.getOnlyFreeConnection(last);
					
			if (step != null) {
				// es existiert nur eine freie Verbindung
				
				// pruefen, ob von step aus ein Ende eines Teilstuecks erreichbar ist
				for (int i = 0; i< this.streamParts.size(); i++) {
					// beide Seiten dieses Teilstuecks testen
					
					// vorne
					if (this.isReachableDirectly(step, ((String) ((LinkedList) this.streamParts.get(i)).getFirst()) )) {
						// i hinten an f dranhaengen (mit step dazwischen)
						
						((LinkedList) this.streamParts.get(f)).addLast(step);
						
						for (int r = 0; r < ((LinkedList) this.streamParts.get(i)).size(); r++)
							((LinkedList) this.streamParts.get(f)).addLast( ((LinkedList) this.streamParts.get(i)).get(r) );
							
						// i loeschen
						this.streamParts.remove(i);
					}
 					// hinten
					else if (this.isReachableDirectly(step, ((String) ((LinkedList) this.streamParts.get(i)).getLast()) )) {
						// step vorne an f dran
						((LinkedList) this.streamParts.get(f)).addFirst(step);
						
						// i in umgekehrter Reihenfolge vorne an f dran
						for (int g = ((LinkedList) this.streamParts.get(i)).size() - 1; g >= 0; g --)
							((LinkedList) this.streamParts.get(f)).addFirst( ((LinkedList) this.streamParts.get(i)).get(g) );
						
						// i loeschen
						this.streamParts.remove(i);
					}
				}
			}
		
		}
				
	}
	
	
	private boolean isStreamComplete () {
		// wenn streamParts mehr als eine Liste enthaelt, kann der Stream nocht nicht komplett sein
		if (this.streamParts.size() == 1) {
			// wenn jetzt der Anfang des StreamParts die Quelle und das Ende des StreamParts das Ziel ist, ist der Stream komplett
			if ( ((String) ((LinkedList) this.streamParts.get(0)).getFirst()).equals(this.source.getHostAddress()) && ((String) ((LinkedList) this.streamParts.get(0)).getLast()).equals(this.destination.getHostAddress()) ) {
				return true;
			}
			// is auch komplett, wenn der Anfang des StreamParts das Ziel und das Ende des StreamParts die Quelle ist
			if ( ((String) ((LinkedList) this.streamParts.get(0)).getFirst()).equals(this.destination.getHostAddress()) && ((String) ((LinkedList) this.streamParts.get(0)).getLast()).equals(this.source.getHostAddress()) ) {
				return true;
			}
		}			
		return false;
	}

	
	// ---------------- Methoden zum Untersuchen der Netzwerktopologie -----------------------
	
	/**
	 *  prueft, ob die beiden als Parameter gegebenen Knoten direkt miteinander verbunden sind
	 */
	private boolean isReachableDirectly (String one, String two) {
		//* DEBUG */ System.out.println("isReachable Directly?");		
		boolean isReachableDirectly = false;
		
		// erst mal den ersten Knoten finden
		Subnet [] oneIsHere = this.findNode(one);
		
		//* DEBUG */ System.out.println("Node one liegt in " + oneIsHere.length + " Subnetzen.");		
		
		// ueber alle diese Subnetze iterieren und pruefen, ob two auch darin liegt		
		for (int i = 0; i < oneIsHere.length; i++) {
			Node [] nodes = oneIsHere[i].getNode();
			
			//* DEBUG */ System.out.println("in Subnetz " + i + " liegen " + nodes.length + " Knoten.");
			// one finden
			Node oneNode = null;
			for (int j = 0; j < nodes.length; j++) {
				if (nodes[j].getIp().equals(one))
					oneNode = nodes[j];
			}
			
			for (int j = 0; j < nodes.length; j++) {
				if (nodes[j].getIp().equals(two)) {
					// erster und zweiter Knoten liegen in diesem Subnetz
					// --> es gibt eine direkte Verbindung, wenn entweder one oder two der Mittelpunkt , also das
					// Gateway dieses Subnetzes, sind.
					if (oneIsHere[i].getGateway().equals(one) || oneIsHere[i].getGateway().equals(two))
						isReachableDirectly = true;
					
					// -------------- musste geaendert werden, wg. neuem Topologie-Dateiformat -----------------------------
					// erster und zweiter Knoten liegen in diesem Subnetz
					// --> es gibt eine direkte Verbindung, wenn entweder one oder two der Mittelpunkt sind
					// (wenn beide als "router" markiert sind, sind es router-IPs, die vollvermascht sind)
					//if (nodes[j].getType() != null || oneNode.getType() != null)
					//	isReachableDirectly = true;
				}
			}
		}
		
		//* DEBUG */ System.out.println(isReachableDirectly);
		return isReachableDirectly;
	}
	
	
	/** prueft, ob es von einem Knoten nur eine Verbindung gibt, die noch nicht in den Datenstrom integriert ist
	 * 
	 * @param fromHere
	 * @return
	 */
	private String getOnlyFreeConnection(String fromHere) {
		// die Verbindungen von und zu diesem Knoten holen
		LinkedList connectedNodes = (LinkedList) this.master.connections.get(fromHere);
		
		String freeNode = null;
		boolean moreThanOneFreeNode = false;
		
		// ueber alle connectedNodes iterieren und pruefen ob diese Verbindung frei ist
		// d.h. dass alle Teilstuecke des Datenstroms durchgegangen werden muessen und geprueft werden muss,
		// ob darin die Verbindung fromHere - connectedNodes[i] vorkommt
		for (int i = 0; i < connectedNodes.size(); i++) {
			int counter = 0;
			for (int j = 0; j < this.streamParts.size(); j++) {
				if ( ((LinkedList) this.streamParts.get(j)).contains(fromHere) && ((LinkedList) this.streamParts.get(j)).contains((String) connectedNodes.get(i)) ) {
					 // streamPart enthaelt fromHere und connectedNode. connectedNode ist also nicht frei
					 break; 
				}
				else {
					counter ++;
				}
			}
			
			if (counter == this.streamParts.size()) {
				// der Knoten ist frei
				if (freeNode == null)
					freeNode = (String) connectedNodes.get(i);
				else {
					// es gab schon einen freien Knoten. Es gibt also mehrere! Das darf nicht sein.
					moreThanOneFreeNode = true;
				}
			}
		}
		
		if (!moreThanOneFreeNode)
			return freeNode; // diese kann trotzdem null sein
		else
			return null;
	}
	
	
	/** gibt alle Subnetze zurueck, in denen der Knoten liegt (koennen mehrere sein, wenn's ein Router ist)
	 * 
	 * @param nodeName
	 * @return
	 */
	private Subnet [] findNode (String nodeIP) {
		Subnet [] erg = null;
		
		Subnet [] subnets = this.topology.getSubnet();
		
		LinkedList subnetIndices = new LinkedList();
		
		// ueber alle Subnetze iterieren und preufen, ob der Knoten in diesem Subnetz liegt
		for (int i = 0; i < subnets.length; i++) {
			Node [] nodes = subnets[i].getNode();
			
			for (int j = 0; j < nodes.length; j++) {
				if (nodes[j].getIp().equals(nodeIP)) {
					// Knoten liegt in diesem Subnetz --> merken
					subnetIndices.add(new Integer(i)); 			
				}
			}
		}
		
		// jetzt noch das Ergebnisfeld zusammenbauen
		erg = new Subnet [subnetIndices.size()];
		
		for (int i = 0; i < erg.length; i++) {
			erg[i] = subnets[( (Integer) subnetIndices.get(i)).intValue()];
		}
				
		return erg;
	}

}

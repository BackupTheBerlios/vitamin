package master;

import java.util.LinkedList;

import master.topology.structure.Topology;

import global.messages.StreamEndMessage;
import global.messages.StreamMessage;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 *
 */
public class StreamManager {
	
	// in dieser Liste liegen alle gerade aktiven Streams (als Objekte der Klasse Stream)
	// die Streams auf dem Master unterscheiden sich von den Streams der Kollektoren: 
	// die Master-Streams enthalten moeglichst alle Zwischenpunkte
	private LinkedList streams;
	
	private Topology topology;
	
	private Master master;

	
	public StreamManager(Topology topology, Master master) {
		this.streams = new LinkedList();
		this.topology = topology;	
		this.master = master;
	}
	
	
	/** hierueber wird der StreamManager informiert, wenn eine neue StreamMessage angekommen ist 
	 * 
	 * @param streamMessage
	 */
	public void newStreamMessage (StreamMessage streamMessage) {
		//System.out.println("port: " + streamMessage.getPort());
		
		// ueber alle Streams iterieren und pruefen, ob diese StreamMessage dazu passt
		// richtigem Stream die Message uebergeben
		boolean found = false;
		//* DEBUG */ System.out.println("checking " + this.streams.size() + " streams.");
		for (int i = 0; i < this.streams.size(); i++) {
			if (((Stream) this.streams.get(i)).belongsToThisStream(streamMessage)) {
				//* DEBUG */ System.out.println("Paket gehoert zu bereits erkanntem Stream. Leite an Stream weiter.");
				// richtigen Stream gefunden --> dem Stream den Sender mitteilen 
				((Stream) this.streams.get(i)).addNode (streamMessage.getSender());				
				found = true;
			} 			
		}
		
		// wenn die Nachricht keinem Stream zugeordnet werden kann, muss der Liste ein neuer Stream zugeordnet werden
		if (!found) {
			//* DEBUG */ System.out.println("StreamManager: lege neuen Stream an. ID: SRC = " + streamMessage.getSource().getHostAddress() + ", DST = " + streamMessage.getDestination().getHostAddress() + ", PROT = " + streamMessage.getProtocol() + ", PORT = " + streamMessage.getPort());		
			this.streams.add(new Stream(streamMessage, this.topology, this.master));			
		}
	}
	
	
	/** hierueber wird der StreamManager informiert, wenn eine neue StreamEndMessage angekommen ist
	 * 
	 * @param streamEndMessage
	 */
	public void newStreamEndMessage (StreamEndMessage streamEndMessage) {
		// wie gehe ich hiermit um? Wird der Stream deaktiviert, wenn alle StreamEndMessages angekommen sind oder sofort bei der ersten?
		// erst mal sofort bei der ersten. Wird sich zeigen, ob das gut ist oder nicht...

		//System.out.println("source: " + streamEndMessage.getSource().getHostAddress());
		//System.out.println("destination: " + streamEndMessage.getDestination().getHostAddress());
		//System.out.println("port: " + streamEndMessage.getPort());
		//System.out.println("protocol: " + streamEndMessage.getProtocol());
		
		// ueber alle Streams iterieren und pruefen, ob diese StreamMessage dazu passt
		// richtigem Stream die Message uebergeben
		
		//* DEBUG */ System.out.println("pruefe " + this.streams.size() + " Streams.");
		for (int i = 0; i < this.streams.size(); i++) {
			if (((Stream) this.streams.get(i)).belongsToThisStream(streamEndMessage)) {
				//* DEBUG */ System.out.println("streamEnd gehoert zu bereits erkanntem Stream. Leite an Stream weiter.");
				// richtigen Stream gefunden --> dem Stream seine Todesnachricht uebergeben und ihn dann loeschen 
				((Stream) this.streams.get(i)).die();		
				this.streams.remove(i);		
			} 			
		}
		
		// wenn die Nachricht keinem Stream zugeordnet werden konnte, kann ich auch nichts machen. Einen nicht
		// dargestellten Stream kann ich nicht loeschen....
	}

}

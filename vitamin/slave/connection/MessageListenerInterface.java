package slave.connection;

import global.messages.SignalMessage;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 13-05-2004
 *
 * Im Client muss eine Klasse dieses Interface implementieren. Diese Klasse muss der Instanz
 * der Verbindungsklasse (die, die ConnectionInterface implementiert) bekannt sein, damit diese
 * die messageArrived-Methode aufrufen kann. 
 */
public interface MessageListenerInterface {

	/** diese Methode wird jedesmal aufgerufen, wenn ein Paket vom Server eintrifft.
	 * 
	 * @param message
	 */
	public void messageArrived(SignalMessage message);

}

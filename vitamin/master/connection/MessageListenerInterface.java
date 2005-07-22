package master.connection;

import global.messages.SignalMessage;
import global.messages.Message;


/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 13-05-2004
 *
 * Im Server muss eine Klasse dieses Interface implementieren. Diese Klasse muss der Instanz
 * der Verbindungsklasse (die, die ConnectionInterface implementiert) bekannt sein, damit diese
 * die messageArrived-Methode aufrufen kann. 
 */
public interface MessageListenerInterface {

	/** diese Methode wird jedesmal aufgerufen, wenn ein Datenpaket von einem Client eintrifft.
	 * 
	 * @param message
	 */
	public void dataMessageArrived(Message message);


    /** diese Methode wird jedesmal aufgerufen, wenn ein Paket mit einer
     * Signalisierungsnachricht von einem Client eintrifft.
	 *
	 * @param message
	 */
    public void signalMessageArrived(SignalMessage message);

}

package slave;

import java.net.InetAddress;
import java.net.UnknownHostException;

import slave.connection.SignalingConnection;

import global.messages.SignalMessage;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 09-03-2005
 * 
 * This thread will be started when the users presses CTRL-C to quit the slave.
 * This thread will do all work to quit the slave safely.
 *
 */
public class ExitThread extends Thread {

	// is used to send BYE to the server when slave is aborted
	private SignalingConnection sConnection;
	
	private Slave slave;
	


	public ExitThread(Slave slave, SignalingConnection sConnection) {
		super();
		
		this.sConnection = sConnection;
		this.slave = slave;
	}
	

	
	/** This method is called when the user presses CTRL-C
	 * 
	 *
	 */
	public void run() {		
		/* DEBUG */ System.out.println("Got CTRL-C. Will shutdown client...");
		
		/* DEBUG */ System.out.println("Sending BYE to master.");		
				
		// send BYE signal to master
		SignalMessage signalMessage = null;
		try {
			signalMessage = new SignalMessage(InetAddress.getLocalHost(), "BYE", null);
		} catch (UnknownHostException e1) {
			// es gibt keinen localhost? Unsinn!
			e1.printStackTrace();
		}
		this.sConnection.sendSignalMessage(signalMessage);
		
		
		/* DEBUG */ System.out.println("Shutting down all components of the slave.");
		
		// stop capturing packets if capuring at the moment
		// end Classifier and SignalingConnection
		this.slave.exitClient();
		
		/* DEBUG */ System.out.println("Done. Program will exit.");
	}

}

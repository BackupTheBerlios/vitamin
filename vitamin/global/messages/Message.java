package global.messages;

import java.net.InetAddress;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public abstract class Message {

    private InetAddress sender;	// IP address of the slave which creates this message
    private long timestamp;		// in microseconds since 1.1.1970


    public Message (InetAddress sender, long timestamp) {
        this.sender = sender;
        this.timestamp = timestamp;
    }


    public InetAddress getSender() {
        return this.sender;
    }
    
    
	public long getTimeStamp() {
		return this.timestamp;
	}

}

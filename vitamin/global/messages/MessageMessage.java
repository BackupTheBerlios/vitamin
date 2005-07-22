package global.messages;

import java.net.InetAddress;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class MessageMessage extends Message {

    private String protocol;
    private String message;
    private InetAddress source;
    private InetAddress destination;


    public MessageMessage (InetAddress sender, long timestamp, String protocol, InetAddress source, InetAddress destination, String message) {
        super(sender, timestamp);

        this.protocol = protocol;
        this.source = source;
        this.destination = destination;
        this.message = message;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InetAddress getSource() {
        return source;
    }

    public void setSource(InetAddress source) {
        this.source = source;
    }

    public InetAddress getDestination() {	
        return destination;
    }

    public void setDestination(InetAddress destination) {
        this.destination = destination;
    }

}

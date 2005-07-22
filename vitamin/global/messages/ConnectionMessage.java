package global.messages;

import java.net.InetAddress;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class ConnectionMessage extends Message {

    private String protocol;
    private InetAddress source;
    private InetAddress destination;


    public ConnectionMessage (InetAddress sender, long timestamp, String protocol, InetAddress source, InetAddress destination) {
        super (sender, timestamp);

        this.protocol = protocol;
        this.source = source;
        this.destination = destination;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
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

package global.messages;

import java.net.InetAddress;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class NameMessage extends Message {

    private InetAddress computer;
    private String protocol;
    private String name;


    public NameMessage (InetAddress sender, long timestamp, InetAddress computer, String protocol, String name) {
        super(sender, timestamp);

        this.computer = computer;
        this.protocol = protocol;
        this.name = name;
    }


    public InetAddress getComputer() {
        return computer;
    }

    public void setComputer(InetAddress computer) {
        this.computer = computer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

}

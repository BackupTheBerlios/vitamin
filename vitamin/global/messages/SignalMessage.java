package global.messages;

import global.Filter;

import java.net.InetAddress;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class SignalMessage extends Message {

    private String signal;
    private Filter filter;


    /** legt eine Signalisierungsnachricht an. Der Typ wird hierbei durch
     * einen String angegeben. Dieser muss eine der folgenden Formen haben:
     * - HELLO
     * - BYE
     * - START
     * - STOP
     *
     * Eine START-Nachricht ben�tigt weitere Daten, weswegen ein weiterer Parameter
     * erlaubt ist. Dieser wird allerdings nur bei START-Nachrichten gebraucht und
     * kann ansonsten null sein.
     *
     * @param sender
     * @param signal
     * @param filter
     */
    public SignalMessage (InetAddress sender, String signal, Filter filter) {
        super (sender, 0);	// SignalMessages brauchen keinen Zeitstempel

        this.signal = signal;

        // wenn das Signal nicht START ist, wird ein evtl.
        // angegebener Filter einfach ignoriert.
        if (signal.equals("START"))
            this.filter = filter;
    }


    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}

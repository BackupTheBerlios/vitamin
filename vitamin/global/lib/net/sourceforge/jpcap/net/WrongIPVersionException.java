package net.sourceforge.jpcap.net;

/**
 * @author Martin Pelzer
 * @version 29.06.2004
 *
 */
public class WrongIPVersionException extends Exception {

	public WrongIPVersionException() {
		super();
	}


	public WrongIPVersionException(String message) {
		super(message);
	}


	public WrongIPVersionException(String message, Throwable cause) {
		super(message, cause);
	}


	public WrongIPVersionException(Throwable cause) {
		super(cause);
	}

}

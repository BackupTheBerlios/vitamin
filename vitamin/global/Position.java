package global;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 *
 */
public class Position {

	private int start;
	private int length;

	public Position(int start, int length) {
		this.start = start;
		this.length = length;		
	}
	
	

	public int getLength() {
		return length;
	}


	public int getStart() {
		return start;
	}

}

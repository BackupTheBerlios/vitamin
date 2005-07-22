package master.diagram;


/*
 * Created on 07.04.2004
 * 
 * @author	Martin Pelzer
 * 				Fraunhofer FOKUS
 * 				martin.pelzer@fokus.fraunhofer.de
 */
public class Parameter {

	// vertikaler Abstand zw. 2 Pfeilen in einem Sequenzdiagramm
	public static int arrowDistance = 30;
	
	// horizontaler Abstand zw. 2 Objeten in einem Sequenzdiagramm
	public static int objectDistance = 120;
	
	// Zeit, nach der ein Diagramm seine Anzeige aktualisiert (in ms)
	public static int refreshRate = 500;
	
	// wenn ein Event an ein Diagramm gegeben wird, kann es sein, dass keine Zeit
	// dazu definiert ist (z.B. wenn das Topology-File ausgelesen wird). Dann wird
	// TIME_UNDEFINED uebergeben.
	public static int TIME_UNDEFINED = -1;
	
}

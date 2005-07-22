package master.topology;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;


import master.topology.structure.Topology;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 05-05-2004
 *
 */
public class TopologyFileReader {
	
	
	public TopologyFileReader(){
	
	}
	
	
	/** liest eine XML-Datei aus und gibt ein Objekt vom Typ Topology zurück, welches die Informationen aus
	 * der Datei enthält.
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Topology readFile(String filename) throws FileNotFoundException, IOException {
		  // Datei oeffnen und auslesen
		  BufferedReader in = new BufferedReader(new FileReader(filename));
		  StringBuffer xmlString= new StringBuffer();
		  String temp ="";
		  while ((temp = in.readLine()) != null){
		  	xmlString.append(temp);
		  }
		  in.close();

		  // Presentationobjekt erzeugen
		  StringReader xml = new StringReader(xmlString.toString());

		  Topology p = null;

		  try{
			 p = (Topology) Topology.unmarshal(xml);
		  } catch (MarshalException e) {
			 System.out.println("error in server.topology.TopologyFileReader.readFile: MarshalException.");
              e.printStackTrace();
             System.exit(1);
		  } catch (ValidationException e) {
			 System.out.println("error in server.topology.TopologyFileReader.readFile: given file is not valid..");
             System.exit(1);
		  }

		  return p;
	   }


	   /** schreibt eine XML-Topology-Datei. Wird von der Software nicht verwendet, könnte aber vielleicht mal nützlich sein....
	    * 
	    * @param filename
	    * @param topology
	    * @throws ValidationException
	    * @throws MarshalException
	    * @throws IOException
	    */
	   public void writeFile(String filename, Topology topology) throws ValidationException, MarshalException, IOException {
		  // hierein wird gleich der XML-String geschrieben (Castor will einen Writer)
		  StringWriter writer = new StringWriter();

		  // marshallen
		  topology.marshal(writer);

		  // jetzt den XML-String aus dem StringWriter in einen StringBuffer ?bergeben
		  StringBuffer buffer = writer.getBuffer();

		  // den XML-String in die Datei schreiben
		  BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		  out.write(buffer.toString());
		  out.close();
	   }

}

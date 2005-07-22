package global;

import global.pluginStructure.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 * @version 14-05-2004
 *
 * Diese Klasse liest die Plugins im Verzeichnis plugins/ aus und parst diese mittels Castor in Java-Objekte.
 * Anschließend wird über entsprechende Methoden Zugriff auf die Plugins gewährt.
 */
public class XMLPluginParser {
	
	private LinkedList protocols;
	
	
	public XMLPluginParser() {
		this.protocols = new LinkedList();
		
		// Pfad zum Plugin-Verzeichnis
		File pfad = new File("plugins");
		
		// alle Dateien aus diesem Verzeichnis auslesen
		File [] pluginFiles = pfad.listFiles();
		
		// über alle Plugindateien iterieren und jedes per Castor einlesen
		for (int i = 0; i < pluginFiles.length; i++) {
			if (!pluginFiles[i].isDirectory()) {
			
				BufferedReader in = null;
				try {
					in = new BufferedReader(new FileReader(pluginFiles[i].getAbsolutePath()));
				} catch (FileNotFoundException e) {
					System.out.println("error in global.XMLProtocolParser: could not find file in plugin-directory.");
				}
				
				StringBuffer xmlString= new StringBuffer();
				String temp = "";
	
				// XML-Datei zeilenweise in den StringBuffer einlesen
	            try{
					while ((temp = in.readLine()) != null){
						xmlString.append(temp);
					}
				}
				catch (IOException e) {
					System.out.println("error in global.XMLProtocolParser: could not read from file in plugin-directory.");
				}
				
				try {
					in.close();
				} catch (IOException e1) {
					System.out.println("error in global.XMLProtocolParser: could not close file in plugin-directory.");
				}
	
				// Protocol-Objekt erzeugen
				StringReader xml = new StringReader(xmlString.toString());
				
				// hier wird der XML-String jetzt in ein Java-Objekt umgewandelt
				try {
					Plugin plugin = (Plugin) Plugin.unmarshal(xml);
					
					// der Pluginliste hinzufügen
					this.protocols.add(plugin);
				} catch (MarshalException e) {				
					System.out.println("error in global.XMLProtocolParser: could not unmarshal plugin in plugin-directory: " + pluginFiles[i].getAbsolutePath());
					e.printStackTrace();
				} catch (ValidationException e) {
					System.out.println("error in global.XMLProtocolParser: plugin file is not valid.");
					e.printStackTrace();
				}
			
			}
						
		}
		
		System.out.println(this.protocols.size() + " plugins loaded.");
	}


	/** liefert eine LinkedList, die alle eingelesenen Plugins als Protocol-Objekte enthält
     *
     * @return
     */
    public LinkedList getPlugins(){
        return this.protocols;
	}


    /** liefert ein Protokollobjekt zu einem Protokollnamen als String.
     * Kann dem Namen kein Protokoll zugeordnet werden, liefert die Methode
     * null.
     *
     * @param name
     * @return
     */
    public Plugin getPlugin (String name) {
        Plugin plugin = null;

        for (int i = 0; i < this.protocols.size(); i++) {
            if ( ((Plugin) this.protocols.get(i)).getName().equals(name) )
                plugin = (Plugin) this.protocols.get(i);
        }

        return plugin;
    }


    /** liefert ein String-Array, welches die Namen aller eingelesenen Protokolle enthält.
     *
     * @return
     */
    public String [] getPluginNames (){
        // in dieses Array werden die Namen aller Plugins eingespeichert
        String [] pluginNames = new String [this.protocols.size()];

        for (int i = 0; i < this.protocols.size(); i++) {
            pluginNames[i] = ((Plugin) this.protocols.get(i)).getName();
        }

        return pluginNames;
    }

}

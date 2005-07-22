/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: SourceAndDestination.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
 */

package global.pluginStructure;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class SourceAndDestination.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class SourceAndDestination implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _source
     */
    private global.pluginStructure.Source _source;

    /**
     * Field _destination
     */
    private global.pluginStructure.Destination _destination;


      //----------------/
     //- Constructors -/
    //----------------/

    public SourceAndDestination() {
        super();
    } //-- global.pluginStructure.SourceAndDestination()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'destination'.
     * 
     * @return the value of field 'destination'.
     */
    public global.pluginStructure.Destination getDestination()
    {
        return this._destination;
    } //-- global.pluginStructure.Destination getDestination() 

    /**
     * Returns the value of field 'source'.
     * 
     * @return the value of field 'source'.
     */
    public global.pluginStructure.Source getSource()
    {
        return this._source;
    } //-- global.pluginStructure.Source getSource() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'destination'.
     * 
     * @param destination the value of field 'destination'.
     */
    public void setDestination(global.pluginStructure.Destination destination)
    {
        this._destination = destination;
    } //-- void setDestination(global.pluginStructure.Destination) 

    /**
     * Sets the value of field 'source'.
     * 
     * @param source the value of field 'source'.
     */
    public void setSource(global.pluginStructure.Source source)
    {
        this._source = source;
    } //-- void setSource(global.pluginStructure.Source) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.SourceAndDestination) Unmarshaller.unmarshal(global.pluginStructure.SourceAndDestination.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}

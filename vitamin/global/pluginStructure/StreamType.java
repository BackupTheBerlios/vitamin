/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: StreamType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class StreamType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class StreamType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sourceAndDestination
     */
    private global.pluginStructure.SourceAndDestination _sourceAndDestination;

    /**
     * Field _comparators
     */
    private global.pluginStructure.Comparators _comparators;


      //----------------/
     //- Constructors -/
    //----------------/

    public StreamType() {
        super();
    } //-- global.pluginStructure.StreamType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'comparators'.
     * 
     * @return the value of field 'comparators'.
     */
    public global.pluginStructure.Comparators getComparators()
    {
        return this._comparators;
    } //-- global.pluginStructure.Comparators getComparators() 

    /**
     * Returns the value of field 'sourceAndDestination'.
     * 
     * @return the value of field 'sourceAndDestination'.
     */
    public global.pluginStructure.SourceAndDestination getSourceAndDestination()
    {
        return this._sourceAndDestination;
    } //-- global.pluginStructure.SourceAndDestination getSourceAndDestination() 

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
     * Sets the value of field 'comparators'.
     * 
     * @param comparators the value of field 'comparators'.
     */
    public void setComparators(global.pluginStructure.Comparators comparators)
    {
        this._comparators = comparators;
    } //-- void setComparators(global.pluginStructure.Comparators) 

    /**
     * Sets the value of field 'sourceAndDestination'.
     * 
     * @param sourceAndDestination the value of field
     * 'sourceAndDestination'.
     */
    public void setSourceAndDestination(global.pluginStructure.SourceAndDestination sourceAndDestination)
    {
        this._sourceAndDestination = sourceAndDestination;
    } //-- void setSourceAndDestination(global.pluginStructure.SourceAndDestination) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.StreamType) Unmarshaller.unmarshal(global.pluginStructure.StreamType.class, reader);
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

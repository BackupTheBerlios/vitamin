/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ConditionType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class ConditionType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class ConditionType extends global.pluginStructure.Pointer 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _conditionFor
     */
    private global.pluginStructure.ConditionFor _conditionFor;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConditionType() {
        super();
    } //-- global.pluginStructure.ConditionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'conditionFor'.
     * 
     * @return the value of field 'conditionFor'.
     */
    public global.pluginStructure.ConditionFor getConditionFor()
    {
        return this._conditionFor;
    } //-- global.pluginStructure.ConditionFor getConditionFor() 

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
     * Sets the value of field 'conditionFor'.
     * 
     * @param conditionFor the value of field 'conditionFor'.
     */
    public void setConditionFor(global.pluginStructure.ConditionFor conditionFor)
    {
        this._conditionFor = conditionFor;
    } //-- void setConditionFor(global.pluginStructure.ConditionFor) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.ConditionType) Unmarshaller.unmarshal(global.pluginStructure.ConditionType.class, reader);
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

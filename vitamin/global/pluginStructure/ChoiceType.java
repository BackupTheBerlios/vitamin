/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ChoiceType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class ChoiceType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class ChoiceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _conditionGroup
     */
    private global.pluginStructure.ConditionGroup _conditionGroup;


      //----------------/
     //- Constructors -/
    //----------------/

    public ChoiceType() {
        super();
    } //-- global.pluginStructure.ChoiceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'conditionGroup'.
     * 
     * @return the value of field 'conditionGroup'.
     */
    public global.pluginStructure.ConditionGroup getConditionGroup()
    {
        return this._conditionGroup;
    } //-- global.pluginStructure.ConditionGroup getConditionGroup() 

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
     * Sets the value of field 'conditionGroup'.
     * 
     * @param conditionGroup the value of field 'conditionGroup'.
     */
    public void setConditionGroup(global.pluginStructure.ConditionGroup conditionGroup)
    {
        this._conditionGroup = conditionGroup;
    } //-- void setConditionGroup(global.pluginStructure.ConditionGroup) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.ChoiceType) Unmarshaller.unmarshal(global.pluginStructure.ChoiceType.class, reader);
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

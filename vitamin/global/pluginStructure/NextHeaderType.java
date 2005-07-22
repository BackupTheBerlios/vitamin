/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: NextHeaderType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class NextHeaderType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class NextHeaderType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _conditionValue
     */
    private int _conditionValue;

    /**
     * keeps track of state for field: _conditionValue
     */
    private boolean _has_conditionValue;

    /**
     * Field _conditionType
     */
    private java.lang.String _conditionType;

    /**
     * Field _reference
     */
    private global.pluginStructure.Reference _reference;


      //----------------/
     //- Constructors -/
    //----------------/

    public NextHeaderType() {
        super();
    } //-- global.pluginStructure.NextHeaderType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteConditionValue
     */
    public void deleteConditionValue()
    {
        this._has_conditionValue= false;
    } //-- void deleteConditionValue() 

    /**
     * Returns the value of field 'conditionType'.
     * 
     * @return the value of field 'conditionType'.
     */
    public java.lang.String getConditionType()
    {
        return this._conditionType;
    } //-- java.lang.String getConditionType() 

    /**
     * Returns the value of field 'conditionValue'.
     * 
     * @return the value of field 'conditionValue'.
     */
    public int getConditionValue()
    {
        return this._conditionValue;
    } //-- int getConditionValue() 

    /**
     * Returns the value of field 'reference'.
     * 
     * @return the value of field 'reference'.
     */
    public global.pluginStructure.Reference getReference()
    {
        return this._reference;
    } //-- global.pluginStructure.Reference getReference() 

    /**
     * Method hasConditionValue
     */
    public boolean hasConditionValue()
    {
        return this._has_conditionValue;
    } //-- boolean hasConditionValue() 

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
     * Sets the value of field 'conditionType'.
     * 
     * @param conditionType the value of field 'conditionType'.
     */
    public void setConditionType(java.lang.String conditionType)
    {
        this._conditionType = conditionType;
    } //-- void setConditionType(java.lang.String) 

    /**
     * Sets the value of field 'conditionValue'.
     * 
     * @param conditionValue the value of field 'conditionValue'.
     */
    public void setConditionValue(int conditionValue)
    {
        this._conditionValue = conditionValue;
        this._has_conditionValue = true;
    } //-- void setConditionValue(int) 

    /**
     * Sets the value of field 'reference'.
     * 
     * @param reference the value of field 'reference'.
     */
    public void setReference(global.pluginStructure.Reference reference)
    {
        this._reference = reference;
    } //-- void setReference(global.pluginStructure.Reference) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.NextHeaderType) Unmarshaller.unmarshal(global.pluginStructure.NextHeaderType.class, reader);
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

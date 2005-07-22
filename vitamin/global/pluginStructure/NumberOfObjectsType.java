/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: NumberOfObjectsType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class NumberOfObjectsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class NumberOfObjectsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _multiplier
     */
    private int _multiplier;

    /**
     * keeps track of state for field: _multiplier
     */
    private boolean _has_multiplier;

    /**
     * Field _reference
     */
    private global.pluginStructure.Reference _reference;

    /**
     * Field _givenValue
     */
    private global.pluginStructure.GivenValue _givenValue;


      //----------------/
     //- Constructors -/
    //----------------/

    public NumberOfObjectsType() {
        super();
    } //-- global.pluginStructure.NumberOfObjectsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteMultiplier
     */
    public void deleteMultiplier()
    {
        this._has_multiplier= false;
    } //-- void deleteMultiplier() 

    /**
     * Returns the value of field 'givenValue'.
     * 
     * @return the value of field 'givenValue'.
     */
    public global.pluginStructure.GivenValue getGivenValue()
    {
        return this._givenValue;
    } //-- global.pluginStructure.GivenValue getGivenValue() 

    /**
     * Returns the value of field 'multiplier'.
     * 
     * @return the value of field 'multiplier'.
     */
    public int getMultiplier()
    {
        return this._multiplier;
    } //-- int getMultiplier() 

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
     * Method hasMultiplier
     */
    public boolean hasMultiplier()
    {
        return this._has_multiplier;
    } //-- boolean hasMultiplier() 

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
     * Sets the value of field 'givenValue'.
     * 
     * @param givenValue the value of field 'givenValue'.
     */
    public void setGivenValue(global.pluginStructure.GivenValue givenValue)
    {
        this._givenValue = givenValue;
    } //-- void setGivenValue(global.pluginStructure.GivenValue) 

    /**
     * Sets the value of field 'multiplier'.
     * 
     * @param multiplier the value of field 'multiplier'.
     */
    public void setMultiplier(int multiplier)
    {
        this._multiplier = multiplier;
        this._has_multiplier = true;
    } //-- void setMultiplier(int) 

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
        return (global.pluginStructure.NumberOfObjectsType) Unmarshaller.unmarshal(global.pluginStructure.NumberOfObjectsType.class, reader);
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

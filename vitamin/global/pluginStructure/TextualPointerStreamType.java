/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: TextualPointerStreamType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
 */

package global.pluginStructure;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import global.pluginStructure.types.LocationType;
import global.pluginStructure.types.ValueStreamType;
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
 * Class TextualPointerStreamType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class TextualPointerStreamType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _value
     */
    private global.pluginStructure.types.ValueStreamType _value;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _location
     */
    private global.pluginStructure.types.LocationType _location;

    /**
     * Field _row
     */
    private java.lang.String _row;

    /**
     * Field _word
     */
    private java.lang.String _word;

    /**
     * Field _additionalSeperator
     */
    private java.lang.String _additionalSeperator;


      //----------------/
     //- Constructors -/
    //----------------/

    public TextualPointerStreamType() {
        super();
    } //-- global.pluginStructure.TextualPointerStreamType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'additionalSeperator'.
     * 
     * @return the value of field 'additionalSeperator'.
     */
    public java.lang.String getAdditionalSeperator()
    {
        return this._additionalSeperator;
    } //-- java.lang.String getAdditionalSeperator() 

    /**
     * Returns the value of field 'location'.
     * 
     * @return the value of field 'location'.
     */
    public global.pluginStructure.types.LocationType getLocation()
    {
        return this._location;
    } //-- global.pluginStructure.types.LocationType getLocation() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'row'.
     * 
     * @return the value of field 'row'.
     */
    public java.lang.String getRow()
    {
        return this._row;
    } //-- java.lang.String getRow() 

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public global.pluginStructure.types.ValueStreamType getValue()
    {
        return this._value;
    } //-- global.pluginStructure.types.ValueStreamType getValue() 

    /**
     * Returns the value of field 'word'.
     * 
     * @return the value of field 'word'.
     */
    public java.lang.String getWord()
    {
        return this._word;
    } //-- java.lang.String getWord() 

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
     * Sets the value of field 'additionalSeperator'.
     * 
     * @param additionalSeperator the value of field
     * 'additionalSeperator'.
     */
    public void setAdditionalSeperator(java.lang.String additionalSeperator)
    {
        this._additionalSeperator = additionalSeperator;
    } //-- void setAdditionalSeperator(java.lang.String) 

    /**
     * Sets the value of field 'location'.
     * 
     * @param location the value of field 'location'.
     */
    public void setLocation(global.pluginStructure.types.LocationType location)
    {
        this._location = location;
    } //-- void setLocation(global.pluginStructure.types.LocationType) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'row'.
     * 
     * @param row the value of field 'row'.
     */
    public void setRow(java.lang.String row)
    {
        this._row = row;
    } //-- void setRow(java.lang.String) 

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(global.pluginStructure.types.ValueStreamType value)
    {
        this._value = value;
    } //-- void setValue(global.pluginStructure.types.ValueStreamType) 

    /**
     * Sets the value of field 'word'.
     * 
     * @param word the value of field 'word'.
     */
    public void setWord(java.lang.String word)
    {
        this._word = word;
    } //-- void setWord(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.TextualPointerStreamType) Unmarshaller.unmarshal(global.pluginStructure.TextualPointerStreamType.class, reader);
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

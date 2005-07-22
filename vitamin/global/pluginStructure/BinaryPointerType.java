/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: BinaryPointerType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
 */

package global.pluginStructure;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import global.pluginStructure.types.BinaryPointerTypeTypeType;
import global.pluginStructure.types.LocationType;
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
 * Class BinaryPointerType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class BinaryPointerType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _type
     */
    private global.pluginStructure.types.BinaryPointerTypeTypeType _type;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _location
     */
    private global.pluginStructure.types.LocationType _location;

    /**
     * Field _start
     */
    private int _start;

    /**
     * keeps track of state for field: _start
     */
    private boolean _has_start;

    /**
     * Field _length
     */
    private int _length;

    /**
     * keeps track of state for field: _length
     */
    private boolean _has_length;


      //----------------/
     //- Constructors -/
    //----------------/

    public BinaryPointerType() {
        super();
    } //-- global.pluginStructure.BinaryPointerType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteLength
     */
    public void deleteLength()
    {
        this._has_length= false;
    } //-- void deleteLength() 

    /**
     * Method deleteStart
     */
    public void deleteStart()
    {
        this._has_start= false;
    } //-- void deleteStart() 

    /**
     * Returns the value of field 'length'.
     * 
     * @return the value of field 'length'.
     */
    public int getLength()
    {
        return this._length;
    } //-- int getLength() 

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
     * Returns the value of field 'start'.
     * 
     * @return the value of field 'start'.
     */
    public int getStart()
    {
        return this._start;
    } //-- int getStart() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public global.pluginStructure.types.BinaryPointerTypeTypeType getType()
    {
        return this._type;
    } //-- global.pluginStructure.types.BinaryPointerTypeTypeType getType() 

    /**
     * Method hasLength
     */
    public boolean hasLength()
    {
        return this._has_length;
    } //-- boolean hasLength() 

    /**
     * Method hasStart
     */
    public boolean hasStart()
    {
        return this._has_start;
    } //-- boolean hasStart() 

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
     * Sets the value of field 'length'.
     * 
     * @param length the value of field 'length'.
     */
    public void setLength(int length)
    {
        this._length = length;
        this._has_length = true;
    } //-- void setLength(int) 

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
     * Sets the value of field 'start'.
     * 
     * @param start the value of field 'start'.
     */
    public void setStart(int start)
    {
        this._start = start;
        this._has_start = true;
    } //-- void setStart(int) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(global.pluginStructure.types.BinaryPointerTypeTypeType type)
    {
        this._type = type;
    } //-- void setType(global.pluginStructure.types.BinaryPointerTypeTypeType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.BinaryPointerType) Unmarshaller.unmarshal(global.pluginStructure.BinaryPointerType.class, reader);
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

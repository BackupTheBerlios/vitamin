/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: MultipleObjectsType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class MultipleObjectsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class MultipleObjectsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _lengthType
     */
    private java.lang.String _lengthType;

    /**
     * Field _headerLength
     */
    private int _headerLength;

    /**
     * keeps track of state for field: _headerLength
     */
    private boolean _has_headerLength;

    /**
     * Field _objectLength
     */
    private global.pluginStructure.ObjectLength _objectLength;

    /**
     * Field _overallLength
     */
    private global.pluginStructure.OverallLength _overallLength;

    /**
     * Field _numberOfObjects
     */
    private global.pluginStructure.NumberOfObjects _numberOfObjects;

    /**
     * Field _nextHeader
     */
    private global.pluginStructure.NextHeader _nextHeader;


      //----------------/
     //- Constructors -/
    //----------------/

    public MultipleObjectsType() {
        super();
    } //-- global.pluginStructure.MultipleObjectsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteHeaderLength
     */
    public void deleteHeaderLength()
    {
        this._has_headerLength= false;
    } //-- void deleteHeaderLength() 

    /**
     * Returns the value of field 'headerLength'.
     * 
     * @return the value of field 'headerLength'.
     */
    public int getHeaderLength()
    {
        return this._headerLength;
    } //-- int getHeaderLength() 

    /**
     * Returns the value of field 'lengthType'.
     * 
     * @return the value of field 'lengthType'.
     */
    public java.lang.String getLengthType()
    {
        return this._lengthType;
    } //-- java.lang.String getLengthType() 

    /**
     * Returns the value of field 'nextHeader'.
     * 
     * @return the value of field 'nextHeader'.
     */
    public global.pluginStructure.NextHeader getNextHeader()
    {
        return this._nextHeader;
    } //-- global.pluginStructure.NextHeader getNextHeader() 

    /**
     * Returns the value of field 'numberOfObjects'.
     * 
     * @return the value of field 'numberOfObjects'.
     */
    public global.pluginStructure.NumberOfObjects getNumberOfObjects()
    {
        return this._numberOfObjects;
    } //-- global.pluginStructure.NumberOfObjects getNumberOfObjects() 

    /**
     * Returns the value of field 'objectLength'.
     * 
     * @return the value of field 'objectLength'.
     */
    public global.pluginStructure.ObjectLength getObjectLength()
    {
        return this._objectLength;
    } //-- global.pluginStructure.ObjectLength getObjectLength() 

    /**
     * Returns the value of field 'overallLength'.
     * 
     * @return the value of field 'overallLength'.
     */
    public global.pluginStructure.OverallLength getOverallLength()
    {
        return this._overallLength;
    } //-- global.pluginStructure.OverallLength getOverallLength() 

    /**
     * Method hasHeaderLength
     */
    public boolean hasHeaderLength()
    {
        return this._has_headerLength;
    } //-- boolean hasHeaderLength() 

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
     * Sets the value of field 'headerLength'.
     * 
     * @param headerLength the value of field 'headerLength'.
     */
    public void setHeaderLength(int headerLength)
    {
        this._headerLength = headerLength;
        this._has_headerLength = true;
    } //-- void setHeaderLength(int) 

    /**
     * Sets the value of field 'lengthType'.
     * 
     * @param lengthType the value of field 'lengthType'.
     */
    public void setLengthType(java.lang.String lengthType)
    {
        this._lengthType = lengthType;
    } //-- void setLengthType(java.lang.String) 

    /**
     * Sets the value of field 'nextHeader'.
     * 
     * @param nextHeader the value of field 'nextHeader'.
     */
    public void setNextHeader(global.pluginStructure.NextHeader nextHeader)
    {
        this._nextHeader = nextHeader;
    } //-- void setNextHeader(global.pluginStructure.NextHeader) 

    /**
     * Sets the value of field 'numberOfObjects'.
     * 
     * @param numberOfObjects the value of field 'numberOfObjects'.
     */
    public void setNumberOfObjects(global.pluginStructure.NumberOfObjects numberOfObjects)
    {
        this._numberOfObjects = numberOfObjects;
    } //-- void setNumberOfObjects(global.pluginStructure.NumberOfObjects) 

    /**
     * Sets the value of field 'objectLength'.
     * 
     * @param objectLength the value of field 'objectLength'.
     */
    public void setObjectLength(global.pluginStructure.ObjectLength objectLength)
    {
        this._objectLength = objectLength;
    } //-- void setObjectLength(global.pluginStructure.ObjectLength) 

    /**
     * Sets the value of field 'overallLength'.
     * 
     * @param overallLength the value of field 'overallLength'.
     */
    public void setOverallLength(global.pluginStructure.OverallLength overallLength)
    {
        this._overallLength = overallLength;
    } //-- void setOverallLength(global.pluginStructure.OverallLength) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.MultipleObjectsType) Unmarshaller.unmarshal(global.pluginStructure.MultipleObjectsType.class, reader);
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

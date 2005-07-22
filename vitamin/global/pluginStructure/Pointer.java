/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: Pointer.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class Pointer.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class Pointer implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _binaryPointer
     */
    private global.pluginStructure.BinaryPointer _binaryPointer;

    /**
     * Field _binaryPointerWithValue
     */
    private global.pluginStructure.BinaryPointerWithValue _binaryPointerWithValue;

    /**
     * Field _textualPointer
     */
    private global.pluginStructure.TextualPointer _textualPointer;

    /**
     * Field _textualPointerWithValue
     */
    private global.pluginStructure.TextualPointerWithValue _textualPointerWithValue;

    /**
     * Field _binaryPointerStream
     */
    private global.pluginStructure.BinaryPointerStream _binaryPointerStream;

    /**
     * Field _textualPointerStream
     */
    private global.pluginStructure.TextualPointerStream _textualPointerStream;


      //----------------/
     //- Constructors -/
    //----------------/

    public Pointer() {
        super();
    } //-- global.pluginStructure.Pointer()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'binaryPointer'.
     * 
     * @return the value of field 'binaryPointer'.
     */
    public global.pluginStructure.BinaryPointer getBinaryPointer()
    {
        return this._binaryPointer;
    } //-- global.pluginStructure.BinaryPointer getBinaryPointer() 

    /**
     * Returns the value of field 'binaryPointerStream'.
     * 
     * @return the value of field 'binaryPointerStream'.
     */
    public global.pluginStructure.BinaryPointerStream getBinaryPointerStream()
    {
        return this._binaryPointerStream;
    } //-- global.pluginStructure.BinaryPointerStream getBinaryPointerStream() 

    /**
     * Returns the value of field 'binaryPointerWithValue'.
     * 
     * @return the value of field 'binaryPointerWithValue'.
     */
    public global.pluginStructure.BinaryPointerWithValue getBinaryPointerWithValue()
    {
        return this._binaryPointerWithValue;
    } //-- global.pluginStructure.BinaryPointerWithValue getBinaryPointerWithValue() 

    /**
     * Returns the value of field 'textualPointer'.
     * 
     * @return the value of field 'textualPointer'.
     */
    public global.pluginStructure.TextualPointer getTextualPointer()
    {
        return this._textualPointer;
    } //-- global.pluginStructure.TextualPointer getTextualPointer() 

    /**
     * Returns the value of field 'textualPointerStream'.
     * 
     * @return the value of field 'textualPointerStream'.
     */
    public global.pluginStructure.TextualPointerStream getTextualPointerStream()
    {
        return this._textualPointerStream;
    } //-- global.pluginStructure.TextualPointerStream getTextualPointerStream() 

    /**
     * Returns the value of field 'textualPointerWithValue'.
     * 
     * @return the value of field 'textualPointerWithValue'.
     */
    public global.pluginStructure.TextualPointerWithValue getTextualPointerWithValue()
    {
        return this._textualPointerWithValue;
    } //-- global.pluginStructure.TextualPointerWithValue getTextualPointerWithValue() 

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
     * Sets the value of field 'binaryPointer'.
     * 
     * @param binaryPointer the value of field 'binaryPointer'.
     */
    public void setBinaryPointer(global.pluginStructure.BinaryPointer binaryPointer)
    {
        this._binaryPointer = binaryPointer;
    } //-- void setBinaryPointer(global.pluginStructure.BinaryPointer) 

    /**
     * Sets the value of field 'binaryPointerStream'.
     * 
     * @param binaryPointerStream the value of field
     * 'binaryPointerStream'.
     */
    public void setBinaryPointerStream(global.pluginStructure.BinaryPointerStream binaryPointerStream)
    {
        this._binaryPointerStream = binaryPointerStream;
    } //-- void setBinaryPointerStream(global.pluginStructure.BinaryPointerStream) 

    /**
     * Sets the value of field 'binaryPointerWithValue'.
     * 
     * @param binaryPointerWithValue the value of field
     * 'binaryPointerWithValue'.
     */
    public void setBinaryPointerWithValue(global.pluginStructure.BinaryPointerWithValue binaryPointerWithValue)
    {
        this._binaryPointerWithValue = binaryPointerWithValue;
    } //-- void setBinaryPointerWithValue(global.pluginStructure.BinaryPointerWithValue) 

    /**
     * Sets the value of field 'textualPointer'.
     * 
     * @param textualPointer the value of field 'textualPointer'.
     */
    public void setTextualPointer(global.pluginStructure.TextualPointer textualPointer)
    {
        this._textualPointer = textualPointer;
    } //-- void setTextualPointer(global.pluginStructure.TextualPointer) 

    /**
     * Sets the value of field 'textualPointerStream'.
     * 
     * @param textualPointerStream the value of field
     * 'textualPointerStream'.
     */
    public void setTextualPointerStream(global.pluginStructure.TextualPointerStream textualPointerStream)
    {
        this._textualPointerStream = textualPointerStream;
    } //-- void setTextualPointerStream(global.pluginStructure.TextualPointerStream) 

    /**
     * Sets the value of field 'textualPointerWithValue'.
     * 
     * @param textualPointerWithValue the value of field
     * 'textualPointerWithValue'.
     */
    public void setTextualPointerWithValue(global.pluginStructure.TextualPointerWithValue textualPointerWithValue)
    {
        this._textualPointerWithValue = textualPointerWithValue;
    } //-- void setTextualPointerWithValue(global.pluginStructure.TextualPointerWithValue) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.Pointer) Unmarshaller.unmarshal(global.pluginStructure.Pointer.class, reader);
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

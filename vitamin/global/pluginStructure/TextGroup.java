/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: TextGroup.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class TextGroup.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class TextGroup implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _reference
     */
    private global.pluginStructure.Reference _reference;

    /**
     * Field _givenText
     */
    private global.pluginStructure.GivenText _givenText;


      //----------------/
     //- Constructors -/
    //----------------/

    public TextGroup() {
        super();
    } //-- global.pluginStructure.TextGroup()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'givenText'.
     * 
     * @return the value of field 'givenText'.
     */
    public global.pluginStructure.GivenText getGivenText()
    {
        return this._givenText;
    } //-- global.pluginStructure.GivenText getGivenText() 

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
     * Sets the value of field 'givenText'.
     * 
     * @param givenText the value of field 'givenText'.
     */
    public void setGivenText(global.pluginStructure.GivenText givenText)
    {
        this._givenText = givenText;
    } //-- void setGivenText(global.pluginStructure.GivenText) 

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
        return (global.pluginStructure.TextGroup) Unmarshaller.unmarshal(global.pluginStructure.TextGroup.class, reader);
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

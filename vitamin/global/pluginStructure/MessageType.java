/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: MessageType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class MessageType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class MessageType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sourceAndDestination
     */
    private global.pluginStructure.SourceAndDestination _sourceAndDestination;

    /**
     * Field _textGroup
     */
    private global.pluginStructure.TextGroup _textGroup;

    /**
     * Field _conditionsGroup
     */
    private global.pluginStructure.ConditionsGroup _conditionsGroup;


      //----------------/
     //- Constructors -/
    //----------------/

    public MessageType() {
        super();
    } //-- global.pluginStructure.MessageType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'conditionsGroup'.
     * 
     * @return the value of field 'conditionsGroup'.
     */
    public global.pluginStructure.ConditionsGroup getConditionsGroup()
    {
        return this._conditionsGroup;
    } //-- global.pluginStructure.ConditionsGroup getConditionsGroup() 

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
     * Returns the value of field 'textGroup'.
     * 
     * @return the value of field 'textGroup'.
     */
    public global.pluginStructure.TextGroup getTextGroup()
    {
        return this._textGroup;
    } //-- global.pluginStructure.TextGroup getTextGroup() 

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
     * Sets the value of field 'conditionsGroup'.
     * 
     * @param conditionsGroup the value of field 'conditionsGroup'.
     */
    public void setConditionsGroup(global.pluginStructure.ConditionsGroup conditionsGroup)
    {
        this._conditionsGroup = conditionsGroup;
    } //-- void setConditionsGroup(global.pluginStructure.ConditionsGroup) 

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
     * Sets the value of field 'textGroup'.
     * 
     * @param textGroup the value of field 'textGroup'.
     */
    public void setTextGroup(global.pluginStructure.TextGroup textGroup)
    {
        this._textGroup = textGroup;
    } //-- void setTextGroup(global.pluginStructure.TextGroup) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.MessageType) Unmarshaller.unmarshal(global.pluginStructure.MessageType.class, reader);
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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: PluginType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class PluginType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class PluginType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _classify
     */
    private global.pluginStructure.Classify _classify;

    /**
     * Field _analyze
     */
    private global.pluginStructure.Analyze _analyze;


      //----------------/
     //- Constructors -/
    //----------------/

    public PluginType() {
        super();
    } //-- global.pluginStructure.PluginType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'analyze'.
     * 
     * @return the value of field 'analyze'.
     */
    public global.pluginStructure.Analyze getAnalyze()
    {
        return this._analyze;
    } //-- global.pluginStructure.Analyze getAnalyze() 

    /**
     * Returns the value of field 'classify'.
     * 
     * @return the value of field 'classify'.
     */
    public global.pluginStructure.Classify getClassify()
    {
        return this._classify;
    } //-- global.pluginStructure.Classify getClassify() 

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
     * Sets the value of field 'analyze'.
     * 
     * @param analyze the value of field 'analyze'.
     */
    public void setAnalyze(global.pluginStructure.Analyze analyze)
    {
        this._analyze = analyze;
    } //-- void setAnalyze(global.pluginStructure.Analyze) 

    /**
     * Sets the value of field 'classify'.
     * 
     * @param classify the value of field 'classify'.
     */
    public void setClassify(global.pluginStructure.Classify classify)
    {
        this._classify = classify;
    } //-- void setClassify(global.pluginStructure.Classify) 

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
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.PluginType) Unmarshaller.unmarshal(global.pluginStructure.PluginType.class, reader);
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

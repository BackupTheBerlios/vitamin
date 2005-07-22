/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: NodeType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
 */

package master.topology.structure;

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
 * Class NodeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class NodeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * Field _ip
     */
    private java.lang.String _ip;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _mask
     */
    private int _mask;

    /**
     * keeps track of state for field: _mask
     */
    private boolean _has_mask;


      //----------------/
     //- Constructors -/
    //----------------/

    public NodeType() {
        super();
        setContent("");
    } //-- master.topology.structure.NodeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteMask
     */
    public void deleteMask()
    {
        this._has_mask= false;
    } //-- void deleteMask() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Returns the value of field 'ip'.
     * 
     * @return the value of field 'ip'.
     */
    public java.lang.String getIp()
    {
        return this._ip;
    } //-- java.lang.String getIp() 

    /**
     * Returns the value of field 'mask'.
     * 
     * @return the value of field 'mask'.
     */
    public int getMask()
    {
        return this._mask;
    } //-- int getMask() 

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
     * Method hasMask
     */
    public boolean hasMask()
    {
        return this._has_mask;
    } //-- boolean hasMask() 

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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Sets the value of field 'ip'.
     * 
     * @param ip the value of field 'ip'.
     */
    public void setIp(java.lang.String ip)
    {
        this._ip = ip;
    } //-- void setIp(java.lang.String) 

    /**
     * Sets the value of field 'mask'.
     * 
     * @param mask the value of field 'mask'.
     */
    public void setMask(int mask)
    {
        this._mask = mask;
        this._has_mask = true;
    } //-- void setMask(int) 

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
        return (master.topology.structure.NodeType) Unmarshaller.unmarshal(master.topology.structure.NodeType.class, reader);
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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: SubnetType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
 */

package master.topology.structure;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import master.topology.structure.types.SubnetTypeType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class SubnetType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class SubnetType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _type
     */
    private master.topology.structure.types.SubnetTypeType _type;

    /**
     * Field _ip
     */
    private java.lang.String _ip;

    /**
     * Field _mask
     */
    private int _mask;

    /**
     * keeps track of state for field: _mask
     */
    private boolean _has_mask;

    /**
     * Field _gateway
     */
    private java.lang.String _gateway;

    /**
     * Field _nodeList
     */
    private java.util.Vector _nodeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SubnetType() {
        super();
        _nodeList = new Vector();
    } //-- master.topology.structure.SubnetType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addNode
     * 
     * @param vNode
     */
    public void addNode(master.topology.structure.Node vNode)
        throws java.lang.IndexOutOfBoundsException
    {
        _nodeList.addElement(vNode);
    } //-- void addNode(master.topology.structure.Node) 

    /**
     * Method addNode
     * 
     * @param index
     * @param vNode
     */
    public void addNode(int index, master.topology.structure.Node vNode)
        throws java.lang.IndexOutOfBoundsException
    {
        _nodeList.insertElementAt(vNode, index);
    } //-- void addNode(int, master.topology.structure.Node) 

    /**
     * Method deleteMask
     */
    public void deleteMask()
    {
        this._has_mask= false;
    } //-- void deleteMask() 

    /**
     * Method enumerateNode
     */
    public java.util.Enumeration enumerateNode()
    {
        return _nodeList.elements();
    } //-- java.util.Enumeration enumerateNode() 

    /**
     * Returns the value of field 'gateway'.
     * 
     * @return the value of field 'gateway'.
     */
    public java.lang.String getGateway()
    {
        return this._gateway;
    } //-- java.lang.String getGateway() 

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
     * Method getNode
     * 
     * @param index
     */
    public master.topology.structure.Node getNode(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _nodeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (master.topology.structure.Node) _nodeList.elementAt(index);
    } //-- master.topology.structure.Node getNode(int) 

    /**
     * Method getNode
     */
    public master.topology.structure.Node[] getNode()
    {
        int size = _nodeList.size();
        master.topology.structure.Node[] mArray = new master.topology.structure.Node[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (master.topology.structure.Node) _nodeList.elementAt(index);
        }
        return mArray;
    } //-- master.topology.structure.Node[] getNode() 

    /**
     * Method getNodeCount
     */
    public int getNodeCount()
    {
        return _nodeList.size();
    } //-- int getNodeCount() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public master.topology.structure.types.SubnetTypeType getType()
    {
        return this._type;
    } //-- master.topology.structure.types.SubnetTypeType getType() 

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
     * Method removeAllNode
     */
    public void removeAllNode()
    {
        _nodeList.removeAllElements();
    } //-- void removeAllNode() 

    /**
     * Method removeNode
     * 
     * @param index
     */
    public master.topology.structure.Node removeNode(int index)
    {
        java.lang.Object obj = _nodeList.elementAt(index);
        _nodeList.removeElementAt(index);
        return (master.topology.structure.Node) obj;
    } //-- master.topology.structure.Node removeNode(int) 

    /**
     * Sets the value of field 'gateway'.
     * 
     * @param gateway the value of field 'gateway'.
     */
    public void setGateway(java.lang.String gateway)
    {
        this._gateway = gateway;
    } //-- void setGateway(java.lang.String) 

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
     * Method setNode
     * 
     * @param index
     * @param vNode
     */
    public void setNode(int index, master.topology.structure.Node vNode)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _nodeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _nodeList.setElementAt(vNode, index);
    } //-- void setNode(int, master.topology.structure.Node) 

    /**
     * Method setNode
     * 
     * @param nodeArray
     */
    public void setNode(master.topology.structure.Node[] nodeArray)
    {
        //-- copy array
        _nodeList.removeAllElements();
        for (int i = 0; i < nodeArray.length; i++) {
            _nodeList.addElement(nodeArray[i]);
        }
    } //-- void setNode(master.topology.structure.Node) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(master.topology.structure.types.SubnetTypeType type)
    {
        this._type = type;
    } //-- void setType(master.topology.structure.types.SubnetTypeType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (master.topology.structure.SubnetType) Unmarshaller.unmarshal(master.topology.structure.SubnetType.class, reader);
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

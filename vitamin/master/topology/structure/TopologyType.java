/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: TopologyType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class TopologyType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class TopologyType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _subnetList
     */
    private java.util.Vector _subnetList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TopologyType() {
        super();
        _subnetList = new Vector();
    } //-- master.topology.structure.TopologyType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addSubnet
     * 
     * @param vSubnet
     */
    public void addSubnet(master.topology.structure.Subnet vSubnet)
        throws java.lang.IndexOutOfBoundsException
    {
        _subnetList.addElement(vSubnet);
    } //-- void addSubnet(master.topology.structure.Subnet) 

    /**
     * Method addSubnet
     * 
     * @param index
     * @param vSubnet
     */
    public void addSubnet(int index, master.topology.structure.Subnet vSubnet)
        throws java.lang.IndexOutOfBoundsException
    {
        _subnetList.insertElementAt(vSubnet, index);
    } //-- void addSubnet(int, master.topology.structure.Subnet) 

    /**
     * Method enumerateSubnet
     */
    public java.util.Enumeration enumerateSubnet()
    {
        return _subnetList.elements();
    } //-- java.util.Enumeration enumerateSubnet() 

    /**
     * Method getSubnet
     * 
     * @param index
     */
    public master.topology.structure.Subnet getSubnet(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _subnetList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (master.topology.structure.Subnet) _subnetList.elementAt(index);
    } //-- master.topology.structure.Subnet getSubnet(int) 

    /**
     * Method getSubnet
     */
    public master.topology.structure.Subnet[] getSubnet()
    {
        int size = _subnetList.size();
        master.topology.structure.Subnet[] mArray = new master.topology.structure.Subnet[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (master.topology.structure.Subnet) _subnetList.elementAt(index);
        }
        return mArray;
    } //-- master.topology.structure.Subnet[] getSubnet() 

    /**
     * Method getSubnetCount
     */
    public int getSubnetCount()
    {
        return _subnetList.size();
    } //-- int getSubnetCount() 

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
     * Method removeAllSubnet
     */
    public void removeAllSubnet()
    {
        _subnetList.removeAllElements();
    } //-- void removeAllSubnet() 

    /**
     * Method removeSubnet
     * 
     * @param index
     */
    public master.topology.structure.Subnet removeSubnet(int index)
    {
        java.lang.Object obj = _subnetList.elementAt(index);
        _subnetList.removeElementAt(index);
        return (master.topology.structure.Subnet) obj;
    } //-- master.topology.structure.Subnet removeSubnet(int) 

    /**
     * Method setSubnet
     * 
     * @param index
     * @param vSubnet
     */
    public void setSubnet(int index, master.topology.structure.Subnet vSubnet)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _subnetList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _subnetList.setElementAt(vSubnet, index);
    } //-- void setSubnet(int, master.topology.structure.Subnet) 

    /**
     * Method setSubnet
     * 
     * @param subnetArray
     */
    public void setSubnet(master.topology.structure.Subnet[] subnetArray)
    {
        //-- copy array
        _subnetList.removeAllElements();
        for (int i = 0; i < subnetArray.length; i++) {
            _subnetList.addElement(subnetArray[i]);
        }
    } //-- void setSubnet(master.topology.structure.Subnet) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (master.topology.structure.TopologyType) Unmarshaller.unmarshal(master.topology.structure.TopologyType.class, reader);
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

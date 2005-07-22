/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ComparatorsType.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
 */

package global.pluginStructure;

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
 * Class ComparatorsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class ComparatorsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _comparatorList
     */
    private java.util.Vector _comparatorList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComparatorsType() {
        super();
        _comparatorList = new Vector();
    } //-- global.pluginStructure.ComparatorsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addComparator
     * 
     * @param vComparator
     */
    public void addComparator(global.pluginStructure.Comparator vComparator)
        throws java.lang.IndexOutOfBoundsException
    {
        _comparatorList.addElement(vComparator);
    } //-- void addComparator(global.pluginStructure.Comparator) 

    /**
     * Method addComparator
     * 
     * @param index
     * @param vComparator
     */
    public void addComparator(int index, global.pluginStructure.Comparator vComparator)
        throws java.lang.IndexOutOfBoundsException
    {
        _comparatorList.insertElementAt(vComparator, index);
    } //-- void addComparator(int, global.pluginStructure.Comparator) 

    /**
     * Method enumerateComparator
     */
    public java.util.Enumeration enumerateComparator()
    {
        return _comparatorList.elements();
    } //-- java.util.Enumeration enumerateComparator() 

    /**
     * Method getComparator
     * 
     * @param index
     */
    public global.pluginStructure.Comparator getComparator(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _comparatorList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (global.pluginStructure.Comparator) _comparatorList.elementAt(index);
    } //-- global.pluginStructure.Comparator getComparator(int) 

    /**
     * Method getComparator
     */
    public global.pluginStructure.Comparator[] getComparator()
    {
        int size = _comparatorList.size();
        global.pluginStructure.Comparator[] mArray = new global.pluginStructure.Comparator[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (global.pluginStructure.Comparator) _comparatorList.elementAt(index);
        }
        return mArray;
    } //-- global.pluginStructure.Comparator[] getComparator() 

    /**
     * Method getComparatorCount
     */
    public int getComparatorCount()
    {
        return _comparatorList.size();
    } //-- int getComparatorCount() 

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
     * Method removeAllComparator
     */
    public void removeAllComparator()
    {
        _comparatorList.removeAllElements();
    } //-- void removeAllComparator() 

    /**
     * Method removeComparator
     * 
     * @param index
     */
    public global.pluginStructure.Comparator removeComparator(int index)
    {
        java.lang.Object obj = _comparatorList.elementAt(index);
        _comparatorList.removeElementAt(index);
        return (global.pluginStructure.Comparator) obj;
    } //-- global.pluginStructure.Comparator removeComparator(int) 

    /**
     * Method setComparator
     * 
     * @param index
     * @param vComparator
     */
    public void setComparator(int index, global.pluginStructure.Comparator vComparator)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _comparatorList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _comparatorList.setElementAt(vComparator, index);
    } //-- void setComparator(int, global.pluginStructure.Comparator) 

    /**
     * Method setComparator
     * 
     * @param comparatorArray
     */
    public void setComparator(global.pluginStructure.Comparator[] comparatorArray)
    {
        //-- copy array
        _comparatorList.removeAllElements();
        for (int i = 0; i < comparatorArray.length; i++) {
            _comparatorList.addElement(comparatorArray[i]);
        }
    } //-- void setComparator(global.pluginStructure.Comparator) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.ComparatorsType) Unmarshaller.unmarshal(global.pluginStructure.ComparatorsType.class, reader);
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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ConditionGroup.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $
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
 * Class ConditionGroup.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:11 $
 */
public class ConditionGroup implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _conditionList
     */
    private java.util.Vector _conditionList;

    /**
     * Field _choiceList
     */
    private java.util.Vector _choiceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConditionGroup() {
        super();
        _conditionList = new Vector();
        _choiceList = new Vector();
    } //-- global.pluginStructure.ConditionGroup()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addChoice
     * 
     * @param vChoice
     */
    public void addChoice(global.pluginStructure.Choice vChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        _choiceList.addElement(vChoice);
    } //-- void addChoice(global.pluginStructure.Choice) 

    /**
     * Method addChoice
     * 
     * @param index
     * @param vChoice
     */
    public void addChoice(int index, global.pluginStructure.Choice vChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        _choiceList.insertElementAt(vChoice, index);
    } //-- void addChoice(int, global.pluginStructure.Choice) 

    /**
     * Method addCondition
     * 
     * @param vCondition
     */
    public void addCondition(global.pluginStructure.Condition vCondition)
        throws java.lang.IndexOutOfBoundsException
    {
        _conditionList.addElement(vCondition);
    } //-- void addCondition(global.pluginStructure.Condition) 

    /**
     * Method addCondition
     * 
     * @param index
     * @param vCondition
     */
    public void addCondition(int index, global.pluginStructure.Condition vCondition)
        throws java.lang.IndexOutOfBoundsException
    {
        _conditionList.insertElementAt(vCondition, index);
    } //-- void addCondition(int, global.pluginStructure.Condition) 

    /**
     * Method enumerateChoice
     */
    public java.util.Enumeration enumerateChoice()
    {
        return _choiceList.elements();
    } //-- java.util.Enumeration enumerateChoice() 

    /**
     * Method enumerateCondition
     */
    public java.util.Enumeration enumerateCondition()
    {
        return _conditionList.elements();
    } //-- java.util.Enumeration enumerateCondition() 

    /**
     * Method getChoice
     * 
     * @param index
     */
    public global.pluginStructure.Choice getChoice(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _choiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (global.pluginStructure.Choice) _choiceList.elementAt(index);
    } //-- global.pluginStructure.Choice getChoice(int) 

    /**
     * Method getChoice
     */
    public global.pluginStructure.Choice[] getChoice()
    {
        int size = _choiceList.size();
        global.pluginStructure.Choice[] mArray = new global.pluginStructure.Choice[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (global.pluginStructure.Choice) _choiceList.elementAt(index);
        }
        return mArray;
    } //-- global.pluginStructure.Choice[] getChoice() 

    /**
     * Method getChoiceCount
     */
    public int getChoiceCount()
    {
        return _choiceList.size();
    } //-- int getChoiceCount() 

    /**
     * Method getCondition
     * 
     * @param index
     */
    public global.pluginStructure.Condition getCondition(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _conditionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (global.pluginStructure.Condition) _conditionList.elementAt(index);
    } //-- global.pluginStructure.Condition getCondition(int) 

    /**
     * Method getCondition
     */
    public global.pluginStructure.Condition[] getCondition()
    {
        int size = _conditionList.size();
        global.pluginStructure.Condition[] mArray = new global.pluginStructure.Condition[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (global.pluginStructure.Condition) _conditionList.elementAt(index);
        }
        return mArray;
    } //-- global.pluginStructure.Condition[] getCondition() 

    /**
     * Method getConditionCount
     */
    public int getConditionCount()
    {
        return _conditionList.size();
    } //-- int getConditionCount() 

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
     * Method removeAllChoice
     */
    public void removeAllChoice()
    {
        _choiceList.removeAllElements();
    } //-- void removeAllChoice() 

    /**
     * Method removeAllCondition
     */
    public void removeAllCondition()
    {
        _conditionList.removeAllElements();
    } //-- void removeAllCondition() 

    /**
     * Method removeChoice
     * 
     * @param index
     */
    public global.pluginStructure.Choice removeChoice(int index)
    {
        java.lang.Object obj = _choiceList.elementAt(index);
        _choiceList.removeElementAt(index);
        return (global.pluginStructure.Choice) obj;
    } //-- global.pluginStructure.Choice removeChoice(int) 

    /**
     * Method removeCondition
     * 
     * @param index
     */
    public global.pluginStructure.Condition removeCondition(int index)
    {
        java.lang.Object obj = _conditionList.elementAt(index);
        _conditionList.removeElementAt(index);
        return (global.pluginStructure.Condition) obj;
    } //-- global.pluginStructure.Condition removeCondition(int) 

    /**
     * Method setChoice
     * 
     * @param index
     * @param vChoice
     */
    public void setChoice(int index, global.pluginStructure.Choice vChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _choiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _choiceList.setElementAt(vChoice, index);
    } //-- void setChoice(int, global.pluginStructure.Choice) 

    /**
     * Method setChoice
     * 
     * @param choiceArray
     */
    public void setChoice(global.pluginStructure.Choice[] choiceArray)
    {
        //-- copy array
        _choiceList.removeAllElements();
        for (int i = 0; i < choiceArray.length; i++) {
            _choiceList.addElement(choiceArray[i]);
        }
    } //-- void setChoice(global.pluginStructure.Choice) 

    /**
     * Method setCondition
     * 
     * @param index
     * @param vCondition
     */
    public void setCondition(int index, global.pluginStructure.Condition vCondition)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _conditionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _conditionList.setElementAt(vCondition, index);
    } //-- void setCondition(int, global.pluginStructure.Condition) 

    /**
     * Method setCondition
     * 
     * @param conditionArray
     */
    public void setCondition(global.pluginStructure.Condition[] conditionArray)
    {
        //-- copy array
        _conditionList.removeAllElements();
        for (int i = 0; i < conditionArray.length; i++) {
            _conditionList.addElement(conditionArray[i]);
        }
    } //-- void setCondition(global.pluginStructure.Condition) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.ConditionGroup) Unmarshaller.unmarshal(global.pluginStructure.ConditionGroup.class, reader);
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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: AnalyzeType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
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
 * Class AnalyzeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class AnalyzeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _multipleObjects
     */
    private global.pluginStructure.MultipleObjects _multipleObjects;

    /**
     * Field _streamList
     */
    private java.util.Vector _streamList;

    /**
     * Field _connectionList
     */
    private java.util.Vector _connectionList;

    /**
     * Field _connectionEndList
     */
    private java.util.Vector _connectionEndList;

    /**
     * Field _messageList
     */
    private java.util.Vector _messageList;

    /**
     * Field _nameList
     */
    private java.util.Vector _nameList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AnalyzeType() {
        super();
        _streamList = new Vector();
        _connectionList = new Vector();
        _connectionEndList = new Vector();
        _messageList = new Vector();
        _nameList = new Vector();
    } //-- global.pluginStructure.AnalyzeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addConnection
     * 
     * @param vConnection
     */
    public void addConnection(global.pluginStructure.Connection vConnection)
        throws java.lang.IndexOutOfBoundsException
    {
        _connectionList.addElement(vConnection);
    } //-- void addConnection(global.pluginStructure.Connection) 

    /**
     * Method addConnection
     * 
     * @param index
     * @param vConnection
     */
    public void addConnection(int index, global.pluginStructure.Connection vConnection)
        throws java.lang.IndexOutOfBoundsException
    {
        _connectionList.insertElementAt(vConnection, index);
    } //-- void addConnection(int, global.pluginStructure.Connection) 

    /**
     * Method addConnectionEnd
     * 
     * @param vConnectionEnd
     */
    public void addConnectionEnd(global.pluginStructure.ConnectionEnd vConnectionEnd)
        throws java.lang.IndexOutOfBoundsException
    {
        _connectionEndList.addElement(vConnectionEnd);
    } //-- void addConnectionEnd(global.pluginStructure.ConnectionEnd) 

    /**
     * Method addConnectionEnd
     * 
     * @param index
     * @param vConnectionEnd
     */
    public void addConnectionEnd(int index, global.pluginStructure.ConnectionEnd vConnectionEnd)
        throws java.lang.IndexOutOfBoundsException
    {
        _connectionEndList.insertElementAt(vConnectionEnd, index);
    } //-- void addConnectionEnd(int, global.pluginStructure.ConnectionEnd) 

    /**
     * Method addMessage
     * 
     * @param vMessage
     */
    public void addMessage(global.pluginStructure.Message vMessage)
        throws java.lang.IndexOutOfBoundsException
    {
        _messageList.addElement(vMessage);
    } //-- void addMessage(global.pluginStructure.Message) 

    /**
     * Method addMessage
     * 
     * @param index
     * @param vMessage
     */
    public void addMessage(int index, global.pluginStructure.Message vMessage)
        throws java.lang.IndexOutOfBoundsException
    {
        _messageList.insertElementAt(vMessage, index);
    } //-- void addMessage(int, global.pluginStructure.Message) 

    /**
     * Method addName
     * 
     * @param vName
     */
    public void addName(global.pluginStructure.Name vName)
        throws java.lang.IndexOutOfBoundsException
    {
        _nameList.addElement(vName);
    } //-- void addName(global.pluginStructure.Name) 

    /**
     * Method addName
     * 
     * @param index
     * @param vName
     */
    public void addName(int index, global.pluginStructure.Name vName)
        throws java.lang.IndexOutOfBoundsException
    {
        _nameList.insertElementAt(vName, index);
    } //-- void addName(int, global.pluginStructure.Name) 

    /**
     * Method addStream
     * 
     * @param vStream
     */
    public void addStream(global.pluginStructure.Stream vStream)
        throws java.lang.IndexOutOfBoundsException
    {
        _streamList.addElement(vStream);
    } //-- void addStream(global.pluginStructure.Stream) 

    /**
     * Method addStream
     * 
     * @param index
     * @param vStream
     */
    public void addStream(int index, global.pluginStructure.Stream vStream)
        throws java.lang.IndexOutOfBoundsException
    {
        _streamList.insertElementAt(vStream, index);
    } //-- void addStream(int, global.pluginStructure.Stream) 

    /**
     * Method enumerateConnection
     */
    public java.util.Enumeration enumerateConnection()
    {
        return _connectionList.elements();
    } //-- java.util.Enumeration enumerateConnection() 

    /**
     * Method enumerateConnectionEnd
     */
    public java.util.Enumeration enumerateConnectionEnd()
    {
        return _connectionEndList.elements();
    } //-- java.util.Enumeration enumerateConnectionEnd() 

    /**
     * Method enumerateMessage
     */
    public java.util.Enumeration enumerateMessage()
    {
        return _messageList.elements();
    } //-- java.util.Enumeration enumerateMessage() 

    /**
     * Method enumerateName
     */
    public java.util.Enumeration enumerateName()
    {
        return _nameList.elements();
    } //-- java.util.Enumeration enumerateName() 

    /**
     * Method enumerateStream
     */
    public java.util.Enumeration enumerateStream()
    {
        return _streamList.elements();
    } //-- java.util.Enumeration enumerateStream() 

    /**
     * Method getConnection
     * 
     * @param index
     */
    public global.pluginStructure.Connection getConnection(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _connectionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (global.pluginStructure.Connection) _connectionList.elementAt(index);
    } //-- global.pluginStructure.Connection getConnection(int) 

    /**
     * Method getConnection
     */
    public global.pluginStructure.Connection[] getConnection()
    {
        int size = _connectionList.size();
        global.pluginStructure.Connection[] mArray = new global.pluginStructure.Connection[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (global.pluginStructure.Connection) _connectionList.elementAt(index);
        }
        return mArray;
    } //-- global.pluginStructure.Connection[] getConnection() 

    /**
     * Method getConnectionCount
     */
    public int getConnectionCount()
    {
        return _connectionList.size();
    } //-- int getConnectionCount() 

    /**
     * Method getConnectionEnd
     * 
     * @param index
     */
    public global.pluginStructure.ConnectionEnd getConnectionEnd(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _connectionEndList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (global.pluginStructure.ConnectionEnd) _connectionEndList.elementAt(index);
    } //-- global.pluginStructure.ConnectionEnd getConnectionEnd(int) 

    /**
     * Method getConnectionEnd
     */
    public global.pluginStructure.ConnectionEnd[] getConnectionEnd()
    {
        int size = _connectionEndList.size();
        global.pluginStructure.ConnectionEnd[] mArray = new global.pluginStructure.ConnectionEnd[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (global.pluginStructure.ConnectionEnd) _connectionEndList.elementAt(index);
        }
        return mArray;
    } //-- global.pluginStructure.ConnectionEnd[] getConnectionEnd() 

    /**
     * Method getConnectionEndCount
     */
    public int getConnectionEndCount()
    {
        return _connectionEndList.size();
    } //-- int getConnectionEndCount() 

    /**
     * Method getMessage
     * 
     * @param index
     */
    public global.pluginStructure.Message getMessage(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _messageList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (global.pluginStructure.Message) _messageList.elementAt(index);
    } //-- global.pluginStructure.Message getMessage(int) 

    /**
     * Method getMessage
     */
    public global.pluginStructure.Message[] getMessage()
    {
        int size = _messageList.size();
        global.pluginStructure.Message[] mArray = new global.pluginStructure.Message[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (global.pluginStructure.Message) _messageList.elementAt(index);
        }
        return mArray;
    } //-- global.pluginStructure.Message[] getMessage() 

    /**
     * Method getMessageCount
     */
    public int getMessageCount()
    {
        return _messageList.size();
    } //-- int getMessageCount() 

    /**
     * Returns the value of field 'multipleObjects'.
     * 
     * @return the value of field 'multipleObjects'.
     */
    public global.pluginStructure.MultipleObjects getMultipleObjects()
    {
        return this._multipleObjects;
    } //-- global.pluginStructure.MultipleObjects getMultipleObjects() 

    /**
     * Method getName
     * 
     * @param index
     */
    public global.pluginStructure.Name getName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _nameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (global.pluginStructure.Name) _nameList.elementAt(index);
    } //-- global.pluginStructure.Name getName(int) 

    /**
     * Method getName
     */
    public global.pluginStructure.Name[] getName()
    {
        int size = _nameList.size();
        global.pluginStructure.Name[] mArray = new global.pluginStructure.Name[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (global.pluginStructure.Name) _nameList.elementAt(index);
        }
        return mArray;
    } //-- global.pluginStructure.Name[] getName() 

    /**
     * Method getNameCount
     */
    public int getNameCount()
    {
        return _nameList.size();
    } //-- int getNameCount() 

    /**
     * Method getStream
     * 
     * @param index
     */
    public global.pluginStructure.Stream getStream(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _streamList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (global.pluginStructure.Stream) _streamList.elementAt(index);
    } //-- global.pluginStructure.Stream getStream(int) 

    /**
     * Method getStream
     */
    public global.pluginStructure.Stream[] getStream()
    {
        int size = _streamList.size();
        global.pluginStructure.Stream[] mArray = new global.pluginStructure.Stream[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (global.pluginStructure.Stream) _streamList.elementAt(index);
        }
        return mArray;
    } //-- global.pluginStructure.Stream[] getStream() 

    /**
     * Method getStreamCount
     */
    public int getStreamCount()
    {
        return _streamList.size();
    } //-- int getStreamCount() 

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
     * Method removeAllConnection
     */
    public void removeAllConnection()
    {
        _connectionList.removeAllElements();
    } //-- void removeAllConnection() 

    /**
     * Method removeAllConnectionEnd
     */
    public void removeAllConnectionEnd()
    {
        _connectionEndList.removeAllElements();
    } //-- void removeAllConnectionEnd() 

    /**
     * Method removeAllMessage
     */
    public void removeAllMessage()
    {
        _messageList.removeAllElements();
    } //-- void removeAllMessage() 

    /**
     * Method removeAllName
     */
    public void removeAllName()
    {
        _nameList.removeAllElements();
    } //-- void removeAllName() 

    /**
     * Method removeAllStream
     */
    public void removeAllStream()
    {
        _streamList.removeAllElements();
    } //-- void removeAllStream() 

    /**
     * Method removeConnection
     * 
     * @param index
     */
    public global.pluginStructure.Connection removeConnection(int index)
    {
        java.lang.Object obj = _connectionList.elementAt(index);
        _connectionList.removeElementAt(index);
        return (global.pluginStructure.Connection) obj;
    } //-- global.pluginStructure.Connection removeConnection(int) 

    /**
     * Method removeConnectionEnd
     * 
     * @param index
     */
    public global.pluginStructure.ConnectionEnd removeConnectionEnd(int index)
    {
        java.lang.Object obj = _connectionEndList.elementAt(index);
        _connectionEndList.removeElementAt(index);
        return (global.pluginStructure.ConnectionEnd) obj;
    } //-- global.pluginStructure.ConnectionEnd removeConnectionEnd(int) 

    /**
     * Method removeMessage
     * 
     * @param index
     */
    public global.pluginStructure.Message removeMessage(int index)
    {
        java.lang.Object obj = _messageList.elementAt(index);
        _messageList.removeElementAt(index);
        return (global.pluginStructure.Message) obj;
    } //-- global.pluginStructure.Message removeMessage(int) 

    /**
     * Method removeName
     * 
     * @param index
     */
    public global.pluginStructure.Name removeName(int index)
    {
        java.lang.Object obj = _nameList.elementAt(index);
        _nameList.removeElementAt(index);
        return (global.pluginStructure.Name) obj;
    } //-- global.pluginStructure.Name removeName(int) 

    /**
     * Method removeStream
     * 
     * @param index
     */
    public global.pluginStructure.Stream removeStream(int index)
    {
        java.lang.Object obj = _streamList.elementAt(index);
        _streamList.removeElementAt(index);
        return (global.pluginStructure.Stream) obj;
    } //-- global.pluginStructure.Stream removeStream(int) 

    /**
     * Method setConnection
     * 
     * @param index
     * @param vConnection
     */
    public void setConnection(int index, global.pluginStructure.Connection vConnection)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _connectionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _connectionList.setElementAt(vConnection, index);
    } //-- void setConnection(int, global.pluginStructure.Connection) 

    /**
     * Method setConnection
     * 
     * @param connectionArray
     */
    public void setConnection(global.pluginStructure.Connection[] connectionArray)
    {
        //-- copy array
        _connectionList.removeAllElements();
        for (int i = 0; i < connectionArray.length; i++) {
            _connectionList.addElement(connectionArray[i]);
        }
    } //-- void setConnection(global.pluginStructure.Connection) 

    /**
     * Method setConnectionEnd
     * 
     * @param index
     * @param vConnectionEnd
     */
    public void setConnectionEnd(int index, global.pluginStructure.ConnectionEnd vConnectionEnd)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _connectionEndList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _connectionEndList.setElementAt(vConnectionEnd, index);
    } //-- void setConnectionEnd(int, global.pluginStructure.ConnectionEnd) 

    /**
     * Method setConnectionEnd
     * 
     * @param connectionEndArray
     */
    public void setConnectionEnd(global.pluginStructure.ConnectionEnd[] connectionEndArray)
    {
        //-- copy array
        _connectionEndList.removeAllElements();
        for (int i = 0; i < connectionEndArray.length; i++) {
            _connectionEndList.addElement(connectionEndArray[i]);
        }
    } //-- void setConnectionEnd(global.pluginStructure.ConnectionEnd) 

    /**
     * Method setMessage
     * 
     * @param index
     * @param vMessage
     */
    public void setMessage(int index, global.pluginStructure.Message vMessage)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _messageList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _messageList.setElementAt(vMessage, index);
    } //-- void setMessage(int, global.pluginStructure.Message) 

    /**
     * Method setMessage
     * 
     * @param messageArray
     */
    public void setMessage(global.pluginStructure.Message[] messageArray)
    {
        //-- copy array
        _messageList.removeAllElements();
        for (int i = 0; i < messageArray.length; i++) {
            _messageList.addElement(messageArray[i]);
        }
    } //-- void setMessage(global.pluginStructure.Message) 

    /**
     * Sets the value of field 'multipleObjects'.
     * 
     * @param multipleObjects the value of field 'multipleObjects'.
     */
    public void setMultipleObjects(global.pluginStructure.MultipleObjects multipleObjects)
    {
        this._multipleObjects = multipleObjects;
    } //-- void setMultipleObjects(global.pluginStructure.MultipleObjects) 

    /**
     * Method setName
     * 
     * @param index
     * @param vName
     */
    public void setName(int index, global.pluginStructure.Name vName)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _nameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _nameList.setElementAt(vName, index);
    } //-- void setName(int, global.pluginStructure.Name) 

    /**
     * Method setName
     * 
     * @param nameArray
     */
    public void setName(global.pluginStructure.Name[] nameArray)
    {
        //-- copy array
        _nameList.removeAllElements();
        for (int i = 0; i < nameArray.length; i++) {
            _nameList.addElement(nameArray[i]);
        }
    } //-- void setName(global.pluginStructure.Name) 

    /**
     * Method setStream
     * 
     * @param index
     * @param vStream
     */
    public void setStream(int index, global.pluginStructure.Stream vStream)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _streamList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _streamList.setElementAt(vStream, index);
    } //-- void setStream(int, global.pluginStructure.Stream) 

    /**
     * Method setStream
     * 
     * @param streamArray
     */
    public void setStream(global.pluginStructure.Stream[] streamArray)
    {
        //-- copy array
        _streamList.removeAllElements();
        for (int i = 0; i < streamArray.length; i++) {
            _streamList.addElement(streamArray[i]);
        }
    } //-- void setStream(global.pluginStructure.Stream) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (global.pluginStructure.AnalyzeType) Unmarshaller.unmarshal(global.pluginStructure.AnalyzeType.class, reader);
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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: BinaryPointerTypeTypeType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
 */

package global.pluginStructure.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class BinaryPointerTypeTypeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class BinaryPointerTypeTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The string type
     */
    public static final int STRING_TYPE = 0;

    /**
     * The instance of the string type
     */
    public static final BinaryPointerTypeTypeType STRING = new BinaryPointerTypeTypeType(STRING_TYPE, "string");

    /**
     * The integer type
     */
    public static final int INTEGER_TYPE = 1;

    /**
     * The instance of the integer type
     */
    public static final BinaryPointerTypeTypeType INTEGER = new BinaryPointerTypeTypeType(INTEGER_TYPE, "integer");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private BinaryPointerTypeTypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- global.pluginStructure.types.BinaryPointerTypeTypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of BinaryPointerTypeTypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * BinaryPointerTypeTypeType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("string", STRING);
        members.put("integer", INTEGER);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance. <br/>
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

    /**
     * Method toStringReturns the String representation of this
     * BinaryPointerTypeTypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new BinaryPointerTypeTypeType based
     * on the given String value.
     * 
     * @param string
     */
    public static global.pluginStructure.types.BinaryPointerTypeTypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid BinaryPointerTypeTypeType";
            throw new IllegalArgumentException(err);
        }
        return (BinaryPointerTypeTypeType) obj;
    } //-- global.pluginStructure.types.BinaryPointerTypeTypeType valueOf(java.lang.String) 

}

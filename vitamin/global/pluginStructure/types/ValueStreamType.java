/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ValueStreamType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
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
 * Class ValueStreamType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class ValueStreamType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The INCREASE type
     */
    public static final int INCREASE_TYPE = 0;

    /**
     * The instance of the INCREASE type
     */
    public static final ValueStreamType INCREASE = new ValueStreamType(INCREASE_TYPE, "INCREASE");

    /**
     * The DECREASE type
     */
    public static final int DECREASE_TYPE = 1;

    /**
     * The instance of the DECREASE type
     */
    public static final ValueStreamType DECREASE = new ValueStreamType(DECREASE_TYPE, "DECREASE");

    /**
     * The SAME type
     */
    public static final int SAME_TYPE = 2;

    /**
     * The instance of the SAME type
     */
    public static final ValueStreamType SAME = new ValueStreamType(SAME_TYPE, "SAME");

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

    private ValueStreamType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- global.pluginStructure.types.ValueStreamType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ValueStreamType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ValueStreamType
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
        members.put("INCREASE", INCREASE);
        members.put("DECREASE", DECREASE);
        members.put("SAME", SAME);
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
     * ValueStreamType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ValueStreamType based on the
     * given String value.
     * 
     * @param string
     */
    public static global.pluginStructure.types.ValueStreamType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ValueStreamType";
            throw new IllegalArgumentException(err);
        }
        return (ValueStreamType) obj;
    } //-- global.pluginStructure.types.ValueStreamType valueOf(java.lang.String) 

}

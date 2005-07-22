/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: SubnetTypeType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
 */

package master.topology.structure.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SubnetTypeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class SubnetTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The internet type
     */
    public static final int INTERNET_TYPE = 0;

    /**
     * The instance of the internet type
     */
    public static final SubnetTypeType INTERNET = new SubnetTypeType(INTERNET_TYPE, "internet");

    /**
     * The ethernet type
     */
    public static final int ETHERNET_TYPE = 1;

    /**
     * The instance of the ethernet type
     */
    public static final SubnetTypeType ETHERNET = new SubnetTypeType(ETHERNET_TYPE, "ethernet");

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

    private SubnetTypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- master.topology.structure.types.SubnetTypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of SubnetTypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this SubnetTypeType
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
        members.put("internet", INTERNET);
        members.put("ethernet", ETHERNET);
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
     * SubnetTypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new SubnetTypeType based on the
     * given String value.
     * 
     * @param string
     */
    public static master.topology.structure.types.SubnetTypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid SubnetTypeType";
            throw new IllegalArgumentException(err);
        }
        return (SubnetTypeType) obj;
    } //-- master.topology.structure.types.SubnetTypeType valueOf(java.lang.String) 

}

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: LocationType.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $
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
 * Class LocationType.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/07/22 14:13:12 $
 */
public class LocationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The udp type
     */
    public static final int UDP_TYPE = 0;

    /**
     * The instance of the udp type
     */
    public static final LocationType UDP = new LocationType(UDP_TYPE, "udp");

    /**
     * The tcp type
     */
    public static final int TCP_TYPE = 1;

    /**
     * The instance of the tcp type
     */
    public static final LocationType TCP = new LocationType(TCP_TYPE, "tcp");

    /**
     * The sctp type
     */
    public static final int SCTP_TYPE = 2;

    /**
     * The instance of the sctp type
     */
    public static final LocationType SCTP = new LocationType(SCTP_TYPE, "sctp");

    /**
     * The transport type
     */
    public static final int TRANSPORT_TYPE = 3;

    /**
     * The instance of the transport type
     */
    public static final LocationType TRANSPORT = new LocationType(TRANSPORT_TYPE, "transport");

    /**
     * The ipv4 type
     */
    public static final int IPV4_TYPE = 4;

    /**
     * The instance of the ipv4 type
     */
    public static final LocationType IPV4 = new LocationType(IPV4_TYPE, "ipv4");

    /**
     * The ipv6 type
     */
    public static final int IPV6_TYPE = 5;

    /**
     * The instance of the ipv6 type
     */
    public static final LocationType IPV6 = new LocationType(IPV6_TYPE, "ipv6");

    /**
     * The ip type
     */
    public static final int IP_TYPE = 6;

    /**
     * The instance of the ip type
     */
    public static final LocationType IP = new LocationType(IP_TYPE, "ip");

    /**
     * The data type
     */
    public static final int DATA_TYPE = 7;

    /**
     * The instance of the data type
     */
    public static final LocationType DATA = new LocationType(DATA_TYPE, "data");

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

    private LocationType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- global.pluginStructure.types.LocationType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of LocationType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this LocationType
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
        members.put("udp", UDP);
        members.put("tcp", TCP);
        members.put("sctp", SCTP);
        members.put("transport", TRANSPORT);
        members.put("ipv4", IPV4);
        members.put("ipv6", IPV6);
        members.put("ip", IP);
        members.put("data", DATA);
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
     * LocationType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new LocationType based on the given
     * String value.
     * 
     * @param string
     */
    public static global.pluginStructure.types.LocationType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LocationType";
            throw new IllegalArgumentException(err);
        }
        return (LocationType) obj;
    } //-- global.pluginStructure.types.LocationType valueOf(java.lang.String) 

}

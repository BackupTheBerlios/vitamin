// $Id: IPPacket.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles, Jonas Lehmann               *
 * IPv6 support by Martin Pelzer, 2004
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.net;

import net.sourceforge.jpcap.util.AnsiEscapeSequences;
import net.sourceforge.jpcap.util.ArrayHelper;
import net.sourceforge.jpcap.util.Timeval;
import java.io.Serializable;

/**
 * An IP protocol packet.
 * <p>
 * Extends an ethernet packet, adding IP header information and an IP 
 * data payload.
 * Supports IPv4 and IPv6. 
 *
 * @author Patrick Charles, Jonas Lehmann, Martin Pelzer
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public class IPPacket
	extends EthernetPacket
	implements IPFields, Serializable {

	private final int IPV4 = 4;
	private final int IPV6 = 6;
	
	private final int HOP_BY_HOP_OPTIONS_HEADER = 0;
	private final int ROUTING_HEADER = 43;
	private final int FRAGMENT_HEADER = 44;
	private final int AUTHENTICATION_HEADER = 51;
	private final int ENCAPSULATING_SECURITY_PAYLOAD_HEADER = 50;
	private final int DESTINATION_OPTIONS_HEADER = 60;


	// offset from beginning of byte array where IP header ends (i.e.,
	//  size of ethernet frame header and IP header
	protected int _ipOffset;

	int _oldIPHeaderLength = 0;
	/** create a new IP packet */
	public IPPacket(int lLen, byte[] bytes) {
		super(lLen, bytes);
		
		//* DEBUG */ System.out.println("IPPacket-Objekt wird angelegt.");		

		// check and set IP version
		_version = ArrayHelper.extractBitIndicatedInteger(_bytes, _ethOffset * 8 + IPv6_VERSION_POS, IPv6_VERSION_LEN);
		
		//* DEBUG */ System.out.println("IP_Version berechnet und gesetzt: " + _version);
		
		_versionSet = true;
		
		//* DEBUG */ System.out.println("IP_Versions-Flag gesetzt.");

		// fetch the actual header length from the incoming bytes
		if (_version == IPV4) {
			//* DEBUG */ System.out.println("Ist IPv4.");
			_ipHeaderLength = (ArrayHelper.extractInteger(_bytes, _ethOffset + IP_VER_POS, IP_VER_LEN) & 0xf) * 4;
		}
		else if (_version == IPV6) {
			//* DEBUG */ System.out.println("IPPacket: Ist IPv6.");
			_ipHeaderLength = IPv6_HEADER_LEN;
			
			/* 
			 * Next-Header-Feld solange auslesen, bis kein IPv6-Extension-Header Wert mehr drinsteht (0, 43, 44, 59, 60).
			 * Wenn ein weiterer Erweiterungsheader folgt, wird die Laenge des IP-Headers um den Wert im "Hdr Ext Len"-Feld (n * 64 Bit)
			 * bzw. um 32 (Fragment-Header) erweitert. Anschliessend wird das naechste "next header"-Feld gelesen (erste 8 Bit des
			 * Erweiterungsheaders).
			 */	
			
			int HOP_BY_HOP_HEADER = 0;
			int ROUTING_HEADER = 43;
			int FRAGMENT_HEADER = 44;
			int DESTINATION_OPTIONS_HEADER = 60;
			int UPPER_LAYER_HEADER = 59;
			
			int nextHeader = this.getNextHeader();
			//* DEBUG */ System.out.println("next Header: " + nextHeader + ", IPHeaderLength: " + _ipHeaderLength);
			 
			while (isIn(nextHeader, new int [] {HOP_BY_HOP_HEADER, ROUTING_HEADER, FRAGMENT_HEADER, DESTINATION_OPTIONS_HEADER, UPPER_LAYER_HEADER})) {						
				// IP-Header-Length verlaengern
				if (nextHeader == FRAGMENT_HEADER)
					// ein Fragment Header hat eine feste Laenge von 32 Bits
					_ipHeaderLength += 4;
				else {
					// alle anderen Header haben eine variable Laenge, die im zweiten Byte codiert ist (Laenge = n * 8 Byte)
					int hdr_ext_len = ArrayHelper.extractBitIndicatedInteger(_bytes, (_ethOffset*8) + (_ipHeaderLength * 8) + 8, 8);
					_oldIPHeaderLength = _ipHeaderLength;
					_ipHeaderLength += hdr_ext_len * 8 + 8; // hdr_ext_len * 64 Bits + 64 Bits, die immer da sind (hdr_ext_len = 0 bedeutet also bereits 8 Bytes) 
				}
			    	
				// naechsten Header auslesen
				nextHeader = this.getNextHeader();
				//* DEBUG */ System.out.println("next Header: " + nextHeader + ", IPHeaderLength: " + _ipHeaderLength);
			}
			
			//* DEBUG */ System.out.println("Ende IPv6-Teil im Konstruktor IPPacket");			
		}
		else {
			//* DEBUG */ System.out.println("Kurioserweise ist die IP-Version " + _version + ". Wie tragisch.");
		}
		
		// set offset into _bytes of previous layers
		_ipOffset = _ethOffset + _ipHeaderLength;
		
		//* DEBUG */ System.out.println("IPPacket-Konstruktor Ende.");
	}
	
	
	/**
  	 * Create a new IP packet.
   	*/
  	public IPPacket(int lLen, byte [] bytes, Timeval tv) {
    	   this(lLen, bytes);
    	   this._timeval = tv;
  	}
	

	private int _version;
	private boolean _versionSet = false; // have to use a boolean, int!=Object
	/** get the IP version code. i.e. 0x4 is IPv4 */
	public int getVersion() {
		//* DEBUG */ System.out.println("getVersion");
		// it can't be that _version isn't set because it is set in the constructor
		return _version;
	}


	// set in constructor
	private int _ipHeaderLength = -1;
	/** fetch the IP header length in bytes */
	public int getIPHeaderLength() {
		//* DEBUG */ System.out.println("gross P.");		
		if (_ipHeaderLength == -1) {
			//* DEBUG */ System.out.println("_ipHeaderLength noch nicht gesetzt.");			
			
			// fetch the actual header length from the incoming bytes
			if (_version == IPV4) {
				_ipHeaderLength = (ArrayHelper.extractInteger(_bytes, _ethOffset + IP_VER_POS, IP_VER_LEN) & 0xf) * 4;
			}
			else if (_version == IPV6) {
				//_ipHeaderLength = IPv6_HEADER_LEN; // TODO Erweiterungsheader
				// dieser Fall kann nicht eintreten, da bei IPv6 die Headerlaenge bereits im Konstruktor berechnet wird
				System.out.println("FATAL ERROR: getHeaderLength IPv6 can't be! Read source code for more information.");
			}
			else {
				//* DEBUG */ System.out.println("Kurioserweise ist die IP-Version " + _version + ". Wie tragisch.");			
			}
		}
		return _ipHeaderLength;
	}
	

	/** fetch the IP header length in bytes */
	public int getIpHeaderLength() {
		//* DEBUG */ System.out.println("Hossa! Hossa! Hasso!");		
		// this is the old method call, but everything else uses all caps for
		// TCP, so in the interest of consistency...
		return getIPHeaderLength();
	}


	/** fetch the packet IP header length */
	public int getHeaderLength() {
		//* DEBUG */ System.out.println("getHeaderLength");
		return getIPHeaderLength();
	}


	private int _typeOfService;
	private boolean _typeOfServiceSet = false;
	/** fetch the type of service. @see TypesOfService */
	public int getTypeOfService() {
		//* DEBUG */ System.out.println("getTypeOfService");
		if (!_typeOfServiceSet) {
			if (_version == IPV4) {			
				_typeOfService = ArrayHelper.extractInteger(_bytes, _ethOffset + IP_TOS_POS, IP_TOS_LEN);
				_typeOfServiceSet = true;
			}
			else if (_version == IPV6) {
				// in IPv6 "type of service" is "traffic class". But it's the same field....
				_typeOfService = this.getTrafficClass();
				_typeOfServiceSet = true;
			}
		}
		return _typeOfService;
	}
	
	
	private int _trafficClass;
	private boolean _trafficClassSet = false;
	/** fetch the traffic class. */
	public int getTrafficClass() {
		//* DEBUG */ System.out.println("getTrafficClass");
		if (!_trafficClassSet) {
			if (_version == IPV4) {
				_trafficClass = this.getTypeOfService();
				_trafficClassSet = true;
			}
			else if (_version == IPV6) {
				_trafficClass = ArrayHelper.extractBitIndicatedInteger(_bytes, _ethOffset * 8 + IPv6_TRAFFIC_CLASS_POS, IPv6_TRAFFIC_CLASS_LEN);
				_trafficClassSet = true;
			}
		}
		return _trafficClass;
	}


	private int _length;
	private boolean _lengthSet = false;
	/** fetch the IP length in bytes */
	/* darf keine WrongIPVersionException schmeissen, da in TCPPacket etc. verwendet.
	 * Muss also fuer IPv4 den Inhalt des Length-Feldes und fuer IPv6 20 zurueckliefern
	 */
	public int getLength() {
		//* DEBUG */ System.out.println("getLength");
		if (!_lengthSet) {
			if (_version == IPV4) {
				_length = ArrayHelper.extractInteger(_bytes, _ethOffset + IP_LEN_POS, IP_LEN_LEN);
				_lengthSet = true;
			}
			else if (_version == IPV6) {
				// the "total length" field does exist in IPv6 ("payload length" field) but it has another meaning (length incl. header)
				_length = ArrayHelper.extractBitIndicatedInteger(_bytes, _ethOffset * 8 + IPv6_PAYLOAD_LENGTH_POS, IPv6_PAYLOAD_LENGTH_LEN) + IPFields.IPv6_HEADER_LEN;
				_lengthSet = true;				
			}
		}
		return _length;
	}
	
	
	private int _payloadLength;
	private boolean _payloadLengthSet = false;
	/** fetch the payload length in bytes */
	public int getPayloadLength() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getPayloadLength");
		if (!_payloadLengthSet) {
			if (_version == IPV4) {
				throw new WrongIPVersionException();
			}
			else if (_version == IPV6) {
				_payloadLength = ArrayHelper.extractBitIndicatedInteger(_bytes, _ethOffset * 8 + IPv6_PAYLOAD_LENGTH_POS, IPv6_PAYLOAD_LENGTH_LEN);
				_payloadLengthSet = true;
			}
		}
		return _payloadLength;
	}


	private int _id;
	private boolean _idSet = false;
	/**
	 * fetch the unique ID of this IP datagram. The ID normally 
	 * increments by one each time a datagram is sent by a host.
	 */
	public int getId() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getId");
		if (!_idSet) {
			if (_version == IPV4) {
				_id = ArrayHelper.extractInteger(_bytes, _ethOffset + IP_ID_POS, IP_ID_LEN);
				_idSet = true;
			}
			else if (_version == IPV6) {
				throw new WrongIPVersionException();
			}
		}
		return _id;
	}


	private int _fragmentFlags;
	private boolean _fragmentFlagsSet = false;
	/** fetch fragmentation flags */
	public int getFragmentFlags() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getFragmentFlags");
		if (!_fragmentFlagsSet) {
			if (_version == IPV4) {
				// fragment flags are the high 3 bits
				int huh = ArrayHelper.extractInteger(_bytes, _ethOffset + IP_FRAG_POS, IP_FRAG_LEN);
				_fragmentFlags = (ArrayHelper.extractInteger(_bytes, _ethOffset + IP_FRAG_POS,  IP_FRAG_LEN) >> 13) & 0x7;
				_fragmentFlagsSet = true;
			}
			else if (_version == IPV6) {
				throw new WrongIPVersionException();
			}
		}
		return _fragmentFlags;
	}


	private int _fragmentOffset;
	private boolean _fragmentOffsetSet = false;
	/** fetch fragmentation offset */
	public int getFragmentOffset() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getFragmentOffset");
		if (!_fragmentOffsetSet) {
			if (_version == IPV4) {
				// offset is the low 13 bits
				_fragmentOffset = ArrayHelper.extractInteger(_bytes, _ethOffset + IP_FRAG_POS, IP_FRAG_LEN) & 0x1fff;
				_fragmentOffsetSet = true;
			}
			else if (_version == IPV6) {
				throw new WrongIPVersionException();
			}			
		}
		return _fragmentOffset;
	}


	private int _timeToLive;
	private boolean _timeToLiveSet = false;
	/**
	 * fetch the time to live. TTL sets the upper limit on the number of 
	 *   routers through which this IP datagram is allowed to pass.
	 */
	public int getTimeToLive() {
		//* DEBUG */ System.out.println("getTimeToLive");
		if (!_timeToLiveSet) {
			if (_version == IPV4) {
				_timeToLive = ArrayHelper.extractInteger(_bytes, _ethOffset + IP_TTL_POS, IP_TTL_LEN);
				_timeToLiveSet = true;
			}
			else if (_version == IPV6) {
				_timeToLive = this.getHopLimit();
				_timeToLiveSet = true;
			}
		}
		return _timeToLive;
	}
		
	private int _hopLimit;
	private boolean _hopLimitSet = false;
	/**
	 * fetch the hop limit.
	 */
	public int getHopLimit() {
		//* DEBUG */ System.out.println("getHopLimit");
		if (!_hopLimitSet) {
			if (_version == IPV4) {
				_hopLimit = this.getTimeToLive();
				_hopLimitSet = true;
			}
			else if (_version == IPV6) {
				_hopLimit = ArrayHelper.extractBitIndicatedInteger(_bytes, _ethOffset *8 + IPv6_HOP_LIMIT_POS, IPv6_HOP_LIMIT_LEN);
				_hopLimitSet = true;
			}
		}
		return _hopLimit;
	}


	private int _ipProtocol;
	private boolean _ipProtocolSet = false;
	/**
	 * fetch the code indicating the type of protocol embedded in the IP
	 * datagram. @see IPProtocols.
	 */
	public int getIPProtocol() {
		//* DEBUG */ System.out.println("getIPProtocol");
		if (!_ipProtocolSet) {
			if (_version == IPV4) {
				_ipProtocol = ArrayHelper.extractInteger(_bytes, _ethOffset + IP_CODE_POS, IP_CODE_LEN);
				_ipProtocolSet = true;
			}
			else if (_version == IPV6) {
				_ipProtocol = this.getNextHeader();
				_ipProtocolSet = true;
			}
		}
		return _ipProtocol;
	}
	
	private int _nextHeader;
	private boolean _nextHeaderSet = false;
	/**
	 * fetch the code indicating the type of protocol embedded in the IP
	 * datagram. @see IPProtocols.
	 */
	public int getNextHeader() {
		// Diese Methode wird bei IPv6 vom Konstruktor so oft aufgerufen, bis alle Erweiterungsheader abgearbeitet sind.
		// Dadurch liefert die Methode dem User gegenueber immer den Wert des letzten nextHeader-Feldes, also die Nummer
		// des Protokolls der naechsten Schicht (bsp. TCP, UDP). 
		
		//* DEBUG */ System.out.println("getNextHeader");
			if (_version == IPV4) {
				if (!_nextHeaderSet) {
					_nextHeader = this.getIPProtocol();
					_nextHeaderSet = true;
				}
			}
			else if (_version == IPV6) {
				if (_ipHeaderLength == IPv6_HEADER_LEN) {
					// das nextHeader-Feld im eigentlich IP-Header wird ausgelesen
					//* DEBUG */ System.out.println("IPPacket.getNextHeader liefert nextHeader-Feld des eigentlichen IP-Headers.");					
					_nextHeader = ArrayHelper.extractBitIndicatedInteger(_bytes, _ethOffset *8 + IPv6_NEXT_HEADER_POS, IPv6_NEXT_HEADER_LEN);
					_nextHeaderSet = true;
				}
				else {
					// nextHeader-Feld in einem Erweiterungsheader auslesen (ist immer das erste Byte im Erweiterungsheader)
					//* DEBUG */ System.out.println("IPPacket.getNextHeader liefert nextHeader-Feld eines Erweiterungsheaders.");
					_nextHeader = ArrayHelper.extractBitIndicatedInteger(_bytes, (_ethOffset*8) + (_oldIPHeaderLength*8), IPv6_NEXT_HEADER_LEN);
					
					// _nextHeaderSet muss bereits vorher auf true gesetzt worden sein --> muss also nicht mehr
				}
			}
		return _nextHeader;
	}
	
	
	// wird von Vitamin benoetigt
	public int getNextHeaderPos() {
		if (_version == 6){
			if (_ipHeaderLength == IPv6_HEADER_LEN)
				return IPv6_NEXT_HEADER_POS;
			else
				// wenn Erweiterungsheader existieren, dann immer am Anfang des letzten Erweiterungsheaders
				return _oldIPHeaderLength;
		}
		
		return -1;
	}
	
	
	private int _flowLabel;
	private boolean _flowLabelSet = false;
	/**
	 * fetch the code indicating the type of protocol embedded in the IP
	 * datagram. @see IPProtocols.
	 */
	public int getFlowLabel() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getFlowLabel");
		if (!_flowLabelSet) {
			if (_version == IPV4) {
				throw new WrongIPVersionException();
			}
			else if (_version == IPV6) {
				_flowLabel = ArrayHelper.extractBitIndicatedInteger(_bytes, _ethOffset *8 + IPv6_FLOW_LABEL_POS, IPv6_FLOW_LABEL_LEN);
				_flowLabelSet = true;
			}
		}
		return _flowLabel;
	}


	/**
	 * fetch the code indicating the type of protocol embedded in the IP
	 * datagram. @see IPProtocols.
	 */
	public int getProtocol() {
		//* DEBUG */ System.out.println("getProtocol");
		int protocol = 0;
		
		if (_version == IPV4)
			protocol = getIPProtocol();
		else if (_version == IPV6)
			protocol = getNextHeader();
			
		return protocol;
	}


	private int _ipChecksum;
	private boolean _ipChecksumSet = false;
	/** fetch the header checksum */
	public int getIPChecksum() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getIPChecksum");
		if (!_ipChecksumSet) {
			if (_version == IPV4) {
				_ipChecksum = ArrayHelper.extractInteger(_bytes, _ethOffset + IP_CSUM_POS, IP_CSUM_LEN);
				_ipChecksumSet = true;
			}
			else if (_version == IPV6) {
				throw new WrongIPVersionException();
			}
		}
		return _ipChecksum;
	}


	/** fetch the header checksum */
	public int getChecksum() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getChecksum");
		return getIPChecksum();
	}


	private String _sourceAddress = null;
	/** fetch the IP address of the host where the packet originated from */
	public String getSourceAddress() {
		//* DEBUG */ System.out.println("getSourceAddress");
		if (_sourceAddress == null) {
			if (_version == IPV4) {
				_sourceAddress = IPAddress.extract(_ethOffset + IP_SRC_POS, _bytes);
			}
			else if (_version == IPV6) {
				_sourceAddress = IPAddress.extractV6(_ethOffset * 8 + IPv6_SOURCE_ADDRESS_POS, _bytes);
			}
		}
		return _sourceAddress;
	}


	private byte[] _sourceAddressBytes = null;
	/** fetch the source address as a byte array */
	public byte[] getSourceAddressBytes() {
		//* DEBUG */ System.out.println("getSourceAddressBytes");
		if (_sourceAddressBytes == null) {
			if (_version == IPV4) {
				_sourceAddressBytes = new byte[4];
				System.arraycopy(_bytes, _ethOffset + IP_SRC_POS, _sourceAddressBytes, 0, 4);
			}
			else if (_version == IPV6) {
				_sourceAddressBytes = new byte [16];
				System.arraycopy(_bytes, _ethOffset + IPv6_SOURCE_ADDRESS_POS / 8, _sourceAddressBytes, 0, IPv6_SOURCE_ADDRESS_LEN / 8);
			}
		}
		return _sourceAddressBytes;
	}


	private long _sourceAddressAsLong;
	private boolean _sourceAddressAsLongSet = false;
	/** fetch the source address as a long */
	public long getSourceAddressAsLong() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getSourceAddressAsLong");
		if (!_sourceAddressAsLongSet) {
			if (_version == IPV4) {
				_sourceAddressAsLong = ArrayHelper.extractLong(_bytes, _ethOffset + IP_SRC_POS, 4);
				_sourceAddressAsLongSet = true;
			}
			else if (_version == IPV6) {
				// that can't work because "long" is only 8 bytes long
				throw new WrongIPVersionException();
			}
		}
		return _sourceAddressAsLong;
	}


	private String _destinationAddress = null;
	/** fetch the IP address of the host where the packet is destined */
	public String getDestinationAddress() {
		//* DEBUG */ System.out.println("getDestinationAddress");
		if (_destinationAddress == null) {
			if (_version == IPV4) {
				_destinationAddress = IPAddress.extract(_ethOffset + IP_DST_POS, _bytes);
			}
			else if (_version == IPV6) {
				_destinationAddress = IPAddress.extractV6(_ethOffset * 8 + IPv6_DESTINATION_ADDRESS_POS, _bytes);
			}
		}
		return _destinationAddress;
	}


	private byte[] _destinationAddressBytes = null;
	/** fetch the destination address as a byte array */
	public byte[] getDestinationAddressBytes() {
		//* DEBUG */ System.out.println("getDestinationAddressBytes");
		if (_destinationAddressBytes == null) {
			if (_version == IPV4) {
				_destinationAddressBytes = new byte[4];
				System.arraycopy(_bytes, _ethOffset + IP_DST_POS, _destinationAddressBytes, 0, 4);
			}
			else if (_version == IPV6) {
				_destinationAddressBytes = new byte[16];
				System.arraycopy(_bytes, _ethOffset + IPv6_DESTINATION_ADDRESS_POS / 8, _destinationAddressBytes, 0, IPv6_DESTINATION_ADDRESS_LEN / 8);
			}
		}
		return _destinationAddressBytes;
	}


	private long _destinationAddressAsLong;
	private boolean _destinationAddressAsLongSet = false;
	/** fetch the destination address as a long */
	public long getDestinationAddressAsLong() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("getDestinationAddressAsLong");
		if (!_destinationAddressAsLongSet) {
			if (_version == IPV4) {
				_destinationAddressAsLong = ArrayHelper.extractLong(_bytes, _ethOffset + IP_DST_POS, 4);
				_destinationAddressAsLongSet = true;
			}
			else if (_version == IPV6) {
				// that can't work because "long" is only 8 bytes long
				throw new WrongIPVersionException();
			}
		}
		return _destinationAddressAsLong;
	}


	private byte[] _ipHeaderBytes = null;
	/** fetch the IP header as byte array (if IPv6, incl all extension headers) */
	public byte[] getIPHeader() {
		//* DEBUG */ System.out.println("getIPHeader");
		if (_ipHeaderBytes == null) {
			// same for IPv4 and IPv6 because getIPHeaderLength() returns the right length
			_ipHeaderBytes = PacketEncoding.extractHeader(_ethOffset, getIPHeaderLength(), _bytes);	
		}
		return _ipHeaderBytes;
	}


	/** fetch the IP header as a byte array */
	public byte[] getHeader() {
		//* DEBUG */ System.out.println("getHeader");
		return getIPHeader();
	}


	private byte[] _ipDataBytes = null;
	/** fetch the IP data as a byte array */
	public byte[] getIPData() {
		//* DEBUG */ System.out.println("getIPData");
		if (_ipDataBytes == null) {
			// set data length based on info in headers (note: tcpdump
			//  can return extra junk bytes which bubble up to here
			int tmpLen = 0;
			if (_version == IPV4) {								
				tmpLen = getLength() - getIPHeaderLength();			
			}
			else if (_version == IPV6) {
				try {
					tmpLen = this.getPayloadLength();
				} catch (WrongIPVersionException e) {
					// can't happen because getLength() will not throw this exception when _version == IPV6
					e.printStackTrace();
				}
				
				// tmpLen ist jetzt so lang, wie das gesamte IPv6-Paket ohne den Standard-IPv6-Header.
				// Es muss aber auch noch die Laenge alle Erweiterungsheader abgezogen werden
				tmpLen = tmpLen +IPv6_HEADER_LEN - _ipHeaderLength;
				//* DEBUG */ System.out.println("IPPacket.getIPData: Laenge des Payloads = " + tmpLen);
			} 
			
			_ipDataBytes = PacketEncoding.extractData(_ethOffset, getIPHeaderLength(), _bytes, tmpLen);
		}
		return _ipDataBytes;
	}


	/** fetch the IP data as a byte array */
	public byte[] getData() {
		//* DEBUG */ System.out.println("getData");
		return getIPData();
	}


	private boolean _isValidChecksum;
	private boolean _isValidChecksumSet = false;
	/** is the IP packet valid, checksum-wise */
	public boolean isValidChecksum() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("isValidChecksum");
		if (!_isValidChecksumSet) {
			if (_version == IPV4) {
				// first validate other information about the packet. if this stuff
				// is not true, the packet (and therefore the checksum) is invalid
				// - ip_hl >= 5 (ip_hl is the length in 4-byte words)
				if (getIPHeaderLength() < IP_HEADER_LEN) {
					_isValidChecksum = false;
				} else {
					_isValidChecksum = (computeReceiverIPChecksum() == 0xffff);
				}
				_isValidChecksumSet = true;
			}
			else if (_version == IPV6) {
				throw new WrongIPVersionException();
			}
		}
		return _isValidChecksum;
	}


	/** is the IP packet valid, checksum-wise */
	public boolean isValidIPChecksum() throws WrongIPVersionException {
		//* DEBUG */ System.out.println("isValidIPChecksum");
		return isValidChecksum();
	}


	// what do these methods below this line do?
	// -------------------------------------------------------------------------------------------
	
	protected int computeReceiverIPChecksum() {
		//* DEBUG */ System.out.println("computeReceiverIPChecksum");
		return computeReceiverChecksum(_ethOffset, getIPHeaderLength());
	}


	protected int computeReceiverChecksum(int start, int len) {
		//* DEBUG */ System.out.println("computeReceiverChecksum");
		// checksum should come out to -1 if checksum is correct
		return onesCompSum(_bytes, start, len);
	}


	protected int computeSenderIPChecksum() {
		//* DEBUG */ System.out.println("getSenderIPChecksum");
		return computeSenderChecksum(_ethOffset, getIPHeaderLength(), 10);
	}


	protected int computeSenderChecksum(int start, int len, int csumPos) {
		//* DEBUG */ System.out.println("computeSenderChecksum");
		// quick bad-data check
		if (csumPos >= len)
			return 0; // bad data, header too short
		// copy bytes, zero out checksum
		byte[] sbytes = new byte[len];
		System.arraycopy(_bytes, start, sbytes, 0, len);
		// zero out any current checksum
		sbytes[csumPos] = sbytes[csumPos + 1] = 0;
		// checksum should come out to -1 if checksum is correct
		return onesCompSum(sbytes, 0, len);
	}


	protected int onesCompSum(byte[] bytes, int start, int len) {
		//* DEBUG */ System.out.println("onesCompSun");
		int sum = 0;
		// basically, IP checksums are done by taking the 16 bit ones-
		// complement sum of the IP header. This means summing two bytes
		// at a time. no error checking is done (e.g. bounds checking)
		int i;
		for (i = 0; i < len; i += 2) {
			// put bytes in ints so we can forget about sign-extension
			int i1 = bytes[start + i] & 0xff;
			// zero-pad, maybe
			int i2 = (start + i + 1 < len ? bytes[start + i + 1] & 0xff : 0);
			sum += ((i1 << 8) + i2);
			while ((sum & 0xffff) != sum) {
				sum &= 0xffff;
				sum += 1;
			}
		}
		return sum;
	}


	/**
	 * Convert this IP packet to a readable string.
	 */
	public String toString() {
		//* DEBUG */ System.out.println("toString");
		return toColoredString(false);
	}


	// TODO I don't have the time to do these methods. later............

	/**
	 * Generate string with contents describing this IP packet.
	 * @param colored whether or not the string should contain ansi
	 * color escape sequences.
	 */
	public String toColoredString(boolean colored) {
		//* DEBUG */ System.out.println("toColoredString");
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		if (colored)
			buffer.append(getColor());
		buffer.append("IPPacket");
		if (colored)
			buffer.append(AnsiEscapeSequences.RESET);
		buffer.append(": ");
		buffer.append(getSourceAddress() + " -> " + getDestinationAddress());
		buffer.append(" proto=" + getProtocol());
		//buffer.append(" l=" + getIPHeaderLength() + "," + getLength());
		buffer.append("]");

		return buffer.toString();
	}


	/**
	 * Convert this IP packet to a more verbose string.
	 */
	public String toColoredVerboseString(boolean colored) {
		//* DEBUG */ System.out.println("toColoredVerboseString");
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		if (colored)
			buffer.append(getColor());
		buffer.append("IPPacket");
		if (colored)
			buffer.append(AnsiEscapeSequences.RESET);
		buffer.append(": ");
		buffer.append("version=" + getVersion() + ", ");
		buffer.append("hlen=" + getHeaderLength() + ", ");
		buffer.append("tos=" + getTypeOfService() + ", ");
		//buffer.append("length=" + getLength() + ", ");
		//buffer.append("id=" + getId() + ", ");
		//buffer.append(
		//	"flags=0x" + Integer.toHexString(getFragmentFlags()) + ", ");
		//buffer.append("offset=" + getFragmentOffset() + ", ");
		buffer.append("ttl=" + getTimeToLive() + ", ");
		buffer.append("proto=" + getProtocol() + ", ");
		//buffer.append("sum=0x" + Integer.toHexString(getChecksum()) + ", ");
		buffer.append("src=" + getSourceAddress() + ", ");
		buffer.append("dest=" + getDestinationAddress());
		buffer.append("]");

		return buffer.toString();
	}


	/**
	 * Fetch ascii escape sequence of the color associated with this packet type.
	 */
	public String getColor() {
		//* DEBUG */ System.out.println("getColor");
		return AnsiEscapeSequences.WHITE;
	}


	/** inner class provides access to private methods for unit testing */
	/*public class TestProbe {
		public int getComputedReceiverIPChecksum() {
			return IPPacket.this.computeReceiverIPChecksum();
		}
		public int getComputedSenderIPChecksum() {
			return IPPacket.this.computeSenderIPChecksum();
		}
	}*/


	private String _rcsid =
		"$Id: IPPacket.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $";
		
		
	// checks, if an array contains a value
	private boolean isIn(int x, int [] array) {
		for (int i = 0; i < array.length; i++)
			if (array[i] == x)
				return true;
		return false;
	}
	
	
	/** 
   	 * This inner class provides access to private methods for unit testing.
   	 */
  	 public class TestProbe {
    	 public int getComputedReceiverIPChecksum() {
      	 	return IPPacket.this.computeReceiverIPChecksum();
    	 }
    	 public int getComputedSenderIPChecksum() {
      	 	return IPPacket.this.computeSenderIPChecksum();
    	 }
  }
	
}

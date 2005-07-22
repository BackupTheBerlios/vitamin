// $Id: IPFields.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.net;


/**
 * IP protocol field encoding information.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public interface IPFields
{

  // ----------------- IPv4 fields--------------------

  // field lengths

  /**
   * Width of the IP version and header length field in bytes.
   */
  int IP_VER_LEN = 1;

  /**
   * Width of the TOS field in bytes.
   */
  int IP_TOS_LEN = 1;

  /**
   * Width of the header length field in bytes.
   */
  int IP_LEN_LEN = 2;

  /**
   * Width of the ID field in bytes.
   */
  int IP_ID_LEN = 2;

  /**
   * Width of the fragmentation bits and offset field in bytes.
   */
  int IP_FRAG_LEN = 2;

  /**
   * Width of the TTL field in bytes.
   */
  int IP_TTL_LEN = 1;

  /**
   * Width of the IP protocol code in bytes.
   */
  int IP_CODE_LEN = 1;

  /**
   * Width of the IP checksum in bytes.
   */
  int IP_CSUM_LEN = 2;


  // field positions

  /**
   * Position of the version code and header length within the IP header.
   */
  int IP_VER_POS = 0;

  /**
   * Position of the type of service code within the IP header.
   */
  int IP_TOS_POS = IP_VER_POS + IP_VER_LEN;

  /**
   * Position of the length within the IP header.
   */
  int IP_LEN_POS = IP_TOS_POS + IP_TOS_LEN;

  /**
   * Position of the packet ID within the IP header.
   */
  int IP_ID_POS = IP_LEN_POS + IP_LEN_LEN;

  /**
   * Position of the flag bits and fragment offset within the IP header.
   */
  int IP_FRAG_POS = IP_ID_POS + IP_ID_LEN;

  /**
   * Position of the ttl within the IP header.
   */
  int IP_TTL_POS = IP_FRAG_POS + IP_FRAG_LEN;

  /**
   * Position of the IP protocol code within the IP header.
   */
  int IP_CODE_POS = IP_TTL_POS + IP_TTL_LEN;

  /**
   * Position of the checksum within the IP header.
   */
  int IP_CSUM_POS = IP_CODE_POS + IP_CODE_LEN;

  /**
   * Position of the source IP address within the IP header.
   */
  int IP_SRC_POS = IP_CSUM_POS + IP_CSUM_LEN;

  /**
   * Position of the destination IP address within a packet.
   */
  int IP_DST_POS = IP_SRC_POS + IPAddress.WIDTH;

//----------------- IPv6 fields--------------------
		
	// -----------> !!! all vars are given in bits, not in bytes !!! <------------

	/**
	 * Width of the IPv6 version field in bits.
	 */
	int IPv6_VERSION_LEN = 4;

	/**
	 * Width of the IPv6 traffic class field in bits.
	 */
	int IPv6_TRAFFIC_CLASS_LEN = 8;

	/**
	 * Width of the IPv6 flow label field in bits.
	 */
	int IPv6_FLOW_LABEL_LEN = 20;

	/**
	 * Width of the IPv6 payload length field in bits.
	 */
	int IPv6_PAYLOAD_LENGTH_LEN = 16;

	/**
	 * Width of the IPv6 next header field in bits.
	 */
	int IPv6_NEXT_HEADER_LEN = 8;

	/**
	 * Width of the IPv6 hop limit field in bits.
	 */
	int IPv6_HOP_LIMIT_LEN = 8;

	/**
	 * Width of the IPv6 source address field in bits.
	 */
	int IPv6_SOURCE_ADDRESS_LEN = 128;

	/**
	 * Width of the IPv6 destination address field in bits.
	 */
	int IPv6_DESTINATION_ADDRESS_LEN = 128;

	// field positions

	/**
	 * Position of the version code within the IPv6 header.
	 */
	int IPv6_VERSION_POS = 0;

	/**
	 * Position of the traffic class field within the IPv6 header.
	 */
	int IPv6_TRAFFIC_CLASS_POS = IPv6_VERSION_LEN; // 4

	/**
	 * Position of the flow label field within the IPv6 header.
	 */
	int IPv6_FLOW_LABEL_POS = IPv6_TRAFFIC_CLASS_POS + IPv6_TRAFFIC_CLASS_LEN; // 12

	/**
	 * Position of the payload length field within the IPv6 header.
	 */
	int IPv6_PAYLOAD_LENGTH_POS = IPv6_FLOW_LABEL_POS + IPv6_FLOW_LABEL_LEN; // 32

	/**
	 * Position of the next header field within the IPv6 header.
	 */
	int IPv6_NEXT_HEADER_POS =
		IPv6_PAYLOAD_LENGTH_POS + IPv6_PAYLOAD_LENGTH_LEN; // 48

	/**
	 * Position of the hop limit field within the IPv6 header.
	 */
	int IPv6_HOP_LIMIT_POS = IPv6_NEXT_HEADER_POS + IPv6_NEXT_HEADER_LEN; // 56

	/**
	 * Position of the source address field within the IPv6 header.
	 */
	int IPv6_SOURCE_ADDRESS_POS = IPv6_HOP_LIMIT_POS + IPv6_HOP_LIMIT_LEN; // 64

	/**
	 * Position of the destination address field within the IPv6 header.
	 */
	int IPv6_DESTINATION_ADDRESS_POS =
		IPv6_SOURCE_ADDRESS_POS + IPv6_SOURCE_ADDRESS_LEN; // 192


  // complete header length 

  /**
   * Length in bytes of an IP header, excluding options.
   */
  int IP_HEADER_LEN = IP_DST_POS + IPAddress.WIDTH; // == 20

	/**
     * Length in bytes of an IPv6 header.
     */
	int IPv6_HEADER_LEN = (IPv6_DESTINATION_ADDRESS_POS + IPv6_DESTINATION_ADDRESS_LEN) / 8;  // == 40 
}

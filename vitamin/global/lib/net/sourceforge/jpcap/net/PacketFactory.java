// $Id: PacketFactory.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.net;

import net.sourceforge.jpcap.util.ArrayHelper;
import net.sourceforge.jpcap.util.Timeval;


/**
 * This factory constructs high-level packet objects from
 * captured data streams.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public class PacketFactory
{
  /**
   * Convert captured packet data into an object.
   */
  public static Packet dataToPacket(int linkType, byte [] bytes) {
    int ethProtocol;

    // record the length of the headers associated with this link layer type.
    // this length is the offset to the header embedded in the packet.
    lLen = LinkLayer.getLinkLayerLength(linkType);

    // extract the protocol code for the type of header embedded in the 
    // link-layer of the packet
    int offset = LinkLayer.getProtoOffset(linkType);
    if(offset == -1)
      // if there is no embedded protocol, assume IP?
      ethProtocol = EthernetProtocols.IP;
    else
      ethProtocol = ArrayHelper.extractInteger(bytes, offset,
                                               EthernetFields.ETH_CODE_LEN);

    // try to recognize the ethernet type..
    switch(ethProtocol) {
      // arp
    case EthernetProtocols.ARP: 
      return new ARPPacket(lLen, bytes);
    case EthernetProtocols.IP:
	case EthernetProtocols.IPV6:
      // ethernet level code is recognized as IP, figure out what kind..
      int ipProtocol = IPProtocol.extractProtocol(lLen, bytes, ethProtocol);
      switch(ipProtocol) {
        // icmp
      case IPProtocols.ICMP: return new ICMPPacket(lLen, bytes);
        // igmp
      case IPProtocols.IGMP: return new IGMPPacket(lLen, bytes);
        // tcp
      case IPProtocols.TCP: return new TCPPacket(lLen, bytes);
        // udp
      case IPProtocols.UDP: return new UDPPacket(lLen, bytes);
        // unidentified ip..
      default: return new IPPacket(lLen, bytes);
      }
      // ethernet level code not recognized, default to anonymous packet..
    default: return new EthernetPacket(lLen, bytes);
    }
  }

  /**
   * Convert captured packet data into an object.
   */
  public static Packet dataToPacket(int linkType, byte [] bytes, Timeval tv) {
    int ethProtocol;

    // record the length of the headers associated with this link layer type.
    // this length is the offset to the header embedded in the packet.
    lLen = LinkLayer.getLinkLayerLength(linkType);

    // extract the protocol code for the type of header embedded in the 
    // link-layer of the packet
    int offset = LinkLayer.getProtoOffset(linkType);
    if(offset == -1)
      // if there is no embedded protocol, assume IP?
      ethProtocol = EthernetProtocols.IP;
    else
      ethProtocol = ArrayHelper.extractInteger(bytes, offset,
                                               EthernetFields.ETH_CODE_LEN);

    // try to recognize the ethernet type..
    switch(ethProtocol) {
      // arp
    case EthernetProtocols.ARP: 
      return new ARPPacket(lLen, bytes, tv);
    case EthernetProtocols.IP:
	case EthernetProtocols.IPV6:
      // ethernet level code is recognized as IP, figure out what kind..
      int ipProtocol = IPProtocol.extractProtocol(lLen, bytes, ethProtocol);
      switch(ipProtocol) {
        // icmp
      case IPProtocols.ICMP: return new ICMPPacket(lLen, bytes, tv);
        // igmp
      case IPProtocols.IGMP: return new IGMPPacket(lLen, bytes, tv);
        // tcp
      case IPProtocols.TCP: return new TCPPacket(lLen, bytes, tv);
        // udp
      case IPProtocols.UDP: return new UDPPacket(lLen, bytes, tv);
        // unidentified ip..
      default: return new IPPacket(lLen, bytes, tv);
      }
      // ethernet level code not recognized, default to anonymous packet..
    default: return new EthernetPacket(lLen, bytes, tv);
    }
  }


  /**
   * Length in bytes of the link-level headers that this factory is 
   * decoding packets for.
   */
  private static int lLen;

  private String _rcsid = 
    "$Id: PacketFactory.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $";
}

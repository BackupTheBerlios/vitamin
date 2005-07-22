// $Id: EthernetFields.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.net;


/**
 * Ethernet protocol field encoding information.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public interface EthernetFields
{
  // field lengths

  /**
   * Width of the ethernet type code in bytes.
   */
  int ETH_CODE_LEN = 2;


  // field positions

  /**
   * Position of the destination MAC address within the ethernet header.
   */
  int ETH_DST_POS = 0;

  /**
   * Position of the source MAC address within the ethernet header.
   */
  int ETH_SRC_POS = MACAddress.WIDTH;

  /**
   * Position of the ethernet type field within the ethernet header.
   */
  int ETH_CODE_POS = MACAddress.WIDTH * 2;


  // complete header length

  /**
   * Total length of an ethernet header in bytes.
   */
  int ETH_HEADER_LEN = ETH_CODE_POS + ETH_CODE_LEN; // == 14
}

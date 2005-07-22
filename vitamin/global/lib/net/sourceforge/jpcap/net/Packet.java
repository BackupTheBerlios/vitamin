// $Id: Packet.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.net;

import net.sourceforge.jpcap.util.Timeval;
import java.io.Serializable;


/**
 * A network packet.
 * <p>
 * This class currently contains no implementation because only ethernet 
 * is supported. In other words, all instances of packets returned by 
 * packet factory will always be at least as specific as EthernetPacket.
 * <p>
 * On large ethernet networks, I sometimes see packets which don't have 
 * link-level ethernet headers. If and when I figure out what these are, 
 * maybe this class will be the root node of a packet hierarchy derived 
 * from something other than ethernet.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public class Packet implements Serializable
{
  public String toColoredString(boolean colored) {
    return toString();
  }

  /**
   * Fetch data portion of the packet.
   */
  public byte [] getHeader() {
    return null;
  }

  /**
   * Fetch data portion of the packet.
   */
  public byte [] getData() {
    return null;
  }

  public String getColor() {
    return "";
  }

  public Timeval getTimeval() {
    return null;
  }


  private String _rcsid = 
    "$Id: Packet.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $";
}

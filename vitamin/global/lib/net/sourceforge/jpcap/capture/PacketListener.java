// $Id: PacketListener.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.capture;

import net.sourceforge.jpcap.net.Packet;


/**
 * Packet data listener.
 * <p>
 * Applications interested in listening for packets must register
 * with PacketCapture and implement PacketDataListener.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public interface PacketListener
{
  void packetArrived(Packet packet);
}

// $Id: PacketDispatchCapable.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.capture;


/**
 * An interface for classes capable of dispatching captured network 
 * packets.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public interface PacketDispatchCapable
{
  /**
   * Register a raw packet listener with this capture system.
   * @param rawListener the raw packet listener to add to the notification 
   * list.
   */
  void addRawPacketListener(RawPacketListener rawListener);

  /**
   * Deregister a raw packet listener from this capture system.
   * @param rawListener the raw packet listener to remove from the 
   * notification list.
   */
  void removeRawPacketListener(RawPacketListener rawListener);

  /**
   * Register a packet object listener with this capture system.
   * @param objListener the packet listener to add to the notification list.
   */
  void addPacketListener(PacketListener objListener);

  /**
   * Deregister a packet object listener from this capture system.
   * @param objListener the packet listener to remove from the 
   * notification list.
   */
  void removePacketListener(PacketListener objListener);
}

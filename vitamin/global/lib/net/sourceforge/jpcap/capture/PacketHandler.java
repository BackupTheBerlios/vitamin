// $Id: PacketHandler.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.capture;


/**
 * This is the jpcap <b>internal</b> capture interface. libjpcap
 * calls back the jpcap packet handler via this interface when packets 
 * arrive.
 * <p> 
 * If you are writing an application which captures packets from jpcap, 
 * you probably want to look at PacketCapture and PacketListener.
 * <p>
 * The scope of this class is protected because it is only used internally
 * as a contract between libjpcap and PacketCaptureBase.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
interface PacketHandler
{
  /**
   * Handle an arriving packet.
   * 
   * @param length the length of the packet off of the wire.
   * @param caplen the number of bytes actually captured.
   * @param seconds the seconds component of the timestamp.
   * @param useconds the microseconds component of the timestamp.
   * @param data the contents of the captured packet.
   */
  void handlePacket(int length, int caplen, int seconds, int useconds, 
                    byte [] data);
}

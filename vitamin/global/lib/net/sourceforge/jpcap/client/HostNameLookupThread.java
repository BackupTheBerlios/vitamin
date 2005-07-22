// $Id: HostNameLookupThread.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.client;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * A thread that populates a host name in a host renderer.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:12 $
 */
public class HostNameLookupThread extends Thread
{
  HostNameLookupThread(String ipAddress, HostRenderer renderer) {
    this.ipAddress = ipAddress;
    this.renderer = renderer;

    start();
  }

  public void run() {
    try {
      InetAddress ia = InetAddress.getByName(ipAddress);
      String name = ia.getHostName();
      renderer.erase();
      renderer.setHostName(name);
      renderer.paint();
    }
    catch(UnknownHostException e) {
      // if the reverse lookup fails, don't do anything.
      // the IP address is already stored under the name by default
      // in the renderer.
    }
  }


  String ipAddress;
  HostRenderer renderer;

  private String _rcsid = 
    "$Id: HostNameLookupThread.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $";
}

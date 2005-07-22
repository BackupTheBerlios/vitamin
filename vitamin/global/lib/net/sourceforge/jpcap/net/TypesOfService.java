// $Id: TypesOfService.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.net;


/**
 * Type of service code constants for IP. Type of service describes 
 * how a packet should be handled.
 * <p>
 * TOS is an 8-bit record in an IP header which contains a 3-bit 
 * precendence field, 4 TOS bit fields and a 0 bit.
 * <p>
 * The following constants are bit masks which can be logically and'ed
 * with the 8-bit IP TOS field to determine what type of service is set.
 * <p>
 * Taken from TCP/IP Illustrated V1 by Richard Stevens, p34.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public interface TypesOfService
{
  int MINIMIZE_DELAY = 0x10;

  int MAXIMIZE_THROUGHPUT = 0x08;

  int MAXIMIZE_RELIABILITY = 0x04;

  int MINIMIZE_MONETARY_COST = 0x02;

  int UNUSED = 0x01;
}

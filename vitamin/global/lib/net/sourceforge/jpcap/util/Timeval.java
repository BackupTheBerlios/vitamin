// $Id: Timeval.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.util;

import java.util.Date;
import java.io.Serializable;


/**
 * POSIX.4 timeval for Java.
 * <p>
 * Container for java equivalent of c's struct timeval.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:12 $
 */
public class Timeval implements Serializable
{
  public Timeval(long seconds, int microseconds) {
    this.seconds = seconds;
    this.microseconds = microseconds;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(seconds);
    sb.append(".");
    sb.append(microseconds);
    sb.append("s");

    return sb.toString();
  }

  /**
   * Convert this timeval to a java Date.
   */
  public Date getDate() {
    return new Date(seconds * 1000 + microseconds / 1000);
  }

  public long getSeconds() {
    return seconds;
  }

  public int getMicroSeconds() {
    return microseconds;
  }

  long seconds;
  int microseconds;

  private String _rcsid = 
  "$Id: Timeval.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $";
}

// $Id: CaptureConfigurationException.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.capture;


/**
 * This exception occurs when the capture client tries to 
 * specify a capture device that does not exist or if the capture
 * device specified is illegal.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public class CaptureConfigurationException extends Exception
{
  /**
   * Create a new invalid capture device exception.
   */
  public CaptureConfigurationException(String message) { 
    super(message);
  }


  private static String _rcsId = 
    "$Id: CaptureConfigurationException.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $";
}

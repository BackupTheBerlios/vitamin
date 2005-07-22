// $Id: IPAddress.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.net;

import net.sourceforge.jpcap.util.ArrayHelper;


/**
 * IP address.
 * <p>
 * This class doesn't store IP addresses. There's a java class for that,
 * and it is too big and cumbersome for our purposes.
 * <p>
 * This class contains a utility method for extracting an IP address 
 * from a big-endian byte array.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:11 $
 */
public class IPAddress
{
  /**
   * Convert an IP address stored in an int to its string representation.
   */
  private static String toString(int address) {
    StringBuffer sa = new StringBuffer();
    for (int i = 0; i < WIDTH; i++) {
      sa.append(0xff & address >> 24);
      address <<= 8;
      if (i != WIDTH - 1)
        sa.append(".");
    }
    return sa.toString();
  }

  /**
   * Extract a string describing an IP address from an array of bytes.
   *
   * @param offset the offset of the address data.
   * @param bytes an array of bytes containing the IP address.
   * @return a string of the form "255.255.255.255"
   */
  public static String extract(int offset, byte [] bytes) {
    return toString(ArrayHelper.extractInteger(bytes, offset, WIDTH));
    /*
    StringBuffer sa = new StringBuffer();
    for(int i=offset; i<offset + WIDTH; i++) {
      sa.append(0xff & bytes[i]);
      if(i != offset + WIDTH - 1)
        sa.append('.');
    }
    return sa.toString();
    */
  }


	/**
	 * Extract a string describing an IPv6 address from an array of bytes.
	 *
	 * @param offset the offset of the address data in bits.
	 * @param bytes an array of bytes containing the IP address.
	 * @return a string of the form "abcd::1234:ab56:ff33"
	 */
	public static String extractV6(int offset, byte[] bytes) {
		// can't do it the same way as IPv4 because there is no type which is 16 bytes long (long is 8 bytes)

		byte [] address = ArrayHelper.extractBitIndicatedIntegerAsByteArray(bytes, offset, WIDTH_V6);
		String asString = "";
		
		for (int i = 0; i < 16; i += 2) {
			// jeweils 2 Bytes ergeben den Wert für einen Teil der IPv6-Adresse
			asString += Integer.toHexString((address[i] << 8) + address[i+1]);
			
			if (i < 14)
				asString += ":";
		}
				
		return asString;
	}


  /**
   * Generate a random IP number between 0.0.0.0 and 255.255.255.255.
   */
  public static int random() {
    // cast to long before int to preserve all 32-bits of precision
    // (otherwise, highest bit is lost for based on sign)
    return (int)(0xffffffffL * Math.random());
  }

  /**
   * Generate a random IP address.
   * @param network the network number. i.e. 0x0a000000.
   * @param mask the network mask. i.e. 0xffffff00.
   * @return a random IP address on the specified network.
   */
  public static int random(int network, int mask) {
    // the bits that get randomized are the inverse of the mask
    int rbits = ~mask;

    int random = network + (int)(rbits * Math.random()) + 1;

    return random;
  }


  /**
   * Unit test.
   */
  public static void main(String [] args) {
    for(int i=0; i<10; i++) {
      // 10.0.0.16/255.255.255.240
      int r = random(0x0a000010, 0xfffffff0);
      System.err.println(Integer.toHexString(r) + " " + toString(r));
    }
  }


  /**
   * The width in bytes of an IP address.
   */
  public static final int WIDTH = 4;
  public static final int WIDTH_V6 = 128; // in bits

  private String _rcsid = 
    "$Id: IPAddress.java,v 1.1 2005/07/22 14:13:11 mpelze2s Exp $";
}

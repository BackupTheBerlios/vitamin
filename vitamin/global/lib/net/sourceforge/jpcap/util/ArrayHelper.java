// $Id: ArrayHelper.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $

/***************************************************************************
 * Copyright (C) 2001, Patrick Charles and Jonas Lehmann                   *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/
package net.sourceforge.jpcap.util;


/**
 * Utility functions for populating and manipulating arrays.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: mpelze2s $
 * @lastModifiedAt $Date: 2005/07/22 14:13:12 $
 */
public class ArrayHelper
{
  /**
   * Join two arrays.
   */
  public static byte [] join(byte [] a, byte [] b) {
    byte [] bytes = new byte[a.length + b.length];

    System.arraycopy(a, 0, bytes, 0, a.length);
    System.arraycopy(b, 0, bytes, a.length, b.length);

    return bytes;
  }

  /**
   * Extract a long from a byte array.
   *
   * @param bytes an array.
   * @param pos the starting position where the integer is stored.
   * @param cnt the number of bytes which contain the integer.
   * @return the long, or <b>0</b> if the index/length to use 
   *         would cause an ArrayOutOfBoundsException
   */
  public static long extractLong(byte[] bytes, int pos, int cnt) {
    // commented out because it seems like it might mask a fundamental 
    // problem, if the caller is referencing positions out of bounds and 
    // silently getting back '0'.
    //   if((pos + cnt) > bytes.length) return 0;
    long value = 0;
    for(int i=0; i<cnt; i++)
      value |= ((bytes[pos + cnt - i - 1] & 0xffL) << 8 * i);

    return value;
  }

  /**
   * Extract an integer from a byte array.
   *
   * @param bytes an array.
   * @param pos the starting position where the integer is stored.
   * @param cnt the number of bytes which contain the integer.
   * @return the integer, or <b>0</b> if the index/length to use 
   *         would cause an ArrayOutOfBoundsException
   */
  public static int extractInteger(byte[] bytes, int pos, int cnt) {
    // commented out because it seems like it might mask a fundamental 
    // problem, if the caller is referencing positions out of bounds and 
    // silently getting back '0'.
    // if((pos + cnt) > bytes.length) return 0;
    int value = 0;
    for(int i=0; i<cnt; i++)
      value |= ((bytes[pos + cnt - i - 1] & 0xff) << 8 * i);

    return value;
  }


  /** 
   * Extract an integer from a byte array.
   * 
   * @param array bytes in array
   * @param startBit the starting position where the integer is stored in bits.
   * @param lengthInBits number of bits which contain the integer.
   * @return
   */
  public static int extractBitIndicatedInteger (byte [] array, int startBit, int lengthInBits) {
  		  //* DEBUG */ System.out.println("extractBitIndicatedInteger BEGIN.");
  	
		  int startByte = startBit / 8; // / schneidet Nachkommastellen einfach ab. perfekt hierfuer.
		  //System.out.println("startByte: " + startByte);

		  // herausfinden, wieviele Ausgabebytes ich brauche
		  int lengthInBytes = lengthInBits / 8;
		  // wenn das Startbit nicht am Anfang eines Bytes liegt, brauch ich ein Byte mehr
		  // dies gilt allerdings nur, wenn ich fuer das erste Ausagebyte zwei Quellbytes brauche.
		  // Dies ist nicht der Fall, wenn die LengthInBit nicht durch 8 teilbar ist, das erste
		  // Byte also mit 0..0 aufgfuellt werden muss, und ich dadurch fuer das erste Byte weniger
		  // als 7 - startBit Bits benoetige.
		  //if (startBit % 8 != 0 && (startBit + lengthInBits % 8) < 8)
		  //    lengthInBytes++;
		  // wenn lengthInBits nicht genau durch 8 teilbar ist, muss noch ein Byte hinzugefuegt werden, da / immer abrundet
		  if (lengthInBits % 8 != 0)
			  lengthInBytes ++;

		  //System.out.println("benoetigte Bytes: " + lengthInBytes);

		  byte [] newArray = new byte [lengthInBytes];

		  if (startBit + lengthInBits < 8) {
			  // trivialer Fall:
			  // alle zu extrahierenden Bits liegen im selben Byte
			  //System.out.println("Alles im selben Byte.");

			  int startBitInTheByte = startBit % 8; // Stelle in dem Bit, ab der gestartet wird
			  int endBitInTheByte = startBitInTheByte + lengthInBits - 1;
			  //System.out.println("startBitInTheByte: " + startBitInTheByte);
			  //System.out.println("endBitInTheByte: " + endBitInTheByte);
			  newArray [0] = (byte) (getPartOfByte (array [startByte], startBitInTheByte, endBitInTheByte)); // (8 - (endBitInTheByte - startBitInTheByte) - 2));
			  //System.out.println("Wert in Quellfeld: " + Utils.byteToInt(Utils.getPartOfByteArray(array, 0, 1)));
			  //System.out.println("Wert vor Shiften: " + Utils.byteToInt(newArray));
			  //System.out.println("Shiften um: " + (8 - lengthInBits - startBitInTheByte));
			  newArray[0] = (byte) (newArray[0]  >>> (8 - lengthInBits - startBitInTheByte));
			
			  // jetzt noch die aufgefuellten 1 entfernen (durch &)
			  int negierer = 0;
			  for (int i = 0; i < lengthInBits - startBitInTheByte; i++)
				  negierer += Math.pow(2, i);				
			  newArray[0] = (byte) (newArray[0] & negierer);
			
			  //System.out.println("Wert nach Shiften: " + Utils.byteToInt(newArray));
		  }
		  else {
			  // Verteilung ueber mehrere Bytes im Feld (startBit + lengthInBits > 8)
			  //System.out.println("Verteilt auf verschiedene Bytes.");

			  // das erste Ausgabebyte wird evtl. nicht komplett mit Bits aus dem Quellfeld
			  // aufgefuellt. Dies ist der Fall, wenn die Laenge nicht durch 8 teilbar ist.
			  // Das erste Byte muss dann nach vorne mit 0...0 aufgefuellt werden.

			  int lengthOfFirstByte = lengthInBits % 8;
			  int startBitOfTheFollowing = (startBit + lengthOfFirstByte) % 8;
			  int startByteOfTheFollowing = startByte + 1;
			  int start = 0;

			  // die Sonderbehandlung fuer das erste Byte gibt's aber nur, wenn die
			  // LengthInBits nicht genau durch 8 teilbar ist.
			  if (lengthOfFirstByte != 0) {
				  //* DEBUG */ System.out.println("Das erste Byte bekommt eine Sonderbehandlung.");
				  start = 1;;

				  //System.out.println("startBitOfTheFollowing: " + startBitOfTheFollowing);

				  // es kann sein, dass das erste Ausgabebyte sich nur aus dem ersten
				  // Quell-Byte zusammensetzt.
				  if (startBit%8 + lengthOfFirstByte < 8) {
					  //System.out.println("Erstes Byte wird aus nur einem Byte gespeist.");
					  int startBitInTheByte = startBit % 8; // Stelle in dem Byte, ab der gestartet wird
					  int endBitInTheByte = startBitInTheByte + lengthOfFirstByte - 1;

					  //System.out.println("startBitInTheByte = " + startBitInTheByte);
					  //System.out.println("endBitInTheByte = " + endBitInTheByte);

					  newArray [0] = (byte) (getPartOfByte (array [startByte], startBitInTheByte, endBitInTheByte) >> (8 - lengthOfFirstByte - startBitInTheByte));

					  // das zweite Zielbyte wird jetzt noch aus dem ersten Quellbyte gespeist
					  startByteOfTheFollowing--;

				  }
				  else {
					  // hole die Bits von startBit bis Ende des Bytes aus dem 1. Byte
					  byte firstPart =  getPartOfByte(array[startByte], startBit, 7);

					  // hole von 0 an soviele Bits aus dem 2. Byte, dass es insgesamt lengthOfFirstByte Bits sind
					  byte secondPart = getPartOfByte(array[startByte+ 1], 0, (startBit + lengthOfFirstByte) % 8);

					  newArray [0] = (byte)  ((firstPart << (startBit)) + (secondPart >> (7 - startBit + 1)));
				  }

				  //System.out.println("array[0] = " + byteToInt(new byte [] {newArray[0]}));
			  }

			  //System.out.println("startByteOfTheFollowing: " + startByteOfTheFollowing);

			  // Diese Schleife iteriert ueber alle folgenden Ausgabebytes und fuellt diese nach und nach auf.
			  for (int i = start; i < lengthInBytes; i++) {
				  // newArray [i] auffuellen. Dazu benoetige ich 2 Quellbytes.

				  // hole die Bits von startBit bis Ende des Bytes aus dem i. Byte
				  byte firstPart =  getPartOfByte(array[startByteOfTheFollowing + i - 1], startBitOfTheFollowing, 7);

				  //* DEBUG */ System.out.println("startBitOfTheFollowing: " + startBitOfTheFollowing);
                
				  byte secondPart = 0;
				  // wenn startBitOfTheFollowing 0 ist, brauch ich keinen zweiten Teil
				  if (startBitOfTheFollowing != 0)
					  // hole die Bits von 0 bis startBit - 1 aus dem (i+1). Byte                
					  secondPart = getPartOfByte(array[startByteOfTheFollowing + i], 0, startBitOfTheFollowing - 1);

				  //* DEBUG */ System.out.println("firstPart: " + firstPart);
				  //* DEBUG */ System.out.println("secondPart: " + secondPart);

				  //System.out.println(((firstPart << (startBitOfTheFollowing)) + (secondPart >> (7 - startBitOfTheFollowing + 1))));
				  newArray [i] = (byte)  ((firstPart << (startBitOfTheFollowing)) + (secondPart >> (7 - startBitOfTheFollowing + 1)));

				  //System.out.println("array[" + i +  "] = " + byteToInt(new byte [] {newArray[i]}));
			  }
		  }

		  //* DEBUG */ System.out.println("extractBitIndicatedInteger VOR RETURN.");

		  return extractInteger(newArray, 0, newArray.length);
	  }
	  
	  
	/** 
	   * Extract a byte array from a byte array ;-)
	   * 
	   * @param array bytes in array
	   * @param startBit the starting position where the integer is stored in bits.
	   * @param lengthInBits number of bits which contain the integer.
	   * @return
	   */
	  public static byte[] extractBitIndicatedIntegerAsByteArray (byte [] array, int startBit, int lengthInBits) {
		  int startByte = startBit / 8; // / schneidet Nachkommastellen einfach ab. perfekt hierfuer.
		  //System.out.println("startByte: " + startByte);

		  // herausfinden, wieviele Ausgabebytes ich brauche
		  int lengthInBytes = lengthInBits / 8;
		  // wenn das Startbit nicht am Anfang eines Bytes liegt, brauch ich ein Byte mehr
		  // dies gilt allerdings nur, wenn ich fuer das erste Ausagebyte zwei Quellbytes brauche.
		  // Dies ist nicht der Fall, wenn die LengthInBit nicht durch 8 teilbar ist, das erste
		  // Byte also mit 0..0 aufgfuellt werden muss, und ich dadurch fuer das erste Byte weniger
		  // als 7 - startBit Bits benoetige.
		  //if (startBit % 8 != 0 && (startBit + lengthInBits % 8) < 8)
		  //    lengthInBytes++;
		  // wenn lengthInBits nicht genau durch 8 teilbar ist, muss noch ein Byte hinzugefuegt werden, da / immer abrundet
		  if (lengthInBits % 8 != 0)
			  lengthInBytes ++;

		  //System.out.println("benoetigte Bytes: " + lengthInBytes);

		  byte [] newArray = new byte [lengthInBytes];

		  if (startBit + lengthInBits < 8) {
			  // trivialer Fall:
			  // alle zu extrahierenden Bits liegen im selben Byte
			  //System.out.println("Alles im selben Byte.");

			  int startBitInTheByte = startBit % 8; // Stelle in dem Bit, ab der gestartet wird
			  int endBitInTheByte = startBitInTheByte + lengthInBits - 1;
			  //System.out.println("startBitInTheByte: " + startBitInTheByte);
			  //System.out.println("endBitInTheByte: " + endBitInTheByte);
			  newArray [0] = (byte) (getPartOfByte (array [startByte], startBitInTheByte, endBitInTheByte)); // (8 - (endBitInTheByte - startBitInTheByte) - 2));
			  //System.out.println("Wert in Quellfeld: " + Utils.byteToInt(Utils.getPartOfByteArray(array, 0, 1)));
			  //System.out.println("Wert vor Shiften: " + Utils.byteToInt(newArray));
			  //System.out.println("Shiften um: " + (8 - lengthInBits - startBitInTheByte));
			  newArray[0] = (byte) (newArray[0]  >>> (8 - lengthInBits - startBitInTheByte));
			
			  // jetzt noch die aufgefuellten 1 entfernen (durch &)
			  int negierer = 0;
			  for (int i = 0; i < lengthInBits - startBitInTheByte; i++)
				  negierer += Math.pow(2, i);				
			  newArray[0] = (byte) (newArray[0] & negierer);
			
			  //System.out.println("Wert nach Shiften: " + Utils.byteToInt(newArray));
		  }
		  else {
			  // Verteilung ueber mehrere Bytes im Feld (startBit + lengthInBits > 8)
			  //System.out.println("Verteilt auf verschiedene Bytes.");

			  // das erste Ausgabebyte wird evtl. nicht komplett mit Bits aus dem Quellfeld
			  // aufgefuellt. Dies ist der Fall, wenn die Laenge nicht durch 8 teilbar ist.
			  // Das erste Byte muss dann nach vorne mit 0...0 aufgefuellt werden.

			  int lengthOfFirstByte = lengthInBits % 8;
			  int startBitOfTheFollowing = (startBit + lengthOfFirstByte) % 8;
			  int startByteOfTheFollowing = startByte + 1;
			  int start = 0;

			  // die Sonderbehandlung fuer das erste Byte gibt's aber nur, wenn die
			  // LengthInBits nicht genau durch 8 teilbar ist.
			  if (lengthOfFirstByte != 0) {
				  //* DEBUG */ System.out.println("Das erste Byte bekommt eine Sonderbehandlung.");
				  start = 1;;

				  //System.out.println("startBitOfTheFollowing: " + startBitOfTheFollowing);

				  // es kann sein, dass das erste Ausgabebyte sich nur aus dem ersten
				  // Quell-Byte zusammensetzt.
				  if (startBit%8 + lengthOfFirstByte < 8) {
					  //System.out.println("Erstes Byte wird aus nur einem Byte gespeist.");
					  int startBitInTheByte = startBit % 8; // Stelle in dem Byte, ab der gestartet wird
					  int endBitInTheByte = startBitInTheByte + lengthOfFirstByte - 1;

					  //System.out.println("startBitInTheByte = " + startBitInTheByte);
					  //System.out.println("endBitInTheByte = " + endBitInTheByte);

					  newArray [0] = (byte) (getPartOfByte (array [startByte], startBitInTheByte, endBitInTheByte) >> (8 - lengthOfFirstByte - startBitInTheByte));

					  // das zweite Zielbyte wird jetzt noch aus dem ersten Quellbyte gespeist
					  startByteOfTheFollowing--;

				  }
				  else {
					  // hole die Bits von startBit bis Ende des Bytes aus dem 1. Byte
					  byte firstPart =  getPartOfByte(array[startByte], startBit, 7);

					  // hole von 0 an soviele Bits aus dem 2. Byte, dass es insgesamt lengthOfFirstByte Bits sind
					  byte secondPart = getPartOfByte(array[startByte+ 1], 0, (startBit + lengthOfFirstByte) % 8);

					  newArray [0] = (byte)  ((firstPart << (startBit)) + (secondPart >> (7 - startBit + 1)));
				  }

				  //System.out.println("array[0] = " + byteToInt(new byte [] {newArray[0]}));
			  }

			  //System.out.println("startByteOfTheFollowing: " + startByteOfTheFollowing);

			  // Diese Schleife iteriert ueber alle folgenden Ausgabebytes und fuellt diese nach und nach auf.
			  for (int i = start; i < lengthInBytes; i++) {
				  // newArray [i] auffuellen. Dazu benoetige ich 2 Quellbytes.

				  // hole die Bits von startBit bis Ende des Bytes aus dem i. Byte
				  byte firstPart =  getPartOfByte(array[startByteOfTheFollowing + i - 1], startBitOfTheFollowing, 7);

				  //* DEBUG */ System.out.println("startBitOfTheFollowing: " + startBitOfTheFollowing);
                
				  byte secondPart = 0;
				  // wenn startBitOfTheFollowing 0 ist, brauch ich keinen zweiten Teil
				  if (startBitOfTheFollowing != 0)
					  // hole die Bits von 0 bis startBit - 1 aus dem (i+1). Byte                
					  secondPart = getPartOfByte(array[startByteOfTheFollowing + i], 0, startBitOfTheFollowing - 1);

				  //* DEBUG */ System.out.println("firstPart: " + firstPart);
				  //* DEBUG */ System.out.println("secondPart: " + secondPart);

				  //System.out.println(((firstPart << (startBitOfTheFollowing)) + (secondPart >> (7 - startBitOfTheFollowing + 1))));
				  newArray [i] = (byte)  ((firstPart << (startBitOfTheFollowing)) + (secondPart >> (7 - startBitOfTheFollowing + 1)));

				  //System.out.println("array[" + i +  "] = " + byteToInt(new byte [] {newArray[i]}));
			  }
		  }

		  return newArray;
	  }


	  private static byte getPartOfByte (byte byteToWorkOn, int startBit, int endBit) {
		  byte value = 0;

		  // Hier wird jetzt eine Maske erstellt, mit der nur die gewuenschten Bits aus dem Byte extrahiert werden.
		  // Dazu wird ein byte aufgebaut, welches von startBit bis endBit 1 ist und sonst 0. Durch eine &-Verknuepfung
		  // bleiben in dem array[x] dann nur die gewollten Bits stehen.
		  int mask = 0;
		  for (int i = startBit; i <= endBit; i++) {
			  mask += Math.pow(2, 8 - i - 1);
			  //System.out.println(8 - i - 1);
		  }

		  //System.out.println("mask: " + mask);

		  // Hier wird die angesprochene &-Verknuepfung durchgefuehrt.
		  value = ((byte) (byteToWorkOn & mask));

		  return value;
	  }


  /**
   * Insert data contained in a long integer into an array.
   *
   * @param bytes an array.
   * @param value the long to insert into the array.
   * @param pos the starting position into which the long is inserted.
   * @param cnt the number of bytes to insert.
   */
  public static void insertLong(byte[] bytes, long value, int pos, int cnt) {
    for(int i=0; i<cnt; i++) {
      bytes[pos + cnt - i - 1] = (byte)(value & 0xff);
      value >>= 8;
    }
  }

  /**
   * Convert a long integer into an array of bytes.
   * 
   * @param value the long to convert.
   * @param cnt the number of bytes to convert.
   */
  public static byte [] toBytes(long value, int cnt) {
    byte [] bytes = new byte[cnt];
    for(int i=0; i<cnt; i++) {
      bytes[cnt - i - 1] = (byte)(value & 0xff);
      value >>= 8;
    }

    return bytes;
  }

  /**
   * Convert a long integer into an array of bytes, little endian format. 
   * (i.e. this does the same thing as toBytes() but returns an array 
   * in reverse order from the array returned in toBytes().
   * @param value the long to convert.
   * @param cnt the number of bytes to convert.
   */
  public static byte [] toBytesLittleEndian(long value, int cnt) {
    byte [] bytes = new byte[cnt];
    for(int i=0; i<cnt; i++) {
      bytes[i] = (byte)(value & 0xff);
      value >>= 8;
    }

    return bytes;
  }

  public static void fillBytes(byte[] byteArray, long value, 
                               int cnt, int index) {

    for(int i=0; i<cnt; i++) {
      byteArray[cnt - i - 1 + index] = (byte)(value & 0xff);
      value >>= 8;
    }
  }

  public static void fillBytesLittleEndian(byte[] byteArray, long value, 
                                           int cnt, int index) {
    for(int i=0; i<cnt; i++) {
      byteArray[index+i] = (byte)(value & 0xff);
      value >>= 8;
    }
  }

  static final String _rcsid = 
    "$Id: ArrayHelper.java,v 1.1 2005/07/22 14:13:12 mpelze2s Exp $";
}

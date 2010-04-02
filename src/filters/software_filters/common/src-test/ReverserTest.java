/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/
////TODO: remove or move to tests
//package com.sun.tmc.util;
//
//import junit.framework.*;
//
//public class ReverserTest extends TestCase
//{
//  private String strPlainEven;
//  private String strPlainOdd;
//  private String strEscapes;
//  private String strEscapesLong;
//  private String strEscapeLastChar;
//
//  public ReverserTest(String name)
//  {
//    super(name);
//  }
//
//  public void setUp()
//  {
//    strPlainEven = "This is a plain string. It has an even number of characters. ";
//    strPlainOdd = "This is a plain string. It has an odd number of characters. ";
//    strEscapes = " \\t\\n\\\\";
//    strEscapesLong = "\\t oook.\\n A string with escapes and \\t '\\\\' and other characters.";
//    strEscapeLastChar = "Some text. \\" ;
//  }
//
//  public void testReversePlainEvenString()
//  {
//    String out = StringReverser.reverseString(strPlainEven);
//    assertEquals(" .sretcarahc fo rebmun neve na sah tI .gnirts nialp a si sihT",out);
//  }
//
//  public void testReversePlainOddString()
//  {
//    String out = StringReverser.reverseString(strPlainOdd);
//    assertEquals(" .sretcarahc fo rebmun ddo na sah tI .gnirts nialp a si sihT",out);
//  }
//
//  public void testReverseEscapes()
//  {
//    StringReverser StringReverser = new StringReverser();
//String out =strEscapes;
//    assertEquals(strEscapes,out);
///*
//    String out = StringReverser.reverseString(strEscapes);
//    assert(out.equals("\\\\\\n\\t"));
//*/
//  }
//
//  public void testReverseEscapesLong()
//  {
//    String out = StringReverser.reverseString(strEscapesLong);
//    assertEquals(".sretcarahc rehto dna '\\\\' \\t dna sepacse htiw gnirts A \\n.kooo \\t",out);
//  }
//
//  public void testEscapeLastChar()
//  {
//    String out = StringReverser.reverseString(strEscapeLastChar);
//    assertEquals("\\ .txet emoS",out);
//  }
//}

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
//package com.sun.tmc.tmci;
//
//import junit.framework.*;
//import com.sun.tmc.util.*;
//import com.sun.tmc.io.*;
//
//public class TMCiEntityConversionTest
//extends TestCase
//{
//  private String strNewline;
//  private String strEntity;
//
//
//  public TMCiEntityConversionTest(String name)
//  {
//    super(name);
//  }
//
//  public void setUp()
//  {
//    strNewline = "This is a string\n"
//      + "that crosses\n"
//      + "a couple\tof lines.\n"
//      + "The string has a few\n"
//      + "newline characters & stuff\n"
//      + "embedded in it.\n";
//    strEntity = "This is a string&#0d;"
//      + "that crosses&#0d;"
//      + "a couple\tof lines.&#0d;"
//      + "The string has a few&#0d;"
//      + "newline characters & stuff&#0d;"
//      + "embedded in it.&#0d;";
//  }
//
//  public void testEntityConversion()
//  {
//
//    String strActual = CharacterEntityBuilder.convertNewlineToEntity(strNewline);
//
//    if(!strActual.equals(strEntity))
//    {
//      System.out.print("\n\nExpected:\n" + strEntity);
//      System.out.print("\n\nActual\n" + strActual + "\n\n");
//    }
//    assertEquals("Entity converstion failed", strEntity, strActual);
//  }
//
//  public void testEntityBackConversion()
//  {
//
//    String strActual = CharacterEntityBuilder.convertEntityToNewline(strEntity);
//
//    if(!strActual.equals(strNewline))
//    {
//      System.out.print("\n\nExpected:\n" + strNewline);
//      System.out.print("\n\nActual\n" + strActual + "\n\n");
//    }
//    assertEquals("Entity converstion failed", strNewline, strActual);
//  }
//
//
//  public static Test suite()
//  {
//    TestSuite suite = new TestSuite();
//
//    suite.addTest( new TMCiEntityConversionTest("testEntityConversion"));
//    suite.addTest( new TMCiEntityConversionTest("testEntityBackConversion"));
//
//    suite.addTest( new ReverserTest("testReversePlainEvenString"));
//    suite.addTest( new ReverserTest("testReversePlainOddString"));
//    suite.addTest( new ReverserTest("testReverseEscapes"));
//    suite.addTest( new ReverserTest("testReverseEscapesLong"));
//    suite.addTest( new ReverserTest("testEscapeLastChar"));
//
//    //  IO class tests.
//    suite.addTest( new ControlDReaderTest("testControlDConversion"));
//    suite.addTest( new ControlDReaderTest("testLoneControlDConversion"));
//
//    return suite;
//  }
//}
//
//

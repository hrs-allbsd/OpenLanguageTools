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
//package com.sun.tmc.io;
//
//import junit.framework.*;
//import com.sun.tmc.util.*;
//import java.io.*;
//
//public class ControlDReaderTest
//extends TestCase
//{
//
//  public ControlDReaderTest(String name)
//  {
//    super(name);
//  }
//
//  public void testControlDConversion()
//  {
//    String strInput = "\004 (\\004) starts this string.\n It also appears in the middle here (\004), and \\004 appears again at the very end \004";
//
//    String strExpected = "\\004 (\\004) starts this string.\n It also appears in the middle here (\\004), and \\004 appears again at the very end \\004";
//
//    doTestEscapeString(strInput, strExpected);
//  }
//
//  public void testLoneControlDConversion()
//  {
//    String strInput = "\004";
//    String strExpected = "\\004";
//
//    doTestEscapeString(strInput, strExpected);
//  }
//
//  public void doTestEscapeString(String strInput, String strExpected)
//  {
//    try
//    {
//      StringReader readerString = new StringReader(strInput);
//      ControlDEscapingFilterReader reader =
//	new ControlDEscapingFilterReader(readerString);
//
//      StringWriter writer = new StringWriter();
//      int ch;
//      while((ch = reader.read()) != -1)
//      {
//	writer.write(ch);
//      }
//      writer.flush();
//      String strActual = writer.toString();
//
//      if(!strActual.equals(strExpected))
//      {
//	System.err.print("Expected:\n" + strExpected + "\n\n");
//	System.err.print("Actual:\n" + strActual + "\n\n");
//      }
//      assertEquals(strExpected, strActual);
//    }
//    catch(Exception ex)
//    {
//      fail(ex.getMessage());
//    }
//  }
//}

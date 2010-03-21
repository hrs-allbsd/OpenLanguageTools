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

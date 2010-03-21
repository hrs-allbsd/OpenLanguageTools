////TODO: remove or move to tests
///*
// * Copyright  2005 Sun Microsystems, Inc.
// * All rights reserved Use is subject to license terms.
// *
// */
//
//package org.jvnet.olt.parsers.MsgFileParser;
//
//import junit.framework.*;
//import java.io.StringReader;
//import java.io.StringWriter;
//import org.jvnet.olt.tmci.TokenCell;
//import org.jvnet.olt.tmci.MessageTokenCell;
//import java.util.Vector;
//import java.util.Enumeration;
//
//public class BlankLineDisplayingTest
//extends TestCase
//implements MsgFileParserTreeConstants
//{
//  StringReader reader;
//  String string;
//
//  public BlankLineDisplayingTest(String name)
//  {
//    super(name);
//  }
//  public void setUp()
//  {
//    //  Put a sample .msg file in a string
//    string =
//      /* START TEST FILE */
//      "$quote \"\n" +
//      "$set 1\n" +
//      "5 \"<unknown>\"\n" +
//      "6 \"...\"\n" +
//      "$set 11\n" +
//      "1 \"SKIP Kij algorithms (version 1):\\n\"\n" +
//      "2 \"SKIP Kij algorithms:\\n\"\n" +
//      "3 \"SKIP Crypt algorithms (version 1):\\n\"\n" +
//      "4 \"SKIP Crypt algorithms:\\n\"\n" +
//      "5 \"SKIP MAC algorithms:\\n\"\n" +
//      "6 \"SKIP NSID list:\\n\"\n" +
//      "7 \"Get IP Mask failed!\\n\"\n" +
//      "8 \"\\t*** Error in key id\\n\"\n" +
//      "\n" +
//      "$ A comment\n" +
//      "9 \"\\t*** Error in key id\\n\"\n" +
//      "\n" +
//      "$quote \n" +
//      "$set 12\n" +
//      "18  mask=%s\n" +
//      "19  tunnel=%s\n" +
//      "20  version=1 kij_alg=%1$s kp_alg=%2$s \n" +
//      "21 \\t*** Error in key id\\n\n" +
//      "22 *** : Invalid NSID/node id\\n\n" +
//      "23 \\t*** Error in key id\\n\n" +
//      "24  version=%d \n" +
//      "25 \\t*** Error in key id\\n\n" +
//      "26 \\t*** Error in key id\\n\n" +
//      "27 \\t*** Mulitline message to\\n\\\n" +
//      "test the quoting code and the\\\n" +
//      "multiline handling of the parser in general.\n";
//    /* END TEST FILE */
//    reader = new StringReader(string);
//  }
//
//
//  public void testTokenCellRoundTrip()
//  {
//    MsgFileParser parser = new MsgFileParser(reader);
//    StringWriter writer = new StringWriter();
//    try
//    {
//      //  Parse the test file
//      parser.parse();
//
//      //  Build TokenCell array
//      DisplayingTokenArrayFactoryVisitor displayingVisitor
//	= new DisplayingTokenArrayFactoryVisitor();
//      parser.walkParseTree(displayingVisitor, "");
//
//      //  Print TokenCell array to test string
//      TokenCell[] cellArray = displayingVisitor.getDisplayingTokens();
//      for(int i = 0; i < cellArray.length; i++ )
//      {
//	if(cellArray[i].getType() != TokenCell.MARKER)
//	{
//	  writer.write(cellArray[i].getText());
//	}
//      }
//    }
//    catch(Exception ex)
//    {
//      fail("Parse of the test file failed!" + ex);
//    }
//
//    String actual =  writer.getBuffer().toString();
//    if(!string.equals( actual ))
//    {
//      System.out.print("\nExpected:\n" + string + "\n\n");
//      System.out.print("\nActual:\n" + actual + "\n\n");
//
//    }
//
//    assertEquals("Round trip handling of the message file, using the TokenCell array, failed!", string, actual );
//
//  }
//}
//

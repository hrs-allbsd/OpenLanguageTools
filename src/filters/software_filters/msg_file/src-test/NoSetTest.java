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
//
//public class NoSetTest
//extends TestCase
//implements MsgFileParserTreeConstants
//{
//  private StringReader reader;
//  private String string;
//  public NoSetTest(String name)
//  {
//    super(name);
//  }
//
//  public void setUp()
//  {
//    //  Put a sample .msg file in a string
//    string =
//      "$quote \"\n" +
//"1 \"SKIP Kij algorithms (version 1):\\n\"\n" +
//"2 \"SKIP Kij algorithms:\\n\"\n" +
//"3 \"SKIP Crypt algorithms (version 1):\\n\"\n" +
//"4 \"SKIP Crypt algorithms:\\n\"\n" +
//"5 \"SKIP MAC algorithms:\\n\"\n" +
//"6 \"SKIP NSID list:\\n\"\n" +
//"7 \"Get IP Mask failed!\\n\"\n" +
//"8 \"\\t*** Error in key id\\n\"\n" +
//"9 \"*** : Invalid NSID/node id\\n\"\n" +
//"10 \"\\t*** Error in key id\\n\"\n" +
//"11 \"\\t*** Error in key id\\n\"\n" +
//"12 \"\\t*** Error in key id\\n\"\n" +
//"13 \"%1$s:\\t\\tbad version %2$d\\n\"\n" +
//"14 \"Get IP Mask failed!\\n\"\n" +
//"15 \"skiphost ifname=%1$s action=%2$s address=%3$s\"\n" +
//"16 \"exclude\"\n" +
//"17 \"�ɲ�\"\n" +
//"$quote \n" +
//"$set 12\n" +
//"18 \" mask=%s\"\n" +
//"19 \" tunnel=%s\"\n" +
//"20 \" version=1 kij_alg=%1$s kp_alg=%2$s \"\n" +
//"21 \"\\t*** Error in key id\\n\"\n" +
//"22 \"*** : Invalid NSID/node id\\n\"\n" +
//"23 \"\\t*** Error in key id\\n\"\n" +
//"24 \" version=%d \"\n" +
//"25 \"\\t*** Error in key id\\n\"\n" +
//"26 \"\\t*** Error in key id\\n\"\n";
//
//    reader = new StringReader(string);
//  }
//
//  public void testParse()
//  {
//    MsgFileParser parser = new MsgFileParser(reader);
//    try
//    {
//      parser.parse();
//    }
//    catch(Exception ex)
//    {
//      fail("Parse of the test file failed!\n" + ex);
//    }
//  }
//
//
//  public void testMessageArrayFactory()
//  {
//    MsgFileParser parser = new MsgFileParser(reader);
//    try
//    {
//      //  Parse file
//      parser.parse();
//
//      //  Count the messages
//      MessageCountingVisitor countVisitor = new MessageCountingVisitor();
//      parser.walkParseTree(countVisitor,"");
//
//      //  Extract the messages to the String array
//      MessageArrayFactoryVisitor factoryVisitor
//	= new MessageArrayFactoryVisitor(countVisitor.getMessageCount(), "catalog");
//      parser.walkParseTree(factoryVisitor,"");
//
//      String[][] stringArray = factoryVisitor.generateMessageArray();
//
//      //  Do tests: A quick sampling
//      assertEquals("catalog", stringArray[4][1]);
//      assertEquals("1:5", stringArray[4][0]);
//      assertEquals("\" tunnel=%s\"", stringArray[18][2]);
//      assertEquals("12:19", stringArray[18][0]);
//      assertEquals("Get IP Mask failed!\\n", stringArray[6][2]);
//      assertEquals("\\t*** Error in key id\\n", stringArray[10][2]);
//
//    }
//    catch(Exception ex)
//    {
//      fail("Parse of the test file failed!" + ex);
//    }
//  }
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
//
//
//
//}

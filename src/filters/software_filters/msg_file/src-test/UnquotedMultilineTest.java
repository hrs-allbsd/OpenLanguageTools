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
//
///**
// *  This class tests that MSG files with unquoted multiline messages
// *  are parsed correctly.
// */
//public class UnquotedMultilineTest
//extends TestCase
//implements MsgFileParserTreeConstants
//{
//  private String msgfile;
//
//  public UnquotedMultilineTest(String name)
//  {
//    super(name);
//  }
//
//  public void setUp()
//  {
//    /* START TEST FILE */
//    msgfile= "$set 1\n" +
//      "12\tStuff on one line.\n" +
//      "13\tStuff on more than \\\n" +
//      "one line.\n" +
//      "14 A message on more than one \\\n" +
//      "line that happens to have a trailing slash.\\\n" +
//      "\n" +
//      "$ A comment\n" +
//      "15\tA dull message\n" +
//      "\t\n" +
//      "16\tA message that has two trailing slashes \\\\\n" +
//      "17\tAnother  dull message\n";
//    /* END TEST FILE */
//  }
//
//  /**
//   *  This method tests how the MSG parser handles a message with
//   *  a single slash at the end of the last line. It should append
//   *  the <newline> to the message.
//   */
//  public void testTrailingSlashHandling()
//  {
//    StringReader reader = new StringReader(msgfile);
//    MsgFileParser parser = new MsgFileParser(reader);
//    try
//    {
//      parser.parse();
//      MessageCountingVisitor counter = new MessageCountingVisitor();
//      parser.walkParseTree(counter, "");
//
//      int iNumMsg = counter.getMessageCount();
//      MessageArrayFactoryVisitor visitor = new MessageArrayFactoryVisitor(iNumMsg, "catalog");
//      parser.walkParseTree(visitor, "");
//
//      String[][] messages = visitor.generateMessageArray();
//
//      assertEquals("A message on more than one \\\nline that happens to have a trailing slash.\\\n", messages[2][2]);
//      assertEquals("A dull message", messages[3][2]);
//    }
//    catch(Exception ex)
//    {
//      fail("Parse failed");
//      ex.printStackTrace();
//    }
//  }
//
//  /**
//   *  This method tests how the MSG parser handles a message with
//   *  a two slashes at the end of the last line. It should not
//   *  append the <newline> to the message.
//   */
// public void testTrailingDoubleSlashHandling()
//  {
//    StringReader reader = new StringReader(msgfile);
//    MsgFileParser parser = new MsgFileParser(reader);
//    try
//    {
//      parser.parse();
//      MessageCountingVisitor counter = new MessageCountingVisitor();
//      parser.walkParseTree(counter, "");
//
//      int iNumMsg = counter.getMessageCount();
//      MessageArrayFactoryVisitor visitor = new MessageArrayFactoryVisitor(iNumMsg, "catalog");
//      parser.walkParseTree(visitor, "");
//
//      String[][] messages = visitor.generateMessageArray();
//
//      assertEquals("A message that has two trailing slashes \\\\", messages[4][2]);
//    }
//    catch(Exception ex)
//    {
//      fail("Parse failed");
//      ex.printStackTrace();
//    }
//  }
//
//}

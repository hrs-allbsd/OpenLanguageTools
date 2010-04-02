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
//public class CommentNSlashTest
//extends TestCase
//{
//  StringReader reader;
//
//  public CommentNSlashTest(String name)
//  {
//    super(name);
//  }
//
//  public void setUp()
//  {
//    String string =   "$quote \"\n" +
//      "$set 1\n" +
//      "$ This is a comment\n" +
//      "1 \"A message\"\n" +
//      "\n" +
//      "$ The message above was sedate. There were not\n" +
//      "$ extra features \\\n" +
//      "2 \"This should be OK\"\n" +
//      "3 \"Foo bar\"\n" +
//      "\n";
//    reader = new StringReader(string);
//  }
//
//  public void testCommentParses()
//  {
//    try
//    {
//      MsgFileParser parser = new MsgFileParser(reader);
//      parser.parse();
//    }
//    catch(Exception ex)
//    {
//      fail("The file did not parse!");
//    }
//  }
//
//  public void testSecondMessageSurvives()
//  {
//    try
//    {
//      MsgFileParser parser = new MsgFileParser(reader);
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
//      assertEquals("This should be OK", stringArray[1][2]);
//
//    }
//    catch(Exception ex)
//    {
//      fail("The file did not parse!");
//    }
//  }
//
//}

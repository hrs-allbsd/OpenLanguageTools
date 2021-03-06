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
//package org.jvnet.olt.parsers.PropsFileParser;
//
//import junit.framework.*;
//import java.io.*;
//import java.util.Hashtable;
//
//public class L10nCommentsTest
//extends TestCase
//{
//  private StringReader m_reader;
//
//  public L10nCommentsTest(String name)
//  {
//    super(name);
//    m_reader = null;
//  }
//
//  public void setUp()
//  {
//    //  Create test string and generated a string reader
//    String strFile = "! A comment\nmessage.one=Text of the message.\n" +
//      "message.two A message without an equals.\n" +
//      "#@LC@ The message below spans three lines\n" +
//      "message.three=A message \\\nthat spans three \\\nlines.\n" +
//      "#@LC@ A multiline comment\n#@LC@ on two lines\n" +
//      "# An intervening base comment\nmessage.four=The fourth message.\n";
//
//    m_reader = new StringReader(strFile);
//  }
//
//
//  public void testL10nCommentStorage()
//  {
//    if(m_reader != null)
//    {
//      try
//      {
//	//  Create the parser
//	PropsFileParser parser = new PropsFileParser(m_reader);
//	parser.parse();
//
//	//  Count the messages
//	MessageCountingVisitor countVisitor = new MessageCountingVisitor();
//	parser.walkParseTree(countVisitor,"");
//
//	//  Extract the messages to the String array
//	MessageArrayFactoryVisitor factoryVisitor
//	  = new MessageArrayFactoryVisitor(countVisitor.getMessageCount(), "catalog");
//	parser.walkParseTree(factoryVisitor,"");
//
//	String[][] stringArray = factoryVisitor.generateMessageArray();
//
//	//  Do the actual tests.
//	assertEquals("",stringArray[0][3]);
//	assertEquals("",stringArray[1][3]);
//	assertEquals("#@LC@ The message below spans three lines",stringArray[2][3]);
//	assertEquals("#@LC@ A multiline comment\n#@LC@ on two lines",stringArray[3][3]);
//      }
//      catch(Exception ex)
//      {
//	fail( ex.toString() );
//      }
//    }
//    else
//    {
//      fail("ERROR: the test file didn't initialize.");
//    }
//  }
//}

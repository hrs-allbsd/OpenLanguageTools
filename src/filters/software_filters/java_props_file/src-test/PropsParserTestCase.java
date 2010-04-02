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
//public class PropsParserTestCase
//extends TestCase
//{
//  private StringReader m_reader;
//
//  public PropsParserTestCase(String name)
//  {
//    super(name);
//    m_reader = null;
//  }
//
//  public void setUp()
//  {
//    //  Create test string and generated a string reader
//    String strFile = "! A comment\n" +
//      "without_equals A message without an equals sign\n" +
//      "with.equals=A message with an equals sign\n# Another comment\n" +
//      "# but this one spans\n# three lines\n" +
//      "message.three=A message that spans \\\nmultiple lines\n";
//
//    m_reader = new StringReader(strFile);
//  }
//
//  /**
//   *  This test checks to see that the test file passes basic
//   *  parsing - to see if the parser 'smokes' when turned on.
//   */
//  public void testSmoke()
//  {
//    if(m_reader != null)
//    {
//      try
//      {
//	PropsFileParser parser = new PropsFileParser(m_reader);
//	parser.parse();
//      }
//      catch(ParseException parseEx)
//      {
//	fail( parseEx.toString() );
//      }
//    }
//    else
//    {
//      fail("ERROR: the test file didn't initialize.");
//    }
//  }
//
//  public void testOutput()
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
//	// No. messages
//	assertEquals("The wrong number of messages were detected. ",3,stringArray.length);
//
//	//  keys
//	assertEquals("without_equals",stringArray[0][0]);
//	assertEquals("with.equals",stringArray[1][0]);
//	assertEquals("message.three",stringArray[2][0]);
//
//	//  values
//	assertEquals("A message without an equals sign",stringArray[0][2]);
//	assertEquals("A message with an equals sign",stringArray[1][2]);
//	assertEquals("A message that spans \\\nmultiple lines",stringArray[2][2]);
//
//	// assertEquals("! A comment\n",stringArray[0][3]);
//	// assertEquals("",stringArray[1][3]);
//	// assertEquals("# Another comment\n# but this one spans\n# three lines\n",stringArray[2][3]);
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
//
//
//  public static Test suite()
//  {
//    TestSuite suite = new TestSuite();
//
//    suite.addTest( new PropsParserTestCase("testSmoke"));
//    suite.addTest( new PropsParserTestCase("testOutput"));
//    suite.addTest( new L10nCommentsTest("testL10nCommentStorage"));
//    suite.addTest( new TokenCellVisitorTest("testTokenCellRoundTrip"));
//    suite.addTest( new LeadingWSCommentsTest("testLeadingWhiteSpaceHandling"));
//
//
//    return suite;
//  }
//}

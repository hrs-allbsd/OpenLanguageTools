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
//public class CommentTerminationTest
//extends TestCase
//{
//  public CommentTerminationTest(String name)
//  {
//    super(name);
//  }
//
//
//  public void testWithSetEnding()
//  {
//    String string =   "$quote \"\n" +
//      "$set 1\n" +
//      "$ This is a comment\n" +
//      "1 \"A message\"\n" +
//      "\n" +
//      "$ The message above was sedate. There were not\n" +
//      "$ extra feature $set\n" +
//      "2 \"This should be OK\"\n" +
//      "3 \"Foo bar\"\n" +
//      "\n";
//    readAndParseString(string);
//  }
//
//  public void testWithQuoteEnding()
//  {
//    String string =   "$quote \"\n" +
//      "$set 1\n" +
//      "$ This is a comment\n" +
//      "1 \"A message\"\n" +
//      "\n" +
//      "$ The message above was sedate. There were not\n" +
//      "$ extra feature $quote\n" +
//      "2 \"This should be OK\"\n" +
//      "3 \"Foo bar\"\n" +
//      "\n";
//    readAndParseString(string);
//  }
//
//  public void testWithJustSlashEnding()
//  {
//    String string =   "$quote \"\n" +
//      "$set 1\n" +
//      "$ This is a comment\n" +
//      "1 \"A message\"\n" +
//      "\n" +
//      "$ \\\\\n" +
//      "$ \\\n" +
//      "2 \"This should be OK\"\n" +
//      "3 \"Foo bar\"\n" +
//      "\n";
//    readAndParseString(string);
//  }
//
//  public void testWithDelsetEnding()
//  {
//    String string =   "$quote \"\n" +
//      "$set 1\n" +
//      "$ This is a comment\n" +
//      "1 \"A message\"\n" +
//      "\n" +
//      "$ Stuff $delset\n" +
//      "$ $delset\n" +
//      "2 \"This should be OK\"\n" +
//      "3 \"Foo bar\"\n" +
//      "\n";
//    readAndParseString(string);
//  }
//
//  public void testWithNumberEnding()
//  {
//    String string =   "$quote \"\n" +
//      "$set 1\n" +
//      "$ This is a comment\n" +
//      "1 \"A message\"\n" +
//      "\n" +
//      "$ 1234 43\n" +
//      "$ 90\n" +
//      "2 \"This should be OK\"\n" +
//      "3 \"Foo bar\"\n" +
//      "\n";
//    readAndParseString(string);
//  }
//  public void testWithTMCCommentEnding()
//  {
//    String string =   "$quote \"\n" +
//      "$set 1\n" +
//      "$ This is a comment\n" +
//      "1 \"A message\"\n" +
//      "\n" +
//      "$ $@TMC@\n" +
//      "2 \"This should be OK\"\n" +
//      "3 \"Foo bar\"\n" +
//      "\n";
//    readAndParseString(string);
//  }
//
//  public void testWithLCCommentEnding()
//  {
//    String string =   "$quote \"\n" +
//      "$set 1\n" +
//      "$ This is a comment\n" +
//      "1 \"A message\"\n" +
//      "\n" +
//      "$ $@LC@\n" +
//      "2 \"This should be OK\"\n" +
//      "3 \"Foo bar\"\n" +
//      "\n";
//    readAndParseString(string);
//  }
//
//  private void readAndParseString(String testfile)
//  {
//    try
//    {
//      StringReader reader = new StringReader(testfile);
//      MsgFileParser parser = new MsgFileParser(reader);
//      parser.parse();
//    }
//    catch(Exception ex)
//    {
//      System.err.print("Test file = <" + testfile + ">\n");
//      fail("The parse of the test file failed.");
//    }
//  }
//
//  public static Test suite()
//  {
//    TestSuite suite = new TestSuite();
//
//    suite.addTest( new CommentTerminationTest("testWithSetEnding"));
//    suite.addTest( new CommentTerminationTest("testWithQuoteEnding"));
//    suite.addTest( new CommentTerminationTest("testWithJustSlashEnding"));
//    suite.addTest( new CommentTerminationTest("testWithJustSlashEnding"));
//    suite.addTest( new CommentTerminationTest("testWithDelsetEnding"));
//    suite.addTest( new CommentTerminationTest("testWithNumberEnding"));
//    suite.addTest( new CommentTerminationTest("testWithTMCCommentEnding"));
//    suite.addTest( new CommentTerminationTest("testWithLCCommentEnding"));
//
//    return suite;
//  }
//}

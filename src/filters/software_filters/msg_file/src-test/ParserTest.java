
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

import junit.framework.*;
import java.io.StringReader;

public class ParserTest 
extends TestCase
{
  StringReader reader;


  public ParserTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    //  Put a sample .msg file in a string
    String string = 
      "$quote \"\n" +
"$set   1\n" +
"5 \"<unknown>\"\n" +
"6 \"...\"\n" +
"$set   11\n" +
"1 \"SKIP Kij algorithms (version 1):\\n\"\n" +
"2 \"SKIP Kij algorithms:\\n\"\n" +
"3 \"SKIP Crypt algorithms (version 1):\\n\"\n" +
"4 \"SKIP Crypt algorithms:\\n\"\n" +
"5 \"SKIP MAC algorithms:\\n\"\n" +
"6 \"SKIP NSID list:\\n\"\n" +
"7 \"Get IP Mask failed!\\n\"\n" +
"8 \"\\t*** Error in key id\\n\"\n" +
"9 \"*** : Invalid NSID/node id\\n\"\n" +
"10 \"\\t*** Error in key id\\n\"\n" +
"11 \"\\t*** Error in key id\\n\"\n" +
"12 \"\\t*** Error in key id\\n\"\n" +
"13 \"%1$s:\\t\\tbad version %2$d\\n\"\n" +
"14 \"Get IP Mask failed!\\n\"\n" +
"15 \"skiphost ifname=%1$s action=%2$s address=%3$s\"\n" +
"16 \"exclude\"\n" +
"17 \"ди╡ц\"\n" +
"$quote \n" +
"$set 12\n" +
"18 \" mask=%s\"\n" +
"19 \" tunnel=%s\"\n" +
"20 \" version=1 kij_alg=%1$s kp_alg=%2$s \"\n" +
"21 \"\\t*** Error in key id\\n\"\n" +
"22 \"*** : Invalid NSID/node id\\n\"\n" +
"23 \"\\t*** Error in key id\\n\"\n" +
"24 \" version=%d \"\n" +
"25 \"\\t*** Error in key id\\n\"\n" +
"26 \"\\t*** Error in key id\\n\"\n";

    reader = new StringReader(string);
  }

  public void testParse()
  {
    MsgFileParser parser = new MsgFileParser(reader);
    try
    {
      parser.parse();      
    }
    catch(Exception ex)
    {
      fail("Parse of the test file failed!" + ex);
    }
  }


  public void testMessageArrayFactory()
  {
    MsgFileParser parser = new MsgFileParser(reader);
    try
    {
      //  Parse file
      parser.parse();

      //  Count the messages
      MessageCountingVisitor countVisitor = new MessageCountingVisitor();
      parser.walkParseTree(countVisitor,"");
      
      //  Extract the messages to the String array
      MessageArrayFactoryVisitor factoryVisitor
	= new MessageArrayFactoryVisitor(countVisitor.getMessageCount(), "catalog");
      parser.walkParseTree(factoryVisitor,"");
      
      String[][] stringArray = factoryVisitor.generateMessageArray();

      //  Do tests: A quick sampling
      assertEquals("catalog", stringArray[4][1]);
      assertEquals("\" tunnel=%s\"", stringArray[20][2]);
      assertEquals("12:19", stringArray[20][0]);
      assertEquals("Get IP Mask failed!\\n", stringArray[8][2]);
      assertEquals("\\t*** Error in key id\\n", stringArray[12][2]);

    }
    catch(Exception ex)
    {
      fail("Parse of the test file failed!" + ex);
    }
    
    
  }


  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTest( new ParserTest("testParse"));
    suite.addTest( new DetailedParserTest("testSetsFound"));
    suite.addTest( new DetailedParserTest("testQuoteDirective"));
    suite.addTest( new DetailedParserTest("testQuoteStripping"));
    suite.addTest( new DetailedParserTest("testMultiLineHandling"));
    suite.addTest( new DisplayingNodeTest("testDisplayRoundTrip"));
    suite.addTest( new SetCommentAdditions("testParse"));
    suite.addTest( new SetCommentAdditions("testDisplayRoundTrip"));
    suite.addTest( new DisplayingNodeTest("testTokenCellRoundTrip"));
    suite.addTest( new ParserTest("testMessageArrayFactory"));
    suite.addTest( new NoSetTest("testParse"));
    suite.addTest( new NoSetTest("testMessageArrayFactory"));
    suite.addTest( new NoSetTest("testTokenCellRoundTrip"));
    suite.addTest( new BlankLineDisplayingTest("testTokenCellRoundTrip"));
    suite.addTest( new UnquotedMultilineTest("testTrailingSlashHandling"));
    suite.addTest( new UnquotedMultilineTest("testTrailingDoubleSlashHandling"));

    //  Add the whitespace handling tests
    suite.addTest( WhiteSpaceHandlingTest.suite() );

    //  Test(s) to check that the parser rejects bad files.
    suite.addTest( new CommentTest("testCommentHaltsParse", org.jvnet.olt.parsers.MsgFileParser.ParseException.class));

    //  Comments with trailing slash tests.
    suite.addTest( new CommentNSlashTest("testCommentParses"));
    suite.addTest( new CommentNSlashTest("testSecondMessageSurvives"));

    suite.addTest( CommentTerminationTest.suite() );

    return suite;
  }


}

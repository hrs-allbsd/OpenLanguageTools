
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

import junit.framework.*;
import java.io.StringReader;


public class WhiteSpaceHandlingTest 
extends TestCase
implements MsgFileParserTreeConstants
{
  private String[][] messages;

  public WhiteSpaceHandlingTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    try
    {
      //  Put a sample .msg file in a string
      String string = 
	/* START TEST FILE */
	"$set 12\n" + 
	"1    This is a message with three leading and three trailing spaces.   \n" + 
	"2 \tThis message has a leading tab \t \n" + 
	"3  \t This is a multiline \\\n" + 
	"message. It also has \\\n" + 
	"ding abd trailing white space. \t\n" + 
	"\n" + 
	"4    Hmmm. \\\n" + 
	"\t \n" + 
	"\n" + 
	"$quote \"\n" + 
	"$set 14\n" + 
	"1 \t \"A single line message in quotes\"   \n" + 
	"2    \"This message has leading spaces only\"\n" + 
	"3\t\"This message has a leading tab and trailing whitespace\"  \t \n" + 
	"4  \"This is a multiline \\\n" + 
	"message, in \\\n" + 
	"quotes.\"  \t \n" + 
	"5 'An incorrectly quoted message'\n" + 
	"\n" +
	"6    \n" +
	"7                     \\\n        A message.          \n" +
	"8                             Another message.          \n";
      
      StringReader reader = new StringReader(string);
      MsgFileParser parser = new MsgFileParser(reader);
            
      parser.parse();

      MessageCountingVisitor counter = new MessageCountingVisitor();
      parser.walkParseTree(counter, "");

      MessageArrayFactoryVisitor factory =
	new MessageArrayFactoryVisitor(counter.getMessageCount(),"WS");
      parser.walkParseTree(factory, "");

      messages = factory.generateMessageArray();
    }
    catch(Exception ex)
    {
      fail("Parse of the test file failed!" + ex);
    }
  }

  public void tearDown()
  {
    messages = null;
  }


  //  Create a suite to make this bunch of tests easier to add
  //  to the main test suite.
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    
    suite.addTest( new WhiteSpaceHandlingTest("testLeadAndTrailingSpace"));
    suite.addTest( new WhiteSpaceHandlingTest("testLeadAndTrailingWhiteSpace"));
    suite.addTest( new WhiteSpaceHandlingTest("testMultilineLeadAndTrailing"));
    suite.addTest( new WhiteSpaceHandlingTest("testMultilineDanglingLine"));
    suite.addTest( new WhiteSpaceHandlingTest("testSimpleWhiteSpaceQuoted"));
    suite.addTest( new WhiteSpaceHandlingTest("testSimpleLeadingSpaceQuoted"));
    suite.addTest( new WhiteSpaceHandlingTest("testSimpleTrailingSpaceQuoted"));
    suite.addTest( new WhiteSpaceHandlingTest("testQuotedMultiline"));
    suite.addTest( new WhiteSpaceHandlingTest("testMisquoted"));
    suite.addTest( new WhiteSpaceHandlingTest("testWhitespaceOnly"));
    suite.addTest( new WhiteSpaceHandlingTest("testLotsOfWhiteSpace"));
    suite.addTest( new WhiteSpaceHandlingTest("testLotsOfWhiteSpace2"));
 
    return suite;
  }

  //  The tests proper.
  /**
   *  Test to check the scenario of a simple, unquoted, single line message
   *  with leading and trailing spaces.
   */
  public void testLeadAndTrailingSpace()
  {
    assertEquals("   This is a message with three leading and three trailing spaces.   ", messages[0][2]);
  }

  /**
   *  Test to check the scenario of a simple, unquoted, single line message
   *  with leading and trailing tabs and spaces
   */
  public void testLeadAndTrailingWhiteSpace()
  {
    assertEquals("\tThis message has a leading tab \t ", messages[1][2]);
  }
  
  /**
   *  Test to check an unquoted multiline message with leading and trailing
   *  whitespace.
   */
  public void testMultilineLeadAndTrailing()
  {
    assertEquals(" \t This is a multiline \\\nmessage. It also has \\\nding abd trailing white space. \t", messages[2][2]);    
  }

  /**
   *  Test to see what happens when a multiline message has a trailing line
   *  that contains only whitespace.
   */
  public void testMultilineDanglingLine()
  {
     assertEquals("   Hmmm. \\\n\t ", messages[3][2]);   
  }

  /**
   *  Test to check the scenario of a simple, quoted, single line message
   *  with leading and trailing whitespace.
   */
  public void testSimpleWhiteSpaceQuoted()
  {
     assertEquals("\t \"A single line message in quotes\"   ", messages[4][2]);   
  }

  /**
   *  Test to check the scenario of a simple, quoted, single line message
   *  with leading spaces.
   */
  public void testSimpleLeadingSpaceQuoted()
  {
     assertEquals("   \"This message has leading spaces only\"", messages[5][2]);   
  }
  
  /**
   *  Test to check the scenario of a simple, quoted, single line message
   *  with trailing spaces.
   */
  public void testSimpleTrailingSpaceQuoted()
  {
     assertEquals("\"This message has a leading tab and trailing whitespace\"  \t ", messages[6][2]);   
  }

  /**
   *  Test to check the behaviour when a multiline message is quoted and
   *  has leading and trailing whitespace.
   */
  public void testQuotedMultiline()
  {
     assertEquals(" \"This is a multiline \\\nmessage, in \\\nquotes.\"  \t ", messages[7][2]);   
  }

  /**
   *  Test to check the behaviour when a misquoted message.
   */
  public void testMisquoted()
  {
     assertEquals("'An incorrectly quoted message'", messages[8][2]);   
  }
 
  /**
   *  Test to check the behaviour when a message consists only of whitespace.
   */
  public void testWhitespaceOnly()
  {
     assertEquals("   ", messages[9][2]);   
  }

  /**
   *  Test to check the behaviour when a message contains lots of whitespace.
   */
  public void testLotsOfWhiteSpace()
  {
     assertEquals("                    \\\n        A message.          ", messages[10][2]);   
  }

  /**
   *  Test to check the behaviour when a message contains lots of whitespace.
   */
  public void testLotsOfWhiteSpace2()
  {
     assertEquals("                            Another message.          ", messages[11][2]);   
  }
}


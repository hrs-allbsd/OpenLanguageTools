
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

import junit.framework.*;
import java.io.StringReader;

public class CommentNSlashTest 
extends TestCase
{
  StringReader reader;

  public CommentNSlashTest(String name)
  {
    super(name);
  }
  
  public void setUp()
  {
    String string =   "$quote \"\n" + 
      "$set 1\n" + 
      "$ This is a comment\n" + 
      "1 \"A message\"\n" + 
      "\n" + 
      "$ The message above was sedate. There were not\n" + 
      "$ extra features \\\n" + 
      "2 \"This should be OK\"\n" + 
      "3 \"Foo bar\"\n" + 
      "\n";   
    reader = new StringReader(string);
  }

  public void testCommentParses()
  {
    try
    {
      MsgFileParser parser = new MsgFileParser(reader);
      parser.parse();
    }
    catch(Exception ex)
    {
      fail("The file did not parse!");
    }
  }

  public void testSecondMessageSurvives()
  {
    try
    {
      MsgFileParser parser = new MsgFileParser(reader);
      parser.parse();

      //  Count the messages
      MessageCountingVisitor countVisitor = new MessageCountingVisitor();
      parser.walkParseTree(countVisitor,"");
      
      //  Extract the messages to the String array
      MessageArrayFactoryVisitor factoryVisitor
	= new MessageArrayFactoryVisitor(countVisitor.getMessageCount(), "catalog");
      parser.walkParseTree(factoryVisitor,"");
      
      String[][] stringArray = factoryVisitor.generateMessageArray();

      assertEquals("This should be OK", stringArray[1][2]);
     
    }
    catch(Exception ex)
    {
      fail("The file did not parse!");
    }    
  }

}

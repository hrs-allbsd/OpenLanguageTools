
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

import junit.framework.*;
import java.io.StringReader;

public class DetailedParserTest 
extends TestCase
implements MsgFileParserTreeConstants
{
  private SimpleNode[] skiplist;

  public DetailedParserTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    //  Put a sample .msg file in a string
    String string = 
      /* START TEST FILE */
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
      "26 \"\\t*** Error in key id\\n\"\n" +
      "27 \"\\t*** Mulitline message to\\n\\\n" +
      "test the quoting code and the\\\n" +
      "multiline handling of the parser in general.\"\n";
    /* END TEST FILE */
    StringReader reader = new StringReader(string);
    MsgFileParser parser = new MsgFileParser(reader);
    try
    {
      parser.parse();      
      SkipListFactoryVisitor visitor = new SkipListFactoryVisitor();
      parser.walkParseTree(visitor, "");
      skiplist = visitor.getSkipList();
    }
    catch(Exception ex)
    {
      fail("Setup failed");
      ex.printStackTrace();
    }

  }

  public void testSetsFound()
  {
    //  First Set directive
    assertEquals("Node 14 is not a set number.",
		 JJTSET_NUMBER, skiplist[13].id);
    assertEquals("First set number in test MSG file is not '1'",
		 "1", skiplist[13].getNodeData());

    //  Second Set directive
    assertEquals("Node 42 is not a set number.", 
		 JJTSET_NUMBER, skiplist[41].id);
    assertEquals("Second set number in test MSG file is not '11'",
		 "11", skiplist[41].getNodeData());

  }

  public void testQuoteDirective()
  {
    //  Test first quote and its char exists
    assertEquals("Node 3 is not a quote directive.",
		 JJTQUOTE_DIRECTIVE, skiplist[2].id);
    assertEquals("The quote character is not \"!",
		 "\"", skiplist[5].getNodeData());

    //  Test second quote and assume the 
    assertEquals("Node 368 is not a quote directive.",
		 JJTQUOTE_DIRECTIVE, skiplist[367].id);
    assertTrue("The parser incorrectly detected a quote character",
		 skiplist[370].id != JJTDIRECTIVE_CHAR);
  }

  public void testQuoteStripping()
  {
    assertEquals("Error in quote stripping postprocessing at node 88:",
   		 "SKIP Crypt algorithms (version 1):\\n", skiplist[87].getNodeData());
    assertEquals("Error in quote stripping postprocessing at node  352:",
		 "exclude", skiplist[351].getNodeData());
    assertEquals("Error in quote stripping postprocessing at node  399:",
		 "\" tunnel=%s\"", skiplist[398].getNodeData());
  }

  public void testMultiLineHandling()
  {
    assertEquals("Node 572 is not of type SlashNewline",
		 JJTSLASHNEWLINE, skiplist[571].id);
    assertEquals("Node 586 is not of type SlashNewline",
		 JJTSLASHNEWLINE, skiplist[585].id);
    assertEquals("Value for message 27 is incorrect",
		 "\"\\t*** Mulitline message to\\n\\\ntest the quoting code and the\\\nmultiline handling of the parser in general.\"", skiplist[557].getNodeData());

  }

  

}

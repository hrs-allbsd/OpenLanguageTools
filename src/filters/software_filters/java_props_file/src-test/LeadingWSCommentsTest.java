
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser;

import junit.framework.*;
import java.io.*;
import java.util.Hashtable;

public class LeadingWSCommentsTest
extends TestCase
{
  private StringReader m_reader;  

  public LeadingWSCommentsTest(String name)
  {
    super(name);
    m_reader = null;
  }

  public void setUp()
  {
    //  Create test string and generated a string reader
    String strFile = "\t# A comment with a lead in tab\n"
      + "\tkey.one=A multi line message.\\\n  This is the second line\n\n"
      + "\t#@LC@ An indented LC Comment\n" 
      + "\tkey.two=Message two.\n"
      + "\t#@TMC@ A TMC Comment\n"
      + "key.three=Yet another message\n"
      + "\t# A multi-line indented comment\n"
      + "\t# More of the multi-line indented comment\n"
      + "key.four=Still more messages\n";

    m_reader = new StringReader(strFile);   
  }


  public void testLeadingWhiteSpaceHandling()
  {
    if(m_reader != null)
    {
      try
      {
	//  Create the parser
	PropsFileParser parser = new PropsFileParser(m_reader);
	parser.parse(); 
	
	CommentHarvestingVisitor visitor = new CommentHarvestingVisitor();

	parser.walkParseTree(visitor,"");

	String[] array = visitor.getHarvestedComments();

	//  Do the actual tests.
	assertEquals("\t# A comment with a lead in tab\n",array[0]);
	assertEquals("\t#@LC@ An indented LC Comment\n",array[1]);
	assertEquals("\t#@TMC@ A TMC Comment\n",array[2]);
	assertEquals("\t# A multi-line indented comment\n\t# More of the multi-line indented comment\n",array[3]);

      }
      catch(Exception ex)
      {
	fail( ex.toString() );
      }
    }
    else
    {
      fail("ERROR: the test file didn't initialize.");
    } 
  }
}

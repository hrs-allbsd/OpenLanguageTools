
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

import junit.framework.*;
import junit.extensions.*;
import java.io.StringReader;

public class CommentTest
extends ExceptionTestCase
{
  StringReader reader;

  public CommentTest(String name, java.lang.Class exception)
  {
    super(name, exception);
  }

  public void setUp()
  {
    //  Put a sample .msg file in a string
    String string =   "$quote \"\n" + 
      "$set 2\n" + 
      "1\t\"A message\"\n" + 
      "2\t\"Another message\"\n" + 
      "$-- This is a bad comment --\n" + 
      "3\t\"A third message\"\n" + 
      "\n";
    reader = new StringReader(string);
  }  

  public void testCommentHaltsParse()
    throws Exception, ParseException
  {
    MsgFileParser parser = new MsgFileParser(reader);
    parser.parse();
  }
}

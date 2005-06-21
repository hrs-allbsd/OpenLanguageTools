
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser.unittest;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.TranslatableTokenCell;
import org.jvnet.olt.parsers.PropsFileParser.*;
import java.io.*;

public class DispTest2
{
  public static void main(String[] argv)
  {
    try
    {
      if(argv.length != 1)
      {
	throw new IllegalArgumentException("The wrong number of parameters was passed to the program!");
      }
      File file = new File(argv[0]);
      FileInputStream inStream = new FileInputStream(file);

      InputStreamReader reader = new InputStreamReader(inStream,System.getProperty("file.encoding"));
 
      PropsFileParser parser = new PropsFileParser(reader);
      parser.parse();

      PrintDisplayingVisitor visitor = new PrintDisplayingVisitor();
      parser.walkParseTree(visitor, null);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}

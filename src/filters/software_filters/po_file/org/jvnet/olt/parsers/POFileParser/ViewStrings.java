
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.POFileParser;

import java.io.*;

public class ViewStrings
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
      System.out.println(""+System.getProperty("file.encoding"));
      System.out.println(reader.getEncoding());
      System.out.println("");
 
      POFileParser parser = new  POFileParser(reader);
      //ResBundleParser parser = new ResBundleParser(System.in);
      parser.parse();
      StringViewingVisitor stringView = new StringViewingVisitor();
      parser.walkParseTree(stringView, null);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }


}

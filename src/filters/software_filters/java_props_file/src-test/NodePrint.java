
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser;

import java.io.*;

public class NodePrint
{
  public static void main(String[] argv)
  {

    try
    {
      //  Get file name from arguments
      if(argv.length != 1)
      {
	throw new IllegalArgumentException("The wrong number of parameters was passed to the program!");
      }
      
      //  Open file
      File file = new File(argv[0]);
      FileInputStream inStream = new FileInputStream(file);
      
      InputStreamReader reader = new InputStreamReader(inStream,System.getProperty("file.encoding"));
      
      //  Create parser object
      PropsFileParser parser = new PropsFileParser( reader );
      parser.parse();
      
      //  Create a Writer
      OutputStreamWriter oswriter = new OutputStreamWriter( System.out );
      BufferedWriter writerOut = new BufferedWriter(oswriter);
      
      //  Create the Node printing visitor
      NodeTypePrintingVisitor visitor = new NodeTypePrintingVisitor(writerOut);
    
      //  Walk the parse tree
      parser.walkParseTree(visitor,"");
      writerOut.flush();
      writerOut.close();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}

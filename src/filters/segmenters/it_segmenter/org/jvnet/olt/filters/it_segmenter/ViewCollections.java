
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.it_segmenter;

import java.io.*;
import java.util.*;

public class ViewCollections{

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
 
      Segmenter_it parser = new  Segmenter_it(reader);
      parser.parse();
      SegmentCollectionFactoryVisitor segmentVisit = new SegmentCollectionFactoryVisitor();
      parser.walkParseTree(segmentVisit, null);
      
      Collection mycoll = segmentVisit.getCollection();
      System.out.println ("Collection is " + mycoll.size() + " elements");

      
      Iterator it = mycoll.iterator();
      
      String s="";

      while (it.hasNext()){
	  s= (String)it.next();
	  System.out.println ("Segment! :"+s);
	  
      }
      
      
      
      
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }


}

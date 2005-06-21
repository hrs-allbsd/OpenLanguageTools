
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.POFileParser.unittest;


import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.TranslatableTokenCell;
import org.jvnet.olt.parsers.POFileParser.*;
import java.io.*;
import java.util.Hashtable;

public class DisplayingTest
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
 
      POFileParser parser = new POFileParser(reader);
      parser.parse();

      DisplayingTokenArrayFactoryVisitor visitor = new DisplayingTokenArrayFactoryVisitor();
      parser.walkParseTree(visitor, null);
      TokenCell[] array = visitor.getDisplayingTokens();

      Hashtable hash = new Hashtable();
      hash.put("1","Message");
      hash.put("2","Key");
      hash.put("3","Comment");
      hash.put("4","Domain");
      hash.put("5","Domain key word");
      hash.put("6","Formating");
      hash.put("7","Marker");


      System.out.println("Number of cells: " +  array.length);
      System.out.println("");
      for(int i = 0; i < array.length; i++)
      {
	System.out.print("Type: " + (String) hash.get( "" + array[i].getType() ) + "\n");
	System.out.print("Text: " + array[i].getText() + "\n");
	System.out.print("Key: " + array[i].getKey() + "\n");
	System.out.print("Comment: " + array[i].getComment() + "\n");
	System.out.print("Translatable?: " + ( array[i].isTranslatable() ? "Yes" : "No" ) + "\n");
	System.out.println("");
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }




}

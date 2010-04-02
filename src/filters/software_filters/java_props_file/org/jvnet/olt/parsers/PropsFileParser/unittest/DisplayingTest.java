/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/

/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser.unittest;


import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import org.jvnet.olt.parsers.PropsFileParser.*;
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
 
      PropsFileParser parser = new PropsFileParser(reader);
      parser.parse();

      DisplayingTokenArrayFactoryVisitor visitor = new DisplayingTokenArrayFactoryVisitor();
      parser.walkParseTree(visitor, null);
      TokenCell[] array = visitor.getDisplayingTokens();

      Hashtable hash = new Hashtable();
      hash.put("1","Message");
      hash.put("2","Key");
      hash.put("3","Comment");
      hash.put("4","Domain");
      hash.put("5","Formating");


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

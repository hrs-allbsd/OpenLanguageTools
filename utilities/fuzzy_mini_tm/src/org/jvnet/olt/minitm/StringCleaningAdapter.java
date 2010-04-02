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

package org.jvnet.olt.minitm;

import org.jvnet.olt.parsers.sgmltokens.*;
import java.util.List;
import java.util.LinkedList;
import java.io.StringReader;
import java.io.IOException;

/**
 *  This class is an Adapter class, designed to hide the goings on of
 *  the SGML processing code from the rest of the MiniTm. It provides
 *  an interface to retrieve the plain text string and a list of all
 *  the markup that appeared in the string.
 */
public class StringCleaningAdapter
{
  private List markupList;
  private String plainTextString;

  /**
   *  The constructor is responsible for building the object. By the 
   *  time it is finished, the string should have been parsed and 
   *  separated out into plaintext string and an ordered list of markup.
   *  These objects can then be retrieved by the calling objects via the 
   *  rest of the inteface.
   *  @param string The string to be "cleaned".
   *  @throws MiniTMException
   */
  public StringCleaningAdapter(String string)
    throws MiniTMException
  {
    try
    {
      //  Create string reader
      StringReader reader = new StringReader(string);
      //System.out.println("stringcleaningAdapter.constructor.reader = "+ string);
      //  Create parser
      SimpleSGMLTokenizer parser = new SimpleSGMLTokenizer(reader);

      //  Parse string
      parser.parse();

      //  Create visitor
      PlainTextExtractingVisitor visitor = new PlainTextExtractingVisitor();
      parser.walkParseTree(visitor, "");

      //  Get cleaned string from the visitor
      plainTextString = visitor.getPlainText();
//      System.out.println("stringCleaningAdapter.constructor.PlaintextString = "+ plainTextString);
      //  Get markup list from the vistor
      markupList = visitor.getMarkup();
    }
    catch(ParseException parseEx)
    {
      throw new MiniTMException("The string did not parse correctly\n" + 
				parseEx.getMessage());
    }
    catch(IOException ioEx)
    {
      throw new MiniTMException("For some reason an IO error occurred.\n" + 
				ioEx.getMessage());
    }
    catch(Exception ex)
    {
      throw new MiniTMException(ex.getMessage());
    }
  }

  /**
   *  This method retrieves the markup list.
   *  @returns List
   */
  public List getMarkup()
  {
    return markupList;
  }

  /**
   *  This method returns a plaintext representation of the string passed
   *  to the constructor. All markup is simply removed.
   *  @returns String
   */
  public String getPlainText()
  {
    return plainTextString;
  }
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.brokensgml;

import java.util.HashMap;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.FormatExtractor;
import org.jvnet.olt.format.GlobalVariableManager;

/** 
 * A class to extract formatting from an non-Sgml, but sgml-like document fragment.
 */
public class BrokenSgmlFormatExtractor
implements FormatExtractor
{
    /** 
     * This method extracts formatting from SGML document fragments and
     * returns them in a HashMap.
     * @param string A string containing the SGML document fragment.
     * @throws InvalidFormattingException Thrown if the string does not parse as SGML.
     * @return A HashMap of FormatItem objects.
     */    
  public HashMap getFormatting(String string, GlobalVariableManager gvm)
    throws InvalidFormattingException
  {
    HashMap formats = new HashMap();

    //  Parse the string, extract tags, marked sections, etc., and
    //  classify them.
    try
    {
      StringReader reader = new StringReader(string);
      NonConformantSgmlDocFragmentParser parser = new NonConformantSgmlDocFragmentParser(reader);
      parser.parse();

      BrokenSgmlFormatExtractionVisitor visitor = new BrokenSgmlFormatExtractionVisitor(formats);
      parser.walkParseTree(visitor, null);      
    }
    catch(Exception exParse)
    {
      throw new InvalidFormattingException(exParse.getMessage());
    }
    return formats;
  }
}

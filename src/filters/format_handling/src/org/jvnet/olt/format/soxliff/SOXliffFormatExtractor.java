
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.soxliff;

import java.util.HashMap;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.FormatExtractor;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.brokensgml.BrokenSgmlFormatExtractionVisitor;

/** 
 * A class to extract formatting from a staroffice xliff document
 */
public class SOXliffFormatExtractor implements FormatExtractor {

  /** 
   * This method extracts formatting from StarOffice xliff and returns them in a HashMap.
   * @param string A string containing the SGML document fragment.
   * @throws InvalidFormattingException Thrown if the string does not parse as SGML.
   * @return A HashMap of FormatItem objects.
   */    
  public HashMap getFormatting(String string, GlobalVariableManager gvm) throws InvalidFormattingException {

    HashMap formats = new HashMap();

    try {
      StringReader reader = new StringReader(string);
      NonConformantSgmlDocFragmentParser parser = new NonConformantSgmlDocFragmentParser(reader);
      parser.parse();

      BrokenSgmlFormatExtractionVisitor visitor = new BrokenSgmlFormatExtractionVisitor(formats);
      parser.walkParseTree(visitor, null);      
    } catch(Throwable exParse) {
      // return empty map if there is broken formatting
      System.err.println("SOXliffFormatExtractor: broken formatting: " + exParse.getMessage());
    }
    return formats;
  }
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.xml;

import java.util.HashMap;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParserVisitor;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParser;
import org.jvnet.olt.parsers.XmlDocFragmentParser.ParseException;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.FormatExtractor;
import org.jvnet.olt.format.GlobalVariableManager;

/** 
 * A class to extract formatting from an XML document fragment.
 */
public class XmlFormatExtractor
implements FormatExtractor
{
    /** 
     * This method extracts formatting from SGML document fragments and
     * returns them in a HashMap.
     * @param string A string containing the SGML document fragment.
     * @param gvm A global variable manager to be used to resolve global variables.
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
      XmlDocFragmentParser parser = new XmlDocFragmentParser(reader);
      parser.parse();

      XmlFormatExtractionVisitor visitor = new XmlFormatExtractionVisitor(formats, gvm);
      parser.walkParseTree(visitor, null);      
    }
    catch(Exception exParse)
    {
      throw new InvalidFormattingException(exParse.getMessage());
    }
    return formats;
  }
}

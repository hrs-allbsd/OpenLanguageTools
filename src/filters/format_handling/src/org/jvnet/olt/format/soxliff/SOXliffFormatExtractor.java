
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.soxliff;

import java.util.HashMap;
import java.util.Map;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.FormatExtractor;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.brokensgml.BrokenSgmlFormatExtractionVisitor;
import org.jvnet.olt.format.sgml.SgmlFormatExtractor;

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
    
    /**
     * Convert formatted segment to normalized form that can be used by string comparer algorithm.
     *
     * The tags are converted to unique charactares with the formatMap. The text is converted to character 'X'.
     *
     * For example the formatted segment: "This is a &lt;b&gt;test&lt;b&gt;." is converted to "XAXB"
     *
     * @param formattedSegment the segment with text and formatting
     *
     * @param formatMap a map that contains mapping from a tag to unique character.
     *        For example: '&lt;b&gt;' -- 'A'.
     *        The tags in formattedSegmented are replace with the unique characters. If the tag is currently not
     *        in the map. A new entry with additionally unique character is added.
     *
     * @param gvm the global variable manager that is used by specific file type parser.
     *
     * @return normalized String.
     *
     * @return InvalidFormattingException if the formatting cannot be parsed with the parser
     */
    public String getNormalizedForm(String formattedSegment,Map formatMap, GlobalVariableManager gvm) throws InvalidFormattingException {
        SgmlFormatExtractor sgmlExtractor = new SgmlFormatExtractor();
        // using sgml format extractor now
        return sgmlExtractor.getNormalizedForm(formattedSegment,formatMap,gvm);
    }
    
}

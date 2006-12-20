
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.plaintext;

import org.jvnet.olt.format.FormatExtractor;
import org.jvnet.olt.format.InvalidFormattingException;
import java.util.HashMap;
import java.util.Map;
import org.jvnet.olt.format.GlobalVariableManager;

public class PlainTextFormatExtractor implements FormatExtractor
{
  public HashMap getFormatting(String string, GlobalVariableManager gvm) throws InvalidFormattingException
  {
    HashMap formats = new HashMap();

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
        // no formatting supported inside plain text format
        return formattedSegment;
    }
}

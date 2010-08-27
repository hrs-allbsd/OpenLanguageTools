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

/*
 * MessageFormatExtractor.java
 *
 * Created on October 23, 2003, 8:54 pm
 */

package org.jvnet.olt.format.messageformat;

import java.util.HashMap;
import java.util.Map;
import java.io.StringWriter;
import java.io.IOException;
import java.io.StringReader;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;
import org.jvnet.olt.parsers.MessageFormatParser.*;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.FormatItem;
import org.jvnet.olt.format.AbstractFormatExtractionVisitor;

/**
 * This class is designed to extract the formatting from text that contains
 * java.text.MessageFormat style formatting.
 *
 * @author  timf
 */
public class MessageFormatExtractor implements org.jvnet.olt.format.FormatExtractor {
    
    /** Creates a new instance of MessageFormatExtractor */
    public MessageFormatExtractor() {
    }
    
    /**
     * This method returns a list of the formatting codes that were found in the
     *  string presented for formatting.
     */
    public java.util.HashMap getFormatting(String string, org.jvnet.olt.format.GlobalVariableManager gvm) throws org.jvnet.olt.format.InvalidFormattingException {
        HashMap formats = new HashMap();
        try {
            StringReader reader = new StringReader(string);
            MessageFormatParser parser = new MessageFormatParser(reader);
            parser.parse();
            MessageFormatExtractionVisitor visitor = new MessageFormatExtractionVisitor();
            parser.walkParseTree(visitor, null);
            formats = visitor.getFormatItems();
        } catch (Throwable e){
            throw new InvalidFormattingException("Invalid MessageFormat sequence while trying to extract formatting from " +string);
            
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
        String normalizedForm = "";
        
        try {
            StringReader reader = new StringReader(formattedSegment);
            MessageFormatParser parser = new MessageFormatParser(reader);
            parser.parse();
            
            FormatExtractionVisitor visitor = new FormatExtractionVisitor(formatMap);
            parser.walkParseTree(visitor, null);
            
            normalizedForm = visitor.getNormalizedForm();
        } catch(Exception exParse) {
            exParse.printStackTrace();
            throw new InvalidFormattingException(exParse.getMessage());
        }
        
        return normalizedForm;
    }
    
    /** This class defines a visitor that traverses the parse tree of a
     *  string containing java.text.MessageFormat style formatting, and
     *  returns a map of formatting based on the number of occurrences
     *  of each formatting chunk ("{0}", "{1,date}", "{0,number,integer}"
     *  would be three such chunks) - the key is the chunk of formatting,
     *  the value is the number of times it
     *  appears.
     *
     * MT: The class was constructed from the SgmlFormatExtractionVisitor. I have changed
     *     the main format comparer logic. The rest of the code was copied.
     */
    public class FormatExtractionVisitor extends AbstractFormatExtractionVisitor implements MessageFormatParserVisitor, MessageFormatParserTreeConstants {
                
        /** Constructs a new MessageFormatExtractingVisitor.
         */
        public FormatExtractionVisitor(Map formatMap) {
            super(formatMap);
        }
        
        /** This method visits a node in the parse tree of an printf document
         * fragment and processes it to see if it represents formatting. If
         * it does represent formatting then it is added to a collection.
         * @param node The node being visited
         * @param data Ancillary data to aid in the processing task.
         * @return A modified version (perhaps) of the data object.
         */
        public Object visit(SimpleNode node, Object data) {
            int type = node.getType();
            String nodeText = node.getNodeData();
            handleNodes(type, nodeText);
            return data;
        }
        
        
        /**
         * @param type
         * @param nodeText
         */
        protected void handleNodes(int type, String nodeText) {
            switch (type){
                case JJTCONV:
                    addFormat(nodeText);
                    break;
                case JJTWORDS:
                case JJTWS:
                    addPCData();
                    break;
                    // possibly do stuff for other types of formatting,
                    // if we decide to add them...
            }
            
        }
        
    }
    
    
}

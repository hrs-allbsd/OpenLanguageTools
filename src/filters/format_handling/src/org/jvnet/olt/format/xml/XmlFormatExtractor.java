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

package org.jvnet.olt.format.xml;

import java.util.HashMap;
import java.util.Map;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParserVisitor;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParser;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParserTreeConstants;
import org.jvnet.olt.parsers.XmlDocFragmentParser.ParseException;
import org.jvnet.olt.parsers.XmlDocFragmentParser.SimpleNode;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.FormatExtractor;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;
import org.jvnet.olt.format.FormatItem;
import org.jvnet.olt.format.AbstractFormatExtractionVisitor;

/**
 * A class to extract formatting from an XML document fragment.
 */
public class XmlFormatExtractor
        implements FormatExtractor {
    /**
     * This method extracts formatting from SGML document fragments and
     * returns them in a HashMap.
     * @param string A string containing the SGML document fragment.
     * @param gvm A global variable manager to be used to resolve global variables.
     * @throws InvalidFormattingException Thrown if the string does not parse as SGML.
     * @return A HashMap of FormatItem objects.
     */
    public HashMap getFormatting(String string, GlobalVariableManager gvm)
    throws InvalidFormattingException {
        HashMap formats = new HashMap();
        
        //  Parse the string, extract tags, marked sections, etc., and
        //  classify them.
        try {
            StringReader reader = new StringReader(string);
            XmlDocFragmentParser parser = new XmlDocFragmentParser(reader);
            parser.parse();
            
            XmlFormatExtractionVisitor visitor = new XmlFormatExtractionVisitor(formats, gvm);
            parser.walkParseTree(visitor, null);
        } catch(Exception exParse) {
            throw new InvalidFormattingException(exParse.getMessage());
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
        return null;
    }
    
    /** This class defines a visitor that traverses the parse tree of an
     * SGML document fragment and extracts a list of the formatting
     * information from it.
     */
    public class XmlFormatExtractionVisitor extends AbstractFormatExtractionVisitor
            implements XmlDocFragmentParserVisitor, XmlDocFragmentParserTreeConstants {
        
        private boolean m_boolIgnoredMarkedSect;
        private int     m_markedSectDepth;
        private GlobalVariableManager m_gvm;
        
        private StringBuffer m_markedSectBuffer;
        
        /** Constructs a new SgmlFormatExtractingVisitor.
         * @param formats A HashMap to store the format information in. The hash
         * is keyed to the text of the format item. If multiple
         * occurrences of a particular format item are found, then
         * a reference counter in the FormatItem object is
         * incremented.
         * @param gvm A global variable manager, which is to be used to
         * resolve parameter entities. This is used to determine
         * if the contents of a conditional section should be
         * considered as formatting, i.e., if the section is
         * IGNOREd.
         */
        public XmlFormatExtractionVisitor(Map formatMap,
                GlobalVariableManager gvm) {
            super(formatMap);
            m_gvm = gvm;
            m_boolIgnoredMarkedSect = false;
            m_markedSectDepth = 0;
            m_markedSectBuffer = new StringBuffer();
        }
        
        /** This method visits a node in the parse tree of an XML document
         * fragment and processes it to see if it represents formatting. If
         * it does represent formatting then it is added to a collection.
         * @param node The node being visited
         * @param data Ancillary data to aid in the processing task.
         * @return A modified version (perhaps) of the data object.
         */
        public Object visit(SimpleNode node, Object data) {
            int type = node.getType();
            String nodeText = node.getNodeData();
            handleNodes(type, nodeText, node);
            return data;
        }
        
        /**
         * @param type
         * @param nodeText
         * @param node  */
        protected void handleNodes(int type, String nodeText, SimpleNode node) {
            switch(type) {
                case JJTOPEN_TAG:
                    if(node.hasAttribute()) {
                        // add bigger format penalty for tags with attributes
                        addFormat(nodeText,true);
                    } else {
                        addFormat(nodeText.toUpperCase());
                    }
                    break;
                case JJTCLOSE_TAG:
                case JJTCOMMENT:
                case JJTPROCESSING_INST:
                    addFormat(nodeText.toUpperCase());
                    break;
                case JJTCDATA:
                case JJTDOCTYPE:
                    // add bigger format penalty for tags with attributes
                    addFormat(nodeText,true);
                    break;
                case JJTPCDATA:
                    addPCData();
                    break;
                default:
                    break;
            }
        }
        
    }
    
}

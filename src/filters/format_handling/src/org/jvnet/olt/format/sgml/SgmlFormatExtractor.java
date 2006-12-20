
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.sgml;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.FormatExtractor;
import org.jvnet.olt.format.FormatItem;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;
import org.jvnet.olt.format.AbstractFormatExtractionVisitor;

/**
 * A class to extract formatting from an SGML document fragment.
 */
public class SgmlFormatExtractor
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
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader);
            parser.parse();
            
            SgmlFormatExtractionVisitor visitor = new SgmlFormatExtractionVisitor(formats, gvm);
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
     * @param formatMap a map that contains mapping from a formatting to single unique character.
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
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader);
            parser.parse();
            
            FormatExtractionVisitor visitor = new FormatExtractionVisitor(formatMap, gvm);
            parser.walkParseTree(visitor, null);
            
            normalizedForm = visitor.getNormalizedForm();
        } catch(Exception exParse) {
            exParse.printStackTrace();
            throw new InvalidFormattingException(exParse.getMessage());
        }
        
        return normalizedForm;
    }
    
    
    /** 
     * This class defines a visitor that traverses the parse tree of an
     * SGML document fragment and extracts a list of the formatting
     * information from it.
     *
     * MT: The class was constructed from the SgmlFormatExtractionVisitor. I have changed
     *     the main format comparer logic. The rest of the code was copied.
     */
    class FormatExtractionVisitor extends AbstractFormatExtractionVisitor
            implements SgmlDocFragmentParserVisitor, SgmlDocFragmentParserTreeConstants {
        
        
        private boolean m_boolIgnoredMarkedSect;
        private int     m_markedSectDepth;
        private GlobalVariableManager m_gvm;
        private StringBuffer m_markedSectBuffer;
        
        /**
         * Constructs a new FormatExtractingVisitor.
         *
         * @param formatMap contains mapping from a tag to unique character
         *               the normalized form is constructed by replacing
         *               the tags with charaters from the map.
         *               If some tag is not in the map new record is added
         *
         * @param gvm A global variable manager, which is to be used to
         *            resolve parameter entities. This is used to determine
         *            if the contents of a conditional section should be
         *            considered as formatting, i.e., if the section is
         *            IGNOREd.
         */
        public FormatExtractionVisitor(Map formatMap,
                GlobalVariableManager gvm) {
            
            super(formatMap);
            
            m_gvm = gvm;
            m_boolIgnoredMarkedSect = false;
            m_markedSectDepth = 0;
            
            m_markedSectBuffer = new StringBuffer();
        }
        
        /** This method visits a node in the parse tree of an SGML document
         * fragment and processes it to see if it represents formatting. If
         * it does represent formatting then it is added to a collection. In
         * the case of conditional sections, IGNOREd sections span a number
         * of nodes in the tree, and this method is responsible for
         * aggregating the data that they contain.
         * @param node The node being visited
         * @param data Ancillary data to aid in the processing task.
         * @return A modified version (perhaps) of the data object.
         */
        public Object visit(SimpleNode node, Object data) {
            int type = node.getType();
            String nodeText = node.getNodeData();
            
            if(m_boolIgnoredMarkedSect) {
                handleIgnoredMarkedSection(type, nodeText);
            } else {
                handleNodes(type, nodeText, node);
            }
            return data;
        }
        
        /**
         * @param type
         * @param nodeText  */
        protected void handleIgnoredMarkedSection(int type, String nodeText) {
            switch(type) {
                case JJTMARKED_SECTION_TAG:
                    m_markedSectDepth++;
                    m_markedSectBuffer.append(nodeText);
                    break;
                case JJTEND_MARKED_SECT:
                    m_markedSectDepth--;
                    m_markedSectBuffer.append(nodeText);
                    if(m_markedSectDepth == 0) {
                        m_boolIgnoredMarkedSect = false;
                        addFormat(m_markedSectBuffer.toString(),true);
                        m_markedSectBuffer = new StringBuffer();
                    }
                    break;
                default:
                    m_markedSectBuffer.append(nodeText);
                    break;
            }
        }
        
        /**
         * @param type
         * @param nodeText
         * @param node  */
        protected void handleNodes(int type, String nodeText, SimpleNode node) {
            switch(type) {
                case JJTOPEN_TAG:
                    if(node.hasAttribute()) {
                        // if we have attributes add large penalty and do not use uppercase
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
                    // large penalty difference for doctype
                    addFormat(nodeText,true);
                    break;
                case JJTMARKED_SECTION_TAG:
                    String flag = node.getMarkedSectFlag();
                    
                    boolean boolEntityMeansIgnore = false;
                    boolEntityMeansIgnore =
                            ( m_gvm.isVariableDefined(flag) &&
                            m_gvm.resolveVariable(flag).equals("IGNORE") );
                    
                    if(flag.equals("IGNORE") || boolEntityMeansIgnore ) {
                        m_boolIgnoredMarkedSect = true;
                        m_markedSectDepth =1;
                        m_markedSectBuffer.append(nodeText);
                    } else {
                        addFormat(nodeText);
                    }
                    break;
                case JJTEND_MARKED_SECT:
                    addFormat(nodeText);
                    break;
                case JJTPCDATA:
                    // do not add mutliple PCDATA characters
                    addPCData();
                    break;
                default:
                    break;
            }
        }
        
    }
    
    
}

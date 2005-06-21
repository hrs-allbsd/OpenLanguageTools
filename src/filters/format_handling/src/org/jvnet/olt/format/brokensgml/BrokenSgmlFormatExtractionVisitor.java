
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.brokensgml;

import java.util.HashMap;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.format.FormatItem;


/** This class defines a visitor that traverses the parse tree of an
 * SGML document fragment and extracts a list of the formatting
 * information from it.
 */
public class BrokenSgmlFormatExtractionVisitor
implements NonConformantSgmlDocFragmentParserVisitor, NonConformantSgmlDocFragmentParserTreeConstants {
    /** This constant defines the penalty score to apply in the case of a
     * difference between large tags.
     */
    public static final int LARGE_TAG_PENALTY = 5;
    
    /** This constant defines the penalty score to apply in the case of a
     * difference between small tags.
     */
    public static final int SMALL_TAG_PENALTY = 2;
    
    private HashMap m_formats;
    private boolean m_boolIgnoredMarkedSect;
    private int     m_markedSectDepth;
    
    private StringBuffer m_markedSectBuffer;
    
    /** Constructs a new BrokenSgmlFormatExtractingVisitor.
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
    public BrokenSgmlFormatExtractionVisitor(HashMap formats){
        m_formats = formats;
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
        }
        else {
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
                    insertFormatIntoMap(m_markedSectBuffer.toString(), LARGE_TAG_PENALTY);
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
                    //  Note: The case folding here is quick and dirty. It is not really
                    //  valid. It folds the values of attributes to upper case as well,
                    //  which is not really what is wanted.
                    insertFormatIntoMap(nodeText.toUpperCase(), LARGE_TAG_PENALTY);
                }
                else {
                    insertFormatIntoMap(nodeText.toUpperCase(), SMALL_TAG_PENALTY);
                }
                break;
            case JJTCLOSE_TAG:
            case JJTCOMMENT:
            case JJTPROCESSING_INST:
                insertFormatIntoMap(nodeText.toUpperCase(), SMALL_TAG_PENALTY);
                break;
            case JJTCDATA:
            case JJTDOCTYPE:
                insertFormatIntoMap(nodeText, LARGE_TAG_PENALTY);
                break;
            case JJTMARKED_SECTION_TAG:
                String flag = node.getMarkedSectFlag();
                
                boolean boolEntityMeansIgnore = false;
                //try {
                    /*boolEntityMeansIgnore =
                    ( m_gvm.isVariableDefined(flag) &&
                    m_gvm.resolveVariable(flag).equals("IGNORE") );*/
                //} catch (GlobalVariableManagerException e){
                //    throw new RuntimeException("Problem while resolving variable "+flag+ " while processing "+
                //    nodeText+" : "+e.getMessage());
                //}
                
                if(flag.equals("IGNORE") || boolEntityMeansIgnore ) {
                    m_boolIgnoredMarkedSect = true;
                    m_markedSectDepth =1;
                    m_markedSectBuffer.append(nodeText);
                }
                else {
                    insertFormatIntoMap(nodeText, SMALL_TAG_PENALTY);
                }
                break;
            case JJTEND_MARKED_SECT:
                insertFormatIntoMap(nodeText, SMALL_TAG_PENALTY);
                break;
            default:
                break;
        }
    }
    
    /** This method allows a client to retrieve the formatting information
     * gathered by the visitor during its treewalk of the SGML document
     * fragmant parse tree.
     *
     * @return A HashMap containing FormatItem objects.
     */
    public HashMap getFormatItems() {
        return m_formats;
    }
    
    private void insertFormatIntoMap(String formatText, int penalty) {
        FormatItem formatItem = null;
        if(m_formats.containsKey(formatText)) {
            formatItem = (FormatItem) m_formats.get(formatText);
            formatItem.iterateOccurrences();
        }
        else {
            formatItem = new FormatItem(formatText, penalty);
            m_formats.put(formatText, formatItem);
        }
    }
}

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
import org.jvnet.olt.parsers.XmlDocFragmentParser.SimpleNode;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParserVisitor;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParserTreeConstants;
import org.jvnet.olt.format.FormatItem;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;

/** This class defines a visitor that traverses the parse tree of an
 * SGML document fragment and extracts a list of the formatting
 * information from it.
 */
public class XmlFormatExtractionVisitor
implements XmlDocFragmentParserVisitor, XmlDocFragmentParserTreeConstants {
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
    public XmlFormatExtractionVisitor(HashMap formats,
    GlobalVariableManager gvm) {
        m_formats = formats;
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

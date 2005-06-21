
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.printf;

import java.util.HashMap;
import org.jvnet.olt.parsers.PrintfParser.*;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParserTreeConstants;
import org.jvnet.olt.format.FormatItem;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;

/** This class defines a visitor that traverses the parse tree of a
 *  string containing printf-style formatting, and returns a map of
 *  formatting based on the number of occurrences of each formatting
 *  chunk ("%s", "%d", "%23f" would be three such chunks) - the key
 *  is the chunk of formatting, the value is the number of times it
 *  appears. It also evaluates the "severity" of different formatting
 *  types. We apply a greater penalty to %d, %s and their type than we
 *  do to \t \b \n.
 */
public class PrintfFormatExtractionVisitor implements PrintfParserVisitor, PrintfParserTreeConstants, SgmlDocFragmentParserTreeConstants {
    
    private HashMap m_formats;
    
    // not sure what the best thing here is for software files.
    /** This constant defines the penalty score to apply in the case of a
     * difference between %s, %d, %f and their kind. Currently set to 3
     */
    public static final int LARGE_PENALTY = 3;
    
    /** This constant defines the penalty score to apply in the case of a
     * difference between formatting such as \n \b \f \t, which aren't as
     * severe as a difference between %s and the like. Currently set to 1
     */
    public static final int SMALL_PENALTY = 1;
            
    
    /** Constructs a new PrintfFormatExtractingVisitor.     
     */
    public PrintfFormatExtractionVisitor() {
        m_formats = new HashMap();
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
     * @param node  */
    protected void handleNodes(int type, String nodeText) {
        switch (type){
            
            case JJTCONV:
            case JJTVARIABLECONV:
                insertFormatIntoMap(nodeText, LARGE_PENALTY);
                break;
            case JJTSLASHESCAPE:
            case JJTOTHERESCAPE:
                insertFormatIntoMap(nodeText, SMALL_PENALTY);
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

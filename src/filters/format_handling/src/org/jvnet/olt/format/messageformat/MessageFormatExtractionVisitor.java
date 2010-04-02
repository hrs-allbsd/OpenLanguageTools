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

package org.jvnet.olt.format.messageformat;

import java.util.HashMap;
import org.jvnet.olt.parsers.MessageFormatParser.*;
import org.jvnet.olt.format.FormatItem;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;

/** This class defines a visitor that traverses the parse tree of a
 *  string containing java.text.MessageFormat style formatting, and
 *  returns a map of formatting based on the number of occurrences
 *  of each formatting chunk ("{0}", "{1,date}", "{0,number,integer}"
 *  would be three such chunks) - the key is the chunk of formatting,
 *  the value is the number of times it
 *  appears.
 */
public class MessageFormatExtractionVisitor implements MessageFormatParserVisitor, MessageFormatParserTreeConstants {
    
    private HashMap m_formats;
    
    // not sure what the best thing here is for software files.
    /** This constant defines the penalty score to apply in the case of a
     * difference between {0}, {0,date}, {0,number,integer} and their kind.
     * Currently set to 4 (which is one higher than the printf parser ... hmmm)
     */
    public static final int LARGE_PENALTY = 4;
    // this code was taken from something that defined large and small penalties
    // for the time being, we're only defining one level of severity : we can add
    // more levels later if necessary.
    
    /** Constructs a new MessageFormatExtractingVisitor.
     */
    public MessageFormatExtractionVisitor() {
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
                insertFormatIntoMap(nodeText, LARGE_PENALTY);
                break;
            // possibly do stuff for other types of formatting,
            // if we decide to add them...
        }
        
    }
    
    /** This method allows a client to retrieve the formatting information
     * gathered by the visitor during its treewalk of the text
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

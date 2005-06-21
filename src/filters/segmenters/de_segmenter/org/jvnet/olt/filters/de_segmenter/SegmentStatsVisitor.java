
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.de_segmenter;
import java.util.*;


public class SegmentStatsVisitor implements Segmenter_deVisitor
{
    
    List wordList;
    List numberList;

    public SegmentStatsVisitor() {
	wordList = new ArrayList();
	numberList = new ArrayList();
    }

    public Object visit(SimpleNode node, Object data)
    {
       	switch (node.getType()){
	case Segmenter_deTreeConstants.JJTNUMBER:
	    numberList.add(node.getNodeData());
	    break;
	case Segmenter_deTreeConstants.JJTWORD:
	    wordList.add(node.getNodeData());
	    break;
        case Segmenter_deTreeConstants.JJTDASHEDWORD:
            wordList.add(node.getNodeData());
            break;

	}
	return data;
    }
    

    public List getWordList(){
	return numberList;
    }

    public List getNumberList(){
	return numberList;
    }
   
    
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.en_segmenter;
import java.util.*;


public class SegmentStatsVisitor implements Segmenter_enVisitor
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
	case Segmenter_enTreeConstants.JJTNUMBER:
	    numberList.add(node.getNodeData());
	    break;
	case Segmenter_enTreeConstants.JJTWORD:
	    wordList.add(node.getNodeData());
	    break;
        case Segmenter_enTreeConstants.JJTDASHEDWORD:
            wordList.add(node.getNodeData());
            break;
	}
	return data;
    }
    

    public List getWordList(){
	return wordList;
    }

    public List getNumberList(){
	return numberList;
    }
   
    
}

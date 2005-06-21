
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.sv_segmenter;
import java.util.*;


public class SegmentStatsVisitor implements Segmenter_svVisitor
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
	case Segmenter_svTreeConstants.JJTNUMBER:
	    numberList.add(node.getNodeData());
	    break;
	case Segmenter_svTreeConstants.JJTWORD:
	    wordList.add(node.getNodeData());
	    break;
        case Segmenter_svTreeConstants.JJTDASHEDWORD:
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

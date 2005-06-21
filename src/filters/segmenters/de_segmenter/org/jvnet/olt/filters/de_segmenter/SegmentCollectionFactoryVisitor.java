
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.de_segmenter;
import java.util.*;


public class SegmentCollectionFactoryVisitor implements Segmenter_deVisitor
{
    private List visitorcoll;
    private Map formattingmap;
    private int segmentcounter=0;
    
    public SegmentCollectionFactoryVisitor() {
    	visitorcoll = new ArrayList();
	formattingmap = new HashMap();
    }

    public Object visit(SimpleNode node, Object data)
    {
       	switch (node.getType()){

	case Segmenter_deTreeConstants.JJTFORMATTING:
	    //do something with formatting info.
	    // System.out.println ("Setting " + segmentcounter + " to contain "+ node.getNodeData());

	    // see if we've got an formatting entry in the map for this segment
	    Integer key = new Integer(segmentcounter);
	    
	    if (formattingmap.containsKey(key)){
		String formatting = (String)formattingmap.get(key);
		formatting = formatting + node.getNodeData();
		formattingmap.put (key, formatting);
	    }
	    else {
	        formattingmap.put (key, node.getNodeData());
	    }
				  
	    break;
	    //case Segmenter_deTreeConstants.JJTSENTSEP:
	    //break;
	    //	case Segmenter_deTreeConstants.JJTFORMATTING:
	    //System.out.println ("Formatting! : "+node.getNodeData());
	    //break;
	case Segmenter_deTreeConstants.JJTSEGMENT:
	    visitorcoll.add(node.getNodeData());
	    segmentcounter++;
	    //System.out.println ("Segment! : "+node.getNodeData());
	    break;
	}
	return data;
    }
    
    public List getCollection(){
	return visitorcoll;
    }

    public Map getFormatting(){
	return formattingmap;
    }
    
}

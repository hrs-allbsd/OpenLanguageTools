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

package org.jvnet.olt.filters.fr_segmenter;
import java.util.*;


public class SegmentCollectionFactoryVisitor implements Segmenter_frVisitor
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

	case Segmenter_frTreeConstants.JJTFORMATTING:
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
	    //case Segmenter_frTreeConstants.JJTSENTSEP:
	    //break;
	    //	case Segmenter_frTreeConstants.JJTFORMATTING:
	    //System.out.println ("Formatting! : "+node.getNodeData());
	    //break;
	case Segmenter_frTreeConstants.JJTSEGMENT:
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

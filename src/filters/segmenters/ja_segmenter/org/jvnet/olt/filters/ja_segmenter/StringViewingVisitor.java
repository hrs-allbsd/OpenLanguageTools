
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.ja_segmenter;

import java.io.StringReader;

public class StringViewingVisitor implements Segmenter_jaVisitor {
    
    public Object visit(SimpleNode node, Object data) {
        //  if (!node.toString().equals("whitespace") && !node.toString().equals("word"))
        System.out.println(node.toString() + " : " + node.getNodeData());
        if (node.toString().equals("sentence")){
            StringReader reader = new StringReader(node.getNodeData());
            try {
                JaSegmentStatsCreator creator = new JaSegmentStatsCreator(reader);
                System.out.println("Word count for that segment is "+creator.getWordList().size());
            } catch (java.io.IOException e){
                System.out.println("Error doing word count for "+node.getNodeData());
            }
        }
        return data;
    }
}

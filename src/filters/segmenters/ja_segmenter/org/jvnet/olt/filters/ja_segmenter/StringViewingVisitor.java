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

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
/*
 * SgmlPcdataExtractorVisitor.java
 *
 * Created on 08 January 2004, 14:54
 */
package org.jvnet.olt.editor.format.sgml;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.laxparsers.sgml.LaxSgmlDocFragmentParserTreeConstants;
import org.jvnet.olt.laxparsers.sgml.LaxSgmlDocFragmentParserVisitor;
import org.jvnet.olt.laxparsers.sgml.SimpleNode;


/**
 *
 * @author  jc73554
 */
public class SgmlPcdataExtractorVisitor implements LaxSgmlDocFragmentParserVisitor, LaxSgmlDocFragmentParserTreeConstants {
    private GlobalVariableManager gvm;
    private StringBuffer buffer;
    private boolean inIgnoredMarkedSect;
    private int markedSectDepth;

    /** Creates a new instance of SgmlPcdataExtractorVisitor */
    public SgmlPcdataExtractorVisitor(GlobalVariableManager gvm) {
        this.gvm = gvm;
        buffer = new StringBuffer();
    }

    public Object visit(SimpleNode simpleNode, Object obj) {
        if (simpleNode.isDisplayingNode()) {
            if (inIgnoredMarkedSect) {
                handleMarkedSectCtx(simpleNode);
            } else {
                handleDefaultCtx(simpleNode);
            }
        }

        return null;
    }

    /**
     */
    public String getPcdata() {
        return buffer.toString();
    }

    /** This method checks if the node presented represents the start of an
     * ignred marked section, or not. It does this by checking the node's type
     * and by looking up the GlobalVariableManager to see if the flag in the
     * marked section start, resolves to IGNORE.
     * @returns Returns true if the node is a start of a marked section whose flag resolves to
     * IGNORE.
     * @param node The node to test.
     */
    protected boolean isIgnoredMarkedSectStart(SimpleNode node) {
        String flag = node.getMarkedSectionFlag();

        boolean boolEntityMeansIgnore = (gvm.isVariableDefined(flag) && gvm.resolveVariable(flag).equals("IGNORE"));

        return (flag.equals("IGNORE") || boolEntityMeansIgnore);
    }

    /**
     */
    protected void handleMarkedSectCtx(SimpleNode node) {
        int nodeType = node.getType();

        switch (nodeType) {
        case JJTMARKED_SECT_START:
            markedSectDepth++;

            break;

        case JJTMARKED_SECT_END:
            markedSectDepth--;

            if (markedSectDepth <= 0) {
                markedSectDepth = 0;
                inIgnoredMarkedSect = false;
            }

            break;

        default:
            break;
        }
    }

    /**
     */
    protected void handleDefaultCtx(SimpleNode node) {
        int nodeType = node.getType();

        switch (nodeType) {
        case JJTMARKED_SECT_START:

            if (isIgnoredMarkedSectStart(node)) {
                inIgnoredMarkedSect = true;
            }

            break;

        case JJTPCDATA:
            buffer.append(node.getNodeData());

            break;

        default:
            break;
        }
    }
}

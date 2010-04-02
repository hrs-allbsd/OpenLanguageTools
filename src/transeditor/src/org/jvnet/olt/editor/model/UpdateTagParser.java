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
package org.jvnet.olt.editor.model;
import java.util.Vector;

import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @version 1.0
 */
public class UpdateTagParser implements SgmlDocFragmentParserVisitor {
    private Vector vSimpleNode = new Vector();
    private int iIndex = 0;
    private String strSeg;

    public UpdateTagParser(String strInput) {
        strSeg = strInput;
    }

    public Vector getParseVector() {
        return vSimpleNode;
    }

    public Object visit(SimpleNode node, Object data) {
        String tagName = node.getTagName();
        String nodeData = node.getNodeData();

        switch (node.getType()) {
        case SgmlDocFragmentParserTreeConstants.JJTOPEN_TAG:

            int iPos = strSeg.indexOf(nodeData);
            ContentTag ct = new ContentTag(tagName, iIndex, iPos, nodeData);
            vSimpleNode.add(ct);
            iIndex++;

            break;

	case SgmlDocFragmentParserTreeConstants.JJTCLOSE_TAG:
            iPos = strSeg.indexOf(nodeData);
            ct = new ContentTag(tagName, iIndex, iPos, nodeData);
            vSimpleNode.add(ct);
            iIndex++;

            break;
        }

        return data;
    }

    public ContentTag[] getTags() {
        return (ContentTag[])vSimpleNode.toArray(new ContentTag[0]);
    }
}

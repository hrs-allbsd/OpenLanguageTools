/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

import java.io.*;
import java.io.StringReader;

import java.util.Iterator;
import java.util.List;
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
        }

        return data;
    }

    public ContentTag[] getTags() {
        return (ContentTag[])vSimpleNode.toArray(new ContentTag[0]);
    }
}

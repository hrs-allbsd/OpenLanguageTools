/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SwMsgBaseElementExtractorVisitor.java
 *
 * Created on 07 June 2004
 */
package org.jvnet.olt.editor.format.swmsg;

import java.util.LinkedList;
import java.util.List;

import org.jvnet.olt.editor.model.PivotBaseElement;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.laxparsers.software.LaxSoftwareMessageParserTreeConstants;
import org.jvnet.olt.laxparsers.software.LaxSoftwareMessageParserVisitor;
import org.jvnet.olt.laxparsers.software.SimpleNode;


public class SwMsgBaseElementExtractorVisitor implements LaxSoftwareMessageParserVisitor, LaxSoftwareMessageParserTreeConstants {
    private GlobalVariableManager gvm;
    private StringBuffer buffer;
    private List baseElementsList;

    public SwMsgBaseElementExtractorVisitor(GlobalVariableManager gvm) {
        this.gvm = gvm;
        buffer = new StringBuffer();
        baseElementsList = new LinkedList();
    }

    /**
     * The implementation of visit in the <CODE>LaxSgmlDocFragmentParserVisitor</CODE> interface.
     * @param simpleNode The node in the parse tree to visit.
     * @param obj A helper object. This parameter is not used in the implementation and is best left <CODE>null</CODE>.
     * @return Object
     */
    public Object visit(SimpleNode simpleNode, Object obj) {
        int nodeType = simpleNode.getType();
        PivotBaseElement element = null;

        switch (nodeType) {
        case JJTDATA:
        case JJTENTITY:
        case JJTESCAPE:
        case JJTCONVENTION:
        case JJTOPEN_TAG:
        case JJTEND_TAG:

            //System.out.println("-->" + simpleNode.toString() + ":" + simpleNode.getNodeData());
            buffer.append(simpleNode.getNodeData());
            element = createBaseElement(simpleNode);
            baseElementsList.add(element);

            break;

        default:
            break;
        }

        return null;
    }

    /**
     * This method returns the array PivotBaseElement object that the visitor
     * built up during its walk of the parse tree.
     * @return PivotBaseElement[]
     */
    public PivotBaseElement[] getBaseElements() {
        return (PivotBaseElement[])baseElementsList.toArray(new PivotBaseElement[0]);
    }

    /**
     * @return String
     */
    public String getMessageData() {
        return buffer.toString();
    }

    /**
     * This method creates an appropriate <code>PivotBaseElement</code> objects to
     * represent the <code>SimpleNode</code> the
     * @param node An instance of SimpleNode
     * @return PivotBaseElement
     */
    protected PivotBaseElement createBaseElement(SimpleNode node) {
        int nodeType = node.getType();

        switch (nodeType) {
        case JJTDATA:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.TEXT);

        case JJTENTITY:
        case JJTCONVENTION:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.PH_ELEM);

        case JJTESCAPE:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.IT_ELEM);

        case JJTOPEN_TAG:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.BPT_ELEM, node.getTagName());

        case JJTEND_TAG:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.EPT_ELEM, node.getTagName());

        default:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.TEXT);
        }
    }
}

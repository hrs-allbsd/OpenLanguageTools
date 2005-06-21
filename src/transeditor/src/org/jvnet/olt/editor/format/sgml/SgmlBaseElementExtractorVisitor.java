/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SgmlBaseElementExtractorVisitor.java
 *
 * Created on 08 January 2004, 14:50
 */
package org.jvnet.olt.editor.format.sgml;

import java.util.LinkedList;
import java.util.List;

import org.jvnet.olt.editor.model.CompoundPivotBaseElement;
import org.jvnet.olt.editor.model.PivotBaseElement;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.laxparsers.sgml.LaxSgmlDocFragmentParserTreeConstants;
import org.jvnet.olt.laxparsers.sgml.LaxSgmlDocFragmentParserVisitor;
import org.jvnet.olt.laxparsers.sgml.SimpleNode;


/** This class is a visitor that visits nodes in the parse tree created by the
 * <CODE>LaxSgmlDocFragmentParser</CODE>. It creates <CODE>PivotBaseElement</CODE>
 * objects for nodes in the tree and categorizes them into a limited and standard
 * number of types.
 * There is one special type, the <CODE>PivotBaseElement.MRK_ELEM</CODE> type. This
 * type is used for aggregates of nodes in the parse tree. These are held in a
 * subclass of <CODE>PivotBaseElement</CODE>, which allows access to contained
 * <CODE>PivotBaseElement</CODE> objects. Multiple levels of nesting will not
 * take place in such cases, i.e., there will be no <CODE>PivotBaseElement.MRK_ELEM</CODE>
 * elements inside a <CODE>PivotBaseElement.MRK_ELEM</CODE>. As such this is not
 * an implementation of the Composite pattern.
 * The Composite pattern is not followed, as it is unnecessary complication. Most
 * uses of the arrays of <CODE>PivotBaseElement</CODE> objects do not need to know
 * about the level of nesting. The top level <CODE>PivotBaseElement.MRK_ELEM</CODE>
 * is needed to denote when PCDATA inside a tag needs to be marked as non-translatable,
 * or needs to be protected or aggregated in some way. The lower level is for
 * determining fine grain differences between segments, e.g., for the tag verification,
 * or the source string difference highlighting.
 * @author  jc73554
 */
public class SgmlBaseElementExtractorVisitor implements LaxSgmlDocFragmentParserVisitor, LaxSgmlDocFragmentParserTreeConstants {
    private GlobalVariableManager gvm;
    private boolean inIgnoredMarkedSection;
    private int ignoredMarkedSectionDepth;
    private boolean inUntranslatableContent;
    private int untranslatableTagDepth;
    private List baseElementsList;
    private CompoundPivotBaseElement currentCompoundBaseElement;

    /** This member variable refers to a table that describes the properties of various
     * tags. It is used to lookup the cases where the contents of tags should be
     * considered untranslatable, and hence protected in the editor in the same way
     * that tags are.
     */
    private org.jvnet.olt.parsers.tagged.SegmenterTable table;

    /** Creates a new instance of SgmlBaseElementExtractorVisitor */
    public SgmlBaseElementExtractorVisitor(GlobalVariableManager gvm, org.jvnet.olt.parsers.tagged.SegmenterTable table) {
        this.gvm = gvm;
        baseElementsList = new LinkedList();
        this.table = table;
    }

    /** The implementation of visit in the <CODE>LaxSgmlDocFragmentParserVisitor</CODE>
     * interface.
     * @param simpleNode The node in the parse tree to visit.
     * @param obj A helper object. This parameter is not used in the implementation and is best
     * left <CODE>null</CODE>.
     */
    public Object visit(SimpleNode simpleNode, Object obj) {
        //  We are not interested in any intermediate node. Only displaying nodes
        //  and the end of file interest us.
        boolean inDisplayingNode = simpleNode.isDisplayingNode();

        if (inDisplayingNode) {
            if (inIgnoredMarkedSection) {
                handleMarkedSection(simpleNode);
            } else {
                if (inUntranslatableContent) {
                    handleUntranslatableContent(simpleNode);
                } else {
                    handleDefault(simpleNode);
                }
            }
        }

        return null;
    }

    /** This method returns the array PivotBaseElement object that the visitor
     * built up during its walk of the parse tree.
     */
    public PivotBaseElement[] getBaseElements() {
        return (PivotBaseElement[])baseElementsList.toArray(new PivotBaseElement[0]);
    }

    /** This method handles the case when we are in the middle of an ignored
     * marked section.
     * @param node The node to process.
     */
    protected void handleMarkedSection(SimpleNode node) {
        int nodeType = node.getType();

        //  Add the appropriate element to the current compound element
        PivotBaseElement elem = createBaseElement(node);
        currentCompoundBaseElement.addSubElement(elem);

        //  Now check on the nesting depth
        switch (nodeType) {
        case JJTMARKED_SECT_START:
            ignoredMarkedSectionDepth++;

            break;

        case JJTMARKED_SECT_END:
            ignoredMarkedSectionDepth--;

            if (ignoredMarkedSectionDepth <= 0) {
                ignoredMarkedSectionDepth = 0;
                inIgnoredMarkedSection = false;
            }

            break;

        default:
            break;
        }
    }

    /** Handles normal nodes in the SGML fragment.
     * @param node The node to process.
     */
    protected void handleDefault(SimpleNode node) {
        int nodeType = node.getType();
        PivotBaseElement element = null;

        switch (nodeType) {
        case JJTMARKED_SECT_START:
            element = createBaseElement(node);

            if (isIgnoredMarkedSectStart(node)) {
                inIgnoredMarkedSection = true;
                ignoredMarkedSectionDepth++;

                currentCompoundBaseElement = new CompoundPivotBaseElement();
                currentCompoundBaseElement.addSubElement(element);
                baseElementsList.add(currentCompoundBaseElement);
            } else {
                baseElementsList.add(element);
            }

            break;

        case JJTOPEN_TAG:
            element = createBaseElement(node);

            if (isTagWithUntranslatableContent(node)) {
                inUntranslatableContent = true;

                // Note using a simple tag depth counter here. Due to the
                // possibility of tag minimization this should probably change
                // to an encapsulated stack. This stack would track the state
                // of tag nesting and detect cases where tag minimization
                // has occurred.
                untranslatableTagDepth++;

                currentCompoundBaseElement = new CompoundPivotBaseElement();
                currentCompoundBaseElement.addSubElement(element);
                baseElementsList.add(currentCompoundBaseElement);
            } else {
                baseElementsList.add(element);
            }

            break;

        case JJTEOF: //  Drop this just to be safe.
            break;

        default:
            element = createBaseElement(node);
            baseElementsList.add(element);

            break;
        }
    }

    /** This method handles the cases where we are inside tags whose content is not
     * translatable.
     * @param node The node to process.
     */
    protected void handleUntranslatableContent(SimpleNode node) {
        int nodeType = node.getType();

        //  Add the appropriate element to the current compound element
        PivotBaseElement elem = createBaseElement(node);
        currentCompoundBaseElement.addSubElement(elem);

        //  Now check on the nesting depth
        switch (nodeType) {
        case JJTOPEN_TAG:

            if (isTagWithUntranslatableContent(node)) {
                untranslatableTagDepth++;
            }

            break;

        case JJTEND_TAG:

            if (isTagWithUntranslatableContent(node)) {
                untranslatableTagDepth--;

                if (untranslatableTagDepth <= 0) {
                    untranslatableTagDepth = 0;
                    inUntranslatableContent = false;
                }
            }

            break;

        default:
            break;
        }
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

        //  System.out.println("Mrk sect flag = " + flag);
        boolean boolEntityMeansIgnore = (gvm.isVariableDefined(flag) && gvm.resolveVariable(flag).equals("IGNORE"));

        return (flag.equals("IGNORE") || boolEntityMeansIgnore);
    }

    /** This method checks if the node presented represents a tag with
     * untranslatable content.
     * @returns Returns true if the node is a tag with untranslatable content.
     * @param node The node to test.
     */
    protected boolean isTagWithUntranslatableContent(SimpleNode node) {
        String tagName = node.getTagName();

        return (!table.containsTranslatableText(tagName));
    }

    /** This method creates an appropriate <code>PivotBaseElement</code> objects to
     * represent the <code>SimpleNode</code> the
     */
    protected PivotBaseElement createBaseElement(SimpleNode node) {
        int nodeType = node.getType();

        switch (nodeType) {
        case JJTMARKED_SECT_START:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.BPT_ELEM, "marked-sect");

        case JJTMARKED_SECT_END:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.EPT_ELEM, "marked-sect");

        case JJTCOMMENT: //  drop through
        case JJTPROCESSING_INST: //  drop through
        case JJTCDATA_SECT:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.IT_ELEM, "<!non tag >");

        case JJTJSP_TAG: //  drop through
        case JJTJSP_INLINE:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.IT_ELEM, "jsp-tag");

        case JJTPCDATA:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.TEXT);

        case JJTENTITY:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.PH_ELEM);

        case JJTOPEN_TAG:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.BPT_ELEM, node.getTagName());

        case JJTEND_TAG:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.EPT_ELEM, node.getTagName());

        default:
            return new PivotBaseElement(node.getNodeData(), PivotBaseElement.TEXT);
        }
    }
}

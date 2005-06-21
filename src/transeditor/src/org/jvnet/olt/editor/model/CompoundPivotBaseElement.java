/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * CompoundPivotBaseElement.java
 *
 * Created on 26 January 2004, 11:00
 */
package org.jvnet.olt.editor.model;


/** This class represents complex BaseElement object that would be marked up as
 * <mrk> elements in XLIFF documents. Examples of such elements would be marked
 * sections, tags with untranslatable content, etc.
 * @author  jc73554
 */
public class CompoundPivotBaseElement extends PivotBaseElement {
    /** Holds value of property subElements. */
    private java.util.List subElements;

    /** Creates a new instance of CompoundPivotBaseElement */
    public CompoundPivotBaseElement() {
        super("", PivotBaseElement.MRK_ELEM);
        subElements = new java.util.ArrayList();
    }

    /** Getter for property subElements.
     * @return Value of property subElements.
     */
    public java.util.List getSubElements() {
        return subElements;
    }

    public void addSubElement(PivotBaseElement elem) {
        //  Add sub element to the list
        subElements.add(elem);
    }

    /** Overrides the method in <CODE>PivotBaseElement</CODE>. It calculates the
     * content on the fly from all the sub elements.
     */
    public String getContent() {
        StringBuffer buffer = new StringBuffer();
        java.util.Iterator iter = subElements.iterator();

        while (iter.hasNext()) {
            PivotBaseElement pbe = (PivotBaseElement)iter.next();
            buffer.append(pbe.getContent());
        }

        return buffer.toString();
    }

    /** Overrides the method in <CODE>PivotBaseElement</CODE>. It calculates the
     * visible length on the fly from all the sub elements.
     */
    public int getVisibleLength() {
        int visLength = 0;
        java.util.Iterator iter = subElements.iterator();

        while (iter.hasNext()) {
            PivotBaseElement pbe = (PivotBaseElement)iter.next();
            visLength += pbe.getVisibleLength();
        }

        return visLength;
    }

    /** Overrides the method in <CODE>PivotBaseElement</CODE>. It returns true to
     * identify itself as a compound base element object.
     */
    public boolean isCompoundBaseElement() {
        return true;
    }
}

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

import javax.swing.text.Position;


/**
 * <p>Title: Open Language Tools XLIFF Translation Editor</p>
 * <p>Description: This class is the definition of a tag element of XLIFF file, and it is used in the feature of Update Tags.</p>
 * @version 1.0
 */
public class PivotBaseElement {
    public final static int TEXT = 0;
    public final static int IT_ELEM = 1;
    public final static int BPT_ELEM = 2;
    public final static int EPT_ELEM = 3;
    public final static int PH_ELEM = 4;
    public final static int MRK_ELEM = 5;
    private String content;
    private String tagName;
    private boolean flag;
    private int positionSite = 0;
    private int visibleLength;

    /** Holds value of property elemType. */
    private int elemType;

    public PivotBaseElement(String content) {
        this(content, TEXT);
    }

    public PivotBaseElement(String content, int elementType) {
        setContent(content, elementType);
    }

    public PivotBaseElement(String content, int elementType, String tagName) {
        setContent(content, elementType);
        this.tagName = tagName;
    }

    public String getContent() {
        return content;
    }

    public String getTagName() {
        return tagName;
    }

    public boolean getFlag() {
        return isMarkup();
    }

    protected void setContent(String content, int elementType) {
        //  Guard clause
        if ((elementType < TEXT) || (elementType > MRK_ELEM)) {
            throw new IllegalArgumentException("The type of element specified for a" + "PivotBaseElement is out of range. Value specified is: " + elementType);
        }

        this.elemType = elementType;
        this.content = content;

        this.tagName = ""; //  extractTagName();

        if (isVisible()) {
            this.visibleLength = content.length();
        } else {
            this.visibleLength = 0;
        }
    }

    public void setPositionSite(int p) {
        positionSite = p;
    }

    public int getPositionSite() {
        return positionSite;
    }

    public String toString() {
        return "Content=" + content + "\nposition=" + positionSite + "\ntagname=" + tagName;
    }

    public boolean isMarkup() {
        return (elemType != TEXT);
    }

    public boolean isTag() {
        return ((elemType == BPT_ELEM) || (elemType == EPT_ELEM) || (elemType == IT_ELEM));
    }

    /** Getter for property elemType.
     * @return Value of property elemType.
     */
    public int getElemType() {
        return this.elemType;
    }

    public int getVisibleLength() {
        return visibleLength;
    }

    public boolean isVisible() {
        return true;
    }

    /**
     */
    public java.util.List getSubElements() {
        return null;
    }

    public boolean isCompoundBaseElement() {
        return false;
    }

    /** Override Object methods to give proper behaviour in collections
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof PivotBaseElement)) {
            return false;
        }

        PivotBaseElement elem = (PivotBaseElement)o;

        boolean equalTypes = (elem.getElemType() == this.getElemType());
        boolean equalContent = (elem.getContent().equals(this.getContent()));

        return (equalTypes && equalContent);
    }

    public int hashCode() {
        return (this.elemType + (this.getContent()).hashCode());
    }
}

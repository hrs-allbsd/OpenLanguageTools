/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.Point;


public class TableView {
    public Point viewPosition;
    public int alignmentNum;
    public int sentenceNum;
    public int selecttionStart;
    public int selectionLen;

    public TableView(Point viewPosition, int alignmentNum, int sentenceNum, int selecttionStart, int selectionLen) {
        this.viewPosition = viewPosition;
        this.alignmentNum = alignmentNum;
        this.sentenceNum = sentenceNum;
        this.selecttionStart = selecttionStart;
        this.selectionLen = selectionLen;
    }
}

/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.util.*;


public class DocumentUndoableEdit extends ViewUndoableEdit {
    boolean isSource;
    String presentationName;
    int alignmentRow = -1;
    int sentenceIndex = 0;
    String oldString = null;
    String newString = null;

    // bug 4745303
    int oldTransType = 0;
    int newTransType = 0;

    //variable for undo REPLACEALL, TRANSFER M:N
    Object undoObject = null;
    Object undoGroupObject = null;

    public DocumentUndoableEdit(boolean isSrc, String presentation, int row, int index) {
        this.isSource = isSrc;
        this.presentationName = presentation;
        this.alignmentRow = row;
        this.sentenceIndex = index;
    }

    public DocumentUndoableEdit(boolean isSrc, String presentation, int row, int index, String oldString, String newString, int oldTransType, int newTransType) {
        this.isSource = isSrc;
        this.presentationName = presentation;
        this.alignmentRow = row;
        this.sentenceIndex = index;
        this.oldString = new String(oldString);
        this.newString = new String(newString);

        //bug 4745303
        this.oldTransType = oldTransType;
        this.newTransType = newTransType;
    }

    public DocumentUndoableEdit(boolean isSrc, String presentation, int row, int index, Object object) {
        this.isSource = isSrc;
        this.presentationName = presentation;
        this.alignmentRow = row;
        this.sentenceIndex = index;
        this.undoObject = object;
    }

    public DocumentUndoableEdit(boolean isSrc, String presentation, int row, int index, Object objectA, Object objectB) {
        this.isSource = isSrc;
        this.presentationName = presentation;
        this.alignmentRow = row;
        this.sentenceIndex = index;
        this.undoObject = objectA;
        this.undoGroupObject = objectB;
    }

    public void setRow(int s) {
        alignmentRow = s;
    }

    public int getRow() {
        return alignmentRow;
    }

    public boolean isSource() {
        return isSource;
    }

    public void setIndex(int i) {
        sentenceIndex = i;
    }

    public int getIndex() {
        return sentenceIndex;
    }

    public void setPresentationName(String name) {
        presentationName = name;
    }

    public String getPresentationName() {
        return presentationName;
    }

    public String getOldString() {
        return oldString;
    }

    public String getNewString() {
        return newString;
    }

    public String toString() {
        return "Action:" + presentationName;
    }

    // bug 4745303
    public void setOldTransType(int type) {
        this.oldTransType = type;
    }

    public int getOldTransType() {
        return this.oldTransType;
    }

    public void setNewTransType(int type) {
        this.newTransType = type;
    }

    public int getNewTransType() {
        return this.newTransType;
    }

    //----------------------------------
    public Object getUndoObject() {
        return undoObject;
    }

    public void setUndoObject(Object undoobject) {
        this.undoObject = undoobject;
    }

    public Object getUndoGroupObject() {
        return undoGroupObject;
    }

    public void setUndoGroupObject(Object undoobject) {
        this.undoGroupObject = undoobject;
    }
}

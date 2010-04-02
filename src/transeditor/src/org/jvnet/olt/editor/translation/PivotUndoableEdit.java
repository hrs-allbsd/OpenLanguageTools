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
package org.jvnet.olt.editor.translation;

import java.awt.Point;

import java.util.Vector;


/**
 * PivotUndoableEdit is a class used to record a undoable edit which can be
 * created by "merge", "split", "delete" actions.
 */
public class PivotUndoableEdit extends ViewUndoableEdit {
    public final static int MERGE_SOURCE = 1;
    public final static int MERGE_TARGET = 2;
    public final static int SPLIT_SOURCE = 4;
    public final static int SPLIT_TARGET = 8;
    public final static int DELETE_SOURCE = 16;
    public final static int DELETE_TARGET = 32;
    public final static int DELETE_ALIGNMENT = 64;
    public final static String MERGE_SOURCE_NAME = "merge source";
    public final static String MERGE_TARGET_NAME = "merge target";
    public final static String SPLIT_SOURCE_NAME = "split source";
    public final static String SPLIT_TARGET_NAME = "split target";
    public final static String DELETE_SOURCE_NAME = "delete source";
    public final static String DELETE_TARGET_NAME = "delete target";
    public final static String DELETE_ALIGNMENT_NAME = "delete alignment";
    int index;
    Vector oldItems;
    Vector newItems;
    String presentationName;
    int type;
    int oldSentenceNumber;
    int newSentenceNumber;
    int oldSize;
    int newSize;

    //public TableView oldSrcTableView = null;
    //public TableView newSrcTableView = null;
    //public TableView oldTargetTableView = null;
    //public TableView newTargetTableView = null;
    //public PivotUndoableEdit srcUndo = null;
    //public PivotUndoableEdit targetUndo = null;

    /**
     * Constructor.
     */
    public PivotUndoableEdit() {
        oldItems = new Vector();
        newItems = new Vector();
    }

    public PivotUndoableEdit(PivotUndoableEdit src, PivotUndoableEdit target) {
        srcUndo = src;
        targetUndo = target;
        type = this.DELETE_ALIGNMENT;
        presentationName = DELETE_ALIGNMENT_NAME;
    }

    /**
     * Constructor.
     * @param index The first changed source or target sentences index.
     * @param number The changed source or target sentences number.
     * @param type Changed cells type, source(0), target(1).
     * @param size The alignments vector size before changed.
     */
    public PivotUndoableEdit(int index, int number, int type, int size) {
        this.index = index;
        this.oldSentenceNumber = number;
        this.type = type;
        this.oldSize = size;
        oldItems = new Vector();
        newItems = new Vector();

        if (type == MERGE_SOURCE) {
            presentationName = MERGE_SOURCE_NAME;
        } else if (type == MERGE_TARGET) {
            presentationName = MERGE_TARGET_NAME;
        } else if (type == SPLIT_TARGET) {
            presentationName = SPLIT_TARGET_NAME;
        } else if (type == SPLIT_SOURCE) {
            presentationName = SPLIT_SOURCE_NAME;
        } else if (type == DELETE_SOURCE) {
            presentationName = DELETE_SOURCE_NAME;
        } else if (type == DELETE_TARGET) {
            presentationName = DELETE_TARGET_NAME;
        }
    }

    /**
     * Set first changed source or target sentences index.
     * @param index The first changed source or target sentences index.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Get first changed source or target sentences index.
     * @return index The first changed source or target sentences index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get changed cells' type.
     * @return The changed cells' type.
     */
    public int getType() {
        return type;
    }

    /**
     * Set sentences number before change.
     * @param number The sentences number before change.
     */
    public void setSentenceNumber(int number) {
        oldSentenceNumber = number;
    }

    /**
     * Get sentences number before change.
     * @return The sentences number before change.
     */
    public int getSentenceNumber() {
        return oldSentenceNumber;
    }

    /**
     * Set sentences number after change.
     * @param number The sentences number after change.
     */
    public void setNewSentenceNumber(int number) {
        newSentenceNumber = number;
    }

    /**
     * Get sentences number after change.
     * @return number The sentences number after change.
     */
    public int getNewSentenceNumber() {
        return newSentenceNumber;
    }

    /**
     * Get alignments size before change.
     * @return alignments size before change.
     */
    public int getSize() {
        return oldSize;
    }

    /**
     * Set alignments size after change.
     * @param alignments size after change.
     */
    public void setNewSize(int size) {
        newSize = size;
    }

    /**
     * Get alignments size after change.
     * @return alignments size after change.
     */
    public int getNewSize() {
        return newSize;
    }

    /**
     * Set representation name of this action.
     * @param name The representation name of this action.
     */
    public void setPresentationName(String name) {
        presentationName = name;
    }

    /**
     * Get representation name of this action.
     * @return name The representation name of this action.
     */
    public String getPresentationName() {
        return presentationName;
    }

    /**
     * Add an item which is before change.
     * @param object The item which is before change.
     */
    public void addOldItem(Object object) {
        oldItems.add(object);
    }

    /**
     * Add an item which is after change.
     * @param object The item which is after change.
     */
    public void addNewItem(Object object) {
        newItems.add(object);
    }

    /**
     * Get items before change.
     * @return The items before change.
     */
    public Vector getOldItems() {
        return oldItems;
    }

    /**
     * Get items after change.
     * @return The items after change.
     */
    public Vector getNewItems() {
        return newItems;
    }
}

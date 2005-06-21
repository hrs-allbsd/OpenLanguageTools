/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.util.*;


public abstract class ViewUndoableEdit {
    public TableView oldSrcTableView = null;
    public TableView newSrcTableView = null;
    public TableView oldTargetTableView = null;
    public TableView newTargetTableView = null;
    public PivotUndoableEdit srcUndo = null;
    public PivotUndoableEdit targetUndo = null;
}

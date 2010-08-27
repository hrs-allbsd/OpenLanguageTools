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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.*;
import java.util.logging.Logger;

import javax.swing.undo.*;

import org.jvnet.olt.editor.model.TMData;
import org.jvnet.olt.editor.translation.MainFrame;


public class PivotUndoManager extends UndoManager {
    private static final Logger logger = Logger.getLogger(PivotUndoManager.class.getName());
    EditStack undoStack = new EditStack();
    EditStack redoStack = new EditStack();
    MainFrame frame;
    TMData pivotdata;
    Backend backend;

    List propertyListeners;

    class EditStack extends Stack {
        int stackSize = 10;

        public void setLimit(int size) {
            stackSize = size;
        }

        public Object push(Object item) {
            addElement(item);

            if (size() > stackSize) {
                remove(0);
            }

            return lastElement();
        }

        public synchronized Object pop() {
            return remove(size() - 1);
        }

        public synchronized Object peek() {
            return lastElement();
        }

        public boolean empty() {
            return isEmpty();
        }
    }

    class UndoableLog {
        final static int DEFAULT_EDIT = 0;
        final static int DOCUMENT_EDIT = 1;
        final static int PIVOT_EDIT = 2;
        String name;
        int type;
        Object edit;

        UndoableLog(int type, String name, Object edit) {
            this.type = type;
            this.name = name;
            this.edit = edit;
        }

        int getType() {
            return type;
        }

        String getName() {
            return name;
        }

        Object getEdit() {
            return edit;
        }
    }

    //TODO add property change listener + firing of property change event;
    //TODO bind to MainFrame in initApplication; get rid of MainFrame parameter
    public PivotUndoManager(MainFrame frame, Backend backend) {
        super();
        this.frame = frame;
        this.backend = backend;
        pivotdata = backend.getTMData();
        setLimit(10);
        undoStack.setLimit(10);
        redoStack.setLimit(10);
    }

    /**
     * Add an regular undoable edit.
     * @param anEdit The regular undoable edit.
     */
    public synchronized boolean addEdit(UndoableEdit anEdit) {
        boolean b = super.addEdit(anEdit);

        if (b == false) {
            return false;
        }

        UndoableLog ul = new UndoableLog(UndoableLog.DEFAULT_EDIT, getUndoPresentationName(), anEdit);
        undoStack.push(ul);
        redoStack.removeAllElements();

        return true;
    }

    public boolean addDocumentEdit(DocumentUndoableEdit anEdit) {
        UndoableLog ul = new UndoableLog(UndoableLog.DOCUMENT_EDIT, anEdit.getPresentationName(), anEdit);

        undoStack.push(ul);
        redoStack.removeAllElements();

        //firePropertyChanged(new PropertyChangeEvent(frame,"undoRedo",null,null))
        frame.propertyChange(new PropertyChangeEvent(frame, "undoRedo", null, null));

        return true;
    }

    public boolean updateDocumentEdit(DocumentUndoableEdit anEdit) {
        if (undoStack.size() > 0) {
            undoStack.pop();
        }

        UndoableLog ul = new UndoableLog(UndoableLog.DOCUMENT_EDIT, anEdit.getPresentationName(), anEdit);

        undoStack.push(ul);

        redoStack.removeAllElements();

        //frame.propertyChange(new PropertyChangeEvent(frame,"undoRedo",null,null));
        return true;
    }

    /**
     * Add a PivotUndoableEdit.
     * @param anEdit The PivotUndoableEdit.
     */
    public boolean addPivotEdit(PivotUndoableEdit anEdit) {
        UndoableLog ul = new UndoableLog(UndoableLog.PIVOT_EDIT, anEdit.getPresentationName(), anEdit);
        undoStack.push(ul);
        redoStack.removeAllElements();
        frame.propertyChange(new PropertyChangeEvent(frame, "undoRedo", null, null));

        return true;
    }

    /**
     * Get the representation name of undo action.
     * @return The representation name of undo action.
     */
    public String getUndoName() {
        if (canUndo() == false) {
            return null;
        }

        UndoableLog log = (UndoableLog)undoStack.peek();

        if (log.getType() == UndoableLog.DEFAULT_EDIT) {
            return getUndoPresentationName();
        } else {
            return "Undo " + log.getName();
        }
    }

    /**
     * Get the representation name of redo action.
     * @return The representation name of redo action.
     */
    public String getRedoName() {
        if (canRedo() == false) {
            return null;
        }

        UndoableLog log = (UndoableLog)redoStack.peek();

        if (log.getType() == UndoableLog.DEFAULT_EDIT) {
            return getRedoPresentationName();
        } else {
            return "Redo " + log.getName();
        }
    }

    public ViewUndoableEdit pivotUndo() {
        if (canUndo() == true) {
            UndoableLog ul = (UndoableLog)undoStack.pop();

            if (ul.getType() == UndoableLog.DEFAULT_EDIT) {
                try {
                    undo();
                } catch (CannotUndoException e) {
                    logger.throwing(getClass().getName(), "pivotUndo", e);
                    logger.severe("Exception:" + e);

                    //TODO throw Exception???
                }
            } else if (ul.getType() == UndoableLog.DOCUMENT_EDIT) {
                DocumentUndoableEdit pedit = (DocumentUndoableEdit)ul.getEdit();

                pivotdata = backend.getTMData();
                pivotdata.undo(pedit);
            } else {
                PivotUndoableEdit pedit = (PivotUndoableEdit)ul.getEdit();

                if ((pedit.srcUndo == null) && (pedit.targetUndo == null)) {
                } else {
                }
            }

            redoStack.push(ul);
            frame.propertyChange(new PropertyChangeEvent(frame, "undoRedo", null, null));

            return (ViewUndoableEdit)ul.getEdit();
        }

        return null;
    }

    /**
     * Redo the last undoable action.
     */
    public ViewUndoableEdit pivotRedo() {
        if (canRedo() == true) {
            UndoableLog ul = (UndoableLog)redoStack.pop();

            if (ul.getType() == UndoableLog.DEFAULT_EDIT) {
                try {
                    redo();
                } catch (CannotRedoException e) {
                    logger.throwing(getClass().getName(), "pivotRedo", e);
                    logger.severe("Exception:" + e);

                    //TODO throw Exception???
                }
            } else if (ul.getType() == UndoableLog.DOCUMENT_EDIT) {
                DocumentUndoableEdit pedit = (DocumentUndoableEdit)ul.getEdit();

                pivotdata = backend.getTMData();
                pivotdata.redo(pedit);
            } else {
                PivotUndoableEdit pedit = (PivotUndoableEdit)ul.getEdit();

                if ((pedit.srcUndo == null) && (pedit.targetUndo == null)) {
                    pivotdata = backend.getTMData();
                } else {
                    pivotdata = backend.getTMData();
                }
            }

            undoStack.push(ul);
            frame.propertyChange(new PropertyChangeEvent(frame, "undoRedo", null, null));

            return (ViewUndoableEdit)ul.getEdit();
        }

        return null;
    }

    /**
     * returns true if an undo operation would be successful now, false otherwise
     */
    public synchronized boolean canUndo() {
        return !undoStack.empty();
    }

    /**
     * returns true if an redo operation would be successful now, false otherwise
     */
    public synchronized boolean canRedo() {
        return !redoStack.empty();
    }

    /**
     * Empty the undo manager
     */
    public void discardAll() {
        while (undoStack.empty() == false)
            undoStack.pop();

        while (redoStack.empty() == false)
            redoStack.pop();

        discardAllEdits();
    }

    public void addUndoPropertyListener(PropertyChangeListener lstnr) {
        if (propertyListeners == null) {
            propertyListeners = new LinkedList();
        }

        if (!propertyListeners.contains(lstnr)) {
            propertyListeners.add(lstnr);
        }
    }

    private void firePropertyChanged(PropertyChangeEvent evt) {
        if (propertyListeners == null) {
            return;
        }

        for (Iterator i = propertyListeners.iterator(); i.hasNext();)
            ((PropertyChangeListener)i.next()).propertyChange(evt);
    }

    public void removePropertyListener(PropertyChangeListener lstnr) {
        if (propertyListeners == null) {
            return;
        }

        propertyListeners.remove(lstnr);
    }
}

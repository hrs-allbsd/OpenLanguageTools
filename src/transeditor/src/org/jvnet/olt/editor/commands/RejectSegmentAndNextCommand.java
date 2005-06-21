/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * RejectReviewedFileCommand.java
 *
 * Created on 21 May 2003, 18:24
 */
package org.jvnet.olt.editor.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jvnet.olt.editor.translation.MainFrame;


/**
 *
 * @author  jc73554
 */
public class RejectSegmentAndNextCommand implements java.awt.event.ActionListener {
    private MainFrame controller;

    /** Creates a new instance of RejectReviewedFileCommand */
    public RejectSegmentAndNextCommand(MainFrame controller) {
        this.controller = controller;
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        controller.setReviewStatusAndGotoNext(false);
    }
}

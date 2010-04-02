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
public class ApproveAllTranslatedSegmentsCommand implements java.awt.event.ActionListener {
    private MainFrame controller;

    /** Creates a new instance of RejectReviewedFileCommand */
    public ApproveAllTranslatedSegmentsCommand(MainFrame controller) {
        this.controller = controller;
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        controller.setReviewStatusOnAllSegs(true);
    }
}

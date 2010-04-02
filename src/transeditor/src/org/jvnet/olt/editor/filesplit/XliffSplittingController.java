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
 * XliffSplittingController.java
 *
 * Created on 24 September 2003, 15:24
 */
package org.jvnet.olt.editor.filesplit;

import java.io.IOException;

import java.util.List;
import org.jvnet.olt.editor.util.Bundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jvnet.olt.xlifftools.XliffDocumentSplitter;


/**
 *
 * @author  jc73554
 */
public class XliffSplittingController {
    private JFrame frame;
    private String defaultFileName;
    private XliffSplittingPrefsDialog dialog;

    private Bundle bundle = Bundle.getBundle(XliffSplittingController.class.getName());
    
    /** Creates a new instance of XliffSplittingController */
    public XliffSplittingController(JFrame frame, String defaultFileName) {
        this.frame = frame;
        this.defaultFileName = defaultFileName;
        dialog = new XliffSplittingPrefsDialog(frame);
    }

    public XliffSplittingController(JFrame frame) {
        this(frame, "");
    }

    public void doXliffSplitting() throws IOException {
        //  Get User Preferences
        XliffSplittingPrefs prefs = getUserPreferences();
        String message = "";

        if (prefs == null) {
            //  Show message and return
            message = bundle.getString("The_file_splitting_operation_has_been_cancelled.");
            JOptionPane.showMessageDialog(frame, message, bundle.getString("Cancelled"), JOptionPane.WARNING_MESSAGE);

            return;
        }

        //  Create the XliffSplitter
        XliffDocumentSplitter splitter = new XliffDocumentSplitter(prefs.getSuffix(), prefs.getSegmentNum());

        //  Run the split operation.
        List docList = splitter.splitDocument(prefs.getXliffDocument());

        //  Write out the documents
        try {
            splitter.writeOutputDocuments(docList, prefs.getOutputLoc());

            //  Announce successful write out.
            message = bundle.getString("The_file_splitting_operation_was_successful.");
            JOptionPane.showMessageDialog(frame, message, bundle.getString("Success"), JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioEx) {
            JOptionPane.showMessageDialog(frame, ioEx.getMessage(), bundle.getString("Exception_thrown"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public XliffSplittingPrefs getUserPreferences() {
        //  Set a default file name
        dialog.setDefaultFilename(defaultFileName);

        //  Show dialog
        dialog.show();

        //  Get preferences from the dialog.
        return dialog.getPreferences();
    }

    /**
     * This is a test main method used to exercise the class.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        XliffSplittingController split = new XliffSplittingController(new JFrame());

        try {
            split.doXliffSplitting();
            System.exit(0);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            System.exit(1);
        }
    }
}

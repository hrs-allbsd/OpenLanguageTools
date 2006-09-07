/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * XliffMergingController.java
 *
 * Created on 26 September 2003, 18:47
 */
package org.jvnet.olt.editor.filesplit;

import java.io.File;
import java.io.IOException;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.xml.parsers.ParserConfigurationException;

import org.jvnet.olt.xliff.XliffDocument;
import org.jvnet.olt.xlifftools.XliffDocumentMerger;

import org.xml.sax.SAXException;


/**
 *
 * @author  jc73554
 */
public class XliffMergingController {
    private String defaultFileName;
    private String defaultInputDir;
    private String defaultSuffix;
    private JFrame frame;
    private XliffMergingPrefsDialog dialog;

    /** Creates a new instance of XliffMergingController */
    public XliffMergingController(JFrame frame) {
        this(frame, "", "", "");
    }

    public XliffMergingController(JFrame frame, java.lang.String inputDir, java.lang.String fileName, java.lang.String suffix) {
        this.frame = frame;
        defaultInputDir = inputDir;
        defaultFileName = fileName;
        defaultSuffix = suffix;
        dialog = new XliffMergingPrefsDialog(frame, true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            XliffMergingController controller = new XliffMergingController(new JFrame());
            controller.doXliffMerging();
            System.exit(0);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            System.exit(1);
        }
    }

    public void doXliffMerging() throws IOException {
        //  Get User Preferences
        XliffMergingPrefs prefs = getUserPreferences();
        String message = "";

        if (prefs == null) {
            //  Show message and return
            message = "The file merging operation has been cancelled.";
            JOptionPane.showMessageDialog(frame, message, "Cancelled", JOptionPane.WARNING_MESSAGE);

            return;
        }

        //  Create XliffDocumentMerger.
        XliffDocumentMerger merger = new XliffDocumentMerger(prefs.getSuffix());
        String fileName = prefs.getBaseFileName();

        //  Build list of input files matching the pattern specified.
        List docsToMerge = merger.buildDocList(prefs.getInputDir(), fileName);
        XliffDocument outputDoc = null;

        try {
            outputDoc = merger.mergeDocuments(docsToMerge, fileName);
        } catch (SAXException saxEx) {
            JOptionPane.showMessageDialog(frame, saxEx.getMessage(), "Exception thrown", JOptionPane.ERROR_MESSAGE);

            return;
        } catch (ParserConfigurationException parserEx) {
            JOptionPane.showMessageDialog(frame, parserEx.getMessage(), "Exception thrown", JOptionPane.ERROR_MESSAGE);

            return;
        }

        //  Write out the merged document to the output dir.
        File outputFile = new File(prefs.getOutputDir(), fileName);

        //  Write out the documents
        try {
            outputDoc.writeTo(outputFile);

            //  Announce successful write out.
            message = "The file merging operation was successful.";
            JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioEx) {
            JOptionPane.showMessageDialog(frame, ioEx.getMessage(), "Exception thrown", JOptionPane.ERROR_MESSAGE);
        }
    }

    public XliffMergingPrefs getUserPreferences() {
        //  Set a default file name
        dialog.setDefaultFileName(defaultFileName);
        dialog.setDefaultSuffix(defaultSuffix);
        dialog.setDefaultInputDir(defaultInputDir);

        //  Show dialog
        dialog.show();

        //  Get preferences from the dialog.
        return dialog.getMergePreferences();
    }
}

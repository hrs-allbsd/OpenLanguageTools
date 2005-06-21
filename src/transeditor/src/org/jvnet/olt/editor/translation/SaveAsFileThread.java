/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SaveAsFileThread.java
 *
 * Created on 20 November 2003, 13:35
 */
package org.jvnet.olt.editor.translation;

import java.awt.Cursor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 *
 * @author  jc73554
 */

//TODO break down: file selection and preparation to MainFrame;
// replace saving with SaveCurrentFileThread
public class SaveAsFileThread implements Runnable {
    private static final Logger logger = Logger.getLogger(SaveAsFileThread.class.getName());
    private MainFrame frame;
    private Backend backend;

    /** Creates a new instance of SaveAsFileThread */
    public SaveAsFileThread(MainFrame mainFrame, Backend backend) {
        this.frame = mainFrame;
        this.backend = backend;
    }

    public void run() {
        //  Raise a semaphore
        if (!frame.testAndToggleSemaphore(false)) {
            JOptionPane.showMessageDialog(frame, "<html>There is currently a Save operation taking place.<br> Please wait until the current save has finished before trying to save again.", "Save In Progress", JOptionPane.ERROR_MESSAGE);

            //  display error message
            return;
        }

        frame.setBHasModified(false);

        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        Object[] message = new Object[2];
        message[0] = "Please select a .xlz or .xlf file to open:";

        JFileChooser f = new JFileChooser();
        f.setMultiSelectionEnabled(false);
        f.setAcceptAllFileFilterUsed(false);
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);

        XLIFFZipOpenFilter xlzfilter = new XLIFFZipOpenFilter();
        XLIFFOpenFilter xlffilter = new XLIFFOpenFilter();
        int iType = 0; //0 for xlz, 1for xlf

        File curFile = backend.getCurrentFile();

        if (curFile.getName().endsWith(".xlz") == true) {
            iType = 0;
            f.addChoosableFileFilter(OpenFileFilters.XLZ_FILTER);
            f.setFileFilter(OpenFileFilters.XLZ_FILTER);
            f.setSelectedFile(new File("*.xlz"));
        } else if (curFile.getName().endsWith(".xlf") == true) {
            iType = 1;
            f.addChoosableFileFilter(OpenFileFilters.XLF_FILTER);
            f.setFileFilter(OpenFileFilters.XLF_FILTER);
            f.setSelectedFile(new File("*.xlf"));
        }

        String fname = backend.getConfig().getStrLastFile();
        f.setCurrentDirectory(new File(fname).getParentFile());
        message[1] = f;

        String[] options = { "OK", "Cancel" };

        File fSaveAsFile = null;
        int result = f.showDialog(frame, "Save As");

        switch (result) {
        case JFileChooser.APPROVE_OPTION: // ok
            frame.disableGUI();
            fSaveAsFile = f.getSelectedFile();

            if (fSaveAsFile.getName().indexOf(".") != -1) {
                if ((fSaveAsFile.getName().endsWith(".xlz") == false) && (fSaveAsFile.getName().endsWith(".xlf") == false)) {
                    if (iType == 0) {
                        String strTemp = fSaveAsFile.getAbsolutePath();
                        strTemp += ".xlz";
                        fSaveAsFile = new File(strTemp);
                    } else {
                        String strTemp = fSaveAsFile.getAbsolutePath();
                        strTemp += ".xlf";
                        fSaveAsFile = new File(strTemp);
                    }
                }
            } else {
                if (iType == 0) {
                    String strTemp = fSaveAsFile.getAbsolutePath();
                    strTemp += ".xlz";
                    fSaveAsFile = new File(strTemp);
                } else {
                    String strTemp = fSaveAsFile.getAbsolutePath();
                    strTemp += ".xlf";
                    fSaveAsFile = new File(strTemp);
                }
            }

            //logger.finer(fSaveAsFile.getAbsolutePath());
            File xFile = new File(fSaveAsFile.getAbsolutePath());

            if (!frame.copyFile(curFile, fSaveAsFile)) {
                JOptionPane.showMessageDialog(frame, "Failed to Save the target File", "Failed to Save", JOptionPane.OK_OPTION);

                break;
            }

            if (!backend.saveFileTo(xFile)) {
                JOptionPane.showMessageDialog(frame, "Failed to Save As the Current File", "Failed to Save", JOptionPane.OK_OPTION);

                break;
            } else {
                frame.setTitle(Constants.TOOL_NAME + " - " + frame.backend.getProject().getProjectName() + "-" + fSaveAsFile.getAbsolutePath());

                break;
            }

        case JFileChooser.CANCEL_OPTION: // cancel

            //  Lower a semaphore
            if (!frame.testAndToggleSemaphore(true)) {
                logger.severe("Error: attempt made to lower an already lowered semaphore flag.");

                //TODO throw Exception???
            }

            return;
        }

        if ((curFile != null) && (fSaveAsFile != null) && !curFile.getName().equals(fSaveAsFile.getName())) {
            curFile = fSaveAsFile;
            frame.resetFileHistory();
        }

        try {
            backend.copyCurrentFileToTemp();
        } catch (IOException ioe) {
            logger.throwing(getClass().getName(), "success", ioe);
        } catch (IllegalArgumentException iae) {
            logger.throwing(getClass().getName(), "success", iae);
        }

        /* TODO remove
         Thread newThread = new CpFileToTempFile(frame, backend);
        newThread.run();
        */

        //  Lower a semaphore
        if (!frame.testAndToggleSemaphore(true)) {
            logger.severe("Error: attempt made to lower an already lowered semaphore flag.");

            //TODO throw Exception???
        }

        //  Give the interface back to the user
        frame.enableGUI();
        JOptionPane.showMessageDialog(frame, "The Current File Saved successfully as '" + curFile + "'.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }
}

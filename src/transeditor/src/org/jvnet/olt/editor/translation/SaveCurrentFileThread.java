/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SaveCurrentFileThread.java
 *
 * Created on 20 November 2003, 13:34
 */
package org.jvnet.olt.editor.translation;

import java.awt.Cursor;

import java.io.IOException;

import java.util.logging.Logger;

import javax.swing.JOptionPane;


/**
 *
 * @author  jc73554
 */
public class SaveCurrentFileThread implements Runnable {
    private static final Logger logger = Logger.getLogger(SaveCurrentFileThread.class.getName());
    private MainFrame mainFrame;
    private Backend backend;

    /** Creates a new instance of SaveCurrentFileThread */
    public SaveCurrentFileThread(MainFrame mainFrame, Backend backend) {
        this.mainFrame = mainFrame;
        this.backend = backend;
    }

    public void run() {
        //  mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        mainFrame.disableGUI();

        //  Raise semaphore
        if (!mainFrame.testAndToggleSemaphore(false)) {
            JOptionPane.showMessageDialog(mainFrame, "<html>There is currently a Save operation taking place.<br> Please wait until the current save has finished before trying to save again.", "Save In Progress", JOptionPane.ERROR_MESSAGE);

            //  display error message
            return;
        }

        mainFrame.setBHasModified(false); //  Disable the save options.

        if (!backend.hasCurrentFile()) {
            return;
        }

        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.saveFile()) {
            mainFrame.saveFileFailed();

            //JOptionPane.showMessageDialog(mainFrame,"Failed to Save the Current File","Failed to Save",JOptionPane.OK_OPTION);
            mainFrame.setBHasModified(true); //  Re-enable the save options.

            //How about reenabling the UI?
            mainFrame.testAndToggleSemaphore(true);
            mainFrame.enableGUI();

            return;
        } else {
            mainFrame.setBHasModified(false);
        }

        mainFrame.resetFileHistory();

        /** asume the part below should not be run in separate thread;
        Thread newThread = new CpFileToTempFile(mainFrame, backend);
        newThread.run();
         */
        try {
            backend.copyCurrentFileToTemp();
        } catch (IOException ioe) {
            logger.throwing(getClass().getName(), "run", ioe);
        } catch (IllegalArgumentException iae) {
            logger.throwing(getClass().getName(), "run", iae);
        }

        //  Lower semaphore
        if (!mainFrame.testAndToggleSemaphore(true)) {
            logger.severe("Error: semaphore already lowered");

            //TODO throw Exception???
            // TODO display error message or throw an exception
        }

        //  Give the GUI back to the user.
        mainFrame.setCursor(Cursor.getDefaultCursor());
        mainFrame.enableGUI();
        JOptionPane.showMessageDialog(mainFrame, "The Current File Saved successfully.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }
}
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

import com.sun.corba.se.spi.orb.Operation;
import java.awt.Cursor;

import java.io.IOException;
import java.util.ResourceBundle;

import java.util.logging.Logger;

import javax.swing.JOptionPane;
import org.jvnet.olt.editor.util.NestableException;


/**
 *
 * @author  jc73554
 */
public class SaveCurrentFileThread implements Runnable {
    private static final Logger logger = Logger.getLogger(SaveCurrentFileThread.class.getName());
    private MainFrame mainFrame;
    private Backend backend;
    private ResourceBundle bundle = ResourceBundle.getBundle(SaveCurrentFileThread.class.getName());

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
            JOptionPane.showMessageDialog(mainFrame, bundle.getString("<html>There_is_currently_a_Save_operation_taking_place.<br>_Please_wait_until_the_current_save_has_finished_before_trying_to_save_again."), bundle.getString("Save_In_Progress"), JOptionPane.ERROR_MESSAGE);

            //  display error message
            return;
        }

        mainFrame.setBHasModified(false); //  Disable the save options.

        if (!backend.hasCurrentFile()) {
            return;
        }

        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        try{
            backend.saveFile();
        }
        catch (NestableException ne){
           mainFrame.exceptionThrown(ne,MainFrame.OPERATION_SAVE);
           mainFrame.setBHasModified(true); //  Re-enable the save options.

            //How about reenabling the UI?
            mainFrame.testAndToggleSemaphore(true);
            mainFrame.enableGUI();

            return;
        }

        mainFrame.setBHasModified(false);
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
        JOptionPane.showMessageDialog(mainFrame, bundle.getString("The_Current_File_Saved_successfully."), bundle.getString("Save_Successful"), JOptionPane.INFORMATION_MESSAGE);
    }
}

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

import com.sun.java.swing.SwingUtilities2;
import java.awt.Cursor;

import java.io.IOException;
import org.jvnet.olt.editor.util.Bundle;

import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jvnet.olt.editor.util.NestableException;
import org.jvnet.olt.editor.util.Task;


/**
 *
 * @author  jc73554
 */
//Changes Jul 11 2006
//Although this class is called Thread it is not Runnable anymore. Due to deadlocks
//caused by this thread when calling UI I changed it create a new thread only when
//doing actual save. This thread uses Task class to trigger any UI changes
public class SaveCurrentFileThread {
    private static final Logger logger = Logger.getLogger(SaveCurrentFileThread.class.getName());
    private MainFrame mainFrame;
    private Backend backend;
    private Bundle bundle = Bundle.getBundle(SaveCurrentFileThread.class.getName());

    /** Creates a new instance of SaveCurrentFileThread */
    public SaveCurrentFileThread(MainFrame mainFrame, Backend backend) {
        this.mainFrame = mainFrame;
        this.backend = backend;
    }
    
    /*
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
        
        try{
            SwingUtilities.invokeAndWait(new Runnable(){
                public void run() {
                    AlignmentMain.testMain1.stopEditing();
                    AlignmentMain.testMain2.stopEditing();
                }
            });
        } catch (Exception e){
            logger.warning("INterrupted:"+e);
        }
        
        try{
            backend.saveFile();
        } catch (NestableException ne){
            mainFrame.exceptionThrown(ne,MainFrame.OPERATION_SAVE);
            mainFrame.setBHasModified(true); //  Re-enable the save options.
            
            //How about reenabling the UI?
            mainFrame.testAndToggleSemaphore(true);
            mainFrame.enableGUI();
            
            return;
        }

        mainFrame.setBHasModified(false);
        mainFrame.resetFileHistory();
        
        //asume the part below should not be run in separate thread;
        // Thread newThread = new CpFileToTempFile(mainFrame, backend);
        // newThread.run();
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
    */
    public void save() {
        //  mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        mainFrame.disableGUI();
        
        //  Raise semaphore
        if (!mainFrame.testAndToggleSemaphore(false)) {
            JOptionPane.showMessageDialog(mainFrame, "<html>There is currently a Save operation taking place.<br> Please wait until the current save has finished before trying to save again.", "Save In Progress", JOptionPane.ERROR_MESSAGE);
            
            //  display error message
            return;
        }
        
        //mainFrame.setBHasModified(false); //  Disable the save options.
        
        if (!backend.hasCurrentFile()) {
            return;
        }
        
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();
        
        try{
            new Thread(new Runnable(){
                
                public void run(){
                    try{
                        backend.saveFile();
                        
                        
                        new Task(true) {
                            protected void execute() {
                                mainFrame.resetFileHistory();
                                mainFrame.setBHasModified(false);
                            }
                        }.start();
                        
                        
                    } catch (NestableException ne){
                        mainFrame.exceptionThrown(ne,MainFrame.OPERATION_SAVE);
                        mainFrame.setBHasModified(true); //  Re-enable the save options.
                        mainFrame.testAndToggleSemaphore(true);
                        
                        return ;
                        
                    } finally {
                        
                        new Task(true){
                            public void execute() {
                                mainFrame.enableGUI();
                            }
                        }.start();
                    }
                    
                    
                    try {
                        /** asume the part below should not be run in separate thread;
                         * Thread newThread = new CpFileToTempFile(mainFrame, backend);
                         * newThread.run();
                         */
                        backend.copyCurrentFileToTemp();
                    } catch (IOException ioe) {
                        logger.throwing(getClass().getName(), "run", ioe);
                    } catch (IllegalArgumentException iae) {
                        logger.throwing(getClass().getName(), "run", iae);
                    }
                    
                    new Task() {
                        protected void execute() {
                            JOptionPane.showMessageDialog(mainFrame, "The Current File Saved successfully.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }.start();
                    mainFrame.testAndToggleSemaphore(true);
                }
            }).start();
        } catch (Exception ie){
            logger.warning("Caught excpetion (ignoring:"+ie);
        }
        
        
    }
}

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


import java.io.File;
import java.io.IOException;

import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jvnet.olt.editor.util.NestableException;
import org.jvnet.olt.editor.util.Task;


/**
 *
 * @author  jc73554
 */

//TODO break down: file selection and preparation to MainFrame;
// replace saving with SaveCurrentFileThread

//Changes Jul 11 2006
//Although this class is called Thread it is not Runnable anymore. Due to deadlocks
//caused by this thread when calling UI I changed it create a new thread only when
//doing actual save. This thread uses Task class to trigger any UI changes
public class SaveAsFileThread {
    private static final Logger logger = Logger.getLogger(SaveAsFileThread.class.getName());
    private MainFrame frame;
    private Backend backend;
    
    /** Creates a new instance of SaveAsFileThread */
    public SaveAsFileThread(MainFrame mainFrame, Backend backend) {
        this.frame = mainFrame;
        this.backend = backend;
    }
/*    
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
        
        int iType = 0; //0 for xlz, 1for xlf
        
        File curFile = backend.getCurrentFile();
        
        
        
        if(OpenFileFilters.isFileNameXLZ(curFile)){
            //if (curFile.getName().endsWith(".xlz") == true) {
            iType = 0;
            f.addChoosableFileFilter(OpenFileFilters.XLZ_FILTER);
            f.setFileFilter(OpenFileFilters.XLZ_FILTER);
        } else if (OpenFileFilters.isFileNameXLF(curFile)) {
            iType = 1;
            f.addChoosableFileFilter(OpenFileFilters.XLF_FILTER);
            f.setFileFilter(OpenFileFilters.XLF_FILTER);
        }
        
        String fname = backend.getConfig().getStrLastFile();
        f.setCurrentDirectory(new File(fname).getParentFile());
        f.setSelectedFile(curFile);
        
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
                
                try{
                    backend.saveFileTo(xFile);
                    frame.setTitle(Constants.TOOL_NAME + " - " + frame.backend.getProject().getProjectName() + "-" + fSaveAsFile.getAbsolutePath());
                } catch (NestableException ne){
                    JOptionPane.showMessageDialog(frame, "Failed to Save As the Current File", "Failed to Save", JOptionPane.OK_OPTION);
                }
                break;
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
        
        // TODO remove
        // Thread newThread = new CpFileToTempFile(frame, backend);
        // newThread.run();
        
        //  Lower a semaphore
        if (!frame.testAndToggleSemaphore(true)) {
            logger.severe("Error: attempt made to lower an already lowered semaphore flag.");
            
            //TODO throw Exception???
        }
        
        //  Give the interface back to the user
        frame.enableGUI();
        JOptionPane.showMessageDialog(frame, "The Current File Saved successfully as '" + curFile + "'.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }
    */
    public void save() {
        //  Raise a semaphore
        if (!frame.testAndToggleSemaphore(false)) {
            JOptionPane.showMessageDialog(frame, "<html>There is currently a Save operation taking place.<br> Please wait until the current save has finished before trying to save again.", "Save In Progress", JOptionPane.ERROR_MESSAGE);
            
            //  display error message
            return;
        }
        
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();
        
        
        final File curFile = backend.getCurrentFile();
        
        final File fSaveAsFile = selectTargetFile(curFile);
        //logger.finer(fSaveAsFile.getAbsolutePath());
        final File xFile = new File(fSaveAsFile.getAbsolutePath());
        
        try{
            new Thread(new Runnable(){
                public void run() {
                    if (!frame.copyFile(curFile, fSaveAsFile)) {
                        new Task(true) {
                            protected void execute() {
                                JOptionPane.showMessageDialog(frame, "Failed to Save the target File", "Failed to Save", JOptionPane.OK_OPTION);
                            }
                        }.start();
                    } else{
                        try{
                            backend.saveFileTo(xFile);
        

                            new Task(true) {
                                protected void execute() {
                                    if ((curFile != null) && (fSaveAsFile != null) && !curFile.getName().equals(fSaveAsFile.getName())) {
                                        //curFile = fSaveAsFile;
                                        frame.resetFileHistory();
                                    }
                                    frame.setTitle(Constants.TOOL_NAME + " - " + frame.backend.getProject().getProjectName() + "-" + fSaveAsFile.getAbsolutePath());
                                    frame.setBHasModified(false);
                                }
                            }.start();
                            
                        } catch (NestableException ne){
                            frame.exceptionThrown(ne,MainFrame.OPERATION_SAVE);
                            frame.setBHasModified(true); //  Re-enable the save options.
                            frame.testAndToggleSemaphore(true);
                            return;
                        } finally{
                            new Task() {
                                protected void execute() {
                                    frame.enableGUI();
                                }
                            }.start();
                        }
                    }
                    
                    
                    try {
                        backend.copyCurrentFileToTemp();
                    } catch (IOException ioe) {
                        logger.throwing(getClass().getName(), "success", ioe);
                    } catch (IllegalArgumentException iae) {
                        logger.throwing(getClass().getName(), "success", iae);
                    }
                    
                    new Task() {
                        protected void execute() {
                            frame.enableGUI();
                            JOptionPane.showMessageDialog(frame, "The Current File Saved successfully as '" + curFile + "'.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }.start();
                    
                    frame.testAndToggleSemaphore(true);
                }
            }).start();
            
        } catch (Exception e){
            
        }        
    }
    
    File selectTargetFile(File curFile){
        Object[] message = new Object[2];
        message[0] = "Please select a .xlz or .xlf file to open:";
        
        JFileChooser f = new JFileChooser();
        f.setMultiSelectionEnabled(false);
        f.setAcceptAllFileFilterUsed(false);
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int iType = 0; //0 for xlz, 1for xlf
        
        ;
        
        if(OpenFileFilters.isFileNameXLZ(curFile)){
            //if (curFile.getName().endsWith(".xlz") == true) {
            iType = 0;
            f.addChoosableFileFilter(OpenFileFilters.XLZ_FILTER);
            f.setFileFilter(OpenFileFilters.XLZ_FILTER);
        } else if (OpenFileFilters.isFileNameXLF(curFile)) {
            iType = 1;
            f.addChoosableFileFilter(OpenFileFilters.XLF_FILTER);
            f.setFileFilter(OpenFileFilters.XLF_FILTER);
        }
        
        String fname = backend.getConfig().getStrLastFile();
        f.setCurrentDirectory(new File(fname).getParentFile());
        f.setSelectedFile(curFile);
        
        message[1] = f;
        String[] options = { "OK", "Cancel" };
        
        File fSaveAsFile = null;
        int result = f.showDialog(frame, "Save As");
        
        if(result == JFileChooser.APPROVE_OPTION){
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
            
            return fSaveAsFile;
        }
        
        return null;
    }
    
    
}

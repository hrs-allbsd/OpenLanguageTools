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
 * SaveAsFileThread.java
 *
 * Created on 20 November 2003, 13:35
 */
package org.jvnet.olt.editor.translation;


import java.io.File;
import java.io.IOException;
import org.jvnet.olt.editor.util.Bundle;

import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jvnet.olt.editor.util.NestableException;


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
    private Bundle bundle = Bundle.getBundle(SaveAsFileThread.class.getName());


    /** Creates a new instance of SaveAsFileThread */
    public SaveAsFileThread(MainFrame mainFrame, Backend backend) {
        this.frame = mainFrame;
        this.backend = backend;
    }

    public void run() {
        //  Raise a semaphore
        if (!frame.testAndToggleSemaphore(false)) {
            JOptionPane.showMessageDialog(frame, bundle.getString("<html>There_is_currently_a_Save_operation_taking_place.<br>_Please_wait_until_the_current_save_has_finished_before_trying_to_save_again."), bundle.getString("Save_In_Progress"), JOptionPane.ERROR_MESSAGE);

            //  display error message
            return;
        }

        frame.setBHasModified(false);

        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

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

        File fSaveAsFile = null;
        int result = f.showDialog(frame, bundle.getString("Save_As"));

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
                JOptionPane.showMessageDialog(frame, bundle.getString("Failed_to_Save_the_target_File"), bundle.getString("Failed_to_Save"), JOptionPane.OK_OPTION);

                break;
            }

            try{
                backend.saveFileTo(xFile);
                frame.setTitle(Constants.TOOL_NAME + " - " + frame.backend.getProject().getProjectName() + "-" + fSaveAsFile.getAbsolutePath());
            }
            catch (NestableException ne){
                JOptionPane.showMessageDialog(frame, bundle.getString("Failed_to_Save_As_the_Current_File"), bundle.getString("Failed_to_Save"), JOptionPane.OK_OPTION);
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
        JOptionPane.showMessageDialog(frame, bundle.getString("The_Current_File_Saved_successfully_as_'") + curFile + "'.", bundle.getString("Save_Successful"), JOptionPane.INFORMATION_MESSAGE);
    }
}

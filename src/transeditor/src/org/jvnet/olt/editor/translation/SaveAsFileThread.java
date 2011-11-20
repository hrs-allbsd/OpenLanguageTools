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

import javax.swing.JOptionPane;
import org.jvnet.olt.editor.util.NestableException;


/**
 *
 * @author  jc73554
 */

// TODO break down:
// replace saving with SaveCurrentFileThread
public class SaveAsFileThread implements Runnable {
    private static final Logger logger = Logger.getLogger(SaveAsFileThread.class.getName());
    private MainFrame frame;
    private Backend backend;
    private File fSaveAsFile;
    private Bundle bundle = Bundle.getBundle(SaveAsFileThread.class.getName());


    /** Creates a new instance of SaveAsFileThread */
    public SaveAsFileThread(MainFrame mainFrame, Backend backend, File file) {
        this.frame = mainFrame;
        this.backend = backend;
        this.fSaveAsFile = file;
    }

    public void run() {
        frame.stopEditing();

        //  Raise a semaphore
        if (!frame.testAndToggleSemaphore(false)) {
            JOptionPane.showMessageDialog(frame, bundle.getString("<html>There_is_currently_a_Save_operation_taking_place.<br>_Please_wait_until_the_current_save_has_finished_before_trying_to_save_again."), bundle.getString("Save_In_Progress"), JOptionPane.ERROR_MESSAGE);

            //  display error message
            return;
        }

        frame.setBHasModified(false);

        //AlignmentMain.testMain1.stopEditing();
        //AlignmentMain.testMain2.stopEditing();


        File curFile = backend.getCurrentFile();

        
        
            frame.disableGUI();

            //logger.finer(fSaveAsFile.getAbsolutePath());
            File xFile = new File(fSaveAsFile.getAbsolutePath());

            if (!frame.copyFile(curFile, fSaveAsFile)) {
                JOptionPane.showMessageDialog(frame, bundle.getString("Failed_to_Save_the_target_File"), bundle.getString("Failed_to_Save"), JOptionPane.OK_OPTION);

            }
            else {


            try{
                backend.saveFileTo(xFile);
                frame.setTitle(Constants.TOOL_NAME + " - " + frame.backend.getProject().getProjectName() + "-" + fSaveAsFile.getAbsolutePath());
            }
            catch (NestableException ne){
                JOptionPane.showMessageDialog(frame, bundle.getString("Failed_to_Save_As_the_Current_File"), bundle.getString("Failed_to_Save"), JOptionPane.OK_OPTION);
            }

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

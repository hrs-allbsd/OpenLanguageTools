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
 * CpFileToTempFile.java
 *
 * Created on 21 November 2003, 11:05
 */
package org.jvnet.olt.editor.translation;

import java.io.File;

import java.util.logging.Logger;


/**
 *
 * @author  jc73554
 *
 * @deprecated
 */
public class CpFileToTempFile extends Thread {
    private static final Logger logger = Logger.getLogger(CpFileToTempFile.class.getName());

    /*
        private MainFrame mainFrame;
        private Backend backend;

        public CpFileToTempFile(MainFrame mainFrame,Backend backend) {
            this.mainFrame = mainFrame;
            this.backend = backend;
        }


        public void run() {
            File f = backend.getCurrentFile();
            if(f.getAbsolutePath().toLowerCase().endsWith(".xlz") == true) {
                mainFrame.strTempFileName = mainFrame.strHome+File.separator+"Temp.xlz";
            }
            else {
                mainFrame.strTempFileName = mainFrame.strHome+File.separator+"Temp.xlf";
            }

            if (!mainFrame.copyFile(f, new File(mainFrame.strTempFileName))) {
                logger.severe("Copy failed:" + f.getAbsolutePath() + " TO " + mainFrame.strTempFileName);

                //TODO throw an exception ???
            } else {
                //loggger.finer("Copy OK:" + currentFile.getAbsolutePath() + " TO " + strTempFileName);
            }

            mainFrame.bSafeExit = false;
            mainFrame.saveConfig();
        }
     */
}

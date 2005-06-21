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

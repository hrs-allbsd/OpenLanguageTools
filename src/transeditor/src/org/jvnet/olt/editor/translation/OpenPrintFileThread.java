/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * OpenPrintFileThread.java
 *
 * Created on 21 November 2003, 10:37
 */
package org.jvnet.olt.editor.translation;

import java.io.File;


/**
 *
 * @author  jc73554
 */
public class OpenPrintFileThread implements Runnable {
    private String strHome;

    public OpenPrintFileThread(String strHome) {
        this.strHome = strHome;
    }

    //TODO specify browser AND use BROWSER env variable for unixes
    public void run() {
        String fileName = strHome + File.separator + "print.html";
        String[] cmd = null;
        Process proc = null;

        try {
            if (File.separator.equals("/")) {
                cmd = new String[2];
                cmd[0] = "netscape";
                cmd[1] = fileName;
                proc = Runtime.getRuntime().exec(cmd);
            } else {
                // bug: 4715337
                String strPro = System.getProperty("os.name");

                if (strPro.endsWith("98") || strPro.endsWith("95") || strPro.endsWith("ME")) {
                    cmd = new String[4];
                    cmd[0] = "command.com";
                    cmd[1] = "/c";
                    cmd[2] = "start";
                    cmd[3] = fileName;
                } else {
                    cmd = new String[3];
                    cmd[0] = "cmd.exe";
                    cmd[1] = "/c";
                    cmd[2] = fileName;
                }

                proc = Runtime.getRuntime().exec(cmd);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //tmpDlgForPrinting.setVisible(false);
    }
}

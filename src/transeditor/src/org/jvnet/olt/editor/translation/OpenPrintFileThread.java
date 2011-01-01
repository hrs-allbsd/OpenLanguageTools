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
 * OpenPrintFileThread.java
 *
 * Created on 21 November 2003, 10:37
 */
package org.jvnet.olt.editor.translation;

import java.io.File;
import java.io.IOException;


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
                String[] possibleBrowsers = new String[] {
                    "firefox",
                    "iceweasel",
                    "netscape",
                };
                cmd = new String[2];
                cmd[0] = "netscape";
                cmd[1] = fileName;
                for (int i = 0; i < possibleBrowsers.length; i++) {
                    try {
                        cmd[0] = possibleBrowsers[i];
                        proc = Runtime.getRuntime().exec(cmd);
                        break;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (proc == null) {
                    throw new Exception("no supported browser found");
                }
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
    }
}

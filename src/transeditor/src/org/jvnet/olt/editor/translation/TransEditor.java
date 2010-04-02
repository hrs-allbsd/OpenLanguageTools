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
package org.jvnet.olt.editor.translation;

import java.io.File;

import java.util.logging.Logger;


/**
 * This class is just the entry of this tool and it will load the
 * main window,MainFrame.
 */
public class TransEditor {
    private static final Logger logger = Logger.getLogger(TransEditor.class.getName());

    private TransEditor() {
    }

    //Main method
    public static void main(String[] args) {
        TransEditor editor = new TransEditor();
        editor.run();
    }

    void run() {
        File home = new File(System.getProperty("user.home") + File.separator + ".xliffeditor" + File.separator);

        File minitmDir = new File(home, "mini-tm");

        logger.finer("Editor version:" + Constants.EDITOR_VERSION);
        logger.finer("User home dir:" + home);
        logger.finer("Mini-tm dir:" + minitmDir);

        try {
            Configuration cfg = new Configuration(home, minitmDir);
            logger.finer("Loading config...");
            cfg.load();
            logger.finer("Loaded");

            logger.finer("Loading backend...");

            Backend backend = Backend.instance(cfg);
            logger.finer("Loaded");

            logger.finer("starting...");

            MainFrame frame = new MainFrame(backend);
            frame.run();
        } catch (Throwable t) {
            logger.throwing(getClass().getName(), "run", t);
            logger.severe("Exception:" + t);
        }
    }
}

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
            logger.finer("Lodaing config...");
            cfg.load();
            logger.finer("Loaded");

            logger.finer("Lodaing backend...");

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

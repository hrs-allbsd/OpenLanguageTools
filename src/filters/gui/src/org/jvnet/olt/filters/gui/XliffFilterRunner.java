
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.gui;

import java.awt.Component;
import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * A simple class that implements Runnable to kick off XLIFF conversion for
 * the file passed in the constructor. 
 * @author timf
 */
public class XliffFilterRunner implements Runnable {
    
    private File fileToConvert = null;
    private XliffFilterFacade filterFacade = null;
    private Logger logger = null;
    private Map attributes = null;
    private XliffFilterGUI gui = null;
    
    /**
     * Does XLIFF conversion of the input job - uses XliffFilterFacade to do
     * the work.
     * @param fileToConvert the file we want to convert to XLIFF
     * @param attributes the attributes we want to pass to the conversion job
     * @param logger a means to log debug and other messages
     * @param gui used to pop up a completion dialog box
     */
    public XliffFilterRunner(File fileToConvert, Map attributes, Logger logger, XliffFilterGUI gui) {
        this.fileToConvert = fileToConvert;
        this.filterFacade = new XliffFilterFacade(attributes, logger);
        this.attributes = attributes;
        this.logger = logger;
        this.gui = gui;
    }
    
    public void run(){
        try {
            this.filterFacade.convert(fileToConvert, this.attributes, this.logger);
            //this.gui.showCompletionDialog(fileToConvert, true);
        } catch (XliffFilterFacadeException e){
            this.logger.log(Level.WARNING,"Problem converting file "+e.getMessage());
            //this.gui.showCompletionDialog(fileToConvert, false, e.getMessage());
        }
    }

}

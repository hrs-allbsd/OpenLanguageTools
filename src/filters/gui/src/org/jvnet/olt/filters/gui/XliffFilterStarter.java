
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.gui;

import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This simple starter kicks off a thread that processes all jobs.
 *
 * It uses a UICallback  istance to inform the UI about the progress of the processing
 * @author timf
 */
public class XliffFilterStarter  {
    
    private Logger logger = null;
    private Map attributes = null;
    private UICallback gui = null;
    private List fileList = null;
    
    private int allFiles;
    private int failedFiles;
    
    private XliffFilterFacade filterFacade = null;

    private static final java.util.ResourceBundle xliffFilterGUIMessages = java.util.ResourceBundle.getBundle("org/jvnet/olt/filters/gui/XliffFilterGUIMessages");
    
    /**
     * Creates a new instance of XliffFilterStarter
     * @param fileList list of java.io.FIle instances
     * @param attributes Conversion attributes
     * @param logger java.util.Logger instance to use for logging to the UI
     * @param gui UICallback instance
     */
    public XliffFilterStarter(List fileList, Map attributes, Logger logger, UICallback gui)  {
        this.fileList = fileList;
        this.attributes = attributes;
        this.logger = logger;
        this.gui = gui;
        
        this.filterFacade = new XliffFilterFacade(attributes, logger);
    }
    
    
    
    /**
     * starts a thread that runs conversions
     */
    public synchronized void runFilters(){
        new Thread(new Runnable(){
            public void run(){
                synchronized(XliffFilterStarter.this){
                    allFiles = fileList.size();
                    
                    gui.start();
                    for(Iterator it = fileList.iterator(); it.hasNext();){
                        
                        File f = (File)it.next();
                        if (! f.isFile() || !f.canRead()){
                            // File f.getAbsolutePath() is not readable, or is a
                            // directory. This file will NOT be converted
                            logger.log(Level.WARNING,MessageFormat.format(
                                    xliffFilterGUIMessages.getString("File_o_is_not_readable,_or_is_a_directory._This_file_will_NOT_be_converted"),
                                    new Object[] {f.getAbsolutePath()}));
                            failedFiles++;
                        } else{
                            runFile(f);
                        }
                    }
                    gui.doneAll(failedFiles);
                    
                }
            }
        }).start();
    }
    
    
    /**
     * process one file
     * @param f
     */
    private void runFile(File f){
        boolean failed = false;
        try {
            gui.startFile(f);
            
            filterFacade.convert(f, attributes, logger);
            
        } catch (XliffFilterFacadeException e){
            // Problem converting file f.getAbsolutePath() : e.getMessage()
            this.logger.log(Level.WARNING,
                    MessageFormat.format(
                    xliffFilterGUIMessages.getString("Problem_converting_file_o"),
                    new Object[] {f.getAbsolutePath(), e.getMessage()}));
            failed = true;
            failedFiles++;
        } finally {
            gui.doneFile();
            if (failed){
                logger.info(xliffFilterGUIMessages.getString("Conversion_FAILED"));
            } else {
                logger.info(xliffFilterGUIMessages.getString("Conversion_SUCCESSFUL"));
            }
        }
    }
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.gui;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

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
                            logger.log(Level.WARNING,"File "+f.getAbsolutePath()+" is not readable, or is a directory. This file will NOT be converted");
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
            this.logger.log(Level.WARNING,"Problem converting file "+f.getAbsolutePath()+":"+e.getMessage());
            failed = true;
            failedFiles++;
        } finally {
            gui.doneFile();
            
            logger.info("Conversion " + (failed ? "FAILED" : "SUCCESSFUL"));
        }
    }
}

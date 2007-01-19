/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

import org.jvnet.olt.editor.model.*;
import org.jvnet.olt.util.FileUtils;
import org.jvnet.olt.editor.util.NestableException;
import org.jvnet.olt.util.FileUtils;
import org.jvnet.olt.xliff.*;
import org.jvnet.olt.minitm.*;


/*
 * Backend.java
 *
 * Created on March 22, 2005, 12:34 PM
 *
 */

/**
 *
 * @author boris
 */
public class Backend {
    private static final Logger logger = Logger.getLogger(Backend.class.getName());
    public static final String PROPERTY_PROJECT = "PROPERTY_PROJECT_RELOADED";
    private static Backend instance = null;
    private File currentFile = null;
    private File currentTemp = null;
    private TMData tmpdata;
    private XLIFFParser xp;
    private TransProject project;
    
    //if the fila has been modified
    private boolean bHasModified;
    private Configuration config;
    private List projPropLstnrs = new LinkedList();
    
    private boolean saveInProgress = false;
    
    //Creates a new instance of Backend
    private Backend(Configuration cfg) {
        this.config = cfg;
    }
    
    public XLIFFParser getXLIFFParser() {
        return xp;
    }
    
    public TMData getTMData() {
        return tmpdata;
    }
    
    //TODO remove all references to instance() and remove both instance methods
    static synchronized public Backend instance() {
        if (instance == null) {
            throw new RuntimeException("backend was not properly instantiated");
        }
        
        return instance;
    }
    
    static synchronized public Backend instance(Configuration cfg) {
        if (instance != null) {
            throw new RuntimeException("Backend has already been instantiated");
        }
        
        instance = new Backend(cfg);
        
        return instance;
    }
    
    public void openFile(File f) throws IOException {
    }
    
    public boolean saveFile() throws NestableException {
        //        if(!bHasModified)
        //            return false;
        for (int i = 0; i < tmpdata.tmsentences.length; i++) {
            boolean autosave = config.isBFlagAutoSave();
            
            if (!autosave && tmpdata.bMiniTMFlags[i]) {
                tmpdata.tmsentences[i].updateMiniTM(config.getTranslatorID(), i);
                tmpdata.bMiniTMFlags[i] = false;
            }
        }
        
        //        try{
        return saveFileTo(currentFile);
        
        /*        }
                catch (IOException ioe){
                    //TODO throw an Exception
                    logger.throwing(getClass().getName(), "saveFile", ioe);
                    logger.severe("Exception:"+ioe);
         
         
                    return false;
         
                }
         */
    }
    
    public boolean copyCurrentFileToTemp() throws IOException, IllegalArgumentException {
        File tempFile = ensureTempFile();
        
        if (tempFile == null) {
            logger.warning("tempFile is null");
            
            return false;
        }
        
        FileUtils.copyFileNoEncode(currentFile, tempFile);
        
        return true;
    }
    
    public boolean saveFileToTemp() throws NestableException {
        if (currentFile == null) {
            logger.warning("currentFile is null while doing autosave; file will not be saved");
            
            return false;
        }
        
        File tempFile = ensureTempFile();
        
        logger.finer((("will autosave to file:" + tempFile) == null) ? "null!!!!" : tempFile.getAbsolutePath());
        
        return doSaveFile(tempFile, true);
    }
    
    public boolean saveFileTo(File newFile) throws NestableException {
        boolean rv = doSaveFile(newFile, false);
        if(!newFile.equals(currentFile)){
            currentFile = newFile;
        }
        return rv;
    }
    
    private boolean doSaveFile(File newFile, boolean autosave)
        throws IllegalStateException,NestableException {
        if ((newFile == null) || (tmpdata == null)) {
            return false;
        }
        
        logger.finest("Will toggle save in progress");
        
        synchronized(this){
            if(saveInProgress)
                throw new IllegalStateException("Save in progress");
            saveInProgress = true;
        }
        
        logger.finest("Thread "+Thread.currentThread().getName()+ " saving file");
        
        
        try {
            boolean writeProtected = config.isBFlagWriteProtection();
            
            
            boolean anySave = tmpdata.saveAllTranslations(autosave, writeProtected);
            
            //if anySave while autosave ->save
            //if !autosave save anyways
            if( (autosave && anySave) || (!autosave) ){
                xp.saveToFile(newFile,autosave);
            } else
                logger.finer("No changes found will not save" );
            
            
            return true;
        } catch (NestableException ne) {
            //TODO throw an Exception
            logger.throwing(getClass().getName(), "saveFileTo", ne);
            logger.severe("Exception:" + ne);
            
            throw ne;
        } finally{
            synchronized(this){
                logger.finest("Will toggle save in progress off");
                
                saveInProgress = false;
            }
        }
    }
    
    public void setTranslatorID(String translatorId) {
        config.setTranslatorID(translatorId);
    }
    
    public String getTranslatorID() {
        return config.getTranslatorID();
    }
    
    public File getCurrentFile() {
        return (currentFile == null) ? null : new File(currentFile.getAbsolutePath());
    }
    
    public boolean hasCurrentFile() {
        return !(currentFile == null);
    }
    
    //Close current file
    public void close() {
        tmpdata = null;
        currentFile = null;
        
        if (currentTemp != null) {
            if (currentTemp.exists()) {
                currentTemp.delete();
            }
            
            currentTemp = null;
        }
        
        config.setStrTempFileName(null);
        
        try {
            config.save();
        } catch (ConfigurationException cfge) {
            logger.throwing(getClass().getName(), "close", cfge);
            logger.severe("While closing file -- WILL BE IGNORED");
        }
        
        //TODO what about xp ???
    }
    
    public TransProject getProject() {
        return project;
    }
    
    //TODO trigger all sorts of property changes here;
    void setTMData(TMData data) {
        this.tmpdata = data;
    }
    
    void setCurrentFile(File f) {
        this.currentFile = f;
    }
    
    //TODO make sure we close xp and set currentFile to null!!!
    public void openProject(TransProject tp) {
        if (this.project != null) {
            this.project.closeProject();
        }
        
        TransProject oldProject = this.project;
        
        this.project = tp;
        this.tmpdata = null;
        this.xp = null;
        
        //trigger event
        fireProjectPropertyChangedListener(new PropertyChangeEvent(this, PROPERTY_PROJECT, oldProject, tp));
        
        //Manage history
        config.addToProjectHsistory(tp);
    }
    
    void setXLIFFParser(XLIFFParser xp) {
        this.xp = xp;
    }
    
    public void resetBeforeOpen() {
        //reset before trying to open new one
        xp = null;
        currentFile = null;
        tmpdata = null;
    }
    
    public void tryToOpenFile(File f, OpenFileThread.PostXLIFFOpenHandler postHandler) {
        resetBeforeOpen();
        
        new OpenFileThread(f, this, postHandler).start();
    }
    
    /* methods delegating to Configuration */
    public Configuration getConfig() {
        return config;
    }
    
    void setConfig(Configuration cfg) {
        if (config != null) {
            throw new IllegalArgumentException("cfg already set");
        }
        
        this.config = cfg;
    }
    
    void shutdown() {
        close();
        
        if (project != null) {
            try {
                MiniTM tm = project.getMiniTM();
                if(tm!=null) {
                    tm.close();
                }
            } catch(MiniTMException e) {
                logger.severe("Cannot close miniTM: " + e.getMessage());
            }
            project.closeProject();
        }
    }
    
    public void addProjectPropertyChangedListener(PropertyChangeListener lstnr) {
        if (lstnr == null) {
            return;
        }
        
        projPropLstnrs.add(lstnr);
    }
    
    private void fireProjectPropertyChangedListener(PropertyChangeEvent evt) {
        if (evt == null) {
            return;
        }
        
        for (Iterator i = projPropLstnrs.iterator(); i.hasNext();) {
            ((PropertyChangeListener)i.next()).propertyChange(evt);
        }
    }
    
    public void removeProjectPropertyChangedListener(PropertyChangeListener lstnr) {
        if (lstnr == null) {
            return;
        }
        
        projPropLstnrs.remove(lstnr);
    }
    
    public List getAllProjects() {
        final File miniTmDir = config.getMiniTMDir();
        String[] projects = miniTmDir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return dir.equals(miniTmDir) && name.toLowerCase().endsWith(".mtm");
            }
        });
        
        List rv = new LinkedList();
        
        for (int i = 0; (projects != null) && (i < projects.length); i++) {
            //cut off the .mtm part
            String x = projects[i].substring(0, projects[i].lastIndexOf('.'));
            String[] parts = x.split("_");
            
            if (parts.length != 3) {
                continue;
            }
            
            rv.add(new ProjectInfo(parts[0], parts[1], parts[2]));
        }
        
        return rv;
    }
    
    public boolean canRevertFromTempFile() {
        if (currentTemp != null) {
            return true;
        }
        
        if ((config.getStrTempFileName() != null) && (config.getStrTempFileName().trim().length() > 0)) {
            return true;
        }
        
        return false;
    }
    
    private File ensureTempFile() {
        if (currentTemp != null) {
            return currentTemp;
        }
        
        //current temp file is null; check config:
        // if it exists in config use it
        String lastTempFile = config.getStrTempFileName();
        
        if ((lastTempFile != null) && (lastTempFile.trim().length() > 0)) {
            currentTemp = new File(lastTempFile);
            
            return currentTemp;
        }
        
        //we need to create new Temp File
        int dot = currentFile.getName().lastIndexOf('.');
        String fname = null;
        
        //strange. should have had a dot in file name
        if (dot == -1) {
            fname = "Temp.xlf";
        } else {
            String name = currentFile.getName();
            fname = name.substring(0, dot) + "Temp" + name.substring(dot);
        }
        
        currentTemp = new File(config.getHome(), fname);
        
        //make sure we save the file
        config.setStrTempFileName(currentTemp.getAbsolutePath());
        
        try {
            config.save();
        } catch (ConfigurationException cfge) {
            logger.throwing(getClass().getName(), "ensureTempFile", cfge);
            logger.severe("While making temp file -- WILL BE IGNORED");
        }
        
        return currentTemp;
    }
    
    
    
}

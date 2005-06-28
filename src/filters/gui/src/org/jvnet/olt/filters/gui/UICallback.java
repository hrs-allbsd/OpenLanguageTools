
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * UICallback.java
 *
 * Created on May 22, 2005, 11:58 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jvnet.olt.filters.gui;

import java.io.File;

/**
 * Notification callback for convertor thread.
 * 
 * Convertor thread uses this callback to notify the UI of the progress of conversion
 * @author boris
 */
public interface UICallback {
 
    /**
     * Called when new batch starts
     */
    public void start();
    
    /**
     * Called before the start of processing of a file
     * @param f File to process
     */
    public void startFile(File f);
    
    /**
     * called after file conversion has fiinished
     */
    public void doneFile();
    
    /**
     * Called after the last file processing finished
     * @param failed number of files failed
     */
    public void doneAll(int failed);
    
    
        
}

/*
 * Task.java
 *
 * Created on July 18, 2005, 4:42 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jvnet.olt.editor.util;

import javax.swing.SwingUtilities;

/**
 *
 * @author boris
 */
abstract public class Task {
    
    /** Creates a new instance of Task */
    public Task() {
    }
    
    abstract protected void execute();
    
    public void start(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                execute();
            }
        });
    }
}

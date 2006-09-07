/*
 * Renamer.java
 *
 * Created on September 6, 2006, 3:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.renamer;

import javax.swing.SwingUtilities;

/**
 
 @author boris
 */
public class Renamer {
    
    /** Creates a new instance of Renamer */
    public Renamer() {
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                RenamerFrame f = new RenamerFrame();
                f.setVisible(true);
            }
            
        });
    }
}

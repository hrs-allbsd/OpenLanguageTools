/*
 * SessionStartException.java
 *
 * Created on November 16, 2006, 2:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

/**
 *
 * @author boris
 */
public class SessionStartException extends Exception{
    
    /** Creates a new instance of SessionStartException */
    public SessionStartException() {
    }
    
    public SessionStartException(Throwable t){
        super(t);
    }

    public SessionStartException(String msg){
        super(msg);
    }
}

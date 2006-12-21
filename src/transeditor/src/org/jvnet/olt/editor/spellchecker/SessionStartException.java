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
    private String command;
    /** Creates a new instance of SessionStartException */
    public SessionStartException(String command) {
        this.command = command;
    }
    
    public SessionStartException(String command,Throwable t){
        super(t);
        this.command = command;
    }

    public SessionStartException(String command,String msg){
        super(msg);
        this.command = command;
    }
    
    public String getCommand(){
        return command;
    }
}

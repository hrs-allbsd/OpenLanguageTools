/*
 * SpellCheckerCreationException.java
 *
 * Created on November 30, 2006, 1:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

/**
 *
 * @author boris
 */
public class SpellCheckerCreationException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>SpellCheckerCreationException</code> without detail message.
     */
    public SpellCheckerCreationException() {
    }
    
    
    /**
     * Constructs an instance of <code>SpellCheckerCreationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SpellCheckerCreationException(String msg) {
        super(msg);
    }

    public SpellCheckerCreationException(Throwable t) {
        super(t);
    }
}

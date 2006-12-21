/*
 * DefaultSuggestion.java
 *
 * Created on November 16, 2006, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

/**
 *
 * @author boris
 */
public class DefaultSuggestion implements Suggestion {
    final private String word;
    
    /** Creates a new instance of DefaultSuggestion */
    public DefaultSuggestion(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public String toString() {
        return word;
    }
    
    
}

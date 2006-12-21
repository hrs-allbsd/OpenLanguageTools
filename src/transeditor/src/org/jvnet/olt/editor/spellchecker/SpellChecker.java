/*
 * SpellChecker.java
 *
 * Created on November 16, 2006, 1:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

import java.util.Properties;
import java.util.prefs.Preferences;
import javax.swing.JComponent;

/**
 *
 * @author boris
 */
public abstract class SpellChecker {
    
    private SpellCheckerFactory factory;
    
    public interface Session {
        
        public Suggestion[] getSuggestions(String word);
        
        public void wordAccpeted(String s);
        
        public void addToPersonal(String s);
        
        public void ignoreWord(String word,boolean alwaysIgnore);
        
        public boolean close();
    }
    
    protected final Properties props;
    
    
    protected SpellChecker(Properties props){
        this.props = props;
    }
    
    
    public static SpellChecker create(Properties props){
        return null;
    }
    
    abstract public Session startSession(String language) throws SessionStartException;
    
    abstract public boolean endSession(Session sess);
    
    abstract public String[] getSupportedLanguages();
    
    protected void storeConfig(Preferences prefs){
        
    }
    
    protected void loadConfig(Preferences prefs){
        
    }
    /** UI stuff
     *
     */
    abstract public String getName();
    
    abstract public String getDisplayName();
    
    public JComponent getCustomizer(){
        return null;
    }

    protected void setFactory(SpellCheckerFactory factory){
        this.factory = factory;
    }
    
    public void release(){
        factory.releaseSpellChecker(this);
    }
}

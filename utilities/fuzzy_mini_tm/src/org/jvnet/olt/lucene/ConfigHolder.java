/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.lucene;

/**
 * Holds configuration for LuceneMiniTM. 
 *
 */
public class ConfigHolder {
    
    String nameTM = "";
    String sourceLang = "";
    String targetLang = "";
    long lastID = 1;
    
    /**
     * Create new instance of LuceneConfigHolder
     */
    public ConfigHolder() {
    }
    
    /**
     * Create new instance of LuceneConfigHolder
     *
     * @param nameTM the name of the TM
     * @param sourceLang the source language
     * @param targetLang the target language
    public ConfigHolder(String nameTM,String sourceLang,String targetLang) {
        this.nameTM = nameTM;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        setLastID(1);
    }
     */
    
    /**
     * Setter for name attribute
     *
     * @param nameTM the name of the TM
     */
    public void setNameTM(String nameTM) {
        this.nameTM = nameTM;
    }
    
    /**
     * Getter for name attribute
     *
     * @return the name of the TM
     */
    public String getNameTM() {
        return nameTM;
    }
    
    /**
     * Setter for source language attribute
     *
     * @param sourceLang the source language
     */
    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }
    
    /**
     * Getter for source language attribute
     *
     * @return the source language
     */
    public String getSourceLang() {
        return sourceLang;
    }
    
    /**
     * Setter for target language
     *
     * @param targetLang the target language
     */
    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }
    
    /**
     * Getter for target language attribute
     *
     * @return the target language attribute
     */
    public String getTargetLang() {
        return targetLang;
    }
    
    /**
     * Setter for lastID attribute
     *
     * @param lastID the last used id in miniTM
     */
    public void setLastID(long lastID) {
        this.lastID = lastID;
    }
    
    /**
     * Getter for lastID attribute
     *
     * @return lastID user in miniTM
     */
    public long getLastID() {
        return lastID;
    }
    
}

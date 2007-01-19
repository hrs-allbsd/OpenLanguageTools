/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.fuzzy;

import org.jvnet.olt.minitm.MiniTM;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import org.jvnet.olt.fuzzy.basicsearch.*;
import org.jvnet.olt.minitm.MiniTMException;

/**
 * Factory to create MiniTM object
 */
public final class MiniTMFactory {
    
    private static final Map fileTypes = Collections.synchronizedMap(new HashMap());
   
    // supported datatypes
    private static final int SGML       = 1;
    private static final int HTML       = 2;
    private static final int XML        = 3;
    private static final int JSP        = 4;
    private static final int PO         = 11;
    private static final int JAVA       = 12;
    private static final int MSG        = 13;
    private static final int PROPERTIES = 14;
    private static final int PLAINTEXT  = 15;
    private static final int DTD        = 16;
    private static final int UNKNOWN     = 17;
    
    static {
        fileTypes.put("SGML",       new Integer(SGML));
        fileTypes.put("HTML",       new Integer(HTML));
        fileTypes.put("XML",        new Integer(XML));
        fileTypes.put("JSP",        new Integer(JSP));
        
        fileTypes.put("PO",         new Integer(PO));
        fileTypes.put("JAVA",       new Integer(JAVA));
        fileTypes.put("MSG",        new Integer(MSG));
        fileTypes.put("PROPERTIES", new Integer(PROPERTIES));
        fileTypes.put("DTD",        new Integer(DTD));
        fileTypes.put("PLAINTEXT",  new Integer(PROPERTIES));
        fileTypes.put("UNKNOWN",    new Integer(UNKNOWN));
    }
    
    // do not allow to create any instance
    private MiniTMFactory() {
    }
    
    /**
     * Factory methods for opening MiniTM store
     *
     * @param engine - type of mini tm
     * @param tmParam - params of tm
     * @param fileType - the file type of openned file
     *
     * @return the MiniTM object
     *
     * @throws MiniTM exception if any error occure
     */
    public static MiniTM createMiniTM(Engine engine,Param tmParam, String fileType) throws MiniTMException {
        
        Object fType = fileTypes.get(fileType);
        
        int file_type = PLAINTEXT;
        
        if(fType!=null)
            file_type = ((Integer)fType).intValue();
        
        switch (file_type) {
            case SGML:
            case XML:
            case HTML:
            case JSP:
                
                if(engine.equals(Engine.STANDARD)) {
                    return new BasicSGMLFuzzySearchMiniTM(tmParam.getPath(), tmParam.getCreate(), tmParam.getName(), tmParam.getSourceLanguage(), tmParam.getTargetLanguage());
                } else {
                    return new LuceneSGMLFuzzySearchMiniTM(tmParam.getPath(), tmParam.getCreate(), tmParam.getName(), tmParam.getSourceLanguage(), tmParam.getTargetLanguage());
                }

            case PROPERTIES:
            case PO:
            case JAVA:
            case MSG:
            case PLAINTEXT:
            case DTD:
            case UNKNOWN:
                
                if(engine.equals(Engine.STANDARD)) {
                    return new BasicFuzzySearchMiniTM(tmParam.getPath(), tmParam.getCreate(), tmParam.getName(), tmParam.getSourceLanguage(), tmParam.getTargetLanguage());
                } else {
                    return new LuceneBasicFuzzySearchMiniTM(tmParam.getPath(), tmParam.getCreate(), tmParam.getName(), tmParam.getSourceLanguage(), tmParam.getTargetLanguage());
                }
                
            default:
                // this should never happen
                throw new RuntimeException("Fatal error, cannot find right miniTM. Report the problem to OLT support.");
        }
        
    }
    
    /**
     * Define supported MiniTM Engines
     */
    public static final class Engine {
        
        private String type = "";
        
        private Engine(String type) {
            this.type = type;
        }
       
        /**
         * Standard XML miniTM engine
         */
        public static final Engine STANDARD = new Engine("STANDARD");
        
        /**
         * Lucene miniTM engine
         */
        public static final Engine LUCENE   = new Engine("LUCENE");
        
    }
    
    /**
     * Store parameters for miniTM store
     */
    public static class Param {
        
        private String name = "";
        private String path = "";
        private String sourceLanguage = "";
        private String targetLanguage = "";
        private boolean create = true;
       
        /**
         * Create new instance of MiniTM Param
         *
         * @param name the name of the MiniTM
         * @param path the path to MiniTM store
         * @param sourceLanguge the source language
         * @param targetLanguage the target language
         * @param create create store if does not exists
         */
        public Param(String name, String path, String sourceLanguage, String targetLanguage, boolean create) {
            this.name = name;
            this.path = path;
            this.sourceLanguage = sourceLanguage;
            this.targetLanguage = targetLanguage;
            this.create = create;
        }
        
        /**
         * Create new instance of MiniTM Param
         *
         * @param name the name of the MiniTM
         * @param path the path to MiniTM store
         * @param sourceLanguge the source language
         * @param targetLanguage the target language
         */
        public Param(String name, String path, String sourceLanguage, String targetLanguage) {
            new Param(name,path,sourceLanguage,targetLanguage,true);
        }
        
        /**
         * Return the name of MiniTM
         */
        public String getName() {
            return name;
        }
        
        /**
         * Return the path of MiniTM
         */
        public String getPath() {
            return path;
        }
        
        /**
         * Return sourceLanguage of MiniTM/
         */
        public String getSourceLanguage() {
            return sourceLanguage;
        }
        
        /**
         * Return targetLanguage of MiniTM
         */
        public String getTargetLanguage() {
            return sourceLanguage;
        }
        
        /**
         * Return true if MiniTM should be created if does not exists
         */
        public boolean getCreate() {
            return create;
        }
        
    }
    
}

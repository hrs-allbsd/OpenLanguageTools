/*
 * BackConversionOptions.java
 *
 * Created on August 30, 2005, 3:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.backconv;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.jvnet.olt.xliff_back_converter.BackConverterProperties;

/**
 *
 * @author boris
 */
public class BackConversionOptions {
    private static final Logger logger = Logger.getLogger(BackConversionOptions.class.getName());
    private boolean overwriteFiles = true;
    private boolean generateTMX = false;
    private boolean preferXLZNames = true;
    
    //SGML
    private boolean writeTransStatusToSGML;
    private Map unicode2entityIncludeMap = new HashMap();
    private Map unicode2entityExcludeMap =  new HashMap();

    private String encoding = "UTF-8";
    
    /** Creates a new instance of BackConversionOptions */
    public BackConversionOptions() {
    }

    public boolean isOverwriteFiles() {
        return overwriteFiles;
    }

    public void setOverwriteFiles(boolean overwriteFiles) {
        this.overwriteFiles = overwriteFiles;
    }

    public boolean isGenerateTMX() {
        return generateTMX;
    }

    public void setGenerateTMX(boolean generateTMX) {
        this.generateTMX = generateTMX;
    }

    public boolean isPreferXLZNames() {
        return preferXLZNames;
    }

    public void setPreferXLZNames(boolean preferXLZNames) {
        this.preferXLZNames = preferXLZNames;
    }

    public boolean isWriteTransStatusToSGML() {
        return writeTransStatusToSGML;
    }

    public void setWriteTransStatusToSGML(boolean writeTransStatusToSGML) {
        this.writeTransStatusToSGML = writeTransStatusToSGML;
    }

    public Map getUnicode2entityIncludeMap() {
        return unicode2entityIncludeMap;
    }

    public void setUnicode2entityIncludeMap(Map unicode2entityIncludeMap) {
        this.unicode2entityIncludeMap = unicode2entityIncludeMap;
    }

    public Map getUnicode2entityExcludeMap() {
        return unicode2entityExcludeMap;
    }

    public void setUnicode2entityExcludeMap(Map unicode2entityExcludeMap) {
        this.unicode2entityExcludeMap = unicode2entityExcludeMap;
    }
    
    public void setEncoding(String encoding){
        this.encoding = encoding;
    }
 
    public String getEncoding(){
        return encoding;
    }
    public BackConverterProperties createBackConverterProperties(){
        BackConverterProperties props = new BackConverterProperties();
        
        props.setBooleanProperty(BackConverterProperties.PROP_GEN_PREFER_XLZ_NAME,preferXLZNames);
        props.setBooleanProperty(BackConverterProperties.PROP_SGML_WRITE_TRANS_STATUS,writeTransStatusToSGML);
        props.setBooleanProperty(BackConverterProperties.PROP_GEN_OVERWRITE_FILES,overwriteFiles);
        
        props.setProperty(BackConverterProperties.PROP_GEN_FILE_ENCODING,encoding == null ? "UTF-8": encoding);
        
        
        props.setSGMLUnicode2EntityIncludeMap(getUnicode2entityIncludeMap());
        props.setSGMLUnicode2EntityExcludeMap(getUnicode2entityExcludeMap());
        
        
        logger.finest("Includes map:"+props.getSGMLUnicode2EntityIncludeMap());
        logger.finest("Excludes map:"+props.getSGMLUnicode2EntityExcludeMap());
        
        return props;
    }

}

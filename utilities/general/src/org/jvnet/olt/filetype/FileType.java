package org.jvnet.olt.filetype;

/*
 * FileType.java
 *
 * Created on March 17, 2006, 10:00 AM
 *
 * Interface that helps to describe supported file types
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author michal
 */
public interface FileType {
    
    /**
     * Set integer representation of file type
     *
     * @param type of filtype
     */
    public void setType(int type);
    
    
    /**
     * Return integer representation of file type
     *
     *@return the type of filetype
     */
    public int getType();
    
    /**
     * Describe file type with short text
     *
     * @param description of the the file type
     */
    public void setDescription(String description);
    
    /**
     * Get description of the file type
     *
     * @return description of the file type
     */
    public String getDescription();
    
    /**
     * Add one extension to the list of supported extensions
     *
     * @param extension that is supported
     */
    public void addExtension(String extension);
    
    /**
     * Get an list of all supported extensions
     *
     * @return arrays of supported extensions
     */
    public List getExtensions();
    
    /**
     * Check extensions of the given file and return true if the extension is supported
     * by the file type.
     *
     * @param file that should be probed
     *
     * @return true if the file extensions is supported otherwise return false
     */
    public boolean detectExtension(File file);
    
    /**
     * Use some advanced method for file type detection
     *
     * @param file that should be probed
     *
     * @return true if the file type was detected otherwise return false
    */
    public boolean detect(File file) throws IOException;
    
}

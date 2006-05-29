/*
 * XSOLFileType.java
 *
 * Created on March 17, 2006, 10:18 AM
 *
 */

package org.jvnet.olt.filetype;

import java.util.regex.*;
import java.io.*;
import java.util.*;

/**
 * FileType detection for XML SOL Book files. We need to combine extension detection
 * and regular expresion detection.
 *
 * @author michal
 */
public class XSOLFileType extends BasicFileType {
    
    /**
     * Construct new file type
     *
     * @param description of the file type
     */
    public XSOLFileType(int type,String description) {
        super(type,description);
    }
    
    /**
     * check extension of the given file and apply regular expresion
     * to file content
     *
     * File name is converted to lowercase.
     *
     * @param file that should be probed
     *
     * @return true if the file extensions is supported and regular expresion match the file content
     */
    public boolean detectExtension(File file) {
        
        boolean result = super.detectExtension(file);
        
        if(result) {
            try {
                result = detect(file);
            } catch(IOException e) {
                System.err.println("Cannot access file " + file.getName()  + " during file type detection " + e.getMessage());
                result = false;
            }
        } 
        
        return result;
    }
    
}

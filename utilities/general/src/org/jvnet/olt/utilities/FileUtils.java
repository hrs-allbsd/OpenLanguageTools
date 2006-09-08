package org.jvnet.olt.utilities;

import java.io.*;

/**
 * static helper methods for file manipulation
 */
public class FileUtils {
    
    
    private FileUtils(){
        
    }
    
    
    /**
     * Create unique file name by appending an integer to it.
     * 
     * This method checks if file exists. If not the original name will be returned
     * If it exists it will append a integer
     * number starting from 1 until the file with such name can not be found.
     * @param file to make unique name of
     * @return unique (non-existing) file name
     */
    public static File ensureUniqueFile(File file){
        if(!file.exists()){
            return file;
        }
        
        int idx = 0;
        File x = null;
        do{
            x = new File(file.getParentFile(),file.getName()+"."+ ++idx);
        }
        while (x.exists());
        
        return x;
    }
    /**
     * Return file without extension.
     *
     * Returns file with name without the specified extension. If file name
     * is the same as the extension, it will return only the extension.
     * 
     * If f or extension are null, NullPointerException will be thrown
     * 
     * @return File without the extension if extension is present. 
     * @param f file to cut the extension off of
     * @param extension extension to cut off
     */
    public static File stripExtension(File f,String extension){
        String fname = f.getPath();
        if(!fname.endsWith(extension) || fname.equals(extension))
            return f;

        fname = fname.substring(0,fname.length()-extension.length());
        
        return new File(fname);
    }
}

package org.jvnet.olt.utilities;

import java.io.*;

public class FileUtils {
    
    
    private FileUtils(){
        
    }
    
    
    public static File ensureUniqueFile(File f){
        if(!f.exists()){
            return f;
        }
        
        int idx = 0;
        File x = null;
        do{
            x = new File(f.getParentFile(),f.getName()+"."+ ++idx);
        }
        while (x.exists());
        
        return x;
    }
    
    public static File stripExtension(File f,String extension){
        String fname = f.getPath();
        if(!fname.endsWith(extension) || fname.equals(extension))
            return f;

        fname = fname.substring(0,fname.length()-extension.length());
        
        return new File(fname);
    }
}

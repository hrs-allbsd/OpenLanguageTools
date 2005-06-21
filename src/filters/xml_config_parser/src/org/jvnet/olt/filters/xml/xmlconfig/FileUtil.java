
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.xml.xmlconfig; 

import java.io.*;

public class FileUtil {
 
    public static void copy(File sourceFile, File targetFile) throws IOException{
   
        if (!sourceFile.exists())
            throw new IOException("Copying File: no such source file: " + sourceFile.getName());
        if (!sourceFile.isFile())
            throw new IOException("Copying File: can't copy directory: " + sourceFile.getName());
        if (!sourceFile.canRead())
            throw new IOException("Copying File: source file is unreadable: " + sourceFile.getName());
            
        
        if (targetFile.exists() && !targetFile.canWrite()) {
            throw new IOException("Copying File: destination file is unwriteable: " + targetFile.getName());
        }
       
        FileInputStream sourceInputStream = null;  
        FileOutputStream targetOutputStream = null;
        try {
            sourceInputStream = new FileInputStream(sourceFile);  
            targetOutputStream = new FileOutputStream(targetFile);     
            byte[] buffer = new byte[4096];        
            int bytes_read;                         
         
            while((bytes_read = sourceInputStream.read(buffer)) != -1) { 
                targetOutputStream.write(buffer, 0, bytes_read);           
            }
        } finally {
            if (sourceInputStream != null) { 
                try { 
                    sourceInputStream.close(); 
                } catch (IOException e) { 
                    ; 
                }
            }
            
            if (targetOutputStream != null) { 
                try { 
                    targetOutputStream.close(); 
                } catch (IOException e) { 
                    ; 
                }
            }
        }
    }
    
}

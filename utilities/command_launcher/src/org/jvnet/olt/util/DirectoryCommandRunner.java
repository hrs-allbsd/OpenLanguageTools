
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * DirectoryCommandRunner.java
 *
 * Created on 02 October 2002, 15:05
 */

package org.jvnet.olt.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


/**
 *
 * @author  jc73554
 */

public class DirectoryCommandRunner {
    
    public int launch(String dirname, FileCommand command, String fileList) throws Exception
    {
        Logger logger = Logger.getLogger("org.jvnet.olt.alignment_import");
        
        BufferedReader in
            = new BufferedReader(new FileReader(fileList));
            
        int counter=0;
        String theFile;
        File file = null;
        
        while((theFile = in.readLine()) != null) {
            
            try {
                
                if(dirname.endsWith(File.separator)) {  
                    file = new File(dirname + theFile);
                } else {
                    file = new File(dirname + File.separator + theFile);
                }
        
                counter++;
                logger.log(Level.INFO, "Running command on file: " + file.getName());            
            
               /* // some memory leak somewhere, so every 5000 files
                // we force a system garbage check - very lazy, but
                // we're up against a deadline, and don't have time
                // to track down the problem.
                if ((counter % 5000)==0){
                    System.gc();
                }*/
                command.run(file);
               /* // rename the file once we're done with it.
                if (file.renameTo(new File(file.getParentFile().getName()+
                File.separator+file.getName()+".processed"))){
                    logger.log(Level.INFO, "renamed file to "+ 
                    File.separator+file.getName()+".processed");
                }
                else {
                    logger.log(Level.SEVERE, "Problem renaming file "+ file.getName()
                    + " to " +file.getParentFile().getName()+
                    File.separator+file.getName()+".processed");
                }
               */ 
            } catch (Exception e){
                logger.log(Level.SEVERE,"Encountered exception running on " + 
                    file.getParentFile().getName()+File.separator+file.getName()+" :\n"+
                    e.getMessage());                
                throw e;
            } finally {
               // command.cleanUp();
            }
            
        }
        command.cleanUp();
        return 1;
    }
    
    private List getFileNames(String directory) throws IOException {
        List entryList = new LinkedList();
        
        File dir = new File(directory);
        String[] names = dir.list();
        if (names != null) {
            for (int i=0; i< names.length; i++) {
                File filename = new File(dir.getPath() + "/"+names[i]);
                if (filename.isDirectory()) {
                    entryList.addAll(getFileNames(dir.getPath() + "/" + names[i]));
                }
                else {
                    String ext = getFileExtension(filename);
                    if (ext.equals("tmx")){
                        entryList.add( filename );
                    }
                }
            }
        }
        return entryList;
    }
    
    private String getFileExtension(File file){
        
        String ext = "";
        String name = file.getName();
        StringTokenizer st = new StringTokenizer(name, ".");
        while (st.hasMoreTokens()){
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        return ext;
    }
    
    
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * FileHarvester.java
 *
 * Created on August 17, 2004, 11:40 AM
 */

package org.jvnet.olt.harvester;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.filechooser.FileFilter;

/** This class walks down a file system tree and generates a list of files in
 * that tree that are accepted by a FileFilter. Traversal of the file system
 * follows a depth first tree walk.
 * @author  jc73554
 */
public class FileHarvester {
    
    /** Creates a new instance of FileHarvester */
    public FileHarvester() {
    }
    
    /**
     */
    public List harvestFiles(String dirName, FileFilter filter) throws IOException {
        //  Delegate to the other harvestFileNames
        return harvestFiles(new File(dirName), filter);
    }
    
    /**
     */
    public List harvestFiles(File directory, FileFilter filter) throws IOException {
        //  Create a list
        List entryList = new java.util.LinkedList();
        
        //  Get the contents of the directory
        String[] names = directory.list();
        
        //  Guard clause: directory has no contents, so we can go now.
        if (names == null) {
            return entryList;
        }
        
        //  Iterate over the contents applying the filter and searching 
        //  subdirectories
        for (int i=0; i< names.length; i++) {
            File filename = new File(directory, names[i]);
            if (filename.isDirectory()) {
                //  A recursive call to deal with the next level of directories
                //  down. Depth first search.
                entryList.addAll(harvestFiles(filename, filter));
            }
            else {
                if(filter.accept(filename)){
                    entryList.add( filename );
                }
            }           
        }
        return entryList;
    }    
}

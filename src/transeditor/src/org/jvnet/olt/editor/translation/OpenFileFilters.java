/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.io.File;
import java.util.HashSet;
import org.jvnet.olt.editor.util.Bundle;
import java.util.Set;
import javax.swing.filechooser.FileFilter;


/*
 * OpenFileFilters.java
 *
 * Created on March 22, 2005, 2:57 PM
 */

/**
 *
 * @author boris
 */
public class OpenFileFilters {
    private static final Bundle bundle = Bundle.getBundle(OpenFileFilters.class.getName());
    
    
    public static final FileFilter XLF_FILTER = new OpenFileFilter(new String[]{"xlf","xliff"}, bundle.getString("XLIFF_files_(.xlf,.xliff)"));
    public static final FileFilter XLZ_FILTER = new OpenFileFilter("xlz", bundle.getString("XLIFF_package_files_(.xlz)"));
    
    static class OpenFileFilter extends FileFilter {
        final String description;
        final Set exts = new HashSet();
        
        OpenFileFilter(String[] extensions, String description) {
            for(int i = 0; extensions != null && i < extensions.length;i++){
                if(extensions[i] != null)
                    exts.add(extensions[i]);
            }
            this.description = description;
        }
        
        OpenFileFilter(String extension, String description) {
            this(new String[]{extension},description);
        }
        
        
        public boolean accept(java.io.File file) {
            if (file == null) {
                return false;
            }
            
            if (file.isDirectory()) {
                return true;
            }
            
            String ext = getExtension(file);
            
            return isKnownExt(ext);
        }
        
        public String getDescription() {
            return description;
        }
        
        boolean isKnownExt(String ext){
            return ext != null && exts.contains(ext);
        }
    }
    
    /** Creates a new instance of OpenFileFilters */
    private OpenFileFilters() {
    }
    
    static public String getExtension(File f) {
        if(f == null)
            return null;
        
        int idx = f.getName().lastIndexOf('.');
        if(idx == -1 || idx + 1 >= f.getName().length())
            return null;
        
        return f.getName().substring(idx+1);
    }
    
    static public boolean isFileNameXLF(File file){
        String ext = getExtension(file);
        
        return ( ((OpenFileFilter)XLF_FILTER).isKnownExt(ext));
    }

    static public boolean isFileNameXLZ(File file){
        String ext = getExtension(file);
        
        return ( ((OpenFileFilter)XLZ_FILTER).isKnownExt(ext));
    }
    
}

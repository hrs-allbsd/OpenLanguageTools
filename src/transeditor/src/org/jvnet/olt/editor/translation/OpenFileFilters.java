/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

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
    public static final FileFilter XLF_FILTER = new OpenFileFilter("xlf", "XLIFF files (.xlf)");
    public static final FileFilter XLZ_FILTER = new OpenFileFilter("xlz", "XLIFF package files (.xlz)");

    static class OpenFileFilter extends FileFilter {
        final String ext;
        final String description;

        OpenFileFilter(String extension, String description) {
            this.ext = "." + extension;
            this.description = description;
        }

        public boolean accept(java.io.File file) {
            if (file == null) {
                return false;
            }

            if (file.isDirectory()) {
                return true;
            }

            return file.getName().endsWith(this.ext);
        }

        public String getDescription() {
            return description;
        }
    }

    /** Creates a new instance of OpenFileFilters */
    private OpenFileFilters() {
    }
}

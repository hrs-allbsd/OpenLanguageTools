/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * WritableDirectoryFileFilter.java
 *
 * Created on 03 April 2003, 16:52
 */
package org.jvnet.olt.editor.filefilters;


/**
 * A filter to select directories that can be written to.
 * @author  jc73554
 */
public class XliffFileFilter extends javax.swing.filechooser.FileFilter {
    /** Creates a new instance of WritableDirectoryFileFilter */
    public XliffFileFilter() {
    }

    /**
     *  Method that determines whether a given file should be accepted by the
     *  filter. The condition for this filter is:
     *    ( file.exists() && file.canRead() && fileName.endsWith(".xlf") )
     */
    public boolean accept(java.io.File file) {
        if (file.isDirectory()) {
            return true;
        }

        String fileName = file.getName();

        return (file.exists() && file.canRead() && fileName.endsWith(".xlf"));
    }

    /**
     *  Provides a description for the filter which is displayed in the drop
     *  list of the dialog.
     */
    public String getDescription() {
        return "Xliff files (*.xlf)";
    }
}

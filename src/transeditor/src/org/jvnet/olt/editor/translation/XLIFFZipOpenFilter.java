/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * XLIFFZipOpenFilter.java
 *
 * Created on 21 November 2003, 10:24
 */
package org.jvnet.olt.editor.translation;

import java.io.File;
import org.jvnet.olt.editor.util.Bundle;

import javax.swing.filechooser.FileFilter;


/**
 *
 * @author  jc73554
 */
public class XLIFFZipOpenFilter extends FileFilter {
    private Bundle bundle = Bundle.getBundle(XLIFFZipOpenFilter.class.getName());
    String tmExtension = "xlz";

    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);

            if ((extension != null) && extension.equals(tmExtension)) {
                return true;
            }
        }

        return false;
    }

    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');

            if ((i > 0) && (i < (filename.length() - 1))) {
                return filename.substring(i + 1).toLowerCase();
            }

            ;
        }

        return null;
    }

    public String getDescription() {
        return bundle.getString("XLIFF_Package_Files(.xlz)");
    }
}

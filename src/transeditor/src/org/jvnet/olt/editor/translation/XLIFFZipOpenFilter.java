/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/
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

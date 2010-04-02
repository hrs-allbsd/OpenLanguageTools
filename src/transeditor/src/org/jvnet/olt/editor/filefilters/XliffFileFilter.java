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

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
 * SelectByExtensionFileFilter.java
 *
 * Created on August 17, 2004, 11:43 AM
 */

package org.jvnet.olt.io;

/** This class is used to filter files based on their file extension. Files whose
 * extensions match the supplied extension, are accepted.
 * @author  jc73554
 */
public class SelectByExtensionFileFilter extends javax.swing.filechooser.FileFilter {
    
    private String ext = "";
    
    /** Creates a new instance of SelectByExtensionFileFilter */
    public SelectByExtensionFileFilter(String extension) {
        ext = extension;
    }
    
    public boolean accept(java.io.File f) {
        String fileName = f.getName();
        int pos = fileName.lastIndexOf('.');
        
        if(pos == -1) {
            //  The file has no extension and so cannot match
            return false;
        } else {
            String fileExt = fileName.substring(pos + 1); //  Add 1 to remove the period too
            if(fileExt == null) {
                return false;
            } else {
                return fileExt.equals(ext);
            }
        }
    }
    
    public String getDescription() {
        return ("This filter accepts files with extension \"" + ext + "\"");
    }
}

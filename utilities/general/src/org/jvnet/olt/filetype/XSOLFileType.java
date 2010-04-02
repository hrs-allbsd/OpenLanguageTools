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
 * XSOLFileType.java
 *
 * Created on March 17, 2006, 10:18 AM
 *
 */

package org.jvnet.olt.filetype;

import java.util.regex.*;
import java.io.*;
import java.util.*;

/**
 * FileType detection for XML SOL Book files. We need to combine extension detection
 * and regular expresion detection.
 *
 * @author michal
 */
public class XSOLFileType extends BasicFileType {
    
    /**
     * Construct new file type
     *
     * @param description of the file type
     */
    public XSOLFileType(int type,String description) {
        super(type,description);
    }
    
    /**
     * check extension of the given file and apply regular expresion
     * to file content
     *
     * File name is converted to lowercase.
     *
     * @param file that should be probed
     *
     * @return true if the file extensions is supported and regular expresion match the file content
     */
    public boolean detectExtension(File file) {
        
        boolean result = super.detectExtension(file);
        
        if(result) {
            try {
                result = detect(file);
            } catch(IOException e) {
                System.err.println("Cannot access file " + file.getName()  + " during file type detection " + e.getMessage());
                result = false;
            }
        } 
        
        return result;
    }
    
}

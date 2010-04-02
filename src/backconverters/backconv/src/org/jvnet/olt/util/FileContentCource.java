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
 * FileContentCource.java
 *
 * Created on June 25, 2004, 1:24 PM
 */

package org.jvnet.olt.util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/** This class represents objects sources of content that come from files on 
 * file systems.
 * @author  jc73554
 */
public class FileContentCource implements ContentSource {
    
    private File file;
    
    private String encoding;
    
    /** Creates a new instance of FileContentCource */
    public FileContentCource(File file, String encoding) {
        if( file == null ) { throw new IllegalArgumentException("The File object passed to a FileContentSource was null."); }
        if(!file.exists()) { throw new IllegalArgumentException("The file referenced does not exist."); }
        this.file = file;
        this.encoding = encoding;
    }
    
    
    public Reader getReader() throws IOException {
        java.io.FileInputStream istream = new java.io.FileInputStream(file);
        return new java.io.InputStreamReader(istream, encoding);
    }    
}

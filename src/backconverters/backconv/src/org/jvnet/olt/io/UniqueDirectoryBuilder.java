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
 * UniqueDirectoryBuilder.java
 *
 * Created on June 22, 2004, 11:33 AM
 */

package org.jvnet.olt.io;

import java.io.File;
import java.io.IOException;

/** This class is here to produce unique directories, to give some ability to
 * have concurrent creation of directories, and storing of files on a file
 * system. If directories are created through this interface then things should
 * be okay.
 * @author  jc73554
 */
public class UniqueDirectoryBuilder {
    
    /** Creates a new instance of UniqueDirectoryBuilder */
    public UniqueDirectoryBuilder() {
    }
    
    public synchronized File buildUniqueDirectory(File parent, String prefix) throws IOException {
        //  Guard clauses
        if(!parent.isDirectory()) { throw new IOException("A file was supplied."); }
        if(!parent.canWrite()) { throw new IOException("The directory supplied was not writable."); }
        
        //  Test for the path separator in the prefix
        int indexOfSeparator = prefix.indexOf(File.separatorChar);
        if(indexOfSeparator != -1) {
            throw new IOException("The prefix string contains the separator character.");
        }
        
        //  Generate dir name: base it on the system time
        long time = System.currentTimeMillis();
        String dirName = prefix + time;
        
        File dir = new File(parent, dirName);
        int retryLimit = 20;
        while(dir.exists()) {
            //  Test if we have gone beyond the retry limit.
            retryLimit--;
            if(retryLimit <= 0) { 
                throw new IOException("Unable to create unique directory name"); 
            }
            
            time = System.currentTimeMillis();
            dirName = prefix + time;
            
            dir = new File(parent, dirName);            
        }
            
        //  Now make the directory    
        if(!dir.mkdir()) {
            throw new IOException("Unable to create unique directory");
        } else {
            return dir;
        }
    }
    
        
    public String makeDirectory(File parent, String name) throws IOException {
        File dir = new File(parent,  name);
        if(dir.exists()) {
            throw new IOException("There already exists a file called '" + name + "' in directory '" + parent.getName() + "'");
        }
        
        if(!dir.mkdir()) {
            throw new IOException("Could not create the directory called '" + name + "' in directory '" + parent.getName() + "'");
        }
        
        return dir.getAbsolutePath();
    }
}

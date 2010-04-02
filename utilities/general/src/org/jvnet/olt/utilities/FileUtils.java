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
package org.jvnet.olt.utilities;

import java.io.*;

public class FileUtils {
    
    
    private FileUtils(){
        
    }
    
    
    public static File ensureUniqueFile(File f){
        if(!f.exists()){
            return f;
        }
        
        int idx = 0;
        File x = null;
        do{
            x = new File(f.getParentFile(),f.getName()+"."+ ++idx);
        }
        while (x.exists());
        
        return x;
    }
    
    public static File stripExtension(File f,String extension){
        String fname = f.getPath();
        if(!fname.endsWith(extension) || fname.equals(extension))
            return f;

        fname = fname.substring(0,fname.length()-extension.length());
        
        return new File(fname);
    }
}

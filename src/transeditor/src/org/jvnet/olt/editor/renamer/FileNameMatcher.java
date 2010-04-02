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
 * FileNameMatcher.java
 *
 * Created on September 6, 2006, 3:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.renamer;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 
 @author boris
 */
public class FileNameMatcher {
    Pattern p;
    
    static interface Callback {
        
        void warning(String s);                
        void info(String s);
        void execute(File f,Matcher m);

        void done();

        void start();
        
        void stop();
        
        boolean getStop();
    }
    
    /** Creates a new instance of FileNameMatcher */
    public FileNameMatcher(Pattern p) {
        this.p = p;
    }
    
    public void walk(File root,Callback o){
        if(o.getStop())
            return ;
        
        if(!root.exists()){
            o.warning("Not found:"+root.getPath());
            return;
        }

        if(root.isDirectory()){
            File[] files = root.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                synchronized(this){
                    if(o.getStop())
                        return ;
                }
                walk(files[i],o);
            }
        }
        
        if(root.isFile()){
            Matcher m = p.matcher(root.getPath());
            if(m.find()){
                o.info("Match found:"+root.getPath());
                o.execute(root,m);
            }
            else
                o.info("No match   :"+root.getPath());
        }
        
    }
    
    
}

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
 * BasicFileType.java
 *
 * Created on March 17, 2006, 10:18 AM
 *
 */

package org.jvnet.olt.filetype;

import java.util.regex.*;
import java.io.*;
import java.util.*;

/**
 * Basic file type implementation. It detect file type by extensions or by regular 
 *
 * @author michal
 */
public class BasicFileType implements FileType {
    
    
    private int type = 0;
    private String description = "";
    private List extensions = null;
    private String regExp = "";
    
    /**
     * Construct new file type
     *
     * @param description of the file type
     */
    public BasicFileType(int type,String description) {
        extensions = java.util.Collections.synchronizedList(new ArrayList());
        setType(type);
        setDescription(description);
    }
       
    /**
     * Set integer representation of file type
     *
     * @param type of filtype
     */
    public void setType(int type) {
        this.type = type;
    }
    
    
    /**
     * Return integer representation of file type
     *
     *@return the type of filetype
     */
    public int getType() {
        return type;
    }
    
    
    /**
     * Describe file type with short text
     *
     * @param description of the the file type
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Get description of the file type
     *
     * @return description of the file type
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Add one extension to the list of supported extensions
     * 
     * Please use lowercase string.
     *
     * @param extension that is supported
     */
    public void addExtension(String extension) {
        extensions.add(extension);
    }
    
    /**
     * Get an List of all supported extensions
     *
     * @return list of all supported extensions
     */
    public List getExtensions() {
        List newOne = java.util.Collections.synchronizedList(new ArrayList());
        Iterator it = extensions.iterator();
        
        while(it.hasNext()) {
            String ext = (String)it.next();
            newOne.add(ext);
        }
        
        return newOne;
    }
    
    /**
    */
     public void setRegExpContent(String regExp) {
         this.regExp = regExp;
     }
    
    /**
     * Check extensions of the given file and return true if the extension is supported
     * by the file type.
     *
     * File name is converted to lowercase.
     *
     * @param file that should be probed
     *
     * @return true if the file extensions is supported otherwise return false
     */
    public boolean detectExtension(File file) {
        String name = file.getName().toLowerCase();
        boolean result = false;
        
        Iterator it = extensions.iterator();
        
        while(it.hasNext()) {
            String ext = (String)it.next();
            if(name.matches(".*\\." + ext + "$")) {
                result = true;
                break;
            }
        }
        
        return result;
    }
    
    /**
     * Read five lines from the begining of the file and apply regular expresion to the content
     *
     * @param file that should be probed
     *
     * @return true if the file type was detected otherwise return false
    */
    public boolean detect(File file) throws java.io.IOException {  
        boolean result = false;
        if("".equals(regExp)) {
            return result;
        }
        
        BufferedReader in = new BufferedReader(new FileReader(file));
        StringBuffer content = new StringBuffer();
        String line = "";
        int counter = 0;
        
        while (((line = in.readLine()) != null) && counter<5) {
            counter++;
            content.append(line);
        }
        in.close();
        
        if(content.toString().matches(".*?" + regExp + ".*")) {
            result = true;
        }
        
        return result;
    }
    
    /**
     * Basic implementation for clone method
     *
     * @return copy of current object
     */
    public Object clone() throws CloneNotSupportedException {
        BasicFileType clone = new BasicFileType(type,description);
        clone.setRegExpContent(regExp);
        
        List newExt = getExtensions();
        Iterator it = newExt.iterator();
        while(it.hasNext()) {
            String ext = (String)it.next();
            clone.addExtension(ext);
        }
        
        return clone;
    }
    
}

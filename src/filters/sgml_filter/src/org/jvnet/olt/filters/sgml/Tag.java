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
 * Tag.java
 *
 * Created on June 11, 2003, 10:36 AM
 */

package org.jvnet.olt.filters.sgml;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
/**
 * This class allows me to store the information I need about a single (open) tag
 * in order to preserve the namespace information that's being passed on the parse
 * tree. (The older code didn't care about namespaces - it just saw tags as being
 * Strings - thus loosing which prefixes mapped to which namespaces)
 *
 * @author  timf
 */
public class Tag {
    
    private String tagName = "";
    private String nameSpaceID = "";
    private boolean hasNamespace = false;
    private Map nameSpaceMap = null;
    
    /** Creates a new instance of Tag 
        @param tagName the name of this tag
     *  @param nameSpaceID the xml namespace being passed in
     */
    public Tag(String tagName, String nameSpaceID, Map nameSpaceMap) {
        if (nameSpaceID.length() == 0){
            this.hasNamespace = false;
        } else {
            this.hasNamespace = true;
        }
        this.nameSpaceID = nameSpaceID;
        this.tagName = tagName;
        this.nameSpaceMap = nameSpaceMap;
        
        /*if (nameSpaceMap != null){
        System.out.println("NAMESPACE MAP HAS :");
        Set keys = nameSpaceMap.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();){
              String key = (String)it.next();
              System.out.println(key+" = "+(String)nameSpaceMap.get(key));
         }
        }*/
    }
    
    public Tag(String tagName){
        this(tagName,"", null);
    }
    
    public boolean hasNamespace(){
        return this.hasNamespace;
    }
    
    public String getNameSpaceID(){
        return this.nameSpaceID;
    }
    
    public String getNameSpaceID(String prefix){
        if (this.nameSpaceMap != null){
            String ns = (String)this.nameSpaceMap.get(prefix);
            if (ns != null)
                return ns;
            else return "";
        } else {
            return "";
        }
    }
    
    public Map getNameSpaceMap(){
        return this.nameSpaceMap;
    }
    
    
    public String getName(){
        return this.tagName;
    }
}

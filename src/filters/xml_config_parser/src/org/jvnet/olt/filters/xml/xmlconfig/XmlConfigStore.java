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

package org.jvnet.olt.filters.xml.xmlconfig; 

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author  bpk1
 */
public class XmlConfigStore {
    
    private Map configMap = new HashMap();
    
    public void addXmlConfig(String key, XmlConfig config) {
        configMap.put(key, config);
    }
    
    public void removeXmlConfig(String key) {
        configMap.remove(key);
    }
    
    public XmlConfig getXmlConfig(String key) {
        return (XmlConfig) configMap.get(key);
    }
    
    public String toString() {
        
        String result = "";
        
        Iterator iter = configMap.keySet().iterator();
        
        while(iter.hasNext()) {
            String key = (String) iter.next();
            XmlConfig config = (XmlConfig) configMap.get(key);
            result = result + ("Key = " + key + " File Type = " + config.getFileType() + "\n");
        }
        
        return result;
    }
    
}

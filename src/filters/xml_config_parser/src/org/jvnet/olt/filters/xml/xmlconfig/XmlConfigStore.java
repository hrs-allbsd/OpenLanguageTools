
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

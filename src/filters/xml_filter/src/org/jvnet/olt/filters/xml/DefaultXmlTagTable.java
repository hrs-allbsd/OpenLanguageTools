
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * DefaultXmlTagTable.java
 *
 * Created on October 29, 2003, 4:19 PM
 */

package org.jvnet.olt.filters.xml;
import org.jvnet.olt.filters.xml.xmlconfig.XmlConfig;

/**
 *
 * @author  timf
 */
public class DefaultXmlTagTable implements org.jvnet.olt.parsers.tagged.TagTable {
    
   
    public boolean tagEmpty(String str) {
        return false;
    }
    
    public boolean tagEmpty(String str, String str1) {
        return false;
    }
    
    public boolean tagForcesVerbatimLayout(String str) {
        return false;
    }
    
    public boolean tagForcesVerbatimLayout(String str, String str1) {
        return false;
    }
    
    public boolean tagMayContainPcdata(String str) {
        return false;
    }
    
    public boolean tagMayContainPcdata(String str, String str1) {
        return false;
    }
    
}

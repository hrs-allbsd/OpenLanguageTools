
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * DefaultTagTable.java
 *
 * Created on May 17, 2004, 5:33 PM
 */

package org.jvnet.olt.parsers.tagged;

/**
 * A default class that implements TagTable, providing "sane" values
 * as defaults.
 * @author  timf
 */
public class DefaultTagTable implements TagTable {
     
    /** Creates a new instance of DefaultTagTable */
    public DefaultTagTable() {
    }
    
    /**
     * is empty ? = false
     * @param tagName
     * @return
     */    
    public boolean tagEmpty(String tagName) {
        return false;
    }
    
    /**
     * false
     */    
    public boolean tagEmpty(String tagName, String namespaceID) {
        return tagEmpty(tagName);
    }
    
    /**
     * forces verbatim layout ? = false
     */    
    public boolean tagForcesVerbatimLayout(String tagName) {
        return false;
    }
    
    /**
     * false
     */    
    public boolean tagForcesVerbatimLayout(String tagName, String namespaceID) {
        return tagForcesVerbatimLayout(tagName);
    }
    
    /**
     * may contain pcdata ? = true
     */    
    public boolean tagMayContainPcdata(String tagName) {
        return true;
    }
    
    /**
     * true
     */    
    public boolean tagMayContainPcdata(String tagName, String namespaceID) {
        return tagMayContainPcdata(tagName);
    }
    
}

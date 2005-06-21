
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * HtmlTagTable.java
 *
 * Created on June 26, 2002, 3:03 PM
 */

package org.jvnet.olt.filters.sgml;

import java.util.HashSet;
import org.jvnet.olt.parsers.tagged.*;

public class TestTagTable implements TagTable {

    private HashSet pcdataSet;
    private HashSet verbatimSet;
    private HashSet emptySet;
    
    public TestTagTable() {
        pcdataSet = new HashSet();
        
        
        pcdataSet.add("inline");
        pcdataSet.add("nonseginline");
        
        //  The Verbatim layout
        verbatimSet = new HashSet();
        verbatimSet.add("pre");
        
        
        //  "EMPTY" tags
        emptySet = new HashSet();
        emptySet.add("img");
        //emptySet.add("a");
        
        emptySet.add("suntransxmlfilter");
    }
    
    public boolean tagMayContainPcdata(String tagName) {
        // we return true if the input is empty        
        if (tagName.equals(""))
            return true;
        return pcdataSet.contains(tagName);
    }
    
    public boolean tagForcesVerbatimLayout(String tagName) {
        return verbatimSet.contains(tagName);
    }
    
    public boolean tagEmpty(String tagName) {
        return emptySet.contains(tagName);
    }
    
    public boolean tagEmpty(String tagName, String namespaceID) {
        return tagEmpty(tagName);
    }
    
    public boolean tagForcesVerbatimLayout(String tagName, String namespaceID) {
        return tagForcesVerbatimLayout(tagName);
    }
    
    public boolean tagMayContainPcdata(String tagName, String namespaceID) {
        return tagMayContainPcdata(tagName);
    }
    
}

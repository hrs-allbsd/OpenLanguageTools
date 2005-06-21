
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

package org.jvnet.olt.filters.html;
/**
 *
 * @author  timf
 */

import java.util.HashSet;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;

public class HtmlTagTable implements TagTable, org.jvnet.olt.parsers.tagged.TagTable {
    
    private HashSet pcdataSet;
    private HashSet verbatimSet;
    private HashSet emptySet;
    
    public HtmlTagTable() {
        pcdataSet = new HashSet();
        verbatimSet = new HashSet();
    emptySet = new HashSet();
        
        //  Create the tag table here.
        pcdataSet.add("tt");
        pcdataSet.add("i");
        pcdataSet.add("b");
        pcdataSet.add("u");
        pcdataSet.add("s");
        pcdataSet.add("strike");
        pcdataSet.add("big");
        pcdataSet.add("small");
        pcdataSet.add("em");
        pcdataSet.add("strong");
        pcdataSet.add("dfn");
        pcdataSet.add("code");
        pcdataSet.add("samp");
        pcdataSet.add("kbd");
        pcdataSet.add("var");
        pcdataSet.add("cite");
        pcdataSet.add("abbr");
        pcdataSet.add("acronym");
        pcdataSet.add("a");
        pcdataSet.add("caption");
        pcdataSet.add("applet");
        pcdataSet.add("object");
        pcdataSet.add("font");
        pcdataSet.add("basefont");
        pcdataSet.add("map");
        pcdataSet.add("q");
        pcdataSet.add("sub");
        pcdataSet.add("sup");
        pcdataSet.add("span");
        pcdataSet.add("bdo");
        pcdataSet.add("select");
        pcdataSet.add("textarea");
        pcdataSet.add("label");
        pcdataSet.add("button");
        // this is not valid html, but some
        // teams in Sun insert this tag - it
        // appears to be inline...
        pcdataSet.add("desc");
        pcdataSet.add("script");
        pcdataSet.add("style");
        pcdataSet.add("option");
        pcdataSet.add("img");
        pcdataSet.add("area");
        pcdataSet.add("link");
        
        // not sure abuot this tag
        //pcdataSet.add("input");
        
        //  The Verbatim layout
        verbatimSet.add("pre");        
        //  "EMPTY" tags
        emptySet.add("br");
        emptySet.add("hr");
        emptySet.add("option");
        emptySet.add("img");
        emptySet.add("area");
        emptySet.add("link");
        emptySet.add("suntransxmlfilter");
    }
    
    public boolean tagMayContainPcdata(String tagName) {
        return pcdataSet.contains(tagName.toLowerCase());
    }
    
    public boolean tagForcesVerbatimLayout(String tagName) {
        return verbatimSet.contains(tagName.toLowerCase());
    }
    
    public boolean tagEmpty(String tagName) {
        return emptySet.contains(tagName.toLowerCase());
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


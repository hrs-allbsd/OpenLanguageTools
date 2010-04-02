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

public class HtmlTagTable implements org.jvnet.olt.parsers.tagged.TagTable {
    
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
        //pcdataSet.add("style");
        pcdataSet.add("option");
        emptySet.add("input");
        pcdataSet.add("img");
        pcdataSet.add("area");
        pcdataSet.add("link");
        pcdataSet.add("param");
        
        // not sure abuot this tag
        //pcdataSet.add("input");
        
        //  The Verbatim layout
        verbatimSet.add("pre");        
        verbatimSet.add("object");
        verbatimSet.add("param");
        //  "EMPTY" tags
        emptySet.add("br");
        emptySet.add("hr");
        emptySet.add("option");
        emptySet.add("img");
        emptySet.add("input");
        emptySet.add("area");
        emptySet.add("link");
        emptySet.add("suntransxmlfilter");
        emptySet.add("object");
        emptySet.add("param");
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


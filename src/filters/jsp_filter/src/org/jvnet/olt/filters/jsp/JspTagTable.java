
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * JspTagTable.java
 *
 * Created on December 17, 2003, 9:54 AM
 */

package org.jvnet.olt.filters.jsp;

/**
 * Jsp is treated in the same way as html, with the exception that we
 * want to do the right thing for jsp block and inline tags.
 *
 * @author  timf
 */
public class JspTagTable extends org.jvnet.olt.filters.html.HtmlTagTable implements org.jvnet.olt.parsers.tagged.TagTable {
    
    
    /** Creates a new instance of JspTagTable */
    public JspTagTable() {
    }
    
    
    
    public boolean tagEmpty(String tagName) {
        if (tagName.equals("jsp_inline") || tagName.equals("jsp_block")){
            return true;
        }
        else {
            return super.tagEmpty(tagName);
        }
    }
    
    public boolean tagEmpty(String tagName, String namespaceID) {
        return tagEmpty(tagName);
    }
    
    public boolean tagMayContainPcdata(String tagName) {  
        if (tagName.equals("jsp_inline")){
            return true;
        } else {
            return super.tagMayContainPcdata(tagName);
        }
    }
    
    public boolean tagMayContainPcdata(String tagName, String namespaceID) {
        return tagMayContainPcdata(tagName);
    }
    
}

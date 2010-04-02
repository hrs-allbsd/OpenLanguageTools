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

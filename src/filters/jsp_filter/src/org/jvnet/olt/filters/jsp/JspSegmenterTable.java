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
 * JspSegmenterTable.java
 *
 * Created on December 17, 2003, 9:53 AM
 */

package org.jvnet.olt.filters.jsp;
import org.jvnet.olt.alignment.*;

/**
 *
 * @author  timf
 */
public class JspSegmenterTable extends org.jvnet.olt.filters.html.HtmlSegmenterTable implements org.jvnet.olt.parsers.tagged.SegmenterTable {
    
    /** Creates a new instance of JspSegmenterTable */
    public JspSegmenterTable() {
    }
    
    // just need to do special things for jsp blocks
    public int getBlockLevel(String tag) {
        if (tag.equals("jsp_block")){
            return Segment.SOFT;
        } else {
            return super.getBlockLevel(tag);
        }
    }
    
    public int getBlockLevel(String tag, String namespaceID) {
        return getBlockLevel(tag);
    }
    
    public boolean dontSegmentInsideTag(String tagname) {
        if (tagname.equals("jsp_inline")){
            return true;
        } else {
            return super.dontSegmentInsideTag(tagname);
        }
    }
    
    public boolean dontSegmentInsideTag(String tagname, String namespaceID) {
        return dontSegmentInsideTag(tagname);
    }
    
    public boolean dontSegmentOrCountInsideTag(String tagname) {
        if (tagname.equals("jsp_inline")){
            return true;
        } else {
            return super.dontSegmentInsideTag(tagname);
        }
    }
    
    public boolean dontSegmentOrCountInsideTag(String tagname, String namespaceID) {
        return dontSegmentOrCountInsideTag(tagname);
    }

    public boolean includeCommentsInTranslatableSection(String tag) {
        if (tag.equals("script")){
            return true;
        } else {
            return false;
        }
    }
    
}

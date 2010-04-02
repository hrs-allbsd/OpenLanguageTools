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
 * DefaultXmlSegmenterTable.java
 *
 * Created on October 29, 2003, 4:20 PM
 */

package org.jvnet.olt.filters.xml;

/**
 *
 * @author  timf
 */
public class DefaultXmlSegmenterTable implements org.jvnet.olt.parsers.tagged.SegmenterTable {
    
    /** Creates a new instance of DefaultXmlSegmenterTable */
    public DefaultXmlSegmenterTable() {
    }
    
    public boolean containsTranslatableAttribute(String str) {
        return false;
    }
    
    public boolean containsTranslatableAttribute(String str, String str1) {
        return false;
    }
    
    public boolean containsTranslatableText(String str) {
        return true;
    }
    
    public boolean containsTranslatableText(String str, String str1) {
        return true;
    }
    
    public boolean dontSegmentInsideTag(String str) {
        return false;
    }
    
    public boolean dontSegmentInsideTag(String str, String str1) {
        return false;
    }
    
    public boolean dontSegmentOrCountInsideTag(String str, String str1) {
        return false;
    }
    
    public boolean dontSegmentOrCountInsideTag(String str) {
        return false;
    }
    
    
    
    public java.util.List getAttrList(String str) {
        return new java.util.ArrayList();
    }
    
    public java.util.List getAttrList(String str, String str1) {
        return new java.util.ArrayList();
    }
    
    public int getBlockLevel(String str) {
        // this is the HARD level, as defined in 
        //  org.jvnet.olt.alignment.Segment - sorry
        // about hard coding this, but that stuff gets
        // built after the filters :-(
        return 3;       
    }
    
    public int getBlockLevel(String str, String str1) {
        return 3;
    }
    
    public Character getEntityCharValue(String str) {
        return new Character('X');
    }

    public boolean includeCommentsInTranslatableSection(String tag) {
        return false;
    }

    public boolean pcdataTranslatableByDefault() {
        return true;
    }
    
}

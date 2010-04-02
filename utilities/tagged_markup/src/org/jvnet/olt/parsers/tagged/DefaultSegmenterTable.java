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
 * DefaultSegmenterTable.java
 *
 * Created on May 17, 2004, 5:33 PM
 */

package org.jvnet.olt.parsers.tagged;

/**
 * A default implementation of the segmenter table providing sane values.
 * @author timf
 */
public class DefaultSegmenterTable implements SegmenterTable { 
     
    /** Creates a new instance of DefaultSegmenterTable */
    public DefaultSegmenterTable() {
    }
    
    /**
     * stuff doesn't contain translatable attributes
     * @return false
     * @param tag the tag to be tested
     */
    public boolean containsTranslatableAttribute(String tag) {
        return false;
    }
    
    /**
     * return false
     * @param tag the tag to be tested
     * @param namespaceID the namespace
     * @return false
     */
    public boolean containsTranslatableAttribute(String tag, String namespaceID) {
        return containsTranslatableAttribute(tag);
    }
    
    /**
     * does contain translatable text
     * @param tag the tag to be tested
     * @return true
     */    
    public boolean containsTranslatableText(String tag) {
        return true;
    }
    
    /**
     * the tag does contain translatable text
     * @return true
     * @param tag the tag name
     * @param namespaceID the namespace id
     */    
    public boolean containsTranslatableText(String tag, String namespaceID) {
        return containsTranslatableText(tag);
    }
    
    /**
     * return false
     * @param tagname the tag to be tested
     * @return false
     */
    public boolean dontSegmentInsideTag(String tagname) {
        return false;
    }
    
    /**
     * we always segment inside this tag
     * @return false
     * @param tagname the tag to be tested
     * @param namespaceID the namespace to be tested
     */    
    public boolean dontSegmentInsideTag(String tagname, String namespaceID) {
        return dontSegmentInsideTag(tagname);
    }
    
    /** @return 
     * /**
     * we assume no attributes
     * @param tag the tag to be tested
     * @return an empty list
     */
    public java.util.List getAttrList(String tag) {
        return new java.util.ArrayList();
    }
    
    
     /**
      * we assume no attributes
      * @param tag the tag to be tested
      * @param namespaceID the namespace to be tested
      * @return an empty list
      */     
    public java.util.List getAttrList(String tag, String namespaceID) {
        return getAttrList(tag);
    }
    
    public int getBlockLevel(String tag) {
        return -1;
    }
    
    public int getBlockLevel(String tag, String namespaceID) {
        return -1;
    }
    
    /**
     * we always return null
     * @param entity the entity name
     * @return null
     */    
    public Character getEntityCharValue(String entity) {
        return null;
    }
    
    public boolean dontSegmentOrCountInsideTag(String tagname) {
        return false;
    }
    
    public boolean dontSegmentOrCountInsideTag(String tagname, String namespaceID) {
        return false;
    }

    public boolean includeCommentsInTranslatableSection(String tag) {
        return false;
    }

    public boolean pcdataTranslatableByDefault() {
        return true;
    }
    
}

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

package org.jvnet.olt.filters.xml;

import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.filters.xml.xmlconfig.XmlConfig;
import org.jvnet.olt.filters.xml.xmlconfig.XmlElement;
import java.util.*;

public class XmlSegmenterTable implements SegmenterTable {
    
    private XmlConfig xmlConfig;
    
    private static String[] numberNames = { "inline", "softflow", "soft", "hard", "hardsubsection", "hardsection" };
    
    private HashMap hardnessMap = new HashMap(6);
    
    private HashMap configMap;
    
    
    public XmlSegmenterTable(XmlConfig theXmlConfig) throws XmlFilterException {
        if (theXmlConfig == null){
            throw new XmlFilterException("The XmlConfig object was null");
        }
        this.xmlConfig = theXmlConfig;
        // clients shouldn't be calling methods that use the config map
        // if we're using this constructor, but we'll initialise it
        // anyway for safety reasons.
        this.configMap = new HashMap();
        this.initialiseHardnessMap();
        
    }
    
    public XmlSegmenterTable(HashMap theConfigMap) throws XmlFilterException {
        // check to see if someone's handed us an empty map.
        if (theConfigMap == null){
            this.configMap = new HashMap();
        } else if (theConfigMap.size() == 0){
            throw new XmlFilterException("No xmlConfig elements in that xmlConfigMap");
        }  else {
            this.configMap = theConfigMap;
        }
        this.initialiseHardnessMap();
    }
    
    
    
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html.
     *
     * @param tagName The tag you're querying.
     * @return true if the tag doesn't contain segmentable text.
     */
    public boolean dontSegmentInsideTag(String tagName) {
        XmlConfig config = xmlConfig;
        
        return this.dontSegmentInsideTag(tagName, config);
    }
    
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html.
     *
     * @param tagName The tag you're querying.
     * @param namespace The namespace ID.
     * @return true if the tag doesn't contain segmentable text.
     */
    public boolean dontSegmentInsideTag(String tagName, String namespace) {
        if(namespace.equals("")) {
            return this.dontSegmentInsideTag(tagName);
        }
        
        namespace = this.stripColon(namespace);
        
        XmlConfig config = (XmlConfig) configMap.get(namespace);
        
        return this.dontSegmentInsideTag(tagName, config);
    }
    
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html.
     *
     * @param tagName The tag you're querying.
     * @param config The XmlConfig object.
     * @return true if the tag doesn't contain segmentable text.
     */
    public boolean dontSegmentInsideTag(String tagName, XmlConfig config) {        
        boolean result = false;
        try {
            XmlElement element = config.getXmlElement(tagName);
            result = element.getNonSegmentable();
        } catch (NullPointerException ex) {
        }
        return result;
    }
    
    
    
    
        /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html.
     *
     * @param tagName The tag you're querying.
     * @return true if the tag doesn't contain segmentable text.
     */
    public boolean dontSegmentOrCountInsideTag(String tagName) {
        XmlConfig config = xmlConfig;
        
        return this.dontSegmentOrCountInsideTag(tagName, config);
    }
    
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html.
     *
     * @param tagName The tag you're querying.
     * @param namespace The namespace ID.
     * @return true if the tag doesn't contain segmentable text.
     */
    public boolean dontSegmentOrCountInsideTag(String tagName, String namespace) {
        if(namespace.equals("")) {
            return this.dontSegmentOrCountInsideTag(tagName);
        }
        
        namespace = this.stripColon(namespace);
        
        XmlConfig config = (XmlConfig) configMap.get(namespace);
        
        return this.dontSegmentOrCountInsideTag(tagName, config);
    }
    
    
    public boolean dontSegmentOrCountInsideTag(String tagName, XmlConfig config){
        boolean result = false;
        try {
            XmlElement element = config.getXmlElement(tagName);
            result = element.getNonSegmentableNoWordcount();
        } catch (NullPointerException ex) {
        }
        return result;
    }
    
    
    /** This is for tags that contain translatable text in their attributes
     *
     * @param tag The tag name you're querying
     * @return true if the tag contains translatble attributes.
     */
    public boolean containsTranslatableAttribute(String tagName) {
        XmlConfig config = xmlConfig;
        
        return this.containsTranslatableAttribute(tagName, config);
    }
    
    /** This is for tags that contain translatable text in their attributes
     *
     * @param tag The tag name you're querying
     * @param namespace The namespace ID.
     * @return true if the tag contains translatble attributes.
     */
    public boolean containsTranslatableAttribute(String tagName, String namespace) {
        if(namespace.equals("")) {
            return this.containsTranslatableAttribute(tagName);
        }
        
        namespace = this.stripColon(namespace);
        
        XmlConfig config = (XmlConfig) configMap.get(namespace);
        
        return this.containsTranslatableAttribute(tagName, config);
    }
    
    /** This is for tags that contain translatable text in their attributes
     *
     * @param tag The tag name you're querying
     * @param config The XmlConfig object.
     * @return true if the tag contains translatble attributes.
     */
    public boolean containsTranslatableAttribute(String tagName, XmlConfig config) {
        return true;
    }
    
    /** This returns the value of an entity as a Character object. If the
     * entity in question isn't a character entity, it returns null. Note, it
     * is probably advisable that implementors shouldn't return &lt; for "lt",
     * &amp; for "amp" and &gt; for "gt" since these are entities that are
     * usually preserved (and would break sgml and xml documents were they
     * converted to their character values)
     *
     * @returns the entity character value
     * @param entity the text of the entity
     */
    public Character getEntityCharValue(String entity) {
        return null;
    }
    
    /** This returns the "hardness level" of a particular tag. This method is only
     * really useful to alignment programs that need some insight into the structural
     * flow of the document. For a description of what a "hardness level" is, see the
     * docs for the alignment program interface.
     *
     * @returns the "hardness level" of a particular tag
     * @param tag the tag you're querying
     */
    public int getBlockLevel(String tagName) {
        XmlConfig config = xmlConfig;
        
        return this.getBlockLevel(tagName, config);
    }
    
    /** This returns the "hardness level" of a particular tag. This method is only
     * really useful to alignment programs that need some insight into the structural
     * flow of the document. For a description of what a "hardness level" is, see the
     * docs for the alignment program interface.
     *
     * @returns the "hardness level" of a particular tag
     * @param namespace The namespace ID.
     * @param tag the tag you're querying
     */
    public int getBlockLevel(String tagName, String namespace) {
        if(namespace.equals("")) {
            return this.getBlockLevel(tagName);
        }
        
        namespace = this.stripColon(namespace);
        
        XmlConfig config = (XmlConfig) configMap.get(namespace);
        
        return this.getBlockLevel(tagName, config);
    }
    
    /** This returns the "hardness level" of a particular tag. This method is only
     * really useful to alignment programs that need some insight into the structural
     * flow of the document. For a description of what a "hardness level" is, see the
     * docs for the alignment program interface.
     *
     * @returns the "hardness level" of a particular tag
     * @param config The XmlConfig object.
     * @param tag the tag you're querying
     */
    public int getBlockLevel(String tagName, XmlConfig config) {
        
        int result = 2;
        try {
            XmlElement element = config.getXmlElement(tagName);
            String hardness = element.getHardness();
            Integer level = (Integer)hardnessMap.get(hardness);
            result = level.intValue();
        } catch (NullPointerException ex) {
        }
        return result;
    }
    
    /** This gets a List containing the Strings that are translatable attributes
     * for the tag passed as a parameter. For example, in html &lt;img&gt; has the
     * attribute <code>alt</code> that can contain translatable text.
     *
     * @returns a List of Strings containing translatable attributes
     * @param tag the tag you're querying
     */
    public java.util.List getAttrList(String tagName) {
        XmlConfig config = xmlConfig;
        
        return this.getAttrList(tagName, config);
    }
    
    /** This gets a List containing the Strings that are translatable attributes
     * for the tag passed as a parameter. For example, in html &lt;img&gt; has the
     * attribute <code>alt</code> that can contain translatable text.
     *
     * @returns a List of Strings containing translatable attributes
     * @param namespace The namespace ID.
     * @param tag the tag you're querying
     */
    public java.util.List getAttrList(String tagName, String namespace) {
        
        if(namespace.equals("")) {
            return this.getAttrList(tagName);
        }
        
        namespace = this.stripColon(namespace);
        
        try {
            XmlConfig config = (XmlConfig) configMap.get(namespace);
            return this.getAttrList(tagName, config);
        } catch (NullPointerException ex) {
        }
        
        return this.getAttrList(tagName);
    }
    
    /** This gets a List containing the Strings that are translatable attributes
     * for the tag passed as a parameter. For example, in html &lt;img&gt; has the
     * attribute <code>alt</code> that can contain translatable text.
     *
     * @returns a List of Strings containing translatable attributes
     * @param config The XmlConfig object.
     * @param tag the tag you're querying
     */
    public java.util.List getAttrList(String tagName, XmlConfig config) {
        
        java.util.List names = new ArrayList();
        
        try {
            names = config.getAttributes(tagName);
        } catch (NullPointerException ex) {
        }
        
        return names;
    }
    
    private void initialiseHardnessMap() {
        hardnessMap.put("inline", new Integer(0));
        hardnessMap.put("softflow", new Integer(1));
        hardnessMap.put("soft", new Integer(2));
        hardnessMap.put("hard", new Integer(3));
        hardnessMap.put("hardsubsection", new Integer(4));
        hardnessMap.put("hardsection", new Integer(5));
    }
    
    private String stripColon(String colonString) {
        if(colonString.endsWith(":")) {
            int endString = colonString.lastIndexOf(":");
            return colonString.substring(0, endString);
        }
        return colonString;
    }
    
    public boolean containsTranslatableText(String tagName) {
        XmlConfig config = xmlConfig;
        
        return this.containsTranslatableText(tagName, config);
    }
    
    
    public boolean containsTranslatableText(String tagName, String namespace) {
        if(namespace.equals("")) {
            return this.containsTranslatableText(tagName);
        }
        
        namespace = this.stripColon(namespace);
        
        XmlConfig config = (XmlConfig) configMap.get(namespace);
        
        return this.containsTranslatableText(tagName, config);
    }
    
    public boolean containsTranslatableText(String tagName, XmlConfig config) {
        
        boolean result = pcdataTranslatableByDefault();
        try {
            XmlElement element = config.getXmlElement(tagName);
            result = element.getTranslatable();
        } catch (NullPointerException ex) {
        }
        return result;
    }
    
    public boolean includeCommentsInTranslatableSection(String tagName){
        return false;
    }

    public boolean pcdataTranslatableByDefault() {
        return true;
    }
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SegmenterTable.java
 *
 * Created on July 1, 2002, 5:22 PM
 */

package org.jvnet.olt.filters.html;

/** This Interface is a helper for the HtmlSegmentFactoryVisitor. It mirrors the
 * work of the TagTable, but is more geared to provide information pertinant to
 * translation/segmentation of the input file type.
 * @author timf
 */
public interface SegmenterTable {
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html.
     * @param tagname The tag you're querying.
     *
     * @return true if the tag doesn't contain segmentable text.
     */    
    
    boolean dontSegmentInsideTag(String tagname);
    /** This is for tags that contain translatable text in their attributes
     * @param tag The tag name you're querying
     * @return true if the tag contains translatble attributes.
     *
     */    
    boolean containsTranslatableAttribute(String tag);
    
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
    Character getEntityCharValue(String entity);
    
    /** This returns the "hardness level" of a particular tag. This method is only 
     * really useful to alignment programs that need some insight into the structural
     * flow of the document. For a description of what a "hardness level" is, see the
     * docs for the alignment program interface.
     *
     * @returns the "hardness level" of a particular tag
     * @param tag the tag you're querying
     */
    int getBlockLevel(String tag);
    
    /** This gets a List containing the Strings that are translatable attributes
     * for the tag passed as a parameter. For example, in html &lt;img&gt; has the
     * attribute <code>alt</code> that can contain translatable text.
     *
     * @returns a List of Strings containing translatable attributes
     * @param tag the tag you're querying
     */
    java.util.List getAttrList(String tag);
    
}
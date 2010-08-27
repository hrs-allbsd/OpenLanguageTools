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
 * SegmenterTable.java
 *
 * Created on July 1, 2002, 5:22 PM
 */

package org.jvnet.olt.parsers.tagged;

/** This Interface is a helper for the HtmlSegmentFactoryVisitor. It mirrors the
 * work of the TagTable, but is more geared to provide information pertinant to
 * translation/segmentation of the input file type.
 * @author timf
 */
public interface SegmenterTable {
    
    /** These are tags that you should not segment inside - for example
     * &gt;title&lt; tags in html. The text inside these tags will
     * be wordcounted as usual.
     * @param tagname The tag you're querying.
     *
     * @return true if the tag doesn't contain segmentable text.
     */  
    boolean dontSegmentInsideTag(String tagname);
    
    /** These are tags that you should not segment inside - for example
     * &gt;title&lt; tags in html. The text inside these tags will be
     * wordcounted as usual.
     * @param tagname The tag you're querying.
     * @param namespaceID The namespace ID.
     *
     * @return true if the tag doesn't contain segmentable text.
     */  
    boolean dontSegmentInsideTag(String tagname, String namespaceID);
    
    
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html. The text inside these tags will not
     * be wordcounted either.
     * @param tagname The tag you're querying.
     *
     * @return true if the tag doesn't contain segmentable text.
     */  
    boolean dontSegmentOrCountInsideTag(String tagname);
    
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html. The text will not be wordcounted either.
     * @param tagname The tag you're querying.
     * @param namespaceID The namespace ID.
     *
     * @return true if the tag doesn't contain segmentable text.
     */  
    boolean dontSegmentOrCountInsideTag(String tagname, String namespaceID);
    
    
    
    /** This is for tags that contain translatable text in their attributes
     * @param tag The tag name you're querying
     * @return true if the tag contains translatable attributes.
     *
     */
    boolean containsTranslatableAttribute(String tag);
    
    /** This is for tags that contain translatable text in their attributes
     * @param tag The tag name you're querying
     * @param namespaceID The namespace ID.
     * @return true if the tag contains translatble attributes.
     *
     */
    boolean containsTranslatableAttribute(String tag, String namespaceID);
    
    boolean containsTranslatableText(String tag);
    boolean containsTranslatableText(String tag, String namespaceID);
    
    /** This returns the value of an entity as a Character object. If the
     * entity in question isn't a character entity, it returns null. Note, it
     * is probably advisable that implementors shouldn't return &lt; for "lt", 
     * &amp; for "amp" and &gt; for "gt" since these are entities that are 
     * usually preserved (and would break sgml and xml documents were they 
     * converted to their character values)
     *
     * @return the entity character value.
     * @param entity the text of the entity
     */
    Character getEntityCharValue(String entity);
    
    /** This returns the "hardness level" of a particular tag. This method is only 
     * really useful to alignment programs that need some insight into the structural
     * flow of the document. For a description of what a "hardness level" is, see the
     * docs for the alignment program interface.
     *
     * @return the "hardness level" of a particular tag.
     * @param tag the tag you're querying
     */
    int getBlockLevel(String tag);
    
     /** This returns the "hardness level" of a particular tag. This method is only 
     * really useful to alignment programs that need some insight into the structural
     * flow of the document. For a description of what a "hardness level" is, see the
     * docs for the alignment program interface.
     *     
     * @param tag the tag you're querying
     * @param namespaceID The namespace ID
     *
     * @return the "hardness level" of a particular tag.
     */
    int getBlockLevel(String tag, String namespaceID);
    
    /** This gets a List containing the Strings that are translatable attributes
     * for the tag passed as a parameter. For example, in html &lt;img&gt; has the
     * attribute <code>alt</code> that can contain translatable text.
     *
     * @return a List of Strings containing translatable attributes.
     * @param tag the tag you're querying
     */
    java.util.List getAttrList(String tag);
    
    /** This gets a List containing the Strings that are translatable attributes
     * for the tag passed as a parameter. For example, in html &lt;img&gt; has the
     * attribute <code>alt</code> that can contain translatable text.
     *    
     * @param tag the tag you're querying
     * @param namespaceID The namespace ID
     * @return a List of Strings containing translatable attributes.
     */
    java.util.List getAttrList(String tag, String namespaceID);

    /** This allows us to mark special tags where any comments that are
     * included within the body of the tag, should be treated as translatable
     * text. In particular, this is required for the correct processing of
     * html &lt;script&gt; tags which contain javascript text. In these cases
     * the comment itself actually can contain translatable text, which should
     * be shown to translators and not written to the formatting section
     * as usual.
     */
    boolean includeCommentsInTranslatableSection(String tag);
    
    
    /** This determines whether we treat pcdata as translatable by default or
     * not. The containsTranslatableText(..) method overrides what's specified
     * here for any tags that apply.
     */ 
     boolean pcdataTranslatableByDefault();
}

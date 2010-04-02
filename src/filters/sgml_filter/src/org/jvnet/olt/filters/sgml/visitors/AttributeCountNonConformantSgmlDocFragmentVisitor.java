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
 * WordCountNonConformantSgmlDocFragmentVisitor.java
 *
 * Created on May 30, 2003, 10:41 AM
 */

package org.jvnet.olt.filters.sgml.visitors;

import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.filters.NonConformantSgmlTagParser.NonConformantSgmlTagParser;


import org.jvnet.olt.parsers.tagged.*;
import java.util.*;
import java.io.StringReader;

/**
 *
 * @author  timf
 */
public class AttributeCountNonConformantSgmlDocFragmentVisitor implements NonConformantSgmlDocFragmentParserVisitor {
    private SegmenterTable segmenterTable;
    private TagTable tagTable;
    private int count=0;
    private String language;
    private int index=0;
    private List tagList;
    
    /** Creates a new instance of WordCountNonConformantSgmlDocFragmentVisitor */
    public AttributeCountNonConformantSgmlDocFragmentVisitor(SegmenterTable segmenterTable, TagTable tagTable, String language, List tagList) {
        this.segmenterTable = segmenterTable;
        this.tagTable = tagTable;
        this.language = language;
        this.tagList = tagList;
        
    }
    
    public Object visit(SimpleNode node, Object data) {
        switch (node.getType()){
            case NonConformantSgmlDocFragmentParserTreeConstants.JJTOPEN_TAG:
                String tag = node.getNodeData();
                String tagName = node.getTagName();
                String prefix = node.getPrefix();
                
                if (!tagName.equals("suntransxmlfilter") && index < tagList.size()) { // we won't even try to count these
                    Tag t = (Tag)tagList.get(index);
                    String nameSpaceID = t.getNameSpaceID();
                    Map nsMap = t.getNameSpaceMap();
                    this.count += extractAttributesAndWordCount(tag, tagName, nameSpaceID, language, nsMap);
                } else { // above, we're indexing into a tagList - but suntransxmlfilter tags
                    // never appear on that list, so we don't want to inc. the index counter
                    // if we encounter a suntransxmlfilter lest we run off the end of the list
                    // and get an ArrayOutOfBounds exception
                    index --;
                }
                index++;
                
                break;
                
        }
        return data;
    }
    
    /** This method will wordcount any translatable attributes in the input tag
     * according to the SegmenterTable that is currently in use. In the future, we
     * may want to change this method to extract the translatable attributes into
     * an XLIFF subflow element. For now, it just does wordcounts.
     * @return a wordcount of the translatable attributes in the tag
     * @param tag the full text of the tag including attributes.
     * @param tagName the parsed name of the tag
     * @param ns the Namespace of this tag - this is actually not used by this code, and was an implementation mistake
     * @throws SgmlFilterException if there was some problem parsing the input tag
     */
    protected int extractAttributesAndWordCount(String tag, String tagName, String ns, String language, Map nameSpaceMap) throws SgmlFilterException {
        List attSegments = new ArrayList();
        int wordCount = 0;
        try {
            // we've got a map of the name spaces, so we need to iterate across
            // them, and search for attributes under each namespace
            // get the segmentable text and segment it if necessary
            String namespace = ns;
            Set nsSet = null;
            if (nameSpaceMap != null && nameSpaceMap.size()>0){
                nsSet = nameSpaceMap.entrySet();
                Iterator nsIterator = nsSet.iterator();
                while (nsIterator.hasNext()){
                    Map.Entry entry = (Map.Entry)nsIterator.next();
                    namespace = (String)entry.getValue();
                    
                    // get the list of segmentable attributes for this namespace
                    List attrList = segmenterTable.getAttrList(tagName, namespace);
		    
                    Iterator it = attrList.iterator();
                    while (it.hasNext()){
                        String attrName = (String)it.next();
                        String attribute = getAttribute(tag, attrName, namespace, nameSpaceMap);
                        if (attribute.length() > 0){
                            wordCount = wordCount + SgmlFilterHelper.wordCount(attribute, language, null);
                        }
                    }
                }
            } else { // no namespaces here, assume 1 empty namespace and continue
                List attrList = segmenterTable.getAttrList(tagName, "");
                Iterator it = attrList.iterator();
                while (it.hasNext()){
                    String attrName = (String)it.next();
                    String attribute = getAttribute(tag, attrName, "", nameSpaceMap);
                    if (attribute.length() > 0){
                        wordCount = wordCount + SgmlFilterHelper.wordCount(attribute, language, null);
                    }
                }
            }
            //System.out.println("Wordcount for tag " + tag + " is now " + wordCount);
            return wordCount;
        } catch (org.jvnet.olt.filters.NonConformantSgmlTagParser.ParseException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new SgmlFilterException("Corrupt tag found : \'" + tag +"\' during wordcount of tag contents");
        }
    }
    
    
    private String getAttribute(String tag, String attributeName, String ns, Map nsMap) throws org.jvnet.olt.filters.NonConformantSgmlTagParser.ParseException{
        //System.out.println("trying to get attribute " + attributeName + " from tag " + tag + " which has namespace " + ns);
        StringReader reader = new StringReader(tag);
        
        AttributeSgmlTagParserVisitor visitor = new AttributeSgmlTagParserVisitor(tag, attributeName, ns, nsMap );
        NonConformantSgmlTagParser parser = new NonConformantSgmlTagParser(reader);
        parser.parse();
        try {
            parser.walkParseTree(visitor,null);
        } catch (Throwable e){
            System.out.println("Caught Exception !");
            e.printStackTrace();
            throw new org.jvnet.olt.filters.NonConformantSgmlTagParser.ParseException(e.getMessage());
        }
        //System.out.println("Got attribute value " + visitor.getAttributeValue());
        return visitor.getAttributeValue();
    }
    
    public int getCount(){
        return this.count;
    }
}

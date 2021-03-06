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
 * SimpleSentenceHelper.java
 *
 * Created on November 1, 2005, 1:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

/**
 *
 * @author boris
 */
public class SimpleSentenceHelper {
    private static final Logger logger = Logger.getLogger(SimpleSentenceHelper.class.getName());

    /** Holds element and its contents when it's a tag
     *  element is text between tags or tags
     *  tag is parsed tag and attributes
     *
     */
    static class TagHolder {
	public final PivotBaseElement element;
	public final ContentTag tag;
	public final boolean closingTag;
        
        
	private int depth;
        private TagHolder closingTagHolder;
        
	TagHolder(PivotBaseElement element,ContentTag tag){
	    if(element == null || tag == null)
		throw new NullPointerException();
	    
	    this.element = element;
	    this.tag = tag;
            this.closingTag = element.getContent().trim().startsWith("</");
	}
	
	
	public int hashCode() {
	    return element.hashCode()*37+tag.hashCode()*11;
	}
	
	public boolean equals(Object obj) {
	    if(obj instanceof TagHolder){
		TagHolder other = (TagHolder)obj;
		
		//This is based on identity because this code deserves it!
		return other.element == element && other.tag == tag;
	    }
	    
	    return false;
	}

        public String toString() {
            return "TagHolder:["+element+"]["+tag+"]";
        }
	
	boolean isClosingTag(){
            return closingTag;
        }
        
        public int getDepth(){
            return depth;
        }
        
        public void setDepth(int depth){
            this.depth = depth;
        }
        
        public TagHolder getClosingTag(){
            return closingTagHolder;
        }
    }
    
    /** Creates a new instance of SimpleSentenceHelper */
    public SimpleSentenceHelper() {
    }
    
    static public String formatTranslation(String s,String matchSrc,String matchT){
	return formatTranslation(s,matchSrc,matchT,true);
    }
    
    static public  String formatTranslation(String s, String matchSrc, String matchT,boolean tagUpdate) {
	if(s == null || matchSrc == null || matchT == null)
	    throw new NullPointerException();
	
	if(!tagUpdate || s.equals(matchT))
	    return matchT;
	
	PivotText newSrc = new PivotText(s, true);
	PivotText oldSrc = new PivotText(matchSrc, true);
	PivotText oldTarget = new PivotText(matchT, true);
	
	List srcTags = buildTagHolders(newSrc);
	
	List tgtTags = buildTagHolders(oldTarget);
	
	List tagsToKeep = new LinkedList();
	
	//1: find tags from source in  the target
	for (Iterator i = srcTags.iterator(); i.hasNext();) {
	    TagHolder srcTag = (TagHolder) i.next();
	    
	    for (Iterator j = tgtTags.iterator(); j.hasNext();) {
		TagHolder tgtTag = (TagHolder)j.next();
		
		//if a source tag is found in target mark it to keep
		//remove from source tags so that at the end we have all tags
		// from source that need to be added to target
		// remove from target so that only those to remove are left
                int cmp = srcTag.tag.compare(tgtTag.tag);
		if(cmp == ContentTag.COMPARE_TAGS_EQUAL){
		    tagsToKeep.add(tgtTag);
		    i.remove();
		    j.remove();
		    break;
		}
		
	    }
	}
	
	List pivots = new LinkedList(Arrays.asList(oldTarget.elements()));
	
	removeRedundantTags(pivots,tgtTags,srcTags);
	addExtraTags(pivots,srcTags);
	
	String result = buildResult(pivots);
	
	return result;
    }
    
    /**
     * modifies pivots !
     */
    static void removeRedundantTags(List pivots,List targetTags,List extraTags){
        
        for (Iterator i = targetTags.iterator(); i.hasNext();) {
	    TagHolder hldr = (TagHolder) i.next();

            pivots.remove(hldr.element);

            TagHolder closer = hldr.getClosingTag();            
            if(closer != null){
                pivots.remove(closer.element);
             
                if(!targetTags.contains(closer))
                    extraTags.add(closer);
            }
	    
	}
        
    }
    
    static void addExtraTags(List pivots, List extraTags) {
	for (Iterator i = extraTags.iterator(); i.hasNext();) {
	    TagHolder hldr = (TagHolder) i.next();
	    pivots.add(hldr.element);
	}
    }
    
    static String buildResult(List pivots){
	StringBuffer sb = new StringBuffer();
	for (Iterator i = pivots.iterator(); i.hasNext();) {
	    PivotBaseElement elem = (PivotBaseElement) i.next();
	    
	    sb.append(elem.getContent());
	}
	return sb.toString();
    }
    
    static List buildTagHolders(PivotText pivot){
	List rv = new LinkedList();
	
	ContentTag[] ctags = pivot.ct;
	
	if(ctags.length == 0)
	    return rv;
	
	PivotBaseElement[] elems = pivot.elements();
	
	int offset = 0;
	int j = 0;
	for (int i = 0; i < elems.length; i++) {
	    if(! (elems[i].getElemType() == PivotBaseElement.TEXT)){
		if(offset == ctags[j].iPos){
		    rv.add(new TagHolder(elems[i],ctags[j]));
		    //stop condition; we used all content tags so just bail out
		    if(++j >= ctags.length)
			break;
		}
	    }
	    offset += elems[i].getVisibleLength();
	}
        calculateDepths(rv);
        connectTags(rv);
        
	return rv;
    }
    
    static private void calculateDepths(List rv){
        int depth = 0;
	//calculate depths;
        for (Iterator i = rv.iterator(); i.hasNext();) {
            TagHolder th = (TagHolder) i.next();
            
            if(!th.isClosingTag())
                depth++;
            
            th.setDepth(depth);

            if(th.isClosingTag())
                depth--;
            
        }
        
    }
    
    static private void connectTags(List rv){
        for (ListIterator i1 = rv.listIterator(); i1.hasNext();) {
            TagHolder th1 = (TagHolder) i1.next();
            
            int depth = th1.getDepth();
            
            for (ListIterator i2 = rv.listIterator(i1.nextIndex()); i2.hasNext();) {
                TagHolder th2 = (TagHolder) i2.next();
                
                if(!th2.isClosingTag())
                    continue;
                
                if(th2.getDepth() < depth)
                    break;

                if(th2.getDepth() == depth &&
                   th2.element.getTagName().equals(th1.element.getTagName())){

                    th1.closingTagHolder = th2;
                    break;
                }
            }
        }
                        
        
    }
}
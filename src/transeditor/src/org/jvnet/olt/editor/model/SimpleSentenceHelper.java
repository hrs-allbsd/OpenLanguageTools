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
import java.util.Vector;
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
	
	
	TagHolder(PivotBaseElement element,ContentTag tag){
	    if(element == null || tag == null)
		throw new NullPointerException();
	    
	    this.element = element;
	    this.tag = tag;
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
		if(srcTag.tag.compare(tgtTag.tag) == ContentTag.COMPARE_TAGS_EQUAL){
		    tagsToKeep.add(tgtTag);
		    i.remove();
		    j.remove();
		    break;
		}
		
	    }
	}
	
	List pivots = new LinkedList(Arrays.asList(oldTarget.elements()));
	
	removeRedundantTags(pivots,tgtTags);
	addExtraTags(pivots,srcTags);
	
	String result = buildResult(pivots);
	
	return result;
    }
    
    /**
     * modifies pivots !
     */
    static void removeRedundantTags(List pivots,List targetTags){
	for (Iterator i = targetTags.iterator(); i.hasNext();) {
	    TagHolder hldr = (TagHolder) i.next();
	    
	    pivots.remove(hldr.element);
	    
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
	    return Collections.EMPTY_LIST;
	
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
	
	return rv;
    }
}
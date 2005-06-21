
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.sgml.docbook;

import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.SgmlTagParser.*;
import org.jvnet.olt.filters.segmenter_facade.*;
import java.io.*;
import java.util.*;
/** This class is designed report information about the html for alignment purposes.
 * @author timf
 */
public class DocbookStatsVisitor implements SgmlDocFragmentParserVisitor {
    
    private List tags;
    private List words;
    private List nums;
    private String language;
    
    private static final int BUFFSIZE = 10;
    
    /** Creates a new instance of HtmlStatsVisitor
     * @param table The TagTable that this class uses.
     */
    public DocbookStatsVisitor(String language) {
        tags = new ArrayList();
        words = new ArrayList();
        nums = new ArrayList();
        this.language = language;
    }
    
    /** This method does the main work of getting stats on the html input.
     *
     * @param simpleNode The simpleNode that is being visited
     * @param obj An object
     * @return An object
     */
    public Object visit(org.jvnet.olt.parsers.SgmlDocFragmentParser.SimpleNode simpleNode, Object obj) {
        try {
            switch (simpleNode.getType()){
                case SgmlDocFragmentParserTreeConstants.JJTOPEN_TAG:
                case SgmlDocFragmentParserTreeConstants.JJTCLOSE_TAG:
                    // add these to the tags
                    tags.add(simpleNode.getNodeData());
                    break;
                case SgmlDocFragmentParserTreeConstants.JJTPCDATA:
                    // count words and numbers and things
                    StringReader reader = new StringReader(simpleNode.getNodeData());
                    SegmenterFacade segmenterFacade = new SegmenterFacade(reader, language);
                    
                    try {
                        segmenterFacade.parseForStats();
                    } catch (java.lang.Exception e){ // need to handle this better.
                        throw new Exception ("Caught an exception getting sentence statistics :" + e.getMessage());
                    }
                    words.addAll(segmenterFacade.getWordList());
                    words.addAll(segmenterFacade.getNumberList());
            }
            
        }catch (java.lang.Exception e){
            e.printStackTrace();
            System.exit(1);// erk - fixme
        }
        return obj;
    }
    
    public List getTags(){
        return tags;
    }
    
    public List getNumbers(){
        return nums;
    }
    
    public List getWords(){
        return words;
    }
    
}


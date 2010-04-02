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
 * ProcessedSgmlFileVisitor.java
 *
 * Created on May 7, 2003, 10:06 AM
 */

package org.jvnet.olt.filters.book; 

/**
 * This class runs over a book file, writes any translatable bits (text entities, and
 * a few special tags) to the xliff file, then goes off and queries any sgml files that
 * the book file
 * @author  timf
 */

import org.jvnet.olt.io.*;
import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.tagged.*;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.SgmlTagParser.*;
import org.jvnet.olt.filters.segmenter_facade.*;
import org.jvnet.olt.alignment.*;
import org.jvnet.olt.format.GlobalVariableManager;
import java.io.*;
import java.util.*;
import com.wutka.dtd.*;


public class ProcessedSgmlFileVisitor implements NonConformantSgmlDocFragmentParserVisitor {
    
    
    private GlobalVariableManager gvm;
    
    private StringBuffer markedSectionBuffer;
    private Stack markedSectionStack;
    private Stack openTagStack;
    private static final int BUFFSIZE = 300;
    
    private boolean inIgnoredMarkedSection = false;
    private boolean inNonTranslatableTag = false;
    
    private static final int ENTITYNAME=0;
    private static final int ENTITYVALUE=1;
    private static final int ENTITYTYPE=2;
    
    private static SegmenterTable segmenterTable = new org.jvnet.olt.filters.sgml.docbook.DocbookSegmenterTable();
    private static org.jvnet.olt.parsers.tagged.TagTable tagTable = new org.jvnet.olt.filters.sgml.docbook.DocbookTagTable();
    
    private int nonTranslatableTagLevel = 0;
    
    private List translatableFiles;
    private List singleSegmentTranslatableFiles;
    
    /**
     * This class will run over the input book file, and source any sgml files
     * that are referenced from the book file -- giving us a list of the translatable
     * files for this book.
     */
    public ProcessedSgmlFileVisitor(GlobalVariableManager gvm) {
        
        this.gvm = gvm;
        markedSectionBuffer = new StringBuffer(BUFFSIZE);
        markedSectionStack = new Stack();
        
        openTagStack = new Stack();
        
        this.translatableFiles = new ArrayList();
        this.singleSegmentTranslatableFiles = new ArrayList();
    }
    
    public List getTranslatableFiles(){
        return this.translatableFiles;
    }
    
    public List getSingleSegmentTranslatableFiles(){
        return this.singleSegmentTranslatableFiles;
    }
    
    
    /**
     * This method sets walks down the sgml parse tree and determines which entities
     * are for translation
     * @param simpleNode The simpleNode that is being visited
     * @param obj An object
     * @return An object
     */
    public Object visit( org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.SimpleNode simpleNode, Object obj) {
        try {
            String tagName = simpleNode.getTagName();
            String nodeData = simpleNode.getNodeData();
            
            switch (simpleNode.getType()){
                
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTINT_ENTITY:
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTENTITY:
                    if (inIgnoredMarkedSection){
                        String entity = nodeData;
                        if (nodeData.charAt(0) == '&' && nodeData.charAt(nodeData.length() -1) == ';'){
                            entity = entity.substring(1,nodeData.length() -1);
                        } else if (nodeData.charAt(0) == '%' && nodeData.charAt(nodeData.length() -1) == ';'){
                            entity = entity.substring(1,nodeData.length() -1);
                        }
                        if (gvm.getVariableType(entity).equals("SYSTEM")){
                            //System.out.println("*** DO NOT PROCESS process file " + gvm.resolveVariable(entity));
                        }
                    } else if (inNonTranslatableTag) {
                        // this means we've come across something like :
                        // <programlisting>&file;</programlisting>
                        // in which case, we need to save that info so we can
                        // parse the file using the sgml filter into a single
                        // segment.
                        String entity = nodeData;
                        if (nodeData.charAt(0) == '&' && nodeData.charAt(nodeData.length() -1) == ';'){
                            entity = entity.substring(1,nodeData.length() -1);
                        }  else if (nodeData.charAt(0) == '%' && nodeData.charAt(nodeData.length() -1) == ';'){
                            entity = entity.substring(1,nodeData.length() -1);
                        }
                        if (gvm.isVariableDefined(entity,"SYSTEM")){
                            singleSegmentTranslatableFiles.add(gvm.resolveVariable(entity,"SYSTEM"));
                            System.out.println("*** we should process file into a single segment" + gvm.resolveVariable(entity));
                        }
                    }
                    else {
                        String entity = nodeData;
                        // trim the % & or ; characters from the entity reference
                        if (nodeData.charAt(0) == '&' && nodeData.charAt(nodeData.length() -1) == ';'){
                            entity = entity.substring(1,nodeData.length() -1);
                        } else if (nodeData.charAt(0) == '%' && nodeData.charAt(nodeData.length() -1) == ';'){
                            entity = entity.substring(1,nodeData.length() -1);
                        }
                        
                        // we want to see if there's a system entity that
                        // points to a file
                        if (gvm.isVariableDefined(entity,"SYSTEM")){
                            translatableFiles.add(gvm.resolveVariable(entity,"SYSTEM"));
                            //System.out.println("*** we should process file " + gvm.resolveVariable(entity));
                        }
                        
                        
                    }
                    break;
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTMARKED_SECTION_TAG:
                    
                    String markedSectFlag = simpleNode.getMarkedSectFlag();
                    // now resolve the marked section
                    boolean isCurrentIgnoredMS = !SgmlFilterHelper.isIncludedMarkedSection(markedSectFlag, gvm); 
                    if (inIgnoredMarkedSection || isCurrentIgnoredMS){
                        markedSectionStack.push(markedSectFlag);
                        inIgnoredMarkedSection = true;
                        markedSectionBuffer.append("<![ "+markedSectFlag+ " [");
                    } else {
                        inIgnoredMarkedSection = false;
                    }
                    break;
                    
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTEND_MARKED_SECT:
                    if (inIgnoredMarkedSection){
                        String ms = (String)markedSectionStack.pop();
                        // don't care - do nothing, except write this to the m buffer
                        markedSectionBuffer.append(nodeData);
                        if (markedSectionStack.empty()){
                            inIgnoredMarkedSection = false;
                        }
                    }
                    break;
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTENTITY_DECL:
                    String[] val = parseEntity(nodeData);
                    
                    break;
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTOPEN_TAG:
                    // we're in a non translatable tag
                    if (segmenterTable.dontSegmentOrCountInsideTag(tagName)){
                        //System.out.println(tagName+" is non transltable");
                        nonTranslatableTagLevel++;
                        inNonTranslatableTag = true;
                    }
                    break;
                    
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTCLOSE_TAG:
                    if (segmenterTable.dontSegmentOrCountInsideTag(tagName)){
                        nonTranslatableTagLevel--;
                        if (nonTranslatableTagLevel == 0){
                            inNonTranslatableTag = false;
                            //System.out.println("back in translatable text content again");
                        }
                    }
            }
            
        }/* catch (org.jvnet.olt.parsers.SgmlTagParser.ParseException ex){
            System.out.println("Exception " + ex.getMessage());
        } */catch (java.lang.Exception e){
            // not good exception handling here. Really, the visit method of
            // the doc fragment parser should be coded to throw an exception...
            e.printStackTrace();
            throw new SgmlFilterException(e.getMessage());
        }
        return obj;
    }
    
    public String[] parseEntity(String s){
        try {
            StringReader reader = new StringReader(s);
            DTDParser parser = new DTDParser(reader);
            DTD dtd = parser.parse();
            
            Hashtable entities = dtd.entities;
            Hashtable externalDTDs = dtd.externalDTDs;
            String[] retval = new String[3];
            
            Enumeration e = dtd.entities.elements();
            
            while (e.hasMoreElements()){
                DTDEntity entity = (DTDEntity)e.nextElement();
                String name = "";
                String value = "";
                String type = "";
                //System.out.println(ent);
                if (entity.name != null){
                    //System.out.println("    Name: " + entity.name);
                    name = entity.name;
                }
                
                if (entity.isParsed()){
                    type="PARAMETER";
                } else {
                    type = "INTERNAL";
                }
                
                if (entity.value != null){
                    //System.out.println("    Value: "+entity.value);
                    value= entity.value;
                }
                
                if (entity.externalID != null){
                    
                    if (entity.externalID instanceof DTDSystem) {
                        //System.out.println("    System: "+
                        //entity.externalID.system);
                        value=entity.externalID.system;
                        type = "SYSTEM";
                    }
                    /*else {
                        DTDPublic pub = (DTDPublic) entity.externalID;
                     
                        System.out.println("    Public: "+
                        pub.pub+" "+pub.system);
                    }*/
                }
                
                /*if (entity.ndata != null){
                    System.out.println("    NDATA "+entity.ndata);
                }*/
                //System.out.println(name +" = "+value);
                gvm.setVariable(name,value,type);
                retval[ENTITYNAME]=name;
                retval[ENTITYVALUE]=value;
                retval[ENTITYTYPE]=type;
                return retval;
            }
            return null;
        } catch (Exception e){
            //System.out.println(e.getMessage());
            return null;
        }
        
        
    }
    
    public GlobalVariableManager getGlobalVariableManager(){
        return this.gvm;
    }
    
}
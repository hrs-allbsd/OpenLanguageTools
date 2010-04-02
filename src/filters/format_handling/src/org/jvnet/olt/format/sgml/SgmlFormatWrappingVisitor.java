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
 * SgmlFormatWrappingVisitor.java
 *
 * Created on 29 July 2002, 18:39
 */

package org.jvnet.olt.format.sgml;

import java.io.StringWriter;
import java.io.IOException;
import java.io.StringReader;
import org.jvnet.olt.io.EntityConversionFilterReader;
import org.jvnet.olt.io.ASCIIControlCodeMapFactory;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;

import org.jvnet.olt.parsers.SgmlDocFragmentParser.SimpleNode;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParserVisitor;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParserTreeConstants;

import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.DefaultTagTable;
import org.jvnet.olt.parsers.tagged.DefaultSegmenterTable;

/**
 *
 * @author  jc73554
 */
public class SgmlFormatWrappingVisitor
        implements SgmlDocFragmentParserVisitor, SgmlDocFragmentParserTreeConstants {
    private StringBuffer m_buffer;
    private GlobalVariableManager m_gvm;
    private boolean m_boolIgnoredMarkedSect;
    private int     m_markedSectDepth;
    private int     m_itCount;
    private int     m_bptCount;
    private int     m_eptCount;
    // used to indicate whether we should wrap all pcdata
    // encountered with <mrk...> tags (when trying to markup
    // non-segmentable text - we use the "phrase" mrk type
    private boolean protectPcdata = false;
    
    // used to indicate inline pcdata that shouldn't be
    // translated at all, like <comment> tags... We use
    // the "protect" mrk type
    private boolean dontTranslatePcdata = false;
    
    private SegmenterTable segmenterTable = null;
    private TagTable tagTable = null;
    
    private int phraseMrkLevel=0;
    private int protMrkLevel=0;
    
    /** Creates a new instance of SgmlFormatWrappingVisitor */
    public SgmlFormatWrappingVisitor(GlobalVariableManager gvm) {
        if (gvm == null) {
            m_gvm = new EntityManager();
        } else {
            m_gvm = gvm;
        }
        
        m_buffer = new StringBuffer();
        m_boolIgnoredMarkedSect = false;
        m_markedSectDepth = 0;
        m_itCount = 1;
        m_bptCount = 1;
        m_eptCount = 1;
        this.tagTable = new DefaultTagTable();
        this.segmenterTable = new DefaultSegmenterTable();
    }
    
    public SgmlFormatWrappingVisitor(GlobalVariableManager gvm, TagTable tagTable, SegmenterTable segmenterTable){
        this(gvm);
        this.tagTable = tagTable;
        this.segmenterTable = segmenterTable;
    }
    
    public String getWrappedString() {
        if (protectPcdata){
            // suggests that we've not closed a protected pcdata
            // section properly - make sure to close the <mrk> element
            m_buffer.append("</mrk>");
        }
        
        if (dontTranslatePcdata){
            m_buffer.append("</mrk>");
        }
        
        return m_buffer.toString();
    }
    
    public Object visit(SimpleNode node, Object data) {
        if(m_boolIgnoredMarkedSect) { processIgnoredSection(node); } else { processFormatting(node); }
        
        return data;
    }
    
    protected void processIgnoredSection(SimpleNode node) {
        int type = node.getType();
        String nodeText = node.getNodeData();
        
        switch(type) {
            case JJTMARKED_SECTION_TAG:
                m_markedSectDepth++;
                m_buffer.append(escapeSgmlTokens(nodeText));
                break;
            case JJTEND_MARKED_SECT:
                m_markedSectDepth--;
                m_buffer.append(escapeSgmlTokens(nodeText));
                if(m_markedSectDepth == 0) {
                    m_boolIgnoredMarkedSect = false;
                    m_buffer.append("</mrk>");
                }
                break;
            case JJTEOF:
                // make sure to close all open marked sections
                if (m_markedSectDepth == 0){
                    m_buffer.append("</mrk>");
                }
                break;
            default:
                if(node.isDisplayingNode()) {
                    m_buffer.append(escapeSgmlTokens(nodeText));
                }
                break;
        }
    }
    
    protected void processFormatting(SimpleNode node) {
        int type = node.getType();
        String nodeText = node.getNodeData();
        String tagName = node.getTagName();
        switch(type) {
            case JJTCOMMENT:
                writeIndividualFormat(nodeText);
                break;
            case JJTCDATA:
                writeIndividualFormat(nodeText);
                break;
            case JJTPROCESSING_INST:
                writeIndividualFormat(nodeText);
                break;
            case JJTMARKED_SECTION_TAG:
                
                //  Test the flag. If it resolves to 'IGNORE' set the visitor state
                //  and open a <it> tag. Otherwise write a <bpt> tag.
                String flag = node.getMarkedSectFlag();
                boolean boolEntityMeansIgnore = false;
                //try {
                boolEntityMeansIgnore =
                        ( m_gvm.isVariableDefined(flag) &&
                        m_gvm.resolveVariable(flag).equals("IGNORE") );
                //} catch (GlobalVariableManagerException e){
                //    throw new RuntimeException("Problem while resolving variable "+flag+ " while processing "+
                //    nodeText+" : "+e.getMessage());
                //}
                
                if(flag.equals("IGNORE") || boolEntityMeansIgnore ) {
                    //System.out.println("In ignored marked section - YAY!");
                    m_boolIgnoredMarkedSect = true;
                    m_markedSectDepth++;
                    m_buffer.append("<mrk mtype=\"protect\">");
                    m_buffer.append(escapeSgmlTokens(nodeText));
                } else {
                    //System.out.println("Not ignored marked section!");
                    writeBeginningPairedFormat(nodeText, null);
                }
                break;
            case JJTEND_MARKED_SECT:
                writeEndingPairedFormat(nodeText, null);
                break;
            case JJTOPEN_TAG:
                writeBeginningPairedFormat(nodeText, tagName);
                break;
            case JJTCLOSE_TAG:
                writeEndingPairedFormat(nodeText, tagName);
                break;
            case JJTENTITY:
                if (wordIsSystemEntityRef(nodeText)){
                    m_buffer.append("<mrk mtype=\"phrase\">"+escapeSgmlTokens(nodeText)+"</mrk>");
                } else {
                    m_buffer.append(escapeSgmlTokens(nodeText));
                }
                break;
            default:
                if(node.isDisplayingNode()) {
                    m_buffer.append(escapeSgmlTokens(nodeText));
                }
                break;
        }
    }
    
    protected void writeIndividualFormat(String format) {
        m_buffer.append("<it id=\"" + (m_itCount++) + "\" pos=\"open\">");
        m_buffer.append(escapeSgmlTokens(format));
        m_buffer.append("</it>");
    }
    
    protected void writeBeginningPairedFormat(String format, String tagName) {
        if (tagName != null){
            // decide whether to start protecting text from translation or not
            if (segmenterTable.dontSegmentInsideTag(tagName) || segmenterTable.dontSegmentOrCountInsideTag(tagName)){
                if (!protectPcdata){
                    protectPcdata = true;
                    //System.out.println("Adding phrase mrk tag to xliff output");
                    m_buffer.append("<mrk mtype=\"phrase\">");
                    phraseMrkLevel++;
                }else {
                    phraseMrkLevel++;
                }
            } else if (!segmenterTable.containsTranslatableText(tagName)){
                if (!dontTranslatePcdata){
                    dontTranslatePcdata = true;
                    //System.out.println("Adding protect mrk tag to xliff output");
                    m_buffer.append("<mrk mtype=\"protect\">");
                    protMrkLevel++;
                } else {
                    protMrkLevel++;
                }
            }
        }
        m_buffer.append("<bpt id=\"" + (m_bptCount++) + "\">");
        m_buffer.append(escapeSgmlTokens(format));
        m_buffer.append("</bpt>");
        
    }
    
    protected void writeEndingPairedFormat(String format, String tagName) {
        // end the text protection, if required.
        m_buffer.append("<ept id=\"" + (m_eptCount++) + "\">");
        m_buffer.append(escapeSgmlTokens(format));
        m_buffer.append("</ept>");
        if (tagName != null){
            if ((segmenterTable.dontSegmentInsideTag(tagName) || segmenterTable.dontSegmentOrCountInsideTag(tagName))
                && protectPcdata){
                phraseMrkLevel--;
                //System.out.println("Adding CLOSE mrk tag to xliff output");
                if (phraseMrkLevel==0){
                    m_buffer.append("</mrk>");
                    protectPcdata = false;
                }
            } else if (!segmenterTable.containsTranslatableText(tagName)){
                protMrkLevel--;
                if (protMrkLevel==0){
                    m_buffer.append("</mrk>");
                    dontTranslatePcdata = false;
                }
            }
        }
    }
    
    protected String escapeSgmlTokens(String string) {
        String returnVal="";
        try {
            StringReader stringReader = new StringReader(string);
            EntityConversionFilterReader reader = new EntityConversionFilterReader(stringReader);
            reader.setEntityMap(ASCIIControlCodeMapFactory.getAsciiControlCodesMap());
            
            StringWriter writer = new StringWriter();
            
            int ch;
            while((ch=reader.read()) != -1) {
                writer.write(ch);
            }
            returnVal = writer.toString();
        } catch(IOException exIO) {
            return returnVal;
        }
        return returnVal;
    }
    
    // quick test to see if an entity is a system entity - wrap it in mrk bits
    // if it is...
    private boolean wordIsSystemEntityRef(String entityRef){
        if (entityRef.charAt(0)=='&' && entityRef.charAt(entityRef.length()-1)==';'){
            return this.m_gvm.isVariableDefined(entityRef.substring(1,entityRef.length()-1),"SYSTEM");
        }
        return false;
    }
}

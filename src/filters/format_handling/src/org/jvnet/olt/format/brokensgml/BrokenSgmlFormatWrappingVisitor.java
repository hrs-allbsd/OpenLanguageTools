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
 * BrokenSgmlFormatWrappingVisitor.java
 *
 * Created on 29 July 2002, 18:39
 */

package org.jvnet.olt.format.brokensgml;

import java.io.StringWriter;
import java.io.IOException;
import java.io.StringReader;
import org.jvnet.olt.io.HTMLEscapeFilterReader;

import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;

import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.DefaultTagTable;
import org.jvnet.olt.parsers.tagged.DefaultSegmenterTable;

/**
 * Mostly copied from the sgml formatting stuff
 * @author  timf
 */
public class BrokenSgmlFormatWrappingVisitor
implements NonConformantSgmlDocFragmentParserVisitor, NonConformantSgmlDocFragmentParserTreeConstants {
    private StringBuffer m_buffer;
    private boolean m_boolIgnoredMarkedSect;
    private int     m_markedSectDepth;
    private int     m_itCount;
    private int     m_bptCount;
    private int     m_eptCount;
    // used to indicate whether we should wrap all pcdata
    // encountered with <mrk...> tags (when trying to protect
    // non-translatable text)
    private boolean protectPcdata = false;
    private SegmenterTable segmenterTable = null;
    private TagTable tagTable = null;
    
    private int mrkLevel=0;
    
    /** Creates a new instance of BrokenSgmlFormatWrappingVisitor */
    public BrokenSgmlFormatWrappingVisitor() {
        m_buffer = new StringBuffer();
        m_boolIgnoredMarkedSect = false;
        m_markedSectDepth = 0;
        m_itCount = 1;
        m_bptCount = 1;
        m_eptCount = 1;
        this.tagTable = new DefaultTagTable();
        this.segmenterTable = new DefaultSegmenterTable();
    }
    
    public BrokenSgmlFormatWrappingVisitor(TagTable tagTable, SegmenterTable segmenterTable){
        this();
        this.tagTable = tagTable;
        this.segmenterTable = segmenterTable;
    }
    
    public String getWrappedString() {
        if (protectPcdata){
            // suggests that we've not closed a protected pcdata
            // section properly - make sure to close the <mrk> element
            m_buffer.append("</mrk>");
        }
        return m_buffer.toString();
    }
    
    public Object visit(SimpleNode node, Object data) {
        if(m_boolIgnoredMarkedSect) { processIgnoredSection(node); }
        else { processFormatting(node); }
        
        return data;
    }
    
    protected void processIgnoredSection(SimpleNode node) {
        int type = node.getType();
        String nodeText = node.getNodeData();
        
        switch(type) {
            case JJTSTART_MARKED_SECT:
                m_markedSectDepth++;
                m_buffer.append(escapeSgmlTokens(nodeText));
                break;
            case JJTEND_MARKED_SECT:
                m_markedSectDepth--;
                m_buffer.append(escapeSgmlTokens(nodeText));
                if(m_markedSectDepth == 0) {
                    m_boolIgnoredMarkedSect = false;
                    m_buffer.append("</it>");
                }
                break;
            case JJTEOF:
                m_buffer.append("</it>");
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
            case JJTCOMMENT: // comments aren't translatable.
                m_buffer.append("<mrk mtype=\"protected\">"+escapeSgmlTokens(nodeText)+"</mrk>");
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
                //} catch (GlobalVariableManagerException e){
                //    throw new RuntimeException("Problem while resolving variable "+flag+ " while processing "+
                //    nodeText+" : "+e.getMessage());
                //}
                
                if(flag.equals("IGNORE") || boolEntityMeansIgnore ) {
                    m_boolIgnoredMarkedSect = true;
                    m_markedSectDepth =1;
                    m_buffer.append("<it id=\"" + (m_itCount++) + "\" pos=\"open\">");
                    m_buffer.append(escapeSgmlTokens(nodeText));
                }
                else {
                    writeBeginningPairedFormat(nodeText, null);
                }
                break;
            case JJTEND_MARKED_SECT:
                writeEndingPairedFormat(nodeText, null);
                break;
            case JJTJSP_INLINE: // we treat jsp inline tags just like normal open tags
            case JJTJSP:    
            case JJTOPEN_TAG:                
                writeBeginningPairedFormat(nodeText, tagName);
                break;
            case JJTCLOSE_TAG:
                writeEndingPairedFormat(nodeText, tagName);
                break;            
            case JJTENTITY:
                if (wordIsSystemEntityRef(nodeText)){
                    m_buffer.append("<mrk mtype=\"protected\">"+escapeSgmlTokens(nodeText)+"</mrk>");
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
            if (segmenterTable.dontSegmentInsideTag(tagName)){
                if (!protectPcdata){
                    protectPcdata = true;
                    //System.out.println("Adding mrk tag to xliff output");
                    m_buffer.append("<mrk mtype=\"protected\">");
                    mrkLevel++;
                }else {
                    mrkLevel++;
                }
            }
        }
        m_buffer.append("<bpt id=\"" + (m_bptCount++) + "\">");
        m_buffer.append(escapeSgmlTokens(format));
        m_buffer.append("</bpt>");
        if (this.tagTable.tagEmpty(tagName) &&
            this.segmenterTable.dontSegmentInsideTag(tagName)){ // if it's an empty tag that we'return mrking
            m_buffer.append("</mrk>");
            protectPcdata = false;
            mrkLevel--;
        }
        
    }
    
    protected void writeEndingPairedFormat(String format, String tagName) {
        // end the text protection, if required.
        
        m_buffer.append("<ept id=\"" + (m_eptCount++) + "\">");
        m_buffer.append(escapeSgmlTokens(format));
        m_buffer.append("</ept>");
        if (tagName != null){
            if (segmenterTable.dontSegmentInsideTag(tagName) && protectPcdata){
                mrkLevel--;
                //System.out.println("Adding CLOSE mrk tag to xliff output");
                if (mrkLevel==0){
                    m_buffer.append("</mrk>");
                    protectPcdata = false;
                }
            }
        }
    }
    
    protected String escapeSgmlTokens(String string) {
        String returnVal="";
        try {
            StringReader stringReader = new StringReader(string);
            HTMLEscapeFilterReader reader = new HTMLEscapeFilterReader(stringReader);
            
            StringWriter writer = new StringWriter();
            
            int ch;
            while((ch=reader.read()) != -1) {
                writer.write(ch);
            }
            returnVal = writer.toString();
        }
        catch(IOException exIO) {
            return returnVal;
        }
        return returnVal;
    }
    
    // quick test to see if an entity is a system entity - wrap it in mrk bits
    // if it is...
    private boolean wordIsSystemEntityRef(String entityRef){
        return false;
    }
}

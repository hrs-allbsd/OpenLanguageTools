
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TmxFormatWrappingVisitor.java
 *
 * Created on 29 July 2002, 18:39
 */

package org.jvnet.olt.format.sgml.tmx;

import java.io.StringWriter;
import java.io.IOException;
import java.io.StringReader;
import org.jvnet.olt.io.HTMLEscapeFilterReader;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SimpleNode;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParserVisitor;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParserTreeConstants;

/**
 *
 * @author  jc73554
 */
public class TmxFormatWrappingVisitor 
implements SgmlDocFragmentParserVisitor, SgmlDocFragmentParserTreeConstants
{  
    private StringBuffer m_buffer;
    private GlobalVariableManager m_gvm;
    private boolean m_boolIgnoredMarkedSect;
    private int     m_markedSectDepth;
    private int     m_itCount;
    private int     m_bptCount;
    private int     m_eptCount;
    
    /** Creates a new instance of SgmlFormatWrappingVisitor */
    public TmxFormatWrappingVisitor(GlobalVariableManager gvm)
    {
        m_gvm = gvm;
        m_buffer = new StringBuffer();
        m_boolIgnoredMarkedSect = false;
        m_markedSectDepth = 0;
        m_itCount = 1;
        m_bptCount = 1;
        m_eptCount = 1;
    }
    
    public String getWrappedString()
    {
        return m_buffer.toString();
    }
    
    public Object visit(SimpleNode node, Object data)
    {
        if(m_boolIgnoredMarkedSect) { processIgnoredSection(node); } 
        else { processFormatting(node); }
           
        return data;
    }
    
    protected void processIgnoredSection(SimpleNode node)
    {
        int type = node.getType();
        String nodeText = node.getNodeData();

        switch(type)
        {
        case JJTSTART_MARKED_SECT: 
            m_markedSectDepth++;
            m_buffer.append(escapeSgmlTokens(nodeText));            
            break; 
        case JJTEND_MARKED_SECT:
            m_markedSectDepth--;
            m_buffer.append(escapeSgmlTokens(nodeText));
            if(m_markedSectDepth == 0) 
            { 
                m_boolIgnoredMarkedSect = false;
                m_buffer.append("</it>");
            }
            break;             
        case JJTEOF:
            m_buffer.append("</it>");
            break;
        default:
            if(node.isDisplayingNode())
            {
                m_buffer.append(escapeSgmlTokens(nodeText));
            }
            break;
        }
    }
    
    protected void processFormatting(SimpleNode node)
    {
        int type = node.getType();
        String nodeText = node.getNodeData();
        
        switch(type)
        {
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
            ///try {
                boolEntityMeansIgnore = 
                ( m_gvm.isVariableDefined(flag) && 
                  m_gvm.resolveVariable(flag).equals("IGNORE") );
            //} catch (GlobalVariableManagerException e){
            //    throw new RuntimeException("Problem while resolving variable "+flag+ " while processing "+
            //    nodeText+" : "+e.getMessage());
            //}

            if(flag.equals("IGNORE") || boolEntityMeansIgnore )
            {
                m_boolIgnoredMarkedSect = true;
                m_markedSectDepth =1;
                m_buffer.append("<it i=\"" + (m_itCount++) + "\" pos=\"begin\">");
                m_buffer.append(escapeSgmlTokens(nodeText));
            }
            else
            { 
                writeBeginningPairedFormat(nodeText);
            }
            break; 
        case JJTEND_MARKED_SECT:    
            writeEndingPairedFormat(nodeText);
            break; 
        case JJTOPEN_TAG:
            writeBeginningPairedFormat(nodeText);
            break; 
        case JJTCLOSE_TAG:
            writeEndingPairedFormat(nodeText);
            break;
        default:
            if(node.isDisplayingNode())
            {
                m_buffer.append(escapeSgmlTokens(nodeText));
            }
            break;
        }        
    }
 
    protected void writeIndividualFormat(String format)
    {
        m_buffer.append("<it i=\"" + (m_itCount++) + "\" pos=\"begin\">");
        m_buffer.append(escapeSgmlTokens(format));
        m_buffer.append("</it>");       
    }
    
    protected void writeBeginningPairedFormat(String format)
    {
        m_buffer.append("<bpt i=\"" + (m_bptCount++) + "\">");
        m_buffer.append(escapeSgmlTokens(format));
        m_buffer.append("</bpt>");       
    }

    protected void writeEndingPairedFormat(String format)
    {
        m_buffer.append("<ept i=\"" + (m_eptCount++) + "\">");
        m_buffer.append(escapeSgmlTokens(format));
        m_buffer.append("</ept>");       
    }

    protected String escapeSgmlTokens(String string)
    {
        String returnVal="";
        try
        {
            StringReader stringReader = new StringReader(string);
            HTMLEscapeFilterReader reader = new HTMLEscapeFilterReader(stringReader);
        
            StringWriter writer = new StringWriter();
            
            int ch;
            while((ch=reader.read()) != -1)
            {
                writer.write(ch);
            }
            returnVal = writer.toString();
        }
        catch(IOException exIO)
        {
            return returnVal;
        }
        return returnVal;
    }
}

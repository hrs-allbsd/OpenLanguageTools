
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

package org.jvnet.olt.format.printf;

import java.io.StringWriter;
import java.io.IOException;
import java.io.StringReader;
import org.jvnet.olt.io.*;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;

import org.jvnet.olt.parsers.PrintfParser.*;

/**
 * This format wrapper writes either XLIFF or TMX formatting
 * - it really only uses one tag, the <it> tag to do this, but XLIFF
 * and TMX are very slightly different in the way they handle these, so
 * it makes sense to use the same code.
 * @author  timf
 */
public class PrintfFormatWrappingVisitor implements PrintfParserVisitor, PrintfParserTreeConstants {
    private StringBuffer buffer;
    private int     m_itCount;
    private boolean isTMX = false;
    
    /** Creates a new instance of PrintfFormatWrappingVisitor */
    public PrintfFormatWrappingVisitor() {
        
        buffer = new StringBuffer();
        m_itCount = 1;
    }
    
    public void setUseTMXBehaviour(boolean isTMX){
        this.isTMX = isTMX;
    }
    
    public String getWrappedString() {
        return buffer.toString();
    }
    
    public Object visit(SimpleNode node, Object data) {
        processFormatting(node);
        return data;
    }
    
    protected void processFormatting(SimpleNode node) {
        int type = node.getType();
        String nodeText = node.getNodeData();
        switch(type) {
            case JJTCONV:
            //B.S. 09/2004
            case JJTMSGCONV:
            case JJTREGEXPCONV:
                
            case JJTVARIABLECONV:
            case JJTSLASHESCAPE:
            case JJTOTHERESCAPE:
            case JJTNULLCONV:
                writeIndividualFormat(nodeText);
                break;
            default:
                buffer.append(escapeSgmlTokens(nodeText));
                break;
        }
    }
    
    protected void writeIndividualFormat(String format) {
        if (isTMX){
            buffer.append("<it pos=\"begin\">");
        } else {
            buffer.append("<it id=\"" + (m_itCount++) + "\" pos=\"open\">");
        }
        buffer.append(escapeSgmlTokens(format));
        buffer.append("</it>");
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
        }
        catch(IOException exIO) {
            return returnVal;
        }
        return returnVal;
    }
}

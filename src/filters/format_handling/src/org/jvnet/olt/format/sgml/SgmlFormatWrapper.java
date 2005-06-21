
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SgmlFormatWrapper.java
 *
 * Created on 29 July 2002, 18:26
 */

package org.jvnet.olt.format.sgml;

import org.jvnet.olt.format.FormatWrapper;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.DefaultTagTable;
import org.jvnet.olt.parsers.tagged.DefaultSegmenterTable;

/**
 *
 * @author  jc73554
 */
public class SgmlFormatWrapper implements FormatWrapper
{
    private GlobalVariableManager m_gvm;
    private TagTable tagTable=null;
    private SegmenterTable segmenterTable=null;
    
    /** Creates a new instance of SgmlFormatWrapper */
    public SgmlFormatWrapper(GlobalVariableManager gvm)
    {
        m_gvm = gvm;
        tagTable = new DefaultTagTable();
        segmenterTable = new DefaultSegmenterTable();
    }
    
    /** Creates a new instance of SgmlFormatWrapper */
    public SgmlFormatWrapper(GlobalVariableManager gvm, TagTable tagTable, SegmenterTable segmenterTable){
        m_gvm = gvm;
        this.tagTable = tagTable;
        this.segmenterTable = segmenterTable;
    }
    
    public String wrapFormatting(String text) throws InvalidFormattingException
    {
        try
        {
            StringReader reader = new StringReader(text);
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader);
            parser.parse();
            SgmlFormatWrappingVisitor visitor;
            // if these guys are null, we'll just use traditional sgml format wrapping
            if (tagTable == null || segmenterTable == null){
                visitor = new SgmlFormatWrappingVisitor(m_gvm);
            }
            else {
                visitor = new SgmlFormatWrappingVisitor(m_gvm, tagTable, segmenterTable);
            }
                
            
            parser.walkParseTree(visitor, null);
            return visitor.getWrappedString();
        }
        catch(Exception exParse)
        {
            InvalidFormattingException newEx =  new InvalidFormattingException(exParse.getMessage());
            newEx.setStackTrace(exParse.getStackTrace());            
            throw newEx;
        }
    }
    
    
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BrokenSgmlFormatWrapper.java
 *
 * Created on 29 July 2002, 18:26
 */

package org.jvnet.olt.format.brokensgml;

import org.jvnet.olt.format.FormatWrapper;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
/**
 * This stuff is mostly copied from the sgml format wrapper (sorry)
 * @author  timf
 */
public class BrokenSgmlFormatWrapper implements FormatWrapper
{
    private TagTable tagTable=null;
    private SegmenterTable segmenterTable=null;
    
 
    
    /** Creates a new instance of BrokenSgmlFormatWrapper */
    public BrokenSgmlFormatWrapper(TagTable tagTable, SegmenterTable segmenterTable){
        this.tagTable = tagTable;
        this.segmenterTable = segmenterTable;
    }
    
    public String wrapFormatting(String text) throws InvalidFormattingException
    {
        try
        {
            StringReader reader = new StringReader(text);
            NonConformantSgmlDocFragmentParser parser = new NonConformantSgmlDocFragmentParser(reader);
            parser.parse();
            BrokenSgmlFormatWrappingVisitor visitor;
            // if these guys are null, we'll just use traditional brokensgml format wrapping
            if (tagTable == null || segmenterTable == null){
                visitor = new BrokenSgmlFormatWrappingVisitor();
            } 
            else {
                visitor = new BrokenSgmlFormatWrappingVisitor(tagTable, segmenterTable);
            }
                
            
            parser.walkParseTree(visitor, null);
            return visitor.getWrappedString();
        }
        catch(Exception exParse)
        {
            System.out.println("Parse error found " + exParse.getMessage());
            exParse.printStackTrace();
            throw new InvalidFormattingException(exParse.getMessage());
        }
    }
    
    
}

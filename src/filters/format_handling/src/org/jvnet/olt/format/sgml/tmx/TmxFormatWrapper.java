
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * STmxFormatWrapper.java
 *
 * Created on 29 July 2002, 18:26
 */

package org.jvnet.olt.format.sgml.tmx;

import org.jvnet.olt.format.FormatWrapper;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser;

/**
 *
 * @author  jc73554
 */
public class TmxFormatWrapper implements FormatWrapper
{
    private GlobalVariableManager m_gvm;
    
    /** Creates a new instance of SgmlFormatWrapper */
    public TmxFormatWrapper(GlobalVariableManager gvm)
    {
        m_gvm = gvm;
    }
    
    public String wrapFormatting(String text) throws InvalidFormattingException
    {
        try
        {
            StringReader reader = new StringReader(text);
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader);
            parser.parse();
            
            TmxFormatWrappingVisitor visitor = new TmxFormatWrappingVisitor(m_gvm);
            
            parser.walkParseTree(visitor, null);
            return visitor.getWrappedString();
        }
        catch(Exception exParse)
        {
            throw new InvalidFormattingException(exParse.getMessage());
        }
    }
}

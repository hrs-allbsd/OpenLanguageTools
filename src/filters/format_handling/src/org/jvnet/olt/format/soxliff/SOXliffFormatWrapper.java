
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SOXliffFormatWrapper.java
 *
 * Created on 29 July 2002, 18:26
 */

package org.jvnet.olt.format.soxliff;

import org.jvnet.olt.format.FormatWrapper;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.format.brokensgml.*;
import org.jvnet.olt.format.plaintext.*;
import org.jvnet.olt.parsers.tagged.*;

/**
 * SOXliff can contain broken formatting. We try to use BrokenSgml format
 * wrapper when it fails plain format wrapper is used.
 *
 * @author  michal
 */
public class SOXliffFormatWrapper implements FormatWrapper {

    private PlainTextFormatWrapper plainFormat = null;
 
    public SOXliffFormatWrapper() {
        plainFormat = new PlainTextFormatWrapper();
    }
    
    public String wrapFormatting(String text) throws InvalidFormattingException {

        String result = "";
        try {
            StringReader reader = new StringReader(text);
            NonConformantSgmlDocFragmentParser parser = new NonConformantSgmlDocFragmentParser(reader);
            parser.parse();
            BrokenSgmlFormatWrappingVisitor visitor;
            
            visitor = new BrokenSgmlFormatWrappingVisitor();
            
            parser.walkParseTree(visitor, null);
            result = visitor.getWrappedString();
        }
        catch(Throwable t) {
            System.err.println("SOXliff broken formatting: " + t.getMessage());
            result = plainFormat.wrapFormatting(text);
        }
        return result;
    }
    
}

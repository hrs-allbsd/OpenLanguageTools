
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * PrintfFormatExtractor.java
 *
 * Created on October 17, 2003, 3:06 PM
 */

package org.jvnet.olt.format.printf;
import java.util.HashMap;
import java.io.StringWriter;
import java.io.IOException;
import java.io.StringReader;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.PrintfParser.*;
import org.jvnet.olt.format.InvalidFormattingException;

/**
 *
 * @author  timf
 */
public class PrintfFormatExtractor implements org.jvnet.olt.format.FormatExtractor {
    
    /** Creates a new instance of PrintfFormatExtractor */
    public PrintfFormatExtractor() {
    }
    
    /** 
     * This method returns a list of the formatting codes that were found in the
     *  string presented for formatting.
     */
    public java.util.HashMap getFormatting(String string, org.jvnet.olt.format.GlobalVariableManager gvm) throws org.jvnet.olt.format.InvalidFormattingException {
        HashMap formats = new HashMap();
        try {
            StringReader reader = new StringReader(string);
            PrintfParser parser = new PrintfParser(reader); 
            parser.parse();
            PrintfFormatExtractionVisitor visitor = new PrintfFormatExtractionVisitor();            
            parser.walkParseTree(visitor, null);
            formats = visitor.getFormatItems();
        } catch (Throwable e){
            throw new InvalidFormattingException("Invalid printf sequence while trying to extract formatting from " +string);
            
        }      
        return formats;
    }
    
}

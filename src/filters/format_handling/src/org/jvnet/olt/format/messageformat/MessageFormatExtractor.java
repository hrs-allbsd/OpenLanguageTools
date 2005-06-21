
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * MessageFormatExtractor.java
 *
 * Created on October 23, 2003, 8:54 pm
 */

package org.jvnet.olt.format.messageformat;
import java.util.HashMap;
import java.io.StringWriter;
import java.io.IOException;
import java.io.StringReader;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.MessageFormatParser.*;
import org.jvnet.olt.format.InvalidFormattingException;

/**
 * This class is designed to extract the formatting from text that contains 
 * java.text.MessageFormat style formatting.
 *
 * @author  timf
 */
public class MessageFormatExtractor implements org.jvnet.olt.format.FormatExtractor {
    
    /** Creates a new instance of MessageFormatExtractor */
    public MessageFormatExtractor() {
    }
    
    /** 
     * This method returns a list of the formatting codes that were found in the
     *  string presented for formatting.
     */
    public java.util.HashMap getFormatting(String string, org.jvnet.olt.format.GlobalVariableManager gvm) throws org.jvnet.olt.format.InvalidFormattingException {
        HashMap formats = new HashMap();
        try {
            StringReader reader = new StringReader(string);
            MessageFormatParser parser = new MessageFormatParser(reader); 
            parser.parse();
            MessageFormatExtractionVisitor visitor = new MessageFormatExtractionVisitor();            
            parser.walkParseTree(visitor, null);
            formats = visitor.getFormatItems();
        } catch (Throwable e){
            throw new InvalidFormattingException("Invalid MessageFormat sequence while trying to extract formatting from " +string);
            
        }      
        return formats;
    }
    
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * PrintfFormatWrapper.java
 *
 * Created on October 17, 2003, 3:04 PM
 */

package org.jvnet.olt.format.printf;
import org.jvnet.olt.io.*;
import java.io.*;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.parsers.PrintfParser.*;
/**
 * This code is designed to take input text that has printf formatting, and produce
 * an XLIFF representation of it. The classes that do most of the work are in the
 * printf parser, which is in the utilities area of the workspace.
 *
 * If the printf parser fails for some reason, then we still try to pass the code into
 * the HTMLEscapeFilterReader, so that we can produce valid pcdata that can appear
 * in an XML file.
 *
 * @author  timf
 */
public class PrintfFormatWrapper implements org.jvnet.olt.format.FormatWrapper {
    
    private boolean useTMXBehaviour = false;
    
    /** Creates a new instance of PrintfFormatWrapper */
    public PrintfFormatWrapper() {
    }
    
    public PrintfFormatWrapper(boolean useTMXBehaviour) {
        this.useTMXBehaviour = useTMXBehaviour;
    }
    
    public String wrapFormatting(String text) throws org.jvnet.olt.format.InvalidFormattingException {
        String output = "";
        try {
            StringReader reader = new StringReader(text);
            PrintfParser parser = new PrintfParser(reader);
            parser.parse();
            PrintfFormatWrappingVisitor visitor = new PrintfFormatWrappingVisitor();
            visitor.setUseTMXBehaviour(useTMXBehaviour);
            parser.walkParseTree(visitor, null);
            output = visitor.getWrappedString();
        } catch (Throwable e){
            System.out.println("Invalid printf formatting for "+ text);
            // e.printStackTrace();
            // some error trying to wrap this string ! Better just escape the
            // > < and & characters so it's at least good XML
            try {
                BufferedReader buf = new BufferedReader(new HTMLEscapeFilterReader(new StringReader(text)));
                StringWriter writer= new StringWriter();
                int i;
                while ((i = buf.read()) != -1){
                    writer.write(i);
                }
                output = writer.toString();
            } catch (java.io.IOException ex){
                throw new InvalidFormattingException("IOException while wrapping printf formatting on " +text+" "+e.getMessage());
            }
        }
        
        return output;
    }
    
}

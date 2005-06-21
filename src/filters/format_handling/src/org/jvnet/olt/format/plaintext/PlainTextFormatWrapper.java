
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * PlainTextWrapper.java
 *
 * Created on 31 July 2002, 17:29
 */

package org.jvnet.olt.format.plaintext;
import org.jvnet.olt.io.*;
import java.io.*;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.InvalidFormattingException;

/**
 *
 * @author  jc73554
 */
public class PlainTextFormatWrapper implements FormatWrapper {
    
    /** Creates a new instance of PlainTextWrapper */
    public PlainTextFormatWrapper() {
    }
    
    public String wrapFormatting(String text) throws InvalidFormattingException {
        String output = "";
        //System.out.println("input = " + text);
        try {
            StringReader stringReader = new StringReader(text);
            EntityConversionFilterReader reader = new EntityConversionFilterReader(stringReader);
            reader.setEntityMap(ASCIIControlCodeMapFactory.getAsciiControlCodesMap());
            
            BufferedReader buf = new BufferedReader(reader);
            StringWriter writer= new StringWriter();
            int i;
            while ((i = buf.read()) != -1){
                writer.write(i);
            }
            output = writer.toString();
        } catch (java.io.IOException e){
            throw new InvalidFormattingException("Invalid formatting while trying to wrap text " +text);
        }       
        //System.out.println("output = " + output);
        return output;
    }
    
}

/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/

/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * MessageFormatWrapper.java
 *
 * Created on October 23, 2003, 8:54 pm
 */

package org.jvnet.olt.format.messageformat;
import org.jvnet.olt.io.*;
import java.io.*;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.parsers.MessageFormatParser.*;
/**
 * This code is designed to take input text that has java.text.MessageFormat style
 * formatting, and produce an XLIFF representation of it. The classes that do most 
 * of the work are in the MessageFormat parser, which is in the utilities area of the workspace.
 *
 * If the MessageFormat parser fails for some reason, then we still try to pass the code into
 * the HTMLEscapeFilterReader, so that we can produce valid pcdata that can appear
 * in an XML file.
 *
 * @author  timf
 */
public class MessageFormatWrapper implements org.jvnet.olt.format.FormatWrapper {
    
    private boolean useTMXBehaviour = false;
    
    /** Creates a new instance of MessageFormatWrapper */
    public MessageFormatWrapper() {
    }
    
    public MessageFormatWrapper(boolean useTMXBehaviour){
        this.useTMXBehaviour = useTMXBehaviour;
    }
    
    public String wrapFormatting(String text) throws org.jvnet.olt.format.InvalidFormattingException {
        String output = "";
        try {
            StringReader reader = new StringReader(text);
            MessageFormatParser parser = new MessageFormatParser(reader);
            parser.parse();
            MessageFormatWrappingVisitor visitor = new MessageFormatWrappingVisitor();    
            visitor.setUseTMXBehaviour(this.useTMXBehaviour);
            parser.walkParseTree(visitor, null);
            output = visitor.getWrappedString();
        } catch (Throwable e){
            System.out.println("Invalid MessageFormat formatting for text "+ text);
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
                throw new InvalidFormattingException("IOException while trying to wrap MessageFormat " +text+" "+e.getMessage());
            }
        }
        
        //System.out.println("output = " + output);
        return output;
    }
    
}

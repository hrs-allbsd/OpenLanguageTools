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
 * SOXliffToXliff.java
 *
 * Created on April 24, 2006, 9:42 AM
 *
 */

package org.jvnet.olt.filters.soxliff;

import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import java.io.*;
import org.jvnet.olt.filters.segmenters.formatters.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;

/**
 * Convert xliff file generated in StarOffice database to OLT xliff
 *
 * @author michal
 */
public class SOXliffToXliff {
    
    /**
     * Creates a new instance of SOXliffToXliff 
     */
    private SOXliffToXliff() {
    }
    
    /**
     * Convert StarOffice xliff to OLT xliff
     *
     * @param soXliffFile to be converted
     * @param sourceLanguage of the StarOffice xliff file
     *
     * @throws SOXliffException
     */
    public static void convert(File soXliffFile, String sourceLanguage) throws SOXliffException {
        
        try {
            
            XliffWriterFacade writer = XliffWriterFacade.createWriter(soXliffFile,sourceLanguage);
            
            ParserVisitor visitor = new ParserVisitor(writer);
            
            // do not resolve entity
            DefaultHandler resolver = new DefaultHandler() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    return new InputSource(new StringReader(""));
                }
            };
            
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEntityResolver(resolver);
            
            Document doc = null;
            
            doc = xmlReader.read(soXliffFile);
            doc.accept( visitor );
            
            writer.close();
        } catch(Throwable t) {
            t.printStackTrace(System.err);
            throw new SOXliffException("SOXliff Error: " + t.getMessage(),t);
        }
    }

}

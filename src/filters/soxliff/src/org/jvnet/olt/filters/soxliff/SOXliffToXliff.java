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

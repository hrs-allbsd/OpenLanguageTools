/*
 * SOXliffToXliff.java
 *
 * Created on April 24, 2006, 9:42 AM
 *
 */

package org.jvnet.olt.soxliff_backconv;

import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import java.io.*;
import org.jvnet.olt.soxliff_backconv.util.*;
import java.util.zip.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;

/**
 * Backconverter implementation for StarOffice xliff implementation
 *
 * @author michal
 */
public class BackconverterSOXliff {
    
    private static final String CONTENT="content.xlf";
    
    /**
     * disable default constructor
     */
    private BackconverterSOXliff() {
    }
    
    /**
     * backconvert the OLT xliff
     *
     * @param xlzFile that should be backconverted
     * @param directory where the result should be placed
     *
     * @throws SOXliffBackException
     */
    public static void backconvert(File xlzFile, String directory) throws SOXliffBackException {
        
        try {
            
            ZipFile zipFile     = new ZipFile(xlzFile);
            ZipEntry content = zipFile.getEntry(CONTENT);
            
            ParserVisitor visitor = new ParserVisitor(directory);
            
            // use empty entity resolver
            DefaultHandler resolver = new DefaultHandler() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    return new InputSource(new StringReader(""));
                }
            };
            
            
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEntityResolver(resolver);
            
            InputStream zipIs = zipFile.getInputStream(content);
            
            // remove tag protection from OLT xliff
            Reader removed = RemoveTagProtection.convert(new InputStreamReader(zipIs,"UTF-8"));
            Document doc = xmlReader.read(removed);
            
            
            doc.accept( visitor );
            visitor.flush();
            
        } catch(Throwable t) {
            throw new SOXliffBackException("SOXliffBackconv Error: " + t.getMessage(),t);
        }
    }

}

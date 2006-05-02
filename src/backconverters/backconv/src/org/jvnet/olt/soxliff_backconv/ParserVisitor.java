/*
 * ParserVisitor.java
 *
 * Created on April 24, 2006, 10:07 AM
 *
 */

package org.jvnet.olt.soxliff_backconv;


import org.dom4j.*;
import java.util.*;
import java.io.*;
import org.dom4j.io.SAXReader;
import org.jvnet.olt.soxliff_backconv.util.*;

/**
 * Visitor that walks through OLT xliff elements and construct the
 * original StarOffice xliff
 *
 * @author michal
 */
public class ParserVisitor extends VisitorSupport {
    
    // xliff tag definition
    private static final String XLIFF_FILE              = "file";
    private static final String XLIFF_FILE_ORIGINAL_ATT = "original";
    private static final String XLIFF_FILE_SOURCELANG_ATT = "source-language";
    private static final String XLIFF_FILE_TARGETLANG_ATT = "target-language";
    private static final String XLIFF_SOURCE        = "source";
    private static final String XLIFF_TARGET        = "target";
    private static final String XLIFF_CONTEXT_GROUP = "context-group";
    private static final String XLIFF_CONTEXT = "context";
    private static final String XLIFF_CONTEXT_TYPE = "context-type";
    private static final String XLIFF_TRANSUNIT   = "trans-unit";
    
    private static final String SO_ORIGINAL_ID = "x-original-id";
    private static final String SO_RESTYPE       = "x-restype";
    private static final String SO_SUBFORMAT  = "x-subformat-ws";
    
    SAXReader saxReader        = null;
    XliffWriterFacade writer       = null;
    
    private String directory = "";
    private String filename = "";
    private String sourceLang = "";
    private String targetLang = "";
    private String originalId = "";
    private StringBuffer source = new StringBuffer("");
    private StringBuffer target  = new StringBuffer("");
    
    /**
     * Create new instance of the visitor
     *
     * @param directory where the result should be placed
     */
    public ParserVisitor(String directory) {
        super();
        this.directory = directory;
        saxReader = new SAXReader();
    }
    
    /**
     * pass every OLT xliff element and do the conversion job
     *
     * @param element of the OLT xliff
     */
    public void visit(Element element) {
        
        try {
            // xliff file element
            if(XLIFF_FILE.equals(element.getName())) {
                
                filename     = element.attributeValue(XLIFF_FILE_ORIGINAL_ATT);
                
                sourceLang = element.attributeValue(XLIFF_FILE_SOURCELANG_ATT);
                
                targetLang = element.attributeValue(XLIFF_FILE_TARGETLANG_ATT);
                
                writer = XliffWriterFacade.createWriter(directory + File.separator + filename,sourceLang,targetLang);
            }
            
            // translation unit element
            if(XLIFF_TRANSUNIT.equals(element.getName())) {
                
                // get source segment
                String source = element.elementText(XLIFF_SOURCE);
                if(source==null || "".equals(source)) {
                    throw new RuntimeException("Document contains empty source");
                }
                
                // get target segment
                String target = element.elementText(XLIFF_TARGET);
                if(target==null || "".equals(target)) {
                    throw new RuntimeException("Document contains empty target");
                }
                
                String originalId = "";
                String formatting = "";
                
                // get context elements
                List contextGroups = element.elements(XLIFF_CONTEXT_GROUP);
                Iterator it = contextGroups.iterator();
                
                while(it.hasNext()) {
                    Element contextGroup = (Element)it.next();
                    List contexts = contextGroup.elements(XLIFF_CONTEXT);
                    Iterator jt = contexts.iterator();
                    while(jt.hasNext()) {
                        Element context = (Element)jt.next();
                        String contextType = context.attributeValue(XLIFF_CONTEXT_TYPE);
                        
                        // original StarOffice key id
                        if(SO_ORIGINAL_ID.equals(contextType)) {
                            originalId = context.getText();
                        }
                        
                        // original StarOffice white space formatting
                        if(SO_SUBFORMAT.equals(contextType)) {
                            formatting = context.getText();
                        }
                    }
                }
                
                if("".equals(originalId)) {
                    throw new RuntimeException("Translation unit does not contains original id");
                }
                
                writer.addTranslationUnit(originalId, source + formatting , target + formatting );
                
                
            }
            
        } catch(Throwable t) {
            
            System.out.println(t.getMessage());
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }
    
    /**
     * flush the content of the StarOffice xliff and close all resources
     *
     * @throws SOXliffBackException
     */
    public void flush() throws SOXliffBackException {
        writer.close();
    }
    
}

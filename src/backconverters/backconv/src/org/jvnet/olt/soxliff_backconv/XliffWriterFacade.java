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
 * XliffWriterFacade.java
 *
 * Created on April 24, 2006, 10:16 AM
 *
 */

package org.jvnet.olt.soxliff_backconv;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.dom.DOMDocumentType;
import org.dom4j.io.*;
import org.jvnet.olt.soxliff_backconv.util.*;
import org.jvnet.olt.io.*;

/**
 * Create StarOffice xliff file and writes translated segment
 *
 * @author michal
 */
public class XliffWriterFacade {
    
    private static final String ELEMENT_NAME = "xliff";
    private static final String PUBLIC_ID = "-//XLIFF//DTD XLIFF//EN";
    private static final String SYSTEM_ID = "http://www.oasis-open.org/committees/xliff/documents/xliff.dtd";
  
    // xlif tags definition
    private static final String XLIFF_TAG = "xliff";
    private static final String XLIFF_TAG_VERSION_ATT = "version";
    private static final String XLIFF_TAG_VERSION_VAL = "1.0";
    private static final String XLIFF_DTD = "xliff.dtd";
    
    private static final String XLIFF_FILE_TAG = "file";
    private static final String XLIFF_FILE_TAG_DATATYPE_ATT = "datatype";
    private static final String XLIFF_FILE_TAG_DATATYPE_VAL = "STAROFFICE";
    private static final String XLIFF_FILE_TAG_DATE_ATT = "date";
    private static final String XLIFF_FILE_TAG_ORIGINAL_ATT = "original";
    private static final String XLIFF_FILE_TAG_ORIGINAL_VAL = "from SunTrans translation database";
    private static final String XLIFF_FILE_TAG_SOURCELANG_ATT = "source-language";
    private static final String XLIFF_FILE_TAG_TARGETLANG_ATT = "target-language";
    private static final String XLIFF_HEADER = "header";
    private static final String XLIFF_BODY = "body";
    private static final String XLIFF_TRANSUNIT_TAG = "trans-unit";
    private static final String XLIFF_TRANSUNIT_ID_ATT = "id";
    private static final String XLIFF_SOURCE_TAG = "source";
    private static final String XLIFF_TARGET_TAG = "target";
    private static final String XLIFF_TARGET_LANG_ATT = "xml:lang";
    private static final String XLIFF_TARGET_STATE_ATT = "state";
    private static final String XLIFF_TARGET_STATE_VALUE = "non-translated:translated";
    
    private Map entityConversionMap = null;
    
    private String fileName = "";
    private String targetLanguage = "";
    
    private Document document = null;
    private Element    body        = null;
    
    private String lastId = "";
    private StringBuffer source = new StringBuffer("");
    private StringBuffer target = new StringBuffer("");
    
    /**
     * Do not allow to use constructor
     */
    private XliffWriterFacade(String filename,String sourceLanguage,String targetLanguage)  {
        
        this.fileName = filename;
        this.targetLanguage = targetLanguage;
        
        document = DocumentHelper.createDocument();
        document.setDocType(new DOMDocumentType(ELEMENT_NAME,PUBLIC_ID,SYSTEM_ID));
        
        // create header of the xliff file
        Element xliff = document.addElement(XLIFF_TAG);
        xliff.addAttribute(XLIFF_TAG_VERSION_ATT, XLIFF_TAG_VERSION_VAL);
        
        Element file = xliff.addElement(XLIFF_FILE_TAG);
        file.addAttribute(XLIFF_FILE_TAG_DATATYPE_ATT,XLIFF_FILE_TAG_DATATYPE_VAL);
        file.addAttribute(XLIFF_FILE_TAG_DATE_ATT, new java.util.Date().toString());
        file.addAttribute(XLIFF_FILE_TAG_ORIGINAL_ATT, XLIFF_FILE_TAG_ORIGINAL_VAL);
        file.addAttribute(XLIFF_FILE_TAG_SOURCELANG_ATT, sourceLanguage);
        file.addAttribute(XLIFF_FILE_TAG_TARGETLANG_ATT, targetLanguage);
        
        file.addElement(XLIFF_HEADER);
        body = file.addElement(XLIFF_BODY);
    }
    
    /**
     * Create new StarOffice xliff file
     *
     * @param filename of the output
     * @param sourceLanguage of the StarOffice xliff
     * @param targetLanguage of the StarOffice xliff
     */
    public static XliffWriterFacade createWriter(String filename,String sourceLanguage,String targetLanguage) {
        return new XliffWriterFacade(filename,sourceLanguage,targetLanguage);
    }
    
    /**
     * Add a segment to the StarOffice xliff
     *
     * @param id of the segment
     * @param source text of the segment
     * @param target text of the segment
     *
     * @throws ParseException
     */
    public void addTranslationUnit(String id,String source,String target) throws ParseException,IOException {
        // first segment
        if("".equals(lastId)) {
            lastId = id;
            this.source.append(source);
            this.target.append(target);
            return;
        }
        
        // sub segment
        if(lastId.equals(id)) {
            this.source.append(source);
            this.target.append(target);
            return;
        }
        
        // start of different segment flush the last one
        flushSegment();
        lastId = id;
        this.source = new StringBuffer(source);
        this.target = new StringBuffer(target);
    }
    
    /**
     * Add the segment to the StarOffice xliff document. Escape all XML tags
     *
     * @throws ParseException
     */
    private void flushSegment() throws ParseException,IOException {
        Element transUnit = body.addElement(XLIFF_TRANSUNIT_TAG);
        transUnit.addAttribute(XLIFF_TRANSUNIT_ID_ATT,lastId);
        
        Element src    = transUnit.addElement(XLIFF_SOURCE_TAG);
        src.setText(wrapXmlEntities(AddEscapeChars.convert(source.toString())));
        
        Element tgt    = transUnit.addElement(XLIFF_TARGET_TAG);
        tgt.addAttribute(XLIFF_TARGET_STATE_ATT,XLIFF_TARGET_STATE_VALUE);
        tgt.addAttribute(XLIFF_TARGET_LANG_ATT,targetLanguage);
        tgt.setText(wrapXmlEntities(AddEscapeChars.convert(target.toString())));
    }
    
    /**
     * Flush StarOffice xliff document and close all resources
     *
     * @throws SOXliffBackException
     */
    public void close() throws SOXliffBackException {
        try {
            flushSegment();
            OutputFormat outformat = OutputFormat.createPrettyPrint();
            outformat.setEncoding("UTF-8");
            
            XMLWriter writer = new XMLWriter(new FileOutputStream(new File(fileName)), outformat);
            writer.setEscapeText(false);
            writer.write(document);
            writer.flush();
        } catch(Throwable t) {
            throw new SOXliffBackException(t.getMessage(),t);
        }
    }
    
    /**
     * wrap characters used in XML with entities
     *
     * @param text that need to be converted
     *
     * @return wrapped text
     *
     * @throws IOException if the conversion fail
     */
    private String wrapXmlEntities(String text) throws IOException {
        
        // set up entity conversion map table
        if(entityConversionMap == null) {
            entityConversionMap = new HashMap();
            
            entityConversionMap.put(new Character('&'), "&amp;");
            entityConversionMap.put(new Character('<'), "&lt;");
            entityConversionMap.put(new Character('>'), "&gt;");
            entityConversionMap.put(new Character('"'),  "&quot;");
            entityConversionMap.put(new Character('\''),"&apos;");
            
        }
        
        // convert the input
        StringReader stringReader = new StringReader(text);
        EntityConversionFilterReader reader = new EntityConversionFilterReader(stringReader);
        reader.setEntityMap(entityConversionMap);
        
        BufferedReader buf = new BufferedReader(reader);
        StringWriter writer= new StringWriter();
        int i;
        while ((i = buf.read()) != -1){
            writer.write(i);
        }
        return writer.toString();
        
    }
    
}

/*
 * XliffWriterFacade.java
 *
 * Created on April 24, 2006, 10:16 AM
 *
 */

package org.jvnet.olt.filters.soxliff;

import filters.segmenter_formatters.src.org.jvnet.olt.filters.segmenters.formatters.XliffContextValue;
import java.io.*;
import java.util.*;
import org.jvnet.olt.filters.sgml.SgmlFilterHelper;
import org.jvnet.olt.utilities.*;
import org.jvnet.olt.filters.segmenters.formatters.*;
import org.jvnet.olt.format.xml.*;
import org.jvnet.olt.format.*;
import org.jvnet.olt.filters.soxliff.util.*;

/**
 * Facade that creates OLT xliff and writes translation units. Standard XliffSegmenterFormatter
 * is used.
 *
 * @author michal
 */
public class XliffWriterFacade {
    
    private String                        sourceLanguage = null;
    private File                           xlzFile = null;
    private XliffZipFileIO              xlzIO   = null;
    private FormatWrapper         wrapper = null;
    private SegmenterFormatter  formatter = null;
    
    private static final String XLIFF_TYPE = "STAROFFICE";
    
    /**
     * Do not allow to create an empty constructor
     */
    private XliffWriterFacade(File soXliff,String sourceLanguage) throws IOException,SegmenterFormatterException {
        xlzFile = new File(soXliff.getAbsolutePath()+".xlz");
        this.sourceLanguage = sourceLanguage;
        xlzIO = new XliffZipFileIO(xlzFile);
        try {
            wrapper = new FormatWrapperFactory().createFormatWrapper("XML", new EntityManager());
        } catch(UnsupportedFormatException e) {
            throw new SegmenterFormatterException("Cannot create formatWrapper: " + e.getMessage());
        }
        formatter = new XliffSegmenterFormatter(XLIFF_TYPE,  sourceLanguage, soXliff.getName(), xlzIO.getXliffWriter(), xlzIO.getSklWriter(), wrapper);
    }
    
    /**
     * Create new OLT xliff file
     *
     * @param soXliff file that should be created
     * @param sourceLanguage of the xliff file
     *
     * @throws IOException
     * @throws SegmenterFormatterException
     */
    public static XliffWriterFacade createWriter(File soXliff,String sourceLanguage) throws IOException,SegmenterFormatterException {
        return new XliffWriterFacade(soXliff,sourceLanguage);
    }
    
    /**
     * Writes single segment to the xliff file
     *
     * @param segment that should be written
     *
     * @throws SegmenterFormatterException
     */
    public void writeSegment(SOSegment segment) throws SegmenterFormatterException {
        
        String source = segment.getSource();
        int wordCount = SgmlFilterHelper.wordCount(source,sourceLanguage);
        String target  = segment.getTarget();
        
        // write context informations
        List contexts = segment.getContext();
        if(contexts.size()>0) {
            Iterator it = contexts.iterator();
            List contextToAdd = Collections.synchronizedList(new ArrayList());
            while(it.hasNext()) {
                String[] context = (String[])it.next();
                String type = context[0];
                String value = context[1];
                if(value==null) {
                    value = "";
                }
                
                XliffContextValue ctx = new XliffContextValue(type,value);
                contextToAdd.add(ctx);
            }
            ((XliffSegmenterFormatter)formatter).writeContext(contextToAdd);
        }
        
        if(target!=null && !"".equals(target)) {
            formatter.writeSegment(source,target,wordCount);
        } else {
            formatter.writeSegment(source,wordCount);
        }
        
    }
    
    /**
     * Flush content of the writer and close all resources
     *
     * @throws IOException
     * @throws SegmenterFormatterException
     */
    public void close() throws IOException,SegmenterFormatterException {
        formatter.flush();
        xlzIO.writeZipFile();
    }
    
}

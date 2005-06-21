
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * StarOfficeToXliff.java
 *
 * Created on June 16, 2004, 2:09 PM
 */

package org.jvnet.olt.filters.intstaroffice;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;
import java.net.*;
import java.util.zip.*;
import java.util.*;
import org.xml.sax.InputSource;

import org.jvnet.olt.filters.intstaroffice.xliff.*;
import org.jvnet.olt.filters.intstaroffice.xliff.XliffParser;
import org.jvnet.olt.filters.xliffsubsegment.*;


/**
 * Converts a paragraph segmented XLIFF file into a sentence-segmented XLIFF file
 * (we use this when processing output from the internal staroffice database.)
 * @author  timf
 */
public class StarOfficeInternalToXliff {
    
    /** Creates a new instance of StarOfficeInternalToXliff
     *
     * @param xliffFileString the absolute path to the file that is to be converted
     * @param srcLang the source language of that file
     * @param logger a logger we can use
     */
    public StarOfficeInternalToXliff(String xliffFileString, String srcLang, Logger logger) throws StarOfficeConverterException {
        try {
            File xliffFile = new File(xliffFileString);
            //XmlIdentifier id = new XmlIdentifier();
            //id.setSystemID("transit-something-to-xml-converter.dtd");
            
            XliffSubSegmenter segmenter = new XliffSubSegmenter(xliffFileString);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xliffFileString+".xlf")));
            segmenter.retrieveXliff(writer);
            writer.flush();
            writer.close();
            
        } catch (java.io.IOException e){
            StarOfficeConverterException ex = new StarOfficeConverterException("IO Exception thrown somewhere : "+e.getMessage());
            ex.initCause(e);
            throw ex;
        }  catch (XliffSubSegmenterException e){
            StarOfficeConverterException ex = new StarOfficeConverterException("Sub Segmenter Exception thrown somewhere : "+e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        
    }
    
    
    public static boolean isStarOfficeXLIFF(File file){
        boolean result = false;
        try {
            XliffHandler handler = new XliffHandlerImpl();
            XliffParser parser = new XliffParser(handler, new NullEntityResolver());
            
            parser.parse(new InputSource(new FileReader(file)));
            result = handler.isInternalStarOfficeXLIFF();
        } catch (java.io.IOException e){
            ;// System.out.println("IOException thrown trying to determine if "+file.getAbsolutePath()+
            // "is a StarOfficeXLIFF file :"+ e.getMessage());
            // System.out.println("returning false anyway");
        } catch (org.xml.sax.SAXException e){
            ;//System.out.println("SAXException thrown "+ e.getMessage());
            //System.out.println("returning false anyway");
        } catch (javax.xml.parsers.ParserConfigurationException e){
            ;//System.out.println("ParserConfigurationException thrown "+ e.getMessage());
            //System.out.println("returning false anyway");
        } catch (Throwable e){
            ; //e.printStackTrace();
        }
        return result;
    }
    
    
    
}

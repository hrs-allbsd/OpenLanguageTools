
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * PlaintextToXliff.java
 *
 * Created on September 16, 2002, 2:32 PM
 */

package org.jvnet.olt.filters.plaintext;

import org.jvnet.olt.filters.sgml.*;
import java.util.*;
import java.io.*;
import java.util.logging.*;
import org.jvnet.olt.utilities.*;

import org.jvnet.olt.format.sgml.EntityManager;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.*;
import org.jvnet.olt.format.plaintext.*;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.ReverseXliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

/**
 *
 * @author  timf
 */
public class PlaintextToXliff_Reverse {
    
    /** Creates a new instance of PlaintextToXliff */
    public PlaintextToXliff_Reverse(String directory, String filename, String language, String targLang, boolean writeReverse,  String encoding, Logger logger) throws PlaintextParserException, IOException{
        try {
            File file = new File(filename);
            String shortname = filename.substring(directory.length()+1,filename.length());
            FileInputStream inStream = new FileInputStream(file);
            Reader reader;
            try {
                reader = new InputStreamReader(inStream, encoding);
            } catch (java.io.UnsupportedEncodingException e){
                throw new PlaintextParserException("Problem trying to read plaintext file in encoding "+ encoding);
            }
            
            BufferedReader buf_txtReader = new BufferedReader(reader);
            
            FileOutputStream xliffStream = new FileOutputStream(filename+".xlf");
            OutputStreamWriter xliffWriter = new OutputStreamWriter(xliffStream, "UTF-8");
            BufferedWriter buf_xliff = new BufferedWriter(xliffWriter);
            
            
            FileOutputStream sklStream = new FileOutputStream(filename+".skl");
            OutputStreamWriter sklWriter = new OutputStreamWriter(sklStream,"UTF-8");
            BufferedWriter buf_skl = new BufferedWriter(sklWriter);
            
            BlockSegmenter_en blockParser = new  BlockSegmenter_en(buf_txtReader);
            blockParser.parse();
            GlobalVariableManager gvm = new EntityManager();
            FormatWrapper wrapper = new PlainTextFormatWrapper();
            
            SegmenterFormatter formatter = new ReverseXliffSegmenterFormatter("PLAINTEXT",
            language, targLang, shortname, buf_xliff , buf_skl, writeReverse,gvm, wrapper);
            
            XLIFFVisitor xliffVisitor = new XLIFFVisitor(formatter, language);
            blockParser.walkParseTree(xliffVisitor, null);
            formatter.flush();
            
            
             XliffZipFileIO xlz = new XliffZipFileIO(new File(filename+".xlz"));
            InputStreamReader sklreader = new InputStreamReader(new FileInputStream(filename+".skl"),"UTF-8");
            InputStreamReader xliffreader = new InputStreamReader(new FileInputStream(filename+".xlf"),"UTF-8");
            
            Writer sklwriter = xlz.getSklWriter();
            while (sklreader.ready()){
                sklwriter.write(sklreader.read());
            }
            sklwriter.flush();
            
            Writer xliffwriter = xlz.getXliffWriter();
            while (xliffreader.ready()){
                xliffwriter.write(xliffreader.read());
            }
            xliffwriter.flush();
            xliffwriter.close();
            xlz.writeZipFile();
            
            buf_xliff.close();
            buf_skl.close();
            xliffreader.close();
            sklreader.close();

            // now delete those temporary files :
            File xliff = new File(filename+".xlf");
            xliff.delete();
            File skeleton = new File(filename+".skl");
            skeleton.delete();
        } catch (Exception e){
            throw new PlaintextParserException(e.getMessage());
        }
        
        
    }
    
}

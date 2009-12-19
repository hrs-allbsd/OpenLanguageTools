
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

import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

import org.jvnet.olt.format.sgml.EntityManager;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.io.EntityConversionFilterReader;
import org.jvnet.olt.format.*;
import org.jvnet.olt.format.plaintext.*;

import java.util.*;
import java.io.*;
import java.util.logging.*;
import org.jvnet.olt.utilities.*;

/**
 * This plaintext stuff isn't nearly as nice as the corresponding html and sgml
 * filters - it was kinda done in a hurry and could do with some refactoring
 * @author  timf
 */
public class PlaintextToXliff {
    /** Creates a new instance of PlaintextToXliff */
    public PlaintextToXliff(String directory, String filename,
    String language, String encoding,
    Logger logger) throws PlaintextParserException, IOException{
           this(directory, filename, language, encoding, logger, "");
       
        
        
    }
    
    public PlaintextToXliff(String directory, String filename,
    String language, String encoding,
    Logger logger , String guid) throws PlaintextParserException, IOException{
        
        try {
            File file = new File(filename);
            String shortname = filename.substring(directory.length(),filename.length());
            FileInputStream inStream = new FileInputStream(file);
            Reader reader;
            try {
                reader = new InputStreamReader(inStream, encoding);
            } catch (java.io.UnsupportedEncodingException e){
                throw new PlaintextParserException("Problem trying to read plaintext file in encoding "+ encoding);
            }
            
            BufferedReader buf_txtReader = new BufferedReader(new EntityConversionFilterReader(reader));
            
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
            
            SegmenterFormatter formatter = new XliffSegmenterFormatter("PLAINTEXT",
            language,shortname, buf_xliff , buf_skl, gvm, wrapper);
            
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
    
    public static void main(String[] argv) {
        try {
            
            if (argv.length != 2){
                System.out.println("Usage : PlaintextToXliff <input file> <language>\n"+
                "where language is currently en, de, es, fr, it, sv\n"+
                " [note: all languages have asian character support.]");
                System.exit(0);
            }
            //String encoding = System.getProperty("file.encoding");
            //System.out.println ("creating reader in " + encoding +" encoding.");
            //  Open the input file.
            Logger logger = Logger.getLogger("com.sun.tt.filters.testlogger");
            PlaintextToXliff task = new PlaintextToXliff("", argv[0], argv[1], System.getProperty("file.encoding"), logger);
        } catch (Exception e){
            System.err.println(e.getMessage());
            
        }
    }
}

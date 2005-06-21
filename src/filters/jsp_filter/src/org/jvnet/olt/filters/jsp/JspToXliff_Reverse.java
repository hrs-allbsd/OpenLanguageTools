
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */


package org.jvnet.olt.filters.jsp;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;
import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.utilities.*;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.format.sgml.*;
import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.html.*;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.logging.*;
/**
 *
 * @author  timf
 */
public class JspToXliff_Reverse {
    
    /**
     *
     *
     */
    public JspToXliff_Reverse(String directory, String filename, String language, String targLang , boolean writeReverse, String encoding, Logger logger) throws JspParserException, IOException{
        try {
            // check to see that javascript has been commented correctly
            ReplaceScriptTag.fix(filename, encoding);
 
            File file = new File(filename);
            String shortname = filename.substring(directory.length(),filename.length());
            FileInputStream inStream = new FileInputStream(file);
            Reader reader;
            try {
                reader = new InputStreamReader(inStream, encoding);
            } catch (java.io.UnsupportedEncodingException e){
                throw new JspParserException("Problem trying to read jsp file in encoding "+ encoding);
            }
            
            //  Parse the input file to get xliff files array
            EntityManager gvm = new EntityManager();            
            SgmlFilter parser = new SgmlFilter(reader, gvm);
            
            FileOutputStream xliffOutputStream = new FileOutputStream(filename+".xlf");
            FileOutputStream sklOutputStream = new FileOutputStream(filename+".skl");
                        
            Writer xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            Writer sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            org.jvnet.olt.parsers.tagged.TagTable jspTagTable = new JspTagTable();
            SegmenterTable jspSegmenterTable = new JspSegmenterTable();
            
            parser.parseJspForReverse("JSP", language, targLang, shortname, xliffWriter, sklWriter,
            jspTagTable, jspSegmenterTable, writeReverse );                        
            
            XliffZipFileIO xlz = new XliffZipFileIO(new File(filename+".xlz"));
            InputStreamReader sklreader = new InputStreamReader(new FileInputStream(filename+".skl"),"UTF-8");
            InputStreamReader xliffreader = new InputStreamReader(new FileInputStream(filename+".xlf"),"UTF-8");
            
            Writer sklwriter = xlz.getSklWriter();
            int c;
            while ((c=sklreader.read()) != -1){
                sklwriter.write(c);
            }
                       
            
            Writer xliffwriter = xlz.getXliffWriter();
            while ((c=xliffreader.read()) != -1){
                xliffwriter.write(c);
            }
            xlz.writeZipFile();
            
            // now delete those temporary files :
            File xliff = new File(filename+".xlf");
            xliff.delete();
            File skeleton = new File(filename+".skl");
            skeleton.delete();
            
            
        } // very bad exception handling here, but it's only sample code
        catch (JspParserException e){
            System.err.println("Caught a jspParserException " + e);
            throw e;
        }
        catch (java.io.IOException e){
            System.err.println("Caught an io exception " + e);
            throw e;
        }
        
    }
    
    
    /** This class takes in a language and a jsp file, and outputs a .zip archive containing
     *  an xliff file and it's associated skeleton file.
     *
     * @param argv  Two arguments needed in here : the input file and language name
     */
    public static void main(String[] argv) {
        try {
            
            if (argv.length != 3){
                System.out.println("Usage : JspToXliff_Reverse <input file> <src lang> <targ language>\n"+
                "where languages are RFC3066 strings\n"+
                " [note: all languages have asian character support.]");
                System.exit(0);
            }
            String encoding = System.getProperty("file.encoding");
            //System.out.println ("creating reader in " + encoding +" encoding.");
            //  Open the input file.
            Logger logger = Logger.getLogger("com.sun.tt.filters.testlogger");
            JspToXliff_Reverse task = new JspToXliff_Reverse("", argv[0], argv[1], argv[2],  false, System.getProperty("file.encoding"), logger);
        } catch (Exception e){
            System.err.println(e.getMessage());
            
        }
    }
    
    
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */


package org.jvnet.olt.filters.html;
import org.jvnet.olt.format.sgml.*;
import org.jvnet.olt.format.*;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.NonConformantToTaggedMarkupConverter;
import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

import org.jvnet.olt.utilities.*;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.logging.*;
/**
 *
 * @author  timf
 */
public class HtmlToXliff {
    
    private String guid = null;
    
    /**
     * This code converts from html to xliff - it's really a static frontend
     * to the htmlparser and hides the actual writing of xliff and skl files
     * from the user
     */
    public HtmlToXliff(String directory, String filename,
    String language, String encoding,
    Logger logger ) throws HtmlParserException, IOException{
        this.guid = null;
        this.convert(directory,filename,language,encoding,logger);
    }

    
    public void convert(String directory, String filename,
    String language, String encoding,
    Logger logger ) throws HtmlParserException, IOException{
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
                throw new HtmlParserException("Problem trying to read html file in encoding "+ encoding);
            }
            
            //  Parse the input file to get xliff files array
            EntityManager gvm = new EntityManager();            
            
            FileOutputStream xliffOutputStream = new FileOutputStream(filename+".xlf");
            FileOutputStream sklOutputStream = new FileOutputStream(filename+".skl");
            
            Writer xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            Writer sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            org.jvnet.olt.parsers.tagged.TagTable tagTable = new HtmlTagTable();
            SegmenterTable segmenterTable = new HtmlSegmenterTable();
            //boolean treatAsSingleSegment = false;
            
            //parser.parseSgmlForXliff("HTML",language, shortname, xliffWriter, sklWriter, htmlTagTable, htmlSegmenterTable, treatAsSingleSegment);
            NonConformantSgmlDocFragmentParser parser = new  NonConformantSgmlDocFragmentParser(reader);
            try {
                parser.parse();
                
                FormatWrapper wrapper = new SgmlFormatWrapper(gvm, tagTable, segmenterTable);
                XliffSegmenterFormatter formatter = new XliffSegmenterFormatter("HTML", language, shortname, xliffWriter, sklWriter, gvm, wrapper);
                
                //System.out.println("setting "+table);
                //System.out.println("setting "+segmenterTable);
                formatter.setTagTable(tagTable);
                formatter.setSegmenterTable(segmenterTable);
                SgmlSegmenterVisitor sgmlSegmenterVisitor =
                new SgmlSegmenterVisitor(formatter, segmenterTable, tagTable, language, gvm, new NonConformantToTaggedMarkupConverter());
                // tell the visitor that we're parsing a file containing only the
                // text of a programlisting or other such tag.
                boolean treatFileAsSingleSegment = false;
                sgmlSegmenterVisitor.setTreatFileAsSingleSegment(treatFileAsSingleSegment);
                parser.walkParseTree(sgmlSegmenterVisitor, null);
            
            } catch (Throwable t){ // bad exception handling here, but it's just sample code
		logger.log(Level.SEVERE,t.getMessage(),t);
                throw new SgmlFilterException("Problem parsing html file "+filename+" "+t.getMessage());
            }
            
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
            
            if(guid != null) {
            	java.util.Properties prop = new java.util.Properties();
            	prop.setProperty("guid",guid);
            	xlz.setWorkflowProperties(prop);
            }
            
            xlz.writeZipFile();
            
            xliffWriter.close();
            sklWriter.close();
            xliffreader.close();
            sklreader.close();

            // now delete those temporary files :
            File xliff = new File(filename+".xlf");
            xliff.delete();
            File skeleton = new File(filename+".skl");
            skeleton.delete();
            
                       
        } // very bad exception handling here, but it's only sample code
        catch (HtmlParserException e){
            System.err.println("Caught a htmlParserException " + e);
            throw e;
        }
        catch (java.io.IOException e){
            System.err.println("Caught an io exception " + e);
            throw e;
        }
        
    }
    
    
    /**
     * A separate constructor used to set the guid
     */
    public HtmlToXliff(String directory, String filename,
    String language, String encoding,
    Logger logger,String guid ) throws HtmlParserException, IOException{
        this.guid = guid;
        this.convert(directory,filename,language,encoding,logger);
    }
    	
    
    
    /** This class takes in a language and a html file, and outputs a .zip archive containing
     *  an xliff file and it's associated skeleton file.
     *
     * @param argv  Two arguments needed in here : the input file and language name
     */
    public static void main(String[] argv) {
        try {
            
            if (argv.length != 2){
                System.out.println("Usage : HtmlToXliff <input file> <language>\n"+
                "where language is currently en, de, es, fr, it, sv\n"+
                " [note: all languages have asian character support.]");
                System.exit(0);
            }
            //String encoding = System.getProperty("file.encoding");
            //System.out.println ("creating reader in " + encoding +" encoding.");
            //  Open the input file.
            Logger logger = Logger.getLogger("com.sun.tt.filters.testlogger");
            HtmlToXliff task = new HtmlToXliff("", argv[0], argv[1], System.getProperty("file.encoding"), logger);
        } catch (Exception e){
            System.err.println(e.getMessage());
            
        }
    }
    
    
}

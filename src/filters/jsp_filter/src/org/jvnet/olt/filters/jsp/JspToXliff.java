
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */


package org.jvnet.olt.filters.jsp;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.brokensgml.*;
import org.jvnet.olt.format.brokensgml.BrokenSgmlFormatWrapper;
import org.jvnet.olt.format.sgml.EntityManager;
import org.jvnet.olt.parsers.tagged.NonConformantToTaggedMarkupConverter;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.filters.sgml.SgmlSegmenterVisitor;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.filters.html.ReplaceScriptTag;
import org.jvnet.olt.utilities.*;

import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.logging.*;
/**
 *
 * @author  timf
 */
public class JspToXliff {
    
    private String guid = null;
    
    /**
     * This code converts from jsp to xliff - it's really a static frontend
     * to the jspparser and hides the actual writing of xliff and skl files
     * from the user
     */
    public JspToXliff(String directory, String filename,
    String language, String encoding,
    Logger logger ) throws JspParserException, IOException{
        this.guid = null;
        this.convert(directory,filename,language,encoding,logger);
    }

    
    public void convert(String directory, String filename,
    String language, String encoding,
    Logger logger ) throws JspParserException, IOException{
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
            
            FileOutputStream xliffOutputStream = new FileOutputStream(filename+".xlf");
            FileOutputStream sklOutputStream = new FileOutputStream(filename+".skl");
            
            Writer xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            Writer sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            org.jvnet.olt.parsers.tagged.TagTable jspTagTable = new JspTagTable();
            SegmenterTable jspSegmenterTable = new JspSegmenterTable();
            
            parseJspForXliff(reader, "JSP",language, shortname, xliffWriter, sklWriter, jspTagTable, jspSegmenterTable, gvm);
            
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
        catch (JspParserException e){
            System.err.println("Caught a jspParserException " + e);
            throw e;
        }
        catch (java.io.IOException e){
            System.err.println("Caught an io exception " + e);
            throw e;
        }
        
    }
    
    
    private void parseJspForXliff(Reader reader, String dataType, String language, String sourceFileName, Writer xliffWriter, Writer sklWriter, org.jvnet.olt.parsers.tagged.TagTable table, org.jvnet.olt.parsers.tagged.SegmenterTable segmenterTable, GlobalVariableManager gvm) throws JspParserException {        
        try {
            //  Parse the input file.
            NonConformantSgmlDocFragmentParser parser = new  NonConformantSgmlDocFragmentParser(reader);
            parser.parse();
            FormatWrapper wrapper = new BrokenSgmlFormatWrapper(table, segmenterTable);
            XliffSegmenterFormatter formatter = new XliffSegmenterFormatter(dataType, language, sourceFileName, xliffWriter, sklWriter, gvm, wrapper);
            formatter.setTagTable(table);
            formatter.setSegmenterTable(segmenterTable);
            SgmlSegmenterVisitor sgmlSegmenterVisitor = 
            new SgmlSegmenterVisitor(formatter, segmenterTable, table, language, gvm, new NonConformantToTaggedMarkupConverter());
            parser.walkParseTree(sgmlSegmenterVisitor, null);
            
        } catch (java.io.IOException e){
            throw new JspParserException(e.getMessage());
        } catch (org.jvnet.olt.parsers.SgmlDocFragmentParser.ParseException e){
            throw new JspParserException(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            throw new JspParserException(e.getMessage());
        }
    }
    
    
    /**
     * A separate constructor used to set the guid
     */
    public JspToXliff(String directory, String filename,
    String language, String encoding,
    Logger logger,String guid ) throws JspParserException, IOException{
        this.guid = guid;
        this.convert(directory,filename,language,encoding,logger);
    }
    	
    
    
    /** This class takes in a language and a jsp file, and outputs a .zip archive containing
     *  an xliff file and it's associated skeleton file.
     *
     * @param argv  Two arguments needed in here : the input file and language name
     */
    public static void main(String[] argv) {
        try {
            
            if (argv.length != 2){
                System.out.println("Usage : JspToXliff <input file> <language>\n"+
                "where language is an RFC3066 locale identifier, eg. en-US, de-DE, etc.\n"+
                " [note: all languages have asian character support.]");
                System.exit(0);
            }
            //String encoding = System.getProperty("file.encoding");
            //System.out.println ("creating reader in " + encoding +" encoding.");
            //  Open the input file.
            Logger logger = Logger.getLogger("com.sun.tt.filters.testlogger");
            JspToXliff task = new JspToXliff("", argv[0], argv[1], System.getProperty("file.encoding"), logger);
        } catch (Exception e){
            System.err.println(e.getMessage());            
        }
    }
    
    
}

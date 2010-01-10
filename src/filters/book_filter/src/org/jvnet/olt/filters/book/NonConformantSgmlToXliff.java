
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * NonConformantSgmlToXliff.java
 *
 * Created on May 19, 2003, 3:30 PM
 */

package org.jvnet.olt.filters.book;
import org.jvnet.olt.filters.sgml.SgmlSegmenterVisitor;
import org.jvnet.olt.filters.sgml.docbook.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.utilities.*;
import java.util.logging.Logger;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.NonConformantToTaggedMarkupConverter;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.sgml.SgmlFormatWrapper;

import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;
import java.io.*;

/**
 * This class calls the non-conformant doc fragment parser to try to parse the
 * incoming sgml file - it's very similar to the SgmlToXliff code - we just use
 * a different parser and TaggedMarkupConverter to call the same underlying
 * functions of the sgml filter (specifically, we didn't want to have to rewrite
 * the SgmlSegmenterFormatter to deal with every little tagged-markup-type language,
 * so we adapted it to take TaggedMarkupNodes instead.
 * @author  timf
 */
public class NonConformantSgmlToXliff {
    
    /** Creates a new instance of SgmlToXliff */
    public NonConformantSgmlToXliff(String sgmlfile, String shortname, GlobalVariableManager gvm, String srclang, String encoding, Logger logger, String guid, boolean treatFileAsSingleSegment) throws BookFilterException {
        try {
            // now convert the sgml file to xliff
            InputStreamReader reader = new InputStreamReader(new FileInputStream(sgmlfile),encoding);
            BufferedReader reader_b = new BufferedReader(reader);
            
            OutputStream xliffOutputStream = new FileOutputStream(sgmlfile+".xlf");
            OutputStream sklOutputStream = new FileOutputStream(sgmlfile+".skl");
            
            Writer xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            Writer sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            BufferedWriter xliffWriter_b = new BufferedWriter(xliffWriter);
            BufferedWriter sklWriter_b = new BufferedWriter(sklWriter);
            //filter.parseDocBookForXliff(srclang,sgmlfile,sgmlfile+".xlf",sgmlfile+".skl",xliffWriter,sklWriter);
            TagTable tagTable = new DocbookTagTable();
            SegmenterTable segmenterTable = new DocbookSegmenterTable();
            
            //filter.parseJspForXliff("SGML", srclang,shortname,xliffWriter_b,sklWriter_b, table, segmenterTable);
            NonConformantSgmlDocFragmentParser parser = new  NonConformantSgmlDocFragmentParser(reader_b);
            try {
                parser.parse();
                
                FormatWrapper wrapper = new SgmlFormatWrapper(gvm, tagTable, segmenterTable);
                XliffSegmenterFormatter formatter = new XliffSegmenterFormatter("SGML", srclang, shortname, xliffWriter_b, sklWriter_b, gvm, wrapper);
                
                //System.out.println("setting "+table);
                //System.out.println("setting "+segmenterTable);
                formatter.setTagTable(tagTable);
                formatter.setSegmenterTable(segmenterTable);
                SgmlSegmenterVisitor sgmlSegmenterVisitor =
                new SgmlSegmenterVisitor(formatter, segmenterTable, tagTable, srclang, gvm, new NonConformantToTaggedMarkupConverter());
                // tell the visitor that we're parsing a file containing only the
                // text of a programlisting or other such tag.
                sgmlSegmenterVisitor.setTreatFileAsSingleSegment(treatFileAsSingleSegment);
                parser.walkParseTree(sgmlSegmenterVisitor, null);
            
            } catch (Throwable t){ // bad exception handling here, but it's just sample code
                throw new BookFilterException("Problem parsing book file "+shortname+" "+t.getMessage());
            }

            
            // Write the sgml .xlz file ------------
            
            XliffZipFileIO xlz = new XliffZipFileIO(new File(sgmlfile+".xlz"));
            Reader sklreader = new BufferedReader(new InputStreamReader(new FileInputStream(sgmlfile+".skl"),"UTF-8"));
            Reader xliffreader =  new BufferedReader(new InputStreamReader(new FileInputStream(sgmlfile+".xlf"),"UTF-8"));
            
            Writer sklwriter = xlz.getSklWriter();
            int c;
            while ((c=sklreader.read()) != -1){
                sklwriter.write(c);
            }
            
            
            Writer xliffwriter = xlz.getXliffWriter();
            while ((c=xliffreader.read()) != -1){
                xliffwriter.write(c);
            }
            if (guid != null){
                java.util.Properties prop = new java.util.Properties();
                prop.setProperty("guid",guid);
                xlz.setWorkflowProperties(prop);
            }
            xlz.writeZipFile();
            
            xliffWriter_b.close();
            sklWriter_b.close();
            xliffreader.close();
            sklreader.close();

            // now delete those temporary files :
            File xliff = new File(sgmlfile+".xlf");
            xliff.delete();
            File skeleton = new File(sgmlfile+".skl");
            skeleton.delete();
            
        } catch (Exception e){
            throw new BookFilterException(e.getMessage());
        }
        
    }
    
    public static void main(String[] args){
        if (args.length != 4){
            usage();
            System.exit(1);
        }
        //String filename = args[0];
        //String bookfile = args[1];
        //String srclang = args[2];
        //String targlang = args[3];
        
        //NonConformantSgmlToXliff converter = new NonConformantSgmlToXliff(filename, bookfile, srclang, targlang);
        
    }
    
    public static void usage(){
        System.out.println("Usage: NonConformantSgmlToXliff <sgmlfile> <bookfile> <src lang> <targlang>");
    }
    
    
    
}

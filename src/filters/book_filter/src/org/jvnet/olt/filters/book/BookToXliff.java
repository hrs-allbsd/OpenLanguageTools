
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BookToXliff.java
 *
 * Created on July 14, 2003, 2:41 PM
 */

package org.jvnet.olt.filters.book;
import java.io.*;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser;
import org.jvnet.olt.filters.sgml.docbook.*;
import org.jvnet.olt.filters.sgml.SgmlSegmenterVisitor;
import org.jvnet.olt.utilities.*;
import java.util.logging.Logger;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.sgml.SgmlFormatWrapper;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.SgmlToTaggedMarkupConverter;

import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;
/**
 *
 * @author  timf
 */
public class BookToXliff {
    
    /** Creates a new instance of BookToXliff - the gvm here can
      be null, if it is, we'll create a new one from this book file
      shortname is the name you want to appear at the top of the xliff file
     * (and subsequently, the passport)
     */
    public BookToXliff(String bookfile, String shortname, GlobalVariableManager gvm, String srclang,
    String encoding, Logger logger, String guid) throws BookFilterException {
        try {
            File file = new File(bookfile);
            String dir = file.getParent();
            BufferedReader bookReader = new BufferedReader(new InputStreamReader
            (new FileInputStream(bookfile),encoding));
            if (gvm == null){
                // rebuild the GVM from scratch - this may not work
                Book book = new Book(bookReader,dir, null,false);
                gvm = book.getGlobalVariableManager();
                // finished with that inputstream - open a new one.
                bookReader.close();
                bookReader = new BufferedReader(new InputStreamReader
                (new FileInputStream(bookfile),encoding));
            }
            // System.out.println("Gvm is " + gvm);
            // now let's convert the book file to xliff
            FileOutputStream xliffOutputStream = new FileOutputStream(bookfile+".xlf");
            FileOutputStream sklOutputStream = new FileOutputStream(bookfile+".skl");
            
            Writer xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            Writer sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            BufferedWriter xliffWriter_b = new BufferedWriter(xliffWriter);
            BufferedWriter sklWriter_b = new BufferedWriter(sklWriter);
            
            //org.jvnet.olt.parsers.tagged.TagTable table = new BookFileTagTable();
            //SegmenterTable segmenterTable = new BookFileSegmenterTable();
            org.jvnet.olt.parsers.tagged.TagTable tagTable = new DocbookTagTable();
            SegmenterTable segmenterTable = new DocbookSegmenterTable();
            
            //SgmlFilter filter = new SgmlFilter(bookReader,gvm);
            boolean treatAsSingleSegment = false;
            // essentially, a .book file is a valid sgml file, so we use the same header val
            SgmlDocFragmentParser parser = new  SgmlDocFragmentParser(bookReader);
            try {
                parser.parse();
                
                FormatWrapper wrapper = new SgmlFormatWrapper(gvm, tagTable, segmenterTable);
                XliffSegmenterFormatter formatter = new XliffSegmenterFormatter("SGML", srclang, shortname, xliffWriter, sklWriter, gvm, wrapper);
                
                //System.out.println("setting "+table);
                //System.out.println("setting "+segmenterTable);
                formatter.setTagTable(tagTable);
                formatter.setSegmenterTable(segmenterTable);
                SgmlSegmenterVisitor sgmlSegmenterVisitor =
                new SgmlSegmenterVisitor(formatter, segmenterTable, tagTable, srclang, gvm, new SgmlToTaggedMarkupConverter());
                // tell the visitor that we're parsing a file containing only the
                // text of a programlisting or other such tag.
                boolean treatFileAsSingleSegment = false;
                sgmlSegmenterVisitor.setTreatFileAsSingleSegment(treatFileAsSingleSegment);
                parser.walkParseTree(sgmlSegmenterVisitor, null);
            
            } catch (Throwable t){ // bad exception handling here, but it's just sample code
                throw new BookFilterException("Problem parsing book file "+shortname+" "+t.getMessage());
            }
            
            //filter.parseSgmlForXliff("SGML",srclang,shortname ,xliffWriter_b,sklWriter_b, table, segmenterTable, treatAsSingleSegment);
            // Write the book .xlz file ------------
            XliffZipFileIO xlz = new XliffZipFileIO(new File(bookfile+".xlz"));
            InputStreamReader sklreader = new InputStreamReader(new FileInputStream(bookfile+".skl"),"UTF-8");
            InputStreamReader xliffreader = new InputStreamReader(new FileInputStream(bookfile+".xlf"),"UTF-8");
            
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
            
            // now delete those temporary files :
            File xliff = new File(bookfile+".xlf");
            xliff.delete();
            File skeleton = new File(bookfile+".skl");
            skeleton.delete();
        } catch (Throwable  t){
            t.printStackTrace();
            throw new BookFilterException(t.getMessage());
        }
        
        
    }
    
    public static void main(String[] args){
        if (args.length != 1){
            usage();
            System.exit(1);
        }
        
        String filename = args[0];
        try {
        BookToXliff converter = new BookToXliff(filename, filename, null, "en-US", "ISO8859-1", Logger.global,"");
        } catch (BookFilterException e ){
            e.printStackTrace();
            System.out.println("Problem with file "+e.getMessage());
        }
        
    }
    
    public static void usage(){
        System.out.println("Usage: BookToXliff <bookfile>");
    }
}

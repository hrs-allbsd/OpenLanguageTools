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
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SgmlToXliff.java
 *
 * Created on May 19, 2003, 3:30 PM
 */

package org.jvnet.olt.filters.book;
import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.sgml.docbook.*;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser;
import org.jvnet.olt.utilities.*;
import java.util.logging.Logger;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.SgmlToTaggedMarkupConverter;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.sgml.EntityManager;
import org.jvnet.olt.format.sgml.SgmlFormatWrapper;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;
import java.io.*;

/**
 *
 * @author  timf
 */
public class SgmlToXliff {
    
    /** Creates a new instance of SgmlToXliff 
     *
     * For most cases, this throws an SgmlFilterException, except when we get
     */
    public SgmlToXliff(String sgmlfile, String shortname, GlobalVariableManager gvm, String srclang,
    String encoding, Logger logger, String guid, boolean treatFileAsSingleSegment) throws SgmlFilterException, SgmlParseException {
        try {
            GlobalVariableManager localGvm = null;
            if (gvm == null){
                localGvm = new EntityManager();
            } else {
                localGvm = gvm;
            }
            
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
            //filter.parseSgmlForXliff("SGML",srclang,shortname,xliffWriter_b,sklWriter_b, table, segmenterTable, treatFileAsSingleSegment);
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader_b);
            try {
                parser.parse();
                
                FormatWrapper wrapper = new SgmlFormatWrapper(localGvm, tagTable, segmenterTable);
                XliffSegmenterFormatter formatter = new XliffSegmenterFormatter("SGML", srclang, shortname, xliffWriter_b, sklWriter_b, localGvm, wrapper);
                
                //System.out.println("setting "+table);
                //System.out.println("setting "+segmenterTable);
                formatter.setTagTable(tagTable);
                formatter.setSegmenterTable(segmenterTable);
                SgmlSegmenterVisitor sgmlSegmenterVisitor =
                new SgmlSegmenterVisitor(formatter, segmenterTable, tagTable, srclang, localGvm, new SgmlToTaggedMarkupConverter());
                
                sgmlSegmenterVisitor.setTreatFileAsSingleSegment(treatFileAsSingleSegment);
                parser.walkParseTree(sgmlSegmenterVisitor, null);
            
            } catch (Throwable t){ // bad exception handling here, we need Throwable since the
                // parsers can throw Errors instead of exceptions. In these cases, we throw a
                // special exception that should pass up the tree and not get wrapped in a
                // BookFileException
                throw new SgmlParseException("Problem parsing sgml file "+shortname+" "+t.getMessage());
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
            
        } catch (SgmlParseException e){
            // want this one to get passed back up the exception chain
            throw e;
        } catch (Exception e){ // hmm, not good, should be more specific
            throw new SgmlFilterException(e.getMessage());
        }
        
    }
    
    public static void main(String[] args){
        if (args.length != 4){
            usage();
            System.exit(1);
        }
        String filename = args[0];
        String bookfile = args[1];
        String srclang = args[2];
        String targlang = args[3];
        
        SgmlToXliff converter = new SgmlToXliff(filename, bookfile, srclang, targlang);
        
    }
    
    public static void usage(){
        System.out.println("Usage: SgmlToXliff <sgmlfile> <bookfile> <src lang> <targlang>");
    }
    
    /**
     * This constructor is *only* to be used for testing on the command line - it's not
     * the main sgml filter converter - it's proctected, and so, can only be used by
     * stuff in this package !
     */
    protected SgmlToXliff(String sgmlfile, String bookfile, String srclang, String targlang) {
        try {
            System.out.println("Parsing book file...");
            File file = new File(bookfile);
            String dir = file.getParent();
            BufferedReader bookReader = new BufferedReader(new InputStreamReader
            (new FileInputStream(bookfile),"ISO8859-1"));
            
            Book book = new Book(bookReader,dir, null,false);
            GlobalVariableManager gvm = book.getGlobalVariableManager();
            // finished with that inputstream - open a new one.
            bookReader.close();
            BookToXliff bookConverter = new BookToXliff(bookfile,bookfile, gvm, srclang, "ISO8859-1", Logger.global, "");
            SgmlToXliff x = new SgmlToXliff(sgmlfile,sgmlfile, gvm,"en-US","ISO8859-1", Logger.global,"", false);
            
        } catch (Throwable t){
            t.printStackTrace();
        }
        
    }
    
}

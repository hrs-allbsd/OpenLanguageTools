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

import org.jvnet.olt.utilities.*;
import java.util.logging.Logger;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import java.io.*;

/**
 *
 * @author  timf
 */
public class Reverse_SgmlToXliff {
    
    /** Creates a new instance of SgmlToXliff */
    public Reverse_SgmlToXliff(String sgmlfile, String shortname, GlobalVariableManager gvm, String srclang, String targlang, boolean writeReverse, String encoding, Logger logger, String guid, boolean treatFileAsSingleSegment) throws SgmlFilterException {
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
            TagTable table = new DocbookTagTable();
            SgmlFilter filter = new SgmlFilter(reader_b,gvm);
            SegmenterTable segmenterTable = new DocbookSegmenterTable();
            filter.parseForReverse("SGML",srclang, targlang, shortname,xliffWriter_b,sklWriter_b, table, segmenterTable, treatFileAsSingleSegment);
            
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
    protected Reverse_SgmlToXliff(String sgmlfile, String bookfile, String srclang, String targlang) {
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
            
            bookReader = new BufferedReader(new InputStreamReader
            (new FileInputStream(bookfile),"ISO8859-1"));
            // now let's convert the book file to xliff
            FileOutputStream xliffOutputStream = new FileOutputStream(bookfile+".xlf");
            FileOutputStream sklOutputStream = new FileOutputStream(bookfile+".skl");
            
            Writer xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            Writer sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            BufferedWriter xliffWriter_b = new BufferedWriter(xliffWriter);
            BufferedWriter sklWriter_b = new BufferedWriter(sklWriter);
            
            org.jvnet.olt.parsers.tagged.TagTable table = new BookFileTagTable();
            SegmenterTable segmenterTable = new BookFileSegmenterTable();
            SgmlFilter filter = new SgmlFilter(bookReader,gvm);
            
            filter.parseForReverse("SGML",srclang,targlang,bookfile,xliffWriter_b,sklWriter_b, table, segmenterTable, false);
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
            xlz.writeZipFile();
            
            xliffWriter_b.close();
            sklWriter_b.close();
            xliffreader.close();
            sklreader.close();

            // now delete those temporary files :
            File xliff = new File(bookfile+".xlf");
            xliff.delete();
            File skeleton = new File(bookfile+".skl");
            skeleton.delete();
            
            
            
            // now convert the sgml file to xliff
            // note ! assuming UTF-8 : will fix in production...
            InputStreamReader reader = new InputStreamReader(new FileInputStream(sgmlfile),"UTF-8");
            BufferedReader reader_b = new BufferedReader(reader);
            
            
            xliffOutputStream = new FileOutputStream(sgmlfile+".xlf");
            sklOutputStream = new FileOutputStream(sgmlfile+".skl");
            
            xliffWriter =new OutputStreamWriter(xliffOutputStream, "UTF-8");
            sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            xliffWriter_b = new BufferedWriter(xliffWriter);
            sklWriter_b = new BufferedWriter(sklWriter);
            System.out.println("Converting sgml file to xliff for reverse...");
            //filter.parseDocBookForXliff(srclang,sgmlfile,sgmlfile+".xlf",sgmlfile+".skl",xliffWriter,sklWriter);
            table = new DocbookTagTable();
            filter = new SgmlFilter(reader_b,gvm);
            segmenterTable = new DocbookSegmenterTable();
            filter.parseForReverse("SGML",srclang,targlang,sgmlfile,xliffWriter_b,sklWriter_b, table, segmenterTable, false);
            // Write the sgml .xlz file ------------
            
            
            xlz = new XliffZipFileIO(new File(sgmlfile+".xlz"));
            sklreader = new InputStreamReader(new FileInputStream(sgmlfile+".skl"),"UTF-8");
            xliffreader = new InputStreamReader(new FileInputStream(sgmlfile+".xlf"),"UTF-8");
            
            sklwriter = xlz.getSklWriter();
            
            while ((c=sklreader.read()) != -1){
                sklwriter.write(c);
            }
            
            
            xliffwriter = xlz.getXliffWriter();
            while ((c=xliffreader.read()) != -1){
                xliffwriter.write(c);
            }
            xlz.writeZipFile();
            
            xliffWriter_b.close();
            sklWriter_b.close();
            xliffreader.close();
            sklreader.close();

            // now delete those temporary files :
            xliff = new File(sgmlfile+".xlf");
            xliff.delete();
            skeleton = new File(sgmlfile+".skl");
            skeleton.delete();
            
        } catch (Throwable t){
            t.printStackTrace();
        }
        
    }
    
}

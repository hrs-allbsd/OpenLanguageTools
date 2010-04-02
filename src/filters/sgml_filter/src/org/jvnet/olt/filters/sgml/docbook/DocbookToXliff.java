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


package org.jvnet.olt.filters.sgml.docbook;

import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;
import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.format.sgml.*;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.utilities.*;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.SgmlToTaggedMarkupConverter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

import java.io.*;
import java.util.logging.*;
/**
 *
 * @author  timf
 */
public class DocbookToXliff {
    
    /**
     * This is just test code - don't use this in production !
     * Instead use the SgmlFilter.xxxx methods !
     */
    public DocbookToXliff(String directory, String filename, String language, String encoding, Logger logger) throws SgmlFilterException, IOException{
        try {
            File file = new File(filename);
            //String shortname = filename.substring(directory.length(),filename.length());
            FileInputStream inStream = new FileInputStream(file);
            Reader reader;
            try {
                reader = new InputStreamReader(inStream, encoding);
            } catch (java.io.UnsupportedEncodingException e){
                throw new SgmlFilterException("Problem trying to read html file in encoding "+ encoding);
            }
            
            //  Parse the input file to get xliff files array
            EntityManager gvm = new EntityManager();
            FileOutputStream xliffOutputStream = new FileOutputStream(filename+".xlf");
            FileOutputStream sklOutputStream = new FileOutputStream(filename+".skl");
            
            OutputStreamWriter xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            OutputStreamWriter sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            
            org.jvnet.olt.parsers.tagged.TagTable tagTable = new DocbookTagTable();
            SegmenterTable segmenterTable = new DocbookSegmenterTable();
            // parser.parseForReverse("SGML",language, "de-DE", shortname, xliffWriter, sklWriter, table, segmenterTable, false);
            //  Parse the input file.
            SgmlDocFragmentParser parser = new  SgmlDocFragmentParser(reader);
            try {
                parser.parse();
                
                FormatWrapper wrapper = new SgmlFormatWrapper(gvm, tagTable, segmenterTable);
                XliffSegmenterFormatter formatter = new XliffSegmenterFormatter("SGML", language, filename, xliffWriter, sklWriter, gvm, wrapper);
                
                //System.out.println("setting "+table);
                //System.out.println("setting "+segmenterTable);
                formatter.setTagTable(tagTable);
                formatter.setSegmenterTable(segmenterTable);
                SgmlSegmenterVisitor sgmlSegmenterVisitor =
                new SgmlSegmenterVisitor(formatter, segmenterTable, tagTable, language, gvm, new SgmlToTaggedMarkupConverter());
                // tell the visitor that we're parsing a file containing only the
                // text of a programlisting or other such tag.
                boolean treatFileAsSingleSegment = false;
                sgmlSegmenterVisitor.setTreatFileAsSingleSegment(treatFileAsSingleSegment);
                parser.walkParseTree(sgmlSegmenterVisitor, null);
            
            } catch (Throwable t){ // bad exception handling here, but it's just sample code
                throw new SgmlFilterException("Problem parsing sgml file "+filename+" "+t.getMessage());
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
        catch (SgmlFilterException e){
            System.err.println("Caught a SgmlFilterException " + e);
            throw e;
        }
        catch (java.io.IOException e){
            System.err.println("Caught an io exception " + e);
            throw e;
        }
        
    }
    
    
    /** This class takes in a language and a html file, and outputs a .zip archive containing
     *  an xliff file and it's associated skeleton file.
     *
     * @param argv  Two arguments needed in here : the input file and language name
     */
    public static void main(String[] argv) {
        try {
            
            if (argv.length != 2){
                System.out.println("Usage : SgmlToXliff <input file> <language>\n"+
                "where language is currently en, de, es, fr, it, sv\n"+
                " [note: all languages have asian character support.]");
                System.exit(0);
            }
            //String encoding = System.getProperty("file.encoding");
            //System.out.println ("creating reader in " + encoding +" encoding.");
            //  Open the input file.
            Logger logger = Logger.getLogger("com.sun.tt.filters.testlogger");
            DocbookToXliff task = new DocbookToXliff("", argv[0], argv[1], System.getProperty("file.encoding"), logger);
        } catch (Exception e){
            System.err.println(e.getMessage());
            
        }
    }
    
    
}
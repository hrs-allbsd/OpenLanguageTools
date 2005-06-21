
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ViewHtmlSegments.java
 *
 * Created on Thu Jul  4 19:20:41 BST 2002
 */

package org.jvnet.olt.filters.html;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SimpleSgmlSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

import org.jvnet.olt.parsers.tagged.SgmlToTaggedMarkupConverter;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.TagTable;

import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.format.sgml.*;
import org.jvnet.olt.filters.sgml.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author  timf
 */
public class ViewHtmlSegments {
    
    /** This is the main method which creates and uses a HtmlParser object
     *
     * @param argv  Two arguments needed in here : the input file and language name
     */
    public static void main(String[] argv) {
        try {
            
            if (argv.length != 2){
                System.out.println("Usage : ViewHtmlSegments <input file> <language>\n"+
                "where language is currently en, de, es, fr, it, sv\n"+
                " [note: all languages have asian character support.]");
                System.exit(0);
            }
            
            String encoding = System.getProperty("file.encoding");
            //System.out.println ("creating reader in " + encoding +" encoding.");
            //  Open the input file.
            File file = new File(argv[0]);
            String language = new String(argv[1]);
            FileInputStream inStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(inStream, encoding);
            
            //  Parse the input file to get xliff files array
            //  Parse the input file to get xliff files array
            EntityManager gvm = new EntityManager();     
            org.jvnet.olt.parsers.tagged.TagTable tagTable = new HtmlTagTable();
            SegmenterTable segmenterTable = new HtmlSegmenterTable();

            //parser.parseSgmlForXliff("HTML",language, shortname, xliffWriter, sklWriter, htmlTagTable, htmlSegmenterTable, treatAsSingleSegment);
            NonConformantSgmlDocFragmentParser parser = new  NonConformantSgmlDocFragmentParser(reader);
            try {
                parser.parse();
                
                SimpleSgmlSegmenterFormatter formatter = new SimpleSgmlSegmenterFormatter();
                //System.out.println("setting "+table);
                //System.out.println("setting "+segmenterTable);
                SgmlSegmenterVisitor sgmlSegmenterVisitor =
                new SgmlSegmenterVisitor(formatter, segmenterTable, tagTable, language, gvm,
                new SgmlToTaggedMarkupConverter());
                // tell the visitor that we're parsing a file containing only the
                // text of a programlisting or other such tag.
                boolean treatFileAsSingleSegment = false;
                sgmlSegmenterVisitor.setTreatFileAsSingleSegment(treatFileAsSingleSegment);
                parser.walkParseTree(sgmlSegmenterVisitor, null);
            
            } catch (Throwable t){ // bad exception handling here, but it's just sample code
                throw new Exception("Problem parsing html file "+argv[0]+" "+t.getMessage());
            }
            //filter.parseSgmlForDemo(language, new HtmlTagTable(), new HtmlSegmenterTable());
            //System.out.println (parser.parseForUnitTest());
           
           
           /* Segment[] arr = parser.parseForAlignment();
            
            for (int i=0;i<arr.length;i++){
                //if (!arr[i].isHardBoundary()){
                System.out.println(arr[i]);
                //}
            }
           
            */
        } // very bad exception handling here, but it's only sample code
        catch (HtmlParserException e){
            System.err.println("Caught a htmlParserException " + e);
            e.printStackTrace();
        }
        catch (java.io.IOException e){
            System.err.println("Caught an io exception " + e);
            e.printStackTrace();
        } catch (Exception e){
            System.err.println("Caught an exception " + e);
            e.printStackTrace();
        }
    }
    
    public static String concatenateTags(List taglist){
        StringBuffer buf = new StringBuffer();
        Iterator it = taglist.iterator();
        while (it.hasNext()){
            buf.append((String)it.next());
        }
        System.out.println("Buffer"+buf.toString());
        return buf.toString();
        
    }
    
}


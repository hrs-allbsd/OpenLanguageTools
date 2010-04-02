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
 * ViewHtmlSegments.java
 *
 * Created on Thu Jul  4 19:20:41 BST 2002
 */

package org.jvnet.olt.filters.sgml;
import org.jvnet.olt.filters.sgml.docbook.*;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser;
import org.jvnet.olt.parsers.tagged.*;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.sgml.EntityManager;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SimpleSgmlSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

import java.io.*;
import java.util.*;
/**
 *
 * @author  timf
 */
public class ViewSgmlSegments {
    
    /** This is the main method which creates and uses a SgmlFilter object
     *
     * @param argv  Two arguments needed in here : the input file and language name
     */
    public static void main(String[] argv) {
        try {
            
            if (argv.length != 2){
                System.out.println("Usage : ViewSgmlSegments <input file> <language>\n"+
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
            GlobalVariableManager gvm = (GlobalVariableManager) new EntityManager();
            //gvm.setVariable("%FCS;","INCLUDE","MARKEDSECTION");
            
            //SgmlFilter filter = new SgmlFilter(reader, gvm);
            DocbookTagTable table = new DocbookTagTable();
            DocbookSegmenterTable segmenterTable = new DocbookSegmenterTable();
            
            // parser.parseForReverse("SGML",language, "de-DE", shortname, xliffWriter, sklWriter, table, segmenterTable, false);
            //  Parse the input file.
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader);
            parser.parse();
            
            SegmenterFormatter formatter = new SimpleSgmlSegmenterFormatter();
            //System.out.println("setting "+table);
            //System.out.println("setting "+segmenterTable);
            SgmlSegmenterVisitor sgmlSegmenterVisitor = 
            new SgmlSegmenterVisitor(formatter, segmenterTable, table, language, gvm, new SgmlToTaggedMarkupConverter());
            // tell the visitor that we're parsing a file containing only the
            // text of a programlisting or other such tag.
            boolean treatFileAsSingleSegment = false;
            sgmlSegmenterVisitor.setTreatFileAsSingleSegment(treatFileAsSingleSegment);
            parser.walkParseTree(sgmlSegmenterVisitor, null);
            
            //filter.parseSgmlForDemo(language, table, segt);
            //System.out.println (parser.parseForUnitTest());
           
           
           /* Segment[] arr = parser.parseForAlignment();
            
            for (int i=0;i<arr.length;i++){
                //if (!arr[i].isHardBoundary()){
                System.out.println(arr[i]);
                //}
            }
           
            */
        } // very bad exception handling here, but it's only sample code
        catch (SgmlFilterException e){
            System.err.println("Caught a sgmlParserException " + e.getMessage());
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


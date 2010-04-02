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

package org.jvnet.olt.filters.xml;

import org.jvnet.olt.filters.xml.xmlconfig.*;
import java.io.*;
import org.xml.sax.InputSource;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SgmlToTaggedMarkupConverter;
import org.jvnet.olt.utilities.XliffZipFileIO;
import org.jvnet.olt.format.sgml.EntityManager;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.sgml.SgmlFormatWrapper;
import org.jvnet.olt.filters.sgml.SgmlSegmenterVisitor;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParser;

import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.ReverseXliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;


import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class XmlFilterMain {
    
    public static void main(String[] args) {
        
        if (args.length != 4 && args.length != 5) {
            System.out.println("Usage: <xml-file> <source-language> <target-language> <config-file> [<config-file>]");
            System.exit(1);
        }
        
        InputSource inputSource = null;
        
        
        try {
            TagTable tagTable = null;
            SegmenterTable segmenterTable = null;
            
            
            if(args.length == 4) {
                try {
                    inputSource = new InputSource(args[3]);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                XmlConfigHandler xmlConfigHandler = new XmlConfigHandlerImpl();
                XmlConfigParser xmlConfigParser =
                new XmlConfigParser(xmlConfigHandler, null);
                xmlConfigParser.parse(inputSource);
                XmlConfig config = xmlConfigParser.getXmlConfig();
                try {
                 tagTable = new XmlTagTable(config);
             segmenterTable = new XmlSegmenterTable(config);
 		} catch (XmlFilterException e){
 		   System.out.println("Unable to initialise either tag table or segmenter table " + e.getMessage());
                }

            } else {
                InputSource inputSource2 = null;
                try {
                    inputSource = new InputSource(args[3]);
                    inputSource2 = new InputSource(args[4]);
                } catch (NullPointerException e) {
                    System.out.println("Caught npe " + e.getMessage());
                    System.exit(1);
                }
                
                XmlConfigHandler xmlConfigHandler = new XmlConfigHandlerImpl();
                
                XmlConfigParser xmlConfigParser =
                new XmlConfigParser(xmlConfigHandler, null);
                
                XmlConfig config = null;
                
                HashMap configMap = new HashMap();
                
                xmlConfigParser.parse(inputSource);
                config = xmlConfigParser.getXmlConfig();
                
                                
                configMap.put(config.getNamespace(),config);
                
                xmlConfigParser.parse(inputSource2);
                config = xmlConfigParser.getXmlConfig();
                                
                configMap.put(config.getNamespace(),config);
                
                /* Set keys = configMap.keySet();
                System.out.println("known configs in Map are ");
                for (Iterator it = keys.iterator(); it.hasNext();){
                    String key = (String)it.next();
                    System.out.println(key);
                } */
                
                try {
                tagTable = new XmlTagTable(configMap);
                segmenterTable = new XmlSegmenterTable(configMap);
 		} catch (XmlFilterException e){
		   System.out.println("Unable to initialise segmenter or tag table "+ e.getMessage());
                }
            }
            
            
            String encoding = System.getProperty("file.encoding");
            //System.out.println ("creating reader in " + encoding +" encoding.");
            //  Open the input file.
            
            String filename = args[0];
            
            File file = new File(filename);
            String shortname = filename.substring(file.getParent().length() + 1);
            FileInputStream inStream = new FileInputStream(file);
            Reader reader;
            try {
                reader = new InputStreamReader(inStream, encoding);
            } catch (java.io.UnsupportedEncodingException e){
                throw new XmlFilterException("Problem trying to read xml file in encoding "+ encoding);
            }
            String sourceLanguage = new String(args[1]);
            String targetLanguage = new String(args[2]);
            
            //  Parse the input file to get xliff files array
            GlobalVariableManager gvm = (GlobalVariableManager) new EntityManager();
                        
            FileOutputStream xliffOutputStream = new FileOutputStream(filename+".xlf");
            FileOutputStream sklOutputStream = new FileOutputStream(filename+".skl");
            
            OutputStreamWriter xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            OutputStreamWriter sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
                                    
            //filter.parseXmlForXliff(sourceLanguage, shortname,
            //xliffWriter, sklWriter, xmlTagTable, xmlSegmenterTable);
            XmlDocFragmentParser parser = new XmlDocFragmentParser(reader);
            try {
                parser.parse();
                
                FormatWrapper wrapper = new SgmlFormatWrapper(gvm, tagTable, segmenterTable);
                ReverseXliffSegmenterFormatter formatter = new ReverseXliffSegmenterFormatter("XML", sourceLanguage, targetLanguage, shortname, xliffWriter, sklWriter, false, gvm, wrapper);
                
                //System.out.println("setting "+table);
                //System.out.println("setting "+segmenterTable);
                formatter.setTagTable(tagTable);
                formatter.setSegmenterTable(segmenterTable);
                SgmlSegmenterVisitor sgmlSegmenterVisitor =
                new SgmlSegmenterVisitor(formatter, segmenterTable, tagTable, sourceLanguage, gvm, new SgmlToTaggedMarkupConverter());
                // tell the visitor that we're not parsing a file containing only the
                // text of a programlisting or other such tag.
                sgmlSegmenterVisitor.setTreatFileAsSingleSegment(false);
                parser.walkParseTree(sgmlSegmenterVisitor, null);
            
            } catch (Throwable t){ // bad exception handling here, but it's just sample code
                throw new XmlFilterException("Problem parsing book file "+shortname+" "+t.getMessage());
            }
            
            
            /* boolean reverse = false;
            filter.parseForXmlReverse(sourceLanguage, targetLanguage, shortname, xliffWriter, sklWriter, xmlTagTable, xmlSegmenterTable, reverse); */
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
            
        } catch (XmlFilterException e){
            System.out.println("Caught SgmlFilterException " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch(org.xml.sax.SAXException ex) {
            System.out.println("Caught SAX Exception " + ex.getMessage());
            System.exit(1);
        }  catch(javax.xml.parsers.ParserConfigurationException ex) {
            System.out.println("Caught parser config exception " + ex.getMessage());
            System.exit(1);
        } catch(java.io.IOException ex) {
            System.out.println("Caught io exception "+  ex.getMessage());
            System.exit(1);
        }
        
    }
    
}

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
 * PlaintextFilter.java
 *
 * Created on July 2, 2003, 12:10 PM
 */

package org.jvnet.olt.filters.plaintext;

import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;
//import org.jvnet.olt.xliff.AlignmentSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SimpleSgmlSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.ReverseXliffSegmenterFormatter;

import org.jvnet.olt.format.sgml.EntityManager;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.*;
import org.jvnet.olt.format.plaintext.*;

import java.util.*;
import java.io.*;
import java.util.logging.*;
import org.jvnet.olt.utilities.*;
/**
 *
 * @author  timf
 */
public class PlaintextParser {
    
    private Reader reader;
    
    public PlaintextParser(java.io.Reader reader) {
        this.reader = reader;
    }
    
    /*public org.jvnet.olt.alignment.Segment[] parseForAlignment(String language)
    throws PlaintextParserException {
        
        try {
            //  Parse the input file.
            BlockSegmenter_en blockParser = new  BlockSegmenter_en(reader);
            blockParser.parse();
            
            GlobalVariableManager gvm = new EntityManager();
            AlignmentSegmenterFormatter formatter = new AlignmentSegmenterFormatter(language);
            XLIFFVisitor xliffVisitor = new XLIFFVisitor(formatter, language);
            blockParser.walkParseTree(xliffVisitor, null);
            formatter.flush();
                       
            List segmentList = formatter.getSegmentList();
            Iterator it = segmentList.iterator();
            org.jvnet.olt.alignment.Segment[] arr = new org.jvnet.olt.alignment.Segment[segmentList.size()];
            int i = 0;
            while (it.hasNext()){
                org.jvnet.olt.alignment.Segment seg = (org.jvnet.olt.alignment.Segment)it.next();
                arr[i] = seg;
                i++;
            }
            
            return arr;
            
        } catch (java.io.IOException e){
            throw new PlaintextParserException(e.getMessage());
        } catch (Exception e){
            throw new PlaintextParserException(e.getMessage());
        }
    }
    */
    
    public void parseForXliff(String language, String sourceFileName, String xliffFileName, String sklFileName,
    Writer xliffWriter, Writer sklWriter) throws PlaintextParserException {
        
        try {
            //  Parse the input file.
            BlockSegmenter_en blockParser = new  BlockSegmenter_en(reader);
            blockParser.parse();
            FormatWrapper wrapper = new PlainTextFormatWrapper();
            SegmenterFormatter formatter = new XliffSegmenterFormatter("PLAINTEXT", language, sourceFileName,
                xliffWriter, sklWriter, wrapper);
            XLIFFVisitor xliffVisitor = new XLIFFVisitor(formatter, language);
            blockParser.walkParseTree(xliffVisitor, null);
            formatter.flush();                            
            
        } catch (java.io.IOException e){
            throw new PlaintextParserException(e.getMessage());
        } catch (Exception e){
            throw new PlaintextParserException(e.getMessage());           
        }
    }
    
    
    
    public List parseForDemo(boolean writeOutput) throws PlaintextParserException {
        
        try {
            //  Parse the input file.
            BlockSegmenter_en blockParser = new  BlockSegmenter_en(reader);
            blockParser.parse();
            
            GlobalVariableManager gvm = new EntityManager();
            
            SimpleSgmlSegmenterFormatter formatter = new SimpleSgmlSegmenterFormatter(true);
            XLIFFVisitor xliffVisitor = new XLIFFVisitor(formatter, "en-US");
            blockParser.walkParseTree(xliffVisitor, null);
            formatter.flush();
            return formatter.getSegments();        
            
            
        } catch (java.io.IOException e){
            throw new PlaintextParserException(e.getMessage());
        } catch (Exception e){
            throw new PlaintextParserException(e.getMessage());
        }
    }
    
    public String parseForUnitTest(){
        ; // nothing here - not yet implemented
        return "";
    }
    
    public void parseForReverse(String language, String targLang,  String sourceFileName, String xliffFileName, String sklFileName,
    Writer xliffWriter, Writer sklWriter, boolean writeReverse) throws PlaintextParserException {
        
              try {
            //  Parse the input file.
            BlockSegmenter_en blockParser = new  BlockSegmenter_en(reader);
            blockParser.parse();
            FormatWrapper wrapper = new PlainTextFormatWrapper();
            GlobalVariableManager gvm = new EntityManager();
            SegmenterFormatter formatter = new ReverseXliffSegmenterFormatter("PLAINTEXT", language,
            targLang, sourceFileName, xliffWriter, sklWriter, writeReverse, gvm, wrapper);
            XLIFFVisitor xliffVisitor = new XLIFFVisitor(formatter, language);
            blockParser.walkParseTree(xliffVisitor, null);
            formatter.flush();
            
        } catch (java.io.IOException e){
            throw new PlaintextParserException(e.getMessage());
        } catch (Exception e){
            throw new PlaintextParserException(e.getMessage());
        }
    }
    
    
}

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

package org.jvnet.olt.filters.plaintext;

import org.jvnet.olt.io.*;
import org.jvnet.olt.filters.segmenter_facade.*;
import org.jvnet.olt.filters.sgml.*;
import java.util.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.BufferedReader;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

import org.jvnet.olt.format.plaintext.*;

public class XLIFFVisitor implements BlockSegmenter_enVisitor {
    private int transUnitId=0;
    private String filename="";
    private Writer xliffWriter;
    private Writer sklWriter;
    
    // This map contains the formatting information for each node.
    // (it's a global, since the visit method populates it, but it needs
    // to be accessed by the printFooter() method for getting the last bit
    // of formatting that's not tied to a segment.
    private Map formatting;
    private SegmenterFormatter formatter;
    private SegmenterFacade segmenterFacade;
    private String language;
    
    public XLIFFVisitor(SegmenterFormatter formatter, String language){
        
        this.xliffWriter = xliffWriter;
        this.sklWriter = sklWriter;
        this.formatting = new HashMap();
        this.formatter = formatter;
        this.language = language;
    }
    
    
    public Object visit(SimpleNode node, Object data) throws RuntimeException {
       try { 
            if (node.getType() == BlockSegmenter_enTreeConstants.JJTBLOCK) {
                String block = node.getNodeData();
                // for plaintext, we normalise all whitespace characters
                // (which zonks low-ascii control characters !)
                //StringReader reader = new StringReader(normalise(new StringBuffer(block)));
                StringReader reader = new StringReader(block);
                Map formatting = new HashMap();
                List tmpSegments = new ArrayList();
                segmenterFacade = new SegmenterFacade(reader, language);
                
                try {
                    segmenterFacade.parse();
                } catch (java.lang.Exception e){
                    throw new PlaintextParserException("Caught an exception doing sentence segmentation " + e.getMessage());
                }
                
                tmpSegments = segmenterFacade.getSegments();
                formatting = segmenterFacade.getFormatting();

                int counter=1;
                
                
                Iterator it = tmpSegments.iterator();
                while (it.hasNext()){
                    String s = (String)it.next();
                    formatter.writeSegment(s, wordCount(s,0));
                    if (formatting.containsKey(new Integer(counter))){
                        formatter.writeFormatting(((String)formatting.get(new Integer(counter))));;
                        // remove that object from the formatting map
                        formatting.remove(new Integer(counter));
                    }
                    counter++;
                }
                
                
            } else if (node.getType() == BlockSegmenter_enTreeConstants.JJTBLANKS ||
            node.getType() == BlockSegmenter_enTreeConstants.JJTNEWLINE){
                formatter.writeFormatting(node.getNodeData());
            }        
         } catch (org.jvnet.olt.filters.plaintext.PlaintextParserException e){
            throw new RuntimeException("Plaintext parse exception "+e.getMessage());
         } catch (org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException ex){
            throw new RuntimeException("Seg formatter exception "+ex.getMessage());
         }
        
        return data;
    }
    
    public int wordCount(String segment, int existingWordCount) throws PlaintextParserException {
        StringReader reader = new StringReader(segment);
        SegmenterFacade segmenterFacade = new SegmenterFacade(reader, language);
        
        try {
            segmenterFacade.parseForStats();
        } catch (java.lang.Exception e){
            throw new PlaintextParserException("Caught an exception doing wordcounting of "+segment+" : " + e.getMessage());
        }
        List words = segmenterFacade.getWordList();
        return words.size()+existingWordCount;
        
    }
    
    
        /* This code comes directly from org.jvnet.olt.parsers.SgmlDocFragmentParser.NewlineRemovingVisitor
     * written by John Corrigan. Nice code !
     */
    /** Written by JohnC, this does SGML whitespace normalisation of the input.
     * Detailed comments are included in the source code.
     * @param buffer Input containing text to be normalised.
     * @throws HtmlParserException if an error was encounted during normalisation.
     * @return a normalised version of the input.
     */
    public String normalise(StringBuffer buffer) throws PlaintextParserException {
        StringBuffer bufOut = new StringBuffer();
        StringReader reader = new StringReader(buffer.toString());
        boolean removeWhitespace = true;
        final int DEFAULT = 0;
        final int WS = 1;
        final int CR_AFTER_WS = 2;
        final int CR = 3;
        
        int state = DEFAULT;
        
        //  State machine
        //
        //   +----+-------+-------+-------+--------+
        //   ||||||  WS   |  CR   |  DEF  | WS_CR  |
        //   +----+-------+-------+-------+--------+
        //   | ws |  WS   |  WS   |  WS   |  WS    |
        //   |    |  (ws) |  (ws) |  (ws) |  (ws)  |
        //   |    |   or  |   or  |   or  |   or   |
        //   |    |   ()  |  (' ')|  (' ')|    ()  |
        //   +----+-------+-------+-------+--------+
        //   | cr | WS_CR |  CR   |  CR   | WS_CR  |
        //   |    |   ()  |   ()  |   ()  |   ()   |
        //   +----+-------+-------+-------+--------+
        //   | ch |  DEF  |  DEF  |  DEF  |  DEF   |
        //   |    |   (ch)|(' ' + |  (ch) |  (ch)  |
        //   |    |       |   ch) |       |        |
        //   +----+-------+-------+-------+--------+
        //
        //   DEF = default state
        //   WS  = white space state
        //   ws  = white space character
        //   CR  = Carriage Return state
        //   cr  = Carriage Return character
        //   WS_CR  = carriage return after white space state
        //   ch  = Any character that is not ws or CR
        //
        //   The table represents the transitions that occur
        //   when the characters are read in when the system
        //   is in a given state. The top row represents the
        //   current state. The entries in the table represent
        //   the new states. The stuff in brackets is what is
        //   written out on transition to the new state.
        //
        int ch = 0;
        
        try {
            while((ch = reader.read()) != -1) {
                switch(ch) {
                    case (int) '\t':
                    case (int) ' ':
                        if(removeWhitespace) {
                            //  Normalize whitespace, i.e., only output the first
                            //  whitespace chracter. Ensure it is a space. This
                            //  will be the case unless we are in the states
                            //  WS or CR_AFTER_WS.
                            if( !( state == WS ||
                            state == CR_AFTER_WS) ) {
                                bufOut.append(' ');
                            }
                        }
                        else {
                            //  Leave whitespace alone if removeWhitespace is false.
                            bufOut.append((char) ch);
                        }
                        state = WS;
                        break;
                    case (int) '\n':
                    case (int) '\r':
                        if( state == WS ||
                        state == CR_AFTER_WS) {
                            state = CR_AFTER_WS;
                        }
                        else {
                            state = CR;
                        }
                        break;
                    default:
                        // handle other whitespaces, not mentioned in the above CASES
                        // (we treat them just as we do in the case of ' ' and '\t')
                        // ----------
                        if (Character.isWhitespace((char)ch)) {
                            if(removeWhitespace) {
                                //  Normalize whitespace, i.e., only output the first
                                //  whitespace chracter. Ensure it is a space. This
                                //  will be the case unless we are in the states
                                //  WS or CR_AFTER_WS.
                                if( !( state == WS ||
                                state == CR_AFTER_WS) ) {
                                    bufOut.append(' ');
                                }
                            }
                            else {
                                //  Leave whitespace alone if removeWhitespace is false.
                                bufOut.append((char) ch);
                            }
                            state = WS;
                        }
                        //----------
                        else if( state == CR ) { bufOut.append(' '); }
                        bufOut.append((char) ch);
                        state = DEFAULT;
                        break;
                }
            }
            //  Should we append a space if we end in the CR state?, i.e.
            if( state == CR ) { bufOut.append(' '); }
        }
        catch(IOException ioEx) {
            throw new PlaintextParserException("Caught io exception in normalise function");
        }
        return bufOut.toString();
    }
    
    
}

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
import java.util.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.BufferedReader;
public class AlignmentTextVisitor implements BlockSegmenter_enVisitor {
    
    private int transUnitId=0;
    private String filename="";
    private Writer alignmentWriter;
    private Writer sklWriter;
    private String language="";
    // This map contains the formatting information for each node.
    // (it's a global, since the visit method populates it, but it needs
    // to be accessed by the printFooter() method for getting the last bit
    // of formatting that's not tied to a segment.
    private Map formatting;
    
    public AlignmentTextVisitor(Writer alignmentWriter){
        
        this.alignmentWriter = alignmentWriter;
        this.formatting = new HashMap();
    }
    
    public void setLanguage(String language){
        this.language = language;
    }
    
    public Object visit(SimpleNode node, Object data) throws RuntimeException{
        try {
            if (node.getType() == BlockSegmenter_enTreeConstants.JJTBLOCK){
                StringReader myreader = new StringReader(node.getNodeData());
                SegmenterFacade segmenter = new SegmenterFacade(myreader,language);
                try {
                segmenter.parse();
                } catch (Exception e){
                  throw new RuntimeException("Exception while visiting parse tree "+e.getMessage());
                }
                
                Collection mycoll;
                mycoll  = segmenter.getSegments();
                formatting = segmenter.getFormatting();
                
                int counter=0;
                Iterator it = mycoll.iterator();
                while (it.hasNext()){
                    String s = (String)it.next();
                    alignmentWriter.write(wrapXMLChars(s)+"\n");
                    if (formatting.containsKey(new Integer(counter))){
                        alignmentWriter.write("*\n");
                        // remove that object from the formatting map
                        formatting.remove(new Integer(counter));
                    }
                    transUnitId++;
                    counter++;
                }       
                
            } else if (node.getType() == BlockSegmenter_enTreeConstants.JJTBLANKS ||
            node.getType() == BlockSegmenter_enTreeConstants.JJTNEWLINE){
                alignmentWriter.write("*\n");
            }
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
        
        return data;
    }
    
    
    public void printAlignmentTextFooter() throws java.io.IOException{
        // Have to write out the remaining (if any) elements in the formatting map.
        // this can happen if there's formatting after the last <trans-unit> element
        System.out.println("Remaining formatting :" +
        formatting.keySet().size());
        Iterator it = formatting.keySet().iterator();
        
        while (it.hasNext()){
            Integer i = (Integer) it.next();
            String s = (String)formatting.get(i);
            alignmentWriter.write("*\n");
            transUnitId = transUnitId+i.intValue();
            
            
        }
        
    }
        
    /**
     * This method takes in characters in the string, and writes them to the writer,
     * first passing them through JohnC's HTMLEscapeFilterReader. What this does, is
     * convert ampersands, less-than and greater-than characters to an SGML/XML friendly
     * format using the &amp;amp; &amp;lt; and &amp;gt; entities
     */
    public String wrapXMLChars(String string) throws java.io.IOException {
        BufferedReader buf = new BufferedReader(new HTMLEscapeFilterReader
        (new StringReader(string)));
        StringWriter writer= new StringWriter();
        while (buf.ready()){
            int i = buf.read();
            if (i == -1)
                break;
            else writer.write(i);
        }
        return writer.toString();
    }
    
}

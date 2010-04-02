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
 * ParserVisitor.java
 *
 * Created on April 24, 2006, 10:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.filters.soxliff;


import org.dom4j.*;
import java.util.*;
import java.io.*;
import org.dom4j.io.SAXReader;
import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.segmenter_facade.*;
import org.jvnet.olt.filters.soxliff.util.*;

/**
 * Visitor that walks through all Staroffice xliff elements and construct
 * OLT xliff file
 *
 * @author michal
 */
public class ParserVisitor extends VisitorSupport {
    
    private XliffWriterFacade writer = null;
    
    // XLIFF tag definition
    private static final String XLIFF_SOURCE        = "source";
    private static final String XLIFF_TARGET        = "target";
    private static final String XLIFF_CONTEXT_GROUP = "context-group";
    private static final String XLIFF_CONTEXT = "context";
    private static final String XLIFF_CONTEXT_TYPE = "context-type";
    private static final String XLIFF_TRANSUNIT   = "trans-unit";
    private static final String XLIFF_TRANSUNIT_ID           = "id";
    private static final String XLIFF_TRANSUNIT_RESTYPE = "restype";
    
    private static final String SO_ORIGINAL_ID = "x-original-id";
    private static final String SO_RESTYPE       = "x-restype";
    private static final String SO_SUBFORMAT  = "x-subformat-ws";
    
    SOSegment currentSegment = null;
    SAXReader saxReader        = null;
    
    /**
     * Create new instance of the visitor
     *
     * @param writer that construct OLT xliff
     */
    public ParserVisitor(XliffWriterFacade writer) {
        super();
        this.writer = writer;
        saxReader = new SAXReader();
    }
    
    /**
     * pass an element and make the converion job
     *
     * @param element of the xliff file
     */
    public void visit(Element element) {
        try {
            if(XLIFF_TRANSUNIT.equals(element.getName())) {
                // get source segment
                String source = element.elementText(XLIFF_SOURCE);
                
                if(source==null || "".equals(source)) {
                    throw new RuntimeException(XLIFF_TRANSUNIT + " does not contain valid " + XLIFF_SOURCE);
                }
                source = RemoveEscapeChars.convert(source);
                SOSegment segment = new SOSegment(source);
                
                // get original id
                String originalId = element.attributeValue(XLIFF_TRANSUNIT_ID);
                if(originalId==null) {
                    throw new RuntimeException(XLIFF_TRANSUNIT + " does not contain valid attribute " + XLIFF_TRANSUNIT_ID);
                }
                segment.addContext(SO_ORIGINAL_ID,originalId);
                
                // get staroffice restype
                String resType  = element.attributeValue(XLIFF_TRANSUNIT_RESTYPE);
                if(resType==null) {
                    throw new RuntimeException(XLIFF_TRANSUNIT + " does not contain valid attribute " + XLIFF_TRANSUNIT_RESTYPE);
                }
                segment.addContext(SO_RESTYPE,resType);
                
                // subsegment source
                StringReader reader = new StringReader(source);
                SegmenterFacade facade = new SegmenterFacade(reader, "en-US");

                List subSegments = null;
                Map subFormatting = null;
              
                // don't subsegment
                
                /* 
                try {
                    facade.parse();
                    subSegments = facade.getSegments();
                    subFormatting = facade.getFormatting();
                } catch (Throwable t) {
                    System.err.println("Cannot segment: " + source + " :"+t.getMessage());
                }
                */

                // get target
                String target  = element.elementText(XLIFF_TARGET);
                
                if(target!=null && !"".equals(target)) {
                    segment.setTarget(RemoveEscapeChars.convert(target));
                }
                
                // get all context elements
                Element contextGroup = element.element(XLIFF_CONTEXT_GROUP);
                if(contextGroup!=null) {
                    List contexts = contextGroup.elements(XLIFF_CONTEXT);
                    Iterator it = contexts.iterator();
                    while(it.hasNext()) {
                        Element contextElement = (Element)it.next();
                        String contextType = contextElement.attributeValue(XLIFF_CONTEXT_TYPE);
                        if(contextType==null) {
                            throw new RuntimeException(XLIFF_CONTEXT + " does not contain attribute" + XLIFF_CONTEXT_TYPE);
                        }
                        
                        String contextValue = contextElement.getText();
                        if(contextValue==null) {
                            contextValue = "";
                        }
                        
                        segment.addContext(contextType,contextValue);
                    }
                }
                
                // write segment
                
                if(subSegments!=null && subSegments.size()>1 && isCorrectFormatting(subSegments)) {
                    
                    // do not write target if source is segmented
                    Iterator it = subSegments.iterator();
                    int segmentCounter = 1;
                    while(it.hasNext()) {
                        SOSegment subSegment = (SOSegment)segment.clone();
                        
                        String subSource = (String)it.next();
                        String subFormat = (String)subFormatting.get(new Integer(segmentCounter));
                        if (subFormat == null){
                            subFormat="";
                        }
                        subSegment.setSource(subSource);
                        subSegment.setTarget("");
                        subSegment.addContext(SO_SUBFORMAT,subFormat);
                        writer.writeSegment(subSegment);
                    }
                } else {
                    writer.writeSegment(segment);
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Check xml tags in subsegments
     * 
     * @return true if formatting is valid otherwise return false
     */
    private boolean isCorrectFormatting(List subSegments) {
        boolean result = true;
        Iterator it = subSegments.iterator();
        while(it.hasNext()) {
            String subSegment = (String)it.next();
            subSegment = "<x>" + subSegment + "</x>";
            try {
                Document doc = saxReader.read(new StringReader(subSegment));
            } catch(DocumentException e) {
                // cannot parse there is bad formatting in some segment
                result = false;
                break;
            }
        }
        return result;
    }
    
}

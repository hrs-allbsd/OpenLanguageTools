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
 * XmlToTaggedMarkupConverter.java
 *
 * Created on May 14, 2003, 4:44 PM
 */

package org.jvnet.olt.parsers.tagged;
import java.util.Map;
import java.util.HashMap;
/**
 *
 * @author  timf
 */
public class NonConformantToTaggedMarkupConverter implements NodeTypeConverter, TaggedMarkupNodeConstants {
    
    private Map jspToTaggedMap;
    
    //  constants defined in NonC doc fragment parser
  public int JJTFILE = 0;
  public int JJTDOCTYPE = 1;
  public int JJTDOCTYPE_BEGINNING = 2;
  public int JJTDOCTYPE_ENDING = 3;
  public int JJTINTERNAL_SUB_SET = 4;
  public int JJTINTERNAL_SUB_SET_BEGINNING = 5;
  public int JJTINTERNAL_SUB_SET_ENDING = 6;
  public int JJTINTERNAL_SUB_SET_WS_COMMENT = 7;
  public int JJTENTITY_DECL = 8;
  public int JJTNOTATION_DECL = 9;
  public int JJTSGML_DATA = 10;
  public int JJTCOMMENT = 11;
  public int JJTJSP = 12;
  public int JJTJSP_INLINE = 13;
  public int JJTCDATA = 14;
  public int JJTPROCESSING_INST = 15;
  public int JJTENTITY = 16;
  public int JJTINT_ENTITY = 17;
  public int JJTPCDATA = 18;
  public int JJTMARKED_SECT = 19;
  public int JJTSTART_MARKED_SECT = 20;
  public int JJTMARKED_SECTION_TAG = 21;
  public int JJTMARKED_SECTION_FLAG = 22;
  public int JJTOPEN_SQR_BRKT = 23;
  public int JJTEND_MARKED_SECT = 24;
  public int JJTTAG = 25;
  public int JJTOPEN_TAG = 26;
  public int JJTCLOSE_TAG = 27;
  public int JJTEOF = 28;

    
    /** Creates a new instance of JspToTaggedMarkupConverter
     *  Maintenance note : really this converter should be in the
     *  XML doc fragment parser package - that way, we could work
     *  solely on defined static final ints, so if the parser changes
     *  the numbers used, we're not affected (since tagged_markup
     *  gets built before the parsers, we'd need to put this code in
     *  the parsers in order to use their constants...)
     */
    public NonConformantToTaggedMarkupConverter() {
        jspToTaggedMap = new HashMap();
        jspToTaggedMap.put(new Integer(JJTFILE),new Integer(FILE));
        jspToTaggedMap.put(new Integer(JJTDOCTYPE),new Integer(DOCTYPE));
        jspToTaggedMap.put(new Integer(JJTDOCTYPE_BEGINNING),new Integer(DOCTYPE_BEGINNING));
        jspToTaggedMap.put(new Integer(JJTDOCTYPE_ENDING),new Integer(DOCTYPE_ENDING));
        jspToTaggedMap.put(new Integer(JJTINTERNAL_SUB_SET),new Integer(INTERNAL_SUB_SET));
        jspToTaggedMap.put(new Integer(JJTINTERNAL_SUB_SET_BEGINNING),new Integer(INTERNAL_SUB_SET_BEGINNING));
        jspToTaggedMap.put(new Integer(JJTINTERNAL_SUB_SET_ENDING),new Integer(INTERNAL_SUB_SET_ENDING));
        jspToTaggedMap.put(new Integer(JJTINTERNAL_SUB_SET_WS_COMMENT),new Integer(INTERNAL_SUB_SET_WS_COMMENT));
        jspToTaggedMap.put(new Integer(JJTENTITY_DECL),new Integer(ENTITY_DECL));
        jspToTaggedMap.put(new Integer(JJTNOTATION_DECL), new Integer(NOTATION_DECL));
        jspToTaggedMap.put(new Integer(JJTSGML_DATA),new Integer(MARKUP_DATA));
        jspToTaggedMap.put(new Integer(JJTCOMMENT),new Integer(COMMENT));

        jspToTaggedMap.put(new Integer(JJTCDATA),new Integer(CDATA));
        jspToTaggedMap.put(new Integer(JJTPROCESSING_INST),new Integer(PROCESSING_INST));
        jspToTaggedMap.put(new Integer(JJTENTITY),new Integer(ENTITY));
        jspToTaggedMap.put(new Integer(JJTINT_ENTITY),new Integer(INT_ENTITY));
        jspToTaggedMap.put(new Integer(JJTPCDATA),new Integer(PCDATA));
        jspToTaggedMap.put(new Integer(JJTMARKED_SECT),new Integer(MARKED_SECT));
        jspToTaggedMap.put(new Integer(JJTSTART_MARKED_SECT),new Integer(START_MARKED_SECT));
        jspToTaggedMap.put(new Integer(JJTMARKED_SECTION_TAG),new Integer(MARKED_SECTION_TAG));
        jspToTaggedMap.put(new Integer(JJTMARKED_SECTION_FLAG),new Integer(MARKED_SECTION_FLAG));
        jspToTaggedMap.put(new Integer(JJTOPEN_SQR_BRKT),new Integer(OPEN_SQR_BRKT));
        jspToTaggedMap.put(new Integer(JJTEND_MARKED_SECT),new Integer(END_MARKED_SECT));
        jspToTaggedMap.put(new Integer(JJTTAG),new Integer(TAG));
        jspToTaggedMap.put(new Integer(JJTOPEN_TAG),new Integer(OPEN_TAG)); 
        jspToTaggedMap.put(new Integer(JJTCLOSE_TAG),new Integer(CLOSE_TAG));
        jspToTaggedMap.put(new Integer(JJTEOF),new Integer(EOF));
        
        jspToTaggedMap.put(new Integer(JJTJSP_INLINE),new Integer(OPEN_TAG));
        jspToTaggedMap.put(new Integer(JJTJSP),new Integer(OPEN_TAG));
        
    }
    
    /**
     * This converts a node type from NonConformantDocFragmentParserTreeConstants into it's
     * equivalent node type as a TaggedMarkupNodeTreeConstants
     *
     * @param originalNodeType the jsp node type you want converted
     * @return a TaggedMarkupNode type
     */
    public int convert(int originalNodeType) {
        Integer i = (Integer)jspToTaggedMap.get(new Integer(originalNodeType));
        if (i == null)
            return originalNodeType;
        else {
            //System.out.println("Got " + originalNodeType + " returning " + i.intValue());
            return i.intValue();
        }
        
    }
    
}

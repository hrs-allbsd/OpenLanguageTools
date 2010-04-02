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
public class XmlToTaggedMarkupConverter implements NodeTypeConverter,TaggedMarkupNodeConstants {
    
    private Map xmlToTaggedMap;
    
    private static final int XMLJJTFILE = 0;
    private static final int XMLJJTDOCTYPE = 1;
    private static final int XMLJJTDOCTYPE_BEGINNING = 2;
    private static final int XMLJJTDOCTYPE_ENDING = 3;
    private static final int XMLJJTINTERNAL_SUB_SET = 4;
    private static final int XMLJJTINTERNAL_SUB_SET_BEGINNING = 5;
    private static final int XMLJJTINTERNAL_SUB_SET_ENDING = 6;
    private static final int XMLJJTINTERNAL_SUB_SET_WS_COMMENT = 7;
    private static final int XMLJJTENTITY_DECL = 8;
    private static final int XMLJJTXML_DATA = 9;
    private static final int XMLJJTCOMMENT = 10;
    private static final int XMLJJTCDATA = 11;
    private static final int XMLJJTPROCESSING_INST = 12;
    private static final int XMLJJTENTITY = 13;
    private static final int XMLJJTINT_ENTITY = 14;
    private static final int XMLJJTPCDATA = 15;
    private static final int XMLJJTMARKED_SECT = 16;
    private static final int XMLJJTSTART_MARKED_SECT = 17;
    private static final int XMLJJTMARKED_SECTION_TAG = 18;
    private static final int XMLJJTMARKED_SECTION_FLAG = 19;
    private static final int XMLJJTOPEN_SQR_BRKT = 20;
    private static final int XMLJJTEND_MARKED_SECT = 21;
    private static final int XMLJJTTAG = 22;
    private static final int XMLJJTOPEN_TAG = 23;
    private static final int XMLJJTCLOSE_TAG = 24;
    private static final int XMLJJTEOF = 25;
    
    /** Creates a new instance of XmlToTaggedMarkupConverter
     *  Maintenance note : really this converter should be in the
     *  XML doc fragment parser package - that way, we could work
     *  solely on defined static final ints, so if the parser changes
     *  the numbers used, we're not affected (since tagged_markup
     *  gets built before the parsers, we'd need to put this code in
     *  the parsers in order to use their constants...)
     */
    public XmlToTaggedMarkupConverter() {
        xmlToTaggedMap = new HashMap();
        xmlToTaggedMap.put(new Integer(XMLJJTFILE),new Integer(FILE));
xmlToTaggedMap.put(new Integer(XMLJJTDOCTYPE),new Integer(DOCTYPE));
xmlToTaggedMap.put(new Integer(XMLJJTDOCTYPE_BEGINNING),new Integer(DOCTYPE_BEGINNING));
xmlToTaggedMap.put(new Integer(XMLJJTDOCTYPE_ENDING),new Integer(DOCTYPE_ENDING));
xmlToTaggedMap.put(new Integer(XMLJJTINTERNAL_SUB_SET),new Integer(INTERNAL_SUB_SET));
xmlToTaggedMap.put(new Integer(XMLJJTINTERNAL_SUB_SET_BEGINNING),new Integer(INTERNAL_SUB_SET_BEGINNING));
xmlToTaggedMap.put(new Integer(XMLJJTINTERNAL_SUB_SET_ENDING),new Integer(INTERNAL_SUB_SET_ENDING));
xmlToTaggedMap.put(new Integer(XMLJJTINTERNAL_SUB_SET_WS_COMMENT),new Integer(INTERNAL_SUB_SET_WS_COMMENT));
xmlToTaggedMap.put(new Integer(XMLJJTENTITY_DECL),new Integer(ENTITY_DECL));
xmlToTaggedMap.put(new Integer(XMLJJTXML_DATA),new Integer(MARKUP_DATA));
xmlToTaggedMap.put(new Integer(XMLJJTCOMMENT),new Integer(COMMENT));
xmlToTaggedMap.put(new Integer(XMLJJTCDATA),new Integer(CDATA));
xmlToTaggedMap.put(new Integer(XMLJJTPROCESSING_INST),new Integer(PROCESSING_INST));
xmlToTaggedMap.put(new Integer(XMLJJTENTITY),new Integer(ENTITY));
xmlToTaggedMap.put(new Integer(XMLJJTINT_ENTITY),new Integer(INT_ENTITY));
xmlToTaggedMap.put(new Integer(XMLJJTPCDATA),new Integer(PCDATA));
xmlToTaggedMap.put(new Integer(XMLJJTMARKED_SECT),new Integer(MARKED_SECT));
xmlToTaggedMap.put(new Integer(XMLJJTSTART_MARKED_SECT),new Integer(START_MARKED_SECT));
xmlToTaggedMap.put(new Integer(XMLJJTMARKED_SECTION_TAG),new Integer(MARKED_SECTION_TAG));
xmlToTaggedMap.put(new Integer(XMLJJTMARKED_SECTION_FLAG),new Integer(MARKED_SECTION_FLAG));
xmlToTaggedMap.put(new Integer(XMLJJTOPEN_SQR_BRKT),new Integer(OPEN_SQR_BRKT));
xmlToTaggedMap.put(new Integer(XMLJJTEND_MARKED_SECT),new Integer(END_MARKED_SECT));
xmlToTaggedMap.put(new Integer(XMLJJTTAG),new Integer(TAG));
xmlToTaggedMap.put(new Integer(XMLJJTOPEN_TAG),new Integer(OPEN_TAG));
xmlToTaggedMap.put(new Integer(XMLJJTCLOSE_TAG),new Integer(CLOSE_TAG));
xmlToTaggedMap.put(new Integer(XMLJJTEOF),new Integer(EOF));

    }
    
    /**
     * This converts a node type from XmlDocFragmentParserTreeConstants into it's
     * equivalent node type as a TaggedMarkupNodeConstants
     *
     * @param originalNodeType the xml node type you want converted
     * @return a TaggedMarkupNode type
     */
    public int convert(int originalNodeType) {
        
        Integer i = (Integer)xmlToTaggedMap.get(new Integer(originalNodeType));
        if (i == null)
            return originalNodeType;
        else
            return i.intValue();
        
    }
    
}

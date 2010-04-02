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
 * FormatExtractorFactory.java
 *
 * Created on 06 January 2004, 15:44
 */
package org.jvnet.olt.editor.format;

import java.util.logging.*;

import org.jvnet.olt.editor.format.sgml.SgmlFormatElementExtractor;
import org.jvnet.olt.editor.format.swmsg.SwMsgElementExtractor;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.format.soxliff.SOXliffFormatExtractor;


/**
 * This class implements the factory pattern to build FormatExtractor objects.
 * @author  jc73554
 */
public class FormatElementExtractorFactory {
    private static final Logger logger = Logger.getLogger(VariableManagerFactory.class.getName());

    private static final int UNKNOWN = 0;
    private static final int SGML = 1;
    private static final int HTML = 2;
    private static final int XML = 3;
    private static final int JSP = 4;
    private static final int PO = 11;
    private static final int JAVA = 12;
    private static final int MSG = 13;
    private static final int PROPERTIES = 14;
    private static final int PLAINTEXT = 15;
    private static final int DTD = 16;
    private static final int STAROFFICE = 17;
    
    private java.util.Map validFormatsHash;

    /** Creates a new instance of FormatExtractorFactory */
    public FormatElementExtractorFactory() {
        validFormatsHash = new java.util.HashMap();
        validFormatsHash.put("", new Integer(UNKNOWN));
        validFormatsHash.put("SGML", new Integer(SGML));
        validFormatsHash.put("HTML", new Integer(HTML));
        validFormatsHash.put("XML", new Integer(XML));
        validFormatsHash.put("JSP", new Integer(JSP));

        validFormatsHash.put("PO", new Integer(PO));
        validFormatsHash.put("JAVA", new Integer(JAVA));
        validFormatsHash.put("MSG", new Integer(MSG));
        validFormatsHash.put("PROPERTIES", new Integer(PROPERTIES));
        validFormatsHash.put("DTD", new Integer(DTD));

        validFormatsHash.put("PLAINTEXT", new Integer(PLAINTEXT));
        
        validFormatsHash.put("STAROFFICE", new Integer(STAROFFICE));
        
    }

    public FormatElementExtractor createFormatExtractor(String type, GlobalVariableManager gvm) {
        Integer iType = (Integer)validFormatsHash.get(type);

        if (iType == null) { //  guard clause
            iType = UNKNOWN;
            logger.warning("Unknown type for original file: " + type);
        }

        SegmenterTable table = null;
        FormatElementExtractor extractor = null;

        switch (iType.intValue()) {
        case SGML:

            //  temporary measure
            table = null; //  new BasicSegmenterTable();

            //  end
            table = new org.jvnet.olt.filters.sgml.docbook.DocbookSegmenterTable();
            extractor = new SgmlFormatElementExtractor(gvm, table);

            return extractor;

        case HTML:
            table = new org.jvnet.olt.filters.html.HtmlSegmenterTable();
            extractor = new SgmlFormatElementExtractor(gvm, table);

            return extractor;

        case STAROFFICE:
        case XML:

            //  Rely on basic defaults for the time being.
            table = new org.jvnet.olt.parsers.tagged.DefaultSegmenterTable();
            extractor = new SgmlFormatElementExtractor(gvm, table);

            return extractor;
            
        case JSP:
            table = new org.jvnet.olt.filters.jsp.JspSegmenterTable();
            extractor = new SgmlFormatElementExtractor(gvm, table);

            return extractor;

        case PLAINTEXT:
        case PO:
        case JAVA:
        case MSG:
        case PROPERTIES:
        case DTD:
            extractor = new SwMsgElementExtractor(gvm);

            return extractor;

        default:
            extractor = new SwMsgElementExtractor(gvm);
            return extractor;
        }
    }
}

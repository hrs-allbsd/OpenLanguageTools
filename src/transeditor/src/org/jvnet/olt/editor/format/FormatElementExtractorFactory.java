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

import org.jvnet.olt.editor.format.sgml.SgmlFormatElementExtractor;
import org.jvnet.olt.editor.format.swmsg.SwMsgElementExtractor;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.tagged.SegmenterTable;


/**
 * This class implements the factory pattern to build FormatExtractor objects.
 * @author  jc73554
 */
public class FormatElementExtractorFactory {
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
    private java.util.Map validFormatsHash;

    /** Creates a new instance of FormatExtractorFactory */
    public FormatElementExtractorFactory() {
        validFormatsHash = new java.util.HashMap();
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
        
        validFormatsHash.put("STAROFFICE", new Integer(XML));
        
    }

    public FormatElementExtractor createFormatExtractor(String type, GlobalVariableManager gvm) throws InvalidFormatTypeException {
        Integer iType = (Integer)validFormatsHash.get(type);

        if (iType == null) { //  guard clause
            throw new InvalidFormatTypeException("The format type, '" + type + "', is not a supported format.");
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
            throw new InvalidFormatTypeException("The format type, '" + type + "', is not a supported format.");
        }
    }
}

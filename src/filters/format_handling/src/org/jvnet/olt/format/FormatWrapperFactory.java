
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * FormatWrapperFactory.java
 *
 * Created on 10 February 2004, 11:53
 */

package org.jvnet.olt.format;

import java.util.Map;
import java.util.HashMap;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.filters.jsp.JspSegmenterTable;
import org.jvnet.olt.filters.html.HtmlSegmenterTable;
import org.jvnet.olt.filters.sgml.docbook.DocbookSegmenterTable;
import org.jvnet.olt.filters.sgml.docbook.DocbookTagTable;
import org.jvnet.olt.format.plaintext.PlainTextFormatWrapper;
import org.jvnet.olt.format.sgml.SgmlFormatWrapper;
import org.jvnet.olt.format.xml.XmlFormatWrapper;
import org.jvnet.olt.format.messageformat.MessageFormatWrapper;
import org.jvnet.olt.format.brokensgml.BrokenSgmlFormatWrapper;
import org.jvnet.olt.format.printf.PrintfFormatWrapper;

/**
 *
 * @author  jc73554
 */
public class FormatWrapperFactory {
    
    private static final int XML = 1;
    private static final int HTML = 2;
    private static final int SGML = 3;
    private static final int PLAINTEXT = 4;
    private static final int PO = 5;
    private static final int MSG = 6;
    private static final int PROPERTIES = 7;
    private static final int JAVA = 8;
    private static final int JSP = 9;
    private static final int DTD = 10;
    
    private Map m_hashAvailableWrappers;
    
    /** Holds a reference to a segmenter table. This member variable can be null */
    private SegmenterTable segmenterTable;
    
    /** Holds a reference to a tag table. This member variable can be null */
    private TagTable tagTable;
    
    /** Creates a new instance of FormatWrapperFactory */
    public FormatWrapperFactory() {
        //  Build the a hash map of available format wrapper types.
        m_hashAvailableWrappers = new HashMap();
        m_hashAvailableWrappers.put("HTML", new Integer(HTML));
        m_hashAvailableWrappers.put("SGML", new Integer(SGML));
        m_hashAvailableWrappers.put("XML", new Integer(XML));
        m_hashAvailableWrappers.put("PLAINTEXT", new Integer(PLAINTEXT));
        m_hashAvailableWrappers.put("MSG",new Integer(MSG));
        m_hashAvailableWrappers.put("PO", new Integer(PO));
        m_hashAvailableWrappers.put("JAVA", new Integer(JAVA));
        m_hashAvailableWrappers.put("PROPERTIES",new Integer(PROPERTIES));
        m_hashAvailableWrappers.put("JSP", new Integer(JSP));
	m_hashAvailableWrappers.put("DTD", new Integer(DTD));
    }
    
    //    protected FormatWrapper getFormatWrapper(String type, GlobalVariableManager gvm) throws UnsupportedFormatException {
    //
    //    }
    
    public FormatWrapper createFormatWrapper(String type, GlobalVariableManager gvm) throws UnsupportedFormatException {
        //  The code for this is borrowed from the
        FormatWrapper ex;
        
        SegmenterTable localSegmenterTable = null;
        TagTable localTagTable = null;
        
        if(m_hashAvailableWrappers.containsKey(type)) {
            Integer code = (Integer) m_hashAvailableWrappers.get(type);
            int iExtract = code.intValue();
            switch(iExtract) {
                case JSP:
                    localSegmenterTable = new org.jvnet.olt.filters.jsp.JspSegmenterTable();
                    localTagTable = new org.jvnet.olt.filters.jsp.JspTagTable();
                    ex = new BrokenSgmlFormatWrapper(localTagTable, localSegmenterTable);
                    return ex;
                case HTML:
                    if (localTagTable == null && localSegmenterTable == null){
                        localSegmenterTable = new org.jvnet.olt.filters.html.HtmlSegmenterTable();
                        localTagTable = new org.jvnet.olt.filters.html.HtmlTagTable();
                    }
                    ex = new SgmlFormatWrapper(gvm, localTagTable, localSegmenterTable);
                    
                    return ex;  //  No drop through                   
                case SGML:
                    if (localTagTable == null && localSegmenterTable == null){
                        localSegmenterTable = new DocbookSegmenterTable();
                        localTagTable = new DocbookTagTable();
                    }
                    ex = new SgmlFormatWrapper(gvm, localTagTable, localSegmenterTable);
                    
                    return ex;   //  No drop through                  
                case XML:
                    // If we're getting html or sgml, we already know the tag tables,
                    // so we use them. For xml, if we're wrapping the original source
                    // text, we also have the correct tag tables passed in with the
                    // constructor. Finally, if we're not doing source text, then
                    // whatever xml came from the database match is unknown -
                    // so we don't bother trying to cleverly wrap formatting
                    // and we just use the default sgml format wrapping behaviour
                    // (tagname insensitive)
                    //
                    if (localTagTable != null && localSegmenterTable != null){
                        //System.out.println("Using format wrapper with "+localTagTable+" and "+localSegmenterTable);
                        ex = new XmlFormatWrapper(gvm, localTagTable, localSegmenterTable);
                        //   }  else if (sourceText){
                        //      ex = new SgmlFormatWrapper(gvm, tagTable, segmenterTable);
                    }  else {
                        ex = new XmlFormatWrapper(gvm);
                    }
                    return ex;                    
                case PLAINTEXT:
                    ex = new PlainTextFormatWrapper();
                    return ex;
                case MSG:
                case PO:
                    ex = new PrintfFormatWrapper();
                    return ex;
                case PROPERTIES:
		case DTD:
                case JAVA:
                    ex = new MessageFormatWrapper();
                    return ex;
                    
                default:
                    throw new UnsupportedFormatException("Format is unsupported : " + type);
            }
        }
        else {
            throw new UnsupportedFormatException("Format is unsupported : " + type);
        }
    }
    
}

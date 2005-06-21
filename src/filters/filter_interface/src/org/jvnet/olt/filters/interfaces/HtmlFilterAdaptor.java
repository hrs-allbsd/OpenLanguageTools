
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * HtmlFilterAdaptor.java
 *
 * Created on June 30, 2003, 3:17 PM
 */

package org.jvnet.olt.filters.interfaces;
import org.jvnet.olt.filters.html.*;
import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.sgml.EntityManager;

/**
 * 
 * @author  timf
 */
public class HtmlFilterAdaptor implements Filter {
    
    //String dataType = "HTML"; not used at the moment
    String language = "en-US";
    String sourceFileName = "";
    org.jvnet.olt.parsers.tagged.TagTable table = new HtmlTagTable();
    org.jvnet.olt.parsers.tagged.SegmenterTable segmenterTable = new HtmlSegmenterTable();
    SgmlFilter filter;
    
    /** Creates a new instance of HtmlFilterAdaptor
     * @param a reader to the html file that should be parsed     *
     */
    public HtmlFilterAdaptor(java.io.Reader reader) {
        GlobalVariableManager gvm = new EntityManager();
        this.filter = new SgmlFilter(reader, gvm);
    }
    
    
    public org.jvnet.olt.alignment.Segment[] getSegments(String language) {
        return this.filter.parseForSgmlAligmnent(language, table, segmenterTable);        
    }
    
}

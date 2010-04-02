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

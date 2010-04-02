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
 * SgmlFilterAdaptor.java
 *
 * Created on June 30, 2003, 3:16 PM
 */
package org.jvnet.olt.filters.interfaces;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.filters.sgml.docbook.*;
import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.book.*;
import org.jvnet.olt.parsers.tagged.SegmenterTable;

/**
 *
 * @author  timf
 */
public class SgmlFilterAdaptor implements Filter {
    // String dataType = "SGML"; not using this at the moment
    String sourceFileName = "";
    org.jvnet.olt.parsers.tagged.TagTable table = new DocbookTagTable();
    SegmenterTable segmenterTable = new DocbookSegmenterTable();
    SgmlFilter filter;
    
    /** Creates a new instance of SgmlFilterAdaptor 
        @param reader a reader to the sgml file that should be parsed
     *  @param bookFileReader a reader to the book file that should be parsed
        
     */
    public SgmlFilterAdaptor(java.io.Reader reader, java.io.Reader bookFileReader) {
         // the null here is the directory the bookfile is in : we should probably
         // have this as a constructor, since it's used to resolve other sgml files
         // with the book, so we can check for new ENTITYs being declared.
         //Book book = new Book(bookFileReader, "", "", null);
         //GlobalVariableManager gvm = book.getGlobalVariableManager();
         // putting this in so that changes I make to Book don't affect this class
         // temporarily
         GlobalVariableManager gvm = null;
         this.filter = new SgmlFilter(reader, gvm);
    }
    
    public org.jvnet.olt.alignment.Segment[] getSegments(String language) {
        return this.filter.parseForSgmlAligmnent(language, table, segmenterTable);
    }
    
}
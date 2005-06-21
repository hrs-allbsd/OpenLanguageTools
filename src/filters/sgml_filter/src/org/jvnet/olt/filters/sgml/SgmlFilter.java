
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SgmlFilter.java
 *
 * Created on July 4, 2002, 5:10 PM
 */

package org.jvnet.olt.filters.sgml;

import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.tagged.*;

// WRONG!
/*
import org.jvnet.olt.filters.sgml.visitors.*;
import org.jvnet.olt.filters.sgml.docbook.*;

import org.jvnet.olt.format.sgml.*;
import org.jvnet.olt.format.brokensgml.*;
import org.jvnet.olt.format.plaintext.*;
import org.jvnet.olt.format.printf.*;
import org.jvnet.olt.format.messageformat.*;

 */
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.*;

/**
 * This is dead code - it shouldn't be run anymore, but has instead been replaced
 * by the code in the different filters. Not all of the functions here have been
 * implemented yet however.
 * @deprecated This code is not used any more - instead use methods in XML filter, JSP filter, SGML Filter, etc.
 *
 */
public class SgmlFilter {
    
    private Reader reader;
    private GlobalVariableManager gvm;
    
    public SgmlFilter(java.io.Reader reader, GlobalVariableManager gvm) {
        this.reader = reader;
        
        System.out.println("NOOOP - why are you trying to run this code ??? ");
        if (gvm == null){
            System.out.println("WARNING ! : SgmlFilter being created with null gvm !");
            System.out.println("            -- creating new GlobalVariableManager instead.");
            this.gvm = new org.jvnet.olt.format.sgml.EntityManager();
            
        } else {
            this.gvm = gvm;
        }
    }
    
    
    
    
    public void parseForReverse(String dataType, String language, String targLang,  String sourceFileName, Writer xliffWriter,
    Writer sklWriter, org.jvnet.olt.parsers.tagged.TagTable table, SegmenterTable segmenterTable, boolean writeReverse) throws SgmlFilterException {
        System.out.println("NOOP !!");
        
    }
    
    public void parseForXmlReverse(String language, String targLang,  String sourceFileName,
    Writer xliffWriter, Writer sklWriter, org.jvnet.olt.parsers.tagged.TagTable table, SegmenterTable segmenterTable, boolean writeReverse) throws SgmlFilterException {
        System.out.println("NOOP !!");
        
    }

    public Segment[] parseForSgmlAligmnent(String language, 
    org.jvnet.olt.parsers.tagged.TagTable table, SegmenterTable segmenterTable) throws SgmlFilterException {
        Segment[] result = null;
        System.out.println("NOOOP!!!");
        return result;
    }
    
    
    public void parseJspForDemo(String language, org.jvnet.olt.parsers.tagged.TagTable table, 
    org.jvnet.olt.parsers.tagged.SegmenterTable segmenterTable) throws org.jvnet.olt.filters.sgml.SgmlFilterException {
        System.out.println("NOOOP!!!");
    }
    
    public void parseJspForReverse(String dataType, String language, String targLang,  String sourceFileName, Writer xliffWriter,
    Writer sklWriter, org.jvnet.olt.parsers.tagged.TagTable table, SegmenterTable segmenterTable, boolean writeReverse) throws SgmlFilterException {
        
        System.out.println("NOOP!!");
    }
    
    public List parseXmlForDemo(String language, org.jvnet.olt.parsers.tagged.TagTable table, SegmenterTable segmenterTable ) throws org.jvnet.olt.filters.sgml.SgmlFilterException {
        List segments = new ArrayList();
        System.out.println("NOOOOOPPPP!!!");
        return segments;
    }

}

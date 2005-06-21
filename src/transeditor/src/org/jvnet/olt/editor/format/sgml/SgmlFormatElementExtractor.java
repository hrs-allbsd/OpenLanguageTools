/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SgmlFormatElementExtractor.java
 *
 * Created on 06 January 2004, 16:57
 */
package org.jvnet.olt.editor.format.sgml;

import java.io.StringReader;

import org.jvnet.olt.editor.format.FormatElementExtractor;
import org.jvnet.olt.editor.model.PivotBaseElement;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.laxparsers.sgml.LaxSgmlDocFragmentParser;
import org.jvnet.olt.laxparsers.sgml.ParseException;
import org.jvnet.olt.parsers.tagged.SegmenterTable;


/**
 *
 * @author  jc73554
 */
public class SgmlFormatElementExtractor implements FormatElementExtractor {
    private GlobalVariableManager gvm;
    private SegmenterTable table;

    /** Creates a new instance of SgmlFormatElementExtractor */
    public SgmlFormatElementExtractor(GlobalVariableManager gvm, SegmenterTable table) {
        this.gvm = gvm;
        this.table = table;
    }

    public PivotBaseElement[] extractBaseElements(String text) {
        StringReader reader = new StringReader(text);

        //  Create parser
        LaxSgmlDocFragmentParser parser = new LaxSgmlDocFragmentParser(reader);

        //  Parse input text
        try {
            parser.parse();
        } catch (ParseException parseEx) {
            //  Do stuff here
            parseEx.printStackTrace();
        }

        //  Create a visitor
        SgmlBaseElementExtractorVisitor visitor = new SgmlBaseElementExtractorVisitor(gvm, table);

        try {
            parser.walkParseTree(visitor, null);
        } catch (Exception ex) {
            //  Do stuff here 
            ex.printStackTrace();
        }

        return visitor.getBaseElements();
    }

    public String extractText(String formattedText) {
        StringReader reader = new StringReader(formattedText);

        //  Create parser
        LaxSgmlDocFragmentParser parser = new LaxSgmlDocFragmentParser(reader);

        //  Parse input text
        try {
            parser.parse();
        } catch (ParseException parseEx) {
            //  Do stuff here
            parseEx.printStackTrace();
        }

        //  Create a visitor
        SgmlPcdataExtractorVisitor visitor = new SgmlPcdataExtractorVisitor(gvm);

        try {
            parser.walkParseTree(visitor, null);
        } catch (Exception ex) {
            //  Do stuff here            
            ex.printStackTrace();
        }

        return visitor.getPcdata();
    }

    public String markupFormatForDisplay(String formattedText) {
        StringReader reader = new StringReader(formattedText);

        //  Create parser
        LaxSgmlDocFragmentParser parser = new LaxSgmlDocFragmentParser(reader);

        //  Parse input text
        try {
            parser.parse();
        } catch (ParseException parseEx) {
            //  Do stuff here
            parseEx.printStackTrace();
        }

        //  Create a visitor
        return "";
    }

    public void setVariableManager(org.jvnet.olt.format.GlobalVariableManager gvm) {
        this.gvm = gvm;
    }
}

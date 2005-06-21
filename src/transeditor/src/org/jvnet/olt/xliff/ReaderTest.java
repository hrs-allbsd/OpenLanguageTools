/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * ReaderTest.java
 *
 * Created on February 25, 2005, 12:46 PM
 */
package org.jvnet.olt.xliff;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;

import junit.framework.TestCase;

import org.jvnet.olt.editor.util.TestSupport;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 *
 * @author boris
 */
public class ReaderTest extends TestCase {
    /** Creates a new instance of ReaderTest */
    public ReaderTest() {
    }

    public void testReadin() throws Exception {
        InputSource src = new InputSource(TestSupport.getResource("xliff/test1.xlf"));

        Reader r = new Reader(src);

        assertNotNull(r.getTransUnits());
        assertFalse(r.getTransUnits().isEmpty());

        Map m = r.getTransUnits();

        Map.Entry entry = (Map.Entry)m.entrySet().iterator().next();
        TransUnitId id = (TransUnitId)entry.getKey();
        TransUnit tu = (TransUnit)entry.getValue();

        assertNotNull(tu);
        assertEquals(id.getStrId(), "a1");

        XLIFFSentence s1 = tu.getSource();
        XLIFFSentence s2 = (XLIFFSentence)r.getGroupZeroSource().get("a1");

        assertSame(s1, s2);

        /* TODO The SingleMatch  we get does not have getSourceSegment and getTargetSegment
         * methods, so I can not compare right now.
         */
        /*
        AltTransUnit altTr = (AltTransUnit)tu.getAltTransUnits().iterator().next();
        XLIFFBasicSentence as1 = altTr.getSource();


        XLIFFBasicSentence xs1 =(XLIFFBasicSentence)  ( ((List)r.getGroupAltTrans().get("a1")) );

        assertSame(as1,xs1);
        //parser().parse(src,r);
        */
    }

    SAXParser parser() throws SAXException {
        return null; //SAXParserFactory.newInstance().newSAXParser();
    }
}

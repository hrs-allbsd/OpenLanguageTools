/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SaxReaderTest.java
 *
 * Created on April 19, 2005, 8:36 PM
 *
 */
package org.jvnet.olt.xliff;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jvnet.olt.editor.model.Match;
import org.jvnet.olt.editor.model.MatchAttributes;
import org.jvnet.olt.util.TestSupport;
import org.jvnet.olt.xliff.reader.handlers.Context;


/**
 *
 * @author boris
 */
public class SAXReaderTest extends TestCase {
    Context ctx;

    /** Creates a new instance of SaxReaderTest */
    public SAXReaderTest() {
    }

    protected void setUp() throws Exception {
        InputStream is = TestSupport.getResource("saxreader/testParseSimpleFile.xlf");

        SAXReader sxr = new SAXReader(Version.XLIFF_1_0);
        ctx = sxr.parse(new InputStreamReader(is));
    }

    public void testHeader() throws Exception {
        assertEquals("en-US", ctx.getSourceLanguage());
        assertEquals("fr-FR", ctx.getTargetLanguage());
        assertEquals("PLAINTEXT", ctx.getOriginalDataType());
        assertEquals("editor", ctx.getToolName());
        assertEquals("phase", ctx.getPhaseName());
        assertEquals("process", ctx.getProcessName());

        TrackingComments tc = ctx.getCommentsTrack();
        assertEquals("HeaderComment", tc.getComment("header"));
    }

    public void testGroups() {
        TrackingGroup grp = ctx.getGroupTrack();

        List l = grp.getListOfOneGroup("922");

        assertEquals(1, l.size());
    }

    public void testSource() {
        assertEquals(1, ctx.getSize());

        Map m = ctx.getGroupZeroSource();
        assertTrue((m != null) && !m.isEmpty());
        assertEquals(1, m.size());

        Map.Entry entry = (Map.Entry)m.entrySet().iterator().next();
        String key = (String)entry.getKey();
        XLIFFSentence src = (XLIFFSentence)entry.getValue();

        assertEquals("922", key);

        checkSentence(src, "922", "SomeText", "en-US", null);
    }

    public void testTarget() {
        Map m = ctx.getGroupZeroTarget();
        assertTrue((m != null) && !m.isEmpty());
        assertEquals(1, m.size());

        Map.Entry entry = (Map.Entry)m.entrySet().iterator().next();
        String key = (String)entry.getKey();
        XLIFFSentence tgt = (XLIFFSentence)entry.getValue();

        checkSentence(tgt, "922", "SomeTranslation", "fr-FR", "100-Match:translated");
    }

    public void testCommentsTrack() {
        //check comments
        TrackingComments tc = ctx.getCommentsTrack();

        //one comment is a header comment
        assertEquals(2, tc.getCommentsMap().size());
        assertEquals("Note", tc.getComment("922"));
    }

    public void testSourceContext() {
        TrackingSourceContext sctx = ctx.getSourceContextTrack();
        Map xctx = sctx.getContext("922");
        TrackingSourceContext.SourceContextKey xkey = new TrackingSourceContext.SourceContextKey("message id", "record");
        assertEquals("Comment", xctx.get(xkey));
    }

    public void testAltTrans() {
        Map m = ctx.getGroupAltTrans();
        assertTrue((m != null) && !m.isEmpty());
        assertEquals(1, m.size());

        List l = (List)m.get("922");

        assertNotNull(l);
        assertEquals(1, l.size());

        Match mch = (Match)l.get(0);
        assertNotNull(mch);
    }

    public void testMatch() {
        Match mch = fetchMatch();

        assertEquals(100, mch.getMatchQuality());
        assertEquals("1:1", mch.getMatchStyle());

        MatchAttributes ma = mch.getMatchAttributes();
        System.out.println("ma:'" + ma + "'");
        assertEquals("Project ID: 8637; Module: StarOffice 6.1; Subject: OS and Apps - Documentation; ", ma.toString());

        //assertEquals("",ma.getAttributesList()
    }

    private Match fetchMatch() {
        Map m = ctx.getGroupAltTrans();
        List l = (List)m.get("922");

        return (Match)l.get(0);
    }

    private void checkSentence(XLIFFSentence src, String id, String text, String xmlLang, String state) {
        assertNotNull(src);
        assertEquals(text, src.getSentence());
        assertEquals(id, src.getTransUnitId());
        assertEquals(xmlLang, src.getXMLLang());
        assertEquals(state, src.getTranslationState());
    }
}

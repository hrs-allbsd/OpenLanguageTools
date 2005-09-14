/*
 * TMXImporterTest.java
 *
 * Created on September 12, 2005, 5:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.minitm;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.jvnet.olt.editor.util.FileUtils;
import org.jvnet.olt.editor.util.TestSupport;

/**
 *
 * @author boris
 */
public class TMXImporterTest extends TestCase {
    
    /** Creates a new instance of TMXImporterTest */
    public TMXImporterTest() {
    }
    
    //High level view if the test passes at all...
    public void testSanity() throws Exception{
        File mtmFile = TestSupport.makeFile("tmximport","1.mtm");
        File tmxFile = TestSupport.getResourceAsFile("tmximport/1.tmx");
        
        TMXImporter imp = new TMXImporter();
        imp.setSrcLang("en-US");
        imp.setTgtLang("zh-CN");
        imp.setSrcLangShort("US");
        imp.setTgtLangShort("ZH");
        imp.setTranslatorId("abcdef");
        
        imp.convertTMX2MTM(tmxFile,mtmFile);
        
        assertTrue(TestSupport.findLine(mtmFile, "<minitm name=\"temp\" srclang=\"US\" tgtlang=\"ZH\">", true));
        assertTrue(TestSupport.findLine(mtmFile, "<source>ABCDEF</source>", true));
        assertTrue(TestSupport.findLine(mtmFile, "<translation>012345</translation>", true));
        assertTrue(TestSupport.findLine(mtmFile, "<translatorId>abcdef</translatorId>", true));
    }
    
}

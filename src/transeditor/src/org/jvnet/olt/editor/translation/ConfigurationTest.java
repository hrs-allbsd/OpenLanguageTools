///*
// * Copyright  2005 Sun Microsystems, Inc.
// * All rights reserved Use is subject to license terms.
// *
// */
///*
// * ConfigurationTest.java
// *
// * Created on March 24, 2005, 11:45 AM
// */
//TODO: remove or move to test
//package org.jvnet.olt.editor.translation;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//import junit.framework.TestCase;
//
//import org.jvnet.olt.util.TestSupport;
//
//
///**
// *
// * @author boris
// */
//public class ConfigurationTest extends TestCase {
//    File miniTmDir;
//
//    /** Creates a new instance of ConfigurationTest */
//    public ConfigurationTest() {
//    }
//
//    protected void setUp() throws Exception {
//        miniTmDir = TestSupport.makeFile("configuration/mini-tm", null);
//
//        File x = TestSupport.makeFile("configuration/mini-tm", "abcdef_US_FR.MTM");
//        writeTo(x);
//        x = TestSupport.makeFile("configuration/mini-tm", "abc_US_JA.MTM");
//        writeTo(x);
//    }
//
//    private void writeTo(File f) throws IOException {
//        FileWriter fw = null;
//
//        try {
//            fw = new FileWriter(f);
//            fw.write("abc");
//        } finally {
//            if (fw != null) {
//                fw.close();
//            }
//        }
//    }
//
//    public void testLoadSave() throws Exception {
//        File f = TestSupport.getResourceAsFile("configuration/testConfigTMEditor_1.INI");
//        File newCfg = TestSupport.makeFile("configuration", "testConfigTMEditor_1.INI");
//        TestSupport.copyFile(f, newCfg);
//
//        fail("Fix me: I've changed from INIFile to Preferences !!!");
//
//        //        Configuration cfg = new Configuration(null,newCfg,(String)null,(String)null,miniTmDir);
//        //fg.load();
//        //cfg.save();
//        //cfg.save();
//        TestSupport.compareFiles(f, newCfg);
//    }
//
//    public void testSimplePropertyChange() throws Exception {
//        File f = TestSupport.getResourceAsFile("configuration/testConfigTMEditor_1.INI");
//        File newCfg = TestSupport.makeFile("configuration", "testConfigTMEditor_2.INI");
//        TestSupport.copyFile(f, newCfg);
//
//        fail("Fix me: I've changed from INIFile to Preferences !!!");
//
//        //        Configuration cfg = new Configuration((File)null,newCfg,(String)null,(String)null,miniTmDir);
//        //        cfg.load();
//        //        String oldTransId = cfg.getTranslatorID();
//        //        cfg.setTranslatorID("ABCDEF");
//        //        cfg.save();
//        //        cfg.load();
//        //        assertEquals("ABCDEF",cfg.getTranslatorID());
//        //        assertFalse("ABCDEF".equals(oldTransId));
//        //        assertTrue(TestSupport.findLine(newCfg,"TransID=ABCDEF", true));
//    }
//}

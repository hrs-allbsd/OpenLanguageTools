///*
// * SchemaLocatorTest.java
// *
// * Created on June 27, 2005, 3:10 PM
// *
// * To change this template, choose Tools | Options and locate the template under
// * the Source Creation and Management node. Right-click the template and choose
// * Open. You can then make changes to the template in the Source Editor.
// */
//TODO: remove or move to test
//package org.jvnet.olt.xliff.handlers;
//
//import junit.framework.TestCase;
//
///**
// *
// * @author boris
// */
//public class SchemaLocatorTest extends TestCase{
//
//    /** Creates a new instance of SchemaLocatorTest */
//    public SchemaLocatorTest() {
//    }
//
//    public void testLookupByPublic(){
//        SchemaLocator loc = new SchemaLocator("PUBLIC",null);
//
//        SchemaLocator tst1 = new SchemaLocator("PUBLIC",null);
//        SchemaLocator tst2 = new SchemaLocator("PUBLIC","SYSTEM");
//        SchemaLocator tst3 = new SchemaLocator(null,"SYSTEM");
//
//        assertEquals(tst1,loc);
//        assertFalse(tst2.equals(loc));
//        assertFalse(tst3.equals(loc));
//
//
//    }
//
//    public void testLookupBySystem(){
//        SchemaLocator loc = new SchemaLocator("PUBLIC","SYSTEM");
//
//        SchemaLocator tst1 = new SchemaLocator("PUBLIC",null);
//        SchemaLocator tst2 = new SchemaLocator("PUBLIC","SYSTEM");
//        SchemaLocator tst3 = new SchemaLocator(null,"SYSTEM");
//
//        assertFalse(tst1.equals(loc));
//        assertEquals(tst2,loc);
//        assertFalse(tst3.equals(loc));
//
//
//    }
//}

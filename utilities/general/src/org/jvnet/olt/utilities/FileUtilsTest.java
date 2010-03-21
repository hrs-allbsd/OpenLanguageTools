///*
// * FileUtilsTest.java
// *
// * Created on September 1, 2005, 5:33 PM
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//TODO: remove or move to TEST
//package org.jvnet.olt.utilities;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.FileWriter;
//import junit.framework.TestCase;
//
//
//
///**
// *
// * @author boris
// */
//public class FileUtilsTest extends TestCase{
//    File f;
//
//    /** Creates a new instance of FileUtilsTest */
//    public FileUtilsTest() {
//
//    }
//
//    protected void setUp() throws Exception {
//        f = File.createTempFile("xxx","txt");
//        f.deleteOnExit();
//    }
//
//    protected void tearDown() throws Exception {
//        if(f != null)
//            f.delete();
//    }
//
//    public void testUniqueFile() throws IOException{
//        File n = FileUtils.ensureUniqueFile(f);
//
//        assertNotSame(n,f);
//
//        assertEquals(n.getParentFile(),f.getParentFile());
//        assertEquals(f.getAbsolutePath()+".1",n.getAbsolutePath());
//    }
//
//    public void testTwoFiles() throws IOException{
//        File n = FileUtils.ensureUniqueFile(f);
//
//		FileWriter fw = null;
//		try{
//        	fw = new FileWriter(n);
//			fw.write("abc");
//			fw.flush();
//		}
//		finally{
//			if(fw != null)
//				fw.close();
//		}
//        File n2 = FileUtils.ensureUniqueFile(f);
//
//        System.out.println("n:"+n);
//        System.out.println("n2:"+n2);
//
//        assertNotSame(n,f);
//        assertNotSame(n,n2);
//
//        assertEquals(n2.getParentFile(),f.getParentFile());
//        assertEquals(f.getAbsolutePath()+".2",n2.getAbsolutePath());
//    }
//
//    public void testStripExtension() throws Exception{
//        File f = new File("a.sgm.xlz");
//
//        File n = FileUtils.stripExtension(f,".xlz");
//
//        System.out.println("n:"+n);
//
//        assertEquals("a.sgm",n.getName());
//
//    }
//
//    public void testStripExtensionWhenNoExtension() throws Exception{
//        File f = new File("/a/b/a.xlz.xlz");
//
//        System.out.println("Path:"+new File("../x/y/").getPath());
//        System.out.println("name:"+f.getName());
//
//        File n = FileUtils.stripExtension(f, ".xlz");
//
//        assertEquals("/a/b/a.xlz",n.getAbsolutePath());
//
//        f = new File("/dir/here/.xlz.abc");
//        n = FileUtils.stripExtension(f, ".xlz");
//        assertEquals("/dir/here/.xlz.abc",n.getAbsolutePath());
//
//        f = new File(".xlz");
//        n = FileUtils.stripExtension(f, ".xlz");
//        assertEquals(".xlz",n.getPath());
//    }
//
//
//}
//

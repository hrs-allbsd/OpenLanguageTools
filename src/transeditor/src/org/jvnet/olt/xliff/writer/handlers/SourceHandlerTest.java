///*
// * SourceHandlerTest.java
// *
// * Created on August 2, 2005, 3:09 PM
// *
// * To change this template, choose Tools | Options and locate the template under
// * the Source Creation and Management node. Right-click the template and choose
// * Open. You can then make changes to the template in the Source Editor.
// */
//TODO: remove or move to test
//package org.jvnet.olt.xliff.writer.handlers;
//
//import java.io.StringWriter;
//import java.util.HashMap;
//import java.util.Map;
//import junit.framework.TestCase;
//import org.jvnet.olt.xliff.Version;
//import org.jvnet.olt.xliff.XLIFFSentence;
//import org.jvnet.olt.xliff.XMLDumper;
//import org.jvnet.olt.xliff.handlers.Element;
//import org.xml.sax.helpers.AttributesImpl;
//
///**
// *
// * @author boris
// */
//public class SourceHandlerTest extends TestCase{
//    Context ctx;
//
//    StringWriter resultWriter;
//
//    Element ELEM_SRC ;
//    Element ELEM_BPT ;
//    Element ELEM_EPT ;
//
//    Map changeSet;
//
//    /** Creates a new instance of SourceHandlerTest */
//    public SourceHandlerTest() {
//    }
//
//    public void setUp() {
//        resultWriter = new StringWriter();
//        XMLDumper dumper = new XMLDumper(resultWriter);
//
//        ctx = new Context(dumper,Version.XLIFF_1_1);
//
//        ELEM_SRC = new Element(null,"source","source",new AttributesImpl(),"/x");
//        ELEM_BPT = new Element(null,"bpt","bpt",new AttributesImpl(),"/x/y/");
//        ELEM_EPT = new Element(null,"ept","ept",new AttributesImpl(),"/x/y/");
//
//        changeSet = new HashMap();
//        changeSet.put("a5",new XLIFFSentence("<bpt id='1'>&lt;b&gt;</bpt>Text<ept id='1'>&lt;/B&gt;</ept>","en-US","a5"));
//
//    }
//
//    public void testSimpleSource() throws Exception{
//
//        ctx.setSrcChangeSet(changeSet);
//        ctx.setCurrentTransId("a4");
//
//        SourceHandler sh = new SourceHandler(ctx);
//
//        sh.dispatch(ELEM_SRC,true);
//        String text="this is a stupid text";
//        sh.dispatchChars(ELEM_SRC,text.toCharArray(),0,text.length());
//        sh.dispatch(ELEM_SRC,false);
//
//        String result = resultWriter.toString();
//        assertEquals("<source>this is a stupid text</source>",result);
//    }
//
//    public void testSourceMixedContent() throws Exception{
//        Map m = new HashMap();
//        m.put("a5",new XLIFFSentence("<bpt id='1'>&lt;b&gt;</bpt>Text<ept id='1'>&lt;/B&gt;</ept>","en-US","a5"));
//
//        ctx.setSrcChangeSet(changeSet);
//        ctx.setCurrentTransId("a4");
//
//        SourceHandler sh = new SourceHandler(ctx);
//
//        sh.dispatch(ELEM_SRC,true);
//        String text="this is a stupid text";
//        sh.dispatchChars(ELEM_SRC,text.toCharArray(),0,text.length());
//
//        sh.dispatch(ELEM_BPT,true);
//        String mixed1 = "in mixed";
//        sh.dispatchChars(ELEM_SRC,mixed1.toCharArray(),0,mixed1.length());
//        sh.dispatch(ELEM_BPT,false);
//
//        String text3 ="text3";
//        sh.dispatchChars(ELEM_SRC,text3.toCharArray(),0,text3.length());
//
//        sh.dispatch(ELEM_EPT,true);
//        String mixed2 = "in mixed2";
//        sh.dispatchChars(ELEM_SRC,mixed2.toCharArray(),0,mixed2.length());
//        sh.dispatch(ELEM_EPT,false);
//
//        sh.dispatch(ELEM_SRC,false);
//
//        String result = resultWriter.toString();
//
//        System.out.println("Result:"+result);
//
//        assertEquals("<source>this is a stupid text<bpt>in mixed</bpt>text3<ept>in mixed2</ept></source>",result);
//    }
//
//    public void testSimpleReplaceWithSentence() throws Exception{
//
//        ctx.setSrcChangeSet(changeSet);
//        ctx.setCurrentTransId("a5");
//
//        SourceHandler sh = new SourceHandler(ctx);
//
//        sh.dispatch(ELEM_SRC,true);
//        String text="this is a stupid text";
//        sh.dispatchChars(ELEM_SRC,text.toCharArray(),0,text.length());
//        sh.dispatch(ELEM_SRC,false);
//
//        String result = resultWriter.toString();
//
//        System.out.println("Result:"+result);
//
//        assertEquals("<source><bpt id='1'>&lt;b&gt;</bpt>Text<ept id='1'>&lt;/B&gt;</ept></source>",result);
//
//    }
//
//    public void testReplaceWithMixed() throws Exception{
//        Map m = new HashMap();
//        m.put("a5",new XLIFFSentence("<bpt id='1'>&lt;b&gt;</bpt>Text<ept id='1'>&lt;/B&gt;</ept>","en-US","a5"));
//
//        ctx.setSrcChangeSet(changeSet);
//        ctx.setCurrentTransId("a5");
//
//        SourceHandler sh = new SourceHandler(ctx);
//        sh.dispatch(ELEM_SRC,true);
//        String text="this is a stupid text";
//        sh.dispatchChars(ELEM_SRC,text.toCharArray(),0,text.length());
//
//        sh.dispatch(ELEM_BPT,true);
//        String mixed1 = "in mixed";
//        sh.dispatchChars(ELEM_SRC,mixed1.toCharArray(),0,mixed1.length());
//        sh.dispatch(ELEM_BPT,false);
//
//        String text3 ="text3";
//        sh.dispatchChars(ELEM_SRC,text3.toCharArray(),0,text3.length());
//
//        sh.dispatch(ELEM_EPT,true);
//        String mixed2 = "in mixed2";
//        sh.dispatchChars(ELEM_SRC,mixed2.toCharArray(),0,mixed2.length());
//        sh.dispatch(ELEM_EPT,false);
//
//        sh.dispatch(ELEM_SRC,false);
//
//        String result = resultWriter.toString();
//
//        System.out.println("Result:"+result);
//
//        assertEquals("<source><bpt id='1'>&lt;b&gt;</bpt>Text<ept id='1'>&lt;/B&gt;</ept></source>",result);
//
//    }
//
//}

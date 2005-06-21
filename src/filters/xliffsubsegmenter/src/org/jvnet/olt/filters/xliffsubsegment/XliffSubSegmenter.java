
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffSubSegmenter.java
 *
 * Created on September 29, 2004, 12:30 PM
 */
package org.jvnet.olt.filters.xliffsubsegment;

import org.jvnet.olt.filters.segmenter_facade.SegmenterFacade;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.sax.SAXSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import org.jvnet.olt.filters.xliffsubsegment.xliff.Body;
import org.jvnet.olt.filters.xliffsubsegment.xliff.File;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Group;
import org.jvnet.olt.filters.xliffsubsegment.xliff.ContextGroup;
import org.jvnet.olt.filters.xliffsubsegment.xliff.CountGroup;import org.jvnet.olt.filters.xliffsubsegment.xliff.Count;
import org.jvnet.olt.filters.xliffsubsegment.xliff.ObjectFactory;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Source;
import org.jvnet.olt.filters.xliffsubsegment.xliff.TransUnit;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Xliff;

// sorry, we shouldn't be depending on this, but...
import org.jvnet.olt.filters.sgml.SgmlFilterHelper;

/**
 * This class is designed to "sub-segment" an XLIFF stream.
 * It takes each trans-unit element, and segments the source into multiple
 * sentences, one trans-unit per sentence, wrapping them in a group, containing
 * those multiple trans-unit elements.
 * 
 * We use it when presented with XLIFF files containing
 * paragraphs that we want to look up against our TM system
 *
 * It goes without saying, that this will only work for *monolingual*
 * XLIFF files - we don't make any attempt to split trans-units that
 * contain both source and target elements (which would need more
 * complex alignment techniques in order for this to work) will fail.
 * @author timf
 */
public class XliffSubSegmenter {
    
    private static JAXBContext context = null;
    private static ObjectFactory factory = null;
    // a field to allow us to name trans-units sequentially
    private int transUnitId=1;
    private Xliff x = null;
    
    private ContextGroup currentContext;
    private static String xliffDocType = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
    "<!DOCTYPE xliff PUBLIC \"-//XLIFF//DTD XLIFF//EN\" \"http://www.oasis-open.org/committees/xliff/documents/xliff.dtd\" >\n";
    
    
    private static void init() throws JAXBException {
        // JAXBContext are not thread safe, so we can only have a single Context.
        if (context == null){
            context = JAXBContext.newInstance(XliffSubSegmenterHelper.XLIFFCONTEXT);
        }
        if (factory == null){
            factory = new ObjectFactory();
        }
        
        if (!XliffSubSegmenterHelper.hasXliffDTD()){
            String xliffLocation = null;
            try {
                Context initial = new InitialContext();
                xliffLocation = (String) initial.lookup("java:comp/env/string/XLIFFDTD");
            } catch (javax.naming.NamingException e){
                throw new JAXBException("Unable to lookup XLIFF DTD");
            }
            XliffSubSegmenterHelper.setXliffDTD(xliffLocation);
        }
        
    }
    
    public void retrieveXliff(Writer writer) throws XliffSubSegmenterException{
        try {
            // marshal it back out to disk
            // jumping through hoops to get the DTD inserted
            Marshaller m = context.createMarshaller();
            m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty( "com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
            StringWriter strWriter = new StringWriter();
            m.marshal(x, strWriter);
            
            String output = strWriter.toString();
            // This is a hack, I can't work out how to get JAXB to
            // write out either a sane XML declaration, nor a <!DOCTYPE
            output = xliffDocType+output;
            StringReader reader = new StringReader(output);
            int i=0;
            while ((i=reader.read()) != -1){
                writer.write(i);
            }
        } catch ( javax.xml.bind.JAXBException e){
            throw new XliffSubSegmenterException("Problem writing XLIFF : "+e.getMessage(),e);
        } catch ( java.io.IOException e){
            throw new XliffSubSegmenterException("IO Problem writing XLIFF : "+e.getMessage(),e);
        }
    }
    
    /**
     * Creates a new instance of XliffSubSegmenter
     * @param fileName The full path to the file you wish to sub-segment
     */
    public XliffSubSegmenter(String fileName) throws XliffSubSegmenterException {
        try {
            XliffSubSegmenter.init();
            
            this.x = XliffSubSegmenterHelper.readFile(fileName);
            List files = x.getFile();
            Iterator it = files.iterator();
            while (it.hasNext()){
                File xf = (File)it.next();
                //System.out.println("Tool that produced this was "+xf.getOriginal());
                xf.setDatatype("PLAINTEXT");
                Body body = xf.getBody();
                List content = body.getContent();
                for (int i=0; i<content.size(); i++){
                    Object o = (Object)content.get(i);
                    if (o instanceof TransUnit){
                        boolean addedContext = false;
                        TransUnit transUnit = (TransUnit)o;
                        // remove it from the list and wrap it with a group
                        content.remove(i);
                        Group newGroup = this.factory.createGroup();
                        newGroup.setExtradata("Sun XLIFF Sub Segmenter");
                        // just out of curiousity, we can set that group to be the same
                        // resType and transUnit id to the one we're wrapping
                        newGroup.setId(transUnit.getId());
                        newGroup.setRestype(transUnit.getRestype());
                        List groupContent = newGroup.getContent();
                        segmentTransUnit(transUnit, groupContent);
                        if (currentContext != null && !addedContext){
                            // we are only allowed to add one context here.
                            groupContent.add(0,currentContext);
                            addedContext = true;
                        }
                        // add the segmented transUnits to the list
                        content.add(i, newGroup);
                    }
                    // clear our context again
                    this.currentContext = null;
                }
            }
            
            //context.createValidator().validate((Object)x);
            this.x = x;
        } catch (JAXBException e){
            e.printStackTrace();
            throw new XliffSubSegmenterException("Error trying to subsegment : "+e.getMessage(),e);
            //System.exit(1);
        } catch (XliffSubSegmenterException e){
            e.printStackTrace();
            throw e;
            //System.exit(1);
        }
        
        
    }
    
    /**
     * Main entry point
     * @param args command line arguments
     */
    public static void main(String[] args){
        try {
            if (args.length != 2){
                System.out.println("Usage : XliffSubSegmenter <source file> <xliff dtd>");
                System.exit(1);
            }
            XliffSubSegmenterHelper.setXliffDTD(args[1]);
            XliffSubSegmenter test = new XliffSubSegmenter(args[0]);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(args[0]+".new"));
            test.retrieveXliff(writer);
            writer.flush();
        } catch (XliffSubSegmenterException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
    
    
    private List segmentTransUnit(TransUnit transUnit, List list){
        
        try {
            List content = transUnit.getContent();
            Iterator contentIt = content.iterator();
            String seg = "";
            
            while (contentIt.hasNext()){
                Object o = contentIt.next();
                if (o instanceof ContextGroup){
                    //System.out.println("Hit Context Group !!");
                    this.currentContext = (ContextGroup)o;
                }
                else if (o instanceof Source){
                    
                    Source s = (Source)o;
                    Marshaller m = context.createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
                    m.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
                    StringWriter writer = new StringWriter();
                    m.marshal(s, writer);
                    seg = writer.toString();
                    // TODO - there should be a better way
                    // to have the marshaller not write the xml declaration
                    // this will do for now.
                    seg = seg.trim();
                    seg = seg.replaceAll("<source xml:lang=\"..-..\">","");
                    seg = seg.replaceAll("<source>","");                    
                    seg = seg.replaceAll("</source>","");
                    StringReader reader = new StringReader(seg);
                    
                    //System.out.println("Block is _"+seg+"_");
                    List segments = new ArrayList();
                    Map formatting = new HashMap();
                    if (seg.equals(" ")){
                        segments.add(" ");
                    }
                    else {
                        // assuming en-US segmentation
                        SegmenterFacade facade = new SegmenterFacade(reader, "en-US");
                        try {
                            facade.parse();
                        } catch (java.lang.Exception e){
                            throw new XliffSubSegmenterException("Exception trying to segment "+seg+" :"+e.getMessage(),e);
                        }
                        segments = facade.getSegments();
                        formatting = facade.getFormatting();
                    }
                    
                    Iterator it = segments.iterator();
                    int i=1;
                    while (it.hasNext()){
                        String format = (String)formatting.get(new Integer(i++));
                        String segment = (String)it.next();
                        if (format == null){
                            format="";
                        }
                        int wordCount = -1;
                        try {
                            wordCount = SgmlFilterHelper.wordCount(segment, "en-US");
                        } catch (Throwable t){
                            System.out.println("Unable to wordcount "+segment);
                            t.printStackTrace();
                        }
                        segment = "<source>"+segment+"</source>";
                        Source source = XliffSubSegmenterHelper.readSource(segment);
                        TransUnit newUnit = this.factory.createTransUnit();
                        
                        newUnit.setId(Integer.toString(this.transUnitId++));
                        XliffSubSegmenterHelper.copyTransUnitAttrs(transUnit, newUnit);
                        newUnit.getContent().add(source);
                        
                        // now add the wordcount for that segment
                        CountGroup countG = factory.createCountGroup();
                        countG.setName("word count");
                        Count count = factory.createCount();
                        count.setCountType("word-count");
                        count.setUnit("word");
                        count.setContent(new Integer(wordCount).toString());
                        countG.getCount().add(count);
                        newUnit.getContent().add(countG);
                        
                        list.add(newUnit);
                        // now add any mid-segment formatting there might be
                        // (spaces between sentences)
                        if (format.length() != 0){
                            newUnit = this.factory.createTransUnit();
                            newUnit.setId(Integer.toString(this.transUnitId++));
                            XliffSubSegmenterHelper.copyTransUnitAttrs(transUnit, newUnit);
                            source = this.factory.createSource();
                            source.getContent().add(format);
                            newUnit.getContent().add(source);
                            list.add(newUnit);
                        }
                    }
                }
            }
            
            
        } catch (XliffSubSegmenterException e){
            System.out.println("Exception trying to get stuff from TransUnit "+e.getMessage());
        } catch (javax.xml.bind.JAXBException e){
            System.out.println("JAXBException trying to get stuff from TransUnit "+e.getMessage());
        }
        return list;
    }
}
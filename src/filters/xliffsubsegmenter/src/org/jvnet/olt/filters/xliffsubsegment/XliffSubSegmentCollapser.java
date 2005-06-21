
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


import org.jvnet.olt.filters.xliffsubsegment.xliff.AltTrans;
import org.jvnet.olt.filters.xliffsubsegment.xliff.ContextGroup;
import org.jvnet.olt.filters.xliffsubsegment.xliff.CountGroup;
import java.io.IOException;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import java.util.Map;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.sax.SAXSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


import org.jvnet.olt.filters.xliffsubsegment.xliff.Body;
import org.jvnet.olt.filters.xliffsubsegment.xliff.File;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Group;
import org.jvnet.olt.filters.xliffsubsegment.xliff.ObjectFactory;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Source;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Target;
import org.jvnet.olt.filters.xliffsubsegment.xliff.TransUnit;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Xliff;

/**
 * This class is designed to collapse an XLIFF file that has been "sub-segmented"
 * by the XliffSubSegmenter.
 * It takes each group of trans-units and collapses them back into a single
 * trans-unit. Typically, this converts back from 1 sentence per trans-unit to
 * 1 paragraph per trans-unit.
 *
 * It goes without saying, that this will only work for *monolingual*
 * XLIFF files - we don't make any attempt to combine trans-units that
 * contain both source and target elements (which would need more
 * complex alignment techniques in order for this to work) will fail
 * @author timf
 */
public class XliffSubSegmentCollapser  {
    
    private static JAXBContext context = null;
    private static ObjectFactory factory = null;
    
    private Xliff x = null;
    private static String xliffDocType = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
    "<!DOCTYPE xliff PUBLIC \"-//XLIFF//DTD XLIFF//EN\" \"http://www.oasis-open.org/committees/xliff/documents/xliff.dtd\" >";
    
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
    public XliffSubSegmentCollapser(String fileName, String dataType) throws XliffSubSegmenterException {
        try {
            XliffSubSegmentCollapser.init();
            
            this.x = XliffSubSegmenterHelper.readFile(fileName);
            List files = x.getFile();
            Iterator it = files.iterator();
            while (it.hasNext()){
                File xf = (File)it.next();
                xf.setDatatype(dataType);
                // System.out.println("Tool that produced this was "+xf.getOriginal());
                Body body = xf.getBody();
                List bodyContent = body.getContent();
                for (int i=0; i<bodyContent.size(); i++){
                    Object o = (Object)bodyContent.get(i);
                    if (o instanceof Group){
                        Group group = (Group)o;
                        String extraData = group.getExtradata();
                        if (extraData != null && extraData.equals("Sun XLIFF Sub Segmenter")){
                            // remove this group
                            bodyContent.remove(i);
                            i--; // the list is shorter now, so we need to respect that
                            collapseGroup(group, bodyContent);
                        }
                    }
                    
                }
            }
            // validate it. Again, the same procedure regardless of the schema language
            context.createValidator().validate((Object)x);
            this.x = x;
        } catch (JAXBException e){
            throw new XliffSubSegmenterException("Problem collapsing XLIFF document : "+e.getMessage(),e);
        }
    }
    
    private void collapseGroup(Group group, List bodyContent) throws javax.xml.bind.JAXBException, XliffSubSegmenterException{
        
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        StringWriter writer = new StringWriter();
        
        // create a single transunit with the id set to the group,
        // containing a single source and single target element
        TransUnit newUnit = factory.createTransUnit();
        newUnit.setXmlSpace("preserve");
        Source resultSource = factory.createSource();
        Target resultTarget = factory.createTarget();
        List otherContent = new ArrayList();
        // we need to collapse the tm units in this group into
        // one large TM unit
        
        List transUnits = group.getContent();
        for (int i=0; i<transUnits.size();i++){
            Object o = transUnits.get(i);
            
            if (o instanceof TransUnit){
                TransUnit trans = (TransUnit)o;
                String resType = trans.getRestype();
                if (resType == null){
                    newUnit.setRestype("res");
                } else {
                    newUnit.setRestype(resType);
                }
                List transContent = trans.getContent();
                Iterator it = transContent.iterator();
                while (it.hasNext()){
                    o = it.next();
                    if (o instanceof Source){
                        Source source = (Source)o;
                        XliffSubSegmenterHelper.copySourceAttrs(source, resultSource);
                        List sContent = source.getContent();
                        Iterator sIterator = sContent.iterator();
                        while (sIterator.hasNext()){
                            Object content = sIterator.next();
                            //System.err.println(content);
                            if (!content.equals(" ") || transUnits.size() ==1){ // whitespace handling of JAXB is suspicious
                                //System.err.println("Adding _"+content+"_");
                                resultSource.getContent().add(content);
                            }
                        }
                    } else if (o instanceof Target){
                        Target target = (Target)o;
                        XliffSubSegmenterHelper.copyTargetAttrs(target, resultTarget);
                        List tContent = target.getContent();
                        Iterator tIterator = tContent.iterator();
                        while (tIterator.hasNext()){
                            Object content = tIterator.next();
                            if (!content.equals(" ") || transUnits.size() ==1){ // whitespace handling of JAXB is suspicious
                                System.err.println("Adding Target _"+content+"_");
                                resultTarget.getContent().add(content);
                            }
                        }
                    } else {
                        //All other content except alt trans and count-groups will be added
                        if (o instanceof AltTrans || o instanceof CountGroup){
                            ;// noop - we don't want to add these
                        } else {
                            otherContent.add(o);
                        }
                    }
                }
            }
        }
        // now add our finised source, target and other stuff to the list.
        newUnit.setId(group.getId());
        newUnit.getContent().add(resultSource);
        newUnit.getContent().add(resultTarget);
        newUnit.getContent().addAll(otherContent);
        bodyContent.add(newUnit);
    }
    
    
    /**
     * Main entry point
     * @param args command line arguments
     */
    public static void main(String[] args){
        if (args.length != 2){
            System.out.println("Usage : XliffSubSegmentCollapser <source file> <xliff dtd>");
            System.exit(1);
        }
        try {
            XliffSubSegmenterHelper.setXliffDTD(args[1]);
            XliffSubSegmentCollapser test = new XliffSubSegmentCollapser(args[0], "STAROFFICE");
            OutputStreamWriter writer = new OutputStreamWriter(System.out);
            test.retrieveXliff(writer);
            writer.flush();
        } catch (XliffSubSegmenterException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

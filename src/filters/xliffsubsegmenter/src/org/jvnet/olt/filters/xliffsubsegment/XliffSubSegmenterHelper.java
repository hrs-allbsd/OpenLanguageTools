
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffSubSegmenterHelper.java
 *
 * Created on October 4, 2004, 1:21 PM
 */

package org.jvnet.olt.filters.xliffsubsegment;

import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import java.util.Map;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.sax.SAXSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


import org.jvnet.olt.filters.xliffsubsegment.xliff.ObjectFactory;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Source;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Target;
import org.jvnet.olt.filters.xliffsubsegment.xliff.TransUnit;
import org.jvnet.olt.filters.xliffsubsegment.xliff.Xliff;

/**
 * This class contains protected helper methods that are used by both
 * the XliffSubSegmenter and the XliffSubSegmentCollapser.
 * @author timf
 */
public class XliffSubSegmenterHelper implements EntityResolver{
    
    private static JAXBContext context = null;
    private static ObjectFactory factory = null;
    private static EntityResolver resolver = null;
    public static String XLIFFCONTEXT = "org.jvnet.olt.filters.xliffsubsegment.xliff";
    public static String xliffDTD = null;
    
    /** Creates a new instance of XliffSubSegmenterHelper */
    public XliffSubSegmenterHelper() {
    }
    
    public static void setXliffDTD(String xliffDTDLocation){
        xliffDTD = xliffDTDLocation;
    }
    
    public static boolean hasXliffDTD(){
        if (xliffDTD == null){
            return false;
        } else return true;
    }
    
    public static String getXliffDTD(){
        return xliffDTD;
    }
    
    /**
     * Initialises the XliffSubSegmentHelper
     * @throws XliffSubSegmenterException if there was some problem initialising the helper
     */
    protected static void init() throws XliffSubSegmenterException {
        try {
            // JAXBContext are not thread safe, so we can only have a single Context.
            if (context == null){
                context = JAXBContext.newInstance(XLIFFCONTEXT);
            }
            if (factory == null){
                factory = new ObjectFactory();
            }
            if (resolver == null){
                resolver = new XliffSubSegmenterHelper();
            }
            
        } catch (javax.xml.bind.JAXBException e){
            throw new XliffSubSegmenterException("JAXB Exception reading XLIFF : "+e.getMessage(), e);
        }
    }
    
    /**
     * A helper method to parse a String containing an XLIFF element and
     * return an object representing that String content.
     * @param xliffReader A reader containing the XLIFF Fragment to be parsed
     * @throws XliffSubSegmenterException if there was some problem reading the xliff content
     * @return an Object representing that XLIFF fragment. It is expected that
     * clients will cast this object to the appropriate type elsewhere
     * in the code.
     */
    protected static Object readXliffFragment(Reader xliffReader) throws XliffSubSegmenterException {
        Object result = null;
        try {
            init();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setValidating(false);
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setEntityResolver(resolver);
            InputSource is = new InputSource(xliffReader);
            
            SAXSource source =
            new SAXSource( xmlReader, is);
            
            Unmarshaller u = context.createUnmarshaller();
            u.setValidating( false );
            result = u.unmarshal( source );
            
        } catch (org.xml.sax.SAXException e){
            throw new XliffSubSegmenterException("XML Parse problem : "+e.getMessage(), e);
        } catch (javax.xml.bind.JAXBException e){
            throw new XliffSubSegmenterException("JAXB Exception reading XLIFF : "+e.getMessage(), e);
        } catch (javax.xml.parsers.ParserConfigurationException e){
            throw new XliffSubSegmenterException("Parser configuration error while reading XLIFF "+e.getMessage(),e);
        }
        return result;
    }
    
    /**
     * Parse an individual <source> XLIFF element, returning a Source object.
     * @return a Source object representing the input file
     * @param sourceContent the string containing the source element
     * @throws XliffSubSegmenterException if there was some problem reading the xliff string
     */
    protected static Source readSource(String sourceContent) throws XliffSubSegmenterException {
        Source s = null;
        try {
            StringReader reader = new StringReader(sourceContent);
            s = (Source)XliffSubSegmenterHelper.readXliffFragment(reader);
        } catch (XliffSubSegmenterException e){
            System.out.println(sourceContent+" was the problematic string");
            throw e;
        }
        return s;
    }
    
    /**
     * Parse an individual <target> XLIFF element returning a Target object.
     * @return an Object representing the input file
     * @param targetContent the string containing the target element
     * @throws XliffSubSegmenterException If there was some problem reading the source xliff string
     */
    protected static Target readTarget(String targetContent) throws XliffSubSegmenterException {
        Target t = null;
        try {
            StringReader reader = new StringReader(targetContent);
            t = (Target)XliffSubSegmenterHelper.readXliffFragment(reader);
        } catch (XliffSubSegmenterException e){
            System.out.println(targetContent+" was the problematic string");
            throw e;
        }
        return t;
    }
    
    /**
     * This method reads an XLIFF file from the given filename
     * @param filename the filename containing the file to be read
     * @throws XliffSubSegmenterException if there was some problem, either reading the file, or parsing it's contents
     * @return An XLIFF object containing the contents of the file
     */
    protected static Xliff readFile(String filename) throws XliffSubSegmenterException {
        Xliff x = null;
        try {
            FileReader reader = new FileReader(filename);
            x = (Xliff)XliffSubSegmenterHelper.readXliffFragment(reader);
        } catch (java.io.FileNotFoundException e){
            throw new XliffSubSegmenterException("Problem reading file "+filename+": "+e.getMessage(),e);
        }
        return x;
    }
    
    
    /**
     * Really simple method - simply duplicates the attributes from source
     * TransUnit to a target TransUnit
     * @param source the source transunit, from which we copy attributes
     * @param target the target trans-unit, to which we copy attributes
     */
    protected static void copyTransUnitAttrs(TransUnit source, TransUnit target){
        
        target.setTranslate(source.getTranslate());
        target.setXmlSpace(source.getXmlSpace());
        target.setReformat(source.getReformat());
        target.setApproved(source.getApproved());
        target.setCharclass(source.getCharclass());
        target.setCoord(source.getCoord());
        target.setCssStyle(source.getCssStyle());
        target.setDatatype(source.getDatatype());
        target.setExstyle(source.getExstyle());
        target.setExtradata(source.getExtradata());
        target.setExtype(source.getExtype());
        target.setFont(source.getFont());
        target.setHelpId(source.getHelpId());
        target.setMaxbytes(source.getMaxbytes());
        target.setMaxheight(source.getMaxheight());
        target.setMaxwidth(source.getMaxwidth());
        target.setMenu(source.getMenu());
        target.setMenuName(source.getMenuName());
        target.setMenuOption(source.getMenuOption());
        target.setMinbytes(source.getMinbytes());
        target.setMinheight(source.getMinheight());
        target.setMinwidth(source.getMinwidth());
        target.setPhaseName(source.getPhaseName());
        target.setResname(source.getResname());
        target.setRestype(source.getRestype());
        target.setSizeUnit(source.getSizeUnit());
        target.setStyle(source.getStyle());
        target.setTs(source.getTs());
    }
    
    /**
     * This duplicates attributes from one Source object to another Source object
     * @param source the object from which we copy attributes
     * @param target the object to which we copy attributes
     */
    protected static void copySourceAttrs(Source source, Source target){
        target.setTs(source.getTs());
        target.setXmlLang(source.getXmlLang());
        //
    }
    
    /**
     * Duplicates the attributes from a Target object to another Target object
     * @param source the source object, from which we copy the attributes
     * @param target the target object, to which we copy the attributes
     */
    protected static void copyTargetAttrs(Target source, Target target){
        target.setCoord(source.getCoord());
        target.setCssStyle(source.getCssStyle());
        target.setExstyle(source.getExstyle());
        target.setFont(source.getFont());
        target.setPhaseName(source.getPhaseName());
        target.setResname(source.getResname());
        target.setRestype(source.getRestype());
        target.setStyle(source.getStyle());
        target.setState(source.getState());
        target.setTs(source.getTs());

    }
    
    /**
     * A resolve entity method that just resolves to a given dtd
     * @param str
     * @param str1
     * @throws SAXException
     * @throws IOException
     * @return
     */
    public InputSource resolveEntity(String str, String str1) throws SAXException, java.io.IOException {
        //System.out.println("Being asked to resolve "+str+", "+str1);
        return new InputSource(xliffDTD);
    }
}

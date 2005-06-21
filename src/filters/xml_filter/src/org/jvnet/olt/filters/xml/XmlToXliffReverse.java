
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */


package org.jvnet.olt.filters.xml;

import org.jvnet.olt.filters.xml.xmlconfig.*;
import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.utilities.*;
import org.jvnet.olt.format.sgml.*;
import org.xml.sax.InputSource;
import org.jvnet.olt.filters.sgml.SgmlFilter;
import org.jvnet.olt.filters.sgml.SgmlFilterException;
import org.jvnet.olt.format.GlobalVariableManager;

import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.TagTable;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.logging.*;
import javax.naming.*;

/**
 * This is copied from XmlToXliff - simply take target lang as well
 * and call a different method in the sgml filter.
 * @author  timf 
 */
public class XmlToXliffReverse {
    
    private String guid = null;
    private GlobalVariableManager gvm = null;
    private static String dummyDTDFile = "";
    private XmlIdentifier xmlIdent = null;
    
    /**
     * This code converts from Xml to xliff - it's really a static frontend
     * to the xmlparser and hides the actual writing of xliff and skl files
     * from the user
     */
    public XmlToXliffReverse(String directory, String filename, String language, String target, String encoding, Logger logger, String dummyDTDFile) throws XmlParseException, IOException{
        this.guid = null;
        this.dummyDTDFile = dummyDTDFile;
        this.convert(directory,filename,language, target, encoding,logger, null);
    }
    
    /**
     * A constructor that uses the same arguments as above, but also
     * allows us to specify an xml identifier
     */
    public XmlToXliffReverse(String directory, String filename, String language, String target, String encoding, Logger logger, String dummyDTDFile, XmlIdentifier id) throws XmlParseException, IOException {
        this.guid = null;
        this.dummyDTDFile = dummyDTDFile;
        this.convert(directory,filename,language, target, encoding,logger, id);
    }
    
    /*
     * This now takes an xml identifier. If this is null, the system tries
     * to determine the type of XML document presented to the system, otherwise
     * it uses the given xml identifier
     */
    public void convert(String directory, String filename,
    String language, String target,  String encoding,
    Logger logger, XmlIdentifier id ) throws XmlParseException, IOException{
        this.xmlIdent = null;
        XmlIdentifier xmlIdentifier = id;
        if (id == null){
            xmlIdentifier = this.getXmlIdentifier(filename, logger);
        }
        System.out.println("xmlIdentifier is " + xmlIdentifier);
        TagTable xmlTagTable = null;
        SegmenterTable xmlSegmenterTable = null;
        // save a copy of this
        this.xmlIdent = xmlIdentifier;
        
        Collection configCollection = XmlConfigManager.getXmlConfigCollection(xmlIdentifier, logger);
        Iterator configIterator = configCollection.iterator();
        
        if(xmlIdentifier.getNamespaceList().isEmpty()) {
            if (configIterator.hasNext()){
                XmlConfig xmlConfig = (XmlConfig) configIterator.next();
                try {
                    logger.log(Level.CONFIG, "Using xml filter configuration " + XmlConfigManager.getXmlConfigName(xmlConfig));
                } catch (XmlConfigNamingException e){
                    logger.log(Level.CONFIG,"Naming exception trying to get real name for xml config object",e);
                }
                try {
                    xmlTagTable = new XmlTagTable(xmlConfig);
                    xmlSegmenterTable = new XmlSegmenterTable(xmlConfig);
                } catch (XmlFilterException e){
                    logger.log(Level.CONFIG, "Null xml config used for non-namespace xml configuration",e);
                    xmlTagTable = null;
                    xmlSegmenterTable = null;
                }
            }
        } else {
            HashMap configMap = new HashMap();
            
            while(configIterator.hasNext()) {
                XmlConfig xmlConfig = (XmlConfig) configIterator.next();
                try {
                    logger.log(Level.CONFIG, "Using xml filter configuration " + XmlConfigManager.getXmlConfigName(xmlConfig));
                } catch (XmlConfigNamingException e){
                    logger.log(Level.CONFIG,"Naming exception trying to get real name for xml config object",e);
                }
                if(xmlConfig !=null && !xmlConfig.getNamespace().equals("")) {
                    configMap.put(xmlConfig.getNamespace(), xmlConfig);
                }
            }
            try {
                xmlTagTable = new XmlTagTable(configMap);
                xmlSegmenterTable = new XmlSegmenterTable(configMap);
            } catch (XmlFilterException e){
                logger.log(Level.CONFIG, "Null xml config map used for namespaced xml configuration",e);
                xmlTagTable = null;
                xmlSegmenterTable = null;
            }
        }
        // finally, check to see if we've got stuff from the above,
        // otherwise, make appropriate noises
        if (xmlTagTable == null || xmlSegmenterTable == null){
            throw new XmlParseException("No XML Filter configuration files were found on the "+
            "server for this type of XML.\n Please submit configuration files for this XML file type.");
            // we used to just issue a warning, and use default behaviour, but this
            // wasn't sufficient (files were not getting sent to manual processing)
            //xmlTagTable = new DefaultXmlTagTable();
            //xmlSegmenterTable = new DefaultXmlSegmenterTable();
        }
        
        File file = new File(filename);
        
        //String shortname = filename.substring(directory.length(),filename.length());
        String shortname = file.getName();
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        Reader reader;
        try {
            reader = new InputStreamReader(inStream, encoding);
        } catch (java.io.UnsupportedEncodingException e){
            throw new XmlParseException("Problem trying to read xml file in encoding "+ encoding);
        }
        
        //  Parse the input file to get xliff files array
        if (gvm == null){
            gvm = new EntityManager();
        }
        SgmlFilter parser = new SgmlFilter(reader, gvm);
        
        BufferedOutputStream xliffOutputStream = new BufferedOutputStream(new FileOutputStream(filename+".xlf"));
        BufferedOutputStream sklOutputStream = new BufferedOutputStream(new FileOutputStream(filename+".skl"));
        
        Writer xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
        Writer sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
        
        
        parser.parseForXmlReverse(language, target,  shortname,
        xliffWriter, sklWriter, xmlTagTable, xmlSegmenterTable, true);
        
        XliffZipFileIO xlz = new XliffZipFileIO(new File(filename+".xlz"));
        InputStreamReader sklreader = new InputStreamReader(new FileInputStream(filename+".skl"),"UTF-8");
        InputStreamReader xliffreader = new InputStreamReader(new FileInputStream(filename+".xlf"),"UTF-8");
        
        Writer sklwriter = xlz.getSklWriter();
        int c;
        while ((c=sklreader.read()) != -1){
            sklwriter.write(c);
        }
        
        
        Writer xliffwriter = xlz.getXliffWriter();
        while ((c=xliffreader.read()) != -1){
            xliffwriter.write(c);
        }
        
        if(guid != null) {
            java.util.Properties prop = new java.util.Properties();
            prop.setProperty("guid",guid);
            xlz.setWorkflowProperties(prop);
        }
        
        xlz.writeZipFile();
        
        // now delete those temporary files :
        File xliff = new File(filename+".xlf");
        xliff.delete();
        File skeleton = new File(filename+".skl");
        skeleton.delete();
        
        
    }
    
    
    
    
    /**
     * A separate constructor used to set the guid
     */
    public XmlToXliffReverse(String directory, String filename, String language, String target, String encoding, Logger logger, String guid, String dummyDTDFile) throws XmlParseException, IOException{
        this.guid = guid;
        this.dummyDTDFile = dummyDTDFile;
        this.convert(directory,filename,language,target,encoding,logger,null);
        
    }
    
    /**
     * This returns the last used xml identfier - used so that we can save this
     * for tools further down the processing chain. (In particular, the TM tool
     * needs to know what sort of xml file it's got in order to write xliff
     * correctly (escaping the correct tags in <mrk> sections) Otherwise, it'll
     * use the default behaviour of leaving all pcdata un-protected
     * (for non-translatable inline tags, this is important)
     *
     * @returns the last xml identifier used by this converter.
     */
    public XmlIdentifier getXmlIdentifier() {
        return this.xmlIdent;
        
    }
    
    private XmlIdentifier getXmlIdentifier(String filename, Logger logger) throws XmlParseException {
        File dummy = new File(dummyDTDFile);
        if (!dummy.exists()){
            throw new XmlParseException("Dummy DTD file " + filename+" does not exist - contact SunTrans2 support staff");
        }
        
        InputSource inputSource = new InputSource(filename);
        
        XmlIdentifier xmlIdentifier = new XmlIdentifier();
        
        XmlLookupHandler xmlLookupHandler = new XmlLookupHandlerImpl(xmlIdentifier);
        
        XmlLookupParser xmlLookupParser =
        new XmlLookupParser(xmlLookupHandler, null, dummyDTDFile);
        
        try {
            xmlLookupParser.parse(inputSource);
        } catch(org.xml.sax.SAXException  ex) {
            throw new XmlParseException("Failed to parse the Xml File due to a SaxException \n" + ex.getMessage());
        } catch(javax.xml.parsers.ParserConfigurationException ex) {
            throw new XmlParseException("Failed to parse the Xml File due to a ParserConfigurationException \n" + ex.getMessage());
        } catch(java.io.IOException ex) {
            throw new XmlParseException("Failed to parse the Xml File due to a IOException \n" + ex.getMessage());
        }
        
        logger.log(Level.INFO,"XML config should match " + xmlIdentifier.toString());
        
        return xmlIdentifier;
        
    }
    
    public static String getDummyDTDFile() throws NamingException {
        
        if(dummyDTDFile.equals("")) {
            
            Context initial = new InitialContext();
            XmlToXliffReverse.dummyDTDFile = (String)
            initial.lookup("java:comp/env/string/DUMMYDTDFILE");
        }
        
        return dummyDTDFile;
        
    }
    
    
    
    /** This class takes in a language and a xml file, and outputs a .zip archive containing
     *  an xliff file and it's associated skeleton file.
     *
     * @param argv  Two arguments needed in here : the input file and language name
     */
    public static void main(String[] argv) {
        try {
            
            if (argv.length != 6){
                System.out.println("Usage : XmlToXliff <xml-config-dtd> <xml-config-store> <input file> <language> <targetlang> <dummy-dtd-location>\n"+
                "where language is currently en-US, de-DE, es-ES, fr-FR, it-IT, sv-SE\n"+
                " [note: all languages have asian character support.]");
                System.exit(0);
            }
            
            XmlConfigManager.init(argv[0], argv[1]);
            
            String encoding = System.getProperty("file.encoding");
            //System.out.println ("creating reader in " + encoding +" encoding.");
            //  Open the input file.
            Logger logger = Logger.getLogger("com.sun.tt.filters.testlogger");
            XmlToXliffReverse task = new XmlToXliffReverse("", argv[2], argv[3], argv[4], System.getProperty("file.encoding"), logger, argv[5]);
        } catch (Exception e){
            System.err.println(e.getMessage());
            
        }
    }
    
    
    
    
}

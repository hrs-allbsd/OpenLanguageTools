
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffFilterFacade.java
 *
 * Created on May 20, 2005, 10:26 AM
 *
 */

package org.jvnet.olt.filters.gui;

import java.text.MessageFormat;
import org.jvnet.olt.filters.xml.xmlconfig.ProcessXmlConfigException;
import org.jvnet.olt.filters.xml.xmlconfig.XmlConfigMain;
import java.util.*;
import java.util.logging.*;
import java.io.File;
import java.io.*;
import java.util.regex.*;

import org.jvnet.olt.utilities.StandaloneLocaleTable;
import org.jvnet.olt.filters.html.*;
import org.jvnet.olt.filters.jsp.*;
import org.jvnet.olt.filters.plaintext.*;
import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.ooo.*;
import org.jvnet.olt.filters.book.*;
import org.jvnet.olt.filters.software.*;
// fixme : this should be moved
import org.jvnet.olt.tmci.TMCParseException;

import org.jvnet.olt.filters.xml.*;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.filters.xml.xmlconfig.XmlConfigManager;
import org.jvnet.olt.filters.xml.xmlconfig.XmlIdentifier;
import org.jvnet.olt.filters.soxliff.*;


/**
 * This code acts as a simple facade to the rest of the XLIFF filters - nothing
 * new in here, but it'll serve a purpose. It uses the filename extension of the
 * input file to choose which converter to invoke. If the extension is unknown,
 * it'll try to use the XML filter by default.
 *
 * Perhaps this should reside in a different package to the gui code, but for
 * now, since there isn't really a general package for filter code, this will do
 * but it's a possible candidate for refactoring in the future.
 *
 * This code grew out of some of the server-side TM code which has not yet been
 * open sourced, but it makes sense to have something like this in with the rest
 * of the filters, so I'm doing a bit of copy/paste coding here - sorry...
 *
 */
public class XliffFilterFacade {
    
    public static final int UNDEFINED = 0;
    public static final int HTML = 1;
    public static final int TEXT = 2;
    public static final int XLZ = 3;
    public static final int SGML = 4;
    public static final int BOOK = 5;
    public static final int XML = 6;
    public static final int JSP = 7;
    
    public static final int PO = 8;
    public static final int JAVA = 9;
    public static final int PROPERTIES = 10;
    public static final int MSG = 11;
    public static final int FRAME = 12;
    public static final int DTD = 13;
    public static final int OPENOFFICE = 14;
    public static final int STAROFFICE_XLIFF = 15;
    
    
    private static Map typeMap;
    private static StandaloneLocaleTable standaloneLocaleTable = new StandaloneLocaleTable();

    private static final java.util.ResourceBundle xliffFilterGUIMessages = java.util.ResourceBundle.getBundle("org/jvnet/olt/filters/gui/XliffFilterGUIMessages");

    
    /**
     * Creates an instance of an XliffFilterFacade. We're taking a Map of attributes
     * as a paramter here to allow us to pass different options to the different
     * filters. This loose coupling is a bit sloppy, and could probably be cleaned
     * up - making sure all needed attributes are present at compile-time isn't
     * possible this way, but it makes for a more flexible class ultimately.
     *
     * @param attributes A set of configuration parameters used during XLIFF conversion
     * @param logger the logger we use to log interesting events
     */
    public XliffFilterFacade(Map attributes, Logger logger){
        typeMap = new HashMap(); // the supported formats right now
        typeMap.put("html",new Integer(HTML));
        typeMap.put("htm",new Integer(HTML));
        typeMap.put("txt",new Integer(TEXT));
        typeMap.put("xlz",new Integer(XLZ));
        typeMap.put("sgml",new Integer(SGML));
        typeMap.put("sgm",new Integer(SGML));
        typeMap.put("book",new Integer(BOOK));
        typeMap.put("xml",new Integer(XML));
        typeMap.put("jsp",new Integer(JSP));
        
        typeMap.put("java", new Integer(JAVA));
        typeMap.put("po", new Integer(PO));
        typeMap.put("properties", new Integer(PROPERTIES));
        typeMap.put("tmsg", new Integer(MSG));
        typeMap.put("msg", new Integer(MSG));
        typeMap.put("dtd", new Integer(DTD)); // mozilla dtd files only
        
        typeMap.put("sxw", new Integer(OPENOFFICE));
        typeMap.put("sxc", new Integer(OPENOFFICE));
        typeMap.put("sxi", new Integer(OPENOFFICE));
        // not strictly an OpenOffice format only anymore, but
        // we use the same mechanism for these files...
        typeMap.put("odt", new Integer(OPENOFFICE));
        typeMap.put("ods", new Integer(OPENOFFICE));
        typeMap.put("odg", new Integer(OPENOFFICE));
        typeMap.put("odp",new Integer(OPENOFFICE));
        
        typeMap.put("xliff", new Integer(STAROFFICE_XLIFF));
        
        //logger.log(Level.FINE,"Initialising xml config manager");
        // need a better way of doing this.
        String xmlConfigDTDLocation = "";
        String xmlConfigStoreLocation = "";
        xmlConfigDTDLocation = (String)attributes.get("xml.config.dtd");
        xmlConfigStoreLocation = (String)attributes.get("xml.config.dir");
        if (xmlConfigDTDLocation == null){
            xmlConfigDTDLocation = "";
        }
        if (xmlConfigStoreLocation == null){
            xmlConfigStoreLocation = "";
        }
        try {
            XmlConfigManager.init(xmlConfigDTDLocation, xmlConfigStoreLocation);
        } catch (java.io.IOException e){
            logger.log(Level.WARNING, xliffFilterGUIMessages.getString("Unable_to_configure_XML_Filter_-_XML_to_XLIFF_conversion_may_not_work."));
        }
        //logger.log(Level.FINE,"Finished initialising config manager");
        
        
    }
    
    public void convert(File file, Map attributes, Logger logger) throws XliffFilterFacadeException {
        String name = file.getName();
        
        // work out the language and encoding - we default to en-US and ISO8859-1
        // if no information is given.
        String language = (String)attributes.get("source.language");
        if (language == null){
            language = "en-US";
            // No language specified : using en_US as a default for source XLIFF conversion
            logger.log(Level.WARNING,xliffFilterGUIMessages.getString("No_language_specified_:_using_en-US_as_default_for_source_XLIFF_conversion"));
        }
        
        // default to ISO8859-1
        String encoding = (String)attributes.get("file.encoding");
        if (encoding == null){
            encoding = "ISO8859-1";
        }
        
        // work out the filename extension
        String ext = fileTypeDetection(file, attributes);
        Integer type = (Integer)typeMap.get(ext);
        if (type == null){
            type = new Integer(UNDEFINED);
        }
        
        // here lieth a big switch statement, sorry.
        switch(type.intValue()){
            case HTML:
                encoding = determineHtmlEncoding(file, language, encoding, logger);
                processHtml(file, attributes, logger, language, encoding);
                break;
            case TEXT:
                encoding = determineEncoding(encoding, language,logger);
                processText(file, attributes, logger, language, encoding);
                break;
            case XLZ:
                // File file.getName() appears to already be in XLIFF zip format
                logger.log(Level.INFO, MessageFormat.format(
                        xliffFilterGUIMessages.getString("File_o_appears_to_already_be_in_XLIFF_zip_format"), 
                        new Object[] {file.getName()}));
                break;
            case SGML:
                encoding = determineEncoding(encoding, language,logger);
                processSgml(file, attributes, logger, language, encoding);
                break;
            /*
             * This code is commented out for the time being while I work out
             * how to deal with multiple files in the XLIFF Filter GUI. Book files
             * in Solbook (a Docbook subset) contain the DOCTYPE subset, defining
             * what marked sections resolve to, system entities and the like.
             * Need to revisit this.
             * case BOOK:
             *
             *
             *   encoding = determineEncoding(encoding, language,logger);
             *   processBook(file, props, logger, language, encoding);
             */
            case BOOK:
                logger.log(Level.INFO,xliffFilterGUIMessages.getString("Not_processing_book_files_-_no_support_yet."));
                break;
            case JSP :
                encoding = determineEncoding(encoding, language,logger);
                processJsp(file, attributes, logger, language, encoding);
                break;
            case PO:
            case PROPERTIES:
            case JAVA:
            case MSG:
            case DTD:
                encoding = determineEncoding(encoding, language, logger);
                processSoftware(file, attributes, logger, language, encoding);
                break;
            case OPENOFFICE:
                processOpenOffice(file, attributes, logger, language, encoding);
                break;
            case STAROFFICE_XLIFF:
                processStarOfficeXliff(file,attributes,logger,language);
                break;
            case XML:
            default:
                encoding = determineXmlEncoding(file, logger);
                processXml(file, attributes, logger, language, encoding);
        }
        /*
         *
         * Other converters will go in here when finished
         *
         *
         */
    }
    
    
    /** This simple method takes in a filename, and tries to detect it's type. At the moment
     * this is done purely on filename extension
     *
     */
    private String fileTypeDetection(File file, Map attributes){
        String ext = "";
        String name = file.getName();
        /* Solbook specific stuff here - probably don't need it at this stage
         * Map translatableFileMap = (Map)attributes.get("translatable.files.map");
         * if (translatableFileMap != null){
         *  if (translatableFileMap.containsKey(file.getAbsolutePath())){
         *       return "sgm";
         *   }
         * }
         */
        
        StringTokenizer st = new StringTokenizer(name, ".");
        while (st.hasMoreTokens()){
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        return ext;
    }
    
    
    private void processHtml(File file,  Map attributes, Logger logger, String language, String encoding)
    throws XliffFilterFacadeException {
        // Doing HML to XLIFF conversion on file.getName()
        logger.log(Level.INFO, MessageFormat.format(
                xliffFilterGUIMessages.getString("Doing_HTML_to_XLIFF_conversion_on_o"),
                new Object[] {file.getName()}));
        try {
            String directory = (String)attributes.get("suntrans2.directory");
            if (directory == null) directory="";
            
            // the guid is a cookie that can be set in the xlz file - it's not used here
            String guid="";
            if (guid == null) {
                HtmlToXliff xliff = new HtmlToXliff(directory, file.getAbsolutePath(), language, encoding, logger);
            }else {
                HtmlToXliff xliff = new HtmlToXliff(directory, file.getAbsolutePath(), language, encoding, logger,guid);
            }
        } catch (HtmlParserException e){
            // try to delete any .xlf or .skl files that were created
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("HtmlToXliff :" + e.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_html_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
        } catch (java.io.IOException e){
            throw new XliffFilterFacadeException("HtmlToXliff IO Exception: " + e.getMessage());
        } catch (Throwable t){
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("HtmlToXliff :" + t.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_html_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
        }
    }
    
    private void processText(File file, Map attributes, Logger logger, String language, String encoding)
    throws XliffFilterFacadeException {
        // ------------------ plaintext conversion -------------------------
        logger.log(Level.INFO, 
                MessageFormat.format(
                xliffFilterGUIMessages.getString("Doing_plaintext_to_XLIFF_conversion_on_o"),
                new Object[] {file.getName()}));
        try {
            // the guid is a cookie that can be set in the xlz file - it's not used here
            String guid = "";
            String directory = (String)attributes.get("suntrans2.directory");
            if (directory == null) directory="";
            PlaintextToXliff plain = new PlaintextToXliff(directory, file.getAbsolutePath(), language, encoding, logger, guid);
            
        } catch (PlaintextParserException e){
            throw new XliffFilterFacadeException("PlaintextToXliff " + e.getMessage());
        } catch (java.io.IOException e){
            throw new XliffFilterFacadeException("PlaintextToXliff IO Exception: " + e.getMessage());
        }
    }
    
    private void processSgml(File file, Map attributes, Logger logger, String language, String encoding)
    throws XliffFilterFacadeException {
        // ------------------ sgml conversion -------------------------
        // right now, this isn't fantastic - we're relying on some artifacts
        // from the server-side-tools to process a directory of Solbook file, with
        // the top-level .book file being parsed before hand, creating a list
        // of tranlslatable sgml files, and sgml files that contain a single
        // segment if any (eg. <programlisting>%myJavaFile</programlisting>)
        // Should be able to deal without that stuff, but if you're
        // confused about some of this code, that's the reason why. Will work
        // on this a bit more.
        
        logger.log(Level.INFO, MessageFormat.format(
                xliffFilterGUIMessages.getString("Doing_SGML_to_XLIFF_conversion_on_o"),
                new Object[] {file.getName()}));
        try {
            String directory = (String)attributes.get("suntrans2.directory");
            String guid = (String)attributes.get("portal.guid");
            GlobalVariableManager gvm = null;
            
            // looking up the gvm
            HashMap gvmMap = (HashMap)attributes.get("gvm.map");
            HashMap translatableFileMap = (HashMap)attributes.get("translatable.files.map");
            HashMap singleSegTranslatableFileMap = (HashMap)attributes.get("single-segment.translatable.files.map");
            boolean treatFileAsSingleSegment = false;
            
            if (translatableFileMap != null){
                logger.log(Level.CONFIG,"Looking for " + file.getAbsolutePath()+" in gvm map");
                String bookFile = (String)translatableFileMap.get(file.getAbsolutePath());
                logger.log(Level.CONFIG,"Found " + bookFile+" in translatable file map");
                
                if (bookFile != null){
                    logger.log(Level.CONFIG,"getting gvm from gvm map");
                    gvm = (GlobalVariableManager)gvmMap.get(bookFile);
                }
            }
            // now check to see if it's one of the single-segment translatable files
            if (gvm == null && singleSegTranslatableFileMap !=null){
                String bookFile = (String)singleSegTranslatableFileMap.get(file.getAbsolutePath());
                if (bookFile != null){
                    // at this point, we know that the file contains just
                    // a program listing or something, so set a flag.
                    treatFileAsSingleSegment = true;
                    logger.log(Level.INFO,MessageFormat.format(
                                xliffFilterGUIMessages.getString("File_appears_to_"+
                                "_be_included_from_a_sgml_programlisting_or_similar_tag."),
                                new Object[] {file.getAbsolutePath()}));
                                
                    logger.log(Level.CONFIG,"getting gvm from gvm map");
                    gvm = (GlobalVariableManager)gvmMap.get(bookFile);
                }
            }
            
            // calculate the short name (ie. strip the internal path used by server tools
            // - this is the file name that appears in the xliff header
            String dirname = (String)attributes.get("suntrans2.directory");
            if (dirname == null) dirname="";
            String filename = file.getAbsolutePath();
            String shortname = filename.substring(dirname.length(),filename.length());
            
            // excellent - now got a gvm or null. Let's try and convert the file...
            try {
                SgmlToXliff xliff = new SgmlToXliff(filename, shortname, gvm, language, encoding, logger, guid,treatFileAsSingleSegment);
            } catch (SgmlParseException e){ // catching any parse exceptions here, we try to then use a different parser
                logger.log(Level.INFO,
                        MessageFormat.format(
                        xliffFilterGUIMessages.getString("Unable_to_process_o_with_standard_SGML_parser"),
                        new Object[] {filename}));
                NonConformantSgmlToXliff nonc = new NonConformantSgmlToXliff(filename, shortname, gvm, language, encoding, logger, guid,treatFileAsSingleSegment);
            }
            
            
        } catch (SgmlFilterException e){
            // try to delete any .xlf or .skl files that were created
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("SgmlToXliff :" + e.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_sgml_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
            
        } catch (Throwable t){
            
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("SgmlToXliff :" + t.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_sgml_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
        }
        
    }
    
    
    /**
     * This method isn't being called until I can write something to allow us
     * to process directories at a time... It's Solbook-specific anyway, so this
     * probably isn't super-urgent.
     *
     * Explanation of GVM setup :
     * For sgml files, we store a mapping of the sgml filename to the book file it's
     * associated with. Using the SortTranslatableFileList method
     * we search for files called .book in the input. Based on this, we get a list
     * of translatable files. We store the  gvm which manages variables defined
     * by the book file in a map (gvm.map) - the key to this map is the book
     * filename itself.
     * <br><br>
     *
     * Each sgml file, has an entry in a different map, which maps it's filename
     * to the book file name it's associated with. Note that only files listed for
     * translation have entries in this map. (translatable.files.map)
     * <br><br>
     *
     * Thusly, if we can't find the map of translatable files, it suggests a .book
     * file isn't associated with this job, and so we can't get a GVM - in that case
     * we create a new one.
     */
    private void processBook(File file, Map attributes, Logger logger, String language, String encoding)
    throws XliffFilterFacadeException {
        // ------------------ book conversion -------------------------
        
        logger.log(Level.INFO, MessageFormat.format(
                xliffFilterGUIMessages.getString("Doing_.book_to_XLIFF_conversion_on_o"),
                new Object[] {file.getName()}));
        try {
            String directory = (String)attributes.get("suntrans2.directory");
            String guid = (String)attributes.get("portal.guid");
            // looking up the gvm
            HashMap gvmMap = (HashMap)attributes.get(xliffFilterGUIMessages.getString("gvm.map"));
            GlobalVariableManager gvm = null;
            logger.log(Level.CONFIG,"getting gvm from gvm map");
            gvm = (GlobalVariableManager)gvmMap.get(file.getAbsolutePath());
            if (gvm == null){
                logger.log(Level.CONFIG,"Unable to get gvm for this book file.");
            }
            // calculate the short name (ie. strip the internal path used by suntrans
            // - this is the file name that appears in the xliff header, and hence passport
            String dirname = (String)attributes.get("suntrans2.directory");
            if (dirname == null) dirname="";
            String filename = file.getAbsolutePath();
            String shortname = filename.substring(dirname.length(),filename.length());
            
            // excellent - now got a gvm or null. Let's try and convert the file...
            BookToXliff xliff = new BookToXliff(filename, shortname, gvm, language, encoding, logger,guid);
            
        } catch (SgmlFilterException e){
            // try to delete any .xlf or .skl files that were created
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("BookToXliff :"+ e.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_book_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
            
        } catch (Throwable t){
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("BookToXliff :"+ t.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_book_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
        }
        
    }
    
    private void processXml(File file, Map attributes, Logger logger, String language, String encoding)
    throws XliffFilterFacadeException {
        // --------------------- xml conversion -----------------
        
        logger.log(Level.INFO, 
                MessageFormat.format(
                xliffFilterGUIMessages.getString("Doing_XML_to_XLIFF_conversion_on_o"),
                new Object[] {file.getName()}));
        
        try {
            String directory = (String)attributes.get("suntrans2.directory");
            if (directory == null) directory="";
            
            String guid = (String)attributes.get("portal.guid");
            File tmpFile = File.createTempFile("dummy","dtd");
            String dummyDTDFile = tmpFile.getAbsolutePath();
            
            XmlIdentifier identifier = null;
            if (guid == null) {
                XmlToXliff xliff = new XmlToXliff(directory, file.getAbsolutePath(), language, encoding, logger,  dummyDTDFile);
                identifier = xliff.getXmlIdentifier();
            }else {
                XmlToXliff xliff = new XmlToXliff(directory, file.getAbsolutePath(), language, encoding, logger,guid, dummyDTDFile);
                identifier = xliff.getXmlIdentifier();
            }
            // save the xml identifier in the map to be used by the next tool
            // in the chain (most likely to be the TM tool);
            attributes.put("last.xml.identifier",identifier);
            tmpFile.delete();
        } catch (XmlParseException e){
            // try to delete any .xlf or .skl files that were created
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("XmlToXliff :" + e.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_xml_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
        } catch (java.io.IOException e){
            throw new XliffFilterFacadeException("XmlToXliff IO Exception: " + e.getMessage());
        } catch (Throwable t){
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("XmlToXliff :" + t.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_xml_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
        }
    }
    
    
    private void processJsp(File file,  Map attributes, Logger logger, String language, String encoding)
    throws XliffFilterFacadeException {
        
        logger.log(Level.INFO, MessageFormat.format(
                xliffFilterGUIMessages.getString("Doing_JSP_to_XLIFF_conversion_on_o"),
                new Object[] {file.getName()}));
        try {
            String directory = (String)attributes.get("suntrans2.directory");
            if (directory == null) directory="";
            
            String guid = (String)attributes.get("portal.guid");
            if (guid == null) {
                JspToXliff xliff = new JspToXliff(directory, file.getAbsolutePath(), language, encoding, logger);
            }else {
                JspToXliff xliff = new JspToXliff(directory, file.getAbsolutePath(), language, encoding, logger,guid);
            }
        } catch (HtmlParserException e){
            // try to delete any .xlf or .skl files that were created
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("JspToXliff :" + e.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_jsp_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
        } catch (java.io.IOException e){
            throw new XliffFilterFacadeException("JspToXliff IO Exception: " + e.getMessage());
        } catch (Throwable t){
            File tmp = new File(file.getAbsolutePath()+".xlf");
            tmp.delete();
            tmp = new File(file.getAbsolutePath()+".skl");
            tmp.delete();
            throw new XliffFilterFacadeException("JspToXliff :" + t.getMessage()+"\n"+
                    xliffFilterGUIMessages.getString("An_error_occurred_while_trying_to_parse_that_jsp_file._")+
                    xliffFilterGUIMessages.getString("Please_check_the_format,_and_try_again."));
        }
    }
    
    private void processSoftware(File file, Map attributes, Logger logger, String language, String encoding)
    throws XliffFilterFacadeException {
        // ------------------ software conversion -------------------------
        logger.log(Level.INFO, MessageFormat.format(
                xliffFilterGUIMessages.getString("Doing_software_to_XLIFF_conversion_on_o"),
                new Object[] {file.getName()}));
        try {
            String directory = (String)attributes.get("suntrans2.directory");
            if (directory == null) directory="";
            String guid = (String)attributes.get("portal.guid");
            String filename = file.getAbsolutePath();
            String shortname = filename.substring(directory.length(),filename.length());
            if (guid == null) {
                SoftwareToXliff sw = new SoftwareToXliff(filename, shortname, language, encoding, logger);
            }else {
                // FIXME - add the guid to the end of the softwaretoxliff stuff
                SoftwareToXliff sw = new SoftwareToXliff(filename, shortname, language, encoding, logger);
            }
        } catch (TMCParseException e){
            throw new XliffFilterFacadeException("SoftwareToXliff " + e.getMessage());
        }
    }
    
    
    private void processOpenOffice(File file, Map attributes, Logger logger, String language, String encoding)
    throws XliffFilterFacadeException {
        // ------------------ OpenOffice Writer conversion -------------------------
        logger.log(Level.INFO, MessageFormat.format(
                xliffFilterGUIMessages.getString("Doing_OpenOffice.org_to_XLIFF_conversion_on_o"),
                new Object[] {file.getName()}));
        try {
            String directory = (String)attributes.get("suntrans2.directory");
            if (directory == null) directory="";
            OOoToXliff sxw = new OOoToXliff( file, language, logger);
        } catch (OOoException e){
            throw new XliffFilterFacadeException("OOoToXliff " + e.getMessage());
        }
    }
    
    /**
     * Try to convert StarOffice Xliff filter to OLT Xliff
     */
    private void processStarOfficeXliff(File file,Map attributes, Logger logger, String language) throws XliffFilterFacadeException {
        try {
            SOXliffToXliff.convert(file,language);
        } catch (SOXliffException e) {
            throw new XliffFilterFacadeException(e.getMessage());
        }
    }
    
    /**
     * This is a the way we determine encoding - we pass in the user-supplied encoding.
     * If that's null, then we try to find out the default encoding for that language,
     * and then finally, we use ISO8859-1 as a fallback
     */
    protected String determineEncoding(String encoding, String language, Logger logger){
        String charset = encoding;
        if (charset == null){
            charset = standaloneLocaleTable.getDefaultEncoding(language);
            if (charset != null){
                // No encoding supplied by user : using default encoding for lang which is charset
                logger.log(Level.INFO, MessageFormat.format(
                        xliffFilterGUIMessages.getString("No_encoding_supplied_by_user_:_using_default_encoding_for_o_which_is_o"),
                        new Object[] {language, charset}));
            } else {
                // Unable to determine default encoding for lang, using ISO8859-1
                logger.log(Level.WARNING,
                        MessageFormat.format(xliffFilterGUIMessages.getString("Unable_to_determine_default_encoding_for_o_using_ISO8859-1"),
                        new Object[] {language}));
                charset = xliffFilterGUIMessages.getString("ISO8859-1");
            }
        }
        if (!java.nio.charset.Charset.isSupported(charset)){
            // Warning ! User supplied charset charset doesn't seem to be supported
            logger.log(Level.INFO,MessageFormat.format(
                    xliffFilterGUIMessages.getString("Warning!_User_supplied_charset_o_doesn't_seem_to_be_supported"),
                    new Object[] {charset}));
            charset = standaloneLocaleTable.getDefaultEncoding(language);
            if (charset != null){
                // Reverting to default encoding for lang which is charset
                logger.log(Level.INFO,MessageFormat.format(
                        xliffFilterGUIMessages.getString("Reverting_to_default_encoding_for_o_which_is_o"),
                        new Object[] {language, charset}));
            } else {
                // Unable to determine default encoding for lang, using ISO8859-1
                logger.log(Level.WARNING, MessageFormat.format(
                        xliffFilterGUIMessages.getString("Unable_to_determine_default_encoding_for_o_using_o"),
                        new Object[] {language, "ISO8859-1"}));
                charset = xliffFilterGUIMessages.getString("ISO8859-1");
            }
        }
        return charset;
    }
    
    
    /**
     * trying to determine the html encoding - we use the user specified encoding only
     * if no encoding was declared in the meta charset tag. If the user-supplied encoding
     * was null, we default to ISO-8859-1
     * @see org.jvnet.olt.filters.html.HtmlMetaTagController
     */
    protected String determineHtmlEncoding(File file, String language, String encoding, Logger logger){
        // try to determine html file character set (if it has one declared)
        String charset = HtmlMetaTagController.getMetaCharset(file.getAbsolutePath());
        if (charset != null){
            // Ignoring user specified charset encoding since file declared it's charset to be charset, using that instead
            logger.log(Level.INFO, MessageFormat.format(
                    xliffFilterGUIMessages.getString("Ignoring_user-specified_charset_o_using_o_instead"),
                    new Object[] {encoding, charset}));
            if (!java.nio.charset.Charset.isSupported(charset)){
                // Warning ! File declared charset charset doesn't seem to be supported, trying user-supplied charset instead...
                logger.log(Level.INFO, MessageFormat.format(
                        xliffFilterGUIMessages.getString("Warning!_File-declared_charset_o"),
                        new Object[] {charset}));
                charset = determineEncoding(encoding, language, logger);
            }
        } else {
            charset = determineEncoding(encoding, language, logger);
        }
        return charset;
    }
    
    /**
     * determine encoding for xml file. If XmlEncodingTagController can't determine encoding
     * from the xml file, we default to UTF-8
     * @see org.jvnet.olt.filters.xml.XmlEncodingTagController
     */
    protected String determineXmlEncoding(File file, Logger logger){
        // try to determine xml file character set
        String encoding = XmlEncodingTagController.getXmlEncoding(file,logger);
        if (encoding != null){
            logger.log(Level.INFO,MessageFormat.format(
                    xliffFilterGUIMessages.getString("Ignoring_user-specified_charset_using_o_instead"),
                    new Object[] {encoding}));
            if (!java.nio.charset.Charset.isSupported(encoding)){
                // Warning ! File-declared charset doesn't seem to be supported, defaulting to UTF-8
                logger.log(Level.INFO, MessageFormat.format(
                        xliffFilterGUIMessages.getString("Warning!_File-declared_charset_UTF"),
                        new Object[] {encoding}));
                encoding="UTF-8";
            }
        } else {
            // Some problem encountered while determining encoding for file.getName(), defaulting to UTF-8
            logger.log(Level.INFO,MessageFormat.format(
                    xliffFilterGUIMessages.getString("Some_problem_encountered_while_determining_encoding_for_o_defaulting_to_UTF-8"),
                    new Object[] {file.getName()}));
            encoding="UTF-8";
        }
        return encoding;
    }
    
    /**
     * This method is used to update the XML configuration file store with
     * the contents of a directory containing config files
     */
    public static void updateXmlConfigRepository(String sourceDirname, String configStoreDirname, String configDtd) throws XliffFilterFacadeException {
        
        File sourceDir = new File(sourceDirname);
        File repositoryDir = new File(configStoreDirname);
        if (!sourceDir.isDirectory() || !repositoryDir.isDirectory()){
            throw new XliffFilterFacadeException(xliffFilterGUIMessages.getString("source_dir_and_config_dir_must_be_directories_!"));
        }
        try {
            XmlConfigManager.init(configDtd, configStoreDirname);
            File[] configFiles = sourceDir.listFiles();
            for (int i=0; i<configFiles.length; i++){
                if (configFiles[i].canRead()){
                    XmlConfigManager.processConfig(configFiles[i]);
                } else {
                    Logger.global.log(Level.WARNING, MessageFormat.format(
                            xliffFilterGUIMessages.getString("Unable_to_access_configuration_file_o"),
                            new Object[] {configFiles[i].getAbsolutePath()}));
                }
            }
        } catch (ProcessXmlConfigException e){
            XliffFilterFacadeException xfe = new XliffFilterFacadeException(
                    xliffFilterGUIMessages.getString("Unable_to_add_files_to_XML_Config_Store_:_")+e.getMessage());
            xfe.initCause(e);
            throw xfe;
        } catch (IOException e){
            XliffFilterFacadeException xfe = new XliffFilterFacadeException(
                    xliffFilterGUIMessages.getString("IO_problem_trying_to_configure_XML_config_store_:_")+e.getMessage());
            xfe.initCause(e);
            throw xfe;
        }
        
    }
    
}

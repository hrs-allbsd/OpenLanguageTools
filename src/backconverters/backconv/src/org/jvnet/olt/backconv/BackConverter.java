
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BackConverter.java
 *
 * Created on June 16, 2004, 2:17 PM
 */

package org.jvnet.olt.backconv;

//import org.jvnet.olt.so_back_converter.StarOfficeBackConverter;
import org.jvnet.olt.util.ContentSource;
import org.jvnet.olt.util.LoadableResourceContentSource;
import org.jvnet.olt.xliff_back_converter.BackConverterProperties;
import org.jvnet.olt.xliff_back_converter.TypeExtractingXliffEventHandler;
import org.jvnet.olt.xliff_back_converter.XliffBackConverter;
import org.jvnet.olt.xliff_back_converter.XliffEntityResolver;
import org.jvnet.olt.xliff_back_converter.XliffParser;
import org.jvnet.olt.utilities.XliffZipFileIO;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



/** This class is to take over the role of being the top level interface for
 * the XLIFF back conversion process. It will be responsible for determining what
 * type of back converson is necessary and for delegating the work to the
 * appropriate classes.
 *
 * Update : There's a problem with this class - we were using it as the main
 * top level interface to the XLIFF backconversion, but the trouble is, while
 * XLIFF backconverter needs to be bundled with the editor, there are
 * some types of backconversion that *have* to be done on the server (such
 * as documents that were created from the XLIFF SubSegmenter (Internal StarOffice
 * process) and the FrameMaker filter (which uses a win32 webservice to complete
 * the backconversion) As a result, I've added the getFileType() method, which
 * can be used after the initial backconversion to see if any further steps
 * are required. They need to be implemented on the XliffBackConverterFileProcessor
 * or somewhere else that doesn't get included in the editor.
 * @author  jc73554
 */
public class BackConverter {
    
    protected final static int SO_XLIFF  = 0;
    protected final static int DEFAULT = 1;
    protected final static int FRAMEMAKER = 2;
    
    private ContentSource xliffDtd;
    private ContentSource sklDtd;
    private Logger logger;
    
    private String fileType = "";
    
    /** Creates a new instance of BackConverter */
    public BackConverter(ContentSource xliffDtd, ContentSource sklDtd, Logger logger) {
        //  Guard clauses.
        if(xliffDtd == null) { throw new IllegalArgumentException("XLIFF DTD content data source is null!"); }
        if(sklDtd == null) { throw new IllegalArgumentException("Skeleton DTD content data source is null!"); }
        
        this.xliffDtd = xliffDtd;
        this.sklDtd = sklDtd;
        
        if(logger == null) {
            this.logger = Logger.getLogger("org.jvnet.olt.backconv");
        } else {
            this.logger = logger;
        }
    }
    
    /** This method determines the original data type of the XLZ file. If the
     * original data type of the file was XML, then it checks to see if the XLZ
     * file has a workflow.properties component, and looks up the XML type from
     * that. Based on the results of that look up, it categorizes the file type
     * as either SO_XLIFF, FRAMEMAKER, or DEFAULT.
     */
    protected int determineOriginalFileType(File file) throws BackConverterException {
        try {
            //  Read the XLZ file. Get the data type by parsing the XLIFF content.
            XliffZipFileIO xlzFile = new XliffZipFileIO(file);
            if( !wasOriginalDataTypeXml(xlzFile) ) { return DEFAULT; }
            logger.finest("Original data type was XML.");
            
            //  If the original data type was XML, look for a workflow properties
            if( !xlzFile.hasWorkflowProperties() ) { return DEFAULT; }
            logger.finest("Workflow properties present.");
           
            Properties properties = xlzFile.getWorkflowProperties();
            
            //  If that exists check for the 'xmltype' property.
            String xmlTypeString = properties.getProperty("xmltype");
            logger.finest("xmltype = " + xmlTypeString);
            
            if(xmlTypeString == null) { return DEFAULT; }
            
            //  Return the relevant value.
            if(xmlTypeString.equals("XLIFF Document (one that needs to be further segmented)")) { //  Change this
                this.fileType="IntStarOffice";
                return SO_XLIFF;
            } else if(xmlTypeString.equals("FrameMaker")) { //  Change this
                this.fileType="FrameMaker";
                return FRAMEMAKER;
            } else {
                return DEFAULT;
            }
        }
        catch(Exception ex) {
            ex.printStackTrace(); //  Fixme: use the logger API
            BackConverterException bcEx = new BackConverterException("Problem determining the file type: " + ex.getMessage());
            bcEx.setStackTrace(ex.getStackTrace());
            
            throw bcEx;
        }
    }
    
    /** This method parses the Xliff content file and determines if the original
     * file type was XML or not. Note: this may intoduce a small performance hit.
     */
    protected boolean wasOriginalDataTypeXml(XliffZipFileIO xlzFile) throws ZipException, IOException, SAXException, ParserConfigurationException {
        //  Parse the file using on of the Xliff parsers 
        BackConverterProperties props = new BackConverterProperties();
        XliffEntityResolver entityResolver = new XliffEntityResolver(logger, xliffDtd.getReader());
        TypeExtractingXliffEventHandler handler = new  TypeExtractingXliffEventHandler();
        XliffParser parser = new XliffParser(handler, entityResolver, logger, props, xliffDtd.getReader());
        
        Reader reader = xlzFile.getXliffReader();
        InputSource inputSource = new InputSource(reader);
        parser.parse(inputSource);
        
        String originalType = handler.getOriginalType();
        
        return originalType.equalsIgnoreCase("XML");
    }
    
    /** This is a convenience method for back conversion without writing translation 
     * status
     */
    public boolean backConvert(File xlzFile, String dir, boolean getSource, String encoding) throws BackConverterException {
        return backConvert(xlzFile, dir, getSource, encoding, false);
    }
    
    public boolean backConvert(File xlzFile, String dir, boolean getSource, String encoding, boolean writeTransStatus) throws BackConverterException {
        //  Determine the original file type
        int fileType = determineOriginalFileType(xlzFile);
        logger.finer("Datatype was " + fileType);
        
        switch(fileType) { 
            case SO_XLIFF:
                // return doStarOfficeBackConversion(xlzFile, dir, getSource, writeTransStatus);
		System.out.println(" Not doing StarOffice Back Conversion !!");
            case FRAMEMAKER:  //  drop through: later Framemaker processing can be added here.
            case DEFAULT:     //  drop through
            default:
                return doOrdinaryBackConversion(xlzFile, dir, getSource, writeTransStatus, encoding);
        }
    }
    
    protected boolean doStarOfficeBackConversion(File xlzFile, String dir, boolean getSource, boolean writeTransStatus) throws BackConverterException {
        logger.fine("Doing StarOffice Xliff back conversion.");
        
        //  Create a StarOfficeBackConverter
        // StarOfficeBackConverter sobc = new StarOfficeBackConverter(xliffDtd, sklDtd, logger);

        // sobc.backConvert(xlzFile, dir, getSource);
                
        return true;
    }
    
    protected boolean doOrdinaryBackConversion(File xlzFile, String dir, boolean getSource, boolean writeTransStatus, String encoding) throws BackConverterException {
        logger.fine("Doing ordinary back conversion.");
        try {
            Reader xliffDTDReader = xliffDtd.getReader();
            Reader skeletonDTDReader = sklDtd.getReader();
            
            //  Initialize an XliffBackConverter
            XliffBackConverter xbc = new XliffBackConverter(xliffDTDReader, skeletonDTDReader, logger);
            
            xbc.backConvert(xlzFile, dir, getSource, encoding, writeTransStatus);
        }
        catch(IOException ioEx) {
            BackConverterException bcEx = new BackConverterException("Problem doing ordinary backconversion: "+ ioEx.getMessage());
            bcEx.setStackTrace(ioEx.getStackTrace());
            throw bcEx;
        }
        return true;
    }
    
    public String getFileType(){
        return this.fileType;
    }
    
    public static void main(java.lang.String[] args) {
        try {
            String strXlzFile = args[0];
            String outputDir  = args[1];
            String encoding = args[2];
            
            ContentSource xliffDtd = new LoadableResourceContentSource("resources/xliff.dtd");
            ContentSource sklDtd = new LoadableResourceContentSource("resources/tt-xliff-skl.dtd");
            
            Logger logger = Logger.getLogger("org.jvnet.olt.backconv");
            
            BackConverter backConv = new BackConverter(xliffDtd, sklDtd, logger);
            backConv.backConvert(new File(strXlzFile), outputDir, false, encoding, false);
         
            System.exit(0);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
}

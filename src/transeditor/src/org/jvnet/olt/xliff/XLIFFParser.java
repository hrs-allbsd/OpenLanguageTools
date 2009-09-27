/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jvnet.olt.util.FileUtils;
import org.jvnet.olt.editor.util.LanguageMappingTable;
import org.jvnet.olt.editor.util.MultiWriter;
import org.jvnet.olt.editor.util.NestableException;
import org.jvnet.olt.format.*;
import org.jvnet.olt.utilities.MissingSklDataException;
import org.jvnet.olt.utilities.XliffZipFileIO;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;




/**
 * @author <a href="mailto:manpreet.singh@sun.com">Manpreet Singh</a>
 * @version 1.3
 * @author Tony Wu 07-31
 */
public class XLIFFParser {
    private static final Logger logger = Logger.getLogger(XLIFFParser.class.getName());
    private org.jvnet.olt.xliff.XLIFFModel model = null;
    private org.jvnet.olt.xliff.SAXWriter writer = null;
    
    private static final int FILE_TYPE_XLF = 0;
    private static final int FILE_TYPE_XLZ = 1;
    
    //added by tony 07-31
    public XLIFFSentence[] m_xlfSrcSentences;
    public XLIFFSentence[] m_xlfTgtSentences;

    private String sourceLang = null;
    private String targetLang = null;
    private String schemaLocation = null;
    
    private int m_iSize = 0;
    
    //end of tony's addition
    private XliffZipFileIO xlzZipFile = null;
    private File sourceFile;
    private File tempCopy;

    private TrackingGroup m_groupTracking = null;
    private TrackingComments m_commentsTracking = null;
    private AttributesFactory m_attrFactory = null;
    private TrackingSourceContext m_sourceContextTracking = null;
    private String originalDataType;
    
    private boolean didFastOpen;
    private File file;
    private int fileType;
    private Version version;
    
    public XLIFFParser(File file){
        this.file = file;
        
        this.sourceFile = new File(file.getAbsolutePath());
    }
    
    /** this method examines the file to open.
     *
     * Result of examination is:
     * <ul>
     *  <li>File type xlz or xlf</li>
     *  <li>XLIFF version 1.0/1.1</li>
     *  <li>Source / Target languages</li>
     * </ul>
     *
     */
    public void examineFile() throws NestableException,IOException{
        try{
            fileType = guessFileType(file);
            
            java.io.Reader isr = getReader(file);
            try {
                guessFileVersion(isr);
                if(version == null)
                    throw new NestableException("Wrong version");
                
            } finally {
                if(isr != null)
                    isr.close();
            }
            
        } catch (Exception e){
            throw new NestableException(e);
        } finally{
            didFastOpen = true;
        }
    }
    
    private int guessFileType(File f) throws IOException {
        ZipFile zf = null;
        try{
            zf = new ZipFile(f);
            return FILE_TYPE_XLZ;
        } catch (ZipException ze){
            return FILE_TYPE_XLF;
        } finally{
            if(zf != null)
                zf.close();
        }
        
    }
    
    //private java.io.Reader getReader() throws IOException{
    public java.io.Reader getReader(File f) throws IOException{
        if(fileType == FILE_TYPE_XLF) {
            FileInputStream fis = new FileInputStream(f);
            // try to skip BOM
            byte[] bbuf = new byte[3];
            if ( fis.read(bbuf, 0, 3)>=0) {
                // test for BOM: EF BB BF
                if ( bbuf[0] == -17 && bbuf[1] == -69 && bbuf[2] == -65 ) {
                    // we catched a BOM -> skip
                } else {
                    // no BOM -> reset
                    fis.close();
                    fis = new FileInputStream(f);
                }
            }
            return new BufferedReader(new InputStreamReader(fis,"UTF-8"));
        } else if(fileType == FILE_TYPE_XLZ){
            xlzZipFile = new XliffZipFileIO(f);
            return new BufferedReader(xlzZipFile.getXliffReader());
            
        } else
            throw new IllegalStateException("unknown file type");
    }
    
    //TODO change param to File
    //bug 4758111
    public void readFile() throws IOException,NestableException {
        
        try {
            //logger.finer(aSourceXLIFFFilePath);
            
            java.io.Reader r = getReader(file);
            
            long t1 = System.currentTimeMillis();
            
            SAXReader reader = new SAXReader(version, schemaLocation);

            model = reader.parse(r);
            
            long t2 = System.currentTimeMillis();
            logger.info("Document read in " + ((t2 - t1) / 1000.0) + " seconds");
            logger.info("Avg speed:" + ((model.getSize() * 1000.0) / (t2 - t1)) + " segs/sec");
            
            writer = new org.jvnet.olt.xliff.SAXWriter(version);
            
            build();
        } catch (ZipException ze){
            throw new NestableException(ze);
        } catch (SAXParseException spe){
            throw new NestableException(spe);
        } catch (SAXException sa){
            throw new NestableException(sa);
        } catch(ParserConfigurationException pce){
            throw new NestableException(pce);
        }
/*
 
            if (OpenFileFilters.isFileNameXLF(file)) {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
 
 
                fis = new FileInputStream(file);
                isr = new InputStreamReader(fis, "UTF-8");
 
                long t1 = System.currentTimeMillis();
                SAXReader reader = new SAXReader(ver);
                model = reader.parse(isr);
 
                long t2 = System.currentTimeMillis();
                logger.info("Document read in " + ((t2 - t1) / 1000.0) + " seconds");
                logger.info("Avg speed:" + ((model.getSize() * 1000.0) / (t2 - t1)) + " segs/sec");
 
                writer = new org.jvnet.olt.xliff.SAXWriter(model.getVersion());
 
                //TODO remove from here
                writer.saveTargetLanguageCode(model.getTargetLanguage());
            } else {
                xlzZipFile = new XliffZipFileIO(file);
 
                java.io.Reader rdr = xlzZipFile.getXliffReader();
 
                try {
                    ver = guessFileVersion(rdr);
                } finally {
                    rdr.close();
                }
 
                long t1 = System.currentTimeMillis();
                SAXReader reader = new SAXReader(ver);
                model = reader.parse(xlzZipFile.getXliffReader());
 
                long t2 = System.currentTimeMillis();
                logger.info("Document read in " + ((t2 - t1) / 1000.0) + " seconds");
                logger.info("Avg speed:" + ((model.getSize() * 1000.0) / (t2 - t1)) + " segs/sec");
                writer = new SAXWriter(model.getVersion());
 
                //TODO remove from here
                writer.saveTargetLanguageCode(model.getTargetLanguage());
            }
 
            //TODO clean up the mess below
        } catch (MalformedURLException e) {
            logger.throwing(getClass().getName(), "XLIFFParser", e);
            throw new NestableException(e);
        } catch (ZipException ze) {
            logger.throwing(getClass().getName(), "XLIFFParser", ze);
 
            if (ze.getMessage().equals("Error - entry for content.xlf doesn't exist")) {
                throw new Exception("xlzFileInvalid");
            } else {
                throw new Exception(ze.getMessage());
            }
        } catch (IOException ie) {
            //TODO throw upwards
            ie.printStackTrace();
            logger.throwing(getClass().getName(), "XLIFFParser", ie);
        } catch (Exception ee) {
            logger.throwing(getClass().getName(), "XLIFFParser", ee);
            throw ee;
        }
 */
    }
    
    private Version guessFileVersion(java.io.Reader r)
    throws IOException, SAXException, ParserConfigurationException,NestableException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        
        SAXParser saxParser = factory.newSAXParser();
        
        XLIFFFastFailParser ffp = new XLIFFFastFailParser(XLIFFFastFailParser.SEARCH_TYPE_LANGS);
        
        try {
            saxParser.parse(new InputSource(r), ffp);
            
            
        } catch (XLIFFFastFailParser.FailureException fe) {
            logger.info("Failure while opening guessing the XLIFF version");
            logger.info("the file is not a valid XLIFF file");
            
            throw new NestableException("unknown version");
            //handle failure
        } catch (XLIFFFastFailParser.SuccessException se) {
            logger.info("XLIFF version:" + ffp.getVersion().toString() );
        } finally {
            version = ffp.getVersion();
            sourceLang = ffp.getSourceLanguage();
            targetLang = ffp.getTargetLanguage();
            schemaLocation = ffp.getSchemaLocation();
            
            return version;
        }
    }
    
    private boolean build() {
        sourceLang = model.getSourceLanguage();
        targetLang = model.getTargetLanguage();
        originalDataType = model.getOriginalDataType();
        
        m_groupTracking = model.getGroupTrack();
        m_commentsTracking = model.getCommentsTrack();
        m_sourceContextTracking = model.getSourceContextTrack();
        m_attrFactory = model.getAttrFactory();
        
        m_iSize = model.getSize();
        m_xlfSrcSentences = new XLIFFSentence[m_iSize];
        m_xlfTgtSentences = new XLIFFSentence[m_iSize];
        
        int j = 0;
        
        for (Iterator i = model.getIDArray().iterator(); i.hasNext(); j++) {
            String key = (String)i.next();
            m_xlfSrcSentences[j] = (XLIFFSentence)model.getGroupZeroSource().get(key);
            m_xlfTgtSentences[j] = (XLIFFSentence)model.getGroupZeroTarget().get(key);
        }
        
        /*
                for(int i=0;i<m_iSize;i++) {
                    m_xlfSrcSentences[i] = (XLIFFSentence)gReader.getGroupZeroSource().get(m_IDList.get(i));
                    //m_xlfSrcSentences[i].setVisibleSentence();
                    m_xlfTgtSentences[i] = (XLIFFSentence)gReader.getGroupZeroTarget().get(m_IDList.get(i));
                    //m_xlfTgtSentences[i].setVisibleSentence();
                }
         */
        return true;
    }
    
    public void shutdown() {
        model = null;
        
        //TODO think of something smart here
        //  System.gc();
    }
    
    /*
    public List getIDArray() {
        if(m_IDList == null) m_IDList = gReader.getIDArray();
        return m_IDList;
    }
     */
    public List getAltTransMatchInfo(String aTransUnitId) {
        ArrayList theList = (ArrayList)model.getGroupAltTrans().get(aTransUnitId);
        
        if (theList == null) {
            return new ArrayList();
        }
        
        return theList;
    }
    
    public void saveSourceSegment(XLIFFSentence aSentence) throws NestableException {
        writer.saveSourceSentence(aSentence);
    }
    
    public void saveTargetSegment(XLIFFSentence aSentence) throws NestableException {
        writer.saveTargetSentence(aSentence);
    }
    
    public void setTargetLanguage(String strTgtLanInput) {
        targetLang = strTgtLanInput;
        
        //        try{
        writer.saveTargetLanguageCode(strTgtLanInput);
        model.setTargetLanguage(strTgtLanInput);
        
        //        } catch  (NestableException ne) {
        //            ne.printStackTrace();
        //        }
    }
    
    private XliffZipFileIO prepareFile(File fInput) throws IOException {
        if ((fInput == null) || sourceFile.equals(fInput)) {
            return xlzZipFile;
        }

        boolean boolNoOpenedFile = (xlzZipFile == null);
        boolean boolFlatXliffFile = ! fInput.getName().endsWith("xlz");

        //  Guard clause to ensure no attempt is made to write an XLZ file when
        //  skeleton data does not exist.
        if (boolNoOpenedFile && !boolFlatXliffFile) {
            throw new MissingSklDataException("There is no skeleton data available to write an XLZ file.");
        }

        if (!boolFlatXliffFile) {
            if (!xlzZipFile.hasSkeleton()) {
                throw new MissingSklDataException("There is no skeleton data available to write an XLZ file.");
            }

            //  Read in the Skeleton data
            java.io.Reader sklReader = xlzZipFile.getSklReader();

            XliffZipFileIO xlzZipFileNew = new XliffZipFileIO(fInput);
            java.io.Writer sklWriter = xlzZipFileNew.getSklWriter();

            int ch = 0;

            while ((ch = sklReader.read()) != -1) {
                sklWriter.write((char)ch);
            }

           return xlzZipFileNew;

            //gWriter.setSaveAsFileWriter(xlzZipFileNew);
        }
        return null;
    }

    public void saveToFile(File dstFile,boolean autosave) throws NestableException {
    
        try {
            
            XliffZipFileIO xlz = xlzZipFile;
            if(!dstFile.equals(sourceFile) && fileType == FILE_TYPE_XLZ){
                xlz = prepareFile(dstFile);
            }

            writer.saveComments(m_commentsTracking);
                 
            if(fileType == FILE_TYPE_XLZ){
                saveToXLZFile(xlz,autosave);
                if(!autosave)
                    xlzZipFile = xlz;
            }
            else
                saveToRegularFile(dstFile,autosave);
            
            
            
            //What we do here: when saving file we need to parse the original file and inject
            //the modified data into into the XML stream
            //So we need the orig file while we write out the new file. Therefore we can not use
            //one file.
            //Before first save we create a tempCopy of the orig file which we'll use when saving.
            //We also will create another temp file tempCopy2 where a copy of the new file will be stored.
            //After saving the file we will delete the first temp file and declare the second one the
            //new temp file
           /*
            if (xlz != null) {
                saveToXLZFile(xlzZipFile,autosave);
                xlzZipFile = xlz;
            } else {
                saveToRegularFile(dstFile,autosave);
            }
            */
        } catch (IOException ioe) {
            throw new NestableException(ioe);
        } catch (SAXException sae) {
            throw new NestableException(sae);
        }
    }

    private void saveToRegularFile(File destFile,boolean autosave) throws IOException, SAXException {
        java.io.Writer xwriter = null;
        java.io.Reader reader = null;
        java.io.Writer realWriter = null;
        java.io.Writer tempCopyWriter = null;
        
        if (tempCopy == null) {
            tempCopy = File.createTempFile("ste", ".xlf");
            FileUtils.copyFiles(sourceFile, tempCopy);
        }
        
        File tempCopy2 = File.createTempFile("ste", ".xlf");
        
        boolean useTemp = false;
        
        try {
            xwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
            tempCopyWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempCopy2), "UTF-8"));
            //reader = new InputStreamReader(new FileInputStream(tempCopy), "UTF-8");
            reader = getReader(tempCopy);

            realWriter = new MultiWriter(new java.io.Writer[] { tempCopyWriter, xwriter });
           
            writer.saveTargetLanguageCode(targetLang);
            writer.write(reader, realWriter,autosave);
            
            tempCopy.delete();
            tempCopy = tempCopy2;
        } finally {
            if (realWriter != null) {
                try {
                    realWriter.flush();
                    realWriter.close();
                } catch (IOException ioe) {
                    ;
                }
            }
            
            if (writer != null) {
                try {
                    xwriter.flush();
                    xwriter.close();
                } catch (IOException xioe) {
                    ;
                }
            }
            
            if (tempCopyWriter != null) {
                try {
                    tempCopyWriter.flush();
                    tempCopyWriter.close();
                } catch (IOException xioe) {
                    ;
                }
            }
            
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException xioe) {
                    ;
                }
            }
        }
    }

    private void saveToXLZFile(XliffZipFileIO xlz,boolean autosave) throws IOException, SAXException {
    
        java.io.Writer realWriter = null;
        java.io.Writer xwriter = null;
        java.io.Reader reader = null;
        java.io.Writer tempCopyWriter = null;
        
        if (tempCopy == null) {
            tempCopy = File.createTempFile("ste", ".xlf");
            
            reader = xlz.getXliffReader();
            FileUtils.copyStreamToFile(reader, tempCopy);
            reader.close();
        }
        
        File tempCopy2 = File.createTempFile("ste", ".xlf");
        
        try {
            reader = xlz.getXliffReader();
            xwriter = xlz.getXliffWriter();
            
            tempCopyWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempCopy2), "UTF-8"));
            realWriter = new MultiWriter(new java.io.Writer[] { tempCopyWriter, xwriter });
            reader = new InputStreamReader(new FileInputStream(tempCopy), "UTF-8");
           
            writer.saveTargetLanguageCode(targetLang);
            writer.write(reader, realWriter,autosave);
            
            realWriter.close();
            reader.close();
            
            xlz.writeZipFile();
            
            tempCopy.delete();
            tempCopy = tempCopy2;
        } finally {
            if (realWriter != null) {
                try {
                    realWriter.close();
                } catch (IOException ioe) {
                    ;
                }
            }
            
            if (xwriter != null) {
                try {
                    xwriter.close();
                } catch (IOException ioe) {
                    ;
                }
            }
            
            if (tempCopyWriter != null) {
                try {
                    tempCopyWriter.close();
                } catch (IOException ioe) {
                    ;
                }
            }
            
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ;
                }
            }
        }
    }
    
    public int getSize() {
        return m_iSize;
    }
    
    public String getSourceLanguage() {
        if (sourceLang == null) {
            return null;
        }
        
        LanguageMappingTable name2id = LanguageMappingTable.getInstance();
        
        return (String)name2id.translateLangCode(sourceLang);
    }
    
    public String getTargetLanguage() {
        if (targetLang == null) {
            return null;
        }
        
        LanguageMappingTable name2id = LanguageMappingTable.getInstance();
        
        return (String)name2id.translateLangCode(targetLang);
    }
    
    public XLIFFSentence[] getSourceXLIFFSentences() {
        return m_xlfSrcSentences;
    }
    
    public XLIFFSentence[] getTargetXLIFFSentences() {
        return m_xlfTgtSentences;
    }
    
    public TrackingGroup getGroupTrack() {
        return m_groupTracking;
    }
    
    public TrackingComments getCommentsTrack() {
        return m_commentsTracking;
    }
    
    /**
     * A method to return the <context> information associated with each
     * <source> element within a trans-unit, used to help the translator
     * see what sort of string they're translating
     */
    public TrackingSourceContext getSourceContextTrack() {
        return m_sourceContextTracking;
    }
    
    public AttributesFactory getAttrFactory() {
        return m_attrFactory;
    }
    
    public String getOriginalDataType() {
        return originalDataType;
    }
    
    public void populateVariableManager(GlobalVariableManager gvm) {
        //  Delegate to the MrkContentTracker
        model.populate(gvm);
    }

}

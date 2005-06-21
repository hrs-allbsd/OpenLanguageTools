
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XlzFileBackConverterProcessor.java
 *
 * Created on August 18, 2004, 2:24 PM
 */

package org.jvnet.olt.app;

import org.jvnet.olt.backconv.BackConverter;
import org.jvnet.olt.backconv.BackConverterException;
import org.jvnet.olt.util.ContentSource;
import org.jvnet.olt.util.LoadableResourceContentSource;
import org.jvnet.olt.xml.ContentSourceEntityResolver;
import org.jvnet.olt.xsltrun.XliffXsltTransformCommand;
import org.jvnet.olt.xsltrun.XsltStylesheet;
import org.jvnet.olt.xsltrun.XsltStylesheetException;
import org.jvnet.olt.utilities.XliffZipFileIO;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

/**
 *
 * @author  jc73554
 */
public abstract class AbstractXlzFileBackConverterProcessor {
    private String srcDir;
    
    private String tgtDir;
    
    private String tempDir;
    
    private String encoding;
    
    private boolean generateTmx;
    
    private XliffXsltTransformCommand xlstRunner;
    
    private BackConverter backConv;
    
    /** Creates a new instance of XlzFileBackConverterProcessor */
    public AbstractXlzFileBackConverterProcessor(String srcDir, String tgtDir, String tempDir, String encoding, boolean generateTmx) throws XsltStylesheetException, IOException{
        //  Set the member variables
        this.srcDir = srcDir;
        this.tgtDir = tgtDir;
        this.tempDir = tempDir;
        this.encoding = encoding;
        this.generateTmx = generateTmx;
                
        //  Create an entity resolver
        ContentSourceEntityResolver resolver = new ContentSourceEntityResolver();
        ContentSource xliffDtd = new LoadableResourceContentSource("dtds/xliff.dtd");
        resolver.addEntityReference("http://www.oasis-open.org/committees/xliff/documents/xliff.dtd", xliffDtd);
        resolver.addEntityReference("xliff.dtd", xliffDtd);
        
        //  Create the transform Command
        xlstRunner = new XliffXsltTransformCommand(resolver);
               
        ContentSource sklDtd = new LoadableResourceContentSource("resources/tt-xliff-skl.dtd");
        
        backConv = new BackConverter(xliffDtd, sklDtd, Logger.getAnonymousLogger());
    }
    
    /** This method is responsible for processing a given XLZ file.
     */
    public void processXlzFile(File file) throws IOException {
        try {
            File newFile = copyXlzFile(file);
            
            //  Open the file and apply the transforms
            XliffZipFileIO xlzZipFile = new XliffZipFileIO(newFile);
            Reader reader = xlzZipFile.getXliffReader();
            Writer writer =  xlzZipFile.getXliffWriter();
            
            xlstRunner.execute(reader, writer);
            
            //  Write the transformed XLZ file to disk :-)
            xlzZipFile.writeZipFile();
            
            //  Notify the user
            System.out.println("Transforms complete on '" + file.getName() + "'. Starting back conversion...");
            
            //  Back convert the file.
            //  Maintenance note: this might be more effective elsewhere
            try {
                backConv.backConvert(newFile, tgtDir, false, encoding);
            }
            catch(BackConverterException bcEx) {
                System.err.println("Back conversion failed for " + file.getName() );
                bcEx.printStackTrace();
            }
            
            if(generateTmx) {
                //  Maintenance Note: The XLIFF to TMX conversion can go here
            }            
        }
        catch(ZipException zipEx) {
            System.err.println("A problem was encountered unzipping " + file.getName() );
            zipEx.printStackTrace();
        }
        catch(IOException ioEx) {
            System.err.println("An I/O problem was encountered processing " + file.getName() );
            ioEx.printStackTrace();
        }
    }
    
    
    /** This method copies the contents of a source file to the corresponding
     * location in the temporary directory.
     */
    protected File copyXlzFile(File file) throws IOException {
        //  Determine where to copy file to
        String newFileName = determineNewFileName(srcDir, tempDir, file);
        File newFile = new File(newFileName);
        
        //  Do sanity checks
        File parent = newFile.getParentFile();
        if(!parent.exists()) {
            if(!parent.mkdirs()) {
                throw new IOException("Unable to create temporary directory: " + parent.getPath());
            }
        }
        
        if(!parent.canWrite()) {
            throw new IOException("Cannot write to the temporary directory: " + parent.getPath());
        }
        
        //  Copy the file
        BufferedInputStream istream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream ostream = new BufferedOutputStream(new FileOutputStream(newFile));
        int ch = 0;
        while((ch = istream.read()) != -1) {
            ostream.write(ch);
        }
        istream.close();
        ostream.flush();
        ostream.close();
        
        return newFile;
    }
    
    protected String determineNewFileName(String fromDir, String toDir, File file) throws IOException {
        Pattern pattern = Pattern.compile("^" + fromDir);
        Matcher matcher = pattern.matcher(file.getPath());
        
        String str = "";
        if(!matcher.find()) {
            System.err.println("File does not seem to come from the source directory");
            throw new IOException("File does not seem to come from the source directory");
        } else {
            return matcher.replaceFirst(toDir);
        }        
    }
    
    /** This helper method creates a content source for styleshhets based on their
     * location in the jar.
     */
    protected XsltStylesheet getStylesheetInstance(String ssLoc) throws XsltStylesheetException, IOException {
        ContentSource source = new LoadableResourceContentSource(ssLoc);
        
        XsltStylesheet xsltSheet = new XsltStylesheet(source);
        
        return xsltSheet;
    }
    
    public void addStylesheet(XsltStylesheet sytlesheet) {
        xlstRunner.addStylesheet(sytlesheet);
    }
    
}

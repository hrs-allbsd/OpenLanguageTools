
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * OOoToXliff.java
 *
 * Created on February 8, 2005, 9:48 AM
 */
package org.jvnet.olt.filters.ooo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.*;
import java.util.zip.*;
import java.util.*;
import org.jvnet.olt.utilities.XliffZipFileIO;
import org.jvnet.olt.filters.xml.XmlToXliff;
import org.jvnet.olt.filters.xml.XmlParseException;
import org.jvnet.olt.filters.xml.xmlconfig.XmlConfigManager;
import org.jvnet.olt.filters.xml.xmlconfig.XmlIdentifier;

/**
 * This is a bit of a hack : it takes OpenOffice.org sxw files, extracts the XML
 * and then converts that to XLIFF using our XML filter. We save all of the extra
 * formatting information and other meta-data that's stored in the OpenOffice file
 * in the .xlz archive, which could then be resored after backconversion.
 *
 * This isn't the whole story, since when we convert back from XLIFF to XML, we
 * would also need to do manipulation of the styles section of the document in
 * order to use the correct fonts, etc. for the localised document.
 *
 * Work in progress...
 * @author timf
 */
public class OOoToXliff {
    
    /** A hash map to map between the OOo file extensions and the names they're given in
     * the workflow.properties file (consulted during backconversion to the original format
     * to work out how to backconvert and name a particular .xlz file)
     */
    private Map xmlTypeMap;
    private Logger logger;
    
    /** Creates a new instance of OOoToXliff */
    public OOoToXliff(File oooFile, String srcLang, Logger logger) throws OOoException{
        try {
            
            
            xmlTypeMap = new HashMap();
            xmlTypeMap.put("sxw","OpenOffice.org Writer");
            xmlTypeMap.put("sxi","OpenOffice.org Impress");
            xmlTypeMap.put("sxc","OpenOffice.org Calc");
            
            xmlTypeMap.put("odt","Open Document Format Text");
            xmlTypeMap.put("ods","Open Document Format Spreadsheet");
            xmlTypeMap.put("odg","Open Document Format Graphics");
            xmlTypeMap.put("odp","Open Document Format Presentation");
            
            this.logger = logger;
            File xmlFile = extractContent(oooFile, oooFile.getParent());
            if (xmlFile == null){
                throw new OOoException("Unable to find content.xml in "+oooFile.getName());
            }
            
            boolean rename = xmlFile.renameTo(new File(oooFile.getAbsolutePath()+".xml"));
            if (!rename){
                throw new OOoException("Unable to rename "+xmlFile.getAbsolutePath()+
                        " to " +oooFile.getAbsolutePath()+".xml");
            }
            xmlFile = new File(oooFile.getAbsolutePath()+".xml");
            
            String dir="./";
            File parent = oooFile.getParentFile();
            if (parent != null){
                dir = parent.getAbsolutePath()+"/";
            } else {
                File tmp = new File(".");
                dir = tmp.getAbsolutePath();
                dir = dir.substring(0,dir.length()-1);
            }

            // create a temporary DTD file, just so the resolver can
            // resolve to something (doesn't have to be a real DTD) - just
            // any readable file
            File dummyDTD = File.createTempFile("dummyDTD","dtd");
            XmlToXliff xliffConv = new XmlToXliff(dir, oooFile.getAbsolutePath()+".xml",
                    srcLang, "UTF-8", logger, dummyDTD.getAbsolutePath());
            
            File xmlXlzFile = new File(oooFile.getAbsolutePath()+".xml.xlz");
            File xlzFile = new File(oooFile.getAbsolutePath()+".xlz");
            dummyDTD.delete();
            // now remove the .xml portion of the filename
            boolean renamed = xmlXlzFile.renameTo(xlzFile);
            
            // now we have an xlz file containing the skeleton.skl, content.xlf
            // and workflow properties. Let's add the contents of the OOo file
            // to that xlz archive.
            addToXliffZipFile(oooFile, xlzFile);
            
            // finally, we want to set a cookie inside the file saying what type of XML this is
            // which gets used to determine how to further backconvert the resulting XML.
            Properties workflowProps = new Properties();
            String xmltype = getXmlTypeString(oooFile);
            workflowProps.setProperty("xmltype", xmltype);
            XliffZipFileIO io = new XliffZipFileIO(xlzFile);
            io.setWorkflowProperties(workflowProps);
            io.writeZipFile();
            // finally, delete the renamed content.xml that we've left hanging around.
            xmlFile = new File(oooFile.getAbsolutePath()+".xml");
            xmlFile.delete();
            
        } catch (java.io.IOException e) {
            OOoException ex = new OOoException("IO Exception thrown somewhere : "+e.getMessage());
            e.printStackTrace();
            ex.initCause(e);
            throw ex;
        } catch (XmlParseException e){
            OOoException ex = new OOoException("Xml Parse Exception thrown somewhere. "+e.getMessage());
            ex.initCause(e);
            e.printStackTrace();
            throw ex;
        }
        
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 3){
            System.out.println("Usage <OOo file> <pointer to xml-config.dtd> <directory containing config files>");
            System.exit(1);
        }
        try {
            XmlConfigManager.init(args[1], args[2]);
            OOoToXliff x = new OOoToXliff(new File(args[0]), "en-US", Logger.global);
        } catch (OOoException e){
            System.out.println("OOoToXliffException "+e.getMessage());
            e.printStackTrace();
        } catch (java.io.IOException e){
            System.out.println("IOException "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    protected void copyFile(File file, File target) throws IOException{
        
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(target));
        int i = 0;
        while ((i=is.read()) != -1){
            os.write(i);
        }
        is.close();
        os.close();
    }
    
    /**
     * This simply extracts the content.xml file from the OOo archive
     * and puts it into the directory given as a parameter
     */
    private File extractContent(File zipFile, String directory)
    throws java.io.IOException{
        File contentFile = null;
        
        if (directory == null){
            directory = "./";
        }
        File outputdir = new File(directory);
        
        ZipFile archive = new ZipFile(directory+"/" + zipFile.getName());
        outputdir.mkdirs();
        
        Enumeration en = archive.entries();
        while (en.hasMoreElements()){
            ZipEntry entry = (ZipEntry)en.nextElement();
            String filename = entry.getName();
            if (filename.equals("content.xml") && !entry.isDirectory()){
                
                BufferedInputStream in = new BufferedInputStream(
                        archive.getInputStream(entry));
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(outputdir.getAbsolutePath()+
                        "/"+filename));
                int i = 0;
                while ((i = in.read()) != -1)
                    out.write(i);
                contentFile = new File(outputdir.getAbsolutePath()+ "/" + filename);
                
                in.close();
                out.flush();
                out.close();
            }
        }
        archive.close();
        return contentFile;
    }
    
    /** This simple method takes in a filename, and tries to detect it's extension. At the moment
     * this is done purely on filename extension. If the file has no extension, it
     * returns an empty string.
     */
    private String getExtension(File file){
        
        String ext = "";
        String name = file.getName();
        StringTokenizer st = new StringTokenizer(name, ".");
        while (st.hasMoreTokens()){
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        return ext;
    }
    
    
    /** This adds all the files from a source zip archive to a target zip archive
     * @param sourceZipFile the zip file containing our source content
     * @param targetZipFile the zip file to which we copy entries
     */
    private void addToXliffZipFile(File sourceZipFile, File targetZipFile) throws IOException {
        
        File tempFile = File.createTempFile("tmp",".zip");
        ZipInputStream is = new ZipInputStream(new BufferedInputStream(new FileInputStream(sourceZipFile)));
        ZipOutputStream os = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile)));
        
        ZipEntry entry = null;
        if (sourceZipFile.length() !=0){ // if the zip file exists...
            while ((entry = is.getNextEntry()) != null){
                if (!entry.getName().equals("content.xml")){
                    os.putNextEntry(entry);
                    int i=0;
                    while ((i=is.read())!=-1){
                        os.write(i);
                    }
                    logger.log(Level.FINE,"Written entry "+entry.getName());
                    os.closeEntry();
                }
                //System.out.println("Written entry " + entry.getName());
            }
        }
        is.close();
        is = new ZipInputStream(new BufferedInputStream(new FileInputStream(targetZipFile)));
        entry = null;
        if (targetZipFile.length() !=0){ // if the zip file exists...
            while ((entry = is.getNextEntry()) != null){
                os.putNextEntry(entry);
                int i=0;
                while ((i=is.read())!=-1){
                    os.write(i);
                }
                logger.log(Level.CONFIG,"Written entry "+entry.getName());
                os.closeEntry();
                
                //System.out.println("Written entry " + entry.getName());
            }
        }
        os.close();
        is.close();
        copyFile(tempFile, targetZipFile);
        tempFile.delete();
    }
    
    /**
     * Simply forces the given XliffZipFileIO object to write itself out again
     * - this is needed just after adding a workflow Properties object to the
     * XLZ
     */
    private void flushXLZFile(XliffZipFileIO io) throws IOException {
        Reader sklReader = io.getSklReader();
        Reader xlfReader = io.getXliffReader();
        
        Writer sklWriter = io.getSklWriter();
        Writer xlfWriter = io.getXliffWriter();
        int i=0;
        while ((i=sklReader.read()) != -1){
            sklWriter.write(i);
        }
        
        i=0;
        while ((i=xlfReader.read()) != -1){
            xlfWriter.write(i);
        }
        sklWriter.close();
        xlfWriter.close();
        io.writeZipFile();
    }
    
    private String getXmlTypeString(File oooFile){
        String xmlType = (String)this.xmlTypeMap.get(getExtension(oooFile));
        if (xmlType == null){
            return "unknown";
        }
        return xmlType;
    }
    
}
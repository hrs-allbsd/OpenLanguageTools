
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * OpenOfficeXmlBackConversionCommand.java
 *
 * Created on February 10, 2005, 11:42 AM
 */

package org.jvnet.olt.xliff_back_converter.format.xml.ooo;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.jvnet.olt.xliff_back_converter.format.xml.XmlBackConversionCommand;
import org.jvnet.olt.xliff_back_converter.format.xml.XmlBackConversionCommandException;


/**
 * This class does backconversion of XLIFF files that represent OpenOffice.org
 * files. In particular, when we have converted an OOo file to xlz, we get an
 * archive that looks like this :
 *
 *<pre>
 * timf@cuprum[550] unzip -l sample.sxw.xlz
 * Archive:  sample.sxw.xlz
 * Length    Date    Time    Name
 * ------    ----    ----    ----
 * 45831  02-08-05  12:11   content.xlf
 * 63938  02-08-05  12:11   skeleton.skl
 *    81  02-08-05  12:11   workflow.properties
 *  2081  02-08-05  12:11   META-INF/manifest.xml
 *  6821  02-08-05  12:11   settings.xml
 *  1316  02-08-05  12:11   meta.xml
 *  8616  02-08-05  12:11   styles.xml
 *    30  02-08-05  12:11   mimetype
 *   585  02-08-05  12:11   layout-cache
 * ------                    -------
 * ......                    9 files
 * </pre>
 *
 * When we backconvert this file using the standard backconversion routines in
 * the parent package of this class, we get a translated file "content.xml"
 * (from the xliff file "content.xlf" and skeleton.skl) This class is designed 
 * to then take all. More work is needed here, I expect, in that I'm not doing any 
 * manipulation of the font table entries (or styles) in order to match fonts that would suit
 * the target language - this in particular is needed for Asian languages which
 * often use different fonts that ones that are likely to be used by english
 * documents (although I admit to not having tested this theory - perhaps it'll
 * all work fine ?) Anyway, if that work is needed, this is where you probably
 * want to start doing it.
 *
 * @author timf
 */
public class OpenOfficeXmlBackConversionCommand implements XmlBackConversionCommand {
    
    private String extension = "";
    
    /** Creates a new instance of Open OfficeXmlBackConversionCommand 
      @param oooExtension the extension of the OpenOffice.org file (eg. sxi, sxw or sxc)
     */
    public OpenOfficeXmlBackConversionCommand(String xmlType) {
        Map typeMap = new HashMap();
        typeMap.put("OpenOffice.org Writer", "sxw");
        typeMap.put("OpenOffice.org Calc","sxc");
        typeMap.put("OpenOffice.org Impress","sxi");
        
        typeMap.put("Open Document Format Text", "odt");
        typeMap.put("Open Document Format Spreadsheet", "ods");
        typeMap.put("Open Document Format Graphics", "odg");
        typeMap.put("Open Document Format Presentation", "odp");
        
        this.extension = (String)typeMap.get(xmlType);
        if (this.extension == null){
            this.extension="unknown";
        }
    }
    
    /**
     * Does XLZ to OpenOffice.org & OpenDocument conversion. We assume that all of the required
     * styles.xml, meta.xml, etc. are already present in the xlz archive.
     */
    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws XmlBackConversionCommandException  {
        try {
            File xmlFile = new File(filename);
            File xlzFile = new File(originalXlzFilename);
            String prefix = xlzFile.getName().replaceAll("."+this.extension+".xlz","");
            
            if (prefix.equals(xlzFile.getName())){
                // the files are named incorrectly
                throw new XmlBackConversionCommandException("Input XLZ file "+xlzFile.getAbsolutePath()+
                        " is incorrectly named, expecting [something]."+extension+".xlz");
            }
            String dir = xmlFile.getParent();
            File renamedXMLFile = new File(dir+File.separatorChar+"content.xml");
            boolean renamed = xmlFile.renameTo(renamedXMLFile);
            if (!renamed){
                throw new XmlBackConversionCommandException("Unable to rename input XML file "
                        +xmlFile.getAbsolutePath()+" to content.xml for inclusion in the OOo/OpenDocument archive");
            }
            
            // since the XLZ file looks really like the output sxw file, rather than
            // recreate the whole archive, what we do, is copy the xlz file to the output
            // filename, add the content.xml file and remove the content.xlf, skeleton.skl
            // and workflow.properties from that copied file.            
            File outputSxwFile = new File(dir+File.separatorChar+prefix+"."+extension);
            copyFile(xlzFile, outputSxwFile);
            // now, remove the content.xlf, workflow.properties and skeleton.skl add 
            // the content.xml to the zip archive
            addContentRemoveXliffFromZip(outputSxwFile, renamedXMLFile, "content.xml");
            
            
        } catch (java.io.IOException e){
            throw new XmlBackConversionCommandException("IO Exception thrown while recreating OOo/OpenDocument archive from "+originalXlzFilename+" " +
                    " : "+e.getMessage());
        }
        
    }
    
    
    /**
     * Manipulate a zip file to add the specifed file, and remove xliff/skeleton content 
     * This is not fantastic I'll admit - probably too much IO going on here.
     * It adds the input file to the given zip archive and removes the content.xlf
     * and skeleton.skl entries from the zip file.
     *
     * @param zipFile the file we want to add stuff to
     * @param inputFile the file we want to add
     * @param entryName the name we want to call this file in the zip archive
     */
    private void addContentRemoveXliffFromZip(File zipFile, File inputFile, String entryName) throws IOException {
        // ack, need to read + write file for this to work
        File tempZipFile = File.createTempFile("temp-",".zip");
        
        // just create the file first of all
        if (!zipFile.exists()){
            zipFile.createNewFile();
        }
        ZipInputStream is = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        ZipOutputStream os = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tempZipFile)));
        ZipEntry entry = null;
        
        // now iterate over the zipfile, skipping any files
        // we don't want to have in the output zipfile.
        while ((entry = is.getNextEntry()) != null){
            if (!(entry.getName().equals("content.xlf") ||
                  entry.getName().equals("skeleton.skl") ||
                  entry.getName().equals("workflow.properties"))){
                os.putNextEntry(entry);
                int i=0;
                while ((i=is.read())!=-1){
                    os.write(i);
                }
                os.closeEntry();
                //System.out.println("Written entry " + entry.getName());
            }
        }
        
        entry = new ZipEntry(entryName);
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(inputFile));
        os.putNextEntry(entry);
        int i=0;
        while ((i=fis.read())!=-1){
            os.write(i);
        }
        os.close();
        //System.out.println("Copying " +tempZipFile.getAbsolutePath()+" to " +zipFile.getAbsolutePath());
        copyFile(tempZipFile, zipFile);
        tempZipFile.delete();
    }
    
    /**
     * Copies a file - the source and target files must be normal files,
     * not directories. I'm not bothering to do much checking here, so ymmv
     * @param file the source file
     * @param targetFile the target file
     * @throws java.io.IOException if there was some problem when copying the file
     */
    protected void copyFile(File file, File targetFile) throws IOException{
        targetFile.getParentFile().mkdirs();
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile));
        int i = 0;
        while ((i=is.read()) != -1){
            os.write(i);
        }
        is.close();
        os.close();
    }    
}

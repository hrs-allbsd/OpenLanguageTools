
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffZipFileIO.java
 *
 * Created on August 29, 2002, 2:01 PM
 */

package org.jvnet.olt.utilities;

import java.util.zip.*;
import java.util.StringTokenizer;
import java.io.*;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;


/** A simple class to provide simple .xlz file writing. Internally, it maintains
 * Strings that represent the xliff and skeleton files. Upon initialisation, these
 * strings are filled with the contents of the xliff and skeleton file. If the user
 * asks for a writer, a StringWriter is supplied and updated by the client. Finally
 * once the client calls the writeZipFile method, the contents of the StringWriter,
 * or the original buffer are written for both the xliff and skeleton file to the
 * underlying .xlz file.<br><br>
 *
 * We now also support reading xlf files, though we still write .xlz files.
 *
 * @author timf
 */
public class XliffZipFileIO {
    
    private File file;
    
    private String xliffString;
    private String sklString;
    private String ext;
    
    private StringWriter xliffWriter;
    private StringWriter sklWriter;
    private Properties workflowProps;
    
    private Map extraFilesInZip;
    
    private boolean hasSkeleton = true;
    private boolean hasWorkflowProperties = false;
    
    
    /** A class that encapsulates an xliff file. For Open Language Tools xliff,
     * we handle the content and skeleton files as part of a zip package. This
     * class makes it easier to handle these types of files. For compatibility,
     * we also deal with .xlf files.
     * @param file The zliff file to write (must have the extension .xlz or .xlf)
     * @throws ZipException if there is some zip file handling problem
     * @throws IOException if the extension is not .xlz or .xlf or if there is some other underlying io
     * problem
     */
    public XliffZipFileIO(File file) throws java.util.zip.ZipException, IOException {
        this.file = file;
        this.xliffWriter = new StringWriter();
        this.sklWriter = new StringWriter();
        this.extraFilesInZip = new HashMap();
        // first check to see if we're looking at an xlz or an xlf file.
        this.ext = fileTypeDetection(file);
        
        if (file.canRead()&&file.exists())//fixed by Jim here
        {
            
            if (ext.equals("xlf")) {
                // if it's vanilla xliff, we don't need to worry about zip stuff
                BufferedReader isr = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                StringWriter strw = new StringWriter();
                int c = -1;
                while ((c = isr.read()) != -1) {
                    strw.write(c);
                }
                strw.flush();
                
                xliffString = strw.toString();
                sklString = "";
                this.hasSkeleton = false;
                this.hasWorkflowProperties = false;
                
            } else if (ext.equals("xlz")) {
                
                
                // this initialises our buffers
                
                ZipFile zipFile = new ZipFile(file);
                
                ZipEntry xliffEntry = zipFile.getEntry("content.xlf");
                if (xliffEntry == null) {
                    throw new java.util.zip.ZipException("Error - entry for content.xlf doesn't exist");
                }
                // InputStreamReader isr = new InputStreamReader(zipFile.getInputStream(xliffEntry),"UTF-8");
                BufferedReader isr = new BufferedReader(new InputStreamReader(zipFile.getInputStream(xliffEntry),"UTF-8"));
                StringWriter strw = new StringWriter();
                int c = -1;
                while ((c = isr.read()) != -1) {
                    strw.write(c);
                }
                strw.flush();
                
                xliffString = strw.toString();
                
                ZipEntry sklEntry = zipFile.getEntry("skeleton.skl");
                if (sklEntry == null) {
                    throw new java.util.zip.ZipException("Error - entry for skeleton.skl doesn't exist");
                }
                
                // isr = new InputStreamReader(zipFile.getInputStream(sklEntry),"UTF-8");
                isr = new BufferedReader(new InputStreamReader(zipFile.getInputStream(sklEntry),"UTF-8"));
                strw = new StringWriter();
                c = -1;
                while ((c = isr.read()) != -1) {
                    strw.write(c);
                }
                strw.flush();
                strw.flush();
                
                sklString = strw.toString();
                
                //  read in the workflow properties if they exist.
                ZipEntry wfpropsEntry = zipFile.getEntry("workflow.properties");
                if (wfpropsEntry != null) {
                    workflowProps = new Properties();
                    workflowProps.load(zipFile.getInputStream(wfpropsEntry));
                    hasWorkflowProperties = true;
                } else {
                    hasWorkflowProperties = false;
                }
                
                // finally, we look for other entries in the zip file, saving them to a temp location
                Enumeration entries = zipFile.entries();
                while (entries.hasMoreElements()){
                    ZipEntry myEntry = (ZipEntry)entries.nextElement();
                    String name = myEntry.getName();
                    if (!name.equals("workflow.properties") &&
                            !name.equals("content.xlf") &&
                            !name.equals("skeleton.skl")) {
                        // extract file to a temp area and add it's information to the hash
                        if (!myEntry.isDirectory()){
                            File tempPath = extractEntryToTempFile(zipFile, myEntry);
                            this.extraFilesInZip.put(name, tempPath);
                        }
                    }
                }
                
            } else {
                
                this.xliffString="";
                this.sklString="";
            }
        }
        
    }
    /*
     *  Maintenance note: I need to add some more constructors here to allow for
     *  conversion from objects that store XLIFF data from sources other than
     *  files. Two constructors will be needed: one for plain XLIFF and one for
     *  XLIFF with a skeleton file.
     *  -- jc73554
     *
     *  public XliffZipFileIO(XliffDocument xliff, File file) throws IOException {}
     *  public XliffZipFileIO(XliffDocument xliff, XliffDocument skl, File file) throws IOException {}
     */
    
    /**  This constructor allows for the conversion of downloaded Xliff documents
     *  into XliffZipFileIO objects. This is being done to minimize the amount of
     *  change necessary to the editor to make an online version. This version
     *  has no skeleton component.
     * @param file File to save the XLIFF content in.
     * @param xliff The object containing the XLIFF content.
     * @throws IOException Thrown if a general I/O error occurs.
     */
    public XliffZipFileIO(java.io.File file, org.jvnet.olt.xliff.XliffDocument xliff) throws java.io.IOException {
        this.file = file;
        xliffString = xliff.getDocumentText();
        sklString = "";
        this.hasSkeleton = false;
    }
    
    /** This constructor allows for the conversion of downloaded Xliff documents
     *  into XliffZipFileIO objects. This is being done to minimize the amount of
     *  change necessary to the editor to make an online version. This version
     *  has a skeleton component, as well as the XLIFF content.
     * @param file A file to store the zipped XLIFF and skeleton content in.
     * @param xliff An object containing XLIFF content.
     * @param skl An object containing formatting content.
     * @throws IOException Thrown if a general I/O error occurs.
     */
    public XliffZipFileIO(java.io.File file, org.jvnet.olt.xliff.XliffDocument xliff, org.jvnet.olt.xliff.XliffSkeletonDocument skl) throws java.io.IOException {
        this.file = file;
        xliffString = xliff.getDocumentText();
        
        Reader reader = skl.getSklReader();
        java.io.StringWriter strw = new java.io.StringWriter();
        int c;
        while ((c = reader.read()) != -1) {
            strw.write(c);
        }
        strw.flush();
        sklString = strw.toString();
        this.hasSkeleton = true;
    }
    
    public XliffZipFileIO(java.io.File file, org.jvnet.olt.xliff.XliffDocument xliff, org.jvnet.olt.xliff.XliffSkeletonDocument skl, Properties workflowProps) throws java.io.IOException {
        this(file, xliff, skl);
        setWorkflowProperties(workflowProps);
    }
    
    /** Gets an OutputStream for writing the Xliff part of an xliff file
     * @throws ZipException if there was some .zip related problem
     * @throws IOException if there was some underlying IO problem
     * @return an OutputStream used to write the xliff
     */
    public Writer getXliffWriter() throws java.util.zip.ZipException, IOException {
        this.xliffWriter = new StringWriter();
        return this.xliffWriter;
    }
    
    /** gets an W for writing the skeleton part of a .xlz file
     * @throws ZipException if there was some .zip related problem
     * @throws IOException if there was some underlying IO problem
     * @return for writing the skeleton file
     */
    public Writer getSklWriter() throws java.util.zip.ZipException, IOException {
        if (this.hasSkeleton == false) {
            throw new IOException("This XliffZipFileIO object does not have a skeleton file associated with it.");
        } else {
            this.sklWriter = new StringWriter();
            return this.sklWriter;
        }
    }
    
    /** Gets an Reader to read the xliff part of an xliff file.
     * @throws ZipException if there was some .zip related problem
     * @throws IOException if there was some underlying IO problem
     * @return for reading the Xliff file
     */
    public Reader getXliffReader() throws java.util.zip.ZipException, IOException {
        return new StringReader(xliffString);
    }
    
    
    /** Gets a Reader for the skeleton part of a .xlz file
     * @throws ZipException if there was some .zip related problem
     * @throws IOException if there was some underlying IO problem
     * @return for reading the skeleton file
     */
    public Reader getSklReader() throws java.util.zip.ZipException, IOException {
        if (this.hasSkeleton == false) {
            throw new IOException("This XliffZipFileIO object does not have a skeleton file associated with it.");
        } else {
            return new StringReader(sklString);
        }
    }
    
    /** Writes the internal buffers of the xliff and skelton files to the .xlz or .xlf
     * file (the latter does not have a skeleton file associated with it, so obviously
     * we don't try and write one)
     * @throws IOException a general I/O problem has occurred.
     * @throws ZipException if there was some error with the zip file handling
     */
    public void writeZipFile() throws java.util.zip.ZipException, IOException {
        
        // first check to see if we've written either xliff or skl content
        this.sklWriter.flush();
        String tmp = this.sklWriter.toString();
        this.sklWriter.close();
        if (tmp.length() != 0) {
            sklString = tmp;
        }
        if(sklString == null) { throw new IOException("Skeleton file data is missing!"); }
        
        this.xliffWriter.flush();
        tmp = this.xliffWriter.toString();
        this.xliffWriter.close();
        if (tmp.length() != 0) {
            xliffString = tmp;
        }
        if(xliffString == null) { throw new IOException("Xliff data is missing!"); }
        
        // now write our strings to disk
        if (ext.equals("xlz")) {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(this.file));
            ZipEntry xliffEntry = new ZipEntry("content.xlf");
            ZipEntry sklEntry = new ZipEntry("skeleton.skl");
            zos.putNextEntry(xliffEntry);
            
            StringReader reader = new StringReader(xliffString);
            BufferedWriter zosWriter = new BufferedWriter(new OutputStreamWriter(zos,"UTF-8"));
            int c = 0;
            while ((c=reader.read()) != -1) {
                zosWriter.write(c);
            }
            zosWriter.flush();
            
            reader = new StringReader(sklString);
            zos.putNextEntry(sklEntry);
            c=0;
            while ((c=reader.read()) != -1) {
                zosWriter.write(c);
            }
            zosWriter.flush();
            
            if(hasWorkflowProperties) {
                ZipEntry propsEntry = new ZipEntry("workflow.properties");
                zos.putNextEntry(propsEntry);
                workflowProps.store(zos,"Portal workflow properties");
            }
            
            // finally, go through our extraFilesInZip Map, writing the temp files back to the xlz
            Set keys = this.extraFilesInZip.keySet();
            Iterator it = keys.iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                File tempFile = (File)this.extraFilesInZip.get(key);
                ZipEntry extraFileEntry = new ZipEntry(key);
                zos.putNextEntry(extraFileEntry);
                BufferedInputStream extraFileIs = new BufferedInputStream(new FileInputStream(tempFile));
                int i=0;
                while ((i=extraFileIs.read())!=-1){
                    zos.write(i);
                }
                extraFileIs.close();
                tempFile.delete();                
            }
            
            zos.flush();
            zos.close();
        } else { // as standard, we just try and write the content.xlf
            BufferedWriter writer = new  BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file),"UTF-8"));
            StringReader reader = new StringReader(xliffString);
            int c = 0;
            while ((c=reader.read()) != -1) {
                writer.write(c);
            }
            writer.flush();
            writer.close();
        }
        
    }
    
    /** A method that lets the user know if there's a skeleton file associated with
     * this XliffZipFileIO object. By default, any .xlz file has a skeleton file
     * and any .xlf file will not have a skeleton file.
     * @return whether we have a skeleton file
     */
    public boolean hasSkeleton() {
        return this.hasSkeleton;
    }
    
    public boolean hasWorkflowProperties() {
        return this.hasWorkflowProperties;
    }
    
    
    private String fileTypeDetection(File file) {
        
        String ext = "";
        String name = file.getName();
        StringTokenizer st = new StringTokenizer(name, ".");
        while (st.hasMoreTokens()) {
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        return ext;
    }
    
    /**
     *  This method creates an object that is not shackled to a file, and which
     *  contains the XLIFF content of the this XliffZipFileIO object.
     */
    public org.jvnet.olt.xliff.PlainXliffDocument toPlainXliffDocument() throws java.io.IOException {
        String name = file.getName();
        return new org.jvnet.olt.xliff.PlainXliffDocument(xliffString, name);
    }
    
    /**
     *  This method creates an object that contains both XLIFF and skeleton
     *  content, zipped together. This object is not tied to a file.
     */
    public org.jvnet.olt.xliff.ZippedXliffDocument toZippedXliffDocument() throws java.io.IOException {
        if(!hasSkeleton) {
            throw new IOException("Internal ERROR. This XliffZipFileIO object does not have a skeleton file associated with it. Should be creating a PlainXliffDocument instead.");
        }
        String name = file.getName();
        if(hasWorkflowProperties) {
            return new org.jvnet.olt.xliff.ZippedXliffDocument(xliffString, sklString, workflowProps, name);
        } else {
            return new org.jvnet.olt.xliff.ZippedXliffDocument(xliffString, sklString, name);
        }
    }
    
    public org.jvnet.olt.xliff.XliffDocument toXliffDocument() throws java.io.IOException {
        if(hasSkeleton) {
            return toZippedXliffDocument();
        } else {
            return toPlainXliffDocument();
        }
    }
    
    public java.util.Properties getWorkflowProperties() {
        return workflowProps;
    }
    
    public void setWorkflowProperties(java.util.Properties workflowProps) {
        this.workflowProps = workflowProps;
        hasWorkflowProperties = (workflowProps != null);
    }
    
    private File extractEntryToTempFile(ZipFile zipFile, ZipEntry entry) throws java.io.IOException{
        
        File tempFile = File.createTempFile("zipcontents.","bin");
        String filename = entry.getName();
        
        BufferedInputStream in = new BufferedInputStream(
                zipFile.getInputStream(entry));
        
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(tempFile));
        
        int i = 0;
        while ((i = in.read()) != -1){
            out.write(i);
        }
        in.close();
        out.flush();
        out.close();
        return tempFile;
    }
}

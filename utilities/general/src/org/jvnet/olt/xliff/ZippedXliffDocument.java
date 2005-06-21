
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ZippedXliffDocument.java
 *
 * Created on 26 March 2003, 18:51
 */

package org.jvnet.olt.xliff;

import java.io.*;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 *
 * @author  jc73554
 */
public class ZippedXliffDocument implements XliffDocument, XliffSkeletonDocument, WorkflowProperties
{
    private String docName;
    
    private String xliffData;
    
    //  The assumption here is that the skeleton file is in some text format
    //  and not a binary one.
    private String sklData;
    
    private Properties workflowProps;
    
    private boolean propsIncluded;
    
    private StringWriter sklWriter;
    
    private StringWriter xliffWriter;
    
    private byte[] codFileBuffer;
    
    /** Creates a new instance of ZippedXliffDocument */
    public ZippedXliffDocument(byte[] content, String name) throws IOException
    {
        docName = name;
        
        //  Create byte stream on the data.
        ByteArrayInputStream byteStream = new ByteArrayInputStream(content);
        readFrom(byteStream);
    }
    
    public ZippedXliffDocument(java.lang.String xliffContent, java.lang.String sklContent, String name) throws IOException
    {
        docName = name;
        xliffData = xliffContent;
        sklData = sklContent;
        workflowProps = new Properties();
    }
    
    public ZippedXliffDocument(java.lang.String xliffContent, java.lang.String sklContent, Properties workflowProps, String name) throws IOException
    {
        docName = name;
        xliffData = xliffContent;
        sklData = sklContent;
        this.workflowProps = workflowProps;
        propsIncluded = true;
    }
    
    
    public String getDocumentText()
    {
        return xliffData;
    }
    
    public void setDocumentText(String text)
    {
        xliffData = text;
    }
    
    public String getDocumentName()
    {
        return docName;
    }
    
    
    public String getMimeType()
    {
        return "application/x-xliff-archive";
    }
    
    public byte[] getBytes() throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writeTo(byteStream);
        return byteStream.toByteArray();
    }
    
    protected void writeDataToZipStream(ZipStreamWriter zipWriter, String name, String data) throws IOException
    {
        ZipEntry zipEntry = new ZipEntry(name);
        zipWriter.putNextEntry(zipEntry);
        StringReader reader = new StringReader(data);
        int ch;
        while((ch = reader.read()) != -1)
        {
            zipWriter.write(ch);
        }
        zipWriter.flush();
    }
    
    protected void writeDataToZipStream(ZipStreamWriter zipWriter, String name, Properties data) throws IOException
    {
        ZipEntry zipEntry = new ZipEntry(name);
        zipWriter.putNextEntry(zipEntry);
        data.store(zipWriter.getZipOutputStream(), "Portal workflow data.");
        zipWriter.flush();
    }
    
    
    protected void commitXliffDataWrites()
    {
        if(xliffWriter != null)
        {
            xliffData = xliffWriter.toString();
        }
    }
    
    protected void commitSkeletonDataWrites()
    {
        if(sklWriter != null)
        {
            sklData = sklWriter.toString();
        }
    }
    
    
    public Writer getXliffWriter() throws IOException
    {
        xliffWriter = new StringWriter();
        return xliffWriter;
    }
    
    public Reader getXliffReader() throws IOException
    {
        commitXliffDataWrites();
        return new StringReader(xliffData);
    }
    
    public void writeTo(File outFile) throws IOException
    {
        FileOutputStream fostream = new FileOutputStream(outFile);
        writeTo(fostream);
    }
    
    public void writeTo(OutputStream ostream) throws IOException
    {
        commitXliffDataWrites();
        commitSkeletonDataWrites();
        
        ZipOutputStream zipStream = new ZipOutputStream(ostream);
        ZipStreamWriter zipWriter = new ZipStreamWriter(zipStream);
        writeDataToZipStream(zipWriter, "content.xlf", xliffData);
        writeDataToZipStream(zipWriter, "skeleton.skl", sklData);
        if(hasWorkflowProperties())
        {
            writeDataToZipStream(zipWriter, "workflow.properties", workflowProps);
        }
        zipWriter.flush();
        zipWriter.close();
    }
    
    public void readFrom(InputStream istream) throws IOException
    {
        //  Create ZipInputStream
        ZipInputStream zipStream = new ZipInputStream(istream);
        ZipStreamReader zipReader = new ZipStreamReader(zipStream);
        
        //  Loop through stream for entries
        ZipEntry zipEntry;
        while((zipEntry = zipReader.getNextEntry()) != null)
        {
            String zipEntryName = zipEntry.getName();
            
            //  Only looking for certain parts of the zip archive. Any other parts
            //  will cause an exception to be thrown.
            if(zipEntryName.equals("content.xlf")  ||
            zipEntryName.equals("skeleton.skl"))
            {
                StringBuffer buffer = new StringBuffer();
                int ch;
                while((ch = zipReader.read()) != -1)
                {
                    buffer.append((char) ch);
                }
                if(zipEntryName.equals("content.xlf"))
                {
                    xliffData = buffer.toString();
                }
                else
                {
                    sklData = buffer.toString();
                }
            }
            else if(zipEntryName.equals("workflow.properties"))
            {
                workflowProps = new Properties();
                workflowProps.load(zipStream);
                propsIncluded = true;
            }
            else
            {
                throw new IOException("Stream contained unexpected data: a zip entry is present that isn't content.xlf or skeleton.skl");
            }
        }
    }
    
    public void readFrom(File inFile) throws IOException
    {
        FileInputStream fistream = new FileInputStream(inFile);
        readFrom(fistream);
    }
    
    
    //  XliffSkeletonDocument Interface
    public Writer getSklWriter() throws IOException
    {
        sklWriter = new StringWriter();
        return sklWriter;
    }
    
    public Reader getSklReader() throws IOException
    {
        commitSkeletonDataWrites();
        return new StringReader(sklData);
    }
    
    /**  Gets the workflow properties.
     */
    public Properties getWorkflowProperties()
    {
        return workflowProps;
    }
    
    /**  Sets the workflow properties
     * @param props The properties to be set to facilitate workflow for this file.
     */
    public void setWorkflowProperties(Properties props)
    {
        workflowProps = props;
        propsIncluded = true;
    }
    
    public boolean hasWorkflowProperties()
    {
        return propsIncluded;
    }
    
    public boolean hasSkeletonDocument()
    {
        return true;
    }
    
    public WorkflowProperties getWorkflowPropertiesInterface() throws UnsupportedOperationException
    {
        return ( (WorkflowProperties) this );
    }
    
    public XliffSkeletonDocument getSkeletonDocument() throws UnsupportedOperationException
    {
        return ( (XliffSkeletonDocument) this );
    }
    
    public String getProperty(String key)
    {
        if(hasWorkflowProperties())
        {
            return this.workflowProps.getProperty(key);
        }
        else
        {
            return null;
        }
    }
    
    public void setSkeletonData(XliffSkeletonDocument sourceSkeletonDoc) throws UnsupportedOperationException, IOException
    {
        Reader reader = sourceSkeletonDoc.getSklReader();
        Writer writer = this.getSklWriter();
        int ch = 0;
        while((ch = reader.read()) != -1)
        {
            writer.write((char)ch);
        }
        this.commitSkeletonDataWrites();
    }
    
}

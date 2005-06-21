
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * PlainXliffDocument.java
 *
 * Created on 21 March 2003, 16:26
 */

package org.jvnet.olt.xliff;

import java.io.*;
/**
 *
 * @author  jc73554
 */
public class PlainXliffDocument implements XliffDocument
{
    private String documentContent;
    private StringWriter contentWriter;
    private String docName;
    
    /** Creates a new instance of PlainXliffDocument */
    public PlainXliffDocument(String content, String name)
    {
        documentContent = content;
        docName = name;
    }

    public String getDocumentText()
    {
        commitWrites();
        return documentContent;
    }
    
    public void setDocumentText(String text)
    {
        contentWriter = null;
        documentContent = text;
    }
    
    public String getDocumentName()
    {
        return docName;
    }
        
    public String getMimeType()
    {
        return "application/x-xml-xliff";
    }
    
    public byte[] getBytes() throws IOException
    {
        commitWrites();
        return documentContent.getBytes("UTF8");
    }
    
    public Writer getXliffWriter() throws IOException
    {
        commitWrites();
        contentWriter = new StringWriter();
        return contentWriter;
    }

    protected void commitWrites()
    {
        if(contentWriter != null)
        {
            documentContent = contentWriter.toString();
        }
    }
    
    public Reader getXliffReader() throws IOException
    {
        commitWrites();
        return new StringReader(documentContent);
    }
    
    public void writeTo(File outFile) throws IOException
    {
        FileOutputStream fostream = new FileOutputStream(outFile);
        writeTo(fostream);
    }
    
    public void writeTo(OutputStream ostream) throws IOException
    {
        commitWrites();
        OutputStreamWriter writer = new OutputStreamWriter(ostream, "UTF8");
        writer.write(documentContent);
        writer.flush();
        writer.close();
    }
    
    public void readFrom(InputStream istream) throws IOException
    {
        StringBuffer buffer = new StringBuffer();
        InputStreamReader reader = new InputStreamReader(istream, "UTF8");
        int ch;
        while((ch = reader.read()) != -1)
        {
            buffer.append((char) ch);
        }
    }
    
    public void readFrom(File inFile) throws IOException
    {
        FileInputStream fistream = new FileInputStream(inFile);
        readFrom(fistream);
    }
    
    
    public boolean hasWorkflowProperties()
    {
        return false;
    }

    public boolean hasSkeletonDocument()
    {
        return false;
    }

    public WorkflowProperties getWorkflowPropertiesInterface() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("The PlainXliffDocument doesn't contain WorkflowProperties.");
    }
    
    public XliffSkeletonDocument getSkeletonDocument() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("The PlainXliffDocument doesn't contain an XliffSkeletonDocument.");
    }
    
    public void setSkeletonData(XliffSkeletonDocument sourceSkeletonDoc) throws UnsupportedOperationException, IOException
    {
        throw new UnsupportedOperationException("It is not possible to add skeleton data to a PlainXliffDocument.");
    }
    
}

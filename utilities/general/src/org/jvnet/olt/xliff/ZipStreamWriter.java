
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ZipStreamWriter.java
 *
 * Created on 13 August 2003, 14:47
 */

package org.jvnet.olt.xliff;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 *
 * @author  jc73554
 */
public class ZipStreamWriter extends Writer
{
    
    private ZipOutputStream zipOutputStream;
    
    private BufferedWriter writer;
    
    /** Creates a new instance of ZipStreamWriter */
    public ZipStreamWriter(ZipOutputStream zipStream) throws UnsupportedEncodingException
    {
        this(zipStream, "UTF-8");
    }
    
    public ZipStreamWriter(ZipOutputStream zipStream, java.lang.String encoding) throws UnsupportedEncodingException
    {
        zipOutputStream = zipStream;
        writer = new BufferedWriter(new OutputStreamWriter(zipOutputStream, encoding));
    }
    
    public void close() throws IOException
    {
        writer.close();
    }
    
    public void flush() throws IOException
    {
        writer.flush();
    }
    
    public void write(char[] cbuf) throws IOException
    {
        writer.write(cbuf);
    }
    
    public void write(int c) throws IOException
    {
        writer.write(c);
    }
    
    public void write(String str) throws IOException
    {
        writer.write(str);
    }
    
    public void write(char[] cbuf, int off, int len) throws IOException
    {
        writer.write(cbuf, off, len);
    }
    
    public void write(String str, int off, int len) throws IOException
    {
        writer.write(str, off, len);        
    }
    
    public void putNextEntry(ZipEntry entry) throws IOException
    {
        zipOutputStream.putNextEntry(entry);
    }
    
    public ZipOutputStream getZipOutputStream()
    {
        return zipOutputStream;
    }
}

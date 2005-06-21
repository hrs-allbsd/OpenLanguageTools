
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ZipStreamReader.java
 *
 * Created on 13 August 2003, 15:15
 */

package org.jvnet.olt.xliff;

import java.io.*;
import java.util.zip.ZipInputStream;


/**
 *
 * @author  jc73554
 */
public class ZipStreamReader extends Reader
{
    
    private ZipInputStream zipInputStream;
    
    private BufferedReader reader;
    
    /** Creates a new instance of ZipStreamReader */
    public ZipStreamReader(ZipInputStream zipStream) throws UnsupportedEncodingException
    {  
        this(zipStream, "UTF-8");
    }
    
    public ZipStreamReader(ZipInputStream zipStream, String encoding) throws UnsupportedEncodingException
    {
        zipInputStream = zipStream;
        reader = new BufferedReader(new InputStreamReader(zipInputStream, encoding));
    }
    
    public void close() throws IOException
    {
        reader.close();
    }
    
    public void mark(int readAheadLimit) throws IOException
    {
        reader.mark(readAheadLimit);
    }
    
    public boolean markSupported()
    {
        return reader.markSupported();
    }
    
    public int read() throws IOException
    {
        return reader.read();
    }
    
    public int read(char[] cbuf) throws IOException
    {
        return reader.read(cbuf);
    }
    
    public int read(char[] cbuf, int off, int len) throws IOException
    {
        return reader.read(cbuf, off, len);        
    }
    
    public boolean ready() throws IOException
    {
        return reader.ready();
    }
    
    public void reset() throws IOException
    {
        reader.reset();
    }
    
    public long skip(long n) throws IOException
    {
        return reader.skip(n);
    }
    
    public java.util.zip.ZipEntry getNextEntry() throws IOException
    {
        return zipInputStream.getNextEntry();
    }
}

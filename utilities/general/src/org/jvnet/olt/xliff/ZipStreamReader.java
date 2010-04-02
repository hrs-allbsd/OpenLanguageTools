/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/

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

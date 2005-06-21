
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmci;

import java.io.*;

/**
 *  This class throws an exception if there is a Control-D character in 
 *  the stream.
 */
public class ControlDWarningFilterReader 
extends FilterReader
{
  private static final int EOF = -1;

  private Reader reader;
  private boolean boolEndOfStream;

  public ControlDWarningFilterReader(Reader reader)
  {
    super(reader);
    this.reader = reader;
    boolEndOfStream = false;
  }

  public int read() throws IOException
  {
    int cval = 0;

    cval = reader.read();
    switch(cval)
    {
    case EOF:
      return -1;
    case 4:  //  A Ctrl-D character has been encountered.
      throw new ControlDReadException("A Control-D character has been read");
    }
    return cval;
  }

  public int read(char[] cbuf) throws IOException
  {
    int pos = 0;
    int cval = 0;

    if(boolEndOfStream) { return -1; } 

    while(pos < cbuf.length)
    {
      cval = this.read();
      cbuf[pos++] = (char) cval;
      if(cval == -1)
      {
	boolEndOfStream = true;
	return pos - 1;     //  Chop off the EOF
      }
    }
    return pos;
  }

  public int read(char[] cbuf,int off, int len) throws IOException
  {
    int pos = off;
    int cval = 0;

    if(boolEndOfStream) { return -1; } 

    while((pos < cbuf.length) && ((pos - off) < len))
    {
      cval = this.read();
      cbuf[pos++] = (char) cval;
      if(cval == -1)
      {
	boolEndOfStream = true;
	return (pos - off) - 1;  //  Chop off the EOF
      }    
    }
    return (pos - off);
  }


  public void reset() throws IOException
  {
    reader.reset();
  }

  public boolean ready() throws IOException
  {
    return reader.ready();
  }

  public boolean markSupported()
  {
    return reader.markSupported();
  }

  public void mark(int readAheadLimit) throws IOException
  {
    reader.mark(readAheadLimit);
  }

  public long skip(long n) throws IOException
  {
    return reader.skip(n);
  }
}

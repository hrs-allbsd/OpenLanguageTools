
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.io;

import java.io.*;

public class HTMLEscapeFilterReader extends FilterReader
{
  private static final int DEFAULT = 0;
  private static final int READING_AMPERSAND = 1;  
  private static final int READING_OPEN_BRACE = 2;
  private static final int READING_CLOSE_BRACE = 3;

  private static final char[] AMP_ARR = {'a','m','p',';'};
  private static final char[] LT_ARR = {'l','t',';'};
  private static final char[] GT_ARR = {'g','t',';'};

  private static final int EOF = -1;
  private static final int AMP = (int) '&';   //  '&' character;
  private static final int GT = (int) '>';    //  '>' character;
  private static final int LT = (int) '<';    //  '<' character;


  private int m_iState;
  private int m_iCharRead;
  private Reader reader;
  private boolean boolEndOfStream;

  public HTMLEscapeFilterReader(Reader reader)
  {
    super(reader);
    this.reader = reader;
    m_iState = DEFAULT;
    m_iCharRead = 0;
    boolEndOfStream = false;
  }

  public int read() throws IOException
  {
    int cval = 0;

    switch(m_iState)
    {
    case DEFAULT:
      cval = reader.read();
      switch(cval)
      {
      case EOF:
	return -1;
      case AMP:
	m_iState = READING_AMPERSAND;
	return AMP;
      case GT:
	m_iState = READING_CLOSE_BRACE;
	return AMP;
      case LT:
	m_iState = READING_OPEN_BRACE;
	return AMP;
      }
      break;
    case READING_AMPERSAND:
      cval = (int) AMP_ARR[m_iCharRead++];
      if(m_iCharRead > 3)
      {
	m_iCharRead = 0;
	m_iState = DEFAULT;
      }
      break;
    case READING_OPEN_BRACE:
      cval = (int) LT_ARR[m_iCharRead++];
      if(m_iCharRead > 2)
      {
	m_iCharRead = 0;
	m_iState = DEFAULT;
      }
      break;
    case READING_CLOSE_BRACE:
      cval = (int) GT_ARR[m_iCharRead++];
      if(m_iCharRead > 2)
      {
	m_iCharRead = 0;
	m_iState = DEFAULT;
      }
      break;
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

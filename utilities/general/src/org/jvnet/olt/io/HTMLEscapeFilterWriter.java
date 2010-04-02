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

package org.jvnet.olt.io;

import java.io.*;

public class HTMLEscapeFilterWriter extends FilterWriter
{
  private static final int AMP = (int) '&';   //  '&' character;
  private static final int GT = (int) '>';    //  '>' character;
  private static final int LT = (int) '<';    //  '<' character;

  private int m_iState;
  private int[] m_buffer;
  private int m_index;

  public HTMLEscapeFilterWriter(Writer writer)
  {
    super(writer);
    m_iState = 1;
    m_buffer = new int[5];
    m_index = 0;
  }

  public void write(int c) throws IOException
  {
    switch(m_iState)
    {
    case 1:
      if(c == AMP) 
      {
	m_iState = 2;
	addToLookAheadBuffer(c);	
      }
      else { out.write(c); }
      break;
    case 2:
      switch(c)
      {
      case AMP:
	flushLookAheadBuffer();
	addToLookAheadBuffer(c);
	break;
      case (int) 'a':
	m_iState = 3;
	addToLookAheadBuffer(c);
	break;
      case (int) 'l':
	m_iState = 6;
	addToLookAheadBuffer(c);
	break;
      case (int) 'g':
	m_iState = 8;
	addToLookAheadBuffer(c);
	break;
      default:
	m_iState = 1;
	addToLookAheadBuffer(c);  //  Make sure we write out current char
        flushLookAheadBuffer();
	break;
      }
      break;
    case 3:
      switch(c)
      {
      case AMP:
	m_iState = 2;
	flushLookAheadBuffer();
	addToLookAheadBuffer(c);
	break;
      case (int) 'm':
	m_iState = 4;
	addToLookAheadBuffer(c);
	break;
      default:
	m_iState = 1;
	addToLookAheadBuffer(c);  //  Make sure we write out current char
        flushLookAheadBuffer();
	break;
      }
      break;
    case 4:
      switch(c)
      {
      case AMP:
	m_iState = 2;
	flushLookAheadBuffer();
	addToLookAheadBuffer(c);
	break;
      case (int) 'p':
	m_iState = 5;
	addToLookAheadBuffer(c);
	break;
      default:
	m_iState = 1;
	addToLookAheadBuffer(c);  //  Make sure we write out current char
        flushLookAheadBuffer();
	break;
      }
      break;
    case 5:
     switch(c)
      {
      case AMP:
	m_iState = 2;
	flushLookAheadBuffer();
	addToLookAheadBuffer(c);
	break;
      case (int) ';':
	m_iState = 1;
	out.write(AMP);
	voidLookAheadBuffer();
	break;
      default:
	m_iState = 1;
	addToLookAheadBuffer(c);  //  Make sure we write out current char
        flushLookAheadBuffer();
	break;
      }
      break;      
    case 6:
      switch(c)
      {
      case AMP:
	m_iState = 2;
	flushLookAheadBuffer();
	addToLookAheadBuffer(c);
	break;
      case (int) 't':
	m_iState = 7;
	addToLookAheadBuffer(c);
	break;
      default:
	m_iState = 1;
	addToLookAheadBuffer(c);  //  Make sure we write out current char
        flushLookAheadBuffer();
	break;
      }
      break;
    case 7:
      switch(c)
      {
      case AMP:
	m_iState = 2;
	flushLookAheadBuffer();
	addToLookAheadBuffer(c);
	break;
      case (int) ';':
	m_iState = 1;
	out.write(LT);
	voidLookAheadBuffer();
	break;
      default:
	m_iState = 1;
	addToLookAheadBuffer(c);  //  Make sure we write out current char
        flushLookAheadBuffer();
	break;
      }
      break;      
    case 8:
      switch(c)
      {
      case AMP:
	m_iState = 2;
	flushLookAheadBuffer();
	addToLookAheadBuffer(c);
	break;
      case (int) 't':
	m_iState = 9;
	addToLookAheadBuffer(c);
	break;
      default:
	m_iState = 1;
	addToLookAheadBuffer(c);  //  Make sure we write out current char
        flushLookAheadBuffer();
	break;
      }
      break;
    case 9:
      switch(c)
      {
      case AMP:
	m_iState = 2;
	flushLookAheadBuffer();
	addToLookAheadBuffer(c);
	break;
      case (int) ';':
	m_iState = 1;
	out.write(GT);
	voidLookAheadBuffer();
	break;
      default:
	m_iState = 1;
	addToLookAheadBuffer(c);  //  Make sure we write out current char
        flushLookAheadBuffer();
	break;
      }
      break;      
    }
  }

  public void write(char[] cbuf,int off, int len) throws IOException
  {
    int i = off;
    while((i < cbuf.length) && ((i - off) < len))
    {
      this.write((int) cbuf[i++]);
    }
  }


  public void write(String str,int off, int len) throws IOException
  {
    try
    {
      int i = off;
      while((i < str.length()) && ((i - off) < len))
      {
	this.write((int) str.charAt(i++));
      }
    }
    catch(StringIndexOutOfBoundsException ex)
    {
      System.err.println(ex);
    } 
  }

  public void write(String str) throws IOException
  {
    this.write(str,0,str.length());
  }

  public void close() throws IOException
  {
    flushLookAheadBuffer();
    out.close();
  }


  public void flush() throws IOException
  {
    out.flush();
  }

  private void flushLookAheadBuffer() throws IOException
  {
    for(int i = 0; i < m_index; i++)
    {
      out.write(m_buffer[i]);
      m_buffer[i] = '\0';
    }
    m_index = 0;
  }

  private void voidLookAheadBuffer()
  {
    for(int i = 0; i < m_index; i++)
    {
      m_buffer[i] = '\0';
    }
    m_index = 0;
  }

  private void addToLookAheadBuffer(int c)
  {
    m_buffer[m_index++] = c;
  }
}

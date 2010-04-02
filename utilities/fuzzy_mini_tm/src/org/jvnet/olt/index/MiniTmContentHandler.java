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

package org.jvnet.olt.index;

import java.util.Hashtable;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.jvnet.olt.minitm.TMUnit;

public class MiniTmContentHandler
extends org.xml.sax.helpers.DefaultHandler
{
  //  Items that exist "outside" this class.
  private Hashtable tmUnits;
  private IdSequence idSequence;
  private StringBuffer buffer;
  private boolean boolInPcdata;

  private String miniTmName = "";
  private String srclang = "";
  private String tgtlang = "";

  private Locator locator = null;

  private String currentSource;
  private String currentTranslation;
  private String currentTranslatorId;

  public MiniTmContentHandler(Hashtable hash, IdSequence sequence)
  {
    tmUnits = hash;
    idSequence = sequence;
    buffer = new StringBuffer();
    boolInPcdata = false;
  }

  public String getTmName()
  {
    return miniTmName;
  }

  public String getSourceLang()
  {
    return srclang;
  }

  public String getTargetLang()
  {
    return tgtlang;
  }

  public void startDocument() throws SAXException { /* no op */; }
  public void endDocument() throws SAXException   { /* no op */; }

  public void startElement(String nsUri, String localName, String fullName, Attributes attlist) 
     throws SAXException 
  {
    //  If we have the minitm element then read its attributes.
    if(localName.equals("minitm"))
    {
      miniTmName = attlist.getValue("name");
      srclang = attlist.getValue("srclang");
      tgtlang = attlist.getValue("tgtlang");   
    }

    //  Reset the three string fields.
    if(localName.equals("entry"))
    {
      currentSource = "";
      currentTranslation = "";
      currentTranslatorId = "";
    }

    //  If we are in a PCDATA holding element, raise the flag.
    if(localName.equals("source") || 
       localName.equals("translation") ||
       localName.equals("translatorId") ) 
    {
      boolInPcdata = true;
    }
  }

  public void endElement(String nsUri, String localName, String fullName) 
    throws SAXException 
  {
    //  If the entry element has closed
    if(localName.equals("entry"))
    {
      long id = idSequence.getNextId();
      TMUnit unit = new TMUnit( id, 
				currentSource,
				currentTranslation,
				currentTranslatorId);
      tmUnits.put( new Long(id), unit);
    }

    if(localName.equals("source"))
    { 
      currentSource = buffer.toString();
      buffer = new StringBuffer();
    } 

    if(localName.equals("translation"))
    { 
      currentTranslation = buffer.toString();
      buffer = new StringBuffer();
    }

    if(localName.equals("translatorId") ) 
    {
      currentTranslatorId = buffer.toString();
      buffer = new StringBuffer();
    }

    boolInPcdata = false;
  }

  public void characters(char[] ch, int start, int length)
    throws SAXException 
  {
    dealWithPcdata(ch, start, length);
  }
  
  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException 
  {
    dealWithPcdata(ch, start, length);
  }

  protected void dealWithPcdata(char[] ch, int start, int length)
    throws SAXException 
  {
    try
    {
      if(boolInPcdata)
      {
	for(int i = 0; i < length; i++)
	{
	  buffer.append(ch[start + i]);
	}
      }
    }
    catch(ArrayIndexOutOfBoundsException ex) 
    {
      throw new SAXException( ex.getMessage());
    }
  }
}

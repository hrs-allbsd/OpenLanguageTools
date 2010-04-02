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

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import java.io.StringReader;
import java.io.IOException;
import java.util.Stack;
import java.util.EmptyStackException;

public class NewlineRemovingVisitor 
implements SgmlDocFragmentParserVisitor, SgmlDocFragmentParserTreeConstants
{
  private boolean m_boolTagContainsPcdata;
  private boolean m_boolInPreservingTag;

  private Stack m_tagStack;
  private TagTable tagTable;

  public NewlineRemovingVisitor()
  {
    //  Initialize helper objects here!
    tagTable = new DocbookTagTable();
    m_tagStack = new Stack();
    m_boolTagContainsPcdata = false;
    m_boolInPreservingTag = false;
  }

  public Object visit(SimpleNode node, Object data)
  {
    String str;
    switch(node.getType())
    {
    case JJTOPEN_TAG:
      str = removeNewlinesAndWhiteSpace(node.getNodeData(), false);
      node.setNodeData(str);
      processOpeningTag(node.getTagName());
      break;
    case JJTCLOSE_TAG:
      str = removeNewlinesAndWhiteSpace(node.getNodeData(), false);
      node.setNodeData(str);
      processClosingTag(node.getTagName());
      break;
    case JJTPCDATA:
      if(canRemoveNewlines())
      {
	str = removeNewlinesAndWhiteSpace(node.getNodeData(), true);
	node.setNodeData(str);
      }
      break;
    default:
      break;
    }
    return data;
  }

  public boolean canRemoveNewlines()
  {
    return (m_boolTagContainsPcdata && !m_boolInPreservingTag);
  }

  /*
   *  Note to maintainers: This method is here purely for convenience. A
   *  possible improvement to this tool would be to extract this method out
   *  behind a Strategy interface. This would cancel the need for the
   *  removeWhitespace parameter, as different strategies could be used in 
   *  different places.
   */
  public String removeNewlinesAndWhiteSpace(String string, boolean removeWhitespace)
  {
    StringBuffer bufOut = new StringBuffer();
    StringReader reader = new StringReader(string);

    final int DEFAULT = 0;
    final int WS = 1;
    final int CR_AFTER_WS = 2;
    final int CR = 3;

    int state = DEFAULT;

    //  State machine
    //
    //   +----+-------+-------+-------+--------+
    //   ||||||  WS   |  CR   |  DEF  | WS_CR  |
    //   +----+-------+-------+-------+--------+
    //   | ws |  WS   |  WS   |  WS   |  WS    |
    //   |    |  (ws) |  (ws) |  (ws) |  (ws)  |
    //   |    |   or  |   or  |   or  |   or   |
    //   |    |   ()  |  (' ')|  (' ')|    ()  |
    //   +----+-------+-------+-------+--------+
    //   | cr | WS_CR |  CR   |  CR   | WS_CR  |
    //   |    |   ()  |   ()  |   ()  |   ()   |
    //   +----+-------+-------+-------+--------+
    //   | ch |  DEF  |  DEF  |  DEF  |  DEF   |
    //   |    |   (ch)|(' ' + |  (ch) |  (ch)  |
    //   |    |       |   ch) |       |        |
    //   +----+-------+-------+-------+--------+
    // 
    //   DEF = default state
    //   WS  = white space state
    //   ws  = white space character
    //   CR  = Carriage Return state
    //   cr  = Carriage Return character
    //   WS_CR  = carriage return after white space state
    //   ch  = Any character that is not ws or CR
    //
    //   The table represents the transitions that occur
    //   when the characters are read in when the system 
    //   is in a given state. The top row represents the
    //   current state. The entries in the table represent 
    //   the new states. The stuff in brackets is what is 
    //   written out on transition to the new state. 
    //
    int ch = 0;

    try
    {    
      while((ch = reader.read()) != -1)
      {
	switch(ch)
        {
	case (int) ' ':
	case (int) '\t':   //  White space read. 
	  if(removeWhitespace)
	  {
	    //  Normalize whitespace, i.e., only output the first
	    //  whitespace chracter. Ensure it is a space. This
	    //  will be the case unless we are in the states
	    //  WS or CR_AFTER_WS.
	    if( !( state == WS || 
		   state == CR_AFTER_WS) ) 
	    {
	      bufOut.append(' '); 
	    }
	  }
	  else
	  {
	    //  Leave whitespace alone if removeWhitespace is false.
	    bufOut.append((char) ch);
	  }
	  state = WS;
	  break;
	case (int) '\n':
	  if( state == WS ||
	      state == CR_AFTER_WS)
	  {
	    state = CR_AFTER_WS;
	  }
	  else
	  {
	    state = CR;
	  }
	  break;
	default:
	  //  Test to see if we are in a situation where removing the carriage
	  //  returns would concatenate words.
	  if( state == CR ) { bufOut.append(' '); }
	  bufOut.append((char) ch);
	  state = DEFAULT;
	  break;	
	}
      }
      //  Should we append a space if we end in the CR state?, i.e.
      if( state == CR ) { bufOut.append(' '); } 
    }
    catch(IOException ioEx)
    {
      ioEx.printStackTrace();
    }
    return bufOut.toString();
  }

  protected void processOpeningTag(String tagName)
  {
    //  This is SGML here. Normalize tagnames to lower case!
    tagName = tagName.toLowerCase();

    //  Ignore empty tags. they do not need to go on the stack.
    //  An example of an empty tag would be an XREF tag.
    if(tagTable.tagEmpty(tagName)) { return; }

    //  Test tag to see if it contains PCDATA
    m_boolTagContainsPcdata = tagTable.tagMayContainPcdata(tagName);

    //  Test to see if the tag expects verbatim layout. All child tags,
    //  i.e., all tags above this in the stack will keep their white 
    //  space.
    if(tagTable.tagForcesVerbatimLayout(tagName))
    {
      m_boolInPreservingTag = true;    
    }

    //  Push tag name onto the stack
    m_tagStack.push(tagName);  
  }

  protected void processClosingTag(String tagName)
  {
    //  This is SGML here. Normalize tagnames to lower case!
    tagName = tagName.toLowerCase();

    //  Pop tags off the stack until it is empty or until the name tag is found.
    //  Pop next tag of the stack, test it and put it back on. If the stack is empty
    //  then set the flags to false.
    try
    {
      //  If the tag name is an empty string then we have found
      //  the tag. Due to tag minimization it is the last open tag 
      //  encountered and therefore on top of the stack.
      boolean boolTagFound = tagName.equals("");

      //  If we have found a tag at this stage it is due to tag minimization,
      //  i.e., the "<bold>Text in bold</>" case.
      //  Therefore we need to pop the current open tag off the stack
      if( !m_tagStack.empty()  && boolTagFound ) { popTagWithTest(); }

      //  Pop tags off the stack until we encounter the tag that
      //  the parser found. It is needed to cover the following case.
      //    <p>Some text <b>in bold <i>italic <u>underlined. </p>
      //  All the tags need to be popped off when </p> is encountered. 
      String strPoppedTag;
      while( !(boolTagFound || m_tagStack.empty()) )
      {
	strPoppedTag = (String) popTagWithTest();
	boolTagFound = strPoppedTag.equals(tagName);
      }

      //  Found the tag name on the stack. Implicitly close the others.
      //  Now to test the currently open tag.
      if(m_tagStack.empty())
      {
	m_boolTagContainsPcdata = false;
	m_boolInPreservingTag = false;
      }
      else
      {
	String testTag = (String) m_tagStack.pop();
	m_boolTagContainsPcdata = tagTable.tagMayContainPcdata(testTag);
	m_tagStack.push(testTag);  //  Put the tag back on again.
      }
    }
    catch(EmptyStackException ex)
    {
      ex.printStackTrace();
    }
  }

  protected String popTagWithTest()
    throws EmptyStackException
  {
    String tag = (String) m_tagStack.pop();

    //  Test to see if we popped a whitespace preserving tag
    //  off the stack. Using a boolean here as these guys can't
    //  be nested.
    if(tagTable.tagForcesVerbatimLayout(tag))
    {
      m_boolInPreservingTag = false;
    }
    return tag;
  }

}

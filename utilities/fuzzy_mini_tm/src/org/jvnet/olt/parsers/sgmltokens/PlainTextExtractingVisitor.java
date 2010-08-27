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

package org.jvnet.olt.parsers.sgmltokens;

import java.util.*;

/**
 *  This class is a visitor class the is responsible for separating
 *  the plain text data from the markup in a string containing SGML
 *  tags.
 */
public class PlainTextExtractingVisitor
implements SimpleSGMLTokenizerVisitor, SimpleSGMLTokenizerTreeConstants
{
  private StringBuffer buffer;
  private List markupList;
  private boolean inIgnoreSection = false;

  /**
   *  This field records how deep in the tag stack we are.
   */
  private int tagDepth = 0;

  public PlainTextExtractingVisitor()
  {
    buffer = new StringBuffer();
    markupList = new LinkedList();
  }

  /**
   *  The visit method.
   *  @param node The node in the tree being visited.
   *  @param data Extra data to be passed between nodes. This is not used.
   */
  public Object visit(SimpleNode node, Object data)
  {
    String textData = node.getNodeData();

    MarkupEntry entry;
    switch(node.getType())
    {
    case JJTPCDATA:
      if(!inIgnoreSection)
      {
	buffer.append(textData);
      }
      else
      {
	//  If we are in an ignored marked section treat the text as
	//  if it we markup data.
        textData = textData.toLowerCase();
	entry = new MarkupEntry(textData, false);
	markupList.add(entry);
      }
      break;
    case JJTOPEN_TAG:
      textData = textData.toLowerCase();
      entry = new MarkupEntry(textData, node.hasAttribs());
      markupList.add(entry);
      if(textData.equals("<sunw_format type=\"ignore\">"))
      {
	inIgnoreSection = true;
	tagDepth++;
      }
      break;
    case JJTCLOSE_TAG:
      textData = textData.toLowerCase();
      entry = new MarkupEntry(textData, false);
      markupList.add(entry);
      if(textData.equals("</sunw_format>"))
      {
	if(tagDepth > 0) { tagDepth--; }
	if(tagDepth <= 0)
	{
	  inIgnoreSection = false;
	  tagDepth = 0;
	}
      }
      break;
    case JJTCDATA_SECTION:
    case JJTMARKED_SECTION:
    case JJTSECTION_END:
    case JJTCOMMENT:
    case JJTPROCESSING_INST:
      textData = textData.toLowerCase();
      entry = new MarkupEntry(textData, false);
      markupList.add(entry);
      break;
    default:
      break;
    }
    return data;
  }

  /**
   *  This method returns the string of all the plaintext data that
   *  occurred in the original string.
   */
  public String getPlainText()
  {
    return buffer.toString();
  }

  /**
   *  This method is used for retrieving the markup that was contained in
   *  the original string.
   *  @return An ordered list of MarkupEntry objects that each are a tag in the original string.
   */
  public List getMarkup()
  {
    return markupList;
  }
}

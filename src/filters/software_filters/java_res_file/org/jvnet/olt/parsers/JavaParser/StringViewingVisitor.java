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

package org.jvnet.olt.parsers.JavaParser;

public class StringViewingVisitor 
implements ResBundleParserVisitor, ResBundleParserTreeConstants
{
  private int m_iParenStack = 0;
  private boolean m_boolInResource = false;

  public Object visit(SimpleNode node, Object data)
  {
      
      System.out.println(node+ " "+ node.getNodeData());
   /* String strData;
    switch(node.getType())
    {
    case JJTSIMPLE_KEY:
    case JJTQUOTED_TEXT_KEY:
      strData = node.getNodeData();
      System.out.println("Key :\t\t" + strData + "\t: " + strData.length());
      break;
    case JJTQUOTED_TEXT_VALUE:
      strData = node.getNodeData();
      System.out.println("Value :\t\t" + strData + "\t: " + strData.length() );
      break;
    case JJTSIMPLE_VALUE:
      System.out.print("Untranslatable value :\t"  );
      m_boolInResource = true;
      m_iParenStack = 1;
      break;
    case JJTBLOCK:
      m_iParenStack++;
      break;
    case JJTRCURLY_HACK:
      m_iParenStack--;
      if(m_iParenStack < 1)
      {
	if(m_boolInResource) { System.out.print("\n"); }
	m_boolInResource = false;
      }
      else
      {
	strData = node.getNodeData();
	System.out.print(strData);
      }
      break;
    default:
      if(m_iParenStack > 0 && m_boolInResource)
      {
	strData = node.getNodeData();
	System.out.print(strData);
      }
      break;
    }
    */
      
    return data;
  }
}

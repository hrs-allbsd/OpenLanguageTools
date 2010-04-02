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

package org.jvnet.olt.parsers.XmlDocFragmentParser;

public class TreeConst
implements XmlDocFragmentParserTreeConstants
{
  public boolean isDisplayingNode(int typeId)
  {
    switch(typeId)
    {
    case JJTDOCTYPE_BEGINNING:
    case JJTDOCTYPE_ENDING:
    case JJTCOMMENT:
    case JJTINTERNAL_SUB_SET_BEGINNING:
    case JJTINTERNAL_SUB_SET_ENDING:
    case JJTINTERNAL_SUB_SET_WS_COMMENT:
    case JJTENTITY_DECL:
    case JJTENTITY:
    case JJTCDATA:
    case JJTPROCESSING_INST:
    case JJTPCDATA:
    case JJTMARKED_SECTION_TAG:
    case JJTEND_MARKED_SECT:
    case JJTOPEN_TAG:
    case JJTCLOSE_TAG:
	case JJTINT_ENTITY:
      return true;
    default:
      return false;
    }
  }
}

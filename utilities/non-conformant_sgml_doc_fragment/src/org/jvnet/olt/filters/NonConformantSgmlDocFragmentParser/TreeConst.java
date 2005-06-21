
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser;

public class TreeConst
implements NonConformantSgmlDocFragmentParserTreeConstants
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

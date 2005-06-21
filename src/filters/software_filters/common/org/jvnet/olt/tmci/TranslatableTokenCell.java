
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmci;

public class TranslatableTokenCell extends TokenCell
{
  private String m_key;
  private String m_comment;

  public TranslatableTokenCell(int type,String text, String key, String comment) throws IllegalArgumentException
  {
    super(type,text);
    m_key = key;
    m_comment = comment;
  }

  public String getKey() { return m_key; }
  public String getComment() { return m_comment; }
  public boolean isTranslatable() { return false; }
}

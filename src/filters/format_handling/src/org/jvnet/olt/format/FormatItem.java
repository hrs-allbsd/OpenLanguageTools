
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format;

import java.util.HashMap;

public class FormatItem
{
  private int m_penalty;
  private int m_numOccurred;
  private String m_format;

  public FormatItem(String formatText, int penalty)
  {
    m_format = formatText;
    m_penalty = penalty;
    m_numOccurred = 1;
  }

  public int getPenalty()     { return m_penalty; }

  public int getOccurrences() { return m_numOccurred; }

  public String getText()     { return m_format; }

  public void iterateOccurrences() { m_numOccurred++; }
}

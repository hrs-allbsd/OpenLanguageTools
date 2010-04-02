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

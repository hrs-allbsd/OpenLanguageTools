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

package org.jvnet.olt.tmci;

public class SplitFile
{
  private String m_strOriginal;
  private String m_text;
  private int m_number;

  public SplitFile(String filename)
    throws IllegalArgumentException,Exception
  {
    //  This smells
  
   
    int index = filename.lastIndexOf((int) '.');
    if(index < 0)
    {
      throw new IllegalArgumentException("No '.[0-9]+' extension on: " + filename);
    }
    try
    {
      m_strOriginal = filename;
      m_text = filename.substring(0,index + 1);
      m_number = Integer.parseInt(filename.substring(index + 1));
    }
    catch(NumberFormatException exNum)
    {
      throw new IllegalArgumentException("No '.[0-9]+' extension on: " + filename);
    }
    catch(StringIndexOutOfBoundsException exStr)
    {
     throw new Exception("Fence post error in class SplitFile");      
    }
  }
  
  public String getText()
  {
    return m_text;
  }


  public int getInt()
  {
    return m_number;
  }

  public String getOriginalString()
  {
    return m_strOriginal;
  }

  public int compareTo(SplitFile split)
  {
    int iStrComp = m_text.compareTo( split.getText());
    if(iStrComp != 0)
    {
      if(m_number == split.getInt()) { return 0; }
      if(m_number < split.getInt()) { return 1; }
      else { return -1; }
    }
    else
    {
      if(iStrComp < 0)
      {
	return -1;
      }
      else
      {
	return 1;
      }
    }    
  }
}




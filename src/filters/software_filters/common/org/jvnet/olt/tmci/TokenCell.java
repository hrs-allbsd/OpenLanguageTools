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


//  This class could be cleaner. The m_type field is a candidate to 
//  be removed and replaced with an object hierarchy.
public class TokenCell
{
  //  Prvate data common to all TokenCells
  private int m_type;
  private int m_filetype;
  private String m_text;
  private String m_keyText;
  private boolean reLayoutAllowed;

  //  Useful fields
  public final static int MESSAGE = 1;
  public final static int KEY = 2;
  public final static int COMMENT = 3;
  public final static int DOMAIN = 4;
  public final static int DOMAIN_KEY_WORD = 5;
  public final static int FORMATING = 6;
  public final static int MARKER = 7;
  public final static int CONTEXT = 8;

  public TokenCell(int type, String text) throws IllegalArgumentException
  {
    if((type >= MESSAGE) && (type <= CONTEXT))
    {
      m_type = type;
    }
    else
    {
      throw new IllegalArgumentException("Type: " + type + " out of range.");
    }
    m_text = text;

    /* Removing this condition. It is causing too many problems 
    //  Barf if m_text is null or empty unless the type is Message
    if(m_text == null  
    
       ||    (m_text.equals("") && (m_type != MESSAGE)))  
       */
    if(m_text == null)
    {
      throw new IllegalArgumentException("Empty text entry for the cell!");
    }
    this.reLayoutAllowed = true;

  }
  
  /**
   * especially for .java, .properties, .msg and ? file
   * Using this constructor, not only record the keyText, but also record its msg text 
   * to do the fuzzy match 
   */
  public TokenCell(int type, int fileType, String keyText, String text) throws IllegalArgumentException
  {
    this(type,text);
    m_filetype = fileType;
    m_keyText = keyText;
  }


  //  Methods required.
  
  // getText is the value of the message - what's displayed to the user
  public String getText() { return m_text; }
  public String getKeyText() { return (m_keyText==null?"":m_keyText); }
  public int getType() { return m_type; }
  public int getFileType() { return m_filetype; }
  public String getKey() { return ""; }
  public String getComment() { return ""; }
  public boolean isTranslatable() { return false; }
  
  /** a flag that tells us if we're allowed to re-format the layout of the message string
   - essentially pretty-printing the message
   */
  public void setReLayoutAllowed(boolean reLayoutAllowed){this.reLayoutAllowed = reLayoutAllowed;}
  public boolean getReLayoutAllowed(){return this.reLayoutAllowed;}
  
  /** tells us whether the message construct contains a translation or not. This
   * is basically for PO files, which contain a source and target string, quite
   * often
   */
  public boolean hasTranslation(){ return false; }
  
  /**
   * This operations are only really valid for MessageTokenCell - not good.
   */
  public void setTranslation(String translation){ ; }
  /**
   * This operations are only really valid for MessageTokenCell - not good.
   */
  public String getTranslation(){ return ""; }
  
}

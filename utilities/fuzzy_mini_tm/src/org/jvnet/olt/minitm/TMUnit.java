
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

public class TMUnit 
{
  /**
   *  informations the editor needs
   **/
  private String source;
  private String translation;
  private String translatorID;
  
  /**
   * informations the Mini-TM API need to know where it is 
   * in the file go here.
   **/
  private long key;
  
  public TMUnit(long key, 
		String source,
		String translation,
		String translatorID
		)
  {
    this.key = key;
    this.source = source;
    this.translation = translation;
    this.translatorID = translatorID;
  }

 
  /**
   *  sets and gets methods for above members
   **/
  public long getDataSourceKey() {
    return key;
  }
  
  public String getSource() {
    return source;
  }
  
  public String getTranslation() {
    return translation;
  }
  
  public String getTranslatorID() {
    return translatorID;
  }

  public void updateTranslation(String newTranslation, String newTranslatorID)
  {
    translation = newTranslation;
    translatorID = newTranslatorID;
  }
}

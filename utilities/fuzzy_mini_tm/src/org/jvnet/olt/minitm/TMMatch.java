
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

/**
 *  This class is a decorator for the TMUnit class that adds 
 *  match quality data.
 */
public class TMMatch
{
  /**
   *  information the editor needs
   **/
  private int ratioOfMatch;
  private int formatQuality;
  private String source;
  private String translation;
  private String translatorID;
  private long dataStoreID;


  public TMMatch(TMUnit unit, int matchQuality, int fmtQuality)
  {
    source = unit.getSource();
    translation = unit.getTranslation();
    translatorID = unit.getTranslatorID();
    dataStoreID = unit.getDataSourceKey();

    ratioOfMatch = matchQuality;
    formatQuality = fmtQuality;
  }

  /**
   *  This method returns a number that represents the quality of the 
   *  match. This number will be in the range of 0 to 100, and can be 
   *  considered a percentage figure.
   */
  public int getRatioOfMatch() 
  { 
    return ratioOfMatch; 
  }

  /**
   *  This method returns a number that represents the degree of the 
   *  formatting similarity. This number will be in the range of 0 to 
   *  100, and can be considered a percentage figure.
   */
  public int getFormatQuality() 
  { 
    return formatQuality; 
  }

  /**
   *  This method returns the source text of the aligned pair in the 
   *  data store.
   *  @returns StringBuffer
   */	 
  public StringBuffer getSource() 
  {
    return new StringBuffer(source); 
  }
	 
  /**
   *  This method returns the translated text of the aligned pair in 
   *  the data store.
   *  @returns StringBuffer
   */	 
  public StringBuffer getTranslation() 
  {
    return new StringBuffer(translation);
  }
	 
  /**
   *  This method returns the ID of the translator that created the 
   *  aligned pair, originally.
   *  @returns StringBuffer 
   */
  public StringBuffer getTranslatorID()
  {
    return new StringBuffer(translatorID);
  }

  /**
   *  This method returns the primary key of the segment in the data
   *  store.
   *  @returns A long representing a the data store primary key.
   */
  public long getDataSourceKey()
  {
    return dataStoreID;
  }
}

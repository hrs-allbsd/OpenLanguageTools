
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

public class AlignedSegment 
{
  private String source;
  private String translation;
  private String translatorID;
  private long   idInDataStore;
 	
  public AlignedSegment(String src, String trans, String translator_id)
  {
    this(src, trans, translator_id, -1L);
  }

  public AlignedSegment(String src, 
			String trans, 
			String translator_id, 
			long segment_id)
  {
    source = src;
    translation = trans;
    translatorID = translator_id;
    idInDataStore = segment_id;
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
  
  public void setSource(String src) {
    source = src;
  }
	 
  public void setTranslation(String trans) {
    translation = trans;
  }
	 
  public void setTranslatorID(String translator_id) {
    translatorID = translator_id;
  }

  public long getDataStoreKey() {
    return idInDataStore;
  }
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

import java.util.List;

public interface DataStore
{	
  /**
   *  This method returns the generated TMUnit key number
   *  so that it can be used in one or more indices.
   */
  public long insertItem(AlignedSegment segment);
  public TMUnit getItem(long key);
  public void removeItem(long key);
  public TMUnit[] getAllTMs();
  public AlignedSegment[] getAllSegments();

  public String[] getAllTranslatorIDs();

  public void saveMiniTmToFile() throws DataStoreException;

  public String getTmName();
  public String getSourceLang();
  public String getTargetLang();
}

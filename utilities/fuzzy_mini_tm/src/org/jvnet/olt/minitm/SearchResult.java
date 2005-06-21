
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

public class SearchResult
{
  private long key;
  private int  match;

  public SearchResult(long dataSourceKey, int matchQuality)
  {
    match = matchQuality;
    key   = dataSourceKey;
  }

  public long getDataSourceKey()
  {
    return key;
  }

  public int getMatchQuality()
  {
    return match;
  }

  public boolean equals(Object obj)
  {
    if(!(obj instanceof SearchResult)) { return false; }

    SearchResult res = (SearchResult) obj;
    
    return ((this.match == res.match) && (this.key == res.key));
  }

}

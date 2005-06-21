
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

import java.util.Comparator;

public class SearchResultComparator
implements Comparator
{
  public int compare(Object a, Object b)
    throws ClassCastException
  {
    if(!( (a instanceof SearchResult) &&
          (b instanceof SearchResult) )  )
    {
      throw new ClassCastException();
    }

    SearchResult srA = (SearchResult) a;
    SearchResult srB = (SearchResult) b;

    if(srA.getMatchQuality() > srB.getMatchQuality())
    {
      return -1;
    }
      
    if(srA.getMatchQuality() < srB.getMatchQuality())
    {
      return 1;
    }
    
    if(srA.getDataSourceKey() < srB.getDataSourceKey())
    {
      return -1;
    }

    if(srA.getDataSourceKey() > srB.getDataSourceKey())
    {
      return 1;
    }

    return 0;
  }
}

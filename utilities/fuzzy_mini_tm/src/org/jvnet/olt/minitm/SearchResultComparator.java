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

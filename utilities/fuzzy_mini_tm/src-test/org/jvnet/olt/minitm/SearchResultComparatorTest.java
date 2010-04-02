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


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

import junit.framework.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;


public class SearchResultComparatorTest 
extends TestCase
{
  private SearchResult[] array;

  public SearchResultComparatorTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    //  Create an unsorted array.
    array = new SearchResult[10];

    array[0] = new SearchResult( 1L, 78);
    array[1] = new SearchResult( 2L, 100);
    array[2] = new SearchResult( 4L, 98);
    array[3] = new SearchResult( 3L, 99);
    array[4] = new SearchResult( 5L, 77);
    array[5] = new SearchResult( 6L, 75);
    array[6] = new SearchResult( 8L, 80);
    array[7] = new SearchResult( 7L, 80);
    array[8] = new SearchResult( 9L, 99);
    array[9] = new SearchResult( 10L, 100);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTest( new SearchResultComparatorTest("testHighMatchQualityFirst"));
    suite.addTest( new SearchResultComparatorTest("testLowerDataStoreIdFirst"));
   
    return suite;
  }

  //
  //  Tests
  //
  public void testHighMatchQualityFirst()
  {
    Arrays.sort( array , new SearchResultComparator());

    assertEquals(100, array[0].getMatchQuality());
    assertEquals(100, array[1].getMatchQuality());
    assertEquals(99, array[2].getMatchQuality());
    assertEquals(99, array[3].getMatchQuality());
    assertEquals(77, array[8].getMatchQuality());
    assertEquals(75, array[9].getMatchQuality());
  
  }

  public void testLowerDataStoreIdFirst()
  {
    Arrays.sort( array , new SearchResultComparator());

    assertEquals(2L, array[0].getDataSourceKey());
    assertEquals(10L, array[1].getDataSourceKey());
    assertEquals(7L, array[5].getDataSourceKey());
    assertEquals(8L, array[6].getDataSourceKey());
    assertEquals(5L, array[8].getDataSourceKey());
    assertEquals(6L, array[9].getDataSourceKey());
  }
}

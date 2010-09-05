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

package org.jvnet.olt.fuzzy.basicsearch;

import org.jvnet.olt.index.AbstractFuzzySearchMiniTM;
import org.jvnet.olt.index.MatchQualityCalculator;
import org.jvnet.olt.minitm.*;

public class BasicFuzzySearchMiniTM extends AbstractFuzzySearchMiniTM implements MiniTM
{
  public BasicFuzzySearchMiniTM(String tmFile,
				boolean boolCreateIfMissing,
				String miniTmName,
				String sourceLang,
				String targetLang)
    throws MiniTMException
  {
    super(tmFile, boolCreateIfMissing, miniTmName, sourceLang, targetLang );
    
    openDataStore();
  }


  //  template methods
  protected FormatRemovingStrategy selectFormatRemover()
  {
    return new PlainTextFormatRemovingStrategy();
  }

  /**  This method is a template method (designed to be overridden) that
   *  creates match objects. In this implementation the match quality
   *  value is passed through from the fuzzy index without being modified.
   *  @param sourceString source.
   *  @param result A search result returned from the fuzzy index.
   */
  protected TMMatch createMatch(String sourceString, SearchResult result) throws MiniTMException
  {
    //  Look up dataStore
    TMUnit unit = dataStore.getItem(result.getDataSourceKey());

    float matchQual = MatchQualityCalculator.calculateMatchQuality(sourceString,
                        unit.getSource() , this.selectFormatRemover());

    //  Create match object
    TMMatch match = new TMMatch(unit, (int) matchQual, 100);

    return match;
  }
  
}




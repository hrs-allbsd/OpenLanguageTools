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

/**
 *  This class is the MiniTM class that extracts the markup from SGML
 *  strings and treats them separately from the text of the string. All
 *  the fuzzy matching is done on the plaintext string. The markup is then
 *  is compared, and matches are penalized based on the level of markup
 *  differences, between thema nd the query string. This prevents matches
 *  being returned for segments that have lots of similar formatting and
 *  only a little actual translatable text in common.
 */
public class BasicSGMLFuzzySearchMiniTM extends AbstractFuzzySearchMiniTM implements MiniTM
{
  public static final int ATTRIBUTED_TAG_PENALTY = 5;
  public static final int PLAIN_TAG_PENALTY = 2;

  public BasicSGMLFuzzySearchMiniTM(String tmFile,
				    boolean boolCreateIfMissing,
				    String miniTmName,
				    String sourceLang,
				    String targetLang)
    throws MiniTMException
  {
    super(tmFile, boolCreateIfMissing, miniTmName, sourceLang, targetLang );
    
    openDataStore();
  }

  protected FormatRemovingStrategy selectFormatRemover()
  {
    return new SGMLFormatRemovingStrategy();
  }

  protected TMMatch createMatch(String sourceString,
				SearchResult result)
    throws MiniTMException
  {
    //  Look up dataStore
    TMUnit unit = dataStore.getItem(result.getDataSourceKey());

    if(unit == null)
    {
      throw new MiniTMException("There is no data store item for a defined data store key. The MiniTM has been corrupted.");
    }

    //  Adjust match quality figures based on the formatting.
    //  Build up a list of differences
    String refString = unit.getSource();   

    FormatRemovingStrategy rf = this.selectFormatRemover();
    
    float matchQuality = MatchQualityCalculator.calculateMatchQuality(sourceString, refString, rf);
    int formatDiff = MatchQualityCalculator.calculateFormatPenalty(sourceString, refString, rf);

    //  Create match object
    TMMatch match = new TMMatch(unit, (int) matchQuality, formatDiff);

    return match;
  }
}

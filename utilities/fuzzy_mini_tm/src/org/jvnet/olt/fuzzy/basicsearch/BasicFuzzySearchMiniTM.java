
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
   *  @param sourceFormatting An ordered list of source formatting
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




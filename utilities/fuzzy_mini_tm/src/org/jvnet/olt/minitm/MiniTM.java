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

import java.io.IOException;
import java.util.List;

public interface MiniTM
{	
        /**
	 * methods for duplicate check
	 * Notion:
	 * You can ignore the second parameter if you want
	 **/
	 public boolean isDuplicate(String newSource, String translationID)
	  throws MiniTMException;

        /**
	 *  This method is for adding a fresh previously untranslated
	 *  segment to the MiniTM.
	 */
         public void addNewSegment(AlignedSegment segment)
	   throws MiniTMException;

        /**
	 *  This method updates a previous TMUnit in the MiniTM with a new
	 *  translation.
	 */
         public void updateSegment(AlignedSegment segment, long dataStoreId)
	   throws MiniTMException;
	
        /**
	 *  This method updates a previous TMUnit in the MiniTM with a new
	 *  translation.
	 */
         public void removeSegment(AlignedSegment segment, long dataStoreId)
	   throws MiniTMException;

	/**
	 * methods for maintenance
	 * return all of the TMs in this mini-TM file
	 **/
         public AlignedSegment[] getAllSegments();
	 
	/**
	 * methods for full match and fuzzy Match
	 * return all of the matchs for a new source including full match
	 * and fuzzy match.
	 * @param matchThreshold  This variable represents a the minimum acceptable match quality for the items returned by the search.
	 **/
	 public TMMatch[] getMatchFor(String newSource, 
				      int matchThreshold,
				      int maxMatchesToReturn)
	   throws MiniTMException;

  /**
   *  This method returns a list of all the distinct translator IDs in
   *  the TMUnits inside the MiniTM.
   */
  public String[] getAllTranslatorIDs();

	
	
	/**
	 *  sets and gets methods
	 **/
	 public String getName();  
         public String getSourceLang();
         public String getTargetLang();

  /**
   *  This method allows the user to force a save of the TM file.
   */
  public void saveMiniTmToFile() throws MiniTMException;
	 
}

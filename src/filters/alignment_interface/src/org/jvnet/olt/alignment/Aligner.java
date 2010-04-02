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

package org.jvnet.olt.alignment;

import java.util.logging.Logger;
import java.util.Map;

public interface Aligner
{
  //  Initialization
  /**
   *  This method initializes the aligner.
   *  @param penalties An object storing penalty scores for 1:2, 2:1, and 2:2 alignments.
   *  @param logger A logger object to log status, warnings and critical errors.
   *  @param preferences A Map of user preferences for the Aligner.
   */
  public void initAligner( SegLenPenalties penalties,
			   Logger logger,
			   Map    preferences );
  
  //  Do alignment
  /**
   *  This method does the alignment. If the aligner is not
   *  initialized an exception is thrown.
   *  @param srcLang A string representing the source language
   *  @param srcSegs The source segments
   *  @param tgtLang A string representing the target language
   *  @param tgtSegs The target segments
   *  @exception UninitialzedAlignerException Thrown if this method is called before initAligner.
   *  @exception AlignmentException Thrown if there is an alignment error.
   *  @return AlignedSeg[] An array containing the aligned segments.
   */
  public AlignedSeg[] doAlignment(String srcLang, 
			  Segment[] srcSegs, 
			  String tgtLang, 
			  Segment[] tgtSegs)
    throws UninitialzedAlignerException, AlignmentException;

  //  Accessors
  /**
   *  This method sets the segment length penalties to be used when
   *  doAlignment is called.
   *  @param penalties Segment penalty scores for 1:2, 2:1, and 2:2 alignments.
   */
  public void setSegLenPenalties(SegLenPenalties penalties);

  /**
   *  This method gets the current segment length penalties that are 
   *  being used.
   *  @return SegLenPenalties Segment length penalties
   */
  public SegLenPenalties getSegLenPenalties();

}

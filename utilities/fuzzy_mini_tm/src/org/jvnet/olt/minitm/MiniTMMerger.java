
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

import java.io.IOException;

public interface MiniTMMerger
{
  


  /**
   * method for merge mini-TMs
   * This method should merge the following segments automatically 
   * for one source:
   * 	. The single or same translation segment;
   * 	. The different translations if and only if the translator 
   *        IDs are different:
   *		1)"Use Translator ID" is switched on
   * 	. The different translations if and only if the translator
   *        IDs are same:
   *		1)"View Duplicates" is switched off
   * At other circumstances,it should store all of the TMs into a 
   * TMUnit array, and at last return the array.	
   **/
  public MiniTM mergeMiniTMs(MiniTM[] minitms, 
			     String[] translatorIDArray,
			     String newTmFile)
    throws MiniTMMergeException, IOException;


  public int countMergedUnits()
    throws MiniTMMergeException;

  public int countUnmergedUnits()
    throws MiniTMMergeException;

  public AlignedSegment[] getUnmergedUnits()
    throws MiniTMMergeException;

}

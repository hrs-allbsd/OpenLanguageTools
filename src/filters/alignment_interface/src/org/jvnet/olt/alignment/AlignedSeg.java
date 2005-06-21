
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.alignment;

/**
 *  This interface describes aligned segments: a set of source and target
 *  segments that align with each other.
 */
public interface AlignedSeg
{
  public int getNumSrcSegs();

  public int getNumTgtSegs();

  public void setSegments(Segment[] srcSegs, 
			  Segment[] tgtSegs);

  public Segment[] getSrcSegments();
  public Segment[] getTgtSegments();
}

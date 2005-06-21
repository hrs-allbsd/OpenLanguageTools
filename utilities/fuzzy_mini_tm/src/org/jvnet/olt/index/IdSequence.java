
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.index;

public class IdSequence
{
  /**
   *  The sequence variable that is used for generationg unique
   *  ids. This variable should only ever be accessed via the 
   *  methods getCurrentId() and getNextId()
   */
  private long idSequence = 1;  
 
  public IdSequence()
  {
    this(1L);
  }

  public IdSequence(long startId)
  {
    idSequence = startId;
  }
 

  public synchronized long getCurrentId()
  {
    return idSequence;
  }

  public synchronized long getNextId()
  {
    return idSequence++;
  }
}

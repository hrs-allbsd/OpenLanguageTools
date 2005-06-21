
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

package org.jvnet.olt.fuzzy;

public class DumpVectors
{
  
  public static void main(String[] args)
  {
    try
    {
    FuzzyIndexEntry entry1 = new FuzzyIndexEntry("The cat in the hat.", 1L);
    FuzzyIndexEntry entry2 = new FuzzyIndexEntry("The cat on the mat.", 2L);
    FuzzyIndexEntry entry3 = new FuzzyIndexEntry("One ring to rule them all, one ring to find them. One ring to bring them all and in darkness bind them.", 3L);
    FuzzyIndexEntry entry4 = new FuzzyIndexEntry("I taut I taw a putty tat. I did! I did! I did!", 4L);
    FuzzyIndexEntry entry5 = new FuzzyIndexEntry("There is no span.", 5L);
    FuzzyIndexEntry entry6 = new FuzzyIndexEntry("There is no spoon.", 6L);
    
    System.out.println("Entry1 = (" + entry1.dumpIndexVector() + ")");
    System.out.println("Entry2 = (" + entry2.dumpIndexVector() + ")");
    System.out.println("Entry3 = (" + entry3.dumpIndexVector() + ")");
    System.out.println("Entry4 = (" + entry4.dumpIndexVector() + ")");
    System.out.println("Entry5 = (" + entry5.dumpIndexVector() + ")");
    System.out.println("Entry6 = (" + entry6.dumpIndexVector() + ")");
    }
    catch(Exception ex) { System.out.println(ex.getMessage()); }
  }







}

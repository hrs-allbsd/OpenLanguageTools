
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.index;

import org.jvnet.olt.minitm.*;
import java.util.*;
import java.io.*;

public class BasicTestDataStore
extends BasicDataStore
{
  public BasicTestDataStore(String tmFile, 
			    boolean boolCreateIfMissing, 
			    String name)
    throws DataStoreException
  {
    super(tmFile, boolCreateIfMissing, name, "en" ,"de");
  }


  public boolean tmFileExists(String tmFile)
    throws DataStoreException
  {
    return true;
  }

  public void populateHashtable(Hashtable hash, String tmFile)
    throws DataStoreException
  {
    //  Build the hashtable of TMUnits in here.
    TMUnit unit;
    long id;

    id = idSequence.getNextId();
    unit = new TMUnit(id,
		      "",
		      "",
		      "");
    hash.put( new Long(id), unit);
    
    id = idSequence.getNextId();
    unit = new TMUnit(id,
		      "",
		      "",
		      "");
    hash.put( new Long(id), unit);   
    
    id = idSequence.getNextId();
    unit = new TMUnit(id,
		      "",
		      "",
		      "");
    hash.put( new Long(id), unit);

    id = idSequence.getNextId();
    unit = new TMUnit(id,
		      "",
		      "",
		      "");
    hash.put( new Long(id), unit);

    id = idSequence.getNextId();
    unit = new TMUnit(id,
		      "",
		      "",
		      "");
    hash.put( new Long(id), unit);

    id = idSequence.getNextId();
    unit = new TMUnit(id,
		      "",
		      "",
		      "");
    hash.put( new Long(id), unit);
  }
}

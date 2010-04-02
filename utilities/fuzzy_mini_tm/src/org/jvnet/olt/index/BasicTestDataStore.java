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

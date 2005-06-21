
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.tagged;

import java.util.HashMap;

public interface TagTable
{
 
  public boolean tagMayContainPcdata(String tagName);

  public boolean tagForcesVerbatimLayout(String tagName);

  public boolean tagEmpty(String tagName);
  
  public boolean tagMayContainPcdata(String tagName, String namespaceID);

  public boolean tagForcesVerbatimLayout(String tagName, 
    String namespaceID);

  public boolean tagEmpty(String tagName, String namespaceID);
}

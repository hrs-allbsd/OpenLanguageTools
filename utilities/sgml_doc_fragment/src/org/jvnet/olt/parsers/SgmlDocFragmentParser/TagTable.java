
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import java.util.HashMap;

public interface TagTable
{
 
  public boolean tagMayContainPcdata(String tagName);

  public boolean tagForcesVerbatimLayout(String tagName);

  public boolean tagEmpty(String tagName);
}

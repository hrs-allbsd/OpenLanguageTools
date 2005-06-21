
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

import java.util.List;

public interface FormatRemovingStrategy
{
    public String removeFormatting(String string)
    throws MiniTMException;
    
    public List extractFormatting(String string)
    throws MiniTMException;
}

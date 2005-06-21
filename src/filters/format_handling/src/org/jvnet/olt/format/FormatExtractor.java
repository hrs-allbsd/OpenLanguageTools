
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format;

import java.util.HashMap;
import org.jvnet.olt.format.GlobalVariableManager;

public interface FormatExtractor
{
    public HashMap getFormatting(String string, GlobalVariableManager gvm) throws InvalidFormattingException;
}

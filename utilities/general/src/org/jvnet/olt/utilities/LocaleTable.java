
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * LocaleTable.java
 *
 * Created on September 5, 2002, 11:49 AM
 */

package org.jvnet.olt.utilities;

/**
 *
 * @author  timf
 */
public interface LocaleTable {
    
    boolean isValidIdentifier(java.lang.String identifier);
    boolean isValidCodeset(String codeset);
    String getDefaultEncoding(java.lang.String identifier);
    
}

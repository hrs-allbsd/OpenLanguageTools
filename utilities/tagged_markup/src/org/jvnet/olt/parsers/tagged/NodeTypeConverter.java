
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * NodeTypeConverter.java
 *
 * Created on May 14, 2003, 4:40 PM
 */

package org.jvnet.olt.parsers.tagged;

/**
 *
 * @author  timf
 */
public interface NodeTypeConverter {
    
    int convert(int originalNodeType);
    
}

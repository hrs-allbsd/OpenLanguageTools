
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlFilterAdaptor.java
 *
 * Created on June 30, 2003, 3:17 PM
 */

package org.jvnet.olt.filters.interfaces;

/**
 *
 * @author  timf
 */
public class XmlFilterAdaptor implements Filter {
    
    /** Creates a new instance of XmlFilterAdaptor
        -- not implemented yet !
     */
    public XmlFilterAdaptor() {
    }
    
    public org.jvnet.olt.alignment.Segment[] getSegments(String language) {
        return new org.jvnet.olt.alignment.Segment[0];
    }
    
}

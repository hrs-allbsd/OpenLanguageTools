
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * StrictEntityConversionFilterWriter.java
 *
 * Created on 23 September 2003, 11:50
 */

package org.jvnet.olt.io;

/**
 *
 * @author  jc73554
 */
public abstract class StrictEntityConversionFilterWriter extends EntityConversionFilterWriter {
    
    /** Creates a new instance of StrictEntityConversionFilterWriter */
    public StrictEntityConversionFilterWriter(java.io.Writer writer) {
        super(writer);
    }
    
    public void handleUnknownEntity() throws java.io.IOException{
        throw new UnknownEntityException("Unknown entity: " + m_buffer.toString());
    }    
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * MissingSklDataException.java
 *
 * Created on 30 July 2003, 17:08
 */

package org.jvnet.olt.utilities;

/**
 *
 * @author  jc73554
 */
public class MissingXliffDataException extends java.io.IOException {
    
    /** Creates a new instance of MissingSklDataException */
    public MissingXliffDataException() {
        super();
    }
    
    public MissingXliffDataException(String mesg) {
        super(mesg);
    }
}

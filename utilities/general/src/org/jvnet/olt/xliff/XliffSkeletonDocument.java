
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffSkeletonDocument.java
 *
 * Created on 31 March 2003, 13:40
 */

package org.jvnet.olt.xliff;

import java.io.Writer;
import java.io.Reader;

/**
 *
 * @author  jc73554
 */
public interface XliffSkeletonDocument
{
    //  Accessing internal XLIFF buffer
    public Writer getSklWriter() throws java.io.IOException;
    
    public Reader getSklReader() throws java.io.IOException;
    
}

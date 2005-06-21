
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * FileCommand.java
 *
 * Created on 02 October 2002, 14:58
 */

package org.jvnet.olt.util;

import java.io.File;

/**
 *
 * @author  jc73554
 */
public interface FileCommand
{
    
    public int run(File file) throws Exception;
    public void cleanUp() throws Exception;
    
}

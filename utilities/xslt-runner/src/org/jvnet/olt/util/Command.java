
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * Command.java
 *
 * Created on August 16, 2004, 11:46 AM
 */

package org.jvnet.olt.util;

/** An interface for specifying a command that can be executed to process data
 * from a stream, and to write out the results to another stream.
 * @author  jc73554
 */
public interface Command {
    
    public void execute(java.io.Reader reader, java.io.Writer writer) throws java.io.IOException;
    
}

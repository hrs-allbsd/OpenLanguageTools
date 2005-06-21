
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BadArgException.java
 *
 * Created on 06 March 2003, 18:02
 */

package org.jvnet.olt.util;

/**
 *
 * @author  jc73554
 */
public class BadArgException extends java.lang.Exception
{    
    /**
     * Creates a new instance of <code>BadArgException</code> without detail message.
     */
    public BadArgException()
    {
    }
      
    /**
     * Constructs an instance of <code>BadArgException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BadArgException(String msg)
    {
        super(msg);
    }
}

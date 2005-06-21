
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * MessageArrayKey.java
 *
 * Created on March 6, 2003, 2:16 PM
 */
package org.jvnet.olt.filters.software;

/**
 * There's nothing here, except some static ints that help us
 * access the String[][] returned from MessageArrayFactoryVisitor
 * a little more easily.
 *
 * @author  timf
 */
public class MessageArrayKey {

    public static final int KEY = 0;
    public static final int DOMAIN = 1;
    public static final int STRING = 2;
    public static final int COMMENT = 3;
    public static final int PLURAL = 4;

    /** abstract class - don't want to ever create a new instance of MessageArrayKey */
    public MessageArrayKey() {
    }
}

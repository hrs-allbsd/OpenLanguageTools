/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/

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

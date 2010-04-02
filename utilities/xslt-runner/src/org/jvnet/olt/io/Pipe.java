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
 * Pipe.java
 *
 * Created on August 13, 2004, 4:43 PM
 */

package org.jvnet.olt.io;

/** This class is a handy abstraction fro creating a pair of connected 
 * PipedReaders and PipedWriters.
 * @author  jc73554
 */
public class Pipe {
    
    /** Holds value of property reader. */
    private java.io.PipedReader reader;
    
    /** Holds value of property writer. */
    private java.io.PipedWriter writer;
    
    /** Creates a new instance of Pipe */
    public Pipe() throws java.io.IOException {
        reader = new java.io.PipedReader();
        writer = new java.io.PipedWriter();
        reader.connect(writer);
    }
    
    /** Getter for property reader.
     * @return Value of property reader.
     *
     */
    public java.io.PipedReader getReader() {
        return this.reader;
    }
    
    /** Getter for property writer.
     * @return Value of property writer.
     *
     */
    public java.io.PipedWriter getWriter() {
        return this.writer;
    }    
}

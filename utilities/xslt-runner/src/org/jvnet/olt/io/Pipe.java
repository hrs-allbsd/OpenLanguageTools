
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

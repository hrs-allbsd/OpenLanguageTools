
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BasicXMLEntityConversionWriter.java
 *
 * Created on 23 September 2003, 11:55
 */

package org.jvnet.olt.io;

/**
 * This class converts the default XML character entities to their respective 
 * characters.
 * @author  jc73554
 */
public class BasicXMLEntityConversionWriter extends EntityConversionFilterWriter {
    
    /** Creates a new instance of BasicXMLEntityConversionWriter */
    public BasicXMLEntityConversionWriter(java.io.Writer writer) {
        super(writer);
        addEntity("&amp;", '&');
        addEntity("&lt;", '<');
        addEntity("&gt;", '>');
        addEntity("&quot;", '"');
        addEntity("&apos;", '\'');
    }    
}

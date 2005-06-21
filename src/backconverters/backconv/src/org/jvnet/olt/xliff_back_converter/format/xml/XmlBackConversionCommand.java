
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlBackConversionCommand.java
 *
 * Created on February 10, 2005, 11:17 AM
 */

package org.jvnet.olt.xliff_back_converter.format.xml;

/**
 * An interface that allows us to perform different conversions from Xliff
 * into other formats (typically, the original file format, but there's no
 * reason why we couldn't use an implementation of this to, say, render the
 * XLIFF file to a nice PDF document or HTML page.
 *
 * In particular though, this is only for particular XML files - perhaps you
 * might instead be interested in the SpecificBackConverter in a parent package,
 * which does similar. See notes in the XmlBackConversionCommandFactory interface.
 *
 * @author timf
 */

public interface XmlBackConversionCommand {
    
    /**
     * Perform specific conversion actions on an XML type
     */
    public void convert(String filename, String lang, String encoding, String originalXlzFilename) 
        throws XmlBackConversionCommandException;
}

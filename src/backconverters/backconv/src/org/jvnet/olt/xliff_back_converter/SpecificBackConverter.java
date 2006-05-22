
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * FormatSpecificBackConverterCommand.java
 *
 * Created on August 1, 2003, 12:20 PM
 */

package org.jvnet.olt.xliff_back_converter;

import java.io.File;

/**
 * This interface allows us to take particular actions
 * based on the different needs of different formats. This is
 * the Command design pattern.
 *
 * @author  timf
 */
public interface SpecificBackConverter {

    /** convert file
     *
     * The implementors must make sure the modified file is copied
     * over the original file (represented by the 'file' param)
     *
     * For more details see #SpecificBackconverterBase
     */
    public void convert(File file) throws SpecificBackConverterException;
    /*
     * This allows us to write format specific actions to take place after the
     * .xlz file has processed to produce an original file.
     *
     * @param filename The absolute path of the file to be converted
     * @param encoding The encoding which the file is written in
     * @param lang The language of the translated document (RFC 3066, please)
     */
//    void convert(String filename, String lang, String encoding) throws SpecificBackConverterException;
    
    /*
     * This interface is building upon the original one, only this time, we have an
     * additional parameter to allow us to perform actions based on the contents of
     * the translated XLIFF file. Primarily, this is being used in the backconversion
     * of OpenOffice.org sxw files, where we need access to the various files in the
     * xlz archive that make up the contents of the sxw files. I suspect that other
     * converters could find this interface useful as well. Most however, will probably
     * just ignore the last parameter, and simply call the convert(filename, lang, encoding)
     * method instead.
     *
     * @param filename The absolute path of the file to be converted
     * @param encoding The encoding which the file is written in
     * @param lang The language of the translated document (RFC 3066, please)
     * @param originalXlzFilename The absolute path of the XLZ file that was backconverted
     */
//    void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException;
    
}

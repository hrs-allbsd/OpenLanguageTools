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
 * SpecificBackconverterBase.java
 *
 * Created on April 24, 2006, 9:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.xliff_back_converter;

import java.io.File;

/** Base class of all backconverters.
 *
 * All backconverter classes are required to subclass this base class.
 * The conversion is performed by convert method.
 *
 * Prior to the call of the convert method, properties like dataType, encoding,
 * language, originalXlzFilename and pros must be set.
 *
 *
 * @author boris
 */
abstract public class SpecificBackconverterBase implements SpecificBackConverter{
    protected static final String UTF8 = "UTF-8";
        
    /** data type of the original  file
     */
    protected String dataType;
    /** encoding of the result type.
     */
    protected String targetEncoding;
    /* language of the file to which it file is backconverted
     */
    protected String lang;
    /** instance of File object describing the source file
     *  Note that the source file does not always need to be an .xlz file
     */
    protected File originalXlzFile;

    /** properties for this back conversion
     *
     */
    protected BackConverterProperties props;

    /**
     * Creates a new instance of SpecificBackconverterBase
     */
    public SpecificBackconverterBase() {
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getTargetEncoding() {
        return targetEncoding;
    }

    public void setTargetEncoding(String encoding) {
        this.targetEncoding = encoding;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public File getOriginalXlzFile() {
        return originalXlzFile;
    }

    public void setOriginalXlzFile(File originalXlzFilename) {
        this.originalXlzFile = originalXlzFilename;
    }

    public BackConverterProperties getProps() {
        return props;
    }

    public void setProps(BackConverterProperties props) {
        this.props = props;
    }

    /** if the result of this conversion is binary
     *
     *  All subclasses that work on binary formats must return true.
     */
    public boolean isBinaryFormat(){
        return false;
    }

    /* make postconversion steps
     *
     * This method operates on file that has been converted to target encoding (if not binary)
     */
    public void postConvert(File file) throws SpecificBackConverterException {
    }
}

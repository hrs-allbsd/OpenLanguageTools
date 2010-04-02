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
 * XliffMergingPrefs.java
 *
 * Created on 26 September 2003, 18:46
 */
package org.jvnet.olt.editor.filesplit;


/**
 *
 * @author  jc73554
 */
public class XliffMergingPrefs {
    /** Holds value of property inputDir. */
    private java.io.File inputDir;

    /** Holds value of property outputDir. */
    private java.io.File outputDir;

    /** Holds value of property baseFileName. */
    private String baseFileName;

    /** Holds value of property suffix. */
    private String suffix;

    /** Creates a new instance of XliffMergingPrefs */
    public XliffMergingPrefs() {
    }

    /** Getter for property inputDir.
     * @return Value of property inputDir.
     *
     */
    public java.io.File getInputDir() {
        return this.inputDir;
    }

    /** Setter for property inputDir.
     * @param inputDir New value of property inputDir.
     *
     */
    public void setInputDir(java.io.File inputDir) {
        this.inputDir = inputDir;
    }

    /** Getter for property outputDir.
     * @return Value of property outputDir.
     *
     */
    public java.io.File getOutputDir() {
        return this.outputDir;
    }

    /** Setter for property outputDir.
     * @param outputDir New value of property outputDir.
     *
     */
    public void setOutputDir(java.io.File outputDir) {
        this.outputDir = outputDir;
    }

    /** Getter for property baseFileName.
     * @return Value of property baseFileName.
     *
     */
    public String getBaseFileName() {
        return this.baseFileName;
    }

    /** Setter for property baseFileName.
     * @param baseFileName New value of property baseFileName.
     *
     */
    public void setBaseFileName(String baseFileName) {
        this.baseFileName = baseFileName;
    }

    /** Getter for property suffix.
     * @return Value of property suffix.
     *
     */
    public String getSuffix() {
        return this.suffix;
    }

    /** Setter for property suffix.
     * @param suffix New value of property suffix.
     *
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}

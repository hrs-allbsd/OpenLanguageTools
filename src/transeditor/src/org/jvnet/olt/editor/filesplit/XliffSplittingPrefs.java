/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * XliffSplittingPres.java
 *
 * Created on 24 September 2003, 15:27
 */
package org.jvnet.olt.editor.filesplit;

import org.jvnet.olt.xliff.XliffDocument;


/**
 *
 * @author  jc73554
 */
public class XliffSplittingPrefs {
    /** Holds value of property suffix. */
    private String suffix;

    /** Holds value of property segmentNum. */
    private int segmentNum;

    /** Holds value of property outputLoc. */
    private java.io.File outputLoc;

    /** Holds value of property inputFile. */
    private XliffDocument xliffDocument;

    /** Creates a new instance of XliffSplittingPres */
    public XliffSplittingPrefs() {
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

    /** Getter for property segmentNum.
     * @return Value of property segmentNum.
     *
     */
    public int getSegmentNum() {
        return this.segmentNum;
    }

    /** Setter for property segmentNum.
     * @param segmentNum New value of property segmentNum.
     *
     * @throws PropertyVetoException
     *
     */
    public void setSegmentNum(int segmentNum) {
        this.segmentNum = segmentNum;
    }

    /** Getter for property outputLoc.
     * @return Value of property outputLoc.
     *
     */
    public java.io.File getOutputLoc() {
        return this.outputLoc;
    }

    /** Setter for property outputLoc.
     * @param outputLoc New value of property outputLoc.
     *
     */
    public void setOutputLoc(java.io.File outputLoc) {
        this.outputLoc = outputLoc;
    }

    /** Getter for property inputFile.
     * @return Value of property inputFile.
     *
     */
    public XliffDocument getXliffDocument() {
        return this.xliffDocument;
    }

    /** Setter for property inputFile.
     * @param inputFile New value of property inputFile.
     *
     */
    public void setXliffDocument(XliffDocument xliffDocument) {
        this.xliffDocument = xliffDocument;
    }
}

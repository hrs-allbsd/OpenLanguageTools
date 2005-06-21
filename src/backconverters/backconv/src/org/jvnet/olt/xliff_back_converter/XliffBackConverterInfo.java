
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.xliff_back_converter;

/**
 * The XliffBackConverterInfo class stores information about the XLIFF file
 * and the back converted file. Namely the number of trans-units an Xliff
 * File contains and the number of target segments that are back converted.
 *
 * <p>N.B. You should use the <code>isFinished</code> method before 
 * attempting to get the number of trans units or targets as the parser may 
 * not have gotten set this information in the object yet. If this the 
 * parser is not finished or if it fails, then the 
 * <code>getNumOfTaretUnits</code> and the <code>getNumofTransUnits</code> 
 * methods will return <code>-1</code>.</p>
 *
 * @author Brian Kidney
 * @created September 10, 2002
 */
public class XliffBackConverterInfo {

    /*
     * The number of trans-units in an xliff file
     */
    private int numOfTransUnits = -1;

    /*
     * The number of target segments that are being back converted
     */
    private int numOfTargetUnits = -1;

    /*
     * Indicates if we have reached the end of the Xliff File
     */
    private boolean finished = false;


    /**
     * Constructor for the XliffBackConverterInfo object
     */
    public XliffBackConverterInfo() { }


    /**
     * Sets the numOfTransUnits attribute of the XliffBackConverterInfo
     * object
     *
     * @param theNumOfTransUnits The number of trans-units in an xliff file
     */
    public void setNumOfTransUnits(int theNumOfTransUnits) {
        numOfTransUnits = theNumOfTransUnits;
    }


    /**
     * Sets the numOfTargetUnits attribute of the XliffBackConverterInfo
     * object
     *
     * @param theNumOfTargetUnits The number of target segments that where
     *      back converted
     */
    public void setNumOfTargetUnits(int theNumOfTargetUnits) {
        numOfTargetUnits = theNumOfTargetUnits;
    }


    /**
     * Gets the numOfTransUnits attribute of the XliffBackConverterInfo
     * object
     *
     * @return The number of trans-units in an xliff file
     */
    public int getNumOfTransUnits() {
        return numOfTransUnits;
    }


    /**
     * Gets the numOfTargetUnits attribute of the XliffBackConverterInfo
     * object
     *
     * @return The number of target segments that where back converted.
     */
    public int getNumOfTargetUnits() {
        return numOfTargetUnits;
    }


    /**
     * Sets the finished attribute of the XliffBackConverterInfo to true
     */
    public void setFinished() {
        finished = true;
    }


    /**
     * Gets the finished attribute of the XliffBackConverterInfo object
     *
     * @return The finished value
     */
    public boolean isFinished() {
        return finished;
    }
}


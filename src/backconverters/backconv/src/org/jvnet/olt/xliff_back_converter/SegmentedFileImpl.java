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
 * SegmentedFileImpl.java
 *
 * Created on August 8, 2002, 2:54 PM
 */
package org.jvnet.olt.xliff_back_converter;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.lang.Integer;
import java.util.Collection;

/**
 * Implements the SegmentedFile interface.
 *
 * <p>SegmentedFile objects store the value of trans-unit elements of a
 * particular file element in a Xliff file.</p>
 *
 * @author    Brian Kidney
 * @version   August 8, 2002
 */
public class SegmentedFileImpl implements SegmentedFile {

    /* A map for storing transUnitId/transUnitText pairs */
    private Map segmentMap = new HashMap();

    /* The name of the file before it was segmented (e.g. index.html) */
    private String originalFilename = "";

    /* The target language */
    private String targetLanguage = "";

    /** Holds value of property datatype. */
    private String datatype = "";

    /* Holds values of datatype. */
    private Map datatypeMap = new HashMap();
    private final int OTHERFILETYPE = 0;
    private final int SOFTWAREMESSAGE = 1;

    /**
     * Creates a new instance of SegmentedFileImpl
     */
    public SegmentedFileImpl() {
        datatypeMap.put("PO", new Integer(SOFTWAREMESSAGE));
        datatypeMap.put("MSG", new Integer(SOFTWAREMESSAGE));
        datatypeMap.put("JAVA", new Integer(SOFTWAREMESSAGE));
        datatypeMap.put("PROPERTIES", new Integer(SOFTWAREMESSAGE));
    }


    /**
     * Returns boolean True if the SegmentedFile object contains a
     * transUnit element with key transUnitId, false otherwise.
     *
     * @param transUnitId  Description of the Parameter
     * @return             Description of the Return Value
     */
    public boolean containsTransUnitId(String transUnitId) {

        if (segmentMap.containsKey(transUnitId)) {
            return true;
        }
        return false;
    }


    /**
     * Gets the number of transUnits of the SegmentedFile object
     *
     * @return   The numberOfTransUnits value
     */
    public int getNumberOfTransUnits() {
        return segmentMap.size();
    }


    /**
     * Gets the transUnit attribute of the SegmentedFile object
     *
     * @param transUnitId  The transUnit ID key value (Represented by the
     *                     "id" attribute in the trans-unit element of Xliff
     * @return             The transUnit value
     */
    public TransUnitData getTransUnit(String transUnitId) {
        return (TransUnitData) segmentMap.get(transUnitId);
    }


    /**
     * Returns boolean True if the SegmentedFile object doesn't contain any
     * transUnit elements, false otherwise.
     *
     * @return   True if the SegmentedFile object doesn't contain any
     * transUnit elements, false otherwise.
     */
    public boolean isEmpty() {
        return segmentMap.isEmpty();
    }


    /**
     * Sets the transUnitId and transUnitText attribute of the SegmentedFile
     * object.
     *
     * @param transUnitId    The new transUnitId value
     * @param transUnitText  The new transUnitText value
     */
    public void setTransUnit(java.lang.String transUnitId, TransUnitData transUnit) {
        segmentMap.put(transUnitId, transUnit);
    }


    /**
     * Gets a collection of the SegmentedFile object transUnit elements.
     *
     * @return   The collection value
     */
    public Collection getCollection() {
        return segmentMap.values();
    }


    /**
     * Gets the originalFilename attribute of the SegmentedFile object.
     * The originalFilename is the name of the file before it was segmented
     * (e.g. index.html, docbbok.xml, etc.).
     *
     * @return   The originalFilename value
     */
    public String getOriginalFilename() {
        return originalFilename;
    }


    /**
     * Sets the originalFilename attribute of the SegmentedFile object.
     * The originalFilename is the name of the file before it was segmented
     * (e.g. index.html, docbbok.xml, etc.).
     *
     * @param originalFilename  The new originalFilename value
     */
    public void setOriginalFilename(String theOriginalFilename) {
        File theFile = new File(theOriginalFilename);
        this.originalFilename = theFile.getName();
    }

    /**
     * Gets the target language.
     *
     * @return   The targetLanguage value
     */
    public String getTargetLanguage() {
        return targetLanguage;
    }


    /**
     * Sets the traget language.
     *
     * @param targetLanguage  The target language
     */
    public void setTargetLanguage(String theTargetLanguage) {
        this.targetLanguage = theTargetLanguage;
    }

    /** Getter for property datatype.
     * @return Value of property datatype.
     */
    public String getDatatype()
    {
        return this.datatype;
    }

    /** Setter for property datatype.
     * @param datatype New value of property datatype.
     */
    public void setDatatype(String datatype)
    {
        this.datatype = datatype;
    }

    /**
     * Returns boolean True if the datatype is software message file, false otherwise
     */
    public boolean isSoftwareMsgDatatype() {
        int filetype = ((Integer)datatypeMap.get(datatype.toUpperCase())).intValue();
        return filetype == SOFTWAREMESSAGE;
    }

}


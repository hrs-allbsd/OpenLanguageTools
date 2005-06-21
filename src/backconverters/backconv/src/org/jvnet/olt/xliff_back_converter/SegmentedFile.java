
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SegmentedFile.java
 *
 * Created on August 8, 2002, 2:51 PM
 */
package org.jvnet.olt.xliff_back_converter;

import java.util.Collection;

/**
 * Interface for SegmentedFile objects.
 *
 * <p>SegmentedFile objects store the value of trans-unit elements of a
 * particular file element in a Xliff file.</p>
 *
 * @author    Brian Kidney
 * @version   August 8, 2002
 * @see       SegmentedFileImpl
 */
public interface SegmentedFile {

    /**
     * Gets the transUnit attribute of the SegmentedFile object
     *
     * @param transUnitId  The transUnit ID key value (Represented by the
     *                     "id" attribute in the trans-unit element of Xliff
     * @return             The transUnit value
     */
    TransUnitData getTransUnit(String transUnitId);


    /**
     * Sets the transUnitId and transUnitText attribute of the SegmentedFile
     * object.
     *
     * @param transUnitId    The new transUnitId value
     * @param transUnitText  The new transUnitText value
     */
    void setTransUnit(java.lang.String transUnitId,
                      TransUnitData transUnitText);


    /**
     * Gets the number of transUnits of the SegmentedFile object
     *
     * @return   The numberOfTransUnits value
     */
    int getNumberOfTransUnits();


    /**
     * Returns boolean True if the SegmentedFile object doesn't contain any
     * transUnit elements, false otherwise.
     *
     * @return   True if the SegmentedFile object doesn't contain any
     * transUnit elements, false otherwise.
     */
    boolean isEmpty();


    /**
     * Returns boolean True if the SegmentedFile object contains a
     * transUnit element with key transUnitId, false otherwise.
     *
     * @param transUnitId  Description of the Parameter
     * @return             Description of the Return Value
     */
    boolean containsTransUnitId(String transUnitId);


    /**
     * Gets the originalFilename attribute of the SegmentedFile object.
     * The originalFilename is the name of the file before it was segmented
     * (e.g. index.html, docbbok.xml, etc.).
     *
     * @return   The originalFilename value
     */
    String getOriginalFilename();


    /**
     * Sets the originalFilename attribute of the SegmentedFile object.
     * The originalFilename is the name of the file before it was segmented
     * (e.g. index.html, docbbok.xml, etc.).
     *
     * @param originalFilename  The new originalFilename value
     */
    void setOriginalFilename(String originalFilename);


    /**
     * Gets a collection of the SegmentedFile object transUnit elements.
     *
     * @return   The collection value
     */
    Collection getCollection();

    /**
     * Gets the target language.
     *
     * @return   The targetLanguage value
     */
    public String getTargetLanguage();


    /**
     * Sets the traget language.
     *
     * @param targetLanguage  The target language
     */
    public void setTargetLanguage(String theTargetLanguage);

    /** Getter for property datatype.
     * @return Value of property datatype.
     */
    public String getDatatype();

    /** Setter for property datatype.
     * @param datatype New value of property datatype.
     */
    public void setDatatype(String datatype);

    /**
     * Returns boolean True if the datatype is software message file, false otherwise
     */
    public boolean isSoftwareMsgDatatype();
}


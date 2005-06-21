
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.xmlmerge;
/*
 * MergingInfo.java
 *
 */
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.Attributes;
import java.io.Writer;
/**
 *
 * @author  Administrator
 */
public class MergingInfo{
    
    //the original xml file inputSource
    private InputSource originalInputSource;
    
    //the translated xml file inputSource
    private InputSource transInputSource;
    
    //the file writer used to wirte the new xml file
    private Writer  newFileWriter;
    
    //    //the attribute name should be changed in target element
    //    private String attrNameToChanged;
    //    //the new attribute vale
    //    private String newAttrValue;
    private Attributes newAttrs;
    
    //the new target element name
    private String newTargetElementName;
    
    //the depth of parent element which include the element ID attribute
    //it' available when the mergeType is MERGE_BY_PARENT_ELEMETN_ID
    private int elementIDDepth;
    //the name of parent element ID attrubute
    //it' available when the mergeType is MERGE_BY_PARENT_ELEMETN_ID
    private String IDAttrName;
    
    //the source ID attribute name,
    //it' available when the mergeType is MERGE_BY_SAME_ORDER
    private String sourceIDAttrName;
    
    //the source path array
    private String[] sourcePathArray;
    
    //merge type ,see class MergeType
    private int mergeType;
    
    /** Creates a new instance of MergingInfo */
    public MergingInfo(InputSource originalInputSource, InputSource transInputSource, Writer newFileWriter, String sourcePath) {
        this.originalInputSource = originalInputSource;
        this.transInputSource = transInputSource;
        this.newFileWriter = newFileWriter;
        this.setSourcePathArray( sourcePath );
    }
    
    public MergingInfo(InputSource originalInputSource, InputSource transInputSource, Writer newFileWriter, String[] sourcePathArray) {
        this.originalInputSource = originalInputSource;
        this.transInputSource = transInputSource;
        this.newFileWriter = newFileWriter;
        this.setSourcePathArray(sourcePathArray);
    }
    
    public MergingInfo(MergingInfo mergingInfo){
        this.elementIDDepth = mergingInfo.getElementIDDepth();
        this.mergeType = mergingInfo.getMergeType();
        this.newFileWriter = mergingInfo.getNewFileWriter();
        this.IDAttrName = mergingInfo.getIDAttrName();
        this.newTargetElementName = mergingInfo.getNewTargetElementName();
        this.sourceIDAttrName = mergingInfo.getSourceIDAttrName();
        this.setSourcePathArray(mergingInfo.getSourcePathArray());
        if(mergingInfo.getNewAttrs()!=null){
            this.newAttrs = new AttributesImpl( mergingInfo.getNewAttrs() );
        }else{
            this.newAttrs = null;
        }
        this.originalInputSource = mergingInfo.getOriginalInputSource();
        this.transInputSource = mergingInfo.getTransInputSource();
    }
    
    /** Getter for property originalInputSource.
     * @return Value of property originalInputSource.
     *
     */
    public org.xml.sax.InputSource getOriginalInputSource() {
        return originalInputSource;
    }
    
    /** Setter for property originalInputSource.
     * @param originalInputSource New value of property originalInputSource.
     *
     */
    public void setOriginalInputSource(org.xml.sax.InputSource originalInputSource) {
        this.originalInputSource = originalInputSource;
    }
    
    /** Getter for property transInputSource.
     * @return Value of property transInputSource.
     *
     */
    public org.xml.sax.InputSource getTransInputSource() {
        return transInputSource;
    }
    
    /** Setter for property transInputSource.
     * @param transInputSource New value of property transInputSource.
     *
     */
    public void setTransInputSource(org.xml.sax.InputSource transInputSource) {
        this.transInputSource = transInputSource;
    }
    
    /** Getter for property newFileWriter.
     * @return Value of property newFileWriter.
     *
     */
    public java.io.Writer getNewFileWriter() {
        return newFileWriter;
    }
    
    /** Setter for property newFileWriter.
     * @param newFileWriter New value of property newFileWriter.
     *
     */
    public void setNewFileWriter(java.io.FileWriter newFileWriter) {
        this.newFileWriter = newFileWriter;
    }
    
    /** Getter for property sourcePathArray.
     * @return Value of property sourcePathArray.
     *
     */
    public java.lang.String[] getSourcePathArray() {
        return sourcePathArray;
    }
    
    /** Setter for property sourcePathArray.
     * @param sourcePath New value of property sourcePathArray.
     *
     */
    public void setSourcePathArray(java.lang.String sourcePath) {
        sourcePathArray = sourcePath.split("/");
    }
    
    /** Setter for property sourcePathArray.
     * @param sourcePathArray New value of property sourcePathArray.
     */
    public void setSourcePathArray(java.lang.String[] sourcePathArray) {
        int length = sourcePathArray.length;
        this.sourcePathArray = new String[length];
        System.arraycopy(sourcePathArray, 0, this.sourcePathArray, 0, length);
    }
    
    /** Getter for property newTargetElementName.
     * @return Value of property newTargetElementName.
     *
     */
    public java.lang.String getNewTargetElementName() {
        return newTargetElementName;
    }
    
    /** Setter for property newTargetElementName.<br>
     * The source name will not be changed if the mothed be not called or the
     * newTargetElementName is null.
     * @param newTargetElementName New value of property newTargetElementName.
     *
     */
    public void setNewTargetElementName(java.lang.String newTargetElementName) {
        this.newTargetElementName = newTargetElementName;
    }
    
    /** Getter for property sourceIDAttrName.
     * @return Value of property sourceIDAttrName.
     *
     */
    public java.lang.String getSourceIDAttrName() {
        return sourceIDAttrName;
    }
    
    /** Setter for property sourceIDAttrName.<br>
     * The mothed must be called to set the sourceIDAttrName
     * when the mergeType is MERGE_BY_SAME_ORDER
     * @param sourceIDAttrName New value of property sourceIDAttrName.
     *
     */
    public void setSourceIDAttrName(java.lang.String sourceIDAttrName) {
        this.sourceIDAttrName = sourceIDAttrName;
    }
    
    /** Getter for property newAttrs.
     * @return Value of property newAttrs.
     *
     */
    public org.xml.sax.Attributes getNewAttrs() {
        if(this.newAttrs==null){
            return null;
        }else{
            return new AttributesImpl(this.newAttrs);
        }
    }
    
    /** Setter for property newAttrs.
     * the newAttrs will be merged into the attributes of the source.<br>
     * If some attribute of newAttrs is already in the attributes of the source,<br>
     * new attribute will replace the attribute of source.<br>
     * If the method is not called or the newAttrs is null,nothing will be done.
     * @param newAttrs New value of property newAttrs.
     *
     */
    public void setNewAttrs(org.xml.sax.Attributes newAttrs) {
        this.newAttrs = new AttributesImpl(newAttrs);
    }
    
    //    /** Setter for property newAttrValue.
    //     * the value of attribute in new file is not changed if the method is not be called.
    //     * @param newAttrValue New value of property newAttrValue.
    //     * @param attrNameToChanged The attribute name should be changed.
    //     */
    //    public void setNewAttrValue( String attrNameToChanged , String newAttrValue) {
    //        this.attrNameToChanged = attrNameToChanged;
    //        this.newAttrValue = newAttrValue;
    //    }
    //
    //     /** Getter for property attrNameToChanged.
    //     * @return Value of property attrNameToChanged.
    //     *
    //     */
    //    public java.lang.String getAttrNameToChanged() {
    //        return attrNameToChanged;
    //    }
    //
    //    /** Getter for property newAttrValue.
    //     * @return Value of property newAttrValue.
    //     *
    //     */
    //    public java.lang.String getNewAttrValue() {
    //        return newAttrValue;
    //    }
    
    
    /** Getter for property IDAttrName.
     * @return Value of property IDAttrName.
     *
     */
    public java.lang.String getIDAttrName() {
        return IDAttrName;
    }
    
    /** Getter for property elementIDDepth.
     * @return Value of property elementIDDepth.
     *
     */
    public int getElementIDDepth() {
        return elementIDDepth;
    }
    
    /** set the parent element ID attribute location
     * @param elementName the parent element name
     * @param IDAttrName the ID attribute name of the parent element
     */
    public void setIDLocation( String elementName , String IDAttrName){
        this.IDAttrName = IDAttrName;
        if(this.sourcePathArray == null){
            throw new IllegalStateException("the source path array has not be set!");
        }else{
            for(int i = 0; i<this.sourcePathArray.length; i++){
                if(elementName.equals(sourcePathArray[i])){
                    this.elementIDDepth = i;
                    return;
                }
            }
            throw new IllegalArgumentException("Can not find the element: \"" + elementName + "\"");
        }
    }
    
    /** set the parent element ID attribute location
     * @param elementIDDepth the parent element depth in the sourcePath
     * @param IDAttrName the ID attribute name of the parent element
     */
    public void setIDLocation( int elementIDDepth , String IDAttrName){
        if( (elementIDDepth<0) || (elementIDDepth >= this.sourcePathArray.length) )
            throw new IllegalArgumentException("the element ID depth should <" + this.sourcePathArray.length + " and >=0");
        this.elementIDDepth = elementIDDepth;
        this.IDAttrName = IDAttrName;
    }
    
    /** Getter for property mergeTpe.
     * @return Value of property mergeTpe.
     *
     */
    public int getMergeType() {
        return mergeType;
    }
    
    /** Setter for property mergeTpe.
     * @param mergeType New value of property mergeTpe.
     * @exception IllegalArgumentException if the mergeType is illegal
     */
    public void setMergeType(int mergeType) {
        MergeType.validate(mergeType);
        this.mergeType = mergeType;
    }
    
    /** validate the data
     */
    public void validate(){
        if( sourcePathArray == null ){
            throw new IllegalStateException("the source path has not been set");
        }
        if( originalInputSource == null ){
            throw new IllegalStateException("the original xml file input source has not been set");
        }
        if( transInputSource == null ){
            throw new IllegalStateException("the translated xml file input source has not been set");
        }
        if( newFileWriter == null ){
            throw new IllegalStateException("the new xml file has not been set");
        }
        if( this.newTargetElementName == null ){
            this.newTargetElementName = this.sourcePathArray[this.sourcePathArray.length-1];
        }
        
        switch (mergeType){
            case MergeType.MERGE_BY_PARENT_ELEMENT_ID:
                if( IDAttrName == null ){
                    throw new IllegalStateException("the element ID location has not been set");
                }
                break;
            case MergeType.MERGE_BY_SOURCE_ID:
                if( sourceIDAttrName == null){
                    throw new IllegalStateException("the attribute name of source element has not been set");
                }
                break;
        }
    }
    
    public String toString(){
        String info  =  "-------Merging Infomation start:" +
        "\noriginalInputSource = " + originalInputSource+
        "\ntransInputSource = " +  transInputSource+
        "\ntransInputSource = " +  transInputSource+
        "\nnewAttrs = " +  newAttrs +
        "\nnewTargetElementName = " +  newTargetElementName+
        "\nelementIDDepth = " +  elementIDDepth+
        "\nIDAttrName = " +  IDAttrName+
        "\nsourcePathArray = " +  sourcePathArray+
        "\nsourceIDAttrName = " + sourceIDAttrName +
        "\n------Merging Infomation end";
        return info;
    }
    
    
    
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.xmlmerge;
/*
 * ReadSource.java
 *
 */
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
/**
 * @author  Fan Song
 * The translated sax parser run over the translated xml file,  creating a java.util.Map of strings, each keyed by an
 * integer or a string indicating the order of each element (what position it was in the translated file)
 */
abstract class TransParser extends DefaultHandler{
    
    protected int currentDepth=-1;          // current depth
    protected int sourceDepth;              // the depth of source
    protected int diffPathDepth=0;          // the current depth of error path
    protected boolean isInSource = false;  // in the source element? in:true  out:false
    
    
    protected String[] sourcePath;          // the source path array
    protected Attributes newAttrs;          // the new attributes that will be merged into the source attribute
    protected String newTargetElementName;  // the new name of source element
    
    protected Map sourceContentMap = new HashMap();// source content map , store the contents of any source
    
    protected StringBuffer sourceContent = new StringBuffer(200); // store content of one store
    protected StringBuffer escapedStr = new StringBuffer();
    
    /** Creates a new instance of ReadSource */
    public TransParser(MergingInfo mergingInfo) {
        this.sourcePath           = mergingInfo.getSourcePathArray();
        this.newAttrs             = mergingInfo.getNewAttrs();
        this.newTargetElementName = mergingInfo.getNewTargetElementName();
        this.sourceDepth          = this.sourcePath.length-1;
        this.currentDepth         = -1;
        this.diffPathDepth        = 0;
        //Do not change the target element name if the newTargetElementName isn't set(equal null)
        if(this.newTargetElementName == null){ 
            this.newTargetElementName = sourcePath[sourceDepth];
        }
    }
    
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException{
        doStartElement(uri,localName,qName,atts);
    }
    
    /** what to do when start a element
     * It may be override this method in a subclass to take specific actions at the start of
     * each element
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping another exception.
     */
    protected abstract void doStartElement(String uri, String localName, String qName, Attributes atts) throws SAXException;
    
    public void endElement(String  uri, String localName, String qName) throws SAXException{
        doEndElement(uri,localName,qName);
    }
    
    /** what to do when end a element
     * It may be override this method in a subclass to take specific actions at the end of
     * each element
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping another exception.
     */
    protected abstract void doEndElement(String  uri, String localName, String qName) throws SAXException;
    
    public void characters( char ch[], int start, int length)throws SAXException{
        if(isInSource){
            generateSourceStrText(ch,start,length);
        }
    }
    
    /** write the start label to source content buffer
     */
    protected void generateSourceStrStart(String uri, String localName, String qName, Attributes atts){
        sourceContent.append("<");
        sourceContent.append(qName);
        if(atts!=null){
            generateSourceStrAttr(atts);
        }
        sourceContent.append(">");
    }
    
    /** write the text to source content buffer
     */
    protected void generateSourceStrText(char ch[], int start, int length){
        sourceContent.append(getEscapedStr(ch,start,length,false));
    }
    
    /** write the end label to source content buffer
     */
    protected void generateSourceStrEnd(String  uri, String localName, String qName){
        sourceContent.append("</");
        sourceContent.append(qName);
        sourceContent.append(">");
    }
    
    /** write the atts to source content buffer
     */
    protected void generateSourceStrAttr(Attributes atts){
        int len = atts.getLength();
        for( int i = 0; i<len ; i++){
            sourceContent.append(" ");
            sourceContent.append(atts.getQName(i));
            sourceContent.append("=\"");
            String attrValue = atts.getValue(i);
            StringBuffer escapeValue = getEscapedStr(attrValue.toCharArray(),0,attrValue.length(),true);
            sourceContent.append(escapeValue);
            sourceContent.append("\"");
        }
    }
    
    /** Change the value of the attribute whose name equals the field "attName"
     * @param atts the attributes which will be changed
     * @return The new attributes whose the appointed attribute's value is changed.
     */
    protected AttributesImpl changeAttr(Attributes atts){
        if( (atts==null) && (newAttrs==null) ){
            return null;
        }else if( (atts==null) && (newAttrs!=null) ){
            return new AttributesImpl( newAttrs );
        }
        
        AttributesImpl result = new AttributesImpl(atts);
        
        if(newAttrs==null){
            return result;
        }
        
        int len = newAttrs.getLength();
        for(int i = 0; i < len ; i++){
            String qName = newAttrs.getQName(i);
            String value = newAttrs.getValue(i);
            if(atts.getValue(qName)!=null){
                result.setValue( result.getIndex(qName) , value);
            }else{
                result.addAttribute(newAttrs.getURI(i), newAttrs.getLocalName(i),
                newAttrs.getQName(i), newAttrs.getType(i),newAttrs.getValue(i));
            }
        }
        return result;
    }
    
    
    /** get the escaped string
     */
    protected StringBuffer getEscapedStr(char ch[], int start, int length, boolean isAttVal) {
        escapedStr.setLength(0);
        for (int i = start; i < start + length; i++) {
            switch (ch[i]) {
                case '&':
                    escapedStr.append("&amp;");
                    break;
                case '<':
                    escapedStr.append("&lt;");
                    break;
                case '>':
                    escapedStr.append("&gt;");
                    break;
                case '\"':
                    if (isAttVal) {
                        escapedStr.append("&quot;");
                    } else {
                        escapedStr.append('\"');
                    }
                    break;
                default:
                    if (ch[i] > '\u007f') {
                        escapedStr.append("&#");
                        escapedStr.append(Integer.toString(ch[i]));
                        escapedStr.append(';');
                    } else {
                        escapedStr.append(ch[i]);
                    }
            }
        }
        return escapedStr;
    }
    
    /** get the map in which the source content is stored .
     *  It should be called after the parse() method is called
     */
    public java.util.Map getSourceContentMap() {
        return sourceContentMap;
    }
    
}

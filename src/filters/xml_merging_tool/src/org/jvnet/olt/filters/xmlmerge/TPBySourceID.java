
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TPBySourceID.java
 *
 */

package org.jvnet.olt.filters.xmlmerge;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.util.Map;
/**
 * The TPBySourceID class extends the TransParser class and override two method:<br>
 * doStratElement() and doEndElement().<br>
 *
 * The OPBySourceID and TPBySourceID can merge this type xml file:<br>
 * the source element has a ID attribute
 */
class TPBySourceID extends TransParser {
    
    private String sourceIDAttrName;
    private String sourceID;
    
    /** Creates a new instance of TPBySourceID */
    public TPBySourceID(MergingInfo mergingInfo){               
        super(mergingInfo);
        sourceIDAttrName = mergingInfo.getSourceIDAttrName();
    }
    
    /** override the do startElement method
     */
    protected void doStartElement(String uri, String localName, 
                        String qName, Attributes atts) 
    throws SAXException{
        currentDepth++;
        
        //in the source element
        if(isInSource){
            // append the source content to sourceContent
            generateSourceStrStart(uri, localName, qName,atts); 
            return;
            
            //in the correct path
        }else if( (0==diffPathDepth) && (qName.equals(sourcePath[currentDepth]) ) ){
            if(currentDepth == sourceDepth){   
                //start a source element
                isInSource = true;
                sourceID = getSourceID(atts);
                sourceContent.setLength(0);   //clear the sourceContent
                generateSourceStrStart(uri, localName, newTargetElementName ,changeAttr(atts) );
            }
        //in the error path
        }else{
            diffPathDepth++;
        }
    }
    
    /** override the toEndElement method
     */
    protected void doEndElement(String  uri, String localName, String qName)
    throws SAXException{
        //end a element in error path
        if(0!=diffPathDepth){  
            diffPathDepth--;
        //end source element or a element in the source element
        } else if(isInSource){ 
            
            //end source element
            if(currentDepth==sourceDepth){
                isInSource = false;
                generateSourceStrEnd(uri,localName, newTargetElementName);
                
                //store the source content
                if( sourceContentMap.containsKey(sourceID) ){
                    String msg = "Two source element can not have the same ID'" + sourceID + "'!";
                    throw new SAXException( msg );
                }
                sourceContentMap.put( sourceID, sourceContent.toString() );
                
            //end a element in source element   
            }else{   
                generateSourceStrEnd(uri,localName, qName);
            }
        }
        currentDepth--;
    }
    
    /** Get the source ID from attrs.
     * the attrs should include the attribute named sourceIDAttrName.
     * or else,the IllegalStateException will be thrown
     */
    private String getSourceID(Attributes attrs)throws SAXException{
        int len = attrs.getLength();
        for(int i=0; i<len; i++){
            if( attrs.getQName(i).equals(sourceIDAttrName) ){
                return attrs.getValue(i);
            }
        }
        throw new SAXException("Element " + sourcePath[sourceDepth] +" must have the ID attribute :" + sourceIDAttrName);
    }
    
}
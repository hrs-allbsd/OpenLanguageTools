
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TPByElementID.java
 */

package org.jvnet.olt.filters.xmlmerge;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
/**
 * The TPByElementID class extends the TransParser class and override two method:<br>
 * doStratElement() and doEndElement().<br>
 *
 * The TPByElementID and OPByElementID can merge this type xml file:<br>
 * 1.the source element ' parent element has a ID<br>
 * 2.the order of source element in original file is the same as in translated file<br>
 * 
 */
class TPByElementID extends TransParser {
    
    private int elementIDDepth;    // the depth of the element include the ID attribute
    protected String IDAttrName;     // the ID attribute name
    private int internalSourceID;  // the ID of the source element in one parent element
    private String IDPrefix;       // the new source ID prefix
    protected final char spt = '_'; // the sperator between the ID prefix and internal source ID
    
    /** Creates a new instance of TPByElementID */
    public TPByElementID(MergingInfo mergingInfo){               
        super(mergingInfo);
        this.elementIDDepth       = mergingInfo.getElementIDDepth();
        this.IDAttrName           = mergingInfo.getIDAttrName(); 
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
        }else if( (0==diffPathDepth)&& (qName.equals(sourcePath[currentDepth]))){
            if(currentDepth==elementIDDepth){   
                //start a element include the ID attribute
                IDPrefix = getSourceIDPrefix(atts); //set the ID prefix
                internalSourceID = -1;
            }
            if(currentDepth == sourceDepth){   
                //start a source element
                isInSource = true;
                internalSourceID++;
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
                String sourceIDTmp = getSourceID();
                if( sourceContentMap.containsKey( sourceIDTmp ) ){
                    String msg = "Two source element can not have the same ID'" + sourceIDTmp + "'!";
                    throw new SAXException( msg );
                }
                sourceContentMap.put( sourceIDTmp, sourceContent.toString() );
                
            //end a element in source element   
            }else{   
                generateSourceStrEnd(uri,localName, qName);
            }
        }
        currentDepth--;
    }
    
    /** get the source ID prefix i.e. the parent element ID 
     *@param attrs The attributes in which the ID attribute is.
     *@return The prefix of source ID.
     */
    protected String getSourceIDPrefix(Attributes attrs) throws SAXException{
        int len = attrs.getLength();
        for(int i=0; i<len; i++){
            if( attrs.getQName(i).equals(IDAttrName) ){
                return attrs.getValue(i);
            }
        }
        throw new SAXException("Can not find the ID attribute :" + IDAttrName);
    }
    
    /**generate the source element ID
     *return the source ID, sourceID = prefix(the parent element ID) + internal source ID.
     */
    private String getSourceID(){
        return IDPrefix + spt + internalSourceID;
    }
    
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TPBySameOrder.java
 */

package org.jvnet.olt.filters.xmlmerge;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
/**
 * The TPBySameOrder class extends the TransParser class and override two method:<br>
 * doStratElement() and doEndElement().<br>
 *
 * The TPBySameOrder and OPBySameOrder can merge this type xml file:<br>
 * the order of the source element in the original file is the same <br>
 * as in the translated file<br>
 */
class TPBySameOrder extends TransParser {
    private int sourceID;
    
    /** Creates a new instance of TPBySameOrder */
    public TPBySameOrder(MergingInfo mergingInfo){               
        super(mergingInfo);
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
            if(currentDepth == sourceDepth){   
                //start a source element
                isInSource = true;
                sourceID++;
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
                Integer iSourceID = new Integer(sourceID);
                if( sourceContentMap.containsKey( iSourceID ) ){
                    String msg = "Two source element can not have the same ID'" + sourceID + "'!";
                    throw new SAXException( msg );
                }
                sourceContentMap.put( iSourceID,
                    sourceContent.toString() );
                
            //end a element in source element   
            }else{   
                generateSourceStrEnd(uri,localName, qName);
            }
        }
        currentDepth--;
    }
    
}

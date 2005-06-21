
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * OPBySameOrder.java
 */

package org.jvnet.olt.filters.xmlmerge;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
/**
 * The OPBySameOrder class extends the OriginalParser class and override two method:<br>
 * doStratElement() and doEndElement().<br>
 *
 * The TPBySameOrder and OPBySameOrder can merge this type xml file:<br>
 * the order of the source element in the original file is the same <br>
 * as in the translated file<br>
 */
class OPBySameOrder extends OriginalParser {
    
    private int sourceID = 0;
    
    /** Creates a new instance of OPBySameOrder */
    public OPBySameOrder(MergingInfo mergingInfo){               
        super(mergingInfo);
    }
    /** override the do startElement method
     */
    protected void doStartElement(String uri, String localName,
                        String qName, Attributes atts)
    throws SAXException{
        currentDepth++;
        if(isInSource){
            return;
        }else if( (0==diffPathDepth) && (qName.equals(sourcePath[currentDepth]))){   
            //the source element start
            if(currentDepth == sourceDepth){
                isInSource = true;
                if( indent == null )
                    indent = indentTmp.toCharArray();
                sourceID++;
            }
        //come in a error path
        }else{
            diffPathDepth++;
        }
    }
    
    /** override the toEndElement method
     */
    protected void doEndElement(String uri, String localName, String qName) 
    throws SAXException{
        if(0!=diffPathDepth){   //end a element in error path
            diffPathDepth--;
        } else if(isInSource && (currentDepth==sourceDepth) ){  //end the source element
            isInSource = false;
            try{
                //get the source content should be inserted
                String source = (String)sourceContentMap.get( new Integer(sourceID) ); 
                if( source==null ){
                    String msg = "Can not find the key : '" + sourceID + "' in map!";
                    throw new SAXException(msg);
                }
                super.characters(indent, 0, indent.length);
                writer.write(source);//insert the source content
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        currentDepth--;
    }
    
}

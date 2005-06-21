
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * OPBySourceID.java
 */

package org.jvnet.olt.filters.xmlmerge;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
/**
 * The OPBySourceID class extends the OriginalParser class and override two method:<br>
 * doStratElement() and doEndElement().<br>
 *
 * The OPBySourceID and TPBySourceID can merge this type xml file:<br>
 * the source element has a ID attribute
 */
class OPBySourceID extends OriginalParser {
    
    private String sourceIDAttrName;
    private String sourceID;
    
    /** Creates a new instance of OPBySourceID */
    public OPBySourceID(MergingInfo mergingInfo){               
        super(mergingInfo);
        sourceIDAttrName = mergingInfo.getSourceIDAttrName();
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
                sourceID = getSourceID(atts);
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
                String source = (String)sourceContentMap.get( sourceID );  
                if( source==null ){
                    String msg = "Can not find the source element whose " + sourceIDAttrName + "= : '" + sourceID + "' in the translated xml file!";
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

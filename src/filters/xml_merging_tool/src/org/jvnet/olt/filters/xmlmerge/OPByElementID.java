
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * OPByElementID.java
 *
 */

package org.jvnet.olt.filters.xmlmerge;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
/**
 * The OPByElementID class extends the OriginalParser class and override two method:<br>
 * doStratElement() and doEndElement().<br>
 *
 * The TPByElementID and OPByElementID can merge this type xml file:<br>
 * 1.the source element ' parent element has a ID<br>
 * 2.the order of source element in original file is the same as in translated file<br>
 * 
 */
class OPByElementID extends OriginalParser {
    
    private int elementIDDepth;           // the depth of the element include the ID attribute
    protected String IDAttrName;            // the ID attribute name
    private int internalSourceID;         // the ID of the source element in one parent element
    private String IDPrefix;              // the new source ID prefix
    protected final char spt = '_';        // the sperator between the ID prefix and internal source ID

    /** Creates a new instance of OPByElementID */
    public OPByElementID(MergingInfo mergingInfo){               
        super(mergingInfo);
        this.elementIDDepth       = mergingInfo.getElementIDDepth();
        this.IDAttrName           = mergingInfo.getIDAttrName(); 
    }
    
    /** get the source ID prefix i.e. the parent element ID 
     *@param attrs The attributes in which the ID attribute is.
     *@return The prefix of source ID.
     */
    protected String getSourceIDPrefix(Attributes attrs)throws SAXException{
        int len = attrs.getLength();
        for(int i=0; i<len; i++){
            if( attrs.getQName(i).equals(IDAttrName) ){
                return attrs.getValue(i);
            }
        }
        return null;
    }
    
    /**generate the source element ID
     *return the source ID, sourceID = prefix(the parent element ID) + internal source ID.
     */
    private String getSourceID(){
        return IDPrefix + spt + internalSourceID;
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
            
            //the element include the ID attribute start
            if(currentDepth==elementIDDepth){
                IDPrefix = getSourceIDPrefix(atts);
                internalSourceID = -1;
            }
            
            //the source element start
            if(currentDepth == sourceDepth){
                isInSource = true;
                if( indent == null )
                    indent = indentTmp.toCharArray();
                internalSourceID++;
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
                String source = (String)sourceContentMap.get( getSourceID());  //get the source content should be inserted
                if( source==null ){
                    String msg = "Can not find the key : '" + getSourceID() + "' in map!";
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

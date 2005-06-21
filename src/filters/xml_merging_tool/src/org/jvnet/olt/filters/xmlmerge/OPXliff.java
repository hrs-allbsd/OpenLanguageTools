
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * OPXliff.java
 *
 */

package org.jvnet.olt.filters.xmlmerge;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
/**
 *
 * @author  Administrator
 */
class OPXliff extends OPByElementID {
    private final static String assistantAttrName = "restype";
    
    /** Creates a new instance of OPXliff */
    public OPXliff(MergingInfo mergingInfo) {
        super(mergingInfo);
    }
     /** get the source ID prefix i.e. the parent element ID + "_" + the restype attribute value
     *@param attrs The attributes in which the ID attribute is.
     *@return The prefix of source ID.
     */
    protected String getSourceIDPrefix(Attributes attrs)throws SAXException{
        int len = attrs.getLength();
        String id = null;
        String assAttrValue = null;
        for(int i=0; i<len; i++){
            if( attrs.getQName(i).equals(IDAttrName) ){
                id = attrs.getValue(i);
            }else if(attrs.getQName(i).equals(assistantAttrName) ){
                assAttrValue = attrs.getValue(i);
            }
        }
        if( (id==null) && (assAttrValue==null) ){
            String msg = "Can not build the source id prefix!" +
                         "id attribute value:" + id + "," +
                          assistantAttrName + " attribute value:" + assAttrValue;
            throw new SAXException(msg);
        }
        return id + spt + assAttrValue;
    }   
        
}

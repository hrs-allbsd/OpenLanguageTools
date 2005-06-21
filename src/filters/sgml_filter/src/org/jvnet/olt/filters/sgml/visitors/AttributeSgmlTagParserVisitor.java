
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TagNameSgmlTagParserVisitor.java
 *
 * Created on June 27, 2002, 1:23 PM
 */

package org.jvnet.olt.filters.sgml.visitors;

import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.NonConformantSgmlTagParser.*;
import java.util.Map;
import java.util.HashMap;
/**
 * This class is used to get the attribute specified by the use from the tag
 * supplied by the user.
 * @author  timf
 */
public class AttributeSgmlTagParserVisitor implements NonConformantSgmlTagParserVisitor {
    
    private String tagname;
    private String attribute;
    private String attributeVal;
    private String prefix;
    private String ns;
    private String defaultNameSpace;
    private Map nsMap;
    
    private boolean inTag = false;
    private boolean inAttribute = false;
    
    /** Creates a new instance of TagNameSgmlTagParserVisitor
     * @param tagname The name of this tag
     * @param attribute The attribute we're told to look for
     * @param ns The namespace this attribute is in
     * @param nsMap a mapping from current prefixes to namespaces
     */
    public AttributeSgmlTagParserVisitor(String tagname, String attribute, String ns, Map nsMap) {
        this.tagname = tagname;
        this.prefix = prefix;
        this.attribute = attribute;
        this.attributeVal = "";
        this.ns = ns;
        this.nsMap = nsMap;
        if (nsMap == null){
            this.nsMap = new HashMap();
        }
    }
    
    public Object visit(SimpleNode simpleNode, Object obj) {
        switch (simpleNode.getType()){
            case NonConformantSgmlTagParserTreeConstants.JJTTAGNAME:
                if (simpleNode.getNodeData().equals(tagname)){
                    inTag = true;
                } else {
                    inTag = false;
                    inAttribute = false;
                }
                break;
            case NonConformantSgmlTagParserTreeConstants.JJTATTNAME:
                String prefix = simpleNode.getPrefix();
                String myNameSpace = "";
                if (prefix.length() > 0){ // Sgml Nodes don't have a prefix
                    prefix = prefix.substring(0,prefix.length() -1);
                    // now work out what namespace this maps to (if any)
                    myNameSpace = (String)nsMap.get(prefix);
                    if (myNameSpace == null) myNameSpace = "";
                    else {
                        myNameSpace = myNameSpace.replaceAll("\"", "");
                    }
                }
                /* System.out.println("This prefix is " + prefix + " this namespace is " + myNameSpace); */
                
                if (simpleNode.getNodeData().equals(attribute)){
                    if (myNameSpace.equals(ns) || myNameSpace.length() == 0){
                        inAttribute = true;
                    }
                } else inAttribute = false;
                break;
            case NonConformantSgmlTagParserTreeConstants.JJTQUOTED_VALUE:
                if (inAttribute){
                    attributeVal = simpleNode.getNodeData().substring(1,simpleNode.getNodeData().length()-1);
                }
                break;
            case NonConformantSgmlTagParserTreeConstants.JJTUNQUOTED_VALUE:
                if (inAttribute){
                    attributeVal = simpleNode.getNodeData();
                }
                break;
        }
        return null;
    }
    
    public String getAttributeValue(){
        if (attributeVal == null)
            attributeVal = "";
        return attributeVal;
    }
}

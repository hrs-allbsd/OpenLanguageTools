
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.sgmltokens;

public class MarkupEntry {
    private String  m_markupString;
    private boolean m_tagWithAttributes;
    
    public MarkupEntry(String markupText, boolean tagWithAtts) {
        m_markupString = markupText;
        m_tagWithAttributes = tagWithAtts;
    }
    
    public String getMarkupText() {
        return m_markupString;
    }
    
    public boolean hasAttributes() {
        return m_tagWithAttributes;
    }
    
    public boolean equals(Object object) {
        if(! (object instanceof MarkupEntry)) { return false; }
        
        MarkupEntry other = (MarkupEntry) object;
        
        return this.m_markupString.equals(other.m_markupString);
    }
    
    public int hashCode() {
        return this.m_markupString.hashCode();
    }
}

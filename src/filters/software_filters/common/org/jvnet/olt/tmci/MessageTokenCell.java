/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/

/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmci;

public class MessageTokenCell extends TokenCell {
    private String m_key;
    private String m_comment;
    private boolean m_translatable;
    private boolean reLayoutAllowed;
    private boolean containsTranslation;
    private String translation="";
    
    public MessageTokenCell(int type,String text, String key, String comment,boolean translatable) throws IllegalArgumentException {
        super(type,text);
        m_key = key;
        m_comment = comment;
        m_translatable = translatable;
        reLayoutAllowed = true;
        containsTranslation = false;
    }
    
    public MessageTokenCell(int type,int filetype, String text, String key, String comment,boolean translatable) throws IllegalArgumentException {
        super(type,filetype,key,text);
        m_key = key;
        m_comment = comment;
        m_translatable = translatable;
        reLayoutAllowed = true;
        containsTranslation = false;
        
    }
    
    public String getKey() {
        int filetype = super.getFileType();
        if(filetype == MessageType.JAVA_PROPS  ||
                filetype == MessageType.MOZDTD_FILE ||
                filetype == MessageType.JAVA_RES ||
                filetype == MessageType.MSG_FILE ) //return msg string itself, for fuzzy match
            return super.getText();
        else
            return m_key;
    }
    
    public void setReLayoutAllowed(boolean reLayoutAllowed){
        this.reLayoutAllowed = reLayoutAllowed;
    }
    
    public boolean getReLayoutAllowed(){
        return this.reLayoutAllowed;
    }
    
    public void setTranslation(String translation){
        if (translation != null){
            this.translation = translation;
            this.containsTranslation = true;
        }
    }
    
    public String getTranslation(){
        return this.translation;
    }
    
    public String getKeyText() { return (m_key==null?"":m_key); } //return msg id in the sw file
    public String getComment() { return m_comment; }
    public boolean isTranslatable() { return m_translatable; }
    
    public boolean hasTranslation() {
        boolean retValue;
        if (this.containsTranslation){
            return true;
        } else { return false; }
    }
    
}

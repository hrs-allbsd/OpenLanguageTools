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
/*
 * TargetHandler.java
 *
 * Created on April 26, 2005, 2:42 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.util.Map;

import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.XLIFFSentence;
import org.jvnet.olt.xliff.handlers.Element;

import org.xml.sax.helpers.AttributesImpl;


/**
 *
 * @author boris
 */
public class TargetHandler extends BaseHandler {
    private Map tgtChangeSet;
    private boolean ignoreChars;
    
    /** Creates a new instance of TargetHandler */
    public TargetHandler(Context ctx) {
        super(ctx);
        
        tgtChangeSet = ctx.getTgtChangeSet();
    }
    
    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if ("target".equals(element.getQName())) {
            ignoreChars = false;

            //end tag -- just dump out
            if(!start){
                writeElement(element, start);
                return;
            }
            
            String transUnitId = ctx.getCurrentTransId().getStrId();

            //has sentence changed ? -- no
            if (!tgtChangeSet.containsKey(transUnitId)){
                writeElement(element, true);
                return;
            }

            //If the sntns has been changed write it out and forbid overwriting
            XLIFFSentence currentSntnc = (XLIFFSentence)tgtChangeSet.get(transUnitId);
            Element newElement = updateElementState(element,currentSntnc.getTranslationState(), currentSntnc.getTranslationStateQualifier());
            
            writeElement(newElement, true);
            char[] ch = currentSntnc.getSentence().toCharArray();
            writeChars(ch, 0, ch.length, false);
            
            tgtChangeSet.remove(transUnitId);
            ignoreChars = true;
        } else {
            
            //copy all mixed content unless we dumped the sentence already
            if(!ignoreChars)
                writeElement(element, start);
        }
    }
    
    /** Create new element which reflects new sentence state
     *
     */
    private org.jvnet.olt.xliff.handlers.Element updateElementState(org.jvnet.olt.xliff.handlers.Element element,String state, String stateQualifier) {
        
                
        AttributesImpl attrs = new AttributesImpl(element.getAttrs());
        if(ctx.getVersion().isXLIFF10()) {
            setAttributeValue(attrs, "state", stateQualifier + ":" + state);
            setAttributeValue(attrs, "xml:lang", ctx.getTargetLang());
        } else {
            setAttributeValue(attrs, "state", state);
            if ( stateQualifier != null && stateQualifier.length()>0 ) {
                setAttributeValue(attrs, "state-qualifier", stateQualifier);
            }
        }
        
        element = new Element(element.getPrefix(), element.getLocalName(), element.getOriginalQName(), attrs, element.getPath());
        
        return element;
    }
    
    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        if (!ignoreChars) {
            writeChars(chars, start, length);
        }
    }
    
    public void dispatchIgnorableChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws ReaderException {
        if (!ignoreChars) {
            writeChars(chars, start, length);
        }
    }

    public boolean handleSubElements() {
        return true;
    }
}

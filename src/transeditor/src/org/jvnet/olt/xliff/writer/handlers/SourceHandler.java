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
 * SourceHandler.java
 *
 * Created on April 26, 2005, 2:15 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.util.Map;

import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.XLIFFSentence;


/**
 *
 * @author boris
 */
public class SourceHandler extends BaseHandler {
    private Map srcChangeSet;
    private boolean ignoreChars;

    /** Creates a new instance of SourceHandler */
    public SourceHandler(Context ctx) {
        super(ctx);
        this.srcChangeSet = ctx.getSrcChangeSet();
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if ("source".equals(element.getQName())) {
            ignoreChars = false;
            
            //end tag -- just dump out
            if(!start){
                writeElement(element, start);
                return;
            }

            String transUnitId = ctx.getCurrentTransId().getStrId();

            //has sentence changed ? -- no
            if (!srcChangeSet.containsKey(transUnitId)){
                writeElement(element, true);
                return;
            }

            //If the sntns has been changed write it out and forbid overwriting
            XLIFFSentence currentSntnc = (XLIFFSentence)srcChangeSet.get(transUnitId);
            
            writeElement(element, true);
            char[] ch = currentSntnc.getSentence().toCharArray();
            writeChars(ch, 0, ch.length, false);
            
            srcChangeSet.remove(transUnitId);
            ignoreChars = true;
        } else {

            //copy all mixed content unless we dumped the sentence already
            if(!ignoreChars)
                writeElement(element, start);
        }
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

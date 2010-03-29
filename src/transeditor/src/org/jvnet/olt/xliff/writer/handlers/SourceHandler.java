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

import org.jvnet.olt.editor.translation.Constants;
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
            
            srcChangeSet.remove(ctx.getCurrentTransId());
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

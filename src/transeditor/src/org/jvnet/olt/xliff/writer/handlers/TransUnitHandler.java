/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * TransUnitHandler.java
 *
 * Created on April 26, 2005, 2:40 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvnet.olt.editor.translation.Constants;
import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.TrackingComments;
import org.jvnet.olt.xliff.TransUnitId;
import org.jvnet.olt.xliff.XLIFFSentence;
import org.jvnet.olt.xliff.handlers.Element;

import org.xml.sax.helpers.AttributesImpl;


/**
 *
 * @author boris
 */
public class TransUnitHandler extends BaseHandler {
    private boolean needTgt;
    private Map tgtChangeSet;
    private TransUnitId transUnitId;
    Set stopElements = new HashSet();

    {
        stopElements.add("context-group");
        stopElements.add("count-group");
        stopElements.add("prop-group");
        stopElements.add("note");
        stopElements.add("alt-trans");
    }

    /** Creates a new instance of TransUnitHandler */
    public TransUnitHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if ("trans-unit".equals(element.getQName())) {
            if (start) {
                transUnitId =  ctx.createTransUnitKey(element.getAttrs().getValue("id"));
                ctx.setCurrentTransId(transUnitId);

                tgtChangeSet = ctx.getTgtChangeSet();

                // set approved attribute for the trans-unit
                XLIFFSentence sentence = (XLIFFSentence) tgtChangeSet.get(transUnitId.getStrId());
                if (sentence != null) {
                    String unitApproved = "no";
                    if ( sentence.getTranslationState() != null && 
                            (sentence.getTranslationState().contains("approved") || sentence.getTranslationState().contains("final") )
                       )
                        unitApproved = "yes";

                    setAttributeValue(element.getAttrs(), "approved", unitApproved);
                }
            } else {
                if (targetNeedsSave()) {
                    saveTarget();
                }
                if(commentNeedsSave()){
                    saveComment();
                }
                transUnitId = null;
            }
        } else {
            if (start && stopElements.contains(element.getQName()) && targetNeedsSave()) {
                saveTarget();
            }

            if ((start && "alt-trans".equals(element.getQName())) && commentNeedsSave()) {
                saveComment();
            }
        }

        writeElement(element, start);
    }

    private void saveTarget() throws ReaderException {
        XLIFFSentence sntnc = (XLIFFSentence)tgtChangeSet.get(transUnitId.getStrId());
        String text = sntnc.getSentence();

        String state = sntnc.getTranslationState();

        if ( ! ctx.getVersion().isXLIFF10()) {
            state = "x-" + state;
        }

        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "lang", "xml:lang", "", ctx.getTargetLang());
        attrs.addAttribute(null, "state", "state", null, state);

        Element e = new Element(null, "target", "target", attrs, "/");
        writeElement(e, true);
        writeChars(text.toCharArray(), 0, text.length(), false);
        writeElement(e, false);

        tgtChangeSet.remove(transUnitId.getStrId());
    }

    private void saveComment() throws ReaderException {
        TrackingComments tc = ctx.getTrackingComments();
        String note = tc.getComment(transUnitId.getStrId());

        //EVIL HACK:note can be null when removing
        if(note != null){
            Element e = new Element(null, "note", "note", null, "/");

            writeElement(e, true);
            writeChars(note.toCharArray(), 0, note.length());
            writeElement(e, false);

            tc.setCommentModified(transUnitId.getStrId(), false);
        }
    }

    private boolean targetNeedsSave() {
        return tgtChangeSet.containsKey(transUnitId.getStrId());
    }

    private boolean commentNeedsSave() {
        // TrackingComments in ctx might be null
        boolean needSave = false;
        TrackingComments tc = ctx.getTrackingComments();
        if ( tc != null ) {
            needSave = tc.isCommentModified(ctx.getCurrentTransId().getStrId());
        }
        return needSave;
    }

    public void dispatchIgnorableChars(Element element, char[] chars, int start, int length) throws ReaderException {
        writeChars(chars, start, length);
    }

    public void dispatchChars(Element element, char[] chars, int start, int length) throws ReaderException {
        writeChars(chars, start, length);
    }

    public boolean handleSubElements() {
        return true;
    }
}

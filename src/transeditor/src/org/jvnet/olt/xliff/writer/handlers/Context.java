/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * Context.java
 *
 * Created on April 26, 2005, 2:27 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.util.Map;

import org.jvnet.olt.xliff.TrackingComments;
import org.jvnet.olt.xliff.Version;
import org.jvnet.olt.xliff.XMLDumper;


/**
 *
 * @author boris
 */
public class Context {
    private Map srcChangeSet;
    private Map tgtChangeSet;
    private String currentTransId;
    private String targetLang;
    private TrackingComments comments;
    final private XMLDumper dumper;
    final private Version version;

    /** Creates a new instance of Context */
    public Context(XMLDumper dumper, Version version) {
        this.dumper = dumper;
        this.version = version;
    }

    public Map getSrcChangeSet() {
        return srcChangeSet;
    }

    public void setSrcChangeSet(Map srcChangeSet) {
        this.srcChangeSet = srcChangeSet;
    }

    public Map getTgtChangeSet() {
        return tgtChangeSet;
    }

    public void setTgtChangeSet(Map tgtChangeSet) {
        this.tgtChangeSet = tgtChangeSet;
    }

    public String getCurrentTransId() {
        return currentTransId;
    }

    public void setCurrentTransId(String currentTransId) {
        this.currentTransId = currentTransId;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }

    public XMLDumper getDumper() {
        return dumper;
    }

    public void setTrackingComments(TrackingComments comments) {
        this.comments = comments;
    }

    public TrackingComments getTrackingComments() {
        return comments;
    }

    public Version getVersion() {
        return version;
    }
}

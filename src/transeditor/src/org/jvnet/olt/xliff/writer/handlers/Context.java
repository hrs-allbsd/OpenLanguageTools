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
import org.jvnet.olt.xliff.TransFile;
import org.jvnet.olt.xliff.TransUnitId;


/**
 *
 * @author boris
 */
public class Context {
    private Map srcChangeSet;
    private Map tgtChangeSet;
    private TransUnitId currentTransId;
    private String targetLang;
    private TransFile currentTransFile;
    private TrackingComments comments;
    private int numFiles;
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

    public void addFile(String fileName) {
        TransFile file = new TransFile(fileName, numFiles++);
        currentTransFile = file;
    }

    public TransUnitId createTransUnitKey(String id) {
        return new TransUnitId(id, currentTransFile);
    }

    public TransUnitId getCurrentTransId() {
        return currentTransId;
    }

    public void setCurrentTransId(TransUnitId currentTransId) {
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

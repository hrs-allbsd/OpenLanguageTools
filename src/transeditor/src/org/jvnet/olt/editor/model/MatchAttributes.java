/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;


/**
 * <p>Title: MatchAttributes</p>
 * <p>Description: Attributes information of Match</p>
 */
import java.util.ArrayList;


public class MatchAttributes {
    String sFileIdentifier = null;
    String sProjectId = null;
    String sWorkspaceBuild = null;
    String sModule = null;
    String sShortBookName = null;
    String sSubject = null;
    String sPartNo = null;

    public MatchAttributes(String sFileIdentifier, String sProjectId, String sWorkspaceBuild, String sModule, String sShortBookName, String sSubject, String sPartNo) {
        this.sFileIdentifier = sFileIdentifier;
        this.sProjectId = sProjectId;
        this.sWorkspaceBuild = sWorkspaceBuild;
        this.sModule = sModule;
        this.sShortBookName = sShortBookName;
        this.sSubject = sSubject;
        this.sPartNo = sPartNo;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(128);

        if (sFileIdentifier != null) {
            sb.append("File Identifier: " + sFileIdentifier + "; ");
        }

        if (sProjectId != null) {
            sb.append("Project ID: " + sProjectId + "; ");
        }

        if (sWorkspaceBuild != null) {
            sb.append("Workspace Build: " + sWorkspaceBuild + "; ");
        }

        if (sModule != null) {
            sb.append("Module: " + sModule + "; ");
        }

        if (sShortBookName != null) {
            sb.append("Short Book Name: " + sShortBookName + "; ");
        }

        if (sSubject != null) {
            sb.append("Subject: " + sSubject + "; ");
        }

        if (sPartNo != null) {
            sb.append("Part No.: " + sPartNo);
        }

        return sb.toString();
    }

    public ArrayList getAttributesList() {
        ArrayList attrList = new ArrayList();

        if (sFileIdentifier != null) {
            attrList.add("File Identifier: ");
            attrList.add(sFileIdentifier + ";  ");
        }

        if (sProjectId != null) {
            attrList.add("Project ID: ");
            attrList.add(sProjectId + ";  ");
        }

        if (sWorkspaceBuild != null) {
            attrList.add("Workspace Build: ");
            attrList.add(sWorkspaceBuild + ";  ");
        }

        if (sModule != null) {
            attrList.add("Module: ");
            attrList.add(sModule + ";  ");
        }

        if (sShortBookName != null) {
            attrList.add("Short Book Name: ");
            attrList.add(sShortBookName + ";  ");
        }

        if (sSubject != null) {
            attrList.add("Subject: ");
            attrList.add(sSubject + ";  ");
        }

        if (sPartNo != null) {
            attrList.add("Part No.: ");
            attrList.add(sPartNo);
        }

        return attrList;
    }
}

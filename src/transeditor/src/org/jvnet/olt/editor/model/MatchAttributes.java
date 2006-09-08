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
import org.jvnet.olt.editor.util.Bundle;


public class MatchAttributes {
    String sFileIdentifier = null;
    String sProjectId = null;
    String sWorkspaceBuild = null;
    String sModule = null;
    String sShortBookName = null;
    String sSubject = null;
    String sPartNo = null;

    private Bundle bundle = Bundle.getBundle(MatchAttributes.class.getName());
    
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
        StringBuilder sb = new StringBuilder(128);

        if (sFileIdentifier != null) {
            sb.append(bundle.getString("File_Identifier:_")).append(' ').append(sFileIdentifier).append("; ");
        }

        if (sProjectId != null) {
            sb.append(bundle.getString("Project_ID:_")).append(' ').append(sProjectId).append("; ");
        }

        if (sWorkspaceBuild != null) {
            sb.append(bundle.getString("Workspace_Build:_")).append(' ').append(sWorkspaceBuild).append("; ");
        }

        if (sModule != null) {
            sb.append(bundle.getString("Module:_")).append(' ').append(sModule).append("; ");
        }

        if (sShortBookName != null) {
            sb.append(bundle.getString("Short_Book_Name:_")).append(sShortBookName).append("; ");
        }

        if (sSubject != null) {
            sb.append(bundle.getString("Subject:_")).append(sSubject).append("; ");
        }

        if (sPartNo != null) {
            sb.append(bundle.getString("Part_No.:_")).append(sPartNo);
        }

        return sb.toString();
    }

    public ArrayList getAttributesList() {
        ArrayList attrList = new ArrayList();

        if (sFileIdentifier != null) {
            attrList.add(bundle.getString("File_Identifier:")+" ");
            attrList.add(sFileIdentifier + ";  ");
        }

        if (sProjectId != null) {
            attrList.add(bundle.getString("Project_ID:")+" ");
            attrList.add(sProjectId + ";  ");
        }

        if (sWorkspaceBuild != null) {
            attrList.add(bundle.getString("Workspace_Build:")+" ");
            attrList.add(sWorkspaceBuild + ";  ");
        }

        if (sModule != null) {
            attrList.add(bundle.getString("Module:")+" ");
            attrList.add(sModule + ";  ");
        }

        if (sShortBookName != null) {
            attrList.add(bundle.getString("Short_Book_Name:")+" ");
            attrList.add(sShortBookName + ";  ");
        }

        if (sSubject != null) {
            attrList.add(bundle.getString("Subject:")+" ");
            attrList.add(sSubject + ";  ");
        }

        if (sPartNo != null) {
            attrList.add(bundle.getString("Part_No.:")+" ");
            attrList.add(sPartNo);
        }

        return attrList;
    }
}

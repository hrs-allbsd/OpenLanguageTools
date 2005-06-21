/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;

import java.util.*;

import org.jvnet.olt.editor.translation.Constants;


public class AttributesFactory {
    private Set fileIdentifier = new HashSet();
    private Set projectId = new HashSet();
    private Set workspaceBuild = new HashSet();
    private Set module = new HashSet();
    private Set shortBookName = new HashSet();
    private Set subject = new HashSet();
    private Set partNo = new HashSet();
    private Map attr2valueSet = new HashMap();

    {
        attr2valueSet.put("file_identifier", fileIdentifier);
        attr2valueSet.put("project_id", projectId);
        attr2valueSet.put("workspace_build", workspaceBuild);
        attr2valueSet.put("module", module);
        attr2valueSet.put("short_book_name", shortBookName);
        attr2valueSet.put("subject", subject);
        attr2valueSet.put("part_no", partNo);
    }

    public AttributesFactory() {
    }

    public String getFileIdentifier(String strInput) {
        return add(fileIdentifier, strInput);
    }

    public String getProjectId(String strInput) {
        return add(projectId, strInput);
    }

    public String getWorkspaceBuild(String strInput) {
        return add(workspaceBuild, strInput);
    }

    public String getModule(String strInput) {
        return add(module, strInput);
    }

    public String getShorBookName(String strInput) {
        return add(shortBookName, strInput);
    }

    public String getSubject(String strInput) {
        return add(subject, strInput);
    }

    public String getPartNo(String strInput) {
        return add(partNo, strInput);
    }

    private String add(Set s, String value) {
        if ((value == null) || Constants.CONTEXT_EMPTY.equals(value)) {
            return null;
        }

        s.add(value);

        return value;
    }

    public String normalize(String attrname, String value) {
        if (attr2valueSet.containsKey(attrname)) {
            Set s = (Set)attr2valueSet.get(attrname);

            return add(s, value);
        }

        return null;
    }
}

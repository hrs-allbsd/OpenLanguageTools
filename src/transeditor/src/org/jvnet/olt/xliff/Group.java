/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * Group.java
 *
 * Created on April 19, 2005, 10:46 AM
 *
 */
package org.jvnet.olt.xliff;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author boris
 */

//TODO change id to object containg the file as well
public class Group {
    private final String id;
    private Map attrs;
    private List trUnits = new LinkedList();

    /** Creates a new instance of Group */
    public Group(String id, Map attrs) {
        this.id = id;
        this.attrs = attrs;
    }

    public void addTransUnit(String trId) {
        trUnits.add(trId);
    }

    public List getUnitIds() {
        return Collections.unmodifiableList(trUnits);
    }

    public Map getAttributeMap() {
        return Collections.unmodifiableMap(attrs);
    }
}

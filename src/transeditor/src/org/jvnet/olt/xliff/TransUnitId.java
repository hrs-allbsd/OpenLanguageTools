/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * TransUnitId.java
 *
 * Created on February 25, 2005, 11:36 AM
 */
package org.jvnet.olt.xliff;


/**
 *
 * @author boris
 */
public class TransUnitId {
    final private String id;
    final private TransFile file;

    /** Creates a new instance of TransUnitId */
    public TransUnitId(String id, TransFile file) {
        this.id = id;
        this.file = file;
    }

    public boolean equals(Object obj) {
        if (obj instanceof TransUnitId) {
            TransUnitId other = (TransUnitId)obj;

            return id.equals(other.id) && file.equals(other.file);
        }

        return false;
    }

    public String toString() {
        return "ID:" + id + " (" + file.toString() + ")";
    }

    public int hashCode() {
        return (id.hashCode() * 37) + (file.hashCode() * 13);
    }

    public String getStrId() {
        return id;
    }
}

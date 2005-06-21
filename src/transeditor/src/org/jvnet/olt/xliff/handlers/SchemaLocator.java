/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SchemaLocator.java
 *
 * Created on April 7, 2005, 12:16 PM
 *
 */
package org.jvnet.olt.xliff.handlers;

import org.jvnet.olt.xliff.Version;


/**
 *
 * @author boris
 */
public class SchemaLocator {
    private String publicId;
    private String systemId;

    public SchemaLocator(String pubId, String sysId) {
        if ((sysId == null) && (pubId == null)) {
            throw new NullPointerException("publicId and sysId are null");
        }

        this.systemId = sysId;
        this.publicId = pubId;
    }

    boolean stringsEqual(String s1, String s2) {
        return ((s1 == null) && (s2 == null)) || (((s1 != null) && (s2 != null)) && (s1.equals(s2)));
    }

    public boolean equals(Object obj) {
        if (obj instanceof SchemaLocator) {
            SchemaLocator sl = (SchemaLocator)obj;

            return stringsEqual(sl.systemId, this.systemId) && stringsEqual(sl.publicId, this.publicId);
        }

        return false;
    }

    public int hashCode() {
        return ((publicId == null) ? 37 : (publicId.hashCode() * 13)) + ((systemId == null) ? 43 : (systemId.hashCode() * 41));
    }
}

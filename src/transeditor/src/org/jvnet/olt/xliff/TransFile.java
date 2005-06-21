/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * TransFile.java
 *
 * Created on February 25, 2005, 11:36 AM
 */
package org.jvnet.olt.xliff;


/**
 *
 * @author boris
 */
public class TransFile {
    private final String fileName;
    private final long uniqueId;

    //TODO add data type, languages etc ...

    /** Creates a new instance of TransFile */
    public TransFile(String fileName, long serialId) {
        this.fileName = fileName;
        uniqueId = serialId;
    }

    public boolean equals(Object obj) {
        if (obj instanceof TransFile) {
            TransFile other = (TransFile)obj;

            return other.uniqueId == uniqueId;
        }

        return false;
    }

    public String toString() {
        return "File:" + fileName + " (id:" + uniqueId + ")";
    }

    public int hashCode() {
        return (int)(uniqueId * 37);
    }
}

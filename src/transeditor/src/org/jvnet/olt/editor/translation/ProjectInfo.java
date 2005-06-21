/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * ProjectInfo.java
 *
 * Created on March 25, 2005, 4:15 PM
 */
package org.jvnet.olt.editor.translation;


/**
 *
 * @author boris
 */
public class ProjectInfo implements Comparable {
    private final String name;
    private final String srcLang;
    private final String tgtLang;

    /** Creates a new instance of ProjectInfo */
    ProjectInfo(String name, String srcLang, String tgtLang) {
        if ((name == null) || (srcLang == null) || (tgtLang == null)) {
            throw new NullPointerException("name/src/tgt lang are null (at least one of them)");
        }

        this.name = name;
        this.tgtLang = tgtLang;
        this.srcLang = srcLang;
    }

    public String getName() {
        return name;
    }

    public String getSourceLang() {
        return srcLang;
    }

    public String getTargetLang() {
        return tgtLang;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ProjectInfo) {
            ProjectInfo other = (ProjectInfo)obj;

            return name.equals(other.name) && srcLang.equals(other.srcLang) && tgtLang.equals(other.tgtLang);
        }

        return false;
    }

    public int hashCode() {
        return (name.hashCode() * 37) + (srcLang.hashCode() * 7) + (tgtLang.hashCode() * 13);
    }

    public int compareTo(Object o) {
        ProjectInfo other = (ProjectInfo)o;

        int x = compare(name, other.name);

        if (x != 0) {
            return x;
        }

        x = compare(srcLang, other.srcLang);

        if (x != 0) {
            return x;
        }

        x = compare(tgtLang, other.tgtLang);

        if (x != 0) {
            return x;
        }

        return 0;
    }

    private int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
}

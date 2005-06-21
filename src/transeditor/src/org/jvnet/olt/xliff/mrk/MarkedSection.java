/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * MarkedSection.java
 *
 * Created on April 22, 2004, 2:09 PM
 */
package org.jvnet.olt.xliff.mrk;


/** This class encapsulates the info from a 'mrk' element that contains a marked
 * section.
 * @author  jc73554
 */
public class MarkedSection implements MrkItem {
    private String mrkString;
    private String identifier;
    private boolean ignored;

    /** Creates a new instance of IncludedIdentifier
     * @param mrkString Conetents of the 'mrk' element.
     * @param identifier The parameter entity in the marked section label.
     * @param ignored true if the marked section is an ignored marked section, and false otherwise.
     */
    public MarkedSection(String mrkString, String identifier, boolean ignored) {
        this.mrkString = mrkString;
        this.ignored = ignored;

        //  Strip the '%' and ';' characters from identifiers if they exist.
        this.identifier = cleanIdentifierString(identifier);
    }

    public boolean hasIdentifier() {
        return true;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public String getMrkString() {
        return mrkString;
    }

    public String getIdentifier() {
        return identifier;
    }

    /** This method removes '%' and ';' characters from the start and end of
     * identifiers if they are present.
     */
    private String cleanIdentifierString(String id) {
        if (id.charAt(0) == '%') {
            id = id.substring(1, id.length() - 1);
        }

        if (id.charAt(id.length() - 1) == ';') {
            id = id.substring(0, id.length() - 2);
        }

        return id;
    }
}

/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * TransUnit.java
 *
 * Created on February 25, 2005, 11:53 AM
 */
package org.jvnet.olt.xliff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 * @author boris
 */
public class TransUnit {
    TransUnitId id;
    String approved = "no";
    XLIFFSentence source;
    XLIFFSentence target;
    List altTrans = new ArrayList();

    /** Creates a new instance of TransUnit */
    public TransUnit(TransUnitId id) {
        this.id = id;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public boolean isApproved() {
        return ( this.approved != null && this.approved.equals("yes"));
    }

    public XLIFFSentence getSource() {
        return source;
    }

    public void setSource(XLIFFSentence sntnc) {
        this.source = sntnc;
    }

    public XLIFFSentence getTarget() {
        return target;
    }

    public void setTarget(XLIFFSentence sntnc) {
        this.target = sntnc;
    }

    public void addAltTrans(AltTransUnit atu) {
        altTrans.add(atu);
    }

    public boolean equals(Object obj) {
        if (obj instanceof TransUnit) {
            TransUnit other = (TransUnit)obj;

            return id.equals(other.id);
        }

        return false;
    }

    public String toString() {
        return "TransUnit: " + id + "Source:" + source;
    }

    public int hashCode() {
        return id.hashCode();
    }

    public List getAltTransUnits() {
        return Collections.unmodifiableList(altTrans);
    }

    public TransUnitId getId() {
        return id;
    }
}

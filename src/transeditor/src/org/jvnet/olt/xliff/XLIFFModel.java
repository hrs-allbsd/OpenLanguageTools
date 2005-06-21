/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * XLIFFModel.java
 *
 * Created on April 20, 2005, 4:43 PM
 *
 */
package org.jvnet.olt.xliff;

import java.util.Collection;
import java.util.Map;


/**
 *
 * @author boris
 */
public interface XLIFFModel {
    public int getSize();

    public Map getGroupZeroSource();

    public Map getGroupZeroTarget();

    public Map getGroupAltTrans();

    public String getSourceLanguage();

    public String getTargetLanguage();

    public void setTargetLanguage(String tgtLang);

    public String getProcessName();

    public String getPhaseName();

    public String getToolName();

    public String getOriginalDataType();

    public TrackingGroup getGroupTrack();

    public TrackingComments getCommentsTrack();

    public AttributesFactory getAttrFactory();

    public TrackingSourceContext getSourceContextTrack();

    //insertion ordered collection of Ids
    public Collection getIDArray();

    public void populate(org.jvnet.olt.format.GlobalVariableManager gvm);

    public Version getVersion();
}

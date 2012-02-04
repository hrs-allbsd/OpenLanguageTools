/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * Context.java
 *
 * Created on April 18, 2005, 5:05 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jvnet.olt.editor.model.Match;
import org.jvnet.olt.editor.model.MatchAttributes;
import org.jvnet.olt.editor.translation.Constants;
import org.jvnet.olt.xliff.AltTransUnit;
import org.jvnet.olt.xliff.AttributesFactory;
import org.jvnet.olt.xliff.Group;
import org.jvnet.olt.xliff.MrkContent;
import org.jvnet.olt.xliff.Note;
import org.jvnet.olt.xliff.TrackingComments;
import org.jvnet.olt.xliff.TrackingGroup;
import org.jvnet.olt.xliff.TrackingSourceContext;
import org.jvnet.olt.xliff.TransFile;
import org.jvnet.olt.xliff.TransUnit;
import org.jvnet.olt.xliff.TransUnitId;
import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.UnknownXLIFFVersionException;
import org.jvnet.olt.xliff.Version;
import org.jvnet.olt.xliff.XLIFFBasicSentence;
import org.jvnet.olt.xliff.XLIFFModel;
import org.jvnet.olt.xliff.XLIFFSentence;
import org.jvnet.olt.xliff.mrk.MrkContentTracker;


/**
 *
 * @author boris
 */
public class Context implements XLIFFModel {
    private static final Logger logger = Logger.getLogger(Context.class.getName());
    private Version version;
    private String dataType;
    private String sourceLanguageCode;
    private String targetLanguageCode;

    //TODO ARE THESE USED ?
    //TODO xliff can hae more than 1 <phase-group> each having several <phase>
    private String processName;
    private String phaseName;
    private String toolName = Constants.TOOL_NAME;
    private int numFiles;
    private List files = new LinkedList();
    private Map transUnits = new LinkedHashMap();
    private TrackingSourceContext sourceContextTrack = new TrackingSourceContext();
    private TrackingGroup groupTrack = new TrackingGroup();
    private TrackingComments commentsTrack = new TrackingComments();
    private MrkContentTracker mrkContentTracker = new MrkContentTracker();
    private String matchType;
    private String formatDiffInfo;
    private Group currentGroup;
    private TransFile currentFile;
    private TransUnit currentTransUnit;
    private AltTransUnit currentAltTransUnit;

    //TODO change name to something meaningless that noone in the Universe will understand
    //implementation note: I use a LinkedHashMap which preserves the order in which
    //pairs were inserted. I do not need to maintain idArray, instead I return the key set
    //for iteration
    private Map groupZeroSource = new LinkedHashMap(); //was: new TreeMap();
    private Map groupZeroTarget = new HashMap(); //was: TreeMap();
    private Map groupAltTrans = new HashMap(); //was:TreeMap();
    private Map sourceContextMap = new HashMap();
    private AttributesFactory attrFactory = new AttributesFactory();

    //TODO 
    private List idArray = new LinkedList();

    /** Creates a new instance of Context */
    public Context(Version version) {
        if (version == null) {
            throw new NullPointerException("version may not be null");
        }

        this.version = version;
    }

    public String getOriginalDataType() {
        return dataType;
    }

    public void setOriginalDataType(String dataType) {
        this.dataType = dataType.toUpperCase();
    }

    public void setSourceLanguage(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public void setTargetLanguage(String targetLanguageCode) {
        if ((this.targetLanguageCode != null) && !this.targetLanguageCode.equals(targetLanguageCode)) {
            throw new IllegalStateException("Trying to set different target language");
        }

        this.targetLanguageCode = targetLanguageCode;
    }

    public void addXLIFF(Version version) throws UnknownXLIFFVersionException {
        //compare against the expected version
        if (!this.version.isEqual(version)) {
            throw new UnknownXLIFFVersionException();
        }
    }

    public void commitXLIFF() {
        logger.finer("adding missing targets");

        for (Iterator i = groupZeroSource.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();

            if (!groupZeroTarget.containsKey(entry.getKey())) {
                groupZeroTarget.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addFile(String fileName) {
        TransFile file = new TransFile(fileName, numFiles++);
        files.add(file);

        currentFile = file;
    }

    public void commitFile() {
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public void addHeaderNote(Note n) {
        commentsTrack.addComment("header", n.getText());
    }

    public void addTransUnitNote(Note n) {
        commentsTrack.addComment(currentTransUnit.getId().getStrId(), n.getText());
    }

    public void addAltTransUnitNote(Note n) {
        //ignore
    }

    public void setFormatDiffInfo(String diffInfo) {
        if (currentAltTransUnit != null) {
            currentAltTransUnit.setFormatDiffInfo(diffInfo);
        }
    }

    public void setMatchType(String matchType) {
        if (currentAltTransUnit != null) {
            currentAltTransUnit.setMatchType(matchType);
        }
    }

    //TODO we could/should put groups on stack
    public void addGroup(Group g) {
        currentGroup = g;
    }

    public void commitGroup() {
        if ((currentGroup == null) || currentGroup.getUnitIds().isEmpty()) {
            return;
        }

        String firstId = (String)currentGroup.getUnitIds().get(0);
        groupTrack.addGroup(firstId, currentGroup.getUnitIds(), false, currentGroup.getAttributeMap());

        currentGroup = null;
    }

    public TransUnitId createTransUnitKey(String id) {
        return new TransUnitId(id, currentFile);
    }

    public void addTransUnit(TransUnit tu) throws ReaderException {
        if (groupZeroSource.containsKey(tu.getId().getStrId())) {
            logger.warning("TransUnit with id:" + tu.getId().getStrId() + " already exists!");
            throw new ReaderException("trans-unit with duplicate id " + tu.getId().getStrId());
        }

        transUnits.put(tu.getId(), tu);

        if (currentGroup != null) {
            currentGroup.addTransUnit(tu.getId().getStrId());
        }

        currentTransUnit = tu;
    }

    public void commitTransUnit() {
        // add trans-unit ID to the sourceContextMap if this is still empty
        if (sourceContextMap.isEmpty() ) {
            HashMap values = new HashMap();
            values.put("trans-unit id", currentTransUnit.getId().getStrId());
            this.addSourceContext("trans-unit-id", values );
        }
        sourceContextTrack.addContext(currentTransUnit.getId().getStrId(), new HashMap(sourceContextMap));
        sourceContextMap.clear();

        currentTransUnit = null;
    }

    public void registerMrk(MrkContent cntn) {
        mrkContentTracker.registerMrk(cntn);
    }

    public void addSource(String xmlLang, String contents) {
        if (xmlLang == null) {
            xmlLang = sourceLanguageCode;
        }

        if (currentAltTransUnit != null) {
            XLIFFBasicSentence srcSntnc = new XLIFFBasicSentence(contents, xmlLang);
            currentAltTransUnit.setSource(srcSntnc);
	    
        } else {
            String theKey = currentTransUnit.getId().getStrId();
            XLIFFSentence m_temp = new XLIFFSentence(contents, xmlLang, theKey);
            groupZeroSource.put(theKey, m_temp);

	    //add a source representation as target, just in case <target> is not present
	    XLIFFSentence m_temp2 = new XLIFFSentence(contents, xmlLang, theKey,"untranslated:non-translated");
            groupZeroTarget.put(theKey, m_temp2);

	    
            currentTransUnit.setSource(m_temp);
        }
    }

    public void addTarget(String xmlLang, String contents, String state, String stateQualifier) {
        if (xmlLang == null) {
            xmlLang = targetLanguageCode;
        }

        if (currentAltTransUnit != null) {
            XLIFFBasicSentence tgtSntnc = new XLIFFBasicSentence(contents, xmlLang);
            currentAltTransUnit.setTarget(tgtSntnc);
        } else {
            String theKey = currentTransUnit.getId().getStrId();
            //ugly hack - force state to "final" if TransUnit is approved
            if ( currentTransUnit.isApproved() ) {
                 if (state != null && state.contains(":")) {
                    String [] listState = state.split(":");
                    state = listState [0] + ":final";
                } else {
                    state = "final";
                }
            }   // other ugly hack force empty target sentences to status "new", if
                // are marked for review (translate toolkit behaviour)
            else if ( (contents == null || contents.length() == 0) &&
                      ( state != null && state.startsWith("needs-review") ) ) {
                state="new";

            }
            XLIFFSentence m_temp = new XLIFFSentence(contents, xmlLang, theKey, state, stateQualifier);
            groupZeroTarget.put(theKey, m_temp);

            currentTransUnit.setTarget(m_temp);
        }
    }

    public void addSourceContext(String groupName, Map values) {
        for (Iterator i = values.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();

            TrackingSourceContext.SourceContextKey key = new TrackingSourceContext.SourceContextKey(groupName, (String)entry.getKey());

            sourceContextMap.put(key, entry.getValue());
        }
    }

    public void addAltTransContext(String groupName, Map values) {
        if (!"SunTrans Attributes".equals(groupName)) {
            return;
        }

        String[] attrs = {
            "file_identifier", "project_id", "workspace_build", "module", "short_book_name",
            "subject", "part_no"
        };
        String[] vals = new String[attrs.length];

        Map newValues = new HashMap(values);

        for (int i = 0; i < attrs.length; i++) {
            String x = (String)newValues.get(attrs[i]);

            String y = attrFactory.normalize(attrs[i], x);

            newValues.put(attrs[i], y);

            vals[i] = y;
        }

        if (currentAltTransUnit != null) {
            currentAltTransUnit.setMatchAttributes(new MatchAttributes(vals[0], vals[1], vals[2], vals[3], vals[4], vals[5], vals[6]));
        }
    }

    public void addAltTrans(String matchQuality, String origin) {
        currentAltTransUnit = new AltTransUnit(matchQuality, origin);
    }

    public void commitAltTrans() {
        if (currentAltTransUnit == null) {
            return;
        }

        String theKey = currentTransUnit.getId().getStrId();

        List theList = null;

        if (groupAltTrans.containsKey(theKey)) {
            theList = (List)groupAltTrans.get(theKey);
        } else {
            theList = new ArrayList();
        }

        // Alt-Trans does not need to have a source. If no source is given, we
        // assume it is the trans-unit's source
        if ( currentAltTransUnit.getSource() == null )
                currentAltTransUnit.setSource(currentTransUnit.getSource());
        Match match = currentAltTransUnit.makeMatch();

        theList.add(match);
        groupAltTrans.put(theKey, theList);

        currentAltTransUnit = null;
    }

    //--
    public int getSize() {
        return groupZeroSource.size();
    }

    public Map getGroupZeroSource() {
        return groupZeroSource;
    }

    public Map getGroupZeroTarget() {
        return groupZeroTarget;
    }

    public Map getGroupAltTrans() {
        return groupAltTrans;
    }

    public String getSourceLanguage() {
        return sourceLanguageCode;
    }

    public String getTargetLanguage() {
        return targetLanguageCode;
    }

    public TrackingGroup getGroupTrack() {
        return groupTrack;
    }

    public TrackingComments getCommentsTrack() {
        return commentsTrack;
    }

    public AttributesFactory getAttrFactory() {
        return attrFactory;
    }

    public TrackingSourceContext getSourceContextTrack() {
        return sourceContextTrack;
    }

    public Collection getIDArray() {
        return groupZeroSource.keySet();
    }

    public void populate(org.jvnet.olt.format.GlobalVariableManager gvm) {
        mrkContentTracker.populate(gvm);
    }

    public Version getVersion() {
        return version;
    }
}

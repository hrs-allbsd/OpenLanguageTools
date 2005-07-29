/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.jvnet.olt.editor.format.InvalidFormatTypeException;
import org.jvnet.olt.editor.format.VariableManagerFactory;
import org.jvnet.olt.editor.translation.AlignmentMain;
import org.jvnet.olt.editor.translation.DocumentUndoableEdit;
import org.jvnet.olt.editor.translation.MainFrame;
import org.jvnet.olt.editor.util.NestableException;
import org.jvnet.olt.format.*;
import org.jvnet.olt.io.HTMLEscapeFilterReader;
import org.jvnet.olt.minitm.AlignedSegment;
import org.jvnet.olt.minitm.MiniTM;
import org.jvnet.olt.minitm.TMMatch;
import org.jvnet.olt.xliff.TrackingComments;
import org.jvnet.olt.xliff.TrackingGroup;
import org.jvnet.olt.xliff.TrackingSourceContext;
import org.jvnet.olt.xliff.XLIFFParser;
import org.jvnet.olt.xliff.XLIFFSentence;


/**
 * a wrapper of the parsed XML tree, which contains all of the information of the tm files.
 */
public class TMData extends PivotData {
    private static final Logger logger = Logger.getLogger(TMData.class.getName());
    public static int MatchThreshold = 75;
    public static int MaxMatchTotalNumber = 10;
    private static Vector tempMatches = new Vector();
    public static final int ERROR_NONE = 0;
    public static final int ERROR_TARGET_LANG_MISSING = 1;
    public TMSentence[] tmsentences;

    /**
     * whether or not the current file is modified, not including status
     */
    public boolean[] bMiniTMFlags;

    /**
     * whether or not the current file is modified, including status
     */
    public boolean[] bTMFlags;
    boolean automodified = false;
    boolean autopropagate = false;
    int size = 0;
    String sourceLanguageCode = null;
    String targetLanguageCode = null;
    private TransProject project;
    XLIFFParser xliffparser;
    TrackingGroup groupTracking = null;
    TrackingComments commentsTracking = null;
    TrackingSourceContext sourceContextTracking = null;
    protected String originalDataType;
    private GlobalVariableManager globalVarManager;
    private int lastError = ERROR_NONE;

    public class TMSentence {
        /*
        100%:Verified
        Auto-translated:Verified
        User:Verified
        Fuzzy:Verified
        non-translated:Verified
        100%:Translated
        Auto-translated:Translated
        User:Translated
        Fuzzy:Translated
        non-translated:Translated
        100%:Untranslated
        Auto-translated:Untranslated
        User:Untranslated
        Fuzzy:Untranslated
        non-translated:Untranslated
         */

        //status
        public final static int UNTRANSLATED = 0;
        public final static int TRANSLATED = 1;
        public final static int VERIFIED = 2;
        public final static int APPROVED = 2; //  VERIFIED and APPROVED are synonymous
        public final static int REJECTED = 3;

        //type
        public final static int UNKNOWN_TRANSLATION = 0; //non-translated
        public final static int EXACT_TRANSLATION = 1; //100%
        public final static int AUTO_TRANSLATION = 2; //auto-translated
        public final static int FUZZY_TRANSLATION = 3; //fuzzy
        public final static int USER_TRANSLATION = 4; //user

        //Public final static int MACHINE_TRANSLATION = 5;
        public final static int OVERWRITE = 0;
        public final static int ADD = 1;
        public final static int CANCEL = 2;
        public final static int CLOSE = -1;
        int iSentenceID;
        int translationType;
        int translationStatus;
        SimpleSentence source = null;
        SimpleSentence translation = null;

        //String autotranslation = null;
        String transUnitId = null;
        int matchesnumber = 0;
        int fullmatchnum = 0;
        int firstfullmatch = -1;
        Match[] matches;

        //        PivotBaseElement[] srcBaseElements;
        //        PivotBaseElement[] translationBaseElements;
        PivotBaseElement[][] matchesBaseElements;
        Map sourceContextMap = null;
        private boolean needRefreshMatchs = true;
        boolean autoTranslated = false;

        public TMSentence(int idInput) {
            iSentenceID = idInput;

            XLIFFSentence xlfSrcSentence = xliffparser.m_xlfSrcSentences[iSentenceID];
            XLIFFSentence xlfTgtSentence = xliffparser.m_xlfTgtSentences[iSentenceID];
            transUnitId = xlfSrcSentence.getTransUnitId();
            sourceContextMap = sourceContextTracking.getContext(transUnitId);

            source = new SimpleSentence(xlfSrcSentence.getVisibleSentence());
            translation = new SimpleSentence(xlfTgtSentence.getVisibleSentence());
            translationStatus = parseStateString(xlfTgtSentence.getTranslationState())[0];
            translationType = parseStateString(xlfTgtSentence.getTranslationState())[1];

            if (groupTracking.isTransunitIdInGroup(transUnitId) != -1) {
                groupTracking.addSentence(transUnitId, iSentenceID, source.getValue(), UNTRANSLATED, UNKNOWN_TRANSLATION);
            }
        }

        public int getSentenceID() {
            return iSentenceID;
        }

        public boolean build() {
            tempMatches.removeAllElements();

            List listThisMatches = (List)xliffparser.getAltTransMatchInfo(transUnitId);

            for (int i = 0; i < listThisMatches.size(); i++) {
                Match m = (Match)listThisMatches.get(i);

                int iMatchQuality = m.getMatchQuality();

                if ((i == 0) && (iMatchQuality == 100) && (translationStatus == UNTRANSLATED) && (translationType != EXACT_TRANSLATION)) {
                    //m.handleAsAppliedMatch(iSentenceID, EXACT_TRANSLATION, Match.EXACTAUTOMATCH);
                    m.handleAsAppliedMatch(this, EXACT_TRANSLATION, Match.EXACTAUTOMATCH);
                    automodified = true;
                }

                if (iMatchQuality == 100) {
                    fullmatchnum++;
                }

                matchesnumber++;
                tempMatches.addElement(m);
            }

            matches = (Match[])tempMatches.toArray(new Match[0]);

            return true;
        }

        //modified by tony
        public PivotBaseElement[] getSourceBaseElements() {
            PivotBaseElement[] srcBaseElements = source.getBaseElementsObject();

            return srcBaseElements;
        }

        public PivotBaseElement[] getTranslationBaseElements() {
            PivotBaseElement[] translationBaseElements = translation.getBaseElementsObject();

            return translationBaseElements;
        }

        public PivotBaseElement[][] getMatchesBaseElements() {
            return matchesBaseElements;
        }

        ///end of modification
        private int[] parseStateString(String strInput) {
            //if(strInput != null)
            int[] iRet = { 0, 0 };

            if (strInput == null) {
                return iRet;
            }

            int iIndex = strInput.indexOf(":");

            if (iIndex == -1) {
                return iRet;
            }

            ArrayList listReturn = new ArrayList(2);
            String strStatus = strInput.substring(iIndex + 1, strInput.length());
            String strType = strInput.substring(0, iIndex);

            //  APPROVED and VERIFIED are synonymous.
            if (strStatus.compareToIgnoreCase("verified") == 0) {
                iRet[0] = 2;
            } else if (strStatus.compareToIgnoreCase("approved") == 0) {
                iRet[0] = 2;
            } else if (strStatus.compareToIgnoreCase("rejected") == 0) {
                iRet[0] = 3;
            } else if (strStatus.compareToIgnoreCase("translated") == 0) {
                iRet[0] = 1;
            } else if (strStatus.compareToIgnoreCase("untranslated") == 0) {
                iRet[0] = 0;
            } else {
                iRet[0] = 0;
            }

            if (strType.compareToIgnoreCase("non-translated") == 0) {
                iRet[1] = 0;
            } else if (strType.compareToIgnoreCase("100-Match") == 0) {
                iRet[1] = 1;
            } else if (strType.compareToIgnoreCase("auto-translated") == 0) {
                iRet[1] = 2;
            } else if (strType.compareToIgnoreCase("fuzzy") == 0) {
                iRet[1] = 3;
            } else if (strType.compareToIgnoreCase("user") == 0) {
                iRet[1] = 4;
            } else {
                iRet[1] = 0;
            }

            return iRet;
        }

        private String combineTranslationState() {
            StringBuffer strRet = new StringBuffer(0);

            switch (translationType) {
            case 0:
                strRet.append("non-translated:");

                break;

            case 1:
                strRet.append("100-Match:");

                break;

            case 2:
                strRet.append("auto-translated:");

                break;

            case 3:
                strRet.append("fuzzy:");

                break;

            case 4:
                strRet.append("user:");

                break;
            }

            switch (translationStatus) {
            case 0:
                strRet.append("untranslated");

                break;

            case 1:
                strRet.append("translated");

                break;

            case 2:
                strRet.append("approved");

                break;

            case 3:
                strRet.append("rejected");

                break;
            }

            return strRet.toString();
        }

        public void reload() {
            if (needRefreshMatchs) {
                getMatches(true);
            }
        }

        public void setTranslationStatus(int status) {
            translationStatus = status;

            // why do we want set the same status for the entire group ?? Timf
            updateStatusForGroup(this.iSentenceID, this.transUnitId, status);
        }

        /**
         * Updating the translation status will cause to set the bTMFlag with true
         */
        public int getTranslationStatus() {
            return translationStatus;
        }

        /**
         * Updating the translation type will cause to set the bTMFlag with true
         */
        public void setTranslationType(int type) {
            translationType = type;

            // why do we want set the same type for the entire group ?? Timf
            updateTypeForGroup(this.iSentenceID, this.transUnitId, type);
        }

        public int getTranslationType() {
            return translationType;
        }

        public boolean isAutoTranslated() {
            return autoTranslated;
        }

        public void setAutoTranslated(boolean bAuto) {
            autoTranslated = bAuto;
        }

        public String getTransUintID() {
            return transUnitId;
        }

        public int hasCommented() {
            int flag = commentsTracking.hasCommented(transUnitId);

            return flag;
        }

        public void removeComment() {
            commentsTracking.setComment(transUnitId, null);
        }

        /**
         * Get the flag which indicate the translation type and if the current segment has comment
         */
        public int getTransCombinedType() {
            int flag = commentsTracking.hasCommented(transUnitId);
            int combinedType = (flag * 10) + this.translationType;

            return combinedType;
        }

        // bug 4737474
        public Match[] getMatches(boolean matchFlag) {
            if (matchFlag) {
                try {
                    TMMatch[] tmunits = project.getMiniTM().getMatchFor(source.getValue(), MatchThreshold, MaxMatchTotalNumber);

                    if ((tmunits != null) && (tmunits.length != 0)) {
                        tempMatches.clear();

                        for (int i = 0; i < matches.length; i++) {
                            tempMatches.addElement(matches[i]);
                        }

                        for (int i = 0; i < tmunits.length; i++) {
                            TMMatch minitmMatch = tmunits[i];
                            int k = 0;
                            int j = 0;
                            boolean bSame = false;

                            while (k < tempMatches.size()) {
                                // maybe exist the same translation matches
                                if (!((Match)tempMatches.elementAt(k)).existMatch(minitmMatch.getSource().toString(), minitmMatch.getTranslation().toString())) {
                                    k++;
                                } else {
                                    bSame = true;

                                    break;
                                }
                            }

                            if (!bSame) {
                                boolean bFlag = false;

                                while (j < tempMatches.size()) {
                                    if (minitmMatch.getRatioOfMatch() >= ((Match)tempMatches.elementAt(j)).getMatchQuality()) {
                                        tempMatches.insertElementAt(new SingleSegmentMatch(new org.jvnet.olt.minitm.TMUnit(minitmMatch.getDataSourceKey(), minitmMatch.getSource().toString(), minitmMatch.getTranslation().toString(), minitmMatch.getTranslatorID().toString()), minitmMatch.getRatioOfMatch(), minitmMatch.getFormatQuality()), j);
                                        bFlag = true;

                                        break;
                                    } else {
                                        j++;
                                    }
                                }

                                if (!bFlag) {
                                    tempMatches.addElement(new SingleSegmentMatch(new org.jvnet.olt.minitm.TMUnit(minitmMatch.getDataSourceKey(), minitmMatch.getSource().toString(), minitmMatch.getTranslation().toString(), minitmMatch.getTranslatorID().toString()), minitmMatch.getRatioOfMatch(), minitmMatch.getFormatQuality()));
                                }

                                matchesnumber++;

                                if (firstfullmatch == -1) {
                                    firstfullmatch = 0;
                                }

                                if (minitmMatch.getRatioOfMatch() == 100) {
                                    fullmatchnum += 1;
                                }
                            }
                        }

                        matches = (Match[])tempMatches.toArray(new Match[0]);
                        tempMatches.clear();

                        if ((getTranslationStatus() == UNTRANSLATED) && (fullmatchnum == 1)) {
                            if (autopropagate) {
                                //logger.finer("autopropagate......");
                                (matches[0]).handleAsAppliedMatch(iSentenceID, AUTO_TRANSLATION, Match.AUTOPROPAGATE);
                                needRefreshMatchs = false;
                            }
                        }
                    } else { //no matches found in mini-tm
                        needRefreshMatchs = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            matchesBaseElements = new PivotBaseElement[matches.length * 3][];

            for (int i = 0; i < matches.length; i++) {
                matchesBaseElements[3 * i] = source.getBaseElementsObject();
                matchesBaseElements[(3 * i) + 1] = (matches[i]).lrds.getBaseElementsObject();
                matchesBaseElements[(3 * i) + 2] = (matches[i]).lrdt.getBaseElementsObject();
            }

            return matches;
        }

        public int getFullMatchesNumber() {
            return fullmatchnum;
        }

        public int getMatchesNumber() {
            return matches.length;
        }

        public String getFirstFullMatch() {
            if (firstfullmatch == -1) {
                return ("");
            }

            Match match = matches[firstfullmatch];

            return match.getMatchTranslation();
        }

        public String getSource() {
            return source.getValue();
        }

        public String getTranslation() {
            return translation.getValue();
        }

        public void setSource(String s) {
            if (source == null) {
                source = new SimpleSentence(s);

                return;
            } else {
                source.setValue(s);
            }
        }

        public void setSource(String s, Vector v) {
            if (source == null) {
                source = new SimpleSentence(s);
            } else {
                source.setValue(s, v);
            }
        }

        public void setTranslation(String t) {
            if (translation == null) {
                translation = new SimpleSentence(t);

                return;
            } else {
                translation.setValue(t);
            }
        }

        public void setTranslation(String t, Vector v) {
            if (translation == null) {
                translation = new SimpleSentence(t);
            } else {
                translation.setValue(t, v);
            }
        }

        /*
        public void setTranslation(XLIFFSentence xlfsenInput) {
          //logger.finer(xlfsenInput.getSentence());
          xliffparser.m_xlfTgtSentences[iSentenceID] = xlfsenInput;
          translation = new SimpleSentence(xlfsenInput.getVisibleSentence());
        }
         */
        public int updateMiniTM(String transID, int indexInTM) {
            if ((translationStatus == TRANSLATED) && (translationType == USER_TRANSLATION)) {
                String src = source.getValue();
                String trans = translation.getValue();

                if (getTargetLanguageCode().equals("ZH") || getTargetLanguageCode().equals("ZT") || getTargetLanguageCode().equals("JA")) {
                } else {
                    if (src.endsWith(" ")) {
                        if (!trans.endsWith(" ")) {
                            trans += " ";
                        }
                    }
                }

                AlignedSegment tu = new AlignedSegment(src, trans, transID);
                TMMatch[] temp = null;

                try {
                    temp = project.getMiniTM().getMatchFor(src, 100, 1);

                    if (temp.length == 1) {
                        if (temp[0].getTranslation().toString().equals(trans)) {
                            return -1;
                        }

                        AlignmentMain.testMain2.navigateTo(indexInTM);

                        Toolkit.getDefaultToolkit().beep();

                        //B.S.: show text in text area to make sure it does not
                        // exceed the screen size
                        JPanel panel = new JPanel();

                        BorderLayout b = new BorderLayout();
                        b.setVgap(20);
                        panel.setLayout(b);
                        panel.setPreferredSize(new Dimension(400, 300));

                        JTextArea topLabel = new JTextArea("This source language segment already exists in the mini-TM, but with the following translation:");
                        topLabel.setWrapStyleWord(true);
                        topLabel.setEditable(false);
                        topLabel.setBackground(new Color(204, 204, 204));
                        topLabel.setFont(MainFrame.dlgFont);
                        topLabel.setLineWrap(true);

                        JTextArea bottomLable = new JTextArea("Do you want to overwrite the translation in the mini-TM with this new translation, or do you wish to add a new segment pair to the mini-TM?");
                        bottomLable.setWrapStyleWord(true);
                        bottomLable.setEditable(false);
                        bottomLable.setBackground(new Color(204, 204, 204));
                        bottomLable.setFont(MainFrame.dlgFont);
                        bottomLable.setLineWrap(true);

                        JTextPane textArea = new JTextPane();
                        textArea.setSize(400, 300);

                        //textArea.setFont(MainFrame.dlgFont);
                        textArea.setBackground(new Color(204, 204, 204));
                        textArea.setEditable(false);

                        //B.S. ??? Do we want to display 'null' string ?
                        textArea.setText((temp[0].getTranslation() == null) ? "null" : temp[0].getTranslation().toString());

                        JScrollPane scr = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                        scr.setBorder(null);

                        panel.add(topLabel, BorderLayout.NORTH);
                        panel.add(scr, BorderLayout.CENTER);
                        panel.add(bottomLable, BorderLayout.SOUTH);

                        Object[] message = new Object[] { panel };

                        String[] options = { "Overwrite", "Add", "Cancel" };

                        int result = JOptionPane.showOptionDialog(MainFrame.rootFrame, // the parent that the dialog blocks
                            message, // the dialog message array
                            "Duplicate Source Segment Entry Check", // the title of the dialog window
                            JOptionPane.DEFAULT_OPTION, // option type
                            JOptionPane.WARNING_MESSAGE, // message type
                            null, // optional icon, use null to use the default icon
                            options, // options string array, will be made into buttons
                            options[0]);

                        switch (result) {
                        case 0: // overwrite
                            textArea = null;
                            scr = null;
                            project.getMiniTM().updateSegment(tu, temp[0].getDataSourceKey());
                            updateNeedRefreshMatchs(true);
                            propagate(tu.getSource(), tu.getTranslation(), tu.getTranslatorID(), 100, 0);

                            return 0;

                        case 1: // add
                            textArea = null;
                            scr = null;
                            project.getMiniTM().addNewSegment(tu);
                            updateNeedRefreshMatchs(true);
                            propagate(tu.getSource(), tu.getTranslation(), tu.getTranslatorID(), 100, 1);

                            return 1;

                        case 2: // cancel
                            textArea = null;
                            scr = null;

                            return 2;

                        case -1: //close this dlg
                            textArea = null;
                            scr = null;

                            return -1;
                        }
                    }

                    project.getMiniTM().addNewSegment(tu);
                    updateNeedRefreshMatchs(true);
                    propagate(tu.getSource(), tu.getTranslation(), tu.getTranslatorID(), 100, 1);

                    return 1;
                } catch (org.jvnet.olt.minitm.MiniTMException ex) {
                    return -1;
                } finally {
                    temp = null;
                }
            }

            return -1;
        }

        void propagate(String src, String tgt, String transID, int ratioofMatch, int iFlag) {
            if (iFlag == 1) {
                Match[] temp = new Match[matches.length + 1];
                Match m = new SingleSegmentMatch(new org.jvnet.olt.minitm.TMUnit(-1, src, tgt, transID), ratioofMatch, 0);
                System.arraycopy(matches, 0, temp, 1, matches.length);

                temp[0] = m;
                fullmatchnum++;
                matches = null; //for gc
                matches = temp;
            } else { //overwrite

                Match[] temp = new Match[matches.length];
                Match m = new SingleSegmentMatch(new org.jvnet.olt.minitm.TMUnit(-1, src, tgt, transID), ratioofMatch, 0);
                System.arraycopy(matches, 0, temp, 0, matches.length);

                temp[0] = m;

                //fullmatchnum++;
                matches = null; //for gc
                matches = temp;
            }

            needRefreshMatchs = false;
        }

        /**
         * save the segment when saving the tm file.
         */
        public void saveTranslation(boolean writeProtection, FormatWrapper formatWrapper) throws NestableException {
            //  Create the string buffer
            StringBuffer sbuf = new StringBuffer();

            //  Wrap formatting in the source if necessary
            if (!writeProtection) {
                try {
                    String wrappedSource = formatWrapper.wrapFormatting(source.getValue());

                    xliffparser.m_xlfSrcSentences[iSentenceID].setSentence(wrappedSource);
                    xliffparser.saveSourceSegment(xliffparser.m_xlfSrcSentences[iSentenceID]);
                } catch (InvalidFormattingException ex) {
                    logger.throwing(getClass().getName(), "saveTranslation", ex);
                    logger.severe("An error was encountered when trying to mark up the formatting in the source string:" + iSentenceID + " value = " + source.getValue());
                    throw new NestableException(ex);
                }

                /*                catch(NestableException ne) {
                                    ne.printStackTrace();
                                }
                 */
            }

            //  Wrap formatting in the target
            try {
                String wrappedTarget = formatWrapper.wrapFormatting(translation.getValue());

                //  Put in an extra space at the end for non asian languages if
                //  it is present in the source and missing in the target.
                if (!(getTargetLanguageCode().equals("ZH") || getTargetLanguageCode().equals("ZT") || getTargetLanguageCode().equals("JA"))) {
                    String src = source.getValue();

                    if (src.endsWith(" ")) {
                        if ((wrappedTarget.length() > 0) && (wrappedTarget.charAt(wrappedTarget.length() - 1) != ' ')) {
                            wrappedTarget += " ";
                        }
                    }
                }

                xliffparser.m_xlfTgtSentences[iSentenceID].setSentence(wrappedTarget);
                xliffparser.m_xlfTgtSentences[iSentenceID].setTranslationState(combineTranslationState());
                xliffparser.saveTargetSegment(xliffparser.m_xlfTgtSentences[iSentenceID]);
            } catch (InvalidFormattingException ex) {
                logger.throwing(getClass().getName(), "saveTranslation", ex);
                logger.severe("An error was encountered when trying to mark up the formatting in the target string:" + iSentenceID + " value = " + translation.getValue());
                throw new NestableException(ex);
            }

            /*            catch(NestableException ne) {
                            ne.printStackTrace();
                        }
             */
            autoTranslated = false;
        }
    }

    /**
     * construct a TMData object using one XML document object and
     * associate it with one MiniTM project.
     */
    public TMData() {
        this.project = null;
        this.xliffparser = null;
    }

    public TMData(TransProject projectInput, XLIFFParser xparserInput) {
        this.project = projectInput;
        this.xliffparser = xparserInput;
    }

    public int getLastError() {
        return lastError;
    }

    //We may pre-set the target language wen forcing to use
    //the project language in case of missing target lang. in the xliff file
    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguageCode = targetLanguage;
    }

    /**
     * construct the children variables
     */
    public boolean build() throws OutOfMemoryError {
        lastError = ERROR_NONE;

        sourceLanguageCode = xliffparser.getSourceLanguage();

        //in case the target lang has been pre-set skip
        if (targetLanguageCode == null) {
            targetLanguageCode = xliffparser.getTargetLanguage();

            if (targetLanguageCode == null) {
                lastError = ERROR_TARGET_LANG_MISSING;

                return false;
            }
        }

        originalDataType = xliffparser.getOriginalDataType();

        globalVarManager = this.createVariableManager(originalDataType, xliffparser);
        org.jvnet.olt.editor.util.BaseElements.setFormatExtractorVariableManager(globalVarManager);

        //getSourceLanguageCode();
        //getTargetLanguageCode();
        getGroupTrack();
        getCommentsTrack();
        getSourceContextTrack();
        getSize();

        tmsentences = new TMSentence[size];
        bMiniTMFlags = new boolean[size];
        bTMFlags = new boolean[size];

        int iError = 0;

        try {
            for (int i = 0; i < size; i++) {
                iError = i;
                tmsentences[i] = new TMSentence(i);
                bMiniTMFlags[i] = false;
                bTMFlags[i] = false;
            }

            for (int i = 0; i < size; i++) {
                iError = i;
                tmsentences[i].build();
            }
        } finally {
            xliffparser.shutdown();
        }

        return true;
    }

    public void setTransProject(TransProject projectInput) {
        this.project = projectInput;
    }

    /** accessor for the project associated with the current XLIFF file
     * - this is only used by the Match data
     */
    public TransProject getTransProject() {
        if (this.project == null) {
            logger.warning("WARNING - the current project is null - what gives ?");

            //TODO throw an exception ?
        }

        return this.project;
    }

    public void setAutoPropagate(boolean bAutoPropagate) {
        autopropagate = bAutoPropagate;
    }

    /**
     * Tells us what type of data is in the TMData object
     * @return a string representign the dataType of this XLIFF file
     */
    public String getDataType() {
        return this.originalDataType;
    }

    public boolean isAutoModified() {
        return automodified;
    }

    /**
     * get the code of the source language.
     */
    public String getSourceLanguageCode() {
        /*    if(sourceLanguageCode != null) return sourceLanguageCode;
            sourceLanguageCode = xliffparser.getSourceLanguage();
         */
        return sourceLanguageCode;
    }

    /**
     * get the code of the translation language.
     */
    public String getTargetLanguageCode() {
        /*    if(targetLanguageCode != null) return targetLanguageCode;
            targetLanguageCode = xliffparser.getTargetLanguage();
            //bug 4758110
            //note: if the targetLanguageCode is null, return it and it must popup error message that indicate the target language is not specified.
         */
        return targetLanguageCode;
    }

    public int getSize() {
        size = xliffparser.getSize();

        return size;
    }

    /**
     * get the tracking group Object
     */
    public TrackingGroup getGroupTrack() {
        if (groupTracking == null) {
            groupTracking = xliffparser.getGroupTrack();
        }

        return groupTracking;
    }

    /**
     * get the tracking group Object
     */
    public TrackingComments getCommentsTrack() {
        if (commentsTracking == null) {
            commentsTracking = xliffparser.getCommentsTrack();
        }

        return commentsTracking;
    }

    /**
     * get the tracking group Object
     */
    public TrackingSourceContext getSourceContextTrack() {
        if (sourceContextTracking == null) {
            sourceContextTracking = xliffparser.getSourceContextTrack();
        }

        return sourceContextTracking;
    }

    /**
     * Update the type for group
     */
    public void updateTypeForGroup(int ID, String transunitID, int type) {
        if (!bTMFlags[ID]) {
            bTMFlags[ID] = true;
        }

        int result = groupTracking.isTransunitIdInGroup(transunitID);

        if (result == 0) {
            int size = groupTracking.getSizeOfOneGroup(transunitID);

            for (int j = 1; j < size; j++) {
                if (!bTMFlags[(ID + j)]) {
                    bTMFlags[(ID + j)] = true;
                }

                tmsentences[(ID + j)].translationType = type;
            }
        }
    }

    /**
     * Update the status for group
     */
    public void updateStatusForGroup(int ID, String transunitID, int status) {
        if (!bTMFlags[ID]) {
            bTMFlags[ID] = true;
        }

        int result = groupTracking.isTransunitIdInGroup(transunitID);

        if (result == 0) {
            int size = groupTracking.getSizeOfOneGroup(transunitID);

            for (int j = 1; j < size; j++) {
                if (!bTMFlags[(ID + j)]) {
                    bTMFlags[(ID + j)] = true;
                }

                tmsentences[(ID + j)].translationStatus = status;
            }
        }
    }

    /**
     * Given a certain sentence start index and direction, try to find the next
     * segment,whose type is as predefined.
     * Types:
     * 0 -- "AutoTranslated"
     * 1 -- "Untranslated"
     * 2 -- "100% Match"
     * 3 -- "Fuzzy Match"
     * 4 -- "User Translation"
     * 5 -- "Translated"
     * 6 -- "Verified"
     * 7 -- "Comment"
     * 8 -- "Multiple 100% match"
     */
    public int getIndexOfTM(int start, int type, int direction) {
        int iRet = -1;

        if (direction == 0) { //next

            if (start <= -1) {
                start = 0;
            } else {
                start = start + 1;
            }

            for (int i = start; i < size; i++) {
                String transunitID = tmsentences[i].getTransUintID();
                int inGroupResult = groupTracking.isTransunitIdInGroup(transunitID);

                // timf: I don't understand this,
                // why would we want to skip searching just
                // because the trans-unit is in a group ? Leaving this
                // uncommented.
                // if(inGroupResult == 1) continue;
                switch (type) {
                case 0: //autoTranslated

                    if (tmsentences[i].getTranslationType() == TMSentence.AUTO_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 1: //untranslated

                    if (tmsentences[i].getTranslationStatus() == TMSentence.UNTRANSLATED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 2: //exact_translation

                    if (tmsentences[i].getTranslationType() == TMSentence.EXACT_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 3:

                    // bugid 4715335 : needs exact fuzzy matched line
                    if (tmsentences[i].getTranslationType() == TMSentence.FUZZY_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }
                    // bugid 4750077 add some criteria
                    else if (((tmsentences[i].getTranslationStatus() == TMSentence.UNTRANSLATED) || (tmsentences[i].getTranslationType() == TMSentence.USER_TRANSLATION)) && (tmsentences[i].getMatchesNumber() > 0) && (tmsentences[i].getFullMatchesNumber() != tmsentences[i].getMatchesNumber())) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 4:

                    if (tmsentences[i].getTranslationType() == TMSentence.USER_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 5:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.TRANSLATED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 6:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.APPROVED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 7:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.REJECTED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 8:

                    if (tmsentences[i].hasCommented() == 1) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 9: //multiple 100% match

                    if (tmsentences[i].fullmatchnum > 1) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                default:
                    break;
                }
            }
        } else { //previous
            start = start - 1;

            if (start < 0) {
                return -1;
            }

            //logger.finer("start="+start);
            for (int i = start; i >= 0; i--) {
                String transunitID = tmsentences[i].getTransUintID();
                int inGroupResult = groupTracking.isTransunitIdInGroup(transunitID);

                // Timf: again, not sure why we'd want to skip searching segments
                // just becayse they're in a group.
                //if(inGroupResult == 1) continue;
                switch (type) {
                case 0: //autoTranslated

                    if (tmsentences[i].getTranslationType() == TMSentence.AUTO_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 1:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.UNTRANSLATED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 2:

                    if (tmsentences[i].getTranslationType() == TMSentence.EXACT_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 3:

                    // bugid 4715335 : needs exact fuzzy matched line
                    if (tmsentences[i].getTranslationType() == TMSentence.FUZZY_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }
                    // bugid 4750077 add some criteria
                    else if (((tmsentences[i].getTranslationStatus() == TMSentence.UNTRANSLATED) || (tmsentences[i].getTranslationType() == TMSentence.USER_TRANSLATION)) && (tmsentences[i].getMatchesNumber() > 0) && (tmsentences[i].getFullMatchesNumber() != tmsentences[i].getMatchesNumber())) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 4:

                    if (tmsentences[i].getTranslationType() == TMSentence.USER_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 5:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.TRANSLATED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 6:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.APPROVED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 7:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.REJECTED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 8:

                    if (tmsentences[i].hasCommented() == 1) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 9: //multiple 100% match

                    if (tmsentences[i].fullmatchnum > 1) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                default:
                    break;
                }
            }
        }

        return iRet;
    }

    public int getIndexOfTM(int start, int end, int type, int direction, boolean recycle) {
        int iRet = -1;

        if (direction == 0) { //next

            if (recycle) {
                start++;
            }

            for (int i = start; i <= end; i++) {
                String transunitID = tmsentences[i].getTransUintID();

                // Timf : Don't know why we're skipping groups like this
                //int inGroupResult = groupTracking.isTransunitIdInGroup(transunitID);
                //if(inGroupResult == 1) continue;
                switch (type) {
                case 0: //autoTranslated

                    if (tmsentences[i].getTranslationType() == TMSentence.AUTO_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 1: //untranslated

                    if (tmsentences[i].getTranslationStatus() == TMSentence.UNTRANSLATED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 2: //exact_translation

                    if (tmsentences[i].getTranslationType() == TMSentence.EXACT_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 3:

                    // bugid 4715335 : needs exact fuzzy matched line
                    if (tmsentences[i].getTranslationType() == TMSentence.FUZZY_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }
                    // bugid 4750077 add some criteria
                    else if (((tmsentences[i].getTranslationStatus() == TMSentence.UNTRANSLATED) || (tmsentences[i].getTranslationType() == TMSentence.USER_TRANSLATION)) && (tmsentences[i].getMatchesNumber() > 0) && (tmsentences[i].getFullMatchesNumber() != tmsentences[i].getMatchesNumber())) {
                        iRet = i;

                        return iRet;
                    }

                case 4:

                    if (tmsentences[i].getTranslationType() == TMSentence.USER_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 5:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.TRANSLATED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 6:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.APPROVED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 7:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.REJECTED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 8:

                    if (tmsentences[i].hasCommented() == 1) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 9: //multiple 100% match

                    if (tmsentences[i].fullmatchnum > 1) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                default:
                    break;
                }
            }
        } else { //previous

            if (recycle) {
                start--;
            }

            for (int i = start; i >= end; i--) {
                String transunitID = tmsentences[i].getTransUintID();

                // timf : don't know why we're skipping groups...
                // int inGroupResult = groupTracking.isTransunitIdInGroup(transunitID);
                // if(inGroupResult == 1) continue;
                switch (type) {
                case 0: //autoTranslated

                    if (tmsentences[i].getTranslationType() == TMSentence.AUTO_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 1:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.UNTRANSLATED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 2:

                    if (tmsentences[i].getTranslationType() == TMSentence.EXACT_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 3:

                    // bugid 4715335 : needs exact fuzzy matched line
                    if (tmsentences[i].getTranslationType() == TMSentence.FUZZY_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }
                    // bugid 4750077 add some criteria
                    else if (((tmsentences[i].getTranslationStatus() == TMSentence.UNTRANSLATED) || (tmsentences[i].getTranslationType() == TMSentence.USER_TRANSLATION)) && (tmsentences[i].getMatchesNumber() > 0) && (tmsentences[i].getFullMatchesNumber() != tmsentences[i].getMatchesNumber())) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 4:

                    if (tmsentences[i].getTranslationType() == TMSentence.USER_TRANSLATION) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 5:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.TRANSLATED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 6:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.APPROVED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 7:

                    if (tmsentences[i].getTranslationStatus() == TMSentence.REJECTED) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 8:

                    if (tmsentences[i].hasCommented() == 1) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                case 9: //multiple 100% match

                    if (tmsentences[i].fullmatchnum > 1) {
                        iRet = i;

                        return iRet;
                    }

                    break;

                default:
                    break;
                }
            }
        }

        return iRet;
    }

    /**
     * This method takes in characters in the string, and writes them to the writer,
     * first passing them through JohnC's HTMLEscapeFilterReader. What this does, is
     * convert ampersands, less-than and greater-than characters to an SGML/XML friendly
     * format using the &amp;amp; &amp;lt; and &amp;gt; entities
     */
    public static String wrapXMLChars(String string) throws IOException {
        StringWriter writer = new StringWriter();
        BufferedReader buf = new BufferedReader(new HTMLEscapeFilterReader(new StringReader(string)));

        while (buf.ready()) {
            int i = buf.read();

            if (i == -1) {
                break;
            } else {
                writer.write(i);
            }
        }

        return writer.toString();
    }

    private void updateNeedRefreshMatchs(boolean flag) {
        if (tmsentences != null) {
            for (int i = 0; i < tmsentences.length; i++)
                tmsentences[i].needRefreshMatchs = flag;
        }
    }

    /**
     * redo
     */
    public void redo(DocumentUndoableEdit dedit) {
        if (dedit.getPresentationName().equals("REPLACE ALL")) {
            List v = (List)dedit.getUndoObject();

            for (int i = 0; i < v.size(); i++) {
                Object[] obj = (Object[])v.get(i);
                int row = ((Integer)obj[0]).intValue();
                int index = ((Integer)obj[1]).intValue();
                String oldstring = (String)obj[2];
                String newstring = (String)obj[3];
                int oldtype = ((Integer)obj[4]).intValue();
                int newtype = ((Integer)obj[5]).intValue();

                if (dedit.isSource()) {
                    tmsentences[row + index].setSource(newstring);
                } else {
                    tmsentences[row + index].setTranslation(newstring);
                }

                tmsentences[row].setTranslationType(newtype % 10);
                tmsentences[row].setTranslationStatus(newtype / 10);
            }
        } else if (dedit.getPresentationName().startsWith("Transfer") && !dedit.getPresentationName().endsWith("1:1")) {
            List list = (List)dedit.getUndoGroupObject();
            groupTracking.addGroup((String)list.get(0), list, true);

            List v = (List)dedit.getUndoObject();

            for (int i = 0; i < v.size(); i++) {
                Object[] obj = (Object[])v.get(i);
                int row = ((Integer)obj[0]).intValue();
                int index = ((Integer)obj[1]).intValue();
                String oldstring = (String)obj[2];
                String newstring = (String)obj[3];
                int oldtype = ((Integer)obj[4]).intValue();
                int newtype = ((Integer)obj[5]).intValue();

                //logger.finer("oldtype="+oldtype+"  newtype="+newtype);
                if (dedit.isSource()) {
                    tmsentences[row + index].setSource(newstring);
                } else {
                    tmsentences[row + index].setTranslation(newstring);
                }

                tmsentences[row].setTranslationType(newtype % 10);
                tmsentences[row].setTranslationStatus(newtype / 10);
            }
        } else if (dedit.getPresentationName().startsWith("Untransfer")) {
            List list = (List)dedit.getUndoGroupObject();
            groupTracking.removeGroup((String)list.get(0));

            List v = (List)dedit.getUndoObject();

            for (int i = (v.size() - 1); i >= 0; i--) {
                Object[] obj = (Object[])v.get(i);
                int row = ((Integer)obj[0]).intValue();
                int index = ((Integer)obj[1]).intValue();
                String oldstring = (String)obj[2];
                String newstring = (String)obj[3];
                int oldtype = ((Integer)obj[4]).intValue();
                int newtype = ((Integer)obj[5]).intValue();

                if (dedit.isSource()) {
                    tmsentences[row + index].setSource(newstring);
                } else {
                    tmsentences[row + index].setTranslation(newstring);
                }

                tmsentences[row].setTranslationType(newtype % 10);
                tmsentences[row].setTranslationStatus(newtype / 10);
            }
        } else {
            if (dedit.isSource()) {
                tmsentences[dedit.getRow() + dedit.getIndex()].setSource(dedit.getNewString());

                String strTemp = dedit.getNewString();
            } else {
                tmsentences[dedit.getRow() + dedit.getIndex()].setTranslation(dedit.getNewString());

                String strTemp = dedit.getNewString();
            }

            tmsentences[dedit.getRow()].setTranslationType(dedit.getNewTransType() % 10);
            tmsentences[dedit.getRow()].setTranslationStatus(dedit.getNewTransType() / 10);
        }
    }

    /**
     * undo
     */
    public void undo(DocumentUndoableEdit dedit) {
        if (dedit.getPresentationName().equals("REPLACE ALL")) {
            List v = (List)dedit.getUndoObject();

            for (int i = (v.size() - 1); i >= 0; i--) {
                Object[] obj = (Object[])v.get(i);
                int row = ((Integer)obj[0]).intValue();
                int index = ((Integer)obj[1]).intValue();
                String oldstring = (String)obj[2];
                String newstring = (String)obj[3];
                int oldtype = ((Integer)obj[4]).intValue();
                int newtype = ((Integer)obj[5]).intValue();

                if (dedit.isSource()) {
                    tmsentences[row + index].setSource(oldstring);
                } else {
                    tmsentences[row + index].setTranslation(oldstring);
                }

                tmsentences[row].setTranslationType(oldtype % 10);
                tmsentences[row].setTranslationStatus(oldtype / 10);
            }
        } else if (dedit.getPresentationName().startsWith("Transfer") && !dedit.getPresentationName().endsWith("1:1")) {
            List list = (List)dedit.getUndoGroupObject();
            groupTracking.removeGroup((String)list.get(0));

            List v = (List)dedit.getUndoObject();

            for (int i = 0; i < v.size(); i++) {
                Object[] obj = (Object[])v.get(i);
                int row = ((Integer)obj[0]).intValue();
                int index = ((Integer)obj[1]).intValue();
                String oldstring = (String)obj[2];
                String newstring = (String)obj[3];
                int oldtype = ((Integer)obj[4]).intValue();
                int newtype = ((Integer)obj[5]).intValue();

                //logger.finer("oldtype="+oldtype+"  newtype="+newtype);
                if (dedit.isSource()) {
                    tmsentences[row + index].setSource(oldstring);
                } else {
                    tmsentences[row + index].setTranslation(oldstring);
                }

                tmsentences[row].setTranslationType(oldtype % 10);
                tmsentences[row].setTranslationStatus(oldtype / 10);
            }
        } else if (dedit.getPresentationName().startsWith("Untransfer")) {
            List list = (List)dedit.getUndoGroupObject();
            groupTracking.addGroup((String)list.get(0), list, true);

            List v = (List)dedit.getUndoObject();

            for (int i = 0; i < v.size(); i++) {
                Object[] obj = (Object[])v.get(i);
                int row = ((Integer)obj[0]).intValue();
                int index = ((Integer)obj[1]).intValue();
                String oldstring = (String)obj[2];
                String newstring = (String)obj[3];
                int oldtype = ((Integer)obj[4]).intValue();
                int newtype = ((Integer)obj[5]).intValue();

                if (dedit.isSource()) {
                    tmsentences[row + index].setSource(oldstring);
                } else {
                    tmsentences[row + index].setTranslation(oldstring);
                }

                tmsentences[row].setTranslationType(oldtype % 10);
                tmsentences[row].setTranslationStatus(oldtype / 10);
            }
        } else {
            if (dedit.isSource()) {
                tmsentences[dedit.getRow() + dedit.getIndex()].setSource(dedit.getOldString());

                String strTemp = dedit.getOldString();
            } else {
                tmsentences[dedit.getRow() + dedit.getIndex()].setTranslation(dedit.getOldString());

                String strTemp = dedit.getOldString();
            }

            tmsentences[dedit.getRow()].setTranslationType(dedit.getOldTransType() % 10);
            tmsentences[dedit.getRow()].setTranslationStatus(dedit.getOldTransType() / 10);
        }
    }

    /** This method creates a format wrapper object based on the type code passed
     * to it.
     */
    protected FormatWrapper createFormatWrapper(java.lang.String type, GlobalVariableManager gvm) {
        FormatWrapperFactory factory = null;

        try {
            factory = new FormatWrapperFactory();

            return factory.createFormatWrapper(type, gvm);
        } catch (UnsupportedFormatException ex) {
            //  If we don't support the format type supplied, then just use the
            //  PLAINTEXT wrapper.
            logger.warning("Format type '" + type + "' is unsupported. Using the plaintext formater instead.");

            try {
                return factory.createFormatWrapper("PLAINTEXT", gvm);
            } catch (UnsupportedFormatException exInner) {
                //  This cannot happen. If it does then there is something very
                //  seriously wrong. We might need to throw an exception here.
                //THIS WILL HAPPEN
                logger.throwing(getClass().getName(), "createFormatWrapper", exInner);
                logger.severe("Exception:" + exInner);

                //TODO throw an exception
                //AAAAAAAAARRRRRGGGGGGGGGGggggghhhhh!!!!!
                return null;
            }
        }
    }

    /** This method creates a <code>GlobalVariableManager</code> object to handle
     * variables defined in the original source files. It returns null if there
     * is no variable manager type for the supplied format.
     */
    protected GlobalVariableManager createVariableManager(java.lang.String formatType, XLIFFParser parser) {
        VariableManagerFactory factory = new VariableManagerFactory();

        try {
            GlobalVariableManager gvm = factory.createVariableManager(formatType);
            parser.populateVariableManager(gvm);

            return gvm;
        } catch (InvalidFormatTypeException ex) {
            logger.throwing(getClass().getName(), "createVariableManager", ex);
            logger.severe("No variable manager needed for type: " + formatType);

            //TODO throw an exception
            return null;
        }
    }

    /** This method saves all unsaved TMSentences.
     */
    public boolean saveAllTranslations(boolean autosave, boolean bFlagWriteProtection) throws NestableException {
        //  Create format wrapper.
        FormatWrapper formatWrapper = createFormatWrapper(originalDataType, globalVarManager);

        boolean anySave = false;
        //save mini-tm and sentences
        for (int i = 0; i < tmsentences.length; i++) {
            if (bTMFlags[i] || tmsentences[i].isAutoTranslated()) {
                //logger.finer("i="+i);
                tmsentences[i].saveTranslation(bFlagWriteProtection, formatWrapper);

                anySave = true;
                
                if (!autosave) {
                    bTMFlags[i] = false;
                }
            } 
        }
        
        return anySave;
    }
}

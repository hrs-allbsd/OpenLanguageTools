/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * Configuration.java
 *
 * Created on March 22, 2005, 11:01 PM
 */
package org.jvnet.olt.editor.translation;

import java.io.File;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.jvnet.olt.editor.model.TransProject;


/**
 * TODO replace all configuration keys with string constants
 * @author boris
 */
public class Configuration {
    private static final Logger logger = Logger.getLogger(Configuration.class.getName());
    private static final String PREFS_NODE_VIEW = "view";
    private static final String PREFS_NODE_OPTS = "options";
    private static final String PREFS_NODE_HISTORY_FILES = "history/files";
    private static final String PREFS_NODE_HISTORY_PROJECTS = "history/projects";
    private static final String PREFS_NODE_SHORTCUTS = "shortcuts";
    private List filesHistory = Collections.synchronizedList(new LinkedList());
    private List projectsHistory = Collections.synchronizedList(new LinkedList());
    private boolean bFlagSearchBar;
    private boolean bFlagViewSwitch;
    private boolean bFlagMatchWindow;
    private boolean bFlagAttributes;
    private boolean bFlagSourceContext;
    private boolean bFlagTagProtection;
    private boolean bFlagTagUpdate;
    private boolean bFlagSynScrolling;
    private boolean bFlagWriteProtection = true;
    private boolean bFlagTagVerifyIgnoreOrder = true;
    private boolean bFlagAutoSave;
    private int iGlobalInterval;
    private boolean bFlagTempFile;
    private boolean[] bSelectFlags = { true, true, true, true, true };
    private boolean bFlagAutoPropagate = true;
    private boolean tagProtection = true;
    private boolean bSafeExit;
    private boolean didAgreeToLicense = false;
    private String translatorID;
    private File miniTmDir;
    private String strTempFileName;
    private String strLastFile;
    private File home;
    private Map shortcutsMap = Collections.synchronizedMap(new HashMap());

    public Configuration(File home, File miniTmDir) throws ConfigurationException {
        this.miniTmDir = miniTmDir;
        this.home = home;
    }

    public void load() throws ConfigurationException {
        try {
            loadFromPrefs();
        } catch (BackingStoreException bse) {
            logger.severe("While loading prefs" + bse);
            throw new ConfigurationException(bse);
        }
    }

    public void save() throws ConfigurationException {
        try {
            saveToPrefs();
        } catch (BackingStoreException bse) {
            logger.severe("While saving config to prefs" + bse);
            throw new ConfigurationException(bse);
        }
    }

    public List getFilesHistory() {
        return filesHistory;
    }

    public List getProjectHistory() {
        return Collections.unmodifiableList(projectsHistory);
    }

    //TODO check correctness of the string
    public void addToProjectHsistory(TransProject tp) {
        if (tp == null) {
            return;
        }

        projectsHistory.remove(tp.getEncodedName());
        projectsHistory.add(0, tp.getEncodedName());
    }

    public boolean isBFlagSearchBar() {
        return bFlagSearchBar;
    }

    public void setBFlagSearchBar(boolean bFlagSearchBar) {
        this.bFlagSearchBar = bFlagSearchBar;
    }

    public boolean isBFlagViewSwitch() {
        return bFlagViewSwitch;
    }

    public void setBFlagViewSwitch(boolean bFlagViewSwitch) {
        this.bFlagViewSwitch = bFlagViewSwitch;
    }

    public boolean isBFlagMatchWindow() {
        return bFlagMatchWindow;
    }

    public void setBFlagMatchWindow(boolean bFlagMatchWindow) {
        this.bFlagMatchWindow = bFlagMatchWindow;
    }

    public boolean isBFlagAttributes() {
        return bFlagAttributes;
    }

    public void setBFlagAttributes(boolean bFlagAttributes) {
        this.bFlagAttributes = bFlagAttributes;
    }

    public boolean isBFlagSourceContext() {
        return bFlagSourceContext;
    }

    public void setBFlagSourceContext(boolean bFlagSourceContext) {
        this.bFlagSourceContext = bFlagSourceContext;
    }

    public boolean isBFlagTagProtection() {
        return bFlagTagProtection;
    }

    public void setBFlagTagProtection(boolean bFlagTagProtection) {
        this.bFlagTagProtection = bFlagTagProtection;
    }

    public boolean isBFlagTagUpdate() {
        return bFlagTagUpdate;
    }

    public void setBFlagTagUpdate(boolean bFlagTagUpdate) {
        this.bFlagTagUpdate = bFlagTagUpdate;
    }

    public boolean isBFlagSynScrolling() {
        return bFlagSynScrolling;
    }

    public void setBFlagSynScrolling(boolean bFlagSynScrolling) {
        this.bFlagSynScrolling = bFlagSynScrolling;
    }

    public boolean isBFlagWriteProtection() {
        return bFlagWriteProtection;
    }

    public void setBFlagWriteProtection(boolean bFlagWriteProtection) {
        this.bFlagWriteProtection = bFlagWriteProtection;
    }

    public boolean isBFlagTagVerifyIgnoreOrder() {
        return bFlagTagVerifyIgnoreOrder;
    }

    public void setBFlagTagVerifyIgnoreOrder(boolean bFlagTagVerifyIgnoreOrder) {
        this.bFlagTagVerifyIgnoreOrder = bFlagTagVerifyIgnoreOrder;
    }

    public boolean isBFlagAutoSave() {
        return bFlagAutoSave;
    }

    public void setBFlagAutoSave(boolean bFlagAutoSave) {
        this.bFlagAutoSave = bFlagAutoSave;
    }

    public int getIGlobalInterval() {
        return iGlobalInterval;
    }

    public void setIGlobalInterval(int iGlobalInterval) {
        this.iGlobalInterval = iGlobalInterval;
    }

    public boolean isBFlagTempFile() {
        return bFlagTempFile;
    }

    public void setBFlagTempFile(boolean bFlagTempFile) {
        this.bFlagTempFile = bFlagTempFile;
    }

    public boolean getBSelectFlag(int idx) {
        return bSelectFlags[idx];
    }

    public void setBSelectFlag(int idx, boolean bSelectFlag) {
        this.bSelectFlags[idx] = bSelectFlag;
    }

    public boolean isBFlagAutoPropagate() {
        return bFlagAutoPropagate;
    }

    public void setBFlagAutoPropagate(boolean bFlagAutoPropagate) {
        this.bFlagAutoPropagate = bFlagAutoPropagate;
    }

    public boolean isTagProtection() {
        return tagProtection;
    }

    public void setTagProtection(boolean tagProtection) {
        this.tagProtection = tagProtection;
    }

    public boolean isDidAgreeToLicense() {
        return didAgreeToLicense;
    }

    public void setDidAgreeToLicense(boolean didAgreeToLicense) {
        this.didAgreeToLicense = didAgreeToLicense;
    }

    public String getTranslatorID() {
        return translatorID;
    }

    public void setTranslatorID(String translatorID) {
        this.translatorID = translatorID;
    }

    public String getStrTempFileName() {
        return strTempFileName;
    }

    public void setStrTempFileName(String strTempFileName) {
        this.strTempFileName = strTempFileName;
    }

    public String getStrLastFile() {
        return strLastFile;
    }

    public void setStrLastFile(String strLastFile) {
        this.strLastFile = strLastFile;
    }

    public int getSelectedFlagsCount() {
        return bSelectFlags.length;
    }

    public File getMiniTMDir() {
        return miniTmDir;
    }

    public File getHome() {
        return home;
    }

    private void loadFromPrefs() throws BackingStoreException {
        Preferences prefs = preferences();

        //get all changes
        prefs.sync();

        Preferences viewNode = prefs.node(PREFS_NODE_VIEW);

        bSelectFlags[0] = viewNode.getBoolean("File", true);
        bSelectFlags[1] = viewNode.getBoolean("Edit", true);
        bSelectFlags[2] = viewNode.getBoolean("Navigation", true);
        bSelectFlags[3] = viewNode.getBoolean("Tools", true);
        bSelectFlags[4] = viewNode.getBoolean("Help", true);

        bFlagMatchWindow = viewNode.getBoolean("MatchWindow", true);
        bFlagAttributes = viewNode.getBoolean("Attributes", true);
        bFlagSourceContext = viewNode.getBoolean("SourceContext", false);
        bFlagSearchBar = viewNode.getBoolean("SearchBar", true);
        bFlagViewSwitch = viewNode.getBoolean("ViewSwitch", true);

        Preferences optNode = prefs.node(PREFS_NODE_OPTS);

        bFlagTagUpdate = optNode.getBoolean("TagUpdate", true);
        bFlagTagProtection = optNode.getBoolean("TagProtection", true);
        tagProtection = bFlagTagProtection;
        bFlagSynScrolling = optNode.getBoolean("SynScrolling", true);
        bFlagWriteProtection = optNode.getBoolean("WriteProtection", true);
        bFlagTagVerifyIgnoreOrder = optNode.getBoolean("TagVerifyIgnoreOrder", true);
        bFlagAutoPropagate = optNode.getBoolean("AutoPropagation", true);
        bFlagAutoSave = optNode.getBoolean("AutoSave", true);
        iGlobalInterval = optNode.getInt("AutoSaveInterval", 5);

        TMPrintOptionDlg.contentType = optNode.getInt("PrintContentType", 1);
        TMPrintOptionDlg.tagType = optNode.getInt("PrintTagType", 1);
        TMPrintOptionDlg.showType = optNode.getBoolean("PrintShowType", true);
        TMPrintOptionDlg.showNumber = optNode.getBoolean("PrintShowNum", true);

        translatorID = optNode.get("TranslatorID", null);
        didAgreeToLicense = optNode.getBoolean("LicenseShow", false);

        strLastFile = ".";

        Preferences fileHistNode = prefs.node(PREFS_NODE_HISTORY_FILES);

        PreferencesUtils.readListFromPrefs(fileHistNode, filesHistory);

        strLastFile = filesHistory.isEmpty() ? "." : (String)filesHistory.iterator().next();

        Preferences projHistNode = prefs.node(PREFS_NODE_HISTORY_PROJECTS);
        PreferencesUtils.readListFromPrefs(projHistNode, projectsHistory);

        Preferences shortcutsNode = prefs.node(PREFS_NODE_SHORTCUTS);
        PreferencesUtils.readMapFromPrefs(shortcutsNode, shortcutsMap);

        //TODO put into a separate node like RunTime ???
        //TODO think of a better way of storing the last open file...
        bFlagTempFile = optNode.getBoolean("TempFile", false);

        String tmpFile = optNode.get("TempFileName", null);

        setStrTempFileName(((tmpFile != null) && (tmpFile.trim().length() == 0)) ? null : tmpFile);

        prefs.flush();
    }

    private void saveToPrefs() throws BackingStoreException {
        Preferences prefs = preferences();

        Preferences viewNode = prefs.node(PREFS_NODE_VIEW);

        viewNode.putBoolean("File", bSelectFlags[0]);
        viewNode.putBoolean("Edit", bSelectFlags[1]);
        viewNode.putBoolean("Navigation", bSelectFlags[2]);
        viewNode.putBoolean("Tools", bSelectFlags[3]);
        viewNode.putBoolean("Help", bSelectFlags[4]);
        viewNode.putBoolean("SearchBar", bFlagSearchBar);
        viewNode.putBoolean("MatchWindow", bFlagMatchWindow);
        viewNode.putBoolean("Attributes", bFlagAttributes);
        viewNode.putBoolean("SourceContext", bFlagSourceContext);
        viewNode.putBoolean("ViewSwitch", bFlagViewSwitch);

        viewNode.flush();

        Preferences optNode = prefs.node(PREFS_NODE_OPTS);

        optNode.putBoolean("TagUpdate", bFlagTagUpdate);
        optNode.putBoolean("TagProtection", bFlagTagProtection);
        optNode.putBoolean("SynScrolling", bFlagSynScrolling);
        optNode.putBoolean("WriteProtection", bFlagWriteProtection);
        optNode.putBoolean("TagVerifyIgnoreOrder", bFlagTagVerifyIgnoreOrder);
        optNode.putBoolean("AutoPropagation", bFlagAutoPropagate);
        optNode.putBoolean("AutoSave", bFlagAutoSave);
        optNode.putInt("AutoSaveInterval", iGlobalInterval);
        optNode.putBoolean("TempFile", bFlagTempFile);

        if (bSafeExit) {
            optNode.put("TempFileName", strTempFileName);
        } else {
            optNode.remove("TempFileName");
        }

        optNode.putInt("PrintContentType", TMPrintOptionDlg.contentType);
        optNode.putInt("PrintTagType", TMPrintOptionDlg.tagType);
        optNode.putBoolean("PrintShowType", TMPrintOptionDlg.showType);
        optNode.putBoolean("PrintShowNum", TMPrintOptionDlg.showNumber);

        optNode.put("TranslatorID", translatorID);
        optNode.putBoolean("LicenseShow", didAgreeToLicense);

        optNode.flush();

        Preferences fileHistNode = prefs.node(PREFS_NODE_HISTORY_FILES);
        PreferencesUtils.writeListToPrefs(fileHistNode, filesHistory, "File");
        fileHistNode.flush();

        Preferences projHistNode = prefs.node(PREFS_NODE_HISTORY_PROJECTS);
        PreferencesUtils.writeListToPrefs(projHistNode, projectsHistory, "Project");
        projHistNode.flush();

        Preferences shortcutsNode = prefs.node(PREFS_NODE_SHORTCUTS);
        PreferencesUtils.writeMapToPrefs(shortcutsNode, shortcutsMap);
        shortcutsNode.flush();

        prefs.flush();
        prefs.sync();
    }

    private Preferences preferences() {
        return Preferences.userNodeForPackage(this.getClass());
    }

    public Map getShortcuts() {
        return shortcutsMap;
    }
}

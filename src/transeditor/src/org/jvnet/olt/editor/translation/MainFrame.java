/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

import org.jvnet.olt.editor.commands.ApproveAllExactMatchSegmentsCommand;
import org.jvnet.olt.editor.commands.ApproveAllTranslatedSegmentsCommand;
import org.jvnet.olt.editor.commands.ApproveSegmentAndNextCommand;
import org.jvnet.olt.editor.commands.ApproveSegmentCommand;
import org.jvnet.olt.editor.commands.RejectAllExactMatchSegmentsCommand;
import org.jvnet.olt.editor.commands.RejectAllTranslatedSegmentsCommand;
import org.jvnet.olt.editor.commands.RejectSegmentAndNextCommand;
import org.jvnet.olt.editor.commands.RejectSegmentCommand;
import org.jvnet.olt.editor.filesplit.XliffMergingController;
import org.jvnet.olt.editor.filesplit.XliffSplittingController;
import org.jvnet.olt.editor.model.*;
import org.jvnet.olt.editor.model.TMData.TMSentence;
import org.jvnet.olt.editor.spellchecker.SpellCheckerAPI;
import org.jvnet.olt.editor.translation.preview.FilePreviewPane;
import org.jvnet.olt.editor.util.*;
import org.jvnet.olt.minitm.MiniTMException;


/**
 *
 * @author      Jim Wang
 * @author      Tony Wu
 * @version     3.0 build 4, 09/24/2001
 * @since       Sun Translation Editor 1.0
 */

/*
 * Maintenance note - this class is perhaps a little unwieldy. Maybe.
 * Just a small bit. No, really... (Netbeans code-folding helps here)
 */
public class MainFrame extends JFrame implements PropertyChangeListener, ItemListener {
    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());

    /**
     * Home directory, User directory
     */

    //The home has moved to config :-)
    //public static String strHome = System.getProperty("user.home");
    public static final String strCurrentDir = System.getProperty("user.dir");
    public static Color DEFAULT_BACK_GROUND = new Color(250, 250, 240);

    /**
     * image path
     */
    public static String strImagePath = ImagePath.PATH;

    /**
     * font used by all the dialogs
     */
    public static Font dlgFont = new Font("Dialog", Font.BOLD, 13);

    /**
     * the menu bar of this frame
     */
    public static JMenuBar menuBar = new JMenuBar();
    static SourceContextFrame jSourceContextFrame = null; // Needed in another class : encapsulation broken.
    public static MatchInfoPanel myMatchstatusBar = new MatchInfoPanel();

    /**
     *  the last opened file
     */
    /**
     * undo/redo manager
     */
    public static PivotUndoManager undo = null;

    /**
     * variables to indicate the current selected segment and selected target
     */
    public static JTable curTable = null;
    public static int curRow = -1;
    public static int curSentence = -1;

    /**
     * whether or not current file is modified
     */
    public static boolean bHasModified = false;

    /**
     * variable for global active text componet
     */
    public static JTextComponent activeComponent = null;

    /**
     * variables for managing mini-tm projects
     *
     *moved to backend and configuration;
     */

    //public static Vector historyProjects = new Vector();
    //public static String sourceLan;
    //public static String targetLan;
    //public static String translatorID;
    //public static TransProject project = null;

    /**
     * languages image flag
     */

    //TODO replace by queries
    public static String strSrcFlag;
    public static String strDestFlag;
    public static ImagePackage ip = null;
    public static String errString = null;
    public static String resultString = null;

    //current dictionary for spellchecker
    public static String dictLang = null;

    //spellchecker dialog instance
    public static SpellCheckerDIALOG spellDlg = null;

    /**
     * the static instance of this frame
     */
    public static MainFrame rootFrame;

    // bug 4714740
    public static boolean canCut = false;
    public static boolean canCopy = false;
    public static boolean canPaste = false;

    /** This variable stores the last selected option for the Font selection dialog */
    private static int fontSelectionOption = FontQueryDialog.SMALLFONT;

    //  Maintenance note: the code below is suboptimal to put it mildly, but it works.

    /** Default Font accessor information */
    private static java.awt.Font _defaultFont = new java.awt.Font("Dialog", Font.PLAIN, 12);
    Backend backend = Backend.instance();

    /**
     * file menus
     */
    private JMenu menuFile = new JMenu();
    private JMenuItem jMenuOpenProject = new JMenuItem();
    private JMenuItem jMenuNewProject = new JMenuItem();
    private JMenuItem jMenuOpen = new JMenuItem();
    private JMenuItem jMenuSave = new JMenuItem();
    private JMenuItem jMenuSaveMiniTM = new JMenuItem();
    private JMenuItem jMenuSaveAs = new JMenuItem();
    private JMenuItem jMenuClose = new JMenuItem();
    private JMenuItem jMenuPrintOptions = new JMenuItem();
    private JMenuItem jMenuPrint = new JMenuItem();
    private JMenuItem jMenuExit = new JMenuItem();

    /**
     * record the user's opened file history
     */
    private JMenu historyMenu;

    /**
     * edit menu
     */
    private JMenu menuEdit = new JMenu();
    private JMenuItem jMenuUndo = new JMenuItem();
    private JMenuItem jMenuRedo = new JMenuItem();
    private JMenuItem jMenuCut = new JMenuItem();
    private JMenuItem jMenuCopy = new JMenuItem();
    private JMenuItem jMenuPaste = new JMenuItem();

    //-------------------------------------
    // These two are needed in another class: ecpsulation broken.
    JMenuItem jMenuTransfer = new JMenuItem();
    JMenuItem jMenuUntransfer = new JMenuItem();

    //-------------------------------------
    private JMenuItem jMenuCopySource = new JMenuItem();
    private JMenuItem jMenuCopySourceTag = new JMenuItem();
    private JMenuItem jMenuClearTarget = new JMenuItem();

    //-------------------------------------
    private JMenu jMenuMarkSeg = new JMenu();
    private JMenuItem jMenuMarkSegTrans = new JMenuItem();
    private JMenuItem jMenuMarkSegUnTrans = new JMenuItem();
    private JMenuItem jMenuMarkSegReviewed = new JMenuItem();
    private JMenu jMenuMarkExactMatch = new JMenu();
    private JMenuItem jMenuMarkExactMatchTrans = new JMenuItem();
    private JMenuItem jMenuMarkExactMatchReviewed = new JMenuItem();
    private JMenu jMenuMarkAllSeg = new JMenu();
    private JMenuItem jMenuMarkAllSegTrans = new JMenuItem();
    private JMenuItem jMenuMarkAllSegReviewed = new JMenuItem();
    private JMenuItem jMenuMarkTranslatedAndNext = new JMenuItem();
    private JMenuItem jMenuMarkReviewedAndNext = new JMenuItem();
    private JMenuItem jMenuSearch = new JMenuItem();
    private JMenuItem jMenuEditComment = new JMenuItem();
    private JMenuItem jMenuDeleteComment = new JMenuItem();
    private JMenu jMenuSegComment = new JMenu();
    private JMenuItem jMenuFileComment = new JMenuItem();
    private JMenuItem jMenuAddComment = new JMenuItem();

    /**
     * view menu
     */
    private JMenu menuView = new JMenu();
    private JMenu jMenuShowOption = new JMenu();
    private JCheckBoxMenuItem jCBMenuFile = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuEditing = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuSearch = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuNavi = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuHelp = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuMatchWindow = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuAttributes = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuSourceContext = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuSearchBar = new JCheckBoxMenuItem();
    private JMenuItem jMenuItemShowAll = new JMenuItem();
    private JMenuItem jMenuItemHideAll = new JMenuItem();
    private JCheckBoxMenuItem jCBMenuSHTag = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuAbbrTagsDisplay = new JCheckBoxMenuItem();
    private JMenuItem jMenuColor = new JMenuItem();
    private JMenuItem jMenuFonts = new JMenuItem();
    private JMenu jMenuHOrV = new JMenu();
    private JMenuItem jMenuHor = new JMenuItem();
    private JMenuItem jMenuVer = new JMenuItem();

    /**
     * navigate menu
     */
    private JMenu menuNavigation = new JMenu();
    private JMenuItem jMenuNaviNextOption = new JMenuItem();
    private JMenuItem jMenuNaviPrevOption = new JMenuItem();

    //---------------------------------------------
    private JMenuItem jMenuNaviNextMultiple100MatchOption = new JMenuItem();
    private JMenuItem jMenuNaviPrevMultiple100MatchOption = new JMenuItem();

    //---------------------------------------------
    private JMenuItem jMenuNaviGotoTop = new JMenuItem();
    private JMenuItem jMenuNaviGotoBottom = new JMenuItem();

    //---------------------------------------------
    private JMenuItem jMenuNaviPageUp = new JMenuItem();
    private JMenuItem jMenuNaviPageDown = new JMenuItem();

    //---------------------------------------------
    private JMenuItem jMenuNaviGoto = new JMenuItem();

    /**
     * tools menu
     */
    private JMenu menuTools = new JMenu();
    private JMenuItem jMenuTagVerify = new JMenuItem();
    private JMenuItem jMenuSpellCheck = new JMenuItem();
    private JMenuItem jMenuMaintainMiniTM = new JMenuItem();
    private JMenuItem jMenuMergeMiniTM = new JMenuItem();
    private JMenuItem jMenuUpdateMiniTM = new JMenuItem();
    private JMenuItem jMenuConvert = new JMenuItem();

    /**
     * option menu
     */
    private JMenu optionMenu = new JMenu();
    private JCheckBoxMenuItem jCheckBoxMenuUpdateTags = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuTagProtection = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuSynScrolling = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuWriteProtection = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuTagVerifyIgnoreOrder = new JCheckBoxMenuItem();
    private JCheckBoxMenuItem jCBMenuAutoPropagate = new JCheckBoxMenuItem();
    private JMenuItem jMenuAutoSave = new JMenuItem();
    private JMenuItem jMenuShortcutOption = new JMenuItem();

    /**
     * help menu
     */
    private JMenu menuHelp = new JMenu();
    private JMenuItem jMenuHelpAbout = new JMenuItem();
    private JMenuItem jMenuHelpLicense = new JMenuItem();
    private JMenuItem jMenuHelpIndex = new JMenuItem();

    /**
     * toolbar of this frame
     */
    private JToolBar toolBar = new JToolBar();
    private JButton jBtnOpen = new JButton();
    private JButton jBtnSave = new JButton();

    //JButton jBtnClose = new JButton ();
    private JButton jBtnUndo = new JButton();
    private JButton jBtnRedo = new JButton();
    private JButton jBtnCopy = new JButton();
    private JButton jBtnPaste = new JButton();
    private JButton jBtnCut = new JButton();
    private JButton jBtnSearch = new JButton();
    private String[] strContent = {
        "Segment", "AutoTranslated", "Untranslated", "100% Match", "Fuzzy Match", "User Translation",
        "Translated", "Approved", "Rejected", "Comment"
    };
    private JComboBox jComboBoxSearch = new JComboBox(strContent);
    private JButton jBtnPrev = new JButton();
    private JButton jBtnNext = new JButton();
    private JButton jBtnMarkAsTransAndGoToNextUnTrans = new JButton();
    private JButton jBtnMarkAsApprovedAndGoToNextTrans = new JButton();
    private JButton jBtnTagVerify = new JButton();
    private JButton jBtnSpellCheck = new JButton();
    private JButton jBtnUpdateMiniTM = new JButton();
    private JButton jBtnHelp = new JButton();

    /**
     * the container for this main frame
     */
    private JPanel contentPane;

    /**
     * the center components of this frame
     */
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel primaryPane = new JPanel();
    private JSplitPane MySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    ContrastFrame jContrastFrame = null; // Needed in another class: ecapsulation broken.
    private MiniTMFrame jMiniTMFrame = null;
    private double dDevision = 0.75;
    private double oldRatio = 0.8;

    /**
     * status bar
     */
    JPanel statusBar = new JPanel();

    /**
     * the parameters for ini file;
     *
     * all moved to Configuration
     */

    //private static Vector historyFiles = new Vector();
    //public static IniFile config;
    //public static boolean tagProtection = true;
    // public String strProjName = "";
    //public boolean bFlagSearchBar;
    //public boolean bFlagViewSwitch;
    //public boolean bFlagMatchWindow;
    //public boolean bFlagAttributes;
    //public boolean bFlagSourceContext;
    //public boolean bFlagTagProtection;
    //public static boolean bFlagTagUpdate;
    //public static boolean bFlagSynScrolling;
    //public static boolean bFlagWriteProtection = true;
    //public static boolean bFlagTagVerifyIgnoreOrder = true;
    //public boolean bFlagAutoSave;
    //public int iGlobalInterval;
    //public boolean bFlagTempFile;
    //public static boolean[] bSelectFlags = {true,true,true,true,true};
    //public String strTempFileName;

    /**
     * whether or not auto refresh the matches for one segment if Mini-TM is modified
     */

    //  public static boolean bFlagAutoPropagate = true;
    //  public String strLastFile;
    public boolean bSafeExit;

    /**
     * auto save Timer instance
     */
    protected Timer m_timer;

    /**
     * variables for spellchecker
     */

    //TODO move to separate class
    private Process proc = null;
    private StreamGobbler errorGobbler = null;
    private StreamGobbler outputGobbler = null;

    //StreamWriter outputWriter = null;
    private Process saveTempFileProc = null;
    private ShortcutsBuilder shortcutsBuilder;

    /**
     * one print option dialog instance
     */
    TMPrintOptionDlg printOptionDlg = null;

    /**
     * temporary dialog for printing
     */
    JDialog tmpDlgForPrinting = null;

    /**
     * search/replace dialog instance
     */
    JDialog findDlg = null;

    /**
     * variable for comment dialog
     */
    JCommentDialog commentdlg = null;

    /**
     * varibales for maintain MiniTM Frame
     */
    JFrame maintainFrame = null;
    MiniTMAlignmentMain miniTMAlignment = null;
    JDialog findDlgForMaintain = null;

    /**
     * Merge MiniTM Frame
     */
    private MergeMiniTMPanel mergeDialog = null;

    /**
     * one custom menu's shortcuts dialog instance
     */
    private CustomKeyboard shortcutOptionDlg = null;

    /**
     * Maintain MiniTM Frame
     */
    private JTable maintainTable = null;

    /**
     * Builder for xliff file
     */

    //Moved to backend
    //public static File currentFile = null;
    //public static TMData tmpdata;
    //public static XLIFFParser xp;
    //
    //  New components
    //
    private JMenuItem jmenuReturnToPortal;
    private JButton jBtnReturnToPortal;
    private JMenuItem jmenuApproveFile;
    private JButton jbtnApproveFile;
    private JMenuItem jmenuRejectFile;
    private JButton jbtnRejectFile;
    private JMenuItem jMenuMarkRejectedAndNext;
    private JMenuItem jMenuMarkSegRejected;
    private JMenuItem jMenuMarkExactMatchRejected;
    private JMenuItem jMenuMarkAllSegRejected;
    private JButton jBtnMarkAsRejectedAndGoToNextTrans;

    //  End new components
    //
    //  New flags
    //
    private boolean boolOnline;
    private boolean boolReview;

    //  End new flags
    private JBackConverterSettings memento;

    //License dialog window
    private LicenseDialog licenseDlg;
    private transient PropertyChangeSupport propertyChangeListeners = new PropertyChangeSupport(this);
    private int totalwidth = 800;
    private String col1 = "width=\"10%\"";
    private String col2 = "width=\"10%\"";
    private String col3 = "width=\"80%\"";

    /**
     * Variables to control navigation
     */
    public int iStartRow = -1;
    public boolean bNextDirection = false;
    public boolean bPrevDirection = false;
    Vector suggestion = new Vector();
    private String guid = "";
    private String strEditorHome;
    private JMenuItem jMenuMergeXliff;
    private JMenuItem jMenuSplitXliff;
    private SafeToExitSemaphore semaphore;

    //end of spellchecker

    /**
     * spellchecker functions
     */
    class StreamGobbler extends Thread {
        InputStream is;
        String type;

        StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        public void run() {
            try {
                String targetLan = backend.getProject().getTgtLang();
                InputStreamReader isr = new InputStreamReader(is, Languages.getLanguageENC(targetLan));
                BufferedReader br = new BufferedReader(isr);
                String line = null;

                while ((line = br.readLine()) != null) {
                    if (type.equals("ERROR")) {
                        if (!line.trim().equals("")) {
                            errString = line;

                            //TODO better handling; make the spellchecker a plugin
                            logger.warning(type + ">" + errString);
                            Toolkit.getDefaultToolkit().beep();

                            JOptionPane.showMessageDialog(null, "The dictionary for the " + MainFrame.dictLang + " language is not installed.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    } else if (type.equals("OUTPUT")) {
                        if (!line.trim().equals("") && !line.startsWith("@")) {
                            resultString = line;
                        }
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    class SetTableViewThread extends Thread {
        DocumentUndoableEdit ue = null;
        boolean isUndo = true;

        SetTableViewThread(ViewUndoableEdit ue, boolean isUndo) {
            this.ue = (DocumentUndoableEdit)ue;
            this.isUndo = isUndo;
        }

        public void run() {
            TMData tmpdata = backend.getTMData();

            if (isUndo) {
                int row = ue.alignmentRow;

                if ((ue.getUndoGroupObject() != null) && ue.getPresentationName().startsWith("Transfer")) { // undo "Transfer M:N"

                    List list = (List)ue.getUndoGroupObject();
                    int size = list.size();
                    tmpdata.getGroupTrack().setShownAsGrouped(false);
                    AlignmentMain.testMain2.tableView.setRowSelectionInterval(row, row);
                    AlignmentMain.testMain1.tableView.setRowSelectionInterval(row, row);

                    //refresh the Match
                    AlignmentMain.testMain2.getMatchesContent();
                    jMiniTMFrame.setButtonEnable(true);
                    jMiniTMFrame.setButtonStatus(false);
                } else if ((ue.getUndoGroupObject() != null) && ue.getPresentationName().startsWith("Untransfer")) { // undo "Untransfer"

                    List list = (List)ue.getUndoGroupObject();
                    int size = list.size();
                    tmpdata.getGroupTrack().setShownAsGrouped(true);

                    AlignmentMain.testMain2.tableView.setRowSelectionInterval(row, (row + size) - 1);
                    AlignmentMain.testMain1.tableView.setRowSelectionInterval(row, (row + size) - 1);

                    //refresh the Match
                    AlignmentMain.testMain2.getMatchesContent();
                    jMiniTMFrame.setButtonEnable(false);
                    jMiniTMFrame.setButtonStatus(true);
                } else { // undo other operation
                    AlignmentMain.testMain2.tableView.setRowSelectionInterval(row, row);
                    AlignmentMain.testMain1.tableView.setRowSelectionInterval(row, row);
                }
            } else { //isRedo

                int row = ue.alignmentRow;

                if ((ue.getUndoGroupObject() != null) && ue.getPresentationName().startsWith("Transfer")) { // redo "Transfer M:N"

                    List list = (List)ue.getUndoGroupObject();
                    int size = list.size();
                    tmpdata.getGroupTrack().setShownAsGrouped(true);

                    AlignmentMain.testMain2.tableView.setRowSelectionInterval(row, (row + size) - 1);
                    AlignmentMain.testMain1.tableView.setRowSelectionInterval(row, (row + size) - 1);

                    //refresh the Match
                    AlignmentMain.testMain2.getMatchesContent();
                    jMiniTMFrame.setButtonEnable(false);
                    jMiniTMFrame.setButtonStatus(true);
                } else if ((ue.getUndoGroupObject() != null) && ue.getPresentationName().startsWith("Untransfer")) { // redo "Untransfer"

                    List list = (List)ue.getUndoGroupObject();
                    int size = list.size();
                    tmpdata.getGroupTrack().setShownAsGrouped(false);
                    AlignmentMain.testMain2.tableView.setRowSelectionInterval(row, row);
                    AlignmentMain.testMain1.tableView.setRowSelectionInterval(row, row);

                    //refresh the Match
                    AlignmentMain.testMain2.getMatchesContent();
                    jMiniTMFrame.setButtonEnable(true);
                    jMiniTMFrame.setButtonStatus(false);
                } else {
                    AlignmentMain.testMain2.tableView.setRowSelectionInterval(row, row);
                    AlignmentMain.testMain1.tableView.setRowSelectionInterval(row, row);
                }
            }

            setBHasModified(true);
        }
    }

    class PostHandler implements OpenFileThread.PostXLIFFOpenHandler {
        public boolean acceptProjectLanguage(String longLangName) {
            int choice = JOptionPane.showConfirmDialog(MainFrame.this, "This file does not specify the target language. Would you like to set \n" + "the target language to the project target language (" + longLangName + ") ?", "Missing target language", JOptionPane.OK_CANCEL_OPTION);

            return choice == JOptionPane.OK_OPTION;
        }

        public void languagesMisMatch() {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(MainFrame.this, "The language combination in this file does not match \r\nthe language combination in the selected mini-TM(s).\r\n Please select a different mini-TM.", "Error", JOptionPane.WARNING_MESSAGE);
            MainFrame.this.enableGUI();

            return;
        }

        public void success() {
            //why do we push ? Why doesn't the model listen to us ???
            TMData tmpdata = MainFrame.this.backend.getTMData();

            // bug 4737474
            tmpdata.setAutoPropagate(MainFrame.this.backend.getConfig().isBFlagAutoPropagate());

            try {
                MainFrame.this.strSrcFlag = Languages.getFlagPath(tmpdata.getSourceLanguageCode());
                MainFrame.this.strDestFlag = Languages.getFlagPath(tmpdata.getTargetLanguageCode());
            } catch (Exception e) {
                //ignore;just one of the flags did not get loaded
                logger.warning("While setting flags:" + e);
            }

            //bug 4758110
            String strSrcNation = Languages.getLanguageName(tmpdata.getSourceLanguageCode());
            MainFrame.this.jContrastFrame.setLeftTooltip(strSrcNation);
            MainFrame.this.jContrastFrame.setLeftFlag(strSrcFlag);

            String strDestNation = Languages.getLanguageName(tmpdata.getTargetLanguageCode());
            MainFrame.this.jContrastFrame.setRightTooltip(strDestNation);
            MainFrame.this.jContrastFrame.setRightFlag(strDestFlag);

            MainFrame.this.setTitle(Constants.TOOL_NAME + " - " + MainFrame.this.backend.getProject().getProjectName() + "-" + MainFrame.this.backend.getCurrentFile().getAbsolutePath());

            MainFrame.this.jMenuNaviGoto.setEnabled(true);

            MainFrame.this.openFileInit();
            MainFrame.this.repaintSelf(0);

            MainFrame.this.resetFileHistory();

            MainFrame.this.setBHasModified(tmpdata.isAutoModified());
            MainFrame.this.enableGUI();

            try {
                backend.copyCurrentFileToTemp();
            } catch (IOException ioe) {
                logger.throwing(getClass().getName(), "success", ioe);
            } catch (IllegalArgumentException iae) {
                logger.throwing(getClass().getName(), "success", iae);
            }

            /*
            Thread newThread = new CpFileToTempFile(MainFrame.this,MainFrame.this.backend);
            //run !? you mean start() ?
            newThread.run();
             */
        }

        public void exceptionThrown(Throwable exce) {
            if (exce instanceof java.lang.OutOfMemoryError) {
                JOptionPane.showMessageDialog(MainFrame.this, "The application was not able to allocate enough memory while opening the XLIFF file." + "\n Please consult the user manual on how to increase the amount of available application memory.", "Not Enough Memory", JOptionPane.OK_OPTION);

                return;
            }

            exce.printStackTrace();

            if (exce.getMessage() != null) {
                Toolkit.getDefaultToolkit().beep();

                if (exce.getMessage().equals("LanguageError")) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Invalid .xlz file - language(s) error", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (exce.getMessage().equals("xlzFileInvalid")) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Invalid .xlz file.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (exce.getMessage().indexOf("Open Language Tools Exception") != -1) {
                    JOptionPane.showMessageDialog(MainFrame.this, "There is a fatal error when opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "Open failed, please check the file which you opened.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(MainFrame.this, "An unknown error occured while opening this file.\n" + "Please copy the text in editor console and send it to\n" + "dev@open-language-tools.dev.java.net along with a copy\n" + "of the source file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void buildMethodFailed() {
            //do nothing
        }

        public void done() {
            MainFrame.this.enableGUI();
        }
    }

    /**
     * Constructor
     */
    public MainFrame(Backend backend) {
        this.backend = backend;
    }

    void run() {
        try {
            /**
             * construct the GUI
             */
            jbInit();
            initGUI();

            /**
             * load license, show dialog
             * if user does not agree, end
             *
             * If anything goes wrong with loading the license etc
             * we end. No license && agreement => ! fun
             */
            try {
                String license = loadLicense();

                licenseDlg = new LicenseDialog(this, license, true);

                boolean didAgree = backend.getConfig().isDidAgreeToLicense();

                if (!didAgree) {
                    licenseDlg.setVisible(true);

                    didAgree = licenseDlg.didAgree();

                    if (!didAgree) {
                        System.exit(3);
                    }

                    backend.getConfig().setDidAgreeToLicense(didAgree);
                }
            } catch (Exception e) {
                logger.throwing(getClass().getName(), "MainFrame", e);
                logger.severe("While loading license:" + e);

                //TOOD refactor NOT to be in constructor
                JOptionPane.showMessageDialog(this, "The license file could not be found. The application will end now", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(2);
            }

            /**
             * input translator id
             */
            String translatorId = backend.getConfig().getTranslatorID();
            InputTranslatorIDDlg dlg = new InputTranslatorIDDlg(this, translatorId);
            dlg.setVisible(true);
            backend.getConfig().setTranslatorID(dlg.getTranslatorId());
            backend.getConfig().save();

            /**
             * initial the application
             */

            //TODO refactor Transproject or introduce a project factory; get rid of project history
            List l = backend.getConfig().getProjectHistory();

            if (!l.isEmpty()) {
                String project = (String)l.get(0);

                String[] parts = project.split("_");

                if ((parts != null) && (parts.length == 3)) {
                    TransProject tp = new TransProject(parts[0], parts[1], parts[2], backend.getConfig().getMiniTMDir().getAbsolutePath(), "");
                    backend.openProject(tp);
                }
            }

            initApplication();
        } catch (Exception ex) {
            logger.throwing(getClass().getName(), "MainFrame", ex);
            logger.severe("Exception:" + ex);

            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "There is a fatal error during initialization ", "Warning", JOptionPane.WARNING_MESSAGE);
            System.exit(-1);
        }
    }

    public static MainFrame getAnInstance() {
        return rootFrame;
    }

    /**
     * initialize the member variables,such as
     */
    private void initApplication() throws Exception {
        rootFrame = this;
        curTable = AlignmentMain.testMain2.tableView;
        bSafeExit = false;
        this.setBHasModified(false);

        semaphore = new SafeToExitSemaphore();

        shortcutsBuilder = new ShortcutsBuilder(backend.getConfig().getShortcuts(), menuBar);
        shortcutsBuilder.parseMenu();

        //CustomKeyboard.init();
        undo = new PivotUndoManager(this, backend);
        initTempPrintDlg();
        spellDlg = new SpellCheckerDIALOG(this, backend);
        spellDlg.setLocationRelativeTo(this);
        reloadLastTempFile();
        initTimer();
    }

    private void reloadLastTempFile() {
        //  Made a change here to only implement the restore from autosave if the
        //  autosave function is turned on. This is part of the fix for bug 5038000
        Configuration cfg = backend.getConfig();
        String tempName = cfg.getStrTempFileName();

        if (cfg.isBFlagAutoSave() && backend.canRevertFromTempFile()) {
            //                tempName != null &&  tempName.trim().length() != 0){
            int iRet = JOptionPane.showConfirmDialog(this, "The file was not saved the last time the editor was closed, would you like to open its autosaved backup file.", "Autosave", JOptionPane.YES_NO_OPTION);

            if (iRet == JOptionPane.YES_OPTION) {
                List l = backend.getConfig().getFilesHistory();
                String strTemp = (String)l.get(0);

                File fsrc = new File(tempName);
                File fdest = new File(strTemp);

                try {
                    FileUtils.copyFileNoEncode(fsrc, fdest);
                    openFile(fdest.getAbsolutePath());
                } catch (IOException ioe) {
                    JOptionPane.showConfirmDialog(this, "Copying of the backup file failed. The file will not be opened.", "Backup copy failed", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * construct tmpDlgForPrinting
     */
    private void initTempPrintDlg() {
        tmpDlgForPrinting = new JDialog(this, "Printing...", false);
        tmpDlgForPrinting.getContentPane().setLayout(new BorderLayout());

        String msg = "The .xlf file is being exported to HTML and will be opened automatically for you in a browser window. \nYou can use the print option in your browser to print the file.";

        JTextPane textArea = new JTextPane();
        textArea.setSize(380, 120);
        textArea.setFont(dlgFont);
        textArea.setBackground(new Color(204, 204, 204));
        textArea.setEditable(false);
        textArea.setText(msg);

        JScrollPane scr = new JScrollPane(textArea);
        scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scr.setBorder(null);

        JPanel p = new JPanel();
        JButton b = new JButton("Ok");
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tmpDlgForPrinting.setVisible(false);
                    print();
                }
            });
        p.add(b);

        tmpDlgForPrinting.getContentPane().add(scr, BorderLayout.CENTER);
        tmpDlgForPrinting.getContentPane().add(p, BorderLayout.SOUTH);
        tmpDlgForPrinting.setSize(400, 160);
        tmpDlgForPrinting.setResizable(false);
        tmpDlgForPrinting.validate();
    }

    /**
     * init the GUI of editor's main frame
     */
    private void initGUI() {
        /**
         * All other menu
         */
        enableMenus(false);

        /**
         * File Menu
         */
        initFileHistoryMenu();

        /**
         * ToolBar and View Menu
         */
        Configuration cfg = backend.getConfig();

        jCBMenuFile.setSelected(cfg.getBSelectFlag(0));
        jCBMenuEditing.setSelected(cfg.getBSelectFlag(1));
        jCBMenuSearch.setSelected(cfg.getBSelectFlag(2));
        jCBMenuNavi.setSelected(cfg.getBSelectFlag(3));
        jCBMenuHelp.setSelected(cfg.getBSelectFlag(4));
        AdjustTooBar();

        jMenuNaviGoto.setEnabled(!backend.hasCurrentFile());

        /**
         * View menu
         */
        jCBMenuMatchWindow.setSelected(cfg.isBFlagMatchWindow());
        jCBMenuAttributes.setSelected(cfg.isBFlagAttributes());
        jCBMenuAttributes.setSelected(cfg.isBFlagSourceContext());
        jCBMenuSearchBar.setSelected(backend.getConfig().isBFlagSearchBar());

        if (!cfg.isBFlagMatchWindow()) {
            dDevision = 1.0;
            MySplitPane.setDividerLocation(dDevision);
            MySplitPane.setDividerSize(0);
            MySplitPane.setBottomComponent(null);
            jCBMenuAttributes.setEnabled(false);
            this.getRootPane().repaint();
        }

        if (!cfg.isBFlagAttributes()) {
            jCBMenuAttributes.setSelected(false);
            jMiniTMFrame.setAttributePanelVisible(false);
        }

        if (!cfg.isBFlagSourceContext()) {
            jCBMenuSourceContext.setSelected(false);
            jSourceContextFrame.setVisible(false);
        }

        if (!cfg.isBFlagSearchBar()) {
            jContrastFrame.tmLeftPanel.remove(jContrastFrame.tmLeftPanel.jToolBarPanel);
            jContrastFrame.tmRightPanel.remove(jContrastFrame.tmRightPanel.jToolBarPanel);
            this.validate();
            this.repaint();
        }

        if (cfg.isBFlagViewSwitch()) {
            jMenuVer.setEnabled(true);
            jMenuHor.setEnabled(false);
        } else {
            jMenuVer.setEnabled(false);
            jMenuHor.setEnabled(true);
        }

        /**
         * Option Menu
         */
        jCheckBoxMenuUpdateTags.setSelected(cfg.isBFlagTagUpdate());
        jCBMenuSynScrolling.setSelected(cfg.isBFlagSynScrolling());
        jCBMenuWriteProtection.setSelected(cfg.isBFlagWriteProtection());
        jCBMenuTagVerifyIgnoreOrder.setSelected(cfg.isBFlagTagVerifyIgnoreOrder());
        jCBMenuAutoPropagate.setSelected(cfg.isBFlagAutoPropagate());

        // refresh the window size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);
    }

    private void initFileHistoryMenu() {
        setFileHistory(menuFile);
    }

    private void initTimer() { //  Autosave function timer

        ActionListener Timer_lst = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disableGUI();
                m_timer.stop();

                backend.saveFileToTemp();

                m_timer.restart();
                enableGUI();
            }
        };

        m_timer = new Timer(backend.getConfig().getIGlobalInterval() * 60 * 1000, Timer_lst);

        if (backend.getConfig().isBFlagAutoSave()) {
            m_timer.start();
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("bHasModified")) {
            this.jMenuSave.setEnabled(bHasModified);
            this.jBtnSave.setEnabled(bHasModified);
        }

        if (e.getPropertyName().equals("undoRedo")) {
            String undoName = undo.getUndoName();
            String redoName = undo.getRedoName();

            if (undoName == null) {
                jMenuUndo.setEnabled(false);
                jMenuUndo.setText("Undo");
                jBtnUndo.setEnabled(false);
                jBtnUndo.setToolTipText("Undo");
            } else {
                jMenuUndo.setEnabled(true);
                jMenuUndo.setText(undoName);
                jBtnUndo.setEnabled(true);
                jBtnUndo.setToolTipText(undoName);
            }

            if (redoName == null) {
                jMenuRedo.setEnabled(false);
                jMenuRedo.setText("Redo");
                jBtnRedo.setEnabled(false);
                jBtnRedo.setToolTipText("Redo");
            } else {
                jMenuRedo.setEnabled(true);
                jMenuRedo.setText(redoName);
                jBtnRedo.setEnabled(true);
                jBtnRedo.setToolTipText(redoName);
            }
        }
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        super.removePropertyChangeListener(l);
        propertyChangeListeners.removePropertyChangeListener(l);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        super.addPropertyChangeListener(l);
        propertyChangeListeners.addPropertyChangeListener(l);
    }

    public void setBHasModified(boolean bHasModified) {
        boolean oldBHasModified = this.bHasModified;
        this.bHasModified = bHasModified;
        propertyChangeListeners.firePropertyChange("bHasModified", new Boolean(oldBHasModified), new Boolean(bHasModified));
    }

    public boolean isBHasModified() {
        return bHasModified;
    }

    //Component initialization
    private void jbInit() throws Exception {
        NullMouseListener nullMouseListener = new NullMouseListener();
        Component glassPane = this.getGlassPane();
        glassPane.addMouseListener(nullMouseListener);
        glassPane.addMouseMotionListener(nullMouseListener);

        backend.addProjectPropertyChangedListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (Backend.PROPERTY_PROJECT.equals(evt.getPropertyName())) {
                        TransProject newProj = (TransProject)evt.getNewValue();
                        TransProject oldProj = (TransProject)evt.getNewValue();

                        MainFrame.this.setTitle(Constants.TOOL_NAME + ((newProj == null) ? "" : (" - " + newProj.getProjectName())));

                        if (newProj != null) {
                            String x = newProj.getSrcLang();
                            String y = Languages.getFlagPathForLan(Languages.getLanguageName(x));

                            MainFrame.this.jContrastFrame.setLeftFlag(y);

                            x = newProj.getTgtLang();
                            y = Languages.getFlagPathForLan(Languages.getLanguageName(x));
                            MainFrame.this.jContrastFrame.setRightFlag(y);
                        }
                    }
                }
            });

        //  Create a few straggler objects
        jBtnMarkAsRejectedAndGoToNextTrans = new JButton();
        jBtnMarkAsApprovedAndGoToNextTrans = new JButton();

        jMenuMarkSegRejected = new JMenuItem();
        jMenuMarkExactMatchRejected = new JMenuItem();
        jMenuMarkAllSegRejected = new JMenuItem();
        jMenuMarkRejectedAndNext = new JMenuItem();

        try {
            ip = ImagePackage.instance();
        } catch (Throwable t) {
            logger.throwing(getClass().getName(), "jbInit", t);
            logger.severe("some of the images did not load.Application will terminate");
            System.exit(0);
        }

        jBtnOpen.setIcon(ip.imageOpen);
        jBtnSave.setIcon(ip.imageSave);
        jBtnUndo.setIcon(ip.imageUndo);
        jBtnRedo.setIcon(ip.imageRedo);
        jBtnCut.setIcon(ip.imageCut);
        jBtnCopy.setIcon(ip.imageCopy);
        jBtnPaste.setIcon(ip.imagePaste);
        jBtnSearch.setIcon(ip.imageSearch);
        jBtnPrev.setIcon(ip.imagePrev);
        jBtnNext.setIcon(ip.imageNext);
        jBtnMarkAsTransAndGoToNextUnTrans.setIcon(ip.imageMTAGNU);
        jBtnMarkAsApprovedAndGoToNextTrans.setIcon(ip.imageMVAGNT);
        jBtnUpdateMiniTM.setIcon(ip.imageUpdate);
        jBtnTagVerify.setIcon(ip.imageTagVerify);
        jBtnSpellCheck.setIcon(ip.imageSpellCheck);
        jBtnMarkAsApprovedAndGoToNextTrans.setIcon(ip.imageApproveSegment);
        jBtnMarkAsRejectedAndGoToNextTrans.setIcon(ip.imageRejectSegment);

        if (boolOnline) {
            if (boolReview) {
                jbtnApproveFile.setIcon(ip.imageApproveFile);
                jbtnRejectFile.setIcon(ip.imageRejectFile);
            } else {
                jBtnReturnToPortal.setIcon(ip.imageUpload);
            }
        }

        jBtnHelp.setIcon(ip.imageHelp);

        jBtnOpen.setPreferredSize(new Dimension(25, 23));
        jBtnOpen.setToolTipText("Open File");
        jBtnOpen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnOpen_actionPerformed(e);
                }
            });
        jBtnSave.setPreferredSize(new Dimension(25, 25));
        jBtnSave.setToolTipText("Save File");
        jBtnSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnSave_actionPerformed(e);
                }
            });
        jBtnUndo.setToolTipText("Undo");
        jBtnUndo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnUndo_actionPerformed(e);
                }
            });
        jBtnRedo.setToolTipText("Redo");
        jBtnRedo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnRedo_actionPerformed(e);
                }
            });
        jBtnCut.setToolTipText("Cut");
        jBtnCut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnCut_actionPerformed(e);
                }
            });
        jBtnCopy.setToolTipText("Copy");
        jBtnCopy.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnCopy_actionPerformed(e);
                }
            });
        jBtnPaste.setToolTipText("Paste");
        jBtnPaste.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnPaste_actionPerformed(e);
                }
            });
        jBtnSearch.setToolTipText("Search");
        jBtnSearch.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnSearch_actionPerformed(e);
                }
            });
        jComboBoxSearch.setMaximumSize(new Dimension(180, 31));
        jComboBoxSearch.setMinimumSize(new Dimension(180, 31));
        jComboBoxSearch.setPreferredSize(new Dimension(180, 31));
        jComboBoxSearch.setToolTipText("Navigate");
        jComboBoxSearch.addItemListener(this);
        jComboBoxSearch.setSelectedIndex(0);

        jBtnNext.setToolTipText("Next Segment");
        jBtnNext.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnNext_actionPerformed(e);
                }
            });

        jBtnPrev.setToolTipText("Previous Segment");
        jBtnPrev.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnPrev_actionPerformed(e);
                }
            });
        jBtnMarkAsTransAndGoToNextUnTrans.setToolTipText("Confirm and Translate Next");
        jBtnMarkAsTransAndGoToNextUnTrans.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnMarkAsTransAndGoToNextUnTrans_actionPerformed(e);
                }
            });

        //  Changes for the Review Process
        jBtnMarkAsApprovedAndGoToNextTrans.setToolTipText("Approve and go to Next Translated Segment");

        ActionListener commandApproveAndGotoNext = new ApproveSegmentAndNextCommand(this);
        jBtnMarkAsApprovedAndGoToNextTrans.addActionListener(commandApproveAndGotoNext);

        jBtnMarkAsRejectedAndGoToNextTrans.setToolTipText("Reject and go to Next Translated Segment");

        ActionListener commandRejectAndGotoNext = new RejectSegmentAndNextCommand(this);
        jBtnMarkAsRejectedAndGoToNextTrans.addActionListener(commandRejectAndGotoNext);

        //  End review changes.
        jBtnUpdateMiniTM.setToolTipText("Update Mini-TM");
        jBtnUpdateMiniTM.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnUpdateMiniTM_actionPerformed(e);
                }
            });
        jBtnTagVerify.setToolTipText("Tag Verification");
        jBtnTagVerify.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stopEditing();
                    jBtnTagVerify_actionPerformed(e);
                }
            });
        jBtnSpellCheck.setToolTipText("Spell Check");
        jBtnSpellCheck.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnSpellCheck_actionPerformed(e);
                }
            });
        jBtnHelp.setPreferredSize(new Dimension(25, 25));
        jBtnHelp.setToolTipText("Help");
        jBtnHelp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnHelp_actionPerformed(e);
                }
            });

        /**
         * File Menu
         */
        menuFile.setText("File");
        menuFile.setMnemonic('F');

        jMenuNewProject.setText("New Project...");
        jMenuNewProject.setMnemonic('N');
        jMenuNewProject.setToolTipText("Create a new project");
        jMenuNewProject.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNewProject_actionPerformed(e);
                }
            });
        jMenuOpenProject.setText("Open Project...");
        jMenuOpenProject.setMnemonic('J');
        jMenuOpenProject.setToolTipText("Open a project");
        jMenuOpenProject.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuOpenProject_actionPerformed(e);
                }
            });
        jMenuOpen.setToolTipText("Open a .xlz or .xlf file...");
        jMenuOpen.setMnemonic('O');
        jMenuOpen.setText("Open");
        jMenuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        jMenuOpen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuOpen_actionPerformed(e);
                }
            });
        jMenuSave.setToolTipText("Save the current file...");
        jMenuSave.setMnemonic('S');
        jMenuSave.setText("Save");
        jMenuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        jMenuSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuSave_actionPerformed(e);
                }
            });
        jMenuSaveAs.setToolTipText("Save as another file...");
        jMenuSaveAs.setMnemonic('A');
        jMenuSaveAs.setText("Save As...");
        jMenuSaveAs.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuSaveAs_actionPerformed(e);
                }
            });
        jMenuSaveMiniTM.setToolTipText("Save the current MiniTM file...");
        jMenuSaveMiniTM.setText("Save Mini-TM");
        jMenuSaveMiniTM.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuSaveMiniTM_actionPerformed(e);
                }
            });
        jMenuClose.setToolTipText("Close the current file");
        jMenuClose.setMnemonic('C');
        jMenuClose.setText("Close");
        jMenuClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuClose_actionPerformed(e);
                }
            });
        jMenuPrintOptions.setToolTipText("Define format of print-out");
        jMenuPrintOptions.setMnemonic('r');
        jMenuPrintOptions.setText("Print Options...");
        jMenuPrintOptions.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuPrintOptions_actionPerformed(e);
                }
            });
        jMenuPrint.setToolTipText("Print the current file");
        jMenuPrint.setText("Print...");
        jMenuPrint.setMnemonic('P');
        jMenuPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
        jMenuPrint.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        jMenuPrint_actionPerformed(e);
                    } catch (Exception ex) {
                    }
                }
            });
        jMenuExit.setToolTipText("Exit the editor");
        jMenuExit.setMnemonic('X');
        jMenuExit.setText("Exit");
        jMenuExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fileExit_actionPerformed(e);
                }
            });

        /**
         * Edit Menu
         */
        menuEdit.setMnemonic('E');
        menuEdit.setText("Edit");
        jMenuUndo.setMnemonic('U');
        jMenuUndo.setText("Undo");
        jMenuUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
        jMenuUndo.setToolTipText("Undo the last operation");
        jMenuUndo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuUndo_actionPerformed(e);
                }
            });
        jMenuRedo.setMnemonic('R');
        jMenuRedo.setText("Redo");
        jMenuRedo.setToolTipText("Redo the last operation");
        jMenuRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
        jMenuRedo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuRedo_actionPerformed(e);
                }
            });
        jMenuCut.setMnemonic('T');
        jMenuCut.setText("Cut");
        jMenuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        jMenuCut.setToolTipText("Cut");
        jMenuCut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuCut_actionPerformed(e);
                }
            });
        jMenuCopy.setMnemonic('C');
        jMenuCopy.setText("Copy");
        jMenuCopy.setToolTipText("Copy");
        jMenuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        jMenuCopy.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuCopy_actionPerformed(e);
                }
            });
        jMenuPaste.setMnemonic('P');
        jMenuPaste.setText("Paste");
        jMenuPaste.setToolTipText("Paste");
        jMenuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
        jMenuPaste.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuPaste_actionPerformed(e);
                }
            });
        jMenuTransfer.setToolTipText("Transfer selected match to target window");
        jMenuTransfer.setText("Transfer");
        jMenuTransfer.setMnemonic('F');
        jMenuTransfer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
        jMenuTransfer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuTransfer_actionPerformed(e);
                }
            });
        jMenuUntransfer.setMnemonic('S');
        jMenuUntransfer.setText("Untransfer");
        jMenuUntransfer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuUntransfer_actionPerformed(e);
                }
            });

        jMenuCopySource.setToolTipText("Copy source segment to target segment");
        jMenuCopySource.setText("Copy Source");
        jMenuCopySource.setMnemonic('O');
        jMenuCopySource.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, KeyEvent.ALT_MASK));
        jMenuCopySource.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuCopySource_actionPerformed(e);
                }
            });
        jMenuCopySourceTag.setToolTipText("Copy source tags to target segment");
        jMenuCopySourceTag.setText("Copy Source Tags");
        jMenuCopySourceTag.setMnemonic('G');
        jMenuCopySourceTag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.ALT_MASK));
        jMenuCopySourceTag.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuCopySourceTags_actionPerformed(e);
                }
            });
        jMenuClearTarget.setToolTipText("Delete target segment text");
        jMenuClearTarget.setText("Clear Target");
        jMenuClearTarget.setMnemonic('L');
        jMenuClearTarget.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.ALT_MASK));
        jMenuClearTarget.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuClearTarget_actionPerformed(e);
                }
            });

        jMenuMarkSeg.setText("Mark Segment As");
        jMenuMarkSeg.setMnemonic('M');

        jMenuMarkSegTrans.setToolTipText("Mark current segment as translated");
        jMenuMarkSegTrans.setEnabled(false);
        jMenuMarkSegTrans.setText("Translated");
        jMenuMarkSegTrans.setMnemonic('T');
        jMenuMarkSegTrans.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.ALT_MASK));
        jMenuMarkSegTrans.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuMarkSegTrans_actionPerformed(e);
                }
            });
        jMenuMarkSegUnTrans.setToolTipText("Mark current segment as untranslated");
        jMenuMarkSegUnTrans.setText("Untranslated");
        jMenuMarkSegUnTrans.setMnemonic('U');
        jMenuMarkSegUnTrans.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        jMenuMarkSegUnTrans.setEnabled(false);
        jMenuMarkSegUnTrans.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuMarkSegUnTrans_actionPerformed(e);
                }
            });

        //  Changes for the review process
        jMenuMarkSegReviewed.setToolTipText("Mark current segment as approved");
        jMenuMarkSegReviewed.setText("Approved");
        jMenuMarkSegReviewed.setMnemonic('A');
        jMenuMarkSegReviewed.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        jMenuMarkSegReviewed.setEnabled(false);
        jMenuMarkSegReviewed.addActionListener(new ApproveSegmentCommand(this));

        jMenuMarkSegRejected.setToolTipText("Mark current segment as rejected");
        jMenuMarkSegRejected.setText("Rejected");
        jMenuMarkSegRejected.setMnemonic('A');
        jMenuMarkSegRejected.setEnabled(false);
        jMenuMarkSegRejected.addActionListener(new RejectSegmentCommand(this));

        //  End review process changes
        jMenuMarkExactMatch.setText("Mark All 100% Matches As");
        jMenuMarkExactMatch.setMnemonic('A');

        jMenuMarkExactMatchTrans.setToolTipText("Mark all 100% matches as translated");
        jMenuMarkExactMatchTrans.setText("Translated");
        jMenuMarkExactMatchTrans.setMnemonic('T');
        jMenuMarkExactMatchTrans.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuMarkExactMatchTrans_actionPerformed(e);
                }
            });

        //  Changes for the review process
        jMenuMarkExactMatchReviewed.setToolTipText("Mark all 100% matches as approved");
        jMenuMarkExactMatchReviewed.setText("Approved");
        jMenuMarkExactMatchReviewed.setMnemonic('A');
        jMenuMarkExactMatchReviewed.addActionListener(new ApproveAllExactMatchSegmentsCommand(this));

        jMenuMarkExactMatchRejected.setToolTipText("Mark all 100% matches as rejected");
        jMenuMarkExactMatchRejected.setText("Rejected");
        jMenuMarkExactMatchRejected.setMnemonic('R');
        jMenuMarkExactMatchRejected.addActionListener(new RejectAllExactMatchSegmentsCommand(this));

        //  End changes for the review process
        jMenuMarkAllSeg.setText("Mark All Segments As");
        jMenuMarkAllSeg.setMnemonic('K');

        jMenuMarkAllSegTrans.setToolTipText("Mark all segments as translated");
        jMenuMarkAllSegTrans.setText("Translated");
        jMenuMarkAllSegTrans.setMnemonic('T');
        jMenuMarkAllSegTrans.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuMarkAllSegTrans_actionPerformed(e);
                }
            });

        //  Changes for the new review process
        jMenuMarkAllSegReviewed.setToolTipText("Mark all segments as approved");
        jMenuMarkAllSegReviewed.setText("Approved");
        jMenuMarkAllSegReviewed.setMnemonic('A');
        jMenuMarkAllSegReviewed.addActionListener(new ApproveAllTranslatedSegmentsCommand(this));

        jMenuMarkAllSegRejected.setToolTipText("Mark all segments as rejected");
        jMenuMarkAllSegRejected.setText("Rejected");
        jMenuMarkAllSegRejected.setMnemonic('R');
        jMenuMarkAllSegRejected.addActionListener(new RejectAllTranslatedSegmentsCommand(this));

        //  End the changes for the new rview process
        jMenuMarkTranslatedAndNext.setText("Confirm and Translate Next");
        jMenuMarkTranslatedAndNext.setMnemonic('N');
        jMenuMarkTranslatedAndNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.ALT_MASK));
        jMenuMarkTranslatedAndNext.setToolTipText("Mark current segment as translated and go to next");
        jMenuMarkTranslatedAndNext.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuMarkTranslatedAndNext_actionPerformed(e);
                }
            });
        jMenuMarkReviewedAndNext.setText("Approve and go to Next Translated");
        jMenuMarkReviewedAndNext.setToolTipText("Mark current segment as approved and go to next");
        jMenuMarkReviewedAndNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, KeyEvent.CTRL_MASK));
        jMenuMarkReviewedAndNext.addActionListener(commandApproveAndGotoNext);

        jMenuMarkRejectedAndNext.setText("Reject and go to Next Translated");
        jMenuMarkRejectedAndNext.setToolTipText("Mark current segment as rejected and go to next");
        jMenuMarkRejectedAndNext.addActionListener(commandRejectAndGotoNext);

        jMenuSegComment.setToolTipText("Comment on Segment");
        jMenuSegComment.setText("Comment on Segment");

        jMenuFileComment.setText("Comment on File");
        jMenuFileComment.setToolTipText("File level comment");
        jMenuFileComment.setMnemonic('I');
        jMenuFileComment.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuFileComment_actionPerformed(e);
                }
            });
        jMenuAddComment.setToolTipText("Add Comment");
        jMenuAddComment.setMnemonic('A');
        jMenuAddComment.setText("Add Comment");
        jMenuAddComment.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuAddComment_actionPerformed(e);
                }
            });
        jMenuEditComment.setToolTipText("Edit Comment");
        jMenuEditComment.setMnemonic('E');
        jMenuEditComment.setText("Edit Comment");
        jMenuEditComment.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuEditComment_actionPerformed(e);
                }
            });
        jMenuDeleteComment.setToolTipText("Delete Comment");
        jMenuDeleteComment.setMnemonic('D');
        jMenuDeleteComment.setText("Delete Comment");
        jMenuDeleteComment.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuDeleteComment_actionPerformed(e);
                }
            });
        jMenuSearch.setText("Search/Replace");
        jMenuSearch.setMnemonic('E');
        jMenuSearch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        jMenuSearch.setToolTipText("Search for and replace text");
        jMenuSearch.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuSearch_actionPerformed(e);
                }
            });

        /**
         * Veiw Menu
         */
        menuView.setMnemonic('V');
        menuView.setText("View");

        jMenuShowOption.setText("Toolbar");
        jMenuShowOption.setMnemonic('T');
        jCBMenuFile.setToolTipText("Show/hide file functions in toolbar");
        jCBMenuFile.setText("File");
        jCBMenuFile.setMnemonic('F');
        jCBMenuFile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuFile_actionPerformed(e);
                }
            });
        jCBMenuEditing.setToolTipText("Show/hide edit functions in toolbar");
        jCBMenuEditing.setText("Edit");
        jCBMenuEditing.setMnemonic('E');
        jCBMenuEditing.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuEditing_actionPerformed(e);
                }
            });
        jCBMenuSearch.setToolTipText("Show/hide navigation functions in toolbar");
        jCBMenuSearch.setText("Navigation");
        jCBMenuSearch.setMnemonic('N');
        jCBMenuSearch.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuSearch_actionPerformed(e);
                }
            });
        jCBMenuNavi.setToolTipText("Show/hide tool functions in toolbar");
        jCBMenuNavi.setText("Tools");
        jCBMenuNavi.setMnemonic('T');
        jCBMenuNavi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuNavi_actionPerformed(e);
                }
            });
        jCBMenuHelp.setToolTipText("Show/hide help function in toolbar");
        jCBMenuHelp.setText("Help");
        jCBMenuHelp.setMnemonic('P');
        jCBMenuHelp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuHelp_actionPerformed(e);
                }
            });
        jMenuItemShowAll.setToolTipText("Show all functions in toolbar");
        jMenuItemShowAll.setText("Show All");
        jMenuItemShowAll.setMnemonic('S');
        jMenuItemShowAll.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuItemShowAll_actionPerformed(e);
                }
            });

        jMenuItemHideAll.setToolTipText("Hide all functions in toolbar");
        jMenuItemHideAll.setText("Hide All");
        jMenuItemHideAll.setMnemonic('H');
        jMenuItemHideAll.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuItemHideAll_actionPerformed(e);
                }
            });
        jCBMenuSearchBar.setToolTipText("Show/hide search bar");
        jCBMenuSearchBar.setText("Search Bar");
        jCBMenuSearchBar.setMnemonic('S');
        jCBMenuSearchBar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuSearchBar_actionPerformed(e);
                }
            });
        jCBMenuMatchWindow.setToolTipText("Show/hide match window");
        jCBMenuMatchWindow.setText("Match Window");
        jCBMenuMatchWindow.setMnemonic('M');
        jCBMenuMatchWindow.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuMatchWindow_actionPerformed(e);
                }
            });
        jCBMenuAttributes.setToolTipText("Show/Hide TM attributes information");
        jCBMenuAttributes.setText("Attributes");
        jCBMenuAttributes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuAttributes_actionPerformed(e);
                }
            });

        jCBMenuSourceContext.setToolTipText("Show/Hide Source context information");
        jCBMenuSourceContext.setText("Source Context");
        jCBMenuSourceContext.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuSourceContext_actionPerformed(e);
                }
            });

        jMenuColor.setToolTipText("Set the colors used in the editor windows");
        jMenuColor.setActionCommand("Color...");
        jMenuColor.setText("Color...");
        jMenuColor.setMnemonic('C');
        jMenuColor.setEnabled(false);
        jMenuColor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuColor_actionPerformed(e);
                }
            });
        jMenuFonts.setToolTipText("Set the font used in the editor windows");
        jMenuFonts.setActionCommand("SetFonts");
        jMenuFonts.setText("Fonts...");
        jMenuFonts.setMnemonic('F');
        jMenuFonts.setEnabled(true);
        jMenuFonts.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectNewComponentFontSize();
                }
            });
        jCBMenuSHTag.setEnabled(false);
        jCBMenuSHTag.setToolTipText("Show/hide tags in editor windows");
        jCBMenuSHTag.setText("Tags");
        jCBMenuAbbrTagsDisplay.setEnabled(false);
        jCBMenuAbbrTagsDisplay.setToolTipText("Switch on/off abbreviated tags");
        jCBMenuAbbrTagsDisplay.setText("Abbreviated Tags");
        jCBMenuTagProtection.setToolTipText("Switch on/off tag protection");
        jCBMenuTagProtection.setText("Tag Protection");
        jCBMenuTagProtection.setMnemonic('T');
        jCBMenuTagProtection.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.ALT_MASK));
        jCBMenuTagProtection.setSelected(backend.getConfig().isTagProtection());
        jCBMenuTagProtection.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuTagProtection_actionPerformed(e);
                }
            });
        jMenuHOrV.setText("Window Arrangment");
        jMenuHOrV.setMnemonic('W');

        jMenuHor.setText("Side by side");
        jMenuHor.setMnemonic('S');
        jMenuHor.setToolTipText("Arrange windows veritically");
        jMenuHor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuHor_actionPerformed(e);
                }
            });
        jMenuVer.setToolTipText("Arrange windows horizontally");
        jMenuVer.setText("Over/under");
        jMenuVer.setMnemonic('O');
        jMenuVer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuVer_actionPerformed(e);
                }
            });

        /**
         * Navigation Menu
         */
        menuNavigation.setMnemonic('N');
        menuNavigation.setText("Navigation");

        jMenuNaviNextOption.setToolTipText("Go to next segment of specified type");
        jMenuNaviNextOption.setText("Next Segment");
        jMenuNaviNextOption.setMnemonic('N');
        jMenuNaviNextOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK));
        jMenuNaviNextOption.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviNextOption_actionPerformed(e);
                }
            });

        jMenuNaviPrevOption.setToolTipText("Go to previous segment of specified type");
        jMenuNaviPrevOption.setText("Previous Segment");
        jMenuNaviPrevOption.setMnemonic('P');
        jMenuNaviPrevOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK));
        jMenuNaviPrevOption.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviPrevOption_actionPerformed(e);
                }
            });

        jMenuNaviNextMultiple100MatchOption.setToolTipText("Navigate to the next multiple 100% segment");
        jMenuNaviNextMultiple100MatchOption.setText("Next Multiple 100% Match");
        jMenuNaviNextMultiple100MatchOption.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviNextMultiple100MatchOption_actionPerformed(e);
                }
            });

        jMenuNaviPrevMultiple100MatchOption.setToolTipText("Navigate to the previous multiple 100% segment");
        jMenuNaviPrevMultiple100MatchOption.setText("Previous Multiple 100% Match");
        jMenuNaviPrevMultiple100MatchOption.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviPrevMultiple100MatchOption_actionPerformed(e);
                }
            });

        jMenuNaviGotoTop.setToolTipText("Navigate to the top of the current file");
        jMenuNaviGotoTop.setText("Go To Top");
        jMenuNaviGotoTop.setMnemonic('T');
        jMenuNaviGotoTop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, KeyEvent.CTRL_MASK));
        jMenuNaviGotoTop.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviGotoTop_actionPerformed(e);
                }
            });

        jMenuNaviGotoBottom.setToolTipText("Navigate to the bottom of the current file");
        jMenuNaviGotoBottom.setText("Go To Bottom");
        jMenuNaviGotoBottom.setMnemonic('B');
        jMenuNaviGotoBottom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END, KeyEvent.CTRL_MASK));
        jMenuNaviGotoBottom.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviGotoBottom_actionPerformed(e);
                }
            });

        jMenuNaviPageUp.setToolTipText("Scroll to top of page");
        jMenuNaviPageUp.setText("Page Up");
        jMenuNaviPageUp.setMnemonic('U');
        jMenuNaviPageUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, KeyEvent.CTRL_MASK));
        jMenuNaviPageUp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviPageUp_actionPerformed(e);
                }
            });

        jMenuNaviPageDown.setToolTipText("Scroll to bottom of page");
        jMenuNaviPageDown.setText("Page Down");
        jMenuNaviPageDown.setMnemonic('D');
        jMenuNaviPageDown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, KeyEvent.CTRL_MASK));
        jMenuNaviPageDown.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviPageDown_actionPerformed(e);
                }
            });

        jMenuNaviGoto.setToolTipText("Go to segment with specified number");
        jMenuNaviGoto.setText("Go To Segment...");
        jMenuNaviGoto.setMnemonic('S');
        jMenuNaviGoto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
        jMenuNaviGoto.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuNaviGoto_actionPerformed(e);
                }
            });

        /**
         * Tools Menu
         */
        menuTools.setMnemonic('T');
        menuTools.setText("Tools");
        jMenuTagVerify.setText("Tag Verification");
        jMenuTagVerify.setMnemonic('V');
        jMenuTagVerify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK));
        jMenuTagVerify.setToolTipText("Verify tags");
        jMenuTagVerify.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuTagVerify_actionPerformed(e);
                }
            });
        jMenuSpellCheck.setText("Spell Checking");
        jMenuSpellCheck.setMnemonic('S');
        jMenuSpellCheck.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        jMenuSpellCheck.setToolTipText("Check spelling");
        jMenuSpellCheck.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuSpellCheck_actionPerformed(e);
                }
            });
        jMenuUpdateMiniTM.setToolTipText("Update mini-TM with current segment");
        jMenuUpdateMiniTM.setText("Update Mini-TM");
        jMenuUpdateMiniTM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK));
        jMenuUpdateMiniTM.setMnemonic('U');
        jMenuUpdateMiniTM.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuUpdateMiniTM_actionPerformed(e);
                }
            });
        jMenuMaintainMiniTM.setToolTipText("Edit mini-TM");
        jMenuMaintainMiniTM.setText("Maintain Mini-TM");
        jMenuMaintainMiniTM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
        jMenuMaintainMiniTM.setMnemonic('M');
        jMenuMaintainMiniTM.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuMaintainMiniTM_actionPerformed(e);
                }
            });
        jMenuMergeMiniTM.setToolTipText("Merge two or more mini-TMs");
        jMenuMergeMiniTM.setText("Merge Mini-TMs");
        jMenuMergeMiniTM.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuMergeMiniTM_actionPerformed(e);
                }
            });
        jMenuConvert.setToolTipText("Convert the current .xlz file to original format");
        jMenuConvert.setText("Convert To Original...");
        jMenuConvert.setMnemonic('O');
        jMenuConvert.setEnabled(true);
        jMenuConvert.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuConvert_actionPerformed(e);
                }
            });

        //  New menu items - 29 sept 2003 - file merging and splitting
        jMenuSplitXliff = new JMenuItem();
        jMenuSplitXliff.setToolTipText("Split a .xlz file into smaller files to make editing easier.");
        jMenuSplitXliff.setText("Split XLIFF file...");
        jMenuSplitXliff.setEnabled(true);
        jMenuSplitXliff.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuSplitXliff_actionPerformed(e);
                }
            });
        jMenuMergeXliff = new JMenuItem();
        jMenuMergeXliff.setToolTipText("Merge a split .xlz file back to its original format");
        jMenuMergeXliff.setText("Merge a split XLIFF file...");
        jMenuMergeXliff.setEnabled(true);
        jMenuMergeXliff.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuMergeXliff_actionPerformed(e);
                }
            });

        //  End new items
        /**
         * Option Menu
         */
        optionMenu.setText("Options");
        optionMenu.setMnemonic('O');

        jCheckBoxMenuUpdateTags.setToolTipText("Switch on/off update tags");
        jCheckBoxMenuUpdateTags.setText("Update Tags");
        jCheckBoxMenuUpdateTags.setMnemonic('U');
        jCheckBoxMenuUpdateTags.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCheckBoxMenuUpdateTags_actionPerformed(e);
                }
            });
        jCBMenuSynScrolling.setToolTipText("Switch on/off synchronized scrolling");
        jCBMenuSynScrolling.setText("Synchronized Scrolling");
        jCBMenuSynScrolling.setMnemonic('S');
        jCBMenuSynScrolling.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuSynScrolling_actionPerformed(e);
                }
            });

        jCBMenuWriteProtection.setToolTipText("Switch on/off source write protection");
        jCBMenuWriteProtection.setText("Source Write Protection");
        jCBMenuWriteProtection.setMnemonic('W');
        jCBMenuWriteProtection.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuWriteProtection_actionPerformed(e);
                }
            });
        jCBMenuTagVerifyIgnoreOrder.setToolTipText("Ignores order of tags during verification");
        jCBMenuTagVerifyIgnoreOrder.setText("Ignore Tag Order");
        jCBMenuTagVerifyIgnoreOrder.setMnemonic('I');
        jCBMenuTagVerifyIgnoreOrder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuTagVerifyIgnoreOrder_actionPerformed(e);
                }
            });
        jCBMenuAutoPropagate.setText("Autopropagate");
        jCBMenuAutoPropagate.setMnemonic('P');
        jCBMenuAutoPropagate.setToolTipText("Translate repetitions automatically");
        jCBMenuAutoPropagate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCBMenuAutoPropagate_actionPerformed(e);
                }
            });
        jMenuAutoSave.setToolTipText("Set the autosave properties");
        jMenuAutoSave.setText("Autosave...");
        jMenuAutoSave.setMnemonic('O');
        jMenuAutoSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuAutoSave_actionPerformed(e);
                }
            });
        jMenuShortcutOption.setText("Customize Shortcuts...");
        jMenuShortcutOption.setToolTipText("Customize shortcuts...");
        jMenuShortcutOption.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuShortcutOption_actionPerformed(e);
                }
            });

        /**
         * Help Menu
         */
        menuHelp.setText("Help");
        menuHelp.setMnemonic('H');

        jMenuHelpIndex.setText("Index...");
        jMenuHelpIndex.setMnemonic('I');

        jMenuHelpLicense.setText("License...");
        jMenuHelpLicense.setMnemonic('L');
        jMenuHelpLicense.setToolTipText("Display editor license");
        jMenuHelpLicense.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    helpLicense_actionPerformed(e);
                }
            });

        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.setMnemonic('A');
        jMenuHelpAbout.setToolTipText("About the editor...");
        jMenuHelpAbout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    helpAbout_actionPerformed(e);
                }
            });

        /**
         * Menu bar
         */
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuView);
        menuBar.add(menuNavigation);
        menuBar.add(menuTools);
        menuBar.add(optionMenu);
        menuBar.add(menuHelp);

        /**
         * File menu
         */
        menuFile.add(jMenuNewProject);
        menuFile.add(jMenuOpenProject);
        menuFile.addSeparator();
        menuFile.add(jMenuOpen);
        menuFile.addSeparator();
        menuFile.add(jMenuSave);
        menuFile.add(jMenuSaveAs);

        if (boolOnline) {
            if (boolReview) {
                menuFile.add(jmenuApproveFile);
                menuFile.add(jmenuRejectFile);
            } else {
                menuFile.add(jmenuReturnToPortal);
            }
        }

        menuFile.add(jMenuSaveMiniTM);
        menuFile.addSeparator();
        menuFile.add(jMenuClose);
        menuFile.addSeparator();
        menuFile.add(jMenuPrintOptions);
        menuFile.add(jMenuPrint);
        menuFile.addSeparator();
        menuFile.add(jMenuExit);

        /**
         * Edit menu
         */
        menuEdit.add(jMenuUndo);
        menuEdit.add(jMenuRedo);
        menuEdit.addSeparator();
        menuEdit.add(jMenuCut);
        menuEdit.add(jMenuCopy);
        menuEdit.add(jMenuPaste);
        menuEdit.addSeparator();
        menuEdit.add(jMenuTransfer);
        menuEdit.add(jMenuUntransfer);
        menuEdit.addSeparator();
        menuEdit.add(jMenuCopySource);
        menuEdit.add(jMenuCopySourceTag);
        menuEdit.add(jMenuClearTarget);
        menuEdit.addSeparator();
        menuEdit.add(jMenuMarkSeg);
        menuEdit.add(jMenuMarkExactMatch);
        menuEdit.add(jMenuMarkAllSeg);
        menuEdit.add(jMenuMarkTranslatedAndNext);
        menuEdit.add(jMenuMarkReviewedAndNext);
        menuEdit.add(jMenuMarkRejectedAndNext);
        menuEdit.addSeparator();
        menuEdit.add(jMenuSegComment);
        menuEdit.add(jMenuFileComment);
        menuEdit.addSeparator();
        menuEdit.add(jMenuSearch);

        /**
         * Edit -> Mark Seg
         */
        jMenuMarkSeg.add(jMenuMarkSegTrans);
        jMenuMarkSeg.add(jMenuMarkSegReviewed);
        jMenuMarkSeg.add(jMenuMarkSegRejected);
        jMenuMarkSeg.add(jMenuMarkSegUnTrans);

        /**
         * Edit -> Mark Exact Match
         */
        jMenuMarkExactMatch.add(jMenuMarkExactMatchTrans);
        jMenuMarkExactMatch.add(jMenuMarkExactMatchReviewed);
        jMenuMarkExactMatch.add(jMenuMarkExactMatchRejected);

        /**
         * Edit -> Mark All Segment
         */
        jMenuMarkAllSeg.add(jMenuMarkAllSegTrans);
        jMenuMarkAllSeg.add(jMenuMarkAllSegReviewed);
        jMenuMarkAllSeg.add(jMenuMarkAllSegRejected);

        /**
         * Edit -> Comment
         */
        jMenuSegComment.add(jMenuAddComment);
        jMenuSegComment.add(jMenuEditComment);
        jMenuSegComment.add(jMenuDeleteComment);

        /**
         * View menu
         */
        menuView.add(jMenuShowOption);
        menuView.add(jCBMenuMatchWindow);
        menuView.add(jCBMenuAttributes);
        menuView.add(jCBMenuSourceContext);
        menuView.add(jCBMenuSearchBar);
        menuView.addSeparator();
        menuView.add(jCBMenuSHTag);
        menuView.add(jCBMenuAbbrTagsDisplay);
        menuView.addSeparator();
        menuView.add(jMenuFonts);
        menuView.add(jMenuColor);
        menuView.addSeparator();
        menuView.add(jMenuHOrV);

        /**
         * View -> Show Option
         */
        jMenuShowOption.add(jCBMenuFile);
        jMenuShowOption.add(jCBMenuEditing);
        jMenuShowOption.add(jCBMenuSearch);
        jMenuShowOption.add(jCBMenuNavi);
        jMenuShowOption.add(jCBMenuHelp);
        jMenuShowOption.addSeparator();
        jMenuShowOption.add(jMenuItemShowAll);
        jMenuShowOption.add(jMenuItemHideAll);

        /**
         * View -> Windows Horizonal or Vertical
         */
        jMenuHOrV.add(jMenuHor);
        jMenuHOrV.add(jMenuVer);

        /**
         * Navigation menu
         */
        menuNavigation.add(jMenuNaviNextOption);
        menuNavigation.add(jMenuNaviPrevOption);
        menuNavigation.addSeparator();
        menuNavigation.add(jMenuNaviNextMultiple100MatchOption);
        menuNavigation.add(jMenuNaviPrevMultiple100MatchOption);
        menuNavigation.addSeparator();
        menuNavigation.add(jMenuNaviGotoTop);
        menuNavigation.add(jMenuNaviGotoBottom);
        menuNavigation.addSeparator();
        menuNavigation.add(jMenuNaviPageUp);
        menuNavigation.add(jMenuNaviPageDown);
        menuNavigation.addSeparator();
        menuNavigation.add(jMenuNaviGoto);

        /**
         * Tools menu
         */
        menuTools.add(jMenuTagVerify);
        menuTools.add(jMenuSpellCheck);
        menuTools.addSeparator();
        menuTools.add(jMenuUpdateMiniTM);
        menuTools.add(jMenuMaintainMiniTM);
        menuTools.add(jMenuMergeMiniTM);
        menuTools.addSeparator();
        menuTools.add(jMenuConvert);
        menuTools.add(jMenuSplitXliff);
        menuTools.add(jMenuMergeXliff);

        /**
         * Option menu
         */
        optionMenu.add(jCheckBoxMenuUpdateTags);
        optionMenu.add(jCBMenuTagProtection);
        optionMenu.add(jCBMenuSynScrolling);
        optionMenu.add(jCBMenuWriteProtection);
        optionMenu.add(jCBMenuTagVerifyIgnoreOrder);
        optionMenu.add(jCBMenuAutoPropagate);
        optionMenu.add(jMenuAutoSave);
        optionMenu.addSeparator();
        optionMenu.add(jMenuShortcutOption);

        /**
         * Help menu
         */
        menuHelp.add(jMenuHelpIndex);
        menuHelp.addSeparator();
        menuHelp.add(jMenuHelpLicense);
        menuHelp.addSeparator();
        menuHelp.add(jMenuHelpAbout);

        dDevision = 0.75;
        jContrastFrame = new ContrastFrame(backend.getConfig().isBFlagViewSwitch(), backend);
        jMiniTMFrame = new MiniTMFrame(backend);
        jSourceContextFrame = new SourceContextFrame();

        jSourceContextFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    boolean bFlagSourceContext = false;
                    jCBMenuSourceContext.setSelected(bFlagSourceContext);
                    backend.getConfig().setBFlagSourceContext(bFlagSourceContext);
                }
            });

        ComponentListener cl = new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int topH = jContrastFrame.getHeight();
                int bottomH = jMiniTMFrame.getHeight();
                double ratio = (double)topH / (topH + bottomH);

                if (ratio != oldRatio) {
                    ((JSplitPane)e.getSource()).setDividerLocation(ratio);
                }

                oldRatio = ratio;
            }
        };

        MySplitPane.addComponentListener(cl);
        MySplitPane.setTopComponent(jContrastFrame);
        MySplitPane.setBottomComponent(jMiniTMFrame);
        MySplitPane.setDividerLocation(450);
        MySplitPane.setDividerSize(8);
        MySplitPane.setBackground(SystemColor.window);

        toolBar.setFloatable(false);
        primaryPane.setLayout(new BorderLayout(0, 0));
        primaryPane.setBackground(SystemColor.desktop);
        primaryPane.add(MySplitPane, BorderLayout.CENTER);

        statusBar.setBorder(null);
        statusBar.setMinimumSize(new Dimension(10, 20));
        statusBar.setPreferredSize(new Dimension(148, 20));
        statusBar.setLayout(new BorderLayout(0, 0));
        statusBar.add(myMatchstatusBar, BorderLayout.CENTER);

        contentPane = (JPanel)this.getRootPane().getContentPane();
        contentPane.setLayout(borderLayout1);
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        contentPane.add(primaryPane, BorderLayout.CENTER);

        this.setSize(new Dimension(925, 700));
        this.addWindowListener(new WindowAdapter() {
                public void windowActivated(WindowEvent e) { /* no op */
                }
            });
        this.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { /* no op */
                }
            });
        this.setJMenuBar(menuBar);

        TransProject project = backend.getProject();

        if ((project == null) || project.getProjectName().trim().equals("")) {
            this.setTitle(Constants.TOOL_NAME);
        } else {
            this.setTitle(Constants.TOOL_NAME + " - " + project.getProjectName());
        }

        this.addPropertyChangeListener(this);
    }

    public void repaintSelf(int oldSegNum) {
        AlignmentMain.testMain1.repaintSelf(oldSegNum);
        AlignmentMain.testMain2.repaintSelf(oldSegNum);
        AlignmentMain.testMain2.navigateTo(0);
    }

    public void repaintSelf() {
        AlignmentMain.testMain1.repaintSelf();
        AlignmentMain.testMain2.repaintSelf();
    }

    public void setMenuState() {
        if ((AlignmentMain.testMain1 == null) || (AlignmentMain.testMain1.tableView == null)) {
            return;
        }

        int row = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (row == -1) {
            return;
        }

        int iStatus = (backend.getTMData()).tmsentences[row].getTranslationStatus();

        switch (iStatus) {
        case TMSentence.UNTRANSLATED:
            this.jMenuMarkSegTrans.setEnabled(true);
            this.jMenuMarkTranslatedAndNext.setEnabled(true);
            this.jMenuMarkReviewedAndNext.setEnabled(false);
            this.jMenuMarkRejectedAndNext.setEnabled(false);
            this.jMenuMarkSegUnTrans.setEnabled(false);
            this.jMenuMarkSegReviewed.setEnabled(false);
            this.jMenuMarkSegRejected.setEnabled(false);
            this.jBtnMarkAsTransAndGoToNextUnTrans.setEnabled(true);
            this.jBtnMarkAsApprovedAndGoToNextTrans.setEnabled(false);
            this.jBtnMarkAsRejectedAndGoToNextTrans.setEnabled(false);

            break;

        case TMSentence.TRANSLATED:
            this.jMenuMarkSegTrans.setEnabled(false);
            this.jMenuMarkTranslatedAndNext.setEnabled(false);
            this.jMenuMarkReviewedAndNext.setEnabled(true);
            this.jMenuMarkRejectedAndNext.setEnabled(true);
            this.jMenuMarkSegRejected.setEnabled(true);
            this.jBtnMarkAsTransAndGoToNextUnTrans.setEnabled(false);
            this.jBtnMarkAsApprovedAndGoToNextTrans.setEnabled(true);
            this.jBtnMarkAsRejectedAndGoToNextTrans.setEnabled(true);

            int targetType = backend.getTMData().tmsentences[row].getTranslationType();

            if (targetType == TMSentence.EXACT_TRANSLATION) {
                this.jMenuMarkSegUnTrans.setEnabled(false);
            } else {
                this.jMenuMarkSegUnTrans.setEnabled(true);
            }

            this.jMenuMarkSegReviewed.setEnabled(true);

            break;

        case TMSentence.VERIFIED:
        case TMSentence.REJECTED:
            this.jMenuMarkSegTrans.setEnabled(true);
            this.jMenuMarkTranslatedAndNext.setEnabled(false);
            this.jMenuMarkReviewedAndNext.setEnabled(true);
            this.jMenuMarkRejectedAndNext.setEnabled(true);
            this.jBtnMarkAsTransAndGoToNextUnTrans.setEnabled(false);
            this.jBtnMarkAsApprovedAndGoToNextTrans.setEnabled(true);
            this.jBtnMarkAsRejectedAndGoToNextTrans.setEnabled(true);
            this.jMenuMarkSegUnTrans.setEnabled(false);
            this.jMenuMarkSegReviewed.setEnabled(true);
            this.jMenuMarkSegRejected.setEnabled(true);

            break;
        }

        iStatus = backend.getTMData().tmsentences[row].hasCommented();

        switch (iStatus) {
        case 0:
            this.jMenuAddComment.setEnabled(true);
            this.jMenuEditComment.setEnabled(false);
            this.jMenuDeleteComment.setEnabled(false);

            break;

        case 1:
            this.jMenuAddComment.setEnabled(false);
            this.jMenuEditComment.setEnabled(true);
            this.jMenuDeleteComment.setEnabled(true);

            break;
        }

        boolean shownAsGroup = backend.getTMData().getGroupTrack().isShownAsGrouped();
        this.jMenuTransfer.setEnabled(!shownAsGroup);
        this.jMenuUntransfer.setEnabled(shownAsGroup);
    }

    private void enableMenus(boolean enable) {
        /**
         * file menu
         */
        this.jMenuSave.setEnabled(false);
        this.jBtnSave.setEnabled(false);

        this.jMenuSaveAs.setEnabled(enable);
        this.jMenuClose.setEnabled(enable);
        this.jMenuPrint.setEnabled(enable);

        /**
         * edit menu
         */
        this.jMenuUndo.setEnabled(false);
        this.jMenuRedo.setEnabled(false);

        this.jMenuCopy.setEnabled(enable);
        this.jMenuCut.setEnabled(enable);
        this.jMenuPaste.setEnabled(enable);

        this.jMenuTransfer.setEnabled(enable);
        this.jMenuUntransfer.setEnabled(enable);

        this.jMenuCopySource.setEnabled(enable);
        this.jMenuCopySourceTag.setEnabled(enable);
        this.jMenuClearTarget.setEnabled(enable);

        this.jMenuMarkAllSeg.setEnabled(enable);
        this.jMenuMarkExactMatch.setEnabled(enable);
        this.jMenuMarkSeg.setEnabled(enable);

        this.jMenuMarkReviewedAndNext.setEnabled(enable);
        this.jMenuMarkRejectedAndNext.setEnabled(enable);
        this.jMenuMarkTranslatedAndNext.setEnabled(enable);

        this.jMenuSegComment.setEnabled(enable);
        this.jMenuFileComment.setEnabled(enable);

        this.jMenuSearch.setEnabled(enable);

        //edit buttons
        this.jBtnUndo.setEnabled(false);
        this.jBtnRedo.setEnabled(false);

        this.jBtnCopy.setEnabled(enable);
        this.jBtnCut.setEnabled(enable);
        this.jBtnPaste.setEnabled(enable);

        this.jBtnSearch.setEnabled(enable);

        this.jBtnMarkAsTransAndGoToNextUnTrans.setEnabled(enable);
        this.jBtnMarkAsApprovedAndGoToNextTrans.setEnabled(enable);
        this.jBtnMarkAsRejectedAndGoToNextTrans.setEnabled(enable);

        /**
         * navigate menus
         */
        this.jMenuNaviGoto.setEnabled(enable);
        this.jMenuNaviGotoBottom.setEnabled(enable);
        this.jMenuNaviGotoTop.setEnabled(enable);
        this.jMenuNaviNextOption.setEnabled(enable);
        this.jMenuNaviPrevOption.setEnabled(enable);
        this.jMenuNaviPageDown.setEnabled(enable);
        this.jMenuNaviPageUp.setEnabled(enable);
        this.jMenuNaviNextMultiple100MatchOption.setEnabled(enable);
        this.jMenuNaviPrevMultiple100MatchOption.setEnabled(enable);

        // navigation buttons
        this.jBtnNext.setEnabled(enable);
        this.jBtnPrev.setEnabled(enable);

        /**
         * tool menus
         */
        this.jMenuTagVerify.setEnabled(enable);
        this.jMenuSpellCheck.setEnabled(enable);
        this.jMenuUpdateMiniTM.setEnabled(enable);

        this.jBtnTagVerify.setEnabled(enable);
        this.jBtnSpellCheck.setEnabled(enable);
        this.jBtnUpdateMiniTM.setEnabled(enable);

        //  Enable online components.
        if (boolOnline) {
            if (boolReview) {
                this.jmenuApproveFile.setEnabled(enable);
                this.jbtnApproveFile.setEnabled(enable);

                this.jmenuRejectFile.setEnabled(enable);
                this.jbtnRejectFile.setEnabled(enable);
            } else {
                this.jmenuReturnToPortal.setEnabled(enable);
                this.jBtnReturnToPortal.setEnabled(enable);
            }
        }

        //bug 4777510
        //this.jMenuAutoSave.setEnabled(enable);
    }

    public void dispose() {
        //  Check if it is a safe time to exit. If not ask the user what to do.
        if ((semaphore != null) && (!semaphore.isSafeToExit())) {
            boolean exitAccepted = askUserToExit();

            if (!exitAccepted) {
                return;
            }
        }

        int f = closeFile();

        if (f == 0) {
            return;
        }

        bSafeExit = true;

        try {
            super.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        backend.shutdown();

        if (proc != null) {
            proc.destroy();
        }

        if (saveTempFileProc != null) {
            saveTempFileProc.destroy();
        }

        //extract menu fills the map from constructor in our case it's the default configuration's shortcuts map
        Map m = shortcutsBuilder.extractShortcuts();

        //CustomKeyboard.saveConfig();
        try {
            backend.getConfig().save();
        } catch (ConfigurationException cfge) {
            logger.throwing(getClass().getName(), "dispose()", cfge);
            logger.severe("Saving config  - WILL BE IGNORED");
        }

        System.exit(0);
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            fileExit_actionPerformed(null);
        }
    }

    /** *******************************************************************************************
     *
     *  Help menus
     *
     *  *******************************************************************************************
     */
    public void helpAbout_actionPerformed(ActionEvent e) {
        String msg = Constants.TOOL_NAME + "\nCopyright 2004,2005 by Sun Microsystems.\n\n";

        if (File.separator.equals("/")) { //unix sparc
            msg += "GNU ASpell spell checker, version 0.50.3\nCopyright 2000 by Kevin Atkinson under the terms of the LGPL.\n\n";
        } else { //windows
            msg += "ASpell spell checker, version .33.5\nCopyright 2000 by Kevin Atkinson under the terms of the LGPL.\n\n";
        }

        JOptionPane.showMessageDialog(this, msg, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void helpLicense_actionPerformed(ActionEvent e) {
        licenseDlg.setAgreementMode(false);
        licenseDlg.setVisible(true);
    }

    /** *******************************************************************************************
     *
     *  File menus
     *
     *  *******************************************************************************************
     */
    void jMenuNewProject_actionPerformed(ActionEvent e) {
        if (backend.hasCurrentFile()) { //a .xlf file is opened
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "To create a new project, you must first close the current file.", "Error", JOptionPane.WARNING_MESSAGE);

            return;
        }

        NewMiniTMPanel newProject = new NewMiniTMPanel(this, backend.getAllProjects());

        Rectangle rect = this.getBounds();
        double x = (rect.getX() + (rect.getWidth() / 2)) - (newProject.getWidth() / 2);
        double y = (rect.getY() + (rect.getHeight() / 2)) - (newProject.getHeight() / 2);
        newProject.setLocation((int)x, (int)y);

        newProject.setVisible(true);

        if (newProject.didCancel()) {
            return;
        }

        String projName = newProject.getProjectName();
        String tgtLang = newProject.getTargetLang();
        String srcLang = newProject.getSourceLang();

        try {
            TransProject project = new TransProject(projName, srcLang, tgtLang, backend.getConfig().getMiniTMDir().getAbsolutePath(), "");

            backend.openProject(project);
        } catch (MiniTMException mte) {
            logger.throwing(getClass().getName(), "jMenuNewProject_actionPerformed", mte);
            logger.severe("Exception:" + mte);
        }
    }

    /**
     * open projects...
     */
    void jMenuOpenProject_actionPerformed(ActionEvent e) {
        if (backend.hasCurrentFile()) { //a .xlf file is opened
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "To open a project, you must first close the current file.", "Error", JOptionPane.WARNING_MESSAGE);

            return;
        }

        OpenMiniTMPanel openProject = new OpenMiniTMPanel(this, backend.getAllProjects());
        Rectangle rect = this.getBounds();
        double x = (rect.getX() + (rect.getWidth() / 2)) - (openProject.getWidth() / 2);
        double y = (rect.getY() + (rect.getHeight() / 2)) - (openProject.getHeight() / 2);
        openProject.setLocation((int)x, (int)y);

        openProject.setVisible(true);

        if (openProject.didCancel()) {
            return;
        }

        String projName = openProject.getProjectName();
        String tgtLang = openProject.getTargetLang();
        String srcLang = openProject.getSourceLang();

        try {
            TransProject project = new TransProject(projName, srcLang, tgtLang, backend.getConfig().getMiniTMDir().getAbsolutePath(), "");

            backend.openProject(project);
        } catch (MiniTMException mte) {
            logger.throwing(getClass().getName(), "jMenuNewProject_actionPerformed", mte);
            logger.severe("Exception:" + mte);
        }
    }

    void jMenuOpen_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!openFile()) {
            return; //  Maintenance note: fix this. Do something productive.
        }
    }

    public boolean openFile() {
        //TODO replace woth saveIfMOdified
        if (bHasModified && (backend.getCurrentFile() != null)) {
            int iRet = JOptionPane.showConfirmDialog(this, "The current xlz or xlf file has been modified,\nDo you want to Save it?", "Save File", JOptionPane.YES_NO_CANCEL_OPTION);

            if (iRet == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (iRet == JOptionPane.YES_OPTION) {
                if (!backend.saveFile()) {
                    //JOptionPane.showMessageDialog(this,"Failed To Save the Current File","Failed to Save",JOptionPane.OK_OPTION);
                    saveFileFailed();

                    return false;
                }

                // bug 4821917 || if cancel the "Open File" dialog, "xp" and "tmpdata" can not be setup to "null"
                this.setBHasModified(false);
            } else {
            }
        }

        if (backend.getProject() == null) {
            JOptionPane.showMessageDialog(this, "You have not opened/created a project, Please open/create a project before you open a .xlf or .xlz file!", "Failed to Open", JOptionPane.OK_OPTION);

            return false;
        }

        Object[] message = new Object[2];
        message[0] = "Please select a .xlf or .xlz file to open:";

        JFileChooser f = new JFileChooser();

        f.setAccessory(new FilePreviewPane(f));

        f.setMultiSelectionEnabled(false);
        f.setAcceptAllFileFilterUsed(false);
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        f.addChoosableFileFilter(OpenFileFilters.XLF_FILTER);
        f.addChoosableFileFilter(OpenFileFilters.XLZ_FILTER);
        f.setSelectedFile(new File("*.xlz"));

        File curFile = backend.getCurrentFile();

        if (curFile != null) {
            backend.getConfig().setStrLastFile(curFile.getAbsolutePath());
            f.setCurrentDirectory(curFile.getParentFile());
        } else {
            f.setCurrentDirectory(new File(backend.getConfig().getStrLastFile()).getParentFile());
        }

        message[1] = f;

        String[] options = { "Open", "Cancel" };

        int result = f.showOpenDialog(this);

        switch (result) {
        case JFileChooser.APPROVE_OPTION: // ok

            try {
                //bug 4821917
                backend.resetBeforeOpen();

                //--------------
                File currentFile = f.getSelectedFile();

                if (currentFile == null) {
                    JOptionPane.showMessageDialog(this, "This file is missing!", "Failed to Open", JOptionPane.OK_OPTION);

                    return false;
                }

                if (!currentFile.exists()) {
                    JOptionPane.showMessageDialog(this, "This file does not exist!", "Failed to Open", JOptionPane.OK_OPTION);

                    return false;
                }

                if (!currentFile.canWrite()) {
                    JOptionPane.showMessageDialog(this, "This file can not be written!", "Failed to Open", JOptionPane.OK_OPTION);

                    return false;
                }

                if ((currentFile.getName() == null) || currentFile.getName().endsWith("*.xlz")) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(this, "Please select one .xlf file.", "Error", JOptionPane.WARNING_MESSAGE);

                    return false;
                }

                tryToOpenFile(currentFile);

                //                    Thread t = new OpenFileThread(this,currentFile, backend);
                //                    t.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unsupported file type!");

                return false;
            }

            break;

        case JFileChooser.CANCEL_OPTION: // cancel
            break;
        }

        return true;
    }

    public boolean openFile(String strFileName) {
        if (bHasModified && (backend.getCurrentFile() != null)) {
            int iRet = JOptionPane.showConfirmDialog(this, "The current file has been modified,\nDo you want to Save it?", "Save File", JOptionPane.YES_NO_CANCEL_OPTION);

            if (iRet == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (iRet == JOptionPane.YES_OPTION) {
                if (!backend.saveFile()) {
                    //JOptionPane.showMessageDialog(this,"Failed To Save the Current File","Failed to Save",JOptionPane.OK_OPTION);
                    saveFileFailed();

                    return false;
                }

                this.setBHasModified(false);
            }
        }

        if (backend.getProject() == null) {
            JOptionPane.showMessageDialog(this, "You have not opened/created a project, Please open/create a project before you open a xlf file!", "Failed to Open", JOptionPane.OK_OPTION);

            return false;
        }

        File f = new File(strFileName);

        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "This file does not exist!", "Failed to Open", JOptionPane.OK_OPTION);
            f = null;

            return false;
        }

        if (!f.canWrite()) {
            JOptionPane.showMessageDialog(this, "This file can not be written!", "Failed to Open", JOptionPane.OK_OPTION);
            f = null;

            return false;
        }

        //Thread t = new OpenFileThread(this,new File(strFileName),backend);
        //t.start();
        tryToOpenFile(new File(strFileName));

        return true;
    }

    void jMenuSave_actionPerformed(ActionEvent e) {
        logger.info("Saving file.");

        try {
            Runnable saveCurrentFileRunnable = new SaveCurrentFileThread(this, backend);
            Thread saveCurrentFileThread = new Thread(saveCurrentFileRunnable);
            saveCurrentFileThread.start();
        } catch (Exception ex) {
            System.err.println("Thread synchronization problem, possible race condition.");
            ex.printStackTrace();
        }
    }

    /*
        public boolean SaveCurrentFile(File f,boolean autosave) {
            if(f == null) return false;
            if(tmpdata == null) return false;

            //  Update MiniTM
            for(int i=0;i<tmpdata.tmsentences.length;i++) {
                if(!autosave && tmpdata.bMiniTMFlags[i]) {
                    tmpdata.tmsentences[i].updateMiniTM(this.translatorID,i);
                    tmpdata.bMiniTMFlags[i] = false;
                }
            }

            try {

                //  save to file
                tmpdata.saveAllTranslations(autosave, this.bFlagWriteProtection);

                xp.setTargetLanguage(targetLan);
                xp.saveToFile(f);
            }
            catch(NestableException ne){
                ne.printStackTrace();
                return false;
            }
            catch(Throwable t) {
                t.printStackTrace();
                return false;
            }
            return true;
        }
     */
    void jMenuSaveAs_actionPerformed(ActionEvent e) {
        logger.info("Saving file as...");

        try {
            Runnable saveAsRunnable = new SaveAsFileThread(this, backend);
            Thread saveAsThread = new Thread(saveAsRunnable);
            saveAsThread.start();
        } catch (Exception ex) {
            System.err.println("Thread synchronization problem, possible race condition.");
            ex.printStackTrace();
        }
    }

    public boolean copyFile(File in_file, File out_file) {
        String in_name = in_file.getAbsolutePath();
        String out_name = out_file.getAbsolutePath();

        if (!in_file.exists()) {
            System.err.println("no such source file: " + in_name);

            return false;
        }

        if (!in_file.isFile()) {
            System.err.println("can't copy directory: " + in_name);

            return false;
        }

        if (!in_file.canRead()) {
            System.err.println("can not read file: " + in_name);

            return false;
        }

        if (out_file.isDirectory()) {
            System.err.println("can not copy to directory: " + out_name);

            return false;
        }

        if (out_file.exists()) {
            if (!out_file.canWrite()) {
                System.err.println("can not copy to file: " + out_name);

                return false;
            }
        } else {
            String parent = out_file.getParent();

            if (parent == null) {
                parent = strCurrentDir;
            }

            File dir = new File(parent);

            if (!dir.exists()) {
                System.err.println("destination directory doesn't exist: " + parent);

                return false;
            }

            if (dir.isFile()) {
                System.err.println("destination is not a directory: " + parent);

                return false;
            }

            if (!dir.canWrite()) {
                System.err.println("destination directory is unwriteable: " + parent);

                return false;
            }
        }

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(in_file);
            out = new FileOutputStream(out_file);

            byte[] buffer = new byte[4096];
            int bytes_read;

            while ((bytes_read = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes_read);
            }
        } catch (Exception e) {
            System.err.println(e.toString());

            return false;
        }

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                System.err.println(e.toString());

                return false;
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                System.err.println(e.toString());

                return false;
            }
        }

        return true;
    }

    /*
        public boolean SaveAsFile(File f,boolean autosave) {
            if(f == null) return false;
            if(tmpdata == null) return false;
            try {
                tmpdata.saveAllTranslations(false, this.bFlagWriteProtection);

                xp.setTargetLanguage(targetLan);
                xp.saveToFile(f);
            }

    //        catch(IOException ioEx) {
    //            ioEx.printStackTrace();
    //            return false;
            }
            catch(NestableException ne) {
                ne.printStackTrace();
                return false;
            }
            return true;
        }
     */
    void jMenuSaveMiniTM_actionPerformed(ActionEvent e) {
        if (backend.getProject() != null) {
            try {
                backend.getProject().getMiniTM().saveMiniTmToFile();
            } catch (Exception ex) {
                logger.throwing(getClass().getName(), "jMenuSaveMiniTM_actionPerformed", ex);
                logger.severe("Exception:" + ex);
            }
        }
    }

    void jMenuClose_actionPerformed(ActionEvent e) {
        if (closeGUI() != 0) {
            /**
             * close mini-TM?
             */
            Toolkit.getDefaultToolkit().beep();

            int iRet = JOptionPane.showConfirmDialog(this, "Do you wish to save the mini-TM?", "Save Mini-TM", JOptionPane.YES_NO_OPTION);

            if (iRet == 0) {
                jMenuSaveMiniTM_actionPerformed(null);
            }
        }
    }

    public int closeFile() {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        File curFile = backend.getCurrentFile();

        if (curFile != null) {
            backend.getConfig().setStrLastFile(curFile.getAbsolutePath());
        }

        if ((bHasModified) && (curFile != null)) {
            Toolkit.getDefaultToolkit().beep();

            int iRet = JOptionPane.showConfirmDialog(this, "The current file has been modified,\nDo you want to Save it?", "Save File", JOptionPane.YES_NO_CANCEL_OPTION);

            if ((JOptionPane.CANCEL_OPTION == iRet) || (JOptionPane.CLOSED_OPTION == iRet)) {
                return 0;
            }

            if (iRet == JOptionPane.OK_OPTION) {
                if (!backend.saveFile()) {
                    saveFileFailed();

                    //JOptionPane.showMessageDialog(this,"Failed To Save the Current File","Failed to Save",JOptionPane.OK_OPTION);
                    return 0;
                }

                this.setBHasModified(false);
            } else if (iRet == JOptionPane.NO_OPTION) {
                this.setBHasModified(false);

                //tmpdata = null;
                backend.resetBeforeOpen();

                //if(bFlagTempFile)
                bSafeExit = true;

                try {
                    backend.getConfig().save();
                } catch (ConfigurationException cfge) {
                    logger.throwing(getClass().getName(), "closeFile", cfge);
                    logger.severe("While closing file -- WILL BE IGNORED");
                }

                return 1;
            }
        } else {
            //if(bFlagTempFile)
            bSafeExit = true;

            try {
                backend.getConfig().save();
            } catch (ConfigurationException cfge) {
                logger.throwing(getClass().getName(), "closeFile", cfge);
                logger.severe("While closing file -- WILL BE IGNORED");
            }
        }

        backend.resetBeforeOpen();

        //xp = null;
        //tmpdata = null;
        // System.gc();
        return 1;
    }

    /**
     * printing options
     */
    void jMenuPrintOptions_actionPerformed(ActionEvent e) {
        if (printOptionDlg == null) {
            printOptionDlg = new TMPrintOptionDlg();
        }

        printOptionDlg.setLocationRelativeTo(this);
        printOptionDlg.setVisible(true);
    }

    /**
     * printing functions
     */
    private void addHtmlHead(PrintWriter out) throws IOException {
        write(out, "<html>\r\n");
        write(out, "<head>\r\n");
        write(out, "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + Languages.getLanguageENC(backend.getProject().getTgtLang()) + "\">");
        write(out, ("<title>" + backend.getCurrentFile().getName() + "</title>\r\n"));
        write(out, "</head>\r\n");
        write(out, "\r\n");
        write(out, "<body>\r\n");
        write(out, "\r\n");
        write(out, "<table border=\"0\" align=\"center\" width=\"" + totalwidth + "\" bordercolor=\"#FFFFFF\">\r\n");
    }

    private void addHtmlTailer(PrintWriter out) throws IOException {
        write(out, "</table>\r\n");
        write(out, "</body>\r\n");
        write(out, "\r\n");
        write(out, "</html>\r\n");
        out.close();
    }

    void write(PrintWriter out, String str) throws IOException {
        out.println(str);

        out.flush();
    }

    void jMenuPrint_actionPerformed(ActionEvent e) throws IOException {
        if (!backend.hasCurrentFile()) {
            return;
        }

        tmpDlgForPrinting.setLocationRelativeTo(tmpDlgForPrinting.getOwner());
        tmpDlgForPrinting.setVisible(true);
    }

    void print() {
        Thread writeFile = new Thread(new Runnable() {
                public void run() {
                    try {
                        PrintWriter out = prepareHtmlFile();

                        addHtmlHead(out);

                        boolean showNum = TMPrintOptionDlg.showNumber;
                        boolean showType = TMPrintOptionDlg.showType;
                        int contentType = TMPrintOptionDlg.contentType;
                        int tagType = TMPrintOptionDlg.tagType;
                        String typeStr = null;

                        TMData tmpdata = backend.getTMData();

                        for (int i = 0; i < ((tmpdata).tmsentences).length; i++) {
                            switch (contentType) {
                            case 1:

                                /**
                                 * append source
                                 */
                                write(out, "<tr>\r\n");

                                if (showNum) {
                                    write(out, "<td " + col1 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\">");
                                    write(out, String.valueOf(i + 1));
                                    write(out, "</td>\r\n");
                                }

                                if (showType) {
                                    write(out, "<td " + col2 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\">");

                                    int type = (tmpdata).tmsentences[i].getTranslationType();

                                    switch (type) {
                                    case TMSentence.EXACT_TRANSLATION:
                                        typeStr = "&lt;100&gt;";

                                        break;

                                    case TMSentence.UNKNOWN_TRANSLATION:
                                        typeStr = "&lt;UNT&gt;";

                                        break;

                                    case TMSentence.USER_TRANSLATION:
                                        typeStr = "&lt;USE&gt;";

                                        break;

                                    case TMSentence.AUTO_TRANSLATION:
                                        typeStr = "&lt;AUT&gt;";

                                        break;

                                    case TMSentence.FUZZY_TRANSLATION:
                                        typeStr = "&lt;FUZ&gt;";

                                        break;
                                    }

                                    write(out, typeStr);
                                    write(out, "</td>\r\n");
                                }

                                write(out, "<td " + col3 + " align=\"left\" bordercolor=\"#FFFFFF\">");

                                switch (tagType) {
                                case 1:
                                    write(out, BaseElements.convert((tmpdata).tmsentences[i].getSource()));

                                    break;

                                case 2:
                                    write(out, BaseElements.pureText((tmpdata).tmsentences[i].getSource()));

                                    break;

                                case 3:
                                    write(out, BaseElements.abbreviateContent((tmpdata).tmsentences[i].getSource()));

                                    break;
                                }

                                write(out, "</td>\r\n");
                                write(out, "</tr>\r\n");

                                /**
                                 * append target
                                 */
                                write(out, "<tr>\r\n");

                                if (showNum) {
                                    write(out, "<td " + col1 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\">");
                                    write(out, String.valueOf(i + 1));
                                    write(out, "</td>\r\n");
                                }

                                if (showType) {
                                    write(out, "<td " + col2 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\">");

                                    int type = (tmpdata).tmsentences[i].getTranslationType();

                                    switch (type) {
                                    case TMSentence.EXACT_TRANSLATION:
                                        typeStr = "&lt;100&gt;";

                                        break;

                                    case TMSentence.UNKNOWN_TRANSLATION:
                                        typeStr = "&lt;UNT&gt;";

                                        break;

                                    case TMSentence.USER_TRANSLATION:
                                        typeStr = "&lt;USE&gt;";

                                        break;

                                    case TMSentence.AUTO_TRANSLATION:
                                        typeStr = "&lt;AUT&gt;";

                                        break;

                                    case TMSentence.FUZZY_TRANSLATION:
                                        typeStr = "&lt;FUZ&gt;";

                                        break;
                                    }

                                    write(out, typeStr);
                                    write(out, "</td>\r\n");
                                }

                                write(out, "<td " + col3 + " align=\"left\" bordercolor=\"#FFFFFF\">");

                                switch (tagType) {
                                case 1:
                                    write(out, BaseElements.convert((tmpdata).tmsentences[i].getTranslation()));

                                    break;

                                case 2:
                                    write(out, BaseElements.pureText((tmpdata).tmsentences[i].getTranslation()));

                                    break;

                                case 3:
                                    write(out, BaseElements.abbreviateContent((tmpdata).tmsentences[i].getTranslation()));

                                    break;
                                }

                                write(out, "</td>\r\n");
                                write(out, "</tr>\r\n");

                                /**
                                 * add a null line
                                 */
                                write(out, "<tr>\r\n");
                                write(out, "<td " + col1 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\"></td>\r\n");
                                write(out, "<td " + col2 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\"></td>\r\n");
                                write(out, "<td " + col3 + " align=\"left\" bordercolor=\"#FFFFFF\"></td>\r\n");
                                write(out, "</tr>\r\n");

                                break;

                            case 2:

                                /**
                                 * append target
                                 */
                                write(out, "<tr>\r\n");

                                if (showNum) {
                                    write(out, "<td " + col1 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\">");
                                    write(out, String.valueOf(i + 1));
                                    write(out, "</td>\r\n");
                                }

                                if (showType) {
                                    write(out, "<td " + col2 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\">");

                                    int type = (tmpdata).tmsentences[i].getTranslationType();

                                    switch (type) {
                                    case TMSentence.EXACT_TRANSLATION:
                                        typeStr = "&lt;100&gt;";

                                        break;

                                    case TMSentence.UNKNOWN_TRANSLATION:
                                        typeStr = "&lt;UNT&gt;";

                                        break;

                                    case TMSentence.USER_TRANSLATION:
                                        typeStr = "&lt;USE&gt;";

                                        break;

                                    case TMSentence.AUTO_TRANSLATION:
                                        typeStr = "&lt;AUT&gt;";

                                        break;

                                    case TMSentence.FUZZY_TRANSLATION:
                                        typeStr = "&lt;FUZ&gt;";

                                        break;
                                    }

                                    write(out, typeStr);
                                    write(out, "</td>\r\n");
                                }

                                write(out, "<td " + col3 + " align=\"left\" bordercolor=\"#FFFFFF\">");

                                switch (tagType) {
                                case 1:
                                    write(out, BaseElements.convert((tmpdata).tmsentences[i].getTranslation()));

                                    break;

                                case 2:
                                    write(out, BaseElements.pureText((tmpdata).tmsentences[i].getTranslation()));

                                    break;

                                case 3:
                                    write(out, BaseElements.abbreviateContent((tmpdata).tmsentences[i].getTranslation()));

                                    break;
                                }

                                write(out, "</td>\r\n");
                                write(out, "</tr>\r\n");

                                /**
                                 * add a null line
                                 */
                                write(out, "<tr>\r\n");
                                write(out, "<td " + col1 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\"></td>\r\n");
                                write(out, "<td " + col2 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\"></td>\r\n");
                                write(out, "<td " + col3 + " align=\"left\" bordercolor=\"#FFFFFF\"></td>\r\n");
                                write(out, "</tr>\r\n");

                                break;

                            case 3:

                                /**
                                 * append source
                                 */
                                write(out, "<tr>\r\n");

                                if (showNum) {
                                    write(out, "<td " + col1 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\">");
                                    write(out, String.valueOf(i + 1));
                                    write(out, "</td>\r\n");
                                }

                                if (showType) {
                                    write(out, "<td " + col2 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\">");

                                    int type = (tmpdata).tmsentences[i].getTranslationType();

                                    switch (type) {
                                    case TMSentence.EXACT_TRANSLATION:
                                        typeStr = "&lt;100&gt;";

                                        break;

                                    case TMSentence.UNKNOWN_TRANSLATION:
                                        typeStr = "&lt;UNT&gt;";

                                        break;

                                    case TMSentence.USER_TRANSLATION:
                                        typeStr = "&lt;USE&gt;";

                                        break;

                                    case TMSentence.AUTO_TRANSLATION:
                                        typeStr = "&lt;AUT&gt;";

                                        break;

                                    case TMSentence.FUZZY_TRANSLATION:
                                        typeStr = "&lt;FUZ&gt;";

                                        break;
                                    }

                                    write(out, typeStr);
                                    write(out, "</td>\r\n");
                                }

                                write(out, "<td " + col3 + " align=\"left\" bordercolor=\"#FFFFFF\">");

                                switch (tagType) {
                                case 1:
                                    write(out, BaseElements.convert((tmpdata).tmsentences[i].getSource()));

                                    break;

                                case 2:
                                    write(out, BaseElements.pureText((tmpdata).tmsentences[i].getSource()));

                                    break;

                                case 3:
                                    write(out, BaseElements.abbreviateContent((tmpdata).tmsentences[i].getSource()));

                                    break;
                                }

                                write(out, "</td>\r\n");
                                write(out, "</tr>\r\n");

                                /**
                                 * add a null line
                                 */
                                write(out, "<tr>\r\n");
                                write(out, "<td " + col1 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\"></td>\r\n");
                                write(out, "<td " + col2 + " align=\"center\" bordercolor=\"#FFFFFF\" valign=\"top\"></td>\r\n");
                                write(out, "<td " + col3 + " align=\"left\" bordercolor=\"#FFFFFF\"></td>\r\n");
                                write(out, "</tr>\r\n");

                                break;
                            }
                        }

                        addHtmlTailer(out);
                        openHtmlFile();
                    } catch (IOException ex) {
                    }
                }
            });
        writeFile.start();
    }

    //TODO refactor printing to separate worker class
    public PrintWriter prepareHtmlFile() throws IOException {
        //String fileName = strHome+File.separator+"print.html";
        //TODO create a temporary file !!! Security issue
        File temp = new File(backend.getConfig().getHome(), "print.html");
        FileOutputStream stream = new FileOutputStream(temp);
        String enc = Languages.getLanguageENC(backend.getProject().getTgtLang());
        PrintWriter out = new PrintWriter(new OutputStreamWriter(stream, enc), true);

        return out;
    }

    public void openHtmlFile() {
        Thread t = new Thread(new OpenPrintFileThread(backend.getConfig().getHome().getAbsolutePath()));
        t.start();
    }

    //end of printing
    //File | Exit action performed
    public void fileExit_actionPerformed(ActionEvent e) {
        dispose();
    }

    /** *******************************************************************************************
     *
     *  Edit menus
     *
     *  *******************************************************************************************
     */
    void jMenuUndo_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        ViewUndoableEdit ue = undo.pivotUndo();

        if (ue == null) {
            return;
        }

        repaintSelf();

        if (ue != null) {
            setOldViewForUndoEdit(ue);
        }
    }

    void jMenuRedo_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        ViewUndoableEdit ue = undo.pivotRedo();

        if (ue == null) {
            return;
        }

        repaintSelf();

        if (ue != null) {
            setNewViewForUndoEdit(ue);
        }
    }

    void jMenuCut_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSrcTableSel = AlignmentMain.testMain2.tableView.getSelectedRow();

        if ((iSrcTableSel == -1)) {
            return;
        }

        if (backend.getTMData().tmsentences[iSrcTableSel].getTranslationStatus() == TMSentence.APPROVED) {
            return;
        }

        if (activeComponent instanceof PivotTextPane) {
            //logger.finer("active component is PivotTextPane");
            PivotTextPane p = getCurEditingTextPane();

            if (p != null) {
                p.PivotCut();
            }
        } else if (activeComponent instanceof JTextField) {
            activeComponent.cut();
        } else {
            //nothing done.
        }
    }

    void jMenuCopy_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        if (activeComponent instanceof PivotTextPane) {
            PivotTextPane p = getCurEditingTextPane();

            if (p != null) {
                p.PivotCopy();
            }
        } else if (activeComponent instanceof JTextField) {
            activeComponent.copy();
        } else {
            //nothing done.
        }
    }

    void jMenuPaste_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSrcTableSel = AlignmentMain.testMain1.tableView.getSelectedRow();

        if ((iSrcTableSel == -1)) {
            return;
        }

        if ((backend.getTMData()).tmsentences[iSrcTableSel].getTranslationStatus() == TMSentence.APPROVED) {
            return;
        }

        if (activeComponent instanceof PivotTextPane) {
            PivotTextPane p = getCurEditingTextPane();

            if (p != null) {
                p.PivotPaste();
            }

            if (!p.hasFocus()) {
                activeComponent = null;
            }
        } else if (activeComponent instanceof JTextField) {
            activeComponent.paste();

            if (!activeComponent.hasFocus()) {
                activeComponent = null;
            }
        } else {
            //nothing done.
        }
    }

    void jMenuTransfer_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSrcTableSel = AlignmentMain.testMain2.tableView.getSelectedRow();

        if ((iSrcTableSel == -1)) {
            return;
        }

        if ((backend.getTMData()).tmsentences[iSrcTableSel].getTranslationStatus() == TMSentence.APPROVED) {
            return;
        }

        this.jMiniTMFrame.jBtnTransfer_actionPerformed(null);
    }

    void jMenuUntransfer_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSrcTableSel = AlignmentMain.testMain2.tableView.getSelectedRow();

        if ((iSrcTableSel == -1)) {
            return;
        }

        if ((backend.getTMData()).tmsentences[iSrcTableSel].getTranslationStatus() == TMSentence.APPROVED) {
            return;
        }

        this.jMiniTMFrame.jBtnDissolve_actionPerformed(null);
    }

    void jMenuCopySource_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSrcTableSel = AlignmentMain.testMain2.tableView.getSelectedRow();

        if ((iSrcTableSel == -1)) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        if (tmpdata.tmsentences[iSrcTableSel].getTranslationStatus() == TMSentence.APPROVED) {
            return;
        }

        String source = "";
        TMSentence tms = tmpdata.tmsentences[iSrcTableSel];
        String transunitID = tms.getTransUintID();
        int iSearchResult = tmpdata.getGroupTrack().isTransunitIdInGroup(transunitID);

        if (iSearchResult == 0) {
            source = tms.getSource();

            int size = tmpdata.getGroupTrack().getSizeOfOneGroup(transunitID);

            for (int k = 1; k < size; k++) {
                tms = tmpdata.tmsentences[iSrcTableSel + k];
                source += (" " + tms.getSource());
            }
        } else if (iSearchResult == -1) {
            source = tms.getSource();
        }

        String strTemp = source;

        // for undo
        String oldString = tmpdata.tmsentences[iSrcTableSel].getTranslation();

        // bug 4745303++
        int oldttype = (((tmpdata).tmsentences[iSrcTableSel]).getTranslationStatus() * 10) + ((tmpdata).tmsentences[iSrcTableSel]).getTranslationType();
        int newttype = (((tmpdata).tmsentences[iSrcTableSel]).getTranslationStatus() * 10) + TMSentence.USER_TRANSLATION;
        DocumentUndoableEdit edit = new DocumentUndoableEdit(false, e.getActionCommand(), iSrcTableSel, 0, oldString, strTemp, oldttype, newttype);
        MainFrame.undo.addDocumentEdit(edit);

        ((tmpdata).tmsentences[iSrcTableSel]).setTranslation(strTemp);
        ((tmpdata).tmsentences[iSrcTableSel]).setTranslationType(TMSentence.USER_TRANSLATION);

        this.setBHasModified(true);

        AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getCellRect(iSrcTableSel, AlignmentMain.testMain2.showType, true));
        AlignmentMain.testMain2.startEditing();
    }

    void jMenuCopySourceTags_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSrcTableSel = AlignmentMain.testMain2.tableView.getSelectedRow();

        if ((iSrcTableSel == -1)) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        if (tmpdata.tmsentences[iSrcTableSel].getTranslationStatus() == TMSentence.APPROVED) {
            return;
        }

        String source = "";
        String target = "";
        TMSentence tms = tmpdata.tmsentences[iSrcTableSel];
        String transunitID = tms.getTransUintID();
        int iSearchResult = tmpdata.getGroupTrack().isTransunitIdInGroup(transunitID);

        if (iSearchResult == 0) {
            source = tms.getSource();
            target = tms.getTranslation();

            int size = tmpdata.getGroupTrack().getSizeOfOneGroup(transunitID);

            for (int k = 1; k < size; k++) {
                tms = tmpdata.tmsentences[iSrcTableSel + k];
                source += (" " + tms.getSource());
                target += (" " + tms.getTranslation());
            }
        } else if (iSearchResult == -1) {
            source = tms.getSource();
            target = tms.getTranslation();
        }

        PivotText sourceP = new PivotText(source);
        PivotText targetP = new PivotText(target);

        String strTemp = targetP.getMatchStringForSource(sourceP);

        /**
         * for undo
         */
        String oldString = ((tmpdata).tmsentences[iSrcTableSel]).getTranslation();

        // bug 4745303++
        int oldttype = (((tmpdata).tmsentences[iSrcTableSel]).getTranslationStatus() * 10) + ((tmpdata).tmsentences[iSrcTableSel]).getTranslationType();
        int newttype = (((tmpdata).tmsentences[iSrcTableSel]).getTranslationStatus() * 10) + TMSentence.USER_TRANSLATION;
        DocumentUndoableEdit edit = new DocumentUndoableEdit(false, e.getActionCommand(), iSrcTableSel, 0, oldString, strTemp, oldttype, newttype);
        MainFrame.undo.addDocumentEdit(edit);

        ((tmpdata).tmsentences[iSrcTableSel]).setTranslation(strTemp);
        ((tmpdata).tmsentences[iSrcTableSel]).setTranslationType(TMSentence.USER_TRANSLATION);

        this.setBHasModified(true);

        AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getCellRect(iSrcTableSel, AlignmentMain.testMain2.showType, true));
        AlignmentMain.testMain2.startEditing();
    }

    void jMenuClearTarget_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSrcTableSel = AlignmentMain.testMain2.tableView.getSelectedRow();

        if ((iSrcTableSel == -1)) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        if ((tmpdata).tmsentences[iSrcTableSel].getTranslationStatus() == TMSentence.APPROVED) {
            return;
        }

        /**
         * for undo
         */
        String oldString = ((tmpdata).tmsentences[iSrcTableSel]).getTranslation();

        // bug 4745303++
        int oldttype = (((tmpdata).tmsentences[iSrcTableSel]).getTranslationStatus() * 10) + ((tmpdata).tmsentences[iSrcTableSel]).getTranslationType();
        int newttype = (((tmpdata).tmsentences[iSrcTableSel]).getTranslationStatus() * 10) + TMSentence.USER_TRANSLATION;
        DocumentUndoableEdit edit = new DocumentUndoableEdit(false, e.getActionCommand(), iSrcTableSel, 0, oldString, "", oldttype, newttype);
        MainFrame.undo.addDocumentEdit(edit);

        ((tmpdata).tmsentences[iSrcTableSel]).setTranslation("");
        ((tmpdata).tmsentences[iSrcTableSel]).setTranslationType(TMSentence.USER_TRANSLATION);

        this.setBHasModified(true);

        AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getCellRect(iSrcTableSel, AlignmentMain.testMain2.showType, true));
        AlignmentMain.testMain2.startEditing();
    }

    void jMenuMarkSegTrans_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        this.setBHasModified(true);

        TMData tmpdata = backend.getTMData();
        ((tmpdata).tmsentences[iSelRow]).setTranslationStatus(TMSentence.TRANSLATED);
        jBtnUpdateMiniTM_actionPerformed(null);

        AlignmentMain.testMain1.tableView.repaint(AlignmentMain.testMain1.tableView.getCellRect(iSelRow, 0, false));

        this.setMenuState();

        // refresh the Match Window
        //  Potential unnecessary match look up.
        AlignmentMain.testMain2.startEditing();
        this.repaintSelf();
    }

    void jMenuMarkSegUnTrans_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        this.setBHasModified(true);

        backend.getTMData().tmsentences[iSelRow].setTranslationStatus(TMSentence.UNTRANSLATED);
        AlignmentMain.testMain1.tableView.repaint(AlignmentMain.testMain1.tableView.getCellRect(iSelRow, 0, false));

        MainFrame.myMatchstatusBar.setStatus(0);
        AlignmentMain.testMain2.startEditing();

        this.setMenuState();

        this.repaintSelf();
    }

    void jMenuMarkSegReviewed_actionPerformed(ActionEvent e) {
        setSegmentReviewStatus(true);
    }

    void jMenuMarkSegRejecteded_actionPerformed(ActionEvent e) {
        setSegmentReviewStatus(false);
    }

    public void setSegmentReviewStatus(boolean segApproved) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        this.setBHasModified(true);

        int iStatus;

        if (segApproved) {
            iStatus = TMSentence.APPROVED;
        } else {
            iStatus = TMSentence.REJECTED;
        }

        ((backend.getTMData()).tmsentences[iSelRow]).setTranslationStatus(iStatus);
        AlignmentMain.testMain1.tableView.repaint(AlignmentMain.testMain1.tableView.getCellRect(iSelRow, 0, false));

        this.setMenuState();

        MainFrame.myMatchstatusBar.setStatus(iStatus);
        this.repaintSelf();
    }

    void jMenuMarkExactMatchTrans_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        this.setBHasModified(true);

        TMData tmpdata = backend.getTMData();

        for (int k = 0; k < tmpdata.tmsentences.length; k++) {
            if (((tmpdata).tmsentences[k]).getTranslationType() == 1) {
                ((tmpdata).tmsentences[k]).setTranslationStatus(TMSentence.TRANSLATED);
            } else {
                continue;
            }
        }

        repaintSelf();

        this.setMenuState();
    }

    void jMenuMarkExactMatchReviewed_actionPerformed(ActionEvent e) {
        setExactMatchSegmentReviewStatus(true);
    }

    public void setExactMatchSegmentReviewStatus(boolean approved) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        this.setBHasModified(true);

        TMData tmpdata = backend.getTMData();

        for (int k = 0; k < tmpdata.tmsentences.length; k++) {
            TMSentence seg = tmpdata.tmsentences[k];

            if (seg.getTranslationType() == TMSentence.EXACT_TRANSLATION) {
                int iNewStatus = approved ? TMSentence.APPROVED : TMSentence.REJECTED;
                seg.setTranslationStatus(iNewStatus);
            }
        }

        AlignmentMain.testMain1.tableView.invalidate();
        AlignmentMain.testMain1.tableView.repaint();

        this.setMenuState();
    }

    void jMenuMarkAllSegTrans_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        this.setBHasModified(true);

        int updateFlagForCurSeg = -1;
        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        TMData tmpdata = backend.getTMData();

        for (int k = 0; k < (tmpdata).tmsentences.length; k++) {
            if (((tmpdata).tmsentences[k]).getTranslationStatus() == TMSentence.TRANSLATED) {
                continue;
            }

            String transId = backend.getConfig().getTranslatorID();

            tmpdata.tmsentences[k].setTranslationStatus(TMSentence.TRANSLATED);

            if (k == iSelRow) {
                updateFlagForCurSeg = tmpdata.tmsentences[k].updateMiniTM(transId, k);
            } else {
                tmpdata.tmsentences[k].updateMiniTM(transId, k);
            }
        }

        AlignmentMain.testMain1.tableView.repaint();

        this.setMenuState();

        if (updateFlagForCurSeg == 1) {
            Vector v = new Vector();
            String src = tmpdata.tmsentences[iSelRow].getSource();
            String trans = tmpdata.tmsentences[iSelRow].getTranslation();

            String targetLan = backend.getProject().getTgtLang();

            if ("ZH".equals(targetLan) || "ZT".equals(targetLan) || "JA".equals(targetLan)) {
            } else {
                if (src.endsWith(" ")) {
                    if (!trans.endsWith(" ")) {
                        trans += " ";
                    }
                }
            }

            v.addElement(new PivotText(src));
            v.addElement(new PivotText(src));
            v.addElement(new PivotText(trans));

            MiniTMFrame.jMiniTM.data.insertElementAt(v, 0);
            MiniTMTableFrame.diff = null;
            MiniTMFrame.jMiniTM.matchesObject = tmpdata.tmsentences[iSelRow].getMatches(false);
            MiniTMFrame.jMiniTM.tableView.tableChanged(new TableModelEvent(MiniTMFrame.jMiniTM.dataModel, 0, 0, 0, TableModelEvent.INSERT));
            MiniTMFrame.jMiniTM.tableView.clearSelection();
            MiniTMFrame.jMiniTM.tableView.setRowSelectionInterval(0, 0);
            MiniTMFrame.jMiniTM.tableView.repaint();
        } else if (updateFlagForCurSeg == 0) {
            Vector v = new Vector();
            String src = tmpdata.tmsentences[iSelRow].getSource();
            String trans = tmpdata.tmsentences[iSelRow].getTranslation();

            String targetLan = backend.getProject().getTgtLang();

            if ("ZH".equals(targetLan) || "ZT".equals(targetLan) || "JA".equals(targetLan)) {
            } else {
                if (src.endsWith(" ")) {
                    if (!trans.endsWith(" ")) {
                        trans += " ";
                    }
                }
            }

            v.addElement(new PivotText(src));
            v.addElement(new PivotText(src));
            v.addElement(new PivotText(trans));

            MiniTMFrame.jMiniTM.data.set(0, v);
            MiniTMTableFrame.diff = null;

            MiniTMFrame.jMiniTM.matchesObject = tmpdata.tmsentences[iSelRow].getMatches(false);
            MiniTMFrame.jMiniTM.tableView.tableChanged(new TableModelEvent(MiniTMFrame.jMiniTM.dataModel, 0, 0, 0, TableModelEvent.UPDATE));
            MiniTMFrame.jMiniTM.tableView.clearSelection();
            MiniTMFrame.jMiniTM.tableView.setRowSelectionInterval(0, 0);
            MiniTMFrame.jMiniTM.tableView.repaint();
        }
    }

    void jMenuMarkAllSegReviewed_actionPerformed(ActionEvent e) {
        setReviewStatusOnAllSegs(true);
    }

    public void setReviewStatusOnAllSegs(boolean approved) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        if (!backend.hasCurrentFile()) {
            return;
        }

        String warningMessage = "";

        if (approved) {
            warningMessage = "Do you want to mark all translated segments as Approved?";
        } else {
            warningMessage = "Do you want to mark all translated segments as Rejected?";
        }

        int iRet = JOptionPane.showConfirmDialog(this, warningMessage, "Information", JOptionPane.YES_NO_OPTION);

        if ((iRet == JOptionPane.NO_OPTION) || (iRet == -1)) {
            return;
        }

        this.setBHasModified(true);

        TMData tmpdata = backend.getTMData();

        for (int k = 0; k < (tmpdata).tmsentences.length; k++) {
            TMSentence seg = (tmpdata).tmsentences[k];

            if (seg.getTranslationStatus() == TMSentence.TRANSLATED) {
                int newStatus = approved ? TMSentence.APPROVED : TMSentence.REJECTED;
                seg.setTranslationStatus(newStatus);
            }
        }

        AlignmentMain.testMain1.tableView.invalidate();
        AlignmentMain.testMain1.tableView.repaint();

        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        Match[] vTemp = (tmpdata).tmsentences[iSelRow].getMatches(true);

        if ((vTemp == null) || (vTemp.length == 0)) {
            return;
        }

        int CurRow = MiniTMFrame.jMiniTM.tableView.getSelectedRow();

        if ((CurRow == -1) || (CurRow >= vTemp.length)) {
            return;
        }

        int iTransStatus = (tmpdata).tmsentences[iSelRow].getTranslationStatus();
        MainFrame.myMatchstatusBar.setStatus(iTransStatus);
    }

    void jMenuMarkTranslatedAndNext_actionPerformed(ActionEvent e) {
        int i = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (i == -1) {
            return;
        }

        jMenuMarkSegTrans_actionPerformed(null);

        TMData tmpdata = backend.getTMData();

        // bug 4713261
        int nextTMIndex = (tmpdata).getIndexOfTM(i, 1, 0);

        if (nextTMIndex == -1) {
            nextTMIndex = (tmpdata).getIndexOfTM(-1, 1, 0); //research from the first row

            if (nextTMIndex == -1) {
                return;
            }
        }

        boolean hasAutoTranslated = false;

        do {
            if (nextTMIndex == -1) {
                break;
            }

            hasAutoTranslated = (updateStatus(nextTMIndex) == 1) ? true : false;

            navigateTo(nextTMIndex);

            if (hasAutoTranslated) {
                nextTMIndex = (tmpdata).getIndexOfTM(nextTMIndex, 1, 0);

                // bug 4713261
                if (nextTMIndex == -1) {
                    nextTMIndex = (tmpdata).getIndexOfTM(-1, 1, 0);
                }

                continue;
            }

            hasAutoTranslated = (tmpdata).tmsentences[nextTMIndex].isAutoTranslated();

            // bug 4713261
            if (hasAutoTranslated) {
                nextTMIndex = (tmpdata).getIndexOfTM(nextTMIndex, 1, 0);

                if (nextTMIndex == -1) {
                    nextTMIndex = (tmpdata).getIndexOfTM(-1, 1, 0);
                }
            }
        } while (hasAutoTranslated);

        this.setMenuState();
        this.repaintSelf();
    }

    void jMenuMarkReviewedAndNext_actionPerformed(ActionEvent e) {
        setReviewStatusAndGotoNext(true);
    }

    public void setReviewStatusAndGotoNext(boolean approved) {
        int i = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (i == -1) {
            return;
        }

        setSegmentReviewStatus(approved);

        if (i == (AlignmentMain.testMain1.tableView.getRowCount() - 1)) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        int nextTMIndex = (tmpdata).getIndexOfTM(i, 5, 0);
        navigateTo(nextTMIndex);

        this.setMenuState();
    }

    public static int updateStatus(int row) {
        TMData tmpdata = Backend.instance().getTMData();
        int status = tmpdata.tmsentences[row].getTranslationStatus();

        if (status == 0) {
            String source = tmpdata.tmsentences[row].getSource().trim();

            PivotText src = new PivotText(source);
            boolean srcContaionTagsOnly = true;
OUTER1: 
            for (int i = 0; i < src.elements().length; i++) {
                if (!(src.elements()[i]).getFlag()) {
                    String content = (src.elements()[i]).getContent();

                    for (int j = 0; j < content.length(); j++) {
                        char c = content.charAt(j);

                        if ((c != ' ') && (c != '\n') && (c != '\t') && (c != '\r')) {
                            srcContaionTagsOnly = false;

                            break OUTER1;
                        }
                    }
                }
            }

            if (srcContaionTagsOnly) {
                String target = tmpdata.tmsentences[row].getTranslation().trim();
                PivotText tgt = new PivotText(target);
                boolean tgtContaionTagsOnly = true;
OUTER2: 
                for (int i = 0; i < tgt.elements().length; i++) {
                    if (!(tgt.elements()[i]).getFlag() && (i < src.elements().length)) {
                        String content = (src.elements()[i]).getContent();

                        for (int j = 0; j < content.length(); j++) {
                            char c = content.charAt(j);

                            if ((c != ' ') && (c != '\n') && (c != '\t') && (c != '\r')) {
                                tgtContaionTagsOnly = false;

                                break OUTER2;
                            }
                        }
                    }
                }

                if (tgtContaionTagsOnly) {
                    tmpdata.tmsentences[row].setTranslationStatus(TMSentence.TRANSLATED);
                }
            }
        }

        return tmpdata.tmsentences[row].getTranslationStatus();
    }

    void jMenuAddComment_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        if (tmpdata.tmsentences[iSelRow].hasCommented() == 1) {
            return;
        }

        String transId = backend.getConfig().getTranslatorID();

        if (commentdlg == null) {
            commentdlg = new JCommentDialog(this);
            commentdlg.setTranslatorId(transId);
        }

        String key = tmpdata.tmsentences[iSelRow].getTransUintID();
        String comment = tmpdata.getCommentsTrack().getComment(key);

        commentdlg.init(JCommentDialog.COMMENT_SEGMENT, comment, iSelRow);

        tmpdata.getCommentsTrack().setComment(key, commentdlg.getComment());

        AlignmentMain.testMain2.refreshIcon();
        this.setMenuState();
    }

    void jMenuEditComment_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        if (tmpdata.tmsentences[iSelRow].hasCommented() == 0) {
            return;
        }

        if (commentdlg == null) {
            commentdlg = new JCommentDialog(this);
        }

        String key = tmpdata.tmsentences[iSelRow].getTransUintID();
        String comment = tmpdata.getCommentsTrack().getComment(key);

        commentdlg.init(JCommentDialog.COMMENT_SEGMENT, comment, iSelRow);
        tmpdata.getCommentsTrack().setComment(key, commentdlg.getComment());

        AlignmentMain.testMain2.refreshIcon();
        this.setMenuState();
    }

    void jMenuDeleteComment_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        if (tmpdata.tmsentences[iSelRow].hasCommented() == 0) {
            return;
        }

        Toolkit.getDefaultToolkit().beep();

        if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this comment?, ", "Delete Comment", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
            return;
        }

        tmpdata.tmsentences[iSelRow].removeComment();
        AlignmentMain.testMain2.tableView.repaint();
        this.setBHasModified(true);
        this.setMenuState();
    }

    void jMenuFileComment_actionPerformed(ActionEvent e) {
        if (commentdlg == null) {
            commentdlg = new JCommentDialog(this);
        }

        TMData tmpdata = backend.getTMData();

        String key = "header";
        String comment = tmpdata.getCommentsTrack().getComment(key);

        commentdlg.init(JCommentDialog.COMMENT_FILE, comment, -1);

        tmpdata.getCommentsTrack().setComment(key, commentdlg.getComment());
    }

    void jMenuSearch_actionPerformed(ActionEvent e) {
        if (findDlg == null) {
            findDlg = new JDialog(this, "Search/replace ...", false);
            findDlg.getContentPane().setLayout(new BorderLayout());

            FindAndReplacePanel findPanel = new FindAndReplacePanel(findDlg, backend);

            findDlg.getContentPane().add(findPanel, "Center");
            findDlg.setSize(460, 300);
            findDlg.setResizable(true);
        }

        findDlg.setLocationRelativeTo(this);
        findDlg.setVisible(true);
        ((FindAndReplacePanel)findDlg.getContentPane().getComponent(0)).init();
    }

    /** *******************************************************************************************
     *
     *  View menus
     *
     *  *******************************************************************************************
     */
    void jCBMenuFile_actionPerformed(ActionEvent e) {
        Configuration cfg = backend.getConfig();
        cfg.setBSelectFlag(0, ((JCheckBoxMenuItem)e.getSource()).isSelected());
        AdjustTooBar();
    }

    void jCBMenuEditing_actionPerformed(ActionEvent e) {
        Configuration cfg = backend.getConfig();
        cfg.setBSelectFlag(1, ((JCheckBoxMenuItem)e.getSource()).isSelected());
        AdjustTooBar();
    }

    void jCBMenuSearch_actionPerformed(ActionEvent e) {
        Configuration cfg = backend.getConfig();
        cfg.setBSelectFlag(2, ((JCheckBoxMenuItem)e.getSource()).isSelected());
        AdjustTooBar();
    }

    void jCBMenuNavi_actionPerformed(ActionEvent e) {
        Configuration cfg = backend.getConfig();
        cfg.setBSelectFlag(3, ((JCheckBoxMenuItem)e.getSource()).isSelected());
        AdjustTooBar();
    }

    void jCBMenuHelp_actionPerformed(ActionEvent e) {
        Configuration cfg = backend.getConfig();
        cfg.setBSelectFlag(4, ((JCheckBoxMenuItem)e.getSource()).isSelected());
        AdjustTooBar();
    }

    public void AdjustTooBar() {
        Configuration cfg = backend.getConfig();

        toolBar.removeAll();
        this.getRootPane().validate();
        this.getRootPane().repaint();

        if ((cfg.getBSelectFlag(0) == false) && (cfg.getBSelectFlag(1) == false) && (cfg.getBSelectFlag(2) == false) && (cfg.getBSelectFlag(3) == false) && (cfg.getBSelectFlag(4) == false)) {
            jMenuItemShowAll.setEnabled(true);
            jMenuItemHideAll.setEnabled(false);
        } else if (cfg.getBSelectFlag(0) && cfg.getBSelectFlag(1) && cfg.getBSelectFlag(2) && cfg.getBSelectFlag(3) && cfg.getBSelectFlag(4)) {
            jMenuItemShowAll.setEnabled(false);
            jMenuItemHideAll.setEnabled(true);
        } else if (cfg.getBSelectFlag(0) || cfg.getBSelectFlag(1) || cfg.getBSelectFlag(2) || cfg.getBSelectFlag(3) || cfg.getBSelectFlag(4)) {
            this.getContentPane().add(toolBar, BorderLayout.NORTH);
            jMenuItemShowAll.setEnabled(true);
            jMenuItemHideAll.setEnabled(true);
        }

        for (int i = 0; i <= 4; i++) {
            switch (i) {
            case 0:

                if (cfg.getBSelectFlag(0) == true) {
                    toolBar.add(jBtnOpen);
                    toolBar.add(jBtnSave);
                    toolBar.addSeparator();
                }

                break;

            case 1:

                if (cfg.getBSelectFlag(1) == true) {
                    toolBar.add(jBtnUndo);
                    toolBar.add(jBtnRedo);
                    toolBar.add(jBtnCopy);
                    toolBar.add(jBtnPaste);
                    toolBar.add(jBtnCut);
                    toolBar.addSeparator();
                    toolBar.add(jBtnSearch);
                    toolBar.addSeparator();
                }

                break;

            case 2:

                if (cfg.getBSelectFlag(2) == true) {
                    toolBar.add(jComboBoxSearch);
                    toolBar.add(jBtnNext);
                    toolBar.add(jBtnPrev);
                    toolBar.add(jBtnMarkAsTransAndGoToNextUnTrans);
                    toolBar.add(jBtnMarkAsApprovedAndGoToNextTrans);
                    toolBar.add(jBtnMarkAsRejectedAndGoToNextTrans);
                    toolBar.addSeparator();
                }

                break;

            case 3:

                if (cfg.getBSelectFlag(3) == true) {
                    toolBar.add(jBtnTagVerify);
                    toolBar.add(jBtnSpellCheck);
                    toolBar.add(jBtnUpdateMiniTM);

                    if (boolOnline) {
                        if (boolReview) {
                            toolBar.add(this.jbtnApproveFile);
                            toolBar.add(this.jbtnRejectFile);
                        } else {
                            toolBar.add(jBtnReturnToPortal);
                        }
                    }

                    toolBar.addSeparator();
                }

                break;

            case 4:

                if (cfg.getBSelectFlag(4) == true) {
                    toolBar.add(jBtnHelp);
                    toolBar.addSeparator();
                }

                break;
            }
        }

        this.getRootPane().validate();
        this.getRootPane().repaint();
    }

    void jMenuItemHideAll_actionPerformed(ActionEvent e) {
        Configuration cfg = backend.getConfig();

        jMenuItemShowAll.setEnabled(true);
        jMenuItemHideAll.setEnabled(false);
        this.contentPane.remove(toolBar);

        for (int i = 0; i < cfg.getSelectedFlagsCount(); i++) {
            cfg.setBSelectFlag(i, false);
        }

        AdjustTooBar();
        jCBMenuFile.setSelected(false);
        jCBMenuEditing.setSelected(false);
        jCBMenuNavi.setSelected(false);
        jCBMenuSearch.setSelected(false);
        jCBMenuHelp.setSelected(false);
        this.contentPane.validate();
        this.contentPane.repaint();
    }

    void jMenuItemShowAll_actionPerformed(ActionEvent e) {
        Configuration cfg = backend.getConfig();

        jMenuItemShowAll.setEnabled(false);
        jMenuItemHideAll.setEnabled(true);
        this.contentPane.add(toolBar, BorderLayout.NORTH);

        for (int i = 0; i < cfg.getSelectedFlagsCount(); i++) {
            cfg.setBSelectFlag(i, true);
        }

        AdjustTooBar();
        jCBMenuFile.setSelected(true);
        jCBMenuEditing.setSelected(true);
        jCBMenuNavi.setSelected(true);
        jCBMenuSearch.setSelected(true);
        jCBMenuHelp.setSelected(true);

        this.contentPane.validate();
        this.contentPane.repaint();
    }

    void jCBMenuMatchWindow_actionPerformed(ActionEvent e) {
        if (jCBMenuMatchWindow.isSelected()) {
            backend.getConfig().setBFlagMatchWindow(true);
            dDevision = 0.75;
            MySplitPane.setBottomComponent(jMiniTMFrame);
            MySplitPane.setDividerLocation(dDevision);
            MySplitPane.setDividerSize(10);
            jCBMenuAttributes.setEnabled(true);

            this.getRootPane().repaint();
        } else {
            backend.getConfig().setBFlagMatchWindow(false);
            dDevision = 1.0;
            MySplitPane.setDividerLocation(dDevision);
            MySplitPane.setDividerSize(0);
            MySplitPane.setBottomComponent(null);
            jCBMenuAttributes.setEnabled(false);

            this.getRootPane().repaint();
        }
    }

    void jCBMenuAttributes_actionPerformed(ActionEvent e) {
        boolean bFlagAttributes = jCBMenuAttributes.isSelected();
        backend.getConfig().setBFlagAttributes(bFlagAttributes);
        this.jMiniTMFrame.setAttributePanelVisible(bFlagAttributes);
    }

    void jCBMenuSourceContext_actionPerformed(ActionEvent e) {
        boolean bFlagSourceContext = jCBMenuSourceContext.isSelected();
        backend.getConfig().setBFlagSourceContext(bFlagSourceContext);
        this.jSourceContextFrame.setVisible(bFlagSourceContext);
    }

    void jCBMenuSearchBar_actionPerformed(ActionEvent e) {
        boolean selected = jCBMenuSearchBar.isSelected();

        backend.getConfig().setBFlagSearchBar(selected);

        if (selected) {
            jContrastFrame.tmLeftPanel.add(jContrastFrame.tmLeftPanel.jToolBarPanel, BorderLayout.NORTH);
            jContrastFrame.tmRightPanel.add(jContrastFrame.tmRightPanel.jToolBarPanel, BorderLayout.NORTH);
        } else {
            jContrastFrame.tmLeftPanel.remove(jContrastFrame.tmLeftPanel.jToolBarPanel);
            jContrastFrame.tmRightPanel.remove(jContrastFrame.tmRightPanel.jToolBarPanel);
        }

        this.validate();
        this.repaint();
    }

    void jMenuColor_actionPerformed(ActionEvent e) {
        ColorSetFrame MyColorSetFrame = new ColorSetFrame();
    }

    void jMenuHor_actionPerformed(ActionEvent e) {
        boolean on = backend.getConfig().isBFlagViewSwitch();

        if (!on) {
            jContrastFrame.setLayout(new GridLayout(1, 2, 10, 0));
            jContrastFrame.doLayout();
            jContrastFrame.repaint();
            repaintSelf();
            MySplitPane.setDividerSize(MySplitPane.getDividerSize() - 1);

            backend.getConfig().setBFlagViewSwitch(true);

            jMenuHor.setEnabled(false);
            jMenuVer.setEnabled(true);
        }
    }

    void jMenuVer_actionPerformed(ActionEvent e) {
        boolean on = backend.getConfig().isBFlagViewSwitch();

        if (on) {
            jContrastFrame.setLayout(new GridLayout(2, 1, 0, 10));
            jContrastFrame.invalidate();
            jContrastFrame.repaint();
            MySplitPane.setDividerSize(MySplitPane.getDividerSize() + 1);
            repaintSelf();

            backend.getConfig().setBFlagViewSwitch(false);

            jMenuHor.setEnabled(true);
            jMenuVer.setEnabled(false);
        }
    }

    /** *******************************************************************************************
     *
     *  Navigation menus
     *
     *  *******************************************************************************************
     */
    public void itemStateChanged(ItemEvent e) {
        JComboBox c = (JComboBox)e.getSource();

        if (c.getSelectedItem() == null) {
            return;
        }

        jMenuNaviNextOption.setText("Next " + (String)c.getSelectedItem());
        jMenuNaviPrevOption.setText("Previous " + (String)c.getSelectedItem());

        // initial navigation
        iStartRow = -1;
        bNextDirection = false;
        bPrevDirection = false;
    }

    void jMenuNaviNextOption_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int start = AlignmentMain.testMain1.tableView.getSelectedRow();

        TMData tmpdata = backend.getTMData();

        int index = ((JComboBox)jComboBoxSearch).getSelectedIndex();

        if (index == 0) { // next segment
            start++;

            if (start < tmpdata.tmsentences.length) {
                String transunitID = tmpdata.tmsentences[start].getTransUintID();
                int inGroupResult = tmpdata.getGroupTrack().isTransunitIdInGroup(transunitID);

                if (inGroupResult == 1) {
                    start++;
                }

                navigateTo(start);
            }

            return;
        }

        int type = index - 1;
        navigateNextOption(start, type);
    }

    private void navigateNextOption(int start, int type) {
        TMData tmpdata = backend.getTMData();

        int nextTMIndex = -1;

        if ((iStartRow != -1) && (start <= iStartRow)) {
            nextTMIndex = (tmpdata).getIndexOfTM(start, iStartRow, type, 0, true);

            if ((nextTMIndex == -1) || (nextTMIndex > iStartRow)) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "The editor has finished searching from the top of the file!\nNo segments found!", "Navigation", JOptionPane.INFORMATION_MESSAGE);

                // initial navigation
                iStartRow = -1;
                bNextDirection = false;
                bPrevDirection = false;

                return;
            }
        } else {
            nextTMIndex = (tmpdata).getIndexOfTM(start, type, 0);
        }

        if (iStartRow == -1) {
            iStartRow = start;
        }

        bNextDirection = true;

        if (bPrevDirection) {
            iStartRow = start; // reset the intial value
            bPrevDirection = false;
        }

        if (nextTMIndex == -1) {
            if (iStartRow != 0) {
                Toolkit.getDefaultToolkit().beep();

                int iRet = JOptionPane.showConfirmDialog(this, "The editor has searched to the bottom of the file.\nDo you want to continue searching from the top of the file?", "Navigation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                switch (iRet) {
                case JOptionPane.YES_OPTION:
                    nextTMIndex = (tmpdata).getIndexOfTM(0, iStartRow, type, 0, false);

                    if (nextTMIndex == -1) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(this, "The editor has finished searching from the top of the file!\nNo segments found!", "Navigation", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        navigateTo(nextTMIndex);

                        return;
                    }

                case JOptionPane.NO_OPTION:
                    //
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "The editor has searched to the bottom of the file!\nNo segments found!", "Navigation", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            navigateTo(nextTMIndex);

            return;
        }

        // initial navigation
        iStartRow = -1;
        bNextDirection = false;
        bPrevDirection = false;
    }

    void jMenuNaviPrevOption_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int start = AlignmentMain.testMain1.tableView.getSelectedRow();

        TMData tmpdata = backend.getTMData();

        int index = ((JComboBox)jComboBoxSearch).getSelectedIndex();

        if (index == 0) { // previous segment
            start--;

            if (start <= -1) {
                return;
            }

            String transunitID = tmpdata.tmsentences[start].getTransUintID();
            int inGroupResult = tmpdata.getGroupTrack().isTransunitIdInGroup(transunitID);

            if (inGroupResult == 1) {
                start--;
            }

            navigateTo(start);

            return;
        }

        int type = index - 1;
        navigatePrevOption(start, type);
    }

    private void navigatePrevOption(int start, int type) {
        int nextTMIndex = -1;

        TMData tmpdata = backend.getTMData();

        if ((iStartRow != -1) && (start >= iStartRow)) {
            nextTMIndex = (tmpdata).getIndexOfTM(start, iStartRow, type, 1, true);

            if ((nextTMIndex == -1) || (nextTMIndex < iStartRow)) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "The editor has finished searching from the bottom of the file!\nNo segments found!", "Navigation", JOptionPane.INFORMATION_MESSAGE);

                // initial navigation
                iStartRow = -1;
                bNextDirection = false;
                bPrevDirection = false;

                return;
            }
        } else {
            nextTMIndex = (tmpdata).getIndexOfTM(start, type, 1);
        }

        if (iStartRow == -1) {
            iStartRow = start;
        }

        bPrevDirection = true;

        if (bNextDirection) {
            iStartRow = start; // reset the intial value
            bNextDirection = false;
        }

        if (nextTMIndex == -1) {
            if (iStartRow != ((tmpdata).getSize() - 1)) {
                Toolkit.getDefaultToolkit().beep();

                int iRet = JOptionPane.showConfirmDialog(this, "The editor has searched to the top of the file.\nDo you want to continue searching from the bottom of the file?", "Navigation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                switch (iRet) {
                case JOptionPane.YES_OPTION:
                    nextTMIndex = (tmpdata).getIndexOfTM((tmpdata.getSize() - 1), iStartRow, type, 1, false);

                    if (nextTMIndex == -1) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(this, "The editor has finished searching from the bottom of the file!\nNo segments found!", "Navigation", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        navigateTo(nextTMIndex);

                        return;
                    }

                case JOptionPane.NO_OPTION:
                    //
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "The editor has searched to the top of the file!\nNo segments found!", "Navigation", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            navigateTo(nextTMIndex);

            return;
        }

        // initial navigation
        iStartRow = -1;
        bNextDirection = false;
        bPrevDirection = false;
    }

    void jMenuNaviNextMultiple100MatchOption_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int start = AlignmentMain.testMain1.tableView.getSelectedRow();

        //        int type = 8;   //  Wrong type code
        int type = 9;
        navigateNextOption(start, type);
    }

    void jMenuNaviPrevMultiple100MatchOption_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        int start = AlignmentMain.testMain1.tableView.getSelectedRow();

        //        int type = 8;   //  Wrong type code
        int type = 9;
        navigatePrevOption(start, type);
    }

    void jMenuNaviGotoTop_actionPerformed(ActionEvent e) {
        navigateTo(0);
    }

    void jMenuNaviGotoBottom_actionPerformed(ActionEvent e) {
        navigateTo(AlignmentMain.testMain1.tableView.getRowCount() - 1);
    }

    // bug 4744603
    void jMenuNaviPageUp_actionPerformed(ActionEvent e) {
        if (MainFrame.curTable == AlignmentMain.testMain1.tableView) {
            AlignmentMain.testMain1.pageUp();
        } else if (MainFrame.curTable == AlignmentMain.testMain2.tableView) {
            AlignmentMain.testMain2.pageUp();
        }
    }

    void jMenuNaviPageDown_actionPerformed(ActionEvent e) {
        if (MainFrame.curTable == AlignmentMain.testMain1.tableView) {
            AlignmentMain.testMain1.pageDown();
        } else if (MainFrame.curTable == AlignmentMain.testMain2.tableView) {
            AlignmentMain.testMain2.pageDown();
        }
    }

    private boolean ValidateInputString(String strInput) {
        String strErrorMsg;

        for (int i = 0; i < strInput.length(); i++) {
            if (Character.isDigit(strInput.charAt(i))) {
                continue;
            } else {
                strErrorMsg = "Please specify a segment number!";
                JOptionPane.showMessageDialog(this, strErrorMsg, "Error Input", JOptionPane.OK_OPTION);

                return false;
            }
        }

        return true;
    }

    void jMenuNaviGoto_actionPerformed(ActionEvent e) {
        if (!backend.hasCurrentFile()) {
            return;
        }

        String strUserInput;
        String strErrorMsg;
        strUserInput = JOptionPane.showInputDialog(this, "Enter segment number:", "Go To Segment", JOptionPane.OK_CANCEL_OPTION);

        if ((strUserInput == null) || (strUserInput.length() == 0)) {
            return;
        }

        while (!ValidateInputString(strUserInput)) {
            strUserInput = JOptionPane.showInputDialog(this, "Enter segment number:", "Go To segment", JOptionPane.OK_CANCEL_OPTION);

            if ((strUserInput == null) || (strUserInput.length() == 0)) {
                return;
            }
        }

        TMData tmpdata = backend.getTMData();

        int iGotoNumber = Integer.parseInt(strUserInput) - 1;

        while ((iGotoNumber < 0) || (iGotoNumber >= tmpdata.getSize())) {
            strErrorMsg = "The specified number should be between 1 and " + tmpdata.getSize();
            JOptionPane.showMessageDialog(this, strErrorMsg, "Error Input", JOptionPane.OK_OPTION);
            strUserInput = JOptionPane.showInputDialog(this, "Enter segment number:", "Go To Segment", JOptionPane.OK_CANCEL_OPTION);

            if ((strUserInput == null) || (strUserInput.length() == 0)) {
                return;
            }

            iGotoNumber = Integer.parseInt(strUserInput) - 1;
        }

        navigateTo(iGotoNumber);

        // update the context information window.
        String transUnit = tmpdata.tmsentences[iGotoNumber].getTransUintID();
        Map contextInfo = tmpdata.getSourceContextTrack().getContext(transUnit);

        if (contextInfo != null) {
            MainFrame.jSourceContextFrame.setContextInformation(contextInfo);
        } else {
            MainFrame.jSourceContextFrame.setContextInformation(new HashMap());
        }
    }

    public static void navigateTo(int index) {
        try {
            if (index == -1) {
                return;
            }

            if (curTable == null) {
                AlignmentMain.testMain1.navigateTo(index);
            } else if (curTable == AlignmentMain.testMain1.tableView) {
                AlignmentMain.testMain1.navigateTo(index);
            } else {
                AlignmentMain.testMain2.navigateTo(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** *******************************************************************************************
     *
     *  Tools menus
     *
     *  *******************************************************************************************
     */

    // bug 4729054
    void jMenuTagVerify_actionPerformed(ActionEvent e) {
        boolean oneError = false;
        TMData tmpdata = backend.getTMData();

        int len = ((tmpdata).tmsentences).length;

        if (len == 0) {
            return;
        }

        int curLoc = AlignmentMain.testMain1.tableView.getSelectedRow();
        int i = curLoc;

        //bug 4729057
        String[] options = { 
            
            // bug 4737973
            "Correct Manually", 
            
            //"Fix",
            "Ignore and Continue"};
        JOptionPane optionPane = new JOptionPane("", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
        JDialog jdlg = optionPane.createDialog(this, "Tag Verification");

        PivotText sourceP;
        PivotText targetP;
        String tmpSrc = "";
        String tmpTrg = "";

        while (true) {
            TMSentence tms = tmpdata.tmsentences[i];

            //check if in the group
            String transunitID = tms.getTransUintID();
            int iSearchResult = tmpdata.getGroupTrack().isTransunitIdInGroup(transunitID);

            if (iSearchResult != -1) {
                if (iSearchResult == 1) {
                    i++;

                    continue;
                }

                tmpSrc = tms.getSource();
                tmpTrg = tms.getTranslation();

                int size = tmpdata.getGroupTrack().getSizeOfOneGroup(transunitID);

                for (int k = 1; k < size; k++) {
                    tms = tmpdata.tmsentences[i + k];
                    tmpSrc += (" " + tms.getSource());
                    tmpTrg += (" " + tms.getTranslation());
                }
            } else {
                tmpSrc = tms.getSource();
                tmpTrg = tms.getTranslation();
            }

            sourceP = new PivotText(tmpSrc);
            targetP = new PivotText(tmpTrg);

            //  Determine if we have a match: delegate this to PivotText
            int p = 0;
            boolean verify = backend.getConfig().isBFlagTagVerifyIgnoreOrder();

            if (verify) {
                p = sourceP.matchIgnoreOrder(targetP);
            } else {
                p = sourceP.match(targetP);
            }

            if (p != -1) { //do not match
                oneError = true;
                AlignmentMain.testMain1.navigateTo(i);

                Object[] message = new Object[1];
                String informationString = "Tags in segment no. " + (i + 1) + " do not match!";
                JTextPane textArea = new JTextPane();
                textArea.setSize(350, 60);
                textArea.setFont(dlgFont);
                textArea.setBackground(new Color(204, 204, 204));
                textArea.setEditable(false);
                textArea.setText(informationString);

                //JScrollPane scr = new JScrollPane(textArea);
                //scr.setSize(new Dimension(textArea.getPreferredSize()));
                //scr.setBorder(null);
                message[0] = textArea;

                optionPane.setMessage(message);
                jdlg.setSize(350, 120);
                jdlg.show();

                Object selectedValue = optionPane.getValue();

                if (selectedValue == null) {
                    break;
                }

                int counter = 0;
                int maxCounter = options.length;

                for (; counter < maxCounter; counter++) {
                    if (options[counter].equals(selectedValue)) {
                        break;
                    }
                }

                switch (counter) {
                case 0: // goto it
                    return;

                case 1: // next
                    sourceP = null;
                    targetP = null;

                    break;

                case -1: //close this dlg
                    return;
                }
            } else {
                sourceP = null;
                targetP = null;

                //continue;
            }

            i++;

            if (i == len) { //reach the bottom

                if (curLoc > 0) {
                    Toolkit.getDefaultToolkit().beep();

                    int resume = JOptionPane.showConfirmDialog(this.getRootPane(), "Tag Verification has searched to the bottom of the segments. Do you want \nto continue at the begining?", "Tag Verification", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (resume == JOptionPane.OK_OPTION) {
                        i = 0;
                    } else {
                        break;
                    }
                } else {
                    i = 0;
                }
            }

            if (i == curLoc) { //circle to the segment which makes tag verification

                if (oneError) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(this.getRootPane(), "Finished Tag Verification!", "Tag Verification", JOptionPane.INFORMATION_MESSAGE);
                }

                break;
            }
        }

        if (!oneError && (i == curLoc)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "All tags in the source language are present in the target language!", "Tag Verification", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    void jMenuSpellCheck_actionPerformed(ActionEvent e) {
        try {
            if (errorGobbler != null) {
                errorGobbler = null;
            }

            if (outputGobbler != null) {
                outputGobbler = null;
            }

            if (proc != null) {
                proc.destroy();
            }

            TMData tmpdata = backend.getTMData();

            String lang = (tmpdata).getTargetLanguageCode().toLowerCase();
            dictLang = null;

            if (lang.equals("de")) {
                dictLang = "german";
            } else if (lang.equals("fr")) {
                dictLang = "francais";
            } else if (lang.equals("it")) {
                dictLang = "italian";
            } else if (lang.equals("es")) {
                dictLang = "spanish";
            } else if (lang.equals("sv")) {
                dictLang = "swedish";
            } else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "There is no spellchecker for this language dictionary!", "Error", JOptionPane.WARNING_MESSAGE);

                return;
            }

            String editorHome = System.getProperty("editor_home");

            if (editorHome == null) {
                editorHome = backend.getConfig().getHome().getAbsolutePath();
            }

            proc = Runtime.getRuntime().exec(SpellCheckerAPI.getCommand(editorHome, dictLang));

            /*errorGobbler = new
            StreamGobbler(proc.getErrorStream(), "ERROR");


            outputGobbler = new
            StreamGobbler(proc.getInputStream(), "OUTPUT");

            // kick them off
            errorGobbler.start();
            outputGobbler.start();
             */
            PrintWriter pw = null;
            String targetLan = backend.getProject().getTgtLang();
            InputStreamReader isr = new InputStreamReader(proc.getInputStream(), Languages.getLanguageENC(targetLan));
            BufferedReader br = new BufferedReader(isr);

            if (proc.getOutputStream() != null) {
                pw = new PrintWriter(new OutputStreamWriter(proc.getOutputStream(), Languages.getLanguageENC(targetLan)), true);
            }

            check(pw, br);

            proc.getOutputStream().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void check(PrintWriter pw, BufferedReader br) {
        AlignmentMain.testMain2.stopEditing();

        int start = AlignmentMain.testMain2.tableView.getSelectedRow();

        if (start == -1) {
            start = 0;
        }

        TMData tmpdata = backend.getTMData();

OUT: 
        for (int i = start; i < ((tmpdata).tmsentences).length; i++) {
            SpellCheckerDIALOG.diff = 0;

            PivotText p = new PivotText((tmpdata).tmsentences[i].getTranslation());

            for (int j = 0; j < p.elements().length; j++) {
                if (!(p.elements()[j]).getFlag()) {
                    if ((j != 0) && (j != (p.elements().length - 1)) && PivotTag.betweenIntegratedTag(j, p.elements())) {
                        continue;
                    }

                    String source = (p.elements()[j]).getContent();

                    for (int k = 0; k < source.length(); k++) {
                        char c = source.charAt(k);

                        if (!Character.isLetter(c)) {
                            if (PivotTextPane.delim.indexOf(String.valueOf(c)) == -1) {
                                PivotTextPane.delim = PivotTextPane.delim + c;
                            }
                        }
                    }

                    String delim = PivotTextPane.delim;
                    StringTokenizer tokens = new StringTokenizer(source, delim, true);
                    int offset = (p.elements()[j]).getPositionSite() + SpellCheckerDIALOG.diff;
                    int curP = 0;

                    while (tokens.hasMoreElements()) {
                        String word = (String)tokens.nextElement();

                        if ((word.length() == 1) && (delim.indexOf(word) != -1)) {
                            curP += 1;

                            continue;
                        }

                        try {
                            int ttt = Integer.parseInt(word);
                            curP += word.length();

                            continue;
                        } catch (NumberFormatException ex) {
                        }

                        if (PivotTextPane.getWordFromIgnoreTable(word) != null) {
                            curP += word.length();

                            continue;
                        }

                        int flag = -1;
                        resultString = null;

                        SpellCheckerAPI.checkWord(pw, word);

                        resultString = SpellCheckerAPI.getResult(br);

                        if (resultString == null) {
                            break OUT;
                        }

                        if (resultString != null) {
                            if (!resultString.trim().equals(SpellCheckerAPI.getFlagForNonErrorWord())) {
                                suggestion.clear();

                                if (resultString.startsWith("#")) {
                                } else if (resultString.startsWith(SpellCheckerAPI.getFlagForErrorWord())) {
                                    String words = resultString.substring(resultString.indexOf(SpellCheckerAPI.getSuggestionStartFlagForErrorWord()) + 1);
                                    StringTokenizer tokens2 = new StringTokenizer(words, SpellCheckerAPI.getSuggestionDelimiterFlagForErrorWord(), false);

                                    while (tokens2.hasMoreElements()) {
                                        suggestion.add(tokens2.nextToken().trim());
                                    }
                                }

                                PivotTextPane.wordInRowIndex = i;
                                PivotTextPane.wordStart = offset + curP;
                                PivotTextPane.wordEnd = offset + curP + word.length();

                                AlignmentMain.testMain2.navigateTo(i);
                                AlignmentMain.testMain2.tableView.repaint();

                                MainFrame.spellDlg.setLan(Languages.getLanguageName(backend.getTMData().getTargetLanguageCode()));
                                MainFrame.spellDlg.setEditor(word, offset + curP);
                                MainFrame.spellDlg.errorTextField.setText(word);

                                if ((tmpdata).tmsentences[i].getTranslationStatus() == TMSentence.APPROVED) {
                                    MainFrame.spellDlg.changeButton.setEnabled(false);
                                } else {
                                    MainFrame.spellDlg.changeButton.setEnabled(true);
                                }

                                MainFrame.spellDlg.suggestionList.setListData(suggestion);

                                if (suggestion.size() != 0) {
                                    MainFrame.spellDlg.suggestionList.setSelectedIndex(0);
                                } else {
                                    MainFrame.spellDlg.suggestionList.setSelectedIndex(-1);
                                }

                                SpellCheckerDIALOG.retValue = -1;
                                SpellCheckerDIALOG.actionSignal = 0;
                                MainFrame.spellDlg.addButton.setEnabled(true);
                                MainFrame.spellDlg.ignoreButton.setEnabled(true);
                                MainFrame.spellDlg.ignoreAllButton.setEnabled(true);
                                MainFrame.spellDlg.enableGUI();

                                if (!MainFrame.spellDlg.isVisible()) {
                                    MainFrame.spellDlg.setVisible(true);
                                }

                                int result = SpellCheckerDIALOG.retValue;

                                switch (result) {
                                case -1: //close this dlg
                                    break OUT;

                                case 3: // cancel
                                    break OUT;

                                case 1: // ignore
                                    curP += word.length();

                                    continue;

                                case 2: // change
                                    curP += (word.length() + SpellCheckerDIALOG.curDiff);

                                    continue;

                                case 0: // add
                                    curP += (word.length() + SpellCheckerDIALOG.curDiff);
                                    SpellCheckerAPI.addToPersonal(pw, word);

                                    continue;

                                case 4: // ignore all
                                    PivotTextPane.addToIgnoreTable(word);
                                    curP += word.length();

                                    continue;
                                }
                            }
                        }

                        curP += word.length();
                    }
                }
            }
        }

        MainFrame.spellDlg.setVisible(false);

        int oldRow = PivotTextPane.wordInRowIndex;
        PivotTextPane.wordInRowIndex = -1;
        AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getCellRect(oldRow, AlignmentMain.testMain2.showType, false));
    }

    void jMenuUpdateMiniTM_actionPerformed(ActionEvent e) {
        jBtnUpdateMiniTM_actionPerformed(null);
    }

    void jMenuMaintainMiniTM_actionPerformed(ActionEvent e) {
        if (backend.hasCurrentFile()) {
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(this, "To maintain the mini-TM, you must first close the current file.", "Warning", JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (maintainFrame == null) {
            maintainFrame = new JFrame("Mini-TM Maintain Tool");
            maintainFrame.getContentPane().setLayout(new BorderLayout());

            miniTMAlignment = new MiniTMAlignmentMain();
            miniTMAlignment.data = getTMUnits();

            if (miniTMAlignment.modifiedSegments != null) {
                miniTMAlignment.modifiedSegments.clear();
            }

            maintainFrame.getContentPane().add(miniTMAlignment, BorderLayout.CENTER);

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JButton findButton = new JButton("Search/Replace...");
            findButton.setMnemonic('F');
            findButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (findDlgForMaintain == null) {
                            findDlgForMaintain = new JDialog(maintainFrame, "Search/Replace ...", false);
                            findDlgForMaintain.getContentPane().setLayout(new BorderLayout());

                            FindDlgForMaintainence findPanel = new FindDlgForMaintainence(findDlgForMaintain, backend);

                            findDlgForMaintain.getContentPane().add(findPanel, "Center");
                            findDlgForMaintain.setSize(400, 240);
                            findDlgForMaintain.setResizable(true);
                        }

                        findDlgForMaintain.setLocationRelativeTo(maintainFrame);
                        findDlgForMaintain.setVisible(true);
                        ((FindDlgForMaintainence)findDlgForMaintain.getContentPane().getComponent(0)).init();
                    }
                });
            panel.add(findButton);

            JButton saveButton = new JButton("Save");
            saveButton.setMnemonic('S');
            saveButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        miniTMAlignment.stopEditing();
                        backend.getProject().saveMaintainenceSegments(MiniTMAlignmentMain.data, MiniTMAlignmentMain.modifiedSegments);
                    }
                });
            panel.add(saveButton);

            JButton closeButton = new JButton("Close");
            closeButton.setMnemonic('C');
            closeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        miniTMAlignment.stopEditing();

                        if ((miniTMAlignment.modifiedSegments != null) && (miniTMAlignment.modifiedSegments.size() != 0)) {
                            Toolkit.getDefaultToolkit().beep();

                            int ret = JOptionPane.showConfirmDialog(maintainFrame, "You have modified current Mini-TM file.\nDo you want to save it?", "Mini-TM Saving", JOptionPane.YES_NO_CANCEL_OPTION);

                            switch (ret) {
                            case -1:
                            case JOptionPane.CANCEL_OPTION:
                                return;

                            case JOptionPane.YES_OPTION:
                                backend.getProject().saveMaintainenceSegments(MiniTMAlignmentMain.data, miniTMAlignment.modifiedSegments);

                                break;

                            case JOptionPane.NO_OPTION:
                                break;
                            }
                        }

                        maintainFrame.setVisible(false);
                    }
                });
            panel.add(closeButton);
            maintainFrame.getContentPane().add(panel, BorderLayout.SOUTH);
            maintainFrame.setSize(800, 600);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = maintainFrame.getSize();

            if (frameSize.height > screenSize.height) {
                frameSize.height = screenSize.height;
            }

            if (frameSize.width > screenSize.width) {
                frameSize.width = screenSize.width;
            }

            maintainFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            maintainFrame.setVisible(true);
        } else {
            int old = (miniTMAlignment.data == null) ? 0 : (3 * miniTMAlignment.data.length);
            Object[] obj = getTMUnits();
            miniTMAlignment.data = new Object[obj.length];
            System.arraycopy(obj, 0, miniTMAlignment.data, 0, obj.length);

            if (miniTMAlignment.modifiedSegments != null) {
                miniTMAlignment.modifiedSegments.clear();
            }

            miniTMAlignment.repaintSelf(old);

            if (!maintainFrame.isVisible()) {
                maintainFrame.setVisible(true);
            }
        }
    }

    /**
     * Mini-TM merge frame
     */
    void jMenuMergeMiniTM_actionPerformed(ActionEvent e) {
        if (mergeDialog == null) {
            mergeDialog = new MergeMiniTMPanel(this, "Create Master Project", backend.getConfig().getMiniTMDir().getAbsolutePath(), backend);

            mergeDialog.setLocationRelativeTo(this);
            mergeDialog.setVisible(true);
        } else {
            if (!mergeDialog.isVisible()) {
                mergeDialog.init();
                mergeDialog.cards.first(mergeDialog.getContentPane());
                mergeDialog.setVisible(true);
            }
        }
    }

    void jMenuConvert_actionPerformed(ActionEvent e) {
        //save the stuff before backconversion
        if (!saveIfModified()) {
            return;
        }

        File file = backend.getCurrentFile();

        if (!backend.hasCurrentFile()) {
            String strLastFile = backend.getConfig().getStrLastFile();

            if (strLastFile == null) {
                file = new File(strCurrentDir);
            } else {
                file = new File(strLastFile);
            }
        }

        JBackConverter m_backConv = new JBackConverter(this, file);

        if (memento != null) {
            m_backConv.setDialogSettings(memento);
        }

        m_backConv.setLocationRelativeTo(this);
        m_backConv.show();

        //  Extract Memento from the JBackConverter when we are done!
        memento = m_backConv.getDialogSettings();
    }

    /* ********************************************************************************************
     *
     *  Options menus
     *
     * *******************************************************************************************
     */
    void jCheckBoxMenuUpdateTags_actionPerformed(ActionEvent e) {
        backend.getConfig().setBFlagTagUpdate(jCheckBoxMenuUpdateTags.isSelected());
    }

    void jCBMenuTagProtection_actionPerformed(ActionEvent e) {
        Configuration cfg = backend.getConfig();
        cfg.setTagProtection(jCBMenuTagProtection.isSelected());
        cfg.setBFlagTagProtection(jCBMenuTagProtection.isSelected());

        // bug 4714740
        PivotTextPane p = getCurEditingTextPane();

        if (p == null) {
            return;
        }

        p.setMainFrameClipEnable();
    }

    void jCBMenuSynScrolling_actionPerformed(ActionEvent e) {
        boolean synScroll = jCBMenuSynScrolling.isSelected();

        backend.getConfig().setBFlagSynScrolling(synScroll);

        if (synScroll) {
            AlignmentMain.testMain1.addAdjustmentListener();
            AlignmentMain.testMain2.addAdjustmentListener();
        } else {
            AlignmentMain.testMain1.removeAdjustmentListener();
            AlignmentMain.testMain2.removeAdjustmentListener();
        }
    }

    void jCBMenuWriteProtection_actionPerformed(ActionEvent e) {
        boolean writeProt = jCBMenuWriteProtection.isSelected();

        backend.getConfig().setBFlagWriteProtection(writeProt);

        if (writeProt) {
            AlignmentMain.testMain1.stopEditing();
            curTable = AlignmentMain.testMain2.tableView;

            // bug 4714740
            this.setClipPaste(false);
        } else {
            AlignmentMain.testMain1.startEditing();

            // bug 4714740
            PivotTextPane p = getCurEditingTextPane();

            if (p == null) {
                return;
            }

            p.setMainFrameClipEnable();
        }
    }

    void jCBMenuTagVerifyIgnoreOrder_actionPerformed(ActionEvent e) {
        backend.getConfig().setBFlagTagVerifyIgnoreOrder(jCBMenuTagVerifyIgnoreOrder.isSelected());
    }

    void jCBMenuAutoPropagate_actionPerformed(ActionEvent e) {
        boolean flag = jCBMenuAutoPropagate.isSelected();

        backend.getConfig().setBFlagAutoPropagate(flag);

        TMData tmpdata = backend.getTMData();

        if (tmpdata != null) {
            tmpdata.setAutoPropagate(flag);
        }
    }

    void jMenuAutoSave_actionPerformed(ActionEvent e) {
        int interval = backend.getConfig().getIGlobalInterval();
        boolean autoSave = backend.getConfig().isBFlagAutoSave();
        boolean tempFile = backend.getConfig().isBFlagTempFile();

        JAutoSaveDlg jAd = new JAutoSaveDlg(this, autoSave, interval);
        jAd.show();

        if (jAd.isCancelled()) {
            return;
        }

        interval = jAd.getInterval();
        autoSave = jAd.isAutoSaveEnabled();

        Configuration cfg = backend.getConfig();
        cfg.setIGlobalInterval(interval);
        cfg.setBFlagAutoSave(autoSave);

        try {
            cfg.save();
        } catch (ConfigurationException cfge) {
            logger.throwing(getClass().getName(), "jMenuAutoSave_actionPerformed", cfge);
            logger.severe("WHile saving interval -- WILL Be IGNORED");
        }

        //TODO move to a separate method
        if (!backend.hasCurrentFile()) {
            return;
        }

        if (autoSave) {
            if (backend.getConfig().isBFlagTempFile()) {
                /*                if(backend.getCurrentFile().getAbsolutePath().toLowerCase().endsWith(".xlz") == true) {
                                    strTempFileName = MainFrame.strHome+File.separator+"Temp.xlz";
                                } else {
                                    strTempFileName = MainFrame.strHome+File.separator+"Temp.xlf";
                                }
                                File f = new File(strTempFileName);
                 */
                m_timer.setInitialDelay(interval * 60 * 1000);

                if (autoSave) {
                    if (m_timer.isRunning()) {
                        m_timer.restart();
                    } else {
                        m_timer.start();
                    }
                } else {
                    m_timer.stop();
                }

                if (!backend.saveFileToTemp()) {
                    logger.warning("Auto save failed");
                }

                bSafeExit = false;
            } else { //if(!bFlagTempFile) {
                bSafeExit = true;
            }
        } else {
            if (m_timer != null) {
                if (m_timer.isRunning()) {
                    m_timer.stop();
                }
            }
        }

        try {
            backend.getConfig().save();
        } catch (ConfigurationException cfge) {
            logger.throwing(getClass().getName(), "jMenuAutoSave_actionPerformed", cfge);
            logger.severe("WHile saving interval -- WILL Be IGNORED");
        }

        //--
    }

    void jMenuShortcutOption_actionPerformed(ActionEvent e) {
        if (shortcutOptionDlg == null) {
            shortcutOptionDlg = new CustomKeyboard(this, shortcutsBuilder);
        }

        shortcutOptionDlg.setLocationRelativeTo(this);
        shortcutOptionDlg.setVisible(true);
    }

    /** *******************************************************************************************
     *
     *  Toolbars actions
     *
     *  *******************************************************************************************
     */
    void jBtnOpen_actionPerformed(ActionEvent e) {
        if (!openFile()) {
            return;
        }
    }

    void jBtnSave_actionPerformed(ActionEvent e) {
        jMenuSave_actionPerformed(null);
    }

    void jBtnClose_actionPerformed(ActionEvent e) {
        jMenuClose_actionPerformed(null);
    }

    void jBtnUndo_actionPerformed(ActionEvent e) {
        jMenuUndo_actionPerformed(null);
    }

    void jBtnRedo_actionPerformed(ActionEvent e) {
        jMenuRedo_actionPerformed(null);
    }

    void jBtnCopy_actionPerformed(ActionEvent e) {
        jMenuCopy_actionPerformed(null);
    }

    void jBtnPaste_actionPerformed(ActionEvent e) {
        jMenuPaste_actionPerformed(null);
    }

    void jBtnCut_actionPerformed(ActionEvent e) {
        jMenuCut_actionPerformed(null);
    }

    void jBtnSearch_actionPerformed(ActionEvent e) {
        jMenuSearch_actionPerformed(null);
    }

    void jBtnNext_actionPerformed(ActionEvent e) {
        this.jMenuNaviNextOption_actionPerformed(null);
    }

    void jBtnPrev_actionPerformed(ActionEvent e) {
        this.jMenuNaviPrevOption_actionPerformed(null);
    }

    void jBtnMarkAsTransAndGoToNextUnTrans_actionPerformed(ActionEvent e) {
        jMenuMarkTranslatedAndNext_actionPerformed(null);
    }

    void jBtnMarkAsApprovedAndGoToNextTrans_actionPerformed(ActionEvent e) {
        jMenuMarkReviewedAndNext_actionPerformed(null);
    }

    void jBtnTagVerify_actionPerformed(ActionEvent e) {
        jMenuTagVerify_actionPerformed(null);
    }

    void jBtnSpellCheck_actionPerformed(ActionEvent e) {
        jMenuSpellCheck_actionPerformed(null);
    }

    void jBtnUpdateMiniTM_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        int iSelRow = AlignmentMain.testMain1.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        if ((tmpdata.bMiniTMFlags[iSelRow] == false) || (tmpdata.tmsentences[iSelRow].getTranslationStatus() != TMSentence.TRANSLATED)) {
            return;
        }

        String transId = backend.getConfig().getTranslatorID();

        int succeeded = tmpdata.tmsentences[iSelRow].updateMiniTM(transId, iSelRow);
        tmpdata.bMiniTMFlags[iSelRow] = false;

        if (succeeded == 1) { // ADD

            Vector v = new Vector();
            String src = tmpdata.tmsentences[iSelRow].getSource();
            String trans = tmpdata.tmsentences[iSelRow].getTranslation();
            String targetLan = backend.getProject().getTgtLang();

            if ("ZH".equals(targetLan) || "ZT".equals(targetLan) || "JA".equals(targetLan)) {
            } else {
                if (src.endsWith(" ")) {
                    if (!trans.endsWith(" ")) {
                        trans += " ";
                    }
                }
            }

            v.addElement(new PivotText(src));
            v.addElement(new PivotText(src));
            v.addElement(new PivotText(trans));
            MiniTMFrame.jMiniTM.data.insertElementAt(v, 0);

            MiniTMTableFrame.diff = null;
            MiniTMFrame.jMiniTM.matchesObject = tmpdata.tmsentences[iSelRow].getMatches(true);
            MiniTMFrame.jMiniTM.tableView.tableChanged(new TableModelEvent(MiniTMFrame.jMiniTM.dataModel, 0, 0, 0, TableModelEvent.INSERT));
            MiniTMFrame.jMiniTM.tableView.clearSelection();
            MiniTMFrame.jMiniTM.tableView.setRowSelectionInterval(0, 0);
            MiniTMFrame.jMiniTM.tableView.repaint();
        } else if (succeeded == 0) { // OVERWRITE

            Vector v = new Vector();
            String src = tmpdata.tmsentences[iSelRow].getSource();
            String trans = tmpdata.tmsentences[iSelRow].getTranslation();

            String targetLan = backend.getProject().getTgtLang();

            if ("ZH".equals(targetLan) || "ZT".equals(targetLan) || "JA".equals(targetLan)) {
            } else {
                if (src.endsWith(" ")) {
                    if (!trans.endsWith(" ")) {
                        trans += " ";
                    }
                }
            }

            v.addElement(new PivotText(src));
            v.addElement(new PivotText(src));
            v.addElement(new PivotText(trans));
            MiniTMFrame.jMiniTM.data.set(0, v);
            MiniTMTableFrame.diff = null;

            MiniTMFrame.jMiniTM.matchesObject = tmpdata.tmsentences[iSelRow].getMatches(true);
            MiniTMFrame.jMiniTM.tableView.tableChanged(new TableModelEvent(MiniTMFrame.jMiniTM.dataModel, 0, 0, 0, TableModelEvent.UPDATE));
            MiniTMFrame.jMiniTM.tableView.clearSelection();
            MiniTMFrame.jMiniTM.tableView.setRowSelectionInterval(0, 0);
            MiniTMFrame.jMiniTM.tableView.repaint();
        }
    }

    void jBtnHelp_actionPerformed(ActionEvent e) {
        this.helpAbout_actionPerformed(null);
    }

    void setOldViewForUndoEdit(ViewUndoableEdit ue) {
        SetTableViewThread t = new SetTableViewThread(ue, true);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    void setNewViewForUndoEdit(ViewUndoableEdit ue) {
        SetTableViewThread t = new SetTableViewThread(ue, false);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    public void resetFileHistory() {
        removeFileHistory();

        File curFile = backend.getCurrentFile();
        String path = (curFile == null) ? null : curFile.getAbsolutePath();

        //Can this happen ?
        if (path == null) {
            return;
        }

        List history = backend.getConfig().getFilesHistory();
        history.remove(path);
        history.add(0, path);

        //TODO Think of number betwenn 0 and 7 ? Change to something loadable from config
        if (history.size() >= 7) {
            history.remove(6);
        }

        setFileHistory(null);
    }

    private void removeFileHistory() {
        List history = backend.getConfig().getFilesHistory();

        for (Iterator i = history.iterator(); (historyMenu.getItemCount() > 0) && i.hasNext();) {
            historyMenu.remove(historyMenu.getItemCount() - 1);
        }
    }

    private void setFileHistory(JMenu menu) {
        if (menu != null) {
            historyMenu = menu;
        }

        historyMenu.removeAll();
        addOriginalMenuItem(historyMenu);
        historyMenu.addSeparator();

        ActionListener lstnr = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuHistory_actionPerformed(e);
            }
        };

        List history = backend.getConfig().getFilesHistory();

        for (Iterator i = history.iterator(); i.hasNext();) {
            String file = (String)i.next();
            JMenuItem mi = new JMenuItem(file);
            mi.addActionListener(lstnr);
            mi.setEnabled(true);
            historyMenu.add(mi);
        }

        historyMenu.addSeparator();
        historyMenu.add(jMenuExit);
    }

    void jMenuHistory_actionPerformed(ActionEvent ex) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();
        openFile(((JMenuItem)ex.getSource()).getText());
    }

    private void addOriginalMenuItem(JMenu menu) {
        menu.add(jMenuNewProject);
        menu.add(jMenuOpenProject);
        menu.addSeparator();
        menu.add(jMenuOpen);
        menu.addSeparator();
        menu.add(jMenuSave);
        menu.add(jMenuSaveAs);

        if (boolOnline) {
            if (boolReview) {
                menuFile.add(jmenuApproveFile);
                menuFile.add(jmenuRejectFile);
            } else {
                menuFile.add(jmenuReturnToPortal);
            }
        }

        menu.add(jMenuSaveMiniTM);
        menu.addSeparator();
        menu.add(jMenuClose);
        menuFile.addSeparator();
        menuFile.add(jMenuPrintOptions);
        menuFile.add(jMenuPrint);
    }

    int closeGUI() {
        //TODO return something else then int
        if (closeFile() == 0) {
            return 0;
        }

        enableMenus(false);

        bSafeExit = true;

        if (proc != null) {
            proc.destroy();
        }

        int oldNum = (MiniTMFrame.jMiniTM.data == null) ? 0 : MiniTMFrame.jMiniTM.data.size();
        MiniTMFrame.jMiniTM.data = new Vector();
        MiniTMFrame.jMiniTM.matchesObject = new Match[0];
        MiniTMFrame.jMiniTM.currentRow = -1;
        MiniTMFrame.jMiniTM.repaintSelf(oldNum);

        backend.close();

        this.setTitle(Constants.TOOL_NAME + " -" + backend.getProject().getProjectName());

        this.myMatchstatusBar.setInformation("");
        this.myMatchstatusBar.setAliNumber("");
        this.myMatchstatusBar.setStatus(-1);
        repaintSelf();

        return 1;
    }

    public void disableGUI() {
        Component glassPane = this.getRootPane().getGlassPane();
        glassPane.setVisible(true);
        glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void enableGUI() {
        Component glassPane = this.getRootPane().getGlassPane();
        glassPane.setVisible(false);
        glassPane.setCursor(Cursor.getDefaultCursor());
    }

    //private String defaultSaveDir;
    public String getDefaultSaveDir() {
        return (strEditorHome + File.separator + "downloads" + File.separator);
    }

    public void setClipCut(boolean enable) {
        jMenuCut.setEnabled(enable);
        jBtnCut.setEnabled(enable);
        canCut = enable;
    }

    public void setClipCopy(boolean enable) {
        jMenuCopy.setEnabled(enable);
        jBtnCopy.setEnabled(enable);
        canCopy = enable;
    }

    public void setClipPaste(boolean enable) {
        jMenuPaste.setEnabled(enable);
        jBtnPaste.setEnabled(enable);
        canPaste = enable;
    }

    private PivotTextPane getCurEditingTextPane() {
        if (curTable == null) {
            return null;
        } else if ((curTable == AlignmentMain.testMain1.tableView) && curTable.isEditing() && (curTable.getEditingColumn() == 1)) {
            return PivotTextEditor1.sourceEditor;
        } else if ((curTable == AlignmentMain.testMain2.tableView) && curTable.isEditing() && (curTable.getEditingColumn() == 1)) {
            return PivotTextEditor1.targetEditor;
        }

        return null;
    }

    private void stopEditing() {
        if (AlignmentMain.testMain1.tableView.isEditing()) {
            TableCellEditor editor = AlignmentMain.testMain1.tableView.getCellEditor(AlignmentMain.testMain1.tableView.getSelectedRow(), AlignmentMain.testMain1.tableView.getSelectedColumn());
            editor.stopCellEditing();
        }

        if (AlignmentMain.testMain2.tableView.isEditing()) {
            TableCellEditor editor = AlignmentMain.testMain2.tableView.getCellEditor(AlignmentMain.testMain2.tableView.getSelectedRow(), AlignmentMain.testMain2.tableView.getSelectedColumn());
            editor.stopCellEditing();
        }
    }

    /**
     * Mini-TM maintainence Frame
     */
    private Object[] getTMUnits() {
        return (backend.getProject() == null) ? null : backend.getProject().getTMs();
    }

    //    void this_focusGained(FocusEvent e) {
    //    }
    //
    //    void this_windowActivated(WindowEvent e) {
    //    }
    private void openFileInit() {
        undo.undoStack.clear();
        undo.redoStack.clear();

        enableMenus(true);

        setClipCut(false);
        setClipCopy(false);
        setClipPaste(false);
    }

    //  Helper methods to implement the file and segment approval and rejection functions
    //  Maintenance Note: the following three methods should probably be extracted
    //  out to a new class: one tasked with working out the review status of segments.

    /** This method checks to see if all segments are approved or not.
     * @return true if all segments are approved, or false, otherwise.
     */
    protected boolean checkSegsApproved() {
        //  Loop over each segment and test if the reviewer has passed it
        TMData tmpdata = backend.getTMData();
        TMSentence[] array = tmpdata.tmsentences;

        for (int i = 0; i < array.length; i++) {
            //  If any segment fails the file fails: we only need one to fail to know.
            if (!(array[i].getTranslationStatus() == TMSentence.APPROVED)) {
                return false;
            }
        }

        return true; //  If we get here all segments passed
    }

    /** This method checks to see if all segments are rejected or not.
     * @return true if all segments are rejected, or false, otherwise.
     */
    protected boolean checkSegsRejected() {
        //  Loop over each segment and test if the reviewer has passed it
        TMData tmpdata = backend.getTMData();
        TMSentence[] array = tmpdata.tmsentences;

        for (int i = 0; i < array.length; i++) {
            //  If any segment fails the file fails: we only need one to fail to know.
            if (!(array[i].getTranslationStatus() == TMSentence.REJECTED)) {
                return false;
            }
        }

        return true; //  If we get here all segments passed
    }

    /** This method checks to see if all segments are rejected or not.
     * @return true if all segments are rejected, or false, otherwise.
     */
    protected boolean checkAllSegsReviewed() {
        //  Loop over each segment and test if the reviewer has passed it
        TMData tmpdata = backend.getTMData();
        TMSentence[] array = tmpdata.tmsentences;

        for (int i = 0; i < array.length; i++) {
            //  If any segment fails the file fails: we only need one to fail to know.
            boolean reviewed = ((array[i].getTranslationStatus() == TMSentence.APPROVED) || (array[i].getTranslationStatus() == TMSentence.REJECTED));

            if (!reviewed) {
                return false;
            }
        }

        return true;
    }

    protected String getUnreviewedSegList(boolean boolAddHtmlBreaks) {
        StringBuffer buffer = new StringBuffer();

        //  Loop over each segment and test if the reviewer has passed it
        TMData tmpdata = backend.getTMData();
        TMSentence[] array = tmpdata.tmsentences;
        int iUnreviewedCount = 0;

        for (int i = 0; i < array.length; i++) {
            boolean reviewed = ((array[i].getTranslationStatus() == TMSentence.APPROVED) || (array[i].getTranslationStatus() == TMSentence.REJECTED));

            if (!reviewed) {
                buffer.append((i + 1) + " ");
                iUnreviewedCount++;

                if (boolAddHtmlBreaks && ((iUnreviewedCount % 30) == 0)) {
                    buffer.append("<br>");
                }
            }
        }

        return buffer.toString();
    }

    /**  This method is a hack to get around the fact that dialog.show() attempts
     *  to display its parent frame as well. This attempt jumps the gun on the
     *  SplitPane setup, causing the divider to appear at the very top. This method
     *  is called after all the initialization is complete to set the ratio of the
     *  SplitPane's panes to the correct value.
     * @param d The ratio of the top to the bottom window, expressed as a
     * double between 0 and 1.0
     */
    public void setSplitPaneRatio(double d) {
        MySplitPane.setDividerLocation(d);
    }

    public void jMenuSplitXliff_actionPerformed(ActionEvent e) {
        XliffSplittingController split = new XliffSplittingController(this);

        try {
            split.doXliffSplitting();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public void jMenuMergeXliff_actionPerformed(ActionEvent e) {
        XliffMergingController controller = new XliffMergingController(this);

        try {
            controller.doXliffMerging();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    /** Setter method to allow other objects to set the value of the semaphore.
     */
    public void setSafeToExit(boolean safeToExit) {
        semaphore.setSafeToExit(safeToExit);
    }

    /** Setter method to allow other objects to set the value of the semaphore.
     * @param safeToExit If this flag is true the method will attempt to toggle the semaphore to true. If the semaphore is already true then this operation will fail and the method will return false.
     * @return true if the semaphore was toggled successfully
     */
    public boolean testAndToggleSemaphore(boolean safeToExit) {
        if (safeToExit) {
            return semaphore.testAndLowerSemaphore();
        } else {
            return semaphore.testAndRaiseSemaphore();
        }
    }

    /** Getter method to allow other objects to access the value of the semaphore.
     */
    public boolean isSafeToExit() {
        return semaphore.isSafeToExit();
    }

    /** This method pops up a dialog box to ask the user if it is okay to exit
     * the application. This method would be called, in cases where it is unsafe
     * to exit the application, e.g., when a File save thread is running.
     */
    public boolean askUserToExit() {
        try {
            JOptionPane queryDlg = new JOptionPane();
            int response = queryDlg.showConfirmDialog(this, "<html>The Editor is currently performing an operation such as a save that could cause some data corruption <br> if it is interrupted.<br> Are you sureyou wish to exit the Editor?", "Okay to Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            return (response == JOptionPane.YES_OPTION);
        } catch (java.awt.HeadlessException ex) {
            ex.printStackTrace();

            return true; // In serious trouble. Best to let the editor exit in this case.
        }
    }

    /** This method handles the menu event to select a new font size for the XLIFF
     * editing components.
     */
    public void selectNewComponentFontSize() {
        //  Create and display the dialog that solicits the user input
        FontQueryDialog dlg = new FontQueryDialog(this, true);
        dlg.setFontSelectionOption(fontSelectionOption);
        dlg.show();

        //  Change the font size
        if (!dlg.wasCancelled()) {
            java.awt.Font newFont = dlg.getFontSizeSelection();

            //  Store the font chosen for later use.
            setDefaultFont(newFont);
            fontSelectionOption = dlg.getFontSelectionOption();

            updateComponentFontSize(newFont);

            //  Set the value in the INI file and default location.
        }
    }

    /** This method updates the Font size of the components in the Tables displaying
     * XLIFF data. This is to allow the user to select the best size font for their
     * language, and thus improve readability.
     * @param newFont The new Font.
     */
    protected void updateComponentFontSize(java.awt.Font newFont) {
        org.jvnet.olt.editor.util.JTextLabel[] editTableComponents = AlignmentMain.testMain1.editPanes;

        if (editTableComponents == null) {
            System.err.println("Components are null");

            return;
        }

        for (int i = 0; i < editTableComponents.length; i++) {
            if (editTableComponents[i] != null) {
                editTableComponents[i].setNewFont(newFont);
            }
        }
    }

    public static void setDefaultFont(java.awt.Font font) {
        if (font != null) {
            _defaultFont = font;
        }
    }

    public static java.awt.Font getDefaultFont() {
        return _defaultFont;
    }

    /** Loads license text from resources.
     *
     * The license text is expected to be found in cp root.
     *
     * @throws NullPointerException,IOException
     * @return The license text
     */
    private String loadLicense() throws NullPointerException, IOException {
        InputStream is = getClass().getResourceAsStream("/resources/LICENSE.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();

        String line = null;

        while ((line = lnr.readLine()) != null)
            sb.append(line).append("\n");

        lnr.close();

        return sb.toString();
    }

    void saveFileFailed() {
        JOptionPane.showMessageDialog(this, "Unknown error occured while saving the file.\n" + "Please copy the text in editor console and send it to\n" + "dev@open-language-tools.dev.java.net along with a copy of\n" + "the file.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Aks user to save file if modified.
     *
     * @return flase if cancelled or save failed. Otherwise true
     */
    boolean saveIfModified() {
        if (bHasModified && backend.hasCurrentFile()) {
            int iRet = JOptionPane.showConfirmDialog(this, "The current xlz or xlf file has been modified,\nDo you want to Save it?", "Save File", JOptionPane.YES_NO_CANCEL_OPTION);

            if (iRet == JOptionPane.CANCEL_OPTION) {
                return false;
            }

            if (!backend.saveFile()) {
                saveFileFailed();

                return false;
            }

            // bug 4821917 || if cancel the "Open File" dialog, "xp" and "tmpdata" can not be setup to "null"
            this.setBHasModified(false);
        }

        return true;
    }

    private void tryToOpenFile(File fileToOpen) {
        disableGUI();

        //Ask to close current file
        if (closeGUI() == 0) {
            return;
        }

        backend.tryToOpenFile(fileToOpen, new PostHandler());
    }
}
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

import java.io.File;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jvnet.olt.fuzzy.basicsearch.BasicFuzzySearchMiniTM;
import org.jvnet.olt.fuzzy.basicsearch.BasicSGMLFuzzySearchMiniTM;
import org.jvnet.olt.minitm.AlignedSegment;
import org.jvnet.olt.minitm.MiniTM;
import org.jvnet.olt.minitm.MiniTMException;


/**
 * This class is a wrapper of a MiniTM : it handles the interaction between the
 * translation editor and the underlying MiniTM.
 */
public class TransProject {
    private static final Logger logger = Logger.getLogger(TransProject.class.getName());
    private static final int SGML = 1;
    private static final int HTML = 2;
    private static final int XML = 3;
    private static final int JSP = 4;
    private static final int PO = 11;
    private static final int JAVA = 12;
    private static final int MSG = 13;
    private static final int PROPERTIES = 14;
    private static final int PLAINTEXT = 15;
    private static final int DTD = 16;
    private static Map knownTMTypes = Collections.synchronizedMap(new HashMap());

    static {
        knownTMTypes.put("SGML", new Integer(SGML));
        knownTMTypes.put("HTML", new Integer(HTML));
        knownTMTypes.put("XML", new Integer(XML));
        knownTMTypes.put("JSP", new Integer(JSP));

        knownTMTypes.put("PO", new Integer(PO));
        knownTMTypes.put("JAVA", new Integer(JAVA));
        knownTMTypes.put("MSG", new Integer(MSG));
        knownTMTypes.put("PROPERTIES", new Integer(PROPERTIES));
        knownTMTypes.put("DTD", new Integer(DTD));
        knownTMTypes.put("PLAINTEXT", new Integer(PROPERTIES));
    }

    private String projectName = null;
    private String src = null;
    private String tgt = null;
    private MiniTM minitm = null;
    private String path;
    private String dataType;

    private String fileName;
    //TODO -- why do we pass the directories ??? Isn't it always the same directory we use ?

    /**
     * Most of the parameters are self explanatory. The one that should get
     * a special mention is the dataType. While the data that's stored in the miniTM is
     * format agnostic (we just store the string), the way we retrieve matches from the
     * mini TM depends on the format. For sgml, html, xml and jsp, we use a miniTM
     * that can deal with tags, so that they don't have as much impact on the fuzzyness
     * value. For other formats, we use a simpler fuzzy search.
     *
     * @param project the project name
     * @param srcLan the source language for the project
     * @param tgtLan The target language for the project
     * @param tmDir the directory where the miniTM is saved to
     * @param dataType the dataType of the project file the project is being used with.
     * @throws java.lang.IllegalArgumentException if there was some probelm creating this project
     * @see BasicFuzzySearchMiniTM and BasicSGMLFuzzySearchTM
     */
    public TransProject(String project, String srcLan, String tgtLan, String tmDir, String dataType) throws IllegalArgumentException, MiniTMException {
        //  Guard clause : If a project is created with an invalid language an
        //  exception is thrown.
        if ((srcLan == null) || (tgtLan == null)) {
            IllegalArgumentException e = new IllegalArgumentException("Languages being nulled!");
            logger.throwing(getClass().getName(), "TransProject(String project,String srcLan,String tgtLan, String tmDir, String dataType) throws IllegalArgumentException", e);
            logger.severe("Languages being nulled!");
            throw e;
        }

        projectName = project;
        src = srcLan;
        tgt = tgtLan;

        if (!tmDir.endsWith(java.io.File.separator)) {
            tmDir += java.io.File.separator;
        }

        path = tmDir;

        fileName = path+ (new StringBuffer(project).append("_").append(srcLan).append("_").append(tgtLan).append(".MTM").toString() );
        knownTMTypes = new HashMap();

        
        minitm = getNewMiniTM(fileName, true, project, srcLan, tgtLan, dataType);
    }

    // reload minitm from file - bug 4727575
    public void reloadMinitm() {
        try {
            //StringBuffer fileName = new StringBuffer(projectName).append("_").append(src).append("_").append(tgt).append(".MTM");            
            //minitm = getNewMiniTM(path + fileName.toString(), true, projectName, src, tgt, this.dataType);
            minitm = getNewMiniTM(fileName, true, projectName, src, tgt, this.dataType);
        } catch (org.jvnet.olt.minitm.MiniTMException ex) {
            logger.throwing(getClass().getName(), "reloadMinitm()", ex);
            logger.severe("Exception:" + ex);

            //TODO throw an exception ???
        }
    }

    /**
     * close this project and save its relative minitm file.
     */
    public void closeProject() {
        try {
            minitm.saveMiniTmToFile();

            //TODO dubious. Should not bi in finally block ???
            minitm = null;
        } catch (org.jvnet.olt.minitm.MiniTMException ex) {
            logger.throwing(getClass().getName(), "closeProject()", ex);
            logger.severe("Exception:" + ex);
        }
    }

    /**
     * get all of the item pairs in this minitm file.
     */
    public AlignedSegment[] getTMs() {
        return minitm.getAllSegments();
    }

    /**
     * get a reference of this minitm.
     */
    public MiniTM getMiniTM() {
        return minitm;
    }

    /**
     * after maintaining the minitm,save the modifications.
     */
    public void saveMaintainenceSegments(Object[] segs, Vector modifiedSegs) {
        if ((segs == null) || (segs.length == 0) || (modifiedSegs == null) || (modifiedSegs.size() == 0)) {
            return;
        }

        for (int i = 0; i < modifiedSegs.size(); i++) {
            int index = Integer.parseInt((String)modifiedSegs.elementAt(i));

            try {
                AlignedSegment seg = (AlignedSegment)segs[index];
                long id = seg.getDataStoreKey();

                if (id < 0) { //segment whose source has been modified
                    minitm.removeSegment(new AlignedSegment("", null, null), -id);
                    minitm.addNewSegment(seg);
                } else { //segment whose translation has been modified only
                    minitm.updateSegment(seg, id);
                }
            } catch (Exception ex) {
                logger.throwing(getClass().getName(), "saveMaintainenceSegments", ex);
                logger.warning("Exception:" + ex);

                continue;
            }
        }

        try {
            minitm.saveMiniTmToFile();
            modifiedSegs.clear();

            // need to reload minitm when updated - bug 4727575
            // logger.finer("going into reloadminitm()...");
            reloadMinitm();
        } catch (Exception ex) {
            logger.throwing(getClass().getName(), "saveMaintainenceSegments", ex);
            logger.severe("Exception:" + ex);
        }
    }

    public String getMiniTMDir() {
        return path;
    }

    public String getSrcLang() {
        return src;
    }

    public String getTgtLang() {
        return tgt;
    }

    public String getProjectName() {
        return projectName;
    }

    private MiniTM getNewMiniTM(String fileName, boolean createIfMissing, String project, String srcLang, String targLang, String dataType) throws MiniTMException {
        MiniTM mini = null;
        Integer type = (Integer)this.knownTMTypes.get(dataType);

        if (type == null) { // if we don't know what type of mini tm to use, then use the simplest by default

            return new BasicFuzzySearchMiniTM(fileName.toString(), true, projectName, src, tgt);
        } else {
            switch (type.intValue()) {
            case SGML:
            case XML:
            case HTML:
            case JSP:

                // logger.finer("Returning a new sgml mini-TM");
                return new BasicSGMLFuzzySearchMiniTM(fileName.toString(), true, projectName, src, tgt);

            case PROPERTIES:
            case PO:
            case JAVA:
            case MSG:
            case PLAINTEXT:
            case DTD:

                // logger.finer("Returning a new basic mini-TM");
                return new BasicFuzzySearchMiniTM(fileName.toString(), true, projectName, src, tgt);

            default: // we should never get here (it implies we've added a new type to the Map

                // but have forgotten to update the above switch statement)
                logger.severe("Error determing type of mini TM to create from datatype " + dataType + "\n" + "Please report a bug about this error !");

                //TODO throw an exception (around someone's neck)
            }
        }

        // returning an empty mini tm will probably throw a null pointer exception
        // somewhere else in the code, which should be handled properly.
        return mini;
    }

    /**
     *  We need a way to change the dataType of a project. When we create a project, we don't
     *  always know what dataType is going to be used for it. When the MainFrame opens a new
     *  XLIFF file, we retrieve the dataType from it, set that DataType here and then ask
     *  this TransProject to reload itself, which instantiates the correct underlying MiniTM
     *  implementation
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /** return project name encoded with src and dest languages.
     *
     * return String in form of <PROJECT_NAME>_<SRC_LANG>_<TGT_LANG>
     *
     */
    public String getEncodedName() {
        return projectName + "_" + src + "_" + tgt;
    }
    
    public File getMiniTMFile(){
        return new File(fileName);

    }
}

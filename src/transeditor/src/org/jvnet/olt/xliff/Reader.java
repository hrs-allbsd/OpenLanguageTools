/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.jvnet.olt.editor.format.DefaultFormatElementExtractor;
import org.jvnet.olt.editor.format.FormatElementExtractor;
import org.jvnet.olt.editor.format.FormatElementExtractorFactory;
import org.jvnet.olt.editor.format.InvalidFormatTypeException;
import org.jvnet.olt.editor.format.VariableManagerFactory;
import org.jvnet.olt.editor.model.MatchAttributes;
import org.jvnet.olt.editor.translation.Constants;
import org.jvnet.olt.editor.util.BaseElements;
import org.jvnet.olt.editor.util.NestableException;
import org.jvnet.olt.xliff.mrk.MrkContentTracker;

import org.jvnet.olt.format.GlobalVariableManager;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


/**
 * @author <a href="mailto:manpreet.singh@sun.com">Manpreet Singh</a>
 * @author <a href="mailto:tony.wu@sun.com">Tony Wu</a>
 * @version 1.0
 *
 * @deprecated
 */

/*
 * Maintenance note: This class is getting very unwieldy. It needs to be refactored
 * to ensure further maintenance can take place. The accumulation of data across
 * handler method invocations really needs to be tidied up. The current implementation
 * is not prepared to handle recursively nested elements. With inline elements in
 * particular, this will come back to haunt us.
 */
class Reader implements Handler, XLIFFModel {
    private static final Logger logger = Logger.getLogger(Reader.class.getName());
    private boolean bInHeader = false;
    private boolean bInPhaseGroup = false;
    private boolean bInContextGroup = false;
    private boolean bInGroup = false;
    private boolean bInTransUnit = false;
    private boolean bInSource = false;
    private boolean bInTarget = false;
    private boolean bInAltTrans = false;
    private boolean bInPropGroup = false;
    private int iCount = 0;
    private ArrayList alID = null;
    private TreeMap gGroupZeroSource = null;
    private TreeMap gGroupZeroTarget = null;
    private TreeMap gGroupAltTrans = null;

    //private TreeMap gGroupZeroSourceAlt = null;
    //private TreeMap gGroupZeroTargetAlt = null;
    //private TreeMap gSourceAltProp = null;
    //private TreeMap gMatchQuality = null;
    private String sSourceLanguageCode = null;
    private String sTargetLanguageCode = null;
    private ArrayList tempList = null;

    // this map is being used to save the attributes of <group> elements
    // temporarily
    private HashMap attributesMap = null;
    private StringBuffer gAccum = null;
    private String sCurrentTransUnitId = null;
    private String sCurrentXMLLang = null;
    private String sCurrentState = null;
    private String sMatchQuality = null;
    private String sFormatDiffInfo = "0"; //  Set a default. This stuff doesn't have to be here!
    private String sMatchType = null;
    private String sPropGroupName = null;
    private String sPropTypeName = null;
    private String sNoteContent = null;
    private String sProcessName = null;
    private String sPhaseName = null;
    private String sTool = Constants.TOOL_NAME;
    private XLIFFBasicSentence m_temps = null;
    private XLIFFBasicSentence m_tempt = null;
    private XLIFFSentence m_temp = null;

    /**
     * variables for match attributes
     */
    private MatchAttributes m_matchattr = null;
    private String sFileIdentifier = null;
    private String sProjectId = null;
    private String sWorkspaceBuild = null;
    private String sModule = null;
    private String sShortBookName = null;
    private String sSubject = null;
    private String sPartNo = null;

    /**
     * variable for other source context attributes
     */
    private Map mSourceContextMap = null;
    private AttributesFactory gAttrFactory = null;
    private TrackingSourceContext gSourceContextTrack = null;
    private TrackingGroup gGroupTrack = null;
    private TrackingComments gCommentsTrack = null;

    //current source context name
    private String sCurContextName;

    //end of handling state attr.
    //Boris:
    //need getLast
    private LinkedList files = new LinkedList();

    // remember the order in which we read them
    private Map transUnits = new LinkedHashMap();
    private TransUnit currentTransUnit;
    private AltTransUnit currentAltTransUnit;

    //How many file do we read ?
    private long numFiles = 0;
    private final String[] contextType = {
        "file_identifier", "project_id", "workspace_build", "module", "short_book_name", "subject",
        "part_no"
    };
    private String originalDataType = "";
    private MrkContentTracker mrkContentTracker;
    private java.util.Stack inlineElementStack;

    /************************************************************************************************
     *
     * Constructors
     *
     ***********************************************************************************************/
    public Reader(URL aURL) throws NestableException {
        // Create the MrkContentTracker
        mrkContentTracker = new MrkContentTracker();
        inlineElementStack = new java.util.Stack();

        bInSource = false;
        bInGroup = false;
        iCount = 0;
        gAccum = new StringBuffer();
        gGroupZeroSource = new TreeMap();
        gGroupZeroTarget = new TreeMap();

        gGroupAltTrans = new TreeMap();

        //gGroupZeroSourceAlt = new TreeMap();
        //gGroupZeroTargetAlt = new TreeMap();
        //gSourceAltProp      = new TreeMap();
        //gMatchQuality       = new TreeMap();
        mSourceContextMap = new HashMap();
        alID = new ArrayList();
        gGroupTrack = new TrackingGroup();
        gCommentsTrack = new TrackingComments();
        gAttrFactory = new AttributesFactory();
        gSourceContextTrack = new TrackingSourceContext();

        XParser theParser = new XParser(this, null);

        try {
            theParser.parse(aURL);
        } catch (Exception e) {
            throw new NestableException(e);
        }
    }

    public Reader(InputSource isInput) throws NestableException {
        // Create the MrkContentTracker
        mrkContentTracker = new MrkContentTracker();
        inlineElementStack = new java.util.Stack();

        bInSource = false;
        bInGroup = false;
        iCount = 0;
        gAccum = new StringBuffer();
        gGroupZeroSource = new TreeMap();
        gGroupZeroTarget = new TreeMap();

        gGroupAltTrans = new TreeMap();

        //gGroupZeroSourceAlt = new TreeMap();
        //gGroupZeroTargetAlt = new TreeMap();
        //gSourceAltProp      = new TreeMap();
        //gMatchQuality       = new TreeMap();
        mSourceContextMap = new HashMap();
        alID = new ArrayList();
        gGroupTrack = new TrackingGroup();
        gCommentsTrack = new TrackingComments();
        gSourceContextTrack = new TrackingSourceContext();
        gAttrFactory = new AttributesFactory();

        XParser theParser = new XParser(this, null);

        try {
            theParser.parse(isInput);
        } catch (Exception e) {
            throw new NestableException(e);
        }
    }

    /************************************************************************************************
     *
     * Get and Sets Methods
     *
     ***********************************************************************************************/
    public int getSize() {
        return gGroupZeroSource.size();
    }

    public Map getGroupZeroSource() {
        return gGroupZeroSource;
    }

    public Map getGroupZeroTarget() {
        return gGroupZeroTarget;
    }

    public Map getGroupAltTrans() {
        return gGroupAltTrans;
    }

    public String getSourceLanguage() {
        return sSourceLanguageCode;
    }

    public String getTargetLanguage() {
        return sTargetLanguageCode;
    }

    public Collection getIDArray() {
        return alID;
    }

    public String getProcessName() {
        return sProcessName;
    }

    public String getPhaseName() {
        return sPhaseName;
    }

    public String getToolName() {
        return sTool;
    }

    public TrackingGroup getGroupTrack() {
        return gGroupTrack;
    }

    public TrackingComments getCommentsTrack() {
        return gCommentsTrack;
    }

    public AttributesFactory getAttrFactory() {
        return gAttrFactory;
    }

    public TrackingSourceContext getSourceContextTrack() {
        return gSourceContextTrack;
    }

    /*
    public Map getGroupZeroLeveragedTranslations() {
      return gGroupZeroTargetAlt;
    }

    public Map getGroupZeroLeveragedSources() {
      return gGroupZeroSourceAlt;
    }

    public Map getSourceAltProp() {
      return gSourceAltProp;
    }

    public Map getMatchQuality() {
      return gMatchQuality;
    }
     */

    /************************************************************************************************
     *
     * Implement ContentHandler
     *
     ***********************************************************************************************/
    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void setDocumentLocator(Locator aLocator) {
    }

    public void characters(char[] aChars, int i, int i1) throws SAXException {
    }

    public void skippedEntity(String name) throws SAXException {
    }

    public void ignorableWhitespace(char[] aChars, int i, int i1) throws SAXException {
    }

    public void processingInstruction(String s, String s1) throws SAXException {
    }

    /************************************************************************************************
     *
     * Top Level and Header Elements
     *
     ***********************************************************************************************/
    public void start_xliff(final Attributes meta) throws SAXException {
        logger.finest("start_xliff: " + meta);
    }

    public void end_xliff() throws SAXException {
        logger.finest("end_xliff()");

        Iterator iter = gGroupZeroSource.keySet().iterator();

        while (iter.hasNext()) {
            String theKey = (String)iter.next();

            if (!gGroupZeroTarget.containsKey(theKey)) {
                gGroupZeroTarget.put(theKey, gGroupZeroSource.get(theKey));
            }
        }
    }

    public void start_file(final Attributes meta) throws SAXException {
        logger.finest("start_file: " + meta);

        // Get XLIFF source datatype here.
        originalDataType = meta.getValue("datatype");

        //  Add new stuff for setting the Format Extractor
        FormatElementExtractor extractor = null;

        try {
            VariableManagerFactory varManagerFactory = new VariableManagerFactory();
            GlobalVariableManager gvm = varManagerFactory.createVariableManager(originalDataType);

            FormatElementExtractorFactory factory = new FormatElementExtractorFactory();
            extractor = factory.createFormatExtractor(originalDataType, gvm);
        } catch (InvalidFormatTypeException ex) {
            //  throw new SAXException(ex.getMessage());
            //  We need to be a bit less brittle here. Use a default extractor that
            //  does nothing if we encounter a type we cannot handle.
            logger.throwing(getClass().getName(), "start_file", ex);
            logger.warning("Format will not be highlighted in the Editor's editing window.");
            extractor = new DefaultFormatElementExtractor();
        }

        BaseElements.setFormatExtractor(extractor);

        //  end new format handling code
        sSourceLanguageCode = meta.getValue("source-language");
        sTargetLanguageCode = meta.getValue("target-language");

        logger.finest(sSourceLanguageCode + " => " + sTargetLanguageCode);

        //Boris:
        String original = meta.getValue("original");
        TransFile file = new TransFile(original, numFiles++);
        files.add(file);

        //
    }

    public void end_file() throws SAXException {
        logger.finest("end_file()");
    }

    public void start_header(final Attributes meta) throws SAXException {
        logger.finest("start_header: " + meta);

        if (bInHeader) {
            throw new SAXException("Invalid nesting of header elements");
        } else {
            bInHeader = true;
        }
    }

    public void end_header() throws SAXException {
        logger.finest("end_header()");
        bInHeader = false;
    }

    public void start_skl(final Attributes meta) throws SAXException {
        logger.finest("start_skl: " + meta);
    }

    public void end_skl() throws SAXException {
        logger.finest("end_skl()");
    }

    public void handle_external_file(final Attributes meta) throws SAXException {
        logger.finest("handle_external_file: " + meta);
    }

    public void handle_internal_file(final java.lang.String data, final Attributes meta) throws SAXException {
        logger.finest("handle_internal_file: " + data);
    }

    public void start_glossary(final Attributes meta) throws SAXException {
        logger.finest("start_glossary: " + meta);
    }

    public void end_glossary() throws SAXException {
        logger.finest("end_glossary()");
    }

    public void start_reference(final Attributes meta) throws SAXException {
        logger.finest("start_reference: " + meta);
    }

    public void end_reference() throws SAXException {
        logger.finest("end_reference()");
    }

    public void start_phase_group(final Attributes meta) throws SAXException {
        logger.finest("start_phase_group: " + meta);

        if (bInPhaseGroup) {
            throw new SAXException("Invalid nesting of Phase-grooup elements");
        } else {
            bInPhaseGroup = true;
        }
    }

    public void end_phase_group() throws SAXException {
        logger.finest("end_phase_group()");
        bInPhaseGroup = false;
    }

    public void start_phase(final Attributes meta) throws SAXException {
        logger.finest("start_phase: " + meta);

        if (bInHeader && bInPhaseGroup) {
            sProcessName = meta.getValue("process-name");
            sPhaseName = meta.getValue("phase-name");
            sTool = meta.getValue("tool");

            if (sTool == null) {
                sTool = Constants.TOOL_NAME;
            }
        }
    }

    public void end_phase() throws SAXException {
        logger.finest("end_phase()");
    }

    public void start_note(final Attributes meta) throws SAXException {
        logger.finest("start_note()" + meta);
    }

    public void handle_note(final java.lang.String data, final Attributes meta) throws SAXException {
        logger.finest("handle_note: " + data);

        if (bInHeader || (bInTransUnit && !bInAltTrans)) {
            sNoteContent = data;
        }
    }

    public void end_note() throws SAXException {
        logger.finest("end_note()");

        if (bInHeader) {
            gCommentsTrack.addComment("header", sNoteContent);
        } else if (bInTransUnit && !bInAltTrans) {
            //logger.finest(sCurrentTransUnitId);
            gCommentsTrack.addComment(sCurrentTransUnitId, sNoteContent);
        }

        sNoteContent = null;
    }

    /************************************************************************************************
     *
     * Named Group Elements
     *
     ***********************************************************************************************/
    public void start_context_group(final Attributes meta) throws SAXException {
        logger.finest("start_context_group: " + meta);

        bInContextGroup = true;
        sFileIdentifier = null;
        sProjectId = null;
        sWorkspaceBuild = null;
        sModule = null;
        sShortBookName = null;
        sSubject = null;
        sPartNo = null;

        //need context group name for storing context-type and value:
        sCurContextName = meta.getValue("name");
    }

    public void end_context_group() throws SAXException {
        logger.finest("end_context_group()");

        if (bInAltTrans) {
            m_matchattr = new MatchAttributes(sFileIdentifier, sProjectId, sWorkspaceBuild, sModule, sShortBookName, sSubject, sPartNo);
        }

        bInContextGroup = false;
    }

    public void handle_context(final java.lang.String data, final Attributes meta) throws SAXException {
        logger.finest("handle_context: " + data);

        if (bInAltTrans && bInContextGroup) {
            if (meta.getValue("context-type").equals(contextType[0])) {
                sFileIdentifier = gAttrFactory.getFileIdentifier(data);
            } else if (meta.getValue("context-type").equals(contextType[1])) {
                sProjectId = gAttrFactory.getProjectId(data);
            } else if (meta.getValue("context-type").equals(contextType[2])) {
                sWorkspaceBuild = gAttrFactory.getWorkspaceBuild(data);
            } else if (meta.getValue("context-type").equals(contextType[3])) {
                sModule = gAttrFactory.getModule(data);
            } else if (meta.getValue("context-type").equals(contextType[4])) {
                sShortBookName = gAttrFactory.getShorBookName(data);
            } else if (meta.getValue("context-type").equals(contextType[5])) {
                sSubject = gAttrFactory.getSubject(data);
            } else if (meta.getValue("context-type").equals(contextType[6])) {
                sPartNo = gAttrFactory.getPartNo(data);
            }
        } else if (bInContextGroup && !bInAltTrans) {
            // these are context items that are describing the source string, not a
            // proposed translation (alt-trans) for now, we're allowing *any* context type, by
            // storing the name/value pairs in a map.
            //Boris: to avoid overwrting of data by value of context with ident. name but in
            //other context we pack the conetx name with it
            TrackingSourceContext.SourceContextKey key = new TrackingSourceContext.SourceContextKey(sCurContextName, meta.getValue("context-type"));
            mSourceContextMap.put(key, data);
        }
    }

    public void start_count_group(final Attributes meta) throws SAXException {
        logger.finest("start_count_group: " + meta);
    }

    public void end_count_group() throws SAXException {
        logger.finest("end_count_group()");
    }

    public void handle_count(final java.lang.String data, final Attributes meta) throws SAXException {
        logger.finest("handle_count: " + data);
    }

    public void start_prop_group(final Attributes meta) throws SAXException {
        logger.finest("start_prop_group: " + meta);

        if (bInPropGroup) {
            throw new SAXException("Invalid nesting of prop-group elements");
        } else {
            bInPropGroup = true;

            if (meta.getValue("name") != null) {
                sPropGroupName = meta.getValue("name");
            } else {
                sPropGroupName = "";
            }
        }
    }

    public void end_prop_group() throws SAXException {
        logger.finest("end_prop_group()");

        //modified by tony -- 26/08
        bInPropGroup = false;
        sPropGroupName = null;
    }

    public void start_prop(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_prop()" + meta);
        }

        if (bInPropGroup) {
            sPropTypeName = meta.getValue("prop-type");
        }
    }

    public void handle_prop(final java.lang.String data, final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_prop: " + data + data.getClass());
        }

        if (bInPropGroup && bInAltTrans) {
            if (sPropGroupName.equals("format penalty")) {
                sFormatDiffInfo = data;
            } else if (sPropGroupName.equals("match")) {
                sMatchType = data;
            }

            /*
            if(sFormatDiffInfo != null) {
              String theKey = sCurrentTransUnitId;
              ArrayList theList = null;
              if (gSourceAltProp.containsKey(theKey)) {
                theList = (ArrayList) gSourceAltProp.get(theKey);
              } else {
                theList = new ArrayList();
              }
              theList.add(sFormatDiffInfo);
              gSourceAltProp.put(theKey, theList);
            }
             */
        }
    }

    public void end_prop() throws SAXException {
        if (true) {
            logger.finest("end_prop()");
        }

        if (bInPropGroup) {
            sPropTypeName = null;
        }
    }

    /************************************************************************************************
     *
     * Structural Elements
     *
     ***********************************************************************************************/
    public void start_body(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_body: " + meta);
        }
    }

    public void end_body() throws SAXException {
        if (true) {
            logger.finest("end_body()");
        }
    }

    public void start_group(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_group: " + meta);
        }

        bInGroup = true;
        tempList = new ArrayList();
        attributesMap = new HashMap();

        int numberOfAttributes = meta.getLength();

        for (int i = 0; i < numberOfAttributes; i++) {
            String key = meta.getQName(i);
            String value = meta.getValue(i);
            attributesMap.put(key, value);
        }
    }

    public void end_group() throws SAXException {
        if (true) {
            logger.finest("end_group()");
        }

        bInGroup = false;
        mSourceContextMap = new HashMap();
        gGroupTrack.addGroup((String)tempList.get(0), tempList, false, attributesMap);
    }

    public void start_trans_unit(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_trans_unit: " + meta);
        }

        bInTransUnit = true;

        sCurrentTransUnitId = meta.getValue("id");
        alID.add(iCount, sCurrentTransUnitId);
        iCount++;

        if (bInGroup) {
            tempList.add(sCurrentTransUnitId);
        } else {
        }

        //BORIS:
        TransUnitId newID = new TransUnitId(sCurrentTransUnitId, (TransFile)files.getLast());
        TransUnit unit = new TransUnit(newID);

        currentTransUnit = unit;
        transUnits.put(newID, unit);
    }

    public void end_trans_unit() throws SAXException {
        if (true) {
            logger.finest("end_trans_unit()");
        }

        // do stuff with the context information we have to hand.
        if (bInGroup) {
            if (gSourceContextTrack == null) {
                logger.finest("Warning source context track is null !");
                Thread.currentThread().dumpStack();
            }

            gSourceContextTrack.addContext(sCurrentTransUnitId, mSourceContextMap);

            //mSourceContextMap = new HashMap();
        } else {
            if (gSourceContextTrack == null) {
                logger.finest("Warning source context track is null !");
                Thread.currentThread().dumpStack();
            }

            gSourceContextTrack.addContext(sCurrentTransUnitId, mSourceContextMap);
            mSourceContextMap = new HashMap();
        }

        sCurrentTransUnitId = null;
        bInTransUnit = false;
    }

    public void start_source(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_source: " + meta);
        }

        if (bInSource || bInTarget) {
            throw new SAXException("Invalid nesting of source and/or target elements");
        }

        bInSource = true;
        gAccum = new StringBuffer();
        sCurrentXMLLang = meta.getValue("xml:lang");

        if (sCurrentXMLLang == null) {
            sCurrentXMLLang = sSourceLanguageCode;
        }
    }

    public void handle_source(final java.lang.String data, final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_source: " + data);
        }

        if (bInTarget) {
            throw new SAXException("Invalid nesting of source and/or target elements");
        }

        if (bInSource) {
            if (data != null) {
                gAccum.append(data);
            }
        }
    }

    public void end_source() throws SAXException {
        if (true) {
            logger.finest("end_source()");
        }

        if (bInTarget || !bInSource) {
            throw new SAXException("Invalid nesting of source and/or target elements");
        }

        if (bInSource) {
            if (sCurrentTransUnitId == null) {
                throw new SAXException("Could not find trans-unit ID for a source unit");
            }

            String theKey = sCurrentTransUnitId;

            if (bInAltTrans) {
                //ArrayList theList = null;
                //if (gGroupZeroSourceAlt.containsKey(theKey)) {
                //  theList = (ArrayList) gGroupZeroSourceAlt.get(theKey);
                //} else {
                //  theList = new ArrayList();
                //}
                m_temps = new XLIFFBasicSentence(gAccum.toString(), sCurrentXMLLang);

                //theList.add(m_temps);
                //gGroupZeroSourceAlt.put(theKey, theList);
                //currentAltTransUnit.setSource(m_temps);
            } else {
                if (gGroupZeroSource.containsKey(theKey)) {
                    throw new SAXException("More than one source element found for trans-unit id " + theKey);
                }

                m_temp = new XLIFFSentence(gAccum.toString(), sCurrentXMLLang, sCurrentTransUnitId);

                //alID.add(iCount,theKey);
                //iCount++;
                gGroupZeroSource.put(theKey, m_temp);

                currentTransUnit.setSource(m_temp);
            }

            bInSource = false;
            gAccum = null;
        }
    }

    public void start_target(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_target: " + meta);
        }

        if (bInSource || bInTarget) {
            throw new SAXException("Invalid nesting of source and/or target elements");
        }

        bInTarget = true;
        gAccum = new StringBuffer();
        sCurrentXMLLang = meta.getValue("xml:lang");

        if (sCurrentXMLLang == null) {
            sCurrentXMLLang = sTargetLanguageCode;
        }

        sCurrentState = meta.getValue("state");

        if (sCurrentState == null) {
            sCurrentState = "";
        }
    }

    public void handle_target(final java.lang.String data, final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_target: " + data);
        }

        if (!bInSource && bInTarget) {
            if (data != null) {
                gAccum.append(data);
            } else {
                gAccum.append("");
            }
        }
    }

    public void end_target() throws SAXException {
        if (true) {
            logger.finest("end_target()");
        }

        if (bInSource || !bInTarget) {
            throw new SAXException("Invalid nesting of source and/or target elements");
        }

        if (bInTarget) {
            if (sCurrentTransUnitId == null) {
                throw new SAXException("Could not find trans-unit ID for a source unit");
            }

            String theKey = sCurrentTransUnitId;

            if (bInAltTrans) {
                //ArrayList theList = null;
                //if (gGroupZeroTargetAlt.containsKey(theKey)) {
                //  theList = (ArrayList) gGroupZeroTargetAlt.get(theKey);
                //} else {
                //  theList = new ArrayList();
                //}
                m_tempt = new XLIFFBasicSentence(gAccum.toString(), sCurrentXMLLang);

                //theList.add(m_tempt);
                //gGroupZeroTargetAlt.put(theKey, theList);
                //currentAltTransUnit.setTarget(m_tempt);
            } else {
                if (gGroupZeroTarget.containsKey(theKey)) {
                    throw new SAXException("More than one target found for trans-unit ID " + theKey);
                }

                m_temp = new XLIFFSentence(gAccum.toString(), sCurrentXMLLang, sCurrentTransUnitId, sCurrentState);

                //m_temp.setTranslationState(sCurrentState);
                gGroupZeroTarget.put(theKey, m_temp);

                currentTransUnit.setTarget(m_temp);
            }

            bInTarget = false;
            gAccum = null;
        }
    }

    public void start_bin_unit(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_bin_unit: " + meta);
        }
    }

    public void end_bin_unit() throws SAXException {
        if (true) {
            logger.finest("end_bin_unit()");
        }
    }

    public void start_bin_source(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_bin_source: " + meta);
        }
    }

    public void end_bin_source() throws SAXException {
        if (true) {
            logger.finest("end_bin_source()");
        }
    }

    public void start_bin_target(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_bin_target: " + meta);
        }
    }

    public void end_bin_target() throws SAXException {
        if (true) {
            logger.finest("end_bin_target()");
        }
    }

    public void start_alt_trans(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_alt_trans: " + meta);
        }

        if ((sCurrentTransUnitId == null) || bInSource || bInTarget) {
            throw new SAXException("alt-trans found at invalid location");
        }

        bInAltTrans = true;
        sMatchQuality = meta.getValue("match-quality");

        if (sMatchQuality == null) {
            sMatchQuality = "0";
        }

        currentAltTransUnit = new AltTransUnit(sMatchQuality);
        currentTransUnit.addAltTrans(currentAltTransUnit);

        /*
        if(sMatchQuality != null)
        {
          String theKey = sCurrentTransUnitId;
          ArrayList theList = null;
          if (gMatchQuality.containsKey(theKey)) {
            theList = (ArrayList) gMatchQuality.get(theKey);
          } else {
            theList = new ArrayList();
          }
          theList.add(sMatchQuality);
          gMatchQuality.put(theKey, theList);
        }
         */
    }

    public void end_alt_trans() throws SAXException {
        if (true) {
            logger.finest("end_alt_trans()");
        }

        if (!bInAltTrans) {
            throw new SAXException("Closing alt-trans found at invalid location");
        }

        String theKey = sCurrentTransUnitId;
        ArrayList theList = null;

        if (gGroupAltTrans.containsKey(theKey)) {
            theList = (ArrayList)gGroupAltTrans.get(theKey);
        } else {
            theList = new ArrayList();
        }

        org.jvnet.olt.editor.model.Match match = null;

        if (sMatchType == null) {
            match = new org.jvnet.olt.editor.model.SingleSegmentMatch(m_temps, m_tempt, sFormatDiffInfo, sMatchQuality);
        } else {
            match = new org.jvnet.olt.editor.model.MultiSegmentMatch(m_temps, m_tempt, sFormatDiffInfo, sMatchQuality, sMatchType);
        }

        match.setMatchAttributes(m_matchattr);
        theList.add(match);
        gGroupAltTrans.put(theKey, theList);

        sFormatDiffInfo = "0";
        sMatchQuality = null;
        sMatchType = null;

        bInAltTrans = false;
    }

    /************************************************************************************************
     *
     * Inline Elements
     *
     ***********************************************************************************************/
    public void start_g(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_g: " + meta);
        }
    }

    public void handle_g(final java.lang.String data, final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_g: " + data);
        }

        if (bInSource || bInTarget) {
            if (data != null) {
                gAccum.append(data);
            }
        }
    }

    public void end_g() throws SAXException {
        if (true) {
            logger.finest("end_g()");
        }
    }

    public void handle_x(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_x: " + meta);
        }
    }

    public void handle_bx(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_bx: " + meta);
        }
    }

    public void handle_ex(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_ex: " + meta);
        }
    }

    public void start_bpt(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_bpt: " + meta);
        }
    }

    public void handle_bpt(final java.lang.String data, final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_bpt: " + data);
        }

        if (bInSource || bInTarget) {
            if (data != null) {
                gAccum.append(data);
            }
        }
    }

    public void end_bpt() throws SAXException {
        if (true) {
            logger.finest("end_bpt()");
        }
    }

    public void start_ept(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_ept: " + meta);
        }
    }

    public void handle_ept(final java.lang.String data, final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_ept: " + data);
        }

        if (bInSource || bInTarget) {
            if (data != null) {
                gAccum.append(data);
            }
        }
    }

    public void end_ept() throws SAXException {
        if (true) {
            logger.finest("end_ept()");
        }
    }

    public void start_sub(final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("start_sub: " + meta);
        }
    }

    public void handle_sub(final java.lang.String data, final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_sub: " + data);
        }

        if (bInSource || bInTarget) {
            if (data != null) {
                gAccum.append(data);
            }
        }
    }

    public void end_sub() throws SAXException {
        if (true) {
            logger.finest("end_sub()");
        }
    }

    public void start_it(final Attributes meta) throws SAXException {
        logger.finest("start_it: " + meta);
    }

    public void handle_it(final java.lang.String data, final Attributes meta) throws SAXException {
        logger.finest("handle_it: " + data);

        if (bInSource || bInTarget) {
            if (data != null) {
                gAccum.append(data);
            }
        }
    }

    public void end_it() throws SAXException {
        logger.finest("end_it()");
    }

    public void start_ph(final Attributes meta) throws SAXException {
        logger.finest("start_ph: " + meta);
    }

    public void handle_ph(final java.lang.String data, final Attributes meta) throws SAXException {
        logger.finest("handle_ph: " + data);

        if (bInSource || bInTarget) {
            if (data != null) {
                gAccum.append(data);
            }
        }
    }

    public void end_ph() throws SAXException {
        logger.finest("end_ph ()");
    }

    /************************************************************************************************
     *
     * Delimiter Elements
     *
     ***********************************************************************************************/
    public void start_mrk(final Attributes meta) throws SAXException {
        logger.finest("start_mrk: " + meta);

        if (bInSource) {
            //  Create a new object to hold the content of this 'mrk' element. Push this
            //  element onto the stack of inline elements.
            String mtype = meta.getValue("mtype");
            MrkContent mrkContent = new MrkContent(mtype);
            inlineElementStack.push(mrkContent);
        }
    }

    public void handle_mrk(final java.lang.String data, final Attributes meta) throws SAXException {
        if (true) {
            logger.finest("handle_mrk: " + data);
        }

        if (bInSource || bInTarget) {
            if (data != null) {
                gAccum.append(data);
            }
        }

        //  Update the MrkContent element with the value of the data passed.
        if (bInSource) {
            try {
                if (!inlineElementStack.isEmpty() && (data != null)) {
                    Object obj = inlineElementStack.pop();

                    if (obj instanceof MrkContent) {
                        MrkContent mrkContent = (MrkContent)obj;
                        mrkContent.appendText(data);
                        inlineElementStack.push(mrkContent);
                    } else {
                        throw new SAXException("Inline element stack in an incorrect state. We were expecting an 'mrk' and didn't get one.");
                    }
                }
            } catch (java.util.EmptyStackException ex) {
                ex.printStackTrace();
                throw new SAXException("Inline element stack in an incorrect state. It is empty but should have elements");
            }
        }
    }

    public void end_mrk() throws SAXException {
        if (true) {
            logger.finest("end_mrk()");
        }

        if (bInSource) {
            try {
                if (!inlineElementStack.isEmpty()) {
                    Object obj = inlineElementStack.pop();

                    if (obj instanceof MrkContent) {
                        MrkContent mrkContent = (MrkContent)obj;
                        mrkContentTracker.registerMrk(mrkContent);

                        //  Propogate data up the stack, just in case
                        java.util.Stack tempStack = new java.util.Stack();

                        boolean boolMrkSearch = !inlineElementStack.isEmpty();

                        while (boolMrkSearch) {
                            obj = inlineElementStack.pop();

                            //  Keep the object for later.
                            tempStack.push(obj);

                            if (obj instanceof MrkContent) { //  Found it

                                MrkContent mrkParent = (MrkContent)obj;
                                mrkParent.appendMrkElement(mrkContent);
                                boolMrkSearch = false;
                            } else {
                                boolMrkSearch = !inlineElementStack.isEmpty();
                            }
                        }

                        //  Now replace all the stuff that was popped of the stack
                        while (!tempStack.isEmpty()) {
                            inlineElementStack.push(tempStack.pop());
                        }
                    } else {
                        throw new SAXException("Inline element stack in an incorrect state. We were expecting an 'mrk' and didn't get one.");
                    }
                }
            } catch (java.util.EmptyStackException ex) {
                ex.printStackTrace();
                throw new SAXException("Inline element stack in an incorrect state. It is empty but should have elements");
            }
        }
    }

    public String getOriginalDataType() {
        return originalDataType;
    }

    public void populate(org.jvnet.olt.format.GlobalVariableManager gvm) {
        mrkContentTracker.populate(gvm);
    }

    /*
    public static void main(String[] args) {
      try {
        Reader me = new Reader(new File(args[0]).toURL());
        Map theList = me.getGroupZeroSource();
        Map theTList = me.getGroupZeroTarget();
        Iterator iter = theList.keySet().iterator();
        while (iter.hasNext()) {
          Integer theKey = (Integer) iter.next();
          logger.finest("SOURCE: " + theList.get(theKey));
          logger.finest("TARGET: " + theTList.get(theKey));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }*/
    public List getFiles() {
        return files;
    }

    public Map getTransUnits() {
        return transUnits;
    }

    public Version getVersion() {
        return Version.fromString("1.0");
    }

    public void setTargetLanguage(String tgtLang) {
        this.sTargetLanguageCode = tgtLang;
    }
}

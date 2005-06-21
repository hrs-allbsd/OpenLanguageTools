
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.software;

import java.util.*;
import java.io.*;
import java.text.MessageFormat;
import java.text.DateFormat;
import java.util.logging.*;

import org.jvnet.olt.io.HTMLEscapeFilterReader;
import org.jvnet.olt.io.ControlDEscapingFilterReader;
import org.jvnet.olt.io.ControlDWarningFilterReader;
import org.jvnet.olt.tmci.*;

import org.jvnet.olt.parsers.JavaParser.ResBundleParserFacade;
import org.jvnet.olt.parsers.MsgFileParser.MsgFileParserFacade;
import org.jvnet.olt.parsers.POFileParser.POFileParserFacade;
import org.jvnet.olt.parsers.PropsFileParser.PropsFileParserFacade;
import org.jvnet.olt.parsers.XResFileParser.XResFileParserFacade;
import org.jvnet.olt.filters.software.moz_dtd.MozDTDFileParserFacade;

import org.jvnet.olt.parsers.mapping.*;
import org.jvnet.olt.utilities.StandaloneLocaleTable;

/**
 * This code is heavily based on the old TMCi SoftwareToTmx (the same way as the
 * SoftwareToXliff class was based on the TMCi TMInputTool), only without all the
 * stuff in it. We're doing the same basic task as the AlignmentTool was, taking
 * two arrays of software message file objects, and writing out TMX instead. Rather
 * than let this class write out tmx, we're delegating that to the TMXFormatter instead.
 * <br><br>
 *
 * There's still lots of junk in here : we could be using Collections to make this
 * code lots clearer (and potentially take advantage of any neat algorithms in the
 * underlying java implementations ?) - if performance is really terrible, we could
 * try doing md5's of the keys, to see if that improves speed over the default
 * hashcode() that get used by the String.equals() method, which I guess would be
 * used if we put the strings into a Collection.
 * <br><br>
 *
 * On the other hand, having two arrays, guarantees the order in which we search for
 * strings, which might well mimic the behaviour of gettext, catgets and friends -
 * though we'd have to look into that (perhaps we should really search the input
 * array in the same order as those different algorithms do, since this matters
 * in terms of what to do with duplicate message keys)
 * <br><br>
 *
 * Finally, more work is needed to handle domains properly.
 *
 * If I have time, I'll try and do all these things, Right now,
 * though, I've cleaned up the code quite a bit just by replacing the indexes
 * into arrays with static int references which has done this code the world of good !
 *
 * @author timf
 *
 */
public class SoftwareToTmx {

    private Hashtable m_hashFileType;
    private HashMap reverseFileTypeMap;

    private HashMap mappings;
    private boolean useMappings;
    private String sourceFilePath;
    private String errstr = "";

    private static final String DEFAULT_DOMAIN = "default domain";

    private static final int KEY = 0;
    private static final int DOMAIN = 1;
    private static final int SRC = 2;
    private static final int TRG = 3;
    private static final int COMMENT = 4;

    /*
     * The Logger to be used
     */
    private static Logger logger =  Logger.getLogger("org.jvnet.olt.filters.software");

    public SoftwareToTmx() {
        m_hashFileType = new Hashtable();
        m_hashFileType.put("po", new Integer(MessageListGenerator.PO_FILE));
        m_hashFileType.put("java", new Integer(MessageListGenerator.JAVA_RES));
        m_hashFileType.put("properties", new Integer(MessageListGenerator.JAVA_PROPS));
        m_hashFileType.put("msg", new Integer(MessageListGenerator.MSG_FILE));
        m_hashFileType.put("tmsg", new Integer(MessageListGenerator.MSG_FILE));
        m_hashFileType.put("xres", new Integer(MessageListGenerator.XRES_FILE));
        m_hashFileType.put("dtd", new Integer(MessageListGenerator.MOZDTD_FILE));

        reverseFileTypeMap = new HashMap();
        reverseFileTypeMap.put(new Integer(MessageListGenerator.PO_FILE),"PO");
        reverseFileTypeMap.put(new Integer(MessageListGenerator.JAVA_RES),"JAVA");
        reverseFileTypeMap.put(new Integer(MessageListGenerator.JAVA_PROPS),"PROPERTIES");
        reverseFileTypeMap.put(new Integer(MessageListGenerator.MSG_FILE),"MSG");
        reverseFileTypeMap.put(new Integer(MessageListGenerator.MOZDTD_FILE),"DTD");
        // incomplete support for XResources files...
        //reverseFileTypeMap.put(new Integer(MessageListGenerator.XRES_FILE),"XRESOURCES");

        mappings = new HashMap();
        useMappings = false;
        sourceFilePath = "";
    }


    public static void main(String[] args) {
        try {
            SoftwareToTmx ali_tool = new SoftwareToTmx();
            if(!ali_tool.runAlign(args)) { System.exit(1); }
        }
        catch(TMCParseException tmcEx) {
            System.err.print(tmcEx.getMessage());
            tmcEx.printStackTrace();
            System.exit(13);
        }
        catch(Exception ex) { // FIXME : crap exception handling here
            System.err.println("Error occurred : "+ex.getMessage());
            ex.printStackTrace();
            System.exit(14);
        }
        /*
        catch(org.jvnet.olt.parsers.POFileParser.TokenMgrError poErr) {
            System.err.print(poErr.getMessage());
            System.exit(13);
        }
        catch(org.jvnet.olt.parsers.MsgFileParser.TokenMgrError msgErr) {
            System.err.print(msgErr.getMessage());
            System.exit(14);
        }
        catch(org.jvnet.olt.parsers.JavaParser.TokenMgrError javaErr) {
            System.err.print(javaErr.getMessage());
            System.exit(15);
        }
        catch(org.jvnet.olt.parsers.PropsFileParser.TokenMgrError propsErr) {
            System.err.print(propsErr.getMessage());
            System.exit(16);
        }*/
        System.exit(0);
    }

    /**
     *  This method starts the alignment of the files. Essentially it is
     *  a hack method to get error messages written to our log file.
     */
    public boolean runAlign(String[] args)
    throws TMCParseException, Exception {
        try {
            if ((args.length != 5 && args.length != 8) ||
                (args.length == 8 && !args[5].equals("-map"))) {
                usage();
                System.exit(1);
            }

            //  Parser the suntrans mapping file
            if (args.length == 8) {
                SuntransMappingFile mf = new SuntransMappingFile();
                mf.parserMappings(args[6],args[7]);

                mappings.putAll(mf.getMappings());
                useMappings = true;
                sourceFilePath = args[0];
                System.out.println("sourceFilePath="+sourceFilePath);
            }

            File srcFile = new File(args[0]);
            String srcLang = args[1];
            File trgFile = new File(args[2]);
            String trgLang = args[3];
            String trgEncoding = args[4];

            //  Here must check the language code and the target encoding
            //  Refer to StandaloneLocaleTable class
            //  System.out.println(args[4]);
            StandaloneLocaleTable slt = new StandaloneLocaleTable();
            if (!slt.isValidIdentifier(srcLang)) {
                errstr = "The source language \"" + srcLang + "\" is NOT valid.";
                throw new Exception(errstr);
            }
            if (!slt.isValidIdentifier(trgLang)) {
                errstr = "The target language \"" + trgLang + "\" is NOT valid.";
                throw new Exception(errstr);
            }
            if (!slt.isValidCodeset(trgEncoding)) {
                errstr = "The localised encoding \"" + trgEncoding + "\"is NOT valid.";
                throw new Exception(errstr);
            }
            if (!slt.getDefaultEncoding(trgLang).equals(trgEncoding)) {
                errstr = "The specified localised encoding (\"" + trgEncoding + "\") is NOT consistent with the default encoding (\"" + slt.getDefaultEncoding(trgLang) +"\") for \"" + trgLang + "\".";
                System.out.println("Warning : " + errstr);
            }

            //  Start to do the alignment
            String[][] arrAligned = alignFiles(srcFile, srcLang, trgFile, trgLang, trgEncoding);
            Writer tmxWriter = new OutputStreamWriter(new FileOutputStream(args[0]+".tmx"),"UTF-8");
            int type = determineFileType(srcFile);
            String dataType = (String)reverseFileTypeMap.get(new Integer(type));
            writeTMX(dataType, tmxWriter,srcLang, trgLang, args[0], arrAligned);

        } catch(TMCParseException tmcpex) {
            logger.log(Level.SEVERE, "TMCParseException", tmcpex);
            if (tmcpex.getMessage().equals(TMCParseException.NO_MESSAGES)) {
                //  Skip the current alignment process
                System.err.println(tmcpex.getMessage() + "\n\nThe current alignment process is skipped!");
            }
            else {
                throw tmcpex;
            }
        } catch(Exception ex) { // FIXME bad exception handling here
            //System.err.print(ex.getMessage());
            //ex.printStackTrace();
            //return false;
            logger.log(Level.SEVERE, "Error Occured", ex);
            throw ex;
        }
        System.out.println("Info : finish to align " + args[0] + " and " + args[2]);
        return true;
    }


    // FIXME : pass useful stuff in here instead of a hashtable...
    public String[][] alignFiles(File srcFile, String srcLang, File trgFile, String trgLang, String trgEncoding)
    throws TMCParseException,IOException,Exception,AliToolException {
        int iAlignType = determineFileType(srcFile);
        String strDefaultDomain = determineDefaultDomain(srcFile,iAlignType);

        //  Parse source file
        String[][] arrSource = parseFile(srcFile, srcLang, iAlignType, strDefaultDomain, "ISO-8859-1");

        //  Parse target file
        String[][] arrTarget = parseFile(trgFile, trgLang, iAlignType, strDefaultDomain, trgEncoding);

        //  Do best comparison of the two lists: may involve sorting
        //    Dodgy way implemented here
        //  Create output array
        String[][] arrAligned = new String[arrSource.length][5];
        boolean boolThisKeyFound = false;

        Set sSource = new HashSet();
        Set sTarget = new HashSet();
        // Store the key/value pairs
        Map mSource = new HashMap();
        Map mTarget = new HashMap();
        boolean duplicateMessageFound=false;

        // Quick check to see that there aren't any duplicate keys in src array
        for(int i =0; i < arrSource.length; i++){
            if((determineFileType(srcFile) == MessageListGenerator.PO_FILE)) {
                // Skip the check for plural msgid
                if (!arrSource[i][MessageArrayKey.PLURAL].equals("false"))
                    continue;

                if (!sSource.add(arrSource[i][MessageArrayKey.KEY])) {
                    System.err.println("Warning! key : " + arrSource[i][MessageArrayKey.KEY] + " appears more than once in the source message file.\n");
                    // Identical key, but not identical value
                    if (!((String)mSource.get(arrSource[i][MessageArrayKey.KEY])).equals(arrSource[i][MessageArrayKey.STRING])) {
                        System.err.println("Error!   key : " + arrSource[i][MessageArrayKey.KEY] + " is duplicate in the source message file.\n");
                        duplicateMessageFound = true;
                    }
                }
                else {
                    mSource.put(arrSource[i][MessageArrayKey.KEY], arrSource[i][MessageArrayKey.STRING]);
                }
            }
            else {
                if (!sSource.add(arrSource[i][MessageArrayKey.KEY])) {
                    System.err.println("Warning! key : " + arrSource[i][MessageArrayKey.KEY] + " appears more than once in the source message file.\n");
                    // Identical key, but not identical value
                    if (!((String)mSource.get(arrSource[i][MessageArrayKey.KEY])).equals(arrSource[i][MessageArrayKey.STRING])) {
                        System.err.println("Error!   key : " + arrSource[i][MessageArrayKey.KEY] + " is duplicate in the source message file.\n");
                        duplicateMessageFound = true;
                    }
                }
                else {
                    mSource.put(arrSource[i][MessageArrayKey.KEY], arrSource[i][MessageArrayKey.STRING]);
                }
            }
            if (duplicateMessageFound == true){
                System.err.println("ERROR!! Duplicate keys constitute bugs in message files \n"+
                "- please fix the message files, and run this tool again\n");
                throw new AliToolException("Duplicate keys constitute bugs in message files - see logfile for further details.");
            }
        }
        // now check for duplicate keys in the target array
        for(int j=0; j <arrTarget.length; j++){
            if((determineFileType(srcFile) == MessageListGenerator.PO_FILE)) {
                if (arrTarget[j][MessageArrayKey.PLURAL] == null){ // this is suspicious
                    System.err.println("Warning ! No value listed for plural form of message key "+arrTarget[j][MessageArrayKey.KEY]);
                    // ultimately, this will result in the tmx file being flagged later on
                    continue;
                }
                // Skip the check for plural msgid
                if (!arrTarget[j][MessageArrayKey.PLURAL].equals("false")){
                    continue;
                }
                
            

                if (!sTarget.add(arrTarget[j][MessageArrayKey.KEY])) {
                    System.err.println("Warning! key : " + arrTarget[j][MessageArrayKey.KEY] + " appears more than once in the target message file.\n");
                    // Identical key, but not identical value
                    if (!((String)mTarget.get(arrTarget[j][MessageArrayKey.KEY])).equals(arrTarget[j][MessageArrayKey.STRING])) {
                        duplicateMessageFound = true;
                        System.err.println("Error!   key : " + arrTarget[j][MessageArrayKey.KEY] + " is duplicate in the target message file.\n");
                    }
                }
                else {
                    mTarget.put(arrTarget[j][MessageArrayKey.KEY], arrTarget[j][MessageArrayKey.STRING]);
                }
            }
            else {
                if (!sTarget.add(arrTarget[j][MessageArrayKey.KEY])) {
                    System.err.println("Warning! key : " + arrTarget[j][MessageArrayKey.KEY] + " appears more than once in the target message file.\n");
                    // Identical key, but not identical value
                    if (!((String)mTarget.get(arrTarget[j][MessageArrayKey.KEY])).equals(arrTarget[j][MessageArrayKey.STRING])) {
                        System.err.println("Error!   key : " + arrTarget[j][MessageArrayKey.KEY] + " is duplicate in the target message file.\n");
                        duplicateMessageFound = true;
                    }
                }
                else {
                    mTarget.put(arrTarget[j][MessageArrayKey.KEY], arrTarget[j][MessageArrayKey.STRING]);
                }
            }
            if (duplicateMessageFound == true){
                System.out.println("ERROR!! Duplicate keys constitute bugs in message files \n"+
                "- please fix the message files, and run this tool again\n");
                throw new AliToolException("Duplicate keys constitute bugs in message files - see logfile for further details.");
            }
        }

        // finished check for duplicates... now do the alignment proper
        // for every element in the source array (indexed by i)
        for(int i = 0; i < arrSource.length; i++) {

            boolThisKeyFound = false;
            int j = 0;
            // iterate over every element in the target array looking for the source key
            // - this is suspect resulting in lots of string comparisons. (indexed by j)
            while( (j < arrTarget.length) && !boolThisKeyFound) {

                if((determineFileType(srcFile) == MessageListGenerator.PO_FILE)) {
                    boolThisKeyFound = (arrSource[i][MessageArrayKey.KEY].equals(arrTarget[j][MessageArrayKey.KEY]) &&
                                        arrSource[i][MessageArrayKey.DOMAIN].equals(arrTarget[j][MessageArrayKey.DOMAIN]) &&
                                        arrSource[i][MessageArrayKey.PLURAL].equals(arrTarget[j][MessageArrayKey.PLURAL]));
                }
                else {
                    boolThisKeyFound = (arrSource[i][MessageArrayKey.KEY].equals(arrTarget[j][MessageArrayKey.KEY]) &&
                                        arrSource[i][MessageArrayKey.DOMAIN].equals(arrTarget[j][MessageArrayKey.DOMAIN]));
                }
                if(boolThisKeyFound) {
                    arrAligned[i][KEY] = arrSource[i][MessageArrayKey.KEY];
                    arrAligned[i][DOMAIN] = arrSource[i][MessageArrayKey.DOMAIN];
                    arrAligned[i][SRC] = arrSource[i][MessageArrayKey.STRING];
                    arrAligned[i][TRG] = arrTarget[j][MessageArrayKey.STRING];

                    if((determineFileType(srcFile) == MessageListGenerator.PO_FILE)
                    || (determineFileType(srcFile) == MessageListGenerator.JAVA_PROPS)
                    || (determineFileType(srcFile) == MessageListGenerator.JAVA_RES)
                    || (determineFileType(srcFile) == MessageListGenerator.MSG_FILE)) {
                        arrAligned[i][COMMENT] = arrTarget[j][MessageArrayKey.COMMENT];
                    }
                    else {
                        arrAligned[i][COMMENT] = "";
                    }
                }
                j++;
            }
            if(!boolThisKeyFound) {
                boolean checkForMissingMessages = true;
                if (checkForMissingMessages){
                    throw new AliToolException("Base message '" + arrSource[i][MessageArrayKey.KEY] +
                    "' does not have a matching message in the Target file.\n");}
                arrAligned[i][KEY] = "@TMC@ Missing Message";
                arrAligned[i][DOMAIN] = arrSource[i][MessageArrayKey.DOMAIN];
                arrAligned[i][SRC] = "@TMC@ Missing Message";
                arrAligned[i][TRG] = "@TMC@ Missing Message";
                System.err.println("Base message '" + arrSource[i][MessageArrayKey.KEY] + "' does not have a matching message in the Target file.\n");

            }
        }
        return arrAligned;
    }


    public int determineFileType(File file) throws AliToolException {
        String ext = "";
        StringTokenizer st = new StringTokenizer(file.getName(), ".");
        while (st.hasMoreTokens()){
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        Integer type = (Integer)m_hashFileType.get(ext);
        if(type == null) {
            throw new AliToolException("Unknown message file type : "+ext);
        }
        return type.intValue();
    }

    // FIXME : need to do the right thing here with respect to domains...
    // TMCi did this by splitting input files in to multiple domains,
    // which was a bad thing - don't do that in SunTrans2, so we may
    // need to fix that.
    public String determineDefaultDomain(File file, int type) {
        //String strDefDomain = "default domain";
        String strDefDomain = DEFAULT_DOMAIN;
        switch(type) {
            case MessageListGenerator.JAVA_PROPS:
            case MessageListGenerator.PO_FILE:
                if((strDefDomain == null) || (strDefDomain == "")) {
                    String strFileName = file.getName();
                    int last_dot_index = strFileName.lastIndexOf((int) '.');
                    if(last_dot_index == -1) {
                        return strFileName;
                    }
                    else {
                        return strFileName.substring(0,last_dot_index);
                    }
                }
                else {
                    return strDefDomain;
                }
            case MessageListGenerator.XRES_FILE:
            case MessageListGenerator.JAVA_RES:
            case MessageListGenerator.MSG_FILE:
                if(strDefDomain == null) {
                    return "";
                }
                else {
                    return strDefDomain;
                }
            default:
                return "";
        }
    }


    /**
     *  This function parses the file based on what the user has set as the
     *  file type.
     *  @param file       The file to parsed
     *  @param langid     The language id of for the file (determines codeset)
     *  @param aligntype  Flag to indicate which parser to use.
     *  @param strDefaultDomain  A string to indicate what the default domain
     *  @param encoding  A string to indicate the encoding
     *
     */
    private String[][] parseFile(File file, String lang, int aligntype, String strDefaultDomain, String encoding)
    throws TMCParseException,IOException,Exception,AliToolException {
        InputStream inStream = new BufferedInputStream(new FileInputStream(file));
        InputStreamReader readerIn = new InputStreamReader(inStream, encoding);

        //  The block of code below is necessary to handle cases where
        //  the a Control-D character is encountered in the input. The
        //  Alpnet tools do not handle this character gracefully, so we
        //  are escaping it for the TM and alignment tools.
        //
        //  Unfortunately this approach does not work well for properties
        //  files so we are not using it in this case.
        Reader reader;   //  Declare this in the outer scope!
        if(aligntype != MessageListGenerator.JAVA_PROPS) {
            ControlDEscapingFilterReader readerEscaped =
            new ControlDEscapingFilterReader(readerIn);
            //reader = new HTMLEscapeFilterReader(readerEscaped);
            reader = readerEscaped;
        }
        else {
            //  Reader set up to throw an exception if Control-D character is
            //  encountered.
            ControlDWarningFilterReader readerWarn  =
            new ControlDWarningFilterReader(readerIn);
            //reader = new HTMLEscapeFilterReader(readerWarn);
            reader = readerWarn;
        }

        String[] args = new String[1];
        args[0] = file.getAbsolutePath();
        System.err.println(MessageFormat.format("Parsing file ... {0}\n",args));

        String[][] messages = null;

        SunTrans2ParserFacade facade;

        switch(aligntype) {
            case MessageListGenerator.PO_FILE:
                facade = new POFileParserFacade();
                break;
            case MessageListGenerator.JAVA_RES:
                facade = new ResBundleParserFacade();
                break;
            case MessageListGenerator.JAVA_PROPS:
                facade = new PropsFileParserFacade();
                break;
            case MessageListGenerator.MSG_FILE:
                facade  = new MsgFileParserFacade();
                break;
            case MessageListGenerator.XRES_FILE:
                facade  = new XResFileParserFacade();
                break;
            case MessageListGenerator.MOZDTD_FILE:
                facade = new MozDTDFileParserFacade();
                break;
            default:
                throw new AliToolException("Unknown message file type specified");
        }

        messages = facade.getSunTrans2MessageStringArr(reader,strDefaultDomain);
        return messages;
    }

    private void writeTMX(String type, Writer tmxWriter, String srcLang, String trgLang, String srcFileName, String[][] arrAligned)
    throws AliToolException{
        try {
            TMXFormatter formatter = new TMXFormatter(type, srcLang, srcFileName, tmxWriter);
            for (int i=0; i<arrAligned.length; i++){
                String msgid = arrAligned[i][KEY];
                String srcSeg = arrAligned[i][SRC];
                String trgSeg = arrAligned[i][TRG];
                String domain = arrAligned[i][DOMAIN];
                String comment = arrAligned[i][COMMENT];
                //System.err.println("Doing nothing with comment "+comment);

                if (useMappings) {
                    domain = (String)mappings.get(sourceFilePath);
                }

                if ( useMappings || (type.equals("PO") && !domain.equals(DEFAULT_DOMAIN)) ) {
                    //  It's necessary to write domain information as part of the key value
                    //  into the TMX file, just only for PO file
                    //  Or the user specify the file mapping infomation
                    System.out.println("domain : " + domain);
                    formatter.writeTU(srcSeg, srcLang, trgSeg, trgLang, msgid, type, domain, comment);
                }
                else {
                    System.err.println("Doing nothing with domain : " + domain);
                    formatter.writeTU(srcSeg, srcLang, trgSeg, trgLang, msgid, type, comment);
                }
            }
            formatter.flush();
        } catch (Exception e){ // FIXME : crap exception handling here
            //System.err.println("Exception writing TMX file : "+e.getMessage());
            //e.printStackTrace();
            throw new AliToolException("Exception writing TMX file : "+e.getMessage());
        }
    }

    // just for testing
    private void usage(){
        System.err.println("Usage: SoftwareToTmx <src file> <src lang> <target file> <target lang> <localised encoding>");
        System.err.println("       SoftwareToTmx <src file> <src lang> <target file> <target lang> <localised encoding> -map <mapping file> <mapping DTD file>");
        System.err.println("");
        System.err.println("Here <mapping file> is an XML format file, contains \"filename-in-workspace\"->\"unique-installpath\" pairs.");
        System.err.println("Please refer the sample.xml file.");
    }

}

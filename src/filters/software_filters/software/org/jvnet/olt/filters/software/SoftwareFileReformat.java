
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.software;


import org.jvnet.olt.parsers.POFileParser.*;
import org.jvnet.olt.parsers.JavaParser.*;
import org.jvnet.olt.parsers.MsgFileParser.*;
import org.jvnet.olt.parsers.PropsFileParser.*;
// not bothering with this file parser
//import org.jvnet.olt.parsers.RepFileParser.*;
import org.jvnet.olt.parsers.XResFileParser.*;

import org.jvnet.olt.io.ControlDEscapingFilterReader;
import org.jvnet.olt.io.ControlDWarningFilterReader;
import org.jvnet.olt.io.HTMLEscapeFilterReader;
import org.jvnet.olt.tmci.*;
import org.jvnet.olt.io.HTMLEscapeFilterWriter;
import org.jvnet.olt.utilities.XliffZipFileIO;

import java.io.*;
import java.text.*;
import java.text.MessageFormat;
import java.util.*;



public class SoftwareFileReformat {
    private Map fileTypeMap;
    private Map reverseFileTypeMap;
    
    
    
    String sourceLang = "en-US";
    
    /**
     * Simple test class, copied/pasted from the SoftwareToXliff class to quickly
     * see what it's like to reformat software message files
     */
    public SoftwareFileReformat(String filename, String lang, String encoding) throws TMCParseException {
        fileTypeMap = new HashMap();
        fileTypeMap.put("po", new Integer(MessageListGenerator.PO_FILE));
        fileTypeMap.put("java", new Integer(MessageListGenerator.JAVA_RES));
        fileTypeMap.put("properties", new Integer(MessageListGenerator.JAVA_PROPS));
        fileTypeMap.put("msg", new Integer(MessageListGenerator.MSG_FILE));
        fileTypeMap.put("tmsg", new Integer(MessageListGenerator.MSG_FILE));
        
        reverseFileTypeMap = new HashMap();
        reverseFileTypeMap.put(new Integer(MessageListGenerator.PO_FILE),"PO");
        reverseFileTypeMap.put(new Integer(MessageListGenerator.JAVA_RES),"JAVA");
        reverseFileTypeMap.put(new Integer(MessageListGenerator.MSG_FILE),"MSG");
        reverseFileTypeMap.put(new Integer(MessageListGenerator.JAVA_PROPS),"PROPERTIES");
        
        // I don't think ppl are going to mark their XResources files
        // as such - but if they do, we'll spot it :-)
        fileTypeMap.put("xres", new Integer(MessageListGenerator.XRES_FILE));
        
        this.runConvertInput(filename, lang, encoding);
    }
    
    public static void main(String[] args) {
        try {
            SoftwareFileReformat converter = new SoftwareFileReformat(args[0], args[1], "ISO8859-1");
        } catch (TMCParseException tmcEx) {
            tmcEx.printStackTrace();
            System.exit(13);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        System.exit(0);
    }
    
    private int getSourceFileType(String ext) throws TMToolException {
        Integer integerSrcFileType = (Integer)fileTypeMap.get(ext);
        
        if (integerSrcFileType == null) {
            throw new TMToolException("Unknown message file type specified " + ext);
        } else {
            return integerSrcFileType.intValue();
        }
    }
    
    
    public boolean runConvertInput(String filename, String lang, String encoding) throws TMCParseException {
        try {
            // Determine the file type from the command file
            int iAlignType = getSourceFileType(getExtension(filename));
            
            //formatWrapper = new SoftwareMessageFormatWrapper(iAlignType);
            
            // really putting this in till I work out the details
            Map data = new HashMap();
            data.put("ext",getExtension(filename));
            //  Create TokenCell array
            
            TokenCell[] pivFileinfo = parseFile(filename, sourceLang, iAlignType, data, encoding);
            //  Write out the Pivot file
            return reformatFiles(pivFileinfo, sourceLang,filename, data, encoding);
            // crap exception handling here, sorry
        } catch (Exception ex) {
            TMCParseException e = new TMCParseException(ex.getMessage());
            e.initCause(ex);
            throw e;
        }
    }
    
    private String getDefaultDomainString(Map data) {
        try {
            int iFileType = getSourceFileType((String)data.get("ext"));
            
            switch (iFileType) {
                case MessageListGenerator.JAVA_PROPS: //  Explicit drop through
                case MessageListGenerator.PO_FILE:
                    String strDefDomain = "defaultDomain";
                    
                    if ((strDefDomain != null) && !strDefDomain.equals("")) {
                        return strDefDomain;
                    }
                    
                    //  Got past the guard so we must have had an empty TextType tag.
                    File fileSrc = (File) data.get("source file");
                    
                    //  return just the file name
                    //  and not the full path
                    String strFileName = fileSrc.getName();
                    int last_dot_index = strFileName.lastIndexOf((int) '.');
                    
                    if (last_dot_index == -1) {
                        return strFileName;
                    } else {
                        return strFileName.substring(0, last_dot_index);
                    }
                    
                case MessageListGenerator.MSG_FILE:
                case MessageListGenerator.XRES_FILE:
                case MessageListGenerator.JAVA_RES:
                    return ((String) data.get("Domain SelCrit"));
                    
                default:
                    return "";
            }
        } catch (TMToolException tmEx) {
            
            tmEx.printStackTrace();
            
            
        }
        
        return "";
    }
    
    
    private boolean reformatFiles(TokenCell[] pivFileinfo, String lang,String filename, Map data, String encoding) throws TMCParseException {
        boolean boolMessageWritten = false;
        //boolean boolFirstFile = true;
        boolean boolInSunBin = false;
        int iDomainNumber = 1;
        int iMessageNumber = 1;
        
        String strDomain = "defaultDomain";
        //PrintWriter writer = null;
        
        try {
            //initiate the formatter
            File file = new File(filename);
            String shortname = filename.substring(0,filename.length()); //need to improve
            
            FileOutputStream outputStream = new FileOutputStream(filename+".formatted");
            HTMLEscapeFilterWriter fileWriter = new HTMLEscapeFilterWriter(new OutputStreamWriter(outputStream, encoding));
            
            int iFileType = getSourceFileType((String)data.get("ext"));
            String format = (String)reverseFileTypeMap.get(new Integer(iFileType));
            
            
            int iNumCells = pivFileinfo.length;
            System.out.println("count="+iNumCells);
            for (int i = 0; i < iNumCells; i++) {
                
                switch (pivFileinfo[i].getType()) {
                    case TokenCell.DOMAIN_KEY_WORD:
                        //System.out.println("domain keyword");
                        fileWriter.write(pivFileinfo[i].getText());
                        break;
                        
                    case TokenCell.DOMAIN:
                        //System.out.println("domain");
                        //strDomain = pivFileinfo[i].getText();
                        //boolInSunBin = writeSunBinText(pivFileinfo[i].getText(),
                        //boolInSunBin, writer);
                        fileWriter.write(pivFileinfo[i].getText());
                        break;
                        
                    case TokenCell.MARKER:
                        //System.out.println("marker");
                        //boolInSunBin = inSunBinTrueTest(boolInSunBin, writer);
                        //writer.print("marker = " + pivFileinfo[i].getText() +"\n");
                        
                        break;
                        
                    case TokenCell.COMMENT:
                        // this is a red-herring
                        // the comment node is used elsewhere to write the notes
                        // for translators into the XLIFF file : this node is not
                        // a printable node, as we also write a formatting cell
                        // into the TokenCell list for the comment, which gets
                        // picked up by the default: state below
                        ; // intentionally do nothing here.
                        break;
                        
                    case TokenCell.MESSAGE:
                        //System.out.println("message");
                        if (pivFileinfo[i].isTranslatable()) {
                            //boolInSunBin = inSunBinTrueTest(boolInSunBin, writer);
                            //String temp = formatWrapper.wrapFormatting(pivFileinfo[i].getText());
                            String message = "";
                            if (pivFileinfo[i].hasTranslation()){
                                message = pivFileinfo[i].getTranslation();
                            } else {
                                message = pivFileinfo[i].getText();
                            }
                            String msgid = pivFileinfo[i].getKeyText();
                            
                            if (msgid==null){
                                msgid="";
                            }
                            ///System.out.println("temp="+temp);
                            String startSegment = "";
                            String endSegment = "";
                            int indent = 7;
                            int maxLength = 70;
                            switch (iFileType){
                                case MessageListGenerator.PO_FILE:
                                    startSegment="\"";
                                    endSegment="\"\n";
                                    
                                    break;
                                case MessageListGenerator.JAVA_RES:
                                    startSegment="\"";
                                    endSegment="\"+\n";
                                    
                                    break;
                                case MessageListGenerator.JAVA_PROPS:
                                    indent=0;
                                    startSegment="";
                                    endSegment="\\\n";
                                    break;
                                case MessageListGenerator.MSG_FILE:
                                    // FIXME : we need to do something clever here
                                    // with the start & end segment things due to
                                    // the changable $quote directive found in msg
                                    // files.
                                    indent=0;
                                    startSegment="";
                                    endSegment="\\\n";
                                    
                                    break;
                                case MessageListGenerator.XRES_FILE:
                                    
                                    break;
                            }
                            StringTokenizer tok = new StringTokenizer(lang,"-");
                            String lng = tok.nextToken();
                            String dialect = tok.nextToken();
                            
                            
                            SoftwareMessageLayout layout = new SoftwareMessageLayout(lng,dialect);
                            if (pivFileinfo[i].getReLayoutAllowed()){
                                fileWriter.write(layout.formatMessage(message,startSegment,endSegment,indent,maxLength));
                            } else {
                                fileWriter.write(message);
                            }
                        } else {
                            fileWriter.write(pivFileinfo[i].getText());
                            //formatter.writeFormatting(pivFileinfo[i].getText());
                        }
                        
                        //if (!boolMessageWritten) {
                        //    boolMessageWritten = true;
                        //}
                        
                        break;
                        
                    case TokenCell.CONTEXT:
                        //make sure we ignore
                        break;
                        
                    default:
                        fileWriter.write(pivFileinfo[i].getText());
                        //formatter.writeFormatting(pivFileinfo[i].getText());
                        
                        break;
                }
                
                
            }
            
            //formatter.flush();
            
            fileWriter.close();
            file.delete();
            File newFile = new File(filename+".formatted");
            if (!newFile.renameTo(file)){
                throw new IOException("Problem renaming " + filename+".formatted to "+filename);
            }
            //package these two files into one .xlz file
            
            return true;
        } catch (Throwable t) {
            TMCParseException e = new TMCParseException("Exception caught "+t.getMessage());
            e.initCause(t);
            throw e;
        }
    }
    
    
    private PrintWriter createNewPivFile(Map data, int iDomainNumber)
    throws IOException, UnsupportedEncodingException {
        String strFileName = ((String) data.get("output file name")) + "." +
                iDomainNumber;
        File file = new File(strFileName);
        FileOutputStream stream = new FileOutputStream(file);
        PrintWriter writer = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(System.out, "UTF-8")));
        
        return writer;
    }
    
    
    private TokenCell[] parseFile(String filename, String langid, int aligntype, Map data, String encoding)
    throws TMCParseException, IOException, Exception {
        // fix this for encoding !
        FileInputStream inStream = new FileInputStream(filename);
        InputStreamReader readerIn = new InputStreamReader(inStream, encoding);
        //  The block of code below is necessary to handle cases where
        //  the a Control-D character is encountered in the input. The
        //  Alpnet tools do not handle this character gracefully, so we
        //  are escaping it for the TM and alignment tools.
        //
        //  Unfortunately this approach does not work well for properties
        //  files so we are not using it in this case.
        HTMLEscapeFilterReader reader;
        
        if (aligntype != MessageListGenerator.JAVA_PROPS) {
            ControlDEscapingFilterReader readerEscaped = new ControlDEscapingFilterReader(readerIn);
            reader = new HTMLEscapeFilterReader(readerEscaped);
        } else {
            //  The ControlDWarningFilterReader throws an exception if the
            //  Control-D character is detected in the properties file.
            ControlDWarningFilterReader readerWarn = new ControlDWarningFilterReader(readerIn);
            reader = new HTMLEscapeFilterReader(readerWarn);
        }
        
        TokenCell[] tokenCells = null;
        SunTrans2ParserFacade facade;
        
        switch (aligntype) {
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
                facade = new MsgFileParserFacade();
                break;
            case MessageListGenerator.XRES_FILE:
                facade = new XResFileParserFacade();
                break;
            default:
                throw new TMToolException("Unknown message file type specified");
        }
        
        tokenCells = facade.getSunTrans2TokenCellArray(reader);
        
        return tokenCells;
    }
    
    private String getExtension(String filename){
        
        String ext = "";
        StringTokenizer st = new StringTokenizer(filename, ".");
        while (st.hasMoreTokens()){
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        return ext;
    }
    
    
}

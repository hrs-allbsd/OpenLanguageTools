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

package org.jvnet.olt.filters.software;

import org.jvnet.olt.parsers.POFileParser.*;
import org.jvnet.olt.parsers.JavaParser.*;
import org.jvnet.olt.parsers.MsgFileParser.*;
import org.jvnet.olt.parsers.PropsFileParser.*;
// not bothering with this file parser
//import org.jvnet.olt.parsers.RepFileParser.*;
import org.jvnet.olt.parsers.XResFileParser.*;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.ReverseXliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;
import org.jvnet.olt.tmci.*;
import org.jvnet.olt.io.*;
import org.jvnet.olt.utilities.XliffZipFileIO;
import org.jvnet.olt.io.HTMLEscapeFilterWriter;

import java.io.*;
import java.text.*;
import java.text.MessageFormat;
import java.util.*;

import java.util.logging.*;

import org.jvnet.olt.xliff.*;
import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.printf.*;
import org.jvnet.olt.format.messageformat.*;
import org.jvnet.olt.filters.segmenter_facade.SegmenterFacade;



/** This class does conversion from a bunch of different software message file
 * formats into XLIFF. It was created using the old SunTrans1 software TmInputTool as a
 * template, so it's not terribly well structured just yet. (it's a bit of a hack - sorry)
 *
 * It's essentially a facade that uses TokenCell arrays containing the software
 * message file, as  created by the various parsers, and then uses the standard
 * XliffSegmenterFormatter in the sgml filter to write an xliff file with the
 * segments found. Unfortunately, this means we have a dependency on the sgml
 * filter, so that has to be fixed at some stage.
 */
public class SoftwareToXliff_Reverse {
    private Map fileTypeMap;
    private Map reverseFileTypeMap;
    
    /*
     * The Logger to be used
     */
    private static Logger logger =  Logger.getLogger("org.jvnet.olt.filters.software");
    
    // we default to en-US,
    private String sourceLang = "en-US";
    
    /** Constructor
     * @param directory the directory this file is in
     * @param filename the filename being parsed (which gets stored later in the xliff header)
     * @param srcLang the source language
     * @param encoding the encoding of the file
     * @param logger a logger used to report interesting events/errors during the creation of that
     * xliff file
     * @throws TMCParseException if a parser error was thrown while the input file was being read
     */
    public SoftwareToXliff_Reverse(String directory, String filename, String srcLang, String trgLang, boolean writeReverse, String encoding, Logger logger) throws TMCParseException {
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
        // with the .xres extension, but if they do, we'll spot it :-)
        fileTypeMap.put("xres", new Integer(MessageListGenerator.XRES_FILE));
        
        try {
            this.runConvertInput(directory+"/"+filename, srcLang, trgLang, writeReverse, encoding, logger);
        } catch(TMCParseException tmcpex) {
            logger.log(Level.SEVERE, "TMCParseException", tmcpex);
            if (!tmcpex.getMessage().equals(TMCParseException.NO_MESSAGES))
                throw tmcpex;
        }
    }
    
    /** This is a simple test method.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            File file = new File(args[0]);
            boolean writeReverse = false;
            SoftwareToXliff_Reverse converter = new SoftwareToXliff_Reverse(file.getAbsolutePath(), file.getName(), args[1], args[2], writeReverse, "UTF-8", Logger.global);
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
    
    /** Does the actual xliff conversion. The parameters are just the same as
     *  those passed to the constructor of this class, for convenience.
     * @param directory
     * @param filename
     * @param sourceLang
     * @param encoding
     * @param logger
     * @throws TMCParseException
     * @return
     */
    public boolean runConvertInput(String filename, String sourceLang, String targLang, boolean writeReverse, String encoding, Logger logger) throws TMCParseException {
        try {
            // Determine the file type from the command file
            int iAlignType = getSourceFileType(getExtension(filename));
            TokenCell[] pivFileinfo = parseFile(filename, sourceLang, iAlignType, encoding);
            //  Write out the Pivot file
            return writeXliffFiles(pivFileinfo, sourceLang, targLang, writeReverse, filename, iAlignType);
        } catch (TMCParseException tmcEx) {
            
            throw tmcEx;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    // Need to do something here for software files.
    private String getDefaultDomainString(Map data) {
        try {
            int iFileType = getSourceFileType((String)data.get("ext"));
            
            switch (iFileType) {
                case MessageListGenerator.JAVA_PROPS: //  Explicit drop through
                case MessageListGenerator.PO_FILE:
                    String strDefDomain = "TIM-Default-domain";
                    
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
    
    
    private boolean writeXliffFiles(TokenCell[] pivFileinfo, String sourceLang, String targLang, boolean writeReverse, String filename, int fileType) {
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
            String shortname = file.getName();
            
            FileOutputStream xliffOutputStream = new FileOutputStream(filename+".xlf");
            FileOutputStream sklOutputStream = new FileOutputStream(filename+".skl");
            
            HTMLEscapeFilterWriter xliffWriter = new HTMLEscapeFilterWriter(new OutputStreamWriter(xliffOutputStream, "UTF-8"));
            //OutputStreamWriter xliffWriter = new OutputStreamWriter(xliffOutputStream, "UTF-8");
            HTMLEscapeFilterWriter sklWriter = new HTMLEscapeFilterWriter(new OutputStreamWriter(sklOutputStream, "UTF-8"));
            //OutputStreamWriter sklWriter = new OutputStreamWriter(sklOutputStream, "UTF-8");
            
            // get the string name of this format
            String format = (String)reverseFileTypeMap.get(new Integer(fileType));
            //SegmenterFormatter formatter =
            //new SoftwareMessageXliffSegmenterFormatter(format, sourceLang, shortname, filename+".xlf", filename+".skl", xliffWriter, sklWriter);
            // handy for testing - the rev-formatter creates a dummy translated file
            FormatWrapper wrapper = getFormatWrapper(format);
            SegmenterFormatter formatter = new ReverseXliffSegmenterFormatter(format, sourceLang, targLang, shortname, xliffWriter, sklWriter, writeReverse, null, wrapper);
            
            int iNumCells = pivFileinfo.length;
            System.out.println("count="+iNumCells);
            for (int i = 0; i < iNumCells; i++) {
                /*System.out.println("token["+i+"]="+pivFileinfo[i].getText());
                System.out.println("---------------------");
                System.out.println("type="+pivFileinfo[i].getType());
                System.out.println("---------------------");*/
                /*if (boolFirstFile) {
                    writer = createNewPivFile(data, iDomainNumber++);
                    writeHeader(writer, data);
                    boolFirstFile = false;
                }*/
                
                switch (pivFileinfo[i].getType()) {
                    case TokenCell.DOMAIN_KEY_WORD:
                        // we're not doing anything for domains just yet...
                        break;
                        
                    case TokenCell.DOMAIN:
                        // likewise, no domain specific stuff happening yet...
                        //strDomain = pivFileinfo[i].getText();
                        break;
                        
                    case TokenCell.MARKER:
                        // markers are a hangover from SunTrans1 - we don't use them in st2
                        
                        //writer.print("marker = " + pivFileinfo[i].getText() +"\n");
                        break;
                        
                    case TokenCell.COMMENT:
                        // add a nice comment for the translator.
                        formatter.writeNote( pivFileinfo[i].getText());
                        break;
                        
                    case TokenCell.MESSAGE:
                        // write the message to the xliff file.
                        if (pivFileinfo[i].isTranslatable()) {
                            String message = pivFileinfo[i].getText();
                            String msgid = pivFileinfo[i].getKeyText();
                            if (msgid==null){
                                msgid="";
                            }
                            
                            int iWordCount = getWordCount(message);
                            formatter.writeMessageId(msgid);
                            formatter.writeSegment(message,iWordCount);
                            
                        } else {
                            formatter.writeFormatting(pivFileinfo[i].getText());
                        }
                        break;
                    case TokenCell.CONTEXT:
                        // comments are being stored as source context information
                        // which gets displayed using the View->Source Context
                        // in the editor
                        formatter.writeContext(pivFileinfo[i].getText());
                        break;
                    default:
                        formatter.writeFormatting(pivFileinfo[i].getText());
                        break;
                }
            }
            
            formatter.flush();
            xliffWriter.close();
            sklWriter.close();
            formatter = null;
            
            //package these two skl+xlf files into one .xlz file
            XliffZipFileIO xlz = new XliffZipFileIO(new File(filename+".xlz"));
            InputStreamReader sklreader = new InputStreamReader(new FileInputStream(filename+".skl"),"UTF-8");
            InputStreamReader xliffreader = new InputStreamReader(new FileInputStream(filename+".xlf"),"UTF-8");
            
            
            
            Writer sklwriter = xlz.getSklWriter();
            int c;
            while ((c=sklreader.read()) != -1){
                sklwriter.write(c);
            }
            sklreader.close();
            
            Writer xliffwriter = xlz.getXliffWriter();
            while ((c=xliffreader.read()) != -1){
                xliffwriter.write(c);
            }
            xlz.writeZipFile();
            xliffreader.close();
            
            // now delete those temporary files :
            File xliff = new File(filename+".xlf");
            xliff.delete();
            File skeleton = new File(filename+".skl");
            skeleton.delete();
            return true;
        } catch (Exception ex) { // not good exception handling here
            ex.printStackTrace();
            return false;
        }
    }
    
    private int getWordCount(String text) throws TMToolException {
        return wordCount(text); // duh. need to fix this.
    }
    
    /** This method returns a wordcount as computed by the SegmentStatsVisitor class
     * in the plaintext segmenter package. Note that words do not currently include numbers !
     * Also note that words don't include &nbsp; &gt; or &lt; entities !
     *
     * This is a method copied/pasted from elsewhere in the workspace - this is a
     * *bad* thing - need to fix that at some point, since duplicate code is
     * always a bad idea.
     *
     * @param segment The segment to be counted.
     * @param existingWordCount any wordcount that we already have (the new wordcount is added to the existing
     * wordcount) - this is just for convenience. Set it to 0 if you don't need it.
     * @throws HtmlParserException if some error was encountered while wordcounting (most probably coming from the
     * plaintext segmenter)
     * @return a wordcount of the number of words in this segment.
     */
    private int wordCount(String segment) throws TMToolException {
        StringReader reader = new StringReader(segment);
        SegmenterFacade segmenterFacade = new SegmenterFacade(reader, sourceLang);
        
        try {
            segmenterFacade.parseForStats();
        } catch (Throwable e){
            throw new TMToolException("Caught an exception doing wordcounting _"+segment+"_ :" + e.getMessage());
        }
        List words = segmenterFacade.getWordList();
        int count=0;
        // now to compute the count
        // == number of words - number of words that are &nbsp; or &gt; or &lt;
        for (int i=0;i<words.size();i++){
            count++;
            String word = (String)words.get(i);
            word = word.replaceAll("&nbsp;","");
            word = word.replaceAll("&gt;","");
            word = word.replaceAll("&lt;","");
            if (word.length() == 0){
                count--;
            }
        }
        return count;
    }
    
    private boolean writeSunBinText(String strText, boolean inSunBin,PrintWriter sklWriter) {
        if (!inSunBin) {
            //writer.print("<write-to-skeleton>");
        }
        
        // Can do conversions on text here : notably, convert Control
        // characters to entities, wrap xml formatting, that sort of thing
        // String strProtectedText = CharacterEntityBuilder.convertNewlineToEntity(strText);
        sklWriter.print(strText);
        
        return true;
    }
    
    private boolean inSunBinTrueTest(boolean inSunBin, PrintWriter writer) {
        /*if (inSunBin) {
            writer.print("</write-to-skeleton>");
        }*/
        
        return false;
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
    
    
    
    private TokenCell[] parseFile(String filename, String langid, int aligntype, String encoding)
    throws TMCParseException, IOException, Exception {
        
        InputStream inStream = new BufferedInputStream(new FileInputStream(filename));
        InputStreamReader readerIn = new InputStreamReader(inStream, encoding);
        
        EntityConversionFilterReader convReader = new EntityConversionFilterReader(readerIn);
        HashMap map = new HashMap();
        
        //convReader.setEntityMap(ASCIIControlCodeMapFactory.getAsciiControlCodesMap());
        Reader reader = convReader;
        
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
        // make sure to get a SunTrans2TokenCellArray - the old
        // SunTransTokenCellArray method is still there for older
        // tools that need it - the differences are well commented
        // in the SunTrans2ParserFacade comments
        tokenCells = facade.getSunTrans2TokenCellArray(reader);
        
        return tokenCells;
    }
    
    // likewise, this method almost certainly appears in many
    // places in the workspace, but it's small enough and
    // simple enough that it probably doesn't matter, unlike
    // the wordcount routine above...
    private String getExtension(String filename){
        
        String ext = "";
        StringTokenizer st = new StringTokenizer(filename, ".");
        while (st.hasMoreTokens()){
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        return ext;
    }
    
    // bit of a hack this, but it'll do.
    protected FormatWrapper getFormatWrapper(String type)  {
        FormatWrapper ex = null;
        if (type.equals("PO") || type.equals("MSG")){
            ex = new PrintfFormatWrapper();
        } else if (type.equals("JAVA") || type.equals("PROPERTIES")){
            ex = new MessageFormatWrapper();
        }
        
        return ex;
    }
    
    
    
}

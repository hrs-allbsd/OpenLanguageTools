
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.ali_tool;

import java.util.*;
import java.io.*;
import java.text.MessageFormat;
import java.text.DateFormat;
import com.oroinc.text.regex.*;

import org.jvnet.olt.tmci.TMCParseException;
import org.jvnet.olt.tmci.EncodingTable;
import org.jvnet.olt.io.HTMLEscapeFilterReader;
import org.jvnet.olt.io.ControlDEscapingFilterReader;
import org.jvnet.olt.io.ControlDWarningFilterReader;
import org.jvnet.olt.tmci.*;

/**
 *  A proof of concept program for aligning .PO files. It needs to be 
 *  significantly refactored before it is production ready.
 */
public class AlignmentTool
{
  private PrintWriter m_writerLog;
  private int m_id;
  private Hashtable m_hashFileType;


  //  Error message strings
  static final String strErrorMsg = "An error has occurred.\nERROR:\n";
  static final String strExceptionStackTrace = "STACK TRACE:\n";
  static final String strUsage = "USAGE:\n\trunAliTool -c command_file\n\n\n";
  static final String strTagExplanations = "EXPLANATIONS OF COMMAND FILE TAGS\n"+
      "Tag: srcfile\nRequired?: yes\n"+
      "Explanation: The name and location of the source file\n\n"+
      "Tag: lan\nRequired?: yes\n"+
      "Explanation: The language code associated with the language of the source file\n\n"+
      "Tag: trgfile\nRequired?: yes\n"+
      "Explanation:The name and location of the localized file which is to be aligned with the source file.\n\n"+
      "Tag: trglan\nRequired?: yes\n"+
      "Explanation: The language code associated with the language of the localized file\n\n"+
      "Tag: projid\nRequired?: yes\n"+
      "Explanation: \n"+
      "Tag: domain\nRequired?: yes\n"+
      "Explanation: \n\n"+
      "Tag: aligntype\nRequired?: yes\n"+
      "Explanation: \n\n"+
      "Tag: title\nRequired?: yes\n"+
      "Explanation: \n\n"+
      "Tag: texttype\nRequired?: no\n"+
      "Explanation: \n\n"+
      "Tag: translator\nRequired?: yes\n"+
      "Explanation: \n\n"+
      "Tag: creationdate\nRequired?: yes\n"+
      "Explanation: \n\n"+
      "Tag: lofilter2\nRequired?: yes\n"+
      "Explanation: \n\n"+
      "Tag: logfile\nRequired?: yes\n"+
      "Explanation: \n\n"+
      "Tag: l10n_comment\nRequired?: no\n"+
      "Explanation: Put the word 'yes' in this tag if you wish l10n comments to be written in to the .ALI file. \n\n"+
      "Tag: encoding\nRequired?: no\n"+
      "Explanation: If the encoding of the localised is different than the default of that locale,"+
      "then it should be specified here.\n"+
      "Tag: dupmsg_check\nRequired?: no\n"+
      "Explanation: This turns on checking for duplicate keys in the input files.\n"+
      "If this is \"no\" then errors will be reported in the log file.\n"+
      "If is is \"yes\" then the tool will exit fatally.\n"+
      "See the user_docs for a more detailed explanation\n\n"+
      "Tag: equal_msgno_check\nRequired?: no\n"+
      "Explanation: This turns on checking for equal numbers of messages, which helps\n"+
      "in determining if input files really are parallel versions\n"+
      "(eg. same number of base messages as localised).\n"+
      "See user_docs for a more detailed explanation\n\n"+
      "\n\n";

  static final String strSampleCmdFile = "SAMPLE COMMAND FILE\n\n"+
      "<AlML>\n"+
      "<OutFile></OutFile>\n"+
      "<SrcFile>/home/johnc/tmci_stuff/en/ConsoleResources.java</SrcFile>\n"+
      "<lan>EN</lan>\n"+
      "<ProjID>11</ProjID>\n"+
      "<Domain>22</Domain>\n"+
      "<AlignType>java resource file</AlignType>\n"+
      "<Project>33</Project>\n"+
      "<Title>44</Title>\n"+
      "<TextType>55</TextType>\n"+
      "<Trg>\n<TrgFile>/home/johnc/tmci_stuff/fr/ConsoleResources_fr.java</TrgFile>\n"+
      "<TrgLan>FR</TrgLan>\n"+
      "<CreationDate>66</CreationDate>\n"+
      "<Translator>77</Translator>\n"+
      "<Lofilter2>88</Lofilter2>\n"+
      "</Trg>\n"+
      "</AlML>\n"+
      
"<logfile>align.log</logfile>\n"+
"<dupmsg_check>yes</dupmsg_check>\n"+
"<equal_msgno_check>yes</equal_msgno_check>\n"+
"\n\n";

  static final String strVersion = "Alignment Tool - version: 1.60";

  /*************************\
   *
   *  Constructor.
   *
  \*************************/
  public AlignmentTool()
  {
    //  FileType Hashtable
    m_hashFileType = new Hashtable();
    m_hashFileType.put("po file", new Integer(1));
    m_hashFileType.put("java resource file", new Integer(2));
    m_hashFileType.put("properties file", new Integer(3));
    m_hashFileType.put("msg file", new Integer(4));
    m_hashFileType.put("xres file", new Integer(5));


    m_writerLog = null;
    /*
     *  Taking this out as it doesn't seem to be needed
     *  and it is making automated testing extremely 
     *  difficult.
     *
     Random random = new Random(System.currentTimeMillis());
     m_id = (int) (random.nextFloat() * 100000);   
     */
    m_id = 3349;
  }


  /*************************\
   *
   *  Main function.
   *
  \*************************/
  public static void main(String[] args)
  {
    try
    {
      AlignmentTool ali_tool = new AlignmentTool();
      if(!ali_tool.runAlign(args)) { System.exit(1); }
    }
    catch(TMCParseException tmcEx)
    {
      System.err.print(tmcEx.getMessage());
      System.exit(13);          
    }
    catch(Exception ex)
    {
      //  Tool Versions
      System.err.print(strVersion + "\n");
      System.err.print(" - " + org.jvnet.olt.parsers.MsgFileParser.MsgFileParser.getVersionInfo() + "\n");
      System.err.print(" - " + org.jvnet.olt.parsers.POFileParser.POFileParser.getVersionInfo() + "\n");
      System.err.print(" - " + org.jvnet.olt.parsers.PropsFileParser.PropsFileParser.getVersionInfo() + "\n");
      System.err.print(" - " + org.jvnet.olt.parsers.JavaParser.ResBundleParser.getVersionInfo() + "\n");
      System.err.print("\n");

      System.err.print(strErrorMsg);
      System.err.print(ex.getMessage());
      System.err.print("\n\n");
      System.err.print(strExceptionStackTrace);
      ex.printStackTrace();
      System.err.print("\n\n");
      System.err.print(strUsage);
      System.err.print(strTagExplanations);
      System.err.print(strSampleCmdFile);
      System.exit(1);
    }
    catch(org.jvnet.olt.parsers.POFileParser.TokenMgrError poErr)
    {
      System.err.print(poErr.getMessage());
      System.exit(13);          
    }
    catch(org.jvnet.olt.parsers.MsgFileParser.TokenMgrError msgErr)
    {
      System.err.print(msgErr.getMessage());
      System.exit(14);          
    }
    catch(org.jvnet.olt.parsers.JavaParser.TokenMgrError javaErr)
    {
      System.err.print(javaErr.getMessage());
      System.exit(15);          
    }
    catch(org.jvnet.olt.parsers.PropsFileParser.TokenMgrError propsErr)
    {
      System.err.print(propsErr.getMessage());
      System.exit(16);          
    }
    System.exit(0);    
  }

  /**
   *  This method starts the alignment of the files. Essentially it is
   *  a hack method to get error messages written to our log file.
   */
  public boolean runAlign(String[] args)
    throws TMCParseException
  {
    try
    {

      Hashtable data = parseCommandLine(args);
      String strLogFile = (String)(data.get("log file") == null ? "" :  data.get("log file"));

      createLogFile(strLogFile);
      writeVersionInfo();

      String[][] arrAligned = alignFiles(data);
      writeAliFiles(arrAligned, data);
    }
    catch(TMCParseException tmcEx)
    {
      if(m_writerLog != null)
      {
	tmcEx.printStackTrace(m_writerLog);
	m_writerLog.flush();
      }
      throw tmcEx;
    }
    catch(Exception ex)
    {
      if(m_writerLog != null)
      {
	writeToLog(strErrorMsg);
	writeToLog(ex.getMessage());
	writeToLog("\n\n");
	writeToLog(strExceptionStackTrace);
	ex.printStackTrace(m_writerLog);
	writeToLog("\n\n");
	writeToLog(strUsage);
	writeToLog(strTagExplanations);
	writeToLog(strSampleCmdFile);
	m_writerLog.close();
      }
      else
      {
	//  Tool Versions
	System.err.print(strVersion + "\n");
	System.err.print(" - " + org.jvnet.olt.parsers.MsgFileParser.MsgFileParser.getVersionInfo() + "\n");
	System.err.print(" - " + org.jvnet.olt.parsers.POFileParser.POFileParser.getVersionInfo() + "\n");
	System.err.print(" - " + org.jvnet.olt.parsers.PropsFileParser.PropsFileParser.getVersionInfo() + "\n");
	System.err.print(" - " + org.jvnet.olt.parsers.JavaParser.ResBundleParser.getVersionInfo() + "\n");
	System.err.print("\n");

	//  Other Stuff
	System.err.print(strErrorMsg);
	System.err.print(strExceptionStackTrace);
	ex.printStackTrace();
	System.err.print("\n");
	System.err.print(strUsage);
	System.err.print(strTagExplanations);
	System.err.print(strSampleCmdFile);
      }      
      return false;
    }
    
    if(m_writerLog != null)
    {
      m_writerLog.close();
    }
    return true;
  }



  /*************************\
   *
   *  Message file aligning 
   *   functions.
   *
  \*************************/

  /**
   *
   */
  public String[][] alignFiles(Hashtable data) 
    throws TMCParseException,IOException,Exception,AliToolException
  {
    int iAlignType = determineFileType(data);
    String strDefaultDomain = determineDefaultDomain(data,iAlignType);

    //  Parse source file
    String[][] arrSource = parseFile((File) data.get("source file"), (String) data.get("source lang"), iAlignType, strDefaultDomain, "");

    //  Parse target file
    String[][] arrTarget = parseFile((File) data.get("target file"), (String) data.get("target lang"), iAlignType, strDefaultDomain, (String) data.get("encoding"));

    //  Do best comparison of the two lists: may involve sorting
    //    Dodgy way implemented here
    //  Create output array
    String[][] arrAligned = new String[arrSource.length][5];
    boolean boolThisKeyFound = false;
    
    writeToLog("Aligning messages:\n");
    writeToLog("Number of messages in base file: " + arrSource.length + "\n");  
    writeToLog("Number of messages in localized file: " + arrTarget.length + "\n");
    
    Set sSource = new HashSet();
    Set sTarget = new HashSet();
    boolean duplicateMessageFound=false;


    // Quick check to see that there aren't any duplicate keys if asked to do this bit
    String check = (String)data.get ("duplicate messages");
    if (check.toUpperCase().equals("YES") ){
    
	for(int i =0; i < arrSource.length; i++){
	    // - should probably refactor this code to use the below collections
	    // - instead of arrays...
	    
	    if (!sSource.add(arrSource[i][0])){
		writeToLog ("Error ! key : " + arrSource[i][0] +
			    " appears more than once in the source message file.\n");
		duplicateMessageFound = true;
	    }	  
	    if (duplicateMessageFound == true){
		writeToLog ("Duplicate keys constitute bugs in message files \n"+
			    "- please fix the message files, and run this tool again\n");
		throw new AliToolException("Duplicate keys constitute bugs in message files - see logfile for further details.");
	    }
	}

	for(int j=0; j <arrTarget.length; j++){
	    if (!sTarget.add(arrTarget[j][0])){
		writeToLog ("Error ! key : " + arrTarget[j][0] +
			    " appears more than once in the target message file.\n");
		duplicateMessageFound = true;
	    }	  
	    if (duplicateMessageFound == true){
		writeToLog ("Duplicate keys constitute bugs in message files \n"+
			    "- please fix the message files, and run this tool again\n");
		throw new AliToolException("Duplicate keys constitute bugs in message files - see logfile for further details.");
	    }
	}
    }
  


    for(int i = 0; i < arrSource.length; i++)
    {
      if((i % 10) == 0) { writeToLog("."); }

      boolThisKeyFound = false;
      int j = 0;
      while( (j < arrTarget.length) && !boolThisKeyFound)
      {

	  boolThisKeyFound = (arrSource[i][0].equals(arrTarget[j][0]) 
			      && arrSource[i][1].equals(arrTarget[j][1]));
	  if(boolThisKeyFound)
	      {
		  arrAligned[i][0] = arrSource[i][0];  //  Key 
		  arrAligned[i][1] = arrSource[i][1];  //  Domain
		  arrAligned[i][2] = arrSource[i][2];  //  Source string
		  arrAligned[i][3] = arrTarget[j][2];  //  Target string

		  if((determineFileType( data) == 1)     // i.e. a PO file
		     || (determineFileType( data) == 3)  // or a .properties file
		     || (determineFileType( data) == 2)  // or a .java file
		     || (determineFileType( data) == 4)) // or a MSG file
		      {
			  arrAligned[i][4] = arrTarget[j][3];  //  Target comment string
		      }
		  else
		      {
			  arrAligned[i][4] = "";
		      }
	      }
	  j++;
      }
      if(!boolThisKeyFound)
	  {
	      check = (String)data.get("equal number of messages");
	      if (check.toUpperCase().equals("YES")){
	      	  throw new AliToolException("Base message '" + arrSource[i][0] + 
	      				     "' does not have a matching message in the Target file.\n");}
	      arrAligned[i][0] = "@TMC@ Missing Message";
	      arrAligned[i][1] = arrSource[i][1];
	      arrAligned[i][2] = "@TMC@ Missing Message";
	      arrAligned[i][3] = "@TMC@ Missing Message";
	      writeToLog("Base message '" + arrSource[i][0] + "' does not have a matching message in the Target file.\n");
	      
	  }
    }
    return arrAligned;
  }
    

  public int determineFileType(Hashtable data)
    throws AliToolException
  {
    Integer integerAlignType = (Integer) m_hashFileType.get(data.get("align type"));
    if(integerAlignType == null)
    {
      throw new AliToolException("Unknown message file type specified : "+data.get("align type"));
    }
    return integerAlignType.intValue();
  }


  public String determineDefaultDomain(Hashtable data, int type)
  {
    String strDefDomain = (String) data.get("default category");
    switch(type)
    {
    case MessageListGenerator.JAVA_PROPS:
    case MessageListGenerator.PO_FILE:
      if((strDefDomain == null) || (strDefDomain == ""))
      {
	File file = ((File) data.get("source file"));
	String strFileName = file.getName();
	int last_dot_index = strFileName.lastIndexOf((int) '.');
	if(last_dot_index == -1) 
	{
	  return strFileName;
	}
	else
	{
	  return strFileName.substring(0,last_dot_index);
	}
      }
      else
      {
	return strDefDomain;
      }
    case MessageListGenerator.XRES_FILE:
    case MessageListGenerator.JAVA_RES:
    case MessageListGenerator.MSG_FILE:
      if(strDefDomain == null)
      {
	return "";
      }
      else
      {
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
   *  @param encoding  A string to indicate the encoding : if this is null, a value is picked using EncodingTable.getEncoding(langid)
   *
   */
  private String[][] parseFile(File file, String langid, int aligntype, String strDefaultDomain, String encoding) 
    throws TMCParseException,IOException,Exception,AliToolException
  {
    FileInputStream inStream = new FileInputStream(file);

    if (encoding.length() == 0)
    encoding = EncodingTable.getEncoding(langid);
    
    InputStreamReader readerIn = new InputStreamReader(inStream, encoding);

    //  The block of code below is necessary to handle cases where
    //  the a Control-D character is encountered in the input. The
    //  Alpnet tools do not handle this character gracefully, so we
    //  are escaping it for the TM and alignment tools.
    //
    //  Unfortunately this approach does not work well for properties
    //  files so we are not using it in this case.
    HTMLEscapeFilterReader reader;   //  Declare this in the outer scope!
    if(aligntype != MessageListGenerator.JAVA_PROPS)
    {
      ControlDEscapingFilterReader readerEscaped =
	new ControlDEscapingFilterReader(readerIn);
      reader = new HTMLEscapeFilterReader(readerEscaped);
    }
    else 
    {
      //  Reader set up to throw an exception if Control-D character is
      //  encountered.
      ControlDWarningFilterReader readerWarn  =
	new ControlDWarningFilterReader(readerIn);
      reader = new HTMLEscapeFilterReader(readerWarn);
    }

    String[] args = new String[1];
    args[0] = file.getAbsolutePath();
    writeToLog(MessageFormat.format("Parsing file ... {0}\n",args));

    String[][] messages = null;

    TMCiParserFacade facade;

    switch(aligntype)
    {
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
    default:
      throw new AliToolException("Unknown message file type specified");
    }

    messages = facade.getMessageStringArr(reader,strDefaultDomain);

    return messages;
  }



  /*************************\
   *
   *  ALI File writing 
   *   functions.
   *
  \*************************/

  /**
   *
   */
  public void writeAliFiles(String[][] arrAligned, Hashtable data) throws IOException,UnsupportedEncodingException
  {
    //  Decide on a file name to write to. 
    writeToLog("\nWriting alignment file...\n\n"); 

    PrintWriter writerAli = null;

    int iFileCount = 1;
    
    String strPrevContext = "";
    boolean boolFirstFile = true;
    int iPrevEnd = 0;

    //  Write MATs to file.
    for(int i = 0; i < arrAligned.length ; i++)
    { 
	if (arrAligned[i][0].equals ("@TMC@ Missing Message")){
	    continue;
	}

      if(!strPrevContext.equals(arrAligned[i][1]) || (writerAli == null))
      {
	if(!boolFirstFile)
	{
	  writeFooter(writerAli);
	  writerAli.close();
	}
	String strAliFile = ((String) data.get("source file name")) + "." +
	  ((String) data.get("target lang")) + "." + (iFileCount++) + ".ali";
	writerAli = createAliFileWriter(strAliFile);
	writeHeader(writerAli, data);
	writeAlignBlock(writerAli, data, arrAligned[i][1]);
	strPrevContext = arrAligned[i][1];
	boolFirstFile = false;
	iPrevEnd += i-1;   //  This doesn't look right!
      }
      
      writerAli.println("<al inst=\"#5AL"+ m_id +"\" ssn=\"1\">");  
      writerAli.println("  <source sn=\"" 
			+ ((i+1) - iPrevEnd)
                        + "\" status=\"\"><s><st><sunw_format tag=\"MsgKey\">" 
                        + arrAligned[i][0]
                        + "</sunw_format>" 
                        + arrAligned[i][2] 
                        + "</st></s></source>"
			);

      boolean boolShouldNotWriteComment = arrAligned[i][4].equals("") ||
	 !((String) data.get("write l10n comments")).equals("yes");

      if(boolShouldNotWriteComment)
      {
	writerAli.print("  <target lan=\"3\" tsn=\"1\" sn=\"" 
			+ ((i+1) - iPrevEnd) 
                        + "\" status=\"\"><s><st><sunw_format tag=\"MsgKey\">" 
                        + arrAligned[i][0] 
                        + "</sunw_format>" 
                        + arrAligned[i][3] 
                        + "</st></s></target>\n"
                        );
      }
      else
      {
	writerAli.print("  <target lan=\"3\" tsn=\"1\" sn=\"" 
			+ ((i+1) - iPrevEnd) 
                        + "\" status=\"\"><s><st><sunw_format tag=\"MsgKey\">" 
                        + arrAligned[i][0] 
                        + "</sunw_format>" 
                        + arrAligned[i][3]
			+ "<sunw_format tag=\"Comment\">"
			+ arrAligned[i][4]
			+ "</sunw_format>"
                        + "</st></s></target>\n"
                        );

      }
      
      writerAli.println("</al>"); 
    } 

    writeFooter(writerAli);
    writerAli.flush();
    writerAli.close();
  }


  /**
   *
   */
  private void writeHeader(PrintWriter writer, Hashtable data)
  {
    //  Generate the strings that will hold the date and time.
    //  For safety's sake generate these strings in German locale
    Date date = new Date();

    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, 
						       Locale.GERMAN);
    String strDate = dateFormat.format(date);


    DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, 
						       Locale.GERMAN);
    String strTime = timeFormat.format(date);

    //  Write stuff out
    writer.println("<!DOCTYPE html SYSTEM \"pivot.dtd\">");
    writer.println("<html version=\"EURAMIS\" code=\"UNICODE\">");

    writer.println("<head></head>");

    writer.println("<history instno=\"1\">");
    writer.println("<appl appname=\"SWALIGNER\" inst=\"#1\" date=\"" + strDate + "\" time=\"" + strTime + "\" file=\"" +  (String) data.get("source file name") + "\" version=\"0.7 FROM 7.01.2000\">");
    writer.println("</history>");
    
    writer.println("<title></title>");

    writer.println("<file LAN=\"" + (String) data.get("source lang") + "\">" + (String) data.get("source file name") + "</file>");

    writer.println("<fonttable></fonttable>");

    writer.println("<body>");
  }

  /**
   *
   */
  private void writeAlignBlock(PrintWriter writer, Hashtable data, String strContext)
  {
    //  Alignment import only works if the language code is uppercase.
    String strSrcLangUpper = ((String) data.get("source lang")).toUpperCase();
    writer.print("<align type=\"header\" lan=\"" + strSrcLangUpper);

    writer.print("\" no=\"1\" Year=\"");
    writer.print("\" Title=\"" + (String) data.get("install path"));
    writer.print("\" SrcFile=\"" + (String) data.get("source file name") + "\">\n");  
    //  Most lines here need something added.
    //  I need to work out the data structure to hold all of this.
    writer.println("<SrcLanguage no=\"2\">" + (String) data.get("source lang") + "</SrcLanguage>");  
    writer.println("<Directional></Directional>");  
    writer.println("<Project></Project>");  
    writer.println("<ProjID>" + (String) data.get("project id") + "</ProjID>");  
    writer.println("<Domain>" + (String) data.get("base team owner")+ "</Domain>");  
    writer.println("<TextType>" + strContext + "</TextType>");  
    writer.println("<AlignType></AlignType>");  
    writer.println("<Status></Status>");  
    writer.println("<CreationDate></CreationDate>");  
    writer.println("<Author></Author>");  
    writer.println("<LOFilter3></LOFilter3>");  
    writer.println("<LOFilter1></LOFilter1>");

    //  Alignment import only works if the language code is uppercase.
    String strTgtLangUpper = ((String) data.get("target lang")).toUpperCase();
    writer.println("<translation TrgLan=\"" + strTgtLangUpper + "\" no=\"3\" TrgFile=\"" + (String) data.get("target file name") + "\">"); 
 
    writer.println("  <Translator></Translator>"); //  May need to change this
    writer.println("  <LOFilter3></LOFilter3>"); //  May need to change this 
    writer.println("  <LOFilter2></LOFilter2>"); //  May need to change this 
    writer.println("  <CreationDate></CreationDate>");  
    writer.println("</translation>");  
    writer.println("</align>");  
  }


  /**
   *  Code to close off an Ali file.
   */
  private void writeFooter(PrintWriter writer)
  {
    writer.println("</body>");  
    writer.println("</html>");  

  }

  /**
   *  Code to start a new ALI file
   */
  private PrintWriter createAliFileWriter(String strFile) throws IOException,UnsupportedEncodingException
  {
    File fileAli = new File(strFile);
    FileOutputStream streamAli = new FileOutputStream(fileAli);
    PrintWriter writer =  new PrintWriter( new BufferedWriter( new OutputStreamWriter(streamAli,"UnicodeBig")));

    writeToLog("Creating new ALI file: " + strFile + "\n");

    return writer;
  }



  /*************************\
   *
   *  Command file parsing
   *   functions.
   *
  \*************************/

  /**
   *
   */
  public Hashtable parseCommandLine(String[] argv) throws AliToolArgsException,IOException,AliToolException
  {
    int iParams = argv.length;

    if(iParams != 2)
    {
      Object[] numParams = new Object[1];
      numParams[0] =  new Integer(iParams); 
      throw new AliToolArgsException(MessageFormat.format("Incorrect number of arguments passed to the program. {0,number,integer} passed instead of 2.", numParams));
    }
    //  Check that the 2 params are -c and a valid command file.
    if(!argv[0].equals("-c")) { throw new AliToolArgsException("-c switch not specified on the command line!"); }
    
    String strCmdFile = argv[1];
    File fileCmd = new File(strCmdFile);
    if(!fileCmd.exists()) { throw new AliToolArgsException("Command file does not exist."); }
    if(!fileCmd.isFile()) { throw new AliToolArgsException("Command file is a directory."); }
    if(!fileCmd.canRead()) { throw new AliToolArgsException("Command file is not readable."); }
    
    //  Parse the Command file
    Hashtable data = readCommandFile(fileCmd);
    
    File fileSource = new File((String) data.get("source file name"));
    if(!fileSource.exists()) { throw new AliToolArgsException("Base file does not exist."); }
    if(!fileSource.isFile()) { throw new AliToolArgsException("Base file is a directory."); }
    if(!fileSource.canRead()) { throw new AliToolArgsException("Base file is not readable."); }
    data.put("source file",fileSource);
    
    File fileTarget = new File((String) data.get("target file name"));
    if(!fileTarget.exists()) { throw new AliToolArgsException("Localized file does not exist."); }
    if(!fileTarget.isFile()) { throw new AliToolArgsException("Localized file is a directory."); }
    if(!fileTarget.canRead()) { throw new AliToolArgsException("Localized file is not readable."); }
    data.put("target file",fileTarget);
    
    return data;
  } 

  /**
   *
   */
  private Hashtable readCommandFile(File fileCmd) throws IOException,AliToolException
  {
    String[][] arrData = {
      {"source file name","srcfile","required"},
      {"source lang","lan","required"},
      {"target file name","trgfile","required"},
      {"target lang","trglan","required"},
      {"project id","projid","required"},
      {"base team owner","domain","required"},
      {"align type","aligntype","required"},
      {"install path","title","required"},
      {"default category","texttype",""},
      {"translator","translator","required"},
      {"creation date","creationdate","required"},
      {"change translator","lofilter2","required"},
      {"log file","logfile","required"},
      {"encoding", "encoding", ""},
      {"write l10n comments","l10n_comment", ""},
      {"duplicate messages", "dupmsg_check", ""},
      {"equal number of messages", "equal_msgno_check", ""},
    };


    //  Create a FileInputStream and slurp the whole
    //  file into a string.
    FileInputStream streamCmd = new FileInputStream(fileCmd);
    InputStreamReader readerCmd = new InputStreamReader(streamCmd);

    int c;
    StringBuffer buffer = new StringBuffer();

    while((c = readerCmd.read()) != -1)
    {
      buffer.append((char) c);
    }
    String strCmdFile = buffer.toString();


    Perl5Compiler perl5comp = new Perl5Compiler();
    Perl5Matcher perl5match = new Perl5Matcher();    
    Hashtable data = new Hashtable();

    int iMask = Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.MULTILINE_MASK ;

    Pattern pattern;
    String strPattern = "";
    
    try
    {
      for(int i = 0; i < arrData.length; i++)
      {
	strPattern = "<" + arrData[i][1] + ">(.+)</" + arrData[i][1] + ">";
	pattern = perl5comp.compile(strPattern,iMask);
	if(perl5match.contains(strCmdFile,pattern))
	{
	  data.put(arrData[i][0], (perl5match.getMatch()).group(1));
	}
	else
	{
	  if (arrData[i][2].equals("required"))
	  {
	    throw new AliToolException(arrData[i][1] + " : not found in the command file. ");
	  }
	  data.put(arrData[i][0],"");   //  To guard against null pointers
	}
      }
    }
    catch(MalformedPatternException ex1)
    {
      throw new AliToolException("DEBUG: A pattern was incorrectly formed for:" + strPattern);
    }
    
    //  Test if the language is valid.
    //  Look up the encoding table. If a non-empty string is returned, then 
    //  the language is supported.
    String strLangTest = EncodingTable.getEncoding((String) data.get("target lang"));
    if( (strLangTest == null) || (strLangTest.equals("")) )
    {
      throw new AliToolException("Target language specified was invalid. Value = " + data.get("target lang"));
    }

    strLangTest = EncodingTable.getEncoding((String) data.get("source lang"));
    if( (strLangTest == null) || (strLangTest.equals("")) )
    {
      throw new AliToolException("Source language specified was invalid. Value = " + data.get("source lang"));
    }
    
    String flagcheck = (String)data.get ("duplicate messages");
    if ( (flagcheck == null) || flagcheck.equals("")){
      data.put("duplicate messages", "yes");
    }
    flagcheck = (String)data.get ("equal number of messages");
    if ( (flagcheck == null) || flagcheck.equals ("")){
	data.put("equal number of messages", "yes");
    }
    
    

    return data;
  }



  /*************************\
   *
   *  Log File functions.
   *
  \*************************/

  public void createLogFile(String fileName)
  {
    if(!fileName.equals(""))
    {
      try
      {
	File fileLog = new File(fileName);
	FileOutputStream streamLog = new FileOutputStream(fileLog);
	m_writerLog =  new PrintWriter(  new OutputStreamWriter(streamLog));
      }
      catch(IOException exIO)
      {
	m_writerLog = null;
	System.out.println(exIO);
      }
    }
  }

  public void writeToLog(String string)
  {
    if(m_writerLog == null)
    {
      System.out.print(string);
    }
    else
    {
      m_writerLog.print(string);
      m_writerLog.flush();
    }
  }

  public void writeVersionInfo()
  {
    writeToLog(strVersion + "\n");
    writeToLog(" - " + org.jvnet.olt.parsers.MsgFileParser.MsgFileParser.getVersionInfo() + "\n");
    writeToLog(" - " + org.jvnet.olt.parsers.POFileParser.POFileParser.getVersionInfo() + "\n");
    writeToLog(" - " + org.jvnet.olt.parsers.PropsFileParser.PropsFileParser.getVersionInfo() + "\n");
    writeToLog(" - " + org.jvnet.olt.parsers.JavaParser.ResBundleParser.getVersionInfo() + "\n");
    writeToLog("\n");
  }

}

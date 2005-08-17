/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.spellchecker;

import java.io.*;
import java.util.logging.Logger;

import org.jvnet.olt.editor.util.Languages;


public class SpellCheckerAPI {
    private static final Logger logger = Logger.getLogger(SpellCheckerAPI.class.getName());
    
    public SpellCheckerAPI() {
    }

    
    public static String[] getCommand(String editorHome, String dictLang,String lang) {
        //  Intorduce an evil hack here: different command strings based on the
        //  OS platform the system is running on.       
        //boolean boolUsingSunOS = System.getProperty("os.name").equals("SunOS");
        
        String os = System.getProperty("os.name").toLowerCase();
        
        boolean onWin = os.startsWith("windows");
        boolean onLin = os.indexOf("linux") != -1;
        boolean onSol = os.startsWith("sunos");
        
        //fallback to SOL. Do far 
        if(!onWin && !onLin && !onSol)
            onSol = true;
        
        String[] cmd = new String[5];
        String aspellHomePath = (editorHome.endsWith(File.separator)) ? (editorHome + "spellchecker" + File.separator) : (editorHome + File.separator + "spellchecker" + File.separator);

        logger.finest("Aspell home:"+aspellHomePath);
        
        if (onLin) {
            //  Command to use on Linux; we assume there is an aspell installed somewhere
            cmd[0] = "aspell";
            cmd[1] = "--lang="+dictLang;
            cmd[2] = "-a";
            cmd[3] = "";
            cmd[4] = "";
        } else if(onSol){
            //  Command to use on Solaris
            cmd[0] = aspellHomePath + "bin" + File.separator + "aspell";
            cmd[1] = "--lang=" + lang;
            cmd[2] = "--dict-dir=" + aspellHomePath + "lib" + File.separator + "aspell";
            cmd[3] = "--data-dir=" + aspellHomePath + "share" + File.separator + "aspell"; 
            cmd[4] = "-a";
        } else if(onWin){
            //  Command to use on other platforms, i.e., Windows
            cmd[0] = aspellHomePath + "bin" + File.separator + "aspell";
            cmd[1] = "--lang=" + dictLang;
            cmd[2] = "--master=" + aspellHomePath + "dict" + File.separator + "aspell" + File.separator + dictLang;
            cmd[3] = "--data-dir=" + aspellHomePath + "share" + File.separator + "aspell";
            cmd[4] = "-a";
        
        }
        logger.finest("Command:"+cmd[0]+" "+cmd[1]+" "+cmd[2]+" "+cmd[3]+" "+cmd[4]);
        logger.finest("Are we running on win? "+onWin);
        
        
        return cmd;
    }

    public static void checkWord(PrintWriter pw, String word) {
        pw.println(word);
        pw.flush();        
    }

    public static String getResult(BufferedReader br) {
        String line = null;
        int waitCount = 0;

        StringBuffer sb = new StringBuffer();
        try {
            do{
                line = br.readLine();
                logger.finest("Line:"+line);
                
                if(line == null || "".equals(line))
                    break;

                if(line.startsWith("@")){
                    logger.finest("Line starts with '@' will ignore it"); 
                    continue;
                }
                
                sb.append(line);
            }
            while(true);
            
            logger.finer("Returning line:"+sb.toString());
            
            return sb.toString();
        }
        catch (Exception e){
            logger.warning("Exception occured:"+e);
        }
        
        return "".equals(sb.toString()) ? "#" : sb.toString();
        
            
/* This code has been replaced by the code above. It seems strange

            while (!br.ready()) {
                Thread.currentThread().sleep(100);
                waitCount++;

                //System.out.println("waitCount="+waitCount);
                if (waitCount > 20) {
                    return "& ";
                }
            }

            int nullLineCount = 0;

            while ((line = br.readLine()) != null) {
                //System.out.println("line="+line);
                if (line.trim().equals("")) {
                    waitCount = 0;

                    while (!br.ready()) {
                        Thread.currentThread().sleep(100);
                        waitCount++;

                        //System.out.println("another waitCount="+waitCount);
                        if (waitCount > 20) {
                            while (br.ready())
                                br.readLine();

                            return "#";
                        }
                    }

                    // nullLineCount++;
                    // if(nullLineCount>=2) return "#";
                    continue; // "& ";
                } else {
                    while (br.ready())
                        br.readLine();

                    return line;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
*/
 //       return (line == null) ? "#" : "&";
    }

    public static void addToPersonal(PrintWriter pw, String newWord) {
        //  String strNewWord ="";
        //byte[] m_strNewWord = new byte[newWord.length()];
        //try{
        //m_strNewWord = newWord.getBytes(Languages.getLanguageENC("IT"));
        //strNewWord = new String(m_strNewWord);
        //}
        //catch(Exception ec)
        //{}
        pw.println(getAppendPersonalDictFlag() + " " + newWord);

        //pw.println(getAppendPersonalDictFlag()+" "+strNewWord);
        pw.flush();
        pw.println(getSavePersonalDictFlag());
        pw.flush();

        //strNewWord = null;
    }

    public static String getFlagForNonErrorWord() {
        return "*";
    }

    public static String getFlagForErrorWord() {
        return "&";
    }

    public static String getSuggestionStartFlagForErrorWord() {
        return ":";
    }

    public static String getSuggestionDelimiterFlagForErrorWord() {
        return ",";
    }

    public static String getAppendPersonalDictFlag() { //* word

        return "*";
    }

    public static String getSavePersonalDictFlag() { //#

        return "#";
    }
}

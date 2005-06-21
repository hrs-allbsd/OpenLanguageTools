/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.spellchecker;

import java.io.*;

import org.jvnet.olt.editor.util.Languages;


public class SpellCheckerAPI {
    public SpellCheckerAPI() {
    }

    public static String[] getCommand(String editorHome, String dictLang) {
        //  Intorduce an evil hack here: different command strings based on the
        //  OS platform the system is running on.       
        boolean boolUsingSunOS = System.getProperty("os.name").equals("SunOS");

        String[] cmd = new String[5];
        String aspellHomePath = (editorHome.endsWith(File.separator)) ? (editorHome + "spellchecker" + File.separator) : (editorHome + File.separator + "spellchecker" + File.separator);

        if (boolUsingSunOS) {
            //  Command to use on Solaris
            cmd[0] = aspellHomePath + "bin" + File.separator + "aspell";
            cmd[1] = "--lang=" + dictLang;
            cmd[2] = "--dict-dir=" + aspellHomePath + "lib" + File.separator + "aspell";
            cmd[3] = "--data-dir=" + aspellHomePath + "share" + File.separator + "aspell";
            cmd[4] = "-a";
        } else {
            //  Command to use on other platforms, i.e., Windows
            cmd[0] = aspellHomePath + "bin" + File.separator + "aspell";
            cmd[1] = "--lang=" + dictLang;
            cmd[2] = "--master=" + aspellHomePath + "dict" + File.separator + "aspell" + File.separator + dictLang;
            cmd[3] = "--data-dir=" + aspellHomePath + "share" + File.separator + "aspell";
            cmd[4] = "-a";
        }

        return cmd;
    }

    public static void checkWord(PrintWriter pw, String word) {
        pw.println(word);
        pw.flush();
    }

    public static String getResult(BufferedReader br) {
        String line = null;
        int waitCount = 0;

        try {
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

                    /* nullLineCount++;
                     if(nullLineCount>=2) return "#";*/
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

        return (line == null) ? "#" : "&";
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

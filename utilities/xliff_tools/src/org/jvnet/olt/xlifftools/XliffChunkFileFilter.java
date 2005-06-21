
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffChunkFileFilter.java
 *
 * Created on 22 August 2003, 14:37
 */

package org.jvnet.olt.xlifftools;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class acts as a file filter to select all the files in a given directory
 * that match '{original filename}_{suffix}_{number}.xlz'. It is used to get all
 * the mini XLZ files that need to be merged together.
 * @author  jc73554
 */
public class XliffChunkFileFilter implements FileFilter
{
    private Pattern pattern;
    
    private String regex;
    
    /** Creates a new instance of XliffChunkFileFilter
     * @param baseFileName The base name for the original file.
     * @param suffix A suffix, provided to distinguish the small
     * XLZ files to be merged.
     */
    public XliffChunkFileFilter(String baseFileName, String suffix)
    {
        //  strip off the file extension from the baseFileName
        String prefix = escapeMetaChars(baseFileName + "_" + suffix);
        regex =  prefix+ "_[0-9]+\\.xl[zf]";
        
        pattern = Pattern.compile(regex);        
    }
    
    /** This method determines if a given file is to be passed
     * through the file filter or not. The file passes if it is a
     * file that matches the pattern '{original filename}_{suffix}_{number}.xlz'.
     * @param pathname The path to the file.
     * @return true if the file is a file that matches the pattern '{original filename}_{suffix}_{number}.xlz'. false otherwise.
     */    
    public boolean accept(File pathname)
    {
        //  Guard clause: only accept files, not directories, etc.
        if(!pathname.isFile()) { return false; }
        
        //  Split pathname into its bits.
        String name = pathname.getName();
        
        //  Test that the name actually corresponds to the pattern built up in
        //  the constructor.
       Matcher matcher = pattern.matcher(name);
       return matcher.matches();       
    }
    
    /** This method escapes regex meta characters that appear in the input
     *  string.
     * @param string The string with meta-characters to escape.
     * @return A string which consists of the the characters of the input string with any meta
     * characters escaped.
     */
    protected String escapeMetaChars(String string)
    {
        //  Do the escaping of any characters that could be considered as regex
        //  meta characters.
        StringBuffer bufferOut = new StringBuffer();
        
        int stringLeng = string.length();
        for(int i = 0; i < stringLeng; i++)
        {
            char ch = string.charAt(i);
            switch((int) ch)
            {
                //  These are all the meta characters
                case ((int) '\\'):
                case ((int) '?'):
                case ((int) '.'):
                case ((int) '('):
                case ((int) ')'):
                case ((int) '['):
                case ((int) ']'):
                case ((int) '{'):
                case ((int) '}'):
                case ((int) '*'):
                case ((int) '+'):                    
                    bufferOut.append('\\');
                    bufferOut.append(ch);                    
                    break;
                default:
                    bufferOut.append(ch);
                    break;
            }
        }
        return bufferOut.toString();
    }
    
}

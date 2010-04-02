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

/*
 * HtmlMetaTagController.java
 *
 * Created on November 17, 2003, 11:50 AM
 */

package org.jvnet.olt.filters.html;

import java.io.*;
import java.util.HashMap;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** This is a simple class that contains two static methods.
 
 * The first replaces the first occurence of a
 * html meta tag that <b>already</b> contains a charset attribute
 * and switches that charset to the user supplied one. Note that
 * it opens the file in the charset specified to be changed, so it
 * can only be used to read and change a UTF-8 encoded file to
 * include a meta tag with a UTF-8 charset attribute.
 *
 * The second method is used to determine if the file delcares a
 * meta tag containing charset information, and if so, what charset
 * it declares.
 *
 * Both methods use the same regular expressions to find charsets.
 * These functions are listed as public static Strings below
 *
 * @author timf
 */
public class HtmlMetaTagController {
    
    public static HashMap charsetMap = null;
    
    /** this regexp finds a meta tag in the input, like 
     * &lt;META http-equiv="Content-Type" content="text/html; charset=ISO-8859-5"&gt;
     *<pre>
     * &lt;(\\s)*"+
     *       "meta"+
     *       "([^&gt;])+"+
     *       "charset="+
     *       "([^&gt;])+&gt;";
     * </pre>
     */
    public static String findMetaTag = "<(\\s)*"+
            "meta"+
            "([^>])+"+
            "charset="+
            "([^>])+>";
    
    /** This finds the charset name in the meta tag. It's value is
     * charset=[^\"]*(?=(\\s|>|\")*)
     */
    public static  String findCharset = "charset=[^\"]*(?=(\\s|>|\")*)";
    
    /** Creates a new instance of HtmlMetaTagController - no need though, since
     * this class only contains static methods
     */
    public HtmlMetaTagController() {
    }
        
    
    public static void main(String args[]){
        try {
        HtmlMetaTagController.getMetaCharset(args[0]);
        } catch (HtmlParserException e){
            System.out.println("Exception occurred reading " + args[0] +" when trying"+
            " to determine what charset it declares " + e.getMessage());
        }
    }
    
    /**
     * This method reads the file pointed to by the filename paramter
     * and returns the character set that was declared by the html
     * &lt;meta&gt; tag at the top of the file. If that tag doesn't
     * exist, we return a null.
     * 
     * @param filename the filename whose charset want to determine
     * @return the charset of that file, or null if it's not declared
     */
    public static String getMetaCharset(String filename) throws HtmlParserException {
        String result = null;
        try {
            String charset = "US-ASCII";
            BufferedReader reader = new BufferedReader(new
            InputStreamReader(new FileInputStream(filename), charset));
            int i=0;
            File file = new File(filename);
            
            StringBuffer buffer = new StringBuffer();
            while ((i=reader.read()) != -1){
                buffer.append((char)i);
            }
            
            reader.close();
            String newfile = buffer.toString();
            
            // looking for a html meta tag like :
            // <META http-equiv="Content-Type" content="text/html; charset=ISO-8859-5">
            
            Pattern mypattern = Pattern.compile(findMetaTag,Pattern.CASE_INSENSITIVE);
            
            Matcher match = mypattern.matcher(buffer);
            
            if (match.find()){
                System.out.println("found match");
                int start = match.start();
                int end = match.end();
                String meta = buffer.subSequence(start,end).toString();
                
                String change = "";
                // want to get just the charset part of it with user supplied character set
                Pattern charsetPat = Pattern.compile(findCharset,Pattern.CASE_INSENSITIVE);
                Matcher charsetMatch = charsetPat.matcher(meta);
                if (charsetMatch.find()){
                    // found match. Now, strip off the "charset=" bit from the beginning
                    result = meta.substring((charsetMatch.start()+"charset=".length()),charsetMatch.end());
                }
            } else {
                //System.out.println ("doing no replacement for " + filename);
            }
                       
        } catch (Exception e){
            throw new HtmlParserException("Exception while querying for meta tag " + e.getMessage());
        }
        return result;
    }
    
    /**
     * This method reads in the file pointed to by the filename parameter in the
     * codeset pointed to by the charset tag, and if a html &lt;meta&gt; tag is
     * found in the input, it changes the charset declaration in that file to the
     * one we listed in the parameters.
     *
     * Primarily, this is to allow us to take a file that has been translated from
     * english to (say) japanese, and fix the character set tag in the top of the
     * file, so browsers won't be confused by files that declare an english charset 
     * but contains japanese contents.
     *
     * @param filename The filename to be read and altered
     * @param charset the character set to be changed (which is a valid
     * java character set that can be used by InputStreamReader)
     *
     * @throws IOException if there was some problem reading/writing the output
     */
    public static void fixMetaTag(String filename, String charset) throws IOException{
        try {
            
            String htmlCharsetName = getHtmlCharsetName(charset);
            //System.out.println ("running fixMetaTag code");
            BufferedReader reader = new BufferedReader(new
            InputStreamReader(new FileInputStream(filename), charset));
            int i=0;
            File file = new File(filename);
            
            StringBuffer buffer = new StringBuffer();
            while ((i=reader.read()) != -1){
                buffer.append((char)i);
            }
            
            reader.close();
            String newfile = buffer.toString();
            
            // looking for a html meta tag like :
            
            Pattern mypattern = Pattern.compile(findMetaTag,Pattern.CASE_INSENSITIVE);
            
            Matcher match = mypattern.matcher(buffer);
            
            if (match.find()){
                //System.out.println ("found match");
                int start = match.start();
                int end = match.end();
                String meta = buffer.subSequence(start,end).toString();
                
                String change = "";
                // want to replace just the charset part of it with user supplied character set
                Pattern charsetPat = Pattern.compile(findCharset,Pattern.CASE_INSENSITIVE);
                Matcher charsetMatch = charsetPat.matcher(meta);
                if (charsetMatch.find()){
                    change = charsetMatch.replaceFirst("charset="+htmlCharsetName);
                }
                newfile = match.replaceFirst(change);
            } else {
                //System.out.println ("doing no replacement for " + filename);
            }
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(filename),charset));
            StringReader str = new StringReader(newfile);
            
            while ((i=str.read()) != -1){
                wr.write(i);
            }
            wr.flush();
            wr.close();
        } catch (java.io.CharConversionException e){
            System.out.println("caught an error trying to do codeset conversion " + e.getMessage());
        }
    }
    
    /**
     * This returns a preferred MIME name of the input character set name
     * for use in HTML meta tags - these were taken from
     * http://www.iana.org/assignments/character-sets
     *
     * If we do not have a replacement for the input character set name,
     * we return the input character set name.
     */
    public static String getHtmlCharsetName(String charset){
        if (charsetMap == null){
            initCharsetMap();
        }
        if (charsetMap.containsKey(charset)){
            return (String)charsetMap.get(charset);
        } else return charset;
    }
    
    
    public static void initCharsetMap(){
        
        charsetMap = new HashMap();
        
        charsetMap.put("ASCII", "US-ASCII");
        charsetMap.put("Cp037","IBM-037");
        charsetMap.put("Cp1026","IBM-1026");
        charsetMap.put("Cp1046","IBM-1046");
        charsetMap.put("Cp1097","IBM-1097");
        charsetMap.put("Cp1098","IBM-1098");
        charsetMap.put("Cp1112","IBM-1112");
        charsetMap.put("Cp1122","IBM-1122");
        charsetMap.put("Cp1123","IBM-1123");
        charsetMap.put("Cp1124","IBM-1124");
        charsetMap.put("Cp1140","IBM-1140");
        charsetMap.put("Cp1141","IBM-1141");
        charsetMap.put("Cp1142","IBM-1142");
        charsetMap.put("Cp1143","IBM-1143");
        charsetMap.put("Cp1144","IBM-1144");
        charsetMap.put("Cp1145","IBM-1145");
        charsetMap.put("Cp1146","IBM-1146");
        charsetMap.put("Cp1147","IBM-1147");
        charsetMap.put("Cp1148","IBM-1148");
        charsetMap.put("Cp1149","IBM-1149");
        charsetMap.put("Cp1250","IBM-1250");
        charsetMap.put("Cp1252","IBM-1252");
        charsetMap.put("Cp1253","IBM-1253");
        charsetMap.put("Cp1254","IBM-1254");
        charsetMap.put("Cp1255","IBM-1255");
        charsetMap.put("Cp1256","IBM-1256");
        charsetMap.put("Cp1257","IBM-1257");
        charsetMap.put("Cp1258","IBM-1258");
        charsetMap.put("Cp1381","IBM-1381");
        charsetMap.put("Cp1383","IBM-1383");
        charsetMap.put("Cp273","IBM-273");
        charsetMap.put("Cp277","IBM-277");
        charsetMap.put("Cp278","IBM-278");
        charsetMap.put("Cp280","IBM-280");
        charsetMap.put("Cp284","IBM-284");
        charsetMap.put("Cp285","IBM-285");
        charsetMap.put("Cp297","IBM-279");
        charsetMap.put("Cp33722","IBM-33722");
        charsetMap.put("Cp420","IBM-420");
        charsetMap.put("Cp424","IBM-424");
        charsetMap.put("Cp437","IBM-437");
        charsetMap.put("Cp500","IBM-500");
        charsetMap.put("Cp737","IBM-737");
        charsetMap.put("Cp775","IBM-775");
        charsetMap.put("Cp838","IBM-838");
        charsetMap.put("Cp850","IBM-850");
        charsetMap.put("Cp852","IBM-852");
        charsetMap.put("Cp855","IBM-855");
        charsetMap.put("Cp856","IBM-856");
        charsetMap.put("Cp857","IBM-857");
        charsetMap.put("Cp858","IBM-858");
        charsetMap.put("Cp860","IBM-860");
        charsetMap.put("Cp861","IBM-861");
        charsetMap.put("Cp862","IBM-862");
        charsetMap.put("Cp863","IBM-863");
        charsetMap.put("Cp864","IBM-864");
        charsetMap.put("Cp865","IBM-865");
        charsetMap.put("Cp866","IBM-866");
        charsetMap.put("Cp868","IBM-868");
        charsetMap.put("Cp869","IBM-869");
        charsetMap.put("Cp870","IBM-870");
        charsetMap.put("Cp871","IBM-871");
        charsetMap.put("Cp874","IBM-874");
        charsetMap.put("Cp875","IBM-875");
        charsetMap.put("Cp918","IBM-918");
        charsetMap.put("Cp921","IBM-921");
        charsetMap.put("Cp922","IBM-922");
        charsetMap.put("Cp930","IBM-930");
        charsetMap.put("Cp933","IBM-933");
        charsetMap.put("Cp935","IBM-935");
        charsetMap.put("Cp937","IBM-937");
        charsetMap.put("Cp939","IBM-939");
        charsetMap.put("Cp942","IBM-942");
        charsetMap.put("Cp942C","IBM-942C");
        charsetMap.put("Cp943","IBM-943");
        charsetMap.put("Cp943C","IBM-943C");
        charsetMap.put("Cp948","IBM-948");
        charsetMap.put("Cp949","IBM-949");
        charsetMap.put("Cp949C","IBM-949C");
        charsetMap.put("Cp950","IBM-950");
        charsetMap.put("Cp964","IBM-964");
        charsetMap.put("Cp970","IBM-970");
        charsetMap.put("EUC_CN","EUC-CN");
        charsetMap.put("EUC_JP","EUC-JP");
        charsetMap.put("eucJP","EUC-JP");
        charsetMap.put("EUC_KR","EUC-KR");
        charsetMap.put("EUC-TW","EUC-TW");
        charsetMap.put("ISO2022CN_CNS","ISO-2022-CN-CNS");
        charsetMap.put("ISO2022CN_GB","ISO-2022-CN-GB");
        charsetMap.put("ISO2022JP","ISO-2022-JP");
        charsetMap.put("ISO2022KR","ISO2022-KR");
        charsetMap.put("ISO8859_1","ISO-8859-1");
        charsetMap.put("ISO8859_13","ISO-8859-13");
        charsetMap.put("ISO8859_15","ISO-8859-15");
        charsetMap.put("ISO8859_2","ISO-8859-2");
        charsetMap.put("ISO8859_3","ISO-8859-3");
        charsetMap.put("ISO8859_4","ISO-8859-4");
        charsetMap.put("ISO8859_5","ISO-8859-5");
        charsetMap.put("ISO8859_6","ISO-8859-6");
        charsetMap.put("ISO8859_7","ISO-8859-7");
        charsetMap.put("ISO8859_8","ISO-8859-8");
        charsetMap.put("ISO8859_9","ISO-8859-9");
        charsetMap.put("KOI8_R","KOI8-R");
        charsetMap.put("SJIS","Shift_JIS");
        charsetMap.put("TIS620","TIS-620");
        charsetMap.put("UTF8","UTF-8");
        
        
        
    }
}

/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlEncodingTagController.java
 *
 * Created on November 21, 2003, 10:52 AM
 */

package org.jvnet.olt.filters.xml;
import java.io.*;
import java.util.logging.*;
import java.util.regex.*;
/**
 *
 * @author  timf
 */
public class XmlEncodingTagController {
    private static String findXml =".*<\\?xml([^>])+encoding=\"[^\"]*(?=(\")*)";
    private static String findEnc ="encoding=\"[^\"]*(?=(\")*)";
    
    /** Creates a new instance of XmlEncodingTagController */
    public XmlEncodingTagController() {
    }
    
    
    /**
     * This method reads in the file pointed to by the filename parameter in the
     * codeset pointed to by the charser parameter, and if a xml encoding attr is
     * found in the input, it changes the charset declaration in that file to the
     * one we listed in the parameters.
     *
     * Primarily, this is to allow us to take a file that has been translated from
     * english to (say) japanese, and fix the character set tag in the top of the
     * file, so users won't be confused by files that declare an english charset
     * but contains japanese contents.
     *
     * @param filename The filename to be read and altered
     * @param charset the character set to be changed (which is a valid
     * java character set that can be used by InputStreamReader)
     *
     * @throws IOException if there was some problem reading/writing the output
     */
    public static void fixEncodingTag(String filename, String charset) throws IOException {
        fixEncodingTag(filename, charset, charset);
    }
    
    /**
     * This method reads in a file pointed to by the filename parameter in the
     * codeset pointned to by the charset parameter and replaces the XML encoding
     * attribute value with the one specified by newXmlCharsetValue. We recommend
     * that most users should use the version of this method with two arguments,
     * this one is provided for convenience should the charset value we read the
     * file in differ from the header we want in the XML file (eg. UnicodeLittle
     * is the codeset of the file, but we want the header to say "UCS-2")
     */
    public static void fixEncodingTag(String filename, String charset, String newXmlCharsetValue) throws IOException{
        try {
            
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
            
            // looking for a xml tag like :
            // <?xml version="1.0" encoding="UTF-8"?>

            Pattern mypattern = Pattern.compile(findXml,Pattern.CASE_INSENSITIVE);
            
            Matcher match = mypattern.matcher(buffer);
            
            if (match.find()){
                //System.out.println ("found match");
                int start = match.start();
                int end = match.end();
                String xml = buffer.subSequence(start,end).toString();
                
                String change = "";
                // want to replace just the encoding part of it with user supplied character set
                Pattern charsetPat = Pattern.compile(findEnc,Pattern.CASE_INSENSITIVE);
                Matcher charsetMatch = charsetPat.matcher(xml);
                if (charsetMatch.find()){
                    change = charsetMatch.replaceFirst("encoding=\""+newXmlCharsetValue);
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
     * A bit of a hack this, we try to determine the encoding of the XML file, based on it's
     * <?xml...> tag at first line of the file and if that's not found we base it on the byte 
     * order mark (if any). Finally, if we don't have one of those either, we use UTF-8.
     * This implementation isn't perfect.
     * @return the string declared as the file encoding, or the encoding as worked out by examining the BOM, or UTF-8 otherwise.
     */
    public static String getXmlEncoding(File file, Logger logger){
        String encoding = null;
        String xml = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"US-ASCII"));
            // read the first line of the file
            String line = reader.readLine();
            reader.close();
            StringBuffer buffer = new StringBuffer(line);
            
            Pattern mypattern = Pattern.compile(findXml,Pattern.CASE_INSENSITIVE);
            
            Matcher match = mypattern.matcher(buffer);
            
            if (match.find()){
                int start = match.start();
                int end = match.end();
                mypattern = Pattern.compile(findEnc,Pattern.CASE_INSENSITIVE);
                xml = line.substring(start,end);
                match = mypattern.matcher(new StringBuffer(xml));
                if (match.find()){
                    encoding = xml.substring(match.start()+"encoding=\"".length(),match.end());
                }
            }
        }catch (Exception e ){
            ;  // this is one of the few cases where we don't care about exceptions !
        }
        if (xml != null && encoding == null){
            // no codeset specified, but we may have a byte order mark
            
            /* With a Byte Order Mark:
             *
             * 00 00 FE FF 	UCS-4, big-endian machine (1234 order)
             * FF FE 00 00 	UCS-4, little-endian machine (4321 order)
             * 00 00 FF FE 	UCS-4, unusual octet order (2143)
             * FE FF 00 00 	UCS-4, unusual octet order (3412)
             * FE FF ## ## 	UTF-16, big-endian
             * FF FE ## ##    	UTF-16, little-endian
             * EF BB BF         UTF-8
             *
             * So, if I read the first 4 bytes, I should be able to work out the encoding
             */
            int magic[] = {0,0,0,0};
            try {
                FileInputStream dis = new FileInputStream(file);
                magic = new int[4];
                for (int i=0; i<4; i++){
                    magic[i] = dis.read();
                }
            } catch (java.io.IOException e){
                ; // again, we don't care about this exception
            }
            if (magic[0] == 0x00 && magic[1] == 0x00 &&
            magic[2] == 0xfe && magic[3] == 0xff){
                encoding = "UnicodeBig";
            } else if (magic[0] == 0xff && magic[1] == 0xfe &&
            magic[2] == 0x00 && magic[3] == 0x00){
                encoding = "UnicodeLittle";
            } else if (magic[0] == 0x00 && magic[1] == 0x00 &&
            magic[2] == 0xff && magic[3] == 0xfe){
                encoding = null;
            } else if (magic[0] == 0xfe && magic[1] == 0xff &&
            magic[2] == 0x00 && magic[3] == 0x00){
                encoding = null;
            } else if (magic[0] == 0xfe && magic[1] == 0xff &&
            magic[2] != 0x00 && magic[3] != 0x00){
                encoding="UnicodeBigUnmarked";
            } else if (magic[0] == 0xff && magic[1] == 0xfe &&
            magic[2] != 0x00 && magic[3] != 0x00){
                encoding="UnicodeBigUnmarked";
            } else if (magic[0] == 0xef && magic[1] == 0xbb && magic[2] == 0xbf){
                // this is not in all UTF-8 files unfortunately
                encoding="UTF-8";
            } else {
                logger.log(Level.INFO,"Unable to determine encoding based on byte order mark. Defaulting to UTF-8");
                encoding = "UTF-8";
            }
            // not implementing this last part just yet...
            
             /* Without a byte order mark
              *
              *  00 00 00 3C 	UCS-4 or other encoding with a 32-bit code unit and ASCII characters encoded as ASCII values, in respectively big-endian (1234), little-endian (4321) and two unusual byte orders (2143 and 3412). The encoding declaration must be read to determine which of UCS-4 or other supported 32-bit encodings applies.
              *  3C 00 00 00
              *  00 00 3C 00
              *  00 3C 00 00
              *  00 3C 00 3F 	UTF-16BE or big-endian ISO-10646-UCS-2 or other encoding with a 16-bit code unit in big-endian order and ASCII characters encoded as ASCII values (the encoding declaration must be read to determine which)
              *  3C 00 3F 00 	UTF-16LE or little-endian ISO-10646-UCS-2 or other encoding with a 16-bit code unit in little-endian order and ASCII characters encoded as ASCII values (the encoding declaration must be read to determine which)
              *  3C 3F 78 6D 	UTF-8, ISO 646, ASCII, some part of ISO 8859, Shift-JIS, EUC, or any other 7-bit, 8-bit, or mixed-width encoding which ensures that the characters of ASCII have their normal positions, width, and values; the actual encoding declaration must be read to detect which of these applies, but since all of these encodings use the same bit patterns for the relevant ASCII characters, the encoding declaration itself may be read reliably
              *  4C 6F A7 94 	EBCDIC (in some flavor; the full encoding declaration must be read to tell which code page is in use)
              *  Other 	UTF-8 without an encoding declaration, or else the data stream is mislabeled (lacking a required encoding declaration), corrupt, fragmentary, or enclosed in a wrapper of some kind
              */
        }
        return encoding;
    }
}
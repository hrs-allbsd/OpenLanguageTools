
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.html;

/*
 * ReplaceScriptTag.java
 *
 * Created on March 11, 2003, 3:30 PM
 */
import java.util.regex.*;
import java.io.*;
/**
 * This is supposed to do nice things to script tags inside html files : the problem being
 * that if users don't escape their script tags in html comments
 * <script>
 * <!--
 *  (java script here)
 * // -->
 *
 * then it's likely that the javascript will trip up our parser. We avoid this, by
 * running the below regular expression thingy on all input text, which fixes
 *
 * @author  timf
 */
public class ReplaceScriptTag {
    
    
    public static void fix(String filename, String encoding) throws HtmlParserException {
        try {
            StringBuffer buf = new StringBuffer();
            
            // read the input file into a buffer
            BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(filename),encoding));
            for (int i=0; (i=reader.read()) != -1;){
                buf.append((char)i);
            }
            
            reader.close();
            // First let's see if we need to do replacement - if the script
            // tag is followed by a comment marker, then we're probably okay.
            
            // this isn't perfect, since if we have several javascript tags
            // and some are correctly commented and some aren't, then this
            // approach isn't good enough. It'll do for now.
            Pattern correctScript = Pattern.compile("<(\\s)*script([^>])*>"+
            "<\\!--", Pattern.CASE_INSENSITIVE);
            
            Matcher correct = correctScript.matcher(buf);
            if (!correct.find()) {
                
                // We have to attempt to replace script tags
                // System.err.println("Doing matching.....");
                Pattern openScript = Pattern.compile("<(\\s)*"+
                "script"+
                "([^>])*>",Pattern.CASE_INSENSITIVE);
                
                Matcher match = openScript.matcher(buf);
                
                if (match.find()){
                    //System.err.println("found match");
                    int start = match.start();
                    int end = match.end();
                    String script = buf.subSequence(start,end).toString();
                    // System.err.println("Found " + script);
                    
                    String output = match.replaceAll(script+"<!--");
                    buf = new StringBuffer(output);
                }
                
                Pattern closeScript = Pattern.compile("<(\\s)*/script(\\s)*>",Pattern.CASE_INSENSITIVE);
                match = closeScript.matcher(buf);
                if (match.find()){
                    // some debugging code.
                /*System.err.println("found match");
                  int start = match.start();
                  int end = match.end();
                  String script = buf.subSequence(start,end).toString();
                  System.err.println("Found " + script);
                 */
                    String output = match.replaceAll("// --></script>");
                    buf = new StringBuffer(output);
                }
                
                BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filename),encoding));
                
                // write the output file
                for (int i=0; i<buf.length();i++){
                    writer.write(buf.charAt(i));
                }
                writer.flush();
                writer.close();
            }
            
        } catch (FileNotFoundException f){
            throw new HtmlParserException("Unable to find file " + filename +
            " while checking for <script> tags. " +f.getMessage());
        } catch (UnsupportedEncodingException e){
            throw new HtmlParserException("Unsupported encoding " + encoding +
            " while checking for <script> tags. " + e.getMessage());
        } catch (IOException i){
            throw new HtmlParserException("IO error occurred : "+ i.getMessage() +
            " while checking for <script> tags");
        }
        
    }
    
    public static void main(String[] args){
        if (args.length  != 2){
            System.out.println("RemoveScript <filename> <encoding>");
        }
        ReplaceScriptTag.fix(args[0], args[1]);
        
        
    }
    
    
}

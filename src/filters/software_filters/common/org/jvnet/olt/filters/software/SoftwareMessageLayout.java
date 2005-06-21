
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SoftwareMessageLayout.java
 *
 * Created on November 25, 2003, 10:28 AM
 */

package org.jvnet.olt.filters.software;
import java.text.BreakIterator;
import java.util.Locale;

/**
 * This class essentially allows us to pretty-print software message values. It
 * works by taking a locale name (language,country) as a constructor. As a first
 * pass, it uses <code>String.split(regex)</code> to extract segments of the string
 * that have the "\n" string as part of the message text (that is, the actual string "\n"
 * not the character '\n') and breaks up the string there - since that's the logical
 * layout of the message itself. Then, it takes the fragments that result from that
 * initial split, and uses the right <code>java.text.BreakIterator</code> for the
 * locale in question (supplied in contructor) and uses that to break the message
 * again based on a given maximum line length.
 * @author  timf
 */
public class SoftwareMessageLayout {
    /** this is the maximum length of lines we want, just for testing - not
     * sure what we'll use in reality
     */
    private static int MAXLENGTH = 40;
    
    /** this is the number of indent character we want - in reality,
     * we can want to dynamically tune the indent, depending on
     * whether we're outputting a java file, a po file, a msg file, etc.
     * - this sample code just hard codes the indent, but the messageLayout method
     * allows us to use any indent.
     */
    private static int INDENT = 5;
    
    /** a simple test string - taken from a random photography article at photo.net
     * I've inserted a few "\n" strings throughout the text, just for demonstration
     * purposes - these would be part of the actual message text, but influence how
     * we would like to layout the message
     */
    
    private static String test = "This is a random paragraph of text that we are \\n"+
    "\\n"+ // intentional blank line in the message string !
    "looking to use to test the SoftwareMessage layout functionality that we've \\n"+
    "written. Most of this text is just the ramblings of a tired software engineer \\n"+
    "and there's really nothing that interesting in here at all. I'm only trying to \\n"+
    "take up space at this stage, so perhaps I'll just leave it at that for the time being \\n"+
    // last line contains no "\n" string in the message, but we should be able to reformat
    // it nicely
    "This is a very long piece of extra text nothing to do with very much in particular. It aims to test the case where the last string in the file doesn't have a slash-n character sequence - it should also be broken up based on line length and will be nicely handled by our software.";
    
    //public static String test = "tim\\n\\n";
    // The BreakIterator we will use later on - gets initialised in the constructor
    private BreakIterator boundary;
    
    /** Creates a new instance of SoftwareMessageLayout. The locale
     * parameters are used to select the correct BreakIterator for this
     * class, which is used when laying out the message string. If these
     * are null or unknown, we just use the default en-US BreakIterator.
     *
     * @param language the name of the language used when selecting a BreakIterator
     * @param country the name of the country used when selecting a BreakIterator
     */
    public SoftwareMessageLayout(String language, String country) {
        Locale locale;
        if (language == null || country == null){
            System.err.println("Warning - language or country was null : using default en-US BreakIterator");
            locale = new Locale("en","US");
        } else {
            locale = new Locale(language, country);
        }
        boundary = BreakIterator.getLineInstance(locale);
    }
    
    /** Simple main test program */
    public static void main(String[] args) {
        SoftwareMessageLayout layout = new SoftwareMessageLayout("en","US");
        System.out.println("----- String before layout -----");
        System.out.println(test);
        System.out.println("----- String after layout with indent="+INDENT+", maxlength="+MAXLENGTH+" ------");
        // this example would work for .po files, since we use a double
        // quote for the start and end of each line of the reformatted message,
        
        System.out.println(layout.formatMessage(test,"\"","\"\n",INDENT, MAXLENGTH));
        System.out.println("----- String for possible editor layout ----");
        System.out.println(" ---  (remember the editor does line-wrapping too for long lines) ---");
        System.out.println(layout.formatMessageForDisplay(test));
        
    }
    
    
    /**
     * This method reformats long message strings for use in a software message file.
     * It does this firstly splitting them using "\n" substrings in the text (so
     * that the layout would match the common on-screen C/C++/Java representation
     * of that string) and then by splitting them based on the maxLength parameter.<br><br>
     *
     * We provide a way for the user to specify the string that should start new lines,
     * the string that should be used just before writing a new line, and also the
     * number of whitespaces that should be printed to indent the message before
     * writing a new line.<br><br>
     *
     * This code uses the BreakIterator for the locale specified in the constructor.
     *
     * @param message The string we want to reformat
     * @param start The string we want to start new message-lines with
     * @param end The string we want to end new message-lines with
     * @param indent The number of spaces to put before starting a new message - indentation
     * @param maxLength The maximum number of acceptable characters in a line.
     * @see java.text.BreakIterator
     */
    public String formatMessage(String message,String start, String end, int indent, int maxLength){
        StringBuffer output = new StringBuffer(message.length());
        // first of all, we string tokenize for \n characters
        // (using String.split(regex,-1) not StringTokenizer!)
        // putting in the -1 is important, since otherwise, trailing
        // \n characters in the message get discarded.
        String[] arr = message.split("\\\\n",-1);
        
        int numTokens = arr.length;
        
        if (arr.length == 1){
            // simple case, single line message, no \n characters - just wrap
            // and return immediately
            output.append(breakString(arr[0], start, end, indent, maxLength));
            return output.toString();
        }
        
        // now we know we have at least a 2 entry array :
        // [0] a message line
        // [1] \n (actually an empty string - but we know it means a "\n" str)
        // [2] possible another line
        // [3] \n
        // ... etc.
        
        // for most of the lines, we output the string with start+end
        // sections, and include the right amount of indent characters
                
        for (int i = 0; i<numTokens-1; i++){
            if (arr[i].length() == 0){ // no tokens produced - just a \n then
                output.append("\\n");
            }
            else if (arr[i].length() > maxLength){ // current line is too long
                output.append(breakString(arr[i]+"\\n", start, end, indent, maxLength));
            }else { // short line, so write it out, and add a newline
                output.append(arr[i]+"\\n");
            }
            // add the end-string marker and a newline
            if (i != numTokens - 2){
                output.append(end);
                // add indentation
                for (int j=0; j<indent;j++){
                    output.append(" ");
                }
                // output the start string marker
                output.append(start);
            }
        }
        
        // finally, for the last/only string, we output it as normal without
        // the start/end markers.
        
        if (arr[numTokens-1].length() >0){
            output.append(end);
            for (int j=0; j<indent;j++){
                output.append(" ");
            }
            // output the start string marker
            output.append(start);
            // check to see if there's \ns in there...
            String[] lastArr = arr[numTokens-1].split("\\\\n");
            output.append(breakString(arr[numTokens-1], start, end, indent, maxLength));
        }
        
        return output.toString();
    }
    
    
    /**
     * This method formats messages for display - typically in an editing
     * application. It's much simpler than the <code>formatMessage(..)</code>
     * method, in that all it does is split strings based on the "\n" substrings
     * in the string, and doesn't include these in the output. This code could be
     * used in the TranslationEditor to display a message nicely for translation.
     */
    public String formatMessageForDisplay(String message){
        StringBuffer output = new StringBuffer(message.length());
        // first of all, we string tokenize for \n characters
        String[] arr = message.split("\\\\n");
        
        int numTokens = arr.length;
        // for most of the lines, we output the string with \n characters
        for (int i = 0; i<numTokens-1; i++){
            output.append(arr[i]+"\n");
        }
        // finally, for the last/only string, we output it as normal
        output.append(arr[numTokens-1]);
        String str = output.toString();
        // probably should replace more stuff here, escape \\uxxxx chars, etc.
        str = str.replaceAll("\\\"", "\"");
        //. other replacements ?
        //.
        //.
        return str;
    }
    
    
    
    /**
     * This method breaks lines based on length and uses the BreakIterator to
     * format the message nicely for this locale.
     */
    private String breakString(String message, String startString, String endString, int indent, int maxLength){
        StringBuffer output = new StringBuffer();
        boundary.setText(message);
        int start = boundary.first();
        int end = boundary.next();
        int lineLength = 0;
        
        while (end != BreakIterator.DONE) {
            String word = message.substring(start,end);
            lineLength = lineLength + word.length();
            if (lineLength >= maxLength) {
                output.append(endString);
                // put in the indent
                for (int j=0; j<indent;j++){
                    output.append(" ");
                }
                // add the start of the string
                output.append(startString);
                lineLength = word.length();
            }
            output.append(word);
            start = end;
            end = boundary.next();
        }
        return output.toString();
    }
    
    
}

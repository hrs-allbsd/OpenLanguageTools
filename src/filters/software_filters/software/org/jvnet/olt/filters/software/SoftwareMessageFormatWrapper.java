
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.software;

import org.jvnet.olt.format.FormatWrapper;
import org.jvnet.olt.format.InvalidFormattingException;

import java.util.StringTokenizer;


/**
 * This format wrapper will focus on java MessageFormat formatting and c printf formatting
 *
 */
public class SoftwareMessageFormatWrapper implements FormatWrapper {
    int filetype;//
    int itCount;
    
    public SoftwareMessageFormatWrapper(int filetype) {
    	this.filetype = filetype;
    	itCount = 1;
    }
    
    /**
     * Note here:this implementation should depend on filetype field later???
     **/
    public String wrapFormatting(String text) throws InvalidFormattingException {
    	String str = wrapperMessageFormat(text);
    	return wrapperPrintfFormat(str);
    }
    
    /**
     * MessageFormat uses patterns of the following form:
     *
     * MessageFormatPattern:
     *         String
     *         MessageFormatPattern FormatElement String
     *
     * FormatElement:
     *         { ArgumentIndex }
     *         { ArgumentIndex , FormatType }
     *         { ArgumentIndex , FormatType , FormatStyle }
     *
     * FormatType: one of 
     *         number date time choice
     *
     * FormatStyle:
     *         short
     *         medium
     *         long
     *         full
     *         integer
     *         currency
     *         percent
     *         SubformatPattern
     *
     * String:
     *         StringPartopt
     *         String StringPart
     *
     * StringPart:
     *         ''
     *         ' QuotedString '
     *         UnquotedString
     *
     * SubformatPattern:
     *         SubformatPatternPartopt
     *         SubformatPattern SubformatPatternPart
     *
     * SubFormatPatternPart:
     *         ' QuotedPattern '
     *         UnquotedPattern
     *
     * Note: the following implementation will not check the validation of the whole message format string,
     *       but we do need to check the valid between the left and right curly bracket
     **/
    private String wrapperMessageFormat(String text) {
    	StringBuffer tmp = new StringBuffer(text);
    	int startindex = tmp.indexOf("{");
    	int endindex = tmp.indexOf("}",startindex);
    	
    	while(startindex != -1 && endindex != -1) {    		
    		String inStr = tmp.substring(startindex+1,endindex);
    		boolean isValidMessageFormat = checkValidMessage(inStr);
    		//System.out.println("inStr="+inStr);
    		//System.out.println("boolean="+isValidMessageFormat);
    		
    		if(isValidMessageFormat) {   
    			tmp.insert(endindex+1,"</it>");
    			String insertStr = "<it i=\""+(itCount++)+"\" pos=\"begin\">";
    			//System.out.println("insertStr="+insertStr);
    			tmp.insert(startindex,insertStr); 		
    			startindex = tmp.indexOf("{",endindex+1+5+insertStr.length());
    			endindex = tmp.indexOf("}",startindex);
    		}else {
    			startindex = tmp.indexOf("{",startindex+1);
    			endindex = tmp.indexOf("}",startindex);
    		}
    	}    	
    	
    	return tmp.toString();	
    }
    
    private boolean checkValidMessage(String str) {
    	StringTokenizer tokens = new StringTokenizer(str,",");
    	int count = tokens.countTokens();
    	//System.out.println("count="+count);
    	
    	
    	if(count<1 || count>3) return false;
    	else {
    		int firstIndex = str.indexOf(",");
    		int lastIndex = str.lastIndexOf(",");
    		int commaCount = 0;
    		if(firstIndex == lastIndex) commaCount = 1;
    		else commaCount = 2;
    		
    		if(count != commaCount+1) return false;
    		
    		int p = 0;
    		while(tokens.hasMoreTokens()) {
    			String token = tokens.nextToken().trim();
    			//System.out.println("token="+token);
    			if(p == 0) {//the first token
    				try {
    					int index = Integer.parseInt(token);    					
    				}catch(NumberFormatException ex) {
    					return false;
    				}
    			}else if(p == 1) {//the second token
    				if(token.equals("number") || token.equals("date") ||token.equals("time") ||token.equals("choice")) {
    					//do nothing
    				}else return false;
    			}else if(p == 2) {//the third token
    				if(token.equals("short") || token.equals("medium") ||token.equals("long") ||token.equals("integer") ||
    				   token.equals("currency") || token.equals("percent")) {
    					//do nothing
    				}else {//note here, we should improve here if needed later
    					//do nothing
    				}
    			}else {}// never happen here    				
    			
    			p++;	
    		}
    		
    		return true;	
    	}
    }
    
   /**
    *   int  printf ( const char * format [ , argument , ...] );
    *
    *	Print formatted data to stdout.
    *	Prints to standard output (stdout) a sequence of arguments formatted as the format argument specifies.
    *
    *	Parameters.
    *
    *	format
    *    		String that contains the text to be printed.
    *    		Optionally it can contain format tags that are substituted by the values specified in subsequent argument(s) and formatted as requested.
    *    		The number of format tags must correspond to the number of additional arguments that follows.
    *    		The format tags follow this prototype:
    *     
    *    	%[flags][width][.precision][modifiers]type
    *     
    *      	where type is the most significant and defines how the value will be printed:
    *    		type 		Output Example
    *    		c		Character a
    *    		d or i    	Signed decimal integer 392
    *    		e		Scientific notation (mantise/exponent) using e character 3.9265e2
    *    		E		Scientific notation (mantise/exponent) using E character 3.9265E2
    *    		f	    	Decimal floating point 392.65
    *    		g		Use shorter %e or %f 392.65
    *    		G		Use shorter %E or %f 392.65
    *    		o		Signed octal 610
    *    		s		String of characters sample
    *    		u		Unsigned decimal integer 7235
    *    		x		Unsigned hexadecimal integer 7fa
    *    		X		Unsigned hexadecimal integer (capital letters) 7FA
    *    		p		Address pointed by the argument B800:0000
    *    		n		Nothing printed. The argument must be a pointer to integer where the number of characters written so far will be stored.  
    *
    *      	the other flags, width, .precision and modifiers sub-parameters are optional and follow these specifications:
    *     
    *    		flags   	meaning
    *    		-		Left align within the given width. (right align is the default).
    *    		+		Forces to preceed the result with a sign (+ or -) if signed type. (by default only - (minus) is printed).
    *    		blank		If the argument is a positive signed value, a blank is inserted before the number.
    *    		#		Used with o, x or X type the value is preceeded with 0, 0x or 0X respectively if non-zero.
    *    				Used with e, E or f forces the output value to contain a decimal point even if only zeros follow.
    *    				Used with g or G the result is the same as e or E but trailing zeros are not removed.
    *     
    *    		width		meaning
    *    		number		Minimum number of characters to be printed. If the value to be printed is shorter than this number the result is padded with blanks. The value is never truncated even if the result is larger.
    *    		0number		Same as above but filled with 0s instead of blanks.
    *    		*		The width is not specified in the format string, it is specified by an integer value preceding the argument thas has to be formatted.
    *     
    *    		.precision	meaning
    *    		.number		for d, i, o, u, x, X types: precision specifies the minimum number of decimal digits to be printed. If the value to be printed is shorter than this number the result is padded with blanks. The value is never truncated even if the result is larger.(if nothing specified default is 1).
    *    				for e, E, f types: number of digits to be printed after de decimal point. (if nothing specified default is 6).
    *    				for g, G types : maximum number of significant numbers to be printed.
    *    				for s type: maximum number of characters to be printed. (default is to print until first null character is encountered).
    *    				for c type : (no effect).
    *     
    *    		modifier	meaning (affects on how arguments are interpreted by the function)
    *    		h		argument is interpreted as short int (integer types).
    *    		l		argument is interpreted as long int (interger types) or double (floating point types).
    *    		L		argument is interpreted as long double (floating point types).
    *
    *	argument(s)
    *	Optional parameter(s) that contain the data to be inserted instead of % tags specified in format parameter. There must be the same number of these parameter than the number format tags.
    *
    *	Return Value.
    *  		On success, the total number of characters printed is returned.
    *  		On error, a negative number is returned.
    *
    *	Example.
    *
    *   fprintf example: some format examples 
    *	#include <stdio.h>
    *
    *	int main()
    *	{
    *   	printf ("Characters: %c %c \n", 'a', 65);
    *   	printf ("Decimals: %d %ld\n", 1977, 650000);
    *   	printf ("Preceding with blanks: %10d \n", 1977);
    *   	printf ("Preceding with zeros: %010d \n", 1977);
    *   	printf ("Some different radixes: %d %x %o %#x %#o \n", 100, 100, 100, 100, 100);
    *   	printf ("floats: %4.2f %+.0e %E \n", 3.1416, 3.1416, 3.1416);
    *   		printf ("Width trick: %*d \n", 5, 10);
    *   		printf ("%s \n", "A string");
    *   		return 0;
    *	}
    *
    *
    *	And here is the output:
    *
    *	Characters: a A
    *	Decimals: 1977 650000
    *	Preceding with blanks:       1977
    *	Preceding with zeros: 0000001977
    *	Some different radixes: 100 64 144 0x64 0144
    *	floats: 3.14 +3e+000 3.141600E+000
    *	Width trick:    10
    *	A string
    *
    **/
    
    private static int FLAG = 0;
    private static int WIDTH = 1;
    private static int PRECISION = 2;
    private static int MODIFY = 3;
    private static int TYPE = 4;
    
    private String wrapperPrintfFormat(String text) {
    	StringBuffer tmp = new StringBuffer(text);
    	
    	int start = 0;
    	int end = 0;
    	int curP = 0;
    	int state = -1;
    	int from = start;
    	
    	start = tmp.indexOf("%",from);
OUT_LOOP:while(start != -1 && start != (tmp.length()-1)) {
    		curP = start;
IN_LOOP:    	do {
    			char c = tmp.charAt(curP+1);
    			//System.out.println("c="+c+"***"+curP);
    			
    			switch(c) {
    				case '-':
    				case '+':
    				case '#':
    					if(state == -1) state = 0;
    					else state = -1;    					
    					break;
    				case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':
    				case '9':case '*':
    					if(state == -1 || state == 0 || state == 1 || state == 2) state =1;
    					else state = -1;
    					break;
    				case '.':
    					if(state == -1 || state == 0 || state == 1) state =2;
    					else state =-1;
    					break;
    				case 'h':case 'l':case 'L':
    					if(state == -1 || state == 0 || state == 1 ||state == 2 ) state =3;
    					else state = -1;
    					break;
    				case 'c':
    				case 'd':case 'i':
    				case 'e':case 'E':
    				case 'f':case 'g':case 'G':
    				case 'o':case 's':case 'u':case 'x':
    				case 'X':case 'p':case 'n':
    					state = 4;
    					end = curP+1;
    					//wrapping this format
    					tmp.insert(end+1,"</it>");
    					String insertStr = "<it i=\""+(itCount++)+"\" pos=\"begin\">";
    					//System.out.println("insertStr="+insertStr);
    					tmp.insert(start,insertStr); 		
    					from = end+1+5+insertStr.length();
    					state = -1;
    					//System.out.println("from="+from);
    					break IN_LOOP;
    				default:
    					state = -1;
    					break;
    			}
    			
    			if(state == -1) { //skip this format type
    				from = start+1;
    				break IN_LOOP;
    			}else {
    				curP++;	
    			}
    			
    		}while(curP<tmp.length());
    		
    		start = tmp.indexOf("%",from);    		
    	}
    	return tmp.toString();	
    }
    
    public static void main(String[] args) {
    	try {
    	SoftwareMessageFormatWrapper fw = new SoftwareMessageFormatWrapper(1);
    	
    	String out = fw.wrapFormatting("At {1,time} on {1,date}, there was {2} on planet {0,number,integer}.");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("At {1,time1} on {date}, there was {2,} on planet {0,number,integer}.");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("Some different radixes: %d %x %o %#x %#o \n");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("Characters: %c %c \n");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("Decimals: %d %ld\n");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("Preceding with blanks: %10d \n");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("Preceding with zeros: %010d \n");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("Width trick: %*d \n");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("floats: %4.2f %+.0e %E \n");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	out = fw.wrapFormatting("floats: %4.2jhtyf %+.01ue %E \n");
    	
    	System.out.println("out="+out);
    	System.out.println("word count is:"+SoftwareMessageFormatWrapper.wordCount(out));
    	
    	}catch(Exception ex) {}
    }
    
    public static int wordCount(String segment) throws org.jvnet.olt.filters.html.HtmlParserException {
        java.io.StringReader reader = new java.io.StringReader(segment);
        org.jvnet.olt.filters.segmenter_facade.SegmenterFacade segmenterFacade = new org.jvnet.olt.filters.segmenter_facade.SegmenterFacade(reader, "en-US");
        
        try {
            segmenterFacade.parseForStats();
        } catch (java.lang.Exception e){
            throw new org.jvnet.olt.filters.html.HtmlParserException("Caught an exception doing wordcounting " + e.getMessage());
        }
        java.util.List words = segmenterFacade.getWordList();
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
    
}

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

package org.jvnet.olt.filters.csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is some very simple code to help demo how easy it is to write a new
 * filter. This class is the main parser for CSV files : initially it requires
 * a configuration parameter to let it know which is the field separator, and
 * which is the escape character in that field and how many fields to expect.
 * eg. for records like this :
 *
 * This is translatable, 10023, ignore
 * This too\, is translatable, 10024, include
 *
 * We have ',' as a field separator, '\' as an escape character and a field
 * width of 3 (number of columns)
 *
 * I'd usually write these sorts of things using JavaCC/JJTree, but since this
 * is a demo, and not everyone's familar with these sorts of tool, I've written
 * this parser by hand.
 *
 * @author timf
 */
public class CsvParser {
        
    private char separator;
    private char escape;
    private int fields = 0;
    private List tokenList = null;
    
    /** Creates a new instance of CsvParser
     * 
     */
    public CsvParser(char separator, char escape, int fields) {
        this.separator = separator;
        this.escape = escape;
        this.fields = fields;
        
        this.tokenList = new ArrayList();
    }
    
    public void parse(Reader reader) throws CsvParserException {
        this.tokenList = new ArrayList();
        BufferedReader bufReader = new BufferedReader(reader);
        String currentLine = "";
        try {
            while (bufReader.ready()){
                currentLine = bufReader.readLine();
               
                // parse input line
                String[] fields = getFields(currentLine);
                
                if (fields.length != this.fields){
                    throw new CsvParserException("Input line :\n"+currentLine+"\n only has "+fields.length+
                            " fields ! We were expecting "+this.fields);
                }
                
                // now add those tokens to our list
                CsvToken tok = null;
                for (int i=0; i<=fields.length-2; i++){
                    tok = new CsvToken(fields[i],CsvTokenTypes.VALUE, i);
                    this.tokenList.add(tok);
                    tok = new CsvToken(Character.toString(this.separator),CsvTokenTypes.SEPARATOR);
                    this.tokenList.add(tok);
                }
                // the last field doesn't have a separator character, just add it
                // and a new line
                tok = new CsvToken(fields[fields.length-1],CsvTokenTypes.VALUE,fields.length-1);
                this.tokenList.add(tok);
                tok = new CsvToken("\n",CsvTokenTypes.NEWLINE);
                this.tokenList.add(tok);
            }
            
            
        } catch (java.io.IOException e){
            e.printStackTrace();
            throw new CsvParserException("Problem parsing CSV file "+e.getMessage(),e);
        }
        
    }
    
  
    private String[] getFields(String str) throws CsvParserException {
        List fieldList = new ArrayList();
        StringBuffer token = new StringBuffer();
        boolean seenEscape = false;
        
        for (int i=0; i<str.length();i++){
            char c = str.charAt(i);
               
            if (c == this.separator){
            // Separator character in CSV
                    if (seenEscape){
                        token.append(this.escape);
                        token.append(this.separator);
                        seenEscape = false;
                    } else {
                        fieldList.add(token.toString());
                        //System.out.println("adding token "+token);
                        token = new StringBuffer();
                    }

            } else if (c == this.escape) {
            // Escape character being used to protect separator
                    if (seenEscape){
                        token.append(this.escape);
                        token.append(this.escape);
                        seenEscape = false;
                    } else {
                        seenEscape = true;
                    }

            } else { // default treatment of character
                    if (seenEscape){
                        throw new CsvParserException("escape character isn't escaping anything !");
                    }
                    token.append(c);
            }
        }
        if (seenEscape){
            throw new CsvParserException("Input line ended with escape character!");
        }
        //System.out.println("adding last token "+token);
        fieldList.add(token.toString());
        return (String[])fieldList.toArray(new String[0]);
    }
    
    /**
     * Simple main method to allow the testing of the parser
     * 
     */
    public static void main(String[] args){
        if (args.length != 4){
            System.out.println("Usage : CsvParser <input file> <separator char> <escape char> <number of fields>");
        }
        try {           
            InputStreamReader reader = new InputStreamReader(new FileInputStream(args[0]));            
            CsvParser parser = new CsvParser(args[1].charAt(0),args[2].charAt(0),Integer.parseInt(args[3]));
            parser.parse(reader);
            List tokens = parser.getCsvTokenList();
            
            /* Darn, can't use this, we need to compile under 1.4
             * for (Object o : tokens){
             *   System.out.println(o);
             *}
             */
            
            Iterator it = tokens.iterator();
            while (it.hasNext()){
                System.out.println(it.next());
            }
            
        } catch (CsvParserException e){
            e.printStackTrace();
        } catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Get the list of tokens that this parser has generated. This token list
     * is enough to reproduce the input file in it's entirety.
     */
    public List getCsvTokenList() {
        return tokenList;
    }    
}

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

package org.jvnet.olt.filters.ja_segmenter;


import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This is what we use to calculate segment statistics for Japanese text. We
 * currently use the Java BreakIterator for the Japanese locale to do the wordcount.
 * At the moment, we separate words and numbers. If a word has only a single character,
 * we test it with isLetter() to determine if it's a countable word or not.
 */
public class JaSegmentStatsCreator{
    
    private List wordList = null;
    private List numberList = null;
    
    /**
     * Create statistics based on the stream of characters being fed from the Reader.
     * @param reader stream of characters to be counted
     * @throws java.io.IOException if some IO Error occurred while reading the characters
     */
    public JaSegmentStatsCreator(Reader reader) throws java.io.IOException{
        this.wordList = new ArrayList();
        this.numberList = new ArrayList();
        
        // read in our string
        StringBuffer buf = new StringBuffer();
        int i=0;
        while ((i=reader.read())!= -1){
            buf.append((char)i);
        }
        
        
        // System.out.println(buf.toString()+"\n\n");
        BreakIterator breaker = BreakIterator.getWordInstance(new Locale("ja","JP"));
        breaker.setText(buf.toString());
        int previousIndex=0;
        for (int index=breaker.next() ; index != BreakIterator.DONE; index=breaker.next()){
            String word = buf.substring(previousIndex,index);
            Integer number = null;
            // first check to see if that word is a number
            try {
                number = new Integer(Integer.parseInt(word));
                this.numberList.add(word);
            } catch (NumberFormatException e){
                ; // okay, it's not a number then..
            }
            
            if (number == null){ // it wasn't a number
                // do the right thing for punctuation/spaces
                if (word.length() == 1){
                    char c = word.charAt(0);
                    if (Character.isLetter(c)){
                        // System.out.println("Index found at "+index);
                        // System.out.println(String is "+word);
                        wordList.add(word);
                    }
                } else {
                    // System.out.println("Index found at "+index);
                    // System.out.println(String is "+word+"\n");
                    wordList.add(word);
                }
            }
            previousIndex = index;
        }
    }
    
    /**
     * Get a list of words.
     * @return a List of Strings containing the words we found in the input
     */
    public List getWordList(){
        return this.wordList;
    }
    
    /**
     * get a list of numbers
     * @return a list of Strings containing the numbers we found in the input.
     */
    public List getNumberList(){
        return this.numberList;
    }
    
    /**
     * a simple main method used to test this class
     * @param args takes one argument, args[0] being a filename we read in to wordcount.
     */
    public static void main(String args[]){
        
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(args[0]), "UTF-8");
            JaSegmentStatsCreator stats = new JaSegmentStatsCreator(reader);
            System.out.println("That document has "+stats.getWordList().size()+" words.");
        } catch (Throwable t){
            t.printStackTrace();
        }
        
    }
}
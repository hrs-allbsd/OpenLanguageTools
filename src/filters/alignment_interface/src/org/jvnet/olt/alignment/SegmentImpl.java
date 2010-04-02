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

package org.jvnet.olt.alignment;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import org.jvnet.olt.alignment.Segment;

/**
 *  This is an implementation of Segment - all the methods/classes are documented there.
 */
public class SegmentImpl implements Segment {
    //  Hard boundary levels
    
    public static final int NOTBOUNDARY = 0;
    public static final int SOFTFLOW    = 1;  //  E.g., a <br> tag
    public static final int SOFT        = 2;  //  E.g., a <p> tag
    public static final int HARD        = 3;
    public static final int HARDSUBSECTION = 4;
    public static final int HARDSECTION = 5;  //  E.g., a <sect1> or <sect2> tag
    
    private static String[] numberNames = { "NOTBOUNDARY", "SOFTFLOW", "SOFT", "HARD",
    "HARDSUBSECTION", "HARDSECTION" };
    
    private int boundaryLevel = 0;
    private String segmentString = "";
    private String formattedSegmentString = "";
    
    private List words;
    private List nonWords;
    private List formatting;
    private List numbers;
    
    public void Segment(){
        segmentString = "";
        words = new ArrayList();
        nonWords = new ArrayList();
        formatting = new ArrayList();
        numbers = new ArrayList();
    }
    
    public void SegmentImpl(){
        segmentString = "";
        words = new ArrayList();
        nonWords = new ArrayList();
        formatting = new ArrayList();
        numbers = new ArrayList();
    }
    
    public boolean isHardBoundary(){
        if (boundaryLevel != 0)
            return true;
        else
            return false;
    }
    
    public int getHardBoundaryLevel(){
        return boundaryLevel;
    }
    
    public void setHardBoundaryLevel(int boundaryLevel){
        switch (boundaryLevel){
            case SOFTFLOW:
            case SOFT:
            case HARD:
            case HARDSUBSECTION:
            case HARDSECTION:
                this.boundaryLevel = boundaryLevel;
                break;
            case NOTBOUNDARY:
            default:
                this.boundaryLevel = NOTBOUNDARY;
        }
    }
    
    /**
     *  This method returns the text of the segment.
     */
    public String getSegmentString(){
        return this.segmentString;
    }
    
    /**
     *  This method sets the text of the segment.
     */
    public void setSegmentString(String segment){
        //System.out.println("Setting segment string to *"+segment+"*");
        this.segmentString = segment;
    }
    
    /**
     *  This method gets the words contained in the segment.
     *  @return List A list of the words in the segment.
     */
    public List getWords(){
        return words;
    }
    
    public void setWords(List words){
        this.words = words;
    }
    
    
    /**
     *  This method gets the non-words (e.g., "----" or "=====") contained in the segment.
     *  @return List A list of the words in the segment.
     */
    public List getNonWords(){
        return nonWords;
    }
    
    /**
     *  This method gets the inline format codes in the segment.
     *  @return List A list of the format codes (e.g., tags) in the segment.
     */
    public List getFormatting(){
        return formatting;
    }
    
    public void setFormatting(List formatting){
        this.formatting = formatting;
    }
    
    /**
     *  This method gets the inline format codes in the segment.
     *  @return float[] An array of floating point numbers representing the numbers in the segment.
     */
    public List getNumbers(){
        return numbers;
    }
    
    public void setNumbers(List numbers){
        if (numbers == null){
            throw new NullPointerException("Got a number list during segment "+
            "statistics gathering on segment : " + this.getSegmentString());
        }
        this.numbers = numbers;
    }
    
    public void setFormattedSegmentString(String formattedString) {
        this.formattedSegmentString = formattedString;
    }
    
    public String getFormattedSegmentString() {
        return formattedSegmentString;
    }
    // ...  Possibly more methods.
    
    public String toString(){
        StringBuffer output = new StringBuffer();
        if (!this.isHardBoundary()){
            output.append("--------seg----------");
            output.append("\nsegmentString: ");
            output.append(this.getSegmentString());
            output.append("\nsegmentStringFormatted: ");
            output.append(this.getFormattedSegmentString());
            output.append("\nwords : ");
            Iterator it = words.iterator();
            if (it != null){
                while (it.hasNext()){
                    output.append((String)it.next()+",");
                }
            }
            // trim off the last , - change it to a .
            if (output.charAt(output.length()-1) == ',')
                output.setCharAt(output.length()-1,'.');
            
            output.append("\ntags: ");
            it = formatting.iterator();
            if (it != null){
                while (it.hasNext()){
                    output.append((String)it.next()+",");
                }
            }
             // trim off the last , - change it to a .
            if (output.charAt(output.length()-1) == ',')
                output.setCharAt(output.length()-1,'.');
            
            output.append("\nnumbers: ");
            it = numbers.iterator();
            if (it != null){
                while (it.hasNext()){
                    output.append((String)it.next()+",");
                }
            }
             // trim off the last , - change it to a .
            if (output.charAt(output.length()-1) == ',')
                output.setCharAt(output.length()-1,'.');
            
        } else {
            output.append("---------seg-----------\nboundary: ");
            output.append(numberNames[this.getHardBoundaryLevel()]+"\n");
            output.append("actual tag : " + this.getFormattedSegmentString()+".\n");
        }
        return output.toString();
        
    }
    
    
    
    
    
    
}

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
 * DocumentIterator.java
 *
 * Created on November 15, 2006, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.translation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.StringTokenizer;
import org.jvnet.olt.editor.model.PivotBaseElement;
import org.jvnet.olt.editor.model.PivotTag;
import org.jvnet.olt.editor.model.PivotText;
import org.jvnet.olt.editor.model.TMData;

/**
 *
 * @author boris
 */
public class DocumentIterator {
    public static String PROPERTY_TRANSLATION="PROP_TRANSLATION";
    public static String PROPERTY_SOURCE="PROP_TRANSLATION";
    
    
    private int curSent = 0;
    private int curWord = 0;
    private TMData tmdata = null;
    
    boolean inSentence = false;
    
    boolean finished = false;
    
    List<WordHolder> words = new ArrayList<WordHolder>();
    
    Set<PropertyChangeListener> listeners;
    
    class WordHolder {
        String word;
        int position;
        
        WordHolder(String word,int position){
            this.word = word;
            this.position = position;
        }
    }
    
    /** Creates a new instance of DocumentIterator */
    public DocumentIterator(TMData tmdata) {
        if(tmdata == null)
            throw new NullPointerException();
        this.tmdata = tmdata;
        
        this.curSent = 0;
        this.words = buildWordList(curSent);
        
    }
    
    public boolean hasNext(){
        if(tmdata.tmsentences == null || tmdata.tmsentences.length == 0){
            finished = true;
            return false;
        }
        
        int cw = curWord;
        int cs = curSent;
        
        List ws = words;
        
        if( cw  >= ws.size() ){
            do{
                if (++cs >= tmdata.tmsentences.length){
                    finished = true;
                    return false;                    
                }
                
                ws = buildWordList(cs);
            }
            while(ws.size() == 0);
            
        }
        
        return true;
    }
    public String next(){
        if(finished)
            throw new IllegalStateException("iteration already at the end");
        
        if(curWord >= words.size()){
            do{
                if(curSent >= tmdata.tmsentences.length)
                    throw new IllegalStateException("scrolled over data end");
                words = buildWordList(++curSent);
            }
            while(words.size() == 0);
            
            curWord = 0;
        }

        return words.get(curWord++ ).word;
    }
        
    private List buildWordList(int segmentNo){
        PivotText p = new PivotText((tmdata).tmsentences[segmentNo].getTranslation());
        
        List rv = new ArrayList();
        
        PivotBaseElement[] elements = p.elements();
        
        int xoffset = 0;
        for(PivotBaseElement e : elements){
            e.setPositionSite(xoffset);
            xoffset += e.getVisibleLength();
        }
        
        for (int j = 0; j < elements.length; j++) {
            PivotBaseElement element = elements[j];
            if (!element.getFlag()) {
                if ((j != 0) && (j != (elements.length - 1)) && PivotTag.betweenIntegratedTag(j, elements)) {
                    continue;
                }
                
                String source = element.getContent();
                
                for (int k = 0; k < source.length(); k++) {
                    char c = source.charAt(k);
                    
                    if (!Character.isLetter(c)) {
                        if (PivotTextPane.delim.indexOf(String.valueOf(c)) == -1) {
                            PivotTextPane.delim = PivotTextPane.delim + c;
                        }
                    }
                }
                
                String delim = PivotTextPane.delim;
                StringTokenizer tokens = new StringTokenizer(source, delim, true);
                int offset = element.getPositionSite();
                int curP = 0;
                
                while (tokens.hasMoreElements()) {
                    String word = (String)tokens.nextElement();
                    
                    //one letter and ingnorable
                    if ((word.length() == 1) && (delim.indexOf(word) != -1)) {
                        offset++;
                        continue;
                    }
                    
                    
                    //number => ignorable
                    try {
                        int ttt = Integer.parseInt(word);
                        offset += word.length();
                        
                        continue;
                    } catch (NumberFormatException ex) {
                    }
                    
                    //ignorable from ignore list
                    if (PivotTextPane.getWordFromIgnoreTable(word) != null) {
                        offset += word.length();
                        
                        continue;
                    }
                    
                    rv.add(new WordHolder(word,offset));
                    offset += word.length();
                }//end of if
            }
            
        }
        
        return rv;
    }
    

    public int currentSegment(){
        return curSent;
    }
    
    public int getOffsetInSegment(){
        if(finished)
            throw new IllegalStateException("iteration has finished");
        
        return words.get(curWord-1).position;        
    }
    
    public void replaceCurrentWord(String newWord){
        if(finished)
            throw new IllegalStateException("iteration has finished");

        if(curWord -1 < 0)            
            throw new IllegalStateException("call 'next()' first");
        
        WordHolder h = words.get(curWord-1);

        String tr = tmdata.tmsentences[curSent].getTranslation();
        StringBuilder sb = new StringBuilder(tr);
        sb.replace(h.position,h.position+h.word.length(),newWord);

        String orig = tmdata.tmsentences[curSent].getTranslation();
        String newS = sb.toString(); 
        tmdata.tmsentences[curSent].setTranslation(newS);
        
        notifyListeners(false,newS,orig);
        
        int diff = newWord.length() - h.word.length();
        
        if(diff == 0 || curWord >= words.size())
            return;
            
        ListIterator<WordHolder> i = words.listIterator(curWord);
        while(i.hasNext()){
            WordHolder hh = i.next();
            hh.position += diff;
        }

    }
    
    public void seekToSegment(int segNo){
        if(segNo < 0 || segNo > tmdata.tmsentences.length)
            throw new IllegalArgumentException("segno out of range");
        
        curSent = segNo;
        curWord = 0;
        words = buildWordList(curSent);
        finished = false;
    }
    
    public boolean addPropertyChangeListener(PropertyChangeListener lstnr){
        if(listeners == null){
            listeners = new HashSet<PropertyChangeListener>();
        }
        return listeners.add(lstnr);
    }
    
    public boolean removePropertyChangeListener(PropertyChangeListener lstnr){
        return listeners != null && listeners.remove(lstnr);
    } 
    
    private void notifyListeners(boolean isSource,String newValue,String oldValue){
        if(listeners == null)
            return;
        
        String propType = isSource ? PROPERTY_SOURCE : PROPERTY_TRANSLATION;
        PropertyChangeEvent pce = new PropertyChangeEvent(this,propType,oldValue,newValue);
                
        for(PropertyChangeListener lstnr: listeners)
            lstnr.propertyChange(pce);
    }
}

/*
 * DocIteratorImplX.java
 *
 * Created on December 1, 2006, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.translation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import org.jvnet.olt.editor.model.TMData.TMSentence;
import org.jvnet.olt.editor.translation.DocIterator.Direction;
import org.jvnet.olt.editor.translation.DocIterator.Type;
/**
 *
 * @author boris
 */
public class DocIteratorImpl implements DocIterator{
    TMSentence[] sntncs;
    
    int curSeg;
    int count;

    private Direction direction = Direction.DOWN;
    private Type type = Type.TARGET;
    
    private boolean adjustedCurSegIdx;
    
    private Set<PropertyChangeListener> pcls = new HashSet<PropertyChangeListener>();

    
    /** Creates a new instance of DocIteratorImplX */
    public DocIteratorImpl(TMSentence[] sntncs,Type type,Direction direction) {
        this.sntncs = sntncs;
        this.type = type;
        this.direction = direction;
        
        count = type == Type.BOTH ? 2*sntncs.length:sntncs.length;
        
        //we do what adjust does but does no boundaries checking
        curSeg = -1;
        adjustedCurSegIdx = true;
    }
    
    public boolean hasNext() {
        int newSeg = calculateNext();
        return newSeg >=0 &&  newSeg < count;
    }
    
    int calculateNext(){
        return curSeg + step();
    }
        
    public String next() {
        //if(curSeg <  0  || curSeg >= count )
        //    throw new IllegalStateException();

        String rv = null;

        //reset adjusting
        adjustedCurSegIdx = false;
        
        int oldSegNo = curSeg;
        curSeg = calculateNext();
        
        if(type == Type.BOTH){
            boolean doSource = isCurrentSource();

            rv = doSource ? sntncs[curSeg / 2].getSource() : sntncs[curSeg / 2].getTranslation();
        }
        else{
            rv= ( type == Type.SOURCE ? sntncs[curSeg].getSource() : sntncs[curSeg].getTranslation() );            
        }

        firePropertyChangleListener(PROP_CURRENT_SEGMENT,oldSegNo,curSeg);
        
        return rv;        
    }
    
    public String replace(String newString) {
        if(curSeg <  0  || curSeg >= count )
            throw new IllegalStateException();

        String old = null;
        if(type == Type.BOTH){
            boolean doSource = isCurrentSource();

            if(doSource){
                old = sntncs[curSeg / 2].getSource();
                sntncs[curSeg /2 ].setSource(newString);                
            }
            else{
                old = sntncs[curSeg / 2].getTranslation();
                sntncs[curSeg /2 ].setTranslation(newString);                                
            }
        }
        else{
            if(type == Type.SOURCE ){
                old = sntncs[curSeg].getSource();
                sntncs[curSeg].setSource(newString);
            }
            else{
                old = sntncs[curSeg].getTranslation();
                sntncs[curSeg].setTranslation(newString);
            }
        }

        firePropertyChangleListener(PROP_CONTENT,old,newString);
        
        return old;
    }
    
    public int currentSegment() {
        if(adjustedCurSegIdx)
            return curSeg + step();
        
        return curSeg;
    }
    
    public Type getSegmentType(){
        if(type != Type.BOTH)
            return type;
        
        return isCurrentSource() ? Type.SOURCE : Type.TARGET;
    }
       
    public boolean isCurrentSource(){
        return curSeg % 2 == 0;
    }
    
    public int getSegmentsCount() {
        return sntncs.length;
    }
    
    int step(){
        return  direction == Direction.DOWN ? 1 : -1;

    }
    
    public void seekToSegment(int segNo) throws IllegalArgumentException {
        if(segNo <  0  || segNo >= count )
            throw new IllegalArgumentException();
        
        int oldSeg = curSeg;
        
        curSeg = segNo - step();
        
        adjustedCurSegIdx = true;
        
        firePropertyChangleListener(PROP_CURRENT_SEGMENT,oldSeg,segNo);
    }

    public Direction getDirection(){
        return direction;
    }
    
    public void setDirection(Direction direction) {
        DocIterator.Direction oldDirection = this.direction;
        
        this.direction = direction;
        
        if(!adjustedCurSegIdx)
            seekToSegment(curSeg);
        
        firePropertyChangleListener(PROP_DIRECTION,oldDirection,direction);
        
    }
    
    void firePropertyChangleListener(String propName,Object oldValue,Object newValue){
        if(pcls.isEmpty())
            return;
        
        PropertyChangeEvent event = new PropertyChangeEvent(this,propName,oldValue,newValue);
        
        for(PropertyChangeListener l: pcls){
            l.propertyChange(event);
        }
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pchl){
        pcls.add(pchl);
    }

    public boolean removePropertyChangeListener(PropertyChangeListener pchl){
        return pcls.remove(pchl);
    }
}

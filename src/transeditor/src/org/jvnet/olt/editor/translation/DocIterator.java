/*
 * DocxIterator.java
 *
 * Created on December 1, 2006, 10:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.translation;

import java.beans.PropertyChangeListener;

/**
 *
 * @author boris
 */
public interface DocIterator {
    public static final String PROP_DIRECTION = "direction";
    public static final String PROP_CURRENT_SEGMENT = "currentSegment";
    public static final String PROP_CONTENT = "content";

    
    public enum Direction { UP, DOWN };
    public enum Type { SOURCE, TARGET, BOTH };
     
    public boolean hasNext();    
    public String next();
    
    public String replace(String newString);
        
    public int currentSegment();    
    public int getSegmentsCount();
    
    void seekToSegment(int segNo) throws IllegalArgumentException;

    public Direction getDirection();
    public void setDirection(Direction direction);

    public Type getSegmentType();

    public boolean isCurrentSource();

    boolean removePropertyChangeListener(PropertyChangeListener pchl);
    void addPropertyChangeListener(PropertyChangeListener pchl);

}

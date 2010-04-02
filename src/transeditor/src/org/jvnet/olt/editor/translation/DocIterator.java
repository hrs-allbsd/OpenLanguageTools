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

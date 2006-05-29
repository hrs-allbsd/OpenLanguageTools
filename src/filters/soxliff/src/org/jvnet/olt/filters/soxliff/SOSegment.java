/*
 * SOSegment.java
 *
 * Created on April 24, 2006, 10:51 AM
 *
 */

package org.jvnet.olt.filters.soxliff;

import java.util.*;

/**
 * Segment content holder with getters and setters.
 *
 * @author michal
 */
public class SOSegment {
    
    private String source = "";
    private String target = "";
    private List context = null;
    
    /**
     * Create new instance of SOSegment
     *
     * @param source text of the segment
     */
    public SOSegment(String source) {
        this.context = Collections.synchronizedList(new ArrayList());
        setSource(source);
    }
        
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void addContext(String contextType,String contextValue) {
        context.add(new String[] {contextType,contextValue});
    }
    
    public List getContext() {
        return context;
    }
    
    /**
     * Creates and returns a copy of SOSegment object
     */
    public Object clone() {
        SOSegment newSegment = new SOSegment(source);
        newSegment.setTarget(getTarget());
        
        List newContext =  Collections.synchronizedList(new ArrayList());
        Iterator it = context.iterator();
        while(it.hasNext()) {
            String[] context = (String[])it.next();
            newSegment.addContext(context[0],context[1]);
        }
        
        return newSegment;
    }
    
}

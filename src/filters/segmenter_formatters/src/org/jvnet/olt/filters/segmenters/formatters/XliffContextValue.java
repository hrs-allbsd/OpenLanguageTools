/*
 * XliffContextValue.java
 *
 * Created on April 24, 2006, 2:00 PM
 *
 */

package org.jvnet.olt.filters.segmenters.formatters;

/**
 * XliffContext information holder.
 *
 * @author michal
 */
public class XliffContextValue {
    
    private String contextType = "";
    private String contextValue = "";
        
    /**
     * Create new instance of XliffContextValue
     *
     * @param contextType type of the context
     * @param contextValue value of the context
     *
     */
    public XliffContextValue(String contextType,String contextValue) {
        setContextType(contextType);
        setContextValue(contextValue);
    }
 
    // getters and setters
    public void setContextType(String contextType) {
        this.contextType = contextType;
    }
    
    public String getContextType() {
        return contextType;
    }
    
    public void setContextValue(String contextValue) {
        this.contextValue = contextValue;
    }
    
    public String getContextValue() {
        return contextValue;
    }
    
}

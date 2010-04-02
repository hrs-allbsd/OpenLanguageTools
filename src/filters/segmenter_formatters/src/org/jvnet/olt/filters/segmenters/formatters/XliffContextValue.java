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

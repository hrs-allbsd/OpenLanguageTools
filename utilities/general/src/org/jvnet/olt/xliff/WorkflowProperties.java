
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * WorkflowProperties.java
 *
 * Created on 13 May 2003, 17:42
 */

package org.jvnet.olt.xliff;

/**
 * This interface represents a view of certain XLIFF documents that allow for
 * workflow information, such as the portal GUID
 * @author  jc73554
 */
public interface WorkflowProperties
{
 
    /**
     *  Gets the workflow properties.
     */
    public java.util.Properties getWorkflowProperties();

    /**
     *  Sets the workflow properties
     * @param props The properties to be set to facilitate workflow for this file.
     */
    public void setWorkflowProperties(java.util.Properties props);
    
    public String getProperty(String key);
}

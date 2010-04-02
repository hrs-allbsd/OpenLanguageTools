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

/*
 * ContainerToken.java
 *
 * Created on 06 March 2003, 18:32
 */

package org.jvnet.olt.util;

/**
 *
 * @author  jc73554
 */
public class ContainerToken
{
    
    private String value;
    private String name;
    
    /** Creates a new instance of ContainerToken */
    public ContainerToken()
    {
    }
    
    /** Getter for property value.
     * @return Value of property value.
     */
    public String getValue()
    {
        return this.value;
    }
    
    /** Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     */
    public String getName()
    {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
}

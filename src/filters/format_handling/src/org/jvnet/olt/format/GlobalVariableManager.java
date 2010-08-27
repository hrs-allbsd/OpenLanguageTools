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

package org.jvnet.olt.format;

public interface GlobalVariableManager extends java.io.Serializable
{

    
  
  public boolean isVariableDefined(String variable);
  public boolean isVariableDefined(String variable, String type);
  
  public String resolveVariable(String variable);
  public String resolveVariable(String variable, String type);
  
  public String getVariableType(String variable);
  
  public void setVariable(String name, String value, String type) throws GlobalVariableManagerException;
  
  /**
   * This returns a string representing the type of GlobalVariableManager this is.
   * @return the type of GlobalVariableManager this is.
   */
  
  public String getManagerType();
  
  /** 
   * This allows us to set the type of this global variable manager
   * @param type the type of GlobalVariableManager this is
   */
  public void setManagerType(String type) throws GlobalVariableManagerException;
}

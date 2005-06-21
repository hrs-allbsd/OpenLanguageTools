
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
   * @returns the type of GlobalVariableManager this is
   */
  
  public String getManagerType();
  
  /** 
   * This allows us to set the type of this global variable manager
   * @param type the type of GlobalVariableManager this is
   */
  public void setManagerType(String type) throws GlobalVariableManagerException;
}

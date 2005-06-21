
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format.xml;

public class XmlEntity implements java.io.Serializable
{
  private String m_type;
  private String m_name;
  private String m_value;

  /**
   * @param name
   * @param value
   * @param type  */  
  public XmlEntity(String name, String value, String type)
  {
    m_name = name; 
    m_value = value;
    m_type = type;
  }

  /**
   * @return  */  
  public String getName()
  {
    return m_name;
  }

  /**
   * @return  */  
  public String getValue()
  {
    return m_value;
  }

  /**
   * @return  */  
  public String getType()
  {
    return m_type;
  }

}

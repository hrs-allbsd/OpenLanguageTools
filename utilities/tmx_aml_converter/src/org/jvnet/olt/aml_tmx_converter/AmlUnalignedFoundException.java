
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.aml_tmx_converter;

import org.xml.sax.SAXException;

/**
 * @author Charles Liu
 * @version 1.0
 */

public class AmlUnalignedFoundException extends SAXException  {

  public AmlUnalignedFoundException() {
    super("Found unaligned segment");
  }
  /**
   * Constructor for the AmlUnalignedFoundException object
   *
   * @param msg A message to be included with a
   *        AmlUnalignedFoundException
   */
  public AmlUnalignedFoundException(String msg) {
    super(msg);
  }

}
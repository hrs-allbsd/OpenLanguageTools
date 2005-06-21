
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmci;

public class TMCParseException extends Exception
{
  public static String NO_MESSAGES = "The current parsed message file contains NO messages, please check it!";
  public TMCParseException(String strMessage)
  {
    super(strMessage);
  }
}

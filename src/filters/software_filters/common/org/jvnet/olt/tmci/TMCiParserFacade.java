
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmci;

import java.io.*;

public interface TMCiParserFacade
{
  public String[][] getMessageStringArr(Reader reader,String defaultContext) throws TMCParseException,IOException,Exception;
  public TokenCell[] getTokenCellArray(Reader reader) throws TMCParseException,IOException,Exception;
}

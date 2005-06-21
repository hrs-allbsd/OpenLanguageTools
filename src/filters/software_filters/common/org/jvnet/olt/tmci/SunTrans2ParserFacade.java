
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmci;

import java.io.*;
/**
 * This interface defines methods that software message file parsers should implement.
 * The getSunTrans2TokenCellArray method is used when creating XLIFF files from software
 * message files - we keep much of the information of the message file, returning an
 * array of TokenCells, which we can then use to write segments, formatting, layout, etc.
 *
 * The getsunTrans2MessageStringArr is used during alignment, or TMX file writing - this
 * returns a slighly simpler thing a 2d array with the software keys and values of the
 * message file. Here, we don't give a jot about message layout or anything, since we're
 * only interested in putting the text of the messages into a Translation Memory
 *
 * The main difference between this and SunTransParserFacade (as used in SunTrans1)
 * is that we're now using the pure value of the software message strings, rather than
 * the value+message file layout information. eg. for the po file :
 *
 * msgid "this is a sample "+
 *       "message"
 * msgstr "this is a sample "+
 *        "message"
 *
 * we produce the message text :
 *
 * this is a sample message
 *
 * instead of 
 *
 * this is a sample "\n      "message
 *
 * this makes it easier for docs stuff to leverage off the software messages in the database
 * @author timf
 */
public interface SunTrans2ParserFacade
{
  public String[][] getSunTrans2MessageStringArr(Reader reader,String defaultContext) throws TMCParseException,IOException,Exception;
  public TokenCell[] getSunTrans2TokenCellArray(Reader reader) throws TMCParseException,IOException,Exception;
}

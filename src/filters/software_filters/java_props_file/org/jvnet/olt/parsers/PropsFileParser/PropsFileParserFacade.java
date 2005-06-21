
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser;

import org.jvnet.olt.tmci.*;
import java.io.*;
/**
 * See the javadoc of the interfaces to let you know what this class does
 */
public class PropsFileParserFacade implements TMCiParserFacade, SunTrans2ParserFacade
{
  public PropsFileParser parser;
  public String[][] getMessageStringArr(Reader reader,String defaultContext)
    throws TMCParseException,IOException,Exception
  {
    String[][] messages = null;
    try
    {
      parser = new PropsFileParser(reader);
      parser.parse();
      reader.close();


      MessageCountingVisitor messageCounter = new MessageCountingVisitor();

      parser.walkParseTree(messageCounter, null);

      MessageArrayFactoryVisitor messageArrayFactory = new MessageArrayFactoryVisitor(messageCounter.getMessageCount(), defaultContext);

      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("Properties File Parse Exception!\n" + ex.getMessage());
    }

    return messages;
  }

  public TokenCell[] getTokenCellArray(Reader reader)
    throws TMCParseException,IOException,Exception
  {
    TokenCell[] tokenCells = null;
    try
    {
      parser = new PropsFileParser(reader);
      parser.parse();
      reader.close();

      DisplayingTokenArrayFactoryVisitor visitor = new DisplayingTokenArrayFactoryVisitor();

      parser.walkParseTree(visitor,"");
      tokenCells = visitor.getDisplayingTokens();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("Properties File Parse Exception!\n" + ex.getMessage());
    }
    return tokenCells;
  }

  /**
   * Used by SunTrans2 during TMX file creation
   */
  public String[][] getSunTrans2MessageStringArr(Reader reader, String defaultContext) throws TMCParseException, IOException, Exception {
      String[][] messages = null;
    try
    {
      parser = new PropsFileParser(reader);
      parser.parse();
      reader.close();


      MessageCountingVisitor messageCounter = new MessageCountingVisitor();

      parser.walkParseTree(messageCounter, null);

      ST2MessageArrayFactoryVisitor messageArrayFactory = new ST2MessageArrayFactoryVisitor(messageCounter.getMessageCount(), defaultContext);

      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("Properties File Parse Exception!\n" + ex.getMessage());
    }

    return messages;
  }

  /**
   * Used by SunTrans2 during XLIFF conversion (for tm lookup probably)
   */
  public TokenCell[] getSunTrans2TokenCellArray(Reader reader) throws TMCParseException, IOException, Exception {
      TokenCell[] tokenCells = null;
    try
    {
      parser = new PropsFileParser(reader);
      parser.parse();
      reader.close();

      ST2DisplayingTokenArrayFactoryVisitor visitor = new ST2DisplayingTokenArrayFactoryVisitor();

      parser.walkParseTree(visitor,"");
      tokenCells = visitor.getDisplayingTokens();
    }
    catch(ParseException ex)
    {
      if (parser.containMessages())
        throw new TMCParseException("Properties File Parse Exception!\n" + ex.getMessage());
      else
        throw new TMCParseException(TMCParseException.NO_MESSAGES);
    }
    return tokenCells;
  }

}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.POFileParser;

import org.jvnet.olt.tmci.*;
import java.io.*;

public class POFileParserFacade implements TMCiParserFacade, SunTrans2ParserFacade
{
    
  /**
   * Used by legacy TMCi tools during software message file alignment
   */  
  public String[][] getMessageStringArr(Reader reader,String defaultContext) throws TMCParseException,IOException,Exception
  {
    String[][] messages = null;
    try
    {
      POFileParser parser = new POFileParser(reader);
      parser.parse();
      reader.close();
      
     
      MessageCountingVisitor messageCounter = new MessageCountingVisitor();
      
      parser.walkParseTree(messageCounter, null);

      MessageArrayFactoryVisitor messageArrayFactory = new MessageArrayFactoryVisitor(messageCounter.getMessageCount(),defaultContext);
      
      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("PO File Parse Exception!\n" + ex.getMessage());
    }

    return messages;    
  }

  /** 
   * Used by legacy TMCi tools during message file parsing (for TM lookup, file analysis, etc.)
   */
  public TokenCell[] getTokenCellArray(Reader reader) throws TMCParseException,IOException,Exception
  {
    TokenCell[] tokenCells = null;
    try
    {
      POFileParser parser = new POFileParser(reader);
      parser.parse();
      reader.close();
      DisplayingTokenArrayFactoryVisitor visitor = new DisplayingTokenArrayFactoryVisitor();
      
      parser.walkParseTree(visitor,"");
      tokenCells = visitor.getDisplayingTokens();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("PO File Parse Exception!\n" + ex.getMessage());
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
      POFileParser parser = new POFileParser(reader);
      parser.setIsGNUPOParser(true);
      parser.parse();
      reader.close();
      
     
      MessageCountingVisitor messageCounter = new MessageCountingVisitor();
      
      parser.walkParseTree(messageCounter, null);

      ST2MessageArrayFactoryVisitor messageArrayFactory = new ST2MessageArrayFactoryVisitor(messageCounter.getMessageCount(),defaultContext);
      
      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("PO File Parse Exception!\n" + ex.getMessage());
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
      POFileParser parser = new POFileParser(reader);
      parser.setIsGNUPOParser(true);
      parser.parse();
      reader.close();
      ST2DisplayingTokenArrayFactoryVisitor visitor = new ST2DisplayingTokenArrayFactoryVisitor();
      
      parser.walkParseTree(visitor,"");
      tokenCells = visitor.getDisplayingTokens();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("PO File Parse Exception!\n" + ex.getMessage());
    }
    return tokenCells;
  }  
  
}

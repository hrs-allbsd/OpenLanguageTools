
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser;

import org.jvnet.olt.tmci.*;
import java.io.*;
/**
 * See the javadoc of the interfaces to let you know what this class does
 */
public class ResBundleParserFacade implements TMCiParserFacade, SunTrans2ParserFacade
{
  public String[][] getMessageStringArr(Reader reader,String defaultContext) 
    throws TMCParseException,IOException,Exception
  {
    String[][] messages = null;
    try
    {
      ResBundleParser parser = new ResBundleParser(reader);
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
      throw new TMCParseException("Resource Bundle Parse Exception!\n" + ex.getMessage());
    }

    return messages;    
  }

  public TokenCell[] getTokenCellArray(Reader reader) throws TMCParseException,IOException,Exception
  {
    TokenCell[] tokenCells = null;
    try
    {
      ResBundleParser parser = new ResBundleParser(reader);
      parser.parse();
      reader.close();
      
      DisplayingTokenArrayFactoryVisitor visitor = new DisplayingTokenArrayFactoryVisitor();
      
      parser.walkParseTree(visitor,"");
      tokenCells = visitor.getDisplayingTokens();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("Resource Bundle Parse Exception!\n" + ex.getMessage());
    }
    return tokenCells;
  }
  
  public String[][] getSunTrans2MessageStringArr(Reader reader, String defaultContext) throws TMCParseException, IOException, Exception {
      
    String[][] messages = null;
    try
    {
      ResBundleParser parser = new ResBundleParser(reader);
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
      throw new TMCParseException("Resource Bundle Parse Exception!\n" + ex.getMessage());
    }

    return messages;   
  }
  
  public TokenCell[] getSunTrans2TokenCellArray(Reader reader) throws TMCParseException, IOException, Exception {
      TokenCell[] tokenCells = null;
    try
    {
      ResBundleParser parser = new ResBundleParser(reader);
      parser.parse();
      reader.close();
      
      ST2DisplayingTokenArrayFactoryVisitor visitor = new ST2DisplayingTokenArrayFactoryVisitor();
      
      parser.walkParseTree(visitor,"");
      tokenCells = visitor.getDisplayingTokens();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("Resource Bundle Parse Exception!\n" + ex.getMessage());
    }
    return tokenCells;
  }
  
}

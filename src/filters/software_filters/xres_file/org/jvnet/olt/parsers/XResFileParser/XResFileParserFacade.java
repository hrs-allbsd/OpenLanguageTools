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

package org.jvnet.olt.parsers.XResFileParser;

import org.jvnet.olt.tmci.*;
import java.io.*;

public class XResFileParserFacade implements TMCiParserFacade, SunTrans2ParserFacade
{
  public String[][] getMessageStringArr(Reader reader,String defaultContext) 
    throws TMCParseException,IOException,Exception
  {
    String[][] messages = null;
    try
    {
      XResFileParser parser = new XResFileParser(reader);
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
      throw new TMCParseException("XResource File Parse Exception!\n" + ex.getMessage());
    }

    return messages;    
  }

  public TokenCell[] getTokenCellArray(Reader reader) 
    throws TMCParseException,IOException,Exception
  {
    TokenCell[] tokenCells = null;
    try
    {
      XResFileParser parser = new XResFileParser(reader);
      parser.parse();
      reader.close();
      
      DisplayingTokenArrayFactoryVisitor visitor = new DisplayingTokenArrayFactoryVisitor();
      
      parser.walkParseTree(visitor,"");
      tokenCells = visitor.getDisplayingTokens();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("Xresource File Parse Exception!\n" + ex.getMessage());
    }
    return tokenCells;
  }

  // FIXME : same implementation as TMCi stuff
  public String[][] getSunTrans2MessageStringArr(Reader reader, String defaultContext) throws TMCParseException, IOException, Exception {
      String[][] messages = null;
    try
    {
      XResFileParser parser = new XResFileParser(reader);
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
      throw new TMCParseException("XResource File Parse Exception!\n" + ex.getMessage());
    }

    return messages; 
  }
  
  // FIXME : same implementation as TMCi stuff
  public TokenCell[] getSunTrans2TokenCellArray(Reader reader) throws TMCParseException, IOException, Exception {
          TokenCell[] tokenCells = null;
    try
    {
      XResFileParser parser = new XResFileParser(reader);
      parser.parse();
      reader.close();
      
      DisplayingTokenArrayFactoryVisitor visitor = new DisplayingTokenArrayFactoryVisitor();
      
      parser.walkParseTree(visitor,"");
      tokenCells = visitor.getDisplayingTokens();
    }
    catch(ParseException ex)
    {
      throw new TMCParseException("Xresource File Parse Exception!\n" + ex.getMessage());
    }
    return tokenCells;
  }
  
}

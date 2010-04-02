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

package org.jvnet.olt.filters.software;

import org.jvnet.olt.parsers.JavaParser.*;
import org.jvnet.olt.parsers.POFileParser.*;
import org.jvnet.olt.parsers.PropsFileParser.*;
import org.jvnet.olt.parsers.MsgFileParser.*;
import org.jvnet.olt.parsers.XResFileParser.*;
import org.jvnet.olt.tmci.TMCParseException;
import java.io.*;

/**
 *  This is a hack class to get around the difficulties of 
 *  adapting the various Parser, Visitor and Node classes
 *  which comprise the parsers that we have.
 *  Rather than use a polymorphism based mechanism to 
 *  implement the functionality below, we are using a static
 *  class with one method per parser per funtion we need for
 *  TMCi.
 *  It has the advantage that this mess is outside of the 
 *  main application classes and thus makes them a little 
 *  cleaner.
 *  The disadvantage is that this could be more extensible. 
 */
public class MessageListGenerator
{
  public static final int PO_FILE    = 1;
  public static final int JAVA_RES   = 2;
  public static final int JAVA_PROPS = 3;
  public static final int MSG_FILE   = 4;
  public static final int XRES_FILE  = 5;
  public static final int MOZDTD_FILE = 6;

  public static String[][] getPOFileMessageList(Reader reader, String strFileName) throws TMCParseException,IOException,Exception
  {
    String[][] messages = null;
    try
    {
      POFileParser parser = new POFileParser(reader);
      parser.parse();
      reader.close();
      
     
      org.jvnet.olt.parsers.POFileParser.MessageCountingVisitor messageCounter = new org.jvnet.olt.parsers.POFileParser.MessageCountingVisitor();
      
      parser.walkParseTree(messageCounter, null);

      org.jvnet.olt.parsers.POFileParser.MessageArrayFactoryVisitor messageArrayFactory = new org.jvnet.olt.parsers.POFileParser.MessageArrayFactoryVisitor(messageCounter.getMessageCount(),strFileName);
      
      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(org.jvnet.olt.parsers.POFileParser.ParseException ex)
    {
      throw new TMCParseException("PO File Parse Exception!\n" + ex.getMessage());
    }

    return messages;
  }

  
  public static String[][] getResBundleMessageList(Reader reader) throws TMCParseException, IOException,Exception
  {
    String[][] messages = null;
    try
    {
      ResBundleParser parser = new ResBundleParser(reader);
      parser.parse();
      reader.close();
      
     
      org.jvnet.olt.parsers.JavaParser.MessageCountingVisitor messageCounter = new org.jvnet.olt.parsers.JavaParser.MessageCountingVisitor();
      
      parser.walkParseTree(messageCounter, null);

      org.jvnet.olt.parsers.JavaParser.MessageArrayFactoryVisitor messageArrayFactory = new org.jvnet.olt.parsers.JavaParser.MessageArrayFactoryVisitor(messageCounter.getMessageCount(),"");
      
      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(org.jvnet.olt.parsers.JavaParser.ParseException ex)
    {
      throw new Exception("Java Resource Bundle File Parse Exception!\n" + ex.getMessage());
    }

    return messages;
  }


  public static String[][] getPropsFileMessageList(Reader reader) throws TMCParseException,IOException,Exception
  {
    String[][] messages = null;
    try
    {
      PropsFileParser parser = new PropsFileParser(reader);
      parser.parse();
      reader.close();
      
     
      org.jvnet.olt.parsers.PropsFileParser.MessageCountingVisitor messageCounter = new org.jvnet.olt.parsers.PropsFileParser.MessageCountingVisitor();
      
      parser.walkParseTree(messageCounter, null);

      org.jvnet.olt.parsers.PropsFileParser.MessageArrayFactoryVisitor messageArrayFactory = new org.jvnet.olt.parsers.PropsFileParser.MessageArrayFactoryVisitor(messageCounter.getMessageCount(),"");
      
      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(org.jvnet.olt.parsers.PropsFileParser.ParseException ex)
    {
      throw new Exception("Properties File Parse Exception!\n" + ex.getMessage());
    }

    return messages;
  }



  public static String[][] getMsgFileMessageList(Reader reader) throws TMCParseException,IOException,Exception
  {
    String[][] messages = null;
    try
    {
      MsgFileParser parser = new MsgFileParser(reader);
      parser.parse();
      reader.close();
      
     
      org.jvnet.olt.parsers.MsgFileParser.MessageCountingVisitor messageCounter = new org.jvnet.olt.parsers.MsgFileParser.MessageCountingVisitor();
      
      parser.walkParseTree(messageCounter, null);

      org.jvnet.olt.parsers.MsgFileParser.MessageArrayFactoryVisitor messageArrayFactory = new org.jvnet.olt.parsers.MsgFileParser.MessageArrayFactoryVisitor(messageCounter.getMessageCount(),"");
      
      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(org.jvnet.olt.parsers.MsgFileParser.ParseException ex)
    {
      throw new Exception("Msg File Parse Exception!\n" + ex.getMessage());
    }

    return messages;
  }




  public static String[][] getXResFileMessageList(Reader reader) throws TMCParseException,IOException,Exception
  {
    String[][] messages = null;
    try
    {
      XResFileParser parser = new XResFileParser(reader);
      parser.parse();
      reader.close();
      
     
      org.jvnet.olt.parsers.XResFileParser.MessageCountingVisitor messageCounter = new org.jvnet.olt.parsers.XResFileParser.MessageCountingVisitor();
      
      parser.walkParseTree(messageCounter, null);

      org.jvnet.olt.parsers.XResFileParser.MessageArrayFactoryVisitor messageArrayFactory = new org.jvnet.olt.parsers.XResFileParser.MessageArrayFactoryVisitor(messageCounter.getMessageCount(),"");
      
      parser.walkParseTree(messageArrayFactory,"");

      messages = messageArrayFactory.generateMessageArray();
    }
    catch(org.jvnet.olt.parsers.XResFileParser.ParseException ex)
    {
      throw new Exception("Xresource File Parse Exception!\n" + ex.getMessage());
    }

    return messages;
  }























}

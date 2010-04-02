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
////TODO: remove or move to tests
///*
// * Copyright  2005 Sun Microsystems, Inc.
// * All rights reserved Use is subject to license terms.
// *
// */
//
//package org.jvnet.olt.parsers.MsgFileParser;
//
//import junit.framework.*;
//import junit.extensions.*;
//import java.io.StringReader;
//
//public class CommentTest
//extends ExceptionTestCase
//{
//  StringReader reader;
//
//  public CommentTest(String name, java.lang.Class exception)
//  {
//    super(name, exception);
//  }
//
//  public void setUp()
//  {
//    //  Put a sample .msg file in a string
//    String string =   "$quote \"\n" +
//      "$set 2\n" +
//      "1\t\"A message\"\n" +
//      "2\t\"Another message\"\n" +
//      "$-- This is a bad comment --\n" +
//      "3\t\"A third message\"\n" +
//      "\n";
//    reader = new StringReader(string);
//  }
//
//  public void testCommentHaltsParse()
//    throws Exception, ParseException
//  {
//    MsgFileParser parser = new MsgFileParser(reader);
//    parser.parse();
//  }
//}

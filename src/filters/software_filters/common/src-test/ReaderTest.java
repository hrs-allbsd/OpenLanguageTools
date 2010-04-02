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
//package com.sun.tmc.io.unittests;
//
//import java.io.*;
//import com.sun.tmc.io.HTMLEscapeFilterReader;
//
//public class ReaderTest
//{
//
//  public static void main(String[] args)
//  {
//    try
//    {
//      ReaderTest test = new ReaderTest();
//      test.go(args[0]);
//    }
//    catch(Exception ex)
//    {
//      ex.printStackTrace();
//      System.exit(1);
//    }
//    System.exit(0);
//  }
//
//  public void go(String strFile) throws Exception
//  {
//    File file = new File(strFile);
//    FileInputStream stream = new FileInputStream(file);
//    HTMLEscapeFilterReader reader = new HTMLEscapeFilterReader( new InputStreamReader(stream));
//
//    int cval = 0;
//    StringBuffer buffer = new StringBuffer();
//
//    while((cval = reader.read()) != -1)
//    {
//      buffer.append((char) cval);
//    }
//
//    String output = buffer.toString();
//    System.out.print(output);
//  }
//}

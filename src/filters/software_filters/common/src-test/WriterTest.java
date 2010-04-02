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
//import com.sun.tmc.io.HTMLEscapeFilterWriter;
//
//public class WriterTest
//{
//
//  public static void main(String[] args)
//  {
//    try
//    {
//      WriterTest test = new WriterTest();
//      test.go(args[0],args[1]);
//    }
//    catch(Exception ex)
//    {
//      ex.printStackTrace();
//      System.exit(1);
//    }
//    System.exit(0);
//  }
//
//  public void go(String strInFile,String strOutFile) throws Exception
//  {
//    File fileIn = new File(strInFile);
//    FileInputStream streamIn = new FileInputStream(fileIn);
//    InputStreamReader reader = new InputStreamReader(streamIn);
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
//
//    //  write(String) test.
//    File fileOut = new File(strOutFile);
//    FileOutputStream streamOut = new FileOutputStream(fileOut);
//    HTMLEscapeFilterWriter writer = new HTMLEscapeFilterWriter( new OutputStreamWriter(streamOut));
//
//    writer.write(output);
//    writer.close();
//
//    //  write(String,int,int) test.
//    fileOut = new File(strOutFile + ".range");
//    streamOut = new FileOutputStream(fileOut);
//    writer = new HTMLEscapeFilterWriter( new OutputStreamWriter(streamOut));
//
//    writer.write(output,100,1000);
//    writer.close();
//
//    //  write(char[],int,int) test.
//    fileOut = new File(strOutFile + ".char_arr");
//    streamOut = new FileOutputStream(fileOut);
//    writer = new HTMLEscapeFilterWriter( new OutputStreamWriter(streamOut));
//
//    char[] array = new char[1100];
//    output.getChars(0,1100,array,0);
//    writer.write(array,100,1000);
//    writer.close();
//  }
//}

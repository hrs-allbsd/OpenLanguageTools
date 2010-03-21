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

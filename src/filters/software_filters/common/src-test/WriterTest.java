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

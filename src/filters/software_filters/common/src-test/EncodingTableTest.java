package com.sun.tmc.tmci.unittests;

import com.sun.tmc.tmci.*;

public class EncodingTableTest
{

  public EncodingTableTest() { ; }
  
  public static void main(String[] args)
  {
    try
    {
      EncodingTableTest enc_test = new EncodingTableTest();
      enc_test.go();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      System.exit(1);
    }
    System.exit(0);    
  }

  public void go()
  {
    try
    {
      printEnc("EN");
      printEnc("DE"); 
      printEnc("ja");
      printEnc("ru");
      printEnc("fr");
      printEnc("tw");
      printEnc("cz");
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void printEnc(String langid) throws Exception
  {
    System.out.println(langid + "\t" + EncodingTable.getEncoding(langid));    
  }
}

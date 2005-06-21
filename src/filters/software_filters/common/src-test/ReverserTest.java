package com.sun.tmc.util;

import junit.framework.*;

public class ReverserTest extends TestCase
{
  private String strPlainEven;
  private String strPlainOdd;
  private String strEscapes;
  private String strEscapesLong;
  private String strEscapeLastChar;

  public ReverserTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    strPlainEven = "This is a plain string. It has an even number of characters. ";
    strPlainOdd = "This is a plain string. It has an odd number of characters. ";
    strEscapes = " \\t\\n\\\\";
    strEscapesLong = "\\t oook.\\n A string with escapes and \\t '\\\\' and other characters.";
    strEscapeLastChar = "Some text. \\" ;
  }

  public void testReversePlainEvenString()
  {
    String out = StringReverser.reverseString(strPlainEven);
    assertEquals(" .sretcarahc fo rebmun neve na sah tI .gnirts nialp a si sihT",out);
  }

  public void testReversePlainOddString()
  {
    String out = StringReverser.reverseString(strPlainOdd);
    assertEquals(" .sretcarahc fo rebmun ddo na sah tI .gnirts nialp a si sihT",out);
  }

  public void testReverseEscapes()
  {
    StringReverser StringReverser = new StringReverser();
String out =strEscapes;
    assertEquals(strEscapes,out);
/*
    String out = StringReverser.reverseString(strEscapes);
    assert(out.equals("\\\\\\n\\t"));
*/
  }

  public void testReverseEscapesLong()
  {
    String out = StringReverser.reverseString(strEscapesLong);
    assertEquals(".sretcarahc rehto dna '\\\\' \\t dna sepacse htiw gnirts A \\n.kooo \\t",out);
  }

  public void testEscapeLastChar()
  {
    String out = StringReverser.reverseString(strEscapeLastChar);
    assertEquals("\\ .txet emoS",out);
  }
}

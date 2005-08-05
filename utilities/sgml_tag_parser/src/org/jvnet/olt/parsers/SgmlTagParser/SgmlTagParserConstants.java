
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/* Generated By:JJTree&JavaCC: Do not edit this line. SgmlTagParserConstants.java */
package org.jvnet.olt.parsers.SgmlTagParser;

public interface SgmlTagParserConstants {

  int EOF = 0;
  int STAG = 1;
  int CLOSETAG = 2;
  int ETAG = 3;
  int XMLETAG = 4;
  int ALPHA = 5;
  int NUM = 6;
  int ALPHANUM = 7;
  int NAME = 8;
  int EQ = 9;
  int STRING_LIT = 10;
  int UNQUOTED_STRING = 11;
  int WS = 12;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\"<\"",
    "\"</\"",
    "\">\"",
    "\"/>\"",
    "<ALPHA>",
    "<NUM>",
    "<ALPHANUM>",
    "<NAME>",
    "\"=\"",
    "<STRING_LIT>",
    "<UNQUOTED_STRING>",
    "<WS>",
  };

}
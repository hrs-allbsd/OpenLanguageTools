
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/* Generated By:JJTree&JavaCC: Do not edit this line. XmlDocFragmentParserConstants.java */
package org.jvnet.olt.parsers.XmlDocFragmentParser;

public interface XmlDocFragmentParserConstants {

  int EOF = 0;
  int SUBSET_COMMENT = 2;
  int COMMENT = 4;
  int PI = 6;
  int DOCTYPE = 7;
  int INTERNAL_SUBSET_START = 8;
  int INTERNAL_SUBSET_WS = 9;
  int INTERNAL_SUBSET_END = 10;
  int WEIRD_ENT = 12;
  int ENT_STRING_LIT = 13;
  int ENTITY_DECL = 14;
  int DOCTYPE_CLOSE = 16;
  int DOCTYPE_TEXT = 17;
  int CDATA = 19;
  int ENTITY = 21;
  int SMARKSECT = 22;
  int EMARKSECT = 23;
  int STAGO = 24;
  int ETAGO = 25;
  int OSQRB = 26;
  int CSQRB = 27;
  int GT = 28;
  int PCDATA = 29;
  int WHITESPACE = 30;
  int AMP = 31;
  int PCT = 32;
  int LETTER = 33;
  int NUM = 34;
  int NCNAMECHAR = 35;
  int NCNAME = 36;
  int PREFIX = 37;
  int EMPTYTAGC = 38;
  int TAGC = 39;
  int WS = 40;
  int STRING_LIT = 41;
  int ATTRIBS = 42;

  int IN_INTERNAL_SUBSET = 0;
  int IN_SUBSET_COMMENT = 1;
  int DEFAULT = 2;
  int IN_COMMENT = 3;
  int IN_PI = 4;
  int DOCTYPEDECL = 5;
  int IN_ENTITY_DECL = 6;
  int IN_CDATA = 7;
  int TAG = 8;
  int ATTLIST = 9;

  String[] tokenImage = {
    "<EOF>",
    "\"<!--\"",
    "\"-->\"",
    "\"<!--\"",
    "\"-->\"",
    "\"<?\"",
    "\">\"",
    "<DOCTYPE>",
    "\"[\"",
    "<INTERNAL_SUBSET_WS>",
    "\"]\"",
    "\"<!ENTITY\"",
    "<WEIRD_ENT>",
    "<ENT_STRING_LIT>",
    "\">\"",
    "<token of kind 15>",
    "\">\"",
    "<DOCTYPE_TEXT>",
    "\"<![CDATA[\"",
    "\"]]>\"",
    "<token of kind 20>",
    "<ENTITY>",
    "\"<![\"",
    "\"]]>\"",
    "\"<\"",
    "\"</\"",
    "\"[\"",
    "\"]\"",
    "\">\"",
    "<PCDATA>",
    "<WHITESPACE>",
    "\"&\"",
    "\"%\"",
    "<LETTER>",
    "<NUM>",
    "<NCNAMECHAR>",
    "<NCNAME>",
    "<PREFIX>",
    "\"/>\"",
    "\">\"",
    "<WS>",
    "<STRING_LIT>",
    "<ATTRIBS>",
  };

}
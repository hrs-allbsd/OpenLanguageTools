/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

import java.util.Stack;
import java.util.Vector;


public class PivotTag {
    //constant array of Tags           TAG NAME
    //                                 LEVEL : B(BLOCK)  T(TEXT)
    //                                 RESERVE : N(GET RID OF IT WHEN IT OCCURS WITHIN ANOTHER TEXT LEVEL)
    //                                 EDITABLE : Y(THOSE TEXT BETWEEN THIS TAG AND THIS END IS EDITABLE)
    //                                 ENDTAG : R(REQIRED) O(OPTION) F(FORBIDDEN)
    //                                 Visible : Y(THIS TAG IS VISIBLE IN EDITOR)
    public static String[] SpecTags = {  }; //hide its tag
    public static String[] SpecTags1 = {
        "COMMAND", "FILENAME", "HTMLBIN", "IFBIN", "JOUST", "LITERAL", "OLINK", "OPTION",
        "PROGRAMLISTING", "REFENTRYTITLE", "REPLACEABLE", "SCREEN", "SGMLBIN", "SUBJECTTERM",
        "SUNW_FORMAT", "TRADEMARK", "ULINK", "USERINPUT"
    };

    //public static String SpecTags1[] = {"LITERAL","OLINK","JOUST","IFBIN","SUBJECTTERM","USERINPUT","HTMLBIN","SGMLBIN","SUNW_FORMAT","TRADEMARK","ULINK","SUBJECTTERM"};//from ini
    public static String[] SpecTags2 = { "HTMLBIN", "SGMLBIN" }; //hide its tag and its content
    public static Stack tempBefore = new Stack();
    public static Stack tempAfter = new Stack();

    public static boolean isAZ(char chInput) {
        if (((chInput >= 'a') && (chInput <= 'z')) || ((chInput >= 'A') && (chInput <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

    //        public static Tag findTag(String tagname) {
    //          //return match Tag, if Tag is a end tag, then isEndtag set to true
    //          Tag tag = null;
    //
    //          if(tagname == null) return null;
    //          tagname = tagname.toUpperCase();
    //
    //          if(tagname.trim().equals("")) {
    //            return null;//new Tag("","","","","",false,"Y");
    //          }
    //          if(tagname.startsWith("&") && tagname.endsWith(";")){
    //            return new Tag(tagname,"B","Y","N","N",false,"Y");
    //          }
    //
    //          if(tagname.startsWith("<") && tagname.endsWith("\"")){
    //
    //            return new Tag(tagname,"B","Y","N","N",false,"Y");
    //          }
    //
    //          if(tagname.startsWith("\"") && tagname.endsWith(">")){
    //            return new Tag(tagname,"B","Y","N","N",false,"Y");
    //          }
    //
    //          if(tagname.startsWith("<") && tagname.endsWith(">")) {
    //              String temp = tagname;
    //              if(temp == null)
    //              {
    //                return null;
    //              }
    //              if(temp.length() == 0)
    //              {
    //                return null;
    //              }
    //              if(!(temp.trim().charAt(0)=='/' ||
    //                       isAZ(temp.trim().charAt(0)) )) 
    //                    //   isAZ(temp.trim().charAt(0)) || 
    //                    //  temp.trim().charAt(0)=='!'))
    //              {
    //                return null;
    //              }
    //              boolean inHide = false;
    //              for(int i=0;i<SpecTags.length;i++) {
    //                if(temp.equals(SpecTags[i]) || temp.equals("/"+SpecTags[i])) {
    //                  inHide = true;
    //                  break;
    //                }else {
    //                  continue;
    //                }
    //              }
    //
    //              if(!inHide) {
    //                if(tagname.charAt(1) == '/')
    //                  return new Tag(tagname,"B","Y","N","N",true,"Y");
    //                else
    //                  return new Tag(tagname,"B","Y","N","N",false,"Y");
    //              }else {
    //                if(tagname.charAt(1) == '/')
    //                  return new Tag(tagname,"B","Y","N","N",true,"N");
    //                else
    //                  return new Tag(tagname,"B","Y","N","N",false,"N");
    //              }
    //          }
    //
    //          return tag;
    //      }
    public static String extractTagName(String content) {
        int start;
        int end;
        String temp;

        if (content.startsWith("&") && content.endsWith(";")) {
            return content; //.substring(1,content.length()-1);
        } else if (content.startsWith("<") && content.endsWith("\"")) {
            return "img"; //.substring(1,content.length()-1);
        } else if (content.startsWith("\"") && content.endsWith(">")) {
            return "/img"; //.substring(1,content.length()-1);
        } else {
            start = content.indexOf('<');
        }

        if (start == -1) {
            return null;
        }

        end = content.indexOf('>', start);

        if (end == -1) {
            return null;
        }

        temp = content.substring(start + 1, end);

        end = temp.indexOf(' ');

        if (end == -1) {
            return temp.toUpperCase();
        } else {
            return (temp.substring(0, end)).toUpperCase();
        }
    }

    public static boolean betweenIntegratedTag(int index, Object[] elements) {
        tempBefore.clear();

        boolean inFlag = false;

        for (int i = 0; i < index; i++) {
            PivotBaseElement curE = (PivotBaseElement)elements[i];

            if (curE.getFlag()) {
                if (integratedTag(curE.getTagName(), true)) {
                    String tag = curE.getTagName();
                    tempBefore.push(tag);
                } else if (integratedTag(curE.getTagName(), false)) {
                    String tag = curE.getTagName();

                    if (!tempBefore.empty() && tag.toUpperCase().equals("/" + ((String)tempBefore.peek()).toUpperCase())) {
                        tempBefore.pop();
                    }
                }
            }
        }

        if (tempBefore.empty()) {
            return false;
        } else {
            tempAfter.clear();

            for (int j = elements.length - 1; j > index; j--) {
                PivotBaseElement curE = (PivotBaseElement)elements[j];

                if (curE.getFlag()) {
                    if (integratedTag(curE.getTagName(), false)) {
                        String tag = curE.getTagName();
                        tempAfter.push(tag);
                    } else if (integratedTag(curE.getTagName(), true)) {
                        String tag = curE.getTagName();

                        if (!tempAfter.empty() && ((String)tempAfter.peek()).toUpperCase().equals("/" + tag.toUpperCase())) {
                            tempAfter.pop();
                        }
                    }
                }
            }

            if (tempAfter.empty()) {
                return false;
            } else {
                while (!tempBefore.empty()) {
                    String temp = "/" + tempBefore.pop();

                    if (tempAfter.search(temp) > 0) {
                        return true;
                    } else {
                        continue;
                    }
                }
            }
        }

        tempBefore.clear();
        tempAfter.clear();

        return inFlag;
    }

    public static boolean betweenIntegratedTag1(int index, Object[] elements) {
        boolean inFlag = false;
LOOP1: 
        for (int i = 0; i < index; i++) {
            PivotBaseElement preE = (PivotBaseElement)elements[i];

            if (preE.getFlag() && integratedTag(preE.getTagName(), true)) {
                String tag = preE.getTagName();

                //boolean containEndBefore = false;
                int sameTagNumberBefore = 0;
LOOP2: 
                for (int j = i + 1; j < index; j++) {
                    PivotBaseElement postE = (PivotBaseElement)elements[j];

                    if (postE.getFlag() && (integratedTag(postE.getTagName(), false) || integratedTag(postE.getTagName(), true))) {
                        String tag2 = postE.getTagName();

                        if (tag2.toUpperCase().equals(tag.toUpperCase())) {
                            sameTagNumberBefore++;
                        }

                        if (tag2.toUpperCase().equals("/" + tag.toUpperCase())) {
                            if (sameTagNumberBefore != 0) {
                                sameTagNumberBefore--;
                            } else {
                                i = j + 1;

                                continue LOOP1;
                            }
                        }
                    }
                }

                if (sameTagNumberBefore != 0) {
                    continue;
                }

                int sameTagNumberAfter = 0;
LOOP3: 
                for (int j = index + 1; j < elements.length; j++) {
                    PivotBaseElement postE = (PivotBaseElement)elements[j];

                    if (postE.getFlag() && (integratedTag(postE.getTagName(), false) || integratedTag(postE.getTagName(), true))) {
                        String tag2 = postE.getTagName();

                        if (tag2.toUpperCase().equals(tag.toUpperCase())) {
                            sameTagNumberAfter++;
                        }

                        if (tag2.toUpperCase().equals("/" + tag.toUpperCase())) {
                            if (sameTagNumberAfter == 0) {
                                return true;
                            } else {
                                sameTagNumberAfter--;
                            }
                        }
                    }
                }
            }
        }

        return inFlag;
    }

    public static boolean integratedTag(String tag, boolean startTag) {
        if (startTag) {
            for (int i = 0; i < SpecTags1.length; i++) {
                if (tag.toUpperCase().equals(SpecTags1[i])) {
                    return true;
                }
            }
        } else {
            if (tag.length() == 0) {
                return false;
            }

            if (tag.charAt(0) != '/') {
                return false;
            }

            tag = tag.substring(1);

            for (int i = 0; i < SpecTags1.length; i++) {
                if (tag.toUpperCase().equals(SpecTags1[i])) {
                    return true;
                }
            }
        }

        return false;
    }
}

/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

import java.util.Vector;

import org.jvnet.olt.editor.translation.Backend;
import org.jvnet.olt.editor.translation.MainFrame;
import org.jvnet.olt.editor.util.BaseElements;
import org.jvnet.olt.editor.util.SgmlCharacter;


public class SimpleSentence {
    String value = "";

    //    PivotBaseElement[] baseElements;
    public SimpleSentence(String s) {
        value = convertSpecialCharacters(s);

        //        baseElements = BaseElements.extractContent(value);
    }

    public SimpleSentence(String s, String matchSrc, String matchT) {
        value = formatTranslation(s, matchSrc, matchT);

        //        baseElements = BaseElements.extractContent(value);
    }

    private String formatTranslation(String s, String matchSrc, String matchT) {
        PivotText newSrc = null;
        PivotText oldSrc = null;
        PivotText oldTarget = null;

        try {
            //TODO THink of a better way to do this
            boolean tagUpdate = Backend.instance().getConfig().isBFlagTagUpdate();

            if (tagUpdate) {
                if (s.equals(matchSrc)) {
                    return matchT;
                } else { //newsrc diff from oldsrc
                    newSrc = new PivotText(s, true);
                    oldSrc = new PivotText(matchSrc, true);
                    oldTarget = new PivotText(matchT, true);

                    if (newSrc.ct.length != 0) { //newSrc has tags.

                        if (oldSrc.ct.length != 0) { //oldsrc has tags

                            if (oldTarget.ct.length != 0) { //oldtarget has tags

                                Vector vTemp = compareTags(newSrc.ct, oldSrc.ct);
                                Vector vTempLocal = compareTags((ContentTag[])vTemp.toArray(new ContentTag[0]), oldTarget.ct);

                                return constructReturnString((ContentTag[])vTempLocal.toArray(new ContentTag[0]), oldTarget.deleteTag());
                            } else { //oldtarget has no tags

                                Vector vTemp = compareTags(newSrc.ct, oldSrc.ct);

                                return constructReturnString((ContentTag[])vTemp.toArray(new ContentTag[0]), matchT);
                            }
                        } else { //oldsrc has no tags

                            if (oldTarget.ct.length != 0) { //oldtarget has tags

                                Vector vTemp = compareTags(newSrc.ct, oldTarget.ct);

                                return constructReturnString((ContentTag[])vTemp.toArray(new ContentTag[0]), matchT);
                            } else { //oldtarget has no tags

                                return constructReturnString(newSrc.ct, matchT);
                            }
                        }
                    } else { //newSrc has no tags

                        if (oldSrc.ct.length != 0) { //oldsrc has tags

                            if (oldTarget.ct.length != 0) { //oldtarget has tags

                                return oldTarget.deleteTag();
                            } else { //oldtarget has no tags

                                return matchT;
                            }
                        } else { //oldsrc has no tags

                            if (oldTarget.ct.length != 0) { //oldtarget has tags

                                return oldTarget.deleteTag();
                            } else { //oldtarget has no tags

                                return matchT;
                            }
                        }
                    }
                }
            } else {
                return matchT;
            }
        } catch (Exception ec) {
            ec.printStackTrace();
        } finally {
            newSrc = null;
            oldSrc = null;
            oldTarget = null;
        }

        return null;
    }

    public String constructReturnString(ContentTag[] oInput, String strMatchT) {
        String strRet = "";

        if (oInput.length >= 2) {
            if ((oInput[0]).iPos == 0) {
                if ((oInput[0]).bFlag) {
                    strRet += (oInput[0].strText + strMatchT);
                } else {
                    strRet += (oInput[0].strText + strMatchT + "</" + oInput[0].strTagName + ">");
                }
            } else {
                strRet += strMatchT;

                if (oInput[0].bFlag) {
                    strRet += oInput[0].strText;
                } else {
                    strRet += (oInput[0].strText + "</" + oInput[0].strTagName + ">");
                }
            }

            for (int i = 1; i < oInput.length; i++) {
                if (oInput[i].bFlag) {
                    strRet += oInput[i].strText;
                } else {
                    strRet += (oInput[i].strText + "</" + oInput[i].strTagName + ">");
                }
            }
        } else if (oInput.length == 1) {
            if (oInput[0].iPos == 0) {
                if (oInput[0].bFlag) {
                    strRet += (oInput[0].strText + strMatchT);
                } else {
                    strRet += (oInput[0].strText + strMatchT + "</" + oInput[0].strTagName + ">");
                }
            } else {
                strRet += strMatchT;

                if (oInput[0].bFlag) {
                    strRet += oInput[0].strText;
                } else {
                    strRet += (oInput[0].strText + "</" + (oInput[0]).strTagName + ">");
                }
            }
        }

        return strRet;
    }

    public Vector compareTags(ContentTag[] src, ContentTag[] target) {
        Vector v = new Vector();
        Vector vSrc = new Vector();
        Vector vTarget = new Vector();

        for (int i = 0; i < src.length; i++) {
            vSrc.add(src[i]);
        }

        for (int i = 0; i < target.length; i++) {
            vTarget.add(target[i]);
        }

        for (int i = 0; i < vSrc.size(); i++) {
            for (int j = 0; j < vTarget.size(); j++) {
                int iResult = ((ContentTag)vSrc.elementAt(i)).compare((ContentTag)vTarget.elementAt(j));

                if (iResult == 0) { //complete same, we will use target;
                    v.addElement(vTarget.elementAt(j));
                    vSrc.set(i, null);
                    vTarget.set(j, null);

                    break;
                } else if (iResult == 1) { //tag name same, attribute diff

                    int iTemp = j;

                    for (int k = j + 1; k < vTarget.size(); k++) {
                        int iResultTemp = ((ContentTag)vSrc.elementAt(i)).compare((ContentTag)vTarget.elementAt(k));

                        if (iResultTemp != 0) {
                            continue;
                        } else {
                            iTemp = k;

                            break;
                        }
                    }

                    if (iTemp > j) {
                        v.addElement((ContentTag)vTarget.elementAt(iTemp));
                        vSrc.set(i, null);
                        vTarget.set(iTemp, null);

                        break;
                    } else {
                        v.addElement((ContentTag)vSrc.elementAt(i));
                        vSrc.set(i, null);

                        break;
                    }
                } else { //quit different

                    continue;
                }
            }
        }

        if (vSrc.size() != 0) {
            for (int i = 0; i < vSrc.size(); i++) {
                if (vSrc.elementAt(i) != null) {
                    v.addElement(vSrc.elementAt(i));
                    vSrc.set(i, null);
                }
            }
        }

        return v;
    }

    public String convertSpecialCharacters(String sentence) {
        StringBuffer sbuf = new StringBuffer(sentence);
        int i = 0;

        while (i < sbuf.length()) {
            char ch = sbuf.charAt(i);

            if (SgmlCharacter.isEntityUnicode(ch)) {
                String entity = SgmlCharacter.getEntityName(ch);
                sbuf.replace(i, i + 1, entity);
                i += entity.length();
            }
            //            else if(SgmlCharacter.isLowerASCII(ch)) {
            //                sbuf.setCharAt(i, (char)(ch - '\ue000'));
            //                i ++;
            //            }
            //            else if(SgmlCharacter.isLessThan(ch)) {
            //                sbuf.setCharAt(i, (char)(ch  - '\u3d00'));
            //                i ++;
            //            }
            //            else if(SgmlCharacter.isGreaterThan(ch)) {
            //                sbuf.setCharAt(i, (char)(ch  - '\u3d00'));
            //                i ++;
            //            }
            else {
                i++;
            }
        }

        return sbuf.toString();
    }

    public PivotBaseElement[] getBaseElementsObject() {
        //        return baseElements;  //  Try the lazy instantiation thing.
        return BaseElements.extractContent(value);
    }

    //    public void setBaseElementsObject(PivotBaseElement[] oInput) {
    //        baseElements = oInput;
    //    }
    public String getValue() {
        return value;
    }

    public void setValue(String s) {
        value = s;

        //        baseElements = BaseElements.extractContent(value);
    }

    public void setValue(String s, Vector v) {
        value = s;

        //        baseElements = BaseElements.extractContent(value);
    }
}

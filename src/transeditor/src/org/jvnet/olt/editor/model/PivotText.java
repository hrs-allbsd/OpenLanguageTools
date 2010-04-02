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
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

import java.io.StringReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;


public class PivotText {
    String allText = "";
    PivotBaseElement[] elements = null;
    ContentTag[] ct = null;

    public PivotText() {
        this("");
    }

    public PivotText(String str) {
        allText = new String(str);
        elements = org.jvnet.olt.editor.util.BaseElements.extractContent(allText);
    }

    public PivotText(String str, boolean bFlag) {
        allText = new String(str);
        elements = org.jvnet.olt.editor.util.BaseElements.extractContent(allText);

        StringReader sr = new StringReader(str);

        org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser parser = new org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser(sr);

        try {
            parser.parse();

            UpdateTagParser utp = new UpdateTagParser(str);
            parser.walkParseTree(utp, null);
            ct = utp.getTags();

            /*for(int i=0;i<ct.length;i++)
            {
              System.out.println("TagName = "+((ContentTag)ct[i]).strTagName);
              System.out.println("iIndex = "+((ContentTag)ct[i]).iIndex);
              System.out.println("iPos = "+((ContentTag)ct[i]).iPos);
              System.out.println("strAttribute = "+((ContentTag)ct[i]).strAttribute);
            }*/
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    public PivotText(String str, PivotBaseElement[] v) {
        allText = str;
        elements = v;
    }

    public String getText() {
        return allText;
    }

    public PivotBaseElement[] elements() {
        return elements;
    }

    public void set(String t) {
        allText = t;
        elements = org.jvnet.olt.editor.util.BaseElements.extractContent(allText);
    }

    public boolean isAllText() {
        for (int i = 0; i < elements.length; i++) {
            PivotBaseElement e = (PivotBaseElement)elements[i];

            if (e.getFlag()) {
                return false;
            }
        }

        return true;
    }

    public String getTagString() {
        if (isAllText()) {
            return "";
        }

        StringBuffer temp = new StringBuffer("");

        for (int i = 0; i < elements.length; i++) {
            PivotBaseElement e = (PivotBaseElement)elements[i];

            if (e.getFlag()) {
                temp.append(e.getContent());
            } else if (PivotTag.betweenIntegratedTag(i, elements)) {
                temp.append(e.getContent());
            }
        }

        return temp.toString();
    }

    public String deleteTag() {
        if (isAllText()) {
            return allText;
        }

        StringBuffer temp = new StringBuffer("");

        for (int i = 0; i < elements.length; i++) {
            PivotBaseElement e = (PivotBaseElement)elements[i];

            if (!e.getFlag()) {
                temp.append(e.getContent());
            } else {
                if ((e.getContent().startsWith("&") == true) && (e.getContent().endsWith(";"))) {
                    temp.append(e.getContent());
                }
            }
        }

        return temp.toString();
    }

    //bug 4764435
    public String getStringIfContainTagA(PivotText p) {
        StringBuffer sb = new StringBuffer();

        int numOfTagA = 0;
        int indexOfTagA = -1;

        for (int i = 0; i < elements.length; i++) {
            PivotBaseElement e = (PivotBaseElement)elements()[i];

            if (!e.getFlag()) {
                sb.append(e.getContent());
            } else {
                String targetTag = e.getTagName();

                if (targetTag.toLowerCase().equals("a")) {
                    numOfTagA++;
                    indexOfTagA = getIndexOfTagA(p, indexOfTagA + 1);

                    if (indexOfTagA != -1) {
                        PivotBaseElement srce = (PivotBaseElement)p.elements()[indexOfTagA];
                        sb.append(srce.getContent());
                    }
                } else {
                    sb.append(e.getContent());
                }
            }
        }

        return sb.toString();
    }

    public int getIndexOfTagA(PivotText p, int index) {
        for (int i = index; i < p.elements.length; i++) {
            PivotBaseElement e = (PivotBaseElement)p.elements[i];

            if (e.getFlag()) {
                String tag = e.getTagName();

                if (tag.toLowerCase().equals("a")) {
                    return i;
                }
            }
        }

        return -1;
    }

    public String getMatchStringForSource(PivotText p) {
        //  This method produces a copy of the target string where tags which
        //  do not appear in the source are removed, and where tags that are
        //  missing from the target, i.e., in the source but not in the target,
        //  are appended to the end of the string.
        //  Step 1: get a version of the target PivotBaseElements which does not
        //  contain Compound elements
        List targetElems = unrollCompoundBaseElements(elements);

        //  Step 2: get a map of the tags in the source.
        Map srcTags = buildTagMap(extractTags(p.elements()));

        //  Step 3: Iterate through the target PivotBaseElements, building the
        //  output string
        StringBuffer buffer = new StringBuffer();
        Iterator tgtIter = targetElems.iterator();

        while (tgtIter.hasNext()) {
            PivotBaseElement elem = (PivotBaseElement)tgtIter.next();

            if (elem.isTag()) {
                if (srcTags.containsKey(elem)) {
                    //  Only append tags if they are in the source
                    buffer.append(elem.getContent());

                    //  Update the source tag map
                    Counter counter = (Counter)srcTags.get(elem);

                    if (counter.decrement() <= 0) {
                        srcTags.remove(elem);
                    }
                } //  end if containsKey
            } else {
                buffer.append(elem.getContent());
            }
        }

        //  Step 4: Append any remaining tags from the source.
        Set srcKeys = srcTags.keySet();
        Iterator srcIter = srcKeys.iterator();

        while (srcIter.hasNext()) {
            PivotBaseElement srcElem = (PivotBaseElement)srcIter.next();
            Counter srcCounter = (Counter)srcTags.get(srcElem);

            for (int j = 0; j < srcCounter.getCount(); j++) {
                buffer.append(srcElem.getContent());
            }
        }

        //  Step 5: Return the result.
        return buffer.toString();
    }

    //end bug 4764435
    //--------------------------------------------------------------------------------------------------------
    private boolean inTags(String tagName, PivotText p) {
        if (tagName == null) {
            return false;
        }

        for (int i = 0; i < p.elements().length; i++) {
            PivotBaseElement e = (PivotBaseElement)p.elements()[i];

            if (e.getFlag()) {
                if (e.getTagName().toLowerCase().equals(tagName.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean inTags(String tagName, Object[] v) {
        if (tagName == null) {
            return false;
        }

        for (int i = 0; i < v.length; i++) {
            PivotBaseElement e = (PivotBaseElement)v[i];

            if (e.getFlag()) {
                if (e.getTagName().toLowerCase().equals(tagName.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check whether the Vector contains a tag with the specified tagname
     * if true, return the index of the Vector,
     * else return -1.
     */
    private int inTags(String tagName, Vector v) { // the Vector contains tags elements

        if (tagName == null) {
            return -1;
        }

        for (int i = v.size() - 1; i >= 0; i--) {
            PivotBaseElement e = (PivotBaseElement)v.get(i);

            if (e.getFlag()) {
                if (e.getTagName().toLowerCase().equals(tagName.toLowerCase())) {
                    return i;
                }
            }
        }

        return -1;
    }

    private int getFirstTagPosition(PivotText p) {
        for (int i = 0; i < p.elements.length; i++) {
            PivotBaseElement e = (PivotBaseElement)p.elements[i];

            if (e.getFlag()) {
                if (e.getContent().startsWith("&")) {
                    continue;
                }

                return e.getPositionSite();
            }
        }

        return -1;
    }

    private int getTagIndexFrom(PivotText p, int index) {
        for (int i = index; i < p.elements.length; i++) {
            PivotBaseElement e = (PivotBaseElement)p.elements[i];

            if (e.getFlag()) {
                if (e.getContent().startsWith("&")) {
                    continue;
                }

                return i;
            }
        }

        return -1;
    }

    /**
     * if match return -1;
     * else {
     *   return 1
     * }
     */
    public int match(PivotText p) {
        PivotBaseElement[] sourceTags = extractTags(elements);
        PivotBaseElement[] targetTags = extractTags(p.elements());

        //  If arrays of tags are not the same length, there must be tag differences.
        if (sourceTags.length != targetTags.length) {
            return 1;
        }

        //  If there are no tags then the tags are the same :-)
        if (sourceTags.length == 0) {
            return -1;
        }

        //  Test that each tag matches up.
        for (int i = 0; i < sourceTags.length; i++) {
            String srcTag = sourceTags[i].getContent();
            String tgtTag = targetTags[i].getContent();

            if (!srcTag.equals(tgtTag)) {
                return 1;
            }
        }

        //  If we get here then all the tags are equal.
        return -1;
    }

    /**
     * if match return -1;
     * else {
     *   return 1
     * }
     */
    public int matchIgnoreOrder(PivotText p) {
        PivotBaseElement[] sourceTags = extractTags(elements);
        PivotBaseElement[] targetTags = extractTags(p.elements());

        //  If arrays of tags are not the same length, there must be tag differences.
        if (sourceTags.length != targetTags.length) {
            return 1;
        }

        //  If there are no tags then the tags are the same :-)
        if (sourceTags.length == 0) {
            return -1;
        }

        Map srcMap = buildTagMap(sourceTags);
        Map tgtMap = buildTagMap(targetTags);

        Set srcKeys = srcMap.keySet();
        Iterator iterator = srcKeys.iterator();

        while (iterator.hasNext()) {
            PivotBaseElement srcKey = (PivotBaseElement)iterator.next();

            if (!tgtMap.containsKey(srcKey)) { // Test if source tag is there

                return 1;
            } else {
                Counter srcCount = (Counter)srcMap.get(srcKey);
                Counter tgtCount = (Counter)tgtMap.get(srcKey);

                //  Test tag is there the same number of times
                if (srcCount.getCount() != tgtCount.getCount()) {
                    return 1;
                } else {
                    tgtMap.remove(srcKey); //  This key has been compared: get rid of it.
                }
            }
        }

        //  By now, all the source tags have been checked and exist in the target
        //  so we now check if the target has extra tags.
        if (!tgtMap.isEmpty()) {
            return 1; //  target string has extra tags
        } else {
            return -1;
        }
    }

    public boolean canReplace(int start, int end, boolean tagProtection) {
        int dot = end;
        int mark = start;

        int selectIdStart = getIndexInElements(mark);
        int selectIdEnd = getIndexInElements(dot);

        if ((selectIdStart == -1) || (selectIdEnd == -1)) {
            return false;
        }

        if (selectIdStart != selectIdEnd) {
            if (tagProtection) { //parent.tagProtection) {

                PivotBaseElement pbe = (PivotBaseElement)elements[selectIdStart];

                if (pbe.getFlag()) {
                    return false;
                } else {
                    if (selectIdEnd > (selectIdStart + 1)) {
                        return false;
                    } else {
                        PivotBaseElement pbe1 = (PivotBaseElement)elements[selectIdEnd];

                        if (dot == pbe1.getPositionSite()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return true;
            }
        } else {
            if (tagProtection) { //parent.tagProtection) {

                PivotBaseElement pbe = (PivotBaseElement)elements[selectIdStart];

                if (pbe.getFlag()) {
                    return false;
                } else { //not tag element

                    if ((selectIdStart != 0) && (selectIdStart != (elements.length - 1))) { //middle element

                        if (PivotTag.betweenIntegratedTag(selectIdStart, elements)) {
                            return false;
                        } else {
                            return true;
                        }
                    } else { // first element or last element

                        return true;
                    }
                }
            } else { //tag protection off

                return true;
            }
        }
    }

    String getVisibleStringTo(int indexOfElements) {
        StringBuffer temp = new StringBuffer(0);

        for (int i = 0; i <= indexOfElements; i++) {
            PivotBaseElement e = (PivotBaseElement)elements[i];

            if (!e.getFlag()) {
                String t = e.getContent();
                temp.append(t);
            } else {
                if (e.isVisible()) {
                    String t = e.getContent();
                    temp.append(t);
                }
            }
        }

        return temp.toString();
    }

    int getIndexInElements(int position) {
        int index = -1;
        String temp = allText.substring(0, position);

        for (int i = 0; i < elements.length; i++) {
            String tt = this.getVisibleStringTo(i);

            if (tt.equals(temp)) {
                if (i != (elements.length - 1)) {
                    PivotBaseElement e = (PivotBaseElement)elements[i + 1];

                    if (e.getFlag()) {
                        if (!(e.isVisible())) {
                            continue;
                        } else {
                            index = i + 1;

                            break;
                        }
                    } else {
                        index = i + 1;

                        break;
                    }
                } else {
                    index = i;

                    break;
                }
            } else if (tt.startsWith(temp)) {
                index = i;

                break;
            }
        }

        if (index == -1) {
            return -1;
        }

        if (index == elements.length) {
            index = index - 1;
        }

        if (index == (elements.length - 1)) {
            PivotBaseElement e = (PivotBaseElement)elements[index];

            while (e.getFlag()) {
                if (!(e.isVisible())) {
                    index = index - 1;

                    if (index != -1) {
                        e = (PivotBaseElement)elements[index];
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        temp = "";

        return index;
    }

    /** This method extracts the tag elements from an array of base elements.
     */
    protected PivotBaseElement[] extractTags(PivotBaseElement[] elems) {
        List tagList = new java.util.LinkedList();

        for (int i = 0; i < elems.length; i++) {
            if (elems[i].isTag()) { //  Add tags
                tagList.add(elems[i]);
            } else {
                if (elems[i].isCompoundBaseElement()) { //  test for compound elements

                    //  Get tags out of the element
                    List subElements = elems[i].getSubElements();
                    Iterator iter = subElements.iterator();

                    while (iter.hasNext()) {
                        PivotBaseElement elem = (PivotBaseElement)iter.next();

                        if (elem.isTag()) { //  Add tags in the compound element
                            tagList.add(elem);
                        }
                    } //  while
                } //  if
            } //  else
        } // for

        return (PivotBaseElement[])tagList.toArray(new PivotBaseElement[0]);
    }

    /** This method builds a map of the tags that occur in an array of
     * PivotBaseElements and ensures the count of the tags included is correct.
     */
    protected Map buildTagMap(PivotBaseElement[] elems) {
        Map map = new HashMap();

        for (int i = 0; i < elems.length; i++) {
            if (map.containsKey(elems[i])) {
                Counter count = (Counter)map.get(elems[i]);
                count.increment();
            } else {
                Counter counter = new Counter(1);
                map.put(elems[i], counter);
            }
        }

        return map;
    }

    /**
     */
    protected List unrollCompoundBaseElements(PivotBaseElement[] elems) {
        List unrolledList = new java.util.LinkedList();

        for (int i = 0; i < elems.length; i++) {
            if (elems[i].isCompoundBaseElement()) {
                List subElements = elems[i].getSubElements();
                unrolledList.addAll(subElements);
            } else {
                unrolledList.add(elems[i]);
            }
        }

        return unrolledList;
    }
}

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

/*
 * HtmlSegmenterTable.java
 *
 * Created on July 1, 2002, 5:23 PM
 */

package org.jvnet.olt.filters.html;
import org.jvnet.olt.alignment.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author  timf
 */
public class HtmlSegmenterTable implements org.jvnet.olt.parsers.tagged.SegmenterTable {
    private HashSet dontSegmentSet;
    private HashSet dontSegmentOrCountSet;
    private HashSet nonTranslatablePcdataSet;
    private HashMap hasTranslatableAttrMap;
    private HashMap segmentLevelMap;
    private HashMap entityMap;
    private ArrayList attrList;
    
    
    /** Creates a new instance of HtmlSegmenterTable */
    public HtmlSegmenterTable() {
        dontSegmentSet = new HashSet();
        dontSegmentOrCountSet = new HashSet();
        
        hasTranslatableAttrMap = new HashMap();
        segmentLevelMap = new HashMap();
        nonTranslatablePcdataSet = new HashSet();
        
        dontSegmentOrCountSet.add("script");
        dontSegmentOrCountSet.add("code");
        dontSegmentOrCountSet.add("object");
        dontSegmentOrCountSet.add("param");
        
        dontSegmentSet.add("caption");
        
        
        attrList = new ArrayList();
        attrList.add("alt");
        hasTranslatableAttrMap.put("img",attrList);
        
        attrList = new ArrayList();
        attrList.add("value");
        hasTranslatableAttrMap.put("input",attrList);
        
        segmentLevelMap.put("html", new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("head", new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("title", new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("body", new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("html", new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("table", new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("thead",new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("tfoot",new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("tbody",new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("h1", new Integer(Segment.HARDSECTION));
        segmentLevelMap.put("div", new Integer(Segment.HARDSECTION));

        
        segmentLevelMap.put("dl", new Integer(Segment.HARDSUBSECTION));
        segmentLevelMap.put("ol", new Integer(Segment.HARDSUBSECTION));
        segmentLevelMap.put("ul", new Integer(Segment.HARDSUBSECTION));
        segmentLevelMap.put("dir", new Integer(Segment.HARDSUBSECTION));
        segmentLevelMap.put("menu", new Integer(Segment.HARDSUBSECTION));
        segmentLevelMap.put("tr",new Integer(Segment.HARDSUBSECTION));
        segmentLevelMap.put("h2",new Integer(Segment.HARDSUBSECTION));
        segmentLevelMap.put("h3",new Integer(Segment.HARDSUBSECTION));
        
        segmentLevelMap.put("colgroup",new Integer(Segment.HARD));
        segmentLevelMap.put("col",new Integer(Segment.HARD));
        segmentLevelMap.put("td",new Integer(Segment.HARD));
        segmentLevelMap.put("frameset",new Integer(Segment.HARD));
        segmentLevelMap.put("frame",new Integer(Segment.HARD));
        segmentLevelMap.put("li",new Integer(Segment.HARD));
        segmentLevelMap.put("h4",new Integer(Segment.HARD));
        segmentLevelMap.put("h5",new Integer(Segment.HARD));
        segmentLevelMap.put("h6",new Integer(Segment.HARD));
        
        
        
        segmentLevelMap.put("dt", new Integer(Segment.SOFT));
        segmentLevelMap.put("dd", new Integer(Segment.SOFT));
        segmentLevelMap.put("p", new Integer(Segment.SOFT));
        
        segmentLevelMap.put("br", new Integer(Segment.SOFTFLOW));
        segmentLevelMap.put("hr", new Integer(Segment.SOFTFLOW));
        segmentLevelMap.put("center", new Integer(Segment.SOFTFLOW));
        
        entityMap = new HashMap();
        
        //24.2 Character entity references for ISO 8859-1 characters
        
        // not putting in nbsp since in the editor, this
        // will appear as ' ' making users think there's a
        // problem with segmentation. (were it to appear in
        // it's own segment, which happens quite often)
        //entityMap.put("nbsp",new Character('\u00a0'));
        entityMap.put("iexcl",new Character('\u00a1'));
        entityMap.put("cent",new Character('\u00a2'));
        entityMap.put("pound",new Character('\u00a3'));
        entityMap.put("curren",new Character('\u00a4'));
        entityMap.put("yen",new Character('\u00a5'));
        entityMap.put("brvbar",new Character('\u00a6'));
        entityMap.put("sect",new Character('\u00a7'));
        entityMap.put("uml",new Character('\u00a8'));
        entityMap.put("copy",new Character('\u00a9'));
        entityMap.put("ordf",new Character('\u00aa'));
        entityMap.put("laquo",new Character('\u00ab'));
        entityMap.put("not",new Character('\u00ac'));
        entityMap.put("shy",new Character('\u00ad'));
        entityMap.put("reg",new Character('\u00ae'));
        entityMap.put("macr",new Character('\u00af'));
        entityMap.put("deg",new Character('\u00b0'));
        entityMap.put("plusmn",new Character('\u00b1'));
        entityMap.put("sup2",new Character('\u00b2'));
        entityMap.put("sup3",new Character('\u00b3'));
        entityMap.put("acute",new Character('\u00b4'));
        entityMap.put("micro",new Character('\u00b5'));
        entityMap.put("para",new Character('\u00b6'));
        entityMap.put("middot",new Character('\u00b7'));
        entityMap.put("cedil",new Character('\u00b8'));
        entityMap.put("sup1",new Character('\u00b9'));
        entityMap.put("ordm",new Character('\u00ba'));
        entityMap.put("raquo",new Character('\u00bb'));
        entityMap.put("frac14",new Character('\u00bc'));
        entityMap.put("frac12",new Character('\u00bd'));
        entityMap.put("frac34",new Character('\u00be'));
        entityMap.put("iquest",new Character('\u00bf'));
        entityMap.put("Agrave",new Character('\u00c0'));
        entityMap.put("Aacute",new Character('\u00c1'));
        entityMap.put("Acirc",new Character('\u00c2'));
        entityMap.put("Atilde",new Character('\u00c3'));
        entityMap.put("Auml",new Character('\u00c4'));
        entityMap.put("Aring",new Character('\u00c5'));
        entityMap.put("AElig",new Character('\u00c6'));
        entityMap.put("Ccedil",new Character('\u00c7'));
        entityMap.put("Egrave",new Character('\u00c8'));
        entityMap.put("Eacute",new Character('\u00c9'));
        entityMap.put("Ecirc",new Character('\u00ca'));
        entityMap.put("Euml",new Character('\u00cb'));
        entityMap.put("Igrave",new Character('\u00cc'));
        entityMap.put("Iacute",new Character('\u00cd'));
        entityMap.put("Icirc",new Character('\u00ce'));
        entityMap.put("Iuml",new Character('\u00cf'));
        entityMap.put("ETH",new Character('\u00d0'));
        entityMap.put("Ntilde",new Character('\u00d1'));
        entityMap.put("Ograve",new Character('\u00d2'));
        entityMap.put("Oacute",new Character('\u00d3'));
        entityMap.put("Ocirc",new Character('\u00d4'));
        entityMap.put("Otilde",new Character('\u00d5'));
        entityMap.put("Ouml",new Character('\u00d6'));
        entityMap.put("times",new Character('\u00d7'));
        entityMap.put("Oslash",new Character('\u00d8'));
        entityMap.put("Ugrave",new Character('\u00d9'));
        entityMap.put("Uacute",new Character('\u00da'));
        entityMap.put("Ucirc",new Character('\u00db'));
        entityMap.put("Uuml",new Character('\u00dc'));
        entityMap.put("Yacute",new Character('\u00dd'));
        entityMap.put("THORN",new Character('\u00de'));
        entityMap.put("szlig",new Character('\u00df'));
        entityMap.put("agrave",new Character('\u00e0'));
        entityMap.put("aacute",new Character('\u00e1'));
        entityMap.put("acirc",new Character('\u00e2'));
        entityMap.put("atilde",new Character('\u00e3'));
        entityMap.put("auml",new Character('\u00e4'));
        entityMap.put("aring",new Character('\u00e5'));
        entityMap.put("aelig",new Character('\u00e6'));
        entityMap.put("ccedil",new Character('\u00e7'));
        entityMap.put("egrave",new Character('\u00e8'));
        entityMap.put("eacute",new Character('\u00e9'));
        entityMap.put("ecirc",new Character('\u00ea'));
        entityMap.put("euml",new Character('\u00eb'));
        entityMap.put("igrave",new Character('\u00ec'));
        entityMap.put("iacute",new Character('\u00ed'));
        entityMap.put("icirc",new Character('\u00ee'));
        entityMap.put("iuml",new Character('\u00ef'));
        entityMap.put("eth",new Character('\u00f0'));
        entityMap.put("ntilde",new Character('\u00f1'));
        entityMap.put("ograve",new Character('\u00f2'));
        entityMap.put("oacute",new Character('\u00f3'));
        entityMap.put("ocirc",new Character('\u00f4'));
        entityMap.put("otilde",new Character('\u00f5'));
        entityMap.put("ouml",new Character('\u00f6'));
        entityMap.put("divide",new Character('\u00f7'));
        entityMap.put("oslash",new Character('\u00f8'));
        entityMap.put("ugrave",new Character('\u00f9'));
        entityMap.put("uacute",new Character('\u00fa'));
        entityMap.put("ucirc",new Character('\u00fb'));
        entityMap.put("uuml",new Character('\u00fc'));
        entityMap.put("yacute",new Character('\u00fd'));
        entityMap.put("thorn",new Character('\u00fe'));
        entityMap.put("yuml",new Character('\u00ff'));
        
        //24.3 Character entity references for symbols, mathematical symbols, and Greek letters
        entityMap.put("fnof",new Character('\u0192'));
        entityMap.put("Alpha",new Character('\u0391'));
        entityMap.put("Beta",new Character('\u0392'));
        entityMap.put("Gamma",new Character('\u0393'));
        entityMap.put("Delta",new Character('\u0394'));
        entityMap.put("Epsilon",new Character('\u0395'));
        entityMap.put("Zeta",new Character('\u0396'));
        entityMap.put("Eta",new Character('\u0397'));
        entityMap.put("Theta",new Character('\u0398'));
        entityMap.put("Iota",new Character('\u0399'));
        entityMap.put("Kappa",new Character('\u039a'));
        entityMap.put("Lambda",new Character('\u039b'));
        entityMap.put("Mu",new Character('\u039c'));
        entityMap.put("Nu",new Character('\u039d'));
        entityMap.put("Xi",new Character('\u039e'));
        entityMap.put("Omicron",new Character('\u039f'));
        entityMap.put("Pi",new Character('\u03a0'));
        entityMap.put("Rho",new Character('\u03a1'));
        entityMap.put("Sigma",new Character('\u03a3'));
        entityMap.put("Tau",new Character('\u03a4'));
        entityMap.put("Upsilon",new Character('\u03a5'));
        entityMap.put("Phi",new Character('\u03a6'));
        entityMap.put("Chi",new Character('\u03a7'));
        entityMap.put("Psi",new Character('\u03a8'));
        entityMap.put("Omega",new Character('\u03a9'));
        entityMap.put("alpha",new Character('\u03b1'));
        entityMap.put("beta",new Character('\u03b2'));
        entityMap.put("gamma",new Character('\u03b3'));
        entityMap.put("delta",new Character('\u03b4'));
        entityMap.put("epsilon",new Character('\u03b5'));
        entityMap.put("zeta",new Character('\u03b6'));
        entityMap.put("eta",new Character('\u03b7'));
        entityMap.put("theta",new Character('\u03b8'));
        entityMap.put("iota",new Character('\u03b9'));
        entityMap.put("kappa",new Character('\u03ba'));
        entityMap.put("lambda",new Character('\u03bb'));
        entityMap.put("mu",new Character('\u03bc'));
        entityMap.put("nu",new Character('\u03bd'));
        entityMap.put("xi",new Character('\u03be'));
        entityMap.put("omicron",new Character('\u03bf'));
        entityMap.put("pi",new Character('\u03c0'));
        entityMap.put("rho",new Character('\u03c1'));
        entityMap.put("sigmaf",new Character('\u03c2'));
        entityMap.put("sigma",new Character('\u03c3'));
        entityMap.put("tau",new Character('\u03c4'));
        entityMap.put("upsilon",new Character('\u03c5'));
        entityMap.put("phi",new Character('\u03c6'));
        entityMap.put("chi",new Character('\u03c7'));
        entityMap.put("psi",new Character('\u03c8'));
        entityMap.put("omega",new Character('\u03c9'));
        entityMap.put("thetasym",new Character('\u03d1'));
        entityMap.put("upsih",new Character('\u03d2'));
        entityMap.put("piv",new Character('\u03d6'));
        entityMap.put("bull",new Character('\u2022'));
        entityMap.put("hellip",new Character('\u2026'));
        entityMap.put("prime",new Character('\u2032'));
        entityMap.put("Prime",new Character('\u2033'));
        entityMap.put("oline",new Character('\u203e'));
        entityMap.put("frasl",new Character('\u2044'));
        entityMap.put("weierp",new Character('\u2118'));
        entityMap.put("image",new Character('\u2111'));
        entityMap.put("real",new Character('\u211c'));
        entityMap.put("trade",new Character('\u2122'));
        entityMap.put("alefsym",new Character('\u2135'));
        entityMap.put("larr",new Character('\u2190'));
        entityMap.put("uarr",new Character('\u2191'));
        entityMap.put("rarr",new Character('\u2192'));
        entityMap.put("darr",new Character('\u2193'));
        entityMap.put("harr",new Character('\u2194'));
        entityMap.put("crarr",new Character('\u21b5'));
        entityMap.put("lArr",new Character('\u21d0'));
        entityMap.put("uArr",new Character('\u21d1'));
        entityMap.put("rArr",new Character('\u21d2'));
        entityMap.put("dArr",new Character('\u21d3'));
        entityMap.put("hArr",new Character('\u21d4'));
        entityMap.put("forall",new Character('\u2200'));
        entityMap.put("part",new Character('\u2202'));
        entityMap.put("exist",new Character('\u2203'));
        entityMap.put("empty",new Character('\u2205'));
        entityMap.put("nabla",new Character('\u2207'));
        entityMap.put("isin",new Character('\u2208'));
        entityMap.put("notin",new Character('\u2209'));
        entityMap.put("ni",new Character('\u220b'));
        entityMap.put("prod",new Character('\u220f'));
        entityMap.put("sum",new Character('\u2211'));
        entityMap.put("minus",new Character('\u2212'));
        entityMap.put("lowast",new Character('\u2217'));
        entityMap.put("radic",new Character('\u221a'));
        entityMap.put("prop",new Character('\u221d'));
        entityMap.put("infin",new Character('\u221e'));
        entityMap.put("ang",new Character('\u2220'));
        entityMap.put("and",new Character('\u2227'));
        entityMap.put("or",new Character('\u2228'));
        entityMap.put("cap",new Character('\u2229'));
        entityMap.put("cup",new Character('\u222a'));
        entityMap.put("int",new Character('\u222b'));
        entityMap.put("there4",new Character('\u2234'));
        entityMap.put("sim",new Character('\u223c'));
        entityMap.put("cong",new Character('\u2245'));
        entityMap.put("asymp",new Character('\u2248'));
        entityMap.put("ne",new Character('\u2260'));
        entityMap.put("equiv",new Character('\u2261'));
        entityMap.put("le",new Character('\u2264'));
        entityMap.put("ge",new Character('\u2265'));
        entityMap.put("sub",new Character('\u2282'));
        entityMap.put("sup",new Character('\u2283'));
        entityMap.put("nsub",new Character('\u2284'));
        entityMap.put("sube",new Character('\u2286'));
        entityMap.put("supe",new Character('\u2287'));
        entityMap.put("oplus",new Character('\u2295'));
        entityMap.put("otimes",new Character('\u2297'));
        entityMap.put("perp",new Character('\u22a5'));
        entityMap.put("sdot",new Character('\u22c5'));
        entityMap.put("lceil",new Character('\u2308'));
        entityMap.put("rceil",new Character('\u2309'));
        entityMap.put("lfloor",new Character('\u230a'));
        entityMap.put("rfloor",new Character('\u230b'));
        entityMap.put("lang",new Character('\u2329'));
        entityMap.put("rang",new Character('\u232a'));
        entityMap.put("loz",new Character('\u25ca'));
        entityMap.put("spades",new Character('\u2660'));
        entityMap.put("clubs",new Character('\u2663'));
        entityMap.put("hearts",new Character('\u2665'));
        entityMap.put("diams",new Character('\u2666'));
        
        //24.4 Character entity references for markup-significant and internationalization characters
        entityMap.put("quot",new Character('\u0022'));
        /* purposely not doing these ones as it makes life
         * awkward for writing xml later on
         * entityMap.put("amp",new Character('\u0026'));
         * entityMap.put("lt",new Character('\u003c'));
         * entityMap.put("gt",new Character('\u003e'));
         */
        entityMap.put("OElig",new Character('\u0152'));
        entityMap.put("oelig",new Character('\u0153'));
        entityMap.put("Scaron",new Character('\u0160'));
        entityMap.put("scaron",new Character('\u0161'));
        entityMap.put("Yuml",new Character('\u0178'));
        entityMap.put("circ",new Character('\u02c6'));
        entityMap.put("tilde",new Character('\u02dc'));
        entityMap.put("ensp",new Character('\u2002'));
        entityMap.put("emsp",new Character('\u2003'));
        entityMap.put("thinsp",new Character('\u2009'));
        entityMap.put("zwnj",new Character('\u200c'));
        entityMap.put("zwj",new Character('\u200d'));
        entityMap.put("lrm",new Character('\u200e'));
        entityMap.put("rlm",new Character('\u200f'));
        entityMap.put("ndash",new Character('\u2013'));
        entityMap.put("mdash",new Character('\u2014'));
        entityMap.put("lsquo",new Character('\u2018'));
        entityMap.put("rsquo",new Character('\u2019'));
        entityMap.put("sbquo",new Character('\u201a'));
        entityMap.put("ldquo",new Character('\u201c'));
        entityMap.put("rdquo",new Character('\u201d'));
        entityMap.put("bdquo",new Character('\u201e'));
        entityMap.put("dagger",new Character('\u2020'));
        entityMap.put("Dagger",new Character('\u2021'));
        entityMap.put("permil",new Character('\u2030'));
        entityMap.put("lsaquo",new Character('\u2039'));
        entityMap.put("rsaquo",new Character('\u203a'));
        entityMap.put("euro",new Character('\u20ac'));
        
        nonTranslatablePcdataSet.add("style");
    }
    
    /**
     * @param tagname The tag name to be tested.
     * @return Whether the tag should be segmented internally
     */
    public boolean dontSegmentInsideTag(String tagname) {
        return dontSegmentSet.contains(tagname.toLowerCase());
    }
    
    /**
     * @param tag The tag name to be tested
     * @return Whether the tag contains any translatable attributes
     */
    public boolean containsTranslatableAttribute(String tag) {
        return hasTranslatableAttrMap.containsKey(tag.toLowerCase());
    }
    
    /**
     * @param tag The tag name to be tested
     * @return A List of attributes that need to be translated
     */
    public java.util.List getAttrList(String tag) {
        java.util.List names = (java.util.List)hasTranslatableAttrMap.get(tag.toLowerCase());
        if (names == null)
            names = new ArrayList();
        return names;
    }
    
    /**
     * @return The "hardness level" of this block tag.
     * Warning ! : Coming into this method, I *know* I'm at a block level tag
     * - so if I don't have the tag type specified, I return
     * a soft hard tag.
     */
    public int getBlockLevel(String tag) {
        Integer level = (Integer)segmentLevelMap.get(tag.toLowerCase());
        if (level == null)
            level = new Integer(Segment.SOFT);
        return level.intValue();
    }
    
    /**
     * @return a character that represents this entity value - null if this is not a character entity.
     */
    public Character getEntityCharValue(String entity) {
        Character character = (Character)entityMap.get(entity);
        return character;
    }
    
    /** This is for tags that contain translatable text in their attributes
     * @param tag The tag name you're querying
     * @return true if the tag contains translatble attributes.
     *
     *
     */
    public boolean containsTranslatableAttribute(String tag, String namespaceID) {
        return containsTranslatableAttribute(tag);
    }
    
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html.
     * @param tagname The tag you're querying.
     * @param namespaceID The namespace ID.
     *
     * @return true if the tag doesn't contain segmentable text.
     *
     */
    public boolean dontSegmentInsideTag(String tagname, String namespaceID) {
        return dontSegmentInsideTag(tagname);
    }
    
    /** This gets a List containing the Strings that are translatable attributes
     * for the tag passed as a parameter. For example, in html &lt;img&gt; has the
     * attribute <code>alt</code> that can contain translatable text.
     *
     * @param tag the tag you're querying
     * @param namespaceID The namespace ID
     * @return a List of Strings containing translatable attributes.
     *
     */
    public java.util.List getAttrList(String tag, String namespaceID) {
        return getAttrList(tag);
    }
    
    /** This returns the "hardness level" of a particular tag. This method is only
     * really useful to alignment programs that need some insight into the structural
     * flow of the document. For a description of what a "hardness level" is, see the
     * docs for the alignment program interface.
     *
     * @param tag the tag you're querying
     * @param namespaceID The namespace ID
     *
     * @return the "hardness level" of a particular tag.
     *
     */
    public int getBlockLevel(String tag, String namespaceID) {
        return getBlockLevel(tag);
    }
    
    public boolean containsTranslatableText(String tag) {
        if (nonTranslatablePcdataSet.contains(tag.toLowerCase())){
            return false;
        }
        else return true;
    }
    
    public boolean containsTranslatableText(String tag, String namespaceID) {
        return containsTranslatableText(tag);
    }
    
    public boolean dontSegmentOrCountInsideTag(String tagname) {
        return dontSegmentOrCountSet.contains(tagname.toLowerCase());
    }
    
    public boolean dontSegmentOrCountInsideTag(String tagname, String namespaceID) {
        return dontSegmentInsideTag(tagname);
    }
    
    public boolean includeCommentsInTranslatableSection(String tag) {
        if (tag.equals("script")){
            return true;
        } else {
            return false;
        }
    }

    public boolean pcdataTranslatableByDefault() {
        return true;
    }
    
    
}

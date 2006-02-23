
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

package org.jvnet.olt.filters.sgml.docbook;
import org.jvnet.olt.alignment.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 *
 * @author  timf
 */
public class DocbookSegmenterTable implements org.jvnet.olt.parsers.tagged.SegmenterTable {
    private HashSet dontSegmentSet;
    private HashMap dontSegmentOrCountMap;
    private HashSet dontTranslateSet;
    private HashMap hasTranslatableAttrMap;
    private HashMap segmentLevelMap;
    private HashMap entityMap;
    private ArrayList imgAttrList;
    
    
    /** Creates a new instance of HtmlSegmenterTable */
    public DocbookSegmenterTable() {
        dontSegmentSet = new HashSet();
        dontSegmentOrCountMap = new HashMap();
        dontTranslateSet = new HashSet();
        hasTranslatableAttrMap = new HashMap();
        segmentLevelMap = new HashMap();
        
        // these tags are inline, but don't allow segmentation inside,
        // eg. "This is <phrase>this uses 802.11b networking.</phrase>
        //     an example"
        // contains one segment :
        //     "This is <phrase>this uses 802.11b networking.</phrase>
        //     an example"
        // and has 5 words (and one number/abbreviation)
        
        dontSegmentSet.add("term");
        dontSegmentSet.add("title");
        dontSegmentSet.add("citetitle");
        dontSegmentSet.add("phrase");
        dontSegmentSet.add("parameter");
        dontSegmentSet.add("option");
        dontSegmentSet.add("literal");
        dontSegmentSet.add("subjectterm");
        dontSegmentSet.add("literallayout");
        dontSegmentSet.add("refentrytitle");
        dontSegmentSet.add("indexterm");
        dontSegmentSet.add("primary");
        dontSegmentSet.add("secondary");
        dontSegmentSet.add("ulink");

        dontSegmentSet.add("author");
        dontSegmentSet.add("firstname");
        dontSegmentSet.add("surname");
        dontSegmentSet.add("publisher");
        dontSegmentSet.add("subjectset");
        dontSegmentSet.add("holder");
        dontSegmentSet.add("year");
        dontSegmentSet.add("copyright");
        dontSegmentSet.add("synopsis");

        dontSegmentSet.add("address");
        dontSegmentSet.add("city");
        dontSegmentSet.add("street");
        dontSegmentSet.add("country");
        dontSegmentSet.add("postcode");
        dontSegmentSet.add("state");
        dontSegmentSet.add("publishername");
        
        dontSegmentSet.add("funcsynopsisinfo");
        
        /* 
         * Here is map of non-segmentable non countable tags that are inline.
         * 
         * If the tag name is included in the map then segment will not be segmented
         * and counted.
         *
         * Additionaly we can use attributes to make the propert decission
         *
         *  Following rules are used:
         *
         *  1) attributes has to be delimited by semicolon
         *  2) attribute start with + then attribute has to be included in attribute list
         *  3) attribute start with - then attribute has not be included in attribute list
         *
         *  Here are simple examples:
         *  "-type=custom-text" - we will dont segment tag if attribute 'type' with value 'custom-text' IS NOT INCLUDED in attribute list
         *  "+street"           - we will dont segment tag if attribute 'street' IS INCLUDED in the attribute list
         *  "+street;-image"    - we will dont segment tag if attribute 'street' IS INCLUDED in the attribute list
         *                          AND
         *                        attribute image IS NOT INCLUDED in the attribute list
         *
         *  The attribute parser is simple we don't care about colons and spaces in attributes list.
         */
        dontSegmentOrCountMap.put("programlisting","");
        dontSegmentOrCountMap.put("screen","");
        dontSegmentOrCountMap.put("filename","");
        dontSegmentOrCountMap.put("command","");
        dontSegmentOrCountMap.put("computeroutput","");
        dontSegmentOrCountMap.put("trademark","");
        dontSegmentOrCountMap.put("userinput","");
        dontSegmentOrCountMap.put("replaceable","");
        dontSegmentOrCountMap.put("olink","-type=custom-text");
        
        dontTranslateSet.add("comment");
        dontTranslateSet.add("remark");
        
        imgAttrList = new ArrayList();
        // translatable attributes in here
        //imgAttrList.add("alt");
        //hasTranslatableAttrMap.put("img",imgAttrList);
        
        //segmentLevelMap.put("html", new Integer(Segment.HARDSECTION));
        
        //segmentLevelMap.put("dl", new Integer(Segment.HARDSUBSECTION));
        
        //segmentLevelMap.put("colgroup",new Integer(Segment.HARD));
        
        //segmentLevelMap.put("dt", new Integer(Segment.SOFT));
        
        //segmentLevelMap.put("br", new Integer(Segment.SOFTFLOW));
        
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
        entityMap.put("rdquor",new Character('\"'));
        
        entityMap.put("bdquo",new Character('\u201e'));
        entityMap.put("dagger",new Character('\u2020'));
        entityMap.put("Dagger",new Character('\u2021'));
        entityMap.put("permil",new Character('\u2030'));
        entityMap.put("lsaquo",new Character('\u2039'));
        entityMap.put("rsaquo",new Character('\u203a'));
        entityMap.put("euro",new Character('\u20ac'));
        
    }
    
    /**
     * @param tag The tag name to be tested
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
     * @returns The "hardness level" of this block tag.
     * Warning ! : Coming into this method, I *know* I'm at a block level tag
     * - so if I don't have the tag type specified, I return
     * a soft hard tag.
     */
    public int getBlockLevel(String tag) {
        Integer level = (Integer)segmentLevelMap.get(tag.toLowerCase());
        if (level == null) // Sgml is much more regimented than html, so everything is hard
            level = new Integer(Segment.HARD);
        return level.intValue();
    }
    
    /**
     * @returns a character that represents this entity value - null if this is not a character entity
     */
    public Character getEntityCharValue(String entity) {
        Character character = (Character)entityMap.get(entity);
        return character;
    }
    
    /** This is for tags that contain translatable text in their attributes
     * @param tag The tag name you're querying
     * @return true if the tag contains translatble attributes.
     *
     */
    public boolean containsTranslatableAttribute(String tag, String namespaceID) {
        return containsTranslatableAttribute(tag);
    }
    
    /** These are tags that you should not segment inside - for example
     * &gt;script&lt; tags in html. This text is still translatable though.
     * @param tagname The tag you're querying.
     * @param namespaceID The namespace ID.
     *
     * @return true if the tag doesn't contain segmentable text.
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
     * @returns a List of Strings containing translatable attributes
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
     * @returns the "hardness level" of a particular tag
     */
    public int getBlockLevel(String tag, String namespaceID) {
        return getBlockLevel(tag);
    }
    
    public boolean containsTranslatableText(String tag) {
        // check to see if this is marked as translatable
        if (dontTranslateSet.contains(tag.toLowerCase())){
            return false;
        } else {return pcdataTranslatableByDefault();}
    }
    
    public boolean containsTranslatableText(String tag, String namespaceID) {
        return containsTranslatableText(tag);
    }
    
    /**
     * Make decision if the tag is non-segmentable and non-countable
     *
     * @param tagname name of the tag to check
     * @return true if the tag is non-segmentable and non-countable
     */
    public boolean dontSegmentOrCountInsideTag(String tagname) {
        return dontSegmentOrCountMap.containsKey(tagname.toLowerCase());
    }
    
    /**
     * Make decision if the tag is non-segmentable and non-countable
     *
     * @param tagname name of the tag to check
     * @param namespaceID to use
     *
     * @return true if the tag is non-segmentable and non-countable
     */
    public boolean dontSegmentOrCountInsideTag(String tagname, String namespaceID) {
        return dontSegmentOrCountInsideTag(tagname);
    }
    
    /**
     * Make decision if the tag is non-segmentable and non-countable
     *
     * @param tagname name of the tag to check
     * @param namespaceID to use
     * @param tagAttributesMap map that contains all tag attributes
     *
     * @return true if the tag is non-segmentable and non-countable
     */
    public boolean dontSegmentOrCountInsideTag(String tagname, String namespaceID, Map tagAttributesMap) {
        
        if(!dontSegmentOrCountMap.containsKey(tagname)) {
            return false;
        }
        
        /**
         * Additionaly we can use attributes to make the right decission
         *
         *  Following rules are used:
         *
         *  1) attributes has to be delimited by semicolon
         *  2) attribute start with + then attribute has to be included in attribute list
         *  3) attribute start with - then attribute has not be included in attribute list
         *
         *  Here are simple examples:
         *  "-type=custom-text" - we will dont segment tag if attribute 'type' with value 'custom-text' IS NOT INCLUDED in attribute list
         *  "+street"           - we will dont segment tag if attribute 'street' IS INCLUDED in the attribute list
         *  "+street;-image"    - we will dont segment tag if attribute 'street' IS INCLUDED in the attribute list
         *                          AND
         *                        attribute image IS NOT INCLUDED in the attribute list
         *
         *  The parser is simple we don't care about colons and spaces in attributes list.
         */
        
        boolean result = true;
        
        // tokenize attribute list
        StringTokenizer attribs = new StringTokenizer((String)dontSegmentOrCountMap.get(tagname),";");
        while(attribs.hasMoreTokens()) {
            
            // parse attribute parts
            String attToken = attribs.nextToken().trim();
            String attSign = "";
            String attName = attToken.trim();
            String attValue = "";
            
            if(attName.indexOf("=")>-1) {
                String[] attParts = attToken.split("=");
                attName = attParts[0].trim();
                attValue = attParts[1].trim();
            }
            
            // get out the sign
            attSign = attName.substring(0,1);
            attName = attName.substring(1);
            
            // convert all attributes to lower case
            Map lowerCaseAttributes = new HashMap();
            Iterator it = tagAttributesMap.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();
                String value = (String)tagAttributesMap.get(key);
                lowerCaseAttributes.put(key.toLowerCase(),value.toLowerCase());
            }
            
            // get the value of attribute from tag
            String tagAttValue = null;
            if(lowerCaseAttributes.get(attName)!=null) {
                tagAttValue = (String)lowerCaseAttributes.get(attName);
                // remove attributes colons
                if(tagAttValue.matches("^[\",\'].*?[\",\']$")) tagAttValue = tagAttValue.substring(1, tagAttValue.length() - 1);
            }
            System.out.println("!!! " + attSign + attName + " = " + attValue + " = " + tagAttValue);
            
            
            // do the decision
            if(attSign.equals("-")) {
                if(tagAttValue!=null && (tagAttValue.equals(attValue) || "".equals(attValue))) {
                    result = false;
                    break;
                }
            } else {
                if(tagAttValue==null || (!tagAttValue.equals(attValue) && !"".equals(attValue))) {
                    result = false;
                    break;
                }
            }
        }
        
        return result;
    }

    public boolean includeCommentsInTranslatableSection(String tag) {
        return false;
    }

    public boolean pcdataTranslatableByDefault() {
        return true;
    }
    
}

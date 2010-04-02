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

package org.jvnet.olt.xliff_back_converter.format.sgml;

import org.jvnet.olt.xliff_back_converter.UnicodeReverse;
import java.util.HashMap;
import java.util.Map;

public class SgmlUnicodeReverseImpl implements UnicodeReverse {
    HashMap entityMap;
    
    public SgmlUnicodeReverseImpl() {
        init();
    }
    
    public String reverse(char uc) {
        Character c = new Character(uc);
        return (String)entityMap.get(c);
    }
    
    public void init() {
        entityMap = new HashMap();
        
        //24.2 Character entity references for ISO 8859-1 characters
        
        // not putting in nbsp since in the editor, this
        // will appear as ' ' making users think there's a
        // problem with segmentation. (were it to appear in
        // it's own segment, which happens quite often)
        entityMap.put(new Character('\u00a0'),"nbsp");
        entityMap.put(new Character('\u00a1'),"iexcl");
        entityMap.put(new Character('\u00a2'),"cent");
        entityMap.put(new Character('\u00a3'),"pound");
        entityMap.put(new Character('\u00a4'),"curren");
        entityMap.put(new Character('\u00a5'),"yen");
        entityMap.put(new Character('\u00a6'),"brvbar");
        entityMap.put(new Character('\u00a7'),"sect");
        entityMap.put(new Character('\u00a8'),"uml");
        entityMap.put(new Character('\u00a9'),"copy");
        entityMap.put(new Character('\u00aa'),"ordf");
        entityMap.put(new Character('\u00ab'),"laquo");
        entityMap.put(new Character('\u00ac'),"not");
        entityMap.put(new Character('\u00ad'),"shy");
        entityMap.put(new Character('\u00ae'),"reg");
        entityMap.put(new Character('\u00af'),"macr");
        entityMap.put(new Character('\u00b0'),"deg");
        entityMap.put(new Character('\u00b1'),"plusmn");
        entityMap.put(new Character('\u00b2'),"sup2");
        entityMap.put(new Character('\u00b3'),"sup3");
        entityMap.put(new Character('\u00b4'),"acute");
        entityMap.put(new Character('\u00b5'),"micro");
        entityMap.put(new Character('\u00b6'),"para");
        entityMap.put(new Character('\u00b7'),"middot");
        entityMap.put(new Character('\u00b8'),"cedil");
        entityMap.put(new Character('\u00b9'),"sup1");
        entityMap.put(new Character('\u00ba'),"ordm");
        entityMap.put(new Character('\u00bb'),"raquo");
        entityMap.put(new Character('\u00bc'),"frac14");
        entityMap.put(new Character('\u00bd'),"frac12");
        entityMap.put(new Character('\u00be'),"frac34");
        entityMap.put(new Character('\u00bf'),"iquest");
        entityMap.put(new Character('\u00c0'),"Agrave");
        entityMap.put(new Character('\u00c1'),"Aacute");
        entityMap.put(new Character('\u00c2'),"Acirc");
        entityMap.put(new Character('\u00c3'),"Atilde");
        entityMap.put(new Character('\u00c4'),"Auml");
        entityMap.put(new Character('\u00c5'),"Aring");
        entityMap.put(new Character('\u00c6'),"AElig");
        entityMap.put(new Character('\u00c7'),"Ccedil");
        entityMap.put(new Character('\u00c8'),"Egrave");
        entityMap.put(new Character('\u00c9'),"Eacute");
        entityMap.put(new Character('\u00ca'),"Ecirc");
        entityMap.put(new Character('\u00cb'),"Euml");
        entityMap.put(new Character('\u00cc'),"Igrave");
        entityMap.put(new Character('\u00cd'),"Iacute");
        entityMap.put(new Character('\u00ce'),"Icirc");
        entityMap.put(new Character('\u00cf'),"Iuml");
        entityMap.put(new Character('\u00d0'),"ETH");
        entityMap.put(new Character('\u00d1'),"Ntilde");
        entityMap.put(new Character('\u00d2'),"Ograve");
        entityMap.put(new Character('\u00d3'),"Oacute");
        entityMap.put(new Character('\u00d4'),"Ocirc");
        entityMap.put(new Character('\u00d5'),"Otilde");
        entityMap.put(new Character('\u00d6'),"Ouml");
        entityMap.put(new Character('\u00d7'),"times");
        entityMap.put(new Character('\u00d8'),"Oslash");
        entityMap.put(new Character('\u00d9'),"Ugrave");
        entityMap.put(new Character('\u00da'),"Uacute");
        entityMap.put(new Character('\u00db'),"Ucirc");
        entityMap.put(new Character('\u00dc'),"Uuml");
        entityMap.put(new Character('\u00dd'),"Yacute");
        entityMap.put(new Character('\u00de'),"THORN");
        entityMap.put(new Character('\u00df'),"szlig");
        entityMap.put(new Character('\u00e0'),"agrave");
        entityMap.put(new Character('\u00e1'),"aacute");
        entityMap.put(new Character('\u00e2'),"acirc");
        entityMap.put(new Character('\u00e3'),"atilde");
        entityMap.put(new Character('\u00e4'),"auml");
        entityMap.put(new Character('\u00e5'),"aring");
        entityMap.put(new Character('\u00e6'),"aelig");
        entityMap.put(new Character('\u00e7'),"ccedil");
        entityMap.put(new Character('\u00e8'),"egrave");
        entityMap.put(new Character('\u00e9'),"eacute");
        entityMap.put(new Character('\u00ea'),"ecirc");
        entityMap.put(new Character('\u00eb'),"euml");
        entityMap.put(new Character('\u00ec'),"igrave");
        entityMap.put(new Character('\u00ed'),"iacute");
        entityMap.put(new Character('\u00ee'),"icirc");
        entityMap.put(new Character('\u00ef'),"iuml");
        entityMap.put(new Character('\u00f0'),"eth");
        entityMap.put(new Character('\u00f1'),"ntilde");
        entityMap.put(new Character('\u00f2'),"ograve");
        entityMap.put(new Character('\u00f3'),"oacute");
        entityMap.put(new Character('\u00f4'),"ocirc");
        entityMap.put(new Character('\u00f5'),"otilde");
        entityMap.put(new Character('\u00f6'),"ouml");
        entityMap.put(new Character('\u00f7'),"divide");
        entityMap.put(new Character('\u00f8'),"oslash");
        entityMap.put(new Character('\u00f9'),"ugrave");
        entityMap.put(new Character('\u00fa'),"uacute");
        entityMap.put(new Character('\u00fb'),"ucirc");
        entityMap.put(new Character('\u00fc'),"uuml");
        entityMap.put(new Character('\u00fd'),"yacute");
        entityMap.put(new Character('\u00fe'),"thorn");
        entityMap.put(new Character('\u00ff'),"yuml");
        
        //24.3 Character entity references for symbols, mathematical symbols, and Greek letters
        entityMap.put(new Character('\u0192'),"fnof");
        entityMap.put(new Character('\u0391'),"Alpha");
        entityMap.put(new Character('\u0392'),"Beta");
        entityMap.put(new Character('\u0393'),"Gamma");
        entityMap.put(new Character('\u0394'),"Delta");
        entityMap.put(new Character('\u0395'),"Epsilon");
        entityMap.put(new Character('\u0396'),"Zeta");
        entityMap.put(new Character('\u0397'),"Eta");
        entityMap.put(new Character('\u0398'),"Theta");
        entityMap.put(new Character('\u0399'),"Iota");
        entityMap.put(new Character('\u039a'),"Kappa");
        entityMap.put(new Character('\u039b'),"Lambda");
        entityMap.put(new Character('\u039c'),"Mu");
        entityMap.put(new Character('\u039d'),"Nu");
        entityMap.put(new Character('\u039e'),"Xi");
        entityMap.put(new Character('\u039f'),"Omicron");
        entityMap.put(new Character('\u03a0'),"Pi");
        entityMap.put(new Character('\u03a1'),"Rho");
        entityMap.put(new Character('\u03a3'),"Sigma");
        entityMap.put(new Character('\u03a4'),"Tau");
        entityMap.put(new Character('\u03a5'),"Upsilon");
        entityMap.put(new Character('\u03a6'),"Phi");
        entityMap.put(new Character('\u03a7'),"Chi");
        entityMap.put(new Character('\u03a8'),"Psi");
        entityMap.put(new Character('\u03a9'),"Omega");
        entityMap.put(new Character('\u03b1'),"alpha");
        entityMap.put(new Character('\u03b2'),"beta");
        entityMap.put(new Character('\u03b3'),"gamma");
        entityMap.put(new Character('\u03b4'),"delta");
        entityMap.put(new Character('\u03b5'),"epsilon");
        entityMap.put(new Character('\u03b6'),"zeta");
        entityMap.put(new Character('\u03b7'),"eta");
        entityMap.put(new Character('\u03b8'),"theta");
        entityMap.put(new Character('\u03b9'),"iota");
        entityMap.put(new Character('\u03ba'),"kappa");
        entityMap.put(new Character('\u03bb'),"lambda");
        entityMap.put(new Character('\u03bc'),"mu");
        entityMap.put(new Character('\u03bd'),"nu");
        entityMap.put(new Character('\u03be'),"xi");
        entityMap.put(new Character('\u03bf'),"omicron");
        entityMap.put(new Character('\u03c0'),"pi");
        entityMap.put(new Character('\u03c1'),"rho");
        entityMap.put(new Character('\u03c2'),"sigmaf");
        entityMap.put(new Character('\u03c3'),"sigma");
        entityMap.put(new Character('\u03c4'),"tau");
        entityMap.put(new Character('\u03c5'),"upsilon");
        entityMap.put(new Character('\u03c6'),"phi");
        entityMap.put(new Character('\u03c7'),"chi");
        entityMap.put(new Character('\u03c8'),"psi");
        entityMap.put(new Character('\u03c9'),"omega");
        entityMap.put(new Character('\u03d1'),"thetasym");
        entityMap.put(new Character('\u03d2'),"upsih");
        entityMap.put(new Character('\u03d6'),"piv");
        entityMap.put(new Character('\u2022'),"bull");
        entityMap.put(new Character('\u2026'),"hellip");
        entityMap.put(new Character('\u2032'),"prime");
        entityMap.put(new Character('\u2033'),"Prime");
        entityMap.put(new Character('\u203e'),"oline");
        entityMap.put(new Character('\u2044'),"frasl");
        entityMap.put(new Character('\u2118'),"weierp");
        entityMap.put(new Character('\u2111'),"image");
        entityMap.put(new Character('\u211c'),"real");
        entityMap.put(new Character('\u2122'),"trade");
        entityMap.put(new Character('\u2135'),"alefsym");
        entityMap.put(new Character('\u2190'),"larr");
        entityMap.put(new Character('\u2191'),"uarr");
        entityMap.put(new Character('\u2192'),"rarr");
        entityMap.put(new Character('\u2193'),"darr");
        entityMap.put(new Character('\u2194'),"harr");
        entityMap.put(new Character('\u21b5'),"crarr");
        entityMap.put(new Character('\u21d0'),"lArr");
        entityMap.put(new Character('\u21d1'),"uArr");
        entityMap.put(new Character('\u21d2'),"rArr");
        entityMap.put(new Character('\u21d3'),"dArr");
        entityMap.put(new Character('\u21d4'),"hArr");
        entityMap.put(new Character('\u2200'),"forall");
        entityMap.put(new Character('\u2202'),"part");
        entityMap.put(new Character('\u2203'),"exist");
        entityMap.put(new Character('\u2205'),"empty");
        entityMap.put(new Character('\u2207'),"nabla");
        entityMap.put(new Character('\u2208'),"isin");
        entityMap.put(new Character('\u2209'),"notin");
        entityMap.put(new Character('\u220b'),"ni");
        entityMap.put(new Character('\u220f'),"prod");
        entityMap.put(new Character('\u2211'),"sum");
        entityMap.put(new Character('\u2212'),"minus");
        entityMap.put(new Character('\u2217'),"lowast");
        entityMap.put(new Character('\u221a'),"radic");
        entityMap.put(new Character('\u221d'),"prop");
        entityMap.put(new Character('\u221e'),"infin");
        entityMap.put(new Character('\u2220'),"ang");
        entityMap.put(new Character('\u2227'),"and");
        entityMap.put(new Character('\u2228'),"or");
        entityMap.put(new Character('\u2229'),"cap");
        entityMap.put(new Character('\u222a'),"cup");
        entityMap.put(new Character('\u222b'),"int");
        entityMap.put(new Character('\u2234'),"there4");
        entityMap.put(new Character('\u223c'),"sim");
        entityMap.put(new Character('\u2245'),"cong");
        entityMap.put(new Character('\u2248'),"asymp");
        entityMap.put(new Character('\u2260'),"ne");
        entityMap.put(new Character('\u2261'),"equiv");
        entityMap.put(new Character('\u2264'),"le");
        entityMap.put(new Character('\u2265'),"ge");
        entityMap.put(new Character('\u2282'),"sub");
        entityMap.put(new Character('\u2283'),"sup");
        entityMap.put(new Character('\u2284'),"nsub");
        entityMap.put(new Character('\u2286'),"sube");
        entityMap.put(new Character('\u2287'),"supe");
        entityMap.put(new Character('\u2295'),"oplus");
        entityMap.put(new Character('\u2297'),"otimes");
        entityMap.put(new Character('\u22a5'),"perp");
        entityMap.put(new Character('\u22c5'),"sdot");
        entityMap.put(new Character('\u2308'),"lceil");
        entityMap.put(new Character('\u2309'),"rceil");
        entityMap.put(new Character('\u230a'),"lfloor");
        entityMap.put(new Character('\u230b'),"rfloor");
        entityMap.put(new Character('\u2329'),"lang");
        entityMap.put(new Character('\u232a'),"rang");
        entityMap.put(new Character('\u25ca'),"loz");
        entityMap.put(new Character('\u2660'),"spades");
        entityMap.put(new Character('\u2663'),"clubs");
        entityMap.put(new Character('\u2665'),"hearts");
        entityMap.put(new Character('\u2666'),"diams");
        
        //24.4 Character entity references for markup-significant and internationalization characters
        // not doing this one, since it's used by entity declarations/doctype headers
        //entityMap.put(new Character('\u0022'),"quot");
            /* purposely not doing these ones as it makes life
             * awkward for writing xml later on
             * entityMap.put(new Character('\u0026'),"amp");
             * entityMap.put(new Character('\u003c'),"lt");
             * entityMap.put(new Character('\u003e'),"gt");
             */
        entityMap.put(new Character('\u0152'),"OElig");
        entityMap.put(new Character('\u0153'),"oelig");
        entityMap.put(new Character('\u0160'),"Scaron");
        entityMap.put(new Character('\u0161'),"scaron");
        entityMap.put(new Character('\u0178'),"Yuml");
        entityMap.put(new Character('\u02c6'),"circ");
        entityMap.put(new Character('\u02dc'),"tilde");
        entityMap.put(new Character('\u2002'),"ensp");
        entityMap.put(new Character('\u2003'),"emsp");
        entityMap.put(new Character('\u2009'),"thinsp");
        entityMap.put(new Character('\u200c'),"zwnj");
        entityMap.put(new Character('\u200d'),"zwj");
        entityMap.put(new Character('\u200e'),"lrm");
        entityMap.put(new Character('\u200f'),"rlm");
        entityMap.put(new Character('\u2013'),"ndash");
        entityMap.put(new Character('\u2014'),"mdash");
        entityMap.put(new Character('\u2018'),"lsquo");
        entityMap.put(new Character('\u2019'),"rsquo");
        entityMap.put(new Character('\u201a'),"sbquo");
        //entityMap.put(new Character('\u201c'),"ldquo");
        //entityMap.put(new Character('\u201d'),"rdquo");
        entityMap.put(new Character('\u201e'),"bdquo");
        entityMap.put(new Character('\u2020'),"dagger");
        entityMap.put(new Character('\u2021'),"Dagger");
        entityMap.put(new Character('\u2030'),"permil");
        entityMap.put(new Character('\u2039'),"lsaquo");
        entityMap.put(new Character('\u203a'),"rsaquo");
        entityMap.put(new Character('\u20ac'),"euro");
        
/*                //entities from sun-iso.gml
            //???entityMap.put(new Character('\u2248'),"ap");  //<!ENTITY ap     SDATA "[ap    ]" ><!-- /approx R: =approximate -->
            entityMap.put(new Character('\u002a'),"ast"); //<!ENTITY ast    SDATA "[ast   ]" ><!-- /ast B: =asterisk -->
            entityMap.put(new Character('\u2502'),"boxv");  //<!ENTITY boxv   SDATA "[boxv  ]" ><!-- vertical line -->
            entityMap.put(new Character('\u005c\u005c'),"bsol");  //<!ENTITY bsol   SDATA "[bsol  ]" ><!-- /backslash =reverse solidus -->
            entityMap.put(new Character('\u2713'),"check");  //<!ENTITY check  SDATA "[check ]" ><!-- /checkmark =tick, check mark -->
            entityMap.put(new Character('\u0040'),"commat");  //<!ENTITY commat SDATA "[commat]" ><!-- commercial at -->
            entityMap.put(new Character('\u2717'),"cross");  //<!ENTITY cross  SDATA "[ballot]" ><!-- ballot cross -->
            entityMap.put(new Character('\u2010'),"dash");  //<!ENTITY dash   SDATA "[dash  ]" ><!-- hyphen (true graphic) -->
            entityMap.put(new Character('\u2155'),"frac15");  //<!ENTITY frac15 SDATA "[frac15]" ><!-- fraction one-fifth -->
            entityMap.put(new Character('\u2153'),"frac13");  //<!ENTITY frac13 SDATA "[frac13]" ><!-- fraction one-third -->
            entityMap.put(new Character('\u2277'),"gl");  //<!ENTITY gl     SDATA "[gl    ]" ><!-- /gtrless R: greater, less -->
            entityMap.put(new Character('\u0060'),"grave");  //<!ENTITY grave  SDATA "[grave ]" ><!-- grave accent -->
            //???entityMap.put(new Character('\u2010'),"hyphen"); // <!ENTITY hyphen SDATA "[hyphen]" ><!-- hyphen -->
            entityMap.put(new Character('\u21da'),"lAarr");  //<!ENTITY lAarr  SDATA "[lAarr ]" ><!-- /Lleftarrow A: left triple arrow  -->
            entityMap.put(new Character('\u201e'),"ldquor");  //<!ENTITY ldquor SDATA "[ldquor]" ><!-- rising dbl quote, left (low) -->
            entityMap.put(new Character('\u2276'),"lg");  //<!ENTITY lg     SDATA "[lg    ]" ><!-- /lessgtr R: less, greater -->
            //???entityMap.put(new Character('\u03bb'),"lgr");  //<!ENTITY lgr    SDATA "[lgr   ]" ><!-- small lambda, Greek -->
            entityMap.put(new Character('\u005f'),"lowbar"); //<!ENTITY lowbar SDATA "[lowbar]" ><!-- low line -->???
            entityMap.put(new Character('\u2213'),"mnplus");  //<!ENTITY mnplus SDATA "[mnplus]" ><!-- /mp B: =minus-or-plus sign -->
            //???entityMap.put(new Character('\u201d'),"rdquor");  //<!ENTITY rdquor SDATA "[rdquor]" ><!-- rising dbl quote, right (high) -->
            entityMap.put(new Character('\u2218'),"ring");  //<!ENTITY ring   SDATA "[ring  ]" ><!-- ring -->
 
        //entities from iso-amsa.ent
        entityMap.put(new Character('\u21b6'),"cularr"); //<!ENTITY cularr	"&#x21B6;"> <!-- ANTICLOCKWISE TOP SEMICIRCLE ARROW -->
        entityMap.put(new Character('\u21b7'),"curarr"); //<!ENTITY curarr	"&#x21B7;"> <!-- CLOCKWISE TOP SEMICIRCLE ARROW -->
        entityMap.put(new Character('\u21d3'),"dArr"); //<!ENTITY dArr	"&#x21D3;"> <!-- DOWNWARDS DOUBLE ARROW -->
        entityMap.put(new Character('\u21ca'),"darr2"); //<!ENTITY darr2	"&#x21CA;"> <!-- DOWNWARDS PAIRED ARROWS -->
        entityMap.put(new Character('\u21c3'),"dharl"); //<!ENTITY dharl	"&#x21C3;"> <!-- DOWNWARDS HARPOON WITH BARB LEFTWARDS -->
        entityMap.put(new Character('\u21c2'),"dharr"); //<!ENTITY dharr	"&#x21C2;"> <!-- DOWNWARDS HARPOON WITH BARB RIGHTWARDS -->
        entityMap.put(new Character('\u21da'),"lAarr"); //<!ENTITY lAarr	"&#x21DA;"> <!-- LEFTWARDS TRIPLE ARROW -->
        entityMap.put(new Character('\u219e'),"Larr"); //<!ENTITY Larr	"&#x219E;"> <!-- LEFTWARDS TWO HEADED ARROW -->
        entityMap.put(new Character('\u21c7'),"larr2"); //<!ENTITY larr2	"&#x21C7;"> <!-- LEFTWARDS PAIRED ARROWS -->
        entityMap.put(new Character('\u21a9'),"larrhk"); //<!ENTITY larrhk	"&#x21A9;"> <!-- LEFTWARDS ARROW WITH HOOK -->
        entityMap.put(new Character('\u21ab'),"larrlp"); //<!ENTITY larrlp	"&#x21AB;"> <!-- LEFTWARDS ARROW WITH LOOP -->
        entityMap.put(new Character('\u21a2'),"larrtl"); //<!ENTITY larrtl	"&#x21A2;"> <!-- LEFTWARDS ARROW WITH TAIL -->
        entityMap.put(new Character('\u21bd'),"lhard"); //<!ENTITY lhard	"&#x21BD;"> <!-- LEFTWARDS HARPOON WITH BARB DOWNWARDS -->
        entityMap.put(new Character('\u21bc'),"lharu"); //<!ENTITY lharu	"&#x21BC;"> <!-- LEFTWARDS HARPOON WITH BARB UPWARDS -->
        entityMap.put(new Character('\u21c6'),"lrarr2"); //<!ENTITY lrarr2	"&#x21C6;"> <!-- LEFTWARDS ARROW OVER RIGHTWARDS ARROW -->
        entityMap.put(new Character('\u21c4'),"rlarr2"); //<!ENTITY rlarr2	"&#x21C4;"> <!-- RIGHTWARDS ARROW OVER LEFTWARDS ARROW -->
        entityMap.put(new Character('\u21ad'),"harrw"); //<!ENTITY harrw	"&#x21AD;"> <!-- LEFT RIGHT WAVE ARROW -->
        entityMap.put(new Character('\u21cc'),"rlhar2"); //<!ENTITY rlhar2	"&#x21CC;"> <!-- RIGHTWARDS HARPOON OVER LEFTWARDS HARPOON -->
        entityMap.put(new Character('\u21cb'),"lrhar2"); //<!ENTITY lrhar2	"&#x21CB;"> <!-- LEFTWARDS HARPOON OVER RIGHTWARDS HARPOON -->
        entityMap.put(new Character('\u21b0'),"lsh"); //<!ENTITY lsh	"&#x21B0;"> <!-- UPWARDS ARROW WITH TIP LEFTWARDS -->
        entityMap.put(new Character('\u21a6'),"map"); //<!ENTITY map	"&#x21A6;"> <!-- RIGHTWARDS ARROW FROM BAR -->
        entityMap.put(new Character('\u22b8'),"mumap"); //<!ENTITY mumap	"&#x22B8;"> <!-- MULTIMAP -->
        entityMap.put(new Character('\u2197'),"nearr"); //<!ENTITY nearr	"&#x2197;"> <!-- NORTH EAST ARROW -->
        entityMap.put(new Character('\u21cd'),"nlArr"); //<!ENTITY nlArr	"&#x21CD;"> <!-- LEFTWARDS DOUBLE ARROW WITH STROKE -->
        entityMap.put(new Character('\u219a'),"nlarr"); //<!ENTITY nlarr	"&#x219A;"> <!-- LEFTWARDS ARROW WITH STROKE -->
        entityMap.put(new Character('\u21ce'),"nhArr"); //<!ENTITY nhArr	"&#x21CE;"> <!-- LEFT RIGHT DOUBLE ARROW WITH STROKE -->
        entityMap.put(new Character('\u21ae'),"nharr"); //<!ENTITY nharr	"&#x21AE;"> <!-- LEFT RIGHT ARROW WITH STROKE -->
        entityMap.put(new Character('\u219b'),"nrarr"); //<!ENTITY nrarr	"&#x219B;"> <!-- RIGHTWARDS ARROW WITH STROKE -->
        entityMap.put(new Character('\u21ce'),"nrArr"); //<!ENTITY nrArr	"&#x21CF;"> <!-- RIGHTWARDS DOUBLE ARROW WITH STROKE -->
        entityMap.put(new Character('\u2196'),"nwarr"); //<!ENTITY nwarr	"&#x2196;"> <!-- NORTH WEST ARROW -->
        entityMap.put(new Character('\u21ba'),"olarr"); //<!ENTITY olarr	"&#x21BA;"> <!-- ANTICLOCKWISE OPEN CIRCLE ARROW -->
        entityMap.put(new Character('\u21bb'),"orarr"); //<!ENTITY orarr	"&#x21BB;"> <!-- CLOCKWISE OPEN CIRCLE ARROW -->
        entityMap.put(new Character('\u21db'),"rAarr"); //<!ENTITY rAarr	"&#x21DB;"> <!-- RIGHTWARDS TRIPLE ARROW -->
        entityMap.put(new Character('\u21a0'),"Rarr"); //<!ENTITY Rarr	"&#x21A0;"> <!-- RIGHTWARDS TWO HEADED ARROW -->
        entityMap.put(new Character('\u21c9'),"rarr2"); //<!ENTITY rarr2	"&#x21C9;"> <!-- RIGHTWARDS PAIRED ARROWS -->
        entityMap.put(new Character('\u21aa'),"rarrhk"); //<!ENTITY rarrhk	"&#x21AA;"> <!-- RIGHTWARDS ARROW WITH HOOK -->
        entityMap.put(new Character('\u21ac'),"rarrlp"); //<!ENTITY rarrlp	"&#x21AC;"> <!-- RIGHTWARDS ARROW WITH LOOP -->
        entityMap.put(new Character('\u21a3'),"rarrtl"); //<!ENTITY rarrtl	"&#x21A3;"> <!-- RIGHTWARDS ARROW WITH TAIL -->
        entityMap.put(new Character('\u219d'),"rarrw"); //<!ENTITY rarrw	"&#x219D;"> <!-- RIGHTWARDS SQUIGGLE ARROW -->
        entityMap.put(new Character('\u21c1'),"rhard"); //<!ENTITY rhard	"&#x21C1;"> <!-- RIGHTWARDS HARPOON WITH BARB DOWNWARDS -->
        entityMap.put(new Character('\u21c0'),"rharu"); //<!ENTITY rharu	"&#x21C0;"> <!-- RIGHTWARDS HARPOON WITH BARB UPWARDS -->
        entityMap.put(new Character('\u21b1'),"rsh"); //<!ENTITY rsh	"&#x21B1;"> <!-- UPWARDS ARROW WITH TIP RIGHTWARDS -->
        entityMap.put(new Character('\u2198'),"drarr"); //<!ENTITY drarr	"&#x2198;"> <!-- SOUTH EAST ARROW -->
        entityMap.put(new Character('\u2199'),"dlarr"); //<!ENTITY dlarr	"&#x2199;"> <!-- SOUTH WEST ARROW -->
        entityMap.put(new Character('\u21d1'),"uArr"); //<!ENTITY uArr	"&#x21D1;"> <!-- UPWARDS DOUBLE ARROW -->
        entityMap.put(new Character('\u21c8'),"uarr2"); //<!ENTITY uarr2	"&#x21C8;"> <!-- UPWARDS PAIRED ARROWS -->
        entityMap.put(new Character('\u21d5'),"vArr"); //<!ENTITY vArr	"&#x21D5;"> <!-- UP DOWN DOUBLE ARROW -->
        entityMap.put(new Character('\u2195'),"varr"); //<!ENTITY varr	"&#x2195;"> <!-- UP DOWN ARROW -->
        entityMap.put(new Character('\u21bf'),"uharl"); //<!ENTITY uharl	"&#x21BF;"> <!-- UPWARDS HARPOON WITH BARB LEFTWARDS -->
        entityMap.put(new Character('\u21be'),"uharr"); //<!ENTITY uharr	"&#x21BE;"> <!-- UPWARDS HARPOON WITH BARB RIGHTWARDS -->
        //???entityMap.put(new Character('\u21d0'),"xlArr"); //<!ENTITY xlArr	"&#x21D0;"> <!-- LEFTWARDS DOUBLE ARROW -->
        //???entityMap.put(new Character('\u2194'),"xhArr"); //<!ENTITY xhArr	"&#x2194;"> <!-- LEFT RIGHT ARROW -->
        //???entityMap.put(new Character('\u2194'),"xharr"); //<!ENTITY xharr	"&#x2194;"> <!-- LEFT RIGHT ARROW -->
        entityMap.put(new Character('\u21d2'),"xrArr"); //<!ENTITY xrArr	"&#x21D2;"> <!-- RIGHTWARDS DOUBLE ARROW -->
 
        //entities from iso-amsb.ent
        entityMap.put(new Character('\u2210'),"amalg"); //<!ENTITY amalg	"&#x2210;"> <!-- N-ARY COPRODUCT -->
        entityMap.put(new Character('\u2306'),"Barwed"); //<!ENTITY Barwed	"&#x2306;"> <!-- PERSPECTIVE -->
        entityMap.put(new Character('\u22bc'),"barwed"); //<!ENTITY barwed	"&#x22BC;"> <!-- NAND -->
        entityMap.put(new Character('\u22d2'),"Cap"); //<!ENTITY Cap	"&#x22D2;"> <!-- DOUBLE INTERSECTION -->
        entityMap.put(new Character('\u22d3'),"Cup"); //<!ENTITY Cup	"&#x22D3;"> <!-- DOUBLE UNION -->
        entityMap.put(new Character('\u22ce'),"cuvee"); //<!ENTITY cuvee	"&#x22CE;"> <!-- CURLY LOGICAL OR -->
        entityMap.put(new Character('\u22cf'),"cuwed"); //<!ENTITY cuwed	"&#x22CF;"> <!-- CURLY LOGICAL AND -->
        entityMap.put(new Character('\u22c4'),"diam"); //<!ENTITY diam	"&#x22C4;"> <!-- DIAMOND OPERATOR -->
        entityMap.put(new Character('\u22c7'),"divonx"); //<!ENTITY divonx	"&#x22C7;"> <!-- DIVISION TIMES -->
        entityMap.put(new Character('\u22ba'),"intcal"); //<!ENTITY intcal	"&#x22BA;"> <!-- INTERCALATE -->
        entityMap.put(new Character('\u22cb'),"lthree"); //<!ENTITY lthree	"&#x22CB;"> <!-- LEFT SEMIDIRECT PRODUCT -->
        entityMap.put(new Character('\u22c9'),"ltimes"); //<!ENTITY ltimes	"&#x22C9;"> <!-- LEFT NORMAL FACTOR SEMIDIRECT PRODUCT -->
        entityMap.put(new Character('\u229f'),"minusb"); //<!ENTITY minusb	"&#x229F;"> <!-- SQUARED MINUS -->
        entityMap.put(new Character('\u229b'),"oast"); //<!ENTITY oast	"&#x229B;"> <!-- CIRCLED ASTERISK OPERATOR -->
        entityMap.put(new Character('\u229a'),"ocir"); //<!ENTITY ocir	"&#x229A;"> <!-- CIRCLED RING OPERATOR -->
        entityMap.put(new Character('\u229d'),"odash"); //<!ENTITY odash	"&#x229D;"> <!-- CIRCLED DASH -->
        entityMap.put(new Character('\u2299'),"odot"); //<!ENTITY odot	"&#x2299;"> <!-- CIRCLED DOT OPERATOR -->
        entityMap.put(new Character('\u2296'),"ominus"); //<!ENTITY ominus	"&#x2296;"> <!-- CIRCLED MINUS -->
        entityMap.put(new Character('\u2298'),"osol"); //<!ENTITY osol	"&#x2298;"> <!-- CIRCLED DIVISION SLASH -->
        entityMap.put(new Character('\u229e'),"plusb"); //<!ENTITY plusb	"&#x229E;"> <!-- SQUARED PLUS -->
        entityMap.put(new Character('\u2214'),"plusdo"); //<!ENTITY plusdo	"&#x2214;"> <!-- DOT PLUS -->
        entityMap.put(new Character('\u22cc'),"rthree"); //<!ENTITY rthree	"&#x22CC;"> <!-- RIGHT SEMIDIRECT PRODUCT -->
        entityMap.put(new Character('\u22ca'),"rtimes"); //<!ENTITY rtimes	"&#x22CA;"> <!-- RIGHT NORMAL FACTOR SEMIDIRECT PRODUCT -->
        entityMap.put(new Character('\u22a1'),"sdotb"); //<!ENTITY sdotb	"&#x22A1;"> <!-- SQUARED DOT OPERATOR -->
        entityMap.put(new Character('\u2216'),"setmn"); //<!ENTITY setmn	"&#x2216;"> <!-- SET MINUS -->
        entityMap.put(new Character('\u2293'),"sqcap"); //<!ENTITY sqcap	"&#x2293;"> <!-- SQUARE CAP -->
        entityMap.put(new Character('\u2294'),"sqcup"); //<!ENTITY sqcup	"&#x2294;"> <!-- SQUARE CUP -->
        //???entityMap.put(new Character('\u2216'),"ssetmn"); //<!ENTITY ssetmn	"&#x2216;"> <!-- SET MINUS -->
        entityMap.put(new Character('\u22c6'),"sstarf"); //<!ENTITY sstarf	"&#x22C6;"> <!-- STAR OPERATOR -->
        entityMap.put(new Character('\u22a0'),"timesb"); //<!ENTITY timesb	"&#x22A0;"> <!-- SQUARED TIMES -->
        entityMap.put(new Character('\u22a4'),"top"); //<!ENTITY top	"&#x22A4;"> <!-- DOWN TACK -->
        entityMap.put(new Character('\u228e'),"uplus"); //<!ENTITY uplus	"&#x228E;"> <!-- MULTISET UNION -->
        entityMap.put(new Character('\u2240'),"wreath"); //<!ENTITY wreath	"&#x2240;"> <!-- WREATH PRODUCT -->
        entityMap.put(new Character('\u25cb'),"xcirc"); //<!ENTITY xcirc	"&#x25CB;"> <!-- WHITE CIRCLE -->
        entityMap.put(new Character('\u25bd'),"xdtri"); //<!ENTITY xdtri	"&#x25BD;"> <!-- WHITE DOWN-POINTING TRIANGLE -->
        entityMap.put(new Character('\u25b3'),"xutri"); //<!ENTITY xutri	"&#x25B3;"> <!-- WHITE UP-POINTING TRIANGLE -->
        //???entityMap.put(new Character('\u2210'),"coprod"); //<!ENTITY coprod	"&#x2210;"> <!-- N-ARY COPRODUCT -->
 
        //entities from iso-amsc.ent
        entityMap.put(new Character('\uE291'),"rpargt"); //<!ENTITY rpargt	"&#xE291;"> <!--  -->
        entityMap.put(new Character('\u231d'),"urcorn"); //<!ENTITY urcorn	"&#x231D;"> <!-- TOP RIGHT CORNER -->
        entityMap.put(new Character('\u231f'),"drcorn"); //<!ENTITY drcorn	"&#x231F;"> <!-- BOTTOM RIGHT CORNER -->
        //XXXentityMap.put(new Character(' '),"lpargt"); //<!--     lpargt	Unknown unicode character -->
        entityMap.put(new Character('\u231c'),"ulcorn"); //<!ENTITY ulcorn	"&#x231C;"> <!-- TOP LEFT CORNER -->
        entityMap.put(new Character('\u231e'),"dlcorn"); //<!ENTITY dlcorn	"&#x231E;"> <!-- BOTTOM LEFT CORNER -->
 
        //entities from iso-amsn.ent
        entityMap.put(new Character('\ue411'),"gnap"); //<!ENTITY gnap	"&#xE411;"> <!--  -->
        entityMap.put(new Character('\u2269'),"gne"); //<!ENTITY gne	"&#x2269;"> <!--  -->
        //???entityMap.put(new Character('\u2269'),"gnE"); //<!ENTITY gnE	"&#x2269;"> <!--  -->
        entityMap.put(new Character('\u22E7'),"gnsim"); //<!ENTITY gnsim	"&#x22E7;"> <!-- GREATER-THAN BUT NOT EQUIVALENT TO -->
        //???entityMap.put(new Character('\u2269'),"gvnE"); //<!ENTITY gvnE	"&#x2269;"> <!-- GREATER-THAN BUT NOT EQUAL TO -->
        entityMap.put(new Character('\ue2a2'),"lnap"); //<!ENTITY lnap	"&#xE2A2;"> <!--  -->
        entityMap.put(new Character('\u2268'),"lnE"); //<!ENTITY lnE	"&#x2268;"> <!--  -->
        //???entityMap.put(new Character('\u2268'),"lne"); //<!ENTITY lne	"&#x2268;"> <!--  -->
        entityMap.put(new Character('\u22e6'),"lnsim"); //<!ENTITY lnsim	"&#x22E6;"> <!--  -->
        //???entityMap.put(new Character('\u2268'),"lvnE"); //<!ENTITY lvnE	"&#x2268;"> <!-- LESS-THAN BUT NOT EQUAL TO -->
        entityMap.put(new Character('\u2249'),"nap"); //<!ENTITY nap	"&#x2249;"> <!-- NOT ALMOST EQUAL TO -->
        entityMap.put(new Character('\u2247'),"ncong"); //<!ENTITY ncong	"&#x2247;"> <!-- NEITHER APPROXIMATELY NOR ACTUALLY EQUAL TO -->
        entityMap.put(new Character('\u2262'),"nequiv"); //<!ENTITY nequiv	"&#x2262;"> <!-- NOT IDENTICAL TO -->
        entityMap.put(new Character('\u2271'),"ngE"); //<!ENTITY ngE	"&#x2271;"> <!--  -->
        //???entityMap.put(new Character('\u2271'),"nge"); //<!ENTITY nge	"&#x2271;"> <!-- NEITHER GREATER-THAN NOR EQUAL TO -->
        //???entityMap.put(new Character('\u2271'),"nges"); //<!ENTITY nges	"&#x2271;"> <!--  -->
        entityMap.put(new Character('\u226f'),"ngt"); //<!ENTITY ngt	"&#x226F;"> <!-- NOT GREATER-THAN -->
        entityMap.put(new Character('\u2270'),"nle"); //<!ENTITY nle	"&#x2270;"> <!-- NEITHER LESS-THAN NOR EQUAL TO -->
        //???entityMap.put(new Character('\u2270'),"nlE"); //<!ENTITY nlE	"&#x2270;"> <!--  -->
        //???entityMap.put(new Character('\u2270'),"nles"); //<!ENTITY nles	"&#x2270;"> <!--  -->
        entityMap.put(new Character('\u226e'),"nlt"); //<!ENTITY nlt	"&#x226E;"> <!-- NOT LESS-THAN -->
        entityMap.put(new Character('\u22ea'),"nltri"); //<!ENTITY nltri	"&#x22EA;"> <!-- NOT NORMAL SUBGROUP OF -->
        entityMap.put(new Character('\u22ec'),"nltrie"); //<!ENTITY nltrie	"&#x22EC;"> <!-- NOT NORMAL SUBGROUP OF OR EQUAL TO -->
        entityMap.put(new Character('\u2224'),"nmid"); //<!ENTITY nmid	"&#x2224;"> <!-- DOES NOT DIVIDE -->
        entityMap.put(new Character('\u2226'),"npar"); //<!ENTITY npar	"&#x2226;"> <!-- NOT PARALLEL TO -->
        entityMap.put(new Character('\u2280'),"npr"); //<!ENTITY npr	"&#x2280;"> <!-- DOES NOT PRECEDE -->
        entityMap.put(new Character('\u22e0'),"npre"); //<!ENTITY npre	"&#x22E0;"> <!-- DOES NOT PRECEDE OR EQUAL -->
        entityMap.put(new Character('\u22eb'),"nrtri"); //<!ENTITY nrtri	"&#x22EB;"> <!-- DOES NOT CONTAIN AS NORMAL SUBGROUP -->
        entityMap.put(new Character('\u22ed'),"nrtrie"); //<!ENTITY nrtrie	"&#x22ED;"> <!-- DOES NOT CONTAIN AS NORMAL SUBGROUP OR EQUAL -->
        entityMap.put(new Character('\u2281'),"nsc"); //<!ENTITY nsc	"&#x2281;"> <!-- DOES NOT SUCCEED -->
        entityMap.put(new Character('\u22e1'),"nsce"); //<!ENTITY nsce	"&#x22E1;"> <!-- DOES NOT SUCCEED OR EQUAL -->
        entityMap.put(new Character('\u2241'),"nsim"); //<!ENTITY nsim	"&#x2241;"> <!--  -->
        entityMap.put(new Character('\u2244'),"nsime"); //<!ENTITY nsime	"&#x2244;"> <!--  -->
        entityMap.put(new Character('\ue2aa'),"nsmid"); //<!ENTITY nsmid	"&#xE2AA;"> <!--  -->
        //???entityMap.put(new Character('\u2226'),"nspar"); //<!ENTITY nspar	"&#x2226;"> <!-- NOT PARALLEL TO -->
        entityMap.put(new Character('\u2288'),"nsube"); //<!ENTITY nsube	"&#x2288;"> <!--  -->
        //???entityMap.put(new Character('\u2288'),"nsubE"); //<!ENTITY nsubE	"&#x2288;"> <!--  -->
        entityMap.put(new Character('\u2285'),"nsup"); //<!ENTITY nsup	"&#x2285;"> <!-- NOT A SUPERSET OF -->
        entityMap.put(new Character('\u2289'),"nsupE"); //<!ENTITY nsupE	"&#x2289;"> <!--  -->
        //???entityMap.put(new Character('\u2289'),"nsupe"); //<!ENTITY nsupe	"&#x2289;"> <!--  -->
        entityMap.put(new Character('\u22ac'),"nvdash"); //<!ENTITY nvdash	"&#x22AC;"> <!-- DOES NOT PROVE -->
        entityMap.put(new Character('\u22ad'),"nvDash"); //<!ENTITY nvDash	"&#x22AD;"> <!-- NOT TRUE -->
        entityMap.put(new Character('\u22af'),"nVDash"); //<!ENTITY nVDash	"&#x22AF;"> <!-- NEGATED DOUBLE VERTICAL BAR DOUBLE RIGHT TURNSTILE -->
        entityMap.put(new Character('\u22ae'),"nVdash"); //<!ENTITY nVdash	"&#x22AE;"> <!-- DOES NOT FORCE -->
        entityMap.put(new Character('\u22e8'),"prnap"); //<!ENTITY prnap	"&#x22E8;"> <!--  -->
        entityMap.put(new Character('\ue2b3'),"prnE"); //<!ENTITY prnE	"&#xE2B3;"> <!--  -->
        //???entityMap.put(new Character('\u22e8'),"prnsim"); //<!ENTITY prnsim	"&#x22E8;"> <!--  -->
        entityMap.put(new Character('\u22e9'),"scnap"); //<!ENTITY scnap	"&#x22E9;"> <!--  -->
        entityMap.put(new Character('\ue2b5'),"scnE"); //<!ENTITY scnE	"&#xE2B5;"> <!--  -->
        //???entityMap.put(new Character('\u22e9'),"scnsim"); //<!ENTITY scnsim	"&#x22E9;"> <!--  -->
        entityMap.put(new Character('\u228a'),"subne"); //<!ENTITY subne	"&#x228A;"> <!--  -->
        //???entityMap.put(new Character('\u228a'),"subnE"); //<!ENTITY subnE	"&#x228A;"> <!-- SUBSET OF WITH NOT EQUAL TO -->
        entityMap.put(new Character('\u228b'),"supne"); //<!ENTITY supne	"&#x228B;"> <!--  -->
        //???entityMap.put(new Character('\u228b'),"supnE"); //<!ENTITY supnE	"&#x228B;"> <!--  -->
        entityMap.put(new Character('\ue28b'),"vsubnE"); //<!ENTITY vsubnE	"&#xE2B8;"> <!--  -->
        //???entityMap.put(new Character('\u228a'),"vsubne"); //<!ENTITY vsubne	"&#x228A;"> <!-- SUBSET OF WITH NOT EQUAL TO -->
        //???entityMap.put(new Character('\u228b'),"vsupne"); //<!ENTITY vsupne	"&#x228B;"> <!-- SUPERSET OF WITH NOT EQUAL TO -->
        //???entityMap.put(new Character('\u228b'),"vsupnE"); //<!ENTITY vsupnE	"&#x228B;"> <!-- SUPERSET OF WITH NOT EQUAL TO -->
 
        //entities from iso-amso.ent
        entityMap.put(new Character('\u2221'),"angmsd"); //<!ENTITY angmsd	"&#x2221;"> <!-- MEASURED ANGLE -->
        entityMap.put(new Character('\u2136'),"beth"); //<!ENTITY beth	"&#x2136;"> <!-- BET SYMBOL -->
        entityMap.put(new Character('\u2035'),"bprime"); //<!ENTITY bprime	"&#x2035;"> <!-- REVERSED PRIME -->
        entityMap.put(new Character('\u2201'),"comp"); //<!ENTITY comp	"&#x2201;"> <!-- COMPLEMENT -->
        entityMap.put(new Character('\u2138'),"daleth"); //<!ENTITY daleth	"&#x2138;"> <!-- DALET SYMBOL -->
        entityMap.put(new Character('\u2113'),"ell"); //<!ENTITY ell	"&#x2113;"> <!-- SCRIPT SMALL L -->
        entityMap.put(new Character('\u2137'),"gimel"); //<!ENTITY gimel	"&#x2137;"> <!-- GIMEL SYMBOL -->
        entityMap.put(new Character('\u0131'),"inodot"); //<!ENTITY inodot	"&#x0131;"> <!-- LATIN SMALL LETTER DOTLESS I -->
        //XXXentityMap.put(new Character(' '),"jnodot"); //<!--     jnodot	Unknown unicode character -->
        entityMap.put(new Character('\u2204'),"nexist"); //<!ENTITY nexist	"&#x2204;"> <!-- THERE DOES NOT EXIST -->
        entityMap.put(new Character('\u24c8'),"oS"); //<!ENTITY oS	"&#x24C8;"> <!-- CIRCLED LATIN CAPITAL LETTER S -->
        entityMap.put(new Character('\u210f'),"planck"); //<!ENTITY planck	"&#x210F;"> <!-- PLANCK CONSTANT OVER TWO PI -->
        entityMap.put(new Character('\ufe68'),"sbsol"); //<!ENTITY sbsol	"&#xFE68;"> <!-- SMALL REVERSE SOLIDUS -->
        //???entityMap.put(new Character('\u2032'),"vprime"); //<!ENTITY vprime	"&#x2032;"> <!-- PRIME -->
 
        //entities from iso-amsr.ent
        entityMap.put(new Character('\u224a'),"ape"); //<!ENTITY ape	"&#x224A;"> <!--  -->
        entityMap.put(new Character('\u224d'),"asymp"); //<!ENTITY asymp	"&#x224D;"> <!-- EQUIVALENT TO -->
        entityMap.put(new Character('\u224c'),"bcong"); //<!ENTITY bcong	"&#x224C;"> <!-- ALL EQUAL TO -->
        entityMap.put(new Character('\u220d'),"bepsi"); //<!ENTITY bepsi	"&#x220D;"> <!-- SMALL CONTAINS AS MEMBER -->
        entityMap.put(new Character('\u22c8'),"bowtie"); //<!ENTITY bowtie	"&#x22C8;"> <!--  -->
        entityMap.put(new Character('\u223d'),"bsim"); //<!ENTITY bsim	"&#x223D;"> <!--  -->
        entityMap.put(new Character('\u22cd'),"bsime"); //<!ENTITY bsime	"&#x22CD;"> <!--  -->
        entityMap.put(new Character('\u224e'),"bump"); //<!ENTITY bump	"&#x224E;"> <!--  -->
        entityMap.put(new Character('\u224f'),"bumpe"); //<!ENTITY bumpe	"&#x224F;"> <!--  -->
        entityMap.put(new Character('\u2257'),"cire"); //<!ENTITY cire	"&#x2257;"> <!--  -->
        entityMap.put(new Character('\u2254'),"colone"); //<!ENTITY colone	"&#x2254;"> <!--  -->
        entityMap.put(new Character('\u22de'),"cuepr"); //<!ENTITY cuepr	"&#x22DE;"> <!--  -->
        entityMap.put(new Character('\u22df'),"cuesc"); //<!ENTITY cuesc	"&#x22DF;"> <!--  -->
        entityMap.put(new Character('\u227c'),"cupre"); //<!ENTITY cupre	"&#x227C;"> <!--  -->
        entityMap.put(new Character('\u22a3'),"dashv"); //<!ENTITY dashv	"&#x22A3;"> <!--  -->
        entityMap.put(new Character('\u2256'),"ecir"); //<!ENTITY ecir	"&#x2256;"> <!--  -->
        entityMap.put(new Character('\u2255'),"ecolon"); //<!ENTITY ecolon	"&#x2255;"> <!--  -->
        entityMap.put(new Character('\u2251'),"eDot"); //<!ENTITY eDot	"&#x2251;"> <!--  -->
        entityMap.put(new Character('\u2250'),"esdot"); //<!ENTITY esdot	"&#x2250;"> <!--  -->
        entityMap.put(new Character('\u2252'),"efDot"); //<!ENTITY efDot	"&#x2252;"> <!--  -->
        entityMap.put(new Character('\u22dd'),"egs"); //<!ENTITY egs	"&#x22DD;"> <!--  -->
        entityMap.put(new Character('\u22dc'),"els"); //<!ENTITY els	"&#x22DC;"> <!--  -->
        entityMap.put(new Character('\u2253'),"erDot"); //<!ENTITY erDot	"&#x2253;"> <!--  -->
        entityMap.put(new Character('\u22d4'),"fork"); //<!ENTITY fork	"&#x22D4;"> <!--  -->
        entityMap.put(new Character('\u2322'),"frown"); //<!ENTITY frown	"&#x2322;"> <!--  -->
        entityMap.put(new Character('\u2273'),"gap"); //<!ENTITY gap	"&#x2273;"> <!-- GREATER-THAN OR EQUIVALENT TO -->
        entityMap.put(new Character('\u22d7'),"gsdot"); //<!ENTITY gsdot	"&#x22D7;"> <!--  -->
        entityMap.put(new Character('\u2267'),"gE"); //<!ENTITY gE	"&#x2267;"> <!--  -->
        entityMap.put(new Character('\u22db'),"gel"); //<!ENTITY gel	"&#x22DB;"> <!--  -->
        //???entityMap.put(new Character('\u22db'),"gEl"); //<!ENTITY gEl	"&#x22DB;"> <!--  -->
        //???entityMap.put(new Character('\u2265'),"ges"); //<!ENTITY ges	"&#x2265;"> <!-- GREATER-THAN OR EQUAL TO -->
        entityMap.put(new Character('\u22d9'),"Gg"); //<!ENTITY Gg	"&#x22D9;"> <!-- VERY MUCH GREATER-THAN -->
        //???entityMap.put(new Character('\u2273'),"gsim"); //<!ENTITY gsim	"&#x2273;"> <!-- GREATER-THAN OR EQUIVALENT TO -->
        entityMap.put(new Character('\u226b'),"Gt"); //<!ENTITY Gt	"&#x226B;"> <!-- MUCH GREATER-THAN -->
        entityMap.put(new Character('\u2272'),"lap"); //<!ENTITY lap	"&#x2272;"> <!-- LESS-THAN OR EQUIVALENT TO -->
        entityMap.put(new Character('\u22d6'),"ldot"); //<!ENTITY ldot	"&#x22D6;"> <!--  -->
        entityMap.put(new Character('\u2266'),"lE"); //<!ENTITY lE	"&#x2266;"> <!--  -->
        entityMap.put(new Character('\u22da'),"lEg"); //<!ENTITY lEg	"&#x22DA;"> <!--  -->
        //???entityMap.put(new Character('\u22da'),"leg"); //<!ENTITY leg	"&#x22DA;"> <!--  -->
        //???entityMap.put(new Character('\u2264'),"les"); //<!ENTITY les	"&#x2264;"> <!-- LESS-THAN OR EQUAL TO -->
        //???entityMap.put(new Character('\u22db'),"Ll"); //<!ENTITY Ll	"&#x22D8;"> <!--  -->
        //???entityMap.put(new Character('\u2272'),"c"); //<!ENTITY c	"&#x2272;"> <!-- LESS-THAN OR EQUIVALENT TO -->
        entityMap.put(new Character('\u226a'),"Lt"); //<!ENTITY Lt	"&#x226A;"> <!-- MUCH LESS-THAN -->
        entityMap.put(new Character('\u22b4'),"ltrie"); //<!ENTITY ltrie	"&#x22B4;"> <!--  -->
        entityMap.put(new Character('\u2223'),"mid"); //<!ENTITY mid	"&#x2223;"> <!--  -->
        entityMap.put(new Character('\u22a7'),"models"); //<!ENTITY models	"&#x22A7;"> <!-- MODELS -->
        entityMap.put(new Character('\u227a'),"pr"); //<!ENTITY pr	"&#x227A;"> <!--  -->
        entityMap.put(new Character('\u227e'),"prap"); //<!ENTITY prap	"&#x227E;"> <!--  -->
        //???entityMap.put(new Character('\u227c'),"pre"); //<!ENTITY pre	"&#x227C;"> <!--  -->
        //???entityMap.put(new Character('\u227e'),"prsim"); //<!ENTITY prsim	"&#x227E;"> <!--  -->
        entityMap.put(new Character('\u22b5'),"rtrie"); //<!ENTITY rtrie	"&#x22B5;"> <!--  -->
        //???entityMap.put(new Character('\u2210'),"samalg"); //<!ENTITY samalg	"&#x2210;"> <!--  -->
        entityMap.put(new Character('\u227b'),"sc"); //<!ENTITY sc	"&#x227B;"> <!--  -->
        entityMap.put(new Character('\u227f'),"scap"); //<!ENTITY scap	"&#x227F;"> <!--  -->
        entityMap.put(new Character('\u227d'),"sccue"); //<!ENTITY sccue	"&#x227D;"> <!--  -->
        //???entityMap.put(new Character('\u227d'),"sce"); //<!ENTITY sce	"&#x227D;"> <!--  -->
        //???entityMap.put(new Character('\u227f'),"scsim"); //<!ENTITY scsim	"&#x227F;"> <!--  -->
        //???entityMap.put(new Character('\u2322'),"sfrown"); //<!ENTITY sfrown	"&#x2322;"> <!-- FROWN -->
        entityMap.put(new Character('\ue301'),"smid"); //<!ENTITY smid	"&#xE301;"> <!--  -->
        entityMap.put(new Character('\u2323'),"smile"); //<!ENTITY smile	"&#x2323;"> <!--  -->
        entityMap.put(new Character('\u2225'),"spar"); //<!ENTITY spar	"&#x2225;"> <!-- PARALLEL TO -->
        entityMap.put(new Character('\u228f'),"sqsub"); //<!ENTITY sqsub	"&#x228F;"> <!--  -->
        entityMap.put(new Character('\u2291'),"sqsube"); //<!ENTITY sqsube	"&#x2291;"> <!--  -->
        entityMap.put(new Character('\u2290'),"sqsup"); //<!ENTITY sqsup	"&#x2290;"> <!--  -->
        entityMap.put(new Character('\u2292'),"sqsupe"); //<!ENTITY sqsupe	"&#x2292;"> <!--  -->
        //???entityMap.put(new Character('\u2323'),"ssmile"); //<!ENTITY ssmile	"&#x2323;"> <!-- SMILE -->
        entityMap.put(new Character('\u22d0'),"Sub"); //<!ENTITY Sub	"&#x22D0;"> <!--  -->
        //???entityMap.put(new Character('\u2286'),"subE"); //<!ENTITY subE	"&#x2286;"> <!--  -->
        entityMap.put(new Character('\u22d1'),"Sup"); //<!ENTITY Sup	"&#x22D1;"> <!--  -->
        //???entityMap.put(new Character('\u2287'),"supE"); //<!ENTITY supE	"&#x2287;"> <!--  -->
        //???entityMap.put(new Character('\u2248'),"thkap"); //<!ENTITY thkap	"&#x2248;"> <!-- ALMOST EQUAL TO -->
        //???entityMap.put(new Character('\u223c'),"thksim"); //<!ENTITY thksim	"&#x223C;"> <!-- TILDE OPERATOR -->
        entityMap.put(new Character('\u225c'),"trie"); //<!ENTITY trie	"&#x225C;"> <!--  -->
        entityMap.put(new Character('\u226c'),"twixt"); //<!ENTITY twixt	"&#x226C;"> <!-- BETWEEN -->
        entityMap.put(new Character('\u22a2'),"vdash"); //<!ENTITY vdash	"&#x22A2;"> <!--  -->
        entityMap.put(new Character('\u22a9'),"Vdash"); //<!ENTITY Vdash	"&#x22A9;"> <!--  -->
        entityMap.put(new Character('\u22a8'),"vDash"); //<!ENTITY vDash	"&#x22A8;"> <!--  -->
        entityMap.put(new Character('\u22bb'),"veebar"); //<!ENTITY veebar	"&#x22BB;"> <!--  -->
        entityMap.put(new Character('\u22b2'),"vltri"); //<!ENTITY vltri	"&#x22B2;"> <!--  -->
        //???entityMap.put(new Character('\u221d'),"vprop"); //<!ENTITY vprop	"&#x221D;"> <!--  -->
        entityMap.put(new Character('\u22b3'),"vrtri"); //<!ENTITY vrtri	"&#x22B3;"> <!--  -->
        entityMap.put(new Character('\u22aa'),"Vvdash"); //<!ENTITY Vvdash	"&#x22AA;"> <!--  -->
 
        //entities from iso-box.ent
        entityMap.put(new Character('\u2500'),"boxh"); //<!ENTITY boxh	"&#x2500;"> <!-- BOX DRAWINGS LIGHT HORIZONTAL -->
        entityMap.put(new Character('\u2514'),"boxur"); //<!ENTITY boxur	"&#x2514;"> <!-- BOX DRAWINGS LIGHT UP AND RIGHT -->
        entityMap.put(new Character('\u2518'),"boxul"); //<!ENTITY boxul	"&#x2518;"> <!-- BOX DRAWINGS LIGHT UP AND LEFT -->
        entityMap.put(new Character('\u2510'),"boxdl"); //<!ENTITY boxdl	"&#x2510;"> <!-- BOX DRAWINGS LIGHT DOWN AND LEFT -->
        entityMap.put(new Character('\u250c'),"boxdr"); //<!ENTITY boxdr	"&#x250C;"> <!-- BOX DRAWINGS LIGHT DOWN AND RIGHT -->
        entityMap.put(new Character('\u251c'),"boxvr"); //<!ENTITY boxvr	"&#x251C;"> <!-- BOX DRAWINGS LIGHT VERTICAL AND RIGHT -->
        entityMap.put(new Character('\u2534'),"boxhu"); //<!ENTITY boxhu	"&#x2534;"> <!-- BOX DRAWINGS LIGHT UP AND HORIZONTAL -->
        entityMap.put(new Character('\u2524'),"boxvl"); //<!ENTITY boxvl	"&#x2524;"> <!-- BOX DRAWINGS LIGHT VERTICAL AND LEFT -->
        entityMap.put(new Character('\u252c'),"boxhd"); //<!ENTITY boxhd	"&#x252C;"> <!-- BOX DRAWINGS LIGHT DOWN AND HORIZONTAL -->
        entityMap.put(new Character('\u253c'),"boxvh"); //<!ENTITY boxvh	"&#x253C;"> <!-- BOX DRAWINGS LIGHT VERTICAL AND HORIZONTAL -->
        entityMap.put(new Character('\u255e'),"boxvR"); //<!ENTITY boxvR	"&#x255E;"> <!-- BOX DRAWINGS VERTICAL SINGLE AND RIGHT DOUBLE -->
        entityMap.put(new Character('\u2567'),"boxhU"); //<!ENTITY boxhU	"&#x2567;"> <!-- BOX DRAWINGS UP SINGLE AND HORIZONTAL DOUBLE -->
        entityMap.put(new Character('\u2561'),"boxvL"); //<!ENTITY boxvL	"&#x2561;"> <!-- BOX DRAWINGS VERTICAL SINGLE AND LEFT DOUBLE -->
        entityMap.put(new Character('\u2564'),"boxhD"); //<!ENTITY boxhD	"&#x2564;"> <!-- BOX DRAWINGS DOWN SINGLE AND HORIZONTAL DOUBLE -->
        entityMap.put(new Character('\u256a'),"boxvH"); //<!ENTITY boxvH	"&#x256A;"> <!-- BOX DRAWINGS VERTICAL SINGLE AND HORIZONTAL DOUBLE -->
        entityMap.put(new Character('\u2550'),"boxH"); //<!ENTITY boxH	"&#x2550;"> <!-- BOX DRAWINGS DOUBLE HORIZONTAL -->
        entityMap.put(new Character('\u2551'),"boxV"); //<!ENTITY boxV	"&#x2551;"> <!-- BOX DRAWINGS DOUBLE VERTICAL -->
        entityMap.put(new Character('\u2558'),"boxUR"); //<!ENTITY boxUR	"&#x2558;"> <!-- BOX DRAWINGS UP SINGLE AND RIGHT DOUBLE -->
        entityMap.put(new Character('\u255b'),"boxUL"); //<!ENTITY boxUL	"&#x255B;"> <!-- BOX DRAWINGS UP SINGLE AND LEFT DOUBLE -->
        entityMap.put(new Character('\u2555'),"boxDL"); //<!ENTITY boxDL	"&#x2555;"> <!-- BOX DRAWINGS DOWN SINGLE AND LEFT DOUBLE -->
        entityMap.put(new Character('\u2552'),"boxDR"); //<!ENTITY boxDR	"&#x2552;"> <!-- BOX DRAWINGS DOWN SINGLE AND RIGHT DOUBLE -->
        entityMap.put(new Character('\u255f'),"boxVR"); //<!ENTITY boxVR	"&#x255F;"> <!-- BOX DRAWINGS VERTICAL DOUBLE AND RIGHT SINGLE -->
        entityMap.put(new Character('\u2568'),"boxHU"); //<!ENTITY boxHU	"&#x2568;"> <!-- BOX DRAWINGS UP DOUBLE AND HORIZONTAL SINGLE -->
        entityMap.put(new Character('\u2562'),"boxVL"); //<!ENTITY boxVL	"&#x2562;"> <!-- BOX DRAWINGS VERTICAL DOUBLE AND LEFT SINGLE -->
        entityMap.put(new Character('\u2565'),"boxHD"); //<!ENTITY boxHD	"&#x2565;"> <!-- BOX DRAWINGS DOWN DOUBLE AND HORIZONTAL SINGLE -->
        entityMap.put(new Character('\u256b'),"boxVH"); //<!ENTITY boxVH	"&#x256B;"> <!-- BOX DRAWINGS VERTICAL DOUBLE AND HORIZONTAL SINGLE -->
        entityMap.put(new Character('\u2560'),"boxVr"); //<!ENTITY boxVr	"&#x2560;"> <!-- BOX DRAWINGS DOUBLE VERTICAL AND RIGHT -->
        entityMap.put(new Character('\u2569'),"boxHu"); //<!ENTITY boxHu	"&#x2569;"> <!-- BOX DRAWINGS DOUBLE UP AND HORIZONTAL -->
        entityMap.put(new Character('\u2563'),"boxVl"); //<!ENTITY boxVl	"&#x2563;"> <!-- BOX DRAWINGS DOUBLE VERTICAL AND LEFT -->
        entityMap.put(new Character('\u2566'),"boxHd"); //<!ENTITY boxHd	"&#x2566;"> <!-- BOX DRAWINGS DOUBLE DOWN AND HORIZONTAL -->
        entityMap.put(new Character('\u256c'),"boxVh"); //<!ENTITY boxVh	"&#x256C;"> <!-- BOX DRAWINGS DOUBLE VERTICAL AND HORIZONTAL -->
        entityMap.put(new Character('\u2559'),"boxuR"); //<!ENTITY boxuR	"&#x2559;"> <!-- BOX DRAWINGS UP DOUBLE AND RIGHT SINGLE -->
        entityMap.put(new Character('\u255c'),"boxUl"); //<!ENTITY boxUl	"&#x255C;"> <!-- BOX DRAWINGS UP DOUBLE AND LEFT SINGLE -->
        entityMap.put(new Character('\u2556'),"boxdL"); //<!ENTITY boxdL	"&#x2556;"> <!-- BOX DRAWINGS DOWN DOUBLE AND LEFT SINGLE -->
        entityMap.put(new Character('\u2553'),"boxDr"); //<!ENTITY boxDr	"&#x2553;"> <!-- BOX DRAWINGS DOWN DOUBLE AND RIGHT SINGLE -->
        entityMap.put(new Character('\u255a'),"boxUr"); //<!ENTITY boxUr	"&#x255A;"> <!-- BOX DRAWINGS DOUBLE UP AND RIGHT -->
        entityMap.put(new Character('\u255d'),"boxuL"); //<!ENTITY boxuL	"&#x255D;"> <!-- BOX DRAWINGS DOUBLE UP AND LEFT -->
        entityMap.put(new Character('\u2557'),"boxDl"); //<!ENTITY boxDl	"&#x2557;"> <!-- BOX DRAWINGS DOUBLE DOWN AND LEFT -->
        entityMap.put(new Character('\u2554'),"boxdR"); //<!ENTITY boxdR	"&#x2554;"> <!-- BOX DRAWINGS DOUBLE DOWN AND RIGHT -->
 
        //entities from iso-cyr1.ent
        entityMap.put(new Character('\u0430'),"acy"); //<!ENTITY acy	"&#x0430;"> <!-- CYRILLIC SMALL LETTER A -->
        entityMap.put(new Character('\u0410'),"Acy"); //<!ENTITY Acy	"&#x0410;"> <!-- CYRILLIC CAPITAL LETTER A -->
        entityMap.put(new Character('\u0431'),"bcy"); //<!ENTITY bcy	"&#x0431;"> <!-- CYRILLIC SMALL LETTER BE -->
        entityMap.put(new Character('\u0411'),"Bcy"); //<!ENTITY Bcy	"&#x0411;"> <!-- CYRILLIC CAPITAL LETTER BE -->
        entityMap.put(new Character('\u0432'),"vcy"); //<!ENTITY vcy	"&#x0432;"> <!-- CYRILLIC SMALL LETTER VE -->
        entityMap.put(new Character('\u0412'),"Vcy"); //<!ENTITY Vcy	"&#x0412;"> <!-- CYRILLIC CAPITAL LETTER VE -->
        entityMap.put(new Character('\u0433'),"gcy"); //<!ENTITY gcy	"&#x0433;"> <!-- CYRILLIC SMALL LETTER GHE -->
        entityMap.put(new Character('\u0413'),"Gcy"); //<!ENTITY Gcy	"&#x0413;"> <!-- CYRILLIC CAPITAL LETTER GHE -->
        entityMap.put(new Character('\u0434'),"dcy"); //<!ENTITY dcy	"&#x0434;"> <!-- CYRILLIC SMALL LETTER DE -->
        entityMap.put(new Character('\u0414'),"Dcy"); //<!ENTITY Dcy	"&#x0414;"> <!-- CYRILLIC CAPITAL LETTER DE -->
        entityMap.put(new Character('\u0435'),"iecy"); //<!ENTITY iecy	"&#x0435;"> <!-- CYRILLIC SMALL LETTER IE -->
        entityMap.put(new Character('\u0415'),"IEcy"); //<!ENTITY IEcy	"&#x0415;"> <!-- CYRILLIC CAPITAL LETTER IE -->
        entityMap.put(new Character('\u0451'),"iocy"); //<!ENTITY iocy	"&#x0451;"> <!-- CYRILLIC SMALL LETTER IO -->
        entityMap.put(new Character('\u0401'),"IOcy"); //<!ENTITY IOcy	"&#x0401;"> <!-- CYRILLIC CAPITAL LETTER IO -->
        entityMap.put(new Character('\u0436'),"zhcy"); //<!ENTITY zhcy	"&#x0436;"> <!-- CYRILLIC SMALL LETTER ZHE -->
        entityMap.put(new Character('\u0416'),"ZHcy"); //<!ENTITY ZHcy	"&#x0416;"> <!-- CYRILLIC CAPITAL LETTER ZHE -->
        entityMap.put(new Character('\u0437'),"zcy"); //<!ENTITY zcy	"&#x0437;"> <!-- CYRILLIC SMALL LETTER ZE -->
        entityMap.put(new Character('\u0417'),"Zcy"); //<!ENTITY Zcy	"&#x0417;"> <!-- CYRILLIC CAPITAL LETTER ZE -->
        entityMap.put(new Character('\u0438'),"icy"); //<!ENTITY icy	"&#x0438;"> <!-- CYRILLIC SMALL LETTER I -->
        entityMap.put(new Character('\u0418'),"Icy"); //<!ENTITY Icy	"&#x0418;"> <!-- CYRILLIC CAPITAL LETTER I -->
        entityMap.put(new Character('\u0439'),"jcy"); //<!ENTITY jcy	"&#x0439;"> <!-- CYRILLIC SMALL LETTER SHORT I -->
        entityMap.put(new Character('\u0419'),"Jcy"); //<!ENTITY Jcy	"&#x0419;"> <!-- CYRILLIC CAPITAL LETTER SHORT I -->
        entityMap.put(new Character('\u043a'),"kcy"); //<!ENTITY kcy	"&#x043A;"> <!-- CYRILLIC SMALL LETTER KA -->
        entityMap.put(new Character('\u041a'),"Kcy"); //<!ENTITY Kcy	"&#x041A;"> <!-- CYRILLIC CAPITAL LETTER KA -->
        entityMap.put(new Character('\u043b'),"lcy"); //<!ENTITY lcy	"&#x043B;"> <!-- CYRILLIC SMALL LETTER EL -->
        entityMap.put(new Character('\u041b'),"Lcy"); //<!ENTITY Lcy	"&#x041B;"> <!-- CYRILLIC CAPITAL LETTER EL -->
        entityMap.put(new Character('\u043c'),"mcy"); //<!ENTITY mcy	"&#x043C;"> <!-- CYRILLIC SMALL LETTER EM -->
        entityMap.put(new Character('\u041c'),"Mcy"); //<!ENTITY Mcy	"&#x041C;"> <!-- CYRILLIC CAPITAL LETTER EM -->
        entityMap.put(new Character('\u043d'),"ncy"); //<!ENTITY ncy	"&#x043D;"> <!-- CYRILLIC SMALL LETTER EN -->
        entityMap.put(new Character('\u041d'),"Ncy"); //<!ENTITY Ncy	"&#x041D;"> <!-- CYRILLIC CAPITAL LETTER EN -->
        entityMap.put(new Character('\u043e'),"ocy"); //<!ENTITY ocy	"&#x043E;"> <!-- CYRILLIC SMALL LETTER O -->
        entityMap.put(new Character('\u041e'),"Ocy"); //<!ENTITY Ocy	"&#x041E;"> <!-- CYRILLIC CAPITAL LETTER O -->
        entityMap.put(new Character('\u043f'),"pcy"); //<!ENTITY pcy	"&#x043F;"> <!-- CYRILLIC SMALL LETTER PE -->
        entityMap.put(new Character('\u041f'),"Pcy"); //<!ENTITY Pcy	"&#x041F;"> <!-- CYRILLIC CAPITAL LETTER PE -->
        entityMap.put(new Character('\u0440'),"rcy"); //<!ENTITY rcy	"&#x0440;"> <!-- CYRILLIC SMALL LETTER ER -->
        entityMap.put(new Character('\u0420'),"Rcy"); //<!ENTITY Rcy	"&#x0420;"> <!-- CYRILLIC CAPITAL LETTER ER -->
        entityMap.put(new Character('\u0441'),"scy"); //<!ENTITY scy	"&#x0441;"> <!-- CYRILLIC SMALL LETTER ES -->
        entityMap.put(new Character('\u0421'),"Scy"); //<!ENTITY Scy	"&#x0421;"> <!-- CYRILLIC CAPITAL LETTER ES -->
        entityMap.put(new Character('\u0442'),"tcy"); //<!ENTITY tcy	"&#x0442;"> <!-- CYRILLIC SMALL LETTER TE -->
        entityMap.put(new Character('\u0422'),"Tcy"); //<!ENTITY Tcy	"&#x0422;"> <!-- CYRILLIC CAPITAL LETTER TE -->
        entityMap.put(new Character('\u0443'),"ucy"); //<!ENTITY ucy	"&#x0443;"> <!-- CYRILLIC SMALL LETTER U -->
        entityMap.put(new Character('\u0423'),"Ucy"); //<!ENTITY Ucy	"&#x0423;"> <!-- CYRILLIC CAPITAL LETTER U -->
        entityMap.put(new Character('\u0444'),"fcy"); //<!ENTITY fcy	"&#x0444;"> <!-- CYRILLIC SMALL LETTER EF -->
        entityMap.put(new Character('\u0424'),"Fcy"); //<!ENTITY Fcy	"&#x0424;"> <!-- CYRILLIC CAPITAL LETTER EF -->
        entityMap.put(new Character('\u0445'),"khcy"); //<!ENTITY khcy	"&#x0445;"> <!-- CYRILLIC SMALL LETTER HA -->
        entityMap.put(new Character('\u0425'),"KHcy"); //<!ENTITY KHcy	"&#x0425;"> <!-- CYRILLIC CAPITAL LETTER HA -->
        entityMap.put(new Character('\u0446'),"tscy"); //<!ENTITY tscy	"&#x0446;"> <!-- CYRILLIC SMALL LETTER TSE -->
        entityMap.put(new Character('\u0426'),"TScy"); //<!ENTITY TScy	"&#x0426;"> <!-- CYRILLIC CAPITAL LETTER TSE -->
        entityMap.put(new Character('\u0447'),"chcy"); //<!ENTITY chcy	"&#x0447;"> <!-- CYRILLIC SMALL LETTER CHE -->
        entityMap.put(new Character('\u0427'),"CHcy"); //<!ENTITY CHcy	"&#x0427;"> <!-- CYRILLIC CAPITAL LETTER CHE -->
        entityMap.put(new Character('\u0448'),"shcy"); //<!ENTITY shcy	"&#x0448;"> <!-- CYRILLIC SMALL LETTER SHA -->
        entityMap.put(new Character('\u0428'),"SHcy"); //<!ENTITY SHcy	"&#x0428;"> <!-- CYRILLIC CAPITAL LETTER SHA -->
        entityMap.put(new Character('\u0449'),"shchcy"); //<!ENTITY shchcy	"&#x0449;"> <!-- CYRILLIC SMALL LETTER SHCHA -->
        entityMap.put(new Character('\u0429'),"SHCHcy"); //<!ENTITY SHCHcy	"&#x0429;"> <!-- CYRILLIC CAPITAL LETTER SHCHA -->
        entityMap.put(new Character('\u044a'),"hardcy"); //<!ENTITY hardcy	"&#x044A;"> <!-- CYRILLIC SMALL LETTER HARD SIGN -->
        entityMap.put(new Character('\u042a'),"HARDcy"); //<!ENTITY HARDcy	"&#x042A;"> <!-- CYRILLIC CAPITAL LETTER HARD SIGN -->
        entityMap.put(new Character('\u044b'),"ycy"); //<!ENTITY ycy	"&#x044B;"> <!-- CYRILLIC SMALL LETTER YERU -->
        entityMap.put(new Character('\u042b'),"Ycy"); //<!ENTITY Ycy	"&#x042B;"> <!-- CYRILLIC CAPITAL LETTER YERU -->
        entityMap.put(new Character('\u044c'),"softcy"); //<!ENTITY softcy	"&#x044C;"> <!-- CYRILLIC SMALL LETTER SOFT SIGN -->
        entityMap.put(new Character('\u042c'),"SOFTcy"); //<!ENTITY SOFTcy	"&#x042C;"> <!-- CYRILLIC CAPITAL LETTER SOFT SIGN -->
        entityMap.put(new Character('\u044d'),"ecy"); //<!ENTITY ecy	"&#x044D;"> <!-- CYRILLIC SMALL LETTER E -->
        entityMap.put(new Character('\u042d'),"Ecy"); //<!ENTITY Ecy	"&#x042D;"> <!-- CYRILLIC CAPITAL LETTER E -->
        entityMap.put(new Character('\u044e'),"yucy"); //<!ENTITY yucy	"&#x044E;"> <!-- CYRILLIC SMALL LETTER YU -->
        entityMap.put(new Character('\u042e'),"YUcy"); //<!ENTITY YUcy	"&#x042E;"> <!-- CYRILLIC CAPITAL LETTER YU -->
        entityMap.put(new Character('\u044f'),"yacy"); //<!ENTITY yacy	"&#x044F;"> <!-- CYRILLIC SMALL LETTER YA -->
        entityMap.put(new Character('\u042F'),"YAcy"); //<!ENTITY YAcy	"&#x042F;"> <!-- CYRILLIC CAPITAL LETTER YA -->
        entityMap.put(new Character('\u2116'),"numero"); //<!ENTITY numero	"&#x2116;"> <!-- NUMERO SIGN -->
 
        //entities from iso-cyr2.ent
        entityMap.put(new Character('\u0452'),"djcy"); //<!ENTITY djcy	"&#x0452;"> <!-- CYRILLIC SMALL LETTER DJE -->
        entityMap.put(new Character('\u0402'),"DJcy"); //<!ENTITY DJcy	"&#x0402;"> <!-- CYRILLIC CAPITAL LETTER DJE -->
        entityMap.put(new Character('\u0453'),"gjcy"); //<!ENTITY gjcy	"&#x0453;"> <!-- CYRILLIC SMALL LETTER GJE -->
        entityMap.put(new Character('\u0403'),"GJcy"); //<!ENTITY GJcy	"&#x0403;"> <!-- CYRILLIC CAPITAL LETTER GJE -->
        entityMap.put(new Character('\u0454'),"jukcy"); //<!ENTITY jukcy	"&#x0454;"> <!-- CYRILLIC SMALL LETTER UKRAINIAN IE -->
        entityMap.put(new Character('\u0404'),"Jukcy"); //<!ENTITY Jukcy	"&#x0404;"> <!-- CYRILLIC CAPITAL LETTER UKRAINIAN IE -->
        entityMap.put(new Character('\u0455'),"dscy"); //<!ENTITY dscy	"&#x0455;"> <!-- CYRILLIC SMALL LETTER DZE -->
        entityMap.put(new Character('\u0405'),"DScy"); //<!ENTITY DScy	"&#x0405;"> <!-- CYRILLIC CAPITAL LETTER DZE -->
        entityMap.put(new Character('\u0456'),"iukcy"); //<!ENTITY iukcy	"&#x0456;"> <!-- CYRILLIC SMALL LETTER BYELORUSSIAN-UKRAINIAN I -->
        entityMap.put(new Character('\u0406'),"Iukcy"); //<!ENTITY Iukcy	"&#x0406;"> <!-- CYRILLIC CAPITAL LETTER BYELORUSSIAN-UKRAINIAN I -->
        entityMap.put(new Character('\u0457'),"yicy"); //<!ENTITY yicy	"&#x0457;"> <!-- CYRILLIC SMALL LETTER YI -->
        entityMap.put(new Character('\u0407'),"YIcy"); //<!ENTITY YIcy	"&#x0407;"> <!-- CYRILLIC CAPITAL LETTER YI -->
        entityMap.put(new Character('\u0458'),"jsercy"); //<!ENTITY jsercy	"&#x0458;"> <!-- CYRILLIC SMALL LETTER JE -->
        entityMap.put(new Character('\u0408'),"Jsercy"); //<!ENTITY Jsercy	"&#x0408;"> <!-- CYRILLIC CAPITAL LETTER JE -->
        entityMap.put(new Character('\u0459'),"ljcy"); //<!ENTITY ljcy	"&#x0459;"> <!-- CYRILLIC SMALL LETTER LJE -->
        entityMap.put(new Character('\u0409'),"LJcy"); //<!ENTITY LJcy	"&#x0409;"> <!-- CYRILLIC CAPITAL LETTER LJE -->
        entityMap.put(new Character('\u045a'),"njcy"); //<!ENTITY njcy	"&#x045A;"> <!-- CYRILLIC SMALL LETTER NJE -->
        entityMap.put(new Character('\u040a'),"NJcy"); //<!ENTITY NJcy	"&#x040A;"> <!-- CYRILLIC CAPITAL LETTER NJE -->
        entityMap.put(new Character('\u045b'),"tshcy"); //<!ENTITY tshcy	"&#x045B;"> <!-- CYRILLIC SMALL LETTER TSHE -->
        entityMap.put(new Character('\u040b'),"TSHcy"); //<!ENTITY TSHcy	"&#x040B;"> <!-- CYRILLIC CAPITAL LETTER TSHE -->
        entityMap.put(new Character('\u045c'),"kjcy"); //<!ENTITY kjcy	"&#x045C;"> <!-- CYRILLIC SMALL LETTER KJE -->
        entityMap.put(new Character('\u040c'),"KJcy"); //<!ENTITY KJcy	"&#x040C;"> <!-- CYRILLIC CAPITAL LETTER KJE -->
        entityMap.put(new Character('\u045e'),"ubrcy"); //<!ENTITY ubrcy	"&#x045E;"> <!-- CYRILLIC SMALL LETTER SHORT U -->
        entityMap.put(new Character('\u040e'),"Ubrcy"); //<!ENTITY Ubrcy	"&#x040E;"> <!-- CYRILLIC CAPITAL LETTER SHORT U -->
        entityMap.put(new Character('\u045f'),"dzcy"); //<!ENTITY dzcy	"&#x045F;"> <!-- CYRILLIC SMALL LETTER DZHE -->
        entityMap.put(new Character('\u040f'),"DZcy"); //<!ENTITY DZcy	"&#x040F;"> <!-- CYRILLIC CAPITAL LETTER DZHE -->
 
        //entities from iso-dia.ent
        entityMap.put(new Character('\u02d8'),"breve"); //<!ENTITY breve	"&#x02D8;"> <!-- BREVE -->
        entityMap.put(new Character('\u02c7'),"caron"); //<!ENTITY caron	"&#x02C7;"> <!-- CARON -->
        entityMap.put(new Character('\u005e'),"circ"); //<!ENTITY circ	"&#x005E;"> <!-- RING OPERATOR -->
        entityMap.put(new Character('\u02dd'),"dblac"); //<!ENTITY dblac	"&#x02DD;"> <!-- DOUBLE ACUTE ACCENT -->
        //???entityMap.put(new Character('\u00a8'),"die"); //<!ENTITY die	"&#x00A8;"> <!--  -->
        entityMap.put(new Character('\u02d9'),"dot"); //<!ENTITY dot	"&#x02D9;"> <!-- DOT ABOVE -->
        entityMap.put(new Character('\u0060'),"grave"); //<!ENTITY grave	"&#x0060;"> <!-- GRAVE ACCENT -->
        entityMap.put(new Character('\u02db'),"ogon"); //<!ENTITY ogon	"&#x02DB;"> <!-- OGONEK -->
        entityMap.put(new Character('\u02da'),"ring"); //<!ENTITY ring	"&#x02DA;"> <!-- RING ABOVE -->
 
        //entities from iso-grk1.ent
        //???entityMap.put(new Character('\u03b1'),"agr"); //<!ENTITY agr	"&#x03B1;"> <!--  -->
        //???entityMap.put(new Character('\u0391'),"Agr"); //<!ENTITY Agr	"&#x0391;"> <!-- GREEK CAPITAL LETTER ALPHA -->
        //???entityMap.put(new Character('\u03b2'),"bgr"); //<!ENTITY bgr	"&#x03B2;"> <!-- GREEK SMALL LETTER BETA -->
        //???entityMap.put(new Character('\u0392'),"Bgr"); //<!ENTITY Bgr	"&#x0392;"> <!-- GREEK CAPITAL LETTER BETA -->
        //???entityMap.put(new Character('\u03b3'),"ggr"); //<!ENTITY ggr	"&#x03B3;"> <!-- GREEK SMALL LETTER GAMMA -->
        //???entityMap.put(new Character('\u0393'),"Ggr"); //<!ENTITY Ggr	"&#x0393;"> <!-- GREEK CAPITAL LETTER GAMMA -->
        //???entityMap.put(new Character('\u03b4'),"dgr"); //<!ENTITY dgr	"&#x03B4;"> <!-- GREEK SMALL LETTER DELTA -->
        //???entityMap.put(new Character('\u0394'),"Dgr"); //<!ENTITY Dgr	"&#x0394;"> <!-- GREEK CAPITAL LETTER DELTA -->
        //???entityMap.put(new Character('\u03b5'),"egr"); //<!ENTITY egr	"&#x03B5;"> <!--  -->
        //???entityMap.put(new Character('\u0395'),"Egr"); //<!ENTITY Egr	"&#x0395;"> <!-- GREEK CAPITAL LETTER EPSILON -->
        //???entityMap.put(new Character('\u03b6'),"zgr"); //<!ENTITY zgr	"&#x03B6;"> <!-- GREEK SMALL LETTER ZETA -->
        //???entityMap.put(new Character('\u0396'),"Zgr"); //<!ENTITY Zgr	"&#x0396;"> <!-- GREEK CAPITAL LETTER ZETA -->
        //???entityMap.put(new Character('\u03b7'),"eegr"); //<!ENTITY eegr	"&#x03B7;"> <!-- GREEK SMALL LETTER ETA -->
        //???entityMap.put(new Character('\u0397'),"EEgr"); //<!ENTITY EEgr	"&#x0397;"> <!-- GREEK CAPITAL LETTER ETA -->
        //???entityMap.put(new Character('\u03b8'),"thgr"); //<!ENTITY thgr	"&#x03B8;"> <!--  -->
        //???entityMap.put(new Character('\u0398'),"THgr"); //<!ENTITY THgr	"&#x0398;"> <!-- GREEK CAPITAL LETTER THETA -->
        //???entityMap.put(new Character('\u03b9'),"igr"); //<!ENTITY igr	"&#x03B9;"> <!-- GREEK SMALL LETTER IOTA -->
        //???entityMap.put(new Character('\u0399'),"Igr"); //<!ENTITY Igr	"&#x0399;"> <!-- GREEK CAPITAL LETTER IOTA -->
        //???entityMap.put(new Character('\u03ba'),"kgr"); //<!ENTITY kgr	"&#x03BA;"> <!-- GREEK SMALL LETTER KAPPA -->
        //???entityMap.put(new Character('\u039a'),"Kgr"); //<!ENTITY Kgr	"&#x039A;"> <!-- GREEK CAPITAL LETTER KAPPA -->
        //???entityMap.put(new Character('\u039b'),"Lgr"); //<!ENTITY Lgr	"&#x039B;"> <!-- GREEK CAPITAL LETTER LAMDA -->
        //???entityMap.put(new Character('\u03bc'),"mgr"); //<!ENTITY mgr	"&#x03BC;"> <!-- GREEK SMALL LETTER MU -->
        //???entityMap.put(new Character('\u039c'),"Mgr"); //<!ENTITY Mgr	"&#x039C;"> <!-- GREEK CAPITAL LETTER MU -->
        //???entityMap.put(new Character('\u03bd'),"ngr"); //<!ENTITY ngr	"&#x03BD;"> <!-- GREEK SMALL LETTER NU -->
        //???entityMap.put(new Character('\u039d'),"Ngr"); //<!ENTITY Ngr	"&#x039D;"> <!-- GREEK CAPITAL LETTER NU -->
        //???entityMap.put(new Character('\u03be'),"xgr"); //<!ENTITY xgr	"&#x03BE;"> <!-- GREEK SMALL LETTER XI -->
        //???entityMap.put(new Character('\u039e'),"Xgr"); //<!ENTITY Xgr	"&#x039E;"> <!-- GREEK CAPITAL LETTER XI -->
        //???entityMap.put(new Character('\u03bf'),"ogr"); //<!ENTITY ogr	"&#x03BF;"> <!-- GREEK SMALL LETTER OMICRON -->
        //???entityMap.put(new Character('\u039f'),"Ogr"); //<!ENTITY Ogr	"&#x039F;"> <!-- GREEK CAPITAL LETTER OMICRON -->
        //???entityMap.put(new Character('\u03c0'),"pgr"); //<!ENTITY pgr	"&#x03C0;"> <!-- GREEK SMALL LETTER PI -->
        //???entityMap.put(new Character('\u03a0'),"Pgr"); //<!ENTITY Pgr	"&#x03A0;"> <!-- GREEK CAPITAL LETTER PI -->
        //???entityMap.put(new Character('\u03c1'),"rgr"); //<!ENTITY rgr	"&#x03C1;"> <!-- GREEK SMALL LETTER RHO -->
        //???entityMap.put(new Character('\u03a1'),"Rgr"); //<!ENTITY Rgr	"&#x03A1;"> <!-- GREEK CAPITAL LETTER RHO -->
        //???entityMap.put(new Character('\u03c3'),"sgr"); //<!ENTITY sgr	"&#x03C3;"> <!-- GREEK SMALL LETTER SIGMA -->
        //???entityMap.put(new Character('\u03a3'),"Sgr"); //<!ENTITY Sgr	"&#x03A3;"> <!-- GREEK CAPITAL LETTER SIGMA -->
        //???entityMap.put(new Character('\u03c2'),"sfgr"); //<!ENTITY sfgr	"&#x03C2;"> <!--  -->
        //???entityMap.put(new Character('\u03c4'),"tgr"); //<!ENTITY tgr	"&#x03C4;"> <!-- GREEK SMALL LETTER TAU -->
        //???entityMap.put(new Character('\u03a4'),"Tgr"); //<!ENTITY Tgr	"&#x03A4;"> <!-- GREEK CAPITAL LETTER TAU -->
        //???entityMap.put(new Character('\u03c5'),"ugr"); //<!ENTITY ugr	"&#x03C5;"> <!-- GREEK SMALL LETTER UPSILON -->
        //???entityMap.put(new Character('\u03a5'),"Ugr"); //<!ENTITY Ugr	"&#x03A5;"> <!--  -->
        //???entityMap.put(new Character('\u03c6'),"phgr"); //<!ENTITY phgr	"&#x03C6;"> <!-- GREEK SMALL LETTER PHI -->
        //???entityMap.put(new Character('\u03a6'),"PHgr"); //<!ENTITY PHgr	"&#x03A6;"> <!-- GREEK CAPITAL LETTER PHI -->
        //???entityMap.put(new Character('\u03c7'),"khgr"); //<!ENTITY khgr	"&#x03C7;"> <!-- GREEK SMALL LETTER CHI -->
        //???entityMap.put(new Character('\u03a7'),"KHgr"); //<!ENTITY KHgr	"&#x03A7;"> <!-- GREEK CAPITAL LETTER CHI -->
        //???entityMap.put(new Character('\u03c8'),"psgr"); //<!ENTITY psgr	"&#x03C8;"> <!-- GREEK SMALL LETTER PSI -->
        //???entityMap.put(new Character('\u03a8'),"PSgr"); //<!ENTITY PSgr	"&#x03A8;"> <!-- GREEK CAPITAL LETTER PSI -->
        //???entityMap.put(new Character('\u03c9'),"ohgr"); //<!ENTITY ohgr	"&#x03C9;"> <!-- GREEK SMALL LETTER OMEGA -->
        //???entityMap.put(new Character('\u03a9'),"OHgr"); //<!ENTITY OHgr	"&#x03A9;"> <!-- GREEK CAPITAL LETTER OMEGA -->
 
        //entities from iso-grk2.ent
        entityMap.put(new Character('\u03ac'),"aacgr"); //<!ENTITY aacgr	"&#x03AC;"> <!-- GREEK SMALL LETTER ALPHA WITH TONOS -->
        entityMap.put(new Character('\u0386'),"Aacgr"); //<!ENTITY Aacgr	"&#x0386;"> <!-- GREEK CAPITAL LETTER ALPHA WITH TONOS -->
        entityMap.put(new Character('\u03ad'),"eacgr"); //<!ENTITY eacgr	"&#x03AD;"> <!-- GREEK SMALL LETTER EPSILON WITH TONOS -->
        entityMap.put(new Character('\u0388'),"Eacgr"); //<!ENTITY Eacgr	"&#x0388;"> <!-- GREEK CAPITAL LETTER EPSILON WITH TONOS -->
        entityMap.put(new Character('\u03ae'),"eeacgr"); //<!ENTITY eeacgr	"&#x03AE;"> <!-- GREEK SMALL LETTER ETA WITH TONOS -->
        entityMap.put(new Character('\u0389'),"EEacgr"); //<!ENTITY EEacgr	"&#x0389;"> <!-- GREEK CAPITAL LETTER ETA WITH TONOS -->
        entityMap.put(new Character('\u03ca'),"idigr"); //<!ENTITY idigr	"&#x03CA;"> <!-- GREEK SMALL LETTER IOTA WITH DIALYTIKA -->
        entityMap.put(new Character('\u03aa'),"Idigr"); //<!ENTITY Idigr	"&#x03AA;"> <!-- GREEK CAPITAL LETTER IOTA WITH DIALYTIKA -->
        entityMap.put(new Character('\u03af'),"iacgr"); //<!ENTITY iacgr	"&#x03AF;"> <!-- GREEK SMALL LETTER IOTA WITH TONOS -->
        entityMap.put(new Character('\u038a'),"Iacgr"); //<!ENTITY Iacgr	"&#x038A;"> <!-- GREEK CAPITAL LETTER IOTA WITH TONOS -->
        entityMap.put(new Character('\u0390'),"idiagr"); //<!ENTITY idiagr	"&#x0390;"> <!-- GREEK SMALL LETTER IOTA WITH DIALYTIKA AND TONOS -->
        entityMap.put(new Character('\u03cc'),"oacgr"); //<!ENTITY oacgr	"&#x03CC;"> <!-- GREEK SMALL LETTER OMICRON WITH TONOS -->
        entityMap.put(new Character('\u038c'),"Oacgr"); //<!ENTITY Oacgr	"&#x038C;"> <!-- GREEK CAPITAL LETTER OMICRON WITH TONOS -->
        entityMap.put(new Character('\u03cb'),"udigr"); //<!ENTITY udigr	"&#x03CB;"> <!-- GREEK SMALL LETTER UPSILON WITH DIALYTIKA -->
        entityMap.put(new Character('\u03ab'),"Udigr"); //<!ENTITY Udigr	"&#x03AB;"> <!-- GREEK CAPITAL LETTER UPSILON WITH DIALYTIKA -->
        entityMap.put(new Character('\u03cd'),"uacgr"); //<!ENTITY uacgr	"&#x03CD;"> <!-- GREEK SMALL LETTER UPSILON WITH TONOS -->
        entityMap.put(new Character('\u038e'),"Uacgr"); //<!ENTITY Uacgr	"&#x038E;"> <!-- GREEK CAPITAL LETTER UPSILON WITH TONOS -->
        entityMap.put(new Character('\u03b0'),"udiagr"); //<!ENTITY udiagr	"&#x03B0;"> <!-- GREEK SMALL LETTER UPSILON WITH DIALYTIKA AND TONOS -->
        entityMap.put(new Character('\u03ce'),"ohacgr"); //<!ENTITY ohacgr	"&#x03CE;"> <!-- GREEK SMALL LETTER OMEGA WITH TONOS -->
        entityMap.put(new Character('\u038f'),"OHacgr"); //<!ENTITY OHacgr	"&#x038F;"> <!-- GREEK CAPITAL LETTER OMEGA WITH TONOS -->
 
        //entities from iso-grk3.ent
        entityMap.put(new Character('\u03dc'),"gammad"); //<!ENTITY gammad	"&#x03DC;"> <!-- GREEK LETTER DIGAMMA -->
        entityMap.put(new Character('\u220a'),"epsi"); //<!ENTITY epsi	"&#x220A;"> <!--  -->
        //???entityMap.put(new Character('\u03b5'),"epsiv"); //<!ENTITY epsiv	"&#x03B5;"> <!--  -->
        //???entityMap.put(new Character('\u220a'),"epsis"); //<!ENTITY epsis	"&#x220A;"> <!--  -->
        //???entityMap.put(new Character('\u03b8'),"thetas"); //<!ENTITY thetas	"&#x03B8;"> <!--  -->
        //???entityMap.put(new Character('\u03d1'),"thetav"); //<!ENTITY thetav	"&#x03D1;"> <!--  -->
        entityMap.put(new Character('\u03f0'),"kappav"); //<!ENTITY kappav	"&#x03F0;"> <!-- GREEK KAPPA SYMBOL -->
        entityMap.put(new Character('\u03f1'),"rhov"); //<!ENTITY rhov	"&#x03F1;"> <!-- GREEK RHO SYMBOL -->
        //???entityMap.put(new Character('\u03c2'),"sigmav"); //<!ENTITY sigmav	"&#x03C2;"> <!--  -->
        //???entityMap.put(new Character('\u03c5'),"upsi"); //<!ENTITY upsi	"&#x03C5;"> <!-- GREEK SMALL LETTER UPSILON -->
        //???entityMap.put(new Character('\u03d2'),"Upsi"); //<!ENTITY Upsi	"&#x03D2;"> <!--  -->
        //???entityMap.put(new Character('\u03c6'),"phis"); //<!ENTITY phis	"&#x03C6;"> <!-- GREEK SMALL LETTER PHI -->
        entityMap.put(new Character('\u03d5'),"phiv"); //<!ENTITY phiv	"&#x03D5;"> <!-- GREEK PHI SYMBOL -->
 
        //entities from iso-lat2.ent
        entityMap.put(new Character('\u0103'),"abreve"); //<!ENTITY abreve	"&#x0103;"> <!-- LATIN SMALL LETTER A WITH BREVE -->
        entityMap.put(new Character('\u0102'),"Abreve"); //<!ENTITY Abreve	"&#x0102;"> <!-- LATIN CAPITAL LETTER A WITH BREVE -->
        entityMap.put(new Character('\u0101'),"amacr"); //<!ENTITY amacr	"&#x0101;"> <!-- LATIN SMALL LETTER A WITH MACRON -->
        entityMap.put(new Character('\u0100'),"Amacr"); //<!ENTITY Amacr	"&#x0100;"> <!-- LATIN CAPITAL LETTER A WITH MACRON -->
        entityMap.put(new Character('\u0105'),"aogon"); //<!ENTITY aogon	"&#x0105;"> <!-- LATIN SMALL LETTER A WITH OGONEK -->
        entityMap.put(new Character('\u0104'),"Aogon"); //<!ENTITY Aogon	"&#x0104;"> <!-- LATIN CAPITAL LETTER A WITH OGONEK -->
        entityMap.put(new Character('\u0107'),"cacute"); //<!ENTITY cacute	"&#x0107;"> <!-- LATIN SMALL LETTER C WITH ACUTE -->
        entityMap.put(new Character('\u0106'),"Cacute"); //<!ENTITY Cacute	"&#x0106;"> <!-- LATIN CAPITAL LETTER C WITH ACUTE -->
        entityMap.put(new Character('\u010d'),"ccaron"); //<!ENTITY ccaron	"&#x010D;"> <!-- LATIN SMALL LETTER C WITH CARON -->
        entityMap.put(new Character('\u010c'),"Ccaron"); //<!ENTITY Ccaron	"&#x010C;"> <!-- LATIN CAPITAL LETTER C WITH CARON -->
        entityMap.put(new Character('\u0109'),"ccirc"); //<!ENTITY ccirc	"&#x0109;"> <!-- LATIN SMALL LETTER C WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0108'),"Ccirc"); //<!ENTITY Ccirc	"&#x0108;"> <!-- LATIN CAPITAL LETTER C WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u010b'),"cdot"); //<!ENTITY cdot	"&#x010B;"> <!-- DOT OPERATOR -->
        entityMap.put(new Character('\u010a'),"Cdot"); //<!ENTITY Cdot	"&#x010A;"> <!-- LATIN CAPITAL LETTER C WITH DOT ABOVE -->
        entityMap.put(new Character('\u010f'),"dcaron"); //<!ENTITY dcaron	"&#x010F;"> <!-- LATIN SMALL LETTER D WITH CARON -->
        entityMap.put(new Character('\u010e'),"Dcaron"); //<!ENTITY Dcaron	"&#x010E;"> <!-- LATIN CAPITAL LETTER D WITH CARON -->
        entityMap.put(new Character('\u0111'),"dstrok"); //<!ENTITY dstrok	"&#x0111;"> <!-- LATIN SMALL LETTER D WITH STROKE -->
        entityMap.put(new Character('\u0110'),"Dstrok"); //<!ENTITY Dstrok	"&#x0110;"> <!-- LATIN CAPITAL LETTER D WITH STROKE -->
        entityMap.put(new Character('\u011b'),"ecaron"); //<!ENTITY ecaron	"&#x011B;"> <!-- LATIN SMALL LETTER E WITH CARON -->
        entityMap.put(new Character('\u011a'),"Ecaron"); //<!ENTITY Ecaron	"&#x011A;"> <!-- LATIN CAPITAL LETTER E WITH CARON -->
        entityMap.put(new Character('\u0117'),"edot"); //<!ENTITY edot	"&#x0117;"> <!-- LATIN SMALL LETTER E WITH DOT ABOVE -->
        entityMap.put(new Character('\u0116'),"Edot"); //<!ENTITY Edot	"&#x0116;"> <!-- LATIN CAPITAL LETTER E WITH DOT ABOVE -->
        entityMap.put(new Character('\u0113'),"emacr"); //<!ENTITY emacr	"&#x0113;"> <!-- LATIN SMALL LETTER E WITH MACRON -->
        entityMap.put(new Character('\u0112'),"Emacr"); //<!ENTITY Emacr	"&#x0112;"> <!-- LATIN CAPITAL LETTER E WITH MACRON -->
        entityMap.put(new Character('\u0119'),"eogon"); //<!ENTITY eogon	"&#x0119;"> <!-- LATIN SMALL LETTER E WITH OGONEK -->
        entityMap.put(new Character('\u0118'),"Eogon"); //<!ENTITY Eogon	"&#x0118;"> <!-- LATIN CAPITAL LETTER E WITH OGONEK -->
        entityMap.put(new Character('\u01f5'),"gacute"); //<!ENTITY gacute	"&#x01F5;"> <!-- LATIN SMALL LETTER G WITH ACUTE -->
        entityMap.put(new Character('\u011f'),"gbreve"); //<!ENTITY gbreve	"&#x011F;"> <!-- LATIN SMALL LETTER G WITH BREVE -->
        entityMap.put(new Character('\u011e'),"Gbreve"); //<!ENTITY Gbreve	"&#x011E;"> <!-- LATIN CAPITAL LETTER G WITH BREVE -->
        entityMap.put(new Character('\u0122'),"Gcedil"); //<!ENTITY Gcedil	"&#x0122;"> <!-- LATIN CAPITAL LETTER G WITH CEDILLA -->
        entityMap.put(new Character('\u011d'),"gcirc"); //<!ENTITY gcirc	"&#x011D;"> <!-- LATIN SMALL LETTER G WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u011c'),"Gcirc"); //<!ENTITY Gcirc	"&#x011C;"> <!-- LATIN CAPITAL LETTER G WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0121'),"gdot"); //<!ENTITY gdot	"&#x0121;"> <!-- LATIN SMALL LETTER G WITH DOT ABOVE -->
        entityMap.put(new Character('\u0120'),"Gdot"); //<!ENTITY Gdot	"&#x0120;"> <!-- LATIN CAPITAL LETTER G WITH DOT ABOVE -->
        entityMap.put(new Character('\u0125'),"hcirc"); //<!ENTITY hcirc	"&#x0125;"> <!-- LATIN SMALL LETTER H WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0124'),"Hcirc"); //<!ENTITY Hcirc	"&#x0124;"> <!-- LATIN CAPITAL LETTER H WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0127'),"hstrok"); //<!ENTITY hstrok	"&#x0127;"> <!-- LATIN SMALL LETTER H WITH STROKE -->
        entityMap.put(new Character('\u0126'),"Hstrok"); //<!ENTITY Hstrok	"&#x0126;"> <!-- LATIN CAPITAL LETTER H WITH STROKE -->
        entityMap.put(new Character('\u0130'),"Idot"); //<!ENTITY Idot	"&#x0130;"> <!-- LATIN CAPITAL LETTER I WITH DOT ABOVE -->
        entityMap.put(new Character('\u012a'),"Imacr"); //<!ENTITY Imacr	"&#x012A;"> <!-- LATIN CAPITAL LETTER I WITH MACRON -->
        entityMap.put(new Character('\u012b'),"imacr"); //<!ENTITY imacr	"&#x012B;"> <!-- LATIN SMALL LETTER I WITH MACRON -->
        entityMap.put(new Character('\u0133'),"ijlig"); //<!ENTITY ijlig	"&#x0133;"> <!-- LATIN SMALL LIGATURE IJ -->
        entityMap.put(new Character('\u0132'),"IJlig"); //<!ENTITY IJlig	"&#x0132;"> <!-- LATIN CAPITAL LIGATURE IJ -->
        entityMap.put(new Character('\u012f'),"iogon"); //<!ENTITY iogon	"&#x012F;"> <!-- LATIN SMALL LETTER I WITH OGONEK -->
        entityMap.put(new Character('\u012e'),"Iogon"); //<!ENTITY Iogon	"&#x012E;"> <!-- LATIN CAPITAL LETTER I WITH OGONEK -->
        entityMap.put(new Character('\u0129'),"itilde"); //<!ENTITY itilde	"&#x0129;"> <!-- LATIN SMALL LETTER I WITH TILDE -->
        entityMap.put(new Character('\u0128'),"Itilde"); //<!ENTITY Itilde	"&#x0128;"> <!-- LATIN CAPITAL LETTER I WITH TILDE -->
        entityMap.put(new Character('\u0135'),"jcirc"); //<!ENTITY jcirc	"&#x0135;"> <!-- LATIN SMALL LETTER J WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0134'),"Jcirc"); //<!ENTITY Jcirc	"&#x0134;"> <!-- LATIN CAPITAL LETTER J WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0137'),"kcedil"); //<!ENTITY kcedil	"&#x0137;"> <!-- LATIN SMALL LETTER K WITH CEDILLA -->
        entityMap.put(new Character('\u0136'),"Kcedil"); //<!ENTITY Kcedil	"&#x0136;"> <!-- LATIN CAPITAL LETTER K WITH CEDILLA -->
        entityMap.put(new Character('\u0138'),"kgreen"); //<!ENTITY kgreen	"&#x0138;"> <!-- LATIN SMALL LETTER KRA -->
        entityMap.put(new Character('\u013a'),"lacute"); //<!ENTITY lacute	"&#x013A;"> <!-- LATIN SMALL LETTER L WITH ACUTE -->
        entityMap.put(new Character('\u0139'),"Lacute"); //<!ENTITY Lacute	"&#x0139;"> <!-- LATIN CAPITAL LETTER L WITH ACUTE -->
        entityMap.put(new Character('\u013e'),"lcaron"); //<!ENTITY lcaron	"&#x013E;"> <!-- LATIN SMALL LETTER L WITH CARON -->
        entityMap.put(new Character('\u013d'),"Lcaron"); //<!ENTITY Lcaron	"&#x013D;"> <!-- LATIN CAPITAL LETTER L WITH CARON -->
        entityMap.put(new Character('\u013c'),"lcedil"); //<!ENTITY lcedil	"&#x013C;"> <!-- LATIN SMALL LETTER L WITH CEDILLA -->
        entityMap.put(new Character('\u013b'),"Lcedil"); //<!ENTITY Lcedil	"&#x013B;"> <!-- LATIN CAPITAL LETTER L WITH CEDILLA -->
        entityMap.put(new Character('\u0140'),"lmidot"); //<!ENTITY lmidot	"&#x0140;"> <!-- LATIN SMALL LETTER L WITH MIDDLE DOT -->
        entityMap.put(new Character('\u013f'),"Lmidot"); //<!ENTITY Lmidot	"&#x013F;"> <!-- LATIN CAPITAL LETTER L WITH MIDDLE DOT -->
        entityMap.put(new Character('\u0142'),"lstrok"); //<!ENTITY lstrok	"&#x0142;"> <!-- LATIN SMALL LETTER L WITH STROKE -->
        entityMap.put(new Character('\u0141'),"Lstrok"); //<!ENTITY Lstrok	"&#x0141;"> <!-- LATIN CAPITAL LETTER L WITH STROKE -->
        entityMap.put(new Character('\u0144'),"nacute"); //<!ENTITY nacute	"&#x0144;"> <!-- LATIN SMALL LETTER N WITH ACUTE -->
        entityMap.put(new Character('\u0143'),"Nacute"); //<!ENTITY Nacute	"&#x0143;"> <!-- LATIN CAPITAL LETTER N WITH ACUTE -->
        entityMap.put(new Character('\u014b'),"eng"); //<!ENTITY eng	"&#x014B;"> <!-- LATIN SMALL LETTER ENG -->
        entityMap.put(new Character('\u014a'),"ENG"); //<!ENTITY ENG	"&#x014A;"> <!-- LATIN CAPITAL LETTER ENG -->
        entityMap.put(new Character('\u0149'),"napos"); //<!ENTITY napos	"&#x0149;"> <!-- LATIN SMALL LETTER N PRECEDED BY APOSTROPHE -->
        entityMap.put(new Character('\u0148'),"ncaron"); //<!ENTITY ncaron	"&#x0148;"> <!-- LATIN SMALL LETTER N WITH CARON -->
        entityMap.put(new Character('\u0147'),"Ncaron"); //<!ENTITY Ncaron	"&#x0147;"> <!-- LATIN CAPITAL LETTER N WITH CARON -->
        entityMap.put(new Character('\u0146'),"ncedil"); //<!ENTITY ncedil	"&#x0146;"> <!-- LATIN SMALL LETTER N WITH CEDILLA -->
        entityMap.put(new Character('\u0145'),"Ncedil"); //<!ENTITY Ncedil	"&#x0145;"> <!-- LATIN CAPITAL LETTER N WITH CEDILLA -->
        entityMap.put(new Character('\u0151'),"odblac"); //<!ENTITY odblac	"&#x0151;"> <!-- LATIN SMALL LETTER O WITH DOUBLE ACUTE -->
        entityMap.put(new Character('\u0150'),"Odblac"); //<!ENTITY Odblac	"&#x0150;"> <!-- LATIN CAPITAL LETTER O WITH DOUBLE ACUTE -->
        entityMap.put(new Character('\u014c'),"Omacr"); //<!ENTITY Omacr	"&#x014C;"> <!-- LATIN CAPITAL LETTER O WITH MACRON -->
        entityMap.put(new Character('\u014d'),"omacr"); //<!ENTITY omacr	"&#x014D;"> <!-- LATIN SMALL LETTER O WITH MACRON -->
        entityMap.put(new Character('\u0155'),"racute"); //<!ENTITY racute	"&#x0155;"> <!-- LATIN SMALL LETTER R WITH ACUTE -->
        entityMap.put(new Character('\u0154'),"Racute"); //<!ENTITY Racute	"&#x0154;"> <!-- LATIN CAPITAL LETTER R WITH ACUTE -->
        entityMap.put(new Character('\u0159'),"rcaron"); //<!ENTITY rcaron	"&#x0159;"> <!-- LATIN SMALL LETTER R WITH CARON -->
        entityMap.put(new Character('\u0158'),"Rcaron"); //<!ENTITY Rcaron	"&#x0158;"> <!-- LATIN CAPITAL LETTER R WITH CARON -->
        entityMap.put(new Character('\u0157'),"rcedil"); //<!ENTITY rcedil	"&#x0157;"> <!-- LATIN SMALL LETTER R WITH CEDILLA -->
        entityMap.put(new Character('\u0156'),"Rcedil"); //<!ENTITY Rcedil	"&#x0156;"> <!-- LATIN CAPITAL LETTER R WITH CEDILLA -->
        entityMap.put(new Character('\u015b'),"sacute"); //<!ENTITY sacute	"&#x015B;"> <!-- LATIN SMALL LETTER S WITH ACUTE -->
        entityMap.put(new Character('\u015a'),"Sacute"); //<!ENTITY Sacute	"&#x015A;"> <!-- LATIN CAPITAL LETTER S WITH ACUTE -->
        entityMap.put(new Character('\u015f'),"scedil"); //<!ENTITY scedil	"&#x015F;"> <!-- LATIN SMALL LETTER S WITH CEDILLA -->
        entityMap.put(new Character('\u015e'),"Scedil"); //<!ENTITY Scedil	"&#x015E;"> <!-- LATIN CAPITAL LETTER S WITH CEDILLA -->
        entityMap.put(new Character('\u015d'),"scirc"); //<!ENTITY scirc	"&#x015D;"> <!-- LATIN SMALL LETTER S WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u015c'),"Scirc"); //<!ENTITY Scirc	"&#x015C;"> <!-- LATIN CAPITAL LETTER S WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0165'),"tcaron"); //<!ENTITY tcaron	"&#x0165;"> <!-- LATIN SMALL LETTER T WITH CARON -->
        entityMap.put(new Character('\u0164'),"Tcaron"); //<!ENTITY Tcaron	"&#x0164;"> <!-- LATIN CAPITAL LETTER T WITH CARON -->
        entityMap.put(new Character('\u0163'),"tcedil"); //<!ENTITY tcedil	"&#x0163;"> <!-- LATIN SMALL LETTER T WITH CEDILLA -->
        entityMap.put(new Character('\u0162'),"Tcedil"); //<!ENTITY Tcedil	"&#x0162;"> <!-- LATIN CAPITAL LETTER T WITH CEDILLA -->
        entityMap.put(new Character('\u0167'),"tstrok"); //<!ENTITY tstrok	"&#x0167;"> <!-- LATIN SMALL LETTER T WITH STROKE -->
        entityMap.put(new Character('\u0166'),"Tstrok"); //<!ENTITY Tstrok	"&#x0166;"> <!-- LATIN CAPITAL LETTER T WITH STROKE -->
        entityMap.put(new Character('\u016d'),"ubreve"); //<!ENTITY ubreve	"&#x016D;"> <!-- LATIN SMALL LETTER U WITH BREVE -->
        entityMap.put(new Character('\u016c'),"Ubreve"); //<!ENTITY Ubreve	"&#x016C;"> <!-- LATIN CAPITAL LETTER U WITH BREVE -->
        entityMap.put(new Character('\u0171'),"udblac"); //<!ENTITY udblac	"&#x0171;"> <!-- LATIN SMALL LETTER U WITH DOUBLE ACUTE -->
        entityMap.put(new Character('\u0170'),"Udblac"); //<!ENTITY Udblac	"&#x0170;"> <!-- LATIN CAPITAL LETTER U WITH DOUBLE ACUTE -->
        entityMap.put(new Character('\u016b'),"umacr"); //<!ENTITY umacr	"&#x016B;"> <!-- LATIN SMALL LETTER U WITH MACRON -->
        entityMap.put(new Character('\u016a'),"Umacr"); //<!ENTITY Umacr	"&#x016A;"> <!-- LATIN CAPITAL LETTER U WITH MACRON -->
        entityMap.put(new Character('\u0173'),"uogon"); //<!ENTITY uogon	"&#x0173;"> <!-- LATIN SMALL LETTER U WITH OGONEK -->
        entityMap.put(new Character('\u0172'),"Uogon"); //<!ENTITY Uogon	"&#x0172;"> <!-- LATIN CAPITAL LETTER U WITH OGONEK -->
        entityMap.put(new Character('\u016f'),"uring"); //<!ENTITY uring	"&#x016F;"> <!-- LATIN SMALL LETTER U WITH RING ABOVE -->
        entityMap.put(new Character('\u016e'),"Uring"); //<!ENTITY Uring	"&#x016E;"> <!-- LATIN CAPITAL LETTER U WITH RING ABOVE -->
        entityMap.put(new Character('\u0169'),"utilde"); //<!ENTITY utilde	"&#x0169;"> <!-- LATIN SMALL LETTER U WITH TILDE -->
        entityMap.put(new Character('\u0168'),"Utilde"); //<!ENTITY Utilde	"&#x0168;"> <!-- LATIN CAPITAL LETTER U WITH TILDE -->
        entityMap.put(new Character('\u0175'),"wcirc"); //<!ENTITY wcirc	"&#x0175;"> <!-- LATIN SMALL LETTER W WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0174'),"Wcirc"); //<!ENTITY Wcirc	"&#x0174;"> <!-- LATIN CAPITAL LETTER W WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0177'),"ycirc"); //<!ENTITY ycirc	"&#x0177;"> <!-- LATIN SMALL LETTER Y WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0176'),"Ycirc"); //<!ENTITY Ycirc	"&#x0176;"> <!-- LATIN CAPITAL LETTER Y WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u017a'),"zacute"); //<!ENTITY zacute	"&#x017A;"> <!-- LATIN SMALL LETTER Z WITH ACUTE -->
        entityMap.put(new Character('\u0179'),"Zacute"); //<!ENTITY Zacute	"&#x0179;"> <!-- LATIN CAPITAL LETTER Z WITH ACUTE -->
        entityMap.put(new Character('\u017e'),"zcaron"); //<!ENTITY zcaron	"&#x017E;"> <!-- LATIN SMALL LETTER Z WITH CARON -->
        entityMap.put(new Character('\u017d'),"Zcaron"); //<!ENTITY Zcaron	"&#x017D;"> <!-- LATIN CAPITAL LETTER Z WITH CARON -->
        entityMap.put(new Character('\u017c'),"zdot"); //<!ENTITY zdot	"&#x017C;"> <!-- LATIN SMALL LETTER Z WITH DOT ABOVE -->
        entityMap.put(new Character('\u017b'),"Zdot"); //<!ENTITY Zdot	"&#x017B;"> <!-- LATIN CAPITAL LETTER Z WITH DOT ABOVE -->
 
        //entities from iso-num.ent
        //???entityMap.put(new Character('\u00bd'),"half"); //<!ENTITY half	"&#x00BD;"> <!-- VULGAR FRACTION ONE HALF -->
        entityMap.put(new Character('\u215b'),"frac18"); //<!ENTITY frac18	"&#x215B;"> <!--  -->
        entityMap.put(new Character('\u215c'),"frac38"); //<!ENTITY frac38	"&#x215C;"> <!--  -->
        entityMap.put(new Character('\u215d'),"frac58"); //<!ENTITY frac58	"&#x215D;"> <!--  -->
        entityMap.put(new Character('\u215e'),"frac78"); //<!ENTITY frac78	"&#x215E;"> <!--  -->
        entityMap.put(new Character('\u002b'),"plus"); //<!ENTITY plus	"&#x002B;"> <!-- PLUS SIGN -->
        entityMap.put(new Character('\u003d'),"equals"); //<!ENTITY equals	"&#x003D;"> <!-- EQUALS SIGN -->
        entityMap.put(new Character('\u0024'),"dollar"); //<!ENTITY dollar	"&#x0024;"> <!-- DOLLAR SIGN -->
        entityMap.put(new Character('\u0023'),"num"); //<!ENTITY num	"&#x0023;"> <!-- NUMBER SIGN -->
        entityMap.put(new Character('\u0025'),"percnt"); //<!ENTITY percnt	"&#x0025;"> <!-- PERCENT SIGN -->
        entityMap.put(new Character('\u005b'),"lsqb"); //<!ENTITY lsqb	"&#x005B;"> <!-- LEFT SQUARE BRACKET -->
        entityMap.put(new Character('\u005d'),"rsqb"); //<!ENTITY rsqb	"&#x005D;"> <!-- RIGHT SQUARE BRACKET -->
        entityMap.put(new Character('\u007b'),"lcub"); //<!ENTITY lcub	"&#x007B;"> <!-- LEFT CURLY BRACKET -->
        entityMap.put(new Character('\u2015'),"horbar"); //<!ENTITY horbar	"&#x2015;"> <!-- HORIZONTAL BAR -->
        entityMap.put(new Character('\u007c'),"verbar"); //<!ENTITY verbar	"&#x007C;"> <!-- VERTICAL LINE -->
        entityMap.put(new Character('\u007d'),"rcub"); //<!ENTITY rcub	"&#x007D;"> <!-- RIGHT CURLY BRACKET -->
        entityMap.put(new Character('\u2126'),"ohm"); //<!ENTITY ohm	"&#x2126;"> <!-- OHM SIGN -->
        entityMap.put(new Character('\u2669'),"sung"); //<!ENTITY sung	"&#x2669;"> <!--  -->
        entityMap.put(new Character('\u0021'),"excl"); //<!ENTITY excl	"&#x0021;"> <!-- EXCLAMATION MARK -->
        entityMap.put(new Character('\u0022'),"quot"); //<!ENTITY quot	"&#x0022;"> <!-- QUOTATION MARK -->
        entityMap.put(new Character('\u0027'),"apos"); //<!ENTITY apos	"&#x0027;"> <!-- APOSTROPHE -->
        entityMap.put(new Character('\u0028'),"lpar"); //<!ENTITY lpar	"&#x0028;"> <!-- LEFT PARENTHESIS -->
        entityMap.put(new Character('\u0029'),"rpar"); //<!ENTITY rpar	"&#x0029;"> <!-- RIGHT PARENTHESIS -->
        entityMap.put(new Character('\u002c'),"comma"); //<!ENTITY comma	"&#x002C;"> <!-- COMMA -->
        entityMap.put(new Character('\u002d'),"hyphen"); //<!ENTITY hyphen	"&#x002D;"> <!-- HYPHEN-MINUS -->
        entityMap.put(new Character('\u002e'),"period"); //<!ENTITY period	"&#x002E;"> <!-- FULL STOP -->
        entityMap.put(new Character('\u002f'),"sol"); //<!ENTITY sol	"&#x002F;"> <!-- SOLIDUS -->
        entityMap.put(new Character('\u003a'),"colon"); //<!ENTITY colon	"&#x003A;"> <!-- COLON -->
        entityMap.put(new Character('\u003b'),"semi"); //<!ENTITY semi	"&#x003B;"> <!-- SEMICOLON -->
        entityMap.put(new Character('\u003f'),"quest"); //<!ENTITY quest	"&#x003F;"> <!-- QUESTION MARK -->
 
        //entities from iso-pub.ent
        entityMap.put(new Character('\u2004'),"emsp13"); //<!ENTITY emsp13	"&#x2004;"> <!-- THREE-PER-EM SPACE -->
        entityMap.put(new Character('\u2005'),"emsp14"); //<!ENTITY emsp14	"&#x2005;"> <!-- FOUR-PER-EM SPACE -->
        entityMap.put(new Character('\u2007'),"numsp"); //<!ENTITY numsp	"&#x2007;"> <!-- FIGURE SPACE -->
        entityMap.put(new Character('\u2008'),"puncsp"); //<!ENTITY puncsp	"&#x2008;"> <!-- PUNCTUATION SPACE -->
        entityMap.put(new Character('\u200a'),"hairsp"); //<!ENTITY hairsp	"&#x200A;"> <!-- HAIR SPACE -->
        entityMap.put(new Character('\u2423'),"blank"); //<!ENTITY blank	"&#x2423;"> <!-- OPEN BOX -->
        entityMap.put(new Character('\u2025'),"nldr"); //<!ENTITY nldr	"&#x2025;"> <!-- TWO DOT LEADER -->
        entityMap.put(new Character('\u2154'),"frac23"); //<!ENTITY frac23	"&#x2154;"> <!-- VULGAR FRACTION TWO THIRDS -->
        entityMap.put(new Character('\u2156'),"frac25"); //<!ENTITY frac25	"&#x2156;"> <!-- VULGAR FRACTION TWO FIFTHS -->
        entityMap.put(new Character('\u2157'),"frac35"); //<!ENTITY frac35	"&#x2157;"> <!-- VULGAR FRACTION THREE FIFTHS -->
        entityMap.put(new Character('\u2158'),"frac45"); //<!ENTITY frac45	"&#x2158;"> <!-- VULGAR FRACTION FOUR FIFTHS -->
        entityMap.put(new Character('\u2159'),"frac16"); //<!ENTITY frac16	"&#x2159;"> <!-- VULGAR FRACTION ONE SIXTH -->
        entityMap.put(new Character('\u215a'),"frac56"); //<!ENTITY frac56	"&#x215A;"> <!-- VULGAR FRACTION FIVE SIXTHS -->
        entityMap.put(new Character('\u2105'),"incare"); //<!ENTITY incare	"&#x2105;"> <!-- CARE OF -->
        entityMap.put(new Character('\u2588'),"block"); //<!ENTITY block	"&#x2588;"> <!-- FULL BLOCK -->
        entityMap.put(new Character('\u2580'),"uhblk"); //<!ENTITY uhblk	"&#x2580;"> <!-- UPPER HALF BLOCK -->
        entityMap.put(new Character('\u2584'),"lhblk"); //<!ENTITY lhblk	"&#x2584;"> <!-- LOWER HALF BLOCK -->
        entityMap.put(new Character('\u2591'),"blk14"); //<!ENTITY blk14	"&#x2591;"> <!-- LIGHT SHADE -->
        entityMap.put(new Character('\u2592'),"blk12"); //<!ENTITY blk12	"&#x2592;"> <!-- MEDIUM SHADE -->
        entityMap.put(new Character('\u2593'),"blk34"); //<!ENTITY blk34	"&#x2593;"> <!-- DARK SHADE -->
        entityMap.put(new Character('\u25ae'),"marker"); //<!ENTITY marker	"&#x25AE;"> <!-- BLACK VERTICAL RECTANGLE -->
        //???entityMap.put(new Character('\u25cb'),"cir"); //<!ENTITY cir	"&#x25CB;"> <!-- WHITE CIRCLE -->
        entityMap.put(new Character('\u25a1'),"squ"); //<!ENTITY squ	"&#x25A1;"> <!-- WHITE SQUARE -->
        entityMap.put(new Character('\u25ad'),"rect"); //<!ENTITY rect	"&#x25AD;"> <!-- WHITE RECTANGLE -->
        entityMap.put(new Character('\u25b5'),"utri"); //<!ENTITY utri	"&#x25B5;"> <!-- WHITE UP-POINTING TRIANGLE -->
        entityMap.put(new Character('\u25bf'),"dtri"); //<!ENTITY dtri	"&#x25BF;"> <!-- WHITE DOWN-POINTING TRIANGLE -->
        //???entityMap.put(new Character('\u22c6'),"star"); //<!ENTITY star	"&#x22C6;"> <!-- STAR OPERATOR -->
        entityMap.put(new Character('\u25aa'),"squf"); //<!ENTITY squf	"&#x25AA;"> <!--  -->
        entityMap.put(new Character('\u25b4'),"utrif"); //<!ENTITY utrif	"&#x25B4;"> <!-- BLACK UP-POINTING TRIANGLE -->
        entityMap.put(new Character('\u25be'),"dtrif"); //<!ENTITY dtrif	"&#x25BE;"> <!-- BLACK DOWN-POINTING TRIANGLE -->
        entityMap.put(new Character('\u25c2'),"ltrif"); //<!ENTITY ltrif	"&#x25C2;"> <!-- BLACK LEFT-POINTING TRIANGLE -->
        entityMap.put(new Character('\u25b8'),"rtrif"); //<!ENTITY rtrif	"&#x25B8;"> <!-- BLACK RIGHT-POINTING TRIANGLE -->
        entityMap.put(new Character('\u2720'),"malt"); //<!ENTITY malt	"&#x2720;"> <!-- MALTESE CROSS -->
        entityMap.put(new Character('\u266f'),"sharp"); //<!ENTITY sharp	"&#x266F;"> <!-- MUSIC SHARP SIGN -->
        entityMap.put(new Character('\u266d'),"flat"); //<!ENTITY flat	"&#x266D;"> <!-- MUSIC FLAT SIGN -->
        entityMap.put(new Character('\u2642'),"male"); //<!ENTITY male	"&#x2642;"> <!-- MALE SIGN -->
        entityMap.put(new Character('\u2640'),"female"); //<!ENTITY female	"&#x2640;"> <!--  -->
        entityMap.put(new Character('\u260e'),"phone"); //<!ENTITY phone	"&#x260E;"> <!-- TELEPHONE SIGN -->
        entityMap.put(new Character('\u2315'),"telrec"); //<!ENTITY telrec	"&#x2315;"> <!-- TELEPHONE RECORDER -->
        entityMap.put(new Character('\u2117'),"copysr"); //<!ENTITY copysr	"&#x2117;"> <!-- SOUND RECORDING COPYRIGHT -->
        entityMap.put(new Character('\u2041'),"caret"); //<!ENTITY caret	"&#x2041;"> <!-- CARET -->
        //???entityMap.put(new Character('\u201a'),"lsquor"); //<!ENTITY lsquor	"&#x201A;"> <!-- SINGLE LOW-9 QUOTATION MARK -->
        entityMap.put(new Character('\ufb00'),"fflig"); //<!ENTITY fflig	"&#xFB00;"> <!--  -->
        entityMap.put(new Character('\ufb01'),"filig"); //<!ENTITY filig	"&#xFB01;"> <!--  -->
        //XXXentityMap.put(new Character(' '),"fjlig"); //<!--     fjlig	Unknown unicode character -->
        entityMap.put(new Character('\ufb03'),"ffilig"); //<!ENTITY ffilig	"&#xFB03;"> <!--  -->
        entityMap.put(new Character('\ufb04'),"ffllig"); //<!ENTITY ffllig	"&#xFB04;"> <!--  -->
        entityMap.put(new Character('\ufb02'),"fllig"); //<!ENTITY fllig	"&#xFB02;"> <!--  -->
        //???entityMap.put(new Character('\u2026'),"mldr"); //<!ENTITY mldr	"&#x2026;"> <!-- HORIZONTAL ELLIPSIS -->
        //???entityMap.put(new Character('\u201c'),"rdquor"); //<!ENTITY rdquor	"&#x201C;"> <!--  -->
        //???entityMap.put(new Character('\u2018'),"rsquor"); //<!ENTITY rsquor	"&#x2018;"> <!--  -->
        entityMap.put(new Character('\u22ee'),"vellip"); //<!ENTITY vellip	"&#x22EE;"> <!--  -->
        entityMap.put(new Character('\u2043'),"hybull"); //<!ENTITY hybull	"&#x2043;"> <!-- HYPHEN BULLET -->
        entityMap.put(new Character('\u2726'),"lozf"); //<!ENTITY lozf	"&#x2726;"> <!--  -->
        entityMap.put(new Character('\u25c3'),"ltri"); //<!ENTITY ltri	"&#x25C3;"> <!-- WHITE LEFT-POINTING TRIANGLE -->
        entityMap.put(new Character('\u25b9'),"rtri"); //<!ENTITY rtri	"&#x25B9;"> <!-- WHITE RIGHT-POINTING TRIANGLE -->
        entityMap.put(new Character('\u2605'),"starf"); //<!ENTITY starf	"&#x2605;"> <!-- BLACK STAR -->
        entityMap.put(new Character('\u266e'),"natur"); //<!ENTITY natur	"&#x266E;"> <!-- MUSIC NATURAL SIGN -->
        entityMap.put(new Character('\u211e'),"rx"); //<!ENTITY rx	"&#x211E;"> <!-- PRESCRIPTION TAKE -->
        entityMap.put(new Character('\u2736'),"sext"); //<!ENTITY sext	"&#x2736;"> <!-- SIX POINTED BLACK STAR -->
        entityMap.put(new Character('\u2316'),"target"); //<!ENTITY target	"&#x2316;"> <!-- POSITION INDICATOR -->
        entityMap.put(new Character('\u230d'),"dlcrop"); //<!ENTITY dlcrop	"&#x230D;"> <!-- BOTTOM LEFT CROP -->
        entityMap.put(new Character('\u230c'),"drcrop"); //<!ENTITY drcrop	"&#x230C;"> <!-- BOTTOM RIGHT CROP -->
        entityMap.put(new Character('\u230f'),"ulcrop"); //<!ENTITY ulcrop	"&#x230F;"> <!-- TOP LEFT CROP -->
        entityMap.put(new Character('\u230e'),"urcrop"); //<!ENTITY urcrop	"&#x230E;"> <!-- TOP RIGHT CROP -->
 
        //entities from iso-tech.ent
        //???entityMap.put(new Character('\u2135'),"aleph"); //<!ENTITY aleph	"&#x2135;"> <!-- ALEF SYMBOL -->
        entityMap.put(new Character('\u221f'),"ang90"); //<!ENTITY ang90	"&#x221F;"> <!-- RIGHT ANGLE -->
        entityMap.put(new Character('\u2222'),"angsph"); //<!ENTITY angsph	"&#x2222;"> <!--  -->
        entityMap.put(new Character('\u2235'),"becaus"); //<!ENTITY becaus	"&#x2235;"> <!-- BECAUSE -->
        //???entityMap.put(new Character('\u22a5'),"bottom"); //<!ENTITY bottom	"&#x22A5;"> <!--  -->
        entityMap.put(new Character('\u222e'),"conint"); //<!ENTITY conint	"&#x222E;"> <!--  -->
        //???entityMap.put(new Character('\u21d4'),"iff"); //<!ENTITY iff	"&#x21D4;"> <!-- LEFT RIGHT DOUBLE ARROW -->
        //???entityMap.put(new Character('\u220a'),"isin"); //<!ENTITY isin	"&#x220A;"> <!--  -->
        entityMap.put(new Character('\u3008'),"lang"); //<!ENTITY lang	"&#x3008;"> <!--  -->
        //???entityMap.put(new Character('\u2225'),"par"); //<!ENTITY par	"&#x2225;"> <!-- PARALLEL TO -->
        entityMap.put(new Character('\u3009'),"rang"); //<!ENTITY rang	"&#x3009;"> <!--  -->
        entityMap.put(new Character('\u2243'),"sime"); //<!ENTITY sime	"&#x2243;"> <!--  -->
        //???entityMap.put(new Character('\u25a1'),"square"); //<!ENTITY square	"&#x25A1;"> <!-- WHITE SQUARE -->
        entityMap.put(new Character('\u2016'),"Verbar"); //<!ENTITY Verbar	"&#x2016;"> <!-- DOUBLE VERTICAL LINE -->
        entityMap.put(new Character('\u212b'),"angst"); //<!ENTITY angst	"&#x212B;"> <!-- ANGSTROM SIGN -->
        entityMap.put(new Character('\u212c'),"bernou"); //<!ENTITY bernou	"&#x212C;"> <!-- SCRIPT CAPITAL B -->
        //???entityMap.put(new Character('\u2218'),"compfn"); //<!ENTITY compfn	"&#x2218;"> <!-- RING OPERATOR -->
        //???entityMap.put(new Character('\u00a8'),"Dot"); //<!ENTITY Dot	"&#x00A8;"> <!--  -->
        entityMap.put(new Character('\u20dc'),"DotDot"); //<!ENTITY DotDot	"&#x20DC;"> <!-- COMBINING FOUR DOTS ABOVE -->
        entityMap.put(new Character('\u210b'),"hamilt"); //<!ENTITY hamilt	"&#x210B;"> <!-- SCRIPT CAPITAL H -->
        entityMap.put(new Character('\u2112'),"lagran"); //<!ENTITY lagran	"&#x2112;"> <!-- SCRIPT CAPITAL L -->
        entityMap.put(new Character('\u2134'),"order"); //<!ENTITY order	"&#x2134;"> <!-- SCRIPT SMALL O -->
        entityMap.put(new Character('\u2133'),"phmmat"); //<!ENTITY phmmat	"&#x2133;"> <!-- SCRIPT CAPITAL M -->
        entityMap.put(new Character('\u20db'),"tdot"); //<!ENTITY tdot	"&#x20DB;"> <!-- COMBINING THREE DOTS ABOVE -->
        entityMap.put(new Character('\u2034'),"tprime"); //<!ENTITY tprime	"&#x2034;"> <!-- TRIPLE PRIME -->
        entityMap.put(new Character('\u2259'),"wedgeq"); //<!ENTITY wedgeq	"&#x2259;"> <!-- ESTIMATES -->
 */
        
        //Entities for Latin 2; source: http://www.oasis-open.org/docbook/xmlcharent/0.3/iso-lat2.ent
        entityMap.put(new Character('\u0103'),"abreve");// <!-- LATIN SMALL LETTER A WITH BREVE -->
        entityMap.put(new Character('\u0102'),"Abreve");// <!-- LATIN CAPITAL LETTER A WITH BREVE -->
        entityMap.put(new Character('\u0101'),"amacr");// <!-- LATIN SMALL LETTER A WITH MACRON -->
        entityMap.put(new Character('\u0100'),"Amacr");// <!-- LATIN CAPITAL LETTER A WITH MACRON -->
        entityMap.put(new Character('\u0105'),"aogon");// <!-- LATIN SMALL LETTER A WITH OGONEK -->
        entityMap.put(new Character('\u0104'),"Aogon");// <!-- LATIN CAPITAL LETTER A WITH OGONEK -->
        entityMap.put(new Character('\u0107'),"cacute");// <!-- LATIN SMALL LETTER C WITH ACUTE -->
        entityMap.put(new Character('\u0106'),"Cacute");// <!-- LATIN CAPITAL LETTER C WITH ACUTE -->
        entityMap.put(new Character('\u010D'),"ccaron");// <!-- LATIN SMALL LETTER C WITH CARON -->
        entityMap.put(new Character('\u010C'),"Ccaron");// <!-- LATIN CAPITAL LETTER C WITH CARON -->
        entityMap.put(new Character('\u0109'),"ccirc");// <!-- LATIN SMALL LETTER C WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0108'),"Ccirc");// <!-- LATIN CAPITAL LETTER C WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u010B'),"cdot");// <!-- LATIN SMALL LETTER C WITH DOT ABOVE -->
        entityMap.put(new Character('\u010A'),"Cdot");// <!-- LATIN CAPITAL LETTER C WITH DOT ABOVE -->
        entityMap.put(new Character('\u010F'),"dcaron");// <!-- LATIN SMALL LETTER D WITH CARON -->
        entityMap.put(new Character('\u010E'),"Dcaron");// <!-- LATIN CAPITAL LETTER D WITH CARON -->
        entityMap.put(new Character('\u0111'),"dstrok");// <!-- LATIN SMALL LETTER D WITH STROKE -->
        entityMap.put(new Character('\u0110'),"Dstrok");// <!-- LATIN CAPITAL LETTER D WITH STROKE -->
        entityMap.put(new Character('\u011B'),"ecaron");// <!-- LATIN SMALL LETTER E WITH CARON -->
        entityMap.put(new Character('\u011A'),"Ecaron");// <!-- LATIN CAPITAL LETTER E WITH CARON -->
        entityMap.put(new Character('\u0117'),"edot");// <!-- LATIN SMALL LETTER E WITH DOT ABOVE -->
        entityMap.put(new Character('\u0116'),"Edot");// <!-- LATIN CAPITAL LETTER E WITH DOT ABOVE -->
        entityMap.put(new Character('\u0113'),"emacr");// <!-- LATIN SMALL LETTER E WITH MACRON -->
        entityMap.put(new Character('\u0112'),"Emacr");// <!-- LATIN CAPITAL LETTER E WITH MACRON -->
        entityMap.put(new Character('\u0119'),"eogon");// <!-- LATIN SMALL LETTER E WITH OGONEK -->
        entityMap.put(new Character('\u0118'),"Eogon");// <!-- LATIN CAPITAL LETTER E WITH OGONEK -->
        entityMap.put(new Character('\u01F5'),"gacute");// <!-- LATIN SMALL LETTER G WITH ACUTE -->
        entityMap.put(new Character('\u011F'),"gbreve");// <!-- LATIN SMALL LETTER G WITH BREVE -->
        entityMap.put(new Character('\u011E'),"Gbreve");// <!-- LATIN CAPITAL LETTER G WITH BREVE -->
        entityMap.put(new Character('\u0122'),"Gcedil");// <!-- LATIN CAPITAL LETTER G WITH CEDILLA -->
        entityMap.put(new Character('\u011D'),"gcirc");// <!-- LATIN SMALL LETTER G WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u011C'),"Gcirc");// <!-- LATIN CAPITAL LETTER G WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0121'),"gdot");// <!-- LATIN SMALL LETTER G WITH DOT ABOVE -->
        entityMap.put(new Character('\u0120'),"Gdot");// <!-- LATIN CAPITAL LETTER G WITH DOT ABOVE -->
        entityMap.put(new Character('\u0125'),"hcirc");// <!-- LATIN SMALL LETTER H WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0124'),"Hcirc");// <!-- LATIN CAPITAL LETTER H WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0127'),"hstrok");// <!-- LATIN SMALL LETTER H WITH STROKE -->
        entityMap.put(new Character('\u0126'),"Hstrok");// <!-- LATIN CAPITAL LETTER H WITH STROKE -->
        entityMap.put(new Character('\u0130'),"Idot");// <!-- LATIN CAPITAL LETTER I WITH DOT ABOVE -->
        entityMap.put(new Character('\u012A'),"Imacr");// <!-- LATIN CAPITAL LETTER I WITH MACRON -->
        entityMap.put(new Character('\u012B'),"imacr");// <!-- LATIN SMALL LETTER I WITH MACRON -->
        entityMap.put(new Character('\u0133'),"ijlig");// <!-- LATIN SMALL LIGATURE IJ -->
        entityMap.put(new Character('\u0132'),"IJlig");// <!-- LATIN CAPITAL LIGATURE IJ -->
        entityMap.put(new Character('\u0131'),"inodot");// <!-- LATIN SMALL LETTER DOTLESS I -->
        entityMap.put(new Character('\u012F'),"iogon");// <!-- LATIN SMALL LETTER I WITH OGONEK -->
        entityMap.put(new Character('\u012E'),"Iogon");// <!-- LATIN CAPITAL LETTER I WITH OGONEK -->
        entityMap.put(new Character('\u0129'),"itilde");// <!-- LATIN SMALL LETTER I WITH TILDE -->
        entityMap.put(new Character('\u0128'),"Itilde");// <!-- LATIN CAPITAL LETTER I WITH TILDE -->
        entityMap.put(new Character('\u0135'),"jcirc");// <!-- LATIN SMALL LETTER J WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0134'),"Jcirc");// <!-- LATIN CAPITAL LETTER J WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0137'),"kcedil");// <!-- LATIN SMALL LETTER K WITH CEDILLA -->
        entityMap.put(new Character('\u0136'),"Kcedil");// <!-- LATIN CAPITAL LETTER K WITH CEDILLA -->
        entityMap.put(new Character('\u0138'),"kgreen");// <!-- LATIN SMALL LETTER KRA -->
        entityMap.put(new Character('\u013A'),"lacute");// <!-- LATIN SMALL LETTER L WITH ACUTE -->
        entityMap.put(new Character('\u0139'),"Lacute");// <!-- LATIN CAPITAL LETTER L WITH ACUTE -->
        entityMap.put(new Character('\u013E'),"lcaron");// <!-- LATIN SMALL LETTER L WITH CARON -->
        entityMap.put(new Character('\u013D'),"Lcaron");// <!-- LATIN CAPITAL LETTER L WITH CARON -->
        entityMap.put(new Character('\u013C'),"lcedil");// <!-- LATIN SMALL LETTER L WITH CEDILLA -->
        entityMap.put(new Character('\u013B'),"Lcedil");// <!-- LATIN CAPITAL LETTER L WITH CEDILLA -->
        entityMap.put(new Character('\u0140'),"lmidot");// <!-- LATIN SMALL LETTER L WITH MIDDLE DOT -->
        entityMap.put(new Character('\u013F'),"Lmidot");// <!-- LATIN CAPITAL LETTER L WITH MIDDLE DOT -->
        entityMap.put(new Character('\u0142'),"lstrok");// <!-- LATIN SMALL LETTER L WITH STROKE -->
        entityMap.put(new Character('\u0141'),"Lstrok");// <!-- LATIN CAPITAL LETTER L WITH STROKE -->
        entityMap.put(new Character('\u0144'),"nacute");// <!-- LATIN SMALL LETTER N WITH ACUTE -->
        entityMap.put(new Character('\u0143'),"Nacute");// <!-- LATIN CAPITAL LETTER N WITH ACUTE -->
        entityMap.put(new Character('\u014B'),"eng");// <!-- LATIN SMALL LETTER ENG -->
        entityMap.put(new Character('\u014A'),"ENG");// <!-- LATIN CAPITAL LETTER ENG -->
        entityMap.put(new Character('\u0149'),"napos");// <!-- LATIN SMALL LETTER N PRECEDED BY APOSTROPHE -->
        entityMap.put(new Character('\u0148'),"ncaron");// <!-- LATIN SMALL LETTER N WITH CARON -->
        entityMap.put(new Character('\u0147'),"Ncaron");// <!-- LATIN CAPITAL LETTER N WITH CARON -->
        entityMap.put(new Character('\u0146'),"ncedil");// <!-- LATIN SMALL LETTER N WITH CEDILLA -->
        entityMap.put(new Character('\u0145'),"Ncedil");// <!-- LATIN CAPITAL LETTER N WITH CEDILLA -->
        entityMap.put(new Character('\u0151'),"odblac");// <!-- LATIN SMALL LETTER O WITH DOUBLE ACUTE -->
        entityMap.put(new Character('\u0150'),"Odblac");// <!-- LATIN CAPITAL LETTER O WITH DOUBLE ACUTE -->
        entityMap.put(new Character('\u014C'),"Omacr");// <!-- LATIN CAPITAL LETTER O WITH MACRON -->
        entityMap.put(new Character('\u014D'),"omacr");// <!-- LATIN SMALL LETTER O WITH MACRON -->
        entityMap.put(new Character('\u0153'),"oelig");// <!-- LATIN SMALL LIGATURE OE -->
        entityMap.put(new Character('\u0152'),"OElig");// <!-- LATIN CAPITAL LIGATURE OE -->
        entityMap.put(new Character('\u0155'),"racute");// <!-- LATIN SMALL LETTER R WITH ACUTE -->
        entityMap.put(new Character('\u0154'),"Racute");// <!-- LATIN CAPITAL LETTER R WITH ACUTE -->
        entityMap.put(new Character('\u0159'),"rcaron");// <!-- LATIN SMALL LETTER R WITH CARON -->
        entityMap.put(new Character('\u0158'),"Rcaron");// <!-- LATIN CAPITAL LETTER R WITH CARON -->
        entityMap.put(new Character('\u0157'),"rcedil");// <!-- LATIN SMALL LETTER R WITH CEDILLA -->
        entityMap.put(new Character('\u0156'),"Rcedil");// <!-- LATIN CAPITAL LETTER R WITH CEDILLA -->
        entityMap.put(new Character('\u015B'),"sacute");// <!-- LATIN SMALL LETTER S WITH ACUTE -->
        entityMap.put(new Character('\u015A'),"Sacute");// <!-- LATIN CAPITAL LETTER S WITH ACUTE -->
        entityMap.put(new Character('\u0161'),"scaron");// <!-- LATIN SMALL LETTER S WITH CARON -->
        entityMap.put(new Character('\u0160'),"Scaron");// <!-- LATIN CAPITAL LETTER S WITH CARON -->
        entityMap.put(new Character('\u015F'),"scedil");// <!-- LATIN SMALL LETTER S WITH CEDILLA -->
        entityMap.put(new Character('\u015E'),"Scedil");// <!-- LATIN CAPITAL LETTER S WITH CEDILLA -->
        entityMap.put(new Character('\u015D'),"scirc");// <!-- LATIN SMALL LETTER S WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u015C'),"Scirc");// <!-- LATIN CAPITAL LETTER S WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0165'),"tcaron");// <!-- LATIN SMALL LETTER T WITH CARON -->
        entityMap.put(new Character('\u0164'),"Tcaron");// <!-- LATIN CAPITAL LETTER T WITH CARON -->
        entityMap.put(new Character('\u0163'),"tcedil");// <!-- LATIN SMALL LETTER T WITH CEDILLA -->
        entityMap.put(new Character('\u0162'),"Tcedil");// <!-- LATIN CAPITAL LETTER T WITH CEDILLA -->
        entityMap.put(new Character('\u0167'),"tstrok");// <!-- LATIN SMALL LETTER T WITH STROKE -->
        entityMap.put(new Character('\u0166'),"Tstrok");// <!-- LATIN CAPITAL LETTER T WITH STROKE -->
        entityMap.put(new Character('\u016D'),"ubreve");// <!-- LATIN SMALL LETTER U WITH BREVE -->
        entityMap.put(new Character('\u016C'),"Ubreve");// <!-- LATIN CAPITAL LETTER U WITH BREVE -->
        entityMap.put(new Character('\u0171'),"udblac");// <!-- LATIN SMALL LETTER U WITH DOUBLE ACUTE -->
        entityMap.put(new Character('\u0170'),"Udblac");// <!-- LATIN CAPITAL LETTER U WITH DOUBLE ACUTE -->
        entityMap.put(new Character('\u016B'),"umacr");// <!-- LATIN SMALL LETTER U WITH MACRON -->
        entityMap.put(new Character('\u016A'),"Umacr");// <!-- LATIN CAPITAL LETTER U WITH MACRON -->
        entityMap.put(new Character('\u0173'),"uogon");// <!-- LATIN SMALL LETTER U WITH OGONEK -->
        entityMap.put(new Character('\u0172'),"Uogon");// <!-- LATIN CAPITAL LETTER U WITH OGONEK -->
        entityMap.put(new Character('\u016F'),"uring");// <!-- LATIN SMALL LETTER U WITH RING ABOVE -->
        entityMap.put(new Character('\u016E'),"Uring");// <!-- LATIN CAPITAL LETTER U WITH RING ABOVE -->
        entityMap.put(new Character('\u0169'),"utilde");// <!-- LATIN SMALL LETTER U WITH TILDE -->
        entityMap.put(new Character('\u0168'),"Utilde");// <!-- LATIN CAPITAL LETTER U WITH TILDE -->
        entityMap.put(new Character('\u0175'),"wcirc");// <!-- LATIN SMALL LETTER W WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0174'),"Wcirc");// <!-- LATIN CAPITAL LETTER W WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0177'),"ycirc");// <!-- LATIN SMALL LETTER Y WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0176'),"Ycirc");// <!-- LATIN CAPITAL LETTER Y WITH CIRCUMFLEX -->
        entityMap.put(new Character('\u0178'),"Yuml");// <!-- LATIN CAPITAL LETTER Y WITH DIAERESIS -->
        entityMap.put(new Character('\u017A'),"zacute");// <!-- LATIN SMALL LETTER Z WITH ACUTE -->
        entityMap.put(new Character('\u0179'),"Zacute");// <!-- LATIN CAPITAL LETTER Z WITH ACUTE -->
        entityMap.put(new Character('\u017E'),"zcaron");// <!-- LATIN SMALL LETTER Z WITH CARON -->
        entityMap.put(new Character('\u017D'),"Zcaron");// <!-- LATIN CAPITAL LETTER Z WITH CARON -->
        entityMap.put(new Character('\u017C'),"zdot");// <!-- LATIN SMALL LETTER Z WITH DOT ABOVE -->
        entityMap.put(new Character('\u017B'),"Zdot");// <!-- LATIN CAPITAL LETTER Z WITH DOT ABOVE -->
        
        //entities for Russian cyrilic source: http://www.oasis-open.org/docbook/xmlcharent/0.3/iso-cyr1.ent
        entityMap.put(new Character('\u0430'),"acy");	// <!-- CYRILLIC SMALL LETTER A -->
        entityMap.put(new Character('\u0410'),"Acy");	// <!-- CYRILLIC CAPITAL LETTER A -->
        entityMap.put(new Character('\u0431'),"bcy");	// <!-- CYRILLIC SMALL LETTER BE -->
        entityMap.put(new Character('\u0411'),"Bcy");	// <!-- CYRILLIC CAPITAL LETTER BE -->
        entityMap.put(new Character('\u0432'),"vcy");	// <!-- CYRILLIC SMALL LETTER VE -->
        entityMap.put(new Character('\u0412'),"Vcy");	// <!-- CYRILLIC CAPITAL LETTER VE -->
        entityMap.put(new Character('\u0433'),"gcy");	// <!-- CYRILLIC SMALL LETTER GHE -->
        entityMap.put(new Character('\u0413'),"Gcy");	// <!-- CYRILLIC CAPITAL LETTER GHE -->
        entityMap.put(new Character('\u0434'),"dcy");	// <!-- CYRILLIC SMALL LETTER DE -->
        entityMap.put(new Character('\u0414'),"Dcy");	// <!-- CYRILLIC CAPITAL LETTER DE -->
        entityMap.put(new Character('\u0435'),"iecy");	// <!-- CYRILLIC SMALL LETTER IE -->
        entityMap.put(new Character('\u0415'),"IEcy");	// <!-- CYRILLIC CAPITAL LETTER IE -->
        entityMap.put(new Character('\u0451'),"iocy");	// <!-- CYRILLIC SMALL LETTER IO -->
        entityMap.put(new Character('\u0401'),"IOcy");	// <!-- CYRILLIC CAPITAL LETTER IO -->
        entityMap.put(new Character('\u0436'),"zhcy");	// <!-- CYRILLIC SMALL LETTER ZHE -->
        entityMap.put(new Character('\u0416'),"ZHcy");	// <!-- CYRILLIC CAPITAL LETTER ZHE -->
        entityMap.put(new Character('\u0437'),"zcy");	// <!-- CYRILLIC SMALL LETTER ZE -->
        entityMap.put(new Character('\u0417'),"Zcy");	// <!-- CYRILLIC CAPITAL LETTER ZE -->
        entityMap.put(new Character('\u0438'),"icy");	// <!-- CYRILLIC SMALL LETTER I -->
        entityMap.put(new Character('\u0418'),"Icy");	// <!-- CYRILLIC CAPITAL LETTER I -->
        entityMap.put(new Character('\u0439'),"jcy");	// <!-- CYRILLIC SMALL LETTER SHORT I -->
        entityMap.put(new Character('\u0419'),"Jcy");	// <!-- CYRILLIC CAPITAL LETTER SHORT I -->
        entityMap.put(new Character('\u043A'),"kcy");	// <!-- CYRILLIC SMALL LETTER KA -->
        entityMap.put(new Character('\u041A'),"Kcy");	// <!-- CYRILLIC CAPITAL LETTER KA -->
        entityMap.put(new Character('\u043B'),"lcy");	// <!-- CYRILLIC SMALL LETTER EL -->
        entityMap.put(new Character('\u041B'),"Lcy");	// <!-- CYRILLIC CAPITAL LETTER EL -->
        entityMap.put(new Character('\u043C'),"mcy");	// <!-- CYRILLIC SMALL LETTER EM -->
        entityMap.put(new Character('\u041C'),"Mcy");	// <!-- CYRILLIC CAPITAL LETTER EM -->
        entityMap.put(new Character('\u043D'),"ncy");	// <!-- CYRILLIC SMALL LETTER EN -->
        entityMap.put(new Character('\u041D'),"Ncy");	// <!-- CYRILLIC CAPITAL LETTER EN -->
        entityMap.put(new Character('\u043E'),"ocy");	// <!-- CYRILLIC SMALL LETTER O -->
        entityMap.put(new Character('\u041E'),"Ocy");	// <!-- CYRILLIC CAPITAL LETTER O -->
        entityMap.put(new Character('\u043F'),"pcy");	// <!-- CYRILLIC SMALL LETTER PE -->
        entityMap.put(new Character('\u041F'),"Pcy");	// <!-- CYRILLIC CAPITAL LETTER PE -->
        entityMap.put(new Character('\u0440'),"rcy");	// <!-- CYRILLIC SMALL LETTER ER -->
        entityMap.put(new Character('\u0420'),"Rcy");	// <!-- CYRILLIC CAPITAL LETTER ER -->
        entityMap.put(new Character('\u0441'),"scy");	// <!-- CYRILLIC SMALL LETTER ES -->
        entityMap.put(new Character('\u0421'),"Scy");	// <!-- CYRILLIC CAPITAL LETTER ES -->
        entityMap.put(new Character('\u0442'),"tcy");	// <!-- CYRILLIC SMALL LETTER TE -->
        entityMap.put(new Character('\u0422'),"Tcy");	// <!-- CYRILLIC CAPITAL LETTER TE -->
        entityMap.put(new Character('\u0443'),"ucy");	// <!-- CYRILLIC SMALL LETTER U -->
        entityMap.put(new Character('\u0423'),"Ucy");	// <!-- CYRILLIC CAPITAL LETTER U -->
        entityMap.put(new Character('\u0444'),"fcy");	// <!-- CYRILLIC SMALL LETTER EF -->
        entityMap.put(new Character('\u0424'),"Fcy");	// <!-- CYRILLIC CAPITAL LETTER EF -->
        entityMap.put(new Character('\u0445'),"khcy");	// <!-- CYRILLIC SMALL LETTER HA -->
        entityMap.put(new Character('\u0425'),"KHcy");	// <!-- CYRILLIC CAPITAL LETTER HA -->
        entityMap.put(new Character('\u0446'),"tscy");	// <!-- CYRILLIC SMALL LETTER TSE -->
        entityMap.put(new Character('\u0426'),"TScy");	// <!-- CYRILLIC CAPITAL LETTER TSE -->
        entityMap.put(new Character('\u0447'),"chcy");	// <!-- CYRILLIC SMALL LETTER CHE -->
        entityMap.put(new Character('\u0427'),"CHcy");	// <!-- CYRILLIC CAPITAL LETTER CHE -->
        entityMap.put(new Character('\u0448'),"shcy");	// <!-- CYRILLIC SMALL LETTER SHA -->
        entityMap.put(new Character('\u0428'),"SHcy");	// <!-- CYRILLIC CAPITAL LETTER SHA -->
        entityMap.put(new Character('\u0449'),"shchcy");	// <!-- CYRILLIC SMALL LETTER SHCHA -->
        entityMap.put(new Character('\u0429'),"SHCHcy");	// <!-- CYRILLIC CAPITAL LETTER SHCHA -->
        entityMap.put(new Character('\u044A'),"hardcy");	// <!-- CYRILLIC SMALL LETTER HARD SIGN -->
        entityMap.put(new Character('\u042A'),"HARDcy");	// <!-- CYRILLIC CAPITAL LETTER HARD SIGN -->
        entityMap.put(new Character('\u044B'),"ycy");	// <!-- CYRILLIC SMALL LETTER YERU -->
        entityMap.put(new Character('\u042B'),"Ycy");	// <!-- CYRILLIC CAPITAL LETTER YERU -->
        entityMap.put(new Character('\u044C'),"softcy");	// <!-- CYRILLIC SMALL LETTER SOFT SIGN -->
        entityMap.put(new Character('\u042C'),"SOFTcy");	// <!-- CYRILLIC CAPITAL LETTER SOFT SIGN -->
        entityMap.put(new Character('\u044D'),"ecy");	// <!-- CYRILLIC SMALL LETTER E -->
        entityMap.put(new Character('\u042D'),"Ecy");	// <!-- CYRILLIC CAPITAL LETTER E -->
        entityMap.put(new Character('\u044E'),"yucy");	// <!-- CYRILLIC SMALL LETTER YU -->
        entityMap.put(new Character('\u042E'),"YUcy");	// <!-- CYRILLIC CAPITAL LETTER YU -->
        entityMap.put(new Character('\u044F'),"yacy");	// <!-- CYRILLIC SMALL LETTER YA -->
        entityMap.put(new Character('\u042F'),"YAcy");	// <!-- CYRILLIC CAPITAL LETTER YA -->
        entityMap.put(new Character('\u2116'),"numero");	// <!-- NUMERO SIGN -->
        
        //non-russian cyrilic source: http://www.oasis-open.org/docbook/xmlcharent/0.3/iso-cyr2.ent
        entityMap.put(new Character('\u0403'),"GJcy");	// <!-- CYRILLIC CAPITAL LETTER GJE -->
        entityMap.put(new Character('\u0454'),"jukcy");	// <!-- CYRILLIC SMALL LETTER UKRAINIAN IE -->
        entityMap.put(new Character('\u0404'),"Jukcy");	// <!-- CYRILLIC CAPITAL LETTER UKRAINIAN IE -->
        entityMap.put(new Character('\u0455'),"dscy");	// <!-- CYRILLIC SMALL LETTER DZE -->
        entityMap.put(new Character('\u0405'),"DScy");	// <!-- CYRILLIC CAPITAL LETTER DZE -->
        entityMap.put(new Character('\u0456'),"iukcy");	// <!-- CYRILLIC SMALL LETTER BYELORUSSIAN-UKRAINIAN I -->
        entityMap.put(new Character('\u0406'),"Iukcy");	// <!-- CYRILLIC CAPITAL LETTER BYELORUSSIAN-UKRAINIAN I -->
        entityMap.put(new Character('\u0457'),"yicy");	// <!-- CYRILLIC SMALL LETTER YI -->
        entityMap.put(new Character('\u0407'),"YIcy");	// <!-- CYRILLIC CAPITAL LETTER YI -->
        entityMap.put(new Character('\u0458'),"jsercy");	// <!-- CYRILLIC SMALL LETTER JE -->
        entityMap.put(new Character('\u0408'),"Jsercy");	// <!-- CYRILLIC CAPITAL LETTER JE -->
        entityMap.put(new Character('\u0459'),"ljcy");	// <!-- CYRILLIC SMALL LETTER LJE -->
        entityMap.put(new Character('\u0409'),"LJcy");	// <!-- CYRILLIC CAPITAL LETTER LJE -->
        entityMap.put(new Character('\u045A'),"njcy");	// <!-- CYRILLIC SMALL LETTER NJE -->
        entityMap.put(new Character('\u040A'),"NJcy");	// <!-- CYRILLIC CAPITAL LETTER NJE -->
        entityMap.put(new Character('\u045B'),"tshcy");	// <!-- CYRILLIC SMALL LETTER TSHE -->
        entityMap.put(new Character('\u040B'),"TSHcy");	// <!-- CYRILLIC CAPITAL LETTER TSHE -->
        entityMap.put(new Character('\u045C'),"kjcy");	// <!-- CYRILLIC SMALL LETTER KJE -->
        entityMap.put(new Character('\u040C'),"KJcy");	// <!-- CYRILLIC CAPITAL LETTER KJE -->
        entityMap.put(new Character('\u045E'),"ubrcy");	// <!-- CYRILLIC SMALL LETTER SHORT U -->
        entityMap.put(new Character('\u040E'),"Ubrcy");	// <!-- CYRILLIC CAPITAL LETTER SHORT U -->
        entityMap.put(new Character('\u045F'),"dzcy");	// <!-- CYRILLIC SMALL LETTER DZHE -->
        entityMap.put(new Character('\u040F'),"DZcy");	// <!-- CYRILLIC CAPITAL LETTER DZHE -->
        
    }
    
    public Map getMap(){
        return new HashMap(entityMap);
    }
}

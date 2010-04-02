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

package org.jvnet.olt.xliff_back_converter.format.html;

import org.jvnet.olt.xliff_back_converter.UnicodeReverse;
import java.util.HashMap;

public class HTMLUnicodeReverseImpl implements UnicodeReverse {
	HashMap entityMap;
	
	public HTMLUnicodeReverseImpl() {
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
        	//entityMap.put(new Character('\u00a0'),"nbsp");
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
                // not doing this one, since it's often used in entity decl/doctype headers
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
        	entityMap.put(new Character('\u201c'),"ldquo");
        	entityMap.put(new Character('\u201d'),"rdquo");
        	entityMap.put(new Character('\u201e'),"bdquo");
        	entityMap.put(new Character('\u2020'),"dagger");
        	entityMap.put(new Character('\u2021'),"Dagger");
        	entityMap.put(new Character('\u2030'),"permil");
        	entityMap.put(new Character('\u2039'),"lsaquo");
        	entityMap.put(new Character('\u203a'),"rsaquo");
        	entityMap.put(new Character('\u20ac'),"euro");
	}
}
/*
 * SimpleSentenceHelper.java
 *
 * Created on November 1, 2005, 1:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 *
 * @author boris
 */
public class SimpleSentenceHelper {
    private static final Logger logger = Logger.getLogger(SimpleSentenceHelper.class.getName());
    
 
    /** Creates a new instance of SimpleSentenceHelper */
    public SimpleSentenceHelper() {
    }
    
    static public String formatTranslation(String s,String matchSrc,String matchT){
	return formatTranslation(s,matchSrc,matchT,true);
    }
    
    static public  String formatTranslation(String s, String matchSrc, String matchT,boolean tagUpdate) {
	PivotText newSrc = null;
	PivotText oldSrc = null;
	PivotText oldTarget = null;
	
	if(!tagUpdate || s.equals(matchT))
	    return matchT;
	
	try {
	    //newsrc diff from oldsrc
	    newSrc = new PivotText(s, true);
	    oldSrc = new PivotText(matchSrc, true);
	    oldTarget = new PivotText(matchT, true);
	    
	    
	    
	    
	    if (newSrc.ct.length != 0) { //newSrc has tags.
		
		if (oldSrc.ct.length != 0) { //oldsrc has tags
		    
		    if (oldTarget.ct.length != 0) { //oldtarget has tags
			
			Vector vTemp = compareTags(newSrc.ct, oldSrc.ct);
			String xx = vTemp.toString();
			logger.finest("result1:"+xx);
			Vector vTempLocal = compareTags((ContentTag[])vTemp.toArray(new ContentTag[0]), oldTarget.ct);
			
			xx = vTempLocal.toString();
			logger.finest("result2:"+xx);
			
			String deletedTag = oldTarget.deleteTag();
			
			return constructReturnString((ContentTag[])vTempLocal.toArray(new ContentTag[0]), deletedTag);
			
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
	} catch (Exception ec) {
	    logger.severe("Got exception:"+ec);
	    //throw new RuntimeException(ec);
	} finally {
	    newSrc = null;
	    oldSrc = null;
	    oldTarget = null;
	}
	
	return null;
    }
    
    static String constructReturnString(ContentTag[] oInput, String strMatchT) {
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
    
    
    static Vector compareTags(ContentTag[] src, ContentTag[] target) {
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
   
}
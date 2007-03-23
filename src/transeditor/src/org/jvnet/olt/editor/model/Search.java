/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/**
 * Title:        Alignment editor<p>
 * Description:  part of TMCi editor<p>
 * @version 1.0
 */
package org.jvnet.olt.editor.model;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Search {
    private static final Logger logger = Logger.getLogger(Search.class.getName());
    /**
     * indicates what will be searched
     */
    public String what = null;

    /**
     * indicates if search the source or not
     */
    public boolean srcFlag = true;

    /**
     * indicates if searching is case sensitive or nto
     */
    public boolean caseFlag = false;

    /**
     * indicates if seraching direction is forward or not
     */
    public boolean forwardFlag = true;

    public boolean matchWords = false;
    
    private String origWhat;
    private String origStringToMatch;
    
    private Matcher m;
    /**
     * Constructor
     */
    public Search(String w, boolean c, boolean f) {
        what = w;
        caseFlag = c;
        forwardFlag = f;
    }

    public Search(String w, boolean s, boolean c, boolean f,boolean matchWords) {
        what = w;
        srcFlag = s;
        caseFlag = c;
        forwardFlag = f;
        this.matchWords = matchWords;
    }

    public String toString() {
        return "what:" + what + " srcFlag:" + srcFlag + " caseFlag:" + caseFlag + " forwardFlag:" + forwardFlag;
    }

    public boolean equals(Search s) {
        if (s.caseFlag) {
            return what.equals(s.what);
        } else {
            return what.toLowerCase().equals(s.what.toLowerCase());
        }
    }

    public Object clone() {
        return new Search(what, srcFlag, caseFlag, forwardFlag,matchWords);
    }

    /**
     * Get Methods
     */
    public String getWhat() {
        return what;
    }

    public boolean isSrcFlag() {
        return srcFlag;
    }

    public boolean isCaseFlag() {
        return caseFlag;
    }

    public boolean isForwardFlag() {
        return forwardFlag;
    }

    /**
     * Set Methods
     */
    public void setWhat(String what) {
        this.what = what;
    }

    public void setSrcFlag(boolean srcFlag) {
        this.srcFlag = srcFlag;
    }

    public void setCaseFlag(boolean caseFlag) {
        this.caseFlag = caseFlag;
    }

    public void setForwardFlag(String stringToMatch,boolean forwardFlag) {
        this.forwardFlag = forwardFlag;
    }
    
    public Matcher getMatcher(String stringToMatch){
        if(origWhat == null){
            origWhat = what;
        }
        
        if(origStringToMatch == null)
            origStringToMatch = stringToMatch;
        
        if(origWhat.equals(what) && origStringToMatch.equals(stringToMatch) && m != null){
            return m;
        }
        else{
            m = getPattern().matcher(stringToMatch);
        }
        
        return m;
    }
    
    Pattern getPattern(){
        String px = "\\b"+Pattern.quote(what)+"\\b";
        Pattern rv = null;
        try{
            if(caseFlag)
                rv  = Pattern.compile(px);                
            else
                rv = Pattern.compile(px,Pattern.CASE_INSENSITIVE);
        }
        catch (PatternSyntaxException pse){
            logger.warning("Pattern failed to compile:"+pse);
        }
        return rv;
    }
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

/**
 *  This class removed formatting from strings that don't have
 *  formatting, that is, all the text in the strings are assumed
 *  to be character data. The removeFormatting function is an
 *  identity mapping in this case.
 */
public class PlainTextFormatRemovingStrategy
implements FormatRemovingStrategy
{
    public String removeFormatting(String string)
    throws MiniTMException
    {
        return string;
    }
    
    public java.util.List extractFormatting(String string) throws MiniTMException
    {
        return new java.util.LinkedList();
    }   
}

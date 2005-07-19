/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.filters.csv;

/**
 * A simple class defining static variables to define the type of tokens we accept
 * @author timf
 */
public class CsvTokenTypes {
    
    /** Creates a new instance of CsvTokenTypes */
    public CsvTokenTypes() {
    }
    
    public final static int VALUE = 0;
    public final static int SEPARATOR = 1;
    public final static int NEWLINE = 2;
    
    public final static String[] display = {"VALUE", "SEPARATOR", "NEWLINE"};

}
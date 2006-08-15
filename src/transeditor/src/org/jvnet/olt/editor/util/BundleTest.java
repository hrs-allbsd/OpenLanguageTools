/*
 * Bundle.java
 *
 * Created on June 23, 2006, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.util;

import junit.framework.TestCase;


/**
 *
 * @author boris
 */
public class BundleTest extends TestCase{
    
    public BundleTest(){
        super();
    }
    
    public void testGetStringOK() throws Exception{
        Bundle b  = Bundle.getBundle(BundleTest.class);
        assertEquals("abcd",b.getString("1234"));
    }

    public void testGetMissingString() throws Exception{
        //now we want to get back the key, actually and a warning
        Bundle b  = Bundle.getBundle(BundleTest.class);
        assertEquals("XYZ",b.getString("XYZ"));
    }
}

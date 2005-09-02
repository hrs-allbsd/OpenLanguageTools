/*
 * BackConverterPropertiesTest.java
 *
 * Created on September 1, 2005, 3:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.xliff_back_converter;

import junit.framework.TestCase;

/**
 *
 * @author boris
 */
public class BackConverterPropertiesTest extends TestCase {
    BackConverterProperties props;
    
    /** Creates a new instance of BackConverterPropertiesTest */
    public BackConverterPropertiesTest() {
    }

    
    protected void setUp() throws Exception {
        super.setUp();
        
        props = new BackConverterProperties();
    }
    
    public void testSetProperty() throws Exception{
        props.setProperty("XXX","YYY");
        props.setProperty("ZZZ",true);
        
        assertEquals("YYY",props.getProperty("XXX"));
        assertEquals(true,props.getBooleanProperty("ZZZ"));
        assertEquals("QQQ",props.getProperty("HELLO","QQQ"));
        assertEquals("YYY",props.getProperty("XXX",null));

        assertEquals(false,props.getBooleanProperty("HELLO"));
        assertEquals(true,props.getBooleanProperty("HELLO",true));
    }
    
}

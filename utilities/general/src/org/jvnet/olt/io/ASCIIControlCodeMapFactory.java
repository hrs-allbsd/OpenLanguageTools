
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ASCIIControlCodeMapFactory.java
 *
 * Created on February 13, 2004, 12:55 PM
 */

package org.jvnet.olt.io;

import java.util.*;
/**
 * This class is intended to be used with the EntityConversionFilterReader
 * it contains a single method that returns a map which maps java.lang.Characters
 * to java.lang.Strings - each character being a low-numbered ascii code (typically
 * known as "control characters"), each string these map to is a processing instruction
 * of the form : <br>
 * 
 * <code>&lt;?suntrans2-ascii-character xxxx?&gt;</code><br><br>
 *
 * where the string "xxxx" is a four digit hex string indicating the unicode value
 * of the character used as the key for that entry.
 *
 * This is done, so we can write XLIFF files with low-numbered ascii values in them,
 * which are expressly forbidden by the xml specification.
 *
 * @author  timf
 */
public class ASCIIControlCodeMapFactory {
    
    private static Map asciiMap = null;
    
    /** Creates a new instance of ASCIIControlCodeMapFactory */
    private static void init () {
        if (asciiMap == null){
            HashMap map = new HashMap();
        /* These ascii codes are taken from
         * http://www.cs.tut.fi/~jkorpela/chars/c0.html
         */
            
            // wrap Ctrl-A (Start of Heading)
            map.put(new Character('\u0001'), "<?suntrans2-ascii-character 0001?>");
            // wrap Ctrl-B (Start of Text)
            map.put(new Character('\u0002'), "<?suntrans2-ascii-character 0002?>");
            // wrap Ctrl-C (End of Text)
            map.put(new Character('\u0003'), "<?suntrans2-ascii-character 0003?>");
            // wrap Ctrl-D (End of Transmission)
            map.put(new Character('\u0004'), "<?suntrans2-ascii-character 0004?>");
            // wrap Ctrl-E (Enquiry)
            map.put(new Character('\u0005'), "<?suntrans2-ascii-character 0005?>");
            // wrap Ctrl-F (Acknowledge)
            map.put(new Character('\u0006'), "<?suntrans2-ascii-character 0006?>");
            // wrap Ctrl-G (Bell)
            map.put(new Character('\u0007'), "<?suntrans2-ascii-character 0007?>");
            // wrap Ctrl-H (Backspace)
            map.put(new Character('\u0008'), "<?suntrans2-ascii-character 0008?>");
            
            // NOT wrapping tab Ctrl-I (Horizontal Tab)
            // map.put(new Character('\u0009'), "<?suntrans2-ascii-character 0009?>");
            
            // NOT wrapping LF
            // wrap Ctrl-J (Line Feed)
            // map.put(new Character('\\u000a'), "<?suntrans2-ascii-character 000a?>");
            
            // wrap Ctrl-K (Vertical Tab)
            map.put(new Character('\u000b'), "<?suntrans2-ascii-character 000b?>");
            // wrap Ctrl-L (Form Feed)
            map.put(new Character('\u000c'), "<?suntrans2-ascii-character 000c?>");
            
            // NOT wrapping dos carriage return
            // wrap Ctrl-M (Carriage Return)
            // map.put(new Character('\\u000d'), "<?suntrans2-ascii-character \\u000d?>");
            
            // wrap Ctrl-N (Shift out)
            map.put(new Character('\u000e'), "<?suntrans2-ascii-character 000e?>");
            // wrap Ctrl-O (Shift in)
            map.put(new Character('\u000f'), "<?suntrans2-ascii-character 000f?>");
            // wrap Ctrl-P (Data link escape)
            map.put(new Character('\u0010'), "<?suntrans2-ascii-character 0010?>");
            // wrap Ctrl-Q (Device control one)
            map.put(new Character('\u0011'), "<?suntrans2-ascii-character 0011?>");
            // wrap Ctrl-R (Device control two)
            map.put(new Character('\u0012'), "<?suntrans2-ascii-character 0012?>");
            // wrap Ctrl-S (Device control three)
            // this fixes 4469938
            map.put(new Character('\u0013'), "<?suntrans2-ascii-character 0013?>");
            // wrap Ctrl-T (Device control four)
            map.put(new Character('\u0014'), "<?suntrans2-ascii-character 0014?>");
            // wrap Ctrl-U (Negative Ackknowledge)
            map.put(new Character('\u0015'), "<?suntrans2-ascii-character 0015?>");
            // wrap Ctrl-V (Synchronous idle)
            map.put(new Character('\u0016'), "<?suntrans2-ascii-character 0016?>");
            // wrap Ctrl-W (End of transmission block)
            map.put(new Character('\u0017'), "<?suntrans2-ascii-character 0017?>");
            // wrap Ctrl-X (Cancel)
            map.put(new Character('\u0018'), "<?suntrans2-ascii-character 0018?>");
            // wrap Ctrl-Y (End of medium)
            map.put(new Character('\u0019'), "<?suntrans2-ascii-character 0019?>");
            // wrap Ctrl-Z (Substitute)
            map.put(new Character('\u001a'), "<?suntrans2-ascii-character 001a?>");
            // wrap Ctrl-[ (Escape)
            map.put(new Character('\u001b'), "<?suntrans2-ascii-character 001b?>");
            // wrap Ctrl-\ (File separator)
            map.put(new Character('\u001c'), "<?suntrans2-ascii-character 001c?>");
            // wrap Ctrl-] (Group separator)
            map.put(new Character('\u001d'), "<?suntrans2-ascii-character 001d?>");
            // wrap Ctrl-^ (Record separator)
            map.put(new Character('\u001e'), "<?suntrans2-ascii-character 001e?>");
            // wrap Ctrl-_ (Unit separator)
            map.put(new Character('\u001f'), "<?suntrans2-ascii-character 001f?>");
            
            asciiMap = map;
        }
    }
    
    public static Map getAsciiControlCodesMap(){
        init();
        return Collections.unmodifiableMap(asciiMap);
    }
}

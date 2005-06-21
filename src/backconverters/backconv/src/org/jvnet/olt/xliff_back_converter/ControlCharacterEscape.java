
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ControlCharacterEscape.java
 *
 * Created on June 3, 2004, 12:15 PM
 */
package org.jvnet.olt.xliff_back_converter;

import java.util.*;

/**
 * <p>ControlCharacterEscape objects is to escape those code with a processing instruction,
 * and back into control characters, especially for software message. These processing
 * instruction is gotten from the software message parser via ASCIIControlCodeMapFactory
 * object.</p>
 *
 * <p>The xml instruction is something like below formatting:</p>
 * <code>&lt;?suntrans-ascii-character xxxx?&gt;</code>
 * <p>Here xxxx is the ascii nubmer of the control code</p>
 *
 * @author    Charles Liu
 * @version   August 8, 2002
 */

public class ControlCharacterEscape {
    private static Map asciiMap = null;

    public static String INSTRUCTION_CTRLCHAR = "suntrans2-ascii-character";

    /**
     * intiate the map elements
     */
    private static void init () {
        if (asciiMap == null){
            HashMap map = new HashMap();
            // escape Ctrl-A (Start of Heading)
            map.put("<?suntrans2-ascii-character 0001?>", new Character('\u0001'));
            // escape Ctrl-B (Start of Text)
            map.put("<?suntrans2-ascii-character 0002?>", new Character('\u0002'));
            // escape Ctrl-C (End of Text)
            map.put("<?suntrans2-ascii-character 0003?>", new Character('\u0003'));
            // escape Ctrl-D (End of Transmission)
            map.put("<?suntrans2-ascii-character 0004?>", new Character('\u0004'));
            // escape Ctrl-E (Enquiry)
            map.put("<?suntrans2-ascii-character 0005?>", new Character('\u0005'));
            // escape Ctrl-F (Acknowledge)
            map.put("<?suntrans2-ascii-character 0006?>", new Character('\u0006'));
            // escape Ctrl-G (Bell)
            map.put("<?suntrans2-ascii-character 0007?>", new Character('\u0007'));
            // escape Ctrl-H (Backspace)
            map.put("<?suntrans2-ascii-character 0008?>", new Character('\u0008'));

            // NOT escape tab Ctrl-I (Horizontal Tab)
            // map.put("<?suntrans2-ascii-character 0009?>", new Character('\u0009'));

            // NOT escape LF
            // escape Ctrl-J (Line Feed)
            // map.put("<?suntrans2-ascii-character 000a?>", new Character('\\u000a'));

            // escape Ctrl-K (Vertical Tab)
            map.put("<?suntrans2-ascii-character 000b?>", new Character('\u000b'));
            // escape Ctrl-L (Form Feed)
            map.put("<?suntrans2-ascii-character 000c?>", new Character('\u000c'));

            // NOT escape dos carriage return
            // escape Ctrl-M (Carriage Return)
            // map.put("<?suntrans2-ascii-character \\u000d?>", new Character('\\u000d'));

            // escape Ctrl-N (Shift out)
            map.put("<?suntrans2-ascii-character 000e?>", new Character('\u000e'));
            // escape Ctrl-O (Shift in)
            map.put("<?suntrans2-ascii-character 000f?>", new Character('\u000f'));
            // escape Ctrl-P (Data link escape)
            map.put("<?suntrans2-ascii-character 0010?>", new Character('\u0010'));
            // escape Ctrl-Q (Device control one)
            map.put("<?suntrans2-ascii-character 0011?>", new Character('\u0011'));
            // escape Ctrl-R (Device control two)
            map.put("<?suntrans2-ascii-character 0012?>", new Character('\u0012'));
            // escape Ctrl-S (Device control three)
            map.put("<?suntrans2-ascii-character 0013?>", new Character('\u0013'));
            // escape Ctrl-T (Device control four)
            map.put("<?suntrans2-ascii-character 0014?>", new Character('\u0014'));
            // escape Ctrl-U (Negative Ackknowledge)
            map.put("<?suntrans2-ascii-character 0015?>", new Character('\u0015'));
            // escape Ctrl-V (Synchronous idle)
            map.put("<?suntrans2-ascii-character 0016?>", new Character('\u0016'));
            // escape Ctrl-W (End of transmission block)
            map.put("<?suntrans2-ascii-character 0017?>", new Character('\u0017'));
            // escape Ctrl-X (Cancel)
            map.put("<?suntrans2-ascii-character 0018?>", new Character('\u0018'));
            // escape Ctrl-Y (End of medium)
            map.put("<?suntrans2-ascii-character 0019?>", new Character('\u0019'));
            // escape Ctrl-Z (Substitute)
            map.put("<?suntrans2-ascii-character 001a?>", new Character('\u001a'));
            // escape Ctrl-[ (Escape)
            map.put("<?suntrans2-ascii-character 001b?>", new Character('\u001b'));
            // escape Ctrl-\ (File separator)
            map.put("<?suntrans2-ascii-character 001c?>", new Character('\u001c'));
            // escape Ctrl-] (Group separator)
            map.put("<?suntrans2-ascii-character 001d?>", new Character('\u001d'));
            // escape Ctrl-^ (Record separator)
            map.put("<?suntrans2-ascii-character 001e?>", new Character('\u001e'));
            // escape Ctrl-_ (Unit separator)
            map.put("<?suntrans2-ascii-character 001f?>", new Character('\u001f'));

            asciiMap = map;
        }
    }

    public static String getControlChar(String str, String str1){
        init();
        Object result = asciiMap.get("<?" + str + " " + str1 + "?>");
        if(result != null)
            return ((Character)result).toString();
        return null;
    }
}
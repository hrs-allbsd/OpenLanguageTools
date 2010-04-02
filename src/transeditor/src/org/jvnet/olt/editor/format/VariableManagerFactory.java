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
/*
 * VariableManagerFactory.java
 *
 * Created on 12 January 2004, 18:00
 */
package org.jvnet.olt.editor.format;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.sgml.EntityManager;


/**
 *
 * @author  jc73554
 */
public class VariableManagerFactory {
    private static final Logger logger = Logger.getLogger(VariableManagerFactory.class.getName());

    private static final int UNKNOWN = 0;
    private static final int SGML = 1;
    private static final int HTML = 2;
    private static final int XML = 3;
    private static final int JSP = 4;
    private static final int PO = 11;
    private static final int JAVA = 12;
    private static final int MSG = 13;
    private static final int PROPERTIES = 14;
    private static final int PLAINTEXT = 15;
    private static final int DTD = 16;
    private static final int STAROFFICE = 17;
    private Map validFormatsHash;

    /** Creates a new instance of VariableManagerFactory */
    public VariableManagerFactory() {
        validFormatsHash = new HashMap();
        validFormatsHash.put("", new Integer(UNKNOWN));
        validFormatsHash.put("SGML", new Integer(SGML));
        validFormatsHash.put("HTML", new Integer(HTML));
        validFormatsHash.put("XML", new Integer(XML));
        validFormatsHash.put("JSP", new Integer(JSP));

        validFormatsHash.put("PO", new Integer(PO));
        validFormatsHash.put("JAVA", new Integer(JAVA));
        validFormatsHash.put("MSG", new Integer(MSG));
        validFormatsHash.put("PROPERTIES", new Integer(PROPERTIES));
        validFormatsHash.put("PLAINTEXT", new Integer(PLAINTEXT));
        validFormatsHash.put("DTD", new Integer(DTD));

        validFormatsHash.put("STAROFFICE", new Integer(STAROFFICE));

    }

    public GlobalVariableManager createVariableManager(java.lang.String type) {
        Integer iType = (Integer)validFormatsHash.get(type);

        if (iType == null) { //  guard clause
            iType = UNKNOWN;
            logger.warning("Unknown type for original file: " + type);
        }

        GlobalVariableManager gvm = null;

        switch (iType.intValue()) {
        case SGML:
        case HTML:
        case XML:
        case JSP:
            gvm = new EntityManager();
            return gvm;

        case PO:
        case JAVA:
        case MSG:
        case PROPERTIES:
        case PLAINTEXT:
        case DTD:
        case STAROFFICE:
            gvm = new EntityManager();
            return gvm;

        default:
            gvm = new EntityManager();
            return gvm;
        }
    }
}

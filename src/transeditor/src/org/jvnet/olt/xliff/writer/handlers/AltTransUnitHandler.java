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
 * AltTransHandler.java
 *
 * Created on April 26, 2005, 4:55 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;


/**
 *
 * @author boris
 */
public class AltTransUnitHandler extends BaseHandler {
    /** Creates a new instance of AltTransHandler */
    public AltTransUnitHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        //this is a placeholder to make TransUnitHandler simpler
    }
}

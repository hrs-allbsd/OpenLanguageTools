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

package org.jvnet.olt.aml_tmx_converter;

import org.xml.sax.SAXException;

/**
 * @author Charles Liu
 * @version 1.0
 */

public class AmlUnalignedFoundException extends SAXException  {

  public AmlUnalignedFoundException() {
    super("Found unaligned segment");
  }
  /**
   * Constructor for the AmlUnalignedFoundException object
   *
   * @param msg A message to be included with a
   *        AmlUnalignedFoundException
   */
  public AmlUnalignedFoundException(String msg) {
    super(msg);
  }

}
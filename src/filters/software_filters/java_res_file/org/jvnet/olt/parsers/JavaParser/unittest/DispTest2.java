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

package org.jvnet.olt.parsers.JavaParser.unittest;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.TranslatableTokenCell;
import org.jvnet.olt.parsers.JavaParser.*;
import java.io.*;

public class DispTest2
{
  public static void main(String[] argv)
  {
    try
    {
      if(argv.length != 1)
      {
	throw new IllegalArgumentException("The wrong number of parameters was passed to the program!");
      }
      File file = new File(argv[0]);
      FileInputStream inStream = new FileInputStream(file);

      InputStreamReader reader = new InputStreamReader(inStream,System.getProperty("file.encoding"));
 
      ResBundleParser parser = new ResBundleParser(reader);
      parser.parse();

      PrintDisplayingVisitor visitor = new PrintDisplayingVisitor();
      parser.walkParseTree(visitor, null);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
}

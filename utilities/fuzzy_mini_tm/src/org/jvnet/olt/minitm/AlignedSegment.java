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

package org.jvnet.olt.minitm;

public class AlignedSegment 
{
  private String source;
  private String translation;
  private String translatorID;
  private long   idInDataStore;
 	
  public AlignedSegment(String src, String trans, String translator_id)
  {
    this(src, trans, translator_id, -1L);
  }

  public AlignedSegment(String src, 
			String trans, 
			String translator_id, 
			long segment_id)
  {
    source = src;
    translation = trans;
    translatorID = translator_id;
    idInDataStore = segment_id;
  }

  public String getSource() {
    return source;
  }
	 
  public String getTranslation() {
    return translation;
  }
	 
  public String getTranslatorID() {
    return translatorID;
  }
  
  public void setSource(String src) {
    source = src;
  }
	 
  public void setTranslation(String trans) {
    translation = trans;
  }
	 
  public void setTranslatorID(String translator_id) {
    translatorID = translator_id;
  }

  public long getDataStoreKey() {
    return idInDataStore;
  }
}

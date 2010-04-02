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

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

import java.util.*;

import org.jvnet.olt.converterhandler.XMLHandler;

/**
 * <p>Title: Alignment editor</p>
 * <p>Description: part of TMCi editor</p>
 * @author Charles
 * @version 1.0
 */

public class AmlXMLReader extends XMLHandler {

  private boolean hasUnaligned = false;
  /**
   * implement XML Element to JAVA methods
   */
  //skl Element
  public void amlElement(Attributes attr) {

  }
  public void amlElementData(String data) {

  }
  public void amlElementEnd() {

  }
  //header Element
  public void headerElement(Attributes attr) {

  }
  public void headerElementData(String data) {

  }
  public void headerElementEnd() {

  }
  //note Element
  public void noteElement(Attributes attr) {

  }
  public void noteElementData(String data) {

  }
  public void noteElementEnd() {

  }
  //prop Element
  public void propElement(Attributes attr) {

  }
  public void propElementData(String data) {

  }
  public void propElementEnd() {

  }
  //body Element
  public void bodyElement(Attributes attr) {

  }
  public void bodyElementData(String data) {

  }
  public void bodyElementEnd() {

  }

  //===========================Alignment Unit=========================================
  //aligned Element
  public void alignedElement(Attributes attr) {

  }
  public void alignedElementData(String data) {

  }
  public void alignedElementEnd() {

  }
  //none-aligned Element
  public void unalignedElement(Attributes attr) {
    this.hasUnaligned = true;
  }
  public void unalignedElementData(String data) {

  }
  public void unalignedElementEnd() throws AmlUnalignedFoundException {
    throw new AmlUnalignedFoundException();
  }
  //src Element
  public void srcElement(Attributes attr) {

  }
  public void srcElementData(String data) {

  }
  public void srcElementEnd() {

  }
  //trg Element
  public void trgElement(Attributes attr) {

  }
  public void trgElementData(String data) {

  }
  public void trgElementEnd() {

  }
  //seg Element
  public void segElement(Attributes attr) {

  }
  public void segElementData(String data) {

  }
  public void segElementEnd() {

  }


  //================================Content Markup===================================================
  //bpt Element
  public void bptElement(Attributes attr) {

  }
  public void bptElementData(String data) {

  }
  public void bptElementEnd() {

  }
  //ept Element
  public void eptElement(Attributes attr) {

  }
  public void eptElementData(String data) {

  }
  public void eptElementEnd() {

  }
  //sub Element
  public void subElement(Attributes attr) {

  }
  public void subElementData(String data) {

  }
  public void subElementEnd() {

  }
  //it Element
  public void itElement(Attributes attr) {

  }
  public void itElementData(String data) {

  }
  public void itElementEnd() {

  }
  //ph Element
  public void phElement(Attributes attr) {

  }
  public void phElementData(String data) {

  }
  public void phElementEnd() {

  }
  //hi Element
  public void hiElement(Attributes attr) {

  }
  public void hiElementData(String data) {

  }
  public void hiElementEnd() {

  }
  //ut Element
  public void utElement(Attributes attr) {

  }
  public void utElementData(String data) {

  }
  public void utElementEnd() {

  }
  public boolean isHasUnaligned() {
    return hasUnaligned;
  }

}


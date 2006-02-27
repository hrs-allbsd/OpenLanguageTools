
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmx_aml_converter;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import org.jvnet.olt.converterhandler.Resource;

/**
 * <p>Title: Alignment editor</p>
 * <p>Description: part of TMCi editor</p>
 * @author Charles
 * @version 1.0
 */

public class TmxEntityResolver implements EntityResolver {
  private Reader tmxDtdReader = null;

  public TmxEntityResolver() {

  }

  public TmxEntityResolver(Reader tmxDtdReader) {
    this.tmxDtdReader = tmxDtdReader;
  }

  /**
   * Tries to find an entity in CLASSPATH.
   * The name of the CLASSPATH resource is obtained
   * from the systemID parameter, after eliminating
   * all characters before the last slash or backslash
   * including this character. The method returns
   * a SAX InputSource created by the getResource()
   * method of our InputSourceUtils class.
   */
  public InputSource resolveEntity(String publicID, String systemID)  throws SAXException {

    File file = null;

    /* Open a file containing the DTD, so we can get its filename later
     */
    try {
        file = new File(systemID);
    } catch (NullPointerException e) {
        throw new NullPointerException();
    }

    if(file.getName().equals(Resource.TMXDTDFileName)){
      if(tmxDtdReader == null) {
        return new InputSource(getClass().getResourceAsStream("/resources/" + Resource.TMXDTDFileName));
      } else {
        return new InputSource(tmxDtdReader);
      }
    }
    return null;
  }

}
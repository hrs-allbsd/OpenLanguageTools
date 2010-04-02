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
 * SuntransMappingFile.java
 *
 * Created on June 11, 2004, 10:30 AM
 */
package org.jvnet.olt.parsers.mapping;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.xml.sax.*;

/**
 *
 * @author    Charles Liu
 * @version   June 11, 2004
 */
public class SuntransMappingFile {

    /* Logging object */
    private static Logger logger = Logger.getLogger("org.jvnet.olt.parsers.mapping");
    private static final String loggername = "Parse SunTransMappingFile";

    private HashMap mappings = new HashMap();

    public void parserMappings(String anXmlFile, String dtdString) throws Exception {
        try {
            FileInputStream is = new FileInputStream(anXmlFile);
            java.io.InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            parserMappings(reader, dtdString);
        }
        catch(Exception ex) {
            logger.log(Level.SEVERE, loggername, ex);
            throw ex;
        }
    }

    public void parserMappings(Reader anInputReader, String mappingFileDtd ) throws Exception {
        try {
            //  Prepare the SuntransMappingFile DTD reader
            //InputStream dtd_is = this.getClass().getResourceAsStream("/resource/suntrans-mapping-file.dtd");
            InputStream dtd_is = new FileInputStream(mappingFileDtd);
            InputStreamReader dtd_reader = new InputStreamReader(dtd_is, "UTF-8");

            //  Prepare the handler
            SuntransMappingFileHandler handler = new SuntransMappingFileHandlerImpl(logger, mappings);

            //  Prepare the parser
            SuntransMappingFileParser parser = new SuntransMappingFileParser(logger, handler, null, dtd_reader);
            parser.parse(new InputSource(anInputReader));

        }
        catch(Exception ex) {
            logger.log(Level.SEVERE, loggername, ex);
            throw ex;
        }
    }

    public HashMap getMappings() {
        return mappings;
    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("Usage: runtest <xmlfile> <mapping file dtd>");
                return;
            }

            SuntransMappingFile mf = new SuntransMappingFile();
            mf.parserMappings(args[0], args[1]);

            Map mappings = mf.getMappings();
            Iterator iter = mappings.keySet().iterator();
            while(iter.hasNext()) {
                String key = (String)iter.next();
                String value = (String)mappings.get(key);
                System.out.println("key  ="+key+"\nvalue="+value);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}


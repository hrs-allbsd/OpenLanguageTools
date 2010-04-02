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
/**
 *
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.utilities;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.dom.DOMDocumentType;
import org.dom4j.io.*;


/**
 * Simple code for conversion of two properties files to tmx file.
 *
 */
public class ConvertPropertiesToTmx {
    
    /**
     * private empty constructor
     */
    private ConvertPropertiesToTmx() {
    }
    
    /**
     * Here is simple code that can convert content of two properties files to tmx file. We don't care
     * about segmentation here. It means loose of some loose of levereage. You need to convert properties
     * files to ascii encoding with native2ascii before starting this code.
     *
     * @param sourceProperty name of property file with original segments
     * @param sourceLanguage language of source segments
     * @param targetProperty name of property file with translated segments
     * @param targetLanguage language of translated segments
     * @param name of target tmx file
     *
     * @throws FileNotFoundException,IOException,UnsupportedEncodingException
     */
    public static void convert(String sourceProperty,String sourceLanguage,String targetProperty,String targetLanguage,String tmxFile)
            throws FileNotFoundException,IOException,UnsupportedEncodingException {
        
        Properties sourceProps = new Properties();
        Properties targetProps = new Properties();
        
        sourceProps.load(new FileInputStream(new File(sourceProperty)));
        targetProps.load(new FileInputStream(new File(targetProperty)));
        
        Document document = DocumentHelper.createDocument();
        document.setDocType(new DOMDocumentType("tmx","tmx13.dtd"));
        
        // create header of the tmx file
        Element tmx = document.addElement( "tmx" );
        tmx.addAttribute("version", "1.3");
        
        Element header = tmx.addElement("header");
        header.addAttribute("adminlang","en-US");
        header.addAttribute("srclang","en-US");
        header.addAttribute("o-tmf","xliff");
        header.addAttribute("segtype","sentence");
        header.addAttribute("creationtoolversion","0.1");
        header.addAttribute("creationtool","PropertiesToTmx");
        header.addAttribute("datatype","properties");
        
        Element prop = header.addElement("prop");
        prop.addAttribute("type","SunTrans::DocFile");
        prop.addText(sourceProperty);
        
        Element body = tmx.addElement("body");
        
        // read all properties and write it to tmx file
        Enumeration propsKeys = sourceProps.keys();
        int counter = 1;
        
        while(propsKeys.hasMoreElements()) {
            Element tu = body.addElement("tu");
            tu.addAttribute("tuid","a" + counter);
            
            String key = (String)propsKeys.nextElement();
            
            String sourceText = (String)sourceProps.get(key);
            String targetText = (String)targetProps.get(key);
            if(sourceText==null || targetText==null) continue;
            Element tuvS = tu.addElement("tuv").addAttribute("xml:lang",sourceLanguage);
            tuvS.addElement("seg").addText(sourceText);
            Element tuvT = tu.addElement("tuv").addAttribute("xml:lang",targetLanguage);
            tuvT.addElement("seg").addText(targetText);
            counter++;
        }
        
        // flush the output to file
        OutputFormat outformat = OutputFormat.createPrettyPrint();
        outformat.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(new File(tmxFile)), outformat);
        writer.write(document);
        writer.flush();
    }
    
    public static void main(String[] args) throws Exception {
        if(args.length!=5) {
            System.err.println("ConvertPropertiesToTmx sourceProperty sourceLanguage targetProperty targetLanguage tmxFile");
            System.exit(1);
        }
        ConvertPropertiesToTmx.convert(args[0],args[1],args[2],args[3],args[4]);
    }
    
}

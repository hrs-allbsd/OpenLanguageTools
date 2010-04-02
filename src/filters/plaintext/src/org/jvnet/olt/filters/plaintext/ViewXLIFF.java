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

package org.jvnet.olt.filters.plaintext;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;
import org.jvnet.olt.utilities.*;
import org.jvnet.olt.format.*;
import org.jvnet.olt.format.plaintext.*;
import java.io.*;

public class ViewXLIFF
{

  public static void main(String[] argv)
  {
    try
    {
      if(argv.length != 3){
          System.out.println("Usage : viewxliff <filename> <language> <encoding>");
	throw new IllegalArgumentException("The wrong number of parameters was passed to the program!");
      }
      
      String filename = argv[0];
      String language = argv[1];
      String encoding = argv[2];
      String directory = (new File(filename).getParent());
      if (directory == null){
          directory="";
      }
      File file = new File(filename);
            //String shortname = filename.substring(directory.length(),filename.length());
            FileInputStream inStream = new FileInputStream(file);
            Reader reader;
            try {
                reader = new InputStreamReader(inStream, encoding);
            } catch (java.io.UnsupportedEncodingException e){
                throw new PlaintextParserException("Problem trying to read plaintext file in encoding "+ encoding);
            }
            
            BufferedReader buf_txtReader = new BufferedReader(reader);
            
            FileOutputStream xliffStream = new FileOutputStream(filename+".xlf");
            OutputStreamWriter xliffWriter = new OutputStreamWriter(xliffStream, "UTF-8");
            BufferedWriter buf_xliff = new BufferedWriter(xliffWriter);
            
            
            FileOutputStream sklStream = new FileOutputStream(filename+".skl");
            OutputStreamWriter sklWriter = new OutputStreamWriter(sklStream,"UTF-8");
            BufferedWriter buf_skl = new BufferedWriter(sklWriter);
            
            BlockSegmenter_en blockParser = new  BlockSegmenter_en(buf_txtReader);
            blockParser.parse();
            
            FormatWrapper wrapper = new PlainTextFormatWrapper();
            SegmenterFormatter formatter = new XliffSegmenterFormatter("PLAINTEXT",
            language,filename,buf_xliff , buf_skl, null, wrapper);
            
            XLIFFVisitor xliffVisitor = new XLIFFVisitor(formatter, language);
            blockParser.walkParseTree(xliffVisitor, null);
            formatter.flush();
            
            
            XliffZipFileIO xlz = new XliffZipFileIO(new File(filename+".xlz"));
            FileReader sklreader = new FileReader(filename+".skl");
            FileReader xliffreader = new FileReader(filename+".xlf");
            
            Writer sklwriter = xlz.getSklWriter();
            while (sklreader.ready()){
                sklwriter.write(sklreader.read());
            }
            sklwriter.flush();
            
            Writer xliffwriter = xlz.getXliffWriter();
            while (xliffreader.ready()){
                xliffwriter.write(xliffreader.read());
            }
            xliffwriter.flush();
            xliffwriter.close();
            xlz.writeZipFile();
            
            buf_xliff.close();
            buf_skl.close();
            xliffreader.close();
            sklreader.close();

            // now delete those temporary files :
            File xliff = new File(filename+".xlf");
            xliff.delete();
            File skeleton = new File(filename+".skl");
            skeleton.delete();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }


}

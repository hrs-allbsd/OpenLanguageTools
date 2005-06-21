
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.plaintext;

import java.io.*;

public class ViewAlignment
{

  public static void main(String[] argv)
  {
    try
    {
      if(argv.length != 3){
	throw new IllegalArgumentException("The wrong number of parameters was passed to the program!");
      }

      File file = new File(argv[0]);
      File aliFile = new File (argv[1]);
      String language = argv[2];
      FileInputStream txtStream = new FileInputStream(file);
      InputStreamReader txtReader = new InputStreamReader(txtStream,"UTF-8");
      BufferedReader buf_txtReader = new BufferedReader(txtReader);
      
      FileOutputStream aliStream = new FileOutputStream(aliFile);
      OutputStreamWriter aliWriter = new OutputStreamWriter(aliStream, "UTF-8");
      BufferedWriter buf_ali = new BufferedWriter(aliWriter);
     
      BlockSegmenter_en blockParser = new  BlockSegmenter_en(buf_txtReader);
      //ResBundleParser parser = new ResBundleParser(System.in);
      blockParser.parse();
      AlignmentTextVisitor aliVisitor = new AlignmentTextVisitor(buf_ali);
      aliVisitor.setLanguage(language);
      blockParser.walkParseTree(aliVisitor, null);
      aliVisitor.printAlignmentTextFooter();
      buf_ali.flush();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }


}

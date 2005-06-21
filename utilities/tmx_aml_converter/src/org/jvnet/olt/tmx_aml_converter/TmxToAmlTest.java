
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmx_aml_converter;


import java.io.*;
import java.io.FileNotFoundException;


/**
 * A class to run and test the <code>TmxToAmlConverter</code>
 *
 * @author Charlse Liu
 * @created December 8, 2002
 */

public class TmxToAmlTest {

  /**
   * Constructor for the TmxToAmlTest object
   */
  public TmxToAmlTest() {  }

  /**
   * The main method to carry out the testing.
   *
   * @param args The arguments entered on the command line.
   */
  public static void main(String args[]) {

    if (args.length != 2) {
      System.out.println("Usage: <file1.tmx> <file2.aml>");
      System.exit(1);
    }

    try {

      File file = new File(args[0]);
      if(!file.exists()) {
        System.out.println("Error: Unable to find file \""+args[0]+"\"");
        System.exit(1);
      }


      String tmxFile = args[0];
      String amlFile = args[1];
      InputStreamReader reader = new InputStreamReader(
                                 new FileInputStream(tmxFile),"UTF-8");
      OutputStreamWriter writer = new OutputStreamWriter(
                                  new FileOutputStream(amlFile),"UTF-8");

      TmxToAmlConverter ttac = new TmxToAmlConverter();
      ttac.tmxToAml(tmxFile, reader, amlFile, writer);


    } catch (IOException ex) {
      System.out.println("Error: Unable to load TMX file");
      return;
    } catch (TmxToAmlTransformerException ex) {
      System.out.println("Error: Unable to create AML file");
      return;
    } catch (Exception ex) {
      System.out.println("Error: "+ex.getMessage());
      return;
    }

    System.out.println("done TMX -> AML successfully.");
  }

}
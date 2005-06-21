
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import java.io.*;
import sun.io.*;

public class RemoveNewlines {
    
    public static void main(String[] argv) {
        try {
            if(argv.length != 3) {
                usage();
                System.exit(1);
            }
            
            //  Determine the encoding
            //  String encoding = argv[2].toUpperCase();
            String encoding = sun.io.CharacterEncoding.aliasName(argv[2]);
            
            if(encoding == null) {
                System.err.print("The encoding " + argv[2] + " is not supported by Java\nPlease use another encoding.\n");
                System.exit(4);
            }
            
            //  Open the input file.
            File file = new File(argv[0]);
            FileInputStream inStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(inStream, encoding);
            
            File fileOut = new File(argv[1]);
            //  Create the output file.
            if(fileOut.exists()) {
                System.out.println("The file \"" + argv[1] + "\" already exists.");
                usage();
                System.exit(3);
            }
            SgmlDocFragmentParser parser = null;
            //  Parse the input file.
            try {
                parser = new  SgmlDocFragmentParser(reader);
                parser.parse();
                
                //  Remove newlines from the parse tree.
                NewlineRemovingVisitor newlineRemover = new NewlineRemovingVisitor();
                parser.walkParseTree(newlineRemover, null);
                
            } catch (Error e){
                System.out.println("WARNING : Sgml parser error trying to remove lines from "+argv[0]);
                System.out.println("Cannot remove new lines from input file, instead we will copy "+argv[0]+" to "+argv[1]);
                try {
                    copyFile(file, fileOut);
                } catch (IOException io){
                    System.out.println("IOException when trying to copy file "+e.getMessage());
                    System.exit(4);
                }
                System.exit(0);
            }
            
            try {
                if(!fileOut.createNewFile()) {
                    System.out.println("Could not create a file named \"" + argv[1] + "\".");
                    usage();
                    System.exit(4);
                }
            }
            catch(IOException ioEx) {
                System.out.println("Could not create a file named \"" +
                argv[1] +
                "\". "  +
                ioEx.getMessage());
                usage();
                System.exit(4);
            }
            
            FileOutputStream outStream = new FileOutputStream(fileOut);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, encoding);
            
            //  Write out the modified parse tree.
            DisplayingNodePrintingVisitor treePrinter =
            new DisplayingNodePrintingVisitor(writer);
            parser.walkParseTree(treePrinter, null);
            
            //  Tidy up.
            writer.close();
            reader.close();
            
            //  Successful completion!
            System.exit(0);
        }
        catch(Exception ex) {
            System.out.println("Error encountered while processing file: " +
            argv[0]);
            ex.printStackTrace();
            System.exit(2);
        }
    }
    
    public static void usage() {
        //  Put the usage message here
        System.out.print("\nUsage:\n\tremovenewlines <input_file> <output-file> <encoding>\n\n");
    }
    
    protected static void copyFile(File file, File target) throws IOException{
        
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(target));
        int i = 0;
        while ((i=is.read()) != -1){
            os.write(i);
        }
        is.close();
        os.close();
    }
    
    
}

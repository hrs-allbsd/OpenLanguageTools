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
 * FixBrokenTMX.java
 *
 *
 *
 * Created on June 17, 2003, 3:32 PM
 */
package src.filters.xliff_validator;

import java.io.*;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;
/**
 * This code was written in a hurry to fix the output from the conv2tmx.pl program
 * which takes trados .txt tms and converts them to slightly broken TMX. All that's
 * wrong is that tags are possibly wrongly nested, since the perl script isn't a
 * full blown xml parser. Most of the time this all just works fine.
 *
 * This program uses a *lot* of memory on large files : I needed -Xmx2048m for it
 * to work on a 5mb tmx file !
 *
 * @author  timf
 */
public class FixBrokenTMX implements org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParserVisitor, org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParserTreeConstants {
    
    private boolean inItTag = false;
    Writer writer;
    
    /** Creates a new instance of test */
    public FixBrokenTMX(String[] argv) {
        try {
            File file = new File(argv[0]);
            FileInputStream inStream = new FileInputStream(file);
            
            InputStreamReader reader = new InputStreamReader(inStream,"UTF-8");
            System.out.println(""+System.getProperty("file.encoding"));
            System.out.println(reader.getEncoding());
            System.out.println("");
            this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getName()+".fixed"),"UTF-8"));
            
            
            SgmlDocFragmentParser parser = new  SgmlDocFragmentParser(reader);
            parser.parse();
            System.out.println("Parsed " + file.getName()+ " fixing tags...");
            parser.walkParseTree(this, null);
            writer.flush();
            writer.close();
            System.out.println("Done");
            
        } catch (Exception e){
            System.out.println("oops"+e.getMessage());
            e.printStackTrace();
        }
        
        
    }
    
    public Object visit(org.jvnet.olt.parsers.SgmlDocFragmentParser.SimpleNode node, Object data) {
        try {
            switch (node.getType()){
                case JJTOPEN_TAG:
                    if (node.getTagName().equals("it")){
                        if (inItTag){
                            // close the previous one
                            writer.write("</it>");
                        }
                        inItTag = true;
                    }
                    // write this tag
                    writer.write(node.getNodeData());
                    break;
                    
                case JJTCLOSE_TAG:
                    if (node.getTagName().equals("it")){
                        if (inItTag){
                            inItTag = false;
                            writer.write(node.getNodeData());
                        } else {
                            System.err.println("Not in it tag == dropping tag");
                        }
                    } else if (node.getTagName().equals("seg")){
                        if (inItTag){
                            // make sure we've closed the inline tags before ending the segment
                            writer.write("</it>");
                        }
                        inItTag = false;
                        writer.write("</seg>\n");
                    }
                    else {
                        writer.write(node.getNodeData());
                    }
                    break;
                default:
                    writer.write(node.getNodeData());
            }
        } catch (java.io.IOException e){
            System.out.println("IOException " + e.getMessage());
        }
        
        
        return null;
        
    }
    
    public static void main(String[] args){
        if (args.length != 1){
            System.out.println("Usage : FixBrokenTMX <filename.tmx>");
            System.out.println("files are expected to be in UTF-8 before running this tool !");
            System.exit(1);
        }
        FixBrokenTMX mytest = new FixBrokenTMX(args);
    }
}

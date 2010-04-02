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

package org.jvnet.olt.filters.software.moz_dtd;

import org.jvnet.olt.tmci.*;
import org.jvnet.olt.parsers.DTDDocFragmentParser.*;
import java.io.*;
/**
 * See the javadoc of the interfaces to let you know what this class does
 */
public class MozDTDFileParserFacade implements TMCiParserFacade, SunTrans2ParserFacade {
    public DTDDocFragmentParser parser;
    
    
    public String[][] getMessageStringArr(Reader reader,String defaultContext)
    throws TMCParseException,IOException,Exception {
        throw new TMCParseException("SunTrans1 support has not been implemented for mozilla dtd files!");
    }
    
    public TokenCell[] getTokenCellArray(Reader reader)
    throws TMCParseException,IOException,Exception {
        throw new TMCParseException("SunTrans1 support has not been implemented for mozilla dtd files!");
    }
    
    /**
     * Used by software message alignment tool during TMX file creation
     */
    public String[][] getSunTrans2MessageStringArr(Reader reader, String defaultContext) throws TMCParseException, IOException, Exception {
        String[][] messageStringArray = null;
        try {
            parser = new DTDDocFragmentParser(reader);
            parser.parse();
            reader.close();
            MessageCountingVisitor messageCounter = new MessageCountingVisitor();

            parser.walkParseTree(messageCounter, "");
            int numMessages = messageCounter.getMessageCount();            
            ST2MessageArrayFactoryVisitor visitor = new ST2MessageArrayFactoryVisitor(numMessages);
            parser.walkParseTree(visitor,"");
            if (visitor.encounteredNonMozillaMessageStringDTD()){
                throw new ParseException("While parsing that DTD file, we encoutered tokens that suggest it may not be a Mozilla message-string DTD file.");
            }
            messageStringArray = visitor.generateMessageArray();
        } catch(ParseException ex) {
            throw new TMCParseException(ex.getMessage());
        }
        return messageStringArray;
    }
    
    /**
     * Used by SunTrans2 during XLIFF conversion (for tm lookup probably)
     */
    public TokenCell[] getSunTrans2TokenCellArray(Reader reader) throws TMCParseException, IOException, Exception {
        TokenCell[] tokenCells = null;
        try {
            parser = new DTDDocFragmentParser(reader);
            parser.parse();
            reader.close();
            
            ST2DisplayingTokenArrayFactoryVisitor visitor = new ST2DisplayingTokenArrayFactoryVisitor();
            parser.walkParseTree(visitor,"");
            tokenCells = visitor.getDisplayingTokens();
            if (visitor.encounteredNonMozillaMessageStringDTD()){
                throw new ParseException("While parsing that DTD file, we encoutered tokens that suggest it may not be a Mozilla message-string DTD file.");
            }
        } catch(ParseException ex) {
            throw new TMCParseException(ex.getMessage());
        }
        return tokenCells;
    }
    
}

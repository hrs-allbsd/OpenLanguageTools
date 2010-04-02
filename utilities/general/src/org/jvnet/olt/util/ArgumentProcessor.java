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
 * ArgumentProcessor.java
 *
 * Created on 06 March 2003, 15:40
 */

package org.jvnet.olt.util;

/**
 * This class does argument processing of the form :<br>
 * <code>program-name prop=a otherprop=b etc=c </code><br><br>
 * 
 * There's another implementation of similar
 * functionality in the SunTrans2Client class which processes arguments of the form:
 * <code>program-name --prop a --otherprop b --etc c</code><br><br>
 * 
 * The code in the ArgumentProcessor implmentation is probably cleaner,
 * but the syntax for supplying command line arguments isn't used as widely as that
 * done in the SunTrans2Client class.
 * 
 * @see org.jvnet.olt.tm.commandline.SunTrans2Client#parseCommandLineArgsString[] args, Set allowedOptions)
 * @author  jc73554
 */
public class ArgumentProcessor
{
    private final int NAME = 0;
    private final int EQUALS = 1;
    private final int OPENQUOTE = 2;
    private final int VALUE = 3;
    private final int ESCAPE_SEQ = 4;
    private final int CLOSEQUOTE = 5;

   
    
   /** Creates a new instance of ArgumentProcessor */
    public ArgumentProcessor()
    {
        //  set up stuff
    }
    
    public java.util.Properties getProperties(String[] args) throws BadArgException
    {
        java.util.Properties props = new java.util.Properties();
        
        for(int i  = 0; i < args.length; i++)
        {
            try
            {
                processArgument(args[i], props);
            }
            catch(java.io.IOException ioEx)
            {
                ioEx.printStackTrace();
            }
        }
        return props;
    }
    
    //Code below here implements a hand written parser for 'name="value"' strings.
    protected void processArgument(String arg, java.util.Properties props) throws BadArgException, java.io.IOException
    {
        //  Parse the arg into 4 bits: name, equals, quotes, and value
        int state = NAME;
        
        int ch = 0;
        java.io.StringReader reader = new java.io.StringReader(arg);
        ContainerToken t = new ContainerToken();
        StringBuffer buffer = new StringBuffer();
        
        while((ch=reader.read()) != -1)
        {
            switch(state)
            {
                case NAME:
                    state = handleNameState(ch, buffer, t);
                    break;
                case EQUALS:
                    state = handleEqualsState(ch, buffer, t);
                    break;
                case OPENQUOTE:
                    state = handleOpenQuoteState(ch, buffer, t);                    
                    break;
                case VALUE:
                    state = handleValueState(ch, buffer, t);                    
                    break;
                case ESCAPE_SEQ:
                    state = handleEscapeSeqState(ch, buffer, t);                    
                    break;
                case CLOSEQUOTE:
                    state = handleCloseQuoteState(ch, buffer, t);                    
                    break;
                default:
                    throw new BadArgException("Illegal state encountered while parsing: " + arg);
            }
        }
        if(state != CLOSEQUOTE)
        { 
            throw new BadArgException("Unexpected end of string found while parsing: " + arg);
        }
        props.setProperty(t.getName(), t.getValue());
    }
    
    private int handleEqualsState(int c, StringBuffer b, ContainerToken t) throws BadArgException
    {
        switch(c)
        {
            case -1:
                throw new BadArgException("Unexpected end of string found while looking for attribute value. Attribute text so far: " + b.toString());
            case (int) '"':
                return OPENQUOTE;
            default:
                throw new BadArgException("Attribute values must be quoted. Attribute text so far: " + b.toString());
        }
    }
    
    private int handleOpenQuoteState(int c, StringBuffer b, ContainerToken t) throws BadArgException
    {
        switch(c)
        {
            case -1:
                throw new BadArgException("Unexpected end of string found while looking for attribute value. Attribute text so far: " + b.toString());
            case (int) '"':
                t.setValue("");
                return CLOSEQUOTE;
            case (int) '\\':
                return ESCAPE_SEQ;
            default:
                b.delete(0, b.length());
                b.append((char) c);
                return VALUE;
        }
      
    }

    private int handleCloseQuoteState(int c, StringBuffer b, ContainerToken t) throws BadArgException
    {
        //  A closed quote should be the last thing in the string.
        if(c != -1) { throw new BadArgException(""); }
        return CLOSEQUOTE;        
    }

    private int handleValueState(int c, StringBuffer b, ContainerToken t) throws BadArgException
    {
        switch(c)
        {
            case -1:
                throw new BadArgException("Unexpected end of string found while looking for attribute value. Attribute text so far: " + b.toString());
            case (int) '"':
                t.setValue(b.toString());
                return CLOSEQUOTE;
            case (int) '\\':
                return ESCAPE_SEQ;
            default:
                b.append((char) c);
                return VALUE;
        }
    
    }

    private int handleEscapeSeqState(int c, StringBuffer b, ContainerToken t) throws BadArgException
    {
        switch(c)
        {
            case -1:
                throw new BadArgException("Unexpected end of string found while prcessing attribute value. Attribute text so far: " + b.toString());
            case (int) '"':
            case (int) '\\':
                b.append((char) c);               
                return VALUE;
            default:
                throw new BadArgException("Unexpected escape sequence found. Attribute text so far: " + b.toString());                
        }    
    }

    
    private int handleNameState(int c, StringBuffer b, ContainerToken t) throws BadArgException
    {
        switch(c)
        {
            case -1:
            case (int) '"':  //  Quotes in the middle of names look weird
                throw new BadArgException("Unexpected end of string found while parsing attribute name. Attribute text so far: " + b.toString());
            case (int) '=':
                t.setName(b.toString());
                return EQUALS;
            default:
                b.append((char) c);
                return NAME;
        }      
    }

}

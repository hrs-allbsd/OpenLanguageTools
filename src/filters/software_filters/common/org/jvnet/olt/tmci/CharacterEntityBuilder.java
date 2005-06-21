
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmci;


public class CharacterEntityBuilder
{
  public static final int DEFAULT_STATE = 0;
  public static final int READ_AMP = 1;
  public static final int READ_HASH = 2;
  public static final int READ_ZERO = 3;
  public static final int READ_DEE = 4;

  public static String convertNewlineToEntity(String strText)
  {
    int ilength = strText.length();
    StringBuffer out = new StringBuffer(ilength);

    int ch;

    for(int i = 0; i < ilength; i++ )
    {
      ch = (int) strText.charAt(i);
      switch(ch)
      {
      case (int) '\n':
	out.append("&#0d;");
	break;
      default:
	out.append((char) ch);
	break;
      }
    }

    return out.toString();
  }

 
  public static String convertEntityToNewline(String strText)
  {
    int ilength = strText.length();

    int iState = CharacterEntityBuilder.DEFAULT_STATE;

    StringBuffer out = new StringBuffer(ilength);

    StringBuffer partialEntity = new StringBuffer(4);

    char ch;

    for(int i = 0; i < ilength; i++ )
    {
      ch = strText.charAt(i);

      switch(iState)
      {
      case CharacterEntityBuilder.DEFAULT_STATE:
	if(ch == '&' )
	{
	  iState = CharacterEntityBuilder.READ_AMP;
	}
	else
	{
	  out.append(ch);
	}
	break;
      case CharacterEntityBuilder.READ_AMP:
	if(ch == '#')
	{
	  iState = CharacterEntityBuilder.READ_HASH;
	}
	else
	{
	  out.append("&");
	  out.append(ch);
	  iState = CharacterEntityBuilder.DEFAULT_STATE;
	}
	break;
      case CharacterEntityBuilder.READ_HASH:
	if(ch == '0')
	{
	  iState = CharacterEntityBuilder.READ_ZERO;
	}
	else
	{
	  out.append("&#");
	  out.append(ch);
	  iState = CharacterEntityBuilder.DEFAULT_STATE;
	}
	break;
      case CharacterEntityBuilder.READ_ZERO:
	if(ch == 'd')
	{
	  iState = CharacterEntityBuilder.READ_DEE;
	}
	else
	{
	  out.append("&#0");
	  out.append(ch);
	  iState = CharacterEntityBuilder.DEFAULT_STATE;
	}
	break;
      case CharacterEntityBuilder.READ_DEE:
	if(ch == ';')
	{
	  out.append('\n');
	  iState = CharacterEntityBuilder.DEFAULT_STATE;
	}
	else
	{
	  out.append("&#0d");
	  out.append(ch);
	  iState = CharacterEntityBuilder.DEFAULT_STATE;
	}
	break;
      default:
	//  Something wrong if we get here
	break;
      }
    }
    return out.toString();
  }
}


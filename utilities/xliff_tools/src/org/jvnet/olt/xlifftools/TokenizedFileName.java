
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TokenizedFileName.java
 *
 * Created on 25 August 2003, 13:31
 */

package org.jvnet.olt.xlifftools;

/**
 *
 * @author  jc73554
 */
public class TokenizedFileName
{
    
    private String baseName;
    
    private int sequenceNum;
    
    private String ext;
    
    /** Creates a new instance of TokenizedFileName */
    public TokenizedFileName(String fileName) throws FilenameFormatException
    {
        //  Tokenize the file name. If the file name is in the right format
        //  then the the baseName includes everything up to the last '_' and
        //  the sequenceNum occurs between that and the last '.'
        
        int lastPeriodIndex = fileName.lastIndexOf('.');
        int lastUnderscoreIndex = fileName.lastIndexOf('_');
        
        baseName = fileName.substring(0, lastUnderscoreIndex);               
        String strSeqNum = fileName.substring(lastUnderscoreIndex + 1, lastPeriodIndex);
        ext = fileName.substring(lastPeriodIndex);
        
        //  Put stuff in the correct places.
        try
        {
            sequenceNum = Integer.parseInt(strSeqNum);
        }
        catch(NumberFormatException numEx)
        {
            throw new FilenameFormatException(numEx.getMessage());
        }
    }
    
    public String toString()
    {
        return baseName + "_" + sequenceNum + ".xlz";
    }
    
    public int compareTo(TokenizedFileName tfn)
    {
        int baseCompareRes = this.getBaseName().compareTo(tfn.getBaseName());
        //  If the baseNames are not equal then return based on the comparison
        //  of the baseName strings.
        if(baseCompareRes != 0)
        {
            return baseCompareRes;
        }
        
        //  Next copmare numerical values of the sequence numbers.
        int iThis = this.getSequenceNum();
        int iOther = tfn.getSequenceNum();
        
        if(iThis < iOther) { return -1; }
        if(iThis > iOther) { return 1; }
        
        //  If we have got here, then it is down to the extensions...
        return ( this.getExt().compareTo(tfn.getExt()) );
    }      
    
    //  Accessors
    //
    public String getBaseName()
    {
        return baseName;
    }
    
    public int getSequenceNum()
    {
        return sequenceNum;
    }
    
    public String getExt()
    {
        return ext;
    }
    
}

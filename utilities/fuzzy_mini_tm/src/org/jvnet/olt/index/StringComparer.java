
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.index;
import java.util.*;
import java.util.Arrays;

/**
 * This class contains static methods that calculate the percentage difference between two strings.
 */
public class StringComparer
{
    /**
     * A string comparison method that uses dynamic programming to
     * calculate the number of differences between two strings.
     * The parameter names are a bit misleading as the method should be
     * symmetrical, i.e., it should produce the same result, independent
     * of the ordering of the parameters.
     * @param source The source string to use for the comparison.
     * @param target The target string to use for the comparison.
     * @return An integer describing the number of differences between the two strings passed as parameters.
     */
    public static int compareStrings(String source, String target, boolean bflag)
    {
        //  Work out string lengths
        int srcLen = source.length();
        int tgtLen = target.length();
        
        //  Generate result array: only want output number.
        int[] prevRow = new int[srcLen + 1];
        int[] workingRow = new int[srcLen + 1];
        int[] temp;
        int[] compArray = new int[3];
        
        //  Set boundary conditions
        for(int i = 0; i <= srcLen; i++)
        { workingRow[i] = i; }
        
        int lastValue;
        
        //  Run the dynamic programming algorithm
        for(int j = 1; j <= tgtLen; j++)
        {
            //  Make the last iteration's workingRow be the current prevRow. Reuse
            //  the old prevRow object for the working row, to cut down on object
            //  creation overhead.
            temp = prevRow;
            prevRow = workingRow;
            workingRow = temp;
            
            workingRow[0] = j;
            lastValue = j;
            
            for(int k = 1; k <= srcLen; k++)
            {
                //  Generate the three scenarios
                compArray[0] = lastValue + 1;
                compArray[1] = prevRow[k] + 1;
                compArray[2] = (source.charAt(k-1) == target.charAt(j-1)) ? (prevRow[k-1]) : (prevRow[k-1] + 1);
                
                //  Get the MIN value :
                lastValue = compArray[0];
                if(compArray[1] < lastValue) { lastValue = compArray[1]; }
                if(compArray[2] < lastValue) { lastValue = compArray[2]; }
                
                //  update the array with lastValue
                workingRow[k] = lastValue;
            }
        }
        
        //  Return the last number.
        return workingRow[srcLen];
    }
    
    /**
     * This method calculates the percentage difference between two strings. The percentage difference
     * is calculated by comparing the number of differences between the two strings to the average length
     * of the strings.
     * @param source The source string to use for the comparison.
     * @param target The target string to use for the comparison.
     * @return A float value representing the percentage difference between the two strings.
     */
    public static float calculatePercentMatch(String source, String target)
    {       
        int srcLen = source.length();
        int tgtLen = target.length();
        
        //  If both strings are empty then they have to be identical.
        if((srcLen + tgtLen) == 0) { return (float) 100; } 
        
        int difference = compareStrings(source, target,true);

        if(srcLen == 0) { return (float) (100.0 - ((difference * 100.0)/tgtLen)); }
        if(tgtLen == 0) { return (float) (100.0 - ((difference * 100.0)/srcLen)); }
        
        return (float)(100.0 - ((difference * 100.0 * 2)/(srcLen + tgtLen)));
    }
    
}

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

package org.jvnet.olt.filters.csv;

/**
 * This is a simple class to enscapsulate the different types of fields
 * in a CSV (Comma Separated Value) file
 * @author timf
 */
public class CsvToken {
    
    private String value = "";
    private int type = -1;
    private int fieldOffset = -1;
    
    /** Creates a new instance of CsvToken */
    public CsvToken(String value, int type) {
        this.setValue(value);
        this.setType(type);
    }

    
    public CsvToken(String value, int type, int fieldOffset){
        this.setValue(value);
        this.setType(type);
        this.fieldOffset = fieldOffset;                
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public int getFieldNumber() {
        return fieldOffset;
    }    
    
    public String toString(){
        StringBuffer buf = new StringBuffer();
        buf.append("Value = "+this.value+"\n");
        buf.append("Type = "+CsvTokenTypes.display[this.type]);
        return buf.toString();
       
        
    }
    
    
}

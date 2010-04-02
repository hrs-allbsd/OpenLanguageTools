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
 * DTDToTaggedMarkupConverter.java
 *
 * Created on July 10, 2003, 10:33 AM
 */

package org.jvnet.olt.parsers.tagged;
import java.util.Map;
import java.util.HashMap;
/**
 * This is badly written - it will silently break if there's major changes to
 * the dtd parser : any time we add new productions to the dtd parser it'll need to be updated, but
 * there's no dependency on the code, so you won't get compilation errors :-(
 *
 * For now, it'll do. In future, we should be putting this code in the dtd doc
 * fragment parser package, and using the static final ints declared there, instead
 * of re-declaring our own new ones.
 *
 * @author  timf
 */
public class DTDToTaggedMarkupConverter implements NodeTypeConverter, TaggedMarkupNodeConstants {
    
    /// DTD parser tree constants
    
    public int JJTFILE = 0;
    public int JJTINTERNAL_SUB_SET = 1;
    public int JJTINTERNAL_SUB_SET_WS = 2;
    public int JJTINTERNAL_SUB_SET_COMMENT = 3;
    public int JJTENTITY_DECL = 4;
    public int JJTNAMED_ENTITY_DECL = 5;
    public int JJTNDATA_ENTITY_DECL = 6;
    public int JJTPARAMETER_ENTITY_DECL = 7;
    public int JJTENTITY_DECL_BEGIN = 8;
    public int JJTENTITY_DECL_NAME = 9;
    public int JJTENTITY_DECL_SPACE = 10;
    public int JJTENTITY_DECL_VALUE = 11;
    public int JJTENTITY_DECL_CLOSE = 12;
    public int JJTNOTATION_DECL = 13;
    public int JJTELEMENT_DECL = 14;
    public int JJTSGML_DATA = 15;
    public int JJTCDATA = 16;
    public int JJTINT_ENTITY = 17;
    public int JJTPCDATA = 18;
    public int JJTEOF = 19;
    
    
    
    private Map dm;
    
    /** Creates a new instance of DTDToTaggedMarkupConverter */
    public DTDToTaggedMarkupConverter()  {
        dm = new HashMap();
        
        dm.put(new Integer(JJTFILE),new Integer(FILE));
        dm.put(new Integer(JJTINTERNAL_SUB_SET),new Integer(INTERNAL_SUB_SET));
        dm.put(new Integer(JJTENTITY_DECL),new Integer(ENTITY_DECL));
        dm.put(new Integer(JJTCDATA),new Integer(CDATA));
        dm.put(new Integer(JJTINT_ENTITY),new Integer(INT_ENTITY));
        dm.put(new Integer(JJTPCDATA),new Integer(PCDATA));
        dm.put(new Integer(JJTEOF),new Integer(EOF));
        
    }
    
    /** Converts a node type from a DTDDocFragmentParser node, to a
     *  generic TaggedMarkupNode type
     */
    public int convert(int originalNodeType) {
        Integer result = (Integer)dm.get(new Integer(originalNodeType));
        if (result != null){
            return result.intValue();
        } else {
            return -1;
        }
    }
    
}

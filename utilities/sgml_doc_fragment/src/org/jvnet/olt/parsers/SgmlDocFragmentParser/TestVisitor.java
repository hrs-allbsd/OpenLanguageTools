
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TestVisitor.java
 *
 * Created on May 14, 2003, 11:26 AM
 */

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import org.jvnet.olt.parsers.tagged.*;
/**
 *
 * @author  timf
 */
public class TestVisitor implements org.jvnet.olt.parsers.tagged.TaggedMarkupVisitor {
    
    /** Creates a new instance of TestVisitor */
    public TestVisitor() {
    }
    
    public Object visit(TaggedMarkupNode node, Object data) {
        System.out.println(node+" : "+node.getNodeData());
        System.out.println("msflag = "+node.getMarkedSectFlag());
        return data;
    }
    
}

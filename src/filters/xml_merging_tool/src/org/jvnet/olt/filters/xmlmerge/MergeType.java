
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * MergeType.java
 */

package org.jvnet.olt.filters.xmlmerge;

/**
 *Defines four merging types.<br>
 *1.MERGE_BY_SAME_ORDER<br>
 *The order of the source element in the original file is the same as in the translated file<br>
 *<br>
 *  2.MERGE_BY_SOURCE_ID<br>
 *  the source element has a ID attribute.Such as : <br>
 *<blockquote><pre>&lt;unit&gt;<br>
 *&lt;source id=&quot;1&quot; lang=&quot;en-US&quot;&gt;Foo&lt;/source&gt;<br>
 *&lt;/unit&gt;<br>
 *&lt;unit&gt;<br>
 *&lt;source id=&quot;2&quot; lang=&quot;en-US&quot;&gt;Bla &lt;/source&gt;<br>
 *&lt;source id=&quot;3&quot; lang=&quot;en-US&quot;&gt;This is&lt;b&gt;bold text&lt;/b&gt; so there&lt;/source&gt;<br>
 *&lt;/unit&gt;</pre></blockquote><br>
 *  <br>
 *  3.MERGE_BY_PARENT_ELEMETN_ID<br>
 *  1)the source element ' parent element has a ID&lt;br&gt;<br>
 *  2)the order of source element in original file is the same as in translated file
 *  Such as: <br>
 *<blockquote><pre>&lt;unit id=&quot;a1&quot;&gt;<br>
 *&lt;source id=&quot;1&quot; lang=&quot;en-US&quot;&gt;Foo&lt;/source&gt;<br>
 *&lt;/unit&gt;<br>
 *&lt;unit id=&quot;a2&quot;&gt;<br>
 *&lt;source id=&quot;2&quot; lang=&quot;en-US&quot;&gt;Bla &lt;/source&gt;<br>
 *&lt;source id=&quot;3&quot; lang=&quot;en-US&quot;&gt;This is&lt;b&gt;bold text&lt;/b&gt; so there&lt;/source&gt;<br>
 *&lt;/unit&gt;</pre></blockquote>
 * 4.MERGE_XLIFF<br>
 *   Merge two xliff file
 */
public class MergeType {
    
    public final static int MERGE_BY_SAME_ORDER = 0;
    public final static int MERGE_BY_SOURCE_ID = 1;
    public final static int MERGE_BY_PARENT_ELEMENT_ID =2;
    public final static int MERGE_XLIFF = 3;
    
    private final static int MERGE_TYPE_NUMBER = 4;
    
    /** if the mergeType is illegal, return true
     * else return false
     */
    public static boolean isIllegalType(int mergeType){
        if( (mergeType>-1) && (mergeType<MERGE_TYPE_NUMBER) ){
            return false;
        }else{
            return true;
        }
    }     
    
    /** if the mergeType is illegal , IllegalArgumentException will be thrown
     * else ,do nothing
     * @exception IllegalArgumentException if the mergeType is illegal
     */
    public static void validate(int mergeType){        
        if(MergeType.isIllegalType(mergeType)){
            throw new IllegalArgumentException("The mergeType is illegal! It should be >=0 and <" + 
                            MERGE_TYPE_NUMBER +"!");
        }
    }
}

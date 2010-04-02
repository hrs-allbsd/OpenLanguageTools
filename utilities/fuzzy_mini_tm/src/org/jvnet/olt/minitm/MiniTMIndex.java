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

package org.jvnet.olt.minitm;

import org.jvnet.olt.index.BasicDataStore;

public interface MiniTMIndex {
    /** 
     */
    public void openDataStore(DataStore store, FormatRemovingStrategy fr) throws MiniTMException;
    
    
    /**
     *  This method inserts a new entry in the fuzzy index that points to
     *  the TMUnit represented by dataStoreKey. The index is passed the
     *  string toIndex to be used to help retrieve items from the index.
     */
    public void insertItem(String toIndex, long dataStoreKey)
    throws MiniTMIndexException;
    
    /**
     *  This method searches the index for entries that are close to the
     *  query string. The degree of closeness is specified by the
     *  matchThreshold parameter.
     */
    public SearchResult[] doSearch(String queryString, int matchThreshold)
    throws MiniTMIndexException;
    
    /**
     *  This method removes the entry from the database that matches the
     *  string toRemove and points to the TMUnit represented by
     *  dataSourceKey.
     */
    public boolean removeItem(String toRemove, long dataSourceKey);
    
    /**
     *  This function checks if there is an entry in the index that indexes
     *  the query string. If there is at least one it returns true.
     */
    public boolean inIndex(String queryString);
}



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
package org.jvnet.olt.xliff;


/**
 * <p>Title: A Class To Tracking Which Segment Is Grouped</p>
 */
import java.util.*;

/* TODO: we would need to refactor the class, so that TransUnitIDs
 *      are of Class TransUnitID and not simple strings
 */
public class TrackingGroup {
    private HashMap idGroupAttrsMap;
    private HashMap idGroupMap;
    private HashMap idSentenceMap;

    //reverse lookup map;
    //the idGroupMap looks like this: "1" -> {"1","2","3"}
    // this is revrse: "1" -> "1"  "2"->"1" "3"->"1"
    private Map unit2groupMap;

    /**
     * variable to indicate if current selected segment is in group
     */
    private boolean shownAsGrouped;

    /**
     * variable to indicate the group of the file changes
     */
    private boolean groupChanged;

    public class Sentence {
        int theRow = -1;
        int theStatus = -1;
        int theType = -1;
        String theTranslation = null;

        public Sentence() {
        }

        public Sentence(int aRow, String aTranslation, int aStatus, int aType) {
            theRow = aRow;
            theTranslation = new String(aTranslation);
            theStatus = aStatus;
            theType = aType;
        }

        public int getTheRow() {
            return theRow;
        }

        public int getTheStatus() {
            return theStatus;
        }

        public int getTheType() {
            return theType;
        }

        public String getTheTranslation() {
            return theTranslation;
        }
    }

    public TrackingGroup() {
        idGroupMap = new HashMap();
        idSentenceMap = new HashMap();
        idGroupAttrsMap = new HashMap();

        unit2groupMap = new HashMap();
    }

    /** gets list of trans unit ids for the first trans unit id in a group
     *
     * @param trId
     * @return the list of trans unit ids in group or null
     */
    private List findList(String trId) {
        //is there such trans-unit ?
        if (unit2groupMap.containsKey(trId)) {
            String grId = (String)unit2groupMap.get(trId);

            return (List)idGroupMap.get(grId);
        }

        return null;
    }

    public void addGroup(String key, List aList, boolean bGroupChanged, Map attrs) {
        idGroupMap.put(key, aList);
        idGroupAttrsMap.put(key, attrs);

        if (bGroupChanged) {
            groupChanged = true;
        }

        //build the reverse map
        if (aList != null) {
            for (Iterator i = aList.iterator(); i.hasNext();) {
                Object o = i.next();
                unit2groupMap.put(o, key);
            }
        }
    }

    /** are the trans units in the same group ?
     *
     * @param trId1 trans-unit 1
     * @param trId2 trans-unit 2
     * @return true if the groups are the same in any other case false
     */
    public boolean inTheSameGroup(String trId1, String trId2) {
        boolean firstIn = unit2groupMap.containsKey(trId1);
        boolean scndIn = unit2groupMap.containsKey(trId2);

        return (firstIn && scndIn) ? unit2groupMap.get(trId1).equals(unit2groupMap.get(trId2)) : false;
    }

    public void addGroup(String key, List aList, boolean bGroupChanged) {
        addGroup(key, aList, bGroupChanged, new HashMap());
    }

    public void removeGroup(String key) {
        /* timf - not allowing this to happen anymore, why would
         * we want to delete a group ??
         idGroupMap.remove(key);
         groupChanged = true;
        */
    }

    /**
     * This method returns the groupID of any given
     * TransUnit. If the transunit could not be found
     * in any known group, we return a null String
     *
     * @return the groupID the transUnit is in, or null if the transunit is not in a group
     */
    public String getGroupId(String transUnitId) {
        return unit2groupMap.containsKey(transUnitId) ? (String)unit2groupMap.get(transUnitId) : null;
    }

    public Map getGroupAttrs(String groupKey) {
        return (Map)idGroupAttrsMap.get(groupKey);
    }

    public int isTransunitIdInGroup(String theUnitId) {
        if (idGroupMap.containsKey(theUnitId)) {
            return 0;
        } else {
            List l = findList(theUnitId);

            return (l == null) ? (-1) : 1;
        }
    }

    public List getListOfOneGroup(String theUnitId) {
        if (idGroupMap.containsKey(theUnitId)) {
            return (List)idGroupMap.get(theUnitId);
        } else {
            return new ArrayList();
        }
    }

    public int getSizeOfOneGroup(String theUnitId) {
        List l = findList(theUnitId);

        return (l == null) ? 0 : l.size();
    }

    public String getFirstIdOfOneGroup(String theUnitId) {
        List l = findList(theUnitId);

        return (l == null) ? null : (String)l.get(0);
    }

    public void setShownAsGrouped(boolean bInput) {
        shownAsGrouped = bInput;
    }

    public boolean isShownAsGrouped() {
        return shownAsGrouped;
    }

    public void setGroupChanged(boolean bInput) {
        groupChanged = bInput;
    }

    public boolean isGroupChanged() {
        return groupChanged;
    }

    public void addSentence(String key, int row, String translation, int status, int type) {
        Sentence aSentence = new Sentence(row, translation, status, type);
        idSentenceMap.put(key, aSentence);
    }

    public void removeSentence(String key) {
        idSentenceMap.remove(key);
    }

    public int getRowForId(String key) {
        Sentence aSen = (Sentence)idSentenceMap.get(key);

        return aSen.getTheRow();
    }

    public int getTransStatusForId(String key) {
        Sentence aSen = (Sentence)idSentenceMap.get(key);

        return aSen.getTheStatus();
    }

    public int getTransTypeForId(String key) {
        Sentence aSen = (Sentence)idSentenceMap.get(key);

        return aSen.getTheType();
    }

    public String getTranslationForId(String key) {
        Sentence aSen = (Sentence)idSentenceMap.get(key);

        return aSen.getTheTranslation();
    }
}

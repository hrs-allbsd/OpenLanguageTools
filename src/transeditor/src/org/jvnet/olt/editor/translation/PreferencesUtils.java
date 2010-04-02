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
 * PreferencesUtils.java
 *
 * Created on June 10, 2005, 6:19 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.jvnet.olt.editor.translation;

import java.text.DecimalFormat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 *
 * @author boris
 */
class PreferencesUtils {
    /** Creates a new instance of PreferencesUtils */
    private PreferencesUtils() {
    }

    /** reads list from preferences
    *
    * reads all keys of a node as Strings into a list. If a any value is duplicated
    * then the it will only occur at the last position of its occurence
    *
    * This method also clears the target list !
    */
    static void readListFromPrefs(Preferences node, List targetList) throws BackingStoreException {
        targetList.clear();

        String[] keys = node.keys();

        for (int i = 0; (keys != null) && (i < keys.length); i++) {
            String value = node.get(keys[i], null);

            if (value == null) {
                continue;
            }

            //this looks stupid, but we should place nodes only one into the list; 
            //if they already are there, at them on the end
            if (targetList.contains(value)) {
                targetList.remove(value);
            }

            targetList.add(value);
        }
    }

    /** write list to node
     *
     *  Writes a list of values a Node's children. The keys are names '<prefix>%05d'
     *
     *  where prefix is supplied param and %0d is index number prefixed with zeros
     *  for sorting purposes
     *
     *
     */
    static void writeListToPrefs(Preferences node, List sourceList, String prefix) throws BackingStoreException {
        DecimalFormat df = new DecimalFormat("00000");

        removeChildKeys(node);

        int cnt = 0;

        for (Iterator i = sourceList.iterator(); i.hasNext();) {
            String value = (String)i.next();

            node.put(prefix + df.format(++cnt), value);
        }
    }

    /** reads a map from preferences
     *
     *  reads first level children of a node. Clears targetMap and puts all reads values
     *  as following:
     *
     *  the map item key  is preferences node key, map value is pref. node's value.
     *
     */
    static void readMapFromPrefs(Preferences node, Map m) throws BackingStoreException {
        m.clear();

        String[] keys = node.keys();

        for (int i = 0; (keys != null) && (i < keys.length); i++)
            m.put(keys[i], node.get(keys[i], ""));
    }

    /** writes a map to preferences
     *
     *  removes all first level children of a node. Copies all key/value pairs from srcmap
     *  as following:
     *
     *  the map item key  is preferences node key, map value is pref. node's value.
     *
     */
    static void writeMapToPrefs(Preferences node, Map m) throws BackingStoreException {
        removeChildKeys(node);

        for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();

            node.put((String)entry.getKey(), (String)entry.getValue());
        }
    }

    static private void removeChildKeys(Preferences node) throws BackingStoreException {
        String[] keys = node.keys();

        for (int i = 0; (keys != null) && (i < keys.length); i++)
            node.remove(keys[i]);
    }
}

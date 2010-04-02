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
 * TrackingSourceContext.java
 *
 * Created on October 5, 2004, 2:26 PM
 */
package org.jvnet.olt.xliff;

import java.util.HashMap;
import java.util.Map;


/**
 * This class allows us to keep track of the source context info that have been
 * provided in the xliff file. Like TrackingComments, we have a map, keyed by the
 * trans-unit id, which maps each one to a Map. The Map referred to by the trans-unit
 * id contains each individual context key/value pairs for that transUnit.
 *
 * @author  timf
 */
public class TrackingSourceContext {
    private Map contextMap = null;

    public static class SourceContextKey {
        public final String groupName;
        public final String contextType;

        public SourceContextKey(String groupName, String contextName) {
            this.groupName = groupName;
            this.contextType = contextName;
        }

        public int hashCode() {
            return (groupName.hashCode() * 37) + contextType.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof SourceContextKey)) {
                return false;
            }

            SourceContextKey other = (SourceContextKey)obj;

            return groupName.equals(other.groupName) && contextType.equals(other.contextType);
        }
    }

    public TrackingSourceContext() {
        contextMap = new HashMap();
    }

    public void addContext(String key, Map theContext) {
        contextMap.put(key, theContext);
    }

    public void setContext(String key, Map theContext) {
        contextMap.put(key, theContext);
    }

    public Map getContext(String key) {
        Map context = (Map)contextMap.get(key);

        if (context == null) {
            return null;
        } else {
            return context;
        }
    }

    public boolean hasContext(String key) {
        Map context = (Map)contextMap.get(key);

        if (context == null) {
            return false;
        } else {
            return true;
        }
    }

    public Map getContextMap() {
        return contextMap;
    }
}

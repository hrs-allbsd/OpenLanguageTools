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
package org.jvnet.olt.xliff.handlers;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;


public class Element {
    public static final Element ROOT_ELEMENT = new Element();
    private final String prefix;
    private final String localName;
    private final String originalQName;
    private AttributesImpl attrs;
    private final String path;
    private String qName;
    private String toString;
    private SortedSet namespaces;

    public class Namespace implements Comparable {
        private final String prefix;
        private final String uri;

        Namespace(String prefix, String uri) {
            this.prefix = prefix;
            this.uri = uri;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getURI() {
            return uri;
        }

        public String toString() {
            return "xmlns" + ((prefix == null || prefix.length() == 0 ) ? "" : (":" + prefix)) + "=\"" + uri + "\"";
        }

        public int compareTo(Object o) {
            Namespace other = (Namespace)o;

            if ((prefix == null) && (other.prefix != null)) {
                return -1;
            }

            if ((prefix != null) && (other.prefix == null)) {
                return 1;
            }

            if ((prefix == null) && (other.prefix == null)) {
                return uri.compareTo(other.uri);
            }

            int x = prefix.compareTo(other.prefix);

            if (x != 0) {
                return x;
            }

            return uri.compareTo(other.uri);
        }
    }

    private Element() {
        if (ROOT_ELEMENT != null) {
            throw new IllegalStateException("ROOT_ELEMENT already defined");
        }

        prefix = localName = originalQName = null;
        attrs = null;
        path = "/";
    }

    public Element(String prefix, String localName, String originalQName, AttributesImpl attrs, String path) {
        if (originalQName == null) {
            throw new NullPointerException("originalQName may not be null");
        }

        if (localName == null) {
            throw new NullPointerException("localName may not be null");
        }

        if (path == null) {
            throw new NullPointerException("path may not be null");
        }

        this.prefix = prefix;
        this.localName = localName;
        this.originalQName = originalQName;

        if (attrs != null) {
            this.attrs = attrs;
        } else {
            this.attrs = new AttributesImpl();
        }

        this.path = path;
    }

    public String toString() {
        if (this == ROOT_ELEMENT) {
            return "RootElement placeholder";
        }

        if (toString == null) {
            toString = "Element " + ((prefix == null) ? "" : (prefix + ":")) + localName + " (" + originalQName + ")";
        }

        return toString;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getLocalName() {
        return localName;
    }

    public String getQName() {
        if (qName == null) {
            if (prefix != null) {
                qName = prefix + ":" + localName;
            } else {
                qName = localName;
            }
        }

        return qName;
    }

    public AttributesImpl getAttrs() {
        return attrs;
    }

    public String getPath() {
        return path;
    }

    public String getOriginalQName() {
        return originalQName;
    }

    public void addNamespaceDeclaration(String nsName, String uri) {
        if (namespaces == null) {
            namespaces = new TreeSet();
        }

        namespaces.add(new Namespace(nsName, uri));
    }

    public Set getNamespacesDeclarations() {
        if (namespaces == null) {
            return Collections.EMPTY_SET;
        }

        return Collections.unmodifiableSet(namespaces);
    }

    public void removeNamespaceDeclaration(Element.Namespace ns) {
        if (namespaces == null) {
            return;
        }

        namespaces.remove(ns);
    }
}

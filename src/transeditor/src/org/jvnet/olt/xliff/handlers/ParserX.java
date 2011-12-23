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
 * ParserX.java
 *
 * Created on April 7, 2005, 5:32 PM
 *
 */
package org.jvnet.olt.xliff.handlers;

import java.io.InputStream;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import org.jvnet.olt.xliff.ReaderException;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;


/**
 *
 * @author boris
 */
public class ParserX extends DefaultHandler {
    static private Logger logger = Logger.getLogger(ParserX.class.getName());
    static private int unmappedIdx = 0;
    private Locator locator;

    //all elements on stack
    private Stack elementStack = new Stack();

    //path -> handler map
    private Map allHandlers = new HashMap();

    //stack of existing handlers
    private Stack handlerStack = new Stack();
    private Set handledElements = new HashSet();

    //The mapping works like this:
    //parser maps URI to prefix; in order to keep the paths independent of the actual prefix
    //in the xml we will map URI to 'our' prefix, which means we need to add one more level
    //of translation of the prefixes.
    // translate uri -> prefix
    private Map uriPrefixMap = new HashMap();

    //translate preifx -> path
    private Map customUriPrefixMap = new HashMap();

    //Map of URIs that the doc contains but we'return not able to translate
    //these elements will be assigned random prefix thus they will never be matched
    private Map unmappedUris = new HashMap();

    //element mapped to handler which is processing it
    private Map elementHandlerMap = new HashMap();

    // A stack to keeep track of xmlns declarations as they happen.
    // this allows us to deal with each declaration only once.
    private Stack xmlnsDeclarationStack = new Stack();

    /** Creates a new instance of ParserX */
    public ParserX() {
        elementStack.push(Element.ROOT_ELEMENT);
    }

    public void startPrefixMapping(String prefix, String uri) throws org.xml.sax.SAXException {
        String newPrefix = prefix;

        if (newPrefix == null) {
            newPrefix = (String)customUriPrefixMap.get(uri);
        }

        if (newPrefix == null) {
            newPrefix = "x" + ++unmappedIdx;
        }

        uriPrefixMap.put(uri, newPrefix);
        xmlnsDeclarationStack.push(new String[]{uri, newPrefix});

        logger.finer("Mapping URI " + uri + " with prefix '" + prefix + "' to prefix '" + newPrefix + "'");

        /*if(uri != null && uri.trim().length() > 0)
            uriPrefixMap.put(uri,prefix);

        logger.finer("Prefix mapping: '"+prefix+"' -> "+uri);
         */
    }

    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attrs) throws org.xml.sax.SAXException {
        String prefix = translateURI2Prefix(uri);

        /*        if ((prefix != null) && (prefix.trim().length() == 0)) {
                    prefix = null;
                }
         */

        //logger.finer("Prefix:"+prefix);
        //if(elementStack.isEmpty())
        //    throw new IllegalStateException("RootElement missing on stack");
        Element parent = (Element)elementStack.peek();
        String path = parent.getPath() + ((prefix == null) ? "" : (prefix + ":")) + localName + "/";

        AttributesImpl attrsImpl = new AttributesImpl(attrs);
        // cycle through the attributes and map namespaces
        for (int i = 0; i < attrsImpl.getLength(); i++) {
            // remove namespace "attributes" - will namespace declaration is added to the xliff-element
            String attrName = attrsImpl.getQName(i);
            if ( attrName.startsWith("xmlns:") ) {
                // NOTE (SJH Feb. 28, 2010
                // It seems like this section will never get
                // executed because xmlns attributes don't seem
                // to be included in the attrsImpl provided
                // They seem to be parsed out instead and handled
                // in the startPrefixMapping method.

                // The above comment is not always true.
                // Behavior depends on the XLIFF file.
                // With some the following line is executed.
                attrsImpl.removeAttribute(i);
                
            } else {
                String attrURI=attrsImpl.getURI(i);
                if (  attrURI.length() > 0 ) {
                    String attrPrefix=translateURI2Prefix(attrsImpl.getURI(i));
                    // set QName only if URI has been mapped
                    if ( attrPrefix != null ) {
                        attrsImpl.setQName(i, attrPrefix + ":" + attrsImpl.getLocalName(i));
                    }
                }
            }
        }

        //logger.finer("Path:"+path);
        Element elem = new Element(prefix, localName, qName, attrsImpl, path);
        
        // MOD ADD START SJH100228 Fixing bug with namespaces not being retained.
        // Ref: https://open-language-tools.dev.java.net/servlets/ReadMsg?list=dev&msgNo=617
        // New: http://java.net/projects/open-language-tools/lists/dev/archive/2010-02/
        while ( !xmlnsDeclarationStack.empty() ){
            String[] ns = (String[]) xmlnsDeclarationStack.pop();
            elem.addNamespaceDeclaration(ns[1], ns[0]);
        }
        // MOD END SJH100228

        // MOD REMOVE START SJH100228 --->
        // Since we're handling all namespaces in a generic way now,
        // we don't need to have this special case for the xliff namespace

        //if ( qName.equals("xliff")) {
            // att namespace declarations to the xliff element
            // TODO: find a better place to do this
            
            //Set keySet = uriPrefixMap.keySet();
            //for (Object key : uriPrefixMap.keySet()) {
            //  if ( parent.
            //   elem.addNamespaceDeclaration( uriPrefixMap.get(key).toString(),  key.toString() );
            //}
        //}

        // <--- MOD REMOVE END SJH100228
        elementStack.push(elem);

        Handler h = null;

        boolean hasHandler = false;

        Object[] aHandlers = allHandlers.keySet().toArray();
        for ( int i=0; i<aHandlers.length && h == null ; i++  ) {
            if ( path.matches(aHandlers[i].toString()) )
                h = (Handler)allHandlers.get(aHandlers[i].toString());
        }

        //if (allHandlers.containsKey(path)) {
        if ( h != null) {
            //h = (Handler)allHandlers.get(path);
            handlerStack.push(h);

            elementHandlerMap.put(elem, h);

            hasHandler = true;
        } else if (!handlerStack.isEmpty()) {
            h = (Handler)handlerStack.peek();
        } else {
            //logger.finer("no handler available");
            return;
        }

        if ((h != null) && (hasHandler || h.handleSubElements())) {
            h.setElementStack(elementStack);
            h.setHandlerStack(handlerStack);

            try {
                h.dispatch(elem, true);
            } catch (ReaderException re) {
                logger.info(" last elem to dispatch: " + elem.getPath());
                logger.info("            attributes: " + elem.getAttrs().toString());
                throw new SAXException(re);
            } catch (Exception ex) {
                logger.info(" last elem to dispatched: " + elem.getPath());
                logger.info("              attributes: " + elem.getAttrs().toString());
                throw new SAXException(ex);
            }

            handledElements.add(elem);
        }
    }

    public void endElement(String uri, String localName, String qName) throws org.xml.sax.SAXException {
        //if(elementStack.isEmpty())
        //    throw new IllegalStateException("Stack is empty");
        Element elem = (Element)elementStack.pop();

        if (!handlerStack.isEmpty() && handledElements.contains(elem)) {
            Handler h = (Handler)handlerStack.peek();
            h.setElementStack(elementStack);
            h.setHandlerStack(handlerStack);

            try {
                h.dispatch(elem, false);
            } catch (ReaderException re) {
                logger.info(" last elem to dispatched: " + elem.getPath());
                logger.info("              attributes: " + elem.getAttrs().toString());
                throw new SAXException(re);
            } catch (Exception ex) {
                logger.info(" last elem to dispatched: " + elem.getPath());
                logger.info("              attributes: " + elem.getAttrs().toString());
                throw new SAXException(ex);
            }
        }

        //if(elementHandlerMap.containsKey(elem)){
        if (elementHandlerMap.remove(elem) != null) {
            handlerStack.pop();
        }
    }

    public void addHandler(String path, Handler hndlr) {
        if ((path == null) || (hndlr == null)) {
            throw new NullPointerException(((path == null) ? "path" : "hndlr") + " is null");
        }

        if ( path.startsWith("/") && !path.endsWith("/")) {
            path = path + "/";
        }

        allHandlers.put(path, hndlr);
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        dispatchChars(ch, start, length, true);
    }

    public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        dispatchChars(ch, start, length, false);
    }

    private void dispatchChars(char[] ch, int start, int length, boolean ignorable) throws SAXException {
        if (handlerStack.isEmpty()) {
            return;
        }

        Handler h = (Handler)handlerStack.peek();

        if (h != null) {
            Element e = (Element)elementStack.peek();

            if (handledElements.contains(e)) {
                try {
                    if (ignorable) {
                        h.dispatchIgnorableChars(e, ch, start, length);
                    } else {
                        h.dispatchChars(e, ch, start, length);
                    }
                } catch (ReaderException re) {
                    throw new SAXException(re);
                }
            }
        }
    }

    protected String translateURI2Prefix(String uri) {
        String p = (String)uriPrefixMap.get(uri);

        return ((p != null) && "".equals(p)) ? null : p;

        /*
        if(uriPrefixMap.containsKey(uri)){
            String parserPrefix = (String)uriPrefixMap.get(uri);

            if(customUriPrefixMap.containsKey(parserPrefix)){
                return (String)customUriPrefixMap.get(parserPrefix);
            }

            return parserPrefix;
        }
        return null;
         */
    }

    /** set the prefix mapping.
     *
     * The prefix map should contains a key whcih is URI to and value which the expected prefix.
     *
     */
    public void setPrefixMap(Map prefixMap) {
        customUriPrefixMap.clear();
        customUriPrefixMap.putAll(prefixMap);
    }

    public Map getPrefixMap(Map prefixMap) {
        return Collections.unmodifiableMap(customUriPrefixMap);
    }

    //Error handling below:
    void location(SAXParseException spe) {
        logger.info("Location: ");

        if (locator == null) {
            logger.info("?");
        } else {
            logger.info("Line:" + spe.getLineNumber() + " col:" + spe.getColumnNumber());
        }
    }

    public void warning(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
        logger.warning("warning Exception:" + exception);
        location(exception);
    }

    public void fatalError(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
        logger.severe("fatalError Exception:" + exception);
        location(exception);
        throw exception;
    }

    public void error(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
        logger.severe("error Exception:" + exception);
        location(exception);

        throw exception;
    }

    public void processingInstruction(String target, String data) throws org.xml.sax.SAXException {
        logger.finer("PI");
    }

    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) {
        logger.finer("UED");
    }

    public void notationDecl(String name, String publicId, String systemId) {
        logger.finer("NOT");
    }

    public void setDocumentLocator(org.xml.sax.Locator locator) {
        this.locator = locator;
    }

    public void startDocument() throws org.xml.sax.SAXException {
        uriPrefixMap.putAll(customUriPrefixMap);

        logger.finer("Prefix map:" + uriPrefixMap);
    }
}


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.converterhandler;

/**
 * <p>Title: Alignment editor</p>
 * <p>Description: part of TMCi editor</p>
 * @author Charles
 * @version 1.0
 */

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

/**
 * This class implements the ContentHandler interface
 * and dispatches the parsing events to methods
 * that must be implemented by subclasses.
 * This is an "XML element-Java method" mapping mechanism.
 *
 * <P>For each element type, there must be a method
 * named as the element and suffixed by "Element",
 * which accepts the values of the attributes as parameters.
 * There must also be a method suffixed by "Data" that
 * takes the character data of the element as a string
 * parameter. An optional method suffixed by "ElementEnd"
 * would be called when the parser meets the end tag
 * of an element.
 *
 * <P>The data of a processing instruction is passed to
 * a method whose name starts with the target string
 * and is suffixed by "Proc".
 *
 * <P>All these methods must be public.
 */

public class XMLHandler implements ContentHandler {
  protected Locator documentLocator;
  protected Hashtable prefixMappings;
  protected Stack namespaceURIStack;
  protected Stack localNameStack;
  protected Stack qualifiedNameStack;

  /**
   * Returns the types of the parameters of a given method.
   * It gets an array of all public methods of this class
   * using getClass().getMethods(). Then it iterates over
   * the elements of this array and returns the first
   * method whose name matches the value of the parameter.
   * Subclasses could override this method to make this
   * process more efficient.
   */
  protected Class[] getParameterTypes(String methodName) {
    Method methods[] = getClass().getMethods();
    for (int i = 0; i < methods.length; i++)
      if (methods[i].getName().equals(methodName))
        return methods[i].getParameterTypes();
    return null;
  }
  /**
   * Invokes a given method whose name is given as first
   * parameter. The second parameter can be a string or
   * an Attributes instance. It can also be null.
   *
   * <P>If param is an Attributes instance, this method
   * calls getParameterTypes() to get the types
   * of the parameters of the method which will be invoked.
   * Then it calls getAttributeNames() to get the names
   * of the attributes of the element associated with
   * that method. The values of the attributes
   * are obtained with AttributesUtils.convertValues()
   * and converted to the parameter types with
   * AttributesUtils.convertValues().
   *
   * <P>The method that must be invoked is obtained with
   * getClass().getMethod(name, parameterTypes).
   * This is actually a java.lang.reflect.Method object.
   * We use it to invoke the method with the given name
   * on this object. The call
   *
   * <PRE>method.invoke(this, parameterValues)</PRE>
   *
   * <P>is the equivalent of
   *
   * <PRE>"name".(parameterValues[0], parameterValues[1], ...)</PRE>
   *
   * <P>We use this mechanism for two reasons. First,
   * the name of the invoked method and its list of
   * parameters is not known by this class. Second, this
   * is a general mechanism for mapping parsing events
   * to method calls.
   *
   * <P>For more information, see the Reflection Specification
   * included within the JDK documentation,
   * java.lang.Class and the java.lang.reflect package.
   */
  protected Object invokeMethod(String name, Object param) throws SAXException {
    Class parameterTypes[] = null;
    Object parameterValues[] = null;
    if (param instanceof String) {
      parameterTypes = new Class[] { String.class };
      parameterValues = new Object[] { (String) param };
    } else if (param instanceof Attributes) {
      parameterTypes = getParameterTypes(name);
      parameterValues = new Object[] { new org.xml.sax.helpers.AttributesImpl((Attributes)param)};
    } else if (param == null) {
      parameterTypes = new Class[0];
      parameterValues = new Object[0];
    }

    if (parameterTypes == null || parameterValues == null) {
      System.err.println("Method not supported: " + name);
      return null;
    }

    try {
      Method method = getClass().getMethod(
          name, parameterTypes);
      return method.invoke(this, parameterValues);
    } catch (InvocationTargetException e) {
      Throwable t = e.getTargetException();
      if (t instanceof Exception)
        throw new SAXException((Exception) t);
      else
        throw new SAXException(t.toString());
    } catch (NoSuchMethodException e) {
      if (param != null)
        System.err.println("Method not found or not public: " + name);
      return null;
    } catch (Exception e) {
      throw new SAXException(e);
    }
  }

  /**
   * Receives the document's locator and saves
   * its reference to an instance variable.
   */
  public void setDocumentLocator(Locator locator) {
    documentLocator = locator;
  }

  /**
   * Receives notification of the beginning of a document.
   * The collection objects of this class are created.
   */
  public void startDocument() throws SAXException {
    prefixMappings = new Hashtable();
    namespaceURIStack = new Stack();
    localNameStack = new Stack();
    qualifiedNameStack = new Stack();
  }

  /**
   * Receives notification of the end of a document.
   * The collection objects of this class are freed
   * so that the garbage collector can dispose them.
   */
  public void endDocument() throws SAXException {
    prefixMappings = null;
    namespaceURIStack = null;
    localNameStack = null;
    qualifiedNameStack = null;
    System.gc();
  }

  /**
   * Begins the scope of a prefix-URI Namespace mapping.
   * The mapping is saved to a hashtable
   */
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    prefixMappings.put(prefix, uri);
  }

  /**
   * Ends the scope of a prefix-URI mapping.
   * The mapping is removed from the hashtable.
   */
  public void endPrefixMapping(String prefix)
      throws SAXException {
    prefixMappings.remove(prefix);
  }

  /**
   * Receives notification of the beginning of an element.
   * The namespace URI, local name and qualified name
   * are pushed to their stacks. The method associated
   * with this element is invoked.
   */
  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    namespaceURIStack.push(uri);
    localNameStack.push(localName);
    qualifiedNameStack.push(qName);
    invokeMethod(localName + "Element", atts);
  }

  /**
   * Receives notification of the end of an element.
   * The namespace URI, local name and qualified name
   * are removed from their stacks. The method associated
   * with this element's end, if any, is invoked.
   */
  public void endElement(String uri, String localName, String qName) throws SAXException {
    invokeMethod(localName + "ElementEnd", null);
    namespaceURIStack.pop();
    localNameStack.pop();
    qualifiedNameStack.pop();
  }

  /**
   * Receives notification of character data.
   * The method associated with this element's data
   * is invoked.
   */
  public void characters(char ch[], int start, int length) throws SAXException {
    String localName = (String) localNameStack.peek();
    String data = new String(ch, start, length);
    invokeMethod(localName + "ElementData", data);
  }

  /**
   * Receives notification of ignorable whitespace.
   */
  public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
  }

  /**
   * Receives notification of a processing instruction.
   * The method associated with this processing instruction
   * is invoked.
   */
  public void processingInstruction(String target, String data) throws SAXException {
    invokeMethod(target + "Proc", data);
  }

  /**
   * Receives notification of a skipped entity.
   * A warning message is printed.
   */
  public void skippedEntity(String name) throws SAXException {
    System.err.println("Skipped Entity: " + name);
  }

  /**
   * Creates default error handler used by this parser.
   * @return org.xml.sax.ErrorHandler implementation
   */
  public ErrorHandler getDefaultErrorHandler() {
    return new ErrorHandler() {
      public void error(SAXParseException ex) throws SAXException {
        System.err.println("Missing DOCTYPE.");
        throw ex;
      }

      public void fatalError(SAXParseException ex) throws SAXException {
        throw ex;
      }

      public void warning(SAXParseException ex) throws SAXException {
        // ignore
      }
    };
  }
}

package org.exolab.xml;

import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

public final class Namespaces {
  public static final String XML_NAMESPACE_PREFIX = "xml";
  public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
  private Namespace _first = null;
  private Namespace _last = null;
  private Namespaces _parent = null;
  private static final String CDATA = "CDATA";
  private static final String XMLNS = "xmlns";
  public Namespaces() {}
  public Namespaces(Namespaces parent)
  {
    this._parent = parent;
  }
  public synchronized void addNamespace(String prefix, String uri) {
    if (uri == null) {
      throw new IllegalArgumentException("Namespace URI must not be null");
    }
    if (prefix == null) {
      prefix = "";
    }
    if ("xml".equalsIgnoreCase(prefix)) {
      if (!"http://www.w3.org/XML/1998/namespace".equals(uri)) {
        String err = "The prefix 'xml' is reserved (XML 1.0 Specification) and cannot be declared.";
        
        throw new IllegalArgumentException(err);
      }
      return;
    }
    if ("http://www.w3.org/XML/1998/namespace".equals(uri)) {
      String err = "The namespace 'http://www.w3.org/XML/1998/namespace";
      err = err + "' is reserved (XML 1.0 Specification) and cannot be declared.";
      throw new IllegalArgumentException(err);
    }
    if (this._first == null) {
      this._first = new Namespace(prefix, uri);
      this._last = this._first;
    }
    else {
      boolean found = false;
      Namespace ns = this._first;
      while (ns != null)
      {
        if (ns.prefix.equals(prefix))
        {
          found = true;
          ns.uri = uri;
          break;
        }
        ns = ns.next;
      }
      if (!found) {
        this._last.next = new Namespace(prefix, uri);
        this._last = this._last.next;
      }
    }
  }
  
  public Namespaces createNamespaces()
  {
    return new Namespaces(this);
  }
  public Enumeration getLocalNamespaces()
  {
    return new NamespaceEnumerator(this._first);
  }
  public String getNamespaceURI(String prefix) {
    if (prefix == null) {
      prefix = "";
    }
    Namespace ns = this._first;
    while (ns != null) {
      if (ns.prefix.equals(prefix)) {
        return ns.uri;
      }
      ns = ns.next;
    }
    if (this._parent != null) {
      return this._parent.getNamespaceURI(prefix);
    }
    if ("xml".equals(prefix)) {
      return "http://www.w3.org/XML/1998/namespace";
    }
    return null;
  }
  public String getNamespacePrefix(String nsURI) {
    if (nsURI == null) {
      throw new IllegalArgumentException("Namespace URI must not be null.");
    }
    Namespace ns = this._first;
    while (ns != null) {
      if (ns.uri.equals(nsURI)) {
        return ns.prefix;
      }
      ns = ns.next;
    }
    if (this._parent != null) {
      return this._parent.getNamespacePrefix(nsURI);
    }
    if ("http://www.w3.org/XML/1998/namespace".equals(nsURI)) {
      return "xml";
    }
    return null;
  }
  
  public Enumeration getLocalNamespacePrefixes()
  {
    return new NamespaceEnumerator(this._first, 1);
  }
  
  public String[] getNamespacePrefixes(String nsURI)
  {
    return getNamespacePrefixes(nsURI, false);
  }
  
  public String[] getNamespacePrefixes(String nsURI, boolean local) {
    if (nsURI == null) {
      throw new IllegalArgumentException("Namespace URI must not be null.");
    }
    Vector prefixes = new Vector(3);
    getNamespacePrefixes(nsURI, local, prefixes);
    
    String[] pArray = new String[prefixes.size()];
    prefixes.copyInto(pArray);
    return pArray;
  }
  
  public String getNonDefaultNamespacePrefix(String nsURI) {
    if (nsURI == null) {
      throw new IllegalArgumentException("Namespace URI must not be null.");
    }
    Namespace ns = this._first;
    while (ns != null) {
      if ((ns.uri.equals(nsURI)) && 
        (ns.prefix.length() > 0)) {
        return ns.prefix;
      }
      ns = ns.next;
    }
    if (this._parent != null) {
      return this._parent.getNonDefaultNamespacePrefix(nsURI);
    }
    if ("http://www.w3.org/XML/1998/namespace".equals(nsURI)) {
      return "xml";
    }
    return null;
  }
  
  public Namespaces getParent()
  {
    return this._parent;
  }
  
  public synchronized boolean removeNamespace(String prefix) {
    if (prefix == null) {
      return false;
    }
    Namespace ns = this._first;
    Namespace previous = null;
    while (ns != null) {
      if (ns.prefix.equals(prefix)) {
        if (ns == this._first) {
          this._first = this._first.next;
          if (this._last == ns) {
            this._last = null;
          }
        }
        else {
          previous.next = ns.next;
          if (this._last == ns) {
            this._last = previous;
          }
        }
        return true;
      }
      previous = ns;
      ns = ns.next;
    }
    return false;
  }
  
  public void setParent(Namespaces namespaces)
  {
    this._parent = namespaces;
  }
  public void sendEndEvents(ContentHandler handler)
    throws SAXException {
    Namespace ns = this._first;
    while (ns != null) {
      handler.endPrefixMapping(ns.prefix);
      ns = ns.next;
    }
  }
  
  public void sendStartEvents(ContentHandler handler) throws SAXException {
    Namespace ns = this._first;
    while (ns != null) {
      handler.startPrefixMapping(ns.prefix, ns.uri);
      ns = ns.next;
    }
  }
  
  public void declareAsAttributes(AttributeListImpl atts, boolean localOnly) {
    Namespace ns = this._first;
    String attName = null;
    while (ns != null) {
      if (ns.prefix != null) {
        int len = ns.prefix.length();
        if (len > 0) {
          StringBuffer buf = new StringBuffer(6 + len);
          buf.append("xmlns");
          buf.append(':');
          buf.append(ns.prefix);
          attName = buf.toString();
          atts.addAttribute(attName, "CDATA", ns.uri);
        }
        else {
          atts.addAttribute("xmlns", "CDATA", ns.uri);
        }
      }
      else {
        atts.addAttribute("xmlns", "CDATA", ns.uri);
      }
      ns = ns.next;
    }
    if ((!localOnly) && (this._parent != null)) {
      this._parent.declareAsAttributes(atts, false);
    }
  }
  
  private void getNamespacePrefixes(String nsURI, boolean local, Vector prefixes) {
    Namespace ns = this._first;
    while (ns != null) {
      if (ns.uri.equals(nsURI)) {
        prefixes.addElement(ns.prefix);
      }
      ns = ns.next;
    }
    if ((this._parent != null) && (!local)) {
      this._parent.getNamespacePrefixes(nsURI, local, prefixes);
    }
  }
  
  class Namespace {
    String prefix = null;
    String uri = null;
    Namespace next = null;
    
    Namespace() {}
    
    Namespace(String prefix, String uri) {
      this.prefix = prefix;
      this.uri = uri;
    }
  }
  
  static class NamespaceEnumerator
    implements Enumeration {
    public static final int URI = 0;
    public static final int PREFIX = 1;
    private Namespace _namespace = null;
    private int _returnType = 0;
    
    NamespaceEnumerator(Namespace namespace)
    {
      this._namespace = namespace;
    }
    NamespaceEnumerator(Namespace namespace, int returnType) {
      this._namespace = namespace;
      this._returnType = returnType;
    }
    public boolean hasMoreElements()
    {
      return this._namespace != null;
    }
    public Object nextElement() {
      String obj = null;
      if (this._namespace != null)
      {
        if (this._returnType == 0) {
          obj = this._namespace.uri;
        } else {
          obj = this._namespace.prefix;
        }
        this._namespace = this._namespace.next;
      }
      return obj;
    }
  }
}

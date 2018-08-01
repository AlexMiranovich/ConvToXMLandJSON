package org.exolab.xml.util;

import java.util.HashSet;
import java.util.Set;
import org.exolab.types.AnyNode;
import org.exolab.xml.Namespaces;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class AnyNode2SAX2 {
  private AnyNode _node;
  private ContentHandler _handler;
  private Set<AnyNode> _elements;
  private Namespaces _context;
  public AnyNode2SAX2()
  {
    this(null, null);
  }
  public AnyNode2SAX2(AnyNode node)
  {
    this(node, null);
  }
  public AnyNode2SAX2(AnyNode node, Namespaces context) {
    this._elements = new HashSet();
    this._node = node;
    this._context = (context == null ? new Namespaces() : context);
  }
  public void setContentHandler(ContentHandler handler) {
    if (handler == null) {
      throw new IllegalArgumentException("AnyNode2SAX2#setContentHandler 'null' value for handler");
    }
    this._handler = handler;
  }
  public static void fireEvents(AnyNode node, ContentHandler handler)
    throws SAXException {
    fireEvents(node, handler, null);
  }
  public static void fireEvents(AnyNode node, ContentHandler handler, Namespaces context)
    throws SAXException {
    AnyNode2SAX2 eventProducer = new AnyNode2SAX2(node, context);
    eventProducer.setContentHandler(handler);
    if ((node != null) && (context != null) && (node.getNamespacePrefix() != null) && (context.getNamespaceURI(node.getNamespacePrefix()) == null)) {
      handler.startPrefixMapping(node.getNamespacePrefix(), node.getNamespaceURI());
    }
    eventProducer.start();
  }
  public void start()
    throws SAXException {
    if ((this._node == null) || (this._handler == null)) {
      return;
    }
    processAnyNode(this._node, this._handler);
  }
  private void processAnyNode(AnyNode node, ContentHandler handler) throws SAXException {
    if ((this._node == null) || (this._handler == null)) {
      throw new IllegalArgumentException();
    }
    if (!this._elements.add(node)) {
      return;
    }
    if (node.getNodeType() == 1) {
      String name = node.getLocalName();
      AnyNode tempNode = node.getFirstNamespace();
      String prefix = null;
      while (tempNode != null) {
        prefix = tempNode.getNamespacePrefix();
        if (prefix == null) {
          prefix = "";
        }
        String value = tempNode.getNamespaceURI();
        if (value == null) {
          value = "";
        }
        handler.startPrefixMapping(prefix, value);
        if ((value != null) && (value.length() > 0)) {
          this._context.addNamespace(prefix, value);
        }
        tempNode = tempNode.getNextSibling();
      }
      AttributesImpl atts = new AttributesImpl();
      tempNode = node.getFirstAttribute();
      String xmlName = null;
      String value = null;
      String attUri = null;
      String attPrefix = null;
      while (tempNode != null) {
        xmlName = tempNode.getLocalName();
        String localName = xmlName;
        attUri = tempNode.getNamespaceURI();
        if (attUri != null) {
          attPrefix = this._context.getNamespacePrefix(attUri);
        } else {
          attUri = "";
        }
        if ((attPrefix != null) && (attPrefix.length() > 0)) {
          xmlName = attPrefix + ':' + xmlName;
        }
        value = tempNode.getStringValue();
        atts.addAttribute(attUri, localName, xmlName, "CDATA", value);
        tempNode = tempNode.getNextSibling();
      }
      String nsPrefix = node.getNamespacePrefix();
      String nsURI = node.getNamespaceURI();
      String qName = null;
      if ((nsURI != null) && (nsURI.length() > 0)) {
        String tempPrefix = this._context.getNamespacePrefix(nsURI);
        if (tempPrefix != null) {
          nsPrefix = tempPrefix;
        } else {
          this._context.addNamespace(nsPrefix, nsURI);
        }
      } else { nsURI = ""; }
      if (nsPrefix != null) {
        int len = nsPrefix.length();
        if (len > 0) {
          StringBuffer sb = new StringBuffer(len + name.length() + 1);
          sb.append(nsPrefix);
          sb.append(':');
          sb.append(name);
          qName = sb.toString();
        }
        else { qName = name; }
      }
      else { qName = name; }try {
        handler.startElement(nsURI, name, qName, atts); }
      catch (SAXException sx) {
        throw new SAXException(sx);
      }
      tempNode = node.getFirstChild();
      while (tempNode != null) {
        this._context = this._context.createNamespaces();
        processAnyNode(tempNode, handler);
        tempNode = tempNode.getNextSibling();
      }
      try {
        handler.endElement(nsURI, name, qName);
        this._context = this._context.getParent();
        tempNode = node.getFirstNamespace();
        while (tempNode != null) {
          prefix = tempNode.getNamespacePrefix();
          if (prefix == null) {
            prefix = "";
          }
          handler.endPrefixMapping(prefix);
          tempNode = tempNode.getNextSibling();
        }
      } catch (SAXException sx) {
        throw new SAXException(sx);
      }
    }
    else if (node.getNodeType() == 6) {
      String value = node.getStringValue();
      if ((value != null) && (value.length() > 0)) {
        char[] chars = value.toCharArray();
        try {
          handler.characters(chars, 0, chars.length);
        } catch (SAXException sx) {
          throw new SAXException(sx);
        }
      }
    }
  }
}

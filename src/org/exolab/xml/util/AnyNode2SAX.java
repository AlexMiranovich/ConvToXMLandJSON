package org.exolab.xml.util;

import java.util.HashSet;
import org.exolab.types.AnyNode;
import org.exolab.xml.EventProducer;
import org.exolab.xml.Namespaces;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

public class AnyNode2SAX implements EventProducer {
  private AnyNode _node;
  private DocumentHandler _handler;
  private HashSet _elements;
  private Namespaces _context;
  public AnyNode2SAX()
  {
    this(null, null);
  }
  public AnyNode2SAX(AnyNode node)
  {
    this(node, null);
  }
  public AnyNode2SAX(AnyNode node, Namespaces context) {
    this._elements = new HashSet();
    this._node = node;
    this._context = (context == null ? new Namespaces() : context);
  }
  public void setDocumentHandler(DocumentHandler handler) {
    if (handler == null) {
      throw new IllegalArgumentException("AnyNode2SAX#setDocumentHandler 'null' value for handler");
    }
    this._handler = handler;
  }
  public static void fireEvents(AnyNode node, DocumentHandler handler) throws SAXException {
    fireEvents(node, handler, null);
  }
  
  public static void fireEvents(AnyNode node, DocumentHandler handler, Namespaces context) throws SAXException {
    AnyNode2SAX eventProducer = new AnyNode2SAX(node, context);
    eventProducer.setDocumentHandler(handler);
    eventProducer.start();
  }
  
  public void start() throws SAXException {
    if ((this._node == null) || (this._handler == null)) {
      return;
    }
    processAnyNode(this._node, this._handler);
  }
  private void processAnyNode(AnyNode node, DocumentHandler handler)
    throws SAXException {
    if ((node == null) || (handler == null)) {
      throw new IllegalArgumentException();
    }
    if (!this._elements.add(node)) {
      return;
    }
    if (node.getNodeType() == 1) {
      String name = node.getLocalName();
      AttributeListImpl atts = new AttributeListImpl();
      AnyNode tempNode = node.getFirstAttribute();
      String xmlName = null;
      String value = null;
      String attUri = null;
      String attPrefix = null;
      while (tempNode != null) {
        xmlName = tempNode.getLocalName();
        attUri = tempNode.getNamespaceURI();
        if (attUri != null) {
          attPrefix = this._context.getNamespacePrefix(attUri);
        }
        if ((attPrefix != null) && (attPrefix.length() > 0)) {
          xmlName = attPrefix + ':' + xmlName;
        }
        value = tempNode.getStringValue();
        atts.addAttribute(xmlName, "CDATA", value);
        tempNode = tempNode.getNextSibling();
      }
      this._context = this._context.createNamespaces();
      String nsPrefix = node.getNamespacePrefix();
      String nsURI = node.getNamespaceURI();
      tempNode = node.getFirstNamespace();
      String prefix = null;
      while (tempNode != null) {
        prefix = tempNode.getNamespacePrefix();
        value = tempNode.getNamespaceURI();
        if ((value != null) && (value.length() > 0)) {
          this._context.addNamespace(prefix, value);
        }
        tempNode = tempNode.getNextSibling();
      }
      String qName = null;
      if ((nsURI != null) && (nsURI.length() > 0)) {
        String tempPrefix = this._context.getNamespacePrefix(nsURI);
        if (tempPrefix != null) {
          nsPrefix = tempPrefix;
        } else {
          this._context.addNamespace(nsPrefix, nsURI);
        }
      }
      if (nsPrefix != null) {
        int len = nsPrefix.length();
        if (len > 0) {
          StringBuffer sb = new StringBuffer(len + name.length() + 1);
          sb.append(nsPrefix);
          sb.append(':');
          sb.append(name);
          qName = sb.toString();
        }
        else
        {
          qName = name;
        }
      }
      else {
        qName = name;
      }
      try {
        this._context.declareAsAttributes(atts, true);
        handler.startElement(qName, atts);
      }
      catch (SAXException sx) {
        throw new SAXException(sx);
      }
      tempNode = node.getFirstChild();
      while (tempNode != null) {
        processAnyNode(tempNode, handler);
        tempNode = tempNode.getNextSibling();
      }
      try {
        handler.endElement(qName);
        this._context = this._context.getParent();
      }
      catch (SAXException sx) {
        throw new SAXException(sx);
      }
    }
    else if (node.getNodeType() == 6) {
      String value = node.getStringValue();
      if ((value != null) && (value.length() > 0)) {
        char[] chars = value.toCharArray();
        try {
          handler.characters(chars, 0, chars.length);
        }
        catch (SAXException sx) {
          throw new SAXException(sx);
        }
      }
    }
  }
}

package org.exolab.xml.util;

import java.util.Stack;
import org.exolab.types.AnyNode;
import org.exolab.xml.Namespaces;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SAX2ANY implements ContentHandler, DocumentHandler, ErrorHandler {
  private static final String XMLNS_PREFIX = "xmlns";
  private static final int XMLNS_PREFIX_LENGTH = "xmlns".length() + 1;
  private AnyNode _startingNode;
  private AnyNode _node;
  private Stack _nodeStack = new Stack();
  private Stack _namespaces = new Stack();
  private boolean _processNamespace = true;
  private boolean _character = false;
  private Namespaces _context;
  private boolean _wsPreserve = false;
  public SAX2ANY()
  {
    init();
  }
  public SAX2ANY(Namespaces context, boolean wsPreserve) {
    this._context = context;
    this._wsPreserve = wsPreserve;
    init();
  }
  private void init() {
    if (this._context == null) {
      this._context = new Namespaces();
    }
  }
  public void setDocumentLocator(Locator locator) {}
  public void startDocument()
    throws SAXException {}
  public void endDocument()
    throws SAXException {}
  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException {}
  public void processingInstruction(String target, String data)
    throws SAXException {}
  public void skippedEntity(String name)
    throws SAXException {}
  public void startPrefixMapping(String prefix, String uri)
    throws SAXException {
    AnyNode temp = new AnyNode((short)3, null, prefix, uri, null);
    this._namespaces.push(temp);
    if (this._processNamespace) {
      this._context = this._context.createNamespaces();
      this._processNamespace = true;
    }
    this._context.addNamespace(prefix, uri);
  }
  public void endPrefixMapping(String prefix) throws SAXException {
    this._context.removeNamespace(prefix);
  }
  public void startElement(String name, AttributeList atts) throws SAXException {
    this._character = false;
    AnyNode tempNode = null;
    this._context = this._context.createNamespaces();
    String prefix = "";
    String namespaceURI = null;
    int idx = name.indexOf(':');
    if (idx >= 0) {
      prefix = name.substring(0, idx);
    }
    namespaceURI = this._context.getNamespaceURI(prefix);
    for (int i = 0; i < atts.getLength(); i++) {
      String qName = atts.getName(i);
      String value = atts.getValue(i);
      String nsPrefix = null;
      if (qName.startsWith("xmlns"))
      {
        nsPrefix = qName.equals("xmlns") ? null : qName.substring(XMLNS_PREFIX_LENGTH);
        tempNode = new AnyNode((short)3, getLocalPart(qName), nsPrefix, value, null);
        this._context.addNamespace(nsPrefix, value);
        this._namespaces.push(tempNode);
        if (prefix.equals(nsPrefix)) {
          namespaceURI = value;
        }
      }
    }
    createNodeElement(namespaceURI, getLocalPart(name), name);
    while (!this._namespaces.empty()) {
      tempNode = (AnyNode)this._namespaces.pop();
      this._node.addNamespace(tempNode);
    }
    for (int i = 0; i < atts.getLength(); i++) {
      String qName = atts.getName(i);
      String value = atts.getValue(i);
      if (!qName.startsWith("xmlns")) {
        tempNode = new AnyNode((short)2, getLocalPart(qName), null, null, value);
        this._node.addAttribute(tempNode);
      }
    }
    tempNode = null;
  }
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
    throws SAXException {
    if (this._processNamespace) {
      this._context = this._context.createNamespaces();
      String prefix = "";
      int idx = qName.indexOf(':');
      if (idx >= 0) {
        prefix = qName.substring(0, idx);
      }
      namespaceURI = this._context.getNamespaceURI(prefix);
      for (int i = 0; i < atts.getLength(); i++) {
        String attrqName = atts.getQName(i);
        String value = atts.getValue(i);
        String nsPrefix = null;
        if (attrqName.startsWith("xmlns"))
        {
          nsPrefix = attrqName.equals("xmlns") ? null : attrqName.substring(XMLNS_PREFIX_LENGTH);
          AnyNode tempNode = new AnyNode((short)3, getLocalPart(attrqName), nsPrefix, value, null);
          this._context.addNamespace(nsPrefix, value);
          this._namespaces.push(tempNode);
          if (prefix.equals(nsPrefix)) {
            namespaceURI = value;
          }
        }
      }
    }
    createNodeElement(namespaceURI, localName, qName);
    for (int i = 0; i < atts.getLength(); i++) {
      String uri = atts.getURI(i);
      String attqName = atts.getQName(i);
      String value = atts.getValue(i);
      String prefix = null;
      if ((!this._processNamespace) || 
        (!attqName.startsWith("xmlns"))) {
        if ((attqName.length() != 0) && (attqName.indexOf(':') != -1)) {
          prefix = attqName.substring(0, attqName.indexOf(':'));
        }
        if (this._processNamespace) {
          if (prefix != null) {
            uri = this._context.getNamespaceURI(prefix);
          }
        }
        AnyNode tempNode = new AnyNode((short)2, getLocalPart(attqName), prefix, uri, value);
        this._node.addAttribute(tempNode);
      }
    }
    while (!this._namespaces.empty()) {
      AnyNode tempNode = (AnyNode)this._namespaces.pop();
      this._node.addNamespace(tempNode);
    }
    AnyNode tempNode = null;
  }
  public void endElement(String name) throws SAXException {
    int idx = name.indexOf(':');
    String prefix = idx >= 0 ? name.substring(0, idx) : "";
    String namespaceURI = this._context.getNamespaceURI(prefix);
    endElement(namespaceURI, getLocalPart(name), name);
    this._context = this._context.getParent();
  }
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    this._character = false;
    String name = null;
    if ((localName != null) && (localName.length() > 0)) {
      name = localName;
    } else {
      name = getLocalPart(qName);
    }
    if ((this._startingNode.getLocalName().equals(name)) && (this._nodeStack.empty())) {
      return;
    }
    this._node = ((AnyNode)this._nodeStack.pop());
    if (this._nodeStack.empty()) {
      this._startingNode.addChild(this._node);
      this._node = this._startingNode;
    } else {
      AnyNode previousNode = (AnyNode)this._nodeStack.peek();
      previousNode.addChild(this._node);
      
      this._node = previousNode;
    }
  }
  public void characters(char[] ch, int start, int length) throws SAXException {
    String temp = new String(ch, start, length);
    if ((isWhitespace(temp)) && (!this._wsPreserve) && (!this._character)) {
      return;
    }
    AnyNode tempNode = new AnyNode((short)6, null, null, null, temp);
    this._node.addChild(tempNode);
    this._character = true;
  }
  public void warning(SAXParseException e) throws SAXException {
    String err = "SAX2ANY warning\nLine : " + e.getLineNumber() + '\n' + "URI : " + e.getSystemId() + '\n' + e.getMessage();
    throw new SAXException(err, e);
  }
  public void error(SAXParseException e)
    throws SAXException {
    String err = "SAX2ANY Error \nLine : " + e.getLineNumber() + '\n' + "URI : " + e.getSystemId() + '\n' + e.getMessage();
    throw new SAXException(err, e);
  }
  public void fatalError(SAXParseException e)
    throws SAXException {
    String err = "SAX2ANY Fatal Error \nLine : " + e.getLineNumber() + '\n' + "URI : " + e.getSystemId() + '\n' + e.getMessage();
    throw new SAXException(err, e);
  }
  public AnyNode getStartingNode()
  {
    return this._startingNode;
  }
  public Namespaces getNamespaceContext()
  {
    return this._context;
  }
  public void setNamespaceContext(Namespaces context)
  {
    this._context = context;
  }
  private boolean isWhitespace(String string) {
    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      switch (ch) {
      case '\t':
      case '\n': 
      case '\r': 
      case ' ': 
        break;
      default: 
        return false;
      }
    }
    return true;
  }
  private String getLocalPart(String ncName) {
    int idx = ncName.indexOf(':');
    if (idx >= 0) {
      return ncName.substring(idx + 1);
    }
    return ncName;
  }
  private void createNodeElement(String namespaceURI, String localName, String qName) {
    String prefix = null;
    if (namespaceURI != null) {
      prefix = this._context.getNamespacePrefix(namespaceURI);
    } else if ((qName != null) && 
      (qName.length() != 0) && (qName.indexOf(':') != -1)) {
      prefix = qName.substring(0, qName.indexOf(':'));
    }
    String name = null;
    if ((localName != null) && (localName.length() > 0)) {
      name = localName;
    } else {
      name = getLocalPart(qName);
    }
    if ((this._nodeStack.empty()) && (this._startingNode == null)) {
      this._startingNode = new AnyNode((short)1, name, prefix, namespaceURI, null);
      this._node = this._startingNode;
    }
    else {
      this._node = new AnyNode((short)1, name, prefix, namespaceURI, null);
      this._nodeStack.push(this._node);
    }
  }
}

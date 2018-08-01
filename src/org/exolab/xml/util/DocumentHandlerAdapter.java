package org.exolab.xml.util;

import org.exolab.xml.Namespaces;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

public class DocumentHandlerAdapter implements ContentHandler {
  private static final String CDATA = "CDATA";
  private DocumentHandler _handler = null;
  private Namespaces _namespaces = null;
  private boolean _createNamespaceScope = true;
  public DocumentHandlerAdapter(DocumentHandler handler) {
    if (handler == null) {
      throw new IllegalArgumentException("The argument 'handler' must not be null.");
    }
    this._handler = handler;
    this._namespaces = new Namespaces();
  }
  public void characters(char[] chars, int start, int length) throws SAXException {
    this._handler.characters(chars, start, length);
  }
  public void endDocument() throws SAXException {
    this._handler.endDocument();
  }
  public void endElement(String uri, String localName, String qName) throws SAXException {
    this._handler.endElement(qName);
    if (this._namespaces.getParent() != null) {
      this._namespaces = this._namespaces.getParent();
    }
  }
  public void endPrefixMapping(String prefix) throws SAXException {}
  
  public void ignorableWhitespace(char[] chars, int start, int length) throws SAXException {
    this._handler.ignorableWhitespace(chars, start, length);
  }
  public void processingInstruction(String target, String data)
    throws SAXException {
    this._handler.processingInstruction(target, data);
  }
  public void setDocumentLocator(Locator locator)
  {
    this._handler.setDocumentLocator(locator);
  }
  public void skippedEntity(String arg0) throws SAXException {}
  public void startDocument() throws SAXException {
    this._handler.startDocument();
  }
  public void startElement(String uri, String localName, String qName, Attributes atts)
    throws SAXException {
    AttributeListImpl attList = new AttributeListImpl();
    if (this._createNamespaceScope) {
      this._namespaces = this._namespaces.createNamespaces();
    } else {
      this._createNamespaceScope = true;
      this._namespaces.declareAsAttributes(attList, true);
    }for (int i = 0; i < atts.getLength(); i++) {
      attList.addAttribute(atts.getQName(i), "CDATA", atts.getValue(i));
    }
    this._handler.startElement(qName, attList);
  }
  public void startPrefixMapping(String prefix, String uri)
    throws SAXException {
    if (this._createNamespaceScope) {
      this._namespaces = this._namespaces.createNamespaces();
      this._createNamespaceScope = false;
    }
    this._namespaces.addNamespace(prefix, uri);
  }
}

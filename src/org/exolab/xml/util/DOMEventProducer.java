package org.exolab.xml.util;

import org.exolab.xml.EventProducer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;

public class DOMEventProducer implements EventProducer {
  private DocumentHandler _handler = null;
  private Node _node = null;
  public DOMEventProducer() {}
  public DOMEventProducer(Node node)
  {
    this._node = node;
  }
  public void setDocumentHandler(DocumentHandler handler)
  {
    this._handler = handler;
  }
  public void setNode(Node node)
  {
    this._node = node;
  }
  public void start() throws SAXException {
    if ((this._node == null) || (this._handler == null)) {
      return;
    }
    process(this._node, this._handler);
  }
  public static void process(Document document, DocumentHandler handler) throws SAXException {
    if (document == null) {
      return;
    }
    if (handler == null) {
      return;
    }
    handler.startDocument();
    processChildren(document, handler);
    handler.endDocument();
  }
  public static void process(Node node, DocumentHandler handler) throws SAXException {
    if ((node == null) || (handler == null)) {
      return;
    }
    switch (node.getNodeType()) {
    case 9: 
      process((Document)node, handler);
      break;
    case 11: 
      processChildren(node, handler);
      break;
    case 1: 
      process((Element)node, handler);
      break;
    case 3: 
    case 4: 
      process((Text)node, handler);
      break;
    case 7: 
      process((ProcessingInstruction)node, handler);
      break;
    }
  }
  private static void process(Element element, DocumentHandler handler) throws SAXException {
    String name = element.getNodeName();
    AttributeList atts = new AttributeListWrapper(element.getAttributes());
    handler.startElement(name, atts);
    processChildren(element, handler);
    handler.endElement(name);
  }
  private static void process(Text text, DocumentHandler handler) throws SAXException {
    String data = text.getData();
    if ((data != null) && (data.length() > 0)) {
      char[] chars = data.toCharArray();
      
      handler.characters(chars, 0, chars.length);
    }
  }
  private static void process(ProcessingInstruction pi, DocumentHandler handler)
    throws SAXException {
    handler.processingInstruction(pi.getTarget(), pi.getData());
  }
  private static void processChildren(Node node, DocumentHandler handler) throws SAXException {
    Node child = node.getFirstChild();
    while (child != null) {
      process(child, handler);
      child = child.getNextSibling();
    }
  }
}

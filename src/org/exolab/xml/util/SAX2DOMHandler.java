package org.exolab.xml.util;

import java.util.Stack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;

public class SAX2DOMHandler extends HandlerBase {
  private Node _document;
  private Stack _parents = new Stack();
  public SAX2DOMHandler(Node node)
  {
    this._document = node;
  }
  public void startElement(String name, AttributeList attributes) {
    Node parent = this._parents.size() > 0 ? (Node)this._parents.peek() : this._document;
    Document document;
    if ((parent instanceof Document)) {
      document = (Document)parent;
    } else {
      document = parent.getOwnerDocument();
    }
    Element element = document.createElement(name);
    int length = attributes.getLength();
    for (int i = 0; i < length; i++) {
      element.setAttribute(attributes.getName(i), attributes.getValue(i));
    }
    parent.appendChild(element);
    this._parents.push(element);
  }
  public void characters(char[] chars, int offset, int length) {
    String data = new String(chars, offset, length);
    Node parent = this._parents.size() > 0 ? (Node)this._parents.peek() : this._document;
    Node last = parent.getLastChild();
    if ((last != null) && (last.getNodeType() == 3)) {
      ((Text)last).appendData(data);
    }
    else {
      Text text = parent.getOwnerDocument().createTextNode(data);
      parent.appendChild(text);
    }
  }
  public void endElement(String name)
  {
    this._parents.pop();
  }
}

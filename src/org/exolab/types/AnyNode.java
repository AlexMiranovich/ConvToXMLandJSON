package org.exolab.types;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Stack;
import org.exec.xml.BackwardCompatibilityContext;
import org.exolab.xml.Serializer;
import org.exolab.xml.util.AnyNode2SAX;
import org.xml.sax.SAXException;

public final class AnyNode implements Serializable {
  private static final long serialVersionUID = -4104117996051705975L;
  private static final String XMLNS_PREFIX = "xmlns";
  public static final short ELEMENT = 1;
  public static final short ATTRIBUTE = 2;
  public static final short NAMESPACE = 3;
  public static final short PI = 4;
  public static final short COMMENT = 5;
  public static final short TEXT = 6;
  private short _nodeType = 1;
  private AnyNode _nextSiblingNode = null;
  private AnyNode _firstChildNode = null;
  private String _localName;
  private String _uri;
  private String _prefix;
  private static Stack _elements;
  private String _value;
  
  public AnyNode()
  {
    this((short)1, null, null, null, null);
  }
  public AnyNode(short type, String localName, String prefix, String uri, String value) {
    if ((type > 6) && (type < 1)) {
      throw new IllegalArgumentException("Illegal node type");
    }
    this._nodeType = type;
    if ((type > 4) && (localName != null)) {
      String err = "This node can not have a local name";
      throw new IllegalArgumentException(err);
    }
    this._localName = localName;
    if ((type > 3) && ((uri != null) || (prefix != null))) {
      String err = "This node can not handle namespace";
      throw new IllegalArgumentException(err);
    }
    this._uri = uri;
    this._prefix = prefix;
    if ((type == 2) && 
      (localName.startsWith("xmlns"))) {
      String err = "Namespaces can't be used as attributes.";
      throw new IllegalArgumentException(err);
    }
    if ((type == 1) && (value != null)) {
      String err = "You can't set a value for this node type";
      throw new IllegalArgumentException(err);
    }
    this._value = value;
  }
  
  public void addAnyNode(AnyNode node) {
    if (node == null) {
      throw new IllegalArgumentException("null argument in addAnyNode");
    }
    switch (node.getNodeType()) {
    case 2: 
      addAttribute(node);
      break;
    case 3: 
      addNamespace(node);
      break;
    default: 
      addChild(node);
    }
  }
  public void addChild(AnyNode node) {
    if (node == null) {
      throw new IllegalArgumentException("null argument in appendChild");
    }
    if ((node.getNodeType() == 2) || (node.getNodeType() == 3)) {
      throw new IllegalArgumentException("An Attribute or an Namespace can't be added as a child");
    }
    if (getNodeType() == 6) {
      throw new IllegalArgumentException("a TEXT node can't have children.");
    }
    if (this._firstChildNode == null) {
      this._firstChildNode = node;
    } else if ((this._firstChildNode.getNodeType() == 2) || (this._firstChildNode.getNodeType() == 3)) {
      this._firstChildNode.addChild(node);
    } else {
      this._firstChildNode.appendSibling(node);
    }
  }
  
  public void addAttribute(AnyNode node) {
    if (node == null) {
      throw new IllegalArgumentException("null argument in addAttribute");
    }
    if (node.getNodeType() != 2) {
      throw new IllegalArgumentException("Only attribute can be added as an attribute");
    }
    if (this._firstChildNode == null) {
      this._firstChildNode = node;
    }
    else if (this._firstChildNode.getNodeType() == 2) {
      this._firstChildNode.appendSibling(node);
    }
    else if (this._firstChildNode.getNodeType() == 3) {
      this._firstChildNode.addAttribute(node);
    }
    else {
      node.addChild(this._firstChildNode);
      this._firstChildNode = node;
    }
  }
  
  public void addNamespace(AnyNode node) {
    if (node == null) {
      throw new IllegalArgumentException("null argument in addNamespace");
    }
    if (node.getNodeType() != 3) {
      throw new IllegalArgumentException("Only namespace can be added as an namespace");
    }
    if (this._firstChildNode == null) {
      this._firstChildNode = node;
    }
    else if (this._firstChildNode.getNodeType() == 3) {
      this._firstChildNode.appendSibling(node);
    }
    else if (this._firstChildNode.getNodeType() == 2) {
      this._firstChildNode.addNamespace(node);
    }
    else
    {
      node.addChild(this._firstChildNode);
      this._firstChildNode = node;
    }
  }
  
  public AnyNode getFirstAttribute() {
    if (getNodeType() != 1) {
      String err = "This node type can not contain attributes";
      throw new UnsupportedOperationException(err);
    }
    boolean found = false;
    AnyNode tempNode = getFirstChildNode();
    while ((tempNode != null) && (!found)) {
      short type = tempNode.getNodeType();
      if ((type == 1) || (type == 5) || (type == 6) || (type == 4)) {
        tempNode = null;
      } else if (type == 3) {
        tempNode = tempNode.getFirstChildNode();
      } else {
        found = true;
      }
    }
    return tempNode;
  }
  
  public AnyNode getFirstNamespace() {
    if (getNodeType() != 1) {
      String err = "This node type can not contain namespaces";
      throw new UnsupportedOperationException(err);
    }
    AnyNode tempNode = getFirstChildNode();
    boolean found = false;
    while ((tempNode != null) && (!found)) {
      short type = tempNode.getNodeType();
      if ((type == 1) || (type == 5) || (type == 6) || (type == 4)) {
        tempNode = null;
      } else if (type == 2) {
        tempNode = tempNode.getFirstChildNode();
      } else {
        found = true;
      }
    }
    return tempNode;
  }
  
  public AnyNode getFirstChild() {
    if ((getNodeType() == 2) || (getNodeType() == 3)) {
      return null;
    }
    AnyNode tempNode = getFirstChildNode();
    boolean found = false;
    while ((tempNode != null) && (!found)) {
      short type = tempNode.getNodeType();
      if ((type == 1) || (type == 5) || (type == 6) || (type == 4)) {
        found = true;
      } else if ((type == 2) || (type == 3)) {
        tempNode = tempNode.getFirstChildNode();
      }
    }
    return tempNode;
  }
  
  public AnyNode getNextSibling()
  {
    return this._nextSiblingNode;
  }
  public short getNodeType()
  {
    return this._nodeType;
  }
  public String getLocalName()
  {
    return this._localName;
  }
  public String getNamespaceURI()
  {
    return this._uri;
  }
  public String getStringValue() {
    switch (this._nodeType) {
    case 2: 
    case 6: 
      return this._value;
    case 3: 
      return this._uri;
    case 4: 
      return "";
    case 5: 
      return this._value;
    case 1: 
      StringBuffer result = new StringBuffer(4096);
      AnyNode tempNode = getNextSibling();
      while ((tempNode != null) && (tempNode.getNodeType() == 6)) {
        result.append(tempNode.getStringValue());
        tempNode = tempNode.getNextSibling();
      }
      tempNode = getFirstChild();
      while (tempNode != null) {
        result.append(tempNode.getStringValue());
        tempNode = tempNode.getNextSibling();
      }
      tempNode = null;
      return result.toString();
    }
    return null;
  }
  
  public String getNamespacePrefix()
  {
    return this._prefix;
  }
  
  public String toString() {
    Serializer serializer = new BackwardCompatibilityContext().getSerializer();
    if (serializer == null) {
      throw new RuntimeException("Unable to obtain serializer");
    }
    StringWriter writer = new StringWriter();
    
    serializer.setOutputCharStream(writer);
    try {
      AnyNode2SAX.fireEvents(this, serializer.asDocumentHandler());
    }
    catch (IOException ioe)
    {
      return privateToString();
    }
    catch (SAXException saxe) {
      throw new RuntimeException(saxe.getMessage());
    }
    return writer.toString();
  }
  
  private String privateToString() {
    StringBuffer sb = new StringBuffer(4096);
    if (_elements == null) {
      _elements = new Stack();
    }
    if (_elements.search(this) == -1) {
      _elements.push(this);
      if (getNodeType() == 1) {
        sb.append("<");
        String prefix = getNamespacePrefix();
        if (prefix != null) {
          sb.append(prefix + ":");
        }
        prefix = null;
        sb.append(getLocalName());
        
        AnyNode tempNode = getFirstAttribute();
        while (tempNode != null) {
          sb.append(" ");
          sb.append(tempNode.getLocalName());
          sb.append("='" + tempNode.getStringValue() + "'");
          tempNode = tempNode.getNextSibling();
        }
        tempNode = getFirstNamespace();
        while (tempNode != null) {
          sb.append(" ");
          sb.append("xmlns");
          prefix = tempNode.getNamespacePrefix();
          if ((prefix != null) && (prefix.length() != 0)) {
            sb.append(":" + prefix);
          }
          sb.append("='" + tempNode.getNamespaceURI() + "'");
          tempNode = tempNode.getNextSibling();
        }
        tempNode = getFirstChild();
        if (tempNode != null) {
          sb.append(">");
          while (tempNode != null) {
            sb.append(tempNode.privateToString());
            tempNode = tempNode.getNextSibling();
          }
          sb.append("</" + getLocalName() + ">");
        }
        else {
          sb.append("/>");
        }
      }
      else
      {
        sb.append(getStringValue());
      }
      return sb.toString();
    }
    return sb.toString();
  }
  
  protected void appendSibling(AnyNode node) {
    if (node == null) {
      throw new IllegalArgumentException();
    }
    if (((node.getNodeType() == 2) || (node.getNodeType() == 3)) && (getNodeType() != node.getNodeType())) {
      String err = "a NAMESPACE or an ATTRIBUTE can only be add as a sibling to a node of the same type";
      throw new UnsupportedOperationException(err);
    }
    if (this._nextSiblingNode == null) {
      if ((node.getNodeType() == 6) && (getNodeType() == 6)) {
        mergeTextNode(this, node);
      } else {
        this._nextSiblingNode = node;
      }
    }
    else {
      this._nextSiblingNode.appendSibling(node);
    }
  }
  
  protected AnyNode getFirstChildNode()
  {
    return this._firstChildNode;
  }
  private void mergeTextNode(AnyNode node1, AnyNode node2) {
    if (node1.getNodeType() != node2.getNodeType()) {
      return;
    }
    if (node1.getNodeType() != 6) {
      return;
    }
    StringBuffer temp = new StringBuffer(node1.getStringValue());
    temp.append(node2.getStringValue());
    node1._value = temp.toString();
    node2 = null;
  }
}

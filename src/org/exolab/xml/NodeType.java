package org.exolab.xml;

public final class NodeType {
  public static final short ATTRIBUTE = 0;
  public static final short ELEMENT = 1;
  public static final short NAMESPACE = 2;
  public static final short TEXT = 3;
  public static final NodeType Attribute = new NodeType((short)0, "attribute");
  public static final NodeType Element = new NodeType((short)1, "element");
  public static final NodeType Namespace = new NodeType((short)2, "namespace");
  public static final NodeType Text = new NodeType((short)3, "text");
  private final String _name;
  private final short _type;
  private NodeType(short type, String name) {
    this._type = type;
    this._name = name;
  }
  public static NodeType getNodeType(String nodeType) {
    if (nodeType == null) {
      return Attribute;
    }
    if (nodeType.equals(Attribute._name)) {
      return Attribute;
    }
    if (nodeType.equals(Namespace._name)) {
      return Namespace;
    }
    if (nodeType.equals(Element._name)) {
      return Element;
    }
    if (nodeType.equals(Text._name)) {
      return Text;
    }
    throw new IllegalArgumentException("Unrecognized node type");
  }
  public short getType()
  {
    return this._type;
  }
  public String toString()
  {
    return this._name;
  }
}

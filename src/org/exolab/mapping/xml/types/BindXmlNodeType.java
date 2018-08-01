package org.exolab.mapping.xml.types;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class BindXmlNodeType implements Serializable {
  public static final int ATTRIBUTE_TYPE = 0;
  public static final BindXmlNodeType ATTRIBUTE = new BindXmlNodeType(0, "attribute");
  public static final int ELEMENT_TYPE = 1;
  public static final BindXmlNodeType ELEMENT = new BindXmlNodeType(1, "element");
  public static final int NAMESPACE_TYPE = 2;
  public static final BindXmlNodeType NAMESPACE = new BindXmlNodeType(2, "namespace");
  public static final int TEXT_TYPE = 3;
  public static final BindXmlNodeType TEXT = new BindXmlNodeType(3, "text");
  private static Hashtable<Object, Object> _memberTable = init();
  private final int type;
  private String stringValue = null;
  private BindXmlNodeType(int type, String value) {
    this.type = type;
    this.stringValue = value;
  }
  public static Enumeration<? extends Object> enumerate()
  {
    return _memberTable.elements();
  }
  public int getType()
  {
    return this.type;
  }
  private static Hashtable<Object, Object> init() {
    Hashtable<Object, Object> members = new Hashtable();
    members.put("attribute", ATTRIBUTE);
    members.put("element", ELEMENT);
    members.put("namespace", NAMESPACE);
    members.put("text", TEXT);
    return members;
  }
  private Object readResolve()
  {
    return valueOf(this.stringValue);
  }
  public String toString()
  {
    return this.stringValue;
  }
  public static BindXmlNodeType valueOf(String string) {
    Object obj = null;
    if (string != null) {
      obj = _memberTable.get(string);
    }
    if (obj == null) {
      String err = "" + string + " is not a valid BindXmlNodeType";
      throw new IllegalArgumentException(err);
    }
    return (BindXmlNodeType)obj;
  }
}

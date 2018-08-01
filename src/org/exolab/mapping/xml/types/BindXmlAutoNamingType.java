package org.exolab.mapping.xml.types;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class BindXmlAutoNamingType implements Serializable {
  public static final int DERIVEBYCLASS_TYPE = 0;
  public static final BindXmlAutoNamingType DERIVEBYCLASS = new BindXmlAutoNamingType(0, "deriveByClass");
  public static final int DERIVEBYFIELD_TYPE = 1;
  public static final BindXmlAutoNamingType DERIVEBYFIELD = new BindXmlAutoNamingType(1, "deriveByField");
  private static Hashtable<Object, Object> _memberTable = init();
  private final int type;
  private String stringValue = null;
  private BindXmlAutoNamingType(int type, String value) {
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
    members.put("deriveByClass", DERIVEBYCLASS);
    members.put("deriveByField", DERIVEBYFIELD);
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
  public static BindXmlAutoNamingType valueOf(String string) {
    Object obj = null;
    if (string != null) {
      obj = _memberTable.get(string);
    }if (obj == null) {
      String err = "" + string + " is not a valid BindXmlAutoNamingType";
      throw new IllegalArgumentException(err);
    }
    return (BindXmlAutoNamingType)obj;
  }
}

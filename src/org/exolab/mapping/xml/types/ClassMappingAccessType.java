package org.exolab.mapping.xml.types;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class ClassMappingAccessType implements Serializable {
  public static final int READ_ONLY_TYPE = 0;
  public static final ClassMappingAccessType READ_ONLY = new ClassMappingAccessType(0, "read-only");
  public static final int SHARED_TYPE = 1;
  public static final ClassMappingAccessType SHARED = new ClassMappingAccessType(1, "shared");
  public static final int EXCLUSIVE_TYPE = 2;
  public static final ClassMappingAccessType EXCLUSIVE = new ClassMappingAccessType(2, "exclusive");
  public static final int DB_LOCKED_TYPE = 3;
  public static final ClassMappingAccessType DB_LOCKED = new ClassMappingAccessType(3, "db-locked");
  private static Hashtable<Object, Object> _memberTable = init();
  private final int type;
  private String stringValue = null;
  private ClassMappingAccessType(int type, String value) {
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
    members.put("read-only", READ_ONLY);
    members.put("shared", SHARED);
    members.put("exclusive", EXCLUSIVE);
    members.put("db-locked", DB_LOCKED);
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
  public static ClassMappingAccessType valueOf(String string) {
    Object obj = null;
    if (string != null) {
      obj = _memberTable.get(string);
    }
    if (obj == null) {
      String err = "" + string + " is not a valid ClassMappingAccessType";
      throw new IllegalArgumentException(err);
    }
    return (ClassMappingAccessType)obj;
  }
}

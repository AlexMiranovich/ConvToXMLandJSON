package org.exolab.mapping.xml.types;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class SqlDirtyType implements Serializable {
  public static final int CHECK_TYPE = 0;
  public static final SqlDirtyType CHECK = new SqlDirtyType(0, "check");
  public static final int IGNORE_TYPE = 1;
  public static final SqlDirtyType IGNORE = new SqlDirtyType(1, "ignore");
  private static Hashtable<Object, Object> _memberTable = init();
  private final int type;
  private String stringValue = null;
  private SqlDirtyType(int type, String value) {
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
    members.put("check", CHECK);
    members.put("ignore", IGNORE);
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
  public static SqlDirtyType valueOf(String string) {
    Object obj = null;
    if (string != null) {
      obj = _memberTable.get(string);
    }if (obj == null) {
      String err = "" + string + " is not a valid SqlDirtyType";
      throw new IllegalArgumentException(err);
    }
    return (SqlDirtyType)obj;
  }
}

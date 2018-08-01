package org.exolab.mapping;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccessMode implements Cloneable, Comparable, Serializable {
  private static final long serialVersionUID = -7113303922354626951L;
  private static final Map IDS = new HashMap(7);
  private static final Map NAMES = new HashMap(7);
  public static final AccessMode ReadOnly = new AccessMode((short)0, "read-only");
  public static final AccessMode Shared = new AccessMode((short)1, "shared");
  public static final AccessMode Exclusive = new AccessMode((short)2, "exclusive");
  public static final AccessMode DbLocked = new AccessMode((short)3, "db-locked");
  private short _id;
  private String _name;
  public static AccessMode valueOf(String accessMode) {
    AccessMode mode = (AccessMode)NAMES.get(accessMode);
    if (mode != null) {
      return mode;
    }
    throw new IllegalArgumentException("Unrecognized access mode");
  }
  public static AccessMode valueOf(short accessMode) {
    AccessMode mode = (AccessMode)IDS.get(new Short(accessMode));
    if (mode != null) {
      return mode;
    }
    throw new IllegalArgumentException("Unrecognized access mode");
  }
  private AccessMode(short id, String name) {
    this._id = id;
    this._name = name;
    
    IDS.put(new Short(id), this);
    NAMES.put(name, this);
  }
  public short getId()
  {
    return this._id;
  }
  public String getName()
  {
    return this._name;
  }
  public String toString()
  {
    return this._name;
  }
  public Object clone()
  {
    return this;
  }
  public boolean equals(Object other)
  {
    return this == other;
  }
  public int hashCode()
  {
    return this._id;
  }
  public int compareTo(Object other)
  {
    return compareTo((AccessMode)other);
  }
  public int compareTo(AccessMode other)
  {
    return this._id - other._id;
  }
  protected Object readResolve()
  {
    return NAMES.get(this._name);
  }
}

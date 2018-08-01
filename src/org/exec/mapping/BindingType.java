package org.exec.mapping;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class BindingType implements Cloneable, Comparable, Serializable {
  private static final long serialVersionUID = -2116844968191798202L;
  private static final Map TYPES = new HashMap();
  public static final BindingType JDO = new BindingType("jdo");
  public static final BindingType XML = new BindingType("xml");
  private final String _type;
  private BindingType(String kind) {
    this._type = kind;
    TYPES.put(kind, this);
  }
  public static BindingType valueOf(String kind)
  {
    return (BindingType)TYPES.get(kind);
  }
  public static Iterator iterator()
  {
    return TYPES.values().iterator();
  }
  public String toString()
  {
    return this._type;
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
    return this._type.hashCode();
  }
  public int compareTo(Object other)
  {
    return this._type.compareTo(((BindingType)other)._type);
  }
  protected Object readResolve()
  {
    return TYPES.get(this._type);
  }
}

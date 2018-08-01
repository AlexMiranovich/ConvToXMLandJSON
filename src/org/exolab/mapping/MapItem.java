package org.exolab.mapping;

public class MapItem {
  private Object _key = null;
  private Object _value = null;
  public MapItem() {}
  public MapItem(Object key, Object value) {
    this._key = key;
    this._value = value;
  }
  public Object getKey()
  {
    return this._key;
  }
  public Object getValue()
  {
    return this._value;
  }
  public void setKey(Object key)
  {
    this._key = key;
  }
  public void setValue(Object value)
  {
    this._value = value;
  }
}

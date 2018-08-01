package org.exolab.xml.handlers;

import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.GeneralizedFieldHandler;

public class ToStringFieldHandler extends GeneralizedFieldHandler {
  private final Class _type;
  public ToStringFieldHandler(Class type)
  {
    this(type, null);
  }
  public ToStringFieldHandler(Class type, FieldHandler handler) {
    if (type == null) {
      throw new IllegalArgumentException("The argument 'type' must not be null.");
    }
    this._type = type;
    if (handler != null) {
      setFieldHandler(handler);
    }
  }
  public Object convertUponGet(Object value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }
  public Object convertUponSet(Object value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }
  public Class getFieldType()
  {
    return this._type;
  }
}

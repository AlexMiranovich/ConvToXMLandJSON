package org.exolab.mapping;

import java.util.Enumeration;
import org.exolab.mapping.loader.CollectionHandlers;

public abstract class GeneralizedFieldHandler extends AbstractFieldHandler {
  private static final String NULL_HANDLER_ERR = "A call to #setFieldHandler (with a non-null value) must be made before calling this method.";
  private FieldHandler _handler = null;
  private boolean _autoCollectionIteration = true;
  public abstract Object convertUponGet(Object paramObject);
  public abstract Object convertUponSet(Object paramObject);
  public abstract Class getFieldType();
  public final void setFieldHandler(FieldHandler handler)
  {
    this._handler = handler;
  }
  public void setCollectionIteration(boolean autoCollectionIteration) {
    this._autoCollectionIteration = autoCollectionIteration;
  }
  public final Object getValue(Object object)
    throws IllegalStateException {
    if (this._handler == null) {
      throw new IllegalStateException("A call to #setFieldHandler (with a non-null value) must be made before calling this method.");
    }
    Object value = this._handler.getValue(object);
    if ((this._autoCollectionIteration) && (value != null)) {
      if ((value instanceof Enumeration)) {
        return new GFHConverterEnumeration(this, (Enumeration)value);
      }
      if (CollectionHandlers.hasHandler(value.getClass())) {
        CollectionHandler colHandler = null;
        try {
          colHandler = CollectionHandlers.getHandler(value.getClass());
        }
        catch (MappingException mx) {
          throw new IllegalStateException(mx.getMessage());
        }
        return new GFHConverterEnumeration(this, colHandler.elements(value));
      }
    }
    return convertUponGet(value);
  }
  public Object newInstance(Object parent)
    throws IllegalStateException {
    if (this._handler == null) {
      throw new IllegalStateException("A call to #setFieldHandler (with a non-null value) must be made before calling this method.");
    }
    return this._handler.newInstance(parent);
  }
  public Object newInstance(Object parent, Object[] args)
    throws IllegalStateException {
    if ((this._handler instanceof ExtendedFieldHandler)) {
      return ((ExtendedFieldHandler)this._handler).newInstance(parent, args);
    }
    return newInstance(parent);
  }
  public final void resetValue(Object object)
    throws IllegalStateException, IllegalArgumentException {
    if (this._handler == null) {
      throw new IllegalStateException("A call to #setFieldHandler (with a non-null value) must be made before calling this method.");
    }
    this._handler.resetValue(object);
  }
  public final void setValue(Object object, Object value)
    throws IllegalStateException, IllegalArgumentException {
    if (this._handler == null) {
      throw new IllegalStateException("A call to #setFieldHandler (with a non-null value) must be made before calling this method.");
    }
    this._handler.setValue(object, convertUponSet(value));
  }
  static class GFHConverterEnumeration implements Enumeration {
    Enumeration _enumeration = null;
    GeneralizedFieldHandler _handler = null;
    GFHConverterEnumeration(GeneralizedFieldHandler handler, Enumeration enumeration) {
      this._enumeration = enumeration;
      this._handler = handler;
    }
    public boolean hasMoreElements()
    {
      return this._enumeration.hasMoreElements();
    }
    public Object nextElement() {
      Object value = this._enumeration.nextElement();
      return this._handler.convertUponGet(value);
    }
  }
}

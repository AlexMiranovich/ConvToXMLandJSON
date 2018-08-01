package org.exolab.mapping;

import java.util.Properties;

public abstract class AbstractFieldHandler extends ExtendedFieldHandler
  implements ConfigurableFieldHandler {
  private FieldDescriptor _descriptor = null;
  protected Properties _properties;
  protected final FieldDescriptor getFieldDescriptor()
  {
    return this._descriptor;
  }
  public void setFieldDescriptor(FieldDescriptor fieldDesc)
  {
    this._descriptor = fieldDesc;
  }
  public boolean hasValue(Object object)
  {
    return getValue(object) != null;
  }
  public abstract Object getValue(Object paramObject)
    throws IllegalStateException;
  public abstract Object newInstance(Object paramObject)
    throws IllegalStateException;
  public abstract Object newInstance(Object paramObject, Object[] paramArrayOfObject)
    throws IllegalStateException;
  public abstract void resetValue(Object paramObject)
    throws IllegalStateException, IllegalArgumentException;
  public abstract void setValue(Object paramObject1, Object paramObject2)
    throws IllegalStateException, IllegalArgumentException;
  public void setConfiguration(Properties config)
    throws ValidityException {}
}

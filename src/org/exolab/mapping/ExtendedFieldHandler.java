package org.exolab.mapping;

import org.exolab.mapping.loader.FieldHandlerFriend;

public abstract class ExtendedFieldHandler extends FieldHandlerFriend {
  protected abstract FieldDescriptor getFieldDescriptor();
  public abstract void setFieldDescriptor(FieldDescriptor paramFieldDescriptor);

  public void checkValidity(Object object)
    throws ValidityException, IllegalStateException {}
  
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
}

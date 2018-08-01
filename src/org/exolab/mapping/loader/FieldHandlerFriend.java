package org.exolab.mapping.loader;

import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.ValidityException;

public abstract class FieldHandlerFriend implements FieldHandler {
  protected abstract FieldDescriptor getFieldDescriptor();
  public abstract void setFieldDescriptor(FieldDescriptor paramFieldDescriptor);
  public abstract void checkValidity(Object paramObject)
    throws ValidityException, IllegalStateException;
  
  public abstract Object getValue(Object paramObject)
    throws IllegalStateException;
  
  public abstract Object newInstance(Object paramObject)
    throws IllegalStateException;
  
  public abstract void resetValue(Object paramObject)
    throws IllegalStateException, IllegalArgumentException;
  
  public abstract void setValue(Object paramObject1, Object paramObject2)
    throws IllegalStateException, IllegalArgumentException;
}

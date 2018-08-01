package org.exolab.util;

public interface ObjectFactory {
  Object createInstance(Class paramClass)
    throws IllegalAccessException, InstantiationException;
  Object createInstance(Class paramClass, Object[] paramArrayOfObject)
    throws IllegalAccessException, InstantiationException;
  Object createInstance(Class paramClass, Class[] paramArrayOfClass, Object[] paramArrayOfObject)
    throws IllegalAccessException, InstantiationException;
}

package org.exolab.xml.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.exolab.mapping.GeneralizedFieldHandler;
import org.exolab.mapping.MappingException;

public class ValueOfFieldHandler extends GeneralizedFieldHandler {
  private static final Class[] ARGS = { String.class };
  private static final String METHOD_NAME = "valueOf";
  private final Class _type;
  private final Method _valueOf;
  public ValueOfFieldHandler(Class type) throws MappingException {
    if (type == null) {
      throw new IllegalArgumentException("The argument 'type' must not be null.");
    }
    this._type = type;
    Method method = null;
    try {
      method = type.getMethod("valueOf", ARGS);
    }
    catch (NoSuchMethodException nsme) {
      throw new MappingException(nsme);
    }
    if (!Modifier.isStatic(method.getModifiers())) {
      String err = "No static method 'valueOf' found in class: " + type.getName();
      throw new MappingException(err);
    }
    this._valueOf = method;
  }
  public Object convertUponGet(Object value)
  {
    return value;
  }
  public Object convertUponSet(Object value) {
    Object[] args = new Object[1];
    if (value != null) {
      args[0] = value.toString();
    }
    Object result = null;
    try {
      result = this._valueOf.invoke(null, args);
    }
    catch (IllegalAccessException iae) {
      throw new IllegalStateException(iae.getMessage());
    }
    catch (InvocationTargetException ite) {
      throw new IllegalStateException(ite.getMessage());
    }
    return result;
  }
  public Class getFieldType()
  {
    return this._type;
  }
  public Object newInstance(Object parent)
    throws IllegalStateException {
    return null;
  }
}

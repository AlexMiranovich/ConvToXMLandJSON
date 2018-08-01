package org.exolab.mapping.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.GeneralizedFieldHandler;

public class EnumFieldHandler extends GeneralizedFieldHandler {
  private Class _enumType = null;
  private Method _createMethod = null;
  private FieldHandler _handler = null;
  public EnumFieldHandler(Class enumType, FieldHandler handler, Method createMethod) {
    if (enumType == null) {
      String err = "The argument 'enumType' must not be null.";
      throw new IllegalArgumentException(err);
    }
    if (handler == null) {
      String err = "The argument 'handler' must not be null.";
      throw new IllegalArgumentException(err);
    }
    if (createMethod == null) {
      String err = "The argument 'createMethod' must not be null.";
      throw new IllegalArgumentException(err);
    }
    this._handler = handler;
    setFieldHandler(handler);
    this._enumType = enumType;
    this._createMethod = createMethod;
    int mods = createMethod.getModifiers();
    if (!Modifier.isStatic(mods)) {
      String err = "The factory create method specified for " + enumType.getName() + " must be static";
      
      throw new IllegalArgumentException(err);
    }
  }
  public Object convertUponGet(Object value)
  {
    return value;
  }
  public Object convertUponSet(Object value) throws IllegalStateException {
    Object[] args = new String[1];
    Object obj = null;
    if (value != null) {
      args[0] = value.toString();
      try {
        obj = this._createMethod.invoke(null, args);
      }
      catch (InvocationTargetException ite) {
        Throwable toss = ite.getTargetException();
        throw new IllegalStateException(toss.toString());
      }
      catch (IllegalAccessException iae)
      {
        throw new IllegalStateException(iae.toString());
      }
    }
    return obj;
  }
  public Class getFieldType()
  {
    return this._enumType;
  }
  public Object newInstance(Object parent)
    throws IllegalStateException {
    return "";
  }
  public Object newInstance(Object parent, Object[] args)
    throws IllegalStateException {
    return "";
  }
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof FieldHandler)) {
      return false;
    }
    return (this._handler.getClass().isInstance(obj)) || (getClass().isInstance(obj));
  }
}

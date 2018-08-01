package org.exolab.xml.handlers;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.ValidityException;

public class EnumFieldHandler implements FieldHandler {
  private static final Class[] STRING_ARGS = { String.class };
  private static final String METHOD_VALUEOF = "valueOf";
  private static final String METHOD_FROMVALUE = "fromValue";
  private static final String METHOD_VALUE = "value";
  private static final String METHOD_TOSTRING = "toString";
  private final Method _valueOf;
  private final FieldHandler _handler;
  public EnumFieldHandler(Class enumType, FieldHandler handler) {
    this._handler = handler;
    this._valueOf = getUnmarshallMethod(enumType);
  }
  private Method getUnmarshallMethod(Class type) {
    if (type == null) {
      String err = "The Class argument passed to the constructor of EnumMarshalDescriptor cannot be null.";
      throw new IllegalArgumentException(err);
    }
    Method method = null;
    try {
      return type.getMethod("fromValue", STRING_ARGS);
    }
    catch (NoSuchMethodException exception) {
      try {
        method = type.getMethod("valueOf", STRING_ARGS);
      }
      catch (NoSuchMethodException nsme) {
        String err = type.getName() + " does not contain one of the required methods public static " + type.getName() + " valueOf(String); " + "or public static " + type.getName() + ".fromvalue(String value)";
        
        throw new IllegalArgumentException(err);
      }
      if (!Modifier.isStatic(method.getModifiers()))
      {
        String err = type.getName() + " public " + type.getName() + " valueOf(String); exists but is not static";
        
        throw new IllegalArgumentException(err);
      }
    }
    return method;
  }
  private Method getMarshallMethod(Class type) {
    if (type == null) {
      String err = "The Class argument passed to the constructor of EnumMarshalDescriptor cannot be null.";
      
      throw new IllegalArgumentException(err);
    }
    Method method = null;
    try {
      return type.getMethod("value", null);
    }
    catch (NoSuchMethodException exception) {
      try {
        method = type.getMethod("toString", null);
      }
      catch (NoSuchMethodException nsme)
      {
        String err = type.getName() + " does not contain one of the required methods value() or toString() ";
        
        throw new IllegalArgumentException(err);
      }
    }
    return method;
  }
  public Object getValue(Object target) throws IllegalStateException {
    Object val = this._handler.getValue(target);
    if (val == null) {
      return val;
    }
    Object result = null;
    if (val.getClass().isArray()) {
      int size = Array.getLength(val);
      String[] values = new String[size];
      for (int i = 0; i < size; i++) {
        Object obj = Array.get(val, i);
        try {
          values[i] = ((String)getMarshallMethod(obj.getClass()).invoke(obj, null));
        }
        catch (Exception e) {
          throw new IllegalStateException(e.toString());
        }
      }
      result = values;
    }
    else {
      try {
        result = getMarshallMethod(val.getClass()).invoke(val, null);
      }
      catch (Exception e) {
        throw new IllegalStateException(e.toString());
      }
    }
    return result;
  }
  public void setValue(Object target, Object value) throws IllegalStateException {
    Object[] args = new String[1];
    Object obj = null;
    if (value != null) {
      args[0] = value.toString();
      try {
        obj = this._valueOf.invoke(null, args);
      }
      catch (InvocationTargetException ite) {
        Throwable toss = ite.getTargetException();
        throw new IllegalStateException(toss.toString());
      }
      catch (IllegalAccessException iae) {
        throw new IllegalStateException(iae.toString());
      }
    }
    this._handler.setValue(target, obj);
  }
  public void resetValue(Object target) {}
  public void checkValidity(Object object) throws ValidityException, IllegalStateException {}
  public Object newInstance(Object parent) throws IllegalStateException { return ""; }
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

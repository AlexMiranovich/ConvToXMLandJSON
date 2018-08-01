package org.exolab.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.sql.Timestamp;
import org.exolab.exceptions.ExecIllegalStateException;


public class DefaultObjectFactory implements ObjectFactory {
  public Object createInstance(Class type)
    throws IllegalAccessException, InstantiationException {
    return createInstance(type, null, null);
  }
  public Object createInstance(Class type, Object[] args)
    throws IllegalAccessException, InstantiationException {
    return createInstance(type, null, args);
  }
  public Object createInstance(Class type, Class[] argTypes, Object[] args)
    throws IllegalAccessException, InstantiationException {
    if ((args == null) || (args.length == 0)) {
      if (java.util.Date.class.isAssignableFrom(type)) {
        return handleDates(type);
      }
      return type.newInstance();
    }
    argTypes = checkArguments(argTypes, args);
    return instantiateUsingConstructor(type, argTypes, args);
  }
  private Object instantiateUsingConstructor(Class type, Class[] argTypes, Object[] args)
    throws InstantiationException, IllegalAccessException {
    try {
      Constructor cons = type.getConstructor(argTypes);
      return cons.newInstance(args);
    }
    catch (NoSuchMethodException nsmx) {
      String err = "unable to find matching public constructor for class: " + type.getName();
      err = err + " with argument types: ";
      for (int i = 0; i < argTypes.length; i++) {
        if (i > 0) {
          err = err + ", ";
        }
        err = err + argTypes[i].getName();
      }
      throw new InstantiationException(err);
    }
    catch (InvocationTargetException ite) {
      throw new ExecIllegalStateException(ite.getMessage(), ite.getTargetException());
    }
  }
  
  private Class[] checkArguments(Class[] argTypes, Object[] args) {
    if (argTypes == null) {
      argTypes = new Class[args.length];
      for (int i = 0; i < args.length; i++) {
        if (args[i] != null) {
          argTypes[i] = args[i].getClass();
        }
        else {
          String err = "null arguments to constructor not accepted if the 'argTypes' array is null.";
          
          throw new IllegalStateException(getClass().getName() + ": " + err);
        }
      }
    }
    else if (argTypes.length != args.length) {
      String err = "The argument type array must be the same length as argument value array.";
      throw new IllegalArgumentException(getClass().getName() + ": " + err);
    }
    return argTypes;
  }
  private Object handleDates(Class type)
    throws IllegalAccessException, InstantiationException {
    java.util.Date date = new java.util.Date();
    if (java.util.Date.class == type) {
      return date;
    }
    if (java.sql.Date.class == type) {
      long time = date.getTime();
      return new java.sql.Date(time);
    }
    if (Time.class == type) {
      long time = date.getTime();
      return new Time(time);
    }
    if (Timestamp.class == type) {
      long time = date.getTime();
      return new Timestamp(time);
    }
    return type.newInstance();
  }
}

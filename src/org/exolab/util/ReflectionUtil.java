package org.exolab.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
  public static Boolean isEnumViaReflection(Class type)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method isEnumMethod = type.getClass().getMethod("isEnum", (Class[])null);
    return (Boolean)isEnumMethod.invoke(type, (Object[])null);
  }
}

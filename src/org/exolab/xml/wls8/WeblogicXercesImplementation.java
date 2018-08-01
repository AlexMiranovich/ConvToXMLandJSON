package org.exolab.xml.wls8;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class WeblogicXercesImplementation {
  private static final Log LOG = LogFactory.getLog(WeblogicXercesImplementation.class);
  protected static Method getMethod(Class aClass, String methodName, Class[] parameterTypes) {
    Method method = null;
    try {
      method = aClass.getMethod(methodName, parameterTypes);
    } catch (SecurityException e) {
      handleStaticInitException("Error while trying to get the method " + methodName + " in class " + aClass, e);
    } catch (NoSuchMethodException e) {
      handleStaticInitException("Error while trying to get the method " + methodName + " in class " + aClass, e);
    }
    return method;
  }
  protected Object invoke(Object anObject, Method method, Object[] params) {
    try {
      return method.invoke(anObject, params);
    } catch (IllegalArgumentException e) {
      handleMethodInvokeException(e);
    } catch (IllegalAccessException e) {
      handleMethodInvokeException(e);
    } catch (InvocationTargetException e) {
      handleMethodInvokeException(e);
    }
    return null;
  }
  public static void handleStaticInitException(Exception e) {
    handleStaticInitException("Error while intializing class", e);
  }
  public static void handleStaticInitException(String message, Exception e) {
    LOG.error(message, e);
    if ((e instanceof RuntimeException)) {
      throw ((RuntimeException)e);
    }
    throw new RuntimeException(message + ". " + e.getMessage());
  }
  protected static void handleMethodInvokeException(Exception e) {
    handleMethodInvokeException("Error while trying to invoke a method", e);
  }
  protected static void handleMethodInvokeException(String message, Exception e) {
    LOG.error(message, e);
    if ((e instanceof RuntimeException)) {
      throw ((RuntimeException)e);
    }
    throw new RuntimeException(message + ". " + e.getMessage());
  }
}

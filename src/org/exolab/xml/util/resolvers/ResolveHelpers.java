
package org.exolab.xml.util.resolvers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ResolveHelpers {
  private static final Log LOG = LogFactory.getLog(ResolveHelpers.class);
  public static String getQualifiedFileName(String fileName, String packageName) {
    if ((packageName == null) || (packageName.length() == 0)) {
      return fileName;
    }
    StringBuffer result = new StringBuffer();
    result.append(packageName.replace('.', '/'));
    result.append('/');
    result.append(fileName);
    return result.toString();
  }
  public static String getPackageName(String className) {
    if (className == null) {
      return "";
    }
    int idx = className.lastIndexOf('.');
    if (idx >= 0) {
      return className.substring(0, idx);
    }
    return "";
  }
  public static boolean namespaceEquals(String ns1, String ns2) {
    if (ns1 == null) {
      return (ns2 == null) || (ns2.length() == 0);
    }
    if (ns2 == null) {
      return ns1.length() == 0;
    }
    return ns1.equals(ns2);
  }
  public static ClassLoader getClassLoader(ClassLoader loader) {
    if (loader != null) {
      return loader;
    }
    return Thread.currentThread().getContextClassLoader();
  }
  public static Class loadClass(ClassLoader classLoader, String className) {
    if (classLoader == null) {
      String message = "Argument class loader must not be null";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    if ((className == null) || (className.length() == 0)) {
      LOG.debug("Name of class to load is null or empty -> ignored!");
      return null;
    }
    try {
      return classLoader.loadClass(className);
    }
    catch (ClassNotFoundException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Ignored problem at loading class: " + className + " through class loader: " + classLoader + ", exception: " + e);
      }
    }
    return null;
  }
}

package org.exec.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractProperties {
  private static final String USER_PROPERTIES_SYSTEM_PROPERTY = "org.exec.user.properties.location";
  private static final Log LOG = LogFactory.getLog(AbstractProperties.class);
  private final ClassLoader _applicationClassLoader;
  private final ClassLoader _domainClassLoader;
  private final AbstractProperties _parent;
  private final Map _map = new HashMap();
  protected AbstractProperties()
  {
    this(null, null);
  }
  protected AbstractProperties(ClassLoader app, ClassLoader domain) {
    this._applicationClassLoader = (app != null ? app : getClass().getClassLoader());
    this._domainClassLoader = (domain != null ? domain : getClass().getClassLoader());
    this._parent = null;
  }
  protected AbstractProperties(AbstractProperties parent) {
    this._applicationClassLoader = parent.getApplicationClassLoader();
    this._domainClassLoader = parent.getDomainClassLoader();
    this._parent = parent;
  }
  public final ClassLoader getApplicationClassLoader() {
    return this._applicationClassLoader;
  }
  public final ClassLoader getDomainClassLoader() {
    return this._domainClassLoader;
  }
  protected void loadDefaultProperties(String path, String filename) {
    Properties properties = new Properties();
    boolean inExecJar = loadFromClassPath(properties, path + filename);
    boolean inJavaLibDir = loadFromJavaHome(properties, filename);
    if ((!inExecJar) && (!inJavaLibDir)) {
      throw new PropertiesException("Failed to load properties: " + filename);
    }
    
    this._map.putAll(properties);
  }
  protected void loadUserProperties(String filename) {
    Properties properties = new Properties();
    boolean userPropertiesLoaded = loadFromClassPath(properties, "/" + filename);
    if (!userPropertiesLoaded) {
      userPropertiesLoaded = loadFromWorkingDirectory(properties, filename);
    }
    if (!userPropertiesLoaded) {
      String property = System.getProperty("org.exec.user.properties.location");
      if ((property != null) && (property.length() > 0)) {
        File file = new File(property);
        if (file.exists()) {
          LOG.info("Loading custom Exec properties from " + file.getAbsolutePath());
          userPropertiesLoaded = loadFromFile(properties, file);
        } else {
          LOG.warn(file.getAbsolutePath() + " is not a valid file.");
        }
      }
    }
    this._map.putAll(properties);
  }
  private boolean loadFromClassPath(Properties properties, String filename){
    InputStream classPathStream = null;
    try {
      URL url = getClass().getResource(filename);
      if (url != null) {
        classPathStream = url.openStream();
        properties.load(classPathStream);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Properties loaded from classpath: " + filename);
        }
        return true;
      }
      return false;
    } catch (Exception ex) { boolean bool;
      LOG.warn("Failed to load properties from classpath: " + filename, ex);
      return false;
    } finally {
      if (classPathStream != null) {
        try {
          classPathStream.close();
        } catch (IOException e) {
          LOG.warn("Failed to close properties from classpath: " + filename);
        }
      }
    }
  }
  private boolean loadFromJavaHome(Properties properties, String filename) {
    try
    {
      String javaHome = System.getProperty("java.home");
      if (javaHome == null) return false;
      return loadFromFile(properties, new File(new File(javaHome, "lib"), filename));
    } catch (SecurityException ex) {
      LOG.warn("Security policy prevented access to system property 'java.home'.", ex); }
    return false;
  }
  private boolean loadFromWorkingDirectory(Properties properties, String filename) {
    return loadFromFile(properties, new File(filename));
  }
  private boolean loadFromFile(Properties properties, File file) {
    InputStream fileStream = null;
    try { boolean bool;
      if ((file.exists()) && (file.canRead())) {
        fileStream = new FileInputStream(file);
        properties.load(fileStream);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Properties file loaded: " + file);
        }
        return true;
      }
      return false;
    } catch (SecurityException ex) {
      LOG.warn("Security policy prevented access to properties file: " + file, ex);
    } catch (Exception ex) {
      LOG.warn("Failed to load properties file: " + file, ex);
    } finally {
      if (fileStream != null) {
        try {
          fileStream.close();
        } catch (IOException e) {
          LOG.warn("Failed to close properties file: " + file);
        }
      }
    }
    return false;
  }
  public final synchronized Object put(String key, Object value) {
    if (value == null) throw new NullPointerException();
    return this._map.put(key, value);
  }
  public final synchronized Object remove(String key) {
    return this._map.remove(key);
  }
  protected synchronized Object get(String key) {
    Object value = this._map.get(key);
    if ((value == null) && (this._parent != null)) {
      value = this._parent.get(key);
    }
    return value;
  }
  public final Boolean getBoolean(String key) {
    Object objectValue = get(key);
    if (objectValue == null)
      return null;
    if ((objectValue instanceof Boolean))
      return (Boolean)objectValue;
    if ((objectValue instanceof String)) {
      String stringValue = (String)objectValue;
      if ("true".equalsIgnoreCase(stringValue))
        return Boolean.TRUE;
      if ("false".equalsIgnoreCase(stringValue)) {
        return Boolean.FALSE;
      }
    }
    Object[] args = { key, objectValue };
    String msg = "Properties value can not be converted to boolean: {0}={1}";
    throw new PropertiesException(MessageFormat.format(msg, args));
  }
  public final boolean getBoolean(String key, boolean defaultValue) {
    Object objectValue = get(key);
    if ((objectValue instanceof Boolean))
      return ((Boolean)objectValue).booleanValue();
    if ((objectValue instanceof String)) {
      String stringValue = (String)objectValue;
      if ("true".equalsIgnoreCase(stringValue))
        return true;
      if ("false".equalsIgnoreCase(stringValue)) {
        return false;
      }
    }
    return defaultValue;
  }
  public final Integer getInteger(String key) {
    Object objectValue = get(key);
    if (objectValue == null)
      return null;
    if ((objectValue instanceof Integer))
      return (Integer)objectValue;
    if ((objectValue instanceof String)) {
      try {
        return Integer.valueOf((String)objectValue);
      } catch (NumberFormatException ex) {
        Object[] args = { key, objectValue };
        String msg = "Properties value can not be converted to int: {0}={1}";
        throw new PropertiesException(MessageFormat.format(msg, args), ex);
      }
    }
    Object[] args = { key, objectValue };
    String msg = "Properties value can not be converted to int: {0}={1}";
    throw new PropertiesException(MessageFormat.format(msg, args));
  }
  public final int getInteger(String key, int defaultValue) {
    Object objectValue = get(key);
    if ((objectValue instanceof Integer))
      return ((Integer)objectValue).intValue();
    if ((objectValue instanceof String)) {
      String stringValue = (String)objectValue;
      try {
        return Integer.parseInt(stringValue);
      } catch (NumberFormatException ex) {
        return defaultValue;
      }
    }
    return defaultValue;
  }
  public final String getString(String key) {
    Object objectValue = get(key);
    if (objectValue == null)
      return null;
    if ((objectValue instanceof String)) {
      return (String)objectValue;
    }
    Object[] args = { key, objectValue };
    String msg = "Properties value is not a string: {0}={1}";
    throw new PropertiesException(MessageFormat.format(msg, args));
  }
  public final String getString(String key, String defaultValue) {
    Object objectValue = get(key);
    if (((objectValue instanceof String)) && (!"".equals(objectValue))) {
      return (String)objectValue;
    }
    return defaultValue;
  }
  public final String[] getStringArray(String key) {
    Object objectValue = get(key);
    if (objectValue == null)
      return null;
    if ((objectValue instanceof String[]))
      return (String[])objectValue;
    if ((objectValue instanceof String)) {
      return ((String)objectValue).split(",");
    }
    Object[] args = { key, objectValue };
    String msg = "Properties value is not a String[]: {0}={1}";
    throw new PropertiesException(MessageFormat.format(msg, args));
  }
  public final Class getClass(String key, ClassLoader loader) {
    Object objectValue = get(key);
    if (objectValue == null)
      return null;
    if ((objectValue instanceof Class))
      return (Class)objectValue;
    if ((objectValue instanceof String)) {
      String classname = (String)objectValue;
      try {
        return loader.loadClass(classname);
      } catch (ClassNotFoundException ex) {
        Object[] args = { key, classname };
        String msg = "Could not find class of properties value: {0}={1}";
        throw new PropertiesException(MessageFormat.format(msg, args), ex);
      }
    }
    Object[] args = { key, objectValue };
    String msg = "Properties value is not a Class: {0}={1}";
    throw new PropertiesException(MessageFormat.format(msg, args));
  }
  public final Class[] getClassArray(String key, ClassLoader loader) {
    Object objectValue = get(key);
    if (objectValue == null)
      return null;
    if ((objectValue instanceof Class[]))
      return (Class[])objectValue;
    if ((objectValue instanceof String)) {
      String[] classnames = ((String)objectValue).split(",");
      Class[] classes = new Class[classnames.length];
      for (int i = 0; i < classnames.length; i++) {
        try {
          classes[i] = loader.loadClass(classnames[i]);
        } catch (ClassNotFoundException ex) {
          Object[] args = { key, new Integer(i), classnames[i] };
          String msg = "Could not find class of properties value: {0}[{1}]={2}";
          throw new PropertiesException(MessageFormat.format(msg, args), ex);
        }
      }
      return classes;
    }
    Object[] args = { key, objectValue };
    String msg = "Properties value is not a Class[]: {0}={1}";
    throw new PropertiesException(MessageFormat.format(msg, args));
  }
  public final Object getObject(String key) {
    return get(key);
  }
  public final Object[] getObjectArray(String key, ClassLoader loader) {
    Object objectValue = get(key);
    if (objectValue == null)
      return null;
    if ((objectValue instanceof Object[]))
      return (Object[])objectValue;
    if ((objectValue instanceof String)) {
      List objects = new ArrayList();
      String[] classnames = ((String)objectValue).split(",");
      for (int i = 0; i < classnames.length; i++) {
        String classname = classnames[i];
        try {
          if ((classname != null) && (!"".equals(classname.trim()))) {
            classname = classname.trim();
            objects.add(loader.loadClass(classname).newInstance());
          }
        } catch (ClassNotFoundException ex) {
          Object[] args = { key, new Integer(i), classname };
          String msg = "Could not find configured class: {0}[{1}]={2}";
          throw new PropertiesException(MessageFormat.format(msg, args), ex);
        } catch (IllegalAccessException ex) {
          Object[] args = { key, new Integer(i), classname };
          String msg = "Could not instantiate configured class: {0}[{1}]={2}";
          throw new PropertiesException(MessageFormat.format(msg, args), ex);
        } catch (InstantiationException ex) {
          Object[] args = { key, new Integer(i), classname };
          String msg = "Could not instantiate configured class: {0}[{1}]={2}";
          throw new PropertiesException(MessageFormat.format(msg, args), ex);
        } catch (ExceptionInInitializerError ex) {
          Object[] args = { key, new Integer(i), classname };
          String msg = "Could not instantiate configured class: {0}[{1}]={2}";
          throw new PropertiesException(MessageFormat.format(msg, args), ex);
        } catch (SecurityException ex) {
          Object[] args = { key, new Integer(i), classname };
          String msg = "Could not instantiate configured class: {0}[{1}]={2}";
          throw new PropertiesException(MessageFormat.format(msg, args), ex);
        }
      }
      return objects.toArray();
    }
    Object[] args = { key, objectValue };
    String msg = "Properties value is not an Object[]: {0}={1}";
    throw new PropertiesException(MessageFormat.format(msg, args));
  }
}

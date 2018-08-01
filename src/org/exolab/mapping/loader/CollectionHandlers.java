package org.exolab.mapping.loader;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.exec.core.util.AbstractProperties;
import org.exec.xml.XMLProperties;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.MappingException;

public final class CollectionHandlers {
  private static Class _collectionClass = null;
  private static boolean _loadedCollectionClass = false;
  private static Info[] _info;
  public static Class getCollectionType(String name)
    throws MappingException {
    if (_info == null) {
      loadInfo();
    }
    for (int i = 0; i < _info.length; i++) {
      if ((_info[i].shortName.equalsIgnoreCase(name)) || (_info[i].javaClass.getName().equals(name))) {
        return _info[i].javaClass;
      }
    }
    if (!_loadedCollectionClass) {
      _loadedCollectionClass = true;
      try
      {
        _collectionClass = Class.forName("java.util.Collection");
      }
      catch (ClassNotFoundException cnfe) {}
    }
    return _collectionClass;
  }
  public static boolean hasHandler(Class javaClass) {
    if (_info == null) {
      loadInfo();
    }
    if ((javaClass.isArray()) && 
      (javaClass.getComponentType() != Byte.TYPE)) {
      javaClass = Object[].class;
    }
    for (int i = 0; i < _info.length; i++) {
      if (_info[i].javaClass.isAssignableFrom(javaClass)) {
        return true;
      }
    }
    return false;
  }
  public static String getCollectionName(Class javaClass) {
    if (_info == null) {
      loadInfo();
    }
    if ((javaClass.isArray()) && 
      (javaClass.getComponentType() != Byte.TYPE)) {
      javaClass = Object[].class;
    }
    for (int i = 0; i < _info.length; i++) {
      if (_info[i].javaClass.equals(javaClass)) {
        return _info[i].shortName;
      }
    }
    for (int i = 0; i < _info.length; i++) {
      if (_info[i].javaClass.isAssignableFrom(javaClass)) {
        return _info[i].shortName;
      }
    }
    return null;
  }
  public static CollectionHandler getHandler(Class javaClass)
    throws MappingException {
    if (_info == null) {
      loadInfo();
    }
    if ((javaClass.isArray()) && 
      (javaClass.getComponentType() != Byte.TYPE)) {
      javaClass = Object[].class;
    }
    for (int i = 0; i < _info.length; i++) {
      if (_info[i].javaClass.equals(javaClass)) {
        return _info[i].handler;
      }
    }
    for (int i = 0; i < _info.length; i++) {
      if (_info[i].javaClass.isAssignableFrom(javaClass)) {
        return _info[i].handler;
      }
    }
    throw new MappingException("mapping.noCollectionHandler", javaClass.getName());
  }
  public static boolean isGetSetCollection(Class javaClass)
    throws MappingException {
    if (_info == null) {
      loadInfo();
    }
    for (int i = 0; i < _info.length; i++) {
      if (_info[i].javaClass.equals(javaClass)) {
        return _info[i].getSetCollection;
      }
    }
    throw new MappingException("mapping.noCollectionHandler", javaClass.getName());
  }
  private static synchronized void loadInfo() {
    if (_info == null)
    {
      Vector allInfo = new Vector();
      AbstractProperties properties = XMLProperties.newInstance();
      StringTokenizer tokenizer = new StringTokenizer(properties.getString("org.exolab.mapping.collections", ""), ", ");
      while (tokenizer.hasMoreTokens()) {
        try {
          Class infoClass;
          if (CollectionHandlers.class.getClassLoader() != null) {
            infoClass = CollectionHandlers.class.getClassLoader().loadClass(tokenizer.nextToken());
          } else {
            infoClass = Class.forName(tokenizer.nextToken());
          }
          Method method = infoClass.getMethod("getCollectionHandlersInfo", (Class[])null);
          Info[] info = (Info[])method.invoke(null, (Object[])null);
          for (int i = 0; i < info.length; i++) {
            allInfo.addElement(info[i]);
          }
        }
        catch (Exception except) {}
      }
      _info = new Info[allInfo.size()];
      allInfo.copyInto(_info);
    }
  }
  static class Info {
    final String shortName;
    final Class javaClass;
    final CollectionHandler handler;
    final boolean getSetCollection;
    Info(String shortName, Class javaClass, boolean getSetCollection, CollectionHandler handler) {
      this.shortName = shortName;
      this.javaClass = javaClass;
      this.handler = handler;
      this.getSetCollection = getSetCollection;
    }
  }
  static final class EmptyEnumerator implements Enumeration, Serializable {
    public boolean hasMoreElements()
    {
      return false;
    }
    public Object nextElement()
    {
      throw new NoSuchElementException();
    }
  }
}

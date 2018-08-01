package org.exolab.mapping.handlers;

import java.util.Hashtable;
import org.exolab.mapping.MapHandler;

public final class MapHandlers {
  private static final String J2MAP_CLASSNAME = "java.util.Map";
  private static final String J2MAP_HANDLER_CLASSNAME = "org.exolab.mapping.handlers.J2MapHandler";
  private static final MapHandler HASHTABLE_HANDLER = new J1MapHandler();
  private static MapHandler _anymapHandler;
  private static Class _j2mapClass = null;
  static {
    if (_j2mapClass == null) {
      try {
        ClassLoader loader = MapHandlers.class.getClassLoader();
        if (loader != null) {
          _j2mapClass = loader.loadClass("java.util.Map");
        } else {
          _j2mapClass = Class.forName("java.util.Map");
        }
        Class handler = Class.forName("org.exolab.mapping.handlers.J2MapHandler");
        _anymapHandler = (MapHandler)handler.newInstance();
      }
      catch (ClassNotFoundException cnfe) {}catch (InstantiationException ie) {}catch (IllegalAccessException iae) {}
    }
  }
  public static MapHandler getHandler(Object object) {
    if (object == null) {
      return null;
    }
    return getHandler(object.getClass());
  }
  public static MapHandler getHandler(Class clazz) {
    if (clazz == null) {
      return null;
    }
    if (Hashtable.class.isAssignableFrom(clazz)) {
      return HASHTABLE_HANDLER;
    }
    if ((_j2mapClass != null) && 
      (_j2mapClass.isAssignableFrom(clazz))) {
      return _anymapHandler;
    }
    return null;
  }
}

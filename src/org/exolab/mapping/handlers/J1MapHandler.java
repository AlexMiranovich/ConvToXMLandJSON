package org.exolab.mapping.handlers;

import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.mapping.MapHandler;

public final class J1MapHandler implements MapHandler {
  public Object create()
  {
    return new Hashtable();
  }
  public Object put(Object map, Object key, Object object) throws ClassCastException {
    Object returnVal = null;
    if (map == null) {
      map = new Hashtable();
      returnVal = map;
    }
    ((Hashtable)map).put(key, object);
    return returnVal;
  }
  public Enumeration elements(Object map) throws ClassCastException {
    if (map == null) {
      map = new Hashtable();
    }
    return ((Hashtable)map).elements();
  }
  public Enumeration keys(Object map) throws ClassCastException {
    if (map == null) {
      map = new Hashtable();
    }
    return ((Hashtable)map).keys();
  }
  public int size(Object map) throws ClassCastException {
    if (map == null) {
      return 0;
    }
    return ((Hashtable)map).size();
  }
  public void clear(Object map) throws ClassCastException {
    if (map == null) {
      return;
    }
    ((Hashtable)map).clear();
  }
  public Object get(Object map, Object key) throws ClassCastException {
    if (map == null) {
      return null;
    }
    return ((Hashtable)map).get(key);
  }
}

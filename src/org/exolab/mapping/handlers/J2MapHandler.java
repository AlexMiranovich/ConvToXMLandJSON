package org.exolab.mapping.handlers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.exolab.mapping.MapHandler;

public final class J2MapHandler implements MapHandler {
  public Object create()
  {
    return new HashMap();
  }
  public Object put(Object map, Object key, Object object) throws ClassCastException {
    Object returnVal = null;
    if (map == null) {
      map = create();
      returnVal = map;
    }
    ((Map)map).put(key, object);
    return returnVal;
  }
  public Enumeration elements(Object map) throws ClassCastException {
    if (map == null) {
      map = create();
    }
    return new IteratorEnumerator(((Map)map).values().iterator());
  }
  public Enumeration keys(Object map) throws ClassCastException {
    if (map == null) {
      map = create();
    }
    return new IteratorEnumerator(((Map)map).keySet().iterator());
  }
  public int size(Object map) throws ClassCastException {
    if (map == null) {
      return 0;
    }
    return ((Map)map).size();
  }
  public void clear(Object map) throws ClassCastException {
    if (map == null) {
      return;
    }
    ((Map)map).clear();
  }
  public Object get(Object map, Object key) throws ClassCastException {
    if (map == null) {
      return null;
    }
    return ((Map)map).get(key);
  }
  static final class IteratorEnumerator implements Enumeration {
    private final Iterator _iterator;
    IteratorEnumerator(Iterator iterator)
    {
      this._iterator = iterator;
    }
    public boolean hasMoreElements()
    {
      return this._iterator.hasNext();
    }
    public Object nextElement()
    {
      return this._iterator.next();
    }
  }
}

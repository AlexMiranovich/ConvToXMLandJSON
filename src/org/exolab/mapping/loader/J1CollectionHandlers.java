package org.exolab.mapping.loader;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.MapItem;

public final class J1CollectionHandlers {
  public static CollectionHandlers.Info[] getCollectionHandlersInfo()
  {
    return _colHandlers;
  }
  private static CollectionHandlers.Info[] _colHandlers = { new CollectionHandlers.Info("array", Object[].class, true, new CollectionHandler() {
    public Object add(Object collection, Object object) {
      if (collection == null) {
        Object newArray = Array.newInstance(object.getClass(), 1);
        Array.set(newArray, 0, object);
        return newArray;
      }
      Class type = collection.getClass();
      if (!type.isArray()) {
        String err = "J1CollectionHandlers.array#add: type mismatch, expecting an array, instead received: ";
        
        err = err + type.getName();
        throw new IllegalArgumentException(err);
      }
      type = type.getComponentType();
      Object newArray = Array.newInstance(type, Array.getLength(collection) + 1);
      for (int i = 0; i < Array.getLength(collection); i++) {
        Array.set(newArray, i, Array.get(collection, i));
      }
      Array.set(newArray, Array.getLength(collection), object);
      return newArray;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new ArrayEnumerator(collection);
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return Array.getLength(collection);
    }
    public Object clear(Object collection) {
      if (collection == null) {
        return null;
      }
      Class type = collection.getClass();
      if (!type.isArray()) {
        String err = "J1CollectionHandlers.array#add: type mismatch, expecting an array, instead received: ";
        err = err + type.getName();
        throw new IllegalArgumentException(err);
      }
      type = type.getComponentType();
      return Array.newInstance(type, 0);
    }
    public String toString()
    {
      return "Object[]";
    }
  }), new CollectionHandlers.Info("vector", Vector.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object) {
      if (collection == null) {
        collection = new Vector();
        ((Vector)collection).addElement(object);
        return collection;
      }
      ((Vector)collection).addElement(object);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return ((Vector)collection).elements();
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((Vector)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((Vector)collection).removeAllElements();
      }
      return null;
    }
    public String toString()
    {
      return "Vector";
    }
  }), new CollectionHandlers.Info("hashtable", Hashtable.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object) {
      Object key = object;
      Object value = object;
      if ((object instanceof MapItem)) {
        MapItem mapItem = (MapItem)object;
        key = mapItem.getKey();
        value = mapItem.getValue();
        if (value == null) {
          value = object;
        }
        if (key == null) {
          key = value;
        }
      }if (collection == null) {
        collection = new Hashtable();
        ((Hashtable)collection).put(key, value);
        return collection;
      }
      ((Hashtable)collection).put(key, value);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return ((Hashtable)collection).elements();
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((Hashtable)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((Hashtable)collection).clear();
      }
      return null;
    }
    public String toString()
    {
      return "Hashtable";
    }
  }), new CollectionHandlers.Info("enumerate", Enumeration.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object)
    {
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return (Enumeration)collection;
    }
    public int size(Object collection)
    {
      return 0;
    }
    public Object clear(Object collection)
    {
      return null;
    }
    public String toString()
    {
      return "Enumeration";
    }
  }) };
  static final class ArrayEnumerator implements Enumeration {
    private final Object _array;
    private int _index;
    ArrayEnumerator(Object array)
    {
      this._array = array;
    }
    public boolean hasMoreElements()
    {
      return this._index < Array.getLength(this._array);
    }
    public Object nextElement() {
      if (this._index >= Array.getLength(this._array)) {
        throw new NoSuchElementException();
      }
      return Array.get(this._array, this._index++);
    }
  }
}

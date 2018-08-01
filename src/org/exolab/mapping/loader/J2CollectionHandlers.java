package org.exolab.mapping.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.MapItem;

public final class J2CollectionHandlers {
  public static CollectionHandlers.Info[] getCollectionHandlersInfo()
  {
    return _colHandlers;
  }
  private static CollectionHandlers.Info[] _colHandlers = { new CollectionHandlers.Info("list", List.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object) {
      if (collection == null) {
        collection = new ArrayList();
        ((Collection)collection).add(object);
        return collection;
      }
      ((Collection)collection).add(object);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new IteratorEnumerator(((Collection)collection).iterator());
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((Collection)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((Collection)collection).clear();
      }
      return null;
    }
    public String toString()
    {
      return "List";
    }
  }), new CollectionHandlers.Info("arraylist", ArrayList.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object) {
      if (collection == null) {
        collection = new ArrayList();
        ((Collection)collection).add(object);
        return collection;
      }
      ((Collection)collection).add(object);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new IteratorEnumerator(((Collection)collection).iterator());
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((Collection)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((Collection)collection).clear();
      }
      return null;
    }
    public String toString()
    {
      return "ArrayList";
    }
  }), new CollectionHandlers.Info("collection", Collection.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object) {
      if (collection == null) {
        collection = new ArrayList();
        ((Collection)collection).add(object);
        return collection;
      }
      ((Collection)collection).add(object);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new IteratorEnumerator(((Collection)collection).iterator());
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((Collection)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((Collection)collection).clear();
      }
      return null;
    }
    public String toString()
    {
      return "Collection";
    }
  }), new CollectionHandlers.Info("set", Set.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object) {
      if (collection == null) {
        collection = new HashSet();
        ((Set)collection).add(object);
        return collection;
      }
      ((Set)collection).add(object);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new IteratorEnumerator(((Set)collection).iterator());
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((Set)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((Set)collection).clear();
      }
      return null;
    }
    public String toString()
    {
      return "Set";
    }
  }), new CollectionHandlers.Info("map", Map.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object) {
      Object key = object;
      Object value = object;
      if ((object instanceof MapItem)) {
        MapItem item = (MapItem)object;
        key = item.getKey();
        value = item.getValue();
        if (value == null) {
          value = object;
        }
        if (key == null) {
          key = value;
        }
      }
      if (collection == null) {
        collection = new HashMap();
        ((Map)collection).put(key, value);
        return collection;
      }
      ((Map)collection).put(key, value);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new IteratorEnumerator(((Map)collection).values().iterator());
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((Map)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((Map)collection).clear();
      }
      return null;
    }
    public String toString()
    {
      return "Map";
    }
  }), new CollectionHandlers.Info("sortedset", SortedSet.class, false, new SortedSetCollectionHandler(null)), new CollectionHandlers.Info("sortedmap", SortedMap.class, false, new SortedMapCollectionHandler(null)), new CollectionHandlers.Info("iterator", Iterator.class, false, new CollectionHandler() {
    public Object add(Object collection, Object object)
    {
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new IteratorEnumerator((Iterator)collection);
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
      return "Iterator";
    }
  }) };
  private static final class SortedSetCollectionHandler
    implements CollectionHandler {
    public SortedSetCollectionHandler(Object o) {
    }

    public Object add(Object collection, Object object) {
      if (collection == null) {
        collection = new TreeSet();
        ((Set)collection).add(object);
        return collection;
      }
      ((Set)collection).add(object);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new IteratorEnumerator(((Set)collection).iterator());
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((Set)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((Set)collection).clear();
      }
      return null;
    }
    
    public String toString()
    {
      return "SortedSet";
    }
  }
  private static final class SortedMapCollectionHandler
    implements CollectionHandler {
    public SortedMapCollectionHandler(Object o) {
    }

    public Object add(Object collection, Object object) {
      Object key = object;
      Object value = object;
      if ((object instanceof MapItem)) {
        MapItem item = (MapItem)object;
        key = item.getKey();
        value = item.getValue();
        if (value == null) {
          value = object;
        }
        if (key == null) {
          key = value;
        }
      }if (collection == null) {
        collection = new TreeMap();
        ((SortedMap)collection).put(key, value);
        return collection;
      }
      ((SortedMap)collection).put(key, value);
      return null;
    }
    public Enumeration elements(Object collection) {
      if (collection == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return new IteratorEnumerator(((SortedMap)collection).values().iterator());
    }
    public int size(Object collection) {
      if (collection == null) {
        return 0;
      }
      return ((SortedMap)collection).size();
    }
    public Object clear(Object collection) {
      if (collection != null) {
        ((SortedMap)collection).clear();
      }
      return null;
    }
    public String toString()
    {
      return "SortedMap";
    }
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

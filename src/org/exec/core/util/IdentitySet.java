package org.exec.core.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public final class IdentitySet implements Set {
  private static final int DEFAULT_CAPACITY = 17;
  private static final float DEFAULT_LOAD = 0.75F;
  private static final int DEFAULT_ENTRIES = 12;
  private static final int DEFAULT_INCREMENT = 2;
  private static final int FIRST_PRIME_TO_CHECK = 3;
  private int _capacity;
  private int _maximum;
  private Entry[] _buckets;
  private int _entries = 0;
  public IdentitySet() {
    this._capacity = 17;
    this._maximum = 12;
    this._buckets = new Entry[17];
  }
  public IdentitySet(int capacity) {
    this._capacity = capacity;
    this._maximum = ((int)(capacity * 0.75F));
    this._buckets = new Entry[capacity];
  }
  public void clear() {
    this._capacity = 17;
    this._maximum = 12;
    this._buckets = new Entry[17];
    this._entries = 0;
  }
  public int size()
  {
    return this._entries;
  }
  public boolean isEmpty()
  {
    return this._entries == 0;
  }
  public boolean add(Object key) {
    int hash = System.identityHashCode(key);
    int index = hash % this._capacity;
    if (index < 0) { index = -index;
    }
    Entry entry = this._buckets[index];
    Entry prev = null;
    while (entry != null) {
      if (entry.getKey() == key) {
        return false;
      }
      prev = entry;
      entry = entry.getNext();
    }
    if (prev == null) {
      this._buckets[index] = new Entry(key, hash);
    }
    else {
      prev.setNext(new Entry(key, hash));
    }
    this._entries += 1;
    if (this._entries > this._maximum) rehash();
    return true;
  }
  private void rehash() {
    long nextCapacity = this._capacity * 2;
    if (nextCapacity > 2147483647L) return;
    nextCapacity = nextPrime(nextCapacity);
    if (nextCapacity > 2147483647L) { return;
    }
    int newCapacity = (int)nextCapacity;
    Entry[] newBuckets = new Entry[newCapacity];
    Entry entry = null;
    Entry temp = null;
    Entry next = null;
    int newIndex = 0;
    for (int index = 0; index < this._capacity; index++) {
      entry = this._buckets[index];
      while (entry != null) {
        next = entry.getNext();
        newIndex = entry.getHash() % newCapacity;
        if (newIndex < 0) { newIndex = -newIndex;
        }
        temp = newBuckets[newIndex];
        if (temp == null) {
          entry.setNext(null);
        }
        else {
          entry.setNext(temp);
        }
        newBuckets[newIndex] = entry;
        
        entry = next;
      }
    }
    this._capacity = newCapacity;
    this._maximum = ((int)(newCapacity * 0.75F));
    this._buckets = newBuckets;
  }
  private long nextPrime(long minimum) {
    long candidate = (minimum + 1L) / 2L * 2L + 1L;
    while (!isPrime(candidate)) candidate += 2L;
    return candidate;
  }
  private boolean isPrime(long candidate) {
    if (candidate / 2L * 2L == candidate) return false;
    long stop = candidate / 2L;
    for (long i = 3L; i < stop; i += 2L) {
      if (candidate / i * i == candidate) return false;
    }
    return true;
  }
  public boolean contains(Object key) {
    int hash = System.identityHashCode(key);
    int index = hash % this._capacity;
    if (index < 0) { index = -index;
    }
    Entry entry = this._buckets[index];
    while (entry != null) {
      if (entry.getKey() == key) return true;
      entry = entry.getNext();
    }
    return false;
  }
  public boolean remove(Object key) {
    int hash = System.identityHashCode(key);
    int index = hash % this._capacity;
    if (index < 0) { index = -index; }
    Entry entry = this._buckets[index];
    Entry prev = null;
    while (entry != null) {
      if (entry.getKey() == key) {
        if (prev == null) {
          this._buckets[index] = entry.getNext();
        }
        else {
          prev.setNext(entry.getNext());
        }
        this._entries -= 1;
        return true;
      }
      prev = entry;
      entry = entry.getNext();
    }
    return false;
  }
  public Iterator iterator()
  {
    return new IdentityIterator();
  }
  public Object[] toArray() {
    Object[] result = new Object[this._entries];
    int j = 0;
    for (int i = 0; i < this._capacity; i++) {
      Entry entry = this._buckets[i];
      while (entry != null) {
        result[(j++)] = entry.getKey();
        entry = entry.getNext();
      }
    }
    return result;
  }
  public Object[] toArray(Object[] a) {
    Object[] result = a;
    if (result.length < this._entries) {
      result = (Object[])Array.newInstance(result.getClass().getComponentType(), this._entries);
    }
    int j = 0;
    for (int i = 0; i < this._capacity; i++) {
      Entry entry = this._buckets[i];
      while (entry != null) {
        result[(j++)] = entry.getKey();
        entry = entry.getNext();
      }
    }
    while (j < result.length) {
      result[(j++)] = null;
    }
    return result;
  }
  public boolean containsAll(Collection c)
  {
    throw new UnsupportedOperationException();
  }
  public boolean addAll(Collection c)
  {
    throw new UnsupportedOperationException();
  }
  public boolean removeAll(Collection c)
  {
    throw new UnsupportedOperationException();
  }
  public boolean retainAll(Collection c)
  {
    throw new UnsupportedOperationException();
  }
  public final class Entry {
    private Object _key;
    private int _hash;
    private Entry _next = null;
    public Entry(Object key, int hash) {
      this._key = key;
      this._hash = hash;
    }
    public Object getKey()
    {
      return this._key;
    }
    public int getHash()
    {
      return this._hash;
    }
    public void setNext(Entry next)
    {
      this._next = next;
    }
    public Entry getNext()
    {
      return this._next;
    }
  }
  private class IdentityIterator
    implements Iterator {
    private int _index = 0;
    private Entry _next = IdentitySet.this._buckets[0];
    public IdentityIterator() {
      if (IdentitySet.this._entries > 0) {
        while ((this._next == null) && (++this._index < IdentitySet.this._capacity)) {
          this._next = IdentitySet.this._buckets[this._index];
        }
      }
    }
    public boolean hasNext()
    {
      return this._next != null;
    }
    public Object next() {
      Entry entry = this._next;
      if (entry == null) { throw new NoSuchElementException();
      }
      this._next = entry.getNext();
      while ((this._next == null) && (++this._index < IdentitySet.this._capacity)) {
        this._next = IdentitySet.this._buckets[this._index];
      }
      
      return entry.getKey();
    }
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}

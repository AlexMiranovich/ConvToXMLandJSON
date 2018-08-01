package org.exec.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class IdentityMap implements Map {
  private static final int DEFAULT_CAPACITY = 17;
  private static final float DEFAULT_LOAD = 0.75F;
  private static final int DEFAULT_ENTRIES = 12;
  private static final int DEFAULT_INCREMENT = 2;
  private static final int FIRST_PRIME_TO_CHECK = 3;
  private int _capacity;
  private int _maximum;
  private Entry[] _buckets;
  private int _entries;
  public IdentityMap() {
    this._capacity = 17;
    this._maximum = 12;
    this._buckets = new Entry[17];
    this._entries = 0;
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
  public Object put(Object key, Object value) {
    int hash = System.identityHashCode(key);
    int index = hash % this._capacity;
    if (index < 0) { index = -index;
    }
    Entry entry = this._buckets[index];
    Entry prev = null;
    while (entry != null) {
      if (entry.getKey() == key) {
        Object ret = entry.getValue();
        entry.setValue(value);
        return ret;
      }
      prev = entry;
      entry = entry.getNext();
    }
    if (prev == null) {
      this._buckets[index] = new Entry(key, hash, value);
    }
    else {
      prev.setNext(new Entry(key, hash, value));
    }
    this._entries += 1;
    if (this._entries > this._maximum) rehash();
    return null;
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
  public boolean containsKey(Object key) {
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
  public Object get(Object key) {
    int hash = System.identityHashCode(key);
    int index = hash % this._capacity;
    if (index < 0) { index = -index;
    }
    Entry entry = this._buckets[index];
    while (entry != null) {
      if (entry.getKey() == key) return entry.getValue();
      entry = entry.getNext();
    }
    return null;
  }
  public Object remove(Object key) {
    int hash = System.identityHashCode(key);
    int index = hash % this._capacity;
    if (index < 0) { index = -index;
    }
    Entry entry = this._buckets[index];
    Entry prev = null;
    while (entry != null) {
      if (entry.getKey() == key) {
        if (prev == null)
        {
          this._buckets[index] = entry.getNext();
        }
        else {
          prev.setNext(entry.getNext());
        }
        this._entries -= 1;
        return entry.getValue();
      }
      prev = entry;
      entry = entry.getNext();
    }
    return null;
  }
  public Set keySet() {
    Set set = new IdentitySet(this._capacity);
    for (int i = 0; i < this._capacity; i++) {
      Entry entry = this._buckets[i];
      while (entry != null) {
        set.add(entry.getKey());
        entry = entry.getNext();
      }
    }
    return set;
  }
  public Set entrySet() {
    Set set = new IdentitySet(this._capacity);
    for (int i = 0; i < this._capacity; i++) {
      Entry entry = this._buckets[i];
      while (entry != null) {
        set.add(entry);
        entry = entry.getNext();
      }
    }
    return set;
  }
  public Collection values()
  {
    throw new UnsupportedOperationException();
  }
  public boolean containsValue(Object value)
  {
    throw new UnsupportedOperationException();
  }
  public void putAll(Map map)
  {
    throw new UnsupportedOperationException();
  }
  public final class Entry implements Map.Entry {
    private Object _key;
    private int _hash;
    private Object _value;
    private Entry _next = null;
    public Entry(Object key, int hash, Object value) {
      this._key = key;
      this._hash = hash;
      this._value = value;
    }
    public Object getKey()
    {
      return this._key;
    }
    public int getHash()
    {
      return this._hash;
    }
    public Object setValue(Object value) {
      Object temp = this._value;
      this._value = value;
      return temp;
    }
    public Object getValue()
    {
      return this._value;
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
}

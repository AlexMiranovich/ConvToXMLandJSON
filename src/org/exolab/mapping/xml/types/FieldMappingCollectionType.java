package org.exolab.mapping.xml.types;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class FieldMappingCollectionType implements Serializable {
  public static final int ARRAY_TYPE = 0;
  public static final FieldMappingCollectionType ARRAY = new FieldMappingCollectionType(0, "array");
  public static final int VECTOR_TYPE = 1;
  public static final FieldMappingCollectionType VECTOR = new FieldMappingCollectionType(1, "vector");
  public static final int ARRAYLIST_TYPE = 2;
  public static final FieldMappingCollectionType ARRAYLIST = new FieldMappingCollectionType(2, "arraylist");
  public static final int HASHTABLE_TYPE = 3;
  public static final FieldMappingCollectionType HASHTABLE = new FieldMappingCollectionType(3, "hashtable");
  public static final int COLLECTION_TYPE = 4;
  public static final FieldMappingCollectionType COLLECTION = new FieldMappingCollectionType(4, "collection");
  public static final int SET_TYPE = 5;
  public static final FieldMappingCollectionType SET = new FieldMappingCollectionType(5, "set");
  public static final int MAP_TYPE = 6;
  public static final FieldMappingCollectionType MAP = new FieldMappingCollectionType(6, "map");
  public static final int ENUMERATE_TYPE = 7;
  public static final FieldMappingCollectionType ENUMERATE = new FieldMappingCollectionType(7, "enumerate");
  public static final int SORTEDSET_TYPE = 8;
  public static final FieldMappingCollectionType SORTEDSET = new FieldMappingCollectionType(8, "sortedset");
  public static final int ITERATOR_TYPE = 9;
  public static final FieldMappingCollectionType ITERATOR = new FieldMappingCollectionType(9, "iterator");
  public static final int SORTEDMAP_TYPE = 10;
  public static final FieldMappingCollectionType SORTEDMAP = new FieldMappingCollectionType(10, "sortedmap");
  private static Hashtable<Object, Object> _memberTable = init();
  private final int type;
  private String stringValue = null;
  private FieldMappingCollectionType(int type, String value) {
    this.type = type;
    this.stringValue = value;
  }
  public static Enumeration<? extends Object> enumerate()
  {
    return _memberTable.elements();
  }
  public int getType()
  {
    return this.type;
  }
  private static Hashtable<Object, Object> init() {
    Hashtable<Object, Object> members = new Hashtable();
    members.put("array", ARRAY);
    members.put("vector", VECTOR);
    members.put("arraylist", ARRAYLIST);
    members.put("hashtable", HASHTABLE);
    members.put("collection", COLLECTION);
    members.put("set", SET);
    members.put("map", MAP);
    members.put("enumerate", ENUMERATE);
    members.put("sortedset", SORTEDSET);
    members.put("iterator", ITERATOR);
    members.put("sortedmap", SORTEDMAP);
    return members;
  }
  private Object readResolve()
  {
    return valueOf(this.stringValue);
  }
  public String toString()
  {
    return this.stringValue;
  }
  public static FieldMappingCollectionType valueOf(String string) {
    Object obj = null;
    if (string != null) {
      obj = _memberTable.get(string);
    }
    if (obj == null) {
      String err = "" + string + " is not a valid FieldMappingCollectionType";
      throw new IllegalArgumentException(err);
    }
    return (FieldMappingCollectionType)obj;
  }
}

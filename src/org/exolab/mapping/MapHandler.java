package org.exolab.mapping;

import java.util.Enumeration;

public interface MapHandler {
  Object create();
    Object put(Object paramObject1, Object paramObject2, Object paramObject3)
    throws ClassCastException;
    Enumeration elements(Object paramObject)
    throws ClassCastException;
    Enumeration keys(Object paramObject)
    throws ClassCastException;
    int size(Object paramObject)
    throws ClassCastException;
    void clear(Object paramObject)
    throws ClassCastException;
    Object get(Object paramObject1, Object paramObject2)
    throws ClassCastException;
}

package org.exolab.mapping;

import java.util.Enumeration;

public interface CollectionHandler {
   Object add(Object paramObject1, Object paramObject2)
    throws ClassCastException;
    Enumeration elements(Object paramObject)
    throws ClassCastException;
    int size(Object paramObject)
    throws ClassCastException;
    Object clear(Object paramObject)
    throws ClassCastException;
}

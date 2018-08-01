package org.exolab.xml;

public interface MarshalListener {
  boolean preMarshal(Object paramObject);
  void postMarshal(Object paramObject);
}

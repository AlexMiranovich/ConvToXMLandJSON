package org.exolab.mapping;

public interface TypeConvertor {
  Object convert(Object paramObject) throws ClassCastException;
}

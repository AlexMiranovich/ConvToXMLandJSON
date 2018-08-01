package org.exolab.mapping;

public interface FieldHandler {
    Object getValue(Object paramObject)
    throws IllegalStateException;
    void setValue(Object paramObject1, Object paramObject2)
    throws IllegalStateException, IllegalArgumentException;
    void resetValue(Object paramObject)
    throws IllegalStateException, IllegalArgumentException;
    void checkValidity(Object paramObject)
    throws ValidityException, IllegalStateException;
    Object newInstance(Object paramObject)
    throws IllegalStateException;
}

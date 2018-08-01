package org.exolab.xml;

import org.exolab.mapping.AbstractFieldHandler;

public class XMLFieldHandler extends AbstractFieldHandler {
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof XMLFieldHandler)) {
      return false;
    }
    return getClass().isInstance(obj);
  }
  public Object getValue(Object object) throws IllegalStateException {
    return null;
  }
  public Object newInstance(Object parent) throws IllegalStateException {
    return null;
  }
  public Object newInstance(Object parent, Object[] args)
    throws IllegalStateException {
    return newInstance(parent);
  }
  public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {}
  public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {}
}

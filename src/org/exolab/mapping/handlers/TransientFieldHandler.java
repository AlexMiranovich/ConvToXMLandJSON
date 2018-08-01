package org.exolab.mapping.handlers;

import org.exolab.mapping.AbstractFieldHandler;

public class TransientFieldHandler extends AbstractFieldHandler {
  public Object getValue(Object target)
  {
    return null;
  }
  public Object newInstance(Object target)
  {
    return null;
  }
  public Object newInstance(Object target, Object[] args)
  {
    return null;
  }
  public void setValue(Object target, Object value) {}
  public void resetValue(Object target) {}
}

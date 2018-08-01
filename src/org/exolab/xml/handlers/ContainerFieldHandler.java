package org.exolab.xml.handlers;

import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.util.ContainerElement;

public final class ContainerFieldHandler implements FieldHandler {
  public static final int MODE_AUTO = 0;
  public static final int MODE_PARENT_LINK = 1;
  public static final int MODE_CHILD_LINK = 2;
  private final FieldHandler _handler;
  private final int _mode = 0;
  public ContainerFieldHandler(FieldHandler handler)
  {
    this._handler = handler;
  }
  public Object getValue(Object object)
    throws IllegalStateException {
    int mode = 0;
    if (mode == 0) {
      if ((object instanceof ContainerElement)) {
        mode = 2;
      } else {
        mode = 1;
      }
    }
    if (mode == 2) {
      return this._handler.getValue(((ContainerElement)object).getParent());
    }
    ContainerElement container = new ContainerElement(this._handler.getValue(object));
    container.setParent(object);
    return container;
  }
  public Object newInstance(Object parent) throws IllegalStateException {
    if ((parent instanceof ContainerElement)) {
      return this._handler.newInstance(((ContainerElement)parent).getParent());
    }
    ContainerElement container = new ContainerElement();
    container.setParent(parent);
    return container;
  }
  public void resetValue(Object object) throws IllegalStateException {
    this._handler.resetValue(object);
  }
  
  public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
    if ((object instanceof ContainerElement)) {
      this._handler.setValue(((ContainerElement)object).getParent(), value);
    }
  }
  public void checkValidity(Object object)
    throws ValidityException, IllegalStateException {}
}

package org.exolab.xml.util;

public class ContainerElement {
  private Object _parent = null;
  private Object _object = null;
  public ContainerElement() {}
  public ContainerElement(Object object)
  {
    this._object = object;
  }
  public Object getObject()
  {
    return this._object;
  }
  public Object getParent()
  {
    return this._parent;
  }
  public void setObject(Object object)
  {
    this._object = object;
  }
  public void setParent(Object parent)
  {
    this._parent = parent;
  }
}

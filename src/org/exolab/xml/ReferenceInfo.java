package org.exolab.xml;

import org.exolab.mapping.FieldDescriptor;

class ReferenceInfo {
  private final String id;
  private final Object target;
  private final XMLFieldDescriptor descriptor;
  private ReferenceInfo next = null;
  public ReferenceInfo(String id, Object target, XMLFieldDescriptor descriptor) {
    this.id = id;
    this.target = target;
    this.descriptor = descriptor;
  }
  public void setNext(ReferenceInfo info)
  {
    this.next = info;
  }
  public FieldDescriptor getDescriptor()
  {
    return this.descriptor;
  }
  public Object getTarget()
  {
    return this.target;
  }
  public ReferenceInfo getNext()
  {
    return this.next;
  }
  public String getId()
  {
    return this.id;
  }
}
